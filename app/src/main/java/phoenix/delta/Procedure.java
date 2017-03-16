package phoenix.delta;

import android.content.Context;
import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class Procedure implements Serializable{
    private int lastSessionID;
    private long lastSessionPrerewardDelay;
    public Session currentSession;

    public String subjectID;
    private String school;
    private String RAID;
    private String dateString;

    public Procedure(Context context, String subjectID, String school, String RAID, String dateString) {
        this.subjectID = subjectID;
        this.school = school;
        this.RAID = RAID;
        this.dateString = dateString;
        File subjectFile = context.getFileStreamPath(subjectID);
        if (!subjectFile.exists())
        {
            subjectFile.mkdir();
        }
        try {
            File progressFile = new File(subjectFile, subjectID + "-progress.csv");
            FileInputStream fis = new FileInputStream(progressFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            Log.d("prg", "Reading " + progressFile.getPath());
            reader.readLine(); // skip the header row
            String line = reader.readLine();
            while(line != null){
                String[] parts = line.split(",");
                String sessionID = parts[0];
                String prerewardDelay = parts[1];
                Log.d("prg", "line " + line + "\n= " + sessionID + ", " + prerewardDelay);
                lastSessionID = Integer.parseInt(sessionID);
                lastSessionPrerewardDelay = Long.parseLong(prerewardDelay);
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            //first session, leave
            lastSessionID = -1;
        } catch (IOException e) {
            Log.i("prg", "", e);
            lastSessionID = -1;
        }

    }

    public boolean startNewSession()
    {
        if (currentSession != null)
        {
            return false;
        }
        else
        {
            if (lastSessionID == -1)
            {
                currentSession = new Session(0);
                currentSession.initDelay = 1*1000L;
                currentSession.sessionType = SessionType.ESTABLISH_INDIFFERENCE;
            }
            else
            {
                currentSession = new Session(lastSessionID + 1);
                currentSession.initDelay = lastSessionPrerewardDelay;
                if (currentSession.sessionID <= 25)  {
                    currentSession.sessionType = SessionType.SHAPING;
                }
                else
                {
                    currentSession.sessionType = SessionType.ESTABLISH_INDIFFERENCE;
                }
            }
        }
        return true;
    }

    public boolean endSession(Context context)
    {
        if(currentSession != null)
        {
            writeSessionToFile(context, currentSession);
            writeProgressToFile(context, currentSession);
            lastSessionID = currentSession.sessionID;
            lastSessionPrerewardDelay = currentSession.waitTime.getPrerewardDelay();
            currentSession = null;

            return true;
        }
        return false;
    }

    private void writeSessionToFile(Context context, Session currentSession) {
        String fileLocation = subjectID + "-" + currentSession.sessionID + ".csv";
        FileHandling fh = new FileHandling();
        fh.fileWriter(context, subjectID, fileLocation, currentSession.fileContents());
    }

    private void writeProgressToFile (Context context, Session currentSession){
        String fileLocation = subjectID + "-progress.csv";
        FileHandling fh = new FileHandling();
        String content = "";
        if (!fh.fileExists(context, subjectID, fileLocation))
        {
            content += "session_id, final_wait_time, selected_game, school, ra, date_time, comments" + "\n";
        }
        fh.fileAppender(context, subjectID, fileLocation, content + currentSession.sessionID + "," + currentSession.waitTime.getPrerewardDelay() + "," + currentSession.selectedGame.getSimpleName() + "," + this.school + "," + this.RAID + "," + this.dateString + "," + StringEscapeUtils.escapeCsv(currentSession.comments) + "\n");
    }
}
