package jp.osd.doce;

import static com.google.inject.Scopes.SINGLETON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.sql.DataSource;

import jp.osd.doce.internal.ClassUtils;
import jp.osd.doce.internal.DbNamedPropeties;
import jp.osd.doce.internal.GuiceManagedConfig;
import jp.osd.doce.internal.provider.AutoDataSourceProvider;
import jp.osd.doce.internal.provider.AutoJdbcLoggerProvider;
import jp.osd.doce.internal.provider.AutoTransactionProvider;
import jp.osd.doce.internal.provider.DefaultContextProvider;
import jp.osd.doce.internal.provider.DefaultDialectProvider;
import jp.osd.doce.internal.provider.DefaultRequiresNewControllerProvider;
import jp.osd.doce.internal.provider.DefaultSqlFileRepositoryProvider;
import jp.osd.doce.internal.tx.TransactionInterceptor;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.dialect.Dialect;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

/**
 * Doma と Guice を連携させるための Guice モジュールクラスです。
 * <P>
 * このクラスはコンストラクタが非公開であるため、{@link DoceModule.Builder} を使用してオブジェクトを生成してください。
 * 
 * @author asuka
 */
public class DoceModule extends AbstractModule {
	private static final String DEFAUL_DAO_PACKAGE = "";
	private static final String DEFAUL_DAO_SUBPACKAGE = "";
	private static final String DEFAUL_DAO_SUFFIX = "Impl";

	private final String dbName;
	private final Collection<Class<?>> daoTypes;
	private final String daoPackage;
	private final String daoSubpackage;
	private final String daoSuffix;
	private final Properties properties;

	public DoceModule(Properties properties, Class<?>... daoTypes) {
		this(properties, Arrays.asList(daoTypes));
	}

	public DoceModule(Properties properties, Collection<Class<?>> daoTypes) {
		this(null, properties, daoTypes);
	}

	public DoceModule(String dbName, Properties properties,
			Collection<Class<?>> daoTypes) {
		this.dbName = dbName;
		this.properties = properties;
		this.daoTypes = daoTypes;
		daoPackage = DEFAUL_DAO_PACKAGE;
		daoSubpackage = DEFAUL_DAO_SUBPACKAGE;
		daoSuffix = DEFAUL_DAO_SUFFIX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure() {
		DbNamedPropeties props = new DbNamedPropeties(dbName, properties);
		DataSourceBinding dataSourceBinding = props.getDataSourceBinding();
		TransactionBinding transactionBinding = props.getTransactionBinding();

		// ネーミングコンテキストの設定
		bindNamed(Context.class).toProvider(DefaultContextProvider.class).in(
				SINGLETON);

		// データソースプロバイダの設定
		switch (dataSourceBinding) {
		case NONE:
			break;
		default:
			AutoDataSourceProvider provider = new AutoDataSourceProvider(props);
			requestInjection(provider);
			bindNamed(DataSource.class).toProvider(provider).in(SINGLETON);
			break;
		}

		// SQL ファイルリポジトリの設定
		bindNamed(SqlFileRepository.class).toProvider(
				DefaultSqlFileRepositoryProvider.class).in(Scopes.SINGLETON);
		// JDBC ロガーの設定
		bindNamed(JdbcLogger.class).toProvider(AutoJdbcLoggerProvider.class)
				.in(Scopes.SINGLETON);
		// Requires new controller の設定
		bindNamed(RequiresNewController.class).toProvider(
				DefaultRequiresNewControllerProvider.class)
				.in(Scopes.SINGLETON);

		// Dialect の設定
		DefaultDialectProvider defaultDialectProvider = new DefaultDialectProvider(
				props);
		requestInjection(defaultDialectProvider);
		bindNamed(Dialect.class).toProvider(defaultDialectProvider).in(
				Scopes.SINGLETON);

		// Doma 用 Config クラスの設定
		GuiceManagedConfig config = new GuiceManagedConfig(props);
		requestInjection(config);
		bindNamed2(Config.class).toInstance(config);

		// トランザクションの設定
		switch (transactionBinding) {
		case NONE:
			break;
		default:
			AutoTransactionProvider provider = new AutoTransactionProvider(
					props);
			requestInjection(provider);
			bindNamed2(Transaction.class).toProvider(provider).in(
					Scopes.SINGLETON);
			break;
		}

		// トランザクションインタセプタの設定
		if (transactionBinding != TransactionBinding.NONE) {
			TransactionInterceptor lti = new TransactionInterceptor(dbName);
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

	private DoceModule(String dbName, List<Class<?>> daoTypes,
			String daoPackage, String daoSubpackage, String daoSuffix,
			Properties properties) {
		this.dbName = dbName;
		this.daoTypes = daoTypes;
		this.daoPackage = daoPackage;
		this.daoSubpackage = daoSubpackage;
		this.daoSuffix = daoSuffix;
		this.properties = properties;
	}

	private <T> void bindDao(Class<T> daoType) {
		String className = ClassUtils.getImplClassName(daoType, daoPackage,
				daoSubpackage, daoSuffix);
		Class<? extends T> implClass;
		try {
			implClass = Class.forName(className).asSubclass(daoType);
		} catch (ClassNotFoundException e) {
			throw new DoceException("Dao implementation class is not found.", e);
		}

		bind(daoType).to(implClass);
	}

	private <T> LinkedBindingBuilder<T> bindNamed(Class<T> type) {
		if (dbName == null) {
			return bind(type).annotatedWith(Doma.class);
		}
		return bind(type).annotatedWith(Names.named(dbName));
	}

	private <T> LinkedBindingBuilder<T> bindNamed2(Class<T> type) {
		if (dbName == null) {
			return bind(type);
		}
		return bind(type).annotatedWith(Names.named(dbName));
	}

	/**
	 * {@link DoceModule} オブジェクトを作成するためのビルダクラスです。
	 * 
	 * @author asuka
	 */
	public static final class Builder {
		private final String dbName;
		private DataSourceBinding dataSourceBinding = null;
		private TransactionBinding transactionBinding = null;
		private final List<Class<?>> daoTypes = new ArrayList<Class<?>>();
		private String daoPackage = DEFAUL_DAO_PACKAGE;
		private String daoSubpackage = DEFAUL_DAO_SUBPACKAGE;
		private String daoSuffix = DEFAUL_DAO_SUFFIX;
		private Properties properties;

		/**
		 * 新たにオブジェクトを構築します。
		 */
		public Builder() {
			this(null);
		}

		/**
		 * 新たにオブジェクトを構築します。
		 * 
		 * @param dbName
		 *            データベース名
		 */
		public Builder(String dbName) {
			this.dbName = dbName;
		}

		/**
		 * 設定プロパティを設定します。
		 * 
		 * @param properties
		 *            設定プロパティ
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setProperties(Properties properties) {
			this.properties = properties;
			return this;
		}

		/**
		 * データソースのバインディング方法を設定します。
		 * 
		 * @param dataSourceBinding
		 *            データソースのバインディング方法
		 * @return このメソッドのレシーバオブジェクト
		 */
		@Deprecated
		public Builder setDataSourceBinding(DataSourceBinding dataSourceBinding) {
			this.dataSourceBinding = dataSourceBinding;
			return this;
		}

		/**
		 * トランザクションのバインディング方法を設定します。
		 * 
		 * @param transactionBinding
		 *            トランザクションのバインディング方法
		 * @return このメソッドのレシーバオブジェクト
		 */
		@Deprecated
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
		 * 設定に基づいて {@link DoceModule} オブジェクトを作成します。
		 * 
		 * @return {@link DoceModule} オブジェクト
		 */
		public DoceModule create() {
			if (dataSourceBinding != null) {
				if (properties == null) {
					properties = new Properties();
				}
				properties.put(DomaProperties.DS_TYPE,
						dataSourceBinding.toString());
			}
			if (transactionBinding != null) {
				if (properties == null) {
					properties = new Properties();
				}
				properties.put(DomaProperties.TX_TYPE,
						transactionBinding.toString());
			}

			return new DoceModule(dbName, daoTypes, daoPackage, daoSubpackage,
					daoSuffix, properties);
		}
	}
}
