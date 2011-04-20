/*
 * 作成日 : 2011/04/19
 */
package jp.osd.doma.guice.internal.logging;

import java.util.ResourceBundle;

import jp.osd.doma.guice.internal.SettingHelperImpl;
import jp.osd.doma.guice.internal.Slf4jLogger;

/**
 * 環境に応じて {@link Logger} 実装を提供するファクトリクラスです。
 *
 * @author asuka
 */
public class LoggerFactory {
	/**
	 * {@link Logger} の実装クラスオブジェクトを取得します。
	 * <P>
	 * {@code org.slf4j.Logger} クラスがロード可能ならば {@link Slf4jLogger}
	 * クラスオブジェクトが返り、それ以外の場合は {@link JulLogger} クラスオブジェクトが返ります。
	 * <P>
	 * いずれの場合でもメッセージリソースにはベース名が {@code "jp/osd/doma/guice/internal/messages"}
	 * であるリソースバンドルが使用されます。
	 *
	 * @param clazz
	 *            ロガーとひもづかせるクラス
	 * @return ロガー実装
	 */
	public static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz,
				new SettingHelperImpl().isClassLoadable("org.slf4j.Logger"));
	}

	/**
	 * {@link Logger} の実装クラスオブジェクトを取得します。
	 * <P>
	 * 引数 {@code slf4jLodable} が <code>true</code> のとき {@link Slf4jLogger}
	 * クラスオブジェクトが返り、それ以外の場合は {@link JulLogger} クラスオブジェクトが返ります。
	 * <P>
	 * いずれの場合でもメッセージリソースにはベース名が {@code "jp/osd/doma/guice/internal/messages"}
	 * であるリソースバンドルが使用されます。
	 *
	 * @param clazz ロガーとひもづかせるクラス
	 * @param slf4jLodable {@code org.slf4j.Logger} クラスがロード可能かどうか
	 * @return ロガー
	 */
	public static Logger getLogger(Class<?> clazz, boolean slf4jLodable) {
		ResourceBundle bundle = ResourceBundle
				.getBundle("jp/osd/doma/guice/internal/messages");
		if (slf4jLodable) {
			return Slf4jLogger.getLogger(clazz.getName(), bundle);
		}
		return JulLogger.getLogger(clazz.getName(), bundle);
	}

	private LoggerFactory() {
		// 何もしない
	}
}
