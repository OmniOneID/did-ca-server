package org.omnione.did.base.annotation;


import org.omnione.did.base.constants.ActionType;
import org.omnione.did.base.constants.ApiType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ArticleLog {
    String name() default "";
    String description() default "";
    ApiType apiType() default ApiType.ADMIN;
    String targetType() default "";
    String targetId() default "";

    ActionType actionType() default ActionType.NONE;
}
