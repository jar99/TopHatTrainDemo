import application.MBO.MBOInterface;
import application.TrackModel.TrackLine;
import application.TrainModel.TrainModelSingleton;

public class MboTestModel implements MBOInterface {

	double suggestespeed;

	int auth;

	@Override
	public void importLine(TrackLine trackLine) {
		// TODO Auto-generated method stub

	}

	void setSuggestedSpeed(double speed) {
		suggestespeed = speed;
	}

	void setAuth(int auth) {
		this.auth = auth;
	}

	public void sendData(int id) {
		TrainModelSingleton trainMod = TrainModelSingleton.getInstance();

		trainMod.setTrainAuthority(id, auth);
		trainMod.setTrainSuggestedSpeed(id, suggestespeed);
	}

	@Override
	public void createTrain(TrackLine trackLine) {
		// TODO Auto-generated method stub
		
	}

}
