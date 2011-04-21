/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doce;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * {@link jp.osd.doce.internal.GuiceManagedConfig} にインジェクションすることを示す注釈です。
 * <P>
 * {@link DomaModule} で設定したインジェクタで DAO にインジェクションされる列挙体を除く定数以外のオブジェクトはこのアノテーションを付加してバインド定義されています。
 * @author asuka
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@BindingAnnotation
public @interface Doma {

}
