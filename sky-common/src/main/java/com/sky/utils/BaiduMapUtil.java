package com.sky.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.exception.MapException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 百度地图工具类
 */
@Component
@Slf4j
public class BaiduMapUtil {

    @Value("${sky.shop.address}")
    private String shopAddress;// todo 把商家信息存储在redis或mysql中方便修改，而不是写在yml配置文件，添加查询与修改商家信息的接口
    @Value("${sky.baidu.ak}")
    private String ak;

    /**
     * 检查用户的收货地址是否超出配送范围
     *
     * @param address 用户收货地址
     * @return 返回店铺地址与用户收货地址之间的距离（单位：米）
     */
    public Integer getDistance(String address) {
        Map<String, String> map = new HashMap<>();
        map.put("address", shopAddress);
        map.put("output", "json");
        map.put("ak", ak);

        //获取店铺的经纬度坐标
        String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);
        JSONObject jsonObject = JSON.parseObject(shopCoordinate);
        if (!jsonObject.getString("status").equals("0")) {
            throw new MapException(MessageConstant.SHOP_ADDRESS_FAILED);
        }
        //数据解析
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        String lat = location.getString("lat");
        String lng = location.getString("lng");
        //店铺经纬度坐标
        String shopLngLat = lat + "," + lng;

        map.put("address", address);
        //获取用户收货地址的经纬度坐标
        String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);
        jsonObject = JSON.parseObject(userCoordinate);
        if (!jsonObject.getString("status").equals("0")) {
            throw new MapException(MessageConstant.USER_ADDRESS_FAILED);
        }
        //数据解析
        location = jsonObject.getJSONObject("result").getJSONObject("location");
        lat = location.getString("lat");
        lng = location.getString("lng");
        //用户收货地址经纬度坐标
        String userLngLat = lat + "," + lng;

        map.put("origin", shopLngLat);
        map.put("destination", userLngLat);
        map.put("steps_info", "0");
        //路线规划
        String json = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving", map);
        jsonObject = JSON.parseObject(json);
        if (!jsonObject.getString("status").equals("0")) {
            throw new MapException(MessageConstant.DELIVERY_ROUTING_FAILED);
        }
        //数据解析
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routes");
        return (Integer) ((JSONObject) jsonArray.get(0)).get("distance");
    }

}
