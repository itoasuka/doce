package jp.osd.doma.guice.internal;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import jp.osd.doma.guice.internal.logging.Logger;

import org.slf4j.LoggerFactory;

/**
 * ログメッセージにリソースバンドルを用いる SLF4J ラッパーロガーです。
 *
 * @author asuka
 */
public class Slf4jLogger implements Logger {
	private final ResourceBundle bundle;
	private final org.slf4j.Logger logger;

	/**
	 * ロガーを取得します。
	 *
	 * @param className
	 *            ロガー名に使用するクラス名
	 * @param bundle
	 *            ログメッセージのリソースバンドル
	 * @return ロガー
	 */
	public static Slf4jLogger getLogger(String className, ResourceBundle bundle) {
		org.slf4j.Logger l = LoggerFactory.getLogger(className);
		return new Slf4jLogger(l, bundle);
	}

	private Slf4jLogger(org.slf4j.Logger logger, ResourceBundle bundle) {
		this.logger = logger;
		this.bundle = bundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(MessageCodes codes, Object... arguments) {
		if (logger.isDebugEnabled()) {
			logger.debug(getString(codes, arguments));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(Throwable throwable, MessageCodes codes,
			Object... arguments) {
		if (logger.isErrorEnabled()) {
			logger.error(getString(codes, arguments), throwable);
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
