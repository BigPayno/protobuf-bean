package protobuf.factory;

import com.google.protobuf.ProtocolMessageEnum;
import protobuf.Configuration;
import protobuf.Protobuf;
import protobuf.ProtobufFactory;
import protobuf.type.EnumProtobuf;

import java.util.EnumSet;

public class EnumProtobufFactory implements ProtobufFactory {
    @Override
    public boolean supports(Class<?> protobuf) {
        return ProtocolMessageEnum.class.isAssignableFrom(protobuf);
    }

    @Override
    public Protobuf load(Class<?> protobuf, Configuration configuration) {
        Class<? extends Enum> protobufEnum = (Class<? extends Enum>) protobuf;
        return EnumProtobuf.of(protobuf, EnumSet.allOf(protobufEnum));
    }
}
