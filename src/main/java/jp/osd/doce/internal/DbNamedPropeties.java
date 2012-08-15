package jp.osd.doce.internal;

import java.util.Properties;

import jp.osd.doce.DataSourceBinding;
import jp.osd.doce.DomaProperties;
import jp.osd.doce.TransactionBinding;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

/**
 * データベース名付きの設定プロパティー操作クラスです。
 * 
 * @author asuka
 * @since 1.0.0
 */
public class DbNamedPropeties {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DbNamedPropeties.class);
	private final String dbName;
	private final Properties properties;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param dbName
	 *            データベース名
	 * @param properties
	 *            設定プロパティー
	 */
	public DbNamedPropeties(String dbName, Properties properties) {
		this.dbName = dbName;
		this.properties = properties;
	}

	/**
	 * データベース名を取得します。
	 * 
	 * @return データベース名
	 */
	public String getDbName() {
		return dbName;
	}
	
	/**
	 * 設定プロパティーを取得します。
	 * @return 設定プロパティー
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * 設定値を文字列として取得します。
	 * 
	 * @param key
	 *            キー
	 * @return 設定値
	 */
	public String getString(String key) {
		if (properties == null) {
			return null;
		}

		String dbNamedKey = getDbNamedKey(key);
		if (properties.containsKey(dbNamedKey)) {
			return properties.getProperty(dbNamedKey);
		}
		return properties.getProperty(key);
	}

	/**
	 * 設定値を整数値として取得します。
	 * 
	 * @param key
	 *            キー
	 * @param defaultValue
	 *            キーに対応する設定値がない場合に返すデフォルト値
	 * @return 設定値
	 */
	public int getInt(String key, int defaultValue) {
		if (properties == null) {
			return defaultValue;
		}

		String dbNamedKey = getDbNamedKey(key);
		String value;
		if (properties.containsKey(dbNamedKey)) {
			value = properties.getProperty(dbNamedKey);
		} else if (properties.containsKey(key)) {
			value = properties.getProperty(key);
		} else {
			return defaultValue;
		}

		return Integer.parseInt(value);
	}

	/**
	 * 設定値を真偽値として取得します。
	 * 
	 * @param key
	 *            キー
	 * @param defaultValue
	 *            キーに対応する設定値がない場合に返すデフォルト値
	 * @return 設定値
	 */
	public boolean getBoolean(String key) {
		if (properties == null) {
			return false;
		}

		String dbNamedKey = getDbNamedKey(key);
		String value;
		if (properties.containsKey(dbNamedKey)) {
			value = properties.getProperty(dbNamedKey);
		} else if (properties.containsKey(key)) {
			value = properties.getProperty(key);
		} else {
			return false;
		}

		return Boolean.parseBoolean(value);
	}

	/**
	 * データソースのバインド方法を取得します。
	 * 
	 * @return データソースのバインド方法
	 */
	public DataSourceBinding getDataSourceBinding() {
		String value = getString(DomaProperties.DS_TYPE);
		if (value == null) {
			return DataSourceBinding.AUTO;
		}
		try {
			return DataSourceBinding.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			LOGGER.debug(MessageCodes.DG024, value);
			return DataSourceBinding.AUTO;
		}
	}
	
	/**
	 * データソースのバインド方法を設定します。
	 * @param dataSourceBinding データソースのバインド方法
	 */
	public void setDataSourceBinding(DataSourceBinding dataSourceBinding) {
		String dbNamedKey = getDbNamedKey(DomaProperties.DS_TYPE);
		if (properties.containsKey(dbNamedKey)) {
			properties.setProperty(dbNamedKey,
					dataSourceBinding.toString());
		} else {
			properties.setProperty(DomaProperties.DS_TYPE,
					dataSourceBinding.toString());
		}
	}

	/**
	 * トランザクションのバインド方法を取得します。
	 * 
	 * @return トランザクションのバインド方法
	 */
	public TransactionBinding getTransactionBinding() {
		String value = getString(DomaProperties.TX_TYPE);
		if (value == null) {
			return TransactionBinding.AUTO;
		}
		try {
			return TransactionBinding.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			LOGGER.debug(MessageCodes.DG024, value);
			return TransactionBinding.AUTO;
		}
	}
	
	/**
	 * トランザクションのバインド方法を設定します。
	 * @param transactionBinding トランザクションのバインド方法
	 */
	public void setTransactionBinding(TransactionBinding transactionBinding) {
		String dbNamedKey = getDbNamedKey(DomaProperties.TX_TYPE);
		if (properties.containsKey(dbNamedKey)) {
			properties.setProperty(dbNamedKey,
					transactionBinding.toString());
		} else {
			properties.setProperty(DomaProperties.TX_TYPE,
					transactionBinding.toString());
		}
	}

	/**
	 * 指定したキーが存在するかを取得します。
	 * 
	 * @param key
	 *            キー
	 * @return 存在した場合 <code>true</code>
	 */
	public boolean containsKey(String key) {
		if (properties == null) {
			return false;
		}
		if (properties.containsKey(getDbNamedKey(key))) {
			return true;
		}
		return properties.containsKey(key);
	}

	private String getDbNamedKey(String key) {
		if (dbName == null) {
			return key;
		}
		return dbName + "." + key;
	}
}
