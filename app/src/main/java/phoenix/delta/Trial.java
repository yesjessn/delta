package phoenix.delta;

import java.io.Serializable;

/**
 * Created by Tiffany Chan on 3/1/2015.
 */
public class Trial implements Serializable {

    // trial properties
    private int trialNum, trialTime;
    // trial data
    private int responseTime;
    private boolean instantGame;// true if choose instant game access,
                                // false if choose to wait before game

    // constructor
    public Trial(int trialNum, int trialTime) {
        this.trialNum = trialNum;
        this.trialTime = trialTime;
    }

    // set response time
    public void setResponseTime(int responseTime) {this.responseTime = responseTime;}

    // set participant's choice
    public void setInstant() {this.instantGame = true;} // play now
    public void setWait() {this.instantGame = false;}   // wait to play


}
