package phoenix.delta;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Tiffany Chan on 3/1/2015.
 */
public class Session implements Serializable {

    private String studID;
    private int numTrial,
                initTrialDurationTime,
                timeIncAmount,
                numTrialPerInc;
    private LinkedList<Trial> trials;

    public Session() {
        // initialize
        this.trials = new LinkedList<>();
        this.studID = "";
        // set default values
        this.numTrial = 5;
        this.initTrialDurationTime = 5;
        this.timeIncAmount = 0;
        this.numTrialPerInc = 0;

    }

    public void setStudent (String stud) {this.studID = stud;}

    public void changeSetting (int numTrial, int initTrialDurationTime,
                               int timeIncAmount, int numTrialPerInc) {
        this.numTrial = numTrial;
        this.initTrialDurationTime = initTrialDurationTime;
        this.timeIncAmount = timeIncAmount;
        this.numTrialPerInc = numTrialPerInc;
    }

    public void startSession () {

    }

    public void addTrial (Trial newTrial) {trials.add(newTrial);}

    public String toString () {return studID;}
}
