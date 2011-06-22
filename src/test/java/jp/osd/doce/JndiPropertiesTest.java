package jp.osd.doce;

import static org.junit.Assert.*;

import org.junit.Test;
import org.seasar.doma.jdbc.dialect.H2Dialect;

public class JndiPropertiesTest {

	@Test
	public void testJndiPropertiesStringClassOfQextendsDialect() {
		JndiProperties prop = new JndiProperties("dataSourceName", H2Dialect.class);
		assertEquals("dataSourceName", prop.getJndiDataSourceName());
		assertEquals(H2Dialect.class, prop.getDomaDialectClass());
		assertNull(prop.getJndiUserTransactionName());
	}

	@Test
	public void testJndiPropertiesStringClassOfQextendsDialectString() {
		JndiProperties prop = new JndiProperties("dataSourceName", H2Dialect.class, "transactionName");
		assertEquals("dataSourceName", prop.getJndiDataSourceName());
		assertEquals(H2Dialect.class, prop.getDomaDialectClass());
		assertEquals("transactionName", prop.getJndiUserTransactionName());
	}

	@Test
	public void testSetJndiDataSourceName() {
		JndiProperties prop = new JndiProperties("dataSourceName", H2Dialect.class);
		prop.setJndiDataSourceName("hoge");
		assertEquals("hoge", prop.getJndiDataSourceName());
	}

	@Test
	public void testSetJndiUserTransactionName() {
		JndiProperties prop = new JndiProperties("dataSourceName", H2Dialect.class);
		prop.setJndiUserTransactionName("hoge");
		assertEquals("hoge", prop.getJndiUserTransactionName());
	}

}
