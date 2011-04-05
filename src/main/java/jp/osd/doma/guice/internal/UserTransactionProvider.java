package jp.osd.doma.guice.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import jp.osd.doma.guice.DomaGuiceException;

import com.google.inject.Provider;

/**
 * JNDI によってユーザトランザクションを取得して提供する Guice プロバイダです。
 * 
 * @author asuka
 */
public class UserTransactionProvider implements Provider<UserTransaction> {
	private final Context context;

	private final String transactionName;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param context
	 *            トランザクションを JNDI でルックアップする際に使用するネーミングコンテキスト
	 * @param transactionName
	 *            トランザクションを JNDI でルックアップする際に使用するデータソース名
	 */
	@Inject
	public UserTransactionProvider(Context context,
			@Named("jndi.transaction") String transactionName) {
		this.context = context;
		this.transactionName = transactionName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserTransaction get() {
		try {
			return (UserTransaction) context.lookup(transactionName);
		} catch (NamingException e) {
			throw new DomaGuiceException("Lookup error : " + transactionName, e);
		}
	}

}
