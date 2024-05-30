package consumerProgram;

import javax.swing.*;

/**
 * The ConsumerMain class serves as the entry point for the consumer application.
 * It initializes and displays the ConsumerFrame for user interaction.
 */
public class ConsumerMain {

    /**
     * Constructs a ConsumerMain object, initializing and displaying the ConsumerFrame.
     */
    public ConsumerMain() {
        ConsumerFrame cframe = new ConsumerFrame();
        cframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cframe.setVisible(true);
    }

    /**
     * The main method, serving as the entry point for the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConsumerMain();
            }
        });
    }
}
