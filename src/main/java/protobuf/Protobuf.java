package protobuf;

import java.lang.reflect.Field;
import java.util.List;

public interface Protobuf {
    Class<?> protobufType();
    List<Field> fields(Configuration configuration);
}
