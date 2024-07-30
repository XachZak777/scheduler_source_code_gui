package am.aua.core;

import am.aua.exceptions.MalformedStringParameterException;

public class Workweek implements Comparable<Workweek> {

    private final Schedulable[][] schedule;

    public Workweek () {
        schedule = new Schedulable[5][2]; // 5 days, 2 time slots per day
    }

    public void addToSchedule (Schedulable event, Days day, Times time) {
        schedule[day.ordinal()][time.ordinal()] = event;
    }

    public void removeFromSchedule (Days day, Times time) {
        schedule[day.ordinal()][time.ordinal()] = null;
    }

    public boolean isEmpty (Days day, Times time) {
        return schedule[day.ordinal()][time.ordinal()] == null;
    }

    public String getTitleAt (Days day, Times time) {
        Schedulable event = schedule[day.ordinal()][time.ordinal()];
        return event == null ? "" : event.getShortDescription();
    }

    public String getFullDetailsAt (Days day, Times time) {
        Schedulable event = schedule[day.ordinal()][time.ordinal()];
        return event == null ? "" : event.getFullDescription();
    }

    public Schedulable getEventAt (Days day, Times time) {
        return schedule[day.ordinal()][time.ordinal()];
    }

    public static Workweek generateWorkweekFromStrings (String[] eventStrings) throws MalformedStringParameterException {
        Workweek workweek = new Workweek();
        for (String eventString : eventStrings) {
            String[] tokens = eventString.split("%%");
            String type = tokens[0];
            String title = tokens[1];
            Days day = Days.valueOf(tokens[2]);
            Times time = Times.valueOf(tokens[3]);
            switch (type) {
                case "VIDEOCALL":
                    workweek.addToSchedule(new VideoCall(title, tokens[4]), day, time);
                    break;
                case "MEETING":
                    workweek.addToSchedule(new Meeting(title, Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5])), day, time);
                    break;
                default:
                    throw new MalformedStringParameterException("Invalid type: " + type);
            }
        }
        return workweek;
    }

    private int getWorkload () {
        int workload = 0;
        for (Days day : Days.values()) {
            for (Times time : Times.values()) {
                if (!isEmpty(day, time)) {
                    workload++;
                }
            }
        }
        return workload;
    }

    @Override
    public int compareTo (Workweek other) {
        return Integer.compare(this.getWorkload(), other.getWorkload());
    }

    public static Workweek getLeastBusyWorkweek (Workweek[] workweeks) {
        if (workweeks == null || workweeks.length == 0) {
            return null; // Or throw an exception based on your requirement
        }
        Workweek leastBusy = workweeks[0];
        for (int i = 1; i < workweeks.length; i++) {
            if (workweeks[i].compareTo(leastBusy) < 0) {
                leastBusy = workweeks[i];
            }
        }
        return leastBusy;
    }

    public String getFullDetails () {
        StringBuilder details = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                WorkEvent event = (WorkEvent) schedule[i][j];
                if (event != null) {
                    details.append(event.getFullDetails()).append("\n");
                }
            }
        }
        return details.toString();
    }

    public String[] toSaveFileStrings() {
        // Convert Workweek to array of strings for saving
        String[] lines = new String[50]; // Assuming max 50 events
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                WorkEvent event = (WorkEvent) schedule[i][j];
                if (event != null) {
                    lines[index++] = event.toSaveFileString();
                }
            }
        }
        return lines;
    }
}