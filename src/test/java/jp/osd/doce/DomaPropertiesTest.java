package jp.osd.doce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;

public class DomaPropertiesTest {
	private DomaProperties properties;

	@Before
	public void setUp() {
		properties = new DomaProperties();
	}

	@Test
	public void testDomaDialectClassName() {
		String s = H2Dialect.class.getName();
		properties.setDomaDialectClassName(s);
		assertEquals(s, properties.getDomaDialectClassName());
	}

	@Test
	public void testDomaDialectClass() {
		Class<? extends Dialect> c = H2Dialect.class;
		properties.setDomaDialectClass(c);
		assertEquals(c, properties.getDomaDialectClass());
	}

	@Test
	public void testGetDomaDialectClass() {
		// 存在しないクラス名を設定。
		properties.setDomaDialectClassName("Hoge");

		try {
			properties.getDomaDialectClass();
		} catch (DoceException e) {
			assertEquals(ClassNotFoundException.class, e.getCause().getClass());
		}
	}
	@Test
	public void testDomaMaxRows() {
		properties.setDomaMaxRows(123);
		assertEquals(Integer.valueOf(123), properties.getDomaMaxRows());
	}

	@Test
	public void testDomaFetchSize() {
		properties.setDomaFetchSize(123);
		assertEquals(Integer.valueOf(123), properties.getDomaFetchSize());
	}

	@Test
	public void testSetDomaQueryTimeout() {
		properties.setDomaQueryTimeout(123);
		assertEquals(Integer.valueOf(123), properties.getDomaQueryTimeout());
	}

	@Test
	public void testSetDomaBatchSize() {
		properties.setDomaBatchSize(123);
		assertEquals(Integer.valueOf(123), properties.getDomaBatchSize());
	}

	@Test
	public void testSetInteger() {
		properties.setDomaBatchSize(123);
		assertTrue(properties.containsKey(DomaProperties.DOMA_BATCH_SIZE));
		properties.setDomaBatchSize(null);
		assertFalse(properties.containsKey(DomaProperties.DOMA_BATCH_SIZE));
	}

	@Test
	public void testGetInteger() {
		assertNull(properties.getDomaBatchSize());
	}

	@Test
	public void setString() {
		properties.setDomaDialectClassName("Hoge");
		assertTrue(properties.containsKey(DomaProperties.DOMA_DIALECT_CLASS_NAME));
		properties.setDomaDialectClassName(null);
		assertFalse(properties.containsKey(DomaProperties.DOMA_DIALECT_CLASS_NAME));
	}
}
