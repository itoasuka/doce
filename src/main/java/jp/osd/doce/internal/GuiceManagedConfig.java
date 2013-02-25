package jp.osd.doce.internal;

import static jp.osd.doce.DomaProperties.DOMA_BATCH_SIZE;
import static jp.osd.doce.DomaProperties.DOMA_FETCH_SIZE;
import static jp.osd.doce.DomaProperties.DOMA_MAX_ROWS;
import static jp.osd.doce.DomaProperties.DOMA_QUERY_TIMEOUT;

import javax.sql.DataSource;

import jp.osd.doce.Doma;

import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DefaultClassHelper;
import org.seasar.doma.jdbc.DomaAbstractConfig;
import org.seasar.doma.jdbc.ExceptionSqlLogType;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.dialect.Dialect;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

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
public class GuiceManagedConfig extends DomaAbstractConfig {
	private final DbNamedProperties properties;
	private Injector injector;
	private DataSource dataSource;
	private Dialect dialect;
	private SqlFileRepository sqlFileRepository;
	private JdbcLogger jdbcLogger;
	private int maxRows = 0;
	private int fetchSize = 0;
	private int queryTimeout = 0;
	private int batchSize = 10;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param properties
	 *            データベース名付き設定プロパティ
	 */
	public GuiceManagedConfig(DbNamedProperties properties) {
		this.properties = properties;
		maxRows = this.properties.getInt(DOMA_MAX_ROWS, maxRows);
		fetchSize = this.properties.getInt(DOMA_FETCH_SIZE, fetchSize);
		queryTimeout = this.properties.getInt(DOMA_QUERY_TIMEOUT, queryTimeout);
		batchSize = this.properties.getInt(DOMA_BATCH_SIZE, batchSize);
	}
	
	/**
	 * Guice インジェクタを設定します。
	 * 
	 * @param injector Guice インジェクタ
	 */
	@Inject
	public void setInjector(Injector injector) {
		this.injector = injector;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = getInstance(DataSource.class);
		}
		
		return dataSource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataSourceName() {
		return getDataSource().getClass().getSimpleName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dialect getDialect() {
		if (dialect == null) {
			dialect = getInstance(Dialect.class);
		}
		
		return dialect;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SqlFileRepository getSqlFileRepository() {
		if (sqlFileRepository == null) {
			sqlFileRepository = getInstance(SqlFileRepository.class);
		}
		
		return sqlFileRepository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JdbcLogger getJdbcLogger() {
		if (jdbcLogger == null) {
			jdbcLogger = getInstance(JdbcLogger.class);
		}
		
		return jdbcLogger;
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
	 * 例外に含めるSQLログのタイプを返します。
	 * 
	 * @return 常に {@link ExceptionSqlLogType#FORMATTED_SQL}
	 */
	@Override
	public ExceptionSqlLogType getExceptionSqlLogType() {
		return ExceptionSqlLogType.FORMATTED_SQL;
	}

	private <T> T getInstance(Class<T> type) {
		if (injector == null) {
			return null;
		}
		String dbName = properties.getDbName();
		if (dbName == null) {
			return injector.getInstance(Key.get(type, Doma.class));
		}
		return injector.getInstance(Key.get(type, Names.named(dbName)));
	}
}
