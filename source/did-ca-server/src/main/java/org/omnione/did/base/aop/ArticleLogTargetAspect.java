package org.omnione.did.base.aop;


import org.omnione.did.base.annotation.ArticleLog;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class ArticleLogTargetAspect {

    private final org.springframework.expression.ExpressionParser parser = new org.springframework.expression.spel.standard.SpelExpressionParser();

    @Around("@annotation(articleLog)")
    public Object captureTarget(ProceedingJoinPoint pjp, ArticleLog articleLog) throws Throwable {
        // SpEL을 통해 메서드 인자에서 targetId 평가
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Method method = sig.getMethod();
        var ctx = new org.springframework.expression.spel.support.StandardEvaluationContext();

        // 파라미터명을 SpEL 변수로 바인딩
        String[] paramNames = sig.getParameterNames();
        Object[] args = pjp.getArgs();
        for (int i = 0; i < paramNames.length; i++) {
            ctx.setVariable(paramNames[i], args[i]);
        }

        String targetId = null;
        if (!articleLog.targetId().isBlank()) {
            var expr = parser.parseExpression(articleLog.targetId());
            Object val = expr.getValue(ctx);
            targetId = (val != null) ? String.valueOf(val) : null;
        }

        var reqAttrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (reqAttrs != null) {
            var req = reqAttrs.getRequest();
            req.setAttribute("auditEnabled", true);
            if (!articleLog.targetType().isBlank()) {
                req.setAttribute("targetType", articleLog.targetType());
            }
            if (targetId != null) {
                req.setAttribute("targetId", targetId);
            }
        }

        return pjp.proceed();
    }
}
