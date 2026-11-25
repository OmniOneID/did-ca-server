/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.cas.v1.admin.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.omnione.did.base.property.JwtProperty;
import org.omnione.did.base.db.repository.AdminRepository;
import org.omnione.did.cas.v1.admin.dto.admin.AdminDto;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

/**
 * Service for creating and parsing admin JWT tokens (HMAC-SHA256).
 * These are separate from OP server tokens and are used only for the admin console.
 */
@RequiredArgsConstructor
@Component
public class JwtService {

    private final JwtProperty props;
    private final AdminRepository adminRepository;

    private Long sessionVersion(Long adminId) {
        return adminRepository.findById(adminId)
                .map(admin -> admin.getSessionVersion())
                .orElseThrow(() -> new IllegalArgumentException("Unknown administrator"));
    }

    private Key key() {
        return Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Creates a short-lived access token for the given admin.
     */
    public String createAccessToken(AdminDto admin) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(admin.getId().toString())
                .claim("typ", "access")
                .claim("sessionVersion", sessionVersion(admin.getId()))
                .claim("loginId", admin.getLoginId())
                .claim("role", admin.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(props.getAccessTtlSeconds())))
                .signWith(key())
                .compact();
    }

    /**
     * Creates a long-lived refresh token for the given admin ID.
     */
    public String createRefreshToken(Long adminId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(adminId.toString())
                .claim("typ", "refresh")
                .claim("sessionVersion", sessionVersion(adminId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(props.getRefreshTtlSeconds())))
                .signWith(key())
                .compact();
    }

    /**
     * Parses and verifies the given JWT string.
     */
    public Jws<Claims> parse(String jwt) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt);
    }

    /**
     * Returns true if the parsed JWT is a refresh token.
     */
    public boolean isRefreshToken(Jws<Claims> jws) {
        return "refresh".equals(jws.getPayload().get("typ", String.class));
    }

    public boolean isCurrentSession(Jws<Claims> token) {
        Long adminId = Long.valueOf(token.getPayload().getSubject());
        Number version = token.getPayload().get("sessionVersion", Number.class);
        return version != null && version.longValue() == sessionVersion(adminId);
    }

    public void revokeAllSessions(Long adminId) {
        if (adminRepository.revokeSessions(adminId) != 1) {
            throw new IllegalArgumentException("Unknown administrator");
        }
    }
}
