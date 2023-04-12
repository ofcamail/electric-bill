package com.skypro.bills.dto;

import lombok.Data;

@Data
public class BalanceDTO {
  private String serialNumber;
  private double currentBalance;
}