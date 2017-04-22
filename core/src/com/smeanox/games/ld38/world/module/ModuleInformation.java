package com.smeanox.games.ld38.world.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleInformation {
	String name() default "Nuthing";

	int width() default 1;

	int height() default 1;
}
