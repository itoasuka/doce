package jp.osd.doce;

/**
 * Doma Guice における実行時例外のクラスです。
 * 
 * @author asuka
 */
public class DomaGuiceException extends RuntimeException {
	private static final long serialVersionUID = 8715074614894392039L;

	/**
	 * 指定された詳細メッセージを使用して、新規例外を構築します。原因は初期化されず、その後
	 * {@link Throwable#initCause(Throwable)} を呼び出すことで初期化されます。
	 * 
	 * @param message
	 *            詳細メッセージ（あとで {@link Throwable#getMessage()} メソッドで取得するために保存される）
	 */
	public DomaGuiceException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージおよび原因を使用して新しい実行時例外を構築します。
	 * <P>
	 * <code>cause</code> と関連付けられた詳細メッセージが、この実行時例外の詳細メッセージに自動的に統合されることはありません。
	 * @param message
	 *            詳細メッセージ（あとで {@link Throwable#getMessage()} メソッドで取得するために保存される）
	 * @param cause
	 *            原因 (あとで {@link Throwable#getCause()} メソッドで取得するために保存される)。(
	 *            <code>null</code> 値が許可されており、原因が存在しないか不明であることを示す)
	 */
	public DomaGuiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
