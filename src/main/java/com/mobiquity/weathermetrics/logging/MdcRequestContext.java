package com.mobiquity.weathermetrics.logging;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MdcRequestContext {

  private String requestId;
  private String city;

  @Override
  public String toString() {
    return requestId + ":" + city;
  }
}
