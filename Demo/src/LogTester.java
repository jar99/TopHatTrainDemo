import application.TrainModel.UI.TrainLogger;

public class LogTester {

	public static void main(String[] args) {
		TrainLogger.printToConsoleS(true);
		System.out.println("Running in " + TrainLogger.getModeS());
		TrainLogger.criticalS("Critical");
		
		TrainLogger.errorS("ERROR");

		TrainLogger.debugS("Debug");
		
		TrainLogger.infoS("Info");
	}

}
