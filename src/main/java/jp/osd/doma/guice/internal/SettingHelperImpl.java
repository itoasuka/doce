package jp.osd.doma.guice.internal;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.inject.Inject;

/**
 * 設定に関するヘルパの実装クラスです。
 *
 * @author asuka
 */
@Singleton
public class SettingHelperImpl implements SettingHelper {
	@Inject(optional = true)
	@Named("DBCP.driver")
	private String dbcpDriver = "";

	private final String jdbcUrl;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param jdbcUrl 接続先のデータベースの URL
	 */
	@Inject
	public SettingHelperImpl(@Named("JDBC.url") String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDriverClassName() {
		if (0 < dbcpDriver.length()) {
			// 明示的にドライバ名が指定されている場合はそれを使用
			return dbcpDriver;
		}
		return JdbcUtils.getDriverClassName(jdbcUrl);
	}

}
