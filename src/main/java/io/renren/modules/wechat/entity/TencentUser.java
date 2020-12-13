package io.renren.modules.wechat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther hss
 * @Date 2020/12/12  11:15:00
 * 公众号号信息实体类
 */
@Data
@TableName("tencent_user")
public class TencentUser implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 编号 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 公众号号名称 */
    @NotBlank(message="公众号名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    /** appid */
    @NotBlank(message="该公众号appid已存在", groups = {AddGroup.class, UpdateGroup.class})
    private String appId;
    /** appSecret */
    @NotBlank(message="appSecret不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String appSecret;
    /** 微信号 */
    @NotBlank(message="微信号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String number;
    /** 微信昵称 */
    @NotBlank(message="微信昵称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String nickName;
    /** accessToken */
    private String accessToken;
    /** accessToken获取时间 */
    private Date tokenGettime;
    /** 创建时间 */
    private Date createDate;
    /** 修改时间 */
    private Date updateDate;
    /** 创建者ID */
    private Long createUserId;
    /** 逻辑删除字段 */
    @TableLogic
    private Integer deleted;

}
