package io.renren.modules.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.wechat.dao.TencentUserDao;
import io.renren.modules.wechat.entity.TencentUser;
import io.renren.modules.wechat.service.TencentUserService;
import io.renren.modules.wechat.utils.AccessTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
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
    public Map<String,Object> saveTencentUser(TencentUser tencentUser) {
        Boolean flag = false;
        String message = null;

        Map<String,Object> map = new HashMap<>();
        //通过appid查询信息
        QueryWrapper<TencentUser> wrapper = new QueryWrapper<>();
        wrapper.eq("app_id",tencentUser.getAppId());
        Integer integer = baseMapper.selectCount(wrapper);
        if(integer > 0){
            message = "appId已存在，不能重复";
            map.put("flag",flag);
            map.put("message",message);
            return map;
        }
        Map<String, Object> accseeToken = AccessTokenUtil.getAccseeToken(tencentUser.getAppId(), tencentUser.getAppSecret());
        //成功获取到accseeToken
        if(accseeToken.containsKey("status") && "success".equals(accseeToken.get("status"))){
            String accessToken = accseeToken.get("accessToken").toString();

            tencentUser.setAccessToken(accessToken);
            tencentUser.setTokenGettime((Date) accseeToken.get("accessTokenTime"));
            //2.调用父类保存方法
            flag = super.save(tencentUser);
            map.put("flag",flag);
            map.put("message",message);
        }else{
            map.put("flag",flag);
            map.put("message","获取accsee_token失败");
        }
        return map;
    }
}
