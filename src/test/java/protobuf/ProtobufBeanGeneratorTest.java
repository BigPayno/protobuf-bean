package protobuf;

import com.qingqing.mpsvc.proto.flashgroupposition.MPSVCFlashGroupPositionProto.MpSvcFlashGroupPositionSaveRequest;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;

public class ProtobufBeanGeneratorTest extends TestCase {

    @Test
    public void test() throws IOException, ClassNotFoundException {
        ConfigurationProperties properties = new ConfigurationProperties();
        properties.setFileSystemPath("E:\\projects\\课程表\\protobuf-bean\\src\\test\\java");
        properties.setGeneratePackagePath("bean");
        properties.setProtobufBeanPackagePaths(new String[]{"bean"});
        ProtobufBeanGenerator generator = new ProtobufBeanGenerator(properties);
        generator.generateIfPossiable(MpSvcFlashGroupPositionSaveRequest.class);
        //generator.generateIfPossiable(MtsvcGroupBuyingBaseInfo.class);
        //generator.generateIfPossiable(UserType.class);
        //generator.generateIfPossiable(MtsvcGroupBuyingActivityListResponse.class);
        //generator.generateIfPossiable(TimeRange.class);
    }
}