/*
 * 作成日 : 2011/04/19
 */
package jp.osd.doma.guice.internal;

import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * {@link JdbcLogger} の実装クラスオブジェクトを提供する Guice プロバイダです。
 * <P>
 * 提供するクラスオブジェクトは以下のようにして決定されます。
 * <OL>
 * <LI>{@link #setJdbcLogger(JdbcLogger)} で値が設定されていればその値を提供する。
 * <LI>{@link org.slf4j.Logger} クラスにクラスパスが通っており、ロード可能であれば
 * {@link Slf4jJdbcLogger} オブジェクトをデフォルトコンストラクタで生成して提供する。
 * <LI>上記以外の時 {@link UtilLoggingJdbcLogger} オブジェクトをデフォルトコンストラクタで生成して提供する。
 * </OL>
 *
 * @author asuka
 */
public class AutoJdbcLoggerProvider implements Provider<JdbcLogger> {
	private SettingHelper settingHelper;

	private JdbcLogger jdbcLogger;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param settingHelper
	 *            設定ヘルパ
	 */
	@Inject
	public AutoJdbcLoggerProvider(SettingHelper settingHelper) {
		this.settingHelper = settingHelper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JdbcLogger get() {
		if (jdbcLogger == null) {
			if (settingHelper.isClassLoadable("org.slf4j.Logger")) {
				jdbcLogger = new Slf4jJdbcLogger();
			} else {
				jdbcLogger = new UtilLoggingJdbcLogger();
			}
		}
		return jdbcLogger;
	}

	/**
	 * {@link #get()} で提供する JDBC ロガーを設定します。
	 *
	 * @param jdbcLogger
	 *            JDBC ロガー
	 */
	@Inject(optional = true)
	public void setJdbcLogger(JdbcLogger jdbcLogger) {
		this.jdbcLogger = jdbcLogger;
	}

}
