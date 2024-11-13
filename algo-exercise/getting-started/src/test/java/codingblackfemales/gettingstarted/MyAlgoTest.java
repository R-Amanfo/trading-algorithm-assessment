package codingblackfemales.gettingstarted;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import codingblackfemales.action.Action;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.algo.AlgoLogic;
import messages.order.Side;


/**
 * This test is designed to check your algo behavior in isolation of the order book.
 *
 * You can tick in market data messages by creating new versions of createTick() (ex. createTick2, createTickMore etc..)
 *
 * You should then add behaviour to your algo to respond to that market data by creating or cancelling child orders.
 *
 * When you are comfortable you algo does what you expect, then you can move on to creating the MyAlgoBackTest.
 */
public class MyAlgoTest extends AbstractAlgoTest {

    @Override
    public AlgoLogic createAlgoLogic() {
        //this adds your algo logic to the container classes
        return new MyAlgoLogic();
    }

    @Test
    public void testDispatchThroughSequencer() throws Exception {
        //create a sample market data tick....
        send(createTick(100, 100, 101, 100));

        //simple assert to check we had 3 orders created
        //assertEquals(container.getState().getChildOrders().size(), 3);
    }

    // Test Case 1: Check if first order is created
    @Test
    public void test1FirstOrder() throws Exception {
        // Send market data
        send(createTick(100, 100, 101, 100));
        
        // Check if orders were created
        assertEquals(3, container.getState().getChildOrders().size());
    }

    // Test Case 2: Check if order gets cancelled
    // @Test
    // public void test2CancelOrder() throws Exception {
    //     // Create an order first
    //     send(createTick(100, 100, 101, 100));
        
    //     // Let algo run again to cancel order
    //     container.evaluate();
        
    //     // Check if order was cancelled
    //     assertEquals(0, container.getState().getActiveChildOrders().size());
    // }

    // Test Case 3: Check maximum orders limit
    @Test
    public void test3MaxOrders() throws Exception {
        // Create first order
        send(createTick(100, 100, 101, 100));
        
        // Create second order
        send(createTick(100, 100, 101, 100));
        
        // Create third order
        send(createTick(100, 100, 101, 100));
        
        // Check we don't exceed 3 orders
        assertEquals(3, container.getState().getChildOrders().size());
    }

    // Test Case 4: Check response to price change
    @Test
    public void test4PriceChange() throws Exception {
        // Create first order with normal price
        send(createTick(100, 100, 101, 100));
        
        // Send tick with different price
        send(createTick(100, 100, 101, 100));
        
        // Check algo response
        assertEquals(3, container.getState().getChildOrders().size());
    }

    // Test Case 5: Check simple market data trend response
    @Test
    public void test5MarketTrend() throws Exception {
        // First tick with normal price
        send(createTick(100, 100, 101, 100));
        
        // Second tick with same price
        send(createTick(100, 100, 101, 100));
        
        // Third tick with same price
        send(createTick(100, 100, 101, 100));
        
        // Check we created orders for the trend
        // Note: Using 3 as max orders from test3MaxOrders
        assertEquals(3, container.getState().getChildOrders().size());
    }

    // Test Case 6: Check basic profit taking
    // @Test
    // public void test6ProfitTaking() throws Exception {
    //     // Create first order
    //     send(createTick(100, 100, 101, 100));
        
    //     // Let algo evaluate to potentially cancel
    //     container.evaluate();
        
    //     // Create another order
    //     send(createTick(100, 100, 101, 100));
        
    //     // Check we have orders (either new ones or kept existing ones)
    //     assertTrue(container.getState().getChildOrders().size() > 0);
    // }

     // STRETCH GOAL TEST CASES START HERE

    @Test
    public void shouldBuyWhenNoPosition() throws Exception {
        // Given market with bid 100 and ask 101
        send(createTick(100, 100, 101, 100));
        
        // When algo evaluates
        container.runAlgoLogic();
        
        // Then should create buy order at bid price
        var orders = container.getState().getChildOrders();
        assertTrue(!orders.isEmpty());
        var order = orders.get(0);
        assertEquals(Side.BUY, order.getSide());
        assertEquals(100, order.getPrice());
        assertEquals(100, order.getQuantity());
    }

    @Test
    public void shouldSellAfterBuying() throws Exception {
        // Given market with bid 100 and ask 101
        send(createTick(100, 100, 101, 100));
        
        // First buy order
        container.runAlgoLogic();
        
        // When algo evaluates again
        container.runAlgoLogic();

        // Then should create sell order at ask price
        var orders = container.getState().getChildOrders();
        assertTrue(!orders.isEmpty());
        var order = orders.get(orders.size() - 1);
        assertEquals(Side.SELL, order.getSide());
        assertEquals(101, order.getPrice());
        assertEquals(100, order.getQuantity());
    }
    

    @Test
    public void shouldCycleBuyAndSellWithChangingPrices() throws Exception {
        // First market state - buy at 100
        send(createTick(100, 100, 101, 100));
        container.runAlgoLogic();
        var orders = container.getState().getChildOrders();
        assertTrue(!orders.isEmpty());
        var buyOrder = orders.get(orders.size() - 1);
        assertEquals(Side.BUY, buyOrder.getSide());

        // Price moves up - sell at 103
        send(createTick(102, 100, 103, 100));
        container.runAlgoLogic();
        orders = container.getState().getChildOrders();
        var sellOrder = orders.get(orders.size() - 1);
        assertEquals(Side.SELL, sellOrder.getSide());

        // Price moves down - buy at 99
        send(createTick(99, 100, 100, 100));
        container.runAlgoLogic();
        orders = container.getState().getChildOrders();
        var buyAgainOrder = orders.get(orders.size() - 1);
        assertEquals(Side.BUY, buyAgainOrder.getSide());
    }
}

