package jp.osd.doce.internal;

import static jp.osd.doce.DomaProperties.*;

import javax.inject.Named;
import javax.sql.DataSource;

import jp.osd.doce.Doma;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.dialect.Dialect;

import com.google.inject.Inject;

/**
 * Guice で管理される Doma 設定クラスです。
 * <P>
 * Guice の定数バインドで設定値を指定することができます。
 * <P>
 * <TABLE BORDER=1>
 * <THEAD>
 * <TR>
 * <TH>Named</TH>
 * <TH>必須</TH>
 * <TH>説明</TH></THEAD> <TBODY>
 * <TR>
 * <TD>Doma.maxRows</TD>
 * <TD><BR>
 * </TD>
 * <TD>最大行数制限。{@link Config#getMaxRows()} の戻り値に使用。</TD>
 * </TR>
 * <TR>
 * <TD>Doma.fetchSize</TD>
 * <TD><BR>
 * </TD>
 * <TD>フェッチサイズ。{@link Config#getFetchSize()} の戻り値に使用。</TD>
 * </TR>
 * <TR>
 * <TD>Doma.queryTimeout</TD>
 * <TD><BR>
 * </TD>
 * <TD>クエリタイムアウト（秒）。{@link Config#getQueryTimeout()} の戻り値に使用。</TD>
 * </TR>
 * <TR>
 * <TD>Doma.batchSize</TD>
 * <TD><BR>
 * </TD>
 * <TD>バッチサイズ。{@link Config#getBatchSize()} の戻り値に使用。</TD>
 * </TR>
 * </TBODY>
 * </TABLE>
 *
 * @author asuka
 */
public class GuiceManagedConfig implements Config {
	private final DataSource dataSource;
	private final Dialect dialect;
	private final SqlFileRepository sqlFileRepository;
	private final JdbcLogger jdbcLogger;
	private final RequiresNewController requiresNewController;
	private int maxRows = 0;
	private int fetchSize = 0;
	private int queryTimeout = 0;
	private int batchSize = 10;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param dataSource
	 *            データソース
	 * @param dialect
	 *            Dialect
	 * @param sqlFileRepository
	 *            SQL ファイルリポジトリ
	 * @param jdbcLogger
	 *            JDBC ロガー
	 * @param requiresNewController
	 *            REQUIRES_NEW の属性をもつトランザクションを制御するコントローラ
	 */
	@Inject
	public GuiceManagedConfig(@Doma DataSource dataSource,
			@Doma Dialect dialect, @Doma SqlFileRepository sqlFileRepository,
			@Doma JdbcLogger jdbcLogger,
			@Doma RequiresNewController requiresNewController) {
		this.dataSource = dataSource;
		this.dialect = dialect;
		this.sqlFileRepository = sqlFileRepository;
		this.jdbcLogger = jdbcLogger;
		this.requiresNewController = requiresNewController;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataSourceName() {
		return dataSource.getClass().getSimpleName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dialect getDialect() {
		return dialect;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SqlFileRepository getSqlFileRepository() {
		return sqlFileRepository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JdbcLogger getJdbcLogger() {
		return jdbcLogger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RequiresNewController getRequiresNewController() {
		return requiresNewController;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxRows() {
		return maxRows;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFetchSize() {
		return fetchSize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getQueryTimeout() {
		return queryTimeout;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getBatchSize() {
		return batchSize;
	}

	/**
	 * 最大行数の制限値を設定します。
	 *
	 * @param maxRows
	 *            最大行数の制限値
	 */
	@Inject(optional = true)
	public void setMaxRows(@Named(DOMA_MAX_ROWS) int maxRows) {
		this.maxRows = maxRows;
	}

	/**
	 * フェッチサイズを設定します。
	 *
	 * @param fetchSize
	 *            フェッチサイズ
	 */
	@Inject(optional = true)
	public void setFetchSize(@Named(DOMA_FETCH_SIZE) int fetchSize) {
		this.fetchSize = fetchSize;
	}

	/**
	 * クエリタイムアウト（秒）を設定します。
	 *
	 * @param queryTimeout
	 *            クエリタイムアウト（秒）
	 */
	@Inject(optional = true)
	public void setQueryTimeout(@Named(DOMA_QUERY_TIMEOUT) int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	/**
	 * バッチサイズを設定します。
	 *
	 * @param batchSize
	 *            バッチサイズ
	 */
	@Inject(optional = true)
	public void setBatchSize(@Named(DOMA_BATCH_SIZE) int batchSize) {
		this.batchSize = batchSize;
	}

}
