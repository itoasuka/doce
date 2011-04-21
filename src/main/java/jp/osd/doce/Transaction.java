package jp.osd.doce;

/**
 * トランザクションのインタフェースです。トランザクションの実装方式を隠蔽するため、このインタフェースの実装クラスで実際のトランザクションの実装をラップします。
 *
 * @author asuka
 */
public interface Transaction {
	/**
	 * トランザクションを開始します。
	 */
	void begin();

	/**
	 * トランザクションをコミットします。
	 */
	void commit();

	/**
	 * トランザクションをロールバックします。
	 */
	void rollback();

	/**
	 * トランザクションがアクティブであるかを取得します。
	 * 
	 * @return トランザクションがアクティブである場合 <code>true</code>
	 */
	boolean isActive();
}
