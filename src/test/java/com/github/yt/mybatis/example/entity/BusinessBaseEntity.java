package com.github.yt.mybatis.example.entity;

import com.github.yt.mybatis.entity.YtBaseEntityColumn;
import com.github.yt.mybatis.entity.YtColumnType;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table
public class BusinessBaseEntity<T extends BusinessBaseEntity<T>> implements Serializable {

    private static final long serialVersionUID = 6468926052770326495L;

    // 创建时间
    @Column(name = "create_time")
    @YtBaseEntityColumn(YtColumnType.CREATE_TIME)
    private Date createTime;
    // 修改时间
    @Column(name = "modify_time")
    @YtBaseEntityColumn(YtColumnType.MODIFY_TIME)
    private Date modifyTime;
    // 创建人ID
    @Column(name = "founder_id")
    @YtBaseEntityColumn(YtColumnType.FOUNDER_ID)
    private String founderId;
    // 修改人ID
    @Column(name = "modifier_id")
    @YtBaseEntityColumn(YtColumnType.MODIFIER_ID)
    private String modifierId;
    // 删除标示
    @Column(name = "delete_flag", nullable = false)
    @YtBaseEntityColumn(YtColumnType.DELETE_FLAG)
    private Boolean deleteFlag;

    public Date getCreateTime() {
        return createTime;
    }

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

    public String getModifierId() {
        return modifierId;
    }

    public T setModifierId(String modifierId) {
        this.modifierId = modifierId;
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
