package io.renren.modules.wechat.service.impl;

import io.renren.common.utils.PageUtils;
import io.renren.modules.wechat.service.TencentUserService;
import io.renren.modules.wechat.utils.CommonWeixinProperties;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TencentUserServiceImplTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TencentUserService tencentUserService;

    @Test
    void queryPage() {
        Map<String,Object> map = new HashMap<>();
        map.put("name",null);
        map.put("appId",null);
        //page  limit
        map.put("page","1");
        map.put("limit","20");
        PageUtils queryPage = tencentUserService.queryPage(map);
        logger.info("总记录数："+queryPage.getTotalCount());
        logger.info("记录信息："+queryPage.getList());
    }

    @Test
    public void testWechatProperties(){
        String defaultNumber = CommonWeixinProperties.defaultNumber;
        logger.info("微信配置文件读取：" + defaultNumber);
    }
}