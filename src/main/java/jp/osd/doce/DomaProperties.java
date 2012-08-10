/*
 * 作成日 : 2011/04/20
 */
package jp.osd.doce;

import java.util.Properties;

import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author asuka
 */
public class DomaProperties extends Properties {
    private static final long serialVersionUID = 8253719579596477785L;
    public static final String DS_TYPE = "ds.type";
    public static final String TX_TYPE = "tx.type";
    public static final String DOMA_DIALECT_CLASS_NAME = "Doma.dialectClassName";
    public static final String DOMA_MAX_ROWS = "Doma.maxRows";
    public static final String DOMA_FETCH_SIZE = "Doma.fetchSize";
    public static final String DOMA_QUERY_TIMEOUT = "Doma.queryTimeout";
    public static final String DOMA_BATCH_SIZE = "Doma.batchSize";

    public void setDataSourceBinding(DataSourceBinding dataSourceBinding) {
    	setProperty(DS_TYPE, dataSourceBinding.toString());
    }
    
    public DataSourceBinding getDataSourceBinding() {
    	if (!containsKey(DS_TYPE)) {
    		return DataSourceBinding.AUTO;
    	}
    	return DataSourceBinding.valueOf(getProperty(DS_TYPE));
    }
    
    public void setTransactionBinding(TransactionBinding transactionBinding) {
    	setProperty(TX_TYPE, transactionBinding.toString());
    }
    
    public TransactionBinding getTransactionBinding() {
    	if (!containsKey(TX_TYPE)) {
    		return TransactionBinding.AUTO;
    	}
    	return TransactionBinding.valueOf(getProperty(TX_TYPE));
    }
    
    public void setDomaDialectClassName(String domaDialectClassName) {
        setString(DOMA_DIALECT_CLASS_NAME, domaDialectClassName);
    }

    public String getDomaDialectClassName() {
        return getProperty(DOMA_DIALECT_CLASS_NAME);
    }

    public void setDomaDialectClass(Class<? extends Dialect> domaDialectClass) {
        setString(DOMA_DIALECT_CLASS_NAME, domaDialectClass.getName());
    }

    public Class<? extends Dialect> getDomaDialectClass() {
        try {
            return Class.forName(getProperty(DOMA_DIALECT_CLASS_NAME)).asSubclass(
                    Dialect.class);
        } catch (ClassNotFoundException e) {
            throw new DoceException("getDomaDialectClass error", e);
        }
    }

    public void setDomaMaxRows(Integer domaMaxRows) {
        setInteger(DOMA_MAX_ROWS, domaMaxRows);
    }

    public Integer getDomaMaxRows() {
        return getInteger(DOMA_MAX_ROWS);
    }

    public void setDomaFetchSize(Integer domaFetchSize) {
        setInteger(DOMA_FETCH_SIZE, domaFetchSize);
    }

    public Integer getDomaFetchSize() {
        return getInteger(DOMA_FETCH_SIZE);
    }

    public void setDomaQueryTimeout(Integer queryTimeout) {
        setInteger(DOMA_QUERY_TIMEOUT, queryTimeout);
    }

    public Integer getDomaQueryTimeout() {
        return getInteger(DOMA_QUERY_TIMEOUT);
    }

    public void setDomaBatchSize(Integer domaBatchSize) {
        setInteger(DOMA_BATCH_SIZE, domaBatchSize);
    }

    public Integer getDomaBatchSize() {
        return getInteger(DOMA_BATCH_SIZE);
    }

    protected void setInteger(String key, Integer value) {
        if (value == null) {
            remove(key);
        } else {
            setProperty(key, value.toString());
        }
    }

    protected Integer getInteger(String key) {
        String value = getProperty(key);
        if (value == null) {
            return null;
        }
        return Integer.valueOf(getProperty(key));
    }

    protected void setString(String key, String value) {
        if (value == null) {
            remove(key);
        } else {
            setProperty(key, value);
        }
    }
}
