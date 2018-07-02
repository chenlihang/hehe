package cn.com.boomhope.common.util;

import java.io.File;

public class ImageReduce
{
  public static String cutImage(String path)
    throws Exception
  {
    int[] size = ImageUtil.getSize(path);
    if ((size != null) && (size.length >= 2) && ((size[0] > 640) || (size[1] > 480))) {
      String tmp1 = new File(path).getParent() + File.separator + "tmp1.jpg";
      ImageUtil.scale2(path, tmp1, 640, 480, false);
      path = tmp1;
    }

    String img = FileBase64Util.encodeFile(path);
    return img;
  }
  
  public static String cutImage(String path, String name)
		    throws Exception
		  {
		    int[] size = ImageUtil.getSize(path);
		    if ((size != null) && (size.length >= 2) && ((size[0] > 640) || (size[1] > 480))) {
		      String tmp1 = new File(path).getParent() + File.separator + name +".cut";
		      ImageUtil.scale2(path, tmp1, 640, 480, false);
		      path = tmp1;
		    }

		    String img = FileBase64Util.encodeFile(path);
		    
		    FileUtil.deleteFile(path);
		    
		    return img;
		  }
}