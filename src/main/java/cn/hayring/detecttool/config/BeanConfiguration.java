package cn.hayring.detecttool.config;

import cn.hayring.detecttool.domain.Node;
import cn.hayring.detecttool.domain.Person;
import com.google.gson.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;

@Configuration
public class BeanConfiguration {


    /**
     * 加密工具
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



//    @Bean
//    GsonHttpMessageConverter gsonHttpMessageConverter(){
//
//        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(Person.class, new NodeSerializer());
//        //其他配置
//        gson = gsonBuilder.create();
//        converter.setGson(gson);
//        return converter;
//    }

    @Bean
    public Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        //其他配置
        //日期格式化
        gsonBuilder.setDateFormat("yyyy年MM月dd日");
        return gsonBuilder.create();
    }

}

//
//class NodeSerializer implements JsonSerializer<Node> {
//
//    @Override
//    public JsonElement serialize(final Node node, final Type type, final JsonSerializationContext context) {
//        final JsonElement json = context.
//        json.getAsJsonObject().addProperty("label", node.getLabel());
//        return json;
//    }
//}