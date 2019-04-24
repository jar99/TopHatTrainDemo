
public class TrackTrainTest {

	int id;
	double displacement, x, y;
	boolean hasCrashed = false;
	
	int auth;
	double speed;

	TrackTrainTest(int id) {
		this.id = id;
	}

	boolean hasCrashed() {
		return hasCrashed;
	}

	void setCrash(boolean value) {
		hasCrashed = value;
	}

	public void displace(double displacement) {
		this.displacement += displacement;
		this.x += displacement;
		this.y += 0;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public void setAuth(int value) {
		this.auth = value;
	}
	
	public int getAuth() {
		return auth;
	}
	
	public void setSpeed(double value) {
		this.speed = value;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	

}
