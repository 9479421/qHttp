package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.example.http.qHttp;
import org.example.http.qHttpResponse;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class YunKeTang {
    public static void main(String[] args) {
        YunKeTang yunKeTang = new YunKeTang("e0f331a0-2b18-415e-bb57-11f58df45df0");
        yunKeTang.watchVideo();
    }

    qHttp http = new qHttp();
    String token = "";

    public YunKeTang(String token) {
        this.token = token;

        http.AutoCookie();
        http.setHeader("token", this.token);
        http.setContentType("application/json");
        http.setHeader("appname", "交通安全云课堂");
        http.setHeader("version", "6.3.6");
        http.setUserAgent("Mozilla/5.0 (Linux; U; Android 10; zh-cn; Pixel Build/QP1A.191005.007.A3) AppleWebKit/533.1 (KHTML, like Gecko) Version/5.0 Mobile Safari/533.1");
        http.setHeader("deviceid", "");
    }
    public static String getUuid() {
        return UUID.randomUUID().toString();
    }
    public static String generateSecret(String str) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        String replaceAll = str.replaceAll("-", "");
        int i = calendar.get(2) + 1;
        int i2 = calendar.get(5);
        char charAt = replaceAll.charAt(i);
        char charAt2 = replaceAll.charAt(i2);
        String[] split = str.split("-");
        String substring = HashUtils.md5(StringUtils.reverse(split[i2 % 5])).substring(8, 23);
        String upperCase = HashUtils.md5(split[i % 5]).substring(8, 23).toUpperCase();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(charAt);
        stringBuffer.append(upperCase);
        stringBuffer.append(charAt2);
        stringBuffer.append(substring);
        return stringBuffer.toString();
    }


    public void watchVideo() {
        try {
            System.out.println(generateSecret("712f8340-dde6-4a19-a46d-dfc5cea73bfd"));

            http.open("https://es.staq360.com/app/forward/education/study/v5/plan/list");
            qHttpResponse response = http.post("{\"educationType\":\"ALL\",\"queryStatus\":\"1\"}");
            JSONObject json = JSON.parseObject(response.getBody());
            System.out.println(json.toJSONString());
            if (json.getInteger("code") == 200) {
                JSONArray json_list = json.getJSONObject("data").getJSONArray("list");
                for (int i = 0; i < json_list.size(); i++) {
                    String planId = json_list.getJSONObject(i).getString("planId");
                    String studyId = json_list.getJSONObject(i).getString("studyId");


                    http.open("https://es.staq360.com/app/forward/education/study/v2/video/" + studyId + "?token=");
                    response = http.get();
                    json = JSON.parseObject(response.getBody());
                    JSONArray jsonArray_courses = json.getJSONObject("data").getJSONArray("outlineList").getJSONObject(0).getJSONArray("coursewareList");
                    for (int j = 0; j < jsonArray_courses.size(); j++) {
                        String name = jsonArray_courses.getJSONObject(j).getString("name");
                        String duration = jsonArray_courses.getJSONObject(j).getString("duration");
                        System.out.println("章节名："+name + "====" +"时长:"+duration);
                        String studyOutlineId = jsonArray_courses.getJSONObject(j).getString("studyOutlineId");
                        Boolean studyComplete = jsonArray_courses.getJSONObject(j).getBoolean("studyComplete");

                        if (studyComplete){
                            continue;
                        }

                        watch("start",studyOutlineId, duration);
                        Thread.sleep(1000);
                        watch("pause",studyOutlineId, String.valueOf(Integer.valueOf(duration)/2));
                        Thread.sleep(1000);
                        watch("end",studyOutlineId, duration);

                    }
                }

            } else {
                throw new Exception("获取学习任务列表失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void watch(String action,String studyOutlineId,String duration) throws IOException {
        String timestamp = System.currentTimeMillis() + "";  //pause restart end
        String uuid = getUuid();
        String sign =  HashUtils.md5("action="+action+"&localTime="+timestamp+"&requestId="+uuid+"&requestTime="+timestamp+"&studyOutlineId="+studyOutlineId+"&studyedDuration="+duration+"&key="+generateSecret(uuid) );
        sign = sign.toUpperCase();
        System.out.println(sign);


        http.open("https://es.staq360.com/app/forward/education/study/v6/log/upload");
        qHttpResponse response = http.post("{\"action\":\""+action+"\",\"localTime\":\""+timestamp+"\",\"oldToken\":\"\",\"requestId\":\""+uuid+"\",\"requestTime\":\""+timestamp+"\",\"sign\":\""+sign+"\",\"studyOutlineId\":\""+studyOutlineId+"\",\"studyedDuration\":"+duration+"}");
        System.out.println(response.getBody());
    }

}
