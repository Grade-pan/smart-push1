package com.sed.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 只是增加注解方便阅读该组件的信息
 * @author sun
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface ExceptionListener {

	/**
	 *获取里面的Sting，此String是来辨别具体通知负责人，
	 * @return
	 */
	String value() default "";

}
