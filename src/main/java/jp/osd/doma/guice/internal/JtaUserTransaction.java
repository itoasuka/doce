package jp.osd.doma.guice.internal;

import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import jp.osd.doma.guice.Doma;
import jp.osd.doma.guice.DomaGuiceException;
import jp.osd.doma.guice.Transaction;

/**
 * JTA の {@link UserTransaction} をラップするトランザクションクラスです。
 *
 * @author asuka
 */
public class JtaUserTransaction implements Transaction {
	private final UserTransaction tx;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param tx
	 *            ラップ対象のユーザトランザクション
	 */
	@Inject
	public JtaUserTransaction(@Doma UserTransaction tx) {
		this.tx = tx;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void begin() {
		try {
			tx.begin();
		} catch (Exception e) {
			throw new DomaGuiceException("Transaction begin error", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
		try {
			tx.commit();
		} catch (Exception e) {
			throw new DomaGuiceException("Transaction commit error", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		try {
			tx.rollback();
		} catch (Exception e) {
			throw new DomaGuiceException("Transaction rollback error", e);
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
			throw new DomaGuiceException("Transaction status getting error", e);
		}
	}

}
