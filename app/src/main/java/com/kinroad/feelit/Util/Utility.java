package com.kinroad.feelit.Util;

import android.text.TextUtils;

import com.kinroad.feelit.Database.WeatherDB;
import com.kinroad.feelit.Model.City;
import com.kinroad.feelit.Model.County;
import com.kinroad.feelit.Model.Province;

public class Utility {

    //解析处理省级数据
    public synchronized static boolean handleProvinceResponse(WeatherDB weatherDB, String response){

        if (!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            //数据非空时解析并存储数据
            if (allProvinces != null && allProvinces.length>0){
                //解析数据
                for (String p : allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //存储数据
                    weatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    //解析处理市级数据
    public static boolean handleCitiesResponse(WeatherDB weatherDB,String response,int provinceId){

        if (!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length>0){
                for (String c : allCities){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);

                    weatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    //解析处理县级数据
    public static boolean handleCountiesResponse(WeatherDB weatherDB,String response,int cityId){

        if (!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length>0){
                for (String c : allCounties){
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);

                    weatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

}
