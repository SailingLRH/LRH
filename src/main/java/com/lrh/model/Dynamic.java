package com.lrh.model;

import java.io.Serializable;

import com.lrh.util.ToolDate;

public class Dynamic implements Serializable {
    private Long id;

    private Short moduleId;

    private Long dataId;

    private String time;

    private static final long serialVersionUID = 1L;

    
    /**
     * 经过处理的时间
     */
    private String formateTime;
    
    /**
     * 起始时间
     */
    private String startTime;
    
    /**
     * 结束时间
     */
    private String endTime;
    
    /**
     * 分页属性
     */
    private Long start;
    
    private Long max;
    
    /**
     * 对应各个模块的数据
     */
    private Object data;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getModuleId() {
        return moduleId;
    }

    public void setModuleId(Short moduleId) {
        this.moduleId = moduleId;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getFormateTime() {
    	formateTime=ToolDate.formatTime(time);
		return formateTime;
	}

	public void setFormateTime(String formateTime) {
		this.formateTime = formateTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", moduleId=").append(moduleId);
        sb.append(", dataId=").append(dataId);
        sb.append(", time=").append(time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}