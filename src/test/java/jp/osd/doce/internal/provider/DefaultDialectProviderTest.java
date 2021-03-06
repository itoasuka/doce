package jp.osd.doce.internal.provider;

import static org.junit.Assert.*;

import jp.osd.doce.JdbcProperties;
import jp.osd.doce.internal.DbNamedProperties;

import org.junit.Test;
import org.seasar.doma.jdbc.dialect.Db2Dialect;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.dialect.HsqldbDialect;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class DefaultDialectProviderTest {
	public static class URLModule extends AbstractModule {
		private final String url;

		public URLModule(String url) {
			this.url = url;
		}

		@Override
		protected void configure() {
			DbNamedProperties props = new DbNamedProperties(null, new JdbcProperties(url, "sa"));
			DefaultDialectProvider provider = new DefaultDialectProvider(props);
			bind(Dialect.class).toProvider(provider);
		}
	}

	@Test
	public void testGet() {
		Injector injector = getInjector("jdbc:db2:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof Db2Dialect);
	}

	@Test
	public void testGet_2() {
		Injector injector = getInjector("jdbc:datadirect:db2:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof Db2Dialect);
	}

	@Test
	public void testGet_3() {
		Injector injector = getInjector("jdbc:as400:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof Db2Dialect);
	}

	@Test
	public void testGet_4() {
		Injector injector = getInjector("jdbc:h2:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof H2Dialect);
	}

	@Test
	public void testGet_5() {
		Injector injector = getInjector("jdbc:hsqldb:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof HsqldbDialect);
	}

	@Test
	public void testGet_6() {
		Injector injector = getInjector("jdbc:datadirect:sqlserver:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof Mssql2008Dialect);
	}

	@Test
	public void testGet_7() {
		Injector injector = getInjector("jdbc:jtds:sqlserver:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof Mssql2008Dialect);
	}

	@Test
	public void testGet_8() {
		Injector injector = getInjector("jdbc:microsoft:sqlserver:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof Mssql2008Dialect);
	}

	@Test
	public void testGet_9() {
		Injector injector = getInjector("jdbc:sqlserver:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof Mssql2008Dialect);
	}

	@Test
	public void testGet_10() {
		Injector injector = getInjector("jdbc:mysql:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof MysqlDialect);
	}

	@Test
	public void testGet_11() {
		Injector injector = getInjector("jdbc:oracle:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof OracleDialect);
	}

	@Test
	public void testGet_12() {
		Injector injector = getInjector("jdbc:datadirect:oracle:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof OracleDialect);
	}

	@Test
	public void testGet_13() {
		Injector injector = getInjector("jdbc:postgresql:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof PostgresDialect);
	}

	@Test
	public void testGet_14() {
		Injector injector = getInjector("jdbc:hoge:localhost:test");
		Dialect d = injector.getInstance(Dialect.class);

		assertTrue(d instanceof StandardDialect);
	}

	private Injector getInjector(String url) {
		return Guice.createInjector(new URLModule(url));
	}
}
