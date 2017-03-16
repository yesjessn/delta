package phoenix.delta;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Procedure implements Serializable{
    private int lastSessionID;
    private long lastSessionPrerewardDelay;
    public boolean lastSessionAllNow;
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
                String allNow = parts[8];
                Log.d("prg", "line " + line + "\n= " + sessionID + ", " + prerewardDelay);
                lastSessionID = Integer.parseInt(sessionID);
                lastSessionPrerewardDelay = Long.parseLong(prerewardDelay);
                lastSessionAllNow = Boolean.parseBoolean(allNow);
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
        String header = "";
        String deviceName = Settings.System.getString(context.getContentResolver(), "device_name");
        if (!fh.fileExists(context, subjectID, fileLocation))
        {
            header += "session_id, final_wait_time, selected_game, school, ra, date_time, comments, device_name, all_now_flag\n";
        }
        List<String> rowData = new ArrayList<>();
        rowData.add(String.valueOf(currentSession.sessionID));
        rowData.add(String.valueOf(currentSession.waitTime.getPrerewardDelay()));
        rowData.add(currentSession.selectedGame.getSimpleName());
        rowData.add(this.school);
        rowData.add(this.RAID);
        rowData.add(this.dateString);
        rowData.add(StringEscapeUtils.escapeCsv(currentSession.comments));
        rowData.add(deviceName);
        rowData.add(String.valueOf(currentSession.allBlockNow()));

        fh.fileAppender(context, subjectID, fileLocation, header + StringUtils.join(rowData, ",") + "\n");
    }
}
