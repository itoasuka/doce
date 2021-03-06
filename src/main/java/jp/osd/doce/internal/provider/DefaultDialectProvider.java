package jp.osd.doce.internal.provider;

import static jp.osd.doce.DomaProperties.DOMA_DIALECT_CLASS_NAME;
import static jp.osd.doce.JdbcProperties.JDBC_URL;

import jp.osd.doce.DoceException;
import jp.osd.doce.internal.DbNamedProperties;
import jp.osd.doce.internal.JdbcUtils;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

import com.google.inject.Inject;
import com.google.inject.Provider;

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
    
    private final String dbName;
    
    private String jdbcUrl = null;

    private Dialect dialect = null;

    private String dialectClassName = null;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param properties
	 *            データベース名付き設定プロパティ
	 */
    public DefaultDialectProvider(DbNamedProperties properties) {
        LOGGER.logConstructor(DbNamedProperties.class);
        
        dbName = properties.getDbName();
        jdbcUrl = properties.getString(JDBC_URL);
        dialectClassName = properties.getString(DOMA_DIALECT_CLASS_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dialect get() {
        if (dialect != null) {
            LOGGER.info(MessageCodes.DG011, dbName);
            return dialect;
        }
        if (dialectClassName != null) {
            try {
                LOGGER.info(MessageCodes.DG012, dbName, dialectClassName);
                return Class.forName(dialectClassName)
                        .asSubclass(Dialect.class).newInstance();
            } catch (Exception e) {
                LOGGER.error(e, MessageCodes.DG013, dbName);
                throw new DoceException(dialectClassName + " newInstance error", e);
            }
        }
        if (jdbcUrl == null) {
            LOGGER.info(MessageCodes.DG014, dbName);
            return new StandardDialect();
        }
        Dialect d = JdbcUtils.getDialect(jdbcUrl);
        LOGGER.info(MessageCodes.DG015, dbName, jdbcUrl, d.getClass());
        return d;
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
}
