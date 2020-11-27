package protobuf.interceptor;

import com.google.protobuf.ProtocolMessageEnum;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.CodeBlock.Builder;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import protobuf.Configuration;
import protobuf.Protobuf;
import protobuf.ProtobufFileInterceptor;
import protobuf.annotation.ProtobufSource;
import protobuf.field.ProtobufField;
import protobuf.type.GeneralProtobuf;
import protobuf.utils.Optionals;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Method;
import java.util.stream.Collectors;

public class ToProtobufMethodInterceptor implements ProtobufFileInterceptor {

    static Logger logger = LoggerFactory.getLogger(ToProtobufMethodInterceptor.class);

    Configuration configuration;

    public ToProtobufMethodInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public boolean supports(Protobuf protobuf) {
        return GeneralProtobuf.class.isAssignableFrom(protobuf.getClass());
    }

    @Override
    public JavaFile intercept(JavaFile javaFile, Protobuf protobuf) throws ClassNotFoundException {
        if(protobuf instanceof GeneralProtobuf){
            Class<?> protobufBeanType = complieAndLoad(javaFile);
            GeneralProtobuf generalProtobuf = (GeneralProtobuf) protobuf;
            TypeSpec typeSpec = javaFile.typeSpec.toBuilder()
                    .addMethod(extractToProtobufMethod(generalProtobuf, protobufBeanType))
                    .build();
            return JavaFile.builder(javaFile.packageName,typeSpec).build();
        }
        return javaFile;
    }

    private MethodSpec extractToProtobufMethod(GeneralProtobuf protobuf, Class<?> protobufBeanType) throws ClassNotFoundException {
        Class<?> protobufBuilderType = builderType(protobuf.protobufType());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("toProtobuf")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$T sink = $T.newBuilder()", protobufBuilderType, protobuf.protobufType());
        protobuf.getProtobufFields().forEach(field->{
            builder.addCode(parse(field, protobufBeanType));
        });
        return builder.addStatement("return sink.build()")
                .returns(protobuf.protobufType())
                .build();
    }

    private Class<?> builderType(Class<?> protobufType) throws ClassNotFoundException {
        return ClassUtils.forName(protobufType.getName()+".Builder",null);
    }

    private CodeBlock parse(ProtobufField protobufField, Class<?> protobufBeanType){
        Builder builder = CodeBlock.builder();
        Method beanGetter = ClassUtils.getMethod(protobufBeanType,protobufField.getBeanGetterMethod());
        ResolvableType fieldType = ResolvableType.forMethodReturnType(beanGetter);
        if(fieldType.hasGenerics()){
            // list
            Class<?> singleFieldType = fieldType.getGeneric(0).getRawClass();
            ProtobufSource protobufSource = AnnotatedElementUtils.findMergedAnnotation(singleFieldType, ProtobufSource.class);
            if(protobufSource!=null){
                if(protobufSource.isGenerated()){
                    try {
                        Class<?> fieldProtobufType = protobufSource.protobufClass()==null?
                                ClassUtils.forName(protobufSource.protobufType(),null):
                                protobufSource.protobufClass();
                        //  protobuf
                        if(ProtocolMessageEnum.class.isAssignableFrom(fieldProtobufType)){
                            //  enum
                            builder.add("$T.forNotNull(this::$N)$W",Optionals.class,protobufField.getBeanGetterMethod())
                                    .add(".map(beanList-> beanList.stream().map(bean->$T.valueOf(bean.getValue())).collect($T.toList()))", fieldProtobufType,Collectors.class)
                                    .addStatement(".ifPresent(sink::addAll$N)",protobufField.getFieldNameUpperCamel());
                        }else{
                            //  general
                            builder.add("$T.forNotNull(this::$N)$W",Optionals.class,protobufField.getBeanGetterMethod())
                                    .add(".map(beanList-> beanList.stream().map(bean->bean.toProtobuf()).collect($T.toList()))", Collectors.class)
                                    .addStatement(".ifPresent(sink::addAll$N)",protobufField.getFieldNameUpperCamel());
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    logger.warn(
                            String.format("this protobuf field bean[{}] is not generated by code,%n" +
                                    "please add refer code to bean[{}] by yourself!"),singleFieldType,protobufBeanType);
                    builder.add(String.format("//  todo @see %s%n",singleFieldType.getSimpleName()));
                }
            }else{
                //  simple
                builder.addStatement(
                        "$T.forNotNull(this::$N).ifPresent(sink::addAll$N)",
                        Optionals.class,protobufField.getBeanGetterMethod(),protobufField.getFieldNameUpperCamel());
            }
        }else{
            // simple
            ProtobufSource protobufSource = AnnotatedElementUtils.findMergedAnnotation(fieldType.getRawClass(), ProtobufSource.class);
            if(protobufSource!=null){
                if(protobufSource.isGenerated()){
                    try {
                        Class<?> fieldProtobufType = protobufSource.protobufClass()==null?
                                ClassUtils.forName(protobufSource.protobufType(),null):
                                protobufSource.protobufClass();
                        //  protobuf
                        if(ProtocolMessageEnum.class.isAssignableFrom(fieldProtobufType)){
                            //  enum
                            builder.add("$T.forNotNull(this::$N)$W",Optionals.class,protobufField.getBeanGetterMethod())
                                    .add(".map(bean->$T.valueOf(bean.getValue()))$W",fieldProtobufType)
                                    .addStatement(".ifPresent(sink::$N)",protobufField.getBeanSetterMethod());
                        }else{
                            //  general
                            builder.add("$T.forNotNull(this::$N)$W",Optionals.class,protobufField.getBeanGetterMethod())
                                    .add(".map($T::toProtobuf)$W",fieldType.getRawClass())
                                    .addStatement(".ifPresent(sink::$N)",protobufField.getBeanSetterMethod());
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    logger.warn(
                            String.format("this protobuf field bean[{}] is not generated by code,%n" +
                                    "please add refer code to bean[{}] by yourself!"),fieldType.getRawClass(),protobufBeanType);
                    builder.add(String.format("//  todo @see %s%n",fieldType.getRawClass().getSimpleName()));
                }
            }else if(fieldType.getRawClass().equals(String.class)){
                //  string
                builder.addStatement(
                        "$T.forNotNullAndEmpty(this::$N).ifPresent(sink::$N)",
                        Optionals.class,protobufField.getBeanGetterMethod(),protobufField.getBeanSetterMethod());
            }else{
                //  simple
                builder.addStatement(
                        "$T.forNotNull(this::$N).ifPresent(sink::$N)",
                        Optionals.class,protobufField.getBeanGetterMethod(),protobufField.getBeanSetterMethod());
            }
        }
        return builder.build();
    }
}
