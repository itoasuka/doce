package jp.osd.doce.internal.tx;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

import jp.osd.doce.DoceException;
import jp.osd.doce.Transaction;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

/**
 * JTA の {@link UserTransaction} をラップするトランザクションクラスです。
 * 
 * @author asuka
 */
public class JtaUserTransaction implements Transaction {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JtaUserTransaction.class);

	private final UserTransaction tx;

	private final String dbName;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param tx
	 *            ラップ対象のユーザトランザクション
	 */
	public JtaUserTransaction(String dbName, UserTransaction tx) {
		LOGGER.logConstructor(String.class, UserTransaction.class);
		this.dbName = dbName;
		this.tx = tx;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void begin() {
		try {
			tx.begin();
			LOGGER.debug(MessageCodes.DG016, dbName);
		} catch (Exception e) {
			LOGGER.error(e, MessageCodes.DG017, dbName);
			throw new DoceException("Transaction begin error", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
		try {
			if (isActive()) {
				tx.commit();
				LOGGER.debug(MessageCodes.DG018, dbName);
			}
		} catch (Exception e) {
			LOGGER.error(e, MessageCodes.DG019, dbName);
			throw new DoceException("Transaction commit error", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		try {
			if (isActive()) {
				tx.rollback();
				LOGGER.debug(MessageCodes.DG020, dbName);
			}
		} catch (Exception e) {
			LOGGER.error(e, MessageCodes.DG021, dbName);
			throw new DoceException("Transaction rollback error", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive() {
		try {
			return tx.getStatus() == Status.STATUS_ACTIVE;
		} catch (Exception e) {
			LOGGER.error(e, MessageCodes.DG022, dbName);
			throw new DoceException("Transaction status getting error", e);
		}
	}

}
