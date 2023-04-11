package com.skypro.bills.controller;


import com.skypro.bills.dto.BalanceDTO;
import com.skypro.bills.dto.PaymentDTO;
import com.skypro.bills.model.ElectricityMeter;
import com.skypro.bills.model.Indication;
import com.skypro.bills.repository.MeterRepository;
import java.util.Comparator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing")
//TODO: Хорошо бы задокументировать АПИ :-(

public class BillingController {

  //TODO: Стоит сделать это свойство конфигурируемым через application.properties
  private final static double priceForKW = 1.05;
  private final MeterRepository meterRepository;

  public BillingController(MeterRepository meterRepository) {
    this.meterRepository = meterRepository;
  }

  @PostMapping("/{serial}")
  public ResponseEntity<?> pay(@PathVariable("serial") String serial,
      @RequestBody PaymentDTO paymentDTO) {
    ElectricityMeter electricityMeter = meterRepository.findById(serial).get();
    if (paymentDTO.getAmount() <= 0) {
      return ResponseEntity.badRequest().body("Сумма платежа меньше или равна 0");
    }
    electricityMeter.setBalance(electricityMeter.getBalance() + paymentDTO.getAmount());
    meterRepository.save(electricityMeter);
    //Подсчитываем баланс - берем последние показания счетчика и умножаем на стоимость,
    // далее вычитаем из положенных на счетчик средств
    Indication indications = electricityMeter.getIndications().stream()
        .max(Comparator.comparing(Indication::getSendingDate))
        .orElse(new Indication());
    double spent = indications.getIndication() * priceForKW;
    double balance = electricityMeter.getBalance() - spent;

    return ResponseEntity.ok(new BalanceDTO(serial, balance));
  }

  @GetMapping("/{serial}")
  public ResponseEntity<?> pay(@PathVariable("serial") String serial) {
    ElectricityMeter electricityMeter = meterRepository.findById(serial).get();
    //Подсчитываем баланс - берем последние показания счетчика и умножаем на стоимость,
    // далее вычитаем из положенных на счетчик средств
    Indication indications = electricityMeter.getIndications().stream()
        .max(Comparator.comparing(Indication::getSendingDate))
        .orElse(new Indication());
    double spent = indications.getIndication() * priceForKW;
    double balance = electricityMeter.getBalance() - spent;

    return ResponseEntity.ok(new BalanceDTO(serial, balance));
  }
}
