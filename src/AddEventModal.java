// AddEventModal.java
// Kendra Fitzgerald

import javax.swing.*;
import java.awt.*;
import java.time.*;

public class AddEventModal extends JDialog {
    private EventListPanel parentPanel;
    private JTextField nameField;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;
    private JTextField locationField;
    private JSpinner endTimeSpinner;
    private JComboBox<String> eventTypeCombo;

    public AddEventModal(Window owner, EventListPanel parent) {
        super(owner, "Add New Event", ModalityType.APPLICATION_MODAL);
        this.parentPanel = parent;

        setupUI();
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // main input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // event type selection
        inputPanel.add(new JLabel("Event Type:"), gbc);
        eventTypeCombo = new JComboBox<>(new String[]{"Deadline", "Meeting"});
        gbc.gridx = 1;
        inputPanel.add(eventTypeCombo, gbc);

        // name field
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        // date spinner
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Date:"), gbc);
        dateSpinner = new JSpinner(new SpinnerDateModel());
        gbc.gridx = 1;
        inputPanel.add(dateSpinner, gbc);

        // time spinner
        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Time:"), gbc);
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        gbc.gridx = 1;
        inputPanel.add(timeSpinner, gbc);

        // meeting-specific fields
        locationField = new JTextField(20);
        endTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
        endTimeSpinner.setEditor(endTimeEditor);

        eventTypeCombo.addActionListener(e -> updateFields(inputPanel, gbc));

        // buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        addButton.addActionListener(e -> addEvent());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateFields(JPanel panel, GridBagConstraints gbc) {
        // remove meeting-specific fields if they exist
        panel.remove(locationField);
        panel.remove(endTimeSpinner);

        if (eventTypeCombo.getSelectedItem().equals("Meeting")) {
            // add location field
            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(new JLabel("Location:"), gbc);
            gbc.gridx = 1;
            panel.add(locationField, gbc);

            // add end time field
            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(new JLabel("End Time:"), gbc);
            gbc.gridx = 1;
            panel.add(endTimeSpinner, gbc);
        }

        pack();
        revalidate();
        repaint();
    }

    private void addEvent() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name");
            return;
        }

        // get date and time
        LocalDateTime dateTime = getDateTime(dateSpinner, timeSpinner);

        if (eventTypeCombo.getSelectedItem().equals("Deadline")) {
            parentPanel.addEvent(new Deadline(name, dateTime));
        } else {
            String location = locationField.getText().trim();
            if (location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a location");
                return;
            }
            LocalDateTime endDateTime = getDateTime(dateSpinner, endTimeSpinner);
            if (endDateTime.isBefore(dateTime)) {
                JOptionPane.showMessageDialog(this, "End time must be after start time");
                return;
            }
            parentPanel.addEvent(new Meeting(name, dateTime, endDateTime, location));
        }

        dispose();
    }

    private LocalDateTime getDateTime(JSpinner dateSpinner, JSpinner timeSpinner) {
        java.util.Date date = (java.util.Date) dateSpinner.getValue();
        java.util.Date time = (java.util.Date) timeSpinner.getValue();

        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime timeOnly = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault());

        return dateTime
                .withHour(timeOnly.getHour())
                .withMinute(timeOnly.getMinute());
    }
}