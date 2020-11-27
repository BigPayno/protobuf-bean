package protobuf.type;

import protobuf.Configuration;
import protobuf.Protobuf;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public abstract class AbstractBaseProtobuf implements Protobuf {

    Class<?> type;

    @Override
    public List<Field> fields(Configuration configuration) {
        return Collections.emptyList();
    }

    @Override
    public Class<?> protobufType() {
        return type;
    }
}
