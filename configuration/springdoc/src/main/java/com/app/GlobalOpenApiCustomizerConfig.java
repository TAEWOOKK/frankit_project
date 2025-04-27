package com.app;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;

@Configuration
@Slf4j
public class GlobalOpenApiCustomizerConfig {

    @Bean
    public OpenApiCustomizer globalApiCustomizer(RequestMappingHandlerMapping handlerMapping) {
        return openApi -> {
            log.info("글로벌 OpenApiCustomizer 실행 시작");
            Paths paths = openApi.getPaths();
            // HandlerMapping을 통해 API의 매핑 정보와 HandlerMethod를 가져옴
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

            // 각 매핑 정보에 대해 처리
            handlerMethods.forEach((mappingInfo, handlerMethod) -> {
                String path = extractPath(mappingInfo);
                if (path == null) {
                    return;
                }
                // Swagger OpenAPI에 등록된 경로와 일치하는지 확인
                if (!paths.containsKey(path)) {
                    log.debug("OpenAPI에 해당 경로가 없음: {}", path);
                    return;
                }
                PathItem pathItem = paths.get(path);


                mappingInfo.getMethodsCondition().getMethods().forEach(requestMethod -> {
                    switch (requestMethod) {
                        case GET:
                            updateOperation(pathItem.getGet(), handlerMethod, path, "GET");
                            break;
                        case POST:
                            updateOperation(pathItem.getPost(), handlerMethod, path, "POST");
                            break;
                        case PUT:
                            updateOperation(pathItem.getPut(), handlerMethod, path, "PUT");
                            break;
                        case PATCH:
                            updateOperation(pathItem.getPatch(), handlerMethod, path, "PATCH");
                            break;
                        case DELETE:
                            updateOperation(pathItem.getDelete(), handlerMethod, path, "DELETE");
                            break;
                        default:
                            break;
                    }
                });
            });
        };
    }

    /**
     * 매핑 정보에서 URL 경로 문자열을 추출합니다.
     * Spring Boot 2.6 이상에서는 PathPatternsCondition을 우선 사용합니다.
     */
    private String extractPath(RequestMappingInfo mappingInfo) {
        // 먼저 PathPatternsCondition 사용 (Spring Boot 2.6+)
        if (mappingInfo.getPathPatternsCondition() != null &&
            !mappingInfo.getPathPatternsCondition().getPatternValues().isEmpty()) {
            return mappingInfo.getPathPatternsCondition().getPatternValues().iterator().next();
        }
        // 기존 방식 fallback
        if (mappingInfo.getPatternsCondition() != null &&
            !mappingInfo.getPatternsCondition().getPatterns().isEmpty()) {
            return mappingInfo.getPatternsCondition().getPatterns().iterator().next();
        }
        return null;
    }

    /**
     * 특정 Operation(HTTP 메서드)에 대해 RequestDTO의 enum 정보를 읽어와서
     * description을 업데이트합니다.
     */
    private void updateOperation(Operation operation, HandlerMethod handlerMethod, String path, String method) {
        if (operation == null) return;

        String originalDescription = operation.getDescription() != null ? operation.getDescription() + "\n\n" : "";

        Class<?> dtoClass = findRequestDto(handlerMethod);
        if (dtoClass != null && !dtoClass.isEnum()) {

            String enumInfo = generateEnumDescription(dtoClass);
            operation.setDescription(originalDescription + enumInfo);
        }
    }

    /**
     * HandlerMethod의 파라미터 중, 특정 패키지(예: "com.app")에 속하는 클래스를 RequestDTO로 간주합니다.
     */
    private Class<?> findRequestDto(HandlerMethod handlerMethod) {

        return Arrays.stream(handlerMethod.getMethod().getParameters())
            .map(Parameter::getType)
            .filter(type -> type.getPackageName().startsWith("com.app"))
            .findFirst()
            .orElse(null);
    }

    /**
     * 주어진 DTO 클래스의 필드 중 enum 타입인 것들의 정보를 정리하여 문자열로 반환합니다.
     * 각 enum 필드에 대해, 필드 이름과 각 enum 상수의 key 및 설명(getDescription() 결과)을 나열합니다.
     */
    private String generateEnumDescription(Class<?> dtoClass) {
        StringBuilder sb = new StringBuilder();
        // DTO의 모든 필드를 순회
        for (Field field : dtoClass.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            // 단순 enum 타입 (또는 List, Set 등 제네릭이 enum인 경우는 추가 처리가 필요)
            if (fieldType.isEnum()) {
                sb.append("✅ **").append(field.getName()).append(" 설명**\n");
                Object[] enumConstants = fieldType.getEnumConstants();
                if (enumConstants != null) {
                    for (Object constant : enumConstants) {
                        String key = constant.toString();
                        String desc = extractEnumDescription(constant);
                        sb.append("- `").append(key).append("`: ").append(desc).append("\n");
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 주어진 enum 상수에서 getDescription() 메서드를 호출하여 설명을 반환하려 시도합니다.
     * 해당 메서드가 없거나 호출 실패 시, 빈 문자열을 반환합니다.
     */
    private String extractEnumDescription(Object enumConstant) {
        try {
            Method method = enumConstant.getClass().getMethod("getDescription");
            Object result = method.invoke(enumConstant);
            return result != null ? result.toString() : "";
        } catch (Exception e) {
            // getDescription()이 없으면 빈 문자열 반환
            return "";
        }
    }
}