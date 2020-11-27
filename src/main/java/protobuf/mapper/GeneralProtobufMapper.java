package protobuf.mapper;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import protobuf.Configuration;
import protobuf.Protobuf;
import protobuf.ProtobufFieldParser;
import protobuf.ProtobufMapper;
import protobuf.field.ProtobufField;
import protobuf.type.GeneralProtobuf;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralProtobufMapper implements ProtobufMapper {

    List<ProtobufFieldParser> parsers;

    public GeneralProtobufMapper(List<ProtobufFieldParser> parsers) {
        this.parsers = parsers;
    }

    @Override
    public boolean supports(Protobuf protobuf) {
        return GeneralProtobuf.class.isAssignableFrom(protobuf.getClass());
    }

    @Override
    public JavaFile mapFrom(Protobuf protobuf, Configuration configuration) {
        Class<?> protobufType = protobuf.protobufType();
        Builder builder = TypeSpec.classBuilder(protobufType.getSimpleName()+"Bean")
                .addModifiers(Modifier.PUBLIC);
        List<ProtobufField> protobufFields = protobuf.fields(configuration).stream()
                .map(field -> ProtobufField.of(protobuf.protobufType(), field))
                .collect(Collectors.toList());
        protobufFields.forEach(protobufField -> {
            for(ProtobufFieldParser parser: parsers){
                try {
                    if(parser.supports(protobufField,configuration)){
                        builder.addField(parser.initFieldSpec(protobufField,configuration))
                                .addMethod(parser.getterMethodSpec(protobufField,configuration))
                                .addMethod(parser.setterMethodSpec(protobufField,configuration));
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        if(protobuf instanceof GeneralProtobuf){
            ((GeneralProtobuf) protobuf).setProtobufFields(protobufFields);
        }
        return JavaFile
                .builder(configuration.generatePackage(),builder.build())
                .build();
    }
}
