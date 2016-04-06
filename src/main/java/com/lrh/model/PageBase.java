package com.lrh.model;

/**
 * 分页基础类
 * @author Sailing_LRH
 * @since 2015年11月8日
 */
public class PageBase {
	public int nowPage;
	public int goPage;
	public long start;
	public int max;
	public long allCount;
	public int allPage;
	
	public int getNowPage() {
		nowPage=goPage>0?goPage:1;
		return nowPage;
	}
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	public int getGoPage() {
		return goPage>0?goPage:1;
	}
	public void setGoPage(int goPage) {
		this.goPage = goPage>0?goPage:1;
	}
	public long getStart() {
		//起始位置:(页数-1) X 每页显示数
		int goPage1=getGoPage();
		int max1=getMax();
		start=(goPage1-1)*max1;
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public int getMax() {
		return max>0?max:10;
	}
	public void setMax(int max) {
		this.max = max>0?max:10;
	}
	public long getAllCount() {
		return allCount>0?allCount:0;
	}
	public void setAllCount(long allCount) {
		this.allCount = allCount>0?allCount:0;
	}
	public int getAllPage() {
		long allCount1=getAllCount();
		int max1=getMax();
		if(allCount1%max1==0){
			allPage=(int) (allCount1/max1);
		}else{
			allPage=(int) (allCount1/max1) +1;
		}
		return allPage;
	}
	public void setAllPage(int allPage) {
		this.allPage = allPage>0?allPage:0;
	}
	
}
