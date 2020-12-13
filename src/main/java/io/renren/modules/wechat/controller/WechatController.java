package io.renren.modules.wechat.controller;

import io.renren.modules.wechat.entity.TencentUser;
import io.renren.modules.wechat.service.TencentUserService;
import io.renren.modules.wechat.utils.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author hss
 * @Date 2020/12/13  14:06:00
 * 微信入口
 */
@RestController
@RequestMapping("/wechat")
public class WechatController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TencentUserService tencentUserService;

    @GetMapping(value = "/wechatMessage")
    public void wechatGet(HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestParam(value = "signature") String signature,
                          @RequestParam(value = "timestamp") String timestamp,
                          @RequestParam(value = "nonce") String nonce,
                          @RequestParam(value = "echostr") String echostr) throws IOException {

        logger.info("-------------------微信公众号响应消息------------wechat------");
        List<TencentUser> tencentUsers = tencentUserService.queryAll();

        if(tencentUsers != null && tencentUsers.size() > 0) {
            logger.info("--------------tencentUsers------------size------" + tencentUsers.size());
            for (TencentUser tencentUser : tencentUsers) {
                if(SignUtil.checkSignature(tencentUser.getToken(), signature, timestamp, nonce)) {
                    try {
                        response.getWriter().print(echostr);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
