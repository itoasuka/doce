/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal;

import javax.sql.DataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author asuka
 */
public class DefaultDataSourceProvider implements Provider<DataSource> {
	private final  DataSource dataSource;

	@Inject
	public DefaultDataSourceProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public DataSource get() {
		return dataSource;
	}
}
