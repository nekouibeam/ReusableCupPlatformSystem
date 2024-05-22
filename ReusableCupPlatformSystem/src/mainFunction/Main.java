package mainFunction;

import javax.swing.*;
import databaseConnection.*;

public class Main {

	private static SelectFrame frame;

	public static void main(String[] args) {
		frame = new SelectFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		DBC_Test t = new DBC_Test();
		t.test();
		
	}

	static void openSelectedProgram(int p) {
		frame.dispose();
		switch (p) {
		case 1:
			consumerProgram.ConsumerMain cm = new consumerProgram.ConsumerMain();
			break;
		case 2:
			shopProgram.ShopMain sm = new shopProgram.ShopMain();
			break;
		case 3:
			platformManagementProgram.PlatformManagementMain pmm = new platformManagementProgram.PlatformManagementMain();
			break;
		}

	}
}
