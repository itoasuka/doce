package jp.osd.doma.guice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Provider;
import javax.sql.DataSource;

import jp.osd.doma.guice.internal.ClassUtils;
import jp.osd.doma.guice.internal.GuiceManagedConfig;
import jp.osd.doma.guice.internal.LocalTransaction;
import jp.osd.doma.guice.internal.TransactionInterceptor;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.NullRequiresNewController;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;
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
	private final List<Class<?>> daoTypes;
	private final Class<? extends Provider<? extends DataSource>> dataSourceProviderType;
	private final Class<? extends SqlFileRepository> sqlFileRepositoryType;
	private final Class<? extends JdbcLogger> jdbcLoggerType;
	private final Class<? extends RequiresNewController> requiresNewControllerType;
	private final Class<? extends Provider<Dialect>> dialectProviderType;
	private final String daoPackage;
	private final String daoSubpackage;
	private final String daoSuffix;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure() {
		requestInjection(this);

		// データソースプロバイダの設定
		bind(DataSource.class).toProvider(dataSourceProviderType).in(
				Scopes.SINGLETON);
		// SQL ファイルリポジトリの設定
		bind(SqlFileRepository.class).to(sqlFileRepositoryType).in(
				Scopes.SINGLETON);
		// JDBC ロガーの設定
		bind(JdbcLogger.class).to(jdbcLoggerType).in(Scopes.SINGLETON);
		// Requires new controller の設定
		bind(RequiresNewController.class).to(requiresNewControllerType).in(
				Scopes.SINGLETON);

		// Dialect の設定
		bind(Dialect.class).toProvider(dialectProviderType);

		// Doma 用 Config クラスの設定
		bind(Config.class).to(GuiceManagedConfig.class).in(Scopes.SINGLETON);

		// トランザクションの設定
		bind(Transaction.class).to(LocalTransaction.class);

		// ローカルトランザクションインタセプタの設定
		TransactionInterceptor lti = new TransactionInterceptor();
		requestInjection(lti);
		bindInterceptor(Matchers.any(),
				Matchers.annotatedWith(Transactional.class), lti);

		// Dao の設定
		for (Class<?> daoType : daoTypes) {
			bindDao(daoType);
		}
	}

	private DomaModule(
			List<Class<?>> daoTypes,
			Class<? extends Provider<? extends DataSource>> dataSourceProviderType,
			Class<? extends SqlFileRepository> sqlFileRepositoryType,
			Class<? extends JdbcLogger> jdbcLoggerType,
			Class<? extends RequiresNewController> requiresNewControllerType,
			Class<? extends Provider<Dialect>> dialectProviderType,
			String daoPackage, String daoSubpackage, String daoSuffix) {
		this.daoTypes = daoTypes;
		this.dataSourceProviderType = dataSourceProviderType;
		this.sqlFileRepositoryType = sqlFileRepositoryType;
		this.jdbcLoggerType = jdbcLoggerType;
		this.requiresNewControllerType = requiresNewControllerType;
		this.dialectProviderType = dialectProviderType;
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
		private final List<Class<?>> daoTypes = new ArrayList<Class<?>>();
		private Class<? extends Provider<? extends DataSource>> dataSourceProviderType = SimpleDataSouceProvider.class;
		private Class<? extends SqlFileRepository> sqlFileRepositoryType = GreedyCacheSqlFileRepository.class;
		private Class<? extends JdbcLogger> jdbcLoggerType = UtilLoggingJdbcLogger.class;
		private Class<? extends RequiresNewController> requiresNewControllerType = NullRequiresNewController.class;
		private Class<? extends Provider<Dialect>> dialectProviderType = DialectProvider.class;
		private String daoPackage = "";
		private String daoSubpackage = "";
		private String daoSuffix = "Impl";

		/**
		 * 新たにオブジェクトを構築します。
		 */
		public Builder() {
		}

		/**
		 * {@link DataSource} オブジェクトを提供するプロバイダの型を設定します。
		 * <P>
		 * このメソッドでプロバイダの型を指定しなかった場合、{@link DataSource} オブジェクトの提供には
		 * {@link SimpleDataSouceProvider} が使用されます。
		 * 
		 * @param dataSourceProviderType
		 *            {@link DataSource} オブジェクトを提供するプロバイダの型を設定
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setDataSourceProviderType(
				Class<? extends Provider<? extends DataSource>> dataSourceProviderType) {
			this.dataSourceProviderType = dataSourceProviderType;
			return this;
		}

		/**
		 * {@link Config} が返す SQL ファイルリポジトリの型を設定します。
		 * <P>
		 * このメソッドで SQL ファイルリポジトリの型を指定しなかった場合、デフォルト値として
		 * {@link GreedyCacheSqlFileRepository} が使用されます。
		 * 
		 * @param sqlFileRepositoryType
		 *            SQL ファイルリポジトリの型
		 * @return このメソッドのレシーバオブジェクト
		 * @see Config#getSqlFileRepository()
		 */
		public Builder setSqlFileRepositoryType(
				Class<? extends SqlFileRepository> sqlFileRepositoryType) {
			this.sqlFileRepositoryType = sqlFileRepositoryType;
			return this;
		}

		/**
		 * {@link Config} が返す JDBC ロガーの型を設定します。
		 * <P>
		 * このメソッドで JDBC ロガーの型を指定しなかった場合、デフォルト値として {@link UtilLoggingJdbcLogger}
		 * が使用されます。
		 * 
		 * @param jdbcLoggerType
		 *            JDBC ロガーの型
		 * @return このメソッドのレシーバオブジェクト
		 * @see Config#getJdbcLogger()
		 */
		public Builder setJdbcLoggerType(
				Class<? extends JdbcLogger> jdbcLoggerType) {
			this.jdbcLoggerType = jdbcLoggerType;
			return this;
		}

		/**
		 * {@link Config} が返す REQUIRES_NEW の属性をもつトランザクションを制御するコントローラの型を設定します。
		 * <P>
		 * このメソッドで REQUIRES_NEW の属性をもつトランザクションを制御するコントローラの型を指定しなかった場合、デフォルト値として
		 * {@link NullRequiresNewController} が使用されます。
		 * 
		 * @param requiresNewControllerType
		 *            REQUIRES_NEW の属性をもつトランザクションを制御するコントローラの型
		 * @return このメソッドのレシーバオブジェクト
		 * @see Config#getRequiresNewController()
		 */
		public Builder setRequiresNewControllerType(
				Class<? extends RequiresNewController> requiresNewControllerType) {
			this.requiresNewControllerType = requiresNewControllerType;
			return this;
		}

		/**
		 * {@link Dialect} オブジェクトを提供するプロバイダの型を設定します。
		 * <P>
		 * このメソッドでプロバイダの型を指定しなかった場合、{@link Dialect} オブジェクトの提供には
		 * {@link DialectProvider} が使用されます。
		 * 
		 * @param dialectProviderType
		 *            {@link Dialect} オブジェクトを提供するプロバイダの型を設定
		 * @return このメソッドのレシーバオブジェクト
		 * @see Config#getDialect()
		 */
		public Builder setDialectProviderType(
				Class<? extends Provider<Dialect>> dialectProviderType) {
			this.dialectProviderType = dialectProviderType;
			return this;
		}

		/**
		 * Guice の管理下に置く Dao インタフェースの型を設定します。ひもづく実装クラスは動的に指定されます。
		 * <P>
		 * これにより javac のバグ（Bug ID <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6403465">6403465</a>）によるコンパイル時のエラーメッセージを抑制することができます。
		 * 
		 * @param daoTypes Dao インタフェースの型
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
		 * これにより javac のバグ（Bug ID <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6403465">6403465</a>）によるコンパイル時のエラーメッセージを抑制することができます。
		 * 
		 * @param daoTypes Dao インタフェースの型のコレクション
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
		 * @param daoPackage Dao の実装クラスのパッケージ名
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setDaoPackage(String daoPackage) {
			this.daoPackage = daoPackage;
			return this;
		}

		/**
		 * Dao の実装クラスのサブパッケージ名を設定します。これは Dao のインタフェースと実装クラスの実行時のひもづけに使用されます。
		 * 
		 * @param daoSubpackage Dao の実装クラスのサブパッケージ名
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
		 * @param daoSuffix Dao の実装クラスのクラス名のサフィックス
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
			return new DomaModule(daoTypes, dataSourceProviderType,
					sqlFileRepositoryType, jdbcLoggerType,
					requiresNewControllerType, dialectProviderType, daoPackage,
					daoSubpackage, daoSuffix);
		}
	}
}
