package protobuf.interceptor;

import com.google.protobuf.ProtocolMessageEnum;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import protobuf.Protobuf;
import protobuf.ProtobufFileInterceptor;
import protobuf.type.EnumProtobuf;
import protobuf.utils.Types;

import javax.lang.model.element.Modifier;
import java.util.Comparator;
import java.util.EnumSet;

public class EnumValueOfMethodInterceptor implements ProtobufFileInterceptor{

    @Override
    public boolean supports(Protobuf protobuf) {
        return EnumProtobuf.class.isAssignableFrom(protobuf.getClass());
    }

    @Override
    public JavaFile intercept(JavaFile javaFile, Protobuf protobuf) throws ClassNotFoundException {
        if(protobuf instanceof EnumProtobuf){
            Class<?> protobufBeanType = complieAndLoad(javaFile);
            EnumProtobuf enumProtobuf = (EnumProtobuf) protobuf;
            TypeSpec typeSpec = javaFile.typeSpec.toBuilder()
                    .addMethod(enumStaticFactoryMethod(enumProtobuf, protobufBeanType))
                    .build();
            return JavaFile.builder(javaFile.packageName,typeSpec).build();
        }
        return javaFile;
    }

    private MethodSpec enumStaticFactoryMethod(EnumProtobuf enumProtobuf,Class<?> protobufBeanType){
        ProtocolMessageEnum protocolMessageEnum = enumProtobuf.getEnumSet().stream()
                .map(item -> Types.castUnsafe(item, ProtocolMessageEnum.class))
                .min(Comparator.comparing(ProtocolMessageEnum::getNumber))
                .get();
        String defaultEnumName = ((Enum) protocolMessageEnum).name();
        return MethodSpec.methodBuilder("valueOf")
                .addParameter(Integer.TYPE,"val")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addCode("return $T.allOf($T.class).stream()$W", EnumSet.class, protobufBeanType)
                .addCode(".filter(item -> item.value == val )$W")
                .addCode(".findFirst().orElse($T.$N);",protobufBeanType, defaultEnumName)
                .returns(protobufBeanType)
                .build();
    }
}
