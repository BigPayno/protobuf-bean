package protobuf.generate;

import com.qingqing.mpsvc.proto.flashgroupposition.MPSVCFlashGroupPositionProto;
import java.lang.Long;
import java.lang.String;
import protobuf.annotation.ProtobufSource;
import protobuf.utils.Optionals;

@ProtobufSource(
    protobufClass = MPSVCFlashGroupPositionProto.MpSvcFlashGroupInfo.class
)
public class MpSvcFlashGroupInfoBean {
  private Long qgroupId;

  private String qgroupName;

  public Long getQgroupId() {
    return this.qgroupId;
  }

  public void setQgroupId(Long qgroupId) {
    this.qgroupId = qgroupId;
  }

  public String getQgroupName() {
    return this.qgroupName;
  }

  public void setQgroupName(String qgroupName) {
    this.qgroupName = qgroupName;
  }

  public static MpSvcFlashGroupInfoBean extractFrom(
      MPSVCFlashGroupPositionProto.MpSvcFlashGroupInfo source) {
    MpSvcFlashGroupInfoBean sink = new MpSvcFlashGroupInfoBean();
    Optionals.forPredicate(source.hasQgroupId(),source::getQgroupId).ifPresent(sink::setQgroupId);
    Optionals.forPredicateAndNotBlank(source.hasQgroupName(),source::getQgroupName).ifPresent(sink::setQgroupName);
    return sink;
  }

  public MPSVCFlashGroupPositionProto.MpSvcFlashGroupInfo toProtobuf() {
    MPSVCFlashGroupPositionProto.MpSvcFlashGroupInfo.Builder sink = MPSVCFlashGroupPositionProto.MpSvcFlashGroupInfo.newBuilder();
    Optionals.forNotNull(this::getQgroupId).ifPresent(sink::setQgroupId);
    Optionals.forNotNullAndEmpty(this::getQgroupName).ifPresent(sink::setQgroupName);
    return sink.build();
  }
}
