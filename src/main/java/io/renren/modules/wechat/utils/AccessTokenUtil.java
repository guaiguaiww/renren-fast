package io.renren.modules.wechat.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 重置accseeToken
 * @author 18534
 *
 */
public class AccessTokenUtil {

    private final static Logger logger = LoggerFactory.getLogger(AccessTokenUtil.class);
    //获取AccessToken
    private final static String requestUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //获取jsApiTicket
    private static final String jsapi_ticket_url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    public static Map<String,Object> getAccseeToken(String appid, String appsecret){
        Map<String,Object> data = new HashMap<String,Object>();
        try {
            //替换参数，生成请求路径
            String url = requestUrl.replace("APPID", appid).replace("APPSECRET", appsecret);
            logger.info("AccseeToken request url={}.", new Object[]{url});
            //发送请求，并获取返回值
            JSONObject jsonObj = WeiXinHttpUtil.sendGet(url);
            logger.info("AccseeToken response jsonStr={}.", new Object[]{jsonObj});
            if(null != jsonObj){
                if(jsonObj.containsKey("access_token")){
                    String accessToken=jsonObj.getString("access_token");
                    data.put("accessToken", accessToken);
                    data.put("accessTokenTime", new Date());
                    //获取jsapi(JS-SDK用)
                    String jsApiUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", accessToken);
                    JSONObject jsonObjJsApi = WeiXinHttpUtil.sendGet(jsApiUrl);
                    logger.info("AccseeToken response jsonObjJsApi={}.", new Object[]{jsonObjJsApi});
                    if(jsonObjJsApi != null){
                        String jsApiTicket = jsonObjJsApi.getString("ticket");
                        data.put("jsApiTicket", jsApiTicket);
                        data.put("jsApiTicketTime", new Date());
                    }
                    data.put("status", "success");
                }else{//请求失败
                    if(jsonObj.containsKey("errcode")){
                        logger.error("AccseeToken request error={}.", new Object[]{jsonObj.getString("errmsg")});
                        data.put("status", "responseErr");
                        //author:sunkai  date:2018-09-26  for:全局返回码说明
                        data.put("errcode", jsonObj.getString("errcode"));
                        String msg = "";
                        if(jsonObj.getString("errcode").equals("40164")){
                            msg = "当前平台的IP未添加到微信公众号IP白名单中，请前往微信公众平台配置";
                        }else{
                            msg = "AppID或AppSecret不正确，请认真检查您的 AppID和AppSecret";
                        }
                        data.put("msg",msg);
                        //author:sunkai  date:2018-09-26  for:全局返回码说明
                    }
                }
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            data.put("status", "sysError");
            logger.error("AccseeToken error={}.", new Object[]{e});
            return data;
        }

    }
}
