package protobuf;

import com.google.common.base.Strings;
import com.qingqing.api.proto.v1.ProtoBufResponse.BaseResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import protobuf.annotation.ProtobufSource;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public interface Configuration {

    void init();

    boolean ignoreProtobufs(Class<?> protobufType);

    Resource generatePath() throws IOException;

    String generatePackage();

    String[] protobufBeanPackages();

    Optional<Class<?>> queryProtobufBeanType(Class<?> protobufType);

    Optional<Class<?>> queryProtobufType(Class<?> protobufBeanType);

    Class<?> queryProtobufBeanTypeByFieldType(Class<?> protobufFieldType);

    void addNewBeanProtobufRelation(Class<?> protobufType,Class<?> protobufBeanType);

    class Default implements Configuration {

        ConfigurationProperties configurationProperties;

        FileSystemResource generatePathResource;

        public Default(ConfigurationProperties configurationProperties) {
            this.configurationProperties = configurationProperties;
            this.generatePathResource = new FileSystemResource(configurationProperties.fileSystemPath);
        }

        ConcurrentHashMap<Class<?>,Class<?>> protobufBeanMap = new ConcurrentHashMap<>();

        ConcurrentHashMap<Class<?>,Class<?>> protobufMap = new ConcurrentHashMap<>();

        ProtobufBeanScanner protobufBeanScanner = new ProtobufBeanScanner(true,new StandardEnvironment());

        private void scanProtobufMappers(){
            Stream.of(protobufBeanPackages())
                    .forEach(packagePath->{
                        protobufBeanScanner.findCandidateComponents(packagePath)
                            .forEach(beanDefinition -> {
                                try {
                                    Class<?> protobufBeanType = ClassUtils.forName(beanDefinition.getBeanClassName(),null);
                                    ProtobufSource protobufSource = AnnotatedElementUtils.findMergedAnnotation(protobufBeanType, ProtobufSource.class);
                                    if(protobufSource!=null){
                                        if(protobufSource.protobufClass()!=null){
                                            protobufBeanMap.putIfAbsent(protobufSource.protobufClass(), protobufBeanType);
                                            protobufMap.put(protobufBeanType, protobufSource.protobufClass());
                                        }
                                        if(!Strings.isNullOrEmpty(protobufSource.protobufType())){
                                            Class<?> protobufType = ClassUtils.forName(protobufSource.protobufType(),null);
                                            protobufBeanMap.putIfAbsent(protobufType, protobufBeanType);
                                            protobufMap.put(protobufBeanType, protobufType);
                                        }
                                    }
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            });
                    });
        }

        @Override
        public void init() {
            scanProtobufMappers();
        }

        @Override
        public boolean ignoreProtobufs(Class<?> protobufType) {
            return BaseResponse.class.isAssignableFrom(protobufType);
        }

        @Override
        public Resource generatePath() throws IOException {
            return generatePathResource;
        }

        @Override
        public String generatePackage() {
            return configurationProperties.generatePackagePath;
        }

        @Override
        public String[] protobufBeanPackages() {
            return configurationProperties.protobufBeanPackagePaths;
        }

        @Override
        public Optional<Class<?>> queryProtobufBeanType(Class<?> protobufType) {
            if(ignoreProtobufs(protobufType)){
                return Optional.empty();
            }
            Class<?> protobufBeanType = protobufBeanMap.get(protobufType);
            return Optional.ofNullable(protobufBeanType);
        }

        @Override
        public Optional<Class<?>> queryProtobufType(Class<?> protobufBeanType) {
            Class<?> protobufType = protobufBeanMap.get(protobufBeanType);
            return Optional.ofNullable(protobufType);
        }

        @Override
        public Class<?> queryProtobufBeanTypeByFieldType(Class<?> protobufFieldType) {
            return queryProtobufBeanType(protobufFieldType).orElse(protobufFieldType);
        }

        @Override
        public void addNewBeanProtobufRelation(Class<?> protobufType, Class<?> protobufBeanType) {
            protobufBeanMap.putIfAbsent(protobufType, protobufBeanType);
            protobufMap.putIfAbsent(protobufBeanType, protobufType);
        }
    }
}
