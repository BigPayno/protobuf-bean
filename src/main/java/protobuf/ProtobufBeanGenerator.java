package protobuf;

import com.google.common.collect.Lists;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.ProtocolMessageEnum;
import com.squareup.javapoet.JavaFile;
import net.openhft.compiler.CompilerUtils;
import org.springframework.core.ResolvableType;
import org.springframework.util.CollectionUtils;
import protobuf.Configuration.Default;
import protobuf.factory.ChainProtobufFactory;
import protobuf.factory.EnumProtobufFactory;
import protobuf.factory.GeneralProtobufFactory;
import protobuf.field.BaseFieldParser;
import protobuf.field.BaseProtobufFieldParser;
import protobuf.field.ListProtobufFieldParser;
import protobuf.interceptor.ChainedSetterMethodInterceptor;
import protobuf.interceptor.EnumValueOfMethodInterceptor;
import protobuf.interceptor.ExtractFromMethodInterceptor;
import protobuf.interceptor.ProtobufSourceAnnotationInterceptor;
import protobuf.interceptor.ToProtobufMethodInterceptor;
import protobuf.mapper.ChainedProtobufMapper;
import protobuf.mapper.EnumProtobufBeanMapper;
import protobuf.mapper.GeneralProtobufMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ProtobufBeanGenerator {

    Configuration configuration;

    private ProtobufBeanGenerator() {
    }

    public ProtobufBeanGenerator(ConfigurationProperties properties) {
        this.configuration = new Default(properties);
        init();
    }

    ProtobufFactory protobufFactory = new ChainProtobufFactory(
            Lists.newArrayList(
                new EnumProtobufFactory(),
                new GeneralProtobufFactory()
            )
    );

    ProtobufMapper protobufMapper = new ChainedProtobufMapper(
            Lists.newArrayList(
                new EnumProtobufBeanMapper(),
                new GeneralProtobufMapper(
                    Lists.newArrayList(
                            new BaseProtobufFieldParser(),
                            new BaseFieldParser(),
                            new ListProtobufFieldParser()
                    )
                )
            )
    );

    List<ProtobufFileInterceptor> protobufFileInterceptors = Lists.newArrayList(
            new ProtobufSourceAnnotationInterceptor(),
            new EnumValueOfMethodInterceptor(),
            new ExtractFromMethodInterceptor(configuration),
            new ToProtobufMethodInterceptor(configuration),
            new ChainedSetterMethodInterceptor()
    );

    private void init(){
        configuration.init();
    }

    private JavaFile generateBase(Class<?> protobufType) throws IOException, ClassNotFoundException {
        Protobuf protobuf = null;
        if(protobufFactory.supports(protobufType)){
            protobuf = protobufFactory.load(protobufType, configuration);
        }
        if(protobuf==null){
            //  todo or not todo!
        }
        JavaFile javaFile = null;
        if(protobufMapper.supports(protobuf)){
            javaFile = protobufMapper.mapFrom(protobuf, configuration);
        }
        if(javaFile==null){
            //  todo or not todo!
        }
        for(ProtobufFileInterceptor interceptor: protobufFileInterceptors){
            if(interceptor.supports(protobuf)){
                javaFile = interceptor.intercept(javaFile, protobuf);
            }
        }
        javaFile.writeTo(configuration.generatePath().getFile());
        return javaFile;
    }

    private List<Class<?>> dependenciesBy(Class<?> protobufType){
        return Stream.of(protobufType.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("get"))
                .map(method->{
                    if(method.getReturnType().isAssignableFrom(List.class)){
                        return ResolvableType.forMethodReturnType(method).getGeneric(0).getRawClass();
                    }else{
                        return method.getReturnType();
                    }
                })
                .filter(type->type!=null)
                //.peek(type-> System.err.println(type.getSimpleName()))
                .filter(type->!type.equals(protobufType))
                //.peek(type-> System.err.println(type.getSimpleName()))
                .filter(type->GeneratedMessage.class.isAssignableFrom(type)||ProtocolMessageEnum.class.isAssignableFrom(type))
                .filter(type->!configuration.queryProtobufBeanType(type).isPresent())
                .filter(type->!configuration.ignoreProtobufs(type))
                .collect(Collectors.toList());
    }

    private Class<?> complieAndLoad(JavaFile javaFile) throws ClassNotFoundException {
        return CompilerUtils.CACHED_COMPILER
                .loadFromJava(javaFile.packageName+"."+javaFile.typeSpec.name,javaFile.toString());
    }

    private JavaFile generate(Class<?> protobufType) throws IOException, ClassNotFoundException {
        List<Class<?>> dependencies = dependenciesBy(protobufType);
        if(!CollectionUtils.isEmpty(dependencies)){
            Iterator<Class<?>> iterator = dependencies.iterator();
            while(iterator.hasNext()){
                Class<?> curProtobufType = iterator.next();
                JavaFile javaFile = generate(curProtobufType);
                Class<?> curProtobufBeanType = complieAndLoad(javaFile);
                configuration.addNewBeanProtobufRelation(curProtobufType, curProtobufBeanType);
            }
        }
        JavaFile javaFile = generateBase(protobufType);
        Class<?> protobufBeanType = complieAndLoad(javaFile);
        configuration.addNewBeanProtobufRelation(protobufType, protobufBeanType);
        return javaFile;
    }

    public void generateIfPossiable(Class<?> protobufType) throws IOException, ClassNotFoundException {
        if(!configuration.queryProtobufBeanType(protobufType).isPresent()){
            generate(protobufType);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

    }
}
