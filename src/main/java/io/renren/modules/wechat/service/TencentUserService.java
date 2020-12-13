package io.renren.modules.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.wechat.entity.TencentUser;

import java.util.Map;

/**
 * @Author hss
 * @Date 2020/12/12  12:43:00
 * 公众号逻辑层
 */
public interface TencentUserService extends IService<TencentUser> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String,Object> saveTencentUser(TencentUser tencentUser);
}
