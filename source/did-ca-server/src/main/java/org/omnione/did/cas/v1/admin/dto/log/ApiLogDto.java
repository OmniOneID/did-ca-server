package org.omnione.did.cas.v1.admin.dto.log;

import org.omnione.did.base.constants.ActionType;
import org.omnione.did.base.constants.ApiType;
import org.omnione.did.base.db.domain.ApiLog;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class ApiLogDto {

    private Long id;
    private String method;
    private String uri;
    private String apiName;
    private String apiDescription;
    private String requesterId;
    private Integer status;
    private String result;
    private Long durationMs;
    private String clientIp;
    private String errorCode;
    private String message;
    private ApiType apiType;
    private String targetId;
    private String targetType;
    private ActionType actionType;
    private String userAgent;
    private String createdAt;
    private String updatedAt;

    public static ApiLogDto listFromEntity(ApiLog apiLog) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ApiLogDto.builder()
                .id(apiLog.getId())
                .method(apiLog.getMethod())
                .uri(apiLog.getUri())
                .status(apiLog.getStatus())
                .result(apiLog.getResult())
                .requesterId(apiLog.getRequesterId())
                .createdAt(formatInstant(apiLog.getCreatedAt(), formatter))
                .build();
    }
    public static ApiLogDto fromEntity(ApiLog apiLog) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ApiLogDto.builder()
                .id(apiLog.getId())
                .method(apiLog.getMethod())
                .uri(apiLog.getUri())
                .status(apiLog.getStatus())
                .result(apiLog.getResult())
                .requesterId(apiLog.getRequesterId())
                .apiName(apiLog.getApiName())
                .apiDescription(apiLog.getApiDescription())
                .clientIp(apiLog.getClientIp())
                .message(apiLog.getMessage())
                .userAgent(apiLog.getUserAgent())
                .createdAt(formatInstant(apiLog.getCreatedAt(), formatter))
                .build();
    }

    private static String formatInstant(Instant instant, DateTimeFormatter formatter) {
        if (instant == null) return null;
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(formatter);
    }
}
