package com.pitchedapps.icons.glass.model;

public class Home3Item
{
	private int ID;
	private int Icon;
	private String Title;
	private String Description;
	
	public Home3Item()
	{
		this.ID = -1;
		this.Icon = 0;
		this.Title = "null";
		this.Description = "null";
	}
	
	public Home3Item(int ID, int Icon, String Title, String Description)
	{
		this.ID = ID;
		this.Icon = Icon;
		this.Title = Title;
		this.Description = Description;
	}
	
	public Home3Item(Builder builder)
	{
		this.ID = builder.ID;
		this.Icon = builder.Icon;
		this.Title = builder.Title;
		this.Description = builder.Description;
	}
	
	public void setID(int ID)
	{
		this.ID = ID;
	}
	
	public void setIcon(int Icon)
	{
		this.Icon = Icon;
	}
	
	public void setTitle(String Title)
	{
		this.Title = Title;
	}
	
	public void setDescription(String Description)
	{
		this.Description = Description;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public int getIcon()
	{
		return this.Icon;
	}
	
	public String getTitle()
	{
		return this.Title;
	}
	
	public String getDescription()
	{
		return this.Description;
	}
	
	public static class Builder
	{
		private int ID;
		private int Icon;
		private String Title;
		private String Description;
		
		public Builder() {
			this.ID = -1;
			this.Icon = 0;
			this.Title = "null";
			this.Description = "null";
		}
		
		public Builder id(int ID) {
			this.ID = ID;
			return this;
		}
		
		public Builder icon(int Icon) {
			this.Icon = Icon;
			return this;
		}
		
		public Builder title(String Title) {
			this.Title = Title;
			return this;
		}
		
		public Builder description(String Description) {
			this.Description = Description;
			return this;
		}
		
		public Home3Item build() {
			return new Home3Item(this);
		}
	}
}