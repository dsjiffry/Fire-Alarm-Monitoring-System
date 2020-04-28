package model;

import java.io.Serializable;

public class Sensor implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String sensorUID;
	private String active = "online";
	private int floor;
	private int room;
	private String sensorType;
	private int smokeLevel = 0;
	private int co2Level = 0;
	
	public Sensor(int floorNumber, int roomNumber) 
	{
		this.setFloor(floorNumber);
		this.setRoom(roomNumber);
		
		setSensorUID(String.valueOf(floorNumber) + String.valueOf(roomNumber));		
	}

	public boolean isActive() {
		if(active.equalsIgnoreCase("online"))
		{
			return true;
		}
		return false;
	}

	public void setActive() {
		this.active = "online";
	}
	
	public void setInactive() {
		this.active = "offline";
	}

	public int getSmokeLevel() {
		return smokeLevel;
	}

	public void setSmokeLevel(int smokeLevel) {
		this.smokeLevel = smokeLevel;
	}

	public int getCo2Level() {
		return co2Level;
	}

	public void setCo2Level(int co2Level) {
		this.co2Level = co2Level;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floorNumber) {
		this.floor = floorNumber;
	}

	public String getSensorUID() {
		return sensorUID;
	}

	public void setSensorUID(String sensorUID) {
		this.sensorUID = sensorUID;
	}

	public int getRoom() {
		return room;
	}

	public void setRoom(int roomNumber) {
		this.room = roomNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}
	
	

	
	
}
