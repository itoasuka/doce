package jp.osd.doma.guice.internal;

import jp.osd.doma.guice.Doma;
import jp.osd.doma.guice.Transaction;

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
	public void setTransaction(@Doma Transaction transaction) {
		this.transaction = transaction;
	}

}
