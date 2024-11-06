package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.NoAction;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.util.Util;
import codingblackfemales.sotw.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic implements AlgoLogic {
    // Create a logger to help us see what's happening
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    @Override
    public Action evaluate(SimpleAlgoState state) {
        // Get the current state of the order book as a string
        var orderBookAsString = Util.orderBookToString(state);

        // Log the current state so we can see what's happening
        logger.info("[MYALGO] Starting to evaluate...");
        logger.info("[MYALGO] The state of the order book is:\n" + orderBookAsString);

        // Get active orders and total orders
        var activeOrders = state.getActiveChildOrders();
        var currentOrders = state.getChildOrders().size();
        
        logger.info("[MYALGO] Current number of orders: " + currentOrders);

        // If we have active orders, cancel the first one
        if (!activeOrders.isEmpty()) {
            logger.info("[MYALGO] Cancelling an existing order");
            return new CancelChildOrder((long) activeOrders.get(0).getOrderId());
        }
        
        // If we have less than 3 orders and no active ones, create a new one
        if (currentOrders < 3) {
            logger.info("[MYALGO] Creating a new order");
            return new CreateChildOrder(Side.BUY, 100, 10);
        }

        return NoAction.NoAction;
    }
}