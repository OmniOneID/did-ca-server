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

@Builder
@Getter
public class AuditApiLogDto {

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

    public static AuditApiLogDto listFromEntity(ApiLog apiLog) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return AuditApiLogDto.builder()
                .id(apiLog.getId())
                .actionType(apiLog.getActionType())
                .targetType(apiLog.getTargetType())
                .targetId(apiLog.getTargetId())
                .result(apiLog.getResult())
                .requesterId(apiLog.getRequesterId())
                .createdAt(formatInstant(apiLog.getCreatedAt(), formatter))
                .build();
    }
    public static AuditApiLogDto fromEntity(ApiLog apiLog) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return AuditApiLogDto.builder()
                .id(apiLog.getId())
                .actionType(apiLog.getActionType())
                .status(apiLog.getStatus())
                .targetType(apiLog.getTargetType())
                .targetId(apiLog.getTargetId())
                .result(apiLog.getResult())
                .requesterId(apiLog.getRequesterId())
                .clientIp(apiLog.getClientIp())
                .apiName(apiLog.getApiName())
                .apiDescription(apiLog.getApiDescription())
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
