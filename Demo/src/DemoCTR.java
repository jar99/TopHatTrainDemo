import java.net.URL;
import java.util.ResourceBundle;

import application.ClockSingleton;
import application.TrackModel.SwitchStateException;
import application.TrackModel.TrackCircuitFailureException;
import application.TrainController.TrainControllerSingleton;
import application.TrainModel.TrainInterface;
import application.TrainModel.TrainModelSingleton;
import application.TrainModel.UI.Converters;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

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
	TextField train_ctr_temperature;

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

	@FXML
	TextField track_passanger_on;

	@FXML
	TextField track_passanger_off;

	@FXML
	Label passangers;

	@FXML
	ToggleButton track_is_station;

	MboTestModel mbo = new MboTestModel();

	@FXML
	TextField mbo_auth;

	@FXML
	TextField mbo_speed;

	private TableRow<Integer> passangersDelta;

	private TableRow<String> trainid;

	private TableRow<Integer> passangersOn;

	private TableRow<Integer> passangersOff;

	private TableRow<Double> angle;

	private TableRow<Double> speedLimit;

	private TableRow<Double> trackSuggestedSpeed;

	private TableRow<Integer> trackAuth;

	private TableRow<Double> MBOSuggestedSpeed;

	private TableRow<Integer> MBOAuth;

	private TableRow<String> beaconData;

	@FXML
	private void pause(ActionEvent e) {
		if (time_pause.isSelected()) {
			clock.setRatio(0);
		} else {
			set_time_scale();
		}
	}

	@FXML
	private void time_set(ActionEvent e) {
		set_time_scale();
	}

	private void set_time_scale() {
		String timeScaleString = time_scale.getText();
		if (!timeScaleString.isEmpty()) {
			int timeScale = Integer.parseInt(timeScaleString);
			System.out.printf("Setting time ratio to %d.\n", timeScale);
			clock.setRatio(timeScale);
		} else {
			System.out.printf("Setting time ratio to 1.\n");
			clock.setRatio(1);
		}
	}

	@FXML
	private void make_train(ActionEvent e) {
		String trainIDstring = train_id.getText();
		if (!trainIDstring.isEmpty()) {
			int trainID = Integer.parseInt(trainIDstring);
			int passanger = 0;
			String passanger_s = train_spassanger.getText();
			if (!passanger_s.isEmpty()) {
				passanger = Integer.parseInt(passanger_s);
			}

			double speed = 0.0;
			String speed_s = train_sspeed.getText();
			if (!speed_s.isEmpty()) {
				speed = Double.parseDouble(speed_s);
			}
			try {
				track.createTrain("Line 1", trainID);
			} catch (SwitchStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			track.addPassangerDiff(passanger);
			TrainInterface t = trainModSin.createTrain(trainID, passanger, speed, track, mbo);
			
			if(Demo.trainControllerEnable) {
				TrainControllerSingleton trnCtrl = TrainControllerSingleton.getInstance();
				trnCtrl.createTrain(trainID, t); // Create this method.
			}
			
			t.dispatch();
			
			System.out.println("Adding: " + t);
			addTrain(trainID, t);
		}
	}

	@FXML
	private void remove_train(ActionEvent e) {
		String trainIDstring = train_id.getText();
		if (!trainIDstring.isEmpty()) {
			int trainID = Integer.parseInt(trainIDstring);
			TrainInterface t = trainModSin.removeTrain(trainID, true);
			System.out.println("Removing: " + t);
			if (train == t)
				changeTrain(null);
			if (t != null)
				removeTrain(trainID, t);
		}
	}

	private void selectTrain(TrainInterface oldTrain, TrainInterface newTrain) {
		TrainInterface t = train_select.getSelectionModel().getSelectedItem();
		;
		changeTrain(t);
	}

	private void changeTrain(TrainInterface t) {
		if (t == null) {
			System.out.println("Removing current train.");
		} else {
			System.out.println("Changing to train " + t);
		}

		train = t;
	}

	private void setupTable() {
		trainid = new TableRow<String>("Train ID", "N/A");
		passangersDelta = new TableRow<Integer>("Passangers Diff", 0, (a) -> Converters.PassangerFormat(a));
		passangersOn = new TableRow<Integer>("Passangers On", 0, (a) -> Converters.PassangerFormat(a));
		passangersOff = new TableRow<Integer>("Passangers Off", 0, (a) -> Converters.PassangerFormat(a));

		angle = new TableRow<Double>("Angle", 0.0);
		speedLimit = new TableRow<Double>("Speed Limit", 0.0, (a) -> Converters.SpeedConverter(a));
		trackSuggestedSpeed = new TableRow<Double>("Track Speed", 0.0, (a) -> Converters.SpeedConverter(a));
		trackAuth = new TableRow<Integer>("Track Auth", 0);

		MBOSuggestedSpeed = new TableRow<Double>("MBO Speed", 0.0, (a) -> Converters.SpeedConverter(a));
		MBOAuth = new TableRow<Integer>("MBO Auth", 0);
		
		beaconData = new TableRow<String>("Beacon", "N/A");	

		info_table.getItems().addAll(trainid, beaconData, passangersDelta, passangersOn, passangersOff, angle, speedLimit,
				trackSuggestedSpeed, trackAuth, MBOSuggestedSpeed, MBOAuth);
	}

	@FXML
	private void change_mbo(ActionEvent e) {
		System.out.println("Changed the mbo info");

		int auth = 0;
		String auth_s = mbo_auth.getText();
		if (!auth_s.isEmpty()) {
			auth = Integer.parseInt(auth_s);
		}

		double speed = 0.0;
		String speed_s = mbo_speed.getText();
		if (!speed_s.isEmpty()) {
			speed = Double.parseDouble(speed_s);
		}
		mbo.setAuth(auth);
		mbo.setSuggestedSpeed(speed);

		MBOAuth.update(auth);
		MBOSuggestedSpeed.update(speed);
	}

	@FXML
	private void send_mbo_data(ActionEvent e) {
		System.out.println("Sending mbo data");
		if(train != null) mbo.sendData(train.getID());
	}

	@FXML
	private void change_track_data(ActionEvent e) {
		System.out.println("Changed the track info");

		int auth = 0;
		String auth_s = track_auth.getText();
		if (!auth_s.isEmpty()) {
			auth = Integer.parseInt(auth_s);
		}

		double speed = 0.0;
		String speed_s = track_speed.getText();
		if (!speed_s.isEmpty()) {
			speed = Double.parseDouble(speed_s);
		}

		if (train == null) {
			track.setAuth(auth);
			track.setSpeed(speed);
			return;
		}
			
		track.setAuth(train.getID(), auth);
		track.setSpeed(train.getID(), speed);

	}

	@FXML
	private void update_track_data(ActionEvent e) {

		double grade = 0.0;
		String grade_s = track_grade.getText();
		if (!grade_s.isEmpty()) {
			grade = Double.parseDouble(grade_s);
		}

		double speedLimit = 0.0;
		String speed_limit_s = track_speed_limit.getText();
		if (!speed_limit_s.isEmpty()) {
			speedLimit = Double.parseDouble(speed_limit_s);
		}

		track.setSpeedLimit(speedLimit);
		track.setGrade(grade);

		angle.update(grade);
		this.speedLimit.update(speedLimit);
	}

	@FXML
	private void update_beacon_data(ActionEvent e) {
		track.setHasBeacon(has_beacon.isSelected());
		track.setBeaconData(beacon_data.getText());
		beaconData.update(beacon_data.getText());
	}

	@FXML
	private void track_rail_fault(ActionEvent e) {
		track.railFault(rail_fault.isSelected());
	}

	@FXML
	private void track_power_fault(ActionEvent e) {
		track.powerFault(power_fault.isSelected());
	}

	@FXML
	private void crash_train(ActionEvent e) {
		if (train == null)
			return;
		track.crashTrain(train.getID(), !crash_train_button.isSelected());
	}

	@FXML
	private void update_train_ctr(ActionEvent e) {
		if (train == null)
			return;
		String power_s = train_ctr_power.getText();
		if (!power_s.isEmpty()) {
			double power = Double.parseDouble(power_s);
			train.setPower(power);
		}
	}
	
	@FXML
	private void update_train_temperature(ActionEvent e) {
		if (train == null)
			return;
		String temperature_s = train_ctr_temperature.getText();
		if (!temperature_s.isEmpty()) {
			double temperature = Double.parseDouble(temperature_s);
			train.setTemperature(temperature);
		}
	}

	@FXML
	private void update_train_button(ActionEvent e) {
		if (train == null)
			return;
		if (train.getLeftDoorState() != train_ctr_left.isSelected())
			train.toggleLeftDoors();
		if (train.getRightDoorState() != train_ctr_right.isSelected())
			train.toggleRightDoors();
		if (train.getInterierLightState() != train_ctr_inLight.isSelected())
			train.toggleInterierLight();
		if (train.getLightState() != train_ctr_outLight.isSelected())
			train.toggleLights();

		if (train.getServiceBrake() != train_ctr_service.isSelected())
			train.toggleServiceBrake();

		if (train_ctr_emergency.isSelected()) {
			train.triggerEmergencyBrake();
		} else {
			train.resetEmergencyBrake();
		}

	}

	@FXML
	private void track_set_station(ActionEvent e) {
		track.setStation(track_is_station.isSelected());
	}

	@FXML
	private void set_passangers_on(ActionEvent e) {
		String passangers_on = track_passanger_on.getText();
		if (!passangers_on.isEmpty()) {
			int passangers = Integer.parseInt(passangers_on);
			track_passanger_on.clear();
			track.addPassangersOn(passangers);
		}
	}

	@FXML
	private void set_passangers_off(ActionEvent e) {
		String passangers_off = track_passanger_off.getText();
		if (!passangers_off.isEmpty()) {
			int passangers = Integer.parseInt(passangers_off);
			track_passanger_off.clear();
			track.addPassangersOff(passangers);
		}
	}

	private void checkNumber(TextField textField) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textField.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}

	private void checkDecimal(TextField textField) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
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

		information_item.setCellValueFactory(new PropertyValueFactory<TableRow, String>("name"));
		information_value
				.setCellValueFactory(new Callback<CellDataFeatures<TableRow, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<TableRow, String> c) {
						return c.getValue().getValue();
					}
				});

		setupTable();

		updateAnimation = new AnimationTimer() {

			@Override
			public void handle(long now) {
				update();
			}
		};
		updateAnimation.start();

		train_select.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends TrainInterface> observable, TrainInterface oldValue,
						TrainInterface newValue) -> selectTrain(oldValue, newValue));
	}

	private void update() {
		time.setText(clock.getCurrentTimeString());

		if (info_table.isVisible()) {
			if (train != null) {
				trainid.update(train.toString());
				try {
					trackAuth.update(track.getTrainBlockAuthority(train.getID()));
					trackSuggestedSpeed.update(track.getTrainSuggestedSpeed(train.getID()));
				} catch (TrackCircuitFailureException e) {
					trackAuth.update(Integer.MIN_VALUE);
					trackSuggestedSpeed.update(Double.NaN);
				}
			} else {
				trainid.update("N/A");
			}
			
			passangersDelta.update(track.getPassangerDiff());
			passangersOn.update(track.getPassangerOn());
			passangersOff.update(track.getPassangerOff());
		}

		if (train == null)
			return;

		train_ctr_emergency.setSelected(train.getEmergencyBrake());
		passangers.setText(String.format("%d", track.getPassangerDiff()));

	}

	void addTrain(int trainID, TrainInterface train) {
		ObservableList<TrainInterface> list = train_select.getItems();
		if (!list.contains(train))
			list.add(train);

	}

	void removeTrain(int trainID, TrainInterface train) {
		ObservableList<TrainInterface> list = train_select.getItems();
		if (list.contains(train)) {
			list.remove(train);
			track.addPassangerDiff(-train.getPassengers());
		}
	}

}
