/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doce.internal.provider;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * 未設定でもデフォルトのネーミングコンテキストを返す Guice プロバイダです。
 *
 * @author asuka
 */
public class DefaultContextProvider extends DefaultProvider<Context> {
	/**
	 * 新たにオブジェクトを構築します。
	 */
	public DefaultContextProvider() {
		// 何もしない
	}

	@Override
	protected Context getDefaultValue() {
		try {
			return new InitialContext();
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
}
