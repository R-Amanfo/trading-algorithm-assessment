import { useRef } from "react";
import "./PriceCell.css";

export interface PriceCellProps {
    price: number;
}

export const PriceCell = (props: PriceCellProps) => {
    const { price } = props;
    
    // Remember the last price we saw
    const previousPrice = useRef(price);
    
    // Is the new price higher or lower?
    const priceWentUp = price > previousPrice.current;
    const priceWentDown = price < previousPrice.current;
    
    // Save current price for next comparison
    previousPrice.current = price;

    // Choose the right color based on price movement
    let priceMovement = "";
    if (priceWentUp) priceMovement = "up";     // Will show green
    if (priceWentDown) priceMovement = "down";  // Will show red

    return (
        <td className={`PriceCell ${priceMovement}`}>
            {price.toFixed(2)}
        </td>
    );
}; 