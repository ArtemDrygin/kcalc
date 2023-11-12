package com.putanyname.kcalc.repository;

import com.putanyname.kcalc.entity.CalorieEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalorieEventRepository extends JpaRepository<CalorieEventEntity, Long> {
}