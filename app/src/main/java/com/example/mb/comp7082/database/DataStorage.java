package com.example.mb.comp7082.database;


public class DataStorage implements IDataStore {
    public String state_ = null;
    public void saveState(String state) {state_=state;}
    public String getState(){return state_;}
}
