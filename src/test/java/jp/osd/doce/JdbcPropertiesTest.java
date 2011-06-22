package jp.osd.doce;

import static org.junit.Assert.*;

import org.junit.Test;

public class JdbcPropertiesTest {

	@Test
	public void testJdbcPropertiesStringString() {
		JdbcProperties prop = new JdbcProperties("url", "username");
		assertEquals("url", prop.getJdbcUrl());
		assertEquals("username", prop.getJdbcUsername());
		assertNull(prop.getJdbcPassword());
	}

	@Test
	public void testJdbcPropertiesStringStringString() {
		JdbcProperties prop = new JdbcProperties("url", "username", "password");
		assertEquals("url", prop.getJdbcUrl());
		assertEquals("username", prop.getJdbcUsername());
		assertEquals("password", prop.getJdbcPassword());
	}

	@Test
	public void testSetJdbcUrl() {
		JdbcProperties prop = new JdbcProperties("url", "username");
		prop.setJdbcUrl("hoge");
		assertEquals("hoge", prop.getJdbcUrl());
	}

	@Test
	public void testSetJdbcUsername() {
		JdbcProperties prop = new JdbcProperties("url", "username");
		prop.setJdbcUsername("hoge");
		assertEquals("hoge", prop.getJdbcUsername());
	}

	@Test
	public void testSetJdbcPassword() {
		JdbcProperties prop = new JdbcProperties("url", "username");
		prop.setJdbcPassword("hoge");
		assertEquals("hoge", prop.getJdbcPassword());
	}

	@Test
	public void testSetJdbcDriverClassName() {
		JdbcProperties prop = new JdbcProperties("url", "username");
		prop.setJdbcDriverClassName("hoge");
		assertEquals("hoge", prop.getJdbcDriverClassName());
	}

}
