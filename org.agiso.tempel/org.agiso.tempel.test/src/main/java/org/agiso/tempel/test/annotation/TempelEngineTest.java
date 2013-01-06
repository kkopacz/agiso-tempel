/* org.agiso.tempel.test.annotation.TempelEngineTest (17-11-2012)
 * 
 * TempelEngineTest.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel.test.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.agiso.tempel.api.ITempelEngine;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TempelEngineTest {
	public Class<? extends ITempelEngine> value();
}
