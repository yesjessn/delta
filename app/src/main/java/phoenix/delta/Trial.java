package phoenix.delta;

import java.io.Serializable;

public class Trial implements Serializable {

    private TrialType trialType;
    private long prerewardDelay;
    private double responseTime;
    private ScheduleChoice choice;

    // constructor
    public Trial(TrialType trialType, long prerewardDelay) {
        this.trialType = trialType;
        this.prerewardDelay = prerewardDelay;
    }


    public ScheduleChoice getChoice() {return choice;}


    // set response time
    public void setResponseTime(double responseTime) {this.responseTime = responseTime;}

    // set participant's choice
    public void setChoice(ScheduleChoice choice) {this.choice = choice;} // play now
    public String toString () {

        return  trialType + ","
                + prerewardDelay + ","
                + responseTime + ","
                + choice;
    }
}
