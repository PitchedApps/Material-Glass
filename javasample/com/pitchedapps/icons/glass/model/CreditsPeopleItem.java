package com.pitchedapps.icons.glass.model;

import android.net.Uri;

public class CreditsPeopleItem {
	private Uri Avatar;
	private Uri Banner;
	private String Name;
	private String Tagline;
	private String Description;
	private int Type;
	private boolean Expanded;
	
	public CreditsPeopleItem()
	{
		this.Avatar = null;
		this.Banner = null;
		this.Name = "null";
		this.Tagline = "null";
		this.Description = "null";
		this.Type = -1;
		this.Expanded = false;
	}
	
	public CreditsPeopleItem(Uri Avatar, Uri Banner, String Name, String Tagline, String Description, int Type)
	{
		this.Avatar = Avatar;
		this.Banner = Banner;
		this.Name = Name;
		this.Tagline = Tagline;
		this.Description = Description;
		this.Type = Type;
		this.Expanded = false;
	}
	
	public CreditsPeopleItem(Uri Avatar, Uri Banner, String Name, String Tagline, String Description, int Type, boolean Expanded)
	{
		this.Avatar = Avatar;
		this.Banner = Banner;
		this.Name = Name;
		this.Tagline = Tagline;
		this.Description = Description;
		this.Type = Type;
		this.Expanded = Expanded;
	}
	
	public CreditsPeopleItem(Builder builder)
	{
		this.Avatar = builder.Avatar;
		this.Banner = builder.Banner;
		this.Name = builder.Name;
		this.Tagline = builder.Tagline;
		this.Description = builder.Description;
		this.Type = builder.Type;
		this.Expanded = builder.Expanded;
	}
	
	public void setAvatar(Uri Avatar) {
		this.Avatar = Avatar;
	}
	
	public void setBanner(Uri Banner) {
		this.Banner = Banner;
	}
	
	public void setName(String Name) {
		this.Name = Name;
	}
	
	public void setTagline(String Tagline) {
		this.Tagline = Tagline;
	}
	
	public void setDescription(String Description) {
		this.Description = Description;
	}
	
	public void setType(int Type) {
		this.Type = Type;
	}
	
	public void setExpanded(boolean Expanded) {
		this.Expanded = Expanded;
	}
	
	public Uri getAvatar() {
		return this.Avatar;
	}
	
	public Uri getBanner() {
		return this.Banner;
	}
	
	public String getName() {
		return this.Name;
	}
	
	public String getTagline() {
		return this.Tagline;
	}
	
	public String getDescription() {
		return this.Description;
	}
	
	public int getType() {
		return this.Type;
	}
	
	public boolean isExpanded() {
		return this.Expanded;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Avatar = [" + this.Avatar.toString() + "], ");
		builder.append("Banner = [" + this.Banner.toString() + "], ");
		builder.append("Name = [" + this.Name + "], ");
		builder.append("Tagline = [" + this.Tagline + "], ");
		builder.append("Description = [" + this.Description + "], ");
		builder.append("Type = [" + this.Type + "], ");
		builder.append("Expanded = [" + this.Expanded + "]");
		return builder.toString();
	}
	
	public static class Builder
	{
		private Uri Avatar;
		private Uri Banner;
		private String Name;
		private String Tagline;
		private String Description;
		private int Type;
		private boolean Expanded;
		
		public Builder()
		{
			this.Avatar = null;
			this.Banner = null;
			this.Name = "null";
			this.Tagline = "null";
			this.Description = "null";
			this.Type = -1;
			this.Expanded = false;
		}
		
		public Builder avatar(Uri Avatar) {
			this.Avatar = Avatar;
			return this;
		}
		
		public Builder banner(Uri Banner) {
			this.Banner = Banner;
			return this;
		}
		
		public Builder name(String Name) {
			this.Name = Name;
			return this;
		}
		
		public Builder tagline(String Tagline) {
			this.Tagline = Tagline;
			return this;
		}
		
		public Builder description(String Description) {
			this.Description = Description;
			return this;
		}
		
		public Builder type(int Type) {
			this.Type = Type;
			return this;
		}
		
		public Builder Expanded(boolean Expanded) {
			this.Expanded = Expanded;
			return this;
		}
		
		public CreditsPeopleItem build()
		{
			return new CreditsPeopleItem(this);
		}
	}
}