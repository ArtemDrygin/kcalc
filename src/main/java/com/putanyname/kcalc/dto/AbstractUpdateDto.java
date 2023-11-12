package com.putanyname.kcalc.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class AbstractUpdateDto {
    @NotNull
    private Long id;
}
