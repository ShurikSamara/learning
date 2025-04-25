package constants;

import lombok.Getter;

@Getter
public enum BrokerName {
  T_GATEWAY_IN("topic-gateway-in"),;

  private final String brokerName;

  BrokerName(String brokerName) {
    this.brokerName = brokerName;
  }

}
