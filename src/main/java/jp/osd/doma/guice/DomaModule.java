package jp.osd.doma.guice;

import static jp.osd.doma.guice.BindingRule.to;
import static jp.osd.doma.guice.BindingRule.toProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import jp.osd.doma.guice.internal.ClassUtils;
import jp.osd.doma.guice.internal.DialectProvider;
import jp.osd.doma.guice.internal.DomaDataSource;
import jp.osd.doma.guice.internal.DomaDataSourceProvider;
import jp.osd.doma.guice.internal.GuiceManagedConfig;
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
	private final BindingRule<? extends SqlFileRepository> sqlFileRepositoryBindingRule;
	private final BindingRule<? extends JdbcLogger> jdbcLoggerBindingRule;
	private final BindingRule<? extends RequiresNewController> requiresNewControllerBindingRule;
	private final BindingRule<? extends Dialect> dialectBindingRule;
	private final String daoPackage;
	private final String daoSubpackage;
	private final String daoSuffix;
	private final boolean useTransactionInterceptor;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure() {
		requestInjection(this);

		// データソースプロバイダの設定
		bind(DataSource.class).annotatedWith(DomaDataSource.class).toProvider(
				DomaDataSourceProvider.class);

		// SQL ファイルリポジトリの設定
		sqlFileRepositoryBindingRule.apply(bind(SqlFileRepository.class));
		// JDBC ロガーの設定
		jdbcLoggerBindingRule.apply(bind(JdbcLogger.class));
		// Requires new controller の設定
		requiresNewControllerBindingRule
				.apply(bind(RequiresNewController.class));

		// Dialect の設定
		dialectBindingRule.apply(bind(Dialect.class));

		// Doma 用 Config クラスの設定
		bind(Config.class).to(GuiceManagedConfig.class).in(Scopes.SINGLETON);

		// トランザクションインタセプタの設定
		if (useTransactionInterceptor) {
			TransactionInterceptor lti = new TransactionInterceptor();
			requestInjection(lti);
			bindInterceptor(Matchers.any(),
					Matchers.annotatedWith(Transactional.class), lti);
		}

		// Dao の設定
		for (Class<?> daoType : daoTypes) {
			bindDao(daoType);
		}
	}

	private DomaModule(
			List<Class<?>> daoTypes,
			BindingRule<? extends SqlFileRepository> sqlFileRepositoryBindingRule,
			BindingRule<? extends JdbcLogger> jdbcLoggerBindingRule,
			BindingRule<? extends RequiresNewController> requiresNewControllerBindingRule,
			BindingRule<? extends Dialect> dialectBindingRule,
			String daoPackage, String daoSubpackage, String daoSuffix,
			boolean useTransactionInterceptor) {
		this.daoTypes = daoTypes;
		this.sqlFileRepositoryBindingRule = sqlFileRepositoryBindingRule;
		this.jdbcLoggerBindingRule = jdbcLoggerBindingRule;
		this.requiresNewControllerBindingRule = requiresNewControllerBindingRule;
		this.dialectBindingRule = dialectBindingRule;
		this.daoPackage = daoPackage;
		this.daoSubpackage = daoSubpackage;
		this.daoSuffix = daoSuffix;
		this.useTransactionInterceptor = useTransactionInterceptor;
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
		private BindingRule<? extends SqlFileRepository> sqlFileRepositoryBindingRule = to(GreedyCacheSqlFileRepository.class);
		private BindingRule<? extends JdbcLogger> jdbcLoggerBindingRule = to(UtilLoggingJdbcLogger.class);
		private BindingRule<? extends RequiresNewController> requiresNewControllerBindingRule = to(NullRequiresNewController.class);
		private BindingRule<? extends Dialect> dialectBindingRule = toProvider(DialectProvider.class);
		private String daoPackage = "";
		private String daoSubpackage = "";
		private String daoSuffix = "Impl";
		private boolean useTransactionInterceptor = false;

		/**
		 * 新たにオブジェクトを構築します。
		 */
		public Builder() {
		}

		/**
		 * {@link Config} が返す SQL ファイルリポジトリをバインドするルールを設定します。
		 * <P>
		 * このメソッドで SQL ファイルリポジトリのバインドルールを指定しなかった場合、デフォルト値として
		 * {@link GreedyCacheSqlFileRepository} がバインドされます。
		 *
		 * @param sqlFileRepositoryBindingRule
		 *            SQL ファイルリポジトリのバインドルール
		 * @return このメソッドのレシーバオブジェクト
		 * @see Config#getSqlFileRepository()
		 */
		public Builder setSqlFileRepositoryBindingRule(
				BindingRule<? extends SqlFileRepository> sqlFileRepositoryBindingRule) {
			this.sqlFileRepositoryBindingRule = sqlFileRepositoryBindingRule;
			return this;
		}

		/**
		 * {@link Config} が返す JDBC ロガーをバインドするルールを設定します。
		 * <P>
		 * このメソッドで JDBC ロガーのバインドルールを指定しなかった場合、デフォルト値として
		 * {@link UtilLoggingJdbcLogger} がバインドされます。
		 *
		 * @param jdbcLoggerBindingRule
		 *            JDBC ロガーのバインドルール
		 * @return このメソッドのレシーバオブジェクト
		 * @see Config#getJdbcLogger()
		 */
		public Builder setJdbcLoggerBindingRule(
				BindingRule<? extends JdbcLogger> jdbcLoggerBindingRule) {
			this.jdbcLoggerBindingRule = jdbcLoggerBindingRule;
			return this;
		}

		/**
		 * {@link Config} が返す REQUIRES_NEW
		 * の属性をもつトランザクションを制御するコントローラをバインドするルールを設定します。
		 * <P>
		 * このメソッドで REQUIRES_NEW
		 * の属性をもつトランザクションを制御するコントローラのバインドルールを指定しなかった場合、デフォルト値として
		 * {@link NullRequiresNewController} がバインドされます。
		 *
		 * @param requiresNewControllerBindingRule
		 *            REQUIRES_NEW の属性をもつトランザクションを制御するコントローラのバインドルール
		 * @return このメソッドのレシーバオブジェクト
		 * @see Config#getRequiresNewController()
		 */
		public Builder setRequiresNewControllerBindingRule(
				BindingRule<? extends RequiresNewController> requiresNewControllerBindingRule) {
			this.requiresNewControllerBindingRule = requiresNewControllerBindingRule;
			return this;
		}

		/**
		 * {@link Dialect} オブジェクトをバインドするルールを設定します。
		 * <P>
		 * このメソッドでルールを指定しなかった場合、{@link Dialect} オブジェクトのバインドには
		 * {@link DialectProvider} が使用されます。
		 *
		 * @param dialectBindingRule
		 *            {@link Dialect} オブジェクトをバインドするルール
		 * @return このメソッドのレシーバオブジェクト
		 * @see Config#getDialect()
		 */
		public Builder setDialectBindingRule(
				BindingRule<? extends Dialect> dialectBindingRule) {
			this.dialectBindingRule = dialectBindingRule;
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
		 * アノテーション {@link Transactional} による宣言的トランザクションの利用を有効にします。
		 *
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder useTransactionInterceptor() {
			useTransactionInterceptor = true;
			return this;
		}

		/**
		 * 設定に基づいて {@link DomaModule} オブジェクトを作成します。
		 *
		 * @return {@link DomaModule} オブジェクト
		 */
		public DomaModule create() {
			return new DomaModule(daoTypes, sqlFileRepositoryBindingRule,
					jdbcLoggerBindingRule, requiresNewControllerBindingRule,
					dialectBindingRule, daoPackage, daoSubpackage, daoSuffix,
					useTransactionInterceptor);
		}
	}
}
