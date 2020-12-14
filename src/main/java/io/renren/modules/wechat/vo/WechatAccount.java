package io.renren.modules.wechat.vo;

import io.renren.modules.wechat.entity.TencentUser;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class WechatAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    /** appid */
    private String appId;
    /** appSecret */
    private String appSecret;
    /** 微信号 */
    private String number;
    /** 微信昵称 */
    private String nickName;
    /** accessToken */
    private String accessToken;
    /** accessToken获取时间 */
    private Date tokenGettime;
    /** 令牌 */
    private String token;

    public WechatAccount(TencentUser tencentUser){
        if(StringUtils.isNotBlank(tencentUser.getName())){
            this.name = tencentUser.getName();
        }
        if(StringUtils.isNotBlank(tencentUser.getAppId())){
            this.appId = tencentUser.getAppId();
        }
        if(StringUtils.isNotBlank(tencentUser.getAppSecret())){
            this.appSecret = tencentUser.getAppSecret();
        }
        if(StringUtils.isNotBlank(tencentUser.getNumber())){
            this.number = tencentUser.getNumber();
        }
        if(StringUtils.isNotBlank(tencentUser.getNickName())){
            this.nickName = tencentUser.getNickName();
        }
        if(StringUtils.isNotBlank(tencentUser.getAccessToken())){
            this.accessToken = tencentUser.getAccessToken();
        }
        if(null != tokenGettime){
            this.tokenGettime = tencentUser.getTokenGettime();
        }
        if(StringUtils.isNotBlank(tencentUser.getToken())){
            this.token = tencentUser.getToken();
        }
    }
}
