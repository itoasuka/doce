package jp.osd.doce;

import static com.google.inject.Scopes.SINGLETON;

import java.lang.reflect.Method;
import java.util.Properties;

import javax.naming.Context;
import javax.sql.DataSource;

import jp.osd.doce.internal.DbNamedProperties;
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
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

public class DoceDataSourceModule extends AbstractModule {

	private final String dbName;

	private final DbNamedProperties properties;

	public DoceDataSourceModule(String dbName, Properties properties) {
		this.dbName = dbName;
		this.properties = new DbNamedProperties(dbName, properties);
	}

	@Override
	protected void configure() {
		// ネーミングコンテキストの設定
		bindNamingContext();

		// データソースプロバイダの設定
		bindDataSource();

		// SQL ファイルリポジトリの設定
		bindSqlFileRepository();

		// JDBC ロガーの設定
		bindJdbcLogger();

		// Requires new controller の設定
		bindRequiresNewController();

		// Dialect の設定
		bindDialect();

		// Doma 用 Config クラスの設定
		bindConfig();

		// トランザクションの設定
		bindTransaction();

		// トランザクションインタセプタの設定
		bindTransactionInterceptor();
	}

	protected void bindNamingContext() {
		bindNamed(Context.class).toProvider(DefaultContextProvider.class).in(
				SINGLETON);
	}

	protected void bindDataSource() {
		switch (properties.getDataSourceBinding()) {
		case NONE:
			break;
		default:
			AutoDataSourceProvider provider = new AutoDataSourceProvider(
					properties);
			requestInjection(provider);
			bindNamed(DataSource.class).toProvider(provider).in(SINGLETON);
			break;
		}
	}

	protected void bindSqlFileRepository() {
		bindNamed(SqlFileRepository.class).toProvider(
				DefaultSqlFileRepositoryProvider.class).in(Scopes.SINGLETON);
	}

	protected void bindJdbcLogger() {
		bindNamed(JdbcLogger.class).toProvider(AutoJdbcLoggerProvider.class)
				.in(Scopes.SINGLETON);
	}

	protected void bindRequiresNewController() {
		bindNamed(RequiresNewController.class).toProvider(
				DefaultRequiresNewControllerProvider.class)
				.in(Scopes.SINGLETON);
	}

	protected void bindDialect() {
		DefaultDialectProvider defaultDialectProvider = new DefaultDialectProvider(
				properties);
		requestInjection(defaultDialectProvider);
		bindNamed(Dialect.class).toProvider(defaultDialectProvider).in(
				Scopes.SINGLETON);
	}

	protected void bindConfig() {
		GuiceManagedConfig config = new GuiceManagedConfig(properties);
		requestInjection(config);
		if (dbName == null) {
			bind(Config.class).toInstance(config);
		} else {
			bind(Config.class).annotatedWith(Names.named(dbName)).toInstance(
					config);
		}
	}

	protected void bindTransaction() {
		switch (properties.getTransactionBinding()) {
		case NONE:
			break;
		default:
			AutoTransactionProvider provider = new AutoTransactionProvider(
					properties);
			requestInjection(provider);
			if (dbName == null) {
				bind(Transaction.class).toProvider(provider).in(
						Scopes.SINGLETON);
			} else {
				bind(Transaction.class).annotatedWith(Names.named(dbName))
						.toProvider(provider).in(Scopes.SINGLETON);
			}
			break;
		}
	}

	protected void bindTransactionInterceptor() {
		if (properties.getTransactionBinding() != TransactionBinding.NONE) {
			TransactionInterceptor lti = new TransactionInterceptor(dbName);
			requestInjection(lti);

			bindInterceptor(Matchers.any(), new AbstractMatcher<Method>() {
				@Override
				public boolean matches(Method method) {
					Transactional t = method.getAnnotation(Transactional.class);

					return matchTransaction(t);
				}
			}, lti);
			bindInterceptor(new AbstractMatcher<Class<?>>() {
				@Override
				public boolean matches(Class<?> clazz) {
					Transactional t = clazz.getAnnotation(Transactional.class);

					return matchTransaction(t);
				}
			}, Matchers.any(), lti);
		}
	}

	protected <T> LinkedBindingBuilder<T> bindNamed(Class<T> type) {
		if (dbName == null) {
			return bind(type).annotatedWith(Doma.class);
		}
		return bind(type).annotatedWith(Names.named(dbName));
	}

	private boolean matchTransaction(Transactional t) {
		if (t == null) {
			return false;
		}
		if (t.value().length == 0) {
			return dbName == null;
		}
		for (String dn : t.value()) {
			if (dbName == null) {
				if (dn == null) {
					return true;
				}
			} else {
				if (dbName.equals(dn)) {
					return true;
				}
			}
		}
		return false;
	}
}
