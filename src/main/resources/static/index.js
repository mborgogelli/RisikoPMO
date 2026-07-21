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
    const autoReadyRooms = new Set();

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
            const isReady = !!readyStates[playerName];
            const readyLabel = isReady ? 'Pronto' : 'Non pronto';
            const readyClass = isReady ? 'ready' : 'not-ready';
            const readyControl = `<span class="ready-status ${readyClass}">${readyLabel}</span>`;

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

        impostaProntoAutomatico(stanza.roomId, nomeCorrente, readyStates);

        const allReady = Object.values(readyStates).length > 0 && Object.values(readyStates).every(Boolean);
        const isFull = stanza.currentPlayers >= stanza.maxPlayers;
        console.log(`DEBUG: roomId=${stanza.roomId}, isFull=${isFull}, allReady=${allReady}, gameStarted=${gameStarted}, startRequestInFlight=${startRequestInFlight}`);
        console.log(`DEBUG: readyStates=${JSON.stringify(readyStates)}`);

        if (!gameStarted && !startRequestInFlight && isFull && allReady) {
            console.log('DEBUG: Condizioni met, avvio gioco...');
            startRequestInFlight = true;
            fetch(`/api/stanze/${stanza.roomId}/avvia-gioco`, { method: 'POST' })
                .then(async response => {
                    const payload = await response.json().catch(() => ({}));
                    console.log(`DEBUG: avvia-gioco response status=${response.status}, payload=${JSON.stringify(payload)}`);
                    return { ok: response.ok, payload };
                })
                .then(({ ok, payload }) => {
                    console.log(`DEBUG: ok=${ok}, gameStarted=${gameStarted}`);
                    if (!ok) {
                        const errorMessage = (payload && payload.error ? payload.error : '').toLowerCase();
                        const partitaGiaAvviata = errorMessage.includes('gia') || errorMessage.includes('già') || errorMessage.includes('already');
                        if (!partitaGiaAvviata) {
                            throw new Error(payload.error || 'Errore avvio gioco');
                        }
                    }

                    gameStarted = true;
                    console.log('DEBUG: Reindirizzamento a game.html');
                    vaiAllaPaginaPartita(stanza.roomId, nomeCorrente);
                })
                .catch(err => console.error('Errore avvio gioco:', err))
                .finally(() => {
                    if (!gameStarted) {
                        startRequestInFlight = false;
                    }
                });
        } else if (!stanza.isFull || !allReady) {
            startRequestInFlight = false;
        }
    }

    function impostaProntoAutomatico(roomId, playerName, readyStates) {
        if (!roomId || !playerName || autoReadyRooms.has(roomId)) {
            return;
        }

        if (readyStates[playerName] === true) {
            autoReadyRooms.add(roomId);
            return;
        }

        fetch(`/api/stanze/${roomId}/pronto`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ playerName: playerName, ready: true })
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.error || 'Errore auto-ready'); });
            }
            autoReadyRooms.add(roomId);
            return response.json();
        })
        .catch(err => console.error('Errore auto-ready:', err));
    }

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