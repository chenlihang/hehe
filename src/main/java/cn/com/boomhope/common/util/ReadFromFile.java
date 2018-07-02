package cn.com.boomhope.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class ReadFromFile {
	
	protected static Logger log = Logger.getLogger(ReadFromFile.class);
	
	/**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            @SuppressWarnings("unused")
			int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
            	sb.append(tempString);
//                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
        	log.error(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }
    
    
    public static void main(String[] args) {
		String path = "F:\\workspace\\bioauth\\bioauth\\bioauth-res\\src\\main\\resources\\face\\faceVer.json";
		String json = readFileByLines(path);
		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = gson.fromJson(json, java.util.Map.class);
		String[] arrs = {"faceDetect", "facialAlignment", "cnnModels", "jbModels"};
		for(String filed : arrs){
			if(map.get(filed) != null){
				String value = (String)map.get(filed);
				if(!new File(value).exists()){
					System.out.println(String.format("%s文件不存在", value));
				}
			}
		}
	}


}
