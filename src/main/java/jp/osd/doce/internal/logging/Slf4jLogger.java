package jp.osd.doce.internal.logging;

import java.util.ResourceBundle;

import org.slf4j.LoggerFactory;

/**
 * ログメッセージにリソースバンドルを用いる SLF4J ラッパーロガーです。
 *
 * @author asuka
 */
public class Slf4jLogger extends Logger {
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
        return new Slf4jLogger(className, bundle);
    }

    private Slf4jLogger(String className, ResourceBundle bundle) {
        super(bundle);
        this.logger = LoggerFactory.getLogger(className);
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
    public void info(MessageCodes codes, Object... arguments) {
        if (logger.isInfoEnabled()) {
            logger.info(getString(codes, arguments));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(MessageCodes codes, Object... arguments) {
        if (logger.isWarnEnabled()) {
            logger.warn(getString(codes, arguments));
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

}
