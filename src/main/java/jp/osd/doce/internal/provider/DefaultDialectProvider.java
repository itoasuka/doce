package jp.osd.doce.internal.provider;

import static jp.osd.doce.DomaProperties.DOMA_DIALECT_CLASS_NAME;
import static jp.osd.doce.JdbcProperties.JDBC_URL;
import jp.osd.doce.DoceException;
import jp.osd.doce.internal.JdbcUtils;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * Dialect オブジェクトを提供するプロバイダクラスです。
 * <P>
 * すでに Dialect オブジェクトがバインドされている場合はそれを提供します。
 * <P>
 * そうでない場合、接続先の URL から適切な Dialect を判断し、Dialect オブジェクトを提供します。未知の URL の場合、
 * {@link StandardDialect} のオブジェクト を提供します。提供される Dialect
 * オブジェクトはすべてデフォルトコンストラクタで生成されます。
 *
 * @author asuka
 */
public class DefaultDialectProvider implements Provider<Dialect> {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultDialectProvider.class);

    private String jdbcUrl = null;

    private Dialect dialect = null;

    private String dialectClassName = null;

    /**
     * 新たにオブジェクトを構築します。
     */
    public DefaultDialectProvider() {
        LOGGER.logConstructor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dialect get() {
        if (dialect != null) {
            LOGGER.info(MessageCodes.DG011);
            return dialect;
        }
        if (dialectClassName != null) {
            try {
                LOGGER.info(MessageCodes.DG012, dialectClassName);
                return Class.forName(dialectClassName)
                        .asSubclass(Dialect.class).newInstance();
            } catch (Exception e) {
                LOGGER.error(e, MessageCodes.DG013, e);
                throw new DoceException(dialectClassName + " newInstance error", e);
            }
        }
        if (jdbcUrl == null) {
            LOGGER.info(MessageCodes.DG014);
            return new StandardDialect();
        }
        Dialect d = JdbcUtils.getDialect(jdbcUrl);
        LOGGER.info(MessageCodes.DG015, jdbcUrl, d.getClass());
        return d;
    }

    /**
     * Dialect を判定するための JDBC 接続 URL を設定します。
     *
     * @param jdbcUrl
     *            JDBC 接続 URL
     */
    @Inject(optional = true)
    public void setJdbcUrl(@Named(JDBC_URL) String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    /**
     * プロバイダとして提供するダイアレクトを設定します。
     *
     * @param dialect
     *            ダイアレクト
     */
    @Inject(optional = true)
    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    /**
     * プロバイダとして提供するダイアレクトのクラス名を設定します。
     * <P>
     * {@link #setDialect(Dialect)} でダイアレクトが設定されなかった場合、この値からダイアレクトを生成して提供します。
     *
     * @param dialectClassName
     *            ダイアレクトのクラス名
     */
    @Inject(optional = true)
    public void setDomaDialectClassName(
            @Named(DOMA_DIALECT_CLASS_NAME) String dialectClassName) {
        this.dialectClassName = dialectClassName;
    }
}
