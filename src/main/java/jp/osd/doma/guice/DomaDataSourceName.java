/*
 * 作成日 : 2011/04/15
 */
package jp.osd.doma.guice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Doma に注入するデータソースインスタンスをインジェクタから取得するための名前をインジェクションすることを示す注釈です。
 *
 * @author asuka
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@BindingAnnotation
public @interface DomaDataSourceName {

}
