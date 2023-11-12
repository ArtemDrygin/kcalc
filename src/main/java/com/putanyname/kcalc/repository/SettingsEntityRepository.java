package com.putanyname.kcalc.repository;

import com.putanyname.kcalc.entity.SettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SettingsEntityRepository extends JpaRepository<SettingsEntity, Long> {
    boolean existsByUserId(UUID userId);
}