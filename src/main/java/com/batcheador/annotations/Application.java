package com.batcheador.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Application {
	String name();

	String command() default "ffmpeg";
}
