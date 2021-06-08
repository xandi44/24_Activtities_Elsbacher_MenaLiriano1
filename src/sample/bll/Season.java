package sample.bll;

import sample.dal.DatabaseManager;
import sample.dal.dao.Dao;

import java.util.HashMap;

public class Season {
    private int id;
    private String identifier;

    public Season(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public static HashMap<Integer,Season> getSeasons(){
        return new HashMap<>(DatabaseManager.getInstance().getSeasons());
    }

    @Override
    public String toString() {
        return identifier;
    }
}
