import application.TrainModel.JPhysics;

public class JPhysicsTest {

	private static final long DELAY = 1000000000;

	public static void main(String[] args) {

		JPhysics ball = new JPhysics(100, 0.0, true);
		ball.print();

		long start = System.nanoTime();
		long last = start;
		while (ball.getPosition() > 0) {
			long now = System.nanoTime();
			if (now - last > DELAY) {
				System.out.println("Since Start: " + (now - start) / 1e9 + " left " + ball.getPosition());
				ball.update(now - last);
//				ball.print();
				last = now;
			}
		}
		System.out.println("Since Start: " + (System.nanoTime() - start) / 1e9);
		ball.print();
	}

}
