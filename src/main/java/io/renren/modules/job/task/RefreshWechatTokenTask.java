package io.renren.modules.job.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.wechat.entity.TencentUser;
import io.renren.modules.wechat.service.TencentUserService;
import io.renren.modules.wechat.utils.AccessTokenUtil;
import io.renren.modules.wechat.utils.CommonWeixinProperties;
import io.renren.modules.wechat.vo.WechatAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @Author hss
 * @Date 2020/12/13  20:27:00
 * 定时刷新微信accessToken等令牌信息
 */
@Component("refreshWechatTokenTask")
public class RefreshWechatTokenTask implements ITask{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private TencentUserService tencentUserService;

    @Override
    public void run(String params) {
        logger.info("开始刷新公众号token信息，参数为{}",params);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("app_id",CommonWeixinProperties.component_appid);
        TencentUser tencentUser = tencentUserService.getOne(wrapper);
        if(null != tencentUser){
            //根据appid和appsecret获取并更新token信息
            Map<String, Object> accseeToken = AccessTokenUtil.getAccseeToken(tencentUser.getAppId(), tencentUser.getAppSecret());
            if(accseeToken.containsKey("status") && "success".equals(accseeToken.get("status"))){
                String accessToken = accseeToken.get("accessToken").toString();
                Date tokenGettime = ((Date) accseeToken.get("accessTokenTime"));
                //1.更新redis
                tencentUser.setAccessToken(accessToken);
                tencentUser.setTokenGettime(tokenGettime);
                if (accseeToken.containsKey("jsApiTicket")) {
                    String jsApiTicket = accseeToken.get("jsApiTicket").toString();
                    Date jsApiTicketTime = (Date) accseeToken.get("jsApiTicketTime");
                    //赋入JsApiTicket
                    tencentUser.setJsApiTicket(jsApiTicket);
                    tencentUser.setJsApiTicketTime(jsApiTicketTime);
                }
                redisUtils.set("res_wechat_account",new WechatAccount(tencentUser),4*60);
                //2.更新数据库
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("app_id",tencentUser.getAppId());
                updateWrapper.set("access_token",accessToken);
                updateWrapper.set("token_gettime",tokenGettime);
                updateWrapper.set("js_api_ticket",tencentUser.getJsApiTicket());
                updateWrapper.set("js_api_ticket_time",tencentUser.getJsApiTicketTime());
                tencentUserService.update(updateWrapper);
            }
        }
        logger.info("成功刷新公众号token信息");
    }
}
