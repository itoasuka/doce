/*
 * 作成日 : 2011/04/19
 */
package jp.osd.doce.internal.logging;

import static org.junit.Assert.assertTrue;
import jp.osd.doce.internal.logging.JulLogger;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.Slf4jLogger;

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
