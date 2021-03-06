package jp.osd.doce.internal;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

import test.dao.HogeDao;

public class ClassUtilsTest {
	@Test
	public void testGetImplClassName() {
		assertEquals("test.dao.HogeDaoImpl", ClassUtils.getImplClassName(HogeDao.class, "", "", "Impl"));
		assertEquals("hoge.HogeDaoImpl", ClassUtils.getImplClassName(HogeDao.class, "hoge", "", "Impl"));
		assertEquals("hoge.impl.HogeDaoImpl", ClassUtils.getImplClassName(HogeDao.class, "hoge", "impl", "Impl"));
	}

	@Test
	public void testClassUtils() throws Exception {
		Constructor<ClassUtils> c = ClassUtils.class.getDeclaredConstructor();
		assertFalse(c.isAccessible());
		c.setAccessible(true);
		c.newInstance();
	}

}
