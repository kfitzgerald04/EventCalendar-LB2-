// EventListPanel.java
// Kendra Fitzgerald

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class EventListPanel extends JPanel {
    private ArrayList<Event> events;
    private JPanel controlPanel;
    private JPanel displayPanel;
    private JComboBox<String> sortDropDown;
    private JCheckBox[] filterBoxes;

    public EventListPanel() {
        events = new ArrayList<>();
        setLayout(new BorderLayout());

        setupControlPanel();
        setupDisplayPanel();

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(displayPanel), BorderLayout.CENTER);
    }

    private void setupControlPanel() {
        controlPanel = new JPanel();

        // setup sort dropdown
        String[] sortOptions = {"Sort by Date", "Sort by Name", "Sort by Date (Reverse)", "Sort by Name (Reverse)"};
        sortDropDown = new JComboBox<>(sortOptions);
        sortDropDown.addActionListener(e -> sortEvents());

        // setup filter checkboxes
        JPanel filterPanel = new JPanel();
        filterBoxes = new JCheckBox[3];
        filterBoxes[0] = new JCheckBox("Hide Completed");
        filterBoxes[1] = new JCheckBox("Hide Deadlines");
        filterBoxes[2] = new JCheckBox("Hide Meetings");

        for (JCheckBox box : filterBoxes) {
            box.addActionListener(e -> updateDisplay());
            filterPanel.add(box);
        }

        // setup add button
        JButton addButton = new JButton("Add Event");
        addButton.addActionListener(e -> new AddEventModal(SwingUtilities.getWindowAncestor(this), this));

        controlPanel.add(sortDropDown);
        controlPanel.add(filterPanel);
        controlPanel.add(addButton);
    }

    private void setupDisplayPanel() {
        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
    }

    public void addEvent(Event event) {
        events.add(event);
        updateDisplay();
    }

    private void sortEvents() {
        int selectedIndex = sortDropDown.getSelectedIndex();
        switch (selectedIndex) {
            case 0: // sort by date
                events.sort(Event::compareTo);
                break;
            case 1: // sort by name
                events.sort(Comparator.comparing(Event::getName));
                break;
            case 2: // reverse date
                events.sort(Event::compareTo);
                events.sort(Comparator.reverseOrder());
                break;
            case 3: // reverse name
                events.sort(Comparator.comparing(Event::getName).reversed());
                break;
        }
        updateDisplay();
    }

    private void updateDisplay() {
        displayPanel.removeAll();

        for (Event event : events) {
            // apply filters
            if (shouldShowEvent(event)) {
                displayPanel.add(new EventPanel(event));
            }
        }

        displayPanel.revalidate();
        displayPanel.repaint();
    }

    private boolean shouldShowEvent(Event event) {
        // check completed filter
        if (filterBoxes[0].isSelected() && event instanceof Completable &&
                ((Completable) event).isComplete()) {
            return false;
        }

        // check deadline filter
        if (filterBoxes[1].isSelected() && event instanceof Deadline) {
            return false;
        }

        // check meeting filter
        if (filterBoxes[2].isSelected() && event instanceof Meeting) {
            return false;
        }

        return true;
    }
}
