package protobuf.interceptor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec.Builder;
import protobuf.Protobuf;
import protobuf.ProtobufFileInterceptor;
import protobuf.type.GeneralProtobuf;

public class ChainedSetterMethodInterceptor implements ProtobufFileInterceptor {

    @Override
    public boolean supports(Protobuf protobuf) {
        return GeneralProtobuf.class.isAssignableFrom(protobuf.getClass());
    }

    @Override
    public JavaFile intercept(JavaFile javaFile, Protobuf protobuf) throws ClassNotFoundException {
        if(protobuf instanceof GeneralProtobuf){
            Class<?> protobufBeanType = complieAndLoad(javaFile);
            Builder builder = javaFile.typeSpec.toBuilder();
            javaFile.typeSpec.methodSpecs.stream()
                    .filter(methodSpec->methodSpec.name.startsWith("set"))
                    .map(methodSpec -> methodSpec.toBuilder().setName(methodSpec.name+"Chain").addStatement("return this").returns(protobufBeanType).build())
                    .forEach(builder::addMethod);
            return JavaFile.builder(javaFile.packageName,builder.build()).build();
        }
        return javaFile;
    }
}
