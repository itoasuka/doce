package jp.osd.doma.guice.internal;

import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.SqlFileRepository;

/**
 * 未設定でもデフォルトの SQL ファイルリポジトリを返す Guice プロバイダです。
 * @author asuka
 */
public class DefaultSqlFileRepositoryProvider extends
		DefaultProvider<SqlFileRepository> {

	@Override
	protected SqlFileRepository getDefaultValue() {
		return new GreedyCacheSqlFileRepository();
	}

}
