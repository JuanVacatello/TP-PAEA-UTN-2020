package com.annotations;

public @interface Parameter {
	
	String flags() default "";

	String type();

	String label();

}
