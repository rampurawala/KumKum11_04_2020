package com.expedite.apps.kumkum.model;

import com.expedite.apps.kumkum.constants.Constants;

public class Child {
	private String name;
	private String text1;
	private String text2;
	
	public String getName()
	{
		Constants.Logwrite("child: name", name);
		return name;
	}
	
	public void setName(String name)
	{
		Constants.Logwrite("child:set name", name);
		this.name = name;
	}
	
	public String getText1()
	{
		Constants.Logwrite("child:get text1", text1);
		return text1;
	}
	
	public void setText1(String text1)
	{
		Constants.Logwrite("child:set text1", text1);
		this.text1 = text1;
	}
	
	public String getText2()
	{
		Constants.Logwrite("child:get text2", text2);
		return text2;
	}
	
	public void setText2(String text2)
	{
		Constants.Logwrite("child:set text2", text2);
		this.text2 = text2;
	}

}
