import static org.junit.jupiter.api.Assertions.assertEquals;

import it.uniurb.pmo.variants.risikonew.utils.EnumRisikoNewPhase;
import org.junit.jupiter.api.Test;

public class EnumRisikoNewPhaseTest {

    /*@Test
    public void nextShouldFollowCyclicOrder() {
        assertEquals(2, EnumRisikoNewPhase.ASSIGNMENT.next());
        assertEquals(3, EnumRisikoNewPhase.REINFORCE.next());
        assertEquals(4, EnumRisikoNewPhase.ATTACK.next());
        assertEquals(1, EnumRisikoNewPhase.MOVEMENT.next());
    }*/

     @Test
    public void getIdShouldMatchExpectedValues() {
        assertEquals(1, EnumRisikoNewPhase.REINFORCE.getId());
        assertEquals(2, EnumRisikoNewPhase.ATTACK.getId());
        assertEquals(3, EnumRisikoNewPhase.MOVEMENT.getId());
    }
}
