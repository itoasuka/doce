package jp.osd.doma.guice;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jp.osd.doma.guice.internal.DialectProvider;

import org.junit.Test;
import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.NullRequiresNewController;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

import test.dao.FugaDao;
import test.dao.HogeDao;
import test.service.TestService;

import com.google.inject.Binder;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

public class DomaModuleTest {
	private final Properties domaProperties = new Properties();

	public DomaModuleTest() {
		domaProperties.setProperty("JDBC.url", "jdbc:h2:mem:mydb");
		domaProperties.setProperty("JDBC.username", "foo");
		domaProperties.setProperty("JDBC.password", "bar");
		domaProperties.setProperty("JDBC.loginTimeout", "10");
		domaProperties.setProperty("Doma.maxRows", "0");
		domaProperties.setProperty("Doma.fetchSize", "0");
		domaProperties.setProperty("Doma.queryTimeout", "0");
		domaProperties.setProperty("Doma.batchSize", "10");
	}

	@Test
	public void testConfigure() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(FugaDao.class);
		Module m1 = new Module() {
			@Override
			public void configure(Binder binder) {
				Names.bindProperties(binder, domaProperties);
			}
		};
		Module m2 = new SimpleDataSourceModule();
		Module m3 = new DomaModule.Builder()
				.setJdbcLoggerType(UtilLoggingJdbcLogger.class)
				.setRequiresNewControllerType(NullRequiresNewController.class)
				.setSqlFileRepositoryType(GreedyCacheSqlFileRepository.class)
				.setDialectProviderType(DialectProvider.class)
				.setDaoPackage("").setDaoSubpackage("").setDaoSuffix("Impl")
				.addDaoTypes(HogeDao.class).addDaoTypes(list).create();

		Injector injector = Guice.createInjector(m1, m2, m3);

		injector.getInstance(TestService.class);
	}

	@Test
	public void testConfigure_2() {
		Module m1 = new Module() {
			@Override
			public void configure(Binder binder) {
				Names.bindProperties(binder, domaProperties);
			}
		};
		Module m2 = new DomaModule.Builder().addDaoTypes(Serializable.class)
				.create();

		try {
			Guice.createInjector(m1, m2);
			fail("例外が発生しなかった。");
		} catch (CreationException e) {
			assertTrue(e.getCause() instanceof DomaGuiceException);
		}
	}
}
