/*
 * 作成日 : 2011/04/19
 */
package jp.osd.doma.guice.internal.logging;

import java.util.ResourceBundle;
import java.util.logging.Level;


/**
 * ログメッセージにリソースバンドルを用いる Java SE API ロガーラッパーロガーです。
 *
 * @author asuka
 */
public class JulLogger extends Logger {
	private final java.util.logging.Logger logger;

	/**
	 * ロガーを取得します。
	 *
	 * @param className
	 *            ロガー名に使用するクラス名
	 * @param bundle
	 *            ログメッセージのリソースバンドル
	 * @return ロガー
	 */
	public static JulLogger getLogger(String className, ResourceBundle bundle) {
		return new JulLogger(className, bundle);
	}

	private JulLogger(String className, ResourceBundle bundle) {
		super(bundle);
		this.logger = java.util.logging.Logger.getLogger(className);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugEnabled() {
		return logger.isLoggable(Level.FINE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(MessageCodes codes, Object... arguments) {
		if (isDebugEnabled()) {
			logger.fine(getString(codes, arguments));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(Throwable throwable, MessageCodes codes,
			Object... arguments) {
		if (logger.isLoggable(Level.SEVERE)) {
			logger.log(Level.SEVERE, getString(codes, arguments), throwable);
		}
	}
}
