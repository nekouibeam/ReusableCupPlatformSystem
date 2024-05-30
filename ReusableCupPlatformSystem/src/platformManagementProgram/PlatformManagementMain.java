package platformManagementProgram;

import javax.swing.JFrame;

/**
 * The main class for the Platform Management program.
 */
public class PlatformManagementMain {

    /**
     * Constructor that initializes and displays the PlatformManagementFrame.
     */
    public PlatformManagementMain() {
        PlatformManagementFrame pframe = new PlatformManagementFrame();
        pframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pframe.setVisible(true);
    }
}
