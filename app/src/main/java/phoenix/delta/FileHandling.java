package phoenix.delta;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class FileHandling extends Activity{

    private String passwordFilename = "password.csv";

    // if succeed, return true
    // if fail, return false
    public boolean fileWriter (Context context, String filename, String content) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
            return true;
            /*
            //FileOutputStream fileout = openFileOutput(filename, MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(openFileOutput(filename, MODE_PRIVATE));
            for (int i = 0; i < rows.size(); i++)
                outputWriter.write(rows.get(i) + "\n");
            outputWriter.close();

            //display file saved message
            //Toast.makeText(getBaseContext(), "File saved successfully!", Toast.LENGTH_SHORT).show();
            return true;*/
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // if succeed, rows != null
    // if fail, rows == null

    /*
    public ArrayList<String> fileReader (String filename) {
        ArrayList<String> rows = new ArrayList<> ();
        try {

            //FileInputStream fileIn=openFileInput(filename);
            System.out.println(filename);
            FileInputStream fis = openFileInput("randomcrap.txt");
            InputStreamReader inputRead= new InputStreamReader(fis);
            //BufferedReader buffRead= new BufferedReader(inputRead);

            String s="";
            int charRead;
            //String line = "";

            /*
            while((line = buffRead.readLine()) != null) {
                rows.add(line);
            }


            while((charRead = inputRead.read()) != -1) { // EOF
                char c = (char) charRead;
                if(c == '\n') {
                    rows.add(s);
                    s = "";
                }
                else
                    s += c;
            }
            if(s != "")
                rows.add(s);


            /*
            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }

            //buffRead.close();
            inputRead.close();

        } catch (Exception e) {
            rows = null;
            e.printStackTrace();
        }

        return rows;
    }

    public boolean checkUserExist(String tuple) {

        ArrayList<String> allLogin = fileReader(passwordFilename);
        boolean found = false;

        // parse the raw data

        for (int i = 0; allLogin != null && i < allLogin.size() && !found; i++) {
            String user = allLogin.get(i);
            if(user.compareTo(tuple) == 0)
                found = true;
        }
        return found;
    }

    public boolean addUser(String tuple) {
        ArrayList<String> allLogin = fileReader(passwordFilename);
        if(allLogin == null)
            allLogin.add(tuple);
        else
            return false;
        return fileWriter(allLogin, passwordFilename);
    }*/

}
