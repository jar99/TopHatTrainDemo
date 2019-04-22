
public class TrackTrainTest {

	int id;
	double displacement, x, y;
	boolean hasCrashed = false;

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

}
