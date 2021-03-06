package com.malabarba.util;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author artur
 *
 */
public class SettingsManager {
    static SharedPreferences sp;
    static SharedPreferences.Editor editor;
    static Boolean edited = false;
    //	public SettingsManager() {}
	
    static private void init() {
        if (sp == null) sp = PreferenceManager.getDefaultSharedPreferences(App.getContext());
    }
    
    static public SharedPreferences getSharedPreferences() {
        init();
        return sp;
    }
    
    static public Set<String> getStringSet(String id) {return getStringSet(id,null);}
    static public Set<String> getStringSet(String id, Set<String> v) {
        App.i("[SettingsManager] Retrieving StringSet "+id);
        if (edited) commit();
        return sp.getStringSet(id, v);
    }
    static public float getFloat(String id) {return getFloat(id,1.0f);}
    static public float getFloat(String id, float v) {
        App.i("[SettingsManager] Retrieving Float "+id);
        if (edited) commit();
        return sp.getFloat(id, v);
    }
    static public int getInt(String id) {return getInt(id,0);}
    static public int getInt(String id, int v) {
        App.i("[SettingsManager] Retrieving Int "+id);
        if (edited) commit();
        return sp.getInt(id, v);
    }
    static public String getString(String id) {return getString(id,null);}
    static public String getString(String id, String v) {
        App.i("[SettingsManager] Retrieving String "+id);
        if (edited) commit();
        return sp.getString(id, v);
    }
    static public Boolean getBoolean(String id) {return getBoolean(id,false);}
    static public Boolean getBoolean(String id, Boolean v) {
        App.i("[SettingsManager] Retrieving Boolean "+id);
        if (edited) commit();
        return sp.getBoolean(id, v);
    }

    /**
     * @return
     */
    @SuppressLint("CommitPrefEdits")
	static public SharedPreferences.Editor getEditor() {
        if (editor == null) {
            init();
            editor = sp.edit();
        }
        return editor;
    }
    

    static public void commit() {
        App.i("[SettingsManager] Commiting edits.");
        getEditor().commit();
        edited = false;
    }
    
    static public void put(String id, Set<String> v) {put(id,v,true);}
    static public void put(String id, Set<String> v, Boolean doCommit) {
        edited = true;
        App.i("[SettingsManager] Setting "+id+" to "+v);
        getEditor().putStringSet(id, v);
        if (doCommit) commit();
    }
    static public void put(String id, float v) {put(id,v,true);}
    static public void put(String id, float v, Boolean doCommit) {
        edited = true;
        App.i("[SettingsManager] Setting "+id+" to "+v);
        getEditor().putFloat(id, v);
        if (doCommit) commit();
    }
    static public void put(String id, int v) {put(id,v,true);}
    static public void put(String id, int v, Boolean doCommit) {
        edited = true;
        App.i("[SettingsManager] Setting "+id+" to "+v);
        getEditor().putInt(id, v);
        if (doCommit) commit();
    }
    static public void put(String id, String v) {put(id,v,true);}
    static public void put(String id, String v, Boolean doCommit) {
        edited = true;
        App.i("[SettingsManager] Setting "+id+" to "+v);
        getEditor().putString(id, v);
        if (doCommit) commit();
    }
    static public void put(String id, Boolean v) {put(id,v,true);}
    static public void put(String id, Boolean v, Boolean doCommit) {
        edited = true;
        App.i("[SettingsManager] Setting "+id+" to "+v);
        getEditor().putBoolean(id, v);
        if (doCommit) commit();
    }

    public static Boolean contains(String id) {
        init();
        return sp.contains(id);
    }
    public static void putIfAbsent(String id, float v) {putIfAbsent(id,v,true);}
    public static void putIfAbsent(String id, float v, Boolean c) {if (!contains(id)) put(id, v, c);}
    public static void putIfAbsent(String id, int v) {putIfAbsent(id,v,true);}
    public static void putIfAbsent(String id, int v, Boolean c) {if (!contains(id)) put(id, v, c);}
    public static void putIfAbsent(String id, String  v) {putIfAbsent(id,v,true);}
    public static void putIfAbsent(String id, String  v, Boolean c) {if (!contains(id)) put(id, v, c);}
    public static void putIfAbsent(String id, boolean v) {putIfAbsent(id,v,true);}
    public static void putIfAbsent(String id, boolean v, Boolean c) {if (!contains(id)) put(id, v, c);}
}
