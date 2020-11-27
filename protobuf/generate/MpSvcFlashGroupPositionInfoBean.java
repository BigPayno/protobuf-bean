package protobuf.generate;

import com.qingqing.mpsvc.proto.flashgroupposition.MPSVCFlashGroupPositionProto;
import com.qingqing.mpsvc.proto.position.MPSVCPositionProto;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import java.util.stream.Collectors;
import protobuf.annotation.ProtobufSource;
import protobuf.utils.Optionals;

@ProtobufSource(
    protobufClass = MPSVCFlashGroupPositionProto.MpSvcFlashGroupPositionInfo.class
)
public class MpSvcFlashGroupPositionInfoBean {
  private Long id;

  private String positionName;

  private List<Long> qgroupIdList;

  private PositionRefActivityTypeEnum activityType;

  private Long activityId;

  private String defaultChannel;

  private TimeRangeBean positionValidTime;

  private String activityRule;

  private String customerServiceQrMediaId;

  private String customerServiceQrMediaUrl;

  private List<MpSvcFlashGroupInfoBean> flashGroupInfoList;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPositionName() {
    return this.positionName;
  }

  public void setPositionName(String positionName) {
    this.positionName = positionName;
  }

  public List<Long> getQgroupIdList() {
    return this.qgroupIdList;
  }

  public void setQgroupIdList(List<Long> qgroupIdList) {
    this.qgroupIdList = qgroupIdList;
  }

  public PositionRefActivityTypeEnum getActivityType() {
    return this.activityType;
  }

  public void setActivityType(PositionRefActivityTypeEnum activityType) {
    this.activityType = activityType;
  }

  public Long getActivityId() {
    return this.activityId;
  }

  public void setActivityId(Long activityId) {
    this.activityId = activityId;
  }

  public String getDefaultChannel() {
    return this.defaultChannel;
  }

  public void setDefaultChannel(String defaultChannel) {
    this.defaultChannel = defaultChannel;
  }

  public TimeRangeBean getPositionValidTime() {
    return this.positionValidTime;
  }

  public void setPositionValidTime(TimeRangeBean positionValidTime) {
    this.positionValidTime = positionValidTime;
  }

  public String getActivityRule() {
    return this.activityRule;
  }

  public void setActivityRule(String activityRule) {
    this.activityRule = activityRule;
  }

  public String getCustomerServiceQrMediaId() {
    return this.customerServiceQrMediaId;
  }

  public void setCustomerServiceQrMediaId(String customerServiceQrMediaId) {
    this.customerServiceQrMediaId = customerServiceQrMediaId;
  }

  public String getCustomerServiceQrMediaUrl() {
    return this.customerServiceQrMediaUrl;
  }

  public void setCustomerServiceQrMediaUrl(String customerServiceQrMediaUrl) {
    this.customerServiceQrMediaUrl = customerServiceQrMediaUrl;
  }

  public List<MpSvcFlashGroupInfoBean> getFlashGroupInfoList() {
    return this.flashGroupInfoList;
  }

  public void setFlashGroupInfoList(List<MpSvcFlashGroupInfoBean> flashGroupInfoList) {
    this.flashGroupInfoList = flashGroupInfoList;
  }

  public static MpSvcFlashGroupPositionInfoBean extractFrom(
      MPSVCFlashGroupPositionProto.MpSvcFlashGroupPositionInfo source) {
    MpSvcFlashGroupPositionInfoBean sink = new MpSvcFlashGroupPositionInfoBean();
    Optionals.forPredicate(source.hasId(),source::getId).ifPresent(sink::setId);
    Optionals.forPredicateAndNotBlank(source.hasPositionName(),source::getPositionName).ifPresent(sink::setPositionName);
    sink.qgroupIdList = source.getQgroupIdList();
    Optionals.forPredicate(source.hasActivityType(),source::getActivityType)
        .map(item->PositionRefActivityTypeEnum.valueOf(item.getNumber()))
        .ifPresent(sink::setActivityType);
    Optionals.forPredicate(source.hasActivityId(),source::getActivityId).ifPresent(sink::setActivityId);
    Optionals.forPredicateAndNotBlank(source.hasDefaultChannel(),source::getDefaultChannel).ifPresent(sink::setDefaultChannel);
    Optionals.forPredicate(source.hasPositionValidTime(),source::getPositionValidTime)
        .map(protobuf->TimeRangeBean.extractFrom(protobuf)) .ifPresent(sink::setPositionValidTime);
    Optionals.forPredicateAndNotBlank(source.hasActivityRule(),source::getActivityRule).ifPresent(sink::setActivityRule);
    Optionals.forPredicateAndNotBlank(source.hasCustomerServiceQrMediaId(),source::getCustomerServiceQrMediaId).ifPresent(sink::setCustomerServiceQrMediaId);
    Optionals.forPredicateAndNotBlank(source.hasCustomerServiceQrMediaUrl(),source::getCustomerServiceQrMediaUrl).ifPresent(sink::setCustomerServiceQrMediaUrl);
    sink.flashGroupInfoList = source.getFlashGroupInfoList().stream()
        .map(protobuf->MpSvcFlashGroupInfoBean.extractFrom(protobuf)).collect(Collectors.toList());
    return sink;
  }

  public MPSVCFlashGroupPositionProto.MpSvcFlashGroupPositionInfo toProtobuf() {
    MPSVCFlashGroupPositionProto.MpSvcFlashGroupPositionInfo.Builder sink = MPSVCFlashGroupPositionProto.MpSvcFlashGroupPositionInfo.newBuilder();
    Optionals.forNotNull(this::getId).ifPresent(sink::setId);
    Optionals.forNotNullAndEmpty(this::getPositionName).ifPresent(sink::setPositionName);
    Optionals.forNotNull(this::getQgroupIdList).ifPresent(sink::addAllQgroupId);
    Optionals.forNotNull(this::getActivityType)
        .map(bean->MPSVCPositionProto.PositionRefActivityTypeEnum.valueOf(bean.getValue()))
        .ifPresent(sink::setActivityType);
    Optionals.forNotNull(this::getActivityId).ifPresent(sink::setActivityId);
    Optionals.forNotNullAndEmpty(this::getDefaultChannel).ifPresent(sink::setDefaultChannel);
    Optionals.forNotNull(this::getPositionValidTime) .map(TimeRangeBean::toProtobuf)
        .ifPresent(sink::setPositionValidTime);
    Optionals.forNotNullAndEmpty(this::getActivityRule).ifPresent(sink::setActivityRule);
    Optionals.forNotNullAndEmpty(this::getCustomerServiceQrMediaId).ifPresent(sink::setCustomerServiceQrMediaId);
    Optionals.forNotNullAndEmpty(this::getCustomerServiceQrMediaUrl).ifPresent(sink::setCustomerServiceQrMediaUrl);
    Optionals.forNotNull(this::getFlashGroupInfoList)
        .map(beanList-> beanList.stream().map(bean->bean.toProtobuf()).collect(Collectors.toList())).ifPresent(sink::addAllFlashGroupInfo);
    return sink.build();
  }
}
