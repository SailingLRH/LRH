package com.lrh.controller;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lrh.model.NoticeMessage;
import com.lrh.model.Spitslot;
import com.lrh.model.Comment;
import com.lrh.model.Praise;
import com.lrh.model.User;
import com.lrh.model.UserUploadFile;
import com.lrh.service.DynamicService;
import com.lrh.service.NoticeMessageService;
import com.lrh.service.SpitslotService;
import com.lrh.service.UserUploadFileService;
import com.lrh.util.ToolContext;
import com.lrh.util.ToolSize;
import com.lrh.view.CommentView;

@Controller
@RequestMapping("/spitslot")
public class SpitslotController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(SpitslotController.class);
	@Autowired
	UserUploadFileService service;
	@Autowired
	SpitslotService spitslotService;
	@Autowired
	NoticeMessageService noticeService;
	@Autowired
	DynamicService dynamicService;

	//打开吐槽天地页面
	@RequestMapping(value = "/index")
	public String saveGameInfo(HttpServletRequest request,
			HttpServletResponse response,Spitslot param) throws IOException{
		User user = (User) request.getSession().getAttribute("user");
		if(user==null){
			response.sendRedirect("/LRH");
			return null;
		}
		param=param==null?new Spitslot():param;
		param.setUserId(user.getId());
		List<Spitslot> list=spitslotService.findSpitslot(param);
		request.setAttribute("spitslotList", list);
		
		request.getSession().setAttribute("spitslotParam", new Spitslot());
		return "spitslot";
	}
	
	//上传吐槽图片
	@RequestMapping(value = "/upload_img")
	public String uploadPhoto(HttpServletRequest request,
			HttpServletResponse response,MultipartFile file){
		User user=(User) request.getSession().getAttribute("user");
		if(user!=null){
			try {
				if(file.isEmpty()){ 
					System.err.println("文件未上传"); 
				}else{ 
					String originalFileName=file.getOriginalFilename();
					long fileSize=file.getSize();
					String fileType=file.getContentType();
					
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
					List<Long> pathList=(List<Long>) request.getSession().getAttribute("spitslotImgId");
					pathList=pathList==null?new ArrayList<Long>():pathList;
					
					String path=ToolContext.getSysProp("user_spitslot_upload_path")+user.getUserName()+"/";
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
						fileName+="jpg";
					}
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path, fileName));
					
					UserUploadFile imgFile=new UserUploadFile();
					imgFile.setFileName(fileName);
					imgFile.setFileSize(ToolSize.conversion(file.getSize()));
					imgFile.setUserId(user.getId());
					imgFile.setFileUrl(path+fileName);
					imgFile.setFileType(ToolContext.getSysPropInt("upload_file_type_image"));
					imgFile.setFileDesc("用户在发表吐槽时上传的图片");
					Long imgId=service.saveFile(imgFile);
					//将路径存到list,当发表吐槽时
					pathList.add(imgId);
					
					data.put("fileUrl", path+fileName);
					data.put("fileId", imgId);
					outJsonResult(response, true, "文件已上传成功!", data);
					
					//将list保存到session作用域,以便在发表吐槽时用上
					request.getSession().setAttribute("spitslotImgId",pathList);
					
				} 
			} catch (IOException e){
				logger.error("上传图片发生异常:"+e.getMessage());
			} 
		}else{
			outJsonResult(response, false, "您需要重新登录后才能吐槽哦!", null);
		}
		
		return null;
	}
	
	
	//删除图片
	@RequestMapping(value = "/deleteImg")
	public String deleteImg(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "imgUrl", required = true) String imgUrl,
			@RequestParam(value = "imgId", required = true) Long imgId){
		boolean f =service.deleteById(imgId);
		if(f){
			File file=new File(imgUrl);
			if(file.exists() && file.isFile()){
				file.delete();
				List<Long> pathList=(List<Long>) request.getSession().getAttribute("spitslotImgId");
				pathList.remove(imgId);
				this.outJsonResult(response, true, "图片删除成功!", null);
			}else{
				this.outJsonResult(response, false, "删除失败,图片不存在!", null);
			}
		}else{
			this.outJsonResult(response, false, "图片删除失败!", null);
		}
		return null;
	}
	
	
	//发表吐槽
	@RequestMapping(value = "/publish")
	public String publish(HttpServletRequest request,
			HttpServletResponse response,Spitslot sp){
		User user=(User) request.getSession().getAttribute("user");
		if(user!=null){
			List<Long> pathList=(List<Long>) request.getSession().getAttribute("spitslotImgId");
			sp.setImgIdList(pathList);
			sp.setUserId(user.getId());
			//保存数据
			sp=spitslotService.saveSpitslot(sp);
			sp.setUser(user);
			request.getSession().removeAttribute("spitslotImgId");
			outJsonResult(response, true, "您的吐槽已发布成功!", sp);
		}else{
			outJsonResult(response, false, "您需要重新登录后才能吐槽哦!", null);
		}
		return null;
	}

	//异步加载数据
	@RequestMapping(value = "/getMore")
	public String getMore(HttpServletRequest request,
			HttpServletResponse response,String notIncludeIds){
		User user = (User) request.getSession().getAttribute("user");
		if(user==null){
			this.outJsonResult(response, false, "您已掉线,请重新登录!", null);
			return null;
		}
		
		Spitslot param=new Spitslot();
		param.setUserId(user.getId());
		System.err.println("要过滤的数据id: "+notIncludeIds);
		if(notIncludeIds!=null && !notIncludeIds.equals("")){
			String [] notIncludeIdArray=notIncludeIds.split(",");
			Set<String> notIncludeIdSet=new HashSet<String>();
			for(int i=0;i<notIncludeIdArray.length;i++){
				notIncludeIdSet.add(notIncludeIdArray[i]);
			}
			param.setNotIncludeIdSet(notIncludeIdSet);
		}
		List<Spitslot> list=spitslotService.findSpitslot(param);
		if(list==null || list.size()<1){
			this.outJsonResult(response, false, "没有更多了", null);
		}else{
			Map<String,Object> data=new HashMap<String,Object>();
//			User user=(User) request.getSession().getAttribute("user");
//			if(user!=null){
//				data.put("me", user.getId());
//			}else{
//				data.put("me", "");
//			}
			data.put("me", user.getId());
			data.put("list", list);
			this.outJsonResult(response, true, "加载数据成功!", data);
		}
		return null;
	}
	
	
	//删除吐槽
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "id", required = true) long id){
		User user=(User) request.getSession().getAttribute("user");
		if(user==null || id<1){
			this.outJsonResult(response, false, "删除失败,您可能已经掉线了,请刷新页面试试!", null);
		}else{
			Spitslot param=new Spitslot();
			param.setUserId(user.getId());
			param.setId(id);
			boolean b=spitslotService.deleteByIdAndUserId(param);
			if(b){
				this.outJsonResult(response, true, "删除成功!", null);
			}else{
				this.outJsonResult(response, false, "删除失败,数据可能不存在,请刷新看看!", null);
			}
		}
		return null;
	}
	
	//写评论
	@RequestMapping(value = "/publishComment")
	public String publishComment(HttpServletRequest request,
			HttpServletResponse response,Comment sc){
		if(sc.getDataId()==null || sc.getContent()==null){
			outJsonResult(response, false, "数据不完整,评论失败!", null);
			return null;
		}
		User user=(User) request.getSession().getAttribute("user");
		if(user!=null){
			sc.setUserId(user.getId());
			sc=spitslotService.publishComment(sc);
			if(sc==null){
				outJsonResult(response, false, "数据有误,评论失败,请刷新后重试!", null);
			}else{
				//==========================================预留空间,通知被评论的人
				if(user.getId()!=sc.getToUserId()){//自己评论自己,则不通知
					NoticeMessage notice=new NoticeMessage();
					notice.setUserId(sc.getToUserId());
					notice.setIsRead(false);
					notice.setModel((short)ToolContext.getSysPropInt("module_spitslot"));
					notice.setDataId(sc.getDataId());
					notice.setInitiatorId(user.getId());
					notice.setTitle("您发表的吐槽有了新的评论!");
					String content=sc.getContent().length()>450?(sc.getContent().substring(0, 450)+"..."):sc.getContent();
					notice.setContent("["+user.getUserName()+"] 评论:<br/>\""+content+"\"");
					notice.setMessageType((short)ToolContext.getSysPropInt("message_interactive"));
					notice.setUrl("/data/tail?model="+ToolContext.getSysProp("module_spitslot")+"&dataId="+sc.getDataId());
					noticeService.insertSelective(notice);
				}
				
				sc.setReplyUser(user);
				outJsonResult(response, true, "发表成功!", sc);
			}
		}else{
			outJsonResult(response, false, "您未登录或已掉线,需要登录后才能发表评论哦!", null);
		}
		return null;
	}
	
	//点赞
	@RequestMapping(value = "/praise")
	public String praise(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "dataId", required = true) long dataId){
		
		Spitslot data = spitslotService.getSpitslotById(dataId);
		if(dataId<0 || data==null){
			outJsonResult(response, false, "点赞失败,请刷新后重试!", null);
			return null;
		}
		User user=(User) request.getSession().getAttribute("user");
		Praise praise=new Praise();
		praise.setDataId(dataId);
		if(user!=null){
			praise.setUserId(user.getId());
		}
		String ip =this.getIpAddr(request);
		praise.setUserIp(ip);
		Boolean f=spitslotService.praise(praise);
		if(f==null){
			outJsonResult(response, false, "亲,您在24小时之内已经点过一次赞了,请过一段时间再点吧!", null);
		}else if(f){
			//==========================================预留空间,通知被点赞的人
			if(user==null || user.getId()!=data.getUserId()){//自己给自己点赞,则不通知
				NoticeMessage notice=new NoticeMessage();
				notice.setUserId(data.getUserId());
				notice.setIsRead(false);
				notice.setModel((short)ToolContext.getSysPropInt("module_spitslot"));
				notice.setDataId(dataId);
				notice.setTitle("您发表的吐槽有了新的点赞!");
				if(user!=null){
					notice.setContent("["+user.getUserName()+"] 为您的吐槽点了个赞!");
					notice.setInitiatorId(user.getId());
				}else{
					String [] array =ip.split("\\.");
					if(array.length==4){
						String newIp=array[0]+"."+array[1]+".*.*";
						notice.setContent("IP ["+newIp+"] 的网友为您的吐槽点了个赞!");
					}else
						notice.setContent("一位网友为您的吐槽点了个赞!");
				}
				notice.setMessageType((short)ToolContext.getSysPropInt("message_interactive"));
				notice.setUrl("/data/tail?model="+ToolContext.getSysProp("module_spitslot")+"&dataId="+dataId);
				noticeService.insertSelective(notice);
			}
			outJsonResult(response, true, "点赞成功!", null);
		}else{
			outJsonResult(response, false, "系统错误,请刷新后重试!", null);
		}
		return null;
	}
	
	//查看更多评论
	@RequestMapping(value = "/moreComment")
	public String moreComment(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "dataId", required = true) Long dataId,
			@RequestParam(value = "page", required = true) int page){
		
		int start = (page-1)*10-5;
		List<CommentView> list=spitslotService.moreComments(dataId, start);
		if(list==null || list.size()<1){
			this.outJsonResult(response, false, "评论都在这里啦~", null);
		}else{
			this.outJsonResult(response, true, "本次为您加载了"+list.size()+"条评论", list);
		}
		return null;
	}
	
	//查看更多评论的回复
	@RequestMapping(value = "/moreReply")
	public String moreReply(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "dataId", required = true) Long dataId,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "oid", required = true) Long oid){
		int start = (page-1)*10-5;
		List<Comment> list=spitslotService.moreReply(dataId, oid, start);
		if(list==null || list.size()<1){
			this.outJsonResult(response, false, "回复都在这里啦~", null);
		}else{
			this.outJsonResult(response, true, "本次为您加载了"+list.size()+"条回复", list);
		}
		return null;
	}
}

