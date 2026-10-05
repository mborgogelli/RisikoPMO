document.addEventListener('DOMContentLoaded', () => {
  const params = new URLSearchParams(window.location.search);
  const roomId = params.get('roomId') || '';
  const playerName = params.get('playerName') || '';

  const elements = {
    roomId: document.getElementById('roomIdLabel'),
    playerName: document.getElementById('playerNameLabel'),
    connection: document.getElementById('connectionStatus'),
    connectionLabel: document.getElementById('connectionLabel'),
    mapSummary: document.getElementById('mapSummary'),
    boardMap: document.getElementById('boardMap'),
    territories: document.getElementById('territoryGroups'),
    phaseName: document.getElementById('phaseName'),
    phaseInstruction: document.getElementById('phaseInstruction'),
    eventBadge: document.getElementById('eventBadge'),
    currentPlayerBanner: document.getElementById('currentPlayerBanner'),
    playersCount: document.getElementById('playersCount'),
    playersList: document.getElementById('playersList'),
    actionTitle: document.getElementById('actionTitle'),
    actionInstruction: document.getElementById('actionInstruction'),
    deploymentBudget: document.getElementById('deploymentBudget'),
    budgetFill: document.getElementById('budgetFill'),
    selectionList: document.getElementById('selectionList'),
    actionError: document.getElementById('actionError'),
    submitDeployment: document.getElementById('submitDeployment'),
    latestEvent: document.getElementById('latestEvent')
  };

  let currentState = null;
  let boardSvg = null;
  let boardTerritoryLayer = null;
  let boardLabels = null;
  let boardRenderSignature = '';
  let deploymentSelection = {};
  let actionSignature = '';
  let territoryRenderSignature = '';
  let isSubmitting = false;

  elements.roomId.textContent = roomId || '—';
  elements.playerName.textContent = playerName || '—';
  elements.territories.addEventListener('click', onTerritoryButtonClick);
  elements.submitDeployment.addEventListener('click', submitDeployment);

  if (!roomId) {
    setConnection(false, 'Manca il codice stanza');
    elements.phaseInstruction.textContent = 'Apri la partita dal link ricevuto nella lobby.';
  } else {
    loadBoard();
    pollGameState();
  }

  async function loadBoard() {
    try {
      const response = await fetch('/images/risikonew_board.svg');
      if (!response.ok) throw new Error(`Mappa non disponibile (${response.status})`);
      const svgText = await response.text();
      const parsed = new DOMParser().parseFromString(svgText, 'image/svg+xml');
      boardSvg = parsed.documentElement;
      if (parsed.querySelector('parsererror') || boardSvg.nodeName.toLowerCase() !== 'svg') {
        throw new Error('Il file della mappa non è leggibile.');
      }
      boardSvg.classList.add('board-svg');
      boardTerritoryLayer = boardSvg.getElementById('layer4');
      if (!boardTerritoryLayer) throw new Error('La mappa non contiene i territori.');
      boardLabels = document.createElementNS('http://www.w3.org/2000/svg', 'g');
      boardLabels.setAttribute('class', 'army-labels');
      boardTerritoryLayer.append(boardLabels);
      elements.boardMap.replaceChildren(document.importNode(boardSvg, true));
      boardSvg = elements.boardMap.firstElementChild;
      boardTerritoryLayer = boardSvg.getElementById('layer4');
      boardLabels = boardSvg.querySelector('.army-labels');
      renderBoard();
    } catch (error) {
      elements.mapSummary.textContent = 'Mappa non disponibile';
      elements.boardMap.replaceChildren(createMessage(error.message, 'error-copy'));
    }
  }

  async function pollGameState() {
    try {
      currentState = await requestJson(`/api/game/${encodeURIComponent(roomId)}/state`);
      setConnection(true, 'Partita online');
      updateActionSignature();
      renderGameState();
    } catch (error) {
      setConnection(false, error.message || 'Connessione interrotta');
      if (!currentState) {
        elements.phaseInstruction.textContent = 'Non riesco a leggere lo stato della partita. Riprovo tra poco.';
      }
    }
    window.setTimeout(pollGameState, 1200);
  }

  async function requestJson(url, options = {}) {
    const response = await fetch(url, options);
    const payload = await response.json().catch(() => null);
    if (!response.ok) {
      throw new Error(payload?.error || `Richiesta non riuscita (${response.status})`);
    }
    return payload;
  }

  function setConnection(connected, label) {
    elements.connection.classList.toggle('is-connected', connected);
    elements.connection.classList.toggle('is-disconnected', !connected);
    elements.connectionLabel.textContent = label;
  }

  function updateActionSignature() {
    const playerState = currentState.players?.[currentState.currentPlayerName];
    const available = getTokenCount(playerState?.availableTokens);
    const pending = currentState.pendingAction;
    const territories = [...(pending?.deployableTerritories || [])].sort().join(',');
    const signature = [currentState.currentPlayerName, currentState.phaseId, available, territories,
      tokenBudget(pending)].join('|');
    if (signature !== actionSignature) {
      deploymentSelection = {};
      actionSignature = signature;
    }
  }

  function renderGameState() {
    renderPhase();
    renderPlayers();
    renderDeployment();
    renderBoard();
    renderTerritories();
    renderLatestEvent();
  }

  function renderBoard() {
    if (!boardSvg || !currentState) return;
    const ownership = territoryOwners();
    const territories = [...ownership.entries()].sort(([a], [b]) => a.localeCompare(b));
    const signature = territories.map(([territory, owner]) =>
      `${territory}:${owner.name}:${owner.color}:${getTokenCount(owner.state.deployedTokensByTerritory?.[territory])}`
    ).join('|');
    const territoryPaths = boardTerritoryLayer.querySelectorAll(':scope > path[id]');
    territoryPaths.forEach(path => {
      const owner = ownership.get(path.id);
      path.style.fill = owner ? colorFor(owner.color) : 'none';
      path.style.fillOpacity = owner ? '0.78' : '1';
    });
    elements.mapSummary.textContent = `${territories.length} territori · ${Object.keys(currentState.players || {}).length} giocatori`;
    if (signature === boardRenderSignature) return;
    boardRenderSignature = signature;
    boardLabels.replaceChildren();
    territories.forEach(([territory, owner]) => {
      const path = boardSvg.getElementById(territory);
      if (!path) return;
      const bounds = path.getBBox();
      const label = document.createElementNS('http://www.w3.org/2000/svg', 'text');
      label.setAttribute('x', String(bounds.x + bounds.width / 2));
      label.setAttribute('y', String(bounds.y + bounds.height / 2));
      label.setAttribute('class', 'army-count');
      label.textContent = String(getTokenCount(owner.state.deployedTokensByTerritory?.[territory]));
      boardLabels.append(label);
    });
  }

  function renderPhase() {
    const phaseId = currentState.phaseId;
    const labels = {
      RISIKONEW_INITIAL_PLACEMENT: 'Piazzamento iniziale',
      RISIKONEW_REINFORCE: 'Rinforzi',
      RISIKONEW_ATTACK: 'Attacco',
      RISIKONEW_MOVEMENT: 'Spostamento',
      RISIKONEW_STRATEGIC: 'Fase strategica'
    };
    const name = labels[phaseId] || 'Partita in corso';
    const currentPlayer = currentState.currentPlayerName;
    const isMyTurn = currentPlayer && currentPlayer === playerName;
    const isDeployAction = currentState.pendingAction?.type === 'DEPLOY';

    elements.phaseName.textContent = name;
    elements.eventBadge.textContent = (currentState.eventType || 'IN ATTESA').replaceAll('_', ' ');
    elements.currentPlayerBanner.hidden = !currentPlayer;
    elements.currentPlayerBanner.replaceChildren();
    if (currentPlayer) {
      const player = document.createElement('strong');
      player.textContent = currentPlayer;
      const turnText = document.createElement('span');
      turnText.textContent = isMyTurn ? 'È il tuo turno' : 'Sta giocando';
      elements.currentPlayerBanner.append(player, turnText);
    }

    if (phaseId === 'RISIKONEW_INITIAL_PLACEMENT') {
      elements.phaseInstruction.textContent = isMyTurn
        ? 'Distribuisci i carri iniziali sui territori che possiedi.'
        : `Attendi che ${currentPlayer || 'il giocatore di turno'} completi il piazzamento iniziale.`;
    } else if (isDeployAction) {
      elements.phaseInstruction.textContent = isMyTurn
        ? 'Scegli i territori su cui distribuire i carri richiesti.'
        : `Attendi la mossa di ${currentPlayer || 'un altro giocatore'}.`;
    } else {
      elements.phaseInstruction.textContent = currentPlayer
        ? `Giocatore di turno: ${currentPlayer}.`
        : 'La partita sta aggiornando il proprio stato.';
    }
  }

  function renderPlayers() {
    const players = Object.entries(currentState.players || {});
    elements.playersCount.textContent = String(players.length);
    elements.playersList.replaceChildren();
    if (players.length === 0) {
      elements.playersList.append(createMessage('Nessun giocatore disponibile.'));
      return;
    }

    players.forEach(([name, state]) => {
      const card = document.createElement('article');
      card.className = 'player-card';
      if (name === currentState.currentPlayerName) card.classList.add('is-current');
      card.style.setProperty('--player-color', colorFor(state.color));

      const heading = document.createElement('div');
      heading.className = 'player-heading';
      const marker = document.createElement('span');
      marker.className = 'player-marker';
      const title = document.createElement('strong');
      title.textContent = name;
      const status = document.createElement('span');
      status.className = 'player-status';
      status.textContent = name === currentState.currentPlayerName ? 'Di turno' : statusLabel(state.turnStatus);
      heading.append(marker, title, status);

      const territories = Object.keys(state.deployedTokensByTerritory || {}).length;
      const troopsOnBoard = Object.values(state.deployedTokensByTerritory || {})
        .reduce((total, tokens) => total + getTokenCount(tokens), 0);
      const troopsAvailable = getTokenCount(state.availableTokens);
      const stats = document.createElement('div');
      stats.className = 'player-stats';
      stats.append(
        stat('Territori', territories),
        stat('Carri sulla mappa', troopsOnBoard),
        stat('Carri disponibili', troopsAvailable)
      );
      card.append(heading, stats);
      elements.playersList.append(card);
    });
  }

  function renderDeployment() {
    const pending = currentState.pendingAction;
    const isDeployAction = pending?.type === 'DEPLOY';
    const isMyTurn = currentState.currentPlayerName === playerName;
    const canAct = isDeployAction && isMyTurn;
    const budget = tokenBudget(pending);
    const selectedTotal = deploymentTotal();
    const phaseLabel = currentState.phaseId === 'RISIKONEW_INITIAL_PLACEMENT' ? 'Piazzamento iniziale' : 'Distribuzione dei rinforzi';

    elements.actionTitle.textContent = phaseLabel;
    elements.deploymentBudget.textContent = `${selectedTotal} / ${budget} carri`;
    elements.budgetFill.style.width = `${budget === 0 ? 0 : Math.min(100, (selectedTotal / budget) * 100)}%`;
    elements.submitDeployment.disabled = !canAct || selectedTotal === 0 || isSubmitting;
    elements.submitDeployment.textContent = isSubmitting ? 'Invio in corso…' : 'Conferma piazzamento';
    if (canAct) {
      elements.actionInstruction.textContent = `Scegli fino a ${budget} carri tra i tuoi territori. Puoi distribuirli su più territori.`;
    } else if (isDeployAction) {
      elements.actionInstruction.textContent = `Il piazzamento è in attesa di ${currentState.currentPlayerName}.`;
    } else {
      elements.actionInstruction.textContent = 'Quando sarà il tuo turno, le azioni disponibili appariranno qui.';
    }

    renderSelectionList(canAct);
  }

  function renderSelectionList(canAct) {
    elements.selectionList.replaceChildren();
    if (!canAct) deploymentSelection = {};
    const selected = Object.entries(deploymentSelection).filter(([, count]) => count > 0);
    if (selected.length === 0) {
      elements.selectionList.append(createMessage('Nessun carro selezionato.'));
      return;
    }

    selected.forEach(([territory, count]) => {
      const row = document.createElement('div');
      row.className = 'selection-row';
      const label = document.createElement('span');
      label.textContent = readableName(territory);
      const amount = document.createElement('strong');
      amount.textContent = `${count} ${count === 1 ? 'carro' : 'carri'}`;
      row.append(label, amount);
      elements.selectionList.append(row);
    });
  }

  function renderTerritories() {
    if (!currentState) return;
    const signature = getTerritoryRenderSignature();
    if (signature === territoryRenderSignature) return;
    territoryRenderSignature = signature;

    const canAct = currentState.currentPlayerName === playerName && currentState.pendingAction?.type === 'DEPLOY';
    const selectable = new Set(currentState.pendingAction?.deployableTerritories || []);
    const budget = tokenBudget(currentState.pendingAction);
    const selectedTotal = deploymentTotal();
    const ownership = territoryOwners();
    const territories = new Set([...ownership.keys(), ...selectable]);
    const cards = document.createDocumentFragment();
    [...territories].sort((a, b) => a.localeCompare(b)).forEach(territory => {
      const owner = ownership.get(territory);
      const card = document.createElement('article');
      card.className = 'territory-card';
      if (owner) card.classList.add('is-owned');
      if (selectable.has(territory) && canAct) card.classList.add('is-selectable');
      if (deploymentSelection[territory]) card.classList.add('is-selected');
      if (owner) card.style.setProperty('--owner-color', colorFor(owner.color));

      const top = document.createElement('div');
      top.className = 'territory-topline';
      const name = document.createElement('strong');
      name.textContent = readableName(territory);
      const troops = owner ? getTokenCount(owner.state.deployedTokensByTerritory?.[territory]) : 0;
      const troopsLabel = document.createElement('span');
      troopsLabel.textContent = `${troops} ${troops === 1 ? 'carro' : 'carri'}`;
      top.append(name, troopsLabel);

      const ownerLabel = document.createElement('span');
      ownerLabel.className = 'territory-owner';
      ownerLabel.textContent = owner ? owner.name : 'Libero';

      const controls = document.createElement('div');
      controls.className = 'territory-controls';
      const chosen = deploymentSelection[territory] || 0;
      const canSelectThis = canAct && !isSubmitting && selectable.has(territory);
      controls.append(
        adjustmentButton('−', 'decrease', territory, !canSelectThis || chosen === 0),
        amountBadge(chosen),
        adjustmentButton('+', 'increase', territory, !canSelectThis || selectedTotal >= budget)
      );
      card.append(top, ownerLabel, controls);
      cards.append(card);
    });
    elements.territories.replaceChildren(cards);
  }

  function onTerritoryButtonClick(event) {
    const button = event.target.closest('button[data-adjustment]');
    if (!button || button.disabled || isSubmitting) return;
    const territory = button.dataset.territory;
    const amount = deploymentSelection[territory] || 0;
    if (button.dataset.adjustment === 'increase') {
      deploymentSelection[territory] = amount + 1;
    } else if (amount <= 1) {
      delete deploymentSelection[territory];
    } else {
      deploymentSelection[territory] = amount - 1;
    }
    elements.actionError.hidden = true;
    renderDeployment();
    renderTerritories();
  }

  async function submitDeployment() {
    if (isSubmitting || deploymentTotal() === 0) return;
    isSubmitting = true;
    renderDeployment();
    renderTerritories();
    try {
      const updatedState = await requestJson(`/api/game/${encodeURIComponent(roomId)}/deploy`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ playerName, tokenType: deploymentTokenType(), deployment: deploymentSelection })
      });
      deploymentSelection = {};
      currentState = updatedState;
      updateActionSignature();
      elements.actionError.hidden = true;
      renderGameState();
      setConnection(true, 'Piazzamento registrato');
    } catch (error) {
      elements.actionError.textContent = error.message;
      elements.actionError.hidden = false;
    } finally {
      isSubmitting = false;
      renderDeployment();
      renderTerritories();
    }
  }

  function deploymentTokenType() {
    const tokenNames = Object.keys(currentState.pendingAction?.tokensToDeploy || {});
    return tokenNames.length === 1 ? tokenNames[0] : null;
  }

  function renderLatestEvent() {
    const phase = currentState.phaseId ? readableName(currentState.phaseId.replace('RISIKONEW_', '')) : 'Partita';
    const player = currentState.currentPlayerName || 'Nessun giocatore';
    elements.latestEvent.textContent = `${currentState.eventType || 'AGGIORNAMENTO'} · ${phase} · ${player}`;
  }

  function territoryOwners() {
    const owners = new Map();
    Object.entries(currentState.players || {}).forEach(([name, state]) => {
      Object.keys(state.deployedTokensByTerritory || {}).forEach(territory => {
        owners.set(territory, { name, color: state.color, state });
      });
    });
    return owners;
  }

  function tokenBudget(pendingAction) {
    return Object.values(pendingAction?.tokensToDeploy || {}).reduce((total, count) => total + Number(count || 0), 0);
  }

  function deploymentTotal() {
    return Object.values(deploymentSelection).reduce((total, count) => total + count, 0);
  }

  function getTerritoryRenderSignature() {
    const players = Object.entries(currentState.players || {}).sort(([a], [b]) => a.localeCompare(b));
    const playerStates = players.map(([name, state]) => {
      const territories = Object.entries(state.deployedTokensByTerritory || {})
        .sort(([a], [b]) => a.localeCompare(b))
        .map(([territory, tokens]) => `${territory}=${tokenSignature(tokens)}`)
        .join(',');
      return `${name}:${state.color}:${tokenSignature(state.availableTokens)}:${territories}`;
    }).join(';');
    const pending = currentState.pendingAction;
    const selectable = [...(pending?.deployableTerritories || [])].sort().join(',');
    const pendingTokens = tokenSignature(pending?.tokensToDeploy);
    const actionType = pending?.type || '';
    const selected = Object.entries(deploymentSelection).sort(([a], [b]) => a.localeCompare(b))
      .map(([territory, amount]) => `${territory}=${amount}`).join(',');
    return [currentState.eventType, currentState.currentPlayerName, currentState.phaseId, actionType, isSubmitting,
      playerStates, selectable, pendingTokens, selected].join('|');
  }

  function tokenSignature(tokens) {
    return Object.entries(tokens || {}).sort(([a], [b]) => a.localeCompare(b))
      .map(([name, amount]) => `${name}:${amount}`).join(',');
  }

  function getTokenCount(tokens) {
    if (!tokens) return 0;
    return Number(tokens.Tank ?? tokens.TANK ?? tokens.tank ?? Object.values(tokens)[0] ?? 0);
  }

  function adjustmentButton(label, adjustment, territory, disabled) {
    const button = document.createElement('button');
    button.type = 'button';
    button.className = `adjustment-button adjustment-${adjustment}`;
    button.dataset.adjustment = adjustment;
    button.dataset.territory = territory;
    button.disabled = disabled;
    button.setAttribute('aria-label', `${adjustment === 'increase' ? 'Aggiungi un carro a' : 'Rimuovi un carro da'} ${readableName(territory)}`);
    button.textContent = label;
    return button;
  }

  function amountBadge(amount) {
    const badge = document.createElement('span');
    badge.className = 'selected-count';
    badge.textContent = String(amount);
    return badge;
  }

  function stat(label, value) {
    const item = document.createElement('div');
    item.className = 'player-stat';
    const name = document.createElement('span');
    name.textContent = label;
    const amount = document.createElement('strong');
    amount.textContent = String(value);
    item.append(name, amount);
    return item;
  }

  function createMessage(message, className = 'empty-state') {
    const paragraph = document.createElement('p');
    paragraph.className = className;
    paragraph.textContent = message;
    return paragraph;
  }

  function readableName(value) {
    return String(value || '').replaceAll('_', ' ').replace(/\b\w/g, letter => letter.toUpperCase());
  }

  function statusLabel(status) {
    const labels = { ACTIVE: 'Attivo', SKIP_TURN: 'Salta il turno', ELIMINATED: 'Eliminato' };
    return labels[status] || status || 'Attivo';
  }

  function colorFor(color) {
    const palette = {
      BLACK: '#8b949e', BLUE: '#5ba8ff', GREEN: '#62d49a', PURPLE: '#b48cff', RED: '#ff7272', YELLOW: '#f5c451'
    };
    return palette[color] || '#94a3b8';
  }
});
