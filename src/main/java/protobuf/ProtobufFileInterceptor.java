package protobuf;

import com.squareup.javapoet.JavaFile;
import net.openhft.compiler.CompilerUtils;

public interface ProtobufFileInterceptor {

    boolean supports(Protobuf protobuf);

    JavaFile intercept(JavaFile javaFile,Protobuf protobuf) throws ClassNotFoundException;

    default Class<?> complieAndLoad(JavaFile javaFile) throws ClassNotFoundException {
        return CompilerUtils.CACHED_COMPILER
                .loadFromJava(javaFile.packageName+"."+javaFile.typeSpec.name,javaFile.toString());
    }
}
