package com.app.common.enums.dto;

import com.app.model.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EnumsResponseDto {

    @Schema(description = "enum 이름", example = "VehicleModelType")
    private String enumClassName;

    private List<EnumValue> enumValues;
}
