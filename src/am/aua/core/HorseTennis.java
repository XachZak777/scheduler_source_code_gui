package am.aua.core;

public class HorseTennis implements Schedulable {
    
    private final Team teamA;
    private final Team teamB;

    public HorseTennis (String horseA, String jockeyA, String horseB, String jockeyB) {
        this.teamA = new Team(horseA, jockeyA);
        this.teamB = new Team(horseB, jockeyB);
    }

    @Override
    public String getShortDescription () { return "Sport match"; }

    @Override
    public String getFullDescription () {
        return "HorseTennis: " +
               teamA.getHorseName() + " - " + teamA.getJockeyName() + " vs. " +
               teamB.getHorseName() + " - " + teamB.getJockeyName();
    }

    @Override
    public String toSaveFileString () { return "HORSETENNIS%%Whatever"; }

    public static class Team {

        private final String horseName;
        private final String jockeyName;

        public Team (String horseName, String jockeyName) {
            if (horseName == null || jockeyName == null || horseName.isEmpty() || jockeyName.isEmpty()) {
                throw new IllegalArgumentException("Horse name and jockey name cannot be null or empty.");
            }
            this.horseName = horseName;
            this.jockeyName = jockeyName;
        }

        public String getHorseName () { return horseName; }

        public String getJockeyName () { return jockeyName; }
    }

    public static class PonyPingPong implements Schedulable {
        
        private final HorseTennis horseTennis;

        public PonyPingPong (HorseTennis horseTennis) { this.horseTennis = horseTennis; }

        @Override
        public String getShortDescription () { return "Sport match"; }

        @Override
        public String getFullDescription () { return horseTennis.getFullDescription(); }

        @Override
        public String toSaveFileString () { return "PONYPINGPONG%%Whatever"; }
    }
}