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

export function createGameMap(elements, dependencies) {
  let boardSvg = null;
  let boardTerritoryLayer = null;
  let boardLabels = null;
  let boardRenderSignature = '';
  let initialMapView = null;
  let mapView = null;
  let mapZoom = 1;
  let mapDrag = null;
  let highlightedTerritory = '';

  elements.mapZoomIn.addEventListener('click', () => zoomMap(1.5));
  elements.mapZoomOut.addEventListener('click', () => zoomMap(1 / 1.5));
  elements.mapZoomReset.addEventListener('click', resetMapView);

  async function load() {
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
      const viewBox = boardSvg.viewBox.baseVal;
      initialMapView = { x: viewBox.x, y: viewBox.y, width: viewBox.width, height: viewBox.height };
      updateMapView(initialMapView.x, initialMapView.y, 1);
      boardSvg.addEventListener('wheel', onMapWheel, { passive: false });
      boardSvg.addEventListener('pointerdown', onMapPointerDown);
      boardSvg.addEventListener('pointermove', onMapPointerMove);
      boardSvg.addEventListener('pointerup', endMapDrag);
      boardSvg.addEventListener('pointercancel', endMapDrag);
      boardSvg.addEventListener('lostpointercapture', endMapDrag);
      render();
    } catch (error) {
      elements.mapSummary.textContent = 'Mappa non disponibile';
      elements.boardMap.replaceChildren(dependencies.createMessage(error.message, 'error-copy'));
    }
  }

  function updateMapView(x, y, zoom) {
    const width = initialMapView.width / zoom;
    const height = initialMapView.height / zoom;
    const maxX = initialMapView.x + initialMapView.width - width;
    const maxY = initialMapView.y + initialMapView.height - height;
    mapZoom = zoom;
    mapView = {
      x: Math.min(Math.max(x, initialMapView.x), maxX),
      y: Math.min(Math.max(y, initialMapView.y), maxY),
      width,
      height
    };
    boardSvg.setAttribute('viewBox', `${mapView.x} ${mapView.y} ${width} ${height}`);
    boardSvg.classList.toggle('is-zoomed', zoom > 1);
    elements.mapZoomIn.disabled = zoom >= 4;
    elements.mapZoomOut.disabled = zoom <= 1;
    elements.mapZoomReset.disabled = zoom <= 1;
  }

  function zoomMap(factor, clientX, clientY) {
    if (boardSvg && mapView) {
      const bounds = boardSvg.getBoundingClientRect();
      const anchorX = clientX == null ? 0.5 : Math.min(Math.max((clientX - bounds.left) / bounds.width, 0), 1);
      const anchorY = clientY == null ? 0.5 : Math.min(Math.max((clientY - bounds.top) / bounds.height, 0), 1);
      const nextZoom = Math.min(Math.max(mapZoom * factor, 1), 4);
      const pointX = mapView.x + anchorX * mapView.width;
      const pointY = mapView.y + anchorY * mapView.height;
      mapDrag = null;
      boardSvg.classList.remove('is-dragging');
      updateMapView(pointX - anchorX * initialMapView.width / nextZoom,
        pointY - anchorY * initialMapView.height / nextZoom, nextZoom);
    }
  }

  function resetMapView() {
    if (boardSvg && initialMapView) {
      mapDrag = null;
      boardSvg.classList.remove('is-dragging');
      updateMapView(initialMapView.x, initialMapView.y, 1);
    }
  }

  function onMapWheel(event) {
    event.preventDefault();
    zoomMap(event.deltaY < 0 ? 1.25 : 0.8, event.clientX, event.clientY);
  }

  function onMapPointerDown(event) {
    if (mapZoom > 1 && event.button === 0) {
      mapDrag = { pointerId: event.pointerId, x: event.clientX, y: event.clientY,
        viewX: mapView.x, viewY: mapView.y, width: mapView.width, height: mapView.height };
      boardSvg.setPointerCapture(event.pointerId);
      boardSvg.classList.add('is-dragging');
    }
  }

  function onMapPointerMove(event) {
    if (mapDrag && event.pointerId === mapDrag.pointerId) {
      const bounds = boardSvg.getBoundingClientRect();
      const x = mapDrag.viewX - (event.clientX - mapDrag.x) * mapDrag.width / bounds.width;
      const y = mapDrag.viewY - (event.clientY - mapDrag.y) * mapDrag.height / bounds.height;
      updateMapView(x, y, mapZoom);
    }
  }

  function endMapDrag(event) {
    if (mapDrag && event.pointerId === mapDrag.pointerId) {
      mapDrag = null;
      boardSvg.classList.remove('is-dragging');
    }
  }

  function render() {
    const currentState = dependencies.getState();
    if (!boardSvg || !currentState) return;
    const ownership = dependencies.getTerritoryOwners();
    const territories = [...ownership.entries()].sort(([a], [b]) => a.localeCompare(b));
    boardTerritoryLayer.style.opacity = '1';
    const signature = territories.map(([territory, owner]) =>
      `${territory}:${owner.name}:${owner.color}:${dependencies.visibleTerritoryCount(territory, owner)}`
    ).join('|') + '|territory-names';
    const territoryPaths = boardTerritoryLayer.querySelectorAll(':scope > path[id]');
    territoryPaths.forEach(path => {
      const owner = ownership.get(path.id);
      path.style.fill = owner ? dependencies.colorFor(owner.color) : '#334238';
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
      marker.dataset.territory = territory;
      marker.setAttribute('transform', `translate(${centerX} ${centerY})`);
      if (owner) {
        const badge = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
        badge.setAttribute('class', 'army-badge');
        badge.setAttribute('r', '10');
        badge.style.setProperty('--army-owner-color', dependencies.colorFor(owner.color));
        const count = document.createElementNS('http://www.w3.org/2000/svg', 'text');
        count.setAttribute('class', 'army-count');
        count.textContent = String(dependencies.visibleTerritoryCount(territory, owner));
        marker.append(badge, count);
      }
      const name = dependencies.readableName(territory);
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
    syncHighlight();
  }

  function syncHighlight() {
    if (boardTerritoryLayer && boardLabels) {
      boardTerritoryLayer.querySelectorAll(':scope > path[id]').forEach(path => {
        path.classList.toggle('is-map-highlighted', path.id === highlightedTerritory);
      });
      boardLabels.querySelectorAll('.army-marker').forEach(marker => {
        marker.classList.toggle('is-map-highlighted', marker.dataset.territory === highlightedTerritory);
      });
    }
  }

  function highlight(territory) {
    highlightedTerritory = territory;
    syncHighlight();
    dependencies.onHighlight(territory);
    if (boardSvg && mapZoom > 1 && territoryLabelPositions[territory]) {
      const [x, y] = territoryLabelPositions[territory];
      const transform = boardTerritoryLayer.transform.baseVal.consolidate()?.matrix;
      if (transform) {
        const mapX = transform.a * x + transform.c * y + transform.e;
        const mapY = transform.b * x + transform.d * y + transform.f;
        updateMapView(mapX - mapView.width / 2, mapY - mapView.height / 2, mapZoom);
      }
    }
  }

  return { load, render, highlight, getHighlightedTerritory: () => highlightedTerritory };
}
