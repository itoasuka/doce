package jp.osd.doma.guice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * メソッドがトランザクション下で実行さすることを宣言するためのアノテーションです。
 *
 * @author asuka
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {

}
