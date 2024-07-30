package am.aua.core;

import am.aua.exceptions.MalformedStringParameterException;
import java.util.ArrayList;

public class VideoCall extends WorkEvent implements Cloneable {
    
    private ArrayList<String> participants;

    public VideoCall (String title, String email) throws MalformedStringParameterException {
        super(title);
        if (!isValidEmail(email)) {
            throw new MalformedStringParameterException("Invalid email address.");
        }
        this.participants = new ArrayList<>();
        this.participants.add(email);
    }

    public VideoCall (String data) throws MalformedStringParameterException {
        super(parseTitle(data));
        String[] tokens = data.split("%%");
        if (tokens.length < 3) {
            throw new MalformedStringParameterException("Invalid number of tokens for VideoCall.");
        }
        this.participants = new ArrayList<>();
        for (int i = 2; i < tokens.length; i++) {
            if (!isValidEmail(tokens[i])) {
                throw new MalformedStringParameterException("Invalid email address.");
            }
            this.participants.add(tokens[i]);
        }
        if (this.participants.isEmpty()) {
            throw new MalformedStringParameterException("VideoCall must have at least one participant.");
        }
    }

    private static String parseTitle (String data) throws MalformedStringParameterException {
        String[] tokens = data.split("%%");
        if (tokens.length < 2) {
            throw new MalformedStringParameterException("Invalid number of tokens for VideoCall.");
        }
        return tokens[1];
    }

    private boolean isValidEmail (String email) {
        return email.contains("@");
    }

    public void addParticipant (String email) throws MalformedStringParameterException {
        if (!isValidEmail(email)) {
            throw new MalformedStringParameterException("Invalid email address.");
        }
        participants.add(email);
    }

    public void removeParticipant (String email) throws MalformedStringParameterException {
        if (participants.size() == 1) {
            throw new MalformedStringParameterException("Cannot remove the last participant.");
        }
        participants.remove(email);
    }

    @Override
    public String getFullDetails () {
        return getTitle() + " with participants: " + String.join(", ", participants);
    }

    @Override
    public String toSaveFileString () {
        return "VIDEOCALL%%" + getTitle() + "%%" + String.join("%%", participants);
    }

    @Override
    public VideoCall clone () {
        try {
            VideoCall cloned = (VideoCall) super.clone();
            cloned.participants = new ArrayList<>(this.participants);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should never happen
        }
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VideoCall videoCall = (VideoCall) obj;
        return getTitle().equals(videoCall.getTitle()) &&
                participants.equals(videoCall.participants);
    }
}