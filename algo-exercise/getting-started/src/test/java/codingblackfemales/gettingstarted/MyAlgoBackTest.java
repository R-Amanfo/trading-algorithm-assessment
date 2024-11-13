package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class MyAlgoBackTest extends AbstractAlgoBackTest {

    @Override
    public AlgoLogic createAlgoLogic() {
        return new MyAlgoLogic();
    }

    @Test
    public void testExampleBackTest() throws Exception {
        // Create first market tick
        send(createTick());
        
        // Check order was created - expecting 3 orders based on MyAlgoLogic implementation
        assertEquals(3, container.getState().getChildOrders().size());

        // Send another market tick to trigger response
        send(createTick());
        
        // Check final state
        assertEquals(3, container.getState().getChildOrders().size());
    }
}
