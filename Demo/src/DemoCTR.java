import java.net.URL;
import java.util.ResourceBundle;

import application.ClockSingleton;
import application.TrackModel.SwitchStateException;
import application.TrainModel.TrainInterface;
import application.TrainModel.TrainModelSingleton;
import application.TrainModel.UI.TableRow;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class DemoCTR implements Initializable {

	private AnimationTimer updateAnimation;
	
	TrainModelSingleton trainModSin = TrainModelSingleton.getInstance();
	ClockSingleton clock = ClockSingleton.getInstance();
	
	TrainInterface train;
	
	 @SuppressWarnings("rawtypes")
	 @FXML
	 TableView<TableRow> info_table;
	 
    @SuppressWarnings("rawtypes")
	@FXML
    TableColumn<TableRow, String> information_item;
    
    @SuppressWarnings("rawtypes")
	@FXML
    TableColumn<TableRow, String> information_value;
    
    
    @FXML
    Label time;
    
    @FXML
    TextField time_scale;
    
    @FXML
    CheckBox time_pause;
    
    
    
    @FXML
    TextField train_id;
    
    @FXML
    TextField train_sspeed;
    
    @FXML
    TextField train_spassanger;
    
    @FXML
    ChoiceBox<TrainInterface> train_select;
    
    TrainCtrlTestModel trainCtrl = new TrainCtrlTestModel();
    
    @FXML
    TextField train_ctr_power;
    
    @FXML
    ToggleButton train_ctr_left;
    
    @FXML
    ToggleButton train_ctr_right;
    
    @FXML
    ToggleButton train_ctr_service;
    
    @FXML
    ToggleButton train_ctr_emergency;
    
    @FXML
    ToggleButton train_ctr_inLight;
    
    @FXML
    ToggleButton train_ctr_outLight;
    
    TrackTestModel track = new TrackTestModel();
    
    @FXML
    TextField track_auth;
    
    @FXML
    TextField track_speed;
    
    @FXML
    TextField track_grade;
    
    @FXML
    TextField beacon_data;
    
    @FXML
    ToggleButton has_beacon;
    
    @FXML
    ToggleButton crash_train_button;
    
    @FXML
    TextField track_speed_limit;
    
    @FXML
    CheckBox rail_fault;
    
    @FXML
    CheckBox power_fault;
    
    MboTestModel mbo = new MboTestModel();
    
    @FXML
    TextField mbo_auth;
    
    @FXML
    TextField mbo_speed;
    
    
    
    @FXML
    private void pause(ActionEvent e){
    	if(time_pause.isSelected()) {
    		clock.setRatio(0);
    	} else {
    		set_time_scale();
    	} 	
    }
    
    @FXML
    private void time_set(ActionEvent e){
    	set_time_scale();
    }
    
    private void set_time_scale() {
    	String timeScaleString = time_scale.getText();
    	if(!timeScaleString.isEmpty()) {
    		int timeScale = Integer.parseInt(timeScaleString);
    		System.out.printf("Setting time ratio to %d.\n", timeScale);
    		clock.setRatio(timeScale);
    	}else {
    		System.out.printf("Setting time ratio to 1.\n");
    		clock.setRatio(1);
    	}
    }
    
    @FXML
    private void make_train(ActionEvent e){
    	String trainIDstring = train_id.getText();
    	if(!trainIDstring.isEmpty()) {
    		int trainID = Integer.parseInt(trainIDstring);
    		int passanger = 0;
    		String passanger_s = train_spassanger.getText();
    		if(!passanger_s.isEmpty()) {
    			passanger = Integer.parseInt(passanger_s);
    		}
    		
    		double speed = 0.0;
    		String speed_s = train_sspeed.getText();
    		if(!speed_s.isEmpty()) {
    			speed = Double.parseDouble(speed_s);
    		}
    		try {
				track.createTrain("Line 1", trainID);
			} catch (SwitchStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		TrainInterface t = trainModSin.createTrain(trainID, passanger, speed, track, mbo);	
    		System.out.println("Adding: "+ t);
    		addTrain(trainID, t);
    	}
    }
    
    @FXML
    private void remove_train(ActionEvent e){
    	String trainIDstring = train_id.getText();
    	if(!trainIDstring.isEmpty()) {
    		int trainID = Integer.parseInt(trainIDstring);
    		TrainInterface t = trainModSin.removeTrain(trainID, true);
    		System.out.println("Removing: "+ t);
    		if(train == t) changeTrain(null);
    		if(t != null) removeTrain(trainID, t);
    	}
    }
    
    private void selectTrain(TrainInterface oldTrain, TrainInterface newTrain){
    	TrainInterface t = train_select.getSelectionModel().getSelectedItem();;
    	changeTrain(t);	
    }
    
    private void changeTrain(TrainInterface t) {
		if(t == null) {
			System.out.println("Removing current train.");
		}else {
			System.out.println("Changing to train " + t);
		}
		
		train = t;
	}

	private void setupTable() {
    	
    	
    }
    
	@FXML
    private void change_mbo(ActionEvent e){
    	System.out.println("Changed the mbo info");
    	
    	int auth = 0;
		String auth_s = mbo_auth.getText();
		if(!auth_s.isEmpty()) {
			auth = Integer.parseInt(auth_s);
		}
    	
    	double speed = 0.0;
		String speed_s = mbo_speed.getText();
		if(!speed_s.isEmpty()) {
			speed = Double.parseDouble(speed_s);
		}	
		mbo.setAuth(auth);
		mbo.setSuggestedSpeed(speed);   	
    }
	
	@FXML
	private void send_mbo_data(ActionEvent e) {
		System.out.println("Sending mbo data");
		mbo.sendData(train.getID());
	}
    
	@FXML
    private void change_track_data(ActionEvent e){
    	System.out.println("Changed the track info");
    	
    	if(train == null) return;
    	
    	int auth = 0;
		String auth_s = track_auth.getText();
		if(!auth_s.isEmpty()) {
			auth = Integer.parseInt(auth_s);
		}
    	
    	double speed = 0.0;
		String speed_s = track_speed.getText();
		if(!speed_s.isEmpty()) {
			speed = Double.parseDouble(speed_s);
		}	
		
		track.setAuth(train.getID(), auth);
		track.setSpeed(train.getID(), speed);   	
    }
	
	@FXML
    private void update_track_data(ActionEvent e){
    	
    	double grade = 0.0;
		String grade_s = track_grade.getText();
		if(!grade_s.isEmpty()) {
			grade = Double.parseDouble(grade_s);
		}	
		
		double speedLimit = 0.0;
		String speed_limit_s = track_speed_limit.getText();
		if(!speed_limit_s.isEmpty()) {
			speedLimit = Double.parseDouble(speed_limit_s);
		}
		 	
		track.setSpeedLimit(speedLimit);
		track.setGrade(grade);
    }
	
	@FXML
    private void update_beacon_data(ActionEvent e){		 	
		track.setHasBeacon(has_beacon.isSelected());
		track.setBeaconData(beacon_data.getText());
    }
	
	@FXML
    private void track_rail_fault(ActionEvent e){	
		track.railFault(rail_fault.isSelected());
	}
	
	@FXML
    private void track_power_fault(ActionEvent e){	
		track.powerFault(power_fault.isSelected());
	}
	
	@FXML
    private void crash_train(ActionEvent e) {
		if(train == null) return;
		track.crashTrain(train.getID(), !crash_train_button.isSelected());
	}
	
	
	@FXML
    private void update_train_ctr(ActionEvent e) {
		if(train == null) return;
		String power_s = train_ctr_power.getText();
		if(!power_s.isEmpty()) {
			double power = Double.parseDouble(power_s);
			train.setPower(power);
		}
		
		
		
	}
	
	@FXML
    private void update_train_button(ActionEvent e) {
		if(train == null) return;
		System.out.println("Update buttons.");
		if(train.getLeftDoorState() != train_ctr_left.isSelected()) train.toggleLeftDoors();
		if(train.getRightDoorState() != train_ctr_right.isSelected()) train.toggleRightDoors();
		if(train.getInterierLightState() != train_ctr_inLight.isSelected()) train.toggleInterierLight();
		if(train.getLightState() != train_ctr_outLight.isSelected()) train.toggleLights();
		
		if(train.getServiceBrake() != train_ctr_service.isSelected()) train.toggleServiceBrake();
		
		if(train_ctr_emergency.isSelected()) {
			train.triggerEmergencyBrake();
		}else {
			train.resetEmergencyBrake();
		}
		
	}
	
	
	
    private void checkNumber(TextField textField) {
    	textField.textProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(ObservableValue<? extends String> observable, String oldValue, 
    	        String newValue) {
    	        if (!newValue.matches("\\d*")) {
    	            textField.setText(newValue.replaceAll("[^\\d]", ""));
    	        }
    	    }
    	});
    }
    
    private void checkDecimal(TextField textField) {
    	textField.textProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(ObservableValue<? extends String> observable, String oldValue, 
    	        String newValue) {	        
    	        if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
    	        	textField.setText(newValue.replaceAll("[^\\d^([\\.]\\d)]", ""));
                }
    	    }
    	});
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		checkNumber(time_scale);
		
		checkNumber(train_id);
		checkDecimal(train_sspeed);
		checkNumber(train_spassanger);
		
		checkDecimal(track_speed_limit);
		checkDecimal(track_speed);
		checkNumber(track_auth);
		
		checkDecimal(mbo_speed);
		checkNumber(mbo_auth);
		
		checkDecimal(train_ctr_power);
		
		
		setupTable();
		
		 updateAnimation = new AnimationTimer() {
	        	
	 			@Override
	 			public void handle(long now) {
	 				update();
	 			}
	 		};
	 		updateAnimation.start();
	 		
	 		
	 		train_select.getSelectionModel()
	 	    .selectedItemProperty()
	 	    .addListener( (ObservableValue<? extends TrainInterface> observable, TrainInterface oldValue, TrainInterface newValue) -> selectTrain(oldValue, newValue) );
	}
	
	private void update() {
    	time.setText(clock.getCurrentTimeString());
    	if(train == null) return;
    	
		train_ctr_emergency.setSelected(train.getEmergencyBrake());
    	
    }
	
	void addTrain(int trainID, TrainInterface train) {
		ObservableList<TrainInterface> list = train_select.getItems();
		if(!list.contains(train)) list.add(train);
		
	}

	void removeTrain(int trainID, TrainInterface train) {
		ObservableList<TrainInterface> list = train_select.getItems();
		if(list.contains(train)) list.remove(train);
	}
	
}
