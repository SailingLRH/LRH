package com.lrh.service;

import java.util.List;

import com.lrh.model.Spitslot;
import com.lrh.model.Comment;
import com.lrh.model.Praise;
import com.lrh.view.CommentView;

public interface SpitslotService {
	
	/**
	 * 保存一条吐槽数据,并级联保存与吐槽的上传图片的关系
	 * @author Sailing_LRH
	 * @since 2015年11月8日
	 * @param sp Spitslot对象
	 * @return sp Spitslot对象本身
	 */
	Spitslot saveSpitslot(Spitslot sp);
	
	
	/**
	 * 条件+分页查询
	 * @author Sailing_LRH
	 * @since 2015年11月8日
	 * @param param Spitslot对象
	 * @return list 满足条件记录
	 */
	List<Spitslot> findSpitslot(Spitslot param);


	/**
	 * 通过id和userId删除一条数据
	 * @author Sailing_LRH
	 * @since 2015年11月11日
	 * @param userId
	 * @param id
	 * @return true:删除成功;false:删除失败
	 */
	boolean deleteByIdAndUserId(Spitslot param);


	/**
	 * 发表评论
	 * @author Sailing_LRH
	 * @since 2015年11月18日
	 * @param sc
	 */
	Comment publishComment(Comment sc);
	
	/**
	 * 点赞
	 * @author Sailing_LRH
	 * @since 2015年11月22日
	 * @param param
	 * @return 成功与否;null 赞过
	 */
	Boolean praise(Praise param);
	
	/**
	 * 查询吐槽是否存在
	 * @author Sailing_LRH
	 * @since 2015年11月22日
	 * @param dataId
	 * @return
	 */
	Boolean isExistsData(Long dataId);
	
	
	/**
	 * 根据吐槽ID和起始位置查询更多评论
	 * @author Sailing_LRH
	 * @since 2015年12月3日
	 * @param dataId
	 * @param start
	 * @return
	 */
	List<CommentView> moreComments(Long dataId,Integer start);
	
	
	/**
	 * 根据吐槽ID和评论ID和起始位置查询更多的回复
	 * @author Sailing_LRH
	 * @since 2015年12月4日
	 * @param dataId 吐槽ID
	 * @param oid 评论ID
	 * @param start 起始位置
	 * @return
	 */
	List<Comment> moreReply(Long dataId,Long oid,Integer start);
	
	/**
	 * 通过id查询一条数据
	 * @author Sailing_LRH
	 * @since 2015年12月23日
	 * @param dataId
	 * @return
	 */
	Spitslot getSpitslotById(Long dataId);
}
