package sample.dal;



import sample.bll.Activity;
import sample.bll.Person;
import sample.bll.Season;
import sample.utie.PropertyManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseManager {
    private Connection connection;
    private String driver;
    private String url;
    private String username;
    private String password;
    private static DatabaseManager instance;
    private HashMap<Integer, Season> seasons = null;
    private HashMap<Integer, Activity> activities = null;

    private DatabaseManager(){
        PropertyManager.getInstance().setFileName("db.properties");
        this.driver = PropertyManager.getInstance().readProperty("driver","oracle.jdbc.OracleDriver");
        this.url = PropertyManager.getInstance().readProperty("url","jdbc:oracle:thin:@tcif.htl-villach.at:1521/orcl");
        this.username = PropertyManager.getInstance().readProperty("username","d3a27");
        this.password = PropertyManager.getInstance().readProperty("password","d3a27");
    }

    private Connection createConnection(){
        Connection con = null;
        //Laden des Treibers
        try {
            Class.forName(this.driver);
            con = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
    public static DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public HashMap<Integer,Season> getAllSeason() {
        Statement stmt;
        ResultSet resultSet;
        this.seasons = new HashMap<>();

        String query = "SELECT * FROM season";

        try(Connection con = this.createConnection()){
            //Statement wird erzeugt
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);
            // resultset wird durchiteriert
            while(resultSet.next()){
                seasons.put(resultSet.getInt(1),new Season(resultSet.getInt(1),resultSet.getString(2)));
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }

        return seasons;
    }

    public HashMap<Integer,Activity> getAllActivities() {
        Statement stmt;
        ResultSet resultSet;
        this.activities = new HashMap<>();

        String query = "SELECT * FROM activity";

        try(Connection con = this.createConnection()){
            //Statement wird erzeugt
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);
            // resultset wird durchiteriert
            while(resultSet.next()){
                activities.put(resultSet.getInt(1),new Activity(resultSet.getInt(1),this.getAllSeason().get(resultSet.getInt(2)),resultSet.getString(3)));
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }

        return activities;
    }

    public List<Person> getAllPersons() {
        ArrayList<Person> studentArrayList = new ArrayList<>();

        Statement stmt;
        ResultSet resultSet;

        String query = "SELECT * FROM person";

        try(Connection con = this.createConnection()){
            //Statement wird erzeugt
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);
            // resultset wird durchiteriert
            while(resultSet.next()){
                studentArrayList.add(new Person(resultSet.getInt(1),this.getActvities().get(resultSet.getInt(2)), resultSet.getString(3),resultSet.getString(4)));
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }

        return studentArrayList;
    }




    public boolean updatePerson(Person s){
        boolean result = false;
        PreparedStatement preparedStatement;
        String stmt_update = "UPDATE person SET idActivity = ?, firstname = ?, lastname = ? WHERE id = ?";
        int numberrows = 0;
        try(Connection con = this.createConnection()) {
            preparedStatement = con.prepareStatement(stmt_update);
            preparedStatement.setInt(1,s.getActivity().getId());
            preparedStatement.setString(2,s.getFirstname());
            preparedStatement.setString(3,s.getLastname());
            preparedStatement.setInt(4,s.getId());
            numberrows = preparedStatement.executeUpdate();
            if(numberrows > 0){
                result = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public boolean insertPerson(Person p){
        boolean result = false;
        PreparedStatement preparedStatement;
        String stmt_insert = "INSERT INTO person (idActivity, firstname, lastname) VALUES (?,?,?)";
        ResultSet resultSet;
        int id = -1;

        try (Connection con = this.createConnection()){
            // damit wir zur automatisiert generierten Id kommen, müssen wir beim Erzeugen des
            // Statements mitgeben, dass diese Column zurückgeliefert werden sollte
            preparedStatement = con.prepareStatement(stmt_insert, new String[]{"id"});
            preparedStatement.setInt(1,p.getActivity().getId());
            preparedStatement.setString(2,p.getFirstname());
            preparedStatement.setString(3,p.getLastname());
            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                id = resultSet.getInt(1);
                p.setId(id);
                result = true;
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return result;
    }
    public boolean deletePerson(int id){
        boolean result = true;
        PreparedStatement preparedStatement;
        String stmt_delete = "DELETE FROM person WHERE id = ?";
        int numberrows = 0;
        try(Connection con = this.createConnection()) {
            preparedStatement = con.prepareStatement(stmt_delete);
            preparedStatement.setInt(1,id);
            numberrows = preparedStatement.executeUpdate();
            if(numberrows > 0){
                result = true;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public HashMap<Integer, Season> getSeasons() {
        if(seasons == null){
            this.getAllSeason();
        }
        return seasons;
    }
    public HashMap<Integer, Activity> getActvities() {
        if(activities == null){
            this.getAllActivities();
        }
        return activities;
    }

}
