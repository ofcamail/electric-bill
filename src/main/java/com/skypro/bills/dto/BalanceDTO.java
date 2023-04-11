package com.skypro.bills.dto;

public class BalanceDTO {

  private String serialNumber;
  private double currentBalance;

  public BalanceDTO() {
  }

  public BalanceDTO(String serialNumber, double currentBalance) {
    this.serialNumber = serialNumber;
    this.currentBalance = currentBalance;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public double getCurrentBalance() {
    return currentBalance;
  }

  public void setCurrentBalance(double currentBalance) {
    this.currentBalance = currentBalance;
  }
}
