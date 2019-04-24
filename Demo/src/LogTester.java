import application.TrainModel.UI.TrainLogger;

public class LogTester {

	public static void main(String[] args) {
		TrainLogger logger = TrainLogger.getInstance();
		logger.print("Test");

	}

}
