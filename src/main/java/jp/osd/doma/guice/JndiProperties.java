/*
 * 作成日 : 2011/04/20
 */
package jp.osd.doma.guice;

/**
 * @author asuka
 */
public class JndiProperties extends DomaProperties {
	private static final long serialVersionUID = 3923825987904530206L;
	public static final String JNDI_DATA_SOURCE = "JNDI.dataSource";
	public static final String JNDI_TRANSACTION = "JNDI.transaction";

	public JndiProperties(String dataSourceName) {
		this(dataSourceName, null);
	}

	public JndiProperties(String dataSourceName, String transactionName) {
		setJndiDataSourceName(dataSourceName);
		setJndiTransactionName(transactionName);
	}

	public void setJndiDataSourceName(String jndiDataSourceName) {
		setString(JNDI_DATA_SOURCE, jndiDataSourceName);
	}

	public String getJndiDataSourceName() {
		return getProperty(JNDI_DATA_SOURCE);
	}

	public void setJndiTransactionName(String jndiTransactionName) {
		setString(JNDI_TRANSACTION, jndiTransactionName);
	}

	public String getJndiTransactionName() {
		return getProperty(JNDI_TRANSACTION);
	}
}
