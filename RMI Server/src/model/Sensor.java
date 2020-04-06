package model;

public class Sensor 
{	
	private String Name;
	private boolean active = true;
	private int floorNumber;
	private int roomNumber;
	private int smokeLevel = 0;
	private int co2Level = 0;
	
	public Sensor() {}	//Required for POJO
	
	public Sensor(int floorNumber, int roomNumber) 
	{
		this.setFloorNumber(floorNumber);
		this.setRoomNumber(roomNumber);
		
		setName(String.valueOf(floorNumber) + String.valueOf(roomNumber));		
	}

	public boolean isActive() {
		return active;
	}

	public void setActive() {
		this.active = true;
	}
	
	public void setInactive() {
		this.active = false;
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

	public int getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(int floorNumber) {
		this.floorNumber = floorNumber;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	
	
	
}
