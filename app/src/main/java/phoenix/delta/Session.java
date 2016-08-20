package phoenix.delta;

import android.app.Activity;
import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Session implements Serializable
{
    private String m_studID;
    private Trial m_currentTrial;

    public Block currentBlock;
    private ArrayList<Block> completedBlocks;

    private ArrayList<Trial> m_trials;
    private boolean m_showTimer;
    private boolean m_isAdmin;

    public ProcedureType procedure;

    public WaitTime waitTime;

    public long initDelay = 45*1000L;
    public long delayChange = 25714L; //25.714 seconds (25714 ms) -> 90 = 45 + DC * [(0.5^0) + (0.5^1) + (0.5^2)]
    public float adjustment = 0.5f;

    public Class<? extends Activity> selectedGame;

    // constructor
    public Session(boolean p_isAdmin)
    {
        completedBlocks = new ArrayList<>();
        m_trials = new ArrayList<>();
        m_studID = "";
        m_showTimer = false;
        m_isAdmin = p_isAdmin;
        waitTime = new WaitTime(this);
        procedure = ProcedureType.ESTABLISH_INDIFFERENCE;
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
        m_studID = "";
    }


    /*********************************************************************************
        setStudent: set the student for this session
        in:  student's code
        out: NOTHING
     */
    public void setStudent (String p_stud)
    {
        m_studID = p_stud;
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
    public boolean startNewTrial (Trial p_newTrial)
    {
        if(p_newTrial == null || m_currentTrial != null)
        {
            return false;
        }
        else
        {
            if (currentBlock == null) {
                currentBlock = new Block(completedBlocks.size()+1, procedure);
            }
            m_currentTrial = p_newTrial;
            return true;
        }
    }

    /*********************************************************************************
        endTrial: mark the end of a trial (save trial in the session's )
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

    /*********************************************************************************
        endSession: save all trails to database
        in:  NOTHING
        OUT: return true if save successfully; return false otherwise
     */
    public boolean endSession (Context context)
    {

        if(m_currentTrial != null)
        {
            endTrial();
        }
        // save all trials to file
        String filename = generateFilename();
        FileHandling fh = new FileHandling();
        return fh.fileWriter(context, filename, fileWriteable());

    }

    public String fileWriteable()
    {
        String content = "";
        for(Trial t : m_trials)
        {
            content += t.toString() + "\n";
        }
        return content;
    }

    private String generateFilename()
    {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DATE);
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);

        // i.e.: at 2015 june 3rd, 6:16:60 pm
        // filename = timmy895_2015-6-3_18-16-60.csv
        return m_studID + "_"
                   + year + "-" + month + "-" + day + "_"
                   + hour + "-" + minute + "-" + second + ".csv";
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

    public boolean isStartedByAdmin()
    {
        return m_isAdmin;
    }

    public boolean isNewSession()
    {
        return m_trials.size() == 0;
    }
}
