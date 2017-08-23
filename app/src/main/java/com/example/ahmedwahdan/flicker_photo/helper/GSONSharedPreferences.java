package com.example.ahmedwahdan.flicker_photo.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

/**
 * Created by hynra on 4/13/17.
 */

public class GSONSharedPreferences {

    private Context context;
    private String mPreferencesName;
    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mEditor;


    public GSONSharedPreferences(Context context, String mPreferencesName){
        this.context = context;
        this.mPreferencesName = mPreferencesName;
        mSharedPrefs = context.getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE);
        mEditor = mSharedPrefs.edit();
    }


    public GSONSharedPreferences(Context context, String mPreferencesName, int mode){
        this.context = context;
        this.mPreferencesName = mPreferencesName;
        mSharedPrefs = context.getSharedPreferences(mPreferencesName, mode);
        mEditor = mSharedPrefs.edit();
    }


    public GSONSharedPreferences(Context context){
        this.context = context;
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPrefs.edit();
    }



    /** save objects from objects  **/
    public void saveObjects(Object[] objects){
        String[] vals = new String[objects.length];
        for(int i = 0; i <objects.length; i++){
            vals[i] = new Gson().toJson(objects[i]);
            mEditor.putString(objects[i].getClass().getCanonicalName()+i, vals[i]);
        }
        mEditor.putInt(objects.getClass().getCanonicalName(), objects.length);
        commit();
    }


    /** save objects from json array  **/
    public void saveObjects(Object object, JSONArray array){
        try {
            String[] vals = new String[array.length()];
            for(int i = 0; i <vals.length; i++){

                vals[i] = array.getJSONObject(i).toString();
                mEditor.putString(object.getClass().getCanonicalName()+i, vals[i]);
            }
            mEditor.putInt(object.getClass().getCanonicalName()+"[]", array.length());
            commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /** save objects from json array string  **/
    public void saveObjects(Object object, String jArrayString){
        try {
            JSONArray array = new JSONArray(jArrayString);
            String[] vals = new String[array.length()];
            for(int i = 0; i <vals.length; i++){

                vals[i] = array.getJSONObject(i).toString();
                mEditor.putString(object.getClass().getCanonicalName()+i, vals[i]);
            }
            mEditor.putInt(object.getClass().getCanonicalName()+"[]", array.length());
            commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**  save object from object **/
    public void saveObject(Object object ){
        String val = new Gson().toJson(object);
        mEditor.putString(object.getClass().getCanonicalName(), val);
        commit();
    }
    /**  save object from object **/
    public void saveObjectWithTag(Object object , String tag ){
        String val = new Gson().toJson(object);
        mEditor.putString(object.getClass().getCanonicalName()+tag, val);
        commit();
    }
    /**  save object from string **/
    public void saveObject(Object object, String values){
        mEditor.putString(object.getClass().getCanonicalName(), values);
        commit();
    }

    /**  save object from json object **/
    public void saveObject(Object object, JSONObject values){
        mEditor.putString(object.getClass().getCanonicalName(), values.toString());
        commit();
    }

    public SharedPreferences getSharedPreferences(){
        return mSharedPrefs;
    }



    private void commit(){
        mEditor.commit();
    }

    /**  get object **/
    public Object getObject(Object object) {
        try {
            String val = mSharedPrefs.getString(object.getClass().getCanonicalName(), "");
            object = object.getClass();
            object = new Gson().fromJson(val, (Class<Object>) object);
        }catch (JsonSyntaxException exception){
            exception.printStackTrace();
        }
        return object;
    }
    /**  get object **/
    public Object getObjectWithTag(Object object , String tag) {
        try {
            String val = mSharedPrefs.getString(object.getClass().getCanonicalName()+tag, "");
            object = object.getClass();
            object = new Gson().fromJson(val, (Class<Object>) object);
        }catch (JsonSyntaxException exception){
            exception.printStackTrace();
        }
        return object;
    }


    /**  get objects **/
    public Object[] getObjects(Object object) {
        Object[] objects = null;
        try {
            int size = mSharedPrefs.getInt(object.getClass().getCanonicalName()+"[]", 0);
            objects = (Object[]) Array.newInstance(object.getClass(), size);
            String[] vals = new String[size];
            for(int i = 0; i < size; i++){
                vals[i] = mSharedPrefs.getString(object.getClass().getCanonicalName()+i, "");
                objects[i] = new Gson().fromJson(vals[i], (Class<Object>) object.getClass());
            }
        } catch (Exception e) {
        }
        return objects;
    }


    /**  get json object **/
    public JSONObject getJsonObject(Object object) {
        String val = mSharedPrefs.getString(object.getClass().getCanonicalName(), "");
        JSONObject jsonObject = null;
        object = object.getClass();
        try {
            jsonObject = new JSONObject(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**  get json object string **/
    public String getJsonObjectString(Object object) {
        String val = "";
        try {
            val = mSharedPrefs.getString(object.getClass().getCanonicalName(), "");
        }catch (JsonSyntaxException exception){
            exception.printStackTrace();
        }
        return val;
    }


    /**  get json array **/
    public JSONArray getJsonArray(Object object) {
        JSONArray jsonArray = new JSONArray();
        try {
            int size = mSharedPrefs.getInt(object.getClass().getCanonicalName()+"[]", 0);
            for(int i =0; i < size; i++){
                JSONObject jsonObject = new JSONObject(mSharedPrefs.getString(object.getClass().getCanonicalName()+i, ""));
                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    /**  get json array string **/
    public String getJsonArrayString(Object object) {
        JSONArray jsonArray = new JSONArray();
        try {
            int size = mSharedPrefs.getInt(object.getClass().getCanonicalName()+"[]", 0);
            for(int i =0; i < size; i++){
                JSONObject jsonObject = new JSONObject(mSharedPrefs.getString(object.getClass().getCanonicalName()+i, ""));
                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }


}
