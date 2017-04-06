package smarthousesimulator;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class PanelDoor extends JPanel {

    private final JLabel doorOpened; //color
    private final JLabel doorUnlocked; //color
    private TitledBorder border;

    public PanelDoor() {
        super();
        super.setLayout(new GridLayout(2, 1));
        super.setBorder(new EmptyBorder(10, 20, 10, 20)); //padding

        doorOpened = new JLabel("Initializing...");
        doorOpened.setHorizontalAlignment(SwingConstants.CENTER);
        doorOpened.setOpaque(true);
        doorOpened.setBorder(BorderFactory.createLineBorder(Color.gray));

        doorUnlocked = new JLabel("Initializing...");
        doorUnlocked.setHorizontalAlignment(SwingConstants.CENTER);
        doorUnlocked.setOpaque(true);
        doorUnlocked.setBorder(BorderFactory.createLineBorder(Color.gray));

        super.add(doorOpened);
        super.add(doorUnlocked);
    }

    public void setDoorOpened(boolean b) {
        if (b == true) {
            doorOpened.setText("Opened");
            doorOpened.setBackground(Color.green);
            doorOpened.setForeground(Color.BLACK);
        } else {
            doorOpened.setText("Closed");
            doorOpened.setBackground(Color.red);
            doorOpened.setForeground(Color.DARK_GRAY);
        }
    }

    public void setDoorUnlocked(boolean b) {
        if (b == true) {
            doorUnlocked.setText("Unlocked");
            doorUnlocked.setBackground(Color.green);
            doorUnlocked.setForeground(Color.BLACK);
        } else {
            doorUnlocked.setText("Locked");
            doorUnlocked.setBackground(Color.red);
            doorUnlocked.setForeground(Color.DARK_GRAY);
        }
    }

    public void setTitle(String title) {
        border = new TitledBorder(title);
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);
        this.setBorder(border);
    }
}
