document.addEventListener('DOMContentLoaded', function() {

    // Riferimenti agli elementi HTML
    const btnCrea = document.getElementById('createBtn');
    const inputNome = document.getElementById('playerName');
	const gameVersion = document.getElementById('mapSelect');
	const maxPlayers = document.getElementById('maxPlayer');

    // Sezioni da mostrare/nascondere
    const sezioneLogin = document.querySelector('aside.left');
    const sezioneListaStanze = document.querySelector('section.right[aria-label="Stanze disponibili"]');
    const sezioneLobby = document.getElementById('lobbySection');
    const roomsContainer = document.getElementById('roomsContainer');
    const emptyRooms = document.getElementById('emptyRooms');

    // Elementi dentro la Lobby da aggiornare
    const lobbyTitle = document.getElementById('roomName');
    const playersContainer = document.getElementById('playersList');
    const playerCount = document.getElementById('playerCount');
    let currentRoomId = null;
    let gameStarted = false;
    let startRequestInFlight = false;
    const ROOM_REFRESH_INTERVAL_MS = 1500;
    const ROOM_LIST_REFRESH_INTERVAL_MS = 1500;
    const statoRefreshStanza = { timer: null };
    const statoRefreshLobby = { timer: null };
    let readyRequestInFlight = false;
    let pendingReadyValue = null;

    // --- CARICAMENTO INIZIALE STANZE ---
    caricaStanze();
    avviaRefreshPeriodico(statoRefreshLobby, () => sezioneListaStanze && sezioneListaStanze.style.display !== 'none', caricaStanze, ROOM_LIST_REFRESH_INTERVAL_MS);

    function caricaStanze() {
        fetch('/api/stanze')
            .then(response => response.json())
            .then(stanze => {
                if (stanze.length === 0) {
                    emptyRooms.style.display = 'block';
                    roomsContainer.style.display = 'none';
                } else {
                    emptyRooms.style.display = 'none';
                    roomsContainer.style.display = 'block';
                    roomsContainer.innerHTML = ''; // Pulisci eventuali card esistenti

                    stanze.forEach(stanza => {
                        const roomCard = `
                            <div class="room-card">
                                <h3>Stanza ${stanza.roomId}</h3>
                                <p>Giocatori: ${stanza.currentPlayers}/${stanza.maxPlayers}</p>
                                <p>Versione: ${stanza.gameVersion}</p>
                                ${!stanza.isFull ? `<button class="primary join-btn" data-room="${stanza.roomId}">Entra</button>` : `<span style="color:red">Piena</span>`}
                            </div>
                        `;
                        roomsContainer.insertAdjacentHTML('beforeend', roomCard);
                    });
                }
            })
            .catch(err => console.error("Errore nel caricamento stanze:", err));
    }

    function avviaRefreshPeriodico(stato, condizioneAttiva, callback, intervalloMs) {
        if (stato.timer) {
            return;
        }

        stato.timer = setInterval(() => {
            if (condizioneAttiva()) {
                callback();
            }
        }, intervalloMs);
    }

    function fermaRefreshPeriodico(stato) {
        if (!stato.timer) {
            return;
        }

        clearInterval(stato.timer);
        stato.timer = null;
    }

    function avviaRefreshStanza() {
        if (!currentRoomId) return;

        avviaRefreshPeriodico(
            statoRefreshStanza,
            () => !!currentRoomId && sezioneLobby.style.display !== 'none',
            aggiornaStanzaCorrente,
            ROOM_REFRESH_INTERVAL_MS
        );
    }

    function fermaRefreshStanza() {
        fermaRefreshPeriodico(statoRefreshStanza);
    }

    function aggiornaStanzaCorrente() {
        if (!currentRoomId || sezioneLobby.style.display === 'none') {
            return;
        }

        fetch(`/api/stanze/${currentRoomId}`)
            .then(response => {
                if (response.status === 404) {
                    // La stanza viene chiusa quando il gioco parte: porta tutti alla pagina partita.
                    const nomeCorrente = inputNome.value || '';
                    gameStarted = true;
                    fermaRefreshStanza();
                    vaiAllaPaginaPartita(currentRoomId, nomeCorrente);
                    return null;
                }

                if (!response.ok) {
                    throw new Error('Errore aggiornamento stanza');
                }

                return response.json();
            })
            .then(stanza => {
                if (stanza) {
                    renderLobby(stanza);
                }
            })
            .catch(err => console.error('Errore nel refresh della stanza:', err));
    }

    // Funzione che disegna la lobby
    function renderLobby(stanza) {
        currentRoomId = stanza.roomId;

        // Aggiorna titolo stanza
        lobbyTitle.innerText = stanza.roomId;
        if (playerCount) {
            playerCount.innerText = `${stanza.currentPlayers}/${stanza.maxPlayers}`;
        }

        // Pulisci lista giocatori vecchia
        playersContainer.innerHTML = '';

        // Elenco dei giocatori ricevuti dal Controller Java
        const players = stanza.players || {};
        const readyStates = stanza.readyStates || {};
        const nomeCorrente = inputNome.value || '';

        Object.entries(players).forEach(([playerName, color]) => {
            const isCurrentPlayer = playerName === nomeCorrente;
            const isReady = isCurrentPlayer && pendingReadyValue !== null
                ? pendingReadyValue : readyStates[playerName] === true;
            const readyLabel = isReady ? 'Pronto' : 'Non pronto';
            const readyClass = isReady ? 'ready' : 'not-ready';
            const readyControl = isCurrentPlayer
                ? `<label class="ready-toggle"><input class="ready-checkbox" type="checkbox" ${isReady ? 'checked' : ''} ${readyRequestInFlight ? 'disabled' : ''}> Sono pronto</label>`
                : `<span class="ready-status ${readyClass}">${readyLabel}</span>`;

            // Crea HTML per ogni giocatore
            const htmlGiocatore = `
                <div class="player-card">
                    <div class="avatar" style="background-color: ${mappaColori(color)}">
                        ${playerName.charAt(0).toUpperCase()}
                    </div>
                    <div class="player-meta">
                        <div class="player-name">${playerName}</div>
                        <div class="player-sub">${color}</div>
                    </div>
                    <div class="player-ready">
                        ${readyControl}
                    </div>
                </div>
            `;
            playersContainer.insertAdjacentHTML('beforeend', htmlGiocatore);
        });

        const playerNames = Object.keys(players);
        const allReady = playerNames.length === stanza.currentPlayers
            && playerNames.every(playerName => readyStates[playerName] === true);
        const enoughPlayers = stanza.currentPlayers >= 3 && stanza.currentPlayers <= 6;

        if (!gameStarted && !startRequestInFlight && enoughPlayers && allReady) {
            startRequestInFlight = true;
            fetch(`/api/stanze/${stanza.roomId}/avvia-gioco`, { method: 'POST' })
                .then(async response => {
                    const payload = await response.json().catch(() => ({}));
                    if (!response.ok) {
                        throw new Error(payload.error || 'Errore avvio gioco');
                    }
                    gameStarted = true;
                    vaiAllaPaginaPartita(stanza.roomId, nomeCorrente);
                })
                .catch(err => console.error('Errore avvio gioco:', err))
                .finally(() => {
                    startRequestInFlight = false;
                });
        }
    }

    playersContainer.addEventListener('change', function(event) {
        const checkbox = event.target;
        if (checkbox.classList.contains('ready-checkbox') && currentRoomId && !readyRequestInFlight) {
            const ready = checkbox.checked;
            readyRequestInFlight = true;
            pendingReadyValue = ready;
            checkbox.disabled = true;
            fetch(`/api/stanze/${currentRoomId}/pronto`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ playerName: inputNome.value, ready })
            })
                .then(async response => {
                    const payload = await response.json().catch(() => ({}));
                    if (!response.ok) {
                        throw new Error(payload.error || 'Errore aggiornamento stato pronto');
                    }
                    return payload;
                })
                .then(renderLobby)
                .catch(err => {
                    console.error('Errore stato pronto:', err);
                    checkbox.checked = !ready;
                })
                .finally(() => {
                    readyRequestInFlight = false;
                    pendingReadyValue = null;
                    aggiornaStanzaCorrente();
                });
        }
    });

    function vaiAllaPaginaPartita(roomId, playerName) {
        const params = new URLSearchParams({
            roomId: roomId || '',
            playerName: playerName || ''
        });
        window.location.href = `/game.html?${params.toString()}`;
    }

    // Funzione che aggiorna la grafica
    function mostraLobby(stanza) {
        // Nascondi login, mostra lobby
        sezioneLogin.style.display = 'none';
        sezioneListaStanze.style.display = 'none';
        sezioneLobby.style.display = 'block';
        fermaRefreshPeriodico(statoRefreshLobby);
        renderLobby(stanza);
        avviaRefreshStanza();
    }

    // Funzione estetica per convertire le stringhe del model in colori CSS
    function mappaColori(nomeColore) {
        // Adatta queste stringhe a come le hai chiamate nel Model Java (es. "ROSSO", "RED", ecc)
        if (!nomeColore) return '#666';
        const c = nomeColore.toString().toUpperCase();
        if (c.includes('ROSSO') || c.includes('RED')) return '#ef4444';
        if (c.includes('BLU') || c.includes('BLUE')) return '#3b82f6';
        if (c.includes('VERDE') || c.includes('GREEN')) return '#22c55e';
        if (c.includes('GIALLO') || c.includes('YELLOW')) return '#eab308';
        if (c.includes('NERO') || c.includes('BLACK')) return '#111';
        if (c.includes('VIOLA') || c.includes('PURPLE')) return '#9333ea';
        return '#666'; // Default
    }

    roomsContainer.addEventListener('click', function(event) {
        const target = event.target;
        if (!target.classList.contains('join-btn')) {
            return;
        }

        const nome = inputNome.value;
        const roomId = target.getAttribute('data-room');

        if (!nome) {
            alert("Devi inserire un nome!");
            return;
        }

        fetch(`/api/stanze/${roomId}/entra`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ playerName: nome })
        })
        .then(response => {
            if (response.ok) return response.json();
            return response.json().then(err => { throw new Error(err.error || 'Errore ingresso stanza'); });
        })
        .then(stanzaModel => {
            mostraLobby(stanzaModel);
        })
        .catch(err => {
            console.error(err);
            alert(err.message || "Errore nel contattare il server.");
        });
    });

    // --- EVENTO CLICK ---
    btnCrea.addEventListener('click', function(event) {
        event.preventDefault();

        const nome = inputNome.value;
		const version = gameVersion.value;
		const players = maxPlayers.value;

        if (!nome) {
            alert("Devi inserire un nome!");
            return;
        }

		if (version !== "Classica") {
			alert("Versione non ancora implementata!");
			return;
		}

        // 1. CHIAMATA AL SERVER (Fetch)
        fetch('/api/stanze/crea-stanza', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ playerName: nome,
								   gameVersion: version,
							   	   maxPlayers: players})
        })
        .then(response => {
            if (response.ok) return response.json(); // Se ok, leggi il JSON
            throw new Error("Errore creazione");
        })
        .then(stanzaModel => {
            // Qui 'stanzaModel' è l'oggetto Java convertito in JS
            console.log("Dati ricevuti dal Model:", stanzaModel);

            mostraLobby(stanzaModel);
        })
        .catch(err => {
            console.error(err);
            alert("Errore nel contattare il server.");
        });
    });


});