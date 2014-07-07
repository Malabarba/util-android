/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Artur Malabarba
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package com.malabarba.util;
import java.io.InputStream;

import android.app.Application;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * A static class which provides a series of convenience utilities
 * that are useful throughout an application.
 * 
 * @author Artur Malabarba
 *
 */
public class App extends Application {
    public static String tg = "AppTag";
    private static Context mContext;
    private static Toast toast;
    private static long lastTime;
    private static long firstTime;
    private static String timeReport;
    private static boolean timing = true;
    private static boolean toastTiming = false;
    private static boolean toasting = true;
    public static final String EXTRA_INT =
        "com.malabarba.util.INTENT_EXTRA_INT";
    
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    /**
     * Restart activity.
     *
     * Calls System.exit(0) on activity, and uses a delayed intent to
     * start it again.
     * @param activity The activity to be restarted.
     * @see loadUpTo
     */
    public static void restart(Activity activity) {restart(activity, false);}
    /**
     * Restart activity.
     *
     * Like restart(activity), but optionally calls the finish()
     * method of activity.
     * @param activity The activity to be restarted.
     * @param finish Whether to invoke activity.finish().
     */
    public static void restart(Activity activity, boolean finish) {
        Intent newActivity = new Intent(mContext, activity.getClass());
        int pendingIntentId = 1219;
        
        PendingIntent pendingIntent =
            PendingIntent.getActivity(mContext, pendingIntentId,
                                      newActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 200, pendingIntent);
        if  (finish) activity.finish();
        System.exit(0);
    }
    
    /**
     * Call browseUrl(url, "/:", -1)
     */
    public static void browseUrl(String url) {browseUrl(url,-1);}
    /**
     * Call browseUrl(url, "/:", extraInt)
     */
    public static void browseUrl(String url, int extraInt) {browseUrl(url,"/:",extraInt);}
    /**
     * Send an Intent.ACTION_VIEW intent with url as data.
     *
     * Like restart(activity), but optionally calls the finish()
     * method of activity.
     * @param url The url to use as data.
     * @param allow Characters that are allowed and won't be % encoded in the given url string. If this is null, don't perform % encoding.
     * @param extraInt An integer to pass to the intent with putExtra(EXTRA_INT, extraInt). Useful for sending some data in case you are also the receiver of the intent.
     */
    public static void browseUrl(String url, String allow, int extraInt) {
        final String finalUrl = (allow == null) ? url : Uri.encode(url, allow);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (extraInt > -1) browserIntent.putExtra(EXTRA_INT,extraInt);
        i("Sending browserIntent:\n" + browserIntent);
        
        mContext.startActivity(browserIntent); 
    }

    /**
     * Send an Intent.ACTION_SEND intent with string as data and "text/plain" as type.
     *
     * @param string The string to use as data.
     */
    public static void sharePlain(String string) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, string);
        sendIntent.setType("text/plain");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(sendIntent);
    }

    
    /**
     * Get a bunch of information on the current system. Useful for bug reports.
     *
     * Gets "Device", "OS Release", "OS Codename", "OS API Level (and
     * Int)", "OS Incremental", and "Model (Product)"
     * @return A string listing system information. 
     */
    @SuppressWarnings("deprecation")
	public static String getSystemInformation() {
        return "OS Version: " + System.getProperty("os.version")
            + "\n OS Incremental: " + android.os.Build.VERSION.INCREMENTAL
            + "\n OS API Level (and Int): " + android.os.Build.VERSION.SDK + " ("+android.os.Build.VERSION.SDK+")"
            + "\n OS Codename: " + android.os.Build.VERSION.CODENAME
            + "\n OS Release: " + android.os.Build.VERSION.RELEASE
            + "\n Device: " + android.os.Build.DEVICE
            + "\n Model (Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")\n\n";
    }
    
    /**
     * Send an Intent.ACTION_SENDTO intent, using mailto protocol, with address.
     *
     * @param address The destination email address.
     * @param subject The resource ID of the string to be used as subject of the email.
     * @param body The resource ID of the string to be used as body of the email.
     */
    public static void emailIntent(String address, int subject, int body) {
        emailIntent(address,
                    (subject == 0)? null : string(subject),
                    (body == 0)? null : string(body));}
    
    /**
     * Send an Intent.ACTION_SENDTO intent, using mailto protocol, with address.
     *
     * @param address The destination email address.
     * @param subject The string to be used as subject of the email.
     * @param body The string to be used as body of the email.
     */
    public static Intent emailIntent(String address, String subject, String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",address, null));
        
        if (subject != null) emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (body != null) emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        return Intent.createChooser(emailIntent, "Send Email");
    }
    
    public static Context getContext(){
        checkContext();
        return mContext;
    }
    
    public static long startTiming(String s) {
        if (timing) {
            firstTime = lastTime = System.currentTimeMillis();
            timeReport = s + ": " + lastTime;
            return lastTime;
        }
        return -1;
    }
    public static long stepTiming(String s){
        if (timing && lastTime > 0) {
            long step = System.currentTimeMillis() - lastTime;
            timeReport += "\n" + s + ": " + step;
            lastTime += step;
            return step;
        } else if (timing) return -2;
        return -1;
    }
    public static long finishTiming(String s){
        if (timing) {
            long step = System.currentTimeMillis() - lastTime;
            timeReport += "\n" + s + ": " + step
                +"\n\nFinished Counting: " + (lastTime - firstTime);
        
            if (toastTiming) toast(timeReport);
            i(timeReport);
        
            lastTime = 0;
            timeReport = null;
        
            return lastTime - firstTime;
        }
        return -1;
    }

    /**
     * Get the string with the given id. Same as getContext().getString(id).
     *
     * @param id Resource ID of the string to return.
     * @return The string resource with id. 
     */
    public static String string(int id){
        checkContext();
        return getContext().getString(id);
    }
       
    
    /**
     * Read the entire InputStream is and convert return it as a string.
     *
     * @param is The InputStream to read.
     * @return The string obtained by reading the stream.
     */
    static String streamToString(java.io.InputStream is) {
        @SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Get the RawResource with the given id. Same as getContext().getResources().openRawResource(id).
     *
     * @param id ID of the resource to return.
     * @return The resource with id. 
     */    
    public static InputStream contextOpenRawResources(int id){
        return getContext().getResources().openRawResource(id);
    }
    

    /**
     * Display a toast notification with text.
     *
     * As long as you use this class for all your toasts, it will
     * always take care of closing an exisiting toast before sending a
     * new one. So your toasts are always displayed immediately, never
     * delayed.
     *
     * @param text String to display.
     * @param isLong If true the toast uses Toast.LENGTH_LONG. Otherwise it uses Toast.LENGTH_SHORT.
     */
    public static void toast(String text, Boolean isLong) {
    	if (!toasting) return;
        Integer length = Toast.LENGTH_LONG;
        if (!isLong) length = Toast.LENGTH_SHORT;

        checkContext();
        if(toast != null) toast.cancel();
        toast = Toast.makeText(getContext(), text, length);
        toast.show();
    }
    /**
     * Display a toast notification with text given by the given ID.
     *
     * Same as toast(getContext().getString(id), true).
     * @param id Resource ID of the string to display.
     */
    public static void toast(Integer id) {toast(string(id), true);}    
    /**
     * Same as toast(text, true).
     */
    public static void toast(String text) {toast(text, true);}    

    // TODO (98072)
    public static void dialog(String text) {toast(text, true);}    
    
    public static Integer e(String text) {
        return BuildConfig.DEBUG ? Log.e(tg,text) : 0;
    }
    public static Integer e(String text, Throwable e) {
        return BuildConfig.DEBUG ? Log.e(tg,text,e) : 0;
    }
    public static Integer d(String text) {
        return BuildConfig.DEBUG ? Log.d(tg,text) : 0;
    }
    public static Integer d(String text, Throwable e) {
        return BuildConfig.DEBUG ? Log.d(tg,text,e) : 0;
    }
    public static Integer i(String text) {
        return BuildConfig.DEBUG ? Log.i(tg,text) : 0;
    }
    public static Integer i(String text, Throwable e) {
        return BuildConfig.DEBUG ? Log.i(tg,text,e) : 0;
    }
    public static Integer v(String text) {
        return BuildConfig.DEBUG ? Log.v(tg,text) : 0;
    }
    public static Integer v(String text, Throwable e) {
        return BuildConfig.DEBUG ? Log.v(tg,text,e) : 0;
    }
    public static Integer wtf(String text) {
        return BuildConfig.DEBUG ? Log.wtf(tg,text) : 0;
    }
    public static Integer wtf(String text, Throwable e) {
        return BuildConfig.DEBUG ? Log.wtf(tg,text,e) : 0;
    }
    
    private static void checkContext() {
        if (mContext == null) 
            wtf("Tried getting a context that isn't defined.");
    }
}
