package jp.osd.doma.guice.internal;

import com.google.inject.ImplementedBy;

/**
 * 設定に関するヘルパのインタフェースです。
 *
 * @author asuka
 */
@ImplementedBy(SettingHelperImpl.class)
public interface SettingHelper {
	/**
	 * データベースへ接続するのに必要なドライバ名を取得します。
	 * 
	 * @return ドライバ名
	 */
	String getDriverClassName();
}
