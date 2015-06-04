package phoenix.delta;

import android.content.Context;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by Tiffany Chan on 3/1/2015.
 */
public class Session implements Serializable {

    public final static int INSTANT_GAME_ACCESS = 0;
    public final static int WAIT_FOR_GAME = 1;

    private String studID;
    private int numTrial,
                initTrialDurationTime,
                timeIncAmount,
                numTrialPerInc,
                gameTimeDelay,
                gameTimeInstant;
    private int currTrialTime;
    private int consecutiveDelayCount;
    private Trial currentTrial;

    private ArrayList<Trial> trials;
    private boolean showTimer;
    private boolean isAdmin;

    // constructor
    public Session(boolean isAdmin) {
        // initialize
        this.trials = new ArrayList<>();
        this.studID = "";
        this.showTimer = false;
        this.isAdmin = isAdmin;
        this.consecutiveDelayCount = 0;
        // set default values
        setDefaultSetting();
        this.currTrialTime = this.initTrialDurationTime;
        this.numTrialPerInc = 3; // hard-code it in!

    }

    public ArrayList<Trial> getAllTrials() {
        return trials;
    }

    public boolean isSessionDone () {return (trials.size() >= numTrial);}
    public void resetSession() {
        currentTrial = null;
        trials.clear();
        studID = "";
        consecutiveDelayCount = 0;
    }

    public void setDefaultSetting () {
        this.numTrial = 10;
        this.initTrialDurationTime = 20;
        this.gameTimeInstant = 5;
        this.gameTimeDelay = 15;
        this.timeIncAmount = 5;
    }

    public String printableTrialSetting () {
        return "Current Trial Setting:"
             + "\nNumber of trials: " + this.numTrial
             + "\nInitial trial duration time (sec): " + this.initTrialDurationTime
             + "\nIncrement trial time by " + this.timeIncAmount + " sec every " + this.numTrialPerInc + " consecutive delay choice(s)."
             + "\nGame Time for Delay Choice (sec): " + this.gameTimeDelay
             + "\nGame Time for Instant Choice (sec): " + this.gameTimeInstant;
    }

    /*********************************************************************************
        setStudent: set the student for this session
        in:  student's code
        out: NOTHING
     */
    public void setStudent (String stud) {this.studID = stud;}

    /*********************************************************************************
        setStudentSelection: set student's selection, wait or play instantly
        in:  INSTANT_GAME_ACCESS or WAIT_FOR_GAME
        out: NOTHING
     */
    public void setStudentSelection(int choice) {
        currentTrial.setChoice(choice);
    }
    /*********************************************************************************
        setStudentResponseTime: set student's response time
        in:  response time
        out: NOTHING
     */
    public void setStudentResponseTime(double time) {
        currentTrial.setResponseTime(time);
    }

    /*********************************************************************************
        changeSetting: change setting for the session
        in:  number of trial, initial trial's duration time, game time if delay is chosen,
             game time if instant is chose, and the wait time increment
        out: NOTHING
     */
    public void changeSetting (int numTrial, int initTrialDurationTime,
                               int gameTimeDelay, int gameTimeInstant, int timeIncAmount) {
        this.numTrial = numTrial;
        this.initTrialDurationTime = initTrialDurationTime;
        this.currTrialTime = initTrialDurationTime;
        this.gameTimeDelay = gameTimeDelay;
        this.gameTimeInstant = gameTimeInstant;
        this.timeIncAmount = timeIncAmount;
    }

    /*********************************************************************************
        startNewTrial: set a new trial as the current trial
        in:  newTrial = the new trail that is about to start next
        out: return true if newTrial is set to current successfully
             return false if newTrial is null OR if current trial is not finished
     */
    public boolean startNewTrial (Trial newTrial) {
        if(newTrial == null || currentTrial != null) {
            return false;
        }
        else {
            if(consecutiveDelayCount == 3) {
                this.currTrialTime += this.timeIncAmount;
                newTrial.setTrialTime(this.currTrialTime);
                consecutiveDelayCount = 0;
            }
            else
                newTrial.setTrialTime(this.currTrialTime);
            currentTrial = newTrial;
            return true;
        }
    }
    public String getStudID () {return studID;}

    /*********************************************************************************
        endTrial: mark the end of a trial (save trial in the session's )
        in:  NOTHING
        out: return true if current trial is not null and added to the storing list
             return false if current trial is null
     */
    public boolean endTrial() {
        if(currentTrial != null) {
            if(currentTrial.getChoice() == WAIT_FOR_GAME)
                consecutiveDelayCount++;
            trials.add(currentTrial);
            currentTrial = null;
            return true;
        }
        return false;
    }

    /*********************************************************************************
        endSession: save all trails to database
        in:  NOTHING
        OUT: return true if save successfully; return false otherwise
     */
    public boolean endSession (Context context) {

        if(currentTrial != null)
            endTrial();

        // save all trials to file
        String filename = generateFilename();
        FileHandling fh = new FileHandling();
        if(fh.fileWriter(context, filename, fileWriteable())) {



            return true;
        }
        else
            return false;

    }

    public String fileWriteable () {
        String content = "";
        for(Trial t : trials)
            content += t.toString() + "\n";
        return content;
    }

    private String generateFilename () {
        String filename = "";

        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DATE);
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);

        // i.e.: at 2015 june 3rd, 6:16:60 pm
        // filename = timmy895_2015-6-3_18-16-60.csv
        filename = studID + "_"
                   + year + "-" + month + "-" + day + "_"
                   + hour + "-" + minute + "-" + second + ".csv";
        return filename;
    }


    /*********************************************************************************
        getNumTrial: get the trial duration time for the next trial
        in:  NOTHING
        OUT: return the number of trials finished

    public int getNextTrialTime(){
        int trialTime = initTrialDurationTime;

        if(numTrialPerInc > 0)
            trialTime += timeIncAmount * (trials.size() / numTrialPerInc);

        return trialTime;
    }*/

    public int getNumTrial() {return this.numTrial;}
    public int getInitTrialDurationTime() {return initTrialDurationTime;}
    public int getTimeIncAmount() {return timeIncAmount;}
    public int getGameTimeDelay() {return gameTimeDelay;}
    public int getGameTimeInstant() {return gameTimeInstant;}
    public int getCurrTrialChoice() {return currentTrial.getChoice();}
    public int getWaitTime () {
        if(currentTrial.getChoice() == WAIT_FOR_GAME)
            return currentTrial.getWaitTime(gameTimeDelay);
        else
            return currentTrial.getWaitTime(gameTimeInstant);
    }

    public void setTimerVisible () {this.showTimer = true;}
    public void setTimerInvisible () {this.showTimer = false;}
    public boolean isTimerVisible () {return this.showTimer;}
    public boolean isTrialNull () {return (currentTrial == null);}
    public boolean isStartedByAdmin () {return this.isAdmin;}
    public boolean isNewSession() {return this.trials.size() == 0;}
}
