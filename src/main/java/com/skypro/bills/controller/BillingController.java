package com.skypro.bills.controller;

import com.skypro.bills.dto.BalanceDTO;
import com.skypro.bills.dto.MeterDTO;
import com.skypro.bills.dto.PaymentDTO;
import com.skypro.bills.model.ElectricityMeter;
import com.skypro.bills.model.Indication;
import com.skypro.bills.repository.MeterRepository;

import java.util.Comparator;

import com.skypro.bills.service.MeterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing")

public class BillingController {
  private final MeterService meterService;

  @Autowired
  public BillingController(MeterService meterService) {
    this.meterService = meterService;
  }

  @Operation(summary = "Пополнение баланса для счетчика")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Счетчик найден",
                  content = { @Content(mediaType = "application/json",
                          schema = @Schema(implementation = BalanceDTO.class)) }),
          @ApiResponse(responseCode = "400", description = "Неверный параметр пополнения суммы",
                  content = @Content),
          @ApiResponse(responseCode = "404", description = "Счетчик не найден",
                  content = @Content) })
  @PostMapping("/{serial}")
  public ResponseEntity<?> topUpBalance(@PathVariable("serial") String serialNumber,
                                        @RequestBody PaymentDTO paymentDTO) {
    return ResponseEntity.ok(meterService.topUpBalance(serialNumber, paymentDTO));
  }

  @Operation(summary = "Получение актуального баланса")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Счетчик найден",
                  content = { @Content(mediaType = "application/json",
                          schema = @Schema(implementation = BalanceDTO.class)) }),
          @ApiResponse(responseCode = "404", description = "Счетчик не найден",
                  content = @Content) })
  @GetMapping("/{serial}")
  public ResponseEntity<?> readBalance(@PathVariable("serial") String serialNumber) {
    return ResponseEntity.ok(meterService.readBalance(serialNumber));
  }
}