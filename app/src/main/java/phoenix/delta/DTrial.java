package phoenix.delta;

/////////////////////
// File:  DTrial.java
// Class: DTrial
//
// Description:
//	DTrial records the decisions made by a student during a trial.
//	It contains no information on the identity
//	of the student -- this is the job of DStudent.
//
//	The csv files created by DTrial have the following format:
//		1: Event ID                     // trial number
//		2: Wait period
//		3: Decision                     // choice
//		4: Elapsed time in milliseconds // response
//	The Event ID is necessary because DFile automatically sorts the rows.
//
// Public methods:
//	void recordDelay(int elapsed)
//		records a delay decision made by student.
//	void recordNow(int elapsed)
//		records a immediate decision made by student.
//	void setWait(int wait)
//		sets the waiting period for the delay decision.
//
// Usage:
//	Sample trial
//	|	DTrial dt = new DTrial();
//	|	dt.setWait(5);
//	|
//	|	// Assume that getTime() is a method
//	|	// that returns the elapsed time
//	|	dt.recordNow  (getTime());
//	|	dt.recordDelay(getTime());
//	|	dt.recordDelay(getTime());
//	|	dt.recordNow  (getTime());
//	|	dt.recordDelay(getTime());
//	|
//	|	dt.writeFile("TK421.csv");
//

import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DTrial extends DFile {
    private int eventId = 0;
    private int waitPeriod = 0;

    @Override
    public FileInputStream openFileInput(String s) throws FileNotFoundException {
        return null;
    }

    @Override
    public FileOutputStream openFileOutput(String s, int i) throws FileNotFoundException {
        return null;
    }

    public DTrial() {
        super();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean readFile(String filename) {
        return super.readFile(filename);
    }

    @Override
    public boolean writeFile(String filename) {
        return super.writeFile(filename);
    }

    @Override
    protected void addEntry(String[] sArray) {
        super.addEntry(sArray);
    }

    @Override
    protected boolean findEntry(String[] sArray) {
        return super.findEntry(sArray);
    }

    @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        super.registerComponentCallbacks(callback);
    }

    @Override
    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        super.unregisterComponentCallbacks(callback);
    }

    @Override
    public boolean isRestricted() {
        return super.isRestricted();
    }

    @Override
    public AssetManager getAssets() {
        return null;
    }

    @Override
    public Resources getResources() {
        return null;
    }

    @Override
    public PackageManager getPackageManager() {
        return null;
    }

    @Override
    public ContentResolver getContentResolver() {
        return null;
    }

    @Override
    public Looper getMainLooper() {
        return null;
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void setTheme(int i) {

    }

    @Override
    public Resources.Theme getTheme() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return null;
    }

    @Override
    public String getPackageResourcePath() {
        return null;
    }

    @Override
    public String getPackageCodePath() {
        return null;
    }

    @Override
    public SharedPreferences getSharedPreferences(String s, int i) {
        return null;
    }

    @Override
    public boolean deleteFile(String s) {
        return false;
    }

    @Override
    public File getFileStreamPath(String s) {
        return null;
    }

    @Override
    public File getFilesDir() {
        return null;
    }

    @Override
    public File getNoBackupFilesDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalFilesDir(String s) {
        return null;
    }

    @Override
    public File[] getExternalFilesDirs(String s) {
        return new File[0];
    }

    @Override
    public File getObbDir() {
        return null;
    }

    @Override
    public File[] getObbDirs() {
        return new File[0];
    }

    @Override
    public File getCacheDir() {
        return null;
    }

    @Override
    public File getCodeCacheDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalCacheDir() {
        return null;
    }

    @Override
    public File[] getExternalCacheDirs() {
        return new File[0];
    }

    @Override
    public File[] getExternalMediaDirs() {
        return new File[0];
    }

    @Override
    public String[] fileList() {
        return new String[0];
    }

    @Override
    public File getDir(String s, int i) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory, DatabaseErrorHandler databaseErrorHandler) {
        return null;
    }

    @Override
    public boolean deleteDatabase(String s) {
        return false;
    }

    @Override
    public File getDatabasePath(String s) {
        return null;
    }

    @Override
    public String[] databaseList() {
        return new String[0];
    }

    @Override
    public Drawable getWallpaper() {
        return null;
    }

    @Override
    public Drawable peekWallpaper() {
        return null;
    }

    @Override
    public int getWallpaperDesiredMinimumWidth() {
        return 0;
    }

    @Override
    public int getWallpaperDesiredMinimumHeight() {
        return 0;
    }

    @Override
    public void setWallpaper(Bitmap bitmap) throws IOException {

    }

    @Override
    public void setWallpaper(InputStream inputStream) throws IOException {

    }

    @Override
    public void clearWallpaper() throws IOException {

    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void startActivity(Intent intent, Bundle bundle) {

    }

    @Override
    public void startActivities(Intent[] intents) {

    }

    @Override
    public void startActivities(Intent[] intents, Bundle bundle) {

    }

    @Override
    public void startIntentSender(IntentSender intentSender, Intent intent, int i, int i1, int i2) throws IntentSender.SendIntentException {

    }

    @Override
    public void startIntentSender(IntentSender intentSender, Intent intent, int i, int i1, int i2, Bundle bundle) throws IntentSender.SendIntentException {

    }

    @Override
    public void sendBroadcast(Intent intent) {

    }

    @Override
    public void sendBroadcast(Intent intent, String s) {

    }

    @Override
    public void sendOrderedBroadcast(Intent intent, String s) {

    }

    @Override
    public void sendOrderedBroadcast(Intent intent, String s, BroadcastReceiver broadcastReceiver, Handler handler, int i, String s1, Bundle bundle) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle userHandle) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle userHandle, String s) {

    }

    @Override
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, String s, BroadcastReceiver broadcastReceiver, Handler handler, int i, String s1, Bundle bundle) {

    }

    @Override
    public void sendStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver broadcastReceiver, Handler handler, int i, String s, Bundle bundle) {

    }

    @Override
    public void removeStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

    }

    @Override
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, BroadcastReceiver broadcastReceiver, Handler handler, int i, String s, Bundle bundle) {

    }

    @Override
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, String s, Handler handler) {
        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {

    }

    @Nullable
    @Override
    public ComponentName startService(Intent intent) {
        return null;
    }

    @Override
    public boolean stopService(Intent intent) {
        return false;
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public boolean startInstrumentation(ComponentName componentName, String s, Bundle bundle) {
        return false;
    }

    @Override
    public Object getSystemService(String s) {
        return null;
    }

    @Override
    public int checkPermission(String s, int i, int i1) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public int checkCallingPermission(String s) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public int checkCallingOrSelfPermission(String s) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void enforcePermission(String s, int i, int i1, String s1) {

    }

    @Override
    public void enforceCallingPermission(String s, String s1) {

    }

    @Override
    public void enforceCallingOrSelfPermission(String s, String s1) {

    }

    @Override
    public void grantUriPermission(String s, Uri uri, int i) {

    }

    @Override
    public void revokeUriPermission(Uri uri, int i) {

    }

    @Override
    public int checkUriPermission(Uri uri, int i, int i1, int i2) {
        return 0;
    }

    @Override
    public int checkCallingUriPermission(Uri uri, int i) {
        return 0;
    }

    @Override
    public int checkCallingOrSelfUriPermission(Uri uri, int i) {
        return 0;
    }

    @Override
    public int checkUriPermission(Uri uri, String s, String s1, int i, int i1, int i2) {
        return 0;
    }

    @Override
    public void enforceUriPermission(Uri uri, int i, int i1, int i2, String s) {

    }

    @Override
    public void enforceCallingUriPermission(Uri uri, int i, String s) {

    }

    @Override
    public void enforceCallingOrSelfUriPermission(Uri uri, int i, String s) {

    }

    @Override
    public void enforceUriPermission(Uri uri, String s, String s1, int i, int i1, int i2, String s2) {

    }

    @Override
    public Context createPackageContext(String s, int i) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createConfigurationContext(Configuration configuration) {
        return null;
    }

    @Override
    public Context createDisplayContext(Display display) {
        return null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    ////////////////////////////////////
    public void recordDelay(int elapsed) {
        // adds a tuple of the form (eventId, waitPeriod, "DELAY", elapsed).
        String[] entry = {idToString(), String.valueOf(waitPeriod),
                "DELAY", String.valueOf(elapsed)};
        this.addEntry(entry);
    } // public void recordDelay(int elapsed)

    //////////////////////////////////
    public void recordNow(int elapsed) {
        // adds a tuple of the form (eventId, waitPeriod, "NOW").
        String[] entry = {idToString(), String.valueOf(waitPeriod),
                "NOW", String.valueOf(elapsed)};
        this.addEntry(entry);
    } // public void recordNow(int elapsed)

    /////////////////////////////
    public void setWait(int wait) {
        // sets the waiting period to be recorded
        // into the database.
        this.waitPeriod = wait;
    } // public void setWait(int wait)

    ///////////////////////////
    private String idToString() {
        // returns a string form of the eventId.
        // The number is padded with zeros to prevent sorting issues.
        // For example, "14" would be sorted before "2",
        // but "02" would be sorted before "14".
        return String.format("%03d", eventId++);
    } // private String idToString()

} // public class DTrial extends DFile