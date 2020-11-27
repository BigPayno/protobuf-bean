package protobuf.factory;

import com.google.protobuf.ProtocolMessageEnum;
import protobuf.Configuration;
import protobuf.Protobuf;
import protobuf.ProtobufFactory;
import protobuf.type.GeneralProtobuf;

public class GeneralProtobufFactory  implements ProtobufFactory {
    @Override
    public boolean supports(Class<?> protobuf) {
        return !ProtocolMessageEnum.class.isAssignableFrom(protobuf);
    }

    @Override
    public Protobuf load(Class<?> protobuf, Configuration configuration) {
        return GeneralProtobuf.of(protobuf);
    }
}
