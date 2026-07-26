import static org.junit.jupiter.api.Assertions.assertEquals;

import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;
import org.junit.jupiter.api.Test;

public class EnumRisikoNewPhaseTest {

    /*@Test
    public void nextShouldFollowCyclicOrder() {
        assertEquals(2, ERisikoNewPhase.ASSIGNMENT.next());
        assertEquals(3, ERisikoNewPhase.REINFORCE.next());
        assertEquals(4, ERisikoNewPhase.ATTACK.next());
        assertEquals(1, ERisikoNewPhase.MOVEMENT.next());
    }*/

     @Test
    public void getIdShouldMatchExpectedValues() {
        assertEquals(1, ERisikoNewPhase.REINFORCE.getId());
        assertEquals(2, ERisikoNewPhase.ATTACK.getId());
        assertEquals(3, ERisikoNewPhase.MOVEMENT.getId());
    }
}
