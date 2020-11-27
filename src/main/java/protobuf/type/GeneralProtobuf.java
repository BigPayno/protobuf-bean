package protobuf.type;

import protobuf.Configuration;
import protobuf.field.ProtobufField;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneralProtobuf extends AbstractBaseProtobuf{

    List<ProtobufField> protobufFields;

    @Override
    public List<Field> fields(Configuration configuration) {
        return Stream.of(protobufType().getDeclaredFields())
                .filter(field -> field.getName().endsWith("_"))
                .filter(field -> !field.getName().equals("bitField0_"))
                .filter(field -> !configuration.ignoreProtobufs(field.getType()))
                .collect(Collectors.toList());
    }

    public static GeneralProtobuf of(Class<?> type){
        GeneralProtobuf protobuf = new GeneralProtobuf();
        protobuf.type = type;
        return protobuf;
    }

    public List<ProtobufField> getProtobufFields() {
        return protobufFields;
    }

    public void setProtobufFields(List<ProtobufField> protobufFields) {
        this.protobufFields = protobufFields;
    }
}
