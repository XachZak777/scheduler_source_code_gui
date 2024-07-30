package am.aua.core;

public abstract class WorkEvent implements Schedulable {
    
    protected final String title;
    
    public String getTitle () { return title; }

    public WorkEvent (String title) { this.title = title; }

    @Override
    public String getShortDescription () { return title; }

    public abstract String getFullDetails ();

    @Override
    public String getFullDescription () { return getFullDetails(); }

    public abstract String toSaveFileString ();
}