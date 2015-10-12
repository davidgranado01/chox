package idas.chox.core.model;

/**
 * @author John
 */
public enum AutomaticRoutingStrategy {
    NONE            (0, "Disabled"),
    POLICY          (1, "By Policy Number"),
    PRICE           (2, "By Customer Vehicle Class Price"),
    CHO             (3, "By CHO Assignment"),
    ROUND_ROBIN     (4, "By Round-Robin Assignment"),
    FEWEST_CLAIMS   (5, "By Exisiting Allocation");
        
    private final String description;
    private final int automaticRoutingStrategyValue;

    AutomaticRoutingStrategy(int automaticRoutingStrategyValue, String description) {
        this.automaticRoutingStrategyValue = automaticRoutingStrategyValue;
        this.description = description;
    }

    public int getAutomaticRoutingStrategyValue() {
        return automaticRoutingStrategyValue;
    }
    
    @Override
    public String toString() {
        return description;
    }

    public static AutomaticRoutingStrategy getAutomaticRoutingStrategy(int strategyIndex) {
      for (AutomaticRoutingStrategy s : AutomaticRoutingStrategy.values()) {
          if (s.getAutomaticRoutingStrategyValue() == strategyIndex) {
              return s;
          }
      }
      throw new IllegalArgumentException("No such Automatic Routing Strategy: " + strategyIndex);
   }
}
