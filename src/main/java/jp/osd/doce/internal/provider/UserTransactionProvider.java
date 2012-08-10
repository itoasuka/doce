package jp.osd.doce.internal.provider;

import static jp.osd.doce.JndiProperties.JNDI_USER_TRANSACTION;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import jp.osd.doce.DoceException;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * JNDI によってユーザトランザクションを取得して提供する Guice プロバイダです。
 *
 * @author asuka
 */
public class UserTransactionProvider implements Provider<UserTransaction> {
	/** トランザクションの JNDI ルックアップに使用するデフォルトのオブジェクト名です。 */
	public static final String DEFAULT_JNDI_TRANSACTION_NAME = "java:comp/UserTransaction";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserTransactionProvider.class);

	private final Context context;

	private String transactionName = DEFAULT_JNDI_TRANSACTION_NAME;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param context
	 *            トランザクションを JNDI でルックアップする際に使用するネーミングコンテキスト
	 */
	public UserTransactionProvider(Context context) {
		LOGGER.logConstructor(Context.class);
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserTransaction get() {
		try {
			LOGGER.debug(MessageCodes.DG006, transactionName);
			UserTransaction tx = (UserTransaction) context
					.lookup(transactionName);
			LOGGER.debug(MessageCodes.DG008);
			return tx;
		} catch (NamingException e) {
			LOGGER.error(e, MessageCodes.DG007, transactionName);
			throw new DoceException("Lookup error : " + transactionName, e);
		}
	}

	/**
	 * トランザクションを JNDI でルックアップする際に使用するオブジェクト名を設定します。
	 *
	 * @param transactionName
	 *            オブジェクト名
	 */
	@Inject(optional = true)
	public void setDefaultJndiTransactionName(
			@Named(JNDI_USER_TRANSACTION) String transactionName) {
		LOGGER.debug(MessageCodes.DG002, JNDI_USER_TRANSACTION, transactionName);
		this.transactionName = transactionName;
	}
}
