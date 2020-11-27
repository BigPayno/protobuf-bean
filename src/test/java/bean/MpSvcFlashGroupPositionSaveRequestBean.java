package bean;

import com.qingqing.mpsvc.proto.flashgroupposition.MPSVCFlashGroupPositionProto;
import protobuf.annotation.ProtobufSource;
import protobuf.utils.Optionals;

@ProtobufSource(
    protobufClass = MPSVCFlashGroupPositionProto.MpSvcFlashGroupPositionSaveRequest.class
)
public class MpSvcFlashGroupPositionSaveRequestBean {
  private MpSvcFlashGroupPositionInfoBean info;

  public MpSvcFlashGroupPositionInfoBean getInfo() {
    return this.info;
  }

  public void setInfo(MpSvcFlashGroupPositionInfoBean info) {
    this.info = info;
  }

  public static MpSvcFlashGroupPositionSaveRequestBean extractFrom(
      MPSVCFlashGroupPositionProto.MpSvcFlashGroupPositionSaveRequest source) {
    MpSvcFlashGroupPositionSaveRequestBean sink = new MpSvcFlashGroupPositionSaveRequestBean();
    Optionals.forPredicate(source.hasInfo(),source::getInfo)
        .map(protobuf->MpSvcFlashGroupPositionInfoBean.extractFrom(protobuf))
        .ifPresent(sink::setInfo);
    return sink;
  }

  public MPSVCFlashGroupPositionProto.MpSvcFlashGroupPositionSaveRequest toProtobuf() {
    MPSVCFlashGroupPositionProto.MpSvcFlashGroupPositionSaveRequest.Builder sink = MPSVCFlashGroupPositionProto.MpSvcFlashGroupPositionSaveRequest.newBuilder();
    Optionals.forNotNull(this::getInfo) .map(MpSvcFlashGroupPositionInfoBean::toProtobuf)
        .ifPresent(sink::setInfo);
    return sink.build();
  }
}
