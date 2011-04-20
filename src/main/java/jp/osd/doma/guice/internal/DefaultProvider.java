/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * アノテーションを伴わずにバインド定義されたオブジェクトをアノテーションを伴った定義に適用するための Guice プロバイダの基底クラスです。
 *
 * @param <T>
 *            このプロバイダが提供するオブジェクトの型
 * @author asuka
 */
public abstract class DefaultProvider<T> implements Provider<T> {
	private T object;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get() {
		if (object == null) {
			return getDefaultValue();
		}
		return object;
	}

	/**
	 * {@link #get()} で返すオブジェクトを設定します。
	 *
	 * @param object
	 *            {@link #get()} で返すオブジェクト
	 */
	@Inject(optional = true)
	public void setObject(T object) {
		this.object = object;
	}

	/**
	 * {@link #setObject(Object)} で値が設定されなかった場合に {@link #get()} が返すオブジェクトを取得します。
	 *
	 * @return {@link #get()} が返すオブジェクト
	 */
	protected abstract T getDefaultValue();

}
