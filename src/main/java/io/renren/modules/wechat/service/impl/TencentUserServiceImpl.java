package io.renren.modules.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.wechat.dao.TencentUserDao;
import io.renren.modules.wechat.entity.TencentUser;
import io.renren.modules.wechat.service.TencentUserService;
import io.renren.modules.wechat.utils.AccessTokenUtil;
import io.renren.modules.wechat.utils.CommonWeixinProperties;
import io.renren.modules.wechat.vo.WechatAccount;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TencentUserServiceImpl  extends ServiceImpl<TencentUserDao, TencentUser> implements TencentUserService {

    @Autowired
    private RedisUtils redisUtils;

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
        //获取公众号令牌信息
        if(giveWechatToken(tencentUser)){
            //2.调用父类保存方法
            flag = super.save(tencentUser);
            //3.如果操作的公众号信息等于默认公众号appid，将有用信息放入redis中
            if(tencentUser.getAppId().equals(CommonWeixinProperties.component_appid)){
                redisUtils.set("res_wechat_account",new WechatAccount(tencentUser),4*60);
            }
            map.put("flag",flag);
        }else{
            map.put("flag",flag);
            map.put("message","获取accsee_token失败");
        }
        return map;
    }

    @Override
    public List<TencentUser> queryAll() {
        return baseMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public Map<String,Object> updateTencentUser(TencentUser tencentUser) {
        Boolean flag = false;
        String message = null;
        Map<String,Object> map = new HashMap<>();

        //修改appid不能重复
        TencentUser oldTencentUser = baseMapper.selectById(tencentUser.getId());
        if(oldTencentUser != null){
            QueryWrapper queryWrapper = new QueryWrapper();
            //统计新的appid出现的次数
            queryWrapper.eq("app_id",tencentUser.getAppId());
            //如果新的appId和久的appid值相同，通过
            queryWrapper.notIn("app_id",oldTencentUser.getAppId());
            Integer integer = baseMapper.selectCount(queryWrapper);
            if(integer > 0){
                map.put("flag",flag);
                map.put("message","app_id已存在");
                return map;
            }
        }else{
            map.put("flag",flag);
            map.put("message","需要修改的信息已失效");
            return map;
        }

        //成功获取到accseeToken
        if(giveWechatToken(tencentUser)){
            //2.调用父类保存方法
            flag = super.updateById(tencentUser);
            //3.如果操作的公众号信息等于默认公众号appid，将更新后信息放入redis中
            if(tencentUser.getAppId().equals(CommonWeixinProperties.component_appid)){
                redisUtils.set("res_wechat_account",new WechatAccount(tencentUser),4*60);
            }
            map.put("flag",flag);
        }else{
            map.put("flag",flag);
            map.put("message","获取accsee_token失败");
        }
        return map;
    }

    @Override
    public boolean removeById(Serializable id) {
        TencentUser tencentUser = baseMapper.selectById(id);
        if(tencentUser != null){
            //从库中移除
            boolean b = super.removeById(id);
            if(b){
                //如果操作的公众号信息等于默认公众号appid，移除缓存信息
                if(tencentUser.getAppId().equals(CommonWeixinProperties.component_appid)){
                    redisUtils.delete("res_wechat_account");
                    return b;
                }
            }
        }
        return false;
    }

    private Boolean giveWechatToken(TencentUser tencentUser){
        Boolean flag = false;
        Map<String, Object> accseeToken = AccessTokenUtil.getAccseeToken(tencentUser.getAppId(), tencentUser.getAppSecret());
        //成功获取到accseeToken
        if(accseeToken.containsKey("status") && "success".equals(accseeToken.get("status"))) {
            String accessToken = accseeToken.get("accessToken").toString();

            tencentUser.setAccessToken(accessToken);
            tencentUser.setTokenGettime((Date) accseeToken.get("accessTokenTime"));

            if (accseeToken.containsKey("jsApiTicket")) {
                String jsApiTicket = accseeToken.get("jsApiTicket").toString();
                //赋入JsApiTicket
                tencentUser.setJsApiTicket(jsApiTicket);
                tencentUser.setJsApiTicketTime((Date) accseeToken.get("jsApiTicketTime"));
                flag = true;
            }
        }
        return flag;
    }
}
