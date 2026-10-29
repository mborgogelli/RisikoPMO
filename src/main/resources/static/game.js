import {
  colorFor, getTokenCount, normalizePlayerName, readableName,
  statusLabel, tokenBudget, tokenSignature
} from './game-utils.js';
import { createGameMap } from './game-map.js';
import { createGameRenderer } from './game-render.js';
import { createGameActions } from './game-actions.js';

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
    mapZoomIn: document.getElementById('mapZoomIn'),
    mapZoomOut: document.getElementById('mapZoomOut'),
    mapZoomReset: document.getElementById('mapZoomReset'),
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

  let currentState = null;
  let stateRevision = 0;
  let actionSignature = '';
  let gameMap = null;
  let gameRenderer = null;
  const gameActions = createGameActions(elements, {
    getState: () => currentState,
    roomId,
    requestJson,
    viewerPlayerName,
    isViewer,
    territoryOwners,
    getTokenCount,
    readableName,
    tokenBudget,
    createMessage,
    highlightTerritory: territory => gameMap.highlight(territory),
    onSelectionChange: () => {
      gameActions.render();
      gameRenderer.renderTerritories();
      gameMap.render();
    },
    onSubmittingChange: () => {
      gameActions.render();
      gameRenderer.renderTerritories();
    },
    onStateChange: (updatedState, actionType) => {
      if (actionType === 'DEPLOY') {
        stateRevision += 1;
        gameRenderer.invalidateTerritories();
      }
      currentState = updatedState;
      updateActionSignature();
      elements.actionError.hidden = true;
      renderGameState();
    },
    setConnection
  });
  gameMap = createGameMap(elements, {
    getState: () => currentState,
    getTerritoryOwners: territoryOwners,
    visibleTerritoryCount: gameActions.visibleTerritoryCount,
    colorFor,
    readableName,
    createMessage,
    onHighlight: syncTerritoryHighlight
  });
  gameRenderer = createGameRenderer(elements, {
    getState: () => currentState,
    isViewer,
    viewerPlayerName,
    territoryOwners,
    visibleTerritoryCount: gameActions.visibleTerritoryCount,
    getDeploymentSelection: gameActions.getDeploymentSelection,
    deploymentTotal: gameActions.deploymentTotal,
    getHighlightedTerritory: gameMap.getHighlightedTerritory,
    isSubmitting: gameActions.isSubmitting,
    colorFor,
    getTokenCount,
    readableName,
    statusLabel,
    tokenBudget,
    tokenSignature,
    createMessage
  });

  elements.roomId.textContent = roomId || '—';
  elements.playerName.textContent = playerName || '—';
  elements.territories.addEventListener('click', gameActions.onTerritoryClick);
  elements.territories.addEventListener('keydown', gameActions.onTerritoryKeyDown);
  elements.submitAction.addEventListener('click', gameActions.submit);
  elements.passAction.addEventListener('click', gameActions.submitPass);
  elements.selectionList.addEventListener('change', gameActions.onActionControlChange);
  if (!roomId) {
    setConnection(false, 'Manca il codice stanza');
    elements.phaseInstruction.textContent = 'Apri la partita dal link ricevuto nella lobby.';
  } else {
    gameMap.load();
    pollGameState();
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
      gameActions.resetSelections();
      gameMap.highlight('');
      actionSignature = signature;
    }
  }

  function renderGameState() {
    gameRenderer.renderPhase();
    gameRenderer.renderPlayers();
    gameActions.render();
    gameMap.render();
    gameRenderer.renderTerritories();
    gameRenderer.renderLatestEvent();
  }

  function syncTerritoryHighlight(territory) {
    elements.territories.querySelectorAll('.territory-card').forEach(card => {
      card.classList.toggle('is-map-highlighted', card.dataset.territory === territory);
    });
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

  function createMessage(message, className = 'empty-state') {
    const paragraph = document.createElement('p');
    paragraph.className = className;
    paragraph.textContent = message;
    return paragraph;
  }

});
