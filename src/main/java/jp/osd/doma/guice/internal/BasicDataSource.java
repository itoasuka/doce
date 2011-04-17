package jp.osd.doma.guice.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Commons DBCP の BasicDataSource によるデータソースに関するインスタンスをインジェクションすることを示す注釈です。
 * @author asuka
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@BindingAnnotation
public @interface BasicDataSource {

}
