package protobuf.type;

import java.util.EnumSet;

public class EnumProtobuf extends AbstractBaseProtobuf {

    EnumSet<? extends Enum> enumSet;

    public static EnumProtobuf of(Class<?> type, EnumSet<?> enumSet){
        EnumProtobuf enumProtobuf = new EnumProtobuf();
        enumProtobuf.type = type;
        enumProtobuf.enumSet = enumSet;
        return enumProtobuf;
    }

    public EnumSet<? extends Enum> getEnumSet() {
        return enumSet;
    }
}
