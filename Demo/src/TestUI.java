import java.util.concurrent.CountDownLatch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

//WARNING: You MUST run this program from Main.java, 
//	otherwise the Singleton's won't talk to each other
public class TestUI extends Application {

	public static final CountDownLatch latch = new CountDownLatch(1);
	public static TestUI uiApp = null;

	private void makeWindow(String path, String title, int width, int height) {
		try {
			Stage trnModStage = new Stage();
//			Code to close all windows if one window closese
//			trnModStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//			    @Override
//			    public void handle(WindowEvent event) {
//			        try {
//			        	Platform.exit();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//			    }
//			});
			
			
			Parent trnModRoot = FXMLLoader.load(getClass().getResource(path));
			trnModStage.setTitle(title);
			Scene trnModScene = new Scene(trnModRoot, width, height); // NOTE: Change last two ints to make window bigger
			trnModStage.setScene(trnModScene);
			trnModStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage ctcStage) {
		makeWindow("./application/TrainModel/TrainModel.fxml", "Train Model", 400, 400);
		makeWindow("./Tester.fxml", "Train Tester", 400, 400);
			
	}

	public static TestUI waitForUITest() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return uiApp;
	}

	public static void setUITest(TestUI uiTest0) {
		uiApp = uiTest0;
		latch.countDown();
	}

	public TestUI() {
		setUITest(this);
	}

	public static void main(String[] args) {
		System.out.println("Starting from TestUI");
		Application.launch(args);
		
	}
}