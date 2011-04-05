package jp.osd.doma.guice;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import jp.osd.doma.guice.internal.JndiDataSourceProvider;
import jp.osd.doma.guice.internal.JndiLocalTransactionDataSourceProvider;
import jp.osd.doma.guice.internal.JtaUserTransaction;
import jp.osd.doma.guice.internal.LocalTransaction;
import jp.osd.doma.guice.internal.UserTransactionProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;

/**
 * JNDI からデータソースを取得して提供する Guice モジュールです。
 * <P>
 * このクラスはコンストラクタが非公開であるため、{@link JndiDataSourceModule.Builder}
 * を使用してオブジェクトを生成してください。
 * 
 * @author asuka
 */
public class JndiDataSourceModule extends AbstractModule {
	private final Class<? extends Provider<? extends Context>> namingContextProviderType;
	private final boolean localTransaction;

	private JndiDataSourceModule(
			Class<? extends Provider<? extends Context>> namingContextProviderType,
			boolean localTransaction) {
		this.namingContextProviderType = namingContextProviderType;
		this.localTransaction = localTransaction;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure() {
		if (namingContextProviderType == null) {
			bind(Context.class).to(InitialContext.class).in(Scopes.SINGLETON);
		} else {
			bind(Context.class).toProvider(namingContextProviderType).in(
					Scopes.SINGLETON);
		}
		if (localTransaction) {
			// ローカルトランザクションを使用する場合
			bind(DataSource.class).toProvider(
					JndiLocalTransactionDataSourceProvider.class).in(
					Scopes.SINGLETON);
			bind(Transaction.class).to(LocalTransaction.class);
		} else {
			// ローカルトランザクションを使用しない場合
			bind(DataSource.class).toProvider(JndiDataSourceProvider.class).in(
					Scopes.SINGLETON);
			bind(UserTransaction.class).toProvider(
					UserTransactionProvider.class).in(Scopes.SINGLETON);
			bind(Transaction.class).to(JtaUserTransaction.class).in(
					Scopes.SINGLETON);
		}
	}

	/**
	 * {@link JndiDataSourceModule} オブジェクトを作成するためのビルダクラスです。
	 * 
	 * @author asuka
	 */
	public static class Builder {
		private Class<? extends Provider<? extends Context>> namingContextProviderType = null;
		private boolean localTransaction = true;

		/**
		 * {@link Context} オブジェクトを提供するプロバイダの型を設定します。
		 * <P>
		 * このメソッドでプロバイダの型を指定しなかった場合、{@link Context} の実装型は {@link InitialContext} になります。
		 * 
		 * @param namingContextProviderType
		 *            {@link Context} オブジェクトを提供するプロバイダの型を設定
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setNamingContextProviderType(
				Class<? extends Provider<? extends Context>> namingContextProviderType) {
			this.namingContextProviderType = namingContextProviderType;
			return this;
		}

		/**
		 * トランザクション管理に JNDI で取得したユーザトランザクションを使用することを宣言します。
		 * 
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder jndiUserTransaction() {
			localTransaction = false;
			return this;
		}

		/**
		 * 設定に基づいて {@link JndiDataSourceModule} オブジェクトを作成します。
		 * 
		 * @return {@link JndiDataSourceModule} オブジェクト
		 */
		public JndiDataSourceModule create() {
			return new JndiDataSourceModule(namingContextProviderType,
					localTransaction);
		}
	}
}
