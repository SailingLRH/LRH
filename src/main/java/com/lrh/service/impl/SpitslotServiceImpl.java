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

import com.lrh.mapper.SpitslotCommentMapper;
import com.lrh.mapper.SpitslotMapper;
import com.lrh.mapper.SpitslotPraiseMapper;
import com.lrh.mapper.SpitslotUploadMapper;
import com.lrh.mapper.UserMapper;
import com.lrh.model.Dynamic;
import com.lrh.model.Spitslot;
import com.lrh.model.Comment;
import com.lrh.model.Praise;
import com.lrh.model.SpitslotUpload;
import com.lrh.model.User;
import com.lrh.service.DynamicService;
import com.lrh.service.NoticeMessageService;
import com.lrh.service.SpitslotService;
import com.lrh.util.ToolContext;
import com.lrh.view.CommentView;

@Service("spitslotService")
public class SpitslotServiceImpl implements SpitslotService{

	@Autowired
	SpitslotMapper spitslotdao;
	@Autowired
	SpitslotUploadMapper uploaddao;
	@Autowired
	SpitslotCommentMapper commentdao;
	@Autowired
	UserMapper userDao;
	@Autowired
	SpitslotPraiseMapper praiseDao;
	
	@Autowired
	NoticeMessageService noticeService;
	@Autowired
	DynamicService dynamicService;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Transactional
	public Spitslot saveSpitslot(Spitslot sp) {
		sp.setAddTime(sdf.format(new Date()));
		spitslotdao.insertSelective(sp);
		long id=sp.getId();
		List<Long> imgList=sp.getImgIdList();
		if(imgList!=null && imgList.size()>0){
			for(Long imgId:imgList){
				SpitslotUpload su=new SpitslotUpload();
				su.setFileId(imgId);
				su.setSpitslotId(id);
				uploaddao.insertSelective(su);
			}
		}
		//往动态表插入一条数据
		Dynamic dynamic = new Dynamic();
		dynamic.setDataId(sp.getId());
		dynamic.setModuleId((short)ToolContext.getSysPropInt("module_spitslot"));
		dynamicService.addDynamic(dynamic);
		return sp;
	}
	@Override
	public List<Spitslot> findSpitslot(Spitslot param) {
		long count=spitslotdao.getSpitslotCount(param);
		param.setAllCount(count);
		if(param.getGoPage()>param.getAllPage()) 
			return null;
		return spitslotdao.findSpitslot(param);
	}
	
	@Transactional
	public boolean deleteByIdAndUserId(Spitslot param) {
		spitslotdao.deleteByIdAndUserId(param);
		//级联删除通知消息
		noticeService.deleteByModelAndDataId((short)ToolContext.getSysPropInt("module_spitslot"),param.getId());
		//级联删除动态表中的数据
		return dynamicService.deleteByModuleAndDataId((short)ToolContext.getSysPropInt("module_spitslot"),param.getId());
	}
	
	@Transactional
	public Comment publishComment(Comment sc) {
		Long dataId=sc.getDataId();
		Spitslot data=spitslotdao.selectByPrimaryKey(dataId);
		if(data==null){
			return null;
		}else{
			Long pid=sc.getPid();
			if(pid==null){//表示直接评论吐槽
				sc.setToUserId(data.getUserId());
				sc.setTime(sdf.format(new Date()));
				sc.setPid(0L);
				sc.setOid(0L);
				commentdao.insertSelective(sc);
				return sc;
			}else{
				Comment comment=commentdao.selectByPrimaryKey(pid);
				if(comment==null || !comment.getDataId().equals(sc.getDataId())){
					return null;
				}else{
					int toUserId=comment.getUserId();
					User toUser=userDao.selectByPrimaryKey(toUserId);
					Long oid=comment.getOid();
					oid=oid>0?oid:pid;
					sc.setOid(oid);
					sc.setToUserId(toUserId);
					sc.setToUser(toUser);
					sc.setTime(sdf.format(new Date()));
					commentdao.insertSelective(sc);
					return sc;
				}
			}
		}
	}
	
	@Transactional
	public Boolean praise(Praise param) {
		Date date=new Date();//取时间 
	    Calendar calendar = new GregorianCalendar(); 
	    calendar.setTime(date); 
	    calendar.add(Calendar.DATE,-1);//把日期往前移1天
		String now=sdf.format(calendar.getTime());
		param.setNowTime(now);
		if(praiseDao.checkHasPraised(param)>0) return null;
		param.setTime(sdf.format(date));
		return praiseDao.insertSelective(param)>0;
	}
	
	@Override
	public Boolean isExistsData(Long dataId) {
		Spitslot sp=spitslotdao.selectByPrimaryKey(dataId);
		if(sp!=null) return true;
		return false;
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
	public Spitslot getSpitslotById(Long dataId) {
		return spitslotdao.selectByPrimaryKey(dataId);
	}

}
