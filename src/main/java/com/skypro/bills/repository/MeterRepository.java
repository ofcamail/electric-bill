package com.skypro.bills.repository;

import com.skypro.bills.model.ElectricityMeter;
import com.skypro.bills.projection.MeterView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MeterRepository extends JpaRepository<ElectricityMeter, String> {
    @Query(value = "select serial_number AS serialNumber, indication as lastIndication, balance as balance\n" +
            "from electricity_meter\n" +
            "      left join indication i on electricity_meter.serial_number = i.electricity_meter_serial_number\n" +
            "where serial_number like ?1 \n" +
            "order by indication desc\n" +
            "limit 1", nativeQuery = true)
    MeterView getMeterView(String serialNumber);
}