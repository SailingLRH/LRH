package com.lrh.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lrh.mapper.BlogCommentMapper;
import com.lrh.mapper.BlogMapper;
import com.lrh.mapper.BlogPraiseMapper;
import com.lrh.mapper.UserMapper;
import com.lrh.model.Blog;
import com.lrh.model.Comment;
import com.lrh.model.Dynamic;
import com.lrh.model.Praise;
import com.lrh.model.User;
import com.lrh.service.BlogService;
import com.lrh.service.DynamicService;
import com.lrh.service.NoticeMessageService;
import com.lrh.util.ToolContext;
import com.lrh.view.CommentView;
@Service("blogService")
public class BlogServiceImpl implements BlogService{

	@Autowired
	BlogMapper blogDao;
	@Autowired
	BlogCommentMapper commentdao;
	@Autowired
	UserMapper userDao;
	@Autowired
	BlogPraiseMapper praiseDao;
	
	@Autowired
	NoticeMessageService noticeService;
	@Autowired
	DynamicService dynamicService;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Transactional
	public boolean deleteBlogById(Blog param) {
		blogDao.deleteByIdAndUserId(param);
		
		//级联删除通知消息
		noticeService.deleteByModelAndDataId((short)ToolContext.getSysPropInt("module_blog"),param.getId());
		//级联删除动态表中的数据
		return dynamicService.deleteByModuleAndDataId((short)ToolContext.getSysPropInt("module_blog"),param.getId());
	}
	
	@Transactional
	public Blog saveBlog(Blog blog) {
		blog.setAddTime(sdf.format(new Date()));
		blogDao.insertSelective(blog);
		
		//往动态表插入一条数据
		Dynamic dynamic = new Dynamic();
		dynamic.setDataId(blog.getId());
		dynamic.setModuleId((short)ToolContext.getSysPropInt("module_blog"));
		dynamicService.addDynamic(dynamic);
		return blog;
	}
	@Override
	public Long insertBlog(Blog blog) {
		blog.setAddTime(sdf.format(new Date()));
		blogDao.insertSelective(blog);
		
		//往动态表插入一条数据
		Dynamic dynamic = new Dynamic();
		dynamic.setDataId(blog.getId());
		dynamic.setModuleId((short)ToolContext.getSysPropInt("module_blog"));
		dynamicService.addDynamic(dynamic);
		return blog.getId();
	}
	
	@Override
	public Blog getBlogById(Long id) {
		return blogDao.selectByPrimaryKey(id);
	}
	
	@Override
	public boolean updateBlogInfo(Blog blog) {
		return blogDao.updateByPrimaryKeySelective(blog)>0;
	}
	
	@Override
	public List<Blog> findBlogByParam(Blog param) {
		return blogDao.findBlogByParam(param);
	}
	
	@Override
	public int getCountByParam(Blog param) {
		return blogDao.getCountByParam(param);
	}
	
	@Override
	public Comment publishComment(Comment comment) {
		Long dataId=comment.getDataId();
		Blog blog=blogDao.selectByPrimaryKey(dataId);
		if(blog==null){
			return null;
		}else{
			Long pid=comment.getPid();
			if(pid==null){//表示直接评论吐槽
				comment.setToUserId(blog.getUserId());
				comment.setTime(sdf.format(new Date()));
				comment.setPid(0L);
				comment.setOid(0L);
				commentdao.insertSelective(comment);
				return comment;
			}else{
				Comment sc=commentdao.selectByPrimaryKey(pid);
				if(sc==null || !sc.getDataId().equals(comment.getDataId())){
					return null;
				}else{
					int toUserId=sc.getUserId();
					User toUser=userDao.selectByPrimaryKey(toUserId);
					Long oid=sc.getOid();
					oid=oid>0?oid:pid;
					comment.setOid(oid);
					comment.setToUserId(toUserId);
					comment.setToUser(toUser);
					comment.setTime(sdf.format(new Date()));
					commentdao.insertSelective(comment);
					return comment;
				}
			}
		}
	}
	
	@Override
	public Boolean praiseBlog(Praise praise) {
		Date date=new Date();//取时间 
	    Calendar calendar = new GregorianCalendar(); 
	    calendar.setTime(date); 
	    calendar.add(Calendar.DATE,-1);//把日期往前移1天
		String now=sdf.format(calendar.getTime());
		praise.setNowTime(now);
		if(praiseDao.checkHasPraised(praise)>0) return null;
		praise.setTime(sdf.format(date));
		return praiseDao.insertSelective(praise)>0;
	}
	
	@Override
	public Boolean isExistsData(Long dataId) {
		return blogDao.selectByPrimaryKey(dataId)==null?false:true;
	}
	
	@Override
	public List<CommentView> moreComments(Long dataId, Integer start) {
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("dataId", dataId);
		param.put("start", start);
		return commentdao.moreComments(param);
	}
	@Override
	public List<Comment> moreReply(Long dataId, Long oid, Integer start) {
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("dataId", dataId);
		param.put("start", start);
		param.put("oid", oid);
		return commentdao.moreReply(param);
	}

	@Override
	public boolean deleteByIdsAndUserId(String[] ids, int userId) {
		Map<String,Object> param =new HashMap<String,Object>();
		param.put("ids", ids);
		param.put("userId", userId);
		blogDao.deleteByIdsAndUserId(param);
		//级联删除通知消息
		noticeService.deleteByModelAndDataIds((short)ToolContext.getSysPropInt("module_blog"),ids);
		//级联删除动态表中的数据
		return dynamicService.deleteByModuleAndDataIds((short)ToolContext.getSysPropInt("module_blog"),ids);
	}
}
