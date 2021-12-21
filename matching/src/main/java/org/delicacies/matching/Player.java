package org.delicacies.matching;

import java.io.Serializable;
import java.util.Objects;

import com.opencsv.bean.CsvBindByName;

public class Player implements Serializable {
	// Instance Variables
	// Not setting @ e.g. @CsvBindByName will make all @CsvBindByName by default. Columns must be named same as the variables
	@CsvBindByName
	public String username;
	@CsvBindByName
	public String name;
	@CsvBindByName
	public String genderpref;
	@CsvBindByName
	public String gender;
	@CsvBindByName
	public int age;
	@CsvBindByName
	public int maxage;
	@CsvBindByName
	public int minage;
	@CsvBindByName
	public String interests;
	@CsvBindByName
	public String twotruthsonelie;
	@CsvBindByName
	public String introduction;
	
	public String religion;
	
	
	public void setUsername(String username) {
		this.username = username;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setGenderpref(String genderpref) {
		this.genderpref = genderpref;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setMaxage(int maxage) {
		this.maxage = maxage;
	}
	public void setMinage(int minage) {
		this.minage = minage;
	}
	public void setInterests(String interests) {
		this.interests = interests;
	}
	public void setTwotruthsonelie(String twotruthsonelie) {
		this.twotruthsonelie = twotruthsonelie;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}
	
	public String getUsername()
	{
		return username.toLowerCase();
		}
	public String getName()
	{
		return name;
		}
	public String getGenderpref()
	{
		return genderpref.toLowerCase();
		}
	public String getGender()
	{
		return gender.toLowerCase();
		}
	public int getAge()
	{
		return age;
		}
	public int getMaxage()
	{
		return maxage;
		}
	public int getMinage()
	{
		return minage;
		}
	public String getInterests()
	{
		return interests;
		}
	public String getTwotruthsonelie()
	{
		return twotruthsonelie;
		}
	public String getIntroduction()
	{
		return introduction;
		}
	public String getReligion()
	{
		return religion.toLowerCase();
		}
	
	//Constructor Declaration of Class
	public Player()
			{}
	
//	public Player(String username,
//	String name,
//	String genderpref,
//	String gender,
//	int age,
//	int maxage,
//	int minage,
//	String interests,
//	String twotruthsonelie,
//	String introduction,
//	String religion
//	)
//	{
//		this.username = username;
//		this.name = name;
//		this.genderpref = genderpref;
//		this.gender = gender;
//		this.age = age;
//		this.maxage = maxage;
//		this.minage = minage;
//		this.interests = interests;
//		this.twotruthsonelie = twotruthsonelie;
//		this.introduction = introduction;
//		this.religion = religion;
//	}
	
	@Override
	public String toString()
	{
		return "Player [Username=" + username + ", Name=" + name + ", Genderpref=" + genderpref
				+ ", Gender=" + gender + ", Age=" + age + ", Maxage=" + maxage + ", Minage=" + minage
				+ ", Interests=" + interests + ", Twotruthsonelie=" + twotruthsonelie + ", Intro=" + introduction
				+ ", Religion=" + religion + "]";
	}
	
	
	public boolean isEqual (Player other_player){
		boolean is_equal;
		is_equal = Objects.equals(this.getUsername(), other_player.getUsername());
		return is_equal;
	}
	
	
}
