package com.app.common.enums.service;

import com.app.common.enums.dto.EnumsResponseDto;
import com.app.model.EnumModel;
import com.app.model.EnumValue;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnumsService {

    public List<EnumsResponseDto> scanEnums() {
        String basePackage = "com.app";

        // 모든 클래스 스캔
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

        // Enum 클래스만 필터링
        scanner.addIncludeFilter(new AssignableTypeFilter(EnumModel.class));
        return scanner.findCandidateComponents(basePackage).stream().map(beanDefinition -> {
            try {
                Class<? extends EnumModel> clazz = (Class<? extends EnumModel>) Class.forName(beanDefinition.getBeanClassName());
                ;
                List<EnumValue> enumValues = toEnumValues(clazz);
                return new EnumsResponseDto(
                    clazz.getSimpleName(),
                    enumValues
                );
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private List<EnumValue> toEnumValues(Class<? extends EnumModel> e) {
        return Arrays.stream(e.getEnumConstants()).map(EnumValue::new).toList();
    }
}
