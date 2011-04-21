/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doce.internal.provider;

import static jp.osd.doce.JndiProperties.JNDI_DATA_SOURCE;

import javax.naming.Context;
import javax.sql.DataSource;

import jp.osd.doce.DataSourceBinding;
import jp.osd.doce.Doma;
import jp.osd.doce.JndiProperties;
import jp.osd.doce.internal.SettingHelper;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Named;

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
 * {@link BasicDataSourceProvider} を用いてデータソースを取得して提供します。
 * <LI>上記のいずれにも当てはまらない場合、{@link SimpleDataSourceProvider} を用いてデータソースを取得して提供します。
 * </OL>
 *
 * <H4>{@link DataSourceBinding#BASIC_DATA_SOURCE} の場合</H4>
 *
 * {@link BasicDataSourceProvider} を用いてデータソースを取得して提供します。
 * {@code org.apache.commons.dbcp.BasicDataSource} クラスがクラスパス上に存在しない場合は
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
 * @see BasicDataSourceProvider
 * @see SimpleDataSourceProvider
 * @see JndiDataSourceProvider
 */
public class AutoDataSourceProvider implements Provider<DataSource> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AutoDataSourceProvider.class);

	private DataSource dataSource = null;

	private String jndiDataSourceName = null;

	private final DataSourceBinding dataSourceBinding;

	private final Injector injector;

	private final SettingHelper settingHelper;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param dataSourceBinding
	 *            データソースのバインド方法
	 * @param injector
	 *            インジェクタ
	 * @param settingHelper
	 *            設定ヘルパ
	 */
	@Inject
	public AutoDataSourceProvider(@Doma DataSourceBinding dataSourceBinding,
			Injector injector, SettingHelper settingHelper) {
		LOGGER.logConstructor(DataSourceBinding.class, Injector.class,
				SettingHelper.class);
		this.dataSourceBinding = dataSourceBinding;
		this.injector = injector;
		this.settingHelper = settingHelper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		LOGGER.debug(MessageCodes.DG002, "DataSourceBinding", dataSourceBinding);
		switch (dataSourceBinding) {
		case AUTO:
			if (dataSource == null) {
				// JNDI からのデータソースの作成を試みる
				if (!createJndiDataSource()) {
					// BasicDataSource の作成を試みる
					if (!createBasicDataSource()) {
						dataSource = injector.getInstance(
								SimpleDataSourceProvider.class).get();
					}
				}
			}
			break;
		case JNDI:
			dataSource = injector.getInstance(BasicDataSourceProvider.class)
					.get();
			break;
		case BASIC_DATA_SOURCE:
			dataSource = injector.getInstance(BasicDataSourceProvider.class)
					.get();
			break;
		case SIMPLE_DATA_SOURCE:
			dataSource = injector.getInstance(SimpleDataSourceProvider.class)
					.get();
			break;
		default:
			break;
		}
		return dataSource;
	}

	private boolean createJndiDataSource() {
		if (jndiDataSourceName != null) {
			dataSource = injector.getInstance(JndiDataSourceProvider.class)
					.get();
			return true;
		}
		LOGGER.debug(MessageCodes.DG003);
		return false;
	}

	private boolean createBasicDataSource() {
		if (settingHelper
				.isClassLoadable("org.apache.commons.dbcp.BasicDataSource")) {
			dataSource = injector.getInstance(BasicDataSourceProvider.class)
					.get();
			return true;
		}
		LOGGER.debug(MessageCodes.DG004);
		return false;
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

	/**
	 * {@link #setDataSource(DataSource)} で値を設定しない場合、JNDI
	 * を用いてデータソースをルックアップする際に使用するオブジェクトの名前を設定します。
	 *
	 * @param jndiDataSourceName
	 *            検索すデータソースの名前
	 * @see Context#lookup(String)
	 */
	@Inject(optional = true)
	public void setJndiDataSourceName(
			@Named(JNDI_DATA_SOURCE) String jndiDataSourceName) {
		this.jndiDataSourceName = jndiDataSourceName;
	}
}
