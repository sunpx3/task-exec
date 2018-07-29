package com.sunpx.quartz_one.bean;

public class ArticleUpdateStatusEntity {

	private String id;

	private String appId;

	private String lastArticleHash;

	private String timestamp;

	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getLastArticleHash() {
		return lastArticleHash;
	}

	public void setLastArticleHash(String lastArticleHash) {
		this.lastArticleHash = lastArticleHash;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
