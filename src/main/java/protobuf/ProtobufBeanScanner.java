package protobuf;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import protobuf.annotation.ProtobufSource;

public class ProtobufBeanScanner extends ClassPathScanningCandidateComponentProvider {

    public ProtobufBeanScanner(boolean useDefaultFilters, Environment environment) {
        super(useDefaultFilters, environment);
        super.addIncludeFilter(new AnnotationTypeFilter(ProtobufSource.class));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return true;
    }
}
