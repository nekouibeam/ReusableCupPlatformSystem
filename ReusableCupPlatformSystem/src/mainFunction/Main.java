package mainFunction;

import javax.swing.*;

public class Main {

	private static SelectFrame frame;

	public static void main(String[] args) {
		frame = new SelectFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	static void openSelectedProgram(int p) {
		frame.dispose();
		switch (p) {
		case 1:
			consumerProgram.ConsumerMain cm = new consumerProgram.ConsumerMain();
			break;
		case 2:
			storeProgram.StoreMain sm = new storeProgram.StoreMain();
			break;
		case 3:
			platformManagementProgram.PlatformManagementMain pmm = new platformManagementProgram.PlatformManagementMain();
			break;
		}

	}
}
