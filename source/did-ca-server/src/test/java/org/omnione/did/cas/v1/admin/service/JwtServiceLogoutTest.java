package org.omnione.did.cas.v1.admin.service;

import org.junit.jupiter.api.Test;
import org.omnione.did.base.db.constant.AdminRole;
import org.omnione.did.base.db.domain.Admin;
import org.omnione.did.base.db.repository.AdminRepository;
import org.omnione.did.base.property.JwtProperty;
import org.omnione.did.cas.v1.admin.dto.admin.AdminDto;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class JwtServiceLogoutTest {
    @Test
    void logoutRevokesAccessAndRefreshTokens() {
        AdminRepository admins = mock(AdminRepository.class);
        AtomicLong version = new AtomicLong();
        when(admins.findById(eq(1L))).thenAnswer(invocation -> Optional.of(
                Admin.builder().id(1L).sessionVersion(version.get()).build()));
        when(admins.revokeSessions(eq(1L))).thenAnswer(invocation -> {
            version.incrementAndGet();
            return 1;
        });

        JwtProperty property = new JwtProperty();
        property.setSecret("opendidAdminJWTsecretPasswordSample");
        property.setAccessTtlSeconds(1800);
        property.setRefreshTtlSeconds(1209600);
        JwtService jwt = new JwtService(property, admins);
        AdminDto admin = AdminDto.builder().id(1L).loginId("admin")
                .role(AdminRole.ROOT).build();

        var access = jwt.parse(jwt.createAccessToken(admin));
        var refresh = jwt.parse(jwt.createRefreshToken(1L));
        assertTrue(jwt.isCurrentSession(access));
        assertTrue(jwt.isCurrentSession(refresh));
        assertTrue(jwt.isRefreshToken(refresh));

        jwt.revokeAllSessions(1L);
        assertFalse(jwt.isCurrentSession(access));
        assertFalse(jwt.isCurrentSession(refresh));
    }
}
