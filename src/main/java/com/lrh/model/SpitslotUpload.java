package com.lrh.model;

import java.io.Serializable;

public class SpitslotUpload implements Serializable {
    private Long id;

    private Long spitslotId;

    private Long fileId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpitslotId() {
        return spitslotId;
    }

    public void setSpitslotId(Long spitslotId) {
        this.spitslotId = spitslotId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", spitslotId=").append(spitslotId);
        sb.append(", fileId=").append(fileId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}