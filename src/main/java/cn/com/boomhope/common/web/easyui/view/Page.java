package cn.com.boomhope.common.web.easyui.view;

import java.util.List;

/**
 * 分页模型
 * @author 郑铭生
 *
 */
public class Page
{
	private int total;
	private List<?> rows;

	public Page(int total, List<?> rows)
	{
		super();
		this.total = total;
		this.rows = rows;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public List<?> getRows()
	{
		return rows;
	}

	public void setRows(List<?> rows)
	{
		this.rows = rows;
	}
}
