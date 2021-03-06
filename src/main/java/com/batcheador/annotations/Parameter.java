package com.batcheador.annotations;

import com.batcheador.utils.ParameterType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
	String flags() default "";

	String label();

	ParameterType type();

	String prefix() default  "";

	String suffix() default  "";
}
