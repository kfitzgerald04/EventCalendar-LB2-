// EventPanel.java
// Kendra Fitzgerald

import javax.swing.*;
import java.awt.*;

public class EventPanel extends JPanel {
    private Event event;
    private JButton completeButton;

    public EventPanel(Event event) {
        this.event = event;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // create main info panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.add(new JLabel("Name: " + event.getName()));
        infoPanel.add(new JLabel("Time: " + event.getDateTime()));

        // add specific info for meetings
        if (event instanceof Meeting) {
            Meeting meeting = (Meeting) event;
            infoPanel.add(new JLabel("Duration: " + meeting.getDuration().toHours() + " hours"));
            infoPanel.add(new JLabel("Location: " + meeting.getLocation()));
        }

        add(infoPanel, BorderLayout.CENTER);

        // add complete button for completable events
        if (event instanceof Completable) {
            completeButton = new JButton("Complete");
            completeButton.addActionListener(e -> {
                ((Completable) event).complete();
                completeButton.setEnabled(false);
            });
            add(completeButton, BorderLayout.EAST);
        }
    }
}