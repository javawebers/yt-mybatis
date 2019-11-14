package com.github.yt.mybatis.example.message.domain;

import com.github.yt.mybatis.domain.BaseEntity;

import javax.persistence.Column;

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

	@Column(name = "field_column")
    private Integer fieldColumn;

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", businessId='" + businessId + '\'' +
                ", read=" + read +
                ", fieldColumn=" + fieldColumn +
                '}';
    }

    public Integer getFieldColumn() {
		return fieldColumn;
	}

	public Message setFieldColumn(Integer fieldColumn) {
		this.fieldColumn = fieldColumn;
		return this;
	}

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
