package com.github.yt.mybatis.entity;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author sheng
 */
@Table
public class BaseEntity<T extends BaseEntity<?>> implements Serializable {

    private static final long serialVersionUID = 6468926052770326495L;

    /**
     * 创建时间
     */
    @YtBaseEntityColumn(YtColumnType.CREATE_TIME)
    private Date createTime;

    /**
     * 修改时间
     */
    @YtBaseEntityColumn(YtColumnType.MODIFY_TIME)
    private Date modifyTime;

    /**
     * 创建人 id
     */
    @YtBaseEntityColumn(YtColumnType.FOUNDER_ID)
    private String founderId;

    /**
     * 创建人名称
     */
    @YtBaseEntityColumn(YtColumnType.FOUNDER_NAME)
    private String founderName;

    /**
     * 修改人 id
     */
    @YtBaseEntityColumn(YtColumnType.MODIFIER_ID)
    private String modifierId;

    /**
     * 修改人名称
     */
    @YtBaseEntityColumn(YtColumnType.MODIFIER_NAME)
    private String modifierName;

    /**
     * 逻辑删除标志
     */
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
