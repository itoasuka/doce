/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author asuka
 */
public abstract class DefaultProvider<T> implements Provider<T> {
	private T value;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get() {
		if (value == null) {
			return getDefaultValue();
		}
		return value;
	}

	@Inject(optional = true)
	public void setValue(T value) {
		this.value = value;
	}

	protected abstract T getDefaultValue();

}
