package protobuf.factory;

import protobuf.Configuration;
import protobuf.Protobuf;
import protobuf.ProtobufFactory;

import java.util.List;

public class ChainProtobufFactory implements ProtobufFactory {

    List<ProtobufFactory> protobufFactoryList;

    public ChainProtobufFactory(List<ProtobufFactory> protobufFactoryList) {
        this.protobufFactoryList = protobufFactoryList;
    }

    @Override
    public boolean supports(Class<?> protobuf) {
        for(ProtobufFactory factory: protobufFactoryList){
            if(factory.supports(protobuf)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Protobuf load(Class<?> protobuf, Configuration configuration) {
        for(ProtobufFactory factory: protobufFactoryList){
            if(factory.supports(protobuf)){
                return factory.load(protobuf, configuration);
            }
        }
        return null;
    }
}
