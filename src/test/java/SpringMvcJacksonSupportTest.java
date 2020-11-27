import bean.MpSvcFlashGroupPositionInfoBean;
import bean.PositionRefActivityTypeEnum;
import bean.TimeRangeBean;
import com.google.common.io.ByteStreams;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import protobuf.utils.Types;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SpringMvcJacksonSupportTest {

    MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();

    class SimpleHttpOutputMessage implements HttpOutputMessage{

        HttpHeaders httpHeaders = new HttpHeaders();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        @Override
        public HttpHeaders getHeaders() {
            return httpHeaders;
        }

        @Override
        public ByteArrayOutputStream getBody() throws IOException {
            return outputStream;
        }
    }

    class SimpleHttpInputMessage implements HttpInputMessage{

        HttpHeaders httpHeaders = new HttpHeaders();
        ByteArrayInputStream inputStream;

        public SimpleHttpInputMessage(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public InputStream getBody() throws IOException {
            return inputStream;
        }

        @Override
        public HttpHeaders getHeaders() {
            return httpHeaders;
        }
    }

    private <T> byte[] writeResponse(T t) throws IOException {
        SimpleHttpOutputMessage response = new SimpleHttpOutputMessage();
        if(jacksonConverter.canWrite(response.getClass(), MediaType.APPLICATION_JSON)){
            jacksonConverter.write(t,null,MediaType.APPLICATION_JSON, response);
        }else{
            throw new IllegalArgumentException(String.format("not support writing this type[%s]",t.getClass()));
        }
        return ByteStreams.newDataOutput(response.getBody()).toByteArray();
    }

    private <T> T readRequest(byte[] messgae, Class<T> clazz) throws IOException {
        SimpleHttpInputMessage inputMessage = new SimpleHttpInputMessage(new ByteArrayInputStream(messgae));
        if(jacksonConverter.canRead(clazz, MediaType.APPLICATION_JSON)){
            T input = Types.castUnsafe(jacksonConverter.read(clazz, inputMessage),clazz);
            return input;
        }else{
            throw new IllegalArgumentException(String.format("not support reading this type[%s]",clazz));
        }
    }

    private void print(byte[] bytes){
        System.out.println(new String(bytes));
    }

    private <T> void testType(T t) throws IOException {
        byte[] message = writeResponse(t);
        print(message);
        System.out.println(readRequest(message, t.getClass()));
    }

    @Test
   public void jacksonSupportEnum() throws IOException {
        testType(PositionRefActivityTypeEnum.group_buying_activity_type_enum);
    }

   @Test
   public void jacksonSupportSingleBean() throws IOException {
       TimeRangeBean timeRangeBean = new TimeRangeBean();
       timeRangeBean.setStartTime(11111L);
       testType(timeRangeBean);
   }

   @Test
   public void jacksonSupportComposeBean() throws Exception{
       MpSvcFlashGroupPositionInfoBean bean = new MpSvcFlashGroupPositionInfoBean();
       bean.setActivityType(PositionRefActivityTypeEnum.group_buying_activity_type_enum);
       TimeRangeBean timeRangeBean = new TimeRangeBean();
       timeRangeBean.setStartTime(11111L);
       bean.setPositionValidTime(timeRangeBean);
       testType(bean);
   }
}
