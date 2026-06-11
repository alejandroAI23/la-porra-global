package com.porraglobal.bars.repository;

import com.porraglobal.bars.entity.Bar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarRepository extends JpaRepository<Bar, Long> {

    List<Bar> findByCityIgnoreCase(String city);

    List<Bar> findByVerifiedTrue();
}
