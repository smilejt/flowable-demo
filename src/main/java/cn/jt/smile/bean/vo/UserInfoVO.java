package cn.jt.smile.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author admin
 */
@Data
@Accessors(chain = true)
public class UserInfoVO implements Serializable {

	/**
	 * 用户主键 定长业务意义
	 */
	private String userId;
	/**
	 * 客户详情主键
	 */
	private Long customerId;
	/**
	 * 租户主键
	 */
	private Long tenantId;
	/**
	 * 部门主键
	 */
	private Long organId;
	/**
	 * 职位主键
	 */
	private Long positionId;
	/**
	 * 登录账号名
	 */
	private String loginName;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 用户别名、昵称
	 */
	private String userNickname;
	/**
	 * 用户类型 0系统用户，1管理用户
	 */
	private Integer userType;
	/**
	 * 0正常，1停用，2锁定
	 */
	private Integer status;
	/**
	 * 角色条件
	 */
	private Long roleId;
	/**
	 * 联系方式，手机号
	 */
	private String contactValue;
	/**
	 * 角色主键列表
	 */
	private String roleIds;
	/**
	 * 角色名列表
	 */
	private String roleNames;
	/**
	 * 数据权限
	 */
	private String organIds;
	/**
	 * 数据权限名
	 */
	private String organNames;
	/**
	 * 归属部门名
	 */
	private String organName;
	/**
	 * 职位名
	 */
	private String positionName;
	/**
	 * 登录范围 0PC，1APP，逗号隔开
	 */
	private String scope;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date gmtCreate;

}
