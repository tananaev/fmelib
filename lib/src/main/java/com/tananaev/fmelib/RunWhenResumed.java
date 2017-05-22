package com.tananaev.fmelib;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Target({METHOD})
@Retention(CLASS)
public @interface RunWhenResumed {
}
