package com.skypro.bills.service;

import com.skypro.bills.dto.BalanceDTO;
import com.skypro.bills.dto.MeterDTO;
import com.skypro.bills.dto.PaymentDTO;
import com.skypro.bills.exception.BadMeterParamException;
import com.skypro.bills.exception.MeterNotFoundException;
import com.skypro.bills.model.ElectricityMeter;
import com.skypro.bills.model.Indication;
import com.skypro.bills.projection.MeterView;
import com.skypro.bills.repository.IndicationRepository;
import com.skypro.bills.repository.MeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;

@Service
@PropertySource("classpath:application.properties")
public class MeterService {
    private final MeterRepository meterRepository;
    private final IndicationRepository indicationRepository;
    private final double priceForKW;

    @Autowired
    public MeterService(MeterRepository meterRepository, IndicationRepository indicationRepository, @Value("#{T(Double).parseDouble('${meter-service.price-for-kw}')}")
    double priceForKW) {
        this.meterRepository = meterRepository;
        this.indicationRepository = indicationRepository;
        this.priceForKW = priceForKW;
    }

    public MeterDTO createMeter(MeterDTO meterDTO) {
        validateParam(meterDTO);
        meterRepository.save(meterDTO.toElectricityMeter());
        return meterDTO;
    }

    private void validateParam(MeterDTO meterDTO) {
        if (StringUtils.isBlank(meterDTO.getSerialNumber()))
            throw new BadMeterParamException();
    }

    public MeterDTO getMeter(String serialNumber) {
        MeterView meterView = meterRepository.getMeterView(serialNumber);
        validateParamNull(meterView);
        MeterDTO meterDTO = new MeterDTO();
        meterDTO.setSerialNumber(meterView.getSerialNumber());
        meterDTO.setLastIndication(meterView.getLastIndication() == null ? 0 : meterView.getLastIndication());
        return meterDTO;
    }

    private void validateParamNull(Object object) {
        if (object == null)
            throw new MeterNotFoundException();
    }

    public MeterDTO newIndication(String serialNumber, int indication) {
        validateIndication(indication);

        MeterView meterView = meterRepository.getMeterView(serialNumber);
        validateParamNull(meterView);

        int lastIndication = Optional.ofNullable(meterView.getLastIndication()).orElse(0);
        validateIndication(indication, lastIndication);

        Indication indicationTmp = new Indication();
        indicationTmp.setIndication(indication);
        indicationTmp.setSendingDate(Instant.now());

        ElectricityMeter electricityMeter = new ElectricityMeter();
        electricityMeter.setSerialNumber(meterView.getSerialNumber());
        indicationTmp.setElectricityMeter(electricityMeter);
        indicationRepository.save(indicationTmp);

        MeterDTO meterDTO = new MeterDTO();
        meterDTO.setSerialNumber(meterView.getSerialNumber());
        meterDTO.setLastIndication(indicationTmp.getIndication());
        return meterDTO;
    }

    private void validateIndication(int indication, int lastIndication) {
        if (indication < lastIndication) {
            throw new BadMeterParamException();
        }
    }

    private void validateIndication(int indication) {
        if (indication < 0) {
            throw new BadMeterParamException();
        }
    }

    public BalanceDTO topUpBalance(String serialNumber, PaymentDTO paymentDTO) {
        if (paymentDTO.getAmount() <= 0) throw new BadMeterParamException();

        MeterView meterView = meterRepository.getMeterView(serialNumber);
        validateParamNull(meterView);

        double currentBalance = meterView.getBalance() + paymentDTO.getAmount();
        double spent = Optional.ofNullable(meterView.getLastIndication()).orElse(0) * priceForKW;
        double balance = currentBalance - spent;

        ElectricityMeter electricityMeter = new ElectricityMeter();
        electricityMeter.setSerialNumber(meterView.getSerialNumber());
        electricityMeter.setBalance(currentBalance);
        meterRepository.save(electricityMeter);

        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setSerialNumber(serialNumber);
        balanceDTO.setCurrentBalance(balance);
        return balanceDTO;
    }

    public BalanceDTO readBalance(String serialNumber) {
        MeterView meterView = meterRepository.getMeterView(serialNumber);
        validateParamNull(meterView);

        double spent = Optional.ofNullable(meterView.getLastIndication()).orElse(0) * priceForKW;
        double balance = meterView.getBalance() - spent;

        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setSerialNumber(serialNumber);
        balanceDTO.setCurrentBalance(balance);

        return balanceDTO;
    }
}