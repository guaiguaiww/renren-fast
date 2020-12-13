package io.renren.modules.wechat.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WeiXinHttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(WeiXinHttpUtil.class);

    public WeiXinHttpUtil() {
    }

    public static JSONObject sendGet(String url) {
        JSONObject jsonObject = null;
        String result = "";
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(30000);
            connection.connect();
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

            String line;
            for(reader = new BufferedReader(inputStreamReader); (line = reader.readLine()) != null; result = result + line) {
                ;
            }
        } catch (Exception var17) {
            logger.info("发送GET请求出现异常！" + var17);
            var17.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }

                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception var16) {
                var16.printStackTrace();
            }

        }

        jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }
}
