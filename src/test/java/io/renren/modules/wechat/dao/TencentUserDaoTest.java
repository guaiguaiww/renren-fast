package io.renren.modules.wechat.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.wechat.entity.TencentUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TencentUserDaoTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TencentUserDao tencentUserDao;

    @Test
    public void insert(){
        TencentUser user = new TencentUser();
        user.setName("hss测试公众号");
        user.setNumber("gh_0d5f4e19ecf7");
        user.setNickName("东");
        user.setAppId("wx05a8ddbcc54e0d4f");
        user.setAppSecret("2fafe98318eeef07c1286b4b30e2235b");
        //根据已知的appid和secret获取accessToken信息
        user.setAccessToken("accessToken");
        user.setTokenGettime(new Date());

        int insert = tencentUserDao.insert(user);
        logger.info("添加操作成功："+user);
    }

    @Test
    public void selectById(){
        TencentUser user = tencentUserDao.selectById(1L);
        logger.info("user:"+user);
    }

    @Test
    public void deleteById(){
        int id = tencentUserDao.deleteById(1L);
        logger.info("删除编号："+id);
    }

    @Test
    public void countByOpenid(){
        QueryWrapper<TencentUser> wrapper = new QueryWrapper<>();
        wrapper.eq("app_id","wx05a8ddbcc54e0d4f");
        Integer count = tencentUserDao.selectCount(wrapper);
        logger.info("count:"+count);
    }
}