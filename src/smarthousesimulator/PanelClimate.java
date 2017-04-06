package smarthousesimulator;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class PanelClimate extends JPanel {

    private final JLabel labelHuminidity;
    private final JLabel labelTemperature;
    private final JLabel availability; //color
    private TitledBorder border;

    public PanelClimate() {
        super();
        super.setLayout(new GridLayout(3, 1)); //panel sa tri redka i jednim stupcem
        super.setBorder(new EmptyBorder(10, 20, 10, 20)); //padding

        this.labelTemperature = new JLabel();
        this.labelTemperature.setHorizontalAlignment(SwingConstants.CENTER); //poravnanje teksta
        this.labelHuminidity = new JLabel();
        this.labelHuminidity.setHorizontalAlignment(SwingConstants.CENTER);

        this.availability = new JLabel();
        this.availability.setText("Available");
        this.availability.setBackground(Color.GREEN);
        this.availability.setForeground(Color.BLACK);
        this.availability.setHorizontalAlignment(SwingConstants.CENTER);
        this.availability.setOpaque(true); //potrebno da se oboja pozadina
        this.availability.setBorder(BorderFactory.createLineBorder(Color.gray)); //border oko labele unutar grida

        //dodavanje svega na panel
        super.add(labelHuminidity);
        super.add(labelTemperature);
        super.add(availability);
    }

    public void setLabelHumindity(String text) {
        this.labelHuminidity.setText(text);
    }

    public void setLabelTemperature(String text) {
        this.labelTemperature.setText(text);
    }
    //ako se mijenja u realtime bazi onda ovdje mijenjamo boju i dostupnost
    public void setAvailability(boolean b) {
        if (b == true) {
            this.availability.setText("Available");
            this.availability.setBackground(Color.GREEN);
            this.availability.setForeground(Color.BLACK);
        } else {
            this.availability.setText("Not Available");
            this.availability.setBackground(Color.red);
            this.availability.setForeground(Color.DARK_GRAY);

        }
    }
    //obrub panela i header sa imenom uredjaja
    public void setTitle(String title) {
        border = new TitledBorder(title);
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);
        this.setBorder(border);
    }
}
