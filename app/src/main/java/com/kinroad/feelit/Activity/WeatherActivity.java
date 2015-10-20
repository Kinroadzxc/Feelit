package com.kinroad.feelit.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kinroad.feelit.R;
import com.kinroad.feelit.Util.AppController;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class WeatherActivity extends Activity {

    private LocationManager locationManager;
    private String provider;

    public static final int GET_LOCATION = 0;

    //启动此活动
    public static void start(Context context) {
        Intent intent = new Intent(context, WeatherActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //加入AppController列表
        AppController.addActivity(this);

        //获取位置服务
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "位置服务不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取实时经纬度
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null){
            //执行获取经纬度数据逻辑
            Log.d("当前经纬度", "latitude is " + location.getLatitude() + "\n" + "longitude is " + location.getLongitude() );
            getLocation(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    //配置位置监听器
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //更新当前经纬度信息
            Log.d("当前经纬度", "latitude is " + location.getLatitude() + "\n" + "longitude is " + location.getLongitude() );
            getLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    //获取城市位置信息
    private void getLocation(final Location location) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //组装反向地理编码的接口地址
                    StringBuilder url = new StringBuilder();

                    url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
                    url.append(location.getLatitude()).append(",");
                    url.append(location.getLongitude());
                    url.append("&sensor=false");

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url.toString());

                    //指定返回语言
                    httpGet.addHeader("Accept-Language", "zh-CN");
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        Log.d("连接位置服务", "成功");
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        JSONObject jsonObject = new JSONObject(response);

                        //读取result中位置信息
                        JSONArray resultArray = jsonObject.getJSONArray("results");
                        if (resultArray.length() > 0) {
                            JSONObject subObject = resultArray.getJSONObject(0);

                            String address = subObject.getString("formatted_address");
                            Log.d("当前位置", address);
//                            Message message = new Message();
//                            message.what = GET_LOCATION;
//                            message.obj = address;
//                            handler.handleMessage(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //关闭位置监听器
        if (locationManager != null){
            locationManager.removeUpdates(locationListener);
        }

        //从AppController移除
        AppController.removeActivity(this);
    }
}
