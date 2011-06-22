package jp.osd.doce;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import test.dao.FugaDao;
import test.dao.HogeDao;
import test.entity.Hoge;
import test.service.TestService;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;

public class DoceModuleTest {
	private final JdbcProperties domaProperties = new JdbcProperties(
			"jdbc:h2:mem:mydb;DB_CLOSE_DELAY=-1", "foo", "bar");

	public DoceModuleTest() {
		domaProperties.setDomaMaxRows(0);
		domaProperties.setDomaFetchSize(0);
		domaProperties.setDomaQueryTimeout(0);
		domaProperties.setDomaBatchSize(10);
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

	/**
	 * ビルダを使わずに動かせるかテストする。
	 */
	@Test
	public void testConfigure_3() {
		Module m = new DoceModule(domaProperties, HogeDao.class);

		Injector injector = Guice.createInjector(m);

		injector.getInstance(TestService.class);

		// エラーなくインスタンスを取得メソッドが呼べれば OK
	}

	/**
	 * データソースなしに設定し、事前に独自に設定したデータソースを使うことができるかテストする。
	 */
	@Test
	public void testConfigure_4() {
		DataSource ds = createMock(DataSource.class);
		replay(ds);
		final LocalTransactionalDataSource lds = new LocalTransactionalDataSource(
				ds);

		Module m1 = new AbstractModule() {
			@Override
			protected void configure() {
				bind(DataSource.class).annotatedWith(Doma.class)
						.toInstance(lds);
			}
		};
		Module m2 = new DoceModule.Builder().setProperties(domaProperties)
				.addDaoTypes(HogeDao.class)
				.setDataSourceBinding(DataSourceBinding.NONE).create();

		Injector injector = Guice.createInjector(m1, m2);

		DataSource ds2 = injector.getInstance(Key.get(DataSource.class,
				Doma.class));
		assertEquals(lds, ds2);
	}

	/**
	 * トランザクションなしで稼働するかを確認する。
	 */
	@Test
	public void testConfigure_5() {
		Module m = new DoceModule.Builder().setProperties(domaProperties)
				.addDaoTypes(HogeDao.class)
				.setTransactionBinding(TransactionBinding.NONE).create();

		Injector injector = Guice.createInjector(m);

		DataSource ds = injector.getInstance(Key.get(DataSource.class,
				Doma.class));
		((BasicDataSource) ds).setDefaultAutoCommit(true);
		TestService ts = injector.getInstance(TestService.class);
		ts.test();
		Hoge hoge = ts.get(1);
		assertEquals("Mike", hoge.name);
	}
}
