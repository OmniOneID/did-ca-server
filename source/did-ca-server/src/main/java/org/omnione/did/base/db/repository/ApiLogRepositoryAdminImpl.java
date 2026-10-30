/*
 * Copyright 2025 OmniOne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.omnione.did.base.db.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.omnione.did.base.constants.ApiType;
import org.omnione.did.base.constants.ActionType;
import org.omnione.did.base.db.domain.ApiLog;
import org.omnione.did.base.db.domain.QApiLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApiLogRepositoryAdminImpl implements ApiLogRepositoryAdmin {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ApiLog> searchApiLogs(String searchKey, String searchValue, Pageable pageable) {
        QApiLog q = QApiLog.apiLog;
        BooleanExpression predicate = buildPredicate(searchKey, searchValue);

        BooleanExpression base = predicate.and(q.apiType.eq(ApiType.AGENT));

        Long totalOpt = queryFactory
                .select(q.count())
                .from(q)
                .where(base)
                .fetchOne();

        long total = totalOpt == null ? 0L : totalOpt;

        List<ApiLog> results = queryFactory
                .selectFrom(q)
                .where(base)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable, q))
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<ApiLog> searchAuditApiLogs(String searchKey, String searchValue, Pageable pageable) {
        QApiLog q = QApiLog.apiLog;
        BooleanExpression predicate = buildPredicate(searchKey, searchValue);

        BooleanExpression base = predicate.and(q.apiType.eq(ApiType.ADMIN));

        Long totalOpt = queryFactory
                .select(q.count())
                .from(q)
                .where(base)
                .fetchOne();

        long total = totalOpt == null ? 0L : totalOpt;

        List<ApiLog> results = queryFactory
                .selectFrom(q)
                .where(base)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable, q))
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }



    public BooleanExpression buildPredicate(String searchKey, String searchValue) {
        QApiLog qApiLog = QApiLog.apiLog;
        BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

        if (searchKey != null && searchValue != null && !searchValue.isEmpty()) {
            predicate = switch (searchKey) {
                case "actionType" -> predicate.and(qApiLog.actionType.eq(ActionType.valueOf(searchValue)));
                case "uri" -> predicate.and(qApiLog.uri.contains(searchValue));
                case "result" -> predicate.and(qApiLog.result.eq(searchValue));
                case "apiName" -> predicate.and(qApiLog.apiName.eq(searchValue));
                case "targetType" -> predicate.and(qApiLog.targetType.eq(searchValue));
                case "requesterId" -> predicate.and(qApiLog.requesterId.eq(searchValue));
                default -> predicate.and(Expressions.FALSE);
            };
        }

        return predicate;
    }

    public OrderSpecifier<?>[] getOrderSpecifier(Pageable pageable, QApiLog qApiLog) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (!pageable.getSort().isSorted()) {
            orders.add(new OrderSpecifier<>(Order.DESC, qApiLog.createdAt));
        }

        for (Sort.Order order: pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "actionType":
                    orders.add(new OrderSpecifier<>(direction, qApiLog.actionType));
                    break;
                case "status":
                    orders.add(new OrderSpecifier<>(direction, qApiLog.status));
                    break;
                case "apiName":
                    orders.add(new OrderSpecifier<>(direction, qApiLog.apiName));
                    break;
                case "targetType":
                    orders.add(new OrderSpecifier<>(direction, qApiLog.targetType));
                    break;
                case "requesterId":
                    orders.add(new OrderSpecifier<>(direction, qApiLog.requesterId));
                    break;
                default:
                    orders.add(new OrderSpecifier<>(Order.ASC, qApiLog.createdAt));
                    break;
            }
        }
        return orders.toArray(new OrderSpecifier[0]);
    }

}
