import { MarketDepthRow } from "./useMarketDepthData";
import { PriceCell } from "./PriceCell";
import "./MarketDepthPanel.css";

interface MarketDepthPanelProps {
  data: MarketDepthRow[];
}

export const MarketDepthPanel = (props: MarketDepthPanelProps) => {
  const { data } = props;

  return (
    <div style={{ height: '400px', overflow: 'auto' }}>
      <table className="MarketDepthPanel">
        <thead>
          <tr>
            <th>Bid Qty</th>
            <th>Bid</th>
            <th>Offer</th>
            <th>Offer Qty</th>
          </tr>
        </thead>
        <tbody>
          {data.map((row) => (
            <tr key={row.symbolLevel}>
              <td>{row.bidQuantity}</td>
              <PriceCell price={row.bid} />
              <PriceCell price={row.offer} />
              <td>{row.offerQuantity}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}; 