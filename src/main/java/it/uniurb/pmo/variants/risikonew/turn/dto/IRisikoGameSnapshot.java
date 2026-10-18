package it.uniurb.pmo.variants.risikonew.turn.dto;

import it.uniurb.pmo.framework.turn.dto.IGameSnapshot;
import it.uniurb.pmo.framework.turn.dto.ICombatResultDTO;

import java.util.Optional;

/** Informazioni aggiuntive dello snapshot specifiche della variante Risiko New. */
public interface IRisikoGameSnapshot extends IGameSnapshot {

    Optional<ICombatResultDTO> lastCombatResult();
}
