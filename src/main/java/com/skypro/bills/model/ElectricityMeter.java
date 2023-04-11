package com.skypro.bills.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class ElectricityMeter {
  @Id
  private String serialNumber;
  @OneToMany(mappedBy = "electricityMeter", cascade = CascadeType.ALL)
  private List<Indication> indications;
  private double balance;

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public List<Indication> getIndications() {
    return indications;
  }

  public void setIndications(List<Indication> indications) {
    this.indications = indications;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }
}
