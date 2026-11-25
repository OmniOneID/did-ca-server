package org.omnione.did.cas.v1.admin.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.omnione.did.base.aop.ApiLogInterceptor;
import org.omnione.did.base.aop.ArticleLogTargetAspect;
import org.omnione.did.base.constants.ActionType;
import org.omnione.did.base.db.domain.ApiLog;
import org.omnione.did.cas.v1.admin.dto.admin.AdminDto;
import org.omnione.did.cas.v1.admin.dto.admin.RequestAdminLoginReqDto;
import org.omnione.did.cas.v1.admin.service.JwtService;
import org.omnione.did.cas.v1.admin.service.SessionService;
import org.omnione.did.cas.v1.common.service.ApiLogService;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminAuditLogTest {
    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void loginAuditIncludesAdminTargetAndLoginId() throws Exception {
        SessionService sessionService = mock(SessionService.class);
        JwtService jwtService = mock(JwtService.class);
        ApiLogService apiLogService = mock(ApiLogService.class);
        SessionController controller = new SessionController(sessionService, jwtService);
        RequestAdminLoginReqDto login = mock(RequestAdminLoginReqDto.class);
        when(login.getLoginId()).thenReturn("admin@example.com");
        AdminDto admin = AdminDto.builder().id(1L).loginId("admin@example.com").build();
        when(sessionService.requestAdminLogin(login)).thenReturn(admin);

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/cas/admin/v1/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
        HandlerMethod handler = new HandlerMethod(controller,
                SessionController.class.getMethod("requestAdminLogin", RequestAdminLoginReqDto.class));
        ApiLogInterceptor interceptor = new ApiLogInterceptor(apiLogService);
        interceptor.preHandle(request, response, handler);

        AspectJProxyFactory factory = new AspectJProxyFactory(controller);
        factory.addAspect(new ArticleLogTargetAspect());
        ((SessionController) factory.getProxy()).requestAdminLogin(login);
        interceptor.afterCompletion(request, response, handler, null);

        org.mockito.ArgumentCaptor<ApiLog> captor = org.mockito.ArgumentCaptor.forClass(ApiLog.class);
        verify(apiLogService).saveAsync(captor.capture());
        ApiLog audit = captor.getValue();
        assertEquals("ADMIN", audit.getTargetType());
        assertEquals("admin@example.com", audit.getTargetId());
        assertEquals(ActionType.LOGIN, audit.getActionType());
        assertEquals("SUCCESS", audit.getResult());
    }
}
