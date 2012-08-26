package jp.osd.doce;

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
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Transactional {
	/**
	 * トランザクションを稼働させるデータベース名を指定します。
	 * <P>
	 * 何も指定しない場合はデフォルトのデータベースのみが対象となります。デフォルトのデータベースを明示的に指定する場合は空文字列を指定してください。
	 * 
	 * @return
	 */
	String[] value() default {};
}
