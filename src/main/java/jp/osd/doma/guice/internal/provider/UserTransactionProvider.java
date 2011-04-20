package jp.osd.doma.guice.internal.provider;

import javax.inject.Named;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import jp.osd.doma.guice.Doma;
import jp.osd.doma.guice.DomaGuiceException;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * JNDI によってユーザトランザクションを取得して提供する Guice プロバイダです。
 *
 * @author asuka
 */
public class UserTransactionProvider implements Provider<UserTransaction> {
	private final Context context;

	private String transactionName = "java:comp/UserTransaction";

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param context
	 *            トランザクションを JNDI でルックアップする際に使用するネーミングコンテキスト
	 */
	@Inject
	public UserTransactionProvider(@Doma Context context) {
		this.context = context;
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

	/**
	 * トランザクションを JNDI でルックアップする際に使用するオブジェクト名を設定します。
	 *
	 * @param transactionName
	 *            オブジェクト名
	 */
	@Inject(optional = true)
	public void setJndiTransactionName(
			@Named("JNDI.transaction") String transactionName) {
		this.transactionName = transactionName;
	}
}
