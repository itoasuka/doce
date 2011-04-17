/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal;

import javax.sql.DataSource;

import com.google.inject.ImplementedBy;

/**
 * @author asuka
 */
@ImplementedBy(BasicDataSourceFactoryImpl.class)
public interface BasicDataSourceFactory {
	/**
	 * Commons DBCP の BasicDataSource によって実装されたデータソースを取得します。
	 *
	 * @return データソース
	 */
	DataSource create();
}
