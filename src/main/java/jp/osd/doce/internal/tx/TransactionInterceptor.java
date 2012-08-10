package jp.osd.doce.internal.tx;

import jp.osd.doce.Transaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * メソッドをトランザクション下で実行させるためのインターセプタです。
 *
 * @author asuka
 */
public class TransactionInterceptor implements MethodInterceptor {
	private final String dbName;
	
	@Inject
	private Injector injector;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param dbName データベース名
	 */
	public TransactionInterceptor(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Transaction transaction;
		if (dbName == null) {
			transaction = injector.getInstance(Transaction.class);
		} else {
			transaction = injector.getInstance(Key.get(Transaction.class, Names.named(dbName)));
		}
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
}
