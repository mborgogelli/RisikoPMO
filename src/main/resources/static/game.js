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
    submitAction: document.getElementById('submitAction'),
    passAction: document.getElementById('passAction'),
    latestEvent: document.getElementById('latestEvent')
  };

  // Coordinate nel sistema locale del layer dei territori dell'SVG RisikoNew.
  const territoryLabelPositions = {
    australia_orientale: [866, 548], indonesia: [779, 483], nuova_guinea: [844, 466],
    alaska: [226, 208], ontario: [340, 254], territori_del_nord_ovest: [305, 190],
    venezuela: [361, 404], madagascar: [644, 554], africa_settentrionale: [525, 428],
    groenlandia: [425, 185], islanda: [490, 224], gran_bretagna: [476, 275],
    scandinavia: [549, 223], giappone: [844, 295], jacuzia: [777, 190],
    kamchatka: [832, 229], siberia: [717, 220], urali: [689, 233],
    afghanistan: [669, 308], medio_oriente: [626, 394], india: [712, 393],
    siam: [771, 408], cina: [755, 335], mongolia: [780, 290], cita: [770, 245],
    ucraina: [614, 267], europa_meridionale: [549, 337], europa_occidentale: [493, 341],
    europa_settentrionale: [543, 287], egitto: [574, 407], africa_orientale: [612, 481],
    congo: [572, 492], africa_meridionale: [584, 553], brasile: [390, 462],
    argentina: [370, 538], stati_uniti_orientali: [348, 305],
    stati_uniti_occidentali: [292, 298], quebec: [388, 249],
    america_centrale: [294, 360], peru: [354, 458],
    australia_occidentale: [823, 553], alberta: [285, 240]
  };

  let currentState = null;
  let stateRevision = 0;
  let boardSvg = null;
  let boardTerritoryLayer = null;
  let boardLabels = null;
  let boardRenderSignature = '';
  let deploymentSelection = {};
  let combatSelection = { target: '', source: '', tokenAmount: 1 };
  let movementSelection = { source: '', destination: '', tokenAmount: 1 };
  let actionSignature = '';
  let actionControlsSignature = '';
  let territoryRenderSignature = '';
  let isSubmitting = false;

  elements.roomId.textContent = roomId || '—';
  elements.playerName.textContent = playerName || '—';
  elements.territories.addEventListener('click', onTerritoryButtonClick);
  elements.submitAction.addEventListener('click', submitAction);
  elements.passAction.addEventListener('click', submitPassAction);
  elements.selectionList.addEventListener('change', onActionControlChange);

  if (!roomId) {
    setConnection(false, 'Manca il codice stanza');
    elements.phaseInstruction.textContent = 'Apri la partita dal link ricevuto nella lobby.';
  } else {
    loadBoard();
    pollGameState();
  }

  async function loadBoard() {
    try {
      const response = await fetch('/images/risikonew_board.svg?v=20261028-3', { cache: 'no-store' });
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
    const requestedRevision = stateRevision;
    try {
      const latestState = await requestJson(`/api/game/${encodeURIComponent(roomId)}/state`, { cache: 'no-store' });
      if (requestedRevision === stateRevision) {
        currentState = latestState;
        setConnection(true, 'Partita online');
        updateActionSignature();
        renderGameState();
      }
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
    const playerState = currentState.players?.[viewerPlayerName()];
    const available = getTokenCount(playerState?.availableTokens);
    const pending = currentState.pendingAction;
    const territories = [...(pending?.deployableTerritories || [])].sort().join(',');
    const signature = [currentState.currentPlayerName, currentState.phaseId, available, territories,
      tokenBudget(pending), pending?.type, pending?.defenderName, pending?.maxDefenderTokens,
      (pending?.possibleTargets || []).join(','), JSON.stringify(pending?.possibleStartingZones || {}),
      JSON.stringify(pending?.possibleMoves || {}), JSON.stringify(pending?.moveableTokens || {}),
      currentState.eventType].join('|');
    if (signature !== actionSignature) {
      deploymentSelection = {};
      combatSelection = { target: '', source: '', tokenAmount: 1 };
      movementSelection = { source: '', destination: '', tokenAmount: 1 };
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
    boardTerritoryLayer.style.opacity = '1';
    const signature = territories.map(([territory, owner]) =>
      `${territory}:${owner.name}:${owner.color}:${getTokenCount(owner.state.deployedTokensByTerritory?.[territory])}`
    ).join('|') + '|territory-names';
    const territoryPaths = boardTerritoryLayer.querySelectorAll(':scope > path[id]');
    territoryPaths.forEach(path => {
      const owner = ownership.get(path.id);
      path.style.fill = owner ? colorFor(owner.color) : '#334238';
      path.style.fillOpacity = '1';
    });
    elements.mapSummary.textContent = `${territories.length} territori · ${Object.keys(currentState.players || {}).length} giocatori`;
    if (signature === boardRenderSignature) return;
    boardRenderSignature = signature;
    boardLabels.replaceChildren();
    territoryPaths.forEach(path => {
      const territory = path.id;
      const owner = ownership.get(territory);
      const bounds = path.getBBox();
      const [centerX, centerY] = territoryLabelPositions[territory]
        || [bounds.x + bounds.width / 2, bounds.y + bounds.height / 2];
      const marker = document.createElementNS('http://www.w3.org/2000/svg', 'g');
      marker.setAttribute('class', 'army-marker');
      marker.setAttribute('transform', `translate(${centerX} ${centerY})`);
      if (owner) {
        const badge = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
        badge.setAttribute('class', 'army-badge');
        badge.setAttribute('r', '10');
        badge.style.setProperty('--army-owner-color', colorFor(owner.color));
        const count = document.createElementNS('http://www.w3.org/2000/svg', 'text');
        count.setAttribute('class', 'army-count');
        count.textContent = String(getTokenCount(owner.state.deployedTokensByTerritory?.[territory]));
        marker.append(badge, count);
      }
      const name = readableName(territory);
      const words = name.split(' ');
      const lines = name.length <= 12 || words.length === 1
        ? [name] : [words.slice(0, Math.ceil(words.length / 2)).join(' '), words.slice(Math.ceil(words.length / 2)).join(' ')];
      const nameLabel = document.createElementNS('http://www.w3.org/2000/svg', 'text');
      nameLabel.setAttribute('class', 'territory-name');
      nameLabel.setAttribute('y', owner ? '21' : '3');
      lines.forEach((line, index) => {
        const span = document.createElementNS('http://www.w3.org/2000/svg', 'tspan');
        span.setAttribute('x', '0');
        if (index > 0) span.setAttribute('dy', '8');
        span.textContent = line;
        nameLabel.append(span);
      });
      marker.append(nameLabel);
      boardLabels.append(marker);
    });
  }

  function renderPhase() {
    const phaseId = currentState.phaseId;
    const labels = {
      RISIKONEW_INITIAL_PLACEMENT: 'Piazzamento iniziale',
      RISIKONEW_REINFORCE: 'Rinforzi',
      RISIKONEW_ATTACK: 'Attacco',
      RISIKONEW_MOVEMENT: 'Spostamento',
      RISIKONEW_STRATEGIC: 'Spostamento'
    };
    const name = labels[phaseId] || 'Partita in corso';
    const currentPlayer = currentState.currentPlayerName;
    const isMyTurn = currentPlayer && isViewer(currentPlayer);
    const isDeployAction = currentState.pendingAction?.type === 'DEPLOY';
    const pending = currentState.pendingAction;

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
    } else if (pending?.type === 'DEFEND') {
      elements.phaseInstruction.textContent = isViewer(pending.defenderName)
        ? `Scegli quante armate usare per difendere il territorio attaccato.`
        : `Attendi la difesa di ${pending.defenderName || 'un altro giocatore'}.`;
    } else if (pending?.type === 'ATTACK') {
      elements.phaseInstruction.textContent = isMyTurn
        ? 'Scegli un territorio bersaglio e da dove attaccare.'
        : `Attendi la mossa di ${currentPlayer || 'un altro giocatore'}.`;
    } else if (pending?.type === 'MOVE') {
      elements.phaseInstruction.textContent = isMyTurn
        ? 'Scegli da quale territorio spostare le armate e dove trasferirle.'
        : `Attendi lo spostamento di ${currentPlayer || 'un altro giocatore'}.`;
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
    const isMyTurn = isViewer(currentState.currentPlayerName);
    const isAttackAction = pending?.type === 'ATTACK';
    const isDefendAction = pending?.type === 'DEFEND';
    const isMovementAction = pending?.type === 'MOVE';
    const canAttack = isAttackAction && isMyTurn;
    const canDefend = isDefendAction && isViewer(pending.defenderName);
    const canAct = isDeployAction && isMyTurn;
    const budget = tokenBudget(pending);
    const selectedTotal = deploymentTotal();
    const phaseLabel = currentState.phaseId === 'RISIKONEW_INITIAL_PLACEMENT' ? 'Piazzamento iniziale' : 'Distribuzione dei rinforzi';

    if (isAttackAction) {
      renderAttackAction(pending, canAttack);
    } else if (isDefendAction) {
      renderDefendAction(pending, canDefend);
    } else if (isMovementAction) {
      renderMovementAction(pending, isMyTurn);
    } else {
      renderDeployAction(pending, canAct, phaseLabel, budget, selectedTotal);
    }
  }

  function renderDeployAction(pending, canAct, phaseLabel, budget, selectedTotal) {
    const isInitialPlacement = currentState.phaseId === 'RISIKONEW_INITIAL_PLACEMENT';

    elements.passAction.hidden = true;
    elements.passAction.disabled = true;
    elements.actionTitle.textContent = phaseLabel;
    elements.deploymentBudget.textContent = `${selectedTotal} / ${budget} carri`;
    elements.budgetFill.style.width = `${budget === 0 ? 0 : Math.min(100, (selectedTotal / budget) * 100)}%`;
    elements.submitAction.disabled = !canAct || selectedTotal === 0
      || (isInitialPlacement && selectedTotal !== budget) || isSubmitting;
    elements.submitAction.textContent = isSubmitting ? 'Invio in corso…' : 'Conferma piazzamento';
    if (canAct) {
      elements.actionInstruction.textContent = isInitialPlacement
        ? `Assegna tutti i ${budget} carri iniziali ai tuoi territori. Puoi distribuirli su più territori.`
        : `Scegli fino a ${budget} carri tra i tuoi territori. Puoi distribuirli su più territori.`;
    } else if (isDeployAction) {
      elements.actionInstruction.textContent = `Il piazzamento è in attesa di ${currentState.currentPlayerName}.`;
    } else {
      elements.actionInstruction.textContent = 'Quando sarà il tuo turno, le azioni disponibili appariranno qui.';
    }

    renderSelectionList(canAct);
  }

  function renderAttackAction(pending, canAct) {
    const targets = pending.possibleTargets || [];
    const target = targets.includes(combatSelection.target) ? combatSelection.target : (targets[0] || '');
    const sources = pending.possibleStartingZones?.[target] || [];
    const source = sources.includes(combatSelection.source) ? combatSelection.source : (sources[0] || '');
    combatSelection.target = target;
    combatSelection.source = source;
    const ownership = territoryOwners();
    const territoryLabel = territory =>
      `${readableName(territory)} (${getTokenCount(ownership.get(territory)?.state.deployedTokensByTerritory?.[territory])})`;
    const sourceOwner = ownership.get(source);
    const sourceTokens = sourceOwner?.state.deployedTokensByTerritory?.[source] || {};
    const tokenType = Object.keys(sourceTokens)[0] || 'Tank';
    const maxAmount = Math.max(1, getTokenCount(sourceTokens) - 1);
    const amount = Math.min(Math.max(1, Number(combatSelection.tokenAmount) || 1), maxAmount);
    combatSelection.tokenAmount = amount;

    elements.actionTitle.textContent = 'Attacca un territorio';
    elements.deploymentBudget.textContent = `${amount} ${readableName(tokenType)}${amount === 1 ? '' : ''}`;
    elements.budgetFill.style.width = `${Math.min(100, (amount / maxAmount) * 100)}%`;
    elements.submitAction.disabled = !canAct || !target || !source || isSubmitting;
    elements.submitAction.textContent = isSubmitting ? 'Invio in corso…' : 'Invia attacco';
    elements.passAction.hidden = !canAct;
    elements.passAction.disabled = !canAct || isSubmitting;
    elements.passAction.textContent = 'Termina attacchi';
    elements.actionInstruction.textContent = canAct
      ? 'Scegli il territorio da attaccare e il territorio da cui partire.'
      : `L’attacco è in attesa di ${currentState.currentPlayerName || 'un altro giocatore'}.`;
    renderActionControls(
      actionSelect('Territorio bersaglio', 'target', targets, target, territoryLabel),
      actionSelect('Attacca da', 'source', sources, source, territoryLabel),
      actionNumber('Armate attaccanti', 'tokenAmount', amount, 1, maxAmount)
    );
    setActionControlsDisabled(!canAct || isSubmitting);
  }

  function renderDefendAction(pending, canAct) {
    const maxAmount = Number(pending.maxDefenderTokens || 0);
    const amount = Math.min(Math.max(1, Number(combatSelection.tokenAmount) || 1), Math.max(1, maxAmount));
    combatSelection.tokenAmount = amount;
    elements.actionTitle.textContent = 'Difendi il territorio';
    elements.deploymentBudget.textContent = `Massimo ${maxAmount} armate`;
    elements.budgetFill.style.width = `${maxAmount === 0 ? 0 : (amount / maxAmount) * 100}%`;
    elements.submitAction.disabled = !canAct || maxAmount === 0 || isSubmitting;
    elements.submitAction.textContent = isSubmitting ? 'Invio in corso…' : 'Conferma difesa';
    elements.passAction.hidden = true;
    elements.passAction.disabled = true;
    elements.actionInstruction.textContent = canAct
      ? 'Scegli quante armate usare per difendere.'
      : `La difesa è in attesa di ${pending.defenderName || 'un altro giocatore'}.`;
    renderActionControls(
      actionNumber(`Armate ${readableName(pending.defenderTokenType || 'unità')}`, 'tokenAmount', amount, 1, maxAmount)
    );
    setActionControlsDisabled(!canAct || isSubmitting);
  }

  function renderMovementAction(pending, canAct) {
    const possibleMoves = pending.possibleMoves || {};
    const sources = Object.keys(possibleMoves);
    const source = sources.includes(movementSelection.source)
      ? movementSelection.source : (sources[0] || '');
    const destinations = possibleMoves[source] || [];
    const destination = destinations.includes(movementSelection.destination)
      ? movementSelection.destination : (destinations[0] || '');
    movementSelection.source = source;
    movementSelection.destination = destination;
    const moveableTokens = pending.moveableTokens?.[source] || {};
    const tokenType = Object.keys(moveableTokens)[0] || 'Tank';
    const maxAmount = Number(moveableTokens[tokenType] || 0);
    const amount = Math.min(Math.max(1, Number(movementSelection.tokenAmount) || 1), Math.max(1, maxAmount));
    movementSelection.tokenAmount = amount;

    elements.actionTitle.textContent = 'Sposta armate';
    elements.deploymentBudget.textContent = `Massimo ${maxAmount} ${readableName(tokenType)}`;
    elements.budgetFill.style.width = `${maxAmount === 0 ? 0 : (amount / maxAmount) * 100}%`;
    elements.submitAction.disabled = !canAct || !source || !destination || maxAmount === 0 || isSubmitting;
    elements.submitAction.textContent = isSubmitting ? 'Invio in corso…' : 'Conferma spostamento';
    elements.passAction.hidden = !canAct;
    elements.passAction.disabled = !canAct || isSubmitting;
    elements.passAction.textContent = 'Salta spostamento';
    elements.actionInstruction.textContent = canAct
      ? 'Puoi spostare armate tra territori confinanti che possiedi.'
      : `Lo spostamento è in attesa di ${currentState.currentPlayerName || 'un altro giocatore'}.`;
    renderActionControls(
      actionSelect('Territorio di partenza', 'movementSource', sources, source),
      actionSelect('Territorio di arrivo', 'movementDestination', destinations, destination),
      actionNumber(`Armate ${readableName(tokenType)}`, 'movementTokenAmount', amount, 1, maxAmount)
    );
    setActionControlsDisabled(!canAct || isSubmitting);
  }

  function actionSelect(label, name, values, selectedValue, formatOption = readableName) {
    const field = document.createElement('label');
    field.className = 'action-field';
    field.textContent = label;
    const select = document.createElement('select');
    select.className = 'action-input';
    select.dataset.actionField = name;
    values.forEach(value => {
      const option = document.createElement('option');
      option.value = value;
      option.textContent = formatOption(value);
      option.selected = value === selectedValue;
      select.append(option);
    });
    field.append(select);
    return field;
  }

  function actionNumber(label, name, value, min, max) {
    const field = document.createElement('label');
    field.className = 'action-field';
    field.textContent = label;
    const input = document.createElement('input');
    input.className = 'action-input';
    input.type = 'number';
    input.dataset.actionField = name;
    input.min = String(min);
    input.max = String(max);
    input.value = String(value);
    field.append(input);
    return field;
  }

  function setActionControlsDisabled(disabled) {
    elements.selectionList.querySelectorAll('.action-input').forEach(control => {
      control.disabled = disabled;
    });
  }

  function renderActionControls(...controls) {
    const signature = controls.map(field => {
      const input = field.querySelector('.action-input');
      const options = [...(input.options || [])].map(option => `${option.value}:${option.textContent}`).join(',');
      return `${field.firstChild.textContent}:${input.dataset.actionField}:${options}:${input.min}:${input.max}`;
    }).join('|');
    if (signature !== actionControlsSignature) {
      elements.selectionList.replaceChildren(...controls);
      actionControlsSignature = signature;
    } else {
      controls.forEach(field => {
        const freshInput = field.querySelector('.action-input');
        const currentInput = elements.selectionList.querySelector(`[data-action-field="${freshInput.dataset.actionField}"]`);
        if (currentInput && currentInput.value !== freshInput.value) currentInput.value = freshInput.value;
      });
    }
  }

  function onActionControlChange(event) {
    const field = event.target.dataset.actionField;
    if (!field || isSubmitting) return;
    if (field === 'target') {
      combatSelection.target = event.target.value;
      combatSelection.source = '';
    } else if (field === 'source') {
      combatSelection.source = event.target.value;
    } else if (field === 'tokenAmount') {
      combatSelection.tokenAmount = Number(event.target.value);
    } else if (field === 'movementSource') {
      movementSelection.source = event.target.value;
      movementSelection.destination = '';
    } else if (field === 'movementDestination') {
      movementSelection.destination = event.target.value;
    } else if (field === 'movementTokenAmount') {
      movementSelection.tokenAmount = Number(event.target.value);
    }
    renderDeployment();
  }

  function renderSelectionList(canAct) {
    actionControlsSignature = '';
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

    const canAct = isViewer(currentState.currentPlayerName) && currentState.pendingAction?.type === 'DEPLOY';
    const selectable = new Set(currentState.pendingAction?.deployableTerritories || []);
    const budget = tokenBudget(currentState.pendingAction);
    const selectedTotal = deploymentTotal();
    const ownership = territoryOwners();
    const ownTerritories = currentState.players?.[viewerPlayerName()]?.deployedTokensByTerritory || {};
    const territories = new Set(Object.keys(ownTerritories));
    if (canAct) selectable.forEach(territory => territories.add(territory));
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
    if (territories.size === 0) {
      elements.territories.replaceChildren(createMessage('Non possiedi ancora territori.'));
    } else {
      elements.territories.replaceChildren(cards);
    }
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

  async function submitAction() {
    const actionType = currentState.pendingAction?.type;
    if (actionType === 'ATTACK') {
      await submitCombatAttack();
    } else if (actionType === 'DEFEND') {
      await submitCombatDefense();
    } else if (actionType === 'MOVE') {
      await submitMovement();
    } else {
      await submitDeployment();
    }
  }

  async function submitPassAction() {
    if (isSubmitting) return;
    isSubmitting = true;
    renderDeployment();
    try {
      const isMovement = currentState.pendingAction?.type === 'MOVE';
      const endpoint = isMovement ? 'movement' : 'combat';
      currentState = await requestJson(`/api/game/${encodeURIComponent(roomId)}/${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ type: 'PASS', playerName: viewerPlayerName() })
      });
      updateActionSignature();
      elements.actionError.hidden = true;
      renderGameState();
    } catch (error) {
      elements.actionError.textContent = error.message;
      elements.actionError.hidden = false;
    } finally {
      isSubmitting = false;
      renderDeployment();
    }
  }

  async function submitDeployment() {
    const selectedTotal = deploymentTotal();
    const budget = tokenBudget(currentState.pendingAction);
    const isInitialPlacement = currentState.phaseId === 'RISIKONEW_INITIAL_PLACEMENT';
    if (isSubmitting || selectedTotal === 0 || (isInitialPlacement && selectedTotal !== budget)) return;
    isSubmitting = true;
    renderDeployment();
    renderTerritories();
    try {
      const updatedState = await requestJson(`/api/game/${encodeURIComponent(roomId)}/deploy`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ playerName: viewerPlayerName(), tokenType: deploymentTokenType(), deployment: deploymentSelection })
      });
      deploymentSelection = {};
      stateRevision += 1;
      currentState = updatedState;
      boardRenderSignature = '';
      territoryRenderSignature = '';
      updateActionSignature();
      elements.actionError.hidden = true;
      renderGameState();
      const confirmation = currentState.phaseId === 'RISIKONEW_INITIAL_PLACEMENT'
        ? 'Piazzamento registrato'
        : 'Rinforzi registrati';
      setConnection(true, confirmation);
    } catch (error) {
      elements.actionError.textContent = error.message;
      elements.actionError.hidden = false;
    } finally {
      isSubmitting = false;
      renderDeployment();
      renderTerritories();
    }
  }

  async function submitCombatAttack() {
    if (isSubmitting) return;
    isSubmitting = true;
    renderDeployment();
    try {
      const sourceState = territoryOwners().get(combatSelection.source)?.state;
      const tokenType = Object.keys(sourceState?.deployedTokensByTerritory?.[combatSelection.source] || {})[0];
      currentState = await requestJson(`/api/game/${encodeURIComponent(roomId)}/combat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          type: 'ATTACK', playerName: viewerPlayerName(), attackerZone: combatSelection.source,
          defenderZone: combatSelection.target, tokenType, tokenAmount: Number(combatSelection.tokenAmount)
        })
      });
      updateActionSignature();
      elements.actionError.hidden = true;
      renderGameState();
    } catch (error) {
      elements.actionError.textContent = error.message;
      elements.actionError.hidden = false;
    } finally {
      isSubmitting = false;
      renderDeployment();
    }
  }

  async function submitCombatDefense() {
    if (isSubmitting) return;
    isSubmitting = true;
    renderDeployment();
    try {
      currentState = await requestJson(`/api/game/${encodeURIComponent(roomId)}/combat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          type: 'DEFEND', playerName: viewerPlayerName(), tokenType: currentState.pendingAction.defenderTokenType,
          tokenAmount: Number(combatSelection.tokenAmount)
        })
      });
      updateActionSignature();
      elements.actionError.hidden = true;
      renderGameState();
    } catch (error) {
      elements.actionError.textContent = error.message;
      elements.actionError.hidden = false;
    } finally {
      isSubmitting = false;
      renderDeployment();
    }
  }

  async function submitMovement() {
    if (isSubmitting) return;
    isSubmitting = true;
    renderDeployment();
    try {
      const tokenType = Object.keys(currentState.pendingAction.moveableTokens?.[movementSelection.source] || {})[0];
      currentState = await requestJson(`/api/game/${encodeURIComponent(roomId)}/movement`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          type: 'MOVE',
          playerName: viewerPlayerName(),
          source: movementSelection.source,
          destination: movementSelection.destination,
          tokenType,
          tokenAmount: Number(movementSelection.tokenAmount)
        })
      });
      updateActionSignature();
      elements.actionError.hidden = true;
      renderGameState();
    } catch (error) {
      elements.actionError.textContent = error.message;
      elements.actionError.hidden = false;
    } finally {
      isSubmitting = false;
      renderDeployment();
    }
  }

  function deploymentTokenType() {
    const tokenNames = Object.keys(currentState.pendingAction?.tokensToDeploy || {});
    return tokenNames.length === 1 ? tokenNames[0] : null;
  }

  function renderLatestEvent() {
    const phase = currentState.phaseId ? readableName(currentState.phaseId.replace('RISIKONEW_', '')) : 'Partita';
    const player = currentState.currentPlayerName || 'Nessun giocatore';
    const latest = `${currentState.eventType || 'AGGIORNAMENTO'} · ${phase} · ${player}`;
    const combat = currentState.combatResult;
    if (combat) {
      const attackerZone = combat.attackerTerritory || combat.attackerZone || 'Territorio attaccante';
      const defenderZone = combat.defenderTerritory || combat.defenderZone || 'Territorio difensore';
      const attackerRolls = (combat.attackerRolls || []).join(', ') || '—';
      const defenderRolls = (combat.defenderRolls || []).join(', ') || '—';
      const conquest = combat.territoryConquered ? 'Territorio conquistato.' : 'Territorio mantenuto.';
      elements.latestEvent.textContent = `${latest}\n${readableName(attackerZone)} → ${readableName(defenderZone)}\nDadi attaccante: ${attackerRolls} · difensore: ${defenderRolls}\nPerdite: attaccante ${combat.attackerLosses}, difensore ${combat.defenderLosses}. ${conquest}`;
    } else {
      elements.latestEvent.textContent = latest;
    }
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

  function viewerPlayerName() {
    const requestedName = normalizePlayerName(playerName);
    const matchingName = Object.keys(currentState?.players || {})
      .find(name => normalizePlayerName(name) === requestedName);
    return matchingName || playerName;
  }

  function isViewer(name) {
    return normalizePlayerName(name) === normalizePlayerName(playerName);
  }

  function normalizePlayerName(name) {
    return String(name || '').trim().toLocaleLowerCase();
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
