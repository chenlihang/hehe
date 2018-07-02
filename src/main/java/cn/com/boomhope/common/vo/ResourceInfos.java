package cn.com.boomhope.common.vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResourceInfos extends AbstractVo
{
	private List<String> biotypes = new ArrayList<String>(4);
	private List<ResourceInfo> resourcesList = new ArrayList<ResourceInfo>();

	/**
	 * 新增一级菜单
	 * @param resourceInfo
	 */
	public void add(ResourceInfo resourceInfo)
	{
		if (resourceInfo != null)
		{
			resourceInfo = biotypeFilter(resourceInfo);
			if (null != resourceInfo)
			{
				this.resourcesList.add(resourceInfo);
			}
		}
	}
	
	public ResourceInfo biotypeFilter(ResourceInfo resourceInfo)
	{
		boolean empty = resourceInfo.getResBioType() == null || resourceInfo.getResBioType().length() == 0;
		if(!empty&&!this.biotypes.contains(resourceInfo.getResBioType()))
		{
			return null;
		}
		else if(!resourceInfo.getResourcesList().isEmpty())
		{
			List<ResourceInfo> infoList = resourceInfo.getResourcesList();
			Iterator<ResourceInfo> it = infoList.iterator();
			while(it.hasNext())
			{
				ResourceInfo info = it.next();
				if(biotypeFilter(info)==null)
				{
					System.out.println("过滤掉："+info.getResUrl());
					it.remove();
				}
			}
		}
		return resourceInfo;
	}

	public void setBiotypes(String biotypes)
	{
		if (biotypes != null && biotypes.length() > 0)
		{
			String[] arr = biotypes.split(",");
			for (String type : arr)
			{
				this.biotypes.add(type);
			}
		}
	}

	public List<ResourceInfo> getResourcesList()
	{
		return resourcesList;
	}

	public void setResourcesList(List<ResourceInfo> resourcesList)
	{
		this.resourcesList = resourcesList;
	}
}
