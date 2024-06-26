package mainFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The frame for selecting the system type.
 */
public class SelectFrame extends JFrame {
    
    private final int frame_width = 400, frame_height = 300;
    private JButton consumerButton, storeButton, platformButton;
    
    /**
     * Constructor to initialize the SelectFrame.
     */
    public SelectFrame() {
        super("System Select Page");
        consumerButton = new JButton("Consumer");
        storeButton = new JButton("Shop");
        platformButton = new JButton("Platform management");
        setSize(frame_width, frame_height);
        setLayout(new GridLayout(3, 1));
        add(consumerButton);
        add(storeButton);
        add(platformButton);
        addButtonListener();
        setLocationRelativeTo(null);
    }
    
    /**
     * Adds action listeners to the buttons.
     */
    public void addButtonListener() {
        this.consumerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Main.openSelectedProgram(1);
            }
        });
        
        this.storeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Main.openSelectedProgram(2);
            }
        });
        
        this.platformButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Main.openSelectedProgram(3);
            }
        });
    }
}
