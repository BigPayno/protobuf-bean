package protobuf.mapper;

import com.squareup.javapoet.JavaFile;
import protobuf.Configuration;
import protobuf.Protobuf;
import protobuf.ProtobufMapper;

import java.util.List;

public class ChainedProtobufMapper implements ProtobufMapper {

    List<ProtobufMapper> protobufMapperList;

    public ChainedProtobufMapper(List<ProtobufMapper> protobufMapperList) {
        this.protobufMapperList = protobufMapperList;
    }

    @Override
    public boolean supports(Protobuf protobuf) {
        for(ProtobufMapper mapper : protobufMapperList){
            if(mapper.supports(protobuf)){
                return true;
            }
        }
        return false;
    }

    @Override
    public JavaFile mapFrom(Protobuf protobuf, Configuration configuration) {
        for(ProtobufMapper mapper : protobufMapperList){
            if(mapper.supports(protobuf)){
                return mapper.mapFrom(protobuf, configuration);
            }
        }
        return null;
    }
}
