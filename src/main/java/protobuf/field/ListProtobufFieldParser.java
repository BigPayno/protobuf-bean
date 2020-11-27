package protobuf.field;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import protobuf.Configuration;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public class ListProtobufFieldParser extends BaseFieldParser {

    @Override
    public boolean supports(ProtobufField protobufField, Configuration configuration) throws ClassNotFoundException {
        try{
            initFieldType(protobufField,configuration);
            ProtobufType type = ProtobufType.parse(protobufField.field,protobufField.getProtobufFieldType());
            return type.equals(ProtobufType.LIST);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Class<?> fieldType(ProtobufField context, Configuration configuration) throws ClassNotFoundException {
        String getter = getterMethodName(context);
        Method getterMethod = ClassUtils.getMethod(context.protobufType,getter);
        Class<?> getterReturnType = getterMethod.getReturnType();
        if(List.class.isAssignableFrom(getterReturnType)||List.class.equals(getterReturnType)){
            Type type = ResolvableType.forMethodReturnType(getterMethod).getGeneric(0).getType();
            Class<?> singleType = Class.forName(type.getTypeName());
            if(ProtobufType.baseTypes.contains(singleType)){
                return ProtobufType.baseTypeMap.get(singleType);
            }
            return configuration.queryProtobufBeanTypeByFieldType(singleType);
        }
        return Object.class;
    }

    @Override
    public String getterMethodName(ProtobufField context) {
        context.setBeanGetterMethod(super.getterMethodName(context)+"List");
        return context.getBeanGetterMethod();
    }

    @Override
    public String setterMethodName(ProtobufField context) {
        context.setBeanSetterMethod(super.setterMethodName(context)+"List");
        return context.getBeanSetterMethod();
    }

    @Override
    public FieldSpec initFieldSpec(ProtobufField context, Configuration configuration) throws ClassNotFoundException {
        initFieldName(context);
        initFieldType(context,configuration);
        context.setListGenericType(ParameterizedTypeName.get(List.class,context.protobufFieldType));
        return FieldSpec.builder(context.getListGenericType(), context.getFieldName()+"List", Modifier.PRIVATE)
                .build();
    }

    @Override
    public MethodSpec getterMethodSpec(ProtobufField context, Configuration configuration) throws ClassNotFoundException {
        initFieldName(context);
        initFieldType(context,configuration);
        initFieldSpec(context,configuration);
        return MethodSpec.methodBuilder(getterMethodName(context))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$N",context.getFieldName()+"List")
                .returns(context.getListGenericType())
                .build();
    }

    @Override
    public MethodSpec setterMethodSpec(ProtobufField context, Configuration configuration) throws ClassNotFoundException {
        initFieldName(context);
        initFieldType(context,configuration);
        initFieldSpec(context,configuration);
        return MethodSpec.methodBuilder(setterMethodName(context))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(context.getListGenericType(),context.getFieldName()+"List")
                .addStatement("this.$N = $N",context.getFieldName()+"List",context.getFieldName()+"List")
                .returns(Void.TYPE)
                .build();
    }
}
