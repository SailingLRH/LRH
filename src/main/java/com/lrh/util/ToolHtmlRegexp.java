package com.lrh.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolHtmlRegexp
{
	public final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签

	public final static String regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*>"; // 找出IMG标签

	public final static String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\""; // 找出IMG标签的SRC属性

	/**
	 * 过滤所有html标签,除了那36个表情(似乎没有意义...)
	 * @author Sailing_LRH
	 * @since 2016年2月4日
	 * @param html
	 * @return
	 */
	public static String filterHtmlBesidesEmoji(String html){
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(html);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1)
		{
			String tag=matcher.group();
			if(!tag.startsWith("<img ") || !tag.contains("default/img/emoji"))
				matcher.appendReplacement(sb, "");
			tag=null;
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * 获取html里的所有图片的src属性
	 * @author Sailing_LRH
	 * @since 2016年2月2日
	 * @param html
	 * @return 没有则返回空集合(非null)
	 */
	public static Set<String> findImgsSrc(String html){
		//先找出所有img标签
		Set<String> imgSet= new HashSet<String>();
		Pattern pattern1 = Pattern.compile(regxpForImgTag);
		Matcher matcher1 = pattern1.matcher(html);
		while (matcher1.find())
		{
			imgSet.add(matcher1.group());
		}
		
		Set<String> imgSrcSet= new HashSet<String>();
		Pattern pattern2 = Pattern.compile(regxpForImaTagSrcAttrib);
		for(String imgTag:imgSet){
			Matcher matcher2 = pattern2.matcher(imgTag);
			if (matcher2.find())
			{
				imgSrcSet.add(matcher2.group());
			}
		}
		return imgSrcSet;
	}
	
	/**
	 * 获取html里第一张图片的url
	 * @author Sailing_LRH
	 * @since 2016年2月2日
	 * @param html
	 * @return 如果没有则返回空
	 */
	public static String getFirstImg(String html){
		Set<String> imgSet= findImgsSrc(html);
		String firstImg="";
		for(String img:imgSet){
			//排除表情和js或flash文件
			if(img.contains("kindEditor/plugins/emoticons") || img.contains("default/img/emoji")) continue;
			else{
				firstImg=img.substring(5, img.length()-1);
			}
		}
		return firstImg.equals("")?null:firstImg;
	}

	/**
	 * 
	 * 基本功能：替换标记以正常显示
	 * <p>
	 * 
	 * @param input
	 * @return String
	 */
	public static String replaceTag(String input)
	{
		if (!hasSpecialChars(input))
		{
			return input;
		}
		StringBuffer filtered = new StringBuffer(input.length());
		char c;
		for (int i = 0; i <= input.length() - 1; i++)
		{
			c = input.charAt(i);
			switch (c)
			{
				case '<':
					filtered.append("&lt;");
					break;
				case '>':
					filtered.append("&gt;");
					break;
				case '"':
					filtered.append("&quot;");
					break;
				case '&':
					filtered.append("&amp;");
					break;
				default:
					filtered.append(c);
			}

		}
		return (filtered.toString());
	}

	/**
	 * 
	 * 基本功能：判断标记是否存在
	 * <p>
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean hasSpecialChars(String input)
	{
		boolean flag = false;
		if ((input != null) && (input.length() > 0))
		{
			char c;
			for (int i = 0; i <= input.length() - 1; i++)
			{
				c = input.charAt(i);
				switch (c)
				{
					case '>':
						flag = true;
						break;
					case '<':
						flag = true;
						break;
					case '"':
						flag = true;
						break;
					case '&':
						flag = true;
						break;
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * 基本功能：过滤所有以"<"开头以">"结尾的标签
	 * <p>
	 * 
	 * @param str
	 * @return String
	 */
	public static String filterHtml(String str)
	{
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1)
		{
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 
	 * 基本功能：过滤指定标签
	 * <p>
	 * 
	 * @param str
	 * @param tag
	 *          指定标签
	 * @return String
	 */
	public static String fiterHtmlTag(String str, String tag)
	{
		String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
		Pattern pattern = Pattern.compile(regxp);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1)
		{
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 
	 * 基本功能：替换指定的标签
	 * <p>
	 * 
	 * @param str
	 * @param beforeTag
	 *          要替换的标签
	 * @param tagAttrib
	 *          要替换的标签属性值
	 * @param startTag
	 *          新标签开始标记
	 * @param endTag
	 *          新标签结束标记
	 * @return String
	 * @如：替换img标签的src属性值为[img]属性值[/img]
	 */
	public static String replaceHtmlTag(String str, String beforeTag, String tagAttrib, String startTag, String endTag)
	{
		String regxpForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";
		String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
		Pattern patternForTag = Pattern.compile(regxpForTag);
		Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
		Matcher matcherForTag = patternForTag.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result = matcherForTag.find();
		while (result)
		{
			StringBuffer sbreplace = new StringBuffer();
			Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
			if (matcherForAttrib.find())
			{
				matcherForAttrib.appendReplacement(sbreplace, startTag + matcherForAttrib.group(1) + endTag);
			}
			matcherForTag.appendReplacement(sb, sbreplace.toString());
			result = matcherForTag.find();
		}
		matcherForTag.appendTail(sb);
		return sb.toString();
	}
	
	public static void main(String[] args) {
//		System.out.println(ToolHtmlRegexp.filterHtml("<a class=\"btn\">保存</a>"));
		String html="<img src=\"btn/dfgfds/sdfgf\"/><object src=\"fsafsdf/adfasdf/erhehe\"></object><img src=\"adsdfsfsa\"/><img src=\"sdafds/sadfsda/ff\"/>";
		System.out.println(getFirstImg(html));
	}
}
