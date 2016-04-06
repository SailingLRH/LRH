package com.lrh.mapper;

import java.util.List;

import com.lrh.model.DataType;

public interface DataTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DataType record);

    int insertSelective(DataType record);

    DataType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DataType record);

    int updateByPrimaryKey(DataType record);
    
    /**
     * 通过类型代码获取一条据
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param typeCode
     * @return
     */
    DataType selectByTypeCode(String typeCode);
    
    /**
     * 通过主类型获取子类型
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param mainType
     * @return
     */
    List<DataType> selectByMainType(String mainType);
    
    /**
     * 通过类型代码删除一条数据
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param typeCode
     * @return
     */
    int deleteByTypeCode(String typeCode);
}