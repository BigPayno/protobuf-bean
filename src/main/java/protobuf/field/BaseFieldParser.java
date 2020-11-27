package protobuf.field;

import com.squareup.javapoet.CodeBlock;
import org.springframework.util.ClassUtils;
import protobuf.Configuration;
import protobuf.ProtobufFieldParser;

public class BaseFieldParser implements ProtobufFieldParser {

    @Override
    public boolean supports(ProtobufField protobufField, Configuration configuration) throws ClassNotFoundException {
        try{
            initFieldType(protobufField,configuration);
            ProtobufType type = ProtobufType.parse(protobufField.field,protobufField.getProtobufFieldType());
            return type.equals(ProtobufType.BASE)||type.equals(ProtobufType.SIMPLE);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Class<?> fieldType(ProtobufField context, Configuration configuration) throws ClassNotFoundException {
        String getter = getterMethodName(context);
        Class<?> getterReturnType = ClassUtils.getMethod(context.protobufType,getter).getReturnType();
        if(ProtobufType.baseTypes.contains(getterReturnType)){
            return ProtobufType.baseTypeMap.get(getterReturnType);
        }
        return getterReturnType;
    }

    @Override
    public CodeBlock extractFrom(ProtobufField context, Class<?> requestType) {
        return null;
    }
}
