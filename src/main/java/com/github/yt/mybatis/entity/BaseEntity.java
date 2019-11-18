package com.github.yt.mybatis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table
public class BaseEntity<T extends BaseEntity<T>> implements Serializable {

    private static final long serialVersionUID = 6468926052770326495L;

    // 创建时间
    @YtBaseEntityColumn(YtColumnType.CREATE_TIME)
    private Date createTime;
    // 修改时间
    @YtBaseEntityColumn(YtColumnType.MODIFY_TIME)
    private Date modifyTime;
    // 创建人ID
    @YtBaseEntityColumn(YtColumnType.FOUNDER_ID)
    private String founderId;
    // 创建人姓名
    @YtBaseEntityColumn(YtColumnType.FOUNDER_NAME)
    private String founderName;
    // 修改人ID
    @YtBaseEntityColumn(YtColumnType.MODIFIER_ID)
    private String modifierId;
    // 修改人姓名
    @YtBaseEntityColumn(YtColumnType.MODIFIER_NAME)
    private String modifierName;
    // 删除标示
    @Column(nullable = false)
    @YtBaseEntityColumn(YtColumnType.DELETE_FLAG)
    private Boolean deleteFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format= "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format= "yyyy-MM-dd HH:mm:ss")
    public Date getModifyTime() {
        return modifyTime;
    }

    public T setCreateTime(Date createTime) {
        this.createTime = createTime;
        return (T) this;
    }

    public T setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return (T) this;
    }

    public String getFounderId() {
        return founderId;
    }

    public T setFounderId(String founderId) {
        this.founderId = founderId;
        return (T) this;
    }

    public String getFounderName() {
        return founderName;
    }

    public T setFounderName(String founderName) {
        this.founderName = founderName;
        return (T) this;
    }

    public String getModifierId() {
        return modifierId;
    }

    public T setModifierId(String modifierId) {
        this.modifierId = modifierId;
        return (T) this;
    }

    public String getModifierName() {
        return modifierName;
    }

    public T setModifierName(String modifierName) {
        this.modifierName = modifierName;
        return (T) this;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public T setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
        return (T) this;
    }
}