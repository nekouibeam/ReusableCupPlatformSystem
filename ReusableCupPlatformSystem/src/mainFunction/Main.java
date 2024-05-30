package mainFunction;

import javax.swing.*;

/**
 * The main class that initializes the program.
 */
public class Main {

    private static SelectFrame frame;

    /**
     * The main method of the program.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        frame = new SelectFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Opens the selected program based on the input parameter.
     *
     * @param p the selected program: 1 for consumer program, 2 for shop program, 3 for platform management program.
     */
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
