package jp.osd.doma.guice.extensions;

import static junit.framework.Assert.assertTrue;

import java.util.Properties;

import javax.sql.DataSource;

import jp.osd.doma.extensions.jdbc.tx.AutoLocalTransactionalDataSource;
import jp.osd.doma.guice.DomaModule;
import jp.osd.doma.guice.SimpleDataSourceModule;
import jp.osd.doma.guice.internal.DomaDataSource;

import org.junit.Test;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;

public class AutoLocalTransactionalDataSourceModuleTest {
	@Test
	public void testConfigure() {
		final Properties domaProperties = new Properties();
		domaProperties.setProperty("JDBC.url", "jdbc:h2:mem:mydb");
		domaProperties.setProperty("JDBC.username", "foo");
		domaProperties.setProperty("JDBC.password", "bar");

		Module m0 = new Module() {
			@Override
			public void configure(Binder binder) {
				Names.bindProperties(binder, domaProperties);
			}
		};
		Module m1 = new SimpleDataSourceModule();
		Module m2 = new AutoLocalTransactionalDataSourceModule(m1);
		Module m3 = new DomaModule.Builder().create();
		Injector injector = Guice.createInjector(m0, m2, m3);
		DataSource ds = injector.getInstance(Key.get(DataSource.class,
				DomaDataSource.class));

		assertTrue(ds instanceof AutoLocalTransactionalDataSource);
	}
}
