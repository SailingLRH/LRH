package com.lrh.model;

import java.io.Serializable;

import com.lrh.controller.UserController;

public class User implements Serializable {
    private Integer id;

    private Integer roleId;

    private String userName;

    private String password;

    private String nickName;

    private String email;

    private String photoUrl;

    private String sign;

    private String actionCode;

    private Boolean isAction;

    private String registTime;
    
    private Integer sex;
    
    private String sexStr;

    private String lastLoginTime;
    
    private String showName;				//如果存在昵称,则显示昵称,否则显示用户名
    
    private Boolean isOnline;				//是否在线 true:在线	false:离线
    
    private String account;				//指用户名,邮箱或昵称,用于模糊查询传参
    
    /**
     * 分页属性
     */
    private Long start;
    
    private Long max;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNickName() {
	nickName=null == nickName || "".equals(nickName) ?"[暂无昵称]":nickName;
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl == null ? null : photoUrl.trim();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode == null ? null : actionCode.trim();
    }

    public Boolean getIsAction() {
        return isAction;
    }

    public void setIsAction(Boolean isAction) {
        this.isAction = isAction;
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime == null ? null : registTime.trim();
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime == null ? null : lastLoginTime.trim();
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public Boolean getIsOnline()
	{
    	if(UserController.userOnlineMap.containsKey(userName))
    		isOnline = true;
    	else 
    		isOnline = false;
    	
		return isOnline;
	}

	public void setIsOnline(Boolean isOnline)
	{
		this.isOnline = isOnline;
	}

	public Integer getSex() {
	    return sex;
	}

	public void setSex(Integer sex) {
	    this.sex = sex;
	}

	public String getSexStr() {
	    if(null==sex || (sex!=1 && sex!=2))
		sexStr = "[未知]";
	    else if(sex==1)
		sexStr = "男";
	    else if(sex==2)
		sexStr = "女";
	    return sexStr;
	}

	public void setSexStr(String sexStr) {
	    this.sexStr = sexStr;
	}

	public String getAccount() {
	    return account;
	}

	public void setAccount(String account) {
	    this.account = account;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleId=").append(roleId);
        sb.append(", userName=").append(userName);
        sb.append(", password=").append(password);
        sb.append(", nickName=").append(nickName);
        sb.append(", email=").append(email);
        sb.append(", photoUrl=").append(photoUrl);
        sb.append(", sign=").append(sign);
        sb.append(", actionCode=").append(actionCode);
        sb.append(", isAction=").append(isAction);
        sb.append(", registTime=").append(registTime);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public String getShowName() {
		showName=nickName!=null && !nickName.equals("")?nickName:userName;
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
    
}