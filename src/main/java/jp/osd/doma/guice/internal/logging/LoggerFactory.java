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
	private static boolean julUsing = false;

	static {
		julUsing = !new SettingHelperImpl().isClassLoadable("org.slf4j.Logger");
	}

	/**
	 * {@link JulLogger} を使用するかを設定します。このメソッドで <code>true</code>
	 * を設定した場合、どのような状況でも {@link Logger} の実装クラスには {@link JulLogger} が使用されます。
	 *
	 * @param julUsing {@link JulLogger} を使用するかのフラグ
	 */
	protected static void setJulUsing(boolean julUsing) {
		LoggerFactory.julUsing = julUsing;
	}

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
		ResourceBundle bundle = ResourceBundle
				.getBundle("jp/osd/doma/guice/internal/messages");
		if (julUsing) {
			return JulLogger.getLogger(clazz.getName(), bundle);
		}
		return Slf4jLogger.getLogger(clazz.getName(), bundle);
	}

	private LoggerFactory() {
		// 何もしない
	}
}
