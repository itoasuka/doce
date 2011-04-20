/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal.provider;

import org.seasar.doma.jdbc.NullRequiresNewController;
import org.seasar.doma.jdbc.RequiresNewController;

/**
 * 未設定でもデフォルトの Requires New Controller を返す Guice プロバイダです。
 *
 * @author asuka
 */
public class DefaultRequiresNewControllerProvider extends
		DefaultProvider<RequiresNewController> {

	@Override
	protected RequiresNewController getDefaultValue() {
		return new NullRequiresNewController();
	}
}
