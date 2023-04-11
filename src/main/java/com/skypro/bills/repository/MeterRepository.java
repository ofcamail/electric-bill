package com.skypro.bills.repository;

import com.skypro.bills.model.ElectricityMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeterRepository extends JpaRepository<ElectricityMeter, String> {

}
