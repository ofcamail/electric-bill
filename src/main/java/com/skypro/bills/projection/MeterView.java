package com.skypro.bills.projection;

public interface MeterView {
    String getSerialNumber();
    Integer getLastIndication();

    double getBalance();
}