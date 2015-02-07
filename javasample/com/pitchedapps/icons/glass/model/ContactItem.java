package com.pitchedapps.icons.glass.model;

import android.net.Uri;

public class ContactItem
{
	private int mIcon;
	private String mData;
	private Uri mLink;
	
	public ContactItem()
	{
		this.mIcon = 0;
		this.mData = "null";
		this.mLink = null;
	}
	
	public ContactItem(int icon, String data, Uri link)
	{
		this.mIcon = icon;
		this.mData = data;
		this.mLink = link;
	}
	
	public void setIcon(int icon)
	{
		this.mIcon = icon;
	}
	
	public void setData(String data)
	{
		this.mData = data;
	}
	
	public void setLink(Uri link)
	{
		this.mLink = link;
	}
	
	public int getIcon()
	{
		return this.mIcon;
	}
	
	public String getData()
	{
		return this.mData;
	}
	
	public Uri getLink()
	{
		return this.mLink;
	}
}