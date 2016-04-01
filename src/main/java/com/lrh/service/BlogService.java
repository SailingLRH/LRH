package com.lrh.service;

import java.util.List;
import com.lrh.model.Blog;
import com.lrh.model.Comment;
import com.lrh.model.Praise;
import com.lrh.view.CommentView;
public interface BlogService {
	/**
	 * 通过主键和用户ID删除
	 * @author Sailing_LRH
	 * @since 2016年1月31日
	 * @param param
	 * @return
	 */
	public boolean deleteBlogById(Blog param);

	/**
	 * 新增一条数据并且返回数据本身
	 * @author Sailing_LRH
	 * @since 2016年1月31日
	 * @param blog
	 * @return
	 */
    public Blog saveBlog(Blog blog);
    
    /**
     * 新增一条数据并且返回数据ID
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param blog
     * @return
     */
    public Long insertBlog(Blog blog);

    /**
     * 通过id查询一条数据
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param id
     * @return
     */
    public Blog getBlogById(Long id);

    /**
     * 修改数据
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param record
     * @return
     */
    public boolean updateBlogInfo(Blog blog);

    /**
     * 条件+分页查询
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param param
     * @return list 数据
     */
    public List<Blog> findBlogByParam(Blog param);
    
    /**
     * 条件查询数量
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param param
     * @return int 数量
     */
    public int getCountByParam(Blog param);
    
    /**
     * 发表评论
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param comment
     * @return
     */
    public Comment publishComment(Comment comment);
    
    /**
     * 为博文点赞
     * @author Sailing_LRH
     * @since 2016年1月31日
     * @param praise
     * @return 为空 表示已经在24小时内赞过,不能再赞
     */
    public Boolean praiseBlog(Praise praise);
    
    /**
	 * 查询数据是否存在
	 * @author Sailing_LRH
	 * @since 2016年1月31日
	 * @param dataId
	 * @return
	 */
    public Boolean isExistsData(Long dataId);
	
	
	/**
	 * 查询更多评论
	 * @author Sailing_LRH
	 * @since 2016年1月31日
	 * @param dataId
	 * @param start
	 * @return
	 */
    public List<CommentView> moreComments(Long dataId,Integer start);
	
	
	/**
	 * 查询更多的回复
	 * @author Sailing_LRH
	 * @since 2016年1月31日
	 * @param dataId 数据ID
	 * @param oid 评论ID
	 * @param start 起始位置
	 * @return
	 */
    public List<Comment> moreReply(Long dataId,Long oid,Integer start);
    
    /**
	 * 通过ID list和userId批量删除
	 * @author Sailing_LRH
	 * @since 2016年2月2日
	 * @param ids
	 * @param userId
	 * @return
	 */
    public boolean deleteByIdsAndUserId(String [] ids,int userId);
}
