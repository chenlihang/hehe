package cn.com.boomhope.common.web.exception;

import cn.com.boomhope.common.exception.LogicException;

public class LogicExceptionModel {
	private int result = 0;
	private String exCode;
	private String exMsg;

	public LogicExceptionModel(LogicException e) {
		super();
		this.exCode = e.getExCode();
		this.exMsg = e.getMessage();
	}

	public LogicExceptionModel(String exCode, String exMsg) {
		super();
		this.exCode = exCode;
		this.exMsg = exMsg;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getExCode() {
		return exCode;
	}

	public void setExCode(String exCode) {
		this.exCode = exCode;
	}

	public String getExMsg() {
		return exMsg;
	}

	public void setExMsg(String exMsg) {
		this.exMsg = exMsg;
	}
}
