/*
 * 作成日 : 2011/04/20
 */
package jp.osd.doce;

import java.util.Properties;

/**
 * @author asuka
 */
public class DomaProperties extends Properties {
	private static final long serialVersionUID = 8253719579596477785L;
	public static final String DOMA_MAX_ROWS = "Doma.maxRows";
	public static final String DOMA_FETCH_SIZE = "Doma.fetchSize";
	public static final String DOMA_QUERY_TIMEOUT = "Doma.queryTimeout";
	public static final String DOMA_BATCH_SIZE = "Doma.batchSize";

	public void setDomaMaxRows(Integer domaMaxRows) {
		setInteger(DOMA_MAX_ROWS, domaMaxRows);
	}

	public Integer setDomaMaxRows() {
		return getInteger(DOMA_MAX_ROWS);
	}

	public void setDomaFetchSize(Integer domaFetchSize) {
		setInteger(DOMA_FETCH_SIZE, domaFetchSize);
	}

	public Integer setDomaFetchSize() {
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
