package org.omnione.did.base.db.repository;

import org.omnione.did.base.db.domain.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long>, QuerydslPredicateExecutor<ApiLog>, ApiLogRepositoryAdmin {

}
