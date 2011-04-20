/*
 * 作成日 : 2011/04/19
 */
package jp.osd.doma.guice.internal;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * ログメッセージにリソースバンドルを用いる Java SE API ロガーラッパーロガーです。
 *
 * @author asuka
 */
public class JulLogger implements Logger {
	private final ResourceBundle bundle;
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
		java.util.logging.Logger l = java.util.logging.Logger.getLogger(className);
		return new JulLogger(l, bundle);
	}

	private JulLogger(java.util.logging.Logger logger, ResourceBundle bundle) {
		this.logger = logger;
		this.bundle = bundle;
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

	private String getString(MessageCodes codes, Object... arguments) {
		String pattern = bundle.getString(codes.toString());
		if (arguments.length == 0) {
			return pattern;
		}
		return MessageFormat.format(pattern, arguments);
	}
}
