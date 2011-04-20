package jp.osd.doma.guice.internal;

/**
 * ロガーのインタフェースです。
 * <P>
 * 実装は環境に合わせて設定されます。
 *
 * @author asuka
 */
public interface Logger {

	/**
	 * デバッグレベルのロギングが有効になっているかを取得します。
	 *
	 * @return デバッグレベルのロギングが有効になっている場合 <code>true</code>
	 */
	boolean isDebugEnabled();

	/**
	 * デバッグレベルでログを取ります。
	 *
	 * @param codes
	 *            ログメッセージのメッセージコード
	 * @param arguments
	 *            ログメッセージに埋め込む引数
	 */
	void debug(MessageCodes codes, Object... arguments);

	/**
	 * エラーレベルでログを取ります。
	 *
	 * @param throwable
	 *            エラーの原因となった例外
	 * @param codes
	 *            ログメッセージのメッセージコード
	 * @param arguments
	 *            ログメッセージに埋め込む引数
	 */
	void error(Throwable throwable, MessageCodes codes, Object... arguments);

}
