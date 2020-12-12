package io.renren.modules.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.wechat.dao.TencentUserDao;
import io.renren.modules.wechat.entity.TencentUser;
import io.renren.modules.wechat.service.TencentUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class TencentUserServiceImpl  extends ServiceImpl<TencentUserDao, TencentUser> implements TencentUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");
        Long appId = (Long)params.get("appId");

        IPage<TencentUser> page = this.page(
                new Query<TencentUser>().getPage(params),
                new QueryWrapper<TencentUser>()
                        .like(StringUtils.isNotBlank(name),"name", name)
                        .eq(appId != null,"app_id", appId)
                        .orderByDesc("id")
        );

        return new PageUtils(page);
    }

    @Override
    public boolean save(TencentUser tencentUser) {
        Boolean flag = false;
        String accessToken = null;
        //1.根据appId 和secret获取access_token信息
        if(StringUtils.isNotBlank(accessToken)){
            //2.调用父类保存方法
            flag = super.save(tencentUser);
        }
        return flag;
    }
}
