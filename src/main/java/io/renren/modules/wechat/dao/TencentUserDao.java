package io.renren.modules.wechat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.wechat.entity.TencentUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auhtor hss
 * @Date 2020/12/12  11:58:00
 * 公众号组件层
 */
@Mapper
public interface TencentUserDao extends BaseMapper<TencentUser> {
}
