package jp.osd.doma.guice.extensions;

import static jp.osd.doma.guice.BindingRule.toInstance;
import static jp.osd.doma.guice.BindingRule.toProvider;
import static junit.framework.Assert.assertEquals;

import java.util.Properties;

import jp.osd.doma.extensions.jdbc.tx.AutoUserTransactionalDataSource;
import jp.osd.doma.guice.DomaModule;
import jp.osd.doma.guice.JndiDataSourceModule;
import jp.osd.doma.guice.JndiDataSourceModuleTest.TestContextProvider;
import jp.osd.doma.guice.Transaction;
import jp.osd.doma.guice.internal.JtaUserTransaction;

import org.junit.Test;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.H2Dialect;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

public class AutoUserTransactionalDataSourceModuleTest {

	@Test
	public void testConfigure() {
		final Properties prop = new Properties();
		prop.setProperty("jndi.dataSource", "ds");
		prop.setProperty("jndi.transaction", "tx");

		Module m1 = new Module() {
			@Override
			public void configure(Binder binder) {
				Names.bindProperties(binder, prop);
			}
		};
		Module m2 = new JndiDataSourceModule.Builder()
				.setNamingContextBindingRule(
						toProvider(new TestContextProvider()))
				.jndiUserTransaction().create();
		Module m3 = new AutoUserTransactionalDataSourceModule(m2);
		Module m4 = new DomaModule.Builder().setDialectBindingRule(
				toInstance(new H2Dialect())).create();

		Injector injector = Guice.createInjector(m1, m3, m4);
		Config config = injector.getInstance(Config.class);
		assertEquals(AutoUserTransactionalDataSource.class, config
				.getDataSource().getClass());
		Transaction tx = injector.getInstance(Transaction.class);
		assertEquals(JtaUserTransaction.class, tx.getClass());
	}

}
