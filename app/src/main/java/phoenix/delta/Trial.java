package phoenix.delta;

import java.io.Serializable;

public class Trial implements Serializable {

    // trial duration time
    private int trialTime;
    // trial data
    private double responseTime;
    private ScheduleChoice choice;// true if choose instant game access,
                                // false if choose to wait before game

    // constructor
    public Trial(int trialTime) {
        this.trialTime = trialTime;
    }
    public Trial() {}

    public ScheduleChoice getChoice() {return choice;}

    // set response time
    public void setResponseTime(double responseTime) {this.responseTime = responseTime;}

    // set participant's choice
    public void setChoice(ScheduleChoice choice) {this.choice = choice;} // play now
    public int getWaitTime (int gameTime) {return (this.trialTime - gameTime);}
    public void setTrialTime(int time) {this.trialTime = time;}
    public String toString () {
        return trialTime + ","
                + responseTime + ","
                + ((choice == ScheduleChoice.INSTANT_GAME_ACCESS)? "INSTANT":"DELAY");
    }
}

/*
* trial time = 10
* game time wait= 5    wait time = 5    (15)
* game time now = 2    wait time = 8    (18)
* */