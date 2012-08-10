package test.dao;

import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;

import com.google.inject.Inject;
import com.google.inject.name.Named;

@AnnotateWith(annotations = {
		@Annotation(target = AnnotationTarget.CONSTRUCTOR, type = Inject.class),
		@Annotation(target = AnnotationTarget.CONSTRUCTOR_PARAMETER, type = Named.class, elements = "\"test\"") })
public @interface InjectTestConfig {

}
