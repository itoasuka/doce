/*
 * 作成日 : 2011/04/19
 */
package jp.osd.doma.guice.internal.logging;

import static org.junit.Assert.assertTrue;
import jp.osd.doma.guice.internal.Slf4jLogger;
import jp.osd.doma.guice.internal.logging.JulLogger;
import jp.osd.doma.guice.internal.logging.Logger;
import jp.osd.doma.guice.internal.logging.LoggerFactory;

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
		Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class, false);
		assertTrue(logger instanceof JulLogger);
	}
}
