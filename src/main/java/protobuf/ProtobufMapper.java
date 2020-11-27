package protobuf;

import com.squareup.javapoet.JavaFile;

public interface ProtobufMapper {
    boolean supports(Protobuf protobuf);
    JavaFile mapFrom(Protobuf protobuf, Configuration configuration);
}
