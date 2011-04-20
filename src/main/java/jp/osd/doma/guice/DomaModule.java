package jp.osd.doma.guice;

import static com.google.inject.Scopes.SINGLETON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.Context;
import javax.sql.DataSource;

import jp.osd.doma.guice.internal.ClassUtils;
import jp.osd.doma.guice.internal.GuiceManagedConfig;
import jp.osd.doma.guice.internal.provider.AutoDataSourceProvider;
import jp.osd.doma.guice.internal.provider.AutoJdbcLoggerProvider;
import jp.osd.doma.guice.internal.provider.AutoTransactionProvider;
import jp.osd.doma.guice.internal.provider.DefaultContextProvider;
import jp.osd.doma.guice.internal.provider.DefaultDialectProvider;
import jp.osd.doma.guice.internal.provider.DefaultRequiresNewControllerProvider;
import jp.osd.doma.guice.internal.provider.DefaultSqlFileRepositoryProvider;
import jp.osd.doma.guice.internal.tx.TransactionInterceptor;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.dialect.Dialect;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;

/**
 * Doma と Guice を連携させるための Guice モジュールクラスです。
 * <P>
 * このクラスはコンストラクタが非公開であるため、{@link DomaModule.Builder} を使用してオブジェクトを生成してください。
 *
 * @author asuka
 */
public class DomaModule extends AbstractModule {
	private final DataSourceBinding dataSourceBinding;
	private final TransactionBinding transactionBinding;
	private final List<Class<?>> daoTypes;
	private final String daoPackage;
	private final String daoSubpackage;
	private final String daoSuffix;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure() {
		requestInjection(this);

		// ネーミングコンテキストの設定
		bind(Context.class).annotatedWith(Doma.class)
				.toProvider(DefaultContextProvider.class).in(SINGLETON);

		// データソースのバインド方法を設定
		bindConstant().annotatedWith(Doma.class).to(dataSourceBinding);
		// トランザクションのバインド方法を設定
		bindConstant().annotatedWith(Doma.class).to(transactionBinding);

		// データソースプロバイダの設定
		switch (dataSourceBinding) {
		case NONE:
			break;
		default:
			bind(DataSource.class).annotatedWith(Doma.class)
					.toProvider(AutoDataSourceProvider.class).in(SINGLETON);
			break;
		}

		// SQL ファイルリポジトリの設定
		bind(SqlFileRepository.class).annotatedWith(Doma.class)
				.toProvider(DefaultSqlFileRepositoryProvider.class)
				.in(Scopes.SINGLETON);
		// JDBC ロガーの設定
		bind(JdbcLogger.class).annotatedWith(Doma.class)
				.toProvider(AutoJdbcLoggerProvider.class)
				.in(Scopes.SINGLETON);
		// Requires new controller の設定
		bind(RequiresNewController.class).annotatedWith(Doma.class)
				.toProvider(DefaultRequiresNewControllerProvider.class)
				.in(Scopes.SINGLETON);

		// Dialect の設定
		bind(Dialect.class).annotatedWith(Doma.class)
				.toProvider(DefaultDialectProvider.class).in(Scopes.SINGLETON);

		// Doma 用 Config クラスの設定
		bind(Config.class).to(GuiceManagedConfig.class).in(Scopes.SINGLETON);

		// トランザクションの設定
		switch (transactionBinding) {
		case NONE:
			break;
		default:
			bind(Transaction.class).annotatedWith(Doma.class)
					.toProvider(AutoTransactionProvider.class)
					.in(Scopes.SINGLETON);
			break;
		}

		// トランザクションインタセプタの設定
		if (transactionBinding != TransactionBinding.NONE) {
			TransactionInterceptor lti = new TransactionInterceptor();
			requestInjection(lti);
			bindInterceptor(Matchers.any(),
					Matchers.annotatedWith(Transactional.class), lti);
			bindInterceptor(Matchers.annotatedWith(Transactional.class),
					Matchers.any(), lti);
		}

		// Dao の設定
		for (Class<?> daoType : daoTypes) {
			bindDao(daoType);
		}
	}

	private DomaModule(DataSourceBinding dataSourceBinding,
			TransactionBinding transactionBinding, List<Class<?>> daoTypes,
			String daoPackage, String daoSubpackage, String daoSuffix) {
		this.dataSourceBinding = dataSourceBinding;
		this.transactionBinding = transactionBinding;
		this.daoTypes = daoTypes;
		this.daoPackage = daoPackage;
		this.daoSubpackage = daoSubpackage;
		this.daoSuffix = daoSuffix;
	}

	private <T> void bindDao(Class<T> daoType) {
		String className = ClassUtils.getImplClassName(daoType, daoPackage,
				daoSubpackage, daoSuffix);
		Class<? extends T> implClass;
		try {
			implClass = Class.forName(className).asSubclass(daoType);
		} catch (ClassNotFoundException e) {
			throw new DomaGuiceException(
					"Dao implementation class is not found.", e);
		}

		bind(daoType).to(implClass);
	}

	/**
	 * {@link DomaModule} オブジェクトを作成するためのビルダクラスです。
	 *
	 * @author asuka
	 */
	public static final class Builder {
		private DataSourceBinding dataSourceBinding = DataSourceBinding.AUTO;
		private TransactionBinding transactionBinding = TransactionBinding.AUTO;
		private final List<Class<?>> daoTypes = new ArrayList<Class<?>>();
		private String daoPackage = "";
		private String daoSubpackage = "";
		private String daoSuffix = "Impl";

		/**
		 * 新たにオブジェクトを構築します。
		 */
		public Builder() {
		}

		/**
		 * データソースのバインディング方法を設定します。
		 *
		 * @param dataSourceBinding データソースのバインディング方法
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setDataSourceBinding(DataSourceBinding dataSourceBinding) {
			this.dataSourceBinding = dataSourceBinding;
			return this;
		}

		/**
		 * トランザクションのバインディング方法を設定します。
		 *
		 * @param transactionBinding トランザクションのバインディング方法
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setTransactionBinding(
				TransactionBinding transactionBinding) {
			this.transactionBinding = transactionBinding;
			return this;
		}

		/**
		 * Guice の管理下に置く Dao インタフェースの型を設定します。ひもづく実装クラスは動的に指定されます。
		 * <P>
		 * これにより javac のバグ（Bug ID <a
		 * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6403465"
		 * >6403465</a>）によるコンパイル時のエラーメッセージを抑制することができます。
		 *
		 * @param daoTypes
		 *            Dao インタフェースの型
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder addDaoTypes(Class<?>... daoTypes) {
			for (Class<? extends Object> daoType : daoTypes) {
				this.daoTypes.add(daoType);
			}
			return this;
		}

		/**
		 * Guice の管理下に置く Dao インタフェースの型を設定します。ひもづく実装クラスは実行時に指定されます。
		 * <P>
		 * これにより javac のバグ（Bug ID <a
		 * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6403465"
		 * >6403465</a>）によるコンパイル時のエラーメッセージを抑制することができます。
		 *
		 * @param daoTypes
		 *            Dao インタフェースの型のコレクション
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder addDaoTypes(Collection<Class<?>> daoTypes) {
			this.daoTypes.addAll(daoTypes);

			return this;
		}

		/**
		 * Dao の実装クラスのパッケージ名を設定します。これは Dao のインタフェースと実装クラスの実行時のひもづけに使用されます。
		 * <P>
		 * 設定しない場合、Dao インターフェース型と同じパッケージ名が使用されます。
		 *
		 * @param daoPackage
		 *            Dao の実装クラスのパッケージ名
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setDaoPackage(String daoPackage) {
			this.daoPackage = daoPackage;
			return this;
		}

		/**
		 * Dao の実装クラスのサブパッケージ名を設定します。これは Dao のインタフェースと実装クラスの実行時のひもづけに使用されます。
		 *
		 * @param daoSubpackage
		 *            Dao の実装クラスのサブパッケージ名
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setDaoSubpackage(String daoSubpackage) {
			this.daoSubpackage = daoSubpackage;
			return this;
		}

		/**
		 * Dao の実装クラスのクラス名のサフィックスを設定します。これは Dao のインタフェースと実装クラスの実行時のひもづけに使用されます。
		 * <P>
		 * 設定しない場合、{@literal "Impl"} が使用されます。
		 *
		 * @param daoSuffix
		 *            Dao の実装クラスのクラス名のサフィックス
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setDaoSuffix(String daoSuffix) {
			this.daoSuffix = daoSuffix;
			return this;
		}

		/**
		 * 設定に基づいて {@link DomaModule} オブジェクトを作成します。
		 *
		 * @return {@link DomaModule} オブジェクト
		 */
		public DomaModule create() {
			return new DomaModule(dataSourceBinding, transactionBinding,
					daoTypes, daoPackage, daoSubpackage, daoSuffix);
		}
	}
}
