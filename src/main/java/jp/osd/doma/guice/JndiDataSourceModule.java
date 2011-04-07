package jp.osd.doma.guice;

import static jp.osd.doma.guice.BindingRule.to;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import jp.osd.doma.guice.internal.JndiDataSourceProvider;
import jp.osd.doma.guice.internal.JtaUserTransaction;
import jp.osd.doma.guice.internal.LocalTransaction;
import jp.osd.doma.guice.internal.LocalTransactionalDataSourceProvider;
import jp.osd.doma.guice.internal.Plain;
import jp.osd.doma.guice.internal.UserTransactionProvider;

import com.google.inject.AbstractModule;
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
	private final BindingRule<? extends Context> namingContextBindingRule;
	private final boolean localTransaction;

	private JndiDataSourceModule(
			BindingRule<? extends Context> namingContextBindingRule,
			boolean localTransaction) {
		this.namingContextBindingRule = namingContextBindingRule;
		this.localTransaction = localTransaction;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure() {
		namingContextBindingRule.apply(bind(Context.class));

		if (localTransaction) {
			// ローカルトランザクションを使用する場合
			bind(DataSource.class).annotatedWith(Plain.class)
					.toProvider(JndiDataSourceProvider.class)
					.in(Scopes.SINGLETON);
			bind(DataSource.class).toProvider(
					LocalTransactionalDataSourceProvider.class).in(
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
		private BindingRule<? extends Context> namingContextBindingRule = to(InitialContext.class);
		private boolean localTransaction = true;

		/**
		 * {@link Context} オブジェクトをバインドするルールを設定します。
		 * <P>
		 * このメソッドでプロバイダの型を指定しなかった場合、{@link Context} の実装型は {@link InitialContext}
		 * になります。
		 * 
		 * @param namingContextBindingRule
		 *            {@link Context} オブジェクトをバインドするルール
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setNamingContextBindingRule(
				BindingRule<? extends Context> namingContextBindingRule) {
			this.namingContextBindingRule = namingContextBindingRule;
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
			return new JndiDataSourceModule(namingContextBindingRule,
					localTransaction);
		}
	}
}
