/*
 * 作成日 : 2011/04/18
 */
package jp.osd.doce;

/**
 * データソースのバインディングをどのように行うのかを指定するための列挙体です。
 * <P>
 * これは、主に {@link DoceModule} が内部行っているデータソースのバインディング方法について指定するのに用います。
 *
 * @author asuka
 * @see DoceModule.Builder#setDataSourceBinding(DataSourceBinding)
 */
public enum DataSourceBinding {
	/**
	 * バインドする実装クラスオブジェクトを自動で判定するように指定します。どのように判定されるかは {@link jp.osd.doce.internal.provider.AutoDataSourceProvider} を参照してください。
	 * @see jp.osd.doce.internal.provider.AutoDataSourceProvider
	 */
	AUTO,
	/**
	 * <a href="http://doma.seasar.org/" target="_blank">Doma</a> 付属の {@link org.seasar.doma.jdbc.SimpleDataSource} オブジェクトを実装クラスオブジェクトとしてバインディングするように指定します。
	 *
	 * @see jp.osd.doce.internal.provider.SimpleDataSourceProvider
	 */
	SIMPLE_DATA_SOURCE,
	/**
	 * <a href="http://jolbox.com/" target="_blank">BoneCP</a> の {@link com.jolbox.bonecp.BoneCPDataSource} オブジェクトを実装クラスオブジェクトとしてバインディングするように指定します。
	 *
	 * @see jp.osd.doce.internal.provider.BoneCPDataSourceProvider
	 */
	BONE_CP_DATA_SOURCE,
	/**
	 * JNDI を用いてデータソースを取得し、バインディングするように指定します。{@code "JNDI.dataSource"} の名前でデータソースのオブジェクト名を定数バインドしておく必要があります。
	 *
	 * @see jp.osd.doce.internal.provider.JndiDataSourceProvider
	 */
	JNDI,
	/**
	 * 別途名前をつけずにバインド定義しているデータソースをバインディングするように指定します。
	 */
	EXTERNAL,
	/**
	 * バインディングに関して何も設定を行わないように指定します。別途 {@link Doma} アノテーションを付加してデータソースのバインドの定義する場合に用います。
	 */
	NONE
}
