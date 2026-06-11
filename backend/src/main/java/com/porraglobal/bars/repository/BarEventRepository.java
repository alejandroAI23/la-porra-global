package com.porraglobal.bars.repository;

import com.porraglobal.bars.entity.BarEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface BarEventRepository extends JpaRepository<BarEvent, Long> {

    List<BarEvent> findByBarIdOrderByStartsAtAsc(Long barId);

    List<BarEvent> findByBarIdAndStartsAtAfterOrderByStartsAtAsc(Long barId, Instant after);
}
