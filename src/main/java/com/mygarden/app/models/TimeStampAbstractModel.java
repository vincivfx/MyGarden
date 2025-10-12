package com.mygarden.app.models;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public abstract class TimeStampAbstractModel {

    @DatabaseField(columnName = "created_at", dataType = DataType.DATE_STRING, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL", readOnly = true, canBeNull = false)    private Date createdAt;

    @DatabaseField(columnName = "updated_at", dataType = DataType.DATE_STRING, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL", readOnly = true, canBeNull = false)    private Date updatedAt;

    protected void updateUpdatedDate() {
        this.updatedAt = new Date();
    }

    public Date getCreatedDate() {
        return this.createdAt;
    }

    public Date getUpdatedDate() {
        return this.updatedAt;
    }
    
}
