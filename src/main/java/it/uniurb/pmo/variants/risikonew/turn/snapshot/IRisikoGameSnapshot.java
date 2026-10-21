package it.uniurb.pmo.variants.risikonew.turn.snapshot;

import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;
import it.uniurb.pmo.framework.turn.snapshot.IGameSnapshot;

import java.util.Optional;

/** Informazioni aggiuntive dello snapshot specifiche della variante Risiko New. */
public interface IRisikoGameSnapshot extends IGameSnapshot {

    Optional<ICombatResultDTO> lastCombatResult();
}
