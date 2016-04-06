package com.lrh.service;

import java.util.List;

import com.lrh.model.DataType;

public interface TypeService {

	public boolean deleteByTypeCode(String typeCode);

    public boolean insert(DataType record);

    public boolean updateType(DataType record);

    /**
     * 通过类型代码获取一条据
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param typeCode
     * @return
     */
    public DataType selectByTypeCode(String typeCode);
    
    /**
     * 通过主类型获取子类型
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param mainType
     * @return
     */
    public List<DataType> selectByMainType(String mainType);
}
