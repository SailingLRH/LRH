package com.lrh.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lrh.mapper.DynamicMapper;
import com.lrh.model.Blog;
import com.lrh.model.Dynamic;
import com.lrh.model.Spitslot;
import com.lrh.service.BlogService;
import com.lrh.service.DynamicService;
import com.lrh.service.SpitslotService;
import com.lrh.util.ToolContext;
import com.lrh.util.ToolDate;
@Service("dynamicService")
public class DynamicServiceImpl implements DynamicService{

	@Autowired
	DynamicMapper dynamicDao;
	
	@Autowired
	SpitslotService spitslotService;
	@Autowired
	BlogService blogService;

	@Override
	public boolean deleteById(Long id) {
		return dynamicDao.deleteByPrimaryKey(id)>0;
	}

	@Override
	public long addDynamic(Dynamic record) {
		record.setTime(ToolDate.getNowTime());
		dynamicDao.insertSelective(record);
		return record.getId();
	}

	@Override
	public Dynamic getDynamicById(Long id) {
		return dynamicDao.selectByPrimaryKey(id);
	}

	@Override
	public boolean updateDynamicById(Dynamic record) {
		return dynamicDao.updateByPrimaryKeySelective(record)>0;
	}

	@Override
	public long getDynamicCountByParam(Dynamic param) {
		return dynamicDao.getDynamicCountByParam(param);
	}

	@Override
	public List<Dynamic> findNoticDynamicByParam(Dynamic param) {
		List<Dynamic> list=dynamicDao.findNoticDynamicByParam(param);
		List<Dynamic> newList=new ArrayList<Dynamic>();
		if(list!=null && list.size()>0){
			for(Dynamic d:list){
				//从各个模块拿数据
				/**
				 * ##模块ID
					module_main=1
					module_music=2
					module_video=3
					module_photography=4
					module_product=5
					module_sport=6
					module_spitslot=7
					module_game=8
					module_blog=9
					module_view=10
					module_note=11
				 */
				short moduleId=d.getModuleId();
				long dataId=d.getDataId();
				if(ToolContext.getSysProp("module_music").equals(moduleId+"")){
					
				}else if(ToolContext.getSysProp("module_video").equals(moduleId+"")){
					
				}else if(ToolContext.getSysProp("module_photography").equals(moduleId+"")){
					
				}else if(ToolContext.getSysProp("module_product").equals(moduleId+"")){
					
				}else if(ToolContext.getSysProp("module_sport").equals(moduleId+"")){
					
				}else if(ToolContext.getSysProp("module_spitslot").equals(moduleId+"")){
					System.err.println("----------------->正在获取一条吐槽动态...");
					Spitslot spitslot = spitslotService.getSpitslotById(dataId);
					d.setData(spitslot);
				}else if(ToolContext.getSysProp("module_blog").equals(moduleId+"")){
					System.err.println("----------------->正在获取一条博文动态...");
					Blog blog = blogService.getBlogById(dataId);
					d.setData(blog);
				}else if(ToolContext.getSysProp("module_view").equals(moduleId+"")){
					
				}
				
				newList.add(d);
			}
		}
		return newList;
	}

	@Override
	public boolean deleteByModuleAndDataId(short moduleId, long dataId) {
		Dynamic param=new Dynamic();
		param.setModuleId(moduleId);
		param.setDataId(dataId);
		return dynamicDao.deleteByModelAndDataId(param)>0;
	}

	@Override
	public boolean deleteByModuleAndDataIds(short moduleId, String[] ids) {
		Map<String,Object> param =new HashMap<String,Object>();
		param.put("ids", ids);
		param.put("moduleId", moduleId);
		return dynamicDao.deleteByModuleAndDataIds(param)>0;
	}

}
