import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import application.ClockSingleton;
import application.TrainController.TrainControllerSingleton;
import application.TrainModel.TrainModelSingleton;
import application.TrainModel.UI.TrainLogger;

public class Demo {

	protected static final boolean trainControllerEnable = true;

	public static void main(String[] args) {
		TrainLogger.enableS();
		
		TrainLogger.printToConsoleS(true);
		
		TrainLogger.infoS("Starting from Demo");

		TrainControllerSingleton 	trnCtrlSin = 	TrainControllerSingleton.getInstance();
		
		TrainModelSingleton trnModSin = TrainModelSingleton.getInstance();
		ClockSingleton clock = ClockSingleton.getInstance();

		final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		final ScheduledFuture<?> future = executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					clock.update();
					TrainLogger.debugS("Clock", "Updating at: " + clock.getCurrentTimeString());
					trnModSin.update();
					
					if(trainControllerEnable) trnCtrlSin.update();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 1, TimeUnit.SECONDS); // Determines update frequency (1 sec atm)

		javafx.application.Application.launch(TestUI.class);
		executorService.shutdown();

		TrainLogger.infoS("We are done.");
	}

}
