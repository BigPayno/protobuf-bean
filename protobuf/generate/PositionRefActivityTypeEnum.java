package protobuf.generate;

import com.qingqing.mpsvc.proto.position.MPSVCPositionProto;
import java.util.EnumSet;
import protobuf.annotation.ProtobufSource;

@ProtobufSource(
    protobufClass = MPSVCPositionProto.PositionRefActivityTypeEnum.class
)
public enum PositionRefActivityTypeEnum {
  unknown_position_ref_activity_type_enum(-1),

  group_buying_activity_type_enum(1);

  private int value;

  PositionRefActivityTypeEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

  public static PositionRefActivityTypeEnum valueOf(int val) {
    return EnumSet.allOf(PositionRefActivityTypeEnum.class).stream()
        .filter(item -> item.value == val )
        .findFirst().orElse(PositionRefActivityTypeEnum.unknown_position_ref_activity_type_enum);
  }
}
