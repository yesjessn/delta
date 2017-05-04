package phoenix.delta;

import android.app.Activity;
import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Session implements Serializable
{
    public int sessionID;

    public String comments;

    private Trial m_currentTrial;

    public Block currentBlock;
    private ArrayList<Block> completedBlocks;

    private ArrayList<Trial> m_trials;
    private boolean m_showTimer;

    public SessionType sessionType;

    public WaitTime waitTime;

    public long initDelay = 225*100L;

    public Class<? extends Activity> selectedGame;

    // constructor
    public Session(int sessionID)
    {
        completedBlocks = new ArrayList<>();
        m_trials = new ArrayList<>();
        this.sessionID = sessionID;
        m_showTimer = false;
        waitTime = new WaitTime(this);
    }

    public ArrayList<Block> getCompletedBlocks() { return completedBlocks;}

    public ArrayList<Trial> getAllTrials()
    {
        return m_trials;
    }

    public boolean isSessionDone()
    {
        return (completedBlocks.size() == 6);
    }

    public void resetSession()
    {
        currentBlock = null;
        m_currentTrial = null;
        completedBlocks.clear();
        m_trials.clear();
    }


    /*********************************************************************************
        setStudentSelection: set student's selection, wait or play instantly
        in:  INSTANT_GAME_ACCESS or WAIT_FOR_GAME
        out: NOTHING
     */
    public void setStudentSelection(ScheduleChoice p_choice)
    {
        m_currentTrial.setChoice(p_choice);
    }
    /*********************************************************************************
        setStudentResponseTime: set student's response time
        in:  response time
        out: NOTHING
     */
    public void setStudentResponseTime(double p_time)
    {
        m_currentTrial.setResponseTime(p_time);
    }


    /*********************************************************************************
        startNewTrial: set a new trial as the current trial
        in:  newTrial = the new trail that is about to start next
        out: return true if newTrial is set to current successfully
             return false if newTrial is null OR if current trial is not finished
     */
    public boolean startNewTrial ()
    {
        if( m_currentTrial != null)
        {
            return false;
        }
        else
        {
            if (currentBlock == null) {
                currentBlock = new Block(completedBlocks.size()+1, sessionType);
            }
            int completedTrials = this.currentBlock.trials.size();
            TrialType trialType;
            if (completedTrials> 1)
            {
                trialType = TrialType.FREE_CHOICE;
            }
            else
            {
                trialType = TrialType.FORCED_CHOICE;
            }
            m_currentTrial = new Trial(trialType,waitTime.getPrerewardDelay());
            return true;
        }
    }

    /*********************************************************************************
        endTrial: mark the end of a trial (save trial in the sessionType's )
        in:  NOTHING
        out: return true if current trial is not null and added to the storing list
             return false if current trial is null
     */
    public boolean endTrial()
    {
        if(m_currentTrial != null)
        {
            m_trials.add(m_currentTrial);
            currentBlock.add(m_currentTrial);
            m_currentTrial = null;

            if(currentBlock.isComplete()) {
                completedBlocks.add(currentBlock);
                currentBlock = null;
            }

            return true;
        }
        return false;
    }

    public boolean allBlockNow() {
        for (Block b : completedBlocks) {
            if (!b.allNow()) {
                return false;
            }
        }
        return true;
    }

    public String fileContents()
    {
        String content = "";
        content += "trial_type, prereward_delay, response_time, choice" + "\n";
        for(Trial t : m_trials)
        {
            content += t.toString() + "\n";
        }
        return content;
    }

    public ScheduleChoice getCurrTrialChoice()
    {
        return m_currentTrial.getChoice();
    }

    public void setTimerVisible()
    {
        m_showTimer = true;
    }

    public void setTimerInvisible()
    {
        m_showTimer = false;
    }

    public boolean isTimerVisible()
    {
        return m_showTimer;
    }

    public boolean isTrialNull()
    {
        return (m_currentTrial == null);
    }

    public boolean isNewSession()
    {
        return m_trials.size() == 0;
    }
}
