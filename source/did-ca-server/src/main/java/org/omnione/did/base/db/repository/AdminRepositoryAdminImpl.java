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
import lombok.RequiredArgsConstructor;
import org.omnione.did.base.db.constant.AdminRole;
import org.omnione.did.base.db.domain.Admin;
import org.omnione.did.base.db.domain.QAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryAdminImpl implements AdminRepositoryAdmin {
    private final JPAQueryFactory queryFactory;

    public Page<Admin> searchAdmins(String searchKey, String searchValue, Pageable pageable) {
        QAdmin qAdmin = QAdmin.admin;
        BooleanExpression predicate = buildPredicate(searchKey, searchValue);

        long total = queryFactory
                .select(qAdmin.count())
                .from(qAdmin)
                .where(predicate)
                .fetchOne();

        List<Admin> results = queryFactory
                    .selectFrom(qAdmin)
                    .where(predicate)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(getOrderSpecifier(pageable, qAdmin))
                    .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    public BooleanExpression buildPredicate(String searchKey, String searchValue) {
        QAdmin qAdmin = QAdmin.admin;
        BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

        if (searchKey != null && searchValue != null && !searchValue.isEmpty()) {
            switch (searchKey) {
                case "loginId":
                    predicate = predicate.and(qAdmin.loginId.eq(searchValue));
                    break;
                case "role":
                    predicate = predicate.and(qAdmin.role.eq(AdminRole.valueOf(searchValue)));
                    break;
                default:
                    predicate = predicate.and(Expressions.FALSE);
            }
        }

        return predicate;
    }

    public OrderSpecifier<?>[] getOrderSpecifier(Pageable pageable, QAdmin qAdmin) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (!pageable.getSort().isSorted()) {
            orders.add(new OrderSpecifier<>(Order.ASC, qAdmin.createdAt));
        }

        for (Sort.Order order: pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "loginId":
                    orders.add(new OrderSpecifier<>(direction, qAdmin.loginId));
                    break;
                case "role":
                    orders.add(new OrderSpecifier<>(direction, qAdmin.role));
                    break;
                default:
                    orders.add(new OrderSpecifier<>(Order.ASC, qAdmin.createdAt));
                    break;
            }
        }
        return orders.toArray(new OrderSpecifier[0]);
    }

}
