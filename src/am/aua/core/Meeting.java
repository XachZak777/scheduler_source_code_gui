package am.aua.core;

public class Meeting extends WorkEvent {
    
    private final double latitude;
    private final double longitude;

    public Meeting (String title, double latitude, double longitude) {
        super(title);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getFullDetails () {
        return "Title: " + title + ", Location: (" + latitude + ", " + longitude + ")";
    }

    @Override
    public String toSaveFileString () {
        return "MEETING%%" + title + "%%" + latitude + "%%" + longitude;
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Meeting meeting = (Meeting) obj;
        return title.equals(meeting.title) &&
                Double.compare(meeting.latitude, latitude) == 0 &&
                Double.compare(meeting.longitude, longitude) == 0;
    }
}