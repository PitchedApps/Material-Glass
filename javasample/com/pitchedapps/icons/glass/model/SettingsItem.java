package com.pitchedapps.icons.glass.model;

public class SettingsItem
{
	private int ID;
	private int Type;
	private String Title;
	private String Description;
	private boolean Selected;

	public SettingsItem()
	{
		this.ID = -1;
		this.Type = -1;
		this.Title = "null";
		this.Description = "null";
		this.Selected = false;
	}
	
	public SettingsItem(int Type)
	{
		this.ID = -1;
		this.Type = Type;
		this.Title = "null";
		this.Description = "null";
		this.Selected = false;
	}
	
	public SettingsItem(int Type, String Title)
	{
		this.ID = -1;
		this.Type = Type;
		this.Title = Title;
		this.Description = "null";
		this.Selected = false;
	}
	
	public SettingsItem(int Type, String Title, String Description)
	{
		this.ID = -1;
		this.Type = Type;
		this.Title = Title;
		this.Description = Description;
		this.Selected = false;
	}
	
	public SettingsItem(int Type, String Title, String Description, boolean Selected)
	{
		this.ID = -1;
		this.Type = Type;
		this.Title = Title;
		this.Description = Description;
		this.Selected = Selected;
	}
	
	public SettingsItem(Builder builder)
	{
		this.ID = builder.ID;
		this.Type = builder.Type;
		this.Title = builder.Title;
		this.Description = builder.Description;
		this.Selected = builder.Selected;
	}
	
	public void setID(int ID)
	{
		this.ID = ID;
	}
	
	public void setType(int Type)
	{
		this.Type = Type;
	}
	
	public void setTitle(String Title)
	{
		this.Title = Title;
	}
	
	public void setDescription(String Description)
	{
		this.Description = Description;
	}
	
	public void setSelected(boolean Selected)
	{
		this.Selected = Selected;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public int getType()
	{
		return this.Type;
	}
	
	public String getTitle()
	{
		return this.Title;
	}
	
	public String getDescription()
	{
		return this.Description;
	}
	
	public boolean isSelected()
	{
		return this.Selected;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ID: " + this.ID + "\n");
		builder.append("Type: " + this.Type + "\n");
		builder.append("Title: " + this.Title + "\n");
		builder.append("Description: " + this.Description + "\n");
		builder.append("Selected: " + this.Selected);
		return builder.toString();
	}
	
	public static class Builder
	{
		private int ID;
		private int Type;
		private String Title;
		private String Description;
		private boolean Selected;
		
		public Builder() {
			this.ID = -1;
			this.Type = -1;
			this.Title = "null";
			this.Description = "null";
			this.Selected = false;
		}
		
		public Builder id(int ID) {
			this.ID = ID;
			return this;
		}
		
		public Builder type(int Type) {
			this.Type = Type;
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
		
		public Builder selected(boolean Selected) {
			this.Selected = Selected;
			return this;
		}
		
		public SettingsItem build() {
			return new SettingsItem(this);
		}
	}
}