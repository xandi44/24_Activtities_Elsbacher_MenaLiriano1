package sample.bll;

import sample.dal.DatabaseManager;
import sample.dal.dao.Dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Activity {
    private int id;
    private Season season;
    private String identifier;


    public Activity(int id, Season season, String identifier) {
        this.id = id;
        this.season = season;
        this.identifier = identifier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public static HashMap<Integer,Activity> getActivities(){
        return new HashMap<>(DatabaseManager.getInstance().getActvities());
    }

    @Override
    public String toString() {
        return identifier;
    }
}
