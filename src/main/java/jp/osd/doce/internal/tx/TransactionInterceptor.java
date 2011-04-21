package jp.osd.doce.internal.tx;

import jp.osd.doce.Transaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Inject;

/**
 * メソッドをトランザクション下で実行させるためのインターセプタです。
 *
 * @author asuka
 */
public class TransactionInterceptor implements MethodInterceptor {
	private Transaction transaction;

	/**
	 * 新たにオブジェクトを構築します。
	 */
	public TransactionInterceptor() {
		// 何もしない
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result;

		if (transaction.isActive()) {
			result = invocation.proceed();
		} else {
			transaction.begin();
			try {
				result = invocation.proceed();
			} catch (Throwable t) {
				transaction.rollback();
				throw t;
			}
			transaction.commit();
		}

		return result;
	}

	/**
	 * トランザクション機能を設定します。
	 *
	 * @param transaction トランザクション機能
	 */
	@Inject
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

}
