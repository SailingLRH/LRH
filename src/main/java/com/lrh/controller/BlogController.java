package com.lrh.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lrh.model.Blog;
import com.lrh.model.Comment;
import com.lrh.model.DataType;
import com.lrh.model.NoticeMessage;
import com.lrh.model.Praise;
import com.lrh.model.User;
import com.lrh.service.BlogService;
import com.lrh.service.NoticeMessageService;
import com.lrh.service.TypeService;
import com.lrh.util.ToolContext;
import com.lrh.util.ToolHtmlRegexp;

@Controller
@RequestMapping("/blog")
public class BlogController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(BlogController.class);
	
	@Autowired
	TypeService typeService;
	@Autowired
	BlogService blogService;
	@Autowired
	NoticeMessageService noticeService;
	
	//打开博文首页
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			response.sendRedirect("/LRH");
			return null;
		}else{
			Blog param=new Blog();
			param.setUserId(user.getId());
			param.setStart(0L);
			param.setMax(10L);
			List<Blog>blogList=blogService.findBlogByParam(param);
			int total = blogService.getCountByParam(param);
			request.setAttribute("blogList", blogList);
			request.setAttribute("max", 10);
			request.setAttribute("total", total);
			long totalPage=total%10==0?total/10:total/10+1;
			request.setAttribute("totalPage", totalPage);
			request.setAttribute("nowPage", 1);
			request.setAttribute("blogTypeName", "全部分类");
			request.setAttribute("blogTypeList", typeService.selectByMainType("100"));
			return "blog";
		}
	}
	
	//分页查询
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "status", required = false) Short status,
			@RequestParam(value = "max", required = false) Long max,
			@RequestParam(value = "pageCode", required = false) Long pageCode) throws IOException{
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			response.sendRedirect("/LRH");
			return null;
		}else{
			pageCode=pageCode==null?1:pageCode;
			status=status==null?1:status;
			max=max==null?10:max;
			max=max>100?100:max;
			max=max<1?1:max;
			
			Blog param=new Blog();
			param.setStatus(status);
			param.setType(type);
			param.setUserId(user.getId());
			int total = blogService.getCountByParam(param);
			Map<String,Long> pageMap=this.getTotalPage(total, max, pageCode);
			
			
			param.setStart(pageMap.get("start"));
			param.setMax(max);
			List<Blog>blogList=blogService.findBlogByParam(param);
			request.setAttribute("blogList", blogList);
			request.setAttribute("max", max);
			request.setAttribute("total", total);
			request.setAttribute("totalPage", pageMap.get("totalPage"));
			request.setAttribute("nowPage", pageCode);
			request.setAttribute("type", type);
			request.setAttribute("status", status);
			List<DataType> dataTypeList = typeService.selectByMainType("100");
			String blogTypeName="全部分类";
			if(type!=null && !type.equals("")){
				if(type.equals("0")){
					blogTypeName="暂未分类";
				}else{
					for(DataType t:dataTypeList){
						if(t.getTypeCode().equals(type)){
							blogTypeName=t.getTypeName();
							break;
						}
					}
				}
			}
			request.setAttribute("blogTypeName", blogTypeName);
			request.setAttribute("blogTypeList", dataTypeList);
			return "blog";
		}
	}
	
	//查看详情
	@RequestMapping(value = "/blogDetail/{id}")
	public String blogDetail(HttpServletRequest request,
			HttpServletResponse response,@PathVariable Long id) throws IOException{
		Blog blog = blogService.getBlogById(id);
		if(blog==null) return this.openErrorPage("啊哦~似乎这条数据不存在,请刷新页面试试!",request);
		if(blog.getPublicType()!=1){//如果不是公开的
			User user=(User) request.getSession().getAttribute("user");
			if(user==null){
				 return this.openErrorPage("对不起,该博文主人设置了权限,您无权查看.您可以先登录试试!",request);
			}else{
				boolean b=false;
				if(user.getId()==blog.getUserId()) b=true;//主人自己
				else if(blog.getPublicType()==4 && blog.getPublicToUsers().contains(user.getId()+"")) b=true;//指定人
				else if(blog.getPublicType()==2){
					b=true;
					//------------留出位置,主人好友也可
				}
				
				if(b){
					request.setAttribute("blog", blog);
					Blog newBlog =new Blog();
					newBlog.setId(blog.getId());
					newBlog.setReadNumber(blog.getReadNumber()+1);
					blogService.updateBlogInfo(newBlog);
					
					return "blog_detail";
				}else{
					return this.openErrorPage("对不起,该博文主人设置了权限,您无权查看.",request);
				}
			}
		}else{
			request.setAttribute("blog", blog);
			Blog newBlog =new Blog();
			newBlog.setId(blog.getId());
			newBlog.setReadNumber(blog.getReadNumber()+1);
			blogService.updateBlogInfo(newBlog);
			return "blog_detail";
		}
		
	}
	
	//保存
	@RequestMapping(value = "/save")
	public String save(HttpServletRequest request,
			HttpServletResponse response,Blog blog) throws IOException{
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			this.outJsonResult(response, false, "您已掉线,请登录后操作!", null);
		}else{
			if(blog.getTitle()==null || blog.getTitle().equals("")){
				blog.setTitle("暂无标题");
			}
			String simpleContent=ToolHtmlRegexp.filterHtml(blog.getContent());
			if(simpleContent.length()>5000 || blog.getContent().length()>100000){
				this.outJsonResult(response, false, "保存失败!内容过长,请删减部分!", null);
				return null;
			}
			if(simpleContent.length()>200){
				simpleContent=simpleContent.substring(0,200)+"...";
			}
			blog.setSimpleContent(simpleContent);
			
			String firstImg=ToolHtmlRegexp.getFirstImg(blog.getContent());
			if(firstImg!=null) blog.setFirstImg(firstImg);
			
			blog.setUserId(user.getId());
			blog.setReadNumber(0);
			
			blogService.saveBlog(blog);
			blog.setContent("");//清空内容,要不然数据量太大页面会失去响应
			blog.setSimpleContent("");//清空内容,要不然数据量太大页面会失去响应
			blog.setFirstImg("");//清空内容,要不然数据量太大页面会失去响应
			this.outJsonResult(response, true, "保存成功!", blog);
		}
		return null;
	}
	
	//删除
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "id", required = true) long id) throws IOException{
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			this.outJsonResult(response, false, "您已掉线,请先登录!", null);
		}else{
			Blog param =new Blog();
			param.setUserId(user.getId());
			param.setId(id);
			
			if(blogService.deleteBlogById(param)){
				this.outJsonResult(response, true, "删除成功!", null);
			}else{
				this.outJsonResult(response, false, "删除失败,可能数据不存在,请刷新试试!", null);
			}
		}
		return null;
	}
	
	//批量删除
	@RequestMapping(value = "/deleteMany")
	public String deleteMany(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "ids", required = true) String ids) throws IOException{
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			this.outJsonResult(response, false, "您已掉线,请先登录!", null);
		}else{
			if(ids!=null && ids.equals("")){
				this.outJsonResult(response, false, "删除失败,可能是没有选择要删除的数据!", null);
				return null;
			}
			
			if(blogService.deleteByIdsAndUserId(ids.split(","), user.getId())){
				this.outJsonResult(response, true, "删除成功!", null);
			}else{
				this.outJsonResult(response, false, "删除失败,可能数据不存在,请刷新试试!", null);
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
			sc=blogService.publishComment(sc);
			if(sc==null){
				outJsonResult(response, false, "数据有误,评论失败,请刷新后重试!", null);
			}else{
				//==========================================预留空间,通知被评论的人
				if(user.getId()!=sc.getToUserId()){//自己评论自己,则不通知
					NoticeMessage notice=new NoticeMessage();
					notice.setUserId(sc.getToUserId());
					notice.setIsRead(false);
					notice.setModel((short)ToolContext.getSysPropInt("module_blog"));
					notice.setDataId(sc.getDataId());
					notice.setInitiatorId(user.getId());
					notice.setTitle("您发表的博文有了新的评论!");
					String content=sc.getContent().length()>450?(sc.getContent().substring(0, 450)+"..."):sc.getContent();
					notice.setContent("["+user.getUserName()+"] 评论:<br/>\""+content+"\"");
					notice.setMessageType((short)ToolContext.getSysPropInt("message_interactive"));//互动
					notice.setUrl("/blog//blogDetail/"+sc.getDataId());
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
		
		Blog data = blogService.getBlogById(dataId);
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
		Boolean f=blogService.praiseBlog(praise);
		if(f==null){
			outJsonResult(response, false, "亲,您在24小时之内已经点过一次赞了,请过一段时间再点吧!", null);
		}else if(f){
			//==========================================预留空间,通知被点赞的人
			if(user==null || user.getId()!=data.getUserId()){//自己给自己点赞,则不通知
				NoticeMessage notice=new NoticeMessage();
				notice.setUserId(data.getUserId());
				notice.setIsRead(false);
				notice.setModel((short)ToolContext.getSysPropInt("module_blog"));
				notice.setDataId(dataId);
				notice.setTitle("您发表的博文有了新的点赞!");
				if(user!=null){
					notice.setContent("["+user.getUserName()+"] 为您的博文点了个赞!");
					notice.setInitiatorId(user.getId());
				}else{
					String [] array =ip.split("\\.");
					if(array.length==4){
						String newIp=array[0]+"."+array[1]+".*.*";
						notice.setContent("IP ["+newIp+"] 的网友为您的博文点了个赞!");
					}else
						notice.setContent("一位网友为您的博文点了个赞!");
				}
				notice.setMessageType((short)ToolContext.getSysPropInt("message_interactive"));
				notice.setUrl("/blog//blogDetail/"+dataId);
				noticeService.insertSelective(notice);
			}
			outJsonResult(response, true, "点赞成功!", null);
		}else{
			outJsonResult(response, false, "系统错误,请刷新后重试!", null);
		}
		return null;
	}
/*	
	//查看更多评论
	@RequestMapping(value = "/spitslot/moreComment")
	public String moreComment(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "dataId", required = true) Long dataId,
			@RequestParam(value = "page", required = true) int page){
		int start = (page-1)*10-5;
		List<SpitslotCommentView> list=spitslotService.moreComments(dataId, start);
		if(list==null || list.size()<1){
			this.outJsonResult(response, false, "评论都在这里啦~", null);
		}else{
			this.outJsonResult(response, true, "本次为您加载了"+list.size()+"条评论", list);
		}
		return null;
	}
	
	//查看更多评论的回复
	@RequestMapping(value = "/spitslot/moreReply")
	public String moreReply(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "dataId", required = true) Long dataId,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "oid", required = true) Long oid){
		int start = (page-1)*10-5;
		List<SpitslotComment> list=spitslotService.moreReply(dataId, oid, start);
		if(list==null || list.size()<1){
			this.outJsonResult(response, false, "回复都在这里啦~", null);
		}else{
			this.outJsonResult(response, true, "本次为您加载了"+list.size()+"条回复", list);
		}
		return null;
	}
*/
}
