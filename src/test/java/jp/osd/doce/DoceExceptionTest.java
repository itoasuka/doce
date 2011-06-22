package jp.osd.doce;

import static org.junit.Assert.*;

import org.junit.Test;

public class DoceExceptionTest {

	/**
	 * コンストラクタに指定したメッセージがスーパークラスに渡っているかを確認する。
	 */
	@Test
	public void testDoceExceptionString() {
		DoceException e = new DoceException("test");
		assertEquals("test", e.getMessage());
	}

	/**
	 * コンストラクタに指定したメッセージおよび原因例外がスーパークラスに渡っているかを確認する。
	 */
	@Test
	public void testDoceExceptionStringThrowable() {
		Exception cause = new Exception("foo");
		DoceException e= new DoceException("bar", cause);
		assertEquals("bar", e.getMessage());
		assertEquals(cause, e.getCause());
	}

}
