package com.skypro.bills.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class MeterDTO {

  private String serialNumber;
  @JsonProperty(access = Access.READ_ONLY)
  private int lastIndication;

  public MeterDTO() {
  }

  public MeterDTO(String serialNumber, int lastIndication) {
    this.serialNumber = serialNumber;
    this.lastIndication = lastIndication;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public int getLastIndication() {
    return lastIndication;
  }

  public void setLastIndication(int lastIndication) {
    this.lastIndication = lastIndication;
  }
}
