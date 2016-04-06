package com.lrh.service;

import com.lrh.model.UserUploadFile;

public interface UserUploadFileService {
	
	/**
	 * 保存一个文件信息
	 * @author Sailing_LRH
	 * @since 2015年11月4日
	 * @param file UserUploadFile 对象
	 * @return id
	 */
	long saveFile(UserUploadFile file);
	
	/**
	 * 通过文件路径删除数据
	 * @author Sailing_LRH
	 * @since 2015年11月6日
	 * @param fileUrl
	 * @return boolean 成功与否
	 */
	Boolean deleteByFileUrl(String fileUrl);
	
	/**
	 * 通过id删除一条数据
	 * @author Sailing_LRH
	 * @since 2015年11月8日
	 * @param id
	 * @return boolean 成功与否
	 */
	Boolean deleteById(Long id);
}
