package com.lrh.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;

/**
 * 图片工具类
 * 
 * @author Sailing_LRH
 * @since 2016年1月9日
 */
public class ToolImage {
	/**
	 * 功能 :调整图片大小
	 * 
	 * @param srcImgPath
	 *            原图片路径
	 * @param distImgPath
	 *            转换大小后图片路径
	 * @param width
	 *            转换后图片宽度
	 * @param height
	 *            转换后图片高度
	 */

	public static void resizeImage(String srcImgPath, String distImgPath, int width, int height) throws IOException {
		System.err.println("------------------->正在将 "+srcImgPath+"转化成 "+distImgPath+",分辨率为 ["+width+" X "+height+"]");
		File srcFile = new File(srcImgPath);

		Image srcImg = ImageIO.read(srcFile);

		BufferedImage buffImg = null;

		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		buffImg.getGraphics().drawImage(

		srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,0, null);

		ImageIO.write(buffImg, "JPEG", new File(distImgPath));
		System.err.println("------------------->图片大小调整成功!");
	}

	public static void main(String[] args) {
		try {
			resizeImage("D:/cube.jpg", "D:/cube.jpg", 300, 300);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
