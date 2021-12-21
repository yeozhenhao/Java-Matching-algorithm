package org.delicacies.matching;

import java.util.Objects;

import com.opencsv.bean.CsvBindByPosition;

public class Player {
	// Instance Variables
	@CsvBindByPosition(position = 0)
	private String username;
	@CsvBindByPosition(position = 1)
	private String name;
	@CsvBindByPosition(position = 2)
	private String genderpref;
	@CsvBindByPosition(position = 3)
	private String gender;
	@CsvBindByPosition(position = 4)
	private int age;
	@CsvBindByPosition(position = 5)
	private int maxage;
	@CsvBindByPosition(position = 6)
	private int minage;
	@CsvBindByPosition(position = 7)
	private String interests;
	@CsvBindByPosition(position = 8)
	private String twotruthsonelie;
	@CsvBindByPosition(position = 9)
	private String introduction;
	@CsvBindByPosition(position = 10)
	private String religion;
	//Constructor Declaration of Class
	public Player(String username,
	String name,
	String genderpref,
	String gender
//	int age,
//	int maxage,
//	int minage,
//	String interests,
//	String twotruthsonelie,
//	String introduction,
//	String religion
	)
	{
		this.username = username;
		this.name = name;
		this.genderpref = genderpref;
		this.gender = gender;
//		this.age = age;
//		this.maxage = maxage;
//		this.minage = minage;
//		this.interests = interests;
//		this.twotruthsonelie = twotruthsonelie;
//		this.introduction = introduction;
//		this.religion = religion;
	}
	public String getUsername()
	{return username;}
	public String getName()
	{return name;}
	public String getGenderpref()
	{return genderpref;}
	public String getGender()
	{return gender;}
	public String getAge()
	{return age;}
	public String getMaxage()
	{return maxage;}
	public String getMinage()
	{return minage;}
	public String getInterests()
	{return interests;}
	public String getTwotruthsonelie()
	{return twotruthsonelie;}
	public String getIntroduction()
	{return introduction;}
	public String getReligion()
	{return religion;}
	
	public boolean isEqual (Player other_player){
		boolean is_equal;
		is_equal = Objects.equals(this.getUsername(), other_player.getUsername());
		return is_equal;
	}
	
	
}
