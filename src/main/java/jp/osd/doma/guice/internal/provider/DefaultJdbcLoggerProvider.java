/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal.provider;

import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

/**
 * 未設定でもデフォルトのロガーを返す Guice プロバイダです。
 *
 * @author asuka
 */
public class DefaultJdbcLoggerProvider extends DefaultProvider<JdbcLogger> {
	@Override
	protected JdbcLogger getDefaultValue() {
		return new UtilLoggingJdbcLogger();
	}

}
