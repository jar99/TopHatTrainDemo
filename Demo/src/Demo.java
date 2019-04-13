import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import application.ClockSingleton;
import application.TrainModel.TrainModelSingleton;

public class Demo {

	
	public static void main(String[] args) {
		TrainModelSingleton 		trnModSin = 	TrainModelSingleton.getInstance();
		ClockSingleton 				clock = 		ClockSingleton.getInstance();
		
		new Thread() {
			@Override
			public void run() {
				javafx.application.Application.launch(TestUI.class);
			}
		}.start();
		TestUI.waitForUITest();
		
		final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		final ScheduledFuture<?> future = executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					clock.update();
					trnModSin.update();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 1, TimeUnit.SECONDS); // Determines update frequency (1 sec atm)
	}
	
	

}
