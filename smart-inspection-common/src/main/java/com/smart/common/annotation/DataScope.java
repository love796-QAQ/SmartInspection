package com.smart.common.annotation;

import java.lang.annotation.*;

/**
 * Data Scope Filtering Annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    /**
     * Alias for department table
     */
    public String deptAlias() default "";

    /**
     * Alias for user table
     */
    public String userAlias() default "";
}
