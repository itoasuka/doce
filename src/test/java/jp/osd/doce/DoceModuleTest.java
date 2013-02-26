package jp.osd.doce;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.jolbox.bonecp.BoneCPDataSource;

import test.dao.FooDao;
import test.dao.FugaDao;
import test.dao.HogeDao;
import test.entity.Foo;
import test.entity.Hoge;
import test.service.Test2Service;
import test.service.TestService;

public class DoceModuleTest {
	private JdbcProperties domaProperties;

	public DoceModuleTest() {
	}

	@Before
	public void setUp() {
		domaProperties = new JdbcProperties(
				"jdbc:h2:mem:mydb;DB_CLOSE_DELAY=-1", "foo", "bar");
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
				binder.bind(Context.class).to(InitialContext.class);
			}
		};
		domaProperties
				.setTransactionBinding(TransactionBinding.LOCAL_TRANSACTION);
		Module m3 = new DoceModule.Builder().setDataSourceProperties(domaProperties)
				.setDaoPackage("").setDaoSubpackage("").setDaoSuffix("Impl")
				.addDaoTypes(HogeDao.class).addDaoTypes(list).create();

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
		domaProperties.setDataSourceBinding(DataSourceBinding.NONE);
		Module m2 = new DoceModule.Builder().setDataSourceProperties(domaProperties)
				.addDaoTypes(HogeDao.class).create();

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
		domaProperties.setTransactionBinding(TransactionBinding.NONE);
		Module m = new DoceModule.Builder().setDataSourceProperties(domaProperties)
				.addDaoTypes(HogeDao.class).create();

		Injector injector = Guice.createInjector(m);

		DataSource ds = injector.getInstance(Key.get(DataSource.class,
				Doma.class));
		((BoneCPDataSource) ds).setDefaultAutoCommit(true);
		TestService ts = injector.getInstance(TestService.class);
		ts.test();
		Hoge hoge = ts.get(1);
		assertEquals("Mike", hoge.name);
	}

	/**
	 * データベース名を指定したモジュールの組立ができるか確認する。
	 */
	@Test
	public void testConfigure_6() {
		Module m1 = new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(Context.class).to(InitialContext.class);
			}
		};
		domaProperties
				.setTransactionBinding(TransactionBinding.LOCAL_TRANSACTION);
		Module m3 = new DoceModule.Builder()
				.setDataSourceProperties("test", domaProperties)
				.setDaoPackage("").setDaoSubpackage("").setDaoSuffix("Impl")
				.addDaoTypes(FooDao.class).create();

		Injector injector = Guice.createInjector(m1, m3);

		Test2Service ts = injector.getInstance(Test2Service.class);
		ts.test();
		Foo foo = ts.get(1);
		assertEquals("Mike", foo.name);
	}
}
