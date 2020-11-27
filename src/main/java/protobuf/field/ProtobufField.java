package protobuf.field;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.ParameterizedTypeName;

import java.lang.reflect.Field;

public class ProtobufField {
    Class<?> protobufType;
    Field field;

    String fieldName;
    boolean hasBuildField;
    Class<?> protobufFieldType;

    ParameterizedTypeName listGenericType;
    String beanGetterMethod;
    String beanSetterMethod;

    public Class<?> getProtobufType() {
        return protobufType;
    }

    public Field getField() {
        return field;
    }

    public static ProtobufField of(Class<?> protobufType, Field field){
        ProtobufField protobufField = new ProtobufField();
        protobufField.protobufType = protobufType;
        protobufField.field = field;
        return protobufField;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldNameUpperCamel() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,fieldName);
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isHasBuildField() {
        return hasBuildField;
    }

    public void setHasBuildField(boolean hasBuildField) {
        this.hasBuildField = hasBuildField;
    }

    public Class<?> getProtobufFieldType() {
        return protobufFieldType;
    }

    public void setProtobufFieldType(Class<?> protobufFieldType) {
        this.protobufFieldType = protobufFieldType;
    }

    public ParameterizedTypeName getListGenericType() {
        return listGenericType;
    }

    public void setListGenericType(ParameterizedTypeName listGenericType) {
        this.listGenericType = listGenericType;
    }

    public String getBeanGetterMethod() {
        return beanGetterMethod;
    }

    public void setBeanGetterMethod(String beanGetterMethod) {
        this.beanGetterMethod = beanGetterMethod;
    }

    public String getBeanSetterMethod() {
        return beanSetterMethod;
    }

    public void setBeanSetterMethod(String beanSetterMethod) {
        this.beanSetterMethod = beanSetterMethod;
    }
}
