export function createGameRenderer(elements, dependencies) {
  let territoryRenderSignature = '';

  function renderPhase() {
    const currentState = dependencies.getState();
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
    const isMyTurn = currentPlayer && dependencies.isViewer(currentPlayer);
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
      elements.phaseInstruction.textContent = dependencies.isViewer(pending.defenderName)
        ? 'Scegli quante armate usare per difendere il territorio attaccato.'
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
    const currentState = dependencies.getState();
    const players = Object.entries(currentState.players || {});
    elements.playersCount.textContent = String(players.length);
    elements.playersList.replaceChildren();
    if (players.length === 0) {
      elements.playersList.append(dependencies.createMessage('Nessun giocatore disponibile.'));
      return;
    }

    players.forEach(([name, state]) => {
      const card = document.createElement('article');
      card.className = 'player-card';
      if (name === currentState.currentPlayerName) card.classList.add('is-current');
      card.style.setProperty('--player-color', dependencies.colorFor(state.color));

      const heading = document.createElement('div');
      heading.className = 'player-heading';
      const marker = document.createElement('span');
      marker.className = 'player-marker';
      const title = document.createElement('strong');
      title.textContent = name;
      const status = document.createElement('span');
      status.className = 'player-status';
      status.textContent = name === currentState.currentPlayerName ? 'Di turno' : dependencies.statusLabel(state.turnStatus);
      heading.append(marker, title, status);

      const territories = Object.keys(state.deployedTokensByTerritory || {}).length;
      const troopsOnBoard = Object.values(state.deployedTokensByTerritory || {})
        .reduce((total, tokens) => total + dependencies.getTokenCount(tokens), 0);
      const troopsAvailable = dependencies.getTokenCount(state.availableTokens);
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

  function renderTerritories() {
    const currentState = dependencies.getState();
    if (!currentState) return;
    const signature = getTerritoryRenderSignature(currentState);
    if (signature === territoryRenderSignature) return;
    territoryRenderSignature = signature;

    const selection = dependencies.getDeploymentSelection();
    const canAct = dependencies.isViewer(currentState.currentPlayerName) && currentState.pendingAction?.type === 'DEPLOY';
    const selectable = new Set(currentState.pendingAction?.deployableTerritories || []);
    const budget = dependencies.tokenBudget(currentState.pendingAction);
    const selectedTotal = dependencies.deploymentTotal();
    const ownership = dependencies.territoryOwners();
    const ownTerritories = currentState.players?.[dependencies.viewerPlayerName()]?.deployedTokensByTerritory || {};
    const territories = new Set(Object.keys(ownTerritories));
    if (canAct) selectable.forEach(territory => territories.add(territory));
    const cards = document.createDocumentFragment();
    [...territories].sort((a, b) => a.localeCompare(b)).forEach(territory => {
      const owner = ownership.get(territory);
      const card = document.createElement('article');
      card.className = 'territory-card';
      card.dataset.territory = territory;
      card.tabIndex = 0;
      if (owner) card.classList.add('is-owned');
      if (selectable.has(territory) && canAct) card.classList.add('is-selectable');
      if (selection[territory]) card.classList.add('is-selected');
      if (dependencies.getHighlightedTerritory() === territory) card.classList.add('is-map-highlighted');
      if (owner) card.style.setProperty('--owner-color', dependencies.colorFor(owner.color));

      const top = document.createElement('div');
      top.className = 'territory-topline';
      const name = document.createElement('strong');
      name.textContent = dependencies.readableName(territory);
      const troops = dependencies.visibleTerritoryCount(territory, owner);
      const troopsLabel = document.createElement('span');
      troopsLabel.textContent = `${troops} ${troops === 1 ? 'carro' : 'carri'}`;
      top.append(name, troopsLabel);

      const ownerLabel = document.createElement('span');
      ownerLabel.className = 'territory-owner';
      ownerLabel.textContent = owner ? owner.name : 'Libero';

      const controls = document.createElement('div');
      controls.className = 'territory-controls';
      const chosen = selection[territory] || 0;
      const canSelectThis = canAct && !dependencies.isSubmitting() && selectable.has(territory);
      controls.append(
        adjustmentButton('−', 'decrease', territory, !canSelectThis || chosen === 0),
        amountBadge(chosen),
        adjustmentButton('+', 'increase', territory, !canSelectThis || selectedTotal >= budget)
      );
      card.append(top, ownerLabel, controls);
      cards.append(card);
    });
    if (territories.size === 0) {
      elements.territories.replaceChildren(dependencies.createMessage('Non possiedi ancora territori.'));
    } else {
      elements.territories.replaceChildren(cards);
    }
  }

  function getTerritoryRenderSignature(currentState) {
    const players = Object.entries(currentState.players || {}).sort(([a], [b]) => a.localeCompare(b));
    const playerStates = players.map(([name, state]) => {
      const territories = Object.entries(state.deployedTokensByTerritory || {})
        .sort(([a], [b]) => a.localeCompare(b))
        .map(([territory, tokens]) => `${territory}=${dependencies.tokenSignature(tokens)}`)
        .join(',');
      return `${name}:${state.color}:${dependencies.tokenSignature(state.availableTokens)}:${territories}`;
    }).join(';');
    const pending = currentState.pendingAction;
    const selectable = [...(pending?.deployableTerritories || [])].sort().join(',');
    const pendingTokens = dependencies.tokenSignature(pending?.tokensToDeploy);
    const actionType = pending?.type || '';
    const selection = Object.entries(dependencies.getDeploymentSelection()).sort(([a], [b]) => a.localeCompare(b))
      .map(([territory, amount]) => `${territory}=${amount}`).join(',');
    return [currentState.eventType, currentState.currentPlayerName, currentState.phaseId, actionType,
      dependencies.isSubmitting(), playerStates, selectable, pendingTokens, selection].join('|');
  }

  function renderLatestEvent() {
    const currentState = dependencies.getState();
    const phase = currentState.phaseId ? dependencies.readableName(currentState.phaseId.replace('RISIKONEW_', '')) : 'Partita';
    const player = currentState.currentPlayerName || 'Nessun giocatore';
    const latest = `${currentState.eventType || 'AGGIORNAMENTO'} · ${phase} · ${player}`;
    const combat = currentState.combatResult;
    if (combat) {
      const attackerZone = combat.attackerTerritory || combat.attackerZone || 'Territorio attaccante';
      const defenderZone = combat.defenderTerritory || combat.defenderZone || 'Territorio difensore';
      const attackerRolls = (combat.attackerRolls || []).join(', ') || '—';
      const defenderRolls = (combat.defenderRolls || []).join(', ') || '—';
      const conquest = combat.territoryConquered ? 'Territorio conquistato.' : 'Territorio mantenuto.';
      elements.latestEvent.textContent = `${latest}\n${dependencies.readableName(attackerZone)} → ${dependencies.readableName(defenderZone)}\nDadi attaccante: ${attackerRolls} · difensore: ${defenderRolls}\nPerdite: attaccante ${combat.attackerLosses}, difensore ${combat.defenderLosses}. ${conquest}`;
    } else {
      elements.latestEvent.textContent = latest;
    }
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

  function adjustmentButton(label, adjustment, territory, disabled) {
    const button = document.createElement('button');
    button.type = 'button';
    button.className = `adjustment-button adjustment-${adjustment}`;
    button.dataset.adjustment = adjustment;
    button.dataset.territory = territory;
    button.disabled = disabled;
    button.setAttribute('aria-label', `${adjustment === 'increase' ? 'Aggiungi un carro a' : 'Rimuovi un carro da'} ${dependencies.readableName(territory)}`);
    button.textContent = label;
    return button;
  }

  function amountBadge(amount) {
    const badge = document.createElement('span');
    badge.className = 'selected-count';
    badge.textContent = String(amount);
    return badge;
  }

  return {
    renderPhase,
    renderPlayers,
    renderTerritories,
    renderLatestEvent,
    invalidateTerritories: () => { territoryRenderSignature = ''; }
  };
}
