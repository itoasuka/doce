/*
 * 作成日 : 2011/04/20
 */
package jp.osd.doce;

import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author asuka
 */
public class JndiProperties extends DomaProperties {
    private static final long serialVersionUID = 3923825987904530206L;
    public static final String JNDI_DATA_SOURCE = "JNDI.dataSource";
    public static final String JNDI_USER_TRANSACTION = "JNDI.userTransaction";

    public JndiProperties(String dataSourceName, Class<? extends Dialect> dialectClass) {
        this(dataSourceName, dialectClass, null);
    }

    public JndiProperties(String dataSourceName, Class<? extends Dialect> dialectClass, String transactionName) {
        setJndiDataSourceName(dataSourceName);
        setDomaDialectClass(dialectClass);
        if (transactionName != null) {
            setJndiUserTransactionName(transactionName);
        }
        setDataSourceBinding(DataSourceBinding.JNDI);
    }

    public void setJndiDataSourceName(String jndiDataSourceName) {
        setString(JNDI_DATA_SOURCE, jndiDataSourceName);
    }

    public String getJndiDataSourceName() {
        return getProperty(JNDI_DATA_SOURCE);
    }

    public void setJndiUserTransactionName(String jndiUserTransactionName) {
        setString(JNDI_USER_TRANSACTION, jndiUserTransactionName);
    }

    public String getJndiUserTransactionName() {
        return getProperty(JNDI_USER_TRANSACTION);
    }
}
