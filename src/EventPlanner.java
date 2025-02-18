// EventPlanner.java
// Kendra Fitzgerald

import java.time.LocalDateTime;
import javax.swing.*;
import java.awt.*;


public class EventPlanner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Event Planner");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            EventListPanel eventListPanel = new EventListPanel();
            addDefaultEvents(eventListPanel);

            frame.add(eventListPanel);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // adds some default events to demonstrate functionality
    public static void addDefaultEvents(EventListPanel panel) {
        LocalDateTime now = LocalDateTime.now();
        panel.addEvent(new Deadline("Submit Report", now.plusDays(7)));
        panel.addEvent(new Meeting("Team Meeting", now.plusDays(1), now.plusDays(1).plusHours(1), "Room 101"));
    }
}
