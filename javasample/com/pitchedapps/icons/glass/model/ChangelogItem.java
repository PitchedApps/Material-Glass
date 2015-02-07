package com.pitchedapps.icons.glass.model;

import java.util.ArrayList;
import java.util.List;

public class ChangelogItem
{
	private List<String> Content;
	private String Date;
	private String Version;
	
	public ChangelogItem() {
		this.Content = new ArrayList<String>();
		this.Date = null;
		this.Version = null;
	}
	
	public ChangelogItem(List<String> Content, String Date, String Version) {
		this.Content = Content;
		this.Date = Date;
		this.Version = Version;
	}
	
	public void addContent(String Content) {
		this.Content.add(Content);
	}
	
	public void setDate(String Date) {
		this.Date = Date;
	}
	
	public void setVersion(String Version) {
		this.Version = Version;
	}
	
	public List<String> getContent() {
		return this.Content;
	}
	
	public String getDate() {
		return this.Date;
	}
	
	public String getVersion() {
		return this.Version;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Content: " + this.Content.toString() + "\n");
		builder.append("Date: " + this.Date + "\n");
		builder.append("Version: " + this.Version + "\n");
		
		return builder.toString();
	}
}