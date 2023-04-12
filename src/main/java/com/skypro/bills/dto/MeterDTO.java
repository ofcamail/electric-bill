package com.skypro.bills.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.skypro.bills.model.ElectricityMeter;
import lombok.Data;

@Data
public class MeterDTO {
  private String serialNumber;
  @JsonProperty(access = Access.READ_ONLY)
  private int lastIndication;

  public ElectricityMeter toElectricityMeter() {
    ElectricityMeter meter = new ElectricityMeter();
    meter.setSerialNumber(this.getSerialNumber());
    return meter;
  }
}