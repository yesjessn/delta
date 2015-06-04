////////////////////
// File:  DFile.java
// Class: DFile
//
// Description:
//	DFile is an abstraction of a csv file.
//	Other classes extend DFile to tailor the functions of DFile
//	to specific file types.
//
// Public methods:
//	String toString()
//		outputs contents of a file in CSV format.
//	boolean readFile(String filename)
//		reads file specified by filename into DFile.
//		True if operation was successful.
//	boolean writeFile(String filename)
//		writes DFile into file specified by filename
//		True if operation was successful.
//

package phoenix.delta;
import android.content.Context;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public abstract class DFile extends Context{
    private List<Entry> entries;

    public DFile() {
        entries = new ArrayList<Entry>();
    } // public DFile()

    ////////////////////////
    public String toString() {
        // returns contents that can be written to a csv file
        // The format in EBNF is
        //   toString() := {entry}
        //   entry      := [field , {',' , field} , '\n']
        //   field      := String

        // sort the entries before converting to string
        Collections.sort(entries, new Comparator<Entry>() {
            public int compare(Entry e1, Entry e2) {
                return e1.compareTo(e2);
            } // public int compare(Entry e1, Entry e2)
        });

        // output the entries as csv file
        StringBuilder sb = new StringBuilder();
        for (Entry e : entries)
            sb.append(e.toString());
        return sb.toString();
    } // public String toString()

    ////////////////////////////////////////
    public boolean readFile(String filename) {
        // reads the contents of a file specified by filename into this object.
        // The function does not clear the list before reading, meaning that
        // it can be used with writeFile() to append entries to a file.
        // It returns true if successful, false otherwise.

        //Context         context;
        String          buffer;
        String[]        entries;
        FileInputStream fstream;
        DataInputStream in;
        InputStreamReader InputRead;
        BufferedReader buffReader;

        int read_line_size = 256;

        try {

            //context = getApplicationContext();
            fstream = openFileInput(filename);
            in = new DataInputStream(fstream);
            InputRead = new InputStreamReader(in);
            buffReader = new BufferedReader(InputRead);

            //char[] inputBuffer= new char[read_line_size];
            String line = "";
            //int charRead;

            while ((line = buffReader.readLine()) != null) {
                String[] tokens = line.split(",");
                this.addEntry(tokens);
            }

            //buffer = fis.toString();

            /*/entries = buffer.split("\n");
            for (String entry : entries) {
                String[] tokens = entry.split(",");
                this.addEntry(tokens);
            }*/

            //fis.close();
            in.close();
            fstream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }

        return false;
    } // public boolean readFile(String filename)

    /////////////////////////////////////////
    public boolean writeFile(String filename) {
        // write the contents of this object to a file specified by filename.
        // If the file already exists, this function will overwrite it.
        // It returns true if successful, false otherwise.

        FileOutputStream outputStream;
        OutputStreamWriter outWriter;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outWriter = new OutputStreamWriter(outputStream);
            outWriter.write(this.toString());
            outWriter.close();
            return true;
        } catch (Exception e) {

            e.printStackTrace();
        } // ensure that file read succeeds

        return false;
    } // public boolean writeFile(String filename)

    ////////////////////////////////////////
    protected void addEntry(String[] sArray) {
        // adds an entry to the list, preventing duplicate entries from occuring.
        // Subclasses will use this method to add entries
        // of a format that defined by the subclass.
        // get rid of commas that may be in sArray
        for (String s : sArray) {
            s.replaceAll(",", "");
        } // get rid of commas

        // add entry
        Entry e = new Entry(sArray);
        if (!entries.contains(e))
            entries.add(e);
    } // protected void addEntry(String[] sArray)

    ////////////////////////////////////////////
    protected boolean findEntry(String[] sArray) {
        // checks whether a certain entry exists.
        // It returns true if the entry exist, and false otherwise.
        // get rid of commas that may be in sArray
        for (String s : sArray) {
            s.replaceAll(",", "");
        } // get rid of commas

        // search entry
        Entry e = new Entry(sArray);
        return entries.contains(e);
    } // protected boolean findEntry(String[] sArray)

    ///////////////////
    private class Entry {
        // defines a row in a csv file.
        private List<String> fields;

        public Entry(String[] sArray) {
            fields = new ArrayList<String>();
            for (String s: sArray)
                fields.add(s);
        } // public Entry(String[] sArray)

        /////////////////////////////
        public int compareTo(Entry e) {
            // compares two entries by analyzing their string form.
            // The string form is the canonical form of an Entry object.
            // This function is used to sort a list of Entry objects.
            return this.toString().compareTo(e.toString());
        } // public int compareTo(Entry e)

        ///////////////////////////////
        public boolean equals(Object o) {
            // returns whether two entries are the same.
            // This function prevents duplicate entries from being added.
            return this.toString().equals(((Entry) o).toString());
        } // public boolean equals(Object o)

        ////////////////////////
        public String toString() {
            // returns a string in the following format in EBNF
            //   toString() := [field , {',' , field} , '\n']
            //   field      := String
            StringBuilder sb = new StringBuilder();
            for (String s : fields) {
                sb.append(s);
                sb.append(',');
            } // output as row of csv file

            // turn last comma into newline
            if (sb.length() > 0)
                sb.setCharAt(sb.length()-1, '\n');

            return sb.toString();
        } // public String toString()
    } // private class Entry

} // public abstract class DFile