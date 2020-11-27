package protobuf.field;

import protobuf.Configuration;

public class BaseProtobufFieldParser extends BaseFieldParser{
    @Override
    public boolean supports(ProtobufField protobufField, Configuration configuration) throws ClassNotFoundException {
        try{
            initFieldType(protobufField,configuration);
            ProtobufType type = ProtobufType.parse(protobufField.field,protobufField.getProtobufFieldType());
            return type.equals(ProtobufType.PROTOBUF)||type.equals(ProtobufType.ENUM);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Class<?> fieldType(ProtobufField context, Configuration configuration) throws ClassNotFoundException {
        Class<?> rawType = super.fieldType(context, configuration);
        Class<?> beanType = configuration.queryProtobufBeanTypeByFieldType(rawType);
        if(beanType==null){
            return rawType;
        }
        return beanType;
    }
}
