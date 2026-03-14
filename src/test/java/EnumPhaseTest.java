import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.uniurb.pmo.model.utils.EnumPhase;

public class EnumPhaseTest {

    @Test
    public void nextShouldFollowCyclicOrder() {
        assertEquals(2, EnumPhase.ASSIGNMENT.next());
        assertEquals(3, EnumPhase.REINFORCE.next());
        assertEquals(4, EnumPhase.ATTACK.next());
        assertEquals(1, EnumPhase.MOVEMENT.next());
    }

    @Test
    public void getIdShouldMatchExpectedValues() {
        assertEquals(1, EnumPhase.ASSIGNMENT.getId());
        assertEquals(2, EnumPhase.REINFORCE.getId());
        assertEquals(3, EnumPhase.ATTACK.getId());
        assertEquals(4, EnumPhase.MOVEMENT.getId());
    }
}
