import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import application.ClockSingleton;
import application.TrainModel.TrainModelSingleton;

public class Demo {

	public static void main(String[] args) {
		System.out.println("Starting from Demo");

		TrainModelSingleton trnModSin = TrainModelSingleton.getInstance();
		ClockSingleton clock = ClockSingleton.getInstance();

		final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		final ScheduledFuture<?> future = executorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					clock.update();
					System.out.println("Updating at: " + clock.getCurrentTimeString());
					trnModSin.update();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 1, TimeUnit.SECONDS); // Determines update frequency (1 sec atm)

		javafx.application.Application.launch(TestUI.class);
		executorService.shutdown();

		System.out.println("We are done.");
	}

}
