package am.aua.ui;

import am.aua.core.*;
import am.aua.exceptions.MalformedStringParameterException;
import am.aua.utils.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SchedulerUI extends JFrame {
    private Workweek workweek;
    private JTextArea textArea;
    private JTextField titleField, emailField, latitudeField, longitudeField;

    public SchedulerUI() {
        workweek = new Workweek(); // Initialize Workweek or load it from a file

        setTitle("Scheduler GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create text area for displaying schedule details
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Create panel for user input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 5, 5));

        // Title input
        inputPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        inputPanel.add(titleField);

        // Email input
        inputPanel.add(new JLabel("Email (for VideoCall):"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        // Latitude input
        inputPanel.add(new JLabel("Latitude (for Meeting):"));
        latitudeField = new JTextField();
        inputPanel.add(latitudeField);

        // Longitude input
        inputPanel.add(new JLabel("Longitude (for Meeting):"));
        longitudeField = new JTextField();
        inputPanel.add(longitudeField);

        add(inputPanel, BorderLayout.NORTH);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Event");
        JButton removeButton = new JButton("Remove Event");
        JButton printButton = new JButton("Print Details");
        JButton loadButton = new JButton("Load Schedule");
        JButton saveButton = new JButton("Save Schedule");
        JButton quitButton = new JButton("Quit");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addEvent();
                } catch (MalformedStringParameterException e1) {
                    e1.printStackTrace();
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEvent();
            }
        });

        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printDetails();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadSchedule();
                } catch (MalformedStringParameterException e1) {
                    e1.printStackTrace();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSchedule();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(printButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addEvent() throws MalformedStringParameterException {
        String title = titleField.getText();
        String email = emailField.getText();
        String latitude = latitudeField.getText();
        String longitude = longitudeField.getText();

        // Implement logic to create and add events based on the input fields
        // For example:
        if (!email.isEmpty()) {
            // Assume creating a VideoCall event
            WorkEvent event = new VideoCall(title, email);
            workweek.addToSchedule(event, Days.MONDAY, Times.MORNING); // Example values
        } else if (!latitude.isEmpty() && !longitude.isEmpty()) {
            // Assume creating a Meeting event
            try {
                double lat = Double.parseDouble(latitude);
                double lon = Double.parseDouble(longitude);
                WorkEvent event = new Meeting(title, lat, lon);
                workweek.addToSchedule(event, Days.MONDAY, Times.MORNING); // Example values
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid latitude or longitude.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please provide either an email or latitude and longitude.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeEvent() {
        // Implement logic to remove an event from the schedule
        String title = titleField.getText();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide the title of the event to remove.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Example values
        workweek.removeFromSchedule(Days.MONDAY, Times.MORNING);
    }

    private void printDetails() {
        // Display the details of the schedule
        textArea.setText(workweek.getFullDetails());
    }

    private void loadSchedule() throws MalformedStringParameterException {
        // Implement logic to load a schedule from a file
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                String[] content = FileUtil.loadStringsFromFile(file.getPath());
                workweek = Workweek.generateWorkweekFromStrings(content);
                JOptionPane.showMessageDialog(this, "Schedule loaded successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to load the schedule.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveSchedule() {
        // Implement logic to save the schedule to a file
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                String[] content = workweek.toSaveFileStrings(); // Implement this method in Workweek
                FileUtil.saveStringsToFile(content, file.getPath());
                JOptionPane.showMessageDialog(this, "Schedule saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save the schedule.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showUI() {
        setVisible(true);
    }
}