package protobuf.field;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.ProtocolMessageEnum;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum ProtobufType {
    BASE,
    SIMPLE,
    LIST,
    PROTOBUF,
    ENUM;

    public static final Set<Class<?>> baseTypes = ImmutableSet.of(
      int.class,boolean.class,long.class,double.class,float.class
    );

    public static final Map<Class<?>,Class<?>> baseTypeMap = ImmutableMap.of(
            int.class,Integer.class,
            boolean.class,Boolean.class,
            long.class,Long.class,
            double.class,Double.class,
            float.class,Float.class
    );

    public static ProtobufType parse(Field field, Class<?> type){
        if(ProtocolMessageEnum.class.isAssignableFrom(type)){
            return ENUM;
        }
        if(baseTypes.contains(type)&&!List.class.isAssignableFrom(field.getType())&&!List.class.equals(field.getType())){
            return BASE;
        }
        if(List.class.isAssignableFrom(field.getType())||List.class.equals(field.getType())){
            return LIST;
        }
        if(GeneratedMessage.class.isAssignableFrom(type)){
            return PROTOBUF;
        }
        return SIMPLE;
    }
}
