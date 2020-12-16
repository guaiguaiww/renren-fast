package io.renren.modules.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.wechat.entity.TencentUser;

import java.util.List;
import java.util.Map;

/**
 * @Author hss
 * @Date 2020/12/12  12:43:00
 * 公众号逻辑层
 */
public interface TencentUserService extends IService<TencentUser> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String,Object> saveTencentUser(TencentUser tencentUser);

    List<TencentUser> queryAll();

    Map<String,Object> updateTencentUser(TencentUser tencentUser);

    void deleteBatch(Long[] ids);
    /** 获取jssdk配置信息 */
    Map<String,Object> getJsSdkConfig(String url);
}
