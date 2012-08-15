package jp.osd.doce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jp.osd.doce.internal.ClassUtils;
import jp.osd.doce.internal.DbNamedPropeties;

import com.google.inject.AbstractModule;

/**
 * Doma と Guice を連携させるための Guice モジュールクラスです。
 * <P>
 * このクラスはコンストラクタが非公開であるため、{@link DoceModule.Builder} を使用してオブジェクトを生成してください。
 * 
 * @author asuka
 */
public class DoceModule extends AbstractModule {
	private static final String DEFAUL_DAO_PACKAGE = "";
	private static final String DEFAUL_DAO_SUBPACKAGE = "";
	private static final String DEFAUL_DAO_SUFFIX = "Impl";

	private final Map<String, DbNamedPropeties> dbNamedPropetiesMap;
	private final Collection<Class<?>> daoTypes;
	private final String daoPackage;
	private final String daoSubpackage;
	private final String daoSuffix;

	public DoceModule(Properties properties, Class<?>... daoTypes) {
		this(properties, Arrays.asList(daoTypes));
	}

	public DoceModule(Properties properties, Collection<Class<?>> daoTypes) {
		this(null, properties, daoTypes);
	}

	public DoceModule(String dbName, Properties properties,
			Collection<Class<?>> daoTypes) {
		this.dbNamedPropetiesMap = new HashMap<String, DbNamedPropeties>();
		dbNamedPropetiesMap.put(dbName,
				new DbNamedPropeties(dbName, properties));
		this.daoTypes = daoTypes;
		daoPackage = DEFAUL_DAO_PACKAGE;
		daoSubpackage = DEFAUL_DAO_SUBPACKAGE;
		daoSuffix = DEFAUL_DAO_SUFFIX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure() {
		for (Map.Entry<String, DbNamedPropeties> e : dbNamedPropetiesMap
				.entrySet()) {
			install(new DoceDataSourceModule(e.getKey(), e.getValue()
					.getProperties()));
		}

		// Dao の設定
		for (Class<?> daoType : daoTypes) {
			bindDao(daoType);
		}
	}

	private DoceModule(Map<String, DbNamedPropeties> dbNamedPropetiesMap,
			List<Class<?>> daoTypes, String daoPackage, String daoSubpackage,
			String daoSuffix) {
		this.dbNamedPropetiesMap = dbNamedPropetiesMap;
		this.daoTypes = daoTypes;
		this.daoPackage = daoPackage;
		this.daoSubpackage = daoSubpackage;
		this.daoSuffix = daoSuffix;
	}

	private <T> void bindDao(Class<T> daoType) {
		String className = ClassUtils.getImplClassName(daoType, daoPackage,
				daoSubpackage, daoSuffix);
		Class<? extends T> implClass;
		try {
			implClass = Class.forName(className).asSubclass(daoType);
		} catch (ClassNotFoundException e) {
			throw new DoceException("Dao implementation class is not found.", e);
		}

		bind(daoType).to(implClass);
	}

	/**
	 * {@link DoceModule} オブジェクトを作成するためのビルダクラスです。
	 * 
	 * @author asuka
	 */
	public static final class Builder {
		private final Map<String, DbNamedPropeties> dbNamedPropetiesMap = new HashMap<String, DbNamedPropeties>();
		private DataSourceBinding dataSourceBinding = null;
		private TransactionBinding transactionBinding = null;
		private final List<Class<?>> daoTypes = new ArrayList<Class<?>>();
		private String daoPackage = DEFAUL_DAO_PACKAGE;
		private String daoSubpackage = DEFAUL_DAO_SUBPACKAGE;
		private String daoSuffix = DEFAUL_DAO_SUFFIX;

		/**
		 * 新たにオブジェクトを構築します。
		 */
		public Builder() {
		}

		/**
		 * データソースの設定を追加します。
		 * 
		 * @param properties
		 *            設定プロパティー
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setDataSourceProperties(Properties properties) {
			return setDataSourceProperties(null, properties);
		}

		/**
		 * データソースの設定を追加します。
		 * 
		 * @param dbName
		 *            データベース名
		 * @param properties
		 *            設定プロパティー
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setDataSourceProperties(String dbName,
				Properties properties) {
			dbNamedPropetiesMap.put(dbName, new DbNamedPropeties(dbName,
					properties));
			return this;
		}

		/**
		 * 設定プロパティを設定します。
		 * 
		 * @param properties
		 *            設定プロパティ
		 * @return このメソッドのレシーバオブジェクト
		 * @deprecated このメソッドは推奨されません。かわりに
		 *             {@link #setDataSourceProperties(Properties)} を使用してください。
		 */
		@Deprecated
		public Builder setProperties(Properties properties) {
			return setDataSourceProperties(properties);
		}

		/**
		 * データソースのバインディング方法を設定します。
		 * 
		 * @param dataSourceBinding
		 *            データソースのバインディング方法
		 * @return このメソッドのレシーバオブジェクト
		 */
		@Deprecated
		public Builder setDataSourceBinding(DataSourceBinding dataSourceBinding) {
			this.dataSourceBinding = dataSourceBinding;
			return this;
		}

		/**
		 * トランザクションのバインディング方法を設定します。
		 * 
		 * @param transactionBinding
		 *            トランザクションのバインディング方法
		 * @return このメソッドのレシーバオブジェクト
		 */
		@Deprecated
		public Builder setTransactionBinding(
				TransactionBinding transactionBinding) {
			this.transactionBinding = transactionBinding;
			return this;
		}

		/**
		 * Guice の管理下に置く Dao インタフェースの型を設定します。ひもづく実装クラスは動的に指定されます。
		 * <P>
		 * これにより javac のバグ（Bug ID <a
		 * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6403465"
		 * >6403465</a>）によるコンパイル時のエラーメッセージを抑制することができます。
		 * 
		 * @param daoTypes
		 *            Dao インタフェースの型
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder addDaoTypes(Class<?>... daoTypes) {
			for (Class<? extends Object> daoType : daoTypes) {
				this.daoTypes.add(daoType);
			}
			return this;
		}

		/**
		 * Guice の管理下に置く Dao インタフェースの型を設定します。ひもづく実装クラスは実行時に指定されます。
		 * <P>
		 * これにより javac のバグ（Bug ID <a
		 * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6403465"
		 * >6403465</a>）によるコンパイル時のエラーメッセージを抑制することができます。
		 * 
		 * @param daoTypes
		 *            Dao インタフェースの型のコレクション
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder addDaoTypes(Collection<Class<?>> daoTypes) {
			this.daoTypes.addAll(daoTypes);

			return this;
		}

		/**
		 * Dao の実装クラスのパッケージ名を設定します。これは Dao のインタフェースと実装クラスの実行時のひもづけに使用されます。
		 * <P>
		 * 設定しない場合、Dao インターフェース型と同じパッケージ名が使用されます。
		 * 
		 * @param daoPackage
		 *            Dao の実装クラスのパッケージ名
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setDaoPackage(String daoPackage) {
			this.daoPackage = daoPackage;
			return this;
		}

		/**
		 * Dao の実装クラスのサブパッケージ名を設定します。これは Dao のインタフェースと実装クラスの実行時のひもづけに使用されます。
		 * 
		 * @param daoSubpackage
		 *            Dao の実装クラスのサブパッケージ名
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setDaoSubpackage(String daoSubpackage) {
			this.daoSubpackage = daoSubpackage;
			return this;
		}

		/**
		 * Dao の実装クラスのクラス名のサフィックスを設定します。これは Dao のインタフェースと実装クラスの実行時のひもづけに使用されます。
		 * <P>
		 * 設定しない場合、{@literal "Impl"} が使用されます。
		 * 
		 * @param daoSuffix
		 *            Dao の実装クラスのクラス名のサフィックス
		 * @return このメソッドのレシーバオブジェクト
		 */
		public Builder setDaoSuffix(String daoSuffix) {
			this.daoSuffix = daoSuffix;
			return this;
		}

		/**
		 * 設定に基づいて {@link DoceModule} オブジェクトを作成します。
		 * 
		 * @return {@link DoceModule} オブジェクト
		 */
		public DoceModule create() {
			if (dataSourceBinding != null) {
				if (!dbNamedPropetiesMap.containsKey(null)) {
					DbNamedPropeties props = new DbNamedPropeties(null,
							new Properties());
					dbNamedPropetiesMap.put(null, props);
				}
				DbNamedPropeties props = dbNamedPropetiesMap.get(null);
				props.setDataSourceBinding(dataSourceBinding);
			}
			if (transactionBinding != null) {
				if (!dbNamedPropetiesMap.containsKey(null)) {
					DbNamedPropeties props = new DbNamedPropeties(null,
							new Properties());
					dbNamedPropetiesMap.put(null, props);
				}
				DbNamedPropeties props = dbNamedPropetiesMap.get(null);
				props.setTransactionBinding(transactionBinding);
			}

			return new DoceModule(dbNamedPropetiesMap, daoTypes, daoPackage,
					daoSubpackage, daoSuffix);
		}
	}
}
