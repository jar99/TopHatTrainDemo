import application.TrainModel.UI.TrainLogger;

public class LogTester {

	public static void main(String[] args) {
		TrainLogger logger = TrainLogger.getInstance();
		logger.printToConsole(true);
		System.out.println("Running in " + logger.getMode());
		logger.critical("Critical");
		
		logger.error("ERROR");

		logger.debug("Debug");
		
		logger.info("Info");
	}

}
