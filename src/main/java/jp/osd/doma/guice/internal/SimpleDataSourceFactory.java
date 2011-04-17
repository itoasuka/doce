/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal;

import javax.sql.DataSource;

/**
 * @author asuka
 */
public interface SimpleDataSourceFactory {
	/**
	 * Doma に添付されている {@link org.seasar.doma.jdbc.SimpleDataSource} によるデータソースを取得します。
	 *
	 * @return データソース
	 */
	DataSource create();
}
