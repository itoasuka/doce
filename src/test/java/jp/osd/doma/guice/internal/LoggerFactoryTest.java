/*
 * 作成日 : 2011/04/19
 */
package jp.osd.doma.guice.internal;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author asuka
 */
public class LoggerFactoryTest {
	@Test
	public void testGetLogger() {
		Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);
		assertTrue(logger instanceof Slf4jLogger);
	}

	@Test
	public void testGetLogger_2() throws Exception {
		LoggerFactory.setJulUsing(true);
		Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);
		assertTrue(logger instanceof JulLogger);
	}
}
