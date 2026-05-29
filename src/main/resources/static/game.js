document.addEventListener('DOMContentLoaded', function() {
  const params = new URLSearchParams(window.location.search);
  const roomId = params.get('roomId') || '-';
  const playerName = params.get('playerName') || '-';

  const roomIdLabel = document.getElementById('roomIdLabel');
  const playerNameLabel = document.getElementById('playerNameLabel');
  const eventsList = document.getElementById('eventsList');

  roomIdLabel.textContent = roomId;
  playerNameLabel.textContent = playerName;

  // Placeholder eventi: in seguito si popoleranno dal backend/engine di gioco
  if (eventsList) {
    eventsList.innerHTML = '';
    appendEvent(eventsList, 'Partita avviata. Caricamento mappa completato.');
    appendEvent(eventsList, 'In attesa della fase iniziale e del primo turno.');
  }

  function appendEvent(container, text) {
    const row = document.createElement('p');
    row.textContent = text;
    container.appendChild(row);
  }
});

