/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Doma 用の {@link Config} にインジェクションすることを示す注釈です。
 * @author asuka
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@BindingAnnotation
public @interface Doma {

}
