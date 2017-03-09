package phoenix.delta;


import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;

public class FileHandling extends Activity
{
    public boolean fileExists (Context p_context,String subjectID, String p_filename)
    {
        File rootDir=new File(p_context.getFilesDir(), subjectID);
        rootDir.mkdirs();
        File f = new File(rootDir, p_filename);
        return f.exists();
    }

    public boolean fileWriter (Context p_context,String subjectID, String p_filename, String p_content)
    {
        try
        {
            File rootDir=new File(p_context.getFilesDir(), subjectID);
            rootDir.mkdirs();
            FileOutputStream fos = new FileOutputStream(new File(rootDir, p_filename));
            fos.write(p_content.getBytes());
            fos.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean fileAppender (Context p_context,String subjectID, String p_filename, String p_content)
    {
        try
        {
            File rootDir=new File(p_context.getFilesDir(), subjectID);
            rootDir.mkdirs();
            FileOutputStream fos = new FileOutputStream(new File(rootDir, p_filename), true);
            fos.write(p_content.getBytes());
            fos.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
