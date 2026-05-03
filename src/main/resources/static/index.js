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

    // --- CARICAMENTO INIZIALE STANZE ---
    caricaStanze();

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

    // Funzione che aggiorna la grafica
    function mostraLobby(stanza) {
        // Nascondi login, mostra lobby
        sezioneLogin.style.display = 'none';
        sezioneListaStanze.style.display = 'none';
        sezioneLobby.style.display = 'block';

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
        const nomeCorrente = inputNome.value;

        Object.entries(players).forEach(([playerName, color]) => {
            const isReady = !!readyStates[playerName];
            const isSelf = playerName === nomeCorrente;
            const readyLabel = isReady ? 'Pronto' : 'Non pronto';
            const readyClass = isReady ? 'ready' : 'not-ready';
            const readyControl = isSelf
                ? `<button class="ready-btn ${readyClass}" data-player="${playerName}" data-ready="${isReady}">${readyLabel}</button>`
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

        const allReady = Object.values(readyStates).length > 0 && Object.values(readyStates).every(Boolean);
        if (!gameStarted && stanza.isFull && allReady) {
            fetch(`/api/stanza/${stanza.roomId}/avvia-gioco`, { method: 'POST' })
                .then(response => response.json())
                .then(result => {
                    gameStarted = true;
                    console.log(result.message || 'Gioco avviato');
                })
                .catch(err => console.error('Errore avvio gioco:', err));
        }
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

        fetch(`/api/stanza/${roomId}/entra`, {
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
        fetch('/api/crea-stanza', {
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

    playersContainer.addEventListener('click', function(event) {
        const target = event.target;
        if (!target.classList.contains('ready-btn')) {
            return;
        }

        if (!currentRoomId) {
            return;
        }

        const playerName = target.getAttribute('data-player');
        const currentReady = target.getAttribute('data-ready') === 'true';
        const nextReady = !currentReady;

        fetch(`/api/stanza/${currentRoomId}/pronto`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ playerName: playerName, ready: nextReady })
        })
        .then(response => {
            if (response.ok) return response.json();
            return response.json().then(err => { throw new Error(err.error || 'Errore stato pronto'); });
        })
        .then(stanzaModel => {
            mostraLobby(stanzaModel);
        })
        .catch(err => {
            console.error(err);
            alert(err.message || "Errore nel contattare il server.");
        });
    });

});