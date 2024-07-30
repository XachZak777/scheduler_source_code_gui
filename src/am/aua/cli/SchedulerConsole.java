package am.aua.cli;

import am.aua.core.*;
import am.aua.exceptions.MalformedStringParameterException;
import am.aua.utils.FileUtil;

import java.util.Scanner;

public class SchedulerConsole {

    public static void start () throws MalformedStringParameterException {
        Scanner scanner = new Scanner(System.in);
        Workweek workweek = new Workweek();
        boolean running = true;

        while (running) {
            printSchedule(workweek);
            printMenu();

            int choice = getIntInput(scanner, "Enter your choice: ", 1, 6);
            switch (choice) {
                case 1:
                    addEvent(scanner, workweek);
                    break;
                case 2:
                    removeEvent(scanner, workweek);
                    break;
                case 3:
                    printDetails(scanner, workweek);
                    break;
                case 4:
                    workweek = loadSchedule(scanner);
                    break;
                case 5:
                    saveSchedule(scanner, workweek);
                    break;
                case 6:
                    running = false;
                    break;
            }
        }
    }

    private static void printSchedule (Workweek workweek) {
        for (Days day : Days.values()) {
            for (Times time : Times.values()) {
                System.out.println(day + " " + time + ": " + workweek.getTitleAt(day, time));
            }
        }
    }

    private static void printMenu () {
        System.out.println("1. Add an event.");
        System.out.println("2. Remove an event.");
        System.out.println("3. Print details.");
        System.out.println("4. Load schedule from file.");
        System.out.println("5. Save schedule to file.");
        System.out.println("6. Quit.");
    }

    private static void addEvent (Scanner scanner, Workweek workweek) throws MalformedStringParameterException {
        System.out.println("Enter event type (VIDEOCALL or MEETING): ");
        String eventType = scanner.nextLine().toUpperCase();

        System.out.println("Enter title: ");
        String title = scanner.nextLine();

        Schedulable event = null;

        if (eventType.equals("VIDEOCALL")) {
            System.out.println("Enter email: ");
            String email = scanner.nextLine();
            event = new VideoCall(title, email);
        } else if (eventType.equals("MEETING")) {
            System.out.println("Enter latitude: ");
            double latitude = scanner.nextDouble();
            System.out.println("Enter longitude: ");
            double longitude = scanner.nextDouble();
            event = new Meeting(title, latitude, longitude);
            scanner.nextLine(); // Consume newline
        }

        if (event != null) {
            addEventToSchedule(scanner, workweek, event);
        } else {
            System.out.println("Invalid event type.");
        }
    }

    private static void addEventToSchedule (Scanner scanner, Workweek workweek, Schedulable event) {
        System.out.println("Enter day (MONDAY to FRIDAY): ");
        Days day = Days.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Enter time (MORNING or AFTERNOON): ");
        Times time = Times.valueOf(scanner.nextLine().toUpperCase());

        workweek.addToSchedule(event, day, time);
    }

    private static void removeEvent (Scanner scanner, Workweek workweek) {
        System.out.println("Enter day (MONDAY to FRIDAY): ");
        Days day = Days.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Enter time (MORNING or AFTERNOON): ");
        Times time = Times.valueOf(scanner.nextLine().toUpperCase());

        workweek.removeFromSchedule(day, time);
    }

    private static void printDetails (Scanner scanner, Workweek workweek) {
        System.out.println("Enter day (MONDAY to FRIDAY): ");
        Days day = Days.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Enter time (MORNING or AFTERNOON): ");
        Times time = Times.valueOf(scanner.nextLine().toUpperCase());

        System.out.println(workweek.getFullDetailsAt(day, time));
    }

    private static Workweek loadSchedule (Scanner scanner) {
        System.out.println("Enter file path (or press Enter for default path): ");
        String path = scanner.nextLine();
        if (path.isEmpty()) {
            path = "default_schedule.txt";
        }

        try {
            String[] eventStrings = FileUtil.loadStringsFromFile(path);
            return Workweek.generateWorkweekFromStrings(eventStrings);
        } catch (Exception e) {
            System.out.println("Error loading schedule: " + e.getMessage());
            return new Workweek();
        }
    }

    private static void saveSchedule (Scanner scanner, Workweek workweek) {
        System.out.println("Enter file path (or press Enter for default path): ");
        String path = scanner.nextLine();
        if (path.isEmpty()) {
            path = "default_schedule.txt";
        }

        try {
            String[] eventStrings = new String[10]; // Maximum 5 days * 2 time slots = 10
            int index = 0;

            for (Days day : Days.values()) {
                for (Times time : Times.values()) {
                    Schedulable event = workweek.getEventAt(day, time);
                    if (event != null) {
                        eventStrings[index++] = ((WorkEvent) event).toSaveFileString();
                    }
                }
            }

            FileUtil.saveStringsToFile(eventStrings, path);
            System.out.println("Schedule saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving schedule: " + e.getMessage());
        }
    }

    private static int getIntInput (Scanner scanner, String prompt, int min, int max) {
        int choice;
        while (true) {
            System.out.print(prompt);
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    break;
                }
            } catch (NumberFormatException e) {}
            System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
        }
        return choice;
    }
}