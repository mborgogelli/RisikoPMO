export function normalizePlayerName(name) {
  return String(name || '').trim().toLocaleLowerCase();
}

export function tokenBudget(pendingAction) {
  return Object.values(pendingAction?.tokensToDeploy || {})
    .reduce((total, count) => total + Number(count || 0), 0);
}

export function tokenSignature(tokens) {
  return Object.entries(tokens || {}).sort(([a], [b]) => a.localeCompare(b))
    .map(([name, amount]) => `${name}:${amount}`).join(',');
}

export function getTokenCount(tokens) {
  return tokens
    ? Number(tokens.Tank ?? tokens.TANK ?? tokens.tank ?? Object.values(tokens)[0] ?? 0)
    : 0;
}

export function readableName(value) {
  return String(value || '').replaceAll('_', ' ').replace(/\b\w/g, letter => letter.toUpperCase());
}

export function statusLabel(status) {
  const labels = { ACTIVE: 'Attivo', SKIP_TURN: 'Salta il turno', ELIMINATED: 'Eliminato' };
  return labels[status] || status || 'Attivo';
}

export function colorFor(color) {
  const palette = {
    BLACK: '#8b949e', BLUE: '#5ba8ff', GREEN: '#62d49a', PURPLE: '#b48cff', RED: '#ff7272', YELLOW: '#f5c451'
  };
  return palette[color] || '#94a3b8';
}
