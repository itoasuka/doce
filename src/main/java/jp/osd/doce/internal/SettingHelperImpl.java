package jp.osd.doce.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 設定に関するヘルパの実装クラスです。
 *
 * @author asuka
 */
@Singleton
public class SettingHelperImpl implements SettingHelper {
	/**
	 * 新たにオブジェクトを構築します。
	 */
	@Inject
	public SettingHelperImpl() {
	}

	@Override
	public boolean isClassLoadable(String className) {
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

}
