package org.omnione.did.base.db.repository;

import org.omnione.did.base.db.domain.ApiLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApiLogRepositoryAdmin {
    Page<ApiLog> searchApiLogs(String searchKey, String searchValue, Pageable pageable);
    Page<ApiLog> searchAuditApiLogs(String searchKey, String searchValue, Pageable pageable);
}
