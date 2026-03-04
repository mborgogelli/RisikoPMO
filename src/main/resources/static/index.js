document.addEventListener('DOMContentLoaded', function() {
    
    // Riferimenti agli elementi HTML
    const btnCrea = document.getElementById('createBtn');
	const btnEntra = document.getElementById('joinBtn');
    const inputNome = document.getElementById('playerName');
	const gameVersion = document.getElementById('mapSelect');
	const maxPlayers = document.getElementById('maxPlayer');
    
    // Sezioni da mostrare/nascondere
    const sezioneLogin = document.querySelector('aside.left');
    const sezioneListaStanze = document.querySelector('section.right[aria-label="Stanze disponibili"]');
    const sezioneLobby = document.getElementById('lobbySection');
    
    // Elementi dentro la Lobby da aggiornare
    const lobbyTitle = document.getElementById('roomName');
    const playersContainer = document.getElementById('playersList');

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

    // Funzione che aggiorna la grafica
    function mostraLobby(stanza) {
        // Nascondi login, mostra lobby
        sezioneLogin.style.display = 'none';
        sezioneListaStanze.style.display = 'none';
        sezioneLobby.style.display = 'block';

        // Aggiorna titolo stanza
        lobbyTitle.innerText = stanza.roomId;

        // Pulisci lista giocatori vecchia
        playersContainer.innerHTML = '';

        // Elenco dei giocatori ricevuti dal Controller Java
        const players = stanza.players || {}; 
        
        Object.entries(players).forEach(([playerName, color]) => {
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
                </div>
            `;
            playersContainer.insertAdjacentHTML('beforeend', htmlGiocatore);
        });
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
});