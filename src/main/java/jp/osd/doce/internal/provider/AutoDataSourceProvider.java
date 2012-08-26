/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doce.internal.provider;

import static jp.osd.doce.JndiProperties.JNDI_DATA_SOURCE;

import java.util.Properties;

import javax.sql.DataSource;

import jp.osd.doce.DataSourceBinding;
import jp.osd.doce.internal.DbNamedPropeties;
import jp.osd.doce.internal.SettingHelper;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

/**
 * {@link DataSource} の実装クラスオブジェクトを提供する Guice プロバイダです。
 * <P>
 * このプロバイダは、コンストラクタ引数 {@code dataSourceBinding} の値によって提供するオブジェクトが変わります。
 * 
 * <h4>{@link DataSourceBinding#AUTO} の場合</h4>
 * 
 * 提供される実装クラスオブジェクトは以下のように決定されます。
 * <OL>
 * <LI>{@link #setDataSource(DataSource)} で設定されたデータソースがあればそれを提供します。
 * <LI>{@link #setJndiDataSourceName(String)} で設定された値があれば
 * {@link JndiDataSourceProvider} を用いてデータソースを取得して提供します。
 * <LI>{@code org.apache.commons.dbcp.BasicDataSource} クラスがクラスパス上に存在するとき、
 * {@link BoneCPDataSourceProvider} を用いてデータソースを取得して提供します。
 * <LI>上記のいずれにも当てはまらない場合、{@link SimpleDataSourceProvider} を用いてデータソースを取得して提供します。
 * </OL>
 * 
 * <H4>{@link DataSourceBinding#BONE_CP_DATA_SOURCE} の場合</H4>
 * 
 * {@link BoneCPDataSourceProvider} を用いてデータソースを取得して提供します。
 * {@code com.jolbox.bonecp.BoneCPDataSource} クラスがクラスパス上に存在しない場合は
 * {@link ClassNotFoundException} がスローされます。
 * 
 * <H4>{@link DataSourceBinding#SIMPLE_DATA_SOURCE} の場合</H4>
 * 
 * {@link SimpleDataSourceProvider} を用いてデータソースを取得して提供します。
 * 
 * <H4>{@link DataSourceBinding#JNDI} の場合</H4>
 * 
 * {@link JndiDataSourceProvider} を用いてデータソースを取得して提供します。
 * 
 * <H4>上記以外の場合</H4>
 * 
 * {@link #setDataSource(DataSource)} で設定された値を提供します。
 * 
 * @author asuka
 * @see BoneCPDataSourceProvider
 * @see SimpleDataSourceProvider
 * @see JndiDataSourceProvider
 */
public class AutoDataSourceProvider implements Provider<DataSource> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AutoDataSourceProvider.class);

	private final DbNamedPropeties properties;

	private DataSource dataSource = null;

	@Inject
	private Injector injector;

	@Inject
	private SettingHelper settingHelper;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param properties
	 *            データベース名付き設定プロパティ
	 */
	public AutoDataSourceProvider(DbNamedPropeties properties) {
		LOGGER.logConstructor(String.class, Properties.class);
		this.properties = properties;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		DataSourceBinding dataSourceBinding = properties.getDataSourceBinding();
		LOGGER.debug(MessageCodes.DG002, "DataSourceBinding", dataSourceBinding);
		switch (dataSourceBinding) {
		case AUTO:
			if (dataSource == null) {
				// JNDI からのデータソースの作成を試みる
				if (!createJndiDataSource()) {
					// BasicDataSource の作成を試みる
					if (!createBoneCPDataSource()) {
						createSimpleDataSource();
					}
				}
			}
			break;
		case JNDI:
			createJndiDataSource();
			break;
		case BONE_CP_DATA_SOURCE:
			createBoneCPDataSource();
			break;
		case SIMPLE_DATA_SOURCE:
			createSimpleDataSource();
			break;
		default:
			break;
		}
		assert (dataSource != null);
		return dataSource;
	}

	private boolean createJndiDataSource() {
		String jndiDataSourceName = properties.getString(JNDI_DATA_SOURCE);
		if (jndiDataSourceName != null) {
			dataSource = injector.getInstance(JndiDataSourceProvider.class)
					.get();
			return true;
		}
		LOGGER.debug(MessageCodes.DG003, properties.getDbName());
		return false;
	}

	private boolean createBoneCPDataSource() {
		if (settingHelper.isClassLoadable("com.jolbox.bonecp.BoneCPDataSource")) {
			BoneCPDataSourceProvider boneCPDataSourceProvider = new BoneCPDataSourceProvider(
					properties);
			dataSource = boneCPDataSourceProvider.get();
			return true;
		}
		LOGGER.debug(MessageCodes.DG004, properties.getDbName());
		return false;
	}
	
	private boolean createSimpleDataSource() {
		SimpleDataSourceProvider simpleDataSourceProvider = new SimpleDataSourceProvider(
				properties);
		dataSource = simpleDataSourceProvider.get();
		return true;
	}

	/**
	 * {@link #get()} で返すデータソースを設定します。このメソッドで設定した値は、すべに優先して {@link #get()}
	 * の戻り値として採用されます。
	 * 
	 * @param dataSource
	 *            データソース
	 */
	@Inject(optional = true)
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
