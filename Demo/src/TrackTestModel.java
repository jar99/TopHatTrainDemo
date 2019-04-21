import java.util.Hashtable;

import application.TrackModel.SwitchStateException;
import application.TrackModel.TrackCircuitFailureException;
import application.TrackModel.TrackModelInterface;
import application.TrackModel.TrainCrashedException;

public class TrackTestModel implements TrackModelInterface {

	private double speed, speedLimit, grade, blockLength;
	private int auth;
	private int onPassengers;
	private int offPassengers;
	private int diffPassengers;
	
	private boolean hasPower = true;
	private boolean isGreen;
	private boolean hasBeacon;
	private boolean isUnderGround;
	private boolean isStation;
	private boolean hasLight;
	private boolean hasRailFault = false;
	private String beaconData;
	
	Hashtable<Integer, TrackTrainTest> trainS = new Hashtable<>();
	

	@Override
	public void createTrain(String lineName, int trainID) throws SwitchStateException {
		trainS.put(trainID, new TrackTrainTest(trainID));
		
	}
	
	@Override
	public int getScheduledBoarding(String lineName, String stationName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScheduledAlighting(String lineName, String stationName) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void updateTrainDisplacement(int trainID, double displacement) throws TrainCrashedException {
		if (!trainS.containsKey(trainID)) throw new IllegalArgumentException("Train: " + trainID + " not found");
		TrackTrainTest t = trainS.get(trainID);
		if(t.hasCrashed()) throw new TrainCrashedException("Train: " + trainID + " has crashed");	
		t.displace(displacement);
	}

	@Override
	public double getTrainXCoordinate(int trainID) {
		if (!trainS.containsKey(trainID)) throw new IllegalArgumentException("Train: " + trainID + " not found");
		TrackTrainTest t = trainS.get(trainID);
		return t.getX();
	}

	@Override
	public double getTrainYCoordinate(int trainID) {
		if (!trainS.containsKey(trainID)) throw new IllegalArgumentException("Train: " + trainID + " not found");
		TrackTrainTest t = trainS.get(trainID);
		return t.getY();
	}

	@Override
	public int stationPassengerExchange(int trainID, int currentPassengers, int capacity) {
		// Need to return the new value of passangers
		int offSum = currentPassengers-offPassengers;
		if(offSum >= 0) {
			currentPassengers-=offPassengers;
			
			diffPassengers-=offPassengers;
			offPassengers = 0;
		}else {
			offPassengers =-offSum;	
			
			diffPassengers-=offSum;
			currentPassengers = 0;
		}
		System.out.println("Train passangers " + currentPassengers + " " + capacity + " off " + offSum + " " + offPassengers + " ");
		
		int onSum = currentPassengers+onPassengers;
		if(onSum <= capacity) {
			currentPassengers+=onPassengers;
			
			diffPassengers+=onSum;
			onPassengers = 0;
		} else {
			int exchange = onSum-capacity;
			currentPassengers += exchange;
			
			diffPassengers+=exchange;
			onPassengers =- exchange;	
		}
		
		System.out.println("Train passangers " + currentPassengers + " " + capacity + " on " + onSum + " " + onPassengers + " ");
		
		return currentPassengers;
	}
	
	public int getPassangerDiff() {
		return diffPassengers;
	}

	@Override
	public double getTrainBlockLength(int trainID) {
		return blockLength;
	}

	@Override
	public double getTrainBlockGrade(int trainID) {
		return grade;
	}

	@Override
	public double getTrainBlockSpeedLimit(int trainID) {
		return speedLimit;
	}

	@Override
	public double getTrainBlockElevation(int trainID) {
		return 0;
	}

	@Override
	public double getTrainBlockTotalElevation(int trainID) {
		return 0;
	}

	@Override
	public boolean trainBlockHasBeacon(int trainID) {
		return hasBeacon;
	}

	@Override
	public String getTrainBlockBeaconData(int trainID) {
		return beaconData;
	}

	@Override
	public boolean trainBlockIsStation(int trainID) {
		return isStation;
	}

	@Override
	public boolean trainBlockIsUnderground(int trainID) {
		return isUnderGround;
	}

	@Override
	public boolean trainBlockHasLight(int trainID) {
		return hasLight;
	}

	@Override
	public boolean trainBlockLightIsGreen(int trainID) {
		return isGreen;
	}

	@Override
	public double getTrainSuggestedSpeed(int trainID) throws TrackCircuitFailureException {
		if(hasRailFault) throw new TrackCircuitFailureException("Track Circuit is Failing");
		return speed;
	}

	@Override
	public int getTrainBlockAuthority(int trainID) throws TrackCircuitFailureException {
		if(hasRailFault) throw new TrackCircuitFailureException("Track Circuit is Failing");
		return auth;
	}

	@Override
	public boolean trainHasPower(int trainID) {
		return hasPower;
	}

	public void setAuth(int id, int auth) {
		
		this.auth = auth;
		
	}
	
	public void setSpeed(int id, double speed) {
		this.speed = speed;
	}


	public void setSpeedLimit(double speedLimit) {
		this.speedLimit = speedLimit;
		
	}
	
	public void setGrade(double grade) {
		this.grade = grade;
	}

	public void setHasBeacon(boolean value) {
		this.hasBeacon = value;		
	}
	
	public void setBeaconData(String data) {
		this.beaconData = data;		
	}

	public void crashTrain(int trainID, boolean notCrash) {
		if (!trainS.containsKey(trainID)) throw new IllegalArgumentException("Train: " + trainID + " not found");
		TrackTrainTest t = trainS.get(trainID);
		t.setCrash(!notCrash);		
	}

	public void railFault(boolean value) {
		this.hasRailFault = value; 		
	}
	
	public void powerFault(boolean value) {
		this.hasPower = value;	
	}
	
	public void setStation(boolean value) {	
		this.isStation = value;
	}
	
	public void addPassangersOn(int passangers) {
		this.onPassengers += passangers;	
	}
	
	public void addPassangersOff(int passangers) {
		this.offPassengers += passangers;	
	}
	
	@Override
	public boolean getSwitchState(String lineName, int switchID) {return false;}
	@Override
	public void setSwitch(String lineName, int switchID, boolean straight) {}
	@Override
	public void setSuggestedSpeed(String lineName, int blockID, double suggestedSpeed) throws TrackCircuitFailureException {}
	@Override
	public void setAuthority(String lineName, int blockID, int authority) throws TrackCircuitFailureException {}
	@Override
	public void setControlAuthority(String lineName, int blockID, boolean ctrlAuthority) throws TrackCircuitFailureException {}
	@Override
	public void setLightStatus(String lineName, int blockID, boolean green) {}
	@Override
	public boolean getOccupancy(String lineName, int blockID) throws TrackCircuitFailureException {return false;}
	@Override
	public void setHeating(String lineName, int blockID, boolean heated) {}
	@Override
	public void setCrossing(String lineName, int blockID, boolean crossingOn) {}

	public int getPassangerOn() {
		return onPassengers;
	}
	
	public int getPassangerOff() {
		return offPassengers;
	}

	@Override
	public int getTotalBoarders(String lineName) {
		// TODO Auto-generated method stub
		return 0;
	}
}
