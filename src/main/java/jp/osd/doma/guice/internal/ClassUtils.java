package jp.osd.doma.guice.internal;

/**
 * 動的クラス処理に関するユーティリティクラスです。
 *
 * @author asuka
 */
public class ClassUtils {
	/**
	 * 実装クラス名を取得します。
	 *
	 * @param interfaceClass 実装クラスに対するインタフェースの型
	 * @param packageName 実装クラスのパッケージ名。長さ 0 の場合は <code>interfaceClass</code> のパッケージ名を使用する。
	 * @param subpackageName 実装クラスのサブパッケージ名
	 * @param suffix 実装クラスのクラス名のサフィックス
	 * @return 実装クラス名
	 */
	public static String getImplClassName(Class<?> interfaceClass,
			String packageName, String subpackageName, String suffix) {
		StringBuilder sb = new StringBuilder();
		if (0 < packageName.length()) {
			sb.append(packageName);
		} else {
			sb.append(interfaceClass.getPackage().getName());
		}
		sb.append('.');
		if (0 < subpackageName.length()) {
			sb.append(subpackageName);
			sb.append('.');
		}
		sb.append(interfaceClass.getSimpleName());
		sb.append(suffix);

		return sb.toString();
	}

	/**
	 * 指定したクラスがクラスパス上にあり、ロード可能かを取得します。
	 *
	 * @param className クラス名
	 * @return ロード可能なとき <code>true</code>
	 */
	public static boolean isLoadable(String className) {
		try {
			Class.forName(className);
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	private ClassUtils() {
		// 何もしない
	}
}
