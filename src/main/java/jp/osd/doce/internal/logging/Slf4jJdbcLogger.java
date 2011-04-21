package jp.osd.doce.internal.logging;

import java.sql.SQLException;
import java.util.ResourceBundle;


import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;

/**
 * SLF4J による Doma 用のロガーです。
 *
 * @author asuka
 */
public class Slf4jJdbcLogger implements JdbcLogger {
	private final ResourceBundle bundle;

	/**
	 * 新たにオブジェクトを構築します。
	 */
	public Slf4jJdbcLogger() {
		this(ResourceBundle.getBundle("jp/osd/doce/internal/messages"));
	}

	/**
	 * ログメッセージのリソースバンドルを指定して新たにオブジェクトを構築します。
	 *
	 * @param bundle ログメッセージのリソースバンドル
	 */
	public Slf4jJdbcLogger(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logDaoMethodEntering(String callerClassName,
			String callerMethodName, Object... args) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				if (0 < i) {
					sb.append(", ");
				}
				sb.append(args[i]);
			}

			logger.debug(MessageCodes.EXT001, callerMethodName, sb.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logDaoMethodExiting(String callerClassName,
			String callerMethodName, Object result) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.debug(MessageCodes.EXT002, callerMethodName, result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logDaoMethodThrowing(String callerClassName,
			String callerMethodName, RuntimeException e) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.error(e, MessageCodes.EXT003, callerMethodName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logSqlExecutionSkipping(String callerClassName,
			String callerMethodName, SqlExecutionSkipCause cause) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.debug(MessageCodes.EXT004, cause.name());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logSql(String callerClassName, String callerMethodName,
			Sql<?> sql) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.debug(MessageCodes.DOMA2076, sql.getSqlFilePath(),
				sql.getFormattedSql());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logLocalTransactionBegun(String callerClassName,
			String callerMethodName, String transactionId) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.debug(MessageCodes.DOMA2063, transactionId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logLocalTransactionEnded(String callerClassName,
			String callerMethodName, String transactionId) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.debug(MessageCodes.DOMA2064, transactionId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logLocalTransactionSavepointCreated(String callerClassName,
			String callerMethodName, String transactionId, String savepointName) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.debug(MessageCodes.DOMA2065, transactionId, savepointName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logLocalTransactionSavepointReleased(String callerClassName,
			String callerMethodName, String transactionId, String savepointName) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.debug(MessageCodes.DOMA2066, transactionId, savepointName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logLocalTransactionCommitted(String callerClassName,
			String callerMethodName, String transactionId) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.debug(MessageCodes.DOMA2067, transactionId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logLocalTransactionRolledback(String callerClassName,
			String callerMethodName, String transactionId) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.debug(MessageCodes.DOMA2068, transactionId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logLocalTransactionSavepointRolledback(String callerClassName,
			String callerMethodName, String transactionId, String savepointName) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.debug(MessageCodes.DOMA2069, transactionId, savepointName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logLocalTransactionRollbackFailure(String callerClassName,
			String callerMethodName, String transactionId, SQLException e) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.debug(MessageCodes.DOMA2070, transactionId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logAutoCommitEnablingFailure(String callerClassName,
			String callerMethodName, SQLException e) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.error(e, MessageCodes.DOMA2071);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logTransactionIsolationSettingFailuer(String callerClassName,
			String callerMethodName, int transactionIsolationLevel,
			SQLException e) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.error(e, MessageCodes.DOMA2072, transactionIsolationLevel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logConnectionClosingFailure(String callerClassName,
			String callerMethodName, SQLException e) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.error(e, MessageCodes.DOMA2073, e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logStatementClosingFailure(String callerClassName,
			String callerMethodName, SQLException e) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.error(e, MessageCodes.DOMA2074);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logResultSetClosingFailure(String callerClassName,
			String callerMethodName, SQLException e) {
		Slf4jLogger logger = Slf4jLogger.getLogger(callerClassName, bundle);
		logger.error(e, MessageCodes.DOMA2075);
	}

}
