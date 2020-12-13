package io.renren.modules.wechat.controller;

import io.renren.common.annotation.SysLog;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.wechat.entity.TencentUser;
import io.renren.modules.wechat.service.TencentUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 微信公众号
 * @Author hss
 * @Date 2020/12/13  11:54:00
 */
@RestController
@RequestMapping("/wechat/tencentUser")
public class TencentUserController extends AbstractController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TencentUserService tencentUserService;

    /**
     * 公众号信息列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params){
        try{
            logger.info("查询公众号列表信息");
            PageUtils page = tencentUserService.queryPage(params);
            return R.ok().put("page", page);
        }catch (Exception e){
            logger.info("查询公众号列表信息-出错",e);
            return R.error(500,"服务器处理异常");
        }
    }

    @SysLog("保存公众号信息")
    @PostMapping("/save")
    //@RequiresPermissions("sys:user:save")
    public R save(@RequestBody TencentUser tencentUser){
        //字段效验
        ValidatorUtils.validateEntity(tencentUser, AddGroup.class);
        try {
            logger.info("添加公众号信息");
            tencentUser.setCreateUserId(getUserId());
            //信息保存
            Map<String, Object> objectMap = tencentUserService.saveTencentUser(tencentUser);
            if(!(Boolean) objectMap.get("flag")){
                return R.error(500,objectMap.get("message")+"<br/>");
            }else {
                return R.ok();
            }
        }catch (Exception e){
            logger.info("添加公众号信息-出错",e);
            return R.error(500,"服务器处理异常");
        }
    }
}
