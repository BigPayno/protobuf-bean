package protobuf.mapper;

import com.google.protobuf.ProtocolMessageEnum;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import protobuf.Configuration;
import protobuf.Protobuf;
import protobuf.ProtobufMapper;
import protobuf.type.EnumProtobuf;
import protobuf.utils.Types;

import javax.lang.model.element.Modifier;

public class EnumProtobufBeanMapper implements ProtobufMapper {

    @Override
    public boolean supports(Protobuf protobuf) {
        return EnumProtobuf.class.isAssignableFrom(protobuf.getClass());
    }

    private String generateClassName(Class<?> enumName){
        return enumName.getSimpleName().endsWith("Enum")?
                enumName.getSimpleName():enumName.getSimpleName()+"Enum";
    }

    @Override
    public JavaFile mapFrom(Protobuf protobuf, Configuration configuration) {
        if(protobuf instanceof EnumProtobuf){
            EnumProtobuf enumProtobuf = (EnumProtobuf) protobuf;
            Builder builder = TypeSpec.enumBuilder(generateClassName(protobuf.protobufType()))
                    .addModifiers(Modifier.PUBLIC);
            builder.addField(
                    FieldSpec.builder(Integer.TYPE,"value",Modifier.PRIVATE)
                    .build()
            );
            builder.addMethod(
                    MethodSpec.constructorBuilder()
                            .addParameter(Integer.TYPE,"value")
                            .addStatement("this.$N = value","value")
                            .build()
            );
            builder.addMethod(
                    MethodSpec.methodBuilder("getValue")
                            .addModifiers(Modifier.PUBLIC)
                            .addStatement("return this.$N","value")
                            .returns(TypeName.INT)
                            .build()
            );
            enumProtobuf.getEnumSet().stream()
                    .map(item-> Types.castUnsafe(item, ProtocolMessageEnum.class))
                    .forEach(item->{
                        builder.addEnumConstant(
                                ((Enum) item).name(),
                                TypeSpec.anonymousClassBuilder("$L",item.getNumber()).build()
                        );
                    });
            return JavaFile
                    .builder(configuration.generatePackage(),builder.build())
                    .build();
        }
        return null;
    }

}
