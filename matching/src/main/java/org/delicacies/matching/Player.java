package org.delicacies.matching;

import java.io.Serializable;
import java.util.Objects;

import com.opencsv.bean.CsvBindByName;

public class Player implements Serializable {
	// Instance Variables
	// Not setting @ e.g. @CsvBindByName will make all @CsvBindByName by default. Columns must be named same as the variables
	@CsvBindByName(column = "Telegram Username", required = true) //required: Whether or not the annotated field is required to be present in every data set of the input.
	public String username;
	@CsvBindByName(column = "Name", required = true)
	public String name;
	@CsvBindByName(column = "Genderpref", required = true)
	public String genderpref;
	@CsvBindByName(column = "Gender", required = true)
	public String gender;
	@CsvBindByName(column = "Age", required = true)
	public int age;
	@CsvBindByName(column = "Maxage", required = true)
	public int maxage;
	@CsvBindByName(column = "Minage", required = true)
	public int minage;
	@CsvBindByName(column = "Interests", required = true)
	public String interests;
	@CsvBindByName(column = "Twotruthsonelie", required = true)
	public String twotruthsonelie;
	@CsvBindByName(column = "Intro", required = true)
	public String introduction;
	@CsvBindByName(column = "Religion", required = true)
	public String religion;
	@CsvBindByName(column = "Religionqn", required = true)
	public String religionqn;
	
	
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
	public void setReligionqn(String religionqn) {
		this.religionqn = religionqn;
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
	
	public String getReligionqn()
	{
		return religionqn.toLowerCase();
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
		return username; //Removed "Player [Username=" +  
//				+ ", Name=" + name + ", Genderpref=" + genderpref
//				+ ", Gender=" + gender + ", Age=" + age + ", Maxage=" + maxage + ", Minage=" + minage
//				+ ", Interests=" + interests + ", Twotruthsonelie=" + twotruthsonelie + ", Intro=" + introduction
//				+ ", Religion=" + religion + ", Religion Qn=" + religionqn 
//				+ "]";
	}
	
	
	public boolean isEqual (Player other_player){
		boolean is_equal;
		is_equal = Objects.equals(this.getUsername(), other_player.getUsername());
		return is_equal;
	}
	
	
	
	
	
	
}
