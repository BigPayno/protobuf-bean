package protobuf.interceptor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import protobuf.Protobuf;
import protobuf.ProtobufFileInterceptor;
import protobuf.annotation.ProtobufSource;

public class ProtobufSourceAnnotationInterceptor implements ProtobufFileInterceptor {

    static AnnotationSpec mapperAnnotationSepc(String protobufType){
        return AnnotationSpec.builder(ProtobufSource.class)
                .addMember("protobufType","$S",protobufType)
                .build();
    }

    static AnnotationSpec mapperAnnotationSepc(Class<?> protobufType){
        return AnnotationSpec.builder(ProtobufSource.class)
                .addMember("protobufClass","$T.class",protobufType)
                .build();
    }

    @Override
    public boolean supports(Protobuf protobuf) {
        return true;
    }

    @Override
    public JavaFile intercept(JavaFile javaFile, Protobuf protobuf) {
        TypeSpec typeSpec = javaFile.typeSpec.toBuilder()
                .addAnnotation(mapperAnnotationSepc(protobuf.protobufType()))
                .build();
        return JavaFile.builder(javaFile.packageName,typeSpec).build();
    }
}
