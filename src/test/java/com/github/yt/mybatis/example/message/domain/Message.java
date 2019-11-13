package com.github.yt.mybatis.example.message.domain;

import com.github.yt.mybatis.domain.BaseEntity;

@javax.persistence.Table(name = "Message")
public class Message extends BaseEntity<Message> {
	
	/** 
	 * id  
	 */
	@javax.persistence.Id
	private String messageId;
	/** 
	 * 用户id  
	 */
	private String userId;
	/** 
	 * 消息类型  
	 */
	private String type;
	/** 
	 * 消息内容  
	 */
	private String content;
	/** 
	 * 业务id  
	 */
	private String businessId;
	/** 
	 * 是否已读  
	 */
	private Boolean read;
	
	public String getMessageId() {
	    return this.messageId;
	}
	
	public Message setMessageId(String messageId) {
		this.messageId = messageId;
		return this;
	}
	
	public String getUserId() {
	    return this.userId;
	}
	
	public Message setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	
	public String getType() {
	    return this.type;
	}
	
	public Message setType(String type) {
		this.type = type;
		return this;
	}
	
	public String getContent() {
	    return this.content;
	}
	
	public Message setContent(String content) {
		this.content = content;
		return this;
	}
	
	public String getBusinessId() {
	    return this.businessId;
	}
	
	public Message setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
	}

	public Boolean getRead() {
		return read;
	}

	public Message setRead(Boolean read) {
		this.read = read;
		return this;
	}
}
