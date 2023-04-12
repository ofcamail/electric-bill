package com.skypro.bills.repository;

import com.skypro.bills.model.Indication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndicationRepository extends JpaRepository<Indication,Integer> {
}