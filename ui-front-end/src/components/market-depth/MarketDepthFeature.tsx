import { MarketDepthPanel } from "./MarketDepthPanel";
import { useMarketDepthData } from "./useMarketDepthData";
import { schemas } from "../../data/algo-schemas";

/**
 * Market Depth Feature Component
 * Displays market depth data using the MarketDepthPanel
 */
export const MarketDepthFeature = () => {
  // Get live data using the provided hook
  const data = useMarketDepthData(schemas.prices);
  
  return <MarketDepthPanel data={data} />;
};
