package jp.osd.doma.guice;

import com.google.inject.Provider;
import com.google.inject.binder.AnnotatedBindingBuilder;

/**
 * Guice によるバインドのルールを設定するための基底クラスです。
 * 
 * @param <T> インジェクション対象の型
 * @author asuka
 */
public abstract class BindingRule<T> {
	/**
	 * インスタンスの提供に使用するプロバイダを使用することをプロバイダオブジェクトで指定するバインドルールを取得します。
	 *
	 * @param <R> インジェクション対象の型
	 * @param provider インスタンスを提供するプロバイダオブジェクト
	 * @return バインドルール
	 */
	public static <R> BindingRule<R> toProvider(final Provider<R> provider) {
		return new BindingRule<R>() {
			@Override
			public void apply(AnnotatedBindingBuilder<? super R> builder) {
				builder.toProvider(provider);
			}
		};
	}

	/**
	 * インスタンスの提供に使用するプロバイダを使用することをプロバイダクラスオブジェクトによって指定するバインドルールを取得します。
	 *
	 * @param <R> インジェクション対象の型
	 * @param providerType インスタンスを提供するプロバイダクラスオブジェクト
	 * @return バインドルール
	 */
	public static <R> BindingRule<R> toProvider(
			final Class<? extends javax.inject.Provider<R>> providerType) {
		return new BindingRule<R>() {
			@Override
			public void apply(AnnotatedBindingBuilder<? super R> builder) {
				builder.toProvider(providerType);
			}
		};
	}

	/**
	 * インジェクションするインスタンスを直に指定するバインドルールを取得します。
	 *
	 * @param <R> インジェクション対象の型
	 * @param instance インジェクションするインスタンス
	 * @return バインドルール
	 */
	public static <R> BindingRule<R> toInstance(final R instance) {
		return new BindingRule<R>() {
			@Override
			public void apply(AnnotatedBindingBuilder<? super R> builder) {
				builder.toInstance(instance);
			}
		};
	}
	/**
	 * インジェクションするインスタンスをクラスオブジェクトで指定するバインドルールを取得します。
	 *
	 * @param <R> インジェクション対象の型
	 * @param type インジェクションするインスタンスのクラスオブジェクト
	 * @return バインドルール
	 */
	public static <R> BindingRule<R> to(final Class<R> type) {
		return new BindingRule<R>() {
			@Override
			public void apply(AnnotatedBindingBuilder<? super R> builder) {
				builder.to(type);
			}
		};
	}

	/**
	 * バインドルールを適用します。
	 *
	 * @param builder 適用対象のバインディングビルダ
	 */
	public abstract void apply(AnnotatedBindingBuilder<? super T> builder);

}
