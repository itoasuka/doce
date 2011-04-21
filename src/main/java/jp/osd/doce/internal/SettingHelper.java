package jp.osd.doce.internal;

import com.google.inject.ImplementedBy;

/**
 * 設定に関するヘルパのインタフェースです。
 *
 * @author asuka
 */
@ImplementedBy(SettingHelperImpl.class)
public interface SettingHelper {
	/**
	 * 指定したクラスがクラスパス上にあり、ロード可能かを取得します。
	 *
	 * @param className クラス名
	 * @return ロード可能なとき <code>true</code>
	 */
	boolean isClassLoadable(String className);
}
