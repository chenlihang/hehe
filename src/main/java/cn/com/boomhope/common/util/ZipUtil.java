package cn.com.boomhope.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class ZipUtil {
	
	protected static Logger log = Logger.getLogger(ZipUtil.class);
	
	public static void main(String[] args) {  
	        // TODO Auto-generated method stub  
		 try {
			unZipFiles(new File("d:\\22.zip"), "d:\\22\\");
		} catch (Exception e) {
			log.error(e);
		} 
    } 
	 
	 /** 
     * 解压文件到指定目录 
     * @param zipFile 
     * @param descDir 
     * @author isea533 
     */  
    @SuppressWarnings("rawtypes")
	public static void unZipFiles(File zipFile,String descDir){  
        File pathFile = new File(descDir);  
        if(!pathFile.exists()){  
            pathFile.mkdirs();  
        }  
        ZipFile zip = null;
        try{
        	zip = new ZipFile(zipFile, "gbk"); 
            for(Enumeration entries = zip.getEntries();entries.hasMoreElements();){  
                ZipEntry entry = (ZipEntry)entries.nextElement();  
                String zipEntryName = entry.getName();  
                InputStream in = null;
                OutputStream out = null;
                try{
                	 in = zip.getInputStream(entry);  
                     String outPath = (descDir+zipEntryName).replaceAll("\\*", "/");;  
                     //判断路径是否存在,不存在则创建文件路径  
                     File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));  
                     if(!file.exists()){  
                         file.mkdirs();  
                     }  
                     //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压  
                     if(new File(outPath).isDirectory()){  
                         continue;  
                     }  
                     //输出文件路径信息  
//                     System.out.println(outPath);  
                       
                     out = new FileOutputStream(outPath);  
                     byte[] buf1 = new byte[1024];  
                     int len;  
                     while((len=in.read(buf1))>0){  
                         out.write(buf1,0,len);  
                     }  
                }catch(Exception e){
                	System.out.println(e);
                }finally{
                	if(in != null){
                		 in.close();  
                	}
                	if(out != null){
                		out.close();  
                	}
                }
            } 
        }catch(Exception e){
        	log.error(e);
        }finally{
        	if(zip != null){
        		try{
        			 zip.close();
        		}catch(Exception e){
        			log.error("关闭流失败", e);
        		}
        	}
        }
//        System.out.println("******************解压完毕********************");  
    }
	 
    /** 
     * 解压文件到指定目录 
     * @param zipFile 
     * @param descDir 
     * @author isea533 
     */  
    @SuppressWarnings("rawtypes")
	public static void unzip(File zipFile,String descDir){  
        File pathFile = new File(descDir);  
        if(!pathFile.exists()){  
            pathFile.mkdirs();  
        }  
        ZipFile zip = null;
        try{
        	zip = new ZipFile(zipFile, "gbk"); 
            for(Enumeration entries = zip.getEntries();entries.hasMoreElements();){  
                ZipEntry entry = (ZipEntry)entries.nextElement();  
                String zipEntryName = entry.getName();  
                InputStream in = null;
                OutputStream out = null;
                try{
                	 in = zip.getInputStream(entry);  
                     String outPath = (descDir+File.separator+zipEntryName).replaceAll("\\*", "/");;  
                     //判断路径是否存在,不存在则创建文件路径  
                     File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));  
                     if(!file.exists()){  
                         file.mkdirs();  
                     }  
                     //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压  
                     if(new File(outPath).isDirectory()){  
                         continue;  
                     }  
                     //输出文件路径信息  
//                     System.out.println(outPath);  
                       
                     out = new FileOutputStream(outPath);  
                     byte[] buf1 = new byte[1024];  
                     int len;  
                     while((len=in.read(buf1))>0){  
                         out.write(buf1,0,len);  
                     }  
                }catch(Exception e){
                	System.out.println(e);
                }finally{
                	if(in != null){
                		 in.close();  
                	}
                	if(out != null){
                		out.close();  
                	}
                }
            } 
        }catch(Exception e){
        	log.error(e);
        }finally{
        	if(zip != null){
        		try{
        			 zip.close();
        		}catch(Exception e){
        			log.error("关闭流失败", e);
        		}
        	}
        }
    } 
}