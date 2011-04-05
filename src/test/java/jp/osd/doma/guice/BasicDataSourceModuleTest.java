package jp.osd.doma.guice;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import java.util.Properties;

import org.junit.Test;

import test.dao.HogeDao;
import test.entity.Hoge;
import test.service.TestService;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

public class BasicDataSourceModuleTest {
	private final Properties domaProperties = new Properties();

	public BasicDataSourceModuleTest() {
		domaProperties.setProperty("JDBC.url", "jdbc:h2:mem:mydb");
		domaProperties.setProperty("JDBC.username", "foo");
		domaProperties.setProperty("JDBC.password", "bar");
		domaProperties.setProperty("DBCP.initialSize", "3");
		domaProperties.setProperty("DBCP.maxActive", "3");
		domaProperties.setProperty("DBCP.maxIdle", "3");
		domaProperties.setProperty("DBCP.maxOpenPreparedStatements", "3");
		domaProperties.setProperty("DBCP.maxWait", "1000");
		domaProperties.setProperty("DBCP.minEvictableIdleTimeMillis", "1000");
		domaProperties.setProperty("DBCP.minIdle", "3");
		domaProperties.setProperty("DBCP.poolPreparedStatements", "true");
	}

	@Test
	public void testBasicDataSourceProvider() {
		Module m1 = new Module() {
			@Override
			public void configure(Binder binder) {
				Names.bindProperties(binder, domaProperties);
			}
		};
		Module m2 = new BasicDataSourceModule();
		Module m3 = new DomaModule.Builder().addDaoTypes(HogeDao.class)
				.create();

		Injector injector = Guice.createInjector(m1, m2, m3);
		TestService service = injector.getInstance(TestService.class);
		service.test();
		Hoge hoge = service.get(1);
		assertEquals("Mike", hoge.name);
		service.dispose();
	}

	@Test
	public void testBasicDataSourceProvider_2() {
		Module m1 = new Module() {
			@Override
			public void configure(Binder binder) {
				Names.bindProperties(binder, domaProperties);
			}
		};
		Module m2 = new BasicDataSourceModule();
		Module m3 = new DomaModule.Builder().addDaoTypes(HogeDao.class)
				.create();

		Injector injector = Guice.createInjector(m1, m2, m3);
		TestService service = injector.getInstance(TestService.class);
		try {
			service.throwException();
		} catch (Exception e) {
			// OK
		}
		Hoge hoge = service.get(1);
		assertNull(hoge);
		service.dispose();
	}
}
