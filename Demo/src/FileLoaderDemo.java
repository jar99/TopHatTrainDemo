import java.io.IOException;

import application.TrainModel.TrainFileLoader;

public class FileLoaderDemo {

	public static void main(String[] args) {
		try {
			TrainFileLoader.loadFile("./test.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
