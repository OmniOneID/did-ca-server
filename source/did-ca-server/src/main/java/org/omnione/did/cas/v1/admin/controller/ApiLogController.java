package org.omnione.did.cas.v1.admin.controller;

import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.cas.v1.admin.dto.log.ApiLogDto;
import org.omnione.did.cas.v1.admin.dto.log.AuditApiLogDto;
import org.omnione.did.cas.v1.common.service.ApiLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = UrlConstant.Cas.ADMIN_V1 + "/logs")
public class ApiLogController {

    private final ApiLogService apiLogService;
    /**
     * Searches the list of API Log with optional filtering and pagination.
     *
     * @param searchKey the key to search by (e.g., name)
     * @param searchValue the value to search for
     * @param pageable the pagination information
     * @return a page of logs
     */
    @GetMapping
    public PageImpl<ApiLogDto> searchApiLogList(String searchKey, String searchValue, Pageable pageable) {
        return apiLogService.searchApiLogList(searchKey, searchValue, pageable);
    }
  /**
     * Searches the list of Audit API Log with optional filtering and pagination.
     *
     * @param searchKey the key to search by (e.g., name)
     * @param searchValue the value to search for
     * @param pageable the pagination information
     * @return a page of logs
     */
    @GetMapping("/audit")
    public PageImpl<AuditApiLogDto> searchAuditApiLogList(String searchKey, String searchValue, Pageable pageable) {
        return apiLogService.searchAuditApiLogList(searchKey, searchValue, pageable);
    }

    /**
     * Retrieves a API Log by its ID.
     *
     * @param id the ID of the namespace
     * @return the log details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiLogDto> getApiLogInfo(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(apiLogService.getLogById(id));
    }

    /**
     * Retrieves a Audit API Log by its ID.
     *
     * @param id the ID of the namespace
     * @return the log details
     */
    @GetMapping("/audit/{id}")
    public ResponseEntity<AuditApiLogDto> getAuditApiLogInfo(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(apiLogService.getAuditLogById(id));
    }
}
