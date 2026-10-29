export function createGameActions(elements, dependencies) {
  let deploymentSelection = {};
  let combatSelection = { target: '', source: '', tokenAmount: 1 };
  let movementSelection = { source: '', destination: '', tokenAmount: 1 };
  let actionControlsSignature = '';
  let isSubmitting = false;

  function resetSelections() {
    deploymentSelection = {};
    combatSelection = { target: '', source: '', tokenAmount: 1 };
    movementSelection = { source: '', destination: '', tokenAmount: 1 };
    actionControlsSignature = '';
  }

  function deploymentTotal() {
    return Object.values(deploymentSelection).reduce((total, count) => total + count, 0);
  }

  function visibleTerritoryCount(territory, owner) {
    const deployed = owner ? dependencies.getTokenCount(owner.state.deployedTokensByTerritory?.[territory]) : 0;
    return deployed + (deploymentSelection[territory] || 0);
  }

  function render() {
    const currentState = dependencies.getState();
    const pending = currentState.pendingAction;
    const isDeployAction = pending?.type === 'DEPLOY';
    const isMyTurn = dependencies.isViewer(currentState.currentPlayerName);
    const isAttackAction = pending?.type === 'ATTACK';
    const isDefendAction = pending?.type === 'DEFEND';
    const isMovementAction = pending?.type === 'MOVE';
    const canAttack = isAttackAction && isMyTurn;
    const canDefend = isDefendAction && dependencies.isViewer(pending.defenderName);
    const canAct = isDeployAction && isMyTurn;
    const budget = dependencies.tokenBudget(pending);
    const selectedTotal = deploymentTotal();
    const phaseLabel = currentState.phaseId === 'RISIKONEW_INITIAL_PLACEMENT'
      ? 'Piazzamento iniziale' : 'Distribuzione dei rinforzi';

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
    const currentState = dependencies.getState();
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
    } else if (pending?.type === 'DEPLOY') {
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
    const ownership = dependencies.territoryOwners();
    const territoryLabel = territory =>
      `${dependencies.readableName(territory)} (${dependencies.getTokenCount(ownership.get(territory)?.state.deployedTokensByTerritory?.[territory])})`;
    const sourceOwner = ownership.get(source);
    const sourceTokens = sourceOwner?.state.deployedTokensByTerritory?.[source] || {};
    const tokenType = Object.keys(sourceTokens)[0] || 'Tank';
    const maxAmount = Math.max(1, dependencies.getTokenCount(sourceTokens) - 1);
    const amount = Math.min(Math.max(1, Number(combatSelection.tokenAmount) || 1), maxAmount);
    combatSelection.tokenAmount = amount;

    elements.actionTitle.textContent = 'Attacca un territorio';
    elements.deploymentBudget.textContent = `${amount} ${dependencies.readableName(tokenType)}${amount === 1 ? '' : ''}`;
    elements.budgetFill.style.width = `${Math.min(100, (amount / maxAmount) * 100)}%`;
    elements.submitAction.disabled = !canAct || !target || !source || isSubmitting;
    elements.submitAction.textContent = isSubmitting ? 'Invio in corso…' : 'Invia attacco';
    elements.passAction.hidden = !canAct;
    elements.passAction.disabled = !canAct || isSubmitting;
    elements.passAction.textContent = 'Termina attacchi';
    elements.actionInstruction.textContent = canAct
      ? 'Scegli il territorio da attaccare e il territorio da cui partire.'
      : `L’attacco è in attesa di ${dependencies.getState().currentPlayerName || 'un altro giocatore'}.`;
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
      actionNumber(`Armate ${dependencies.readableName(pending.defenderTokenType || 'unità')}`, 'tokenAmount', amount, 1, maxAmount)
    );
    setActionControlsDisabled(!canAct || isSubmitting);
  }

  function renderMovementAction(pending, canAct) {
    const currentState = dependencies.getState();
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
    elements.deploymentBudget.textContent = `Massimo ${maxAmount} ${dependencies.readableName(tokenType)}`;
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
      actionNumber(`Armate ${dependencies.readableName(tokenType)}`, 'movementTokenAmount', amount, 1, maxAmount)
    );
    setActionControlsDisabled(!canAct || isSubmitting);
  }

  function actionSelect(label, name, values, selectedValue, formatOption = dependencies.readableName) {
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

  function renderSelectionList(canAct) {
    actionControlsSignature = '';
    elements.selectionList.replaceChildren();
    if (!canAct) deploymentSelection = {};
    const selected = Object.entries(deploymentSelection).filter(([, count]) => count > 0);
    if (selected.length === 0) {
      elements.selectionList.append(dependencies.createMessage('Nessun carro selezionato.'));
      return;
    }

    selected.forEach(([territory, count]) => {
      const row = document.createElement('div');
      row.className = 'selection-row';
      const label = document.createElement('span');
      label.textContent = dependencies.readableName(territory);
      const amount = document.createElement('strong');
      amount.textContent = `${count} ${count === 1 ? 'carro' : 'carri'}`;
      row.append(label, amount);
      elements.selectionList.append(row);
    });
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
    render();
    if (['target', 'source', 'movementSource', 'movementDestination'].includes(field)) {
      dependencies.highlightTerritory(event.target.value);
    }
  }

  function onTerritoryClick(event) {
    const card = event.target.closest('.territory-card');
    if (card) dependencies.highlightTerritory(card.dataset.territory);
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
    dependencies.onSelectionChange();
  }

  function onTerritoryKeyDown(event) {
    if (event.target.matches('.territory-card') && ['Enter', ' '].includes(event.key)) {
      event.preventDefault();
      dependencies.highlightTerritory(event.target.dataset.territory);
    }
  }

  async function submit() {
    const currentState = dependencies.getState();
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

  async function submitPass() {
    if (isSubmitting) return;
    const currentState = dependencies.getState();
    isSubmitting = true;
    dependencies.onSubmittingChange();
    try {
      const isMovement = currentState.pendingAction?.type === 'MOVE';
      const endpoint = isMovement ? 'movement' : 'combat';
      const updatedState = await dependencies.requestJson(`/api/game/${encodeURIComponent(dependencies.roomId)}/${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ type: 'PASS', playerName: dependencies.viewerPlayerName() })
      });
      dependencies.onStateChange(updatedState, 'PASS');
    } catch (error) {
      elements.actionError.textContent = error.message;
      elements.actionError.hidden = false;
    } finally {
      isSubmitting = false;
      dependencies.onSubmittingChange();
    }
  }

  async function submitDeployment() {
    const currentState = dependencies.getState();
    const selectedTotal = deploymentTotal();
    const budget = dependencies.tokenBudget(currentState.pendingAction);
    const isInitialPlacement = currentState.phaseId === 'RISIKONEW_INITIAL_PLACEMENT';
    if (isSubmitting || selectedTotal === 0 || (isInitialPlacement && selectedTotal !== budget)) return;
    isSubmitting = true;
    dependencies.onSubmittingChange();
    try {
      const updatedState = await dependencies.requestJson(`/api/game/${encodeURIComponent(dependencies.roomId)}/deploy`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          playerName: dependencies.viewerPlayerName(),
          tokenType: deploymentTokenType(),
          deployment: deploymentSelection
        })
      });
      deploymentSelection = {};
      dependencies.onStateChange(updatedState, 'DEPLOY');
      const confirmation = updatedState.phaseId === 'RISIKONEW_INITIAL_PLACEMENT'
        ? 'Piazzamento registrato' : 'Rinforzi registrati';
      dependencies.setConnection(true, confirmation);
    } catch (error) {
      elements.actionError.textContent = error.message;
      elements.actionError.hidden = false;
    } finally {
      isSubmitting = false;
      dependencies.onSubmittingChange();
    }
  }

  async function submitCombatAttack() {
    if (isSubmitting) return;
    isSubmitting = true;
    dependencies.onSubmittingChange();
    try {
      const sourceState = dependencies.territoryOwners().get(combatSelection.source)?.state;
      const tokenType = Object.keys(sourceState?.deployedTokensByTerritory?.[combatSelection.source] || {})[0];
      const updatedState = await dependencies.requestJson(`/api/game/${encodeURIComponent(dependencies.roomId)}/combat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          type: 'ATTACK', playerName: dependencies.viewerPlayerName(), attackerZone: combatSelection.source,
          defenderZone: combatSelection.target, tokenType, tokenAmount: Number(combatSelection.tokenAmount)
        })
      });
      dependencies.onStateChange(updatedState, 'ATTACK');
    } catch (error) {
      elements.actionError.textContent = error.message;
      elements.actionError.hidden = false;
    } finally {
      isSubmitting = false;
      dependencies.onSubmittingChange();
    }
  }

  async function submitCombatDefense() {
    if (isSubmitting) return;
    const currentState = dependencies.getState();
    isSubmitting = true;
    dependencies.onSubmittingChange();
    try {
      const updatedState = await dependencies.requestJson(`/api/game/${encodeURIComponent(dependencies.roomId)}/combat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          type: 'DEFEND', playerName: dependencies.viewerPlayerName(), tokenType: currentState.pendingAction.defenderTokenType,
          tokenAmount: Number(combatSelection.tokenAmount)
        })
      });
      dependencies.onStateChange(updatedState, 'DEFEND');
    } catch (error) {
      elements.actionError.textContent = error.message;
      elements.actionError.hidden = false;
    } finally {
      isSubmitting = false;
      dependencies.onSubmittingChange();
    }
  }

  async function submitMovement() {
    if (isSubmitting) return;
    const currentState = dependencies.getState();
    isSubmitting = true;
    dependencies.onSubmittingChange();
    try {
      const tokenType = Object.keys(currentState.pendingAction.moveableTokens?.[movementSelection.source] || {})[0];
      const updatedState = await dependencies.requestJson(`/api/game/${encodeURIComponent(dependencies.roomId)}/movement`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          type: 'MOVE', playerName: dependencies.viewerPlayerName(), source: movementSelection.source,
          destination: movementSelection.destination, tokenType, tokenAmount: Number(movementSelection.tokenAmount)
        })
      });
      dependencies.onStateChange(updatedState, 'MOVE');
    } catch (error) {
      elements.actionError.textContent = error.message;
      elements.actionError.hidden = false;
    } finally {
      isSubmitting = false;
      dependencies.onSubmittingChange();
    }
  }

  function deploymentTokenType() {
    const tokenNames = Object.keys(dependencies.getState().pendingAction?.tokensToDeploy || {});
    return tokenNames.length === 1 ? tokenNames[0] : null;
  }

  return {
    render,
    submit,
    submitPass,
    onActionControlChange,
    onTerritoryClick,
    onTerritoryKeyDown,
    resetSelections,
    getDeploymentSelection: () => deploymentSelection,
    deploymentTotal,
    visibleTerritoryCount,
    isSubmitting: () => isSubmitting
  };
}
