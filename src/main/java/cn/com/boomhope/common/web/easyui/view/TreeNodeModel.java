package cn.com.boomhope.common.web.easyui.view;

import java.util.List;
import java.util.Map;

public class TreeNodeModel
{
	private String id;
	private String text;
	private String state;
	private List<TreeNodeModel> children;
	private Map<String, String> attributes;
	private boolean checked;
	private String iconCls;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public List<TreeNodeModel> getChildren()
	{
		return children;
	}

	public void setChildren(List<TreeNodeModel> children)
	{
		this.children = children;
	}

	public Map<String, String> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes)
	{
		this.attributes = attributes;
	}

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	public String getIconCls()
	{
		return iconCls;
	}

	public void setIconCls(String iconCls)
	{
		this.iconCls = iconCls;
	}
}
