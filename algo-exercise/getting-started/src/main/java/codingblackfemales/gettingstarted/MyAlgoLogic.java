package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.action.NoAction;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.util.Util;
import messages.order.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic implements AlgoLogic {
    // Create a logger to help us see what's happening
    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    // Track whether we should buy or sell next
    private boolean shouldBuy = true;

    // Add new tracking variables
    private double lastBuyPrice = 0.0;
    private double lastSellPrice = 0.0;
    private static final double MIN_PROFIT_MARGIN = 0.02; // 2% minimum profit target

    @Override
    public Action evaluate(SimpleAlgoState state) {
        // Get the current state of the order book as a string
        var orderBookAsString = Util.orderBookToString(state);

        // Log the current state so we can see what's happening
        logger.info("[MYALGO] Starting to evaluate...");
        logger.info("[MYALGO] The state of the order book is:\n" + orderBookAsString);

        // Get total orders and log the count
        var currentOrders = state.getChildOrders().size();
        logger.info("[MYALGO] Current number of orders: " + currentOrders);

        // Don't create more than 3 orders (requirement from tests)
        if (currentOrders >= 3) {
            logger.info("[MYALGO] Already have 3 orders, no action needed");
            return NoAction.NoAction;
        }

        // Get best prices from both sides of the market
        var bestBidPrice = state.getBidAt(0).getPrice();
        var bestAskPrice = state.getAskAt(0).getPrice();

        // Add spread calculation
        double spread = bestAskPrice - bestBidPrice;
        double spreadPercentage = (spread / bestBidPrice) * 100;
        logger.info("[MYALGO] Current spread: " + spreadPercentage + "%");

        // Safety check - only trade if we have valid prices
        if (bestBidPrice <= 0 || bestAskPrice <= 0) {
            logger.info("[MYALGO] Invalid prices detected, no action needed");
            return NoAction.NoAction;
        }

        // Enhanced trading logic
        if (shouldBuy) {
            // Only buy if the price is lower than our last sell price (minus minimum profit)
            if (lastSellPrice > 0 && bestBidPrice >= lastSellPrice - (lastSellPrice * MIN_PROFIT_MARGIN)) {
                logger.info("[MYALGO] Waiting for better buying opportunity");
                return NoAction.NoAction;
            }
            
            logger.info("[MYALGO] Creating BUY order at price: " + bestBidPrice);
            lastBuyPrice = bestBidPrice;
            shouldBuy = false;
            return new CreateChildOrder(Side.BUY, 100, bestBidPrice);
        } else {
            // Only sell if the price is higher than our last buy price (plus minimum profit)
            if (lastBuyPrice > 0 && bestAskPrice <= lastBuyPrice + (lastBuyPrice * MIN_PROFIT_MARGIN)) {
                logger.info("[MYALGO] Waiting for better selling opportunity");
                return NoAction.NoAction;
            }
            
            logger.info("[MYALGO] Creating SELL order at price: " + bestAskPrice);
            lastSellPrice = bestAskPrice;
            shouldBuy = true;
            return new CreateChildOrder(Side.SELL, 100, bestAskPrice);
        }
    }
}