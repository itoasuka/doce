package jp.osd.doce.internal.logging;

import java.text.MessageFormat;
import java.util.ResourceBundle;


/**
 * ロガーのインタフェースです。
 * <P>
 * 実装は環境に合わせて設定されます。
 *
 * @author asuka
 */
public abstract class Logger {
	private final ResourceBundle bundle;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param bundle
	 *            {@link #getString(MessageCodes, Object...)}
	 *            でメッセージの取得元となるリソースバンドル
	 */
	protected Logger(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	/**
	 * デバッグレベルのロギングが有効になっているかを取得します。
	 *
	 * @return デバッグレベルのロギングが有効になっている場合 <code>true</code>
	 */
	public abstract boolean isDebugEnabled();

	/**
	 * デバッグレベルでログを取ります。
	 *
	 * @param codes
	 *            ログメッセージのメッセージコード
	 * @param arguments
	 *            ログメッセージに埋め込む引数
	 */
	public abstract void debug(MessageCodes codes, Object... arguments);

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
	public abstract void error(Throwable throwable, MessageCodes codes,
			Object... arguments);

	/**
	 * コンストラクタ呼び出しをロギングします。
	 *
	 * @param argumentClasses コンストラクタの引数の型
	 */
	public void logConstructor(Class<?>... argumentClasses) {
		if (isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < argumentClasses.length; i++) {
				if (0 < i) {
					sb.append(", ");
				}
				sb.append(argumentClasses[i].getSimpleName());
			}

			String className = new Exception().getStackTrace()[1].getClassName();
			String[] ss = className.split("\\.");
			debug(MessageCodes.DG001, ss[ss.length - 1], sb.toString());
		}
	}

	/**
	 * メッセージリソースからメッセージを取得し、メッセージ引数を埋め込んで返します。
	 * <P>
	 * メッセージ引数の埋込みには、{@link MessageFormat} を使用します。
	 *
	 * @param codes
	 *            メッセージリソースからメッセージを取得するためのキーとなるメッセージコード
	 * @param arguments
	 *            メッセージに埋め込むメッセージ引数
	 * @return メッセージ
	 */
	protected String getString(MessageCodes codes, Object... arguments) {
		String pattern = bundle.getString(codes.toString());
		if (arguments.length == 0) {
			return pattern;
		}
		return MessageFormat.format(pattern, arguments);
	}
}
