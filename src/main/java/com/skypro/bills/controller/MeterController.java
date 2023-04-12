package com.skypro.bills.controller;

import com.skypro.bills.dto.MeterDTO;

import com.skypro.bills.service.MeterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meter")
public class MeterController {
  private MeterService meterService;

  @Autowired
  public MeterController(MeterService meterService) {
    this.meterService = meterService;
  }

  @Operation(summary = "Добавление нового счетчика в БД")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Счетчик добавлен",
                  content = { @Content(mediaType = "application/json",
                          schema = @Schema(implementation = MeterDTO.class)) }),
          @ApiResponse(responseCode = "400", description = "Неверный параметр серийного номера",
                  content = @Content) })
  @PostMapping
  public MeterDTO createMeter(@RequestBody MeterDTO meterDTO) {
    return meterService.createMeter(meterDTO);
  }

  @Operation(summary = "Получение счетчика и его показания из БД")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Счетчик найден",
                  content = { @Content(mediaType = "application/json",
                          schema = @Schema(implementation = MeterDTO.class)) }),
          @ApiResponse(responseCode = "404", description = "Счетчик не найден",
                  content = @Content) })
  @GetMapping("/{serial}")
  public MeterDTO getMeter(@PathVariable("serial") String serialNumber) {
    return meterService.getMeter(serialNumber);
  }

  @Operation(summary = "Ввод новых показаний для счетчика из БД")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Баланс пополнен",
                  content = { @Content(mediaType = "application/json",
                          schema = @Schema(implementation = MeterDTO.class)) }),
          @ApiResponse(responseCode = "400", description = "Неверный параметр показания",
                  content = @Content),
          @ApiResponse(responseCode = "404", description = "Счетчик не найден",
                  content = @Content) })
  @PostMapping("/{serial}/{indication}")
  public ResponseEntity<?> newIndication(@PathVariable("serial") String serialNumber,
                                         @PathVariable("indication") int indication) {
    return ResponseEntity.ok(meterService.newIndication(serialNumber, indication));
  }
}