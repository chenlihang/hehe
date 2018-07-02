package cn.com.boomhope.common.util;

public class FormItem {
	private String name;
	private Object value;
	private boolean file;
	private String contentType;
	
	public static String CONTENTTYPE_JPG = "image/pjpeg";

	public FormItem(String name, String value) {
		super();
		this.name = name;
		this.value = value;
		this.file = false;
	}

	public FormItem(String name, byte[] value ,String contentType) {
		super();
		this.name = name;
		this.value = value;
		this.file = true;
		this.contentType = contentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
