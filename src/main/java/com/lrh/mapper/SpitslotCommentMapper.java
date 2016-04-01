package com.lrh.mapper;

import java.util.List;
import java.util.Map;

import com.lrh.model.Comment;
import com.lrh.view.CommentView;

public interface SpitslotCommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);
    
    /**
     * 通过吐槽ID查询5条评论
     * @author Sailing_LRH
     * @since 2015年11月15日
     * @param dataId
     * @return
     */
    List<CommentView> findFiveComments(Long dataId);
    
    /**
     * 通过评论id查询5条回复
     * @author Sailing_LRH
     * @since 2015年11月15日
     * @param id
     * @return
     */
    List<Comment> findFiveReplys (Long id);
    
    /**
     * 通过吐槽ID查询所有评论数量
     * @author Sailing_LRH
     * @since 2015年11月15日
     * @param dataId
     * @return
     */
    int getCommentCount(Long dataId);
    
    /**
     * 通过评论ID查询所有回复数量
     * @author Sailing_LRH
     * @since 2015年11月15日
     * @param id
     * @return
     */
    int getReplyCount(Long id);
    
    
    /**
     * 多条件查询
     * @author Sailing_LRH
     * @since 2015年11月18日
     * @param params
     * @return
     */
    List<Comment> getSpitslotCommentByParams(Comment params);
    
    
    /**
     * 根据吐槽ID和起始位置查询更多评论(包括评论的前5条回复)
     * @author Sailing_LRH
     * @since 2015年12月3日
     * @param dataId 吐槽ID;start 起始位置
     * @return
     */
    List<CommentView> moreComments(Map<String,Object> param);
    
    
    /**
     * 根据吐槽ID和评论ID和起始位置查询更多的回复
     * @author Sailing_LRH
     * @since 2015年12月4日
     * @param map dataId 吐槽ID;oid 评论ID;start 起始位置
     * @return
     */
    List<Comment> moreReply(Map<String,Object> param);
}