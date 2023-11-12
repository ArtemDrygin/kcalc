package com.putanyname.kcalc;

import com.putanyname.kcalc.entity.SettingsEntity;
import com.putanyname.kcalc.repository.SettingsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class KCalcApplicationTests {
    @Autowired
    SettingsRepository settingsRepository;

    @Test
    void insertDefaultSettings() {
        var userId = UUID.randomUUID();
        var isSettingsExist = settingsRepository.existsByUserId(userId);
        if (!isSettingsExist) {
            var settingsEntity = new SettingsEntity();
            settingsEntity.setUserId(userId);
            settingsRepository.save(settingsEntity);
        }
    }

}
