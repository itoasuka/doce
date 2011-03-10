package jp.osd.doma.guice.internal;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import jp.osd.doma.guice.DomaGuiceException;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

public class SettingHelperImplTest {
	public static class URLModule extends AbstractModule {
		private final String url;

		public URLModule(String url) {
			this.url = url;
		}

		@Override
		protected void configure() {
			bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
		}
	}

	@Test
	public void testGetDriverName() {
		Injector injector = getInjector("jdbc:db2:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("com.ibm.db2.jcc.DB2Driver", helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_2() {
		Injector injector = getInjector("jdbc:datadirect:db2:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("com.ddtek.jdbc.db2.DB2Driver", helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_3() {
		Injector injector = getInjector("jdbc:as400:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("com.ibm.as400.access.AS400JDBCDriver",
				helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_4() {
		Injector injector = getInjector("jdbc:h2:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("org.h2.Driver", helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_5() {
		Injector injector = getInjector("jdbc:hsqldb:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("org.hsqldb.jdbcDriver", helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_6() {
		Injector injector = getInjector("jdbc:datadirect:sqlserver:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("com.ddtek.jdbc.sqlserver.SQLServerDriver",
				helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_7() {
		Injector injector = getInjector("jdbc:jtds:sqlserver:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("net.sourceforge.jtds.jdbc.Driver", helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_8() {
		Injector injector = getInjector("jdbc:microsoft:sqlserver:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("com.microsoft.jdbc.sqlserver.SQLServerDriver",
				helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_9() {
		Injector injector = getInjector("jdbc:sqlserver:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("com.microsoft.sqlserver.jdbc.SQLServerDriver",
				helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_10() {
		Injector injector = getInjector("jdbc:mysql:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("com.mysql.jdbc.Driver", helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_11() {
		Injector injector = getInjector("jdbc:oracle:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("oracle.jdbc.OracleDriver", helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_12() {
		Injector injector = getInjector("jdbc:datadirect:oracle:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("com.ddtek.jdbc.oracle.OracleDriver",
				helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_13() {
		Injector injector = getInjector("jdbc:postgresql:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("org.postgresql.Driver", helper.getDriverClassName());
	}

	@Test
	public void testGetDriverName_14() {
		Injector injector = getInjector("jdbc:hoge:localhost:test");
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		try {
			helper.getDriverClassName();
			fail();
		} catch (DomaGuiceException e) {
			// OK
		}
	}

	@Test
	public void testGetDriverName_15() {
		Injector injector = Guice.createInjector(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bindConstant().annotatedWith(Names.named("DBCP.driver"))
						.to("com.example.Driver");
			}
		}, new URLModule("jdbc:hoge:localhost:test"));
		SettingHelper helper = injector.getInstance(SettingHelper.class);

		assertEquals("com.example.Driver", helper.getDriverClassName());
	}

	private Injector getInjector(String url) {
		return Guice.createInjector(new URLModule(url));
	}
}
