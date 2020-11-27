package protobuf;

public interface ProtobufFactory {
    boolean supports(Class<?> protobuf);
    Protobuf load(Class<?> protobuf,Configuration configuration);
}
