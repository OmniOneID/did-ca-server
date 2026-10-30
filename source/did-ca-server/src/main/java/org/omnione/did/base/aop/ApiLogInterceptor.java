package org.omnione.did.base.aop;

import org.omnione.did.base.annotation.ArticleLog;
import org.omnione.did.base.constants.ActionType;
import org.omnione.did.base.constants.ApiType;
import org.omnione.did.base.db.domain.ApiLog;
import org.omnione.did.cas.v1.common.service.ApiLogService;
import org.omnione.did.base.constants.LogAttrs;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Slf4j
@Component
public class ApiLogInterceptor implements HandlerInterceptor {
    private final ApiLogService apiLogService;
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        if (handler instanceof HandlerMethod hm) {
            ArticleLog anno = hm.getMethodAnnotation(ArticleLog.class);
            if (anno != null) {
                req.setAttribute("auditEnabled", true);
                req.setAttribute("articleLogName", anno.name());
                req.setAttribute("articleLogDesc", anno.description());
                req.setAttribute("apiLogType", anno.apiType());
                req.setAttribute("startAtNs", System.nanoTime());
                req.setAttribute("actionType", anno.actionType());
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ignored) {
        if (!Boolean.TRUE.equals(req.getAttribute("auditEnabled"))) return;

        Object start = req.getAttribute("startAtNs");
        if (!(start instanceof Long startAtNs)) return;
        long durationMs = (System.nanoTime() - startAtNs) / 1_000_000;

        ApiLog log = ApiLog.builder()
                .method(req.getMethod())
                .uri(req.getRequestURI())
                .apiName((String) req.getAttribute("articleLogName"))
                .apiDescription((String) req.getAttribute("articleLogDesc"))
                .requesterId(resolveRequesterId())
                .status(res.getStatus())
                .result(res.getStatus() < 400 ? "SUCCESS" : "FAIL")
                .durationMs(durationMs)
                .clientIp(extractClientIp(req))
                .errorCode((String) req.getAttribute(LogAttrs.ERROR_CODE))
                .message(safeMsg((String) req.getAttribute(LogAttrs.ERROR_MSG)))
                .apiType((ApiType) req.getAttribute("apiLogType"))
                .targetType((String) req.getAttribute("targetType"))
                .targetId((String) req.getAttribute("targetId"))
                .actionType((ActionType) req.getAttribute("actionType"))
                .userAgent(req.getHeader("User-Agent"))
                .build();

        apiLogService.saveAsync(log);
    }

    private static String extractClientIp(HttpServletRequest req) {
        String h = req.getHeader("X-Forwarded-For");
        if (h != null && !h.isBlank()) return h.split(",")[0].trim();
        h = req.getHeader("X-Real-IP");
        return (h == null || h.isBlank()) ? req.getRemoteAddr() : h;
    }

    private static String safeMsg(String msg) {
        if (msg == null) return null;
        // 토큰/민감정보 마스킹 등 필요 시 여기에서 처리
        return msg.length() > 1000 ? msg.substring(0, 1000) : msg;
    }

    private static String resolveRequesterId() {
        var ctx = SecurityContextHolder.getContext();
        if (ctx == null) return null;
        var auth = ctx.getAuthentication();
        if (auth == null) return null;


        return auth.getName();
    }
}
