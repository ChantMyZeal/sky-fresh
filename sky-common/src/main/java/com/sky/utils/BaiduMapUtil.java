package com.sky.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.entity.DeliveryPath;
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
     * 根据地址信息查询经纬度坐标
     *
     * @param map      查询参数
     * @param eMessage 抛出异常时显示的信息
     * @return 返回经纬度坐标
     * @throws MapException 抛出异常
     */
    public String getLngLat(Map<String, String> map, String eMessage) throws MapException {
        //查询坐标
        String coordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);
        JSONObject jsonObject = JSON.parseObject(coordinate);
        if (!jsonObject.getString("status").equals("0")) {
            throw new MapException(eMessage);
        }
        //数据解析
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        String lat = location.getString("lat");
        String lng = location.getString("lng");
        //返回结果
        return lat + "," + lng;
    }

    /**
     * 查询店铺的经纬度坐标
     *
     * @return 返回坐标
     */
    public String getShopLngLat() {
        Map<String, String> map = new HashMap<>();
        map.put("ak", ak);
        map.put("address", shopAddress);
        map.put("output", "json");
        return getLngLat(map, MessageConstant.SHOP_ADDRESS_FAILED);
    }

    /**
     * 根据两地经纬度查询运输距离和运输时间
     *
     * @param shopLngLat 店铺地址经纬度
     * @param userLngLat 用户收货地址经纬度
     * @return 返回运输路径实体对象
     */
    public DeliveryPath getPath(String shopLngLat, String userLngLat) {
        Map<String, String> map = new HashMap<>();
        map.put("ak", ak);
        map.put("origin", shopLngLat);
        map.put("destination", userLngLat);
        map.put("steps_info", "0");
        //路线规划
        String json = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving", map);
        JSONObject jsonObject = JSON.parseObject(json);
        if (!jsonObject.getString("status").equals("0")) {
            throw new MapException(MessageConstant.DELIVERY_ROUTING_FAILED);
        }
        //数据解析
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routes");
        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");
        Integer duration = (Integer) ((JSONObject) jsonArray.get(0)).get("duration");

        return DeliveryPath.builder()
                .distance(distance)
                .duration(duration)
                .build();
    }

    /**
     * 查询店铺地址与用户收货地址之间的运输距离和运输时间
     *
     * @param userAddress 用户收货地址
     * @return 返回运输路径实体对象
     */
    public DeliveryPath getPathByUserAddress(String userAddress) {
        //获取店铺的经纬度坐标
        String shopLngLat = getShopLngLat();

        Map<String, String> map = new HashMap<>();
        map.put("ak", ak);
        map.put("address", userAddress);
        map.put("output", "json");
        //获取用户收货地址的经纬度坐标
        String userLngLat = getLngLat(map, MessageConstant.USER_ADDRESS_FAILED);

        return getPath(shopLngLat, userLngLat);
    }

    /**
     * 查询店铺地址与用户收货地址经纬度之间的运输距离和运输时间
     *
     * @param userLngLat 用户收货地址经纬度
     * @return 返回运输路径实体对象
     */
    public DeliveryPath getPathByUserLngLat(String userLngLat) {
        //获取店铺的经纬度坐标
        String shopLngLat = getShopLngLat();

        return getPath(shopLngLat, userLngLat);
    }

}
