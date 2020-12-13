package io.renren.modules.wechat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 公众号模块配置文件
 */
public class CommonWeixinProperties {

    private static final Logger logger = LoggerFactory.getLogger(CommonWeixinProperties.class);

    public final static String defaultNumber;

    public final static String component_appid;

    public final static String domain;

    static {
        logger.info("========================Init==============CommonWeixin==============Properties====================");
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = CommonWeixinProperties.class.getClassLoader().getResourceAsStream("wechat.properties");
        // 使用properties对象加载输入流
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取key对应的value值
        defaultNumber = properties.getProperty("defaultNumber");
        component_appid = properties.getProperty("COMPONENT_APPID");
        domain = properties.getProperty("oAuthDomain");
        logger.info("========================Init==============CommonWeixin==============Properties====================");
    }
}
