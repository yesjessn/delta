package phoenix.delta;


import android.app.Activity;
import android.content.Context;

import java.io.FileOutputStream;

public class FileHandling extends Activity
{
    public boolean fileWriter (Context p_context, String p_filename, String p_content)
    {
        try
        {
            FileOutputStream fos = p_context.openFileOutput(p_filename, MODE_PRIVATE);
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
