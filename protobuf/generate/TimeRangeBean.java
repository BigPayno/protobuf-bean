package protobuf.generate;

import com.qingqing.api.proto.v1.Time;
import java.lang.Long;
import protobuf.annotation.ProtobufSource;
import protobuf.utils.Optionals;

@ProtobufSource(
    protobufClass = Time.TimeRange.class
)
public class TimeRangeBean {
  private Long startTime;

  private Long endTime;

  public Long getStartTime() {
    return this.startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public Long getEndTime() {
    return this.endTime;
  }

  public void setEndTime(Long endTime) {
    this.endTime = endTime;
  }

  public static TimeRangeBean extractFrom(Time.TimeRange source) {
    TimeRangeBean sink = new TimeRangeBean();
    Optionals.forPredicate(source.hasStartTime(),source::getStartTime).ifPresent(sink::setStartTime);
    Optionals.forPredicate(source.hasEndTime(),source::getEndTime).ifPresent(sink::setEndTime);
    return sink;
  }

  public Time.TimeRange toProtobuf() {
    Time.TimeRange.Builder sink = Time.TimeRange.newBuilder();
    Optionals.forNotNull(this::getStartTime).ifPresent(sink::setStartTime);
    Optionals.forNotNull(this::getEndTime).ifPresent(sink::setEndTime);
    return sink.build();
  }
}
