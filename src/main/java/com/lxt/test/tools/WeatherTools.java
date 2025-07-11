package com.lxt.test.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
@Component
public class WeatherTools {

//    @Tool(name = "天气获取", value = "可以通过该接口获取指定城市的天气，入参city是城市的名称 返回值是关于天气的描述")
//    public String getWeather(@ToolMemoryId String memoryId, @P(required = true, value = "城市") String city) {
//        log.info("getWeather: {}, {}", memoryId, city);
//        if (city.equals("石家庄")) {
//            return "今天" + city + "的天气是阴天";
//        }
//        return "今天" + city + "的天气是晴天";
//    }

    @Tool(name = "订单查询", value = "可以通过该接口查询指定订单的订单信息，入参orderCode是订单编号 订单信息返回值是订单信息")
    public Tuple2<Boolean, String> getOrder(@P(required = true, value = "订单编号") String orderCode) {
        if (StringUtils.isBlank(orderCode)) {
            return Tuples.of(false, "订单编号不能为空");
        }
        if (orderCode.equals("834489522")) {
            return Tuples.of(true, "收货手机号:15311459651,收货地址：河北省石家庄市深泽县，收货人：李啸天");
        }
        return Tuples.of(false, "订单不存在");
    }
}
