package jp.osd.doma.guice;

import static jp.osd.doma.guice.BindingRule.to;
import static jp.osd.doma.guice.BindingRule.toInstance;
import static jp.osd.doma.guice.BindingRule.toProvider;
import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import jp.osd.doma.guice.internal.JtaUserTransaction;
import junit.framework.Assert;

import org.junit.Test;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SimpleDataSource;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Binder;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.name.Names;

public class JndiDataSourceModuleTest {
	@Test
	public void testJndiDataSourceModule() throws Exception {
		final Properties prop = new Properties();
		prop.setProperty("jndi.dataSource", "ds");

		Module m1 = new Module() {
			@Override
			public void configure(Binder binder) {
				Names.bindProperties(binder, prop);
			}
		};
		Module m2 = new JndiDataSourceModule.Builder()
				.setNamingContextBindingRule(
						toProvider(new TestContextProvider())).create();
		Module m3 = new DomaModule.Builder().setDialectBindingRule(
				to(H2Dialect.class)).create();

		Injector injector = Guice.createInjector(m1, m2, m3);
		Config config = injector.getInstance(Config.class);
		Assert.assertEquals(LocalTransactionalDataSource.class, config
				.getDataSource().getClass());
	}

	@Test
	public void testJndiDataSourceModule_2() throws Exception {
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
		Module m3 = new DomaModule.Builder().setDialectBindingRule(
				toInstance(new H2Dialect())).create();

		Injector injector = Guice.createInjector(m1, m2, m3);
		Config config = injector.getInstance(Config.class);
		assertEquals(SimpleDataSource.class, config.getDataSource().getClass());
		Transaction tx = injector.getInstance(Transaction.class);
		assertEquals(JtaUserTransaction.class, tx.getClass());

	}

	@Test
	public void testJndiDataSourceModule_3() throws Exception {
		final Properties prop = new Properties();
		prop.setProperty("jndi.dataSource", "ds");

		Module m1 = new Module() {
			@Override
			public void configure(Binder binder) {
				Names.bindProperties(binder, prop);
			}
		};
		Module m2 = new JndiDataSourceModule.Builder().create();
		Module m3 = new DomaModule.Builder().setDialectBindingRule(
				toInstance(new H2Dialect())).create();

		try {
			Guice.createInjector(m1, m2, m3);
		} catch (CreationException e) {
			assertEquals("Lookup error : ds", e.getCause().getMessage());
		}
	}

	public static class TestContextProvider implements Provider<Context> {
		@Override
		public Context get() {
			Context context = createMock(Context.class);
			try {
				expect(context.lookup("ds")).andReturn(new SimpleDataSource());
			} catch (NamingException e) {
				throw new RuntimeException(e);
			}
			try {
				expect(context.lookup("tx")).andReturn(
						createMock(UserTransaction.class));
			} catch (NamingException e) {
				throw new RuntimeException(e);
			}
			replay(context);

			return context;
		}
	}
}