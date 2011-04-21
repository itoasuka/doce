package jp.osd.doce;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;

import com.google.inject.Inject;

/**
 * Doma Dao に Guice によるインジェクションによって設定ファイルをインジェクトするためのアノテーションです。
 *
 * @author asuka
 *
 */
@AnnotateWith(annotations = { @Annotation(target = AnnotationTarget.CONSTRUCTOR, type = Inject.class) })
public @interface InjectConfig {

}
