package protobuf.utils;

public class Types {
    public static <T> T castUnsafe(Object object,Class<T> type){
        return (T) object;
    }
}
