package am.aua.core;

public interface Schedulable {

    String getShortDescription ();

    String getFullDescription ();
    
    String toSaveFileString ();
}