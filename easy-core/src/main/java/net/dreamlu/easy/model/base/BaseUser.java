package net.dreamlu.easy.model.base;

import com.jfinal.plugin.activerecord.IBean;

import net.dreamlu.easy.commons.base.EasyModel;

/**
 * Generated by Easy4JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends EasyModel<M> implements IBean {

	public void setUserId(java.lang.Integer userId) {
		set("userId", userId);
	}

	public java.lang.Integer getUserId() {
		return get("userId");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setEmail(java.lang.String email) {
		set("email", email);
	}

	public java.lang.String getEmail() {
		return get("email");
	}

	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}

	public java.lang.String getPhone() {
		return get("phone");
	}

	public void setPwd(java.lang.String pwd) {
		set("pwd", pwd);
	}

	public java.lang.String getPwd() {
		return get("pwd");
	}

	public java.lang.String getSalt() {
		return get("salt");
	}

	public void setSalt(java.lang.String salt) {
		set("salt", salt);
	}

	public void setRealName(java.lang.String realName) {
		set("realName", realName);
	}

	public java.lang.String getRealName() {
		return get("realName");
	}

	public void setType(java.lang.Integer type) {
		set("type", type);
	}

	public java.lang.Integer getType() {
		return get("type");
	}

	public void setAddress(java.lang.String address) {
		set("address", address);
	}

	public java.lang.String getAddress() {
		return get("address");
	}

	public void setGravatar(java.lang.String gravatar) {
		set("gravatar", gravatar);
	}

	public java.lang.String getGravatar() {
		return get("gravatar");
	}

	public void setScore(java.lang.Integer score) {
		set("score", score);
	}

	public java.lang.Integer getScore() {
		return get("score");
	}

	public void setSignature(java.lang.String signature) {
		set("signature", signature);
	}

	public java.lang.String getSignature() {
		return get("signature");
	}

	public void setUrl(java.lang.String url) {
		set("url", url);
	}

	public java.lang.String getUrl() {
		return get("url");
	}

	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}

	public java.lang.String getRemark() {
		return get("remark");
	}

	public void setTheme(java.lang.String theme) {
		set("theme", theme);
	}

	public java.lang.String getTheme() {
		return get("theme");
	}

	public void setEnable(java.lang.Boolean enable) {
		set("enable", enable);
	}

	public java.lang.Boolean getEnable() {
		return get("enable");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}

	public void setLoginTime(java.util.Date loginTime) {
		set("loginTime", loginTime);
	}

	public java.util.Date getLoginTime() {
		return get("loginTime");
	}

}
