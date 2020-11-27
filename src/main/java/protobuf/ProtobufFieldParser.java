package protobuf;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import protobuf.field.ProtobufField;

import javax.lang.model.element.Modifier;

public interface ProtobufFieldParser {

    default void initFieldName(ProtobufField context){
        if(context.getFieldName()==null){
            String fieldName = context.getField().getName().substring(0,context.getField().getName().length()-1);
            context.setFieldName(fieldName);
        }
    }

    default void initFieldType(ProtobufField context, Configuration configuration) throws ClassNotFoundException {
        if(context.getProtobufFieldType()==null){
            Class<?> fieldType = fieldType(context,configuration);
            context.setProtobufFieldType(fieldType);
        }
    }

    default FieldSpec initFieldSpec(ProtobufField context, Configuration configuration) throws ClassNotFoundException {
        initFieldName(context);
        initFieldType(context,configuration);
        return FieldSpec.builder(context.getProtobufFieldType(), context.getFieldName(), Modifier.PRIVATE)
                .build();
    }

    default String getterMethodName(ProtobufField context){
        initFieldName(context);
        context.setBeanGetterMethod("get"+ context.getFieldNameUpperCamel());
        return context.getBeanGetterMethod();
    }

    default String setterMethodName(ProtobufField context){
        initFieldName(context);
        context.setBeanSetterMethod("set"+ context.getFieldNameUpperCamel());
        return context.getBeanSetterMethod();
    }

    default MethodSpec getterMethodSpec(ProtobufField context, Configuration configuration) throws ClassNotFoundException {
        initFieldName(context);
        initFieldType(context,configuration);
        initFieldSpec(context,configuration);
        return MethodSpec.methodBuilder(getterMethodName(context))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$N",context.getFieldName())
                .returns(context.getProtobufFieldType())
                .build();
    }

    default MethodSpec setterMethodSpec(ProtobufField context, Configuration configuration) throws ClassNotFoundException {
        initFieldName(context);
        initFieldType(context,configuration);
        initFieldSpec(context,configuration);
        return MethodSpec.methodBuilder(setterMethodName(context))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(context.getProtobufFieldType(),context.getFieldName())
                .addStatement("this.$N = $N",context.getFieldName(),context.getFieldName())
                .returns(Void.TYPE)
                .build();
    }

    boolean supports(ProtobufField protobufField, Configuration configuration) throws ClassNotFoundException;

    Class<?> fieldType(ProtobufField protobufField, Configuration configuration) throws ClassNotFoundException;

    CodeBlock extractFrom(ProtobufField context, Class<?> requestType);
}
