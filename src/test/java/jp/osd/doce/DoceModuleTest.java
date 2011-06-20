package jp.osd.doce;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.junit.Test;

import test.dao.FugaDao;
import test.dao.HogeDao;
import test.service.TestService;

import com.google.inject.Binder;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

public class DoceModuleTest {
	private final Properties domaProperties = new Properties();

	public DoceModuleTest() {
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
				binder.bind(Context.class).to(InitialContext.class);
			}
		};
		Module m3 = new DoceModule.Builder().setDaoPackage("")
				.setDaoSubpackage("").setDaoSuffix("Impl")
				.addDaoTypes(HogeDao.class).addDaoTypes(list)
				.setTransactionBinding(TransactionBinding.LOCAL_TRANSACTION)
				.create();

		Injector injector = Guice.createInjector(m1, m3);

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
		Module m2 = new DoceModule.Builder().addDaoTypes(Serializable.class)
				.create();

		try {
			Guice.createInjector(m1, m2);
			fail("例外が発生しなかった。");
		} catch (CreationException e) {
			assertTrue(e.getCause() instanceof DoceException);
		}
	}
}
