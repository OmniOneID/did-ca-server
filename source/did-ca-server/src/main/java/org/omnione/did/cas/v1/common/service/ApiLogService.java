package org.omnione.did.cas.v1.common.service;

import org.omnione.did.base.db.domain.ApiLog;
import org.omnione.did.base.db.repository.ApiLogRepository;
import org.omnione.did.cas.v1.admin.dto.log.ApiLogDto;
import org.omnione.did.cas.v1.admin.dto.log.AuditApiLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiLogService {

    private final ApiLogRepository apiLogRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAsync(ApiLog log) {
        apiLogRepository.save(log);
    }

    public PageImpl<ApiLogDto> searchApiLogList(String searchKey, String searchValue, Pageable pageable) {
        Page<ApiLog> entityPage = apiLogRepository.searchApiLogs(searchKey, searchValue, pageable);

        List<ApiLogDto> apiLogDtos = entityPage.getContent().stream()
                .map(ApiLogDto::listFromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(apiLogDtos, pageable, entityPage.getTotalElements());
    }

    public PageImpl<AuditApiLogDto> searchAuditApiLogList(String searchKey, String searchValue, Pageable pageable) {
        Page<ApiLog> entityPage = apiLogRepository.searchAuditApiLogs(searchKey, searchValue, pageable);

        List<AuditApiLogDto> apiLogDtos = entityPage.getContent().stream()
                .map(AuditApiLogDto::listFromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(apiLogDtos, pageable, entityPage.getTotalElements());
    }

    public ApiLogDto getLogById(Long id) {
        ApiLog apiLog = apiLogRepository.findById(id)
                .orElseThrow();
        return ApiLogDto.fromEntity(apiLog);
    }

    public AuditApiLogDto getAuditLogById(Long id) {
        ApiLog apiLog = apiLogRepository.findById(id)
                .orElseThrow();
        return AuditApiLogDto.fromEntity(apiLog);
    }
}