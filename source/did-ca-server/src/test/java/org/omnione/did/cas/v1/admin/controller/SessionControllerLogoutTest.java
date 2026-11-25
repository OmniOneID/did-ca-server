package org.omnione.did.cas.v1.admin.controller;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.omnione.did.cas.v1.admin.dto.admin.AdminDto;
import org.omnione.did.cas.v1.admin.service.JwtService;
import org.omnione.did.cas.v1.admin.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SessionControllerLogoutTest {
    @Test
    void gsonReadsRefreshTokenRequest() {
        SessionController.RefreshReqDto request = new Gson().fromJson(
                "{\"refreshToken\":\"refresh\"}", SessionController.RefreshReqDto.class);
        assertEquals("refresh", request.refreshToken());
    }

    @SuppressWarnings("unchecked")
    @Test
    void repeatedLogoutSucceedsWithoutRevokingAgain() {
        JwtService jwt = mock(JwtService.class);
        Jws<Claims> token = mock(Jws.class);
        Claims claims = mock(Claims.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jwt.parse("refresh")).thenReturn(token);
        when(jwt.isRefreshToken(token)).thenReturn(true);
        when(token.getPayload()).thenReturn(claims);
        when(claims.getSubject()).thenReturn("7");
        when(jwt.isCurrentSession(token)).thenReturn(false);
        SessionService sessionService = mock(SessionService.class);
        AdminDto admin = mock(AdminDto.class);
        when(admin.getLoginId()).thenReturn("admin@opendid.omnione.com");
        when(sessionService.requestAdminLoginById(7L)).thenReturn(admin);

        SessionController controller = new SessionController(sessionService, jwt);
        assertDoesNotThrow(() -> controller.logout(new SessionController.RefreshReqDto("refresh"), request));
        verify(jwt, never()).revokeAllSessions(7L);
        verify(request).setAttribute("targetId", "admin@opendid.omnione.com");
        verify(request).setAttribute("requesterId", "admin@opendid.omnione.com");
    }

    @SuppressWarnings("unchecked")
    @Test
    void nonRefreshTokenStillFails() {
        JwtService jwt = mock(JwtService.class);
        Jws<Claims> token = mock(Jws.class);
        when(jwt.parse("invalid")).thenReturn(token);
        when(jwt.isRefreshToken(token)).thenReturn(false);

        SessionController controller = new SessionController(mock(SessionService.class), jwt);
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> controller.logout(new SessionController.RefreshReqDto("invalid"), mock(HttpServletRequest.class)));
        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatusCode());
    }
}
