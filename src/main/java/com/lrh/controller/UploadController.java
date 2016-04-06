package com.lrh.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.lrh.model.User;
import com.lrh.model.UserUploadFile;
import com.lrh.service.UserUploadFileService;
import com.lrh.util.ToolContext;
import com.lrh.util.ToolSize;

@RequestMapping(value = "/upload")
@Controller
public class UploadController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	@Autowired
	UserUploadFileService service;
	
	//富文本编辑器上传
	@RequestMapping(value = "/editor")
	public String uploadPhoto(HttpServletRequest request,
			HttpServletResponse response,MultipartFile [] files){
		User user=(User) request.getSession().getAttribute("user");
		if(user!=null){
			try {
				if(files==null || files.length<1){ 
					System.err.println("文件未上传"); 
				}else{ 
					List<UserUploadFile> pathList=new ArrayList<UserUploadFile>();
					for(MultipartFile file:files){
						String originalFileName=file.getOriginalFilename();
						String fileType=file.getContentType();
						
/*						
						long fileSize=file.getSize();
						Map<String, Object> data=new HashMap<String, Object>();
						if(fileType.indexOf("image")==-1){
							outJsonResult(response, false, "咦~您选择的好像不是图片文件呢,请重新选择!", null);
							return null;
						}
						if(fileSize>2097152){
							outJsonResult(response, false, "哦~您选择的图片是在太大了,最多支持2MB的图片文件,请重新选择!", null);
							return null;
						}
						//通过校验=======================================
*/						
						String path="";
						boolean isImg=true;
						if(fileType.indexOf("image")==-1){//非图片
							isImg=false;
							path=ToolContext.getSysProp("useditor_upload_files_path")+user.getUserName()+"/";
						}else{//图片
							path=ToolContext.getSysProp("editor_upload_images_path")+user.getUserName()+"/";
						}
						//创建目录
						File userFile=new File(path);
						if (!userFile.mkdir()) {
							userFile.mkdirs();
						}
						//用时间加以命名
						String fileName=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".";
						//System.out.println("文件名前缀为:"+fileNameStartWith);
						String [] sp=originalFileName.split("\\.");
						if(sp.length>1){
							fileName+=sp[sp.length-1];
						}else{
							System.err.println("文件没有后缀");
							continue;
						}
						FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path, fileName));
						
						UserUploadFile uploadFile=new UserUploadFile();
						uploadFile.setFileName(fileName);
						uploadFile.setFileSize(ToolSize.conversion(file.getSize()));
						uploadFile.setUserId(user.getId());
						uploadFile.setFileUrl(path+fileName);
						uploadFile.setFileType(ToolContext.getSysPropInt("upload_file_type_image"));
						if(isImg)
							uploadFile.setFileDesc("用户通过富文本编辑器上传的图片");
						else
							uploadFile.setFileDesc("用户通过富文本编辑器上传的文件");
						
						service.saveFile(uploadFile);
						
						pathList.add(uploadFile);
					}
					outJsonResult(response, true, "上传成功!", pathList);
				} 
			} catch (IOException e){
				logger.error("上传图片发生异常:"+e.getMessage());
				outJsonResult(response, false, "出错啦!"+e.getMessage(), null);
			} 
		}else{
			outJsonResult(response, false, "您需要重新登录后才能吐槽哦!", null);
		}
		
		return null;
	}
}
