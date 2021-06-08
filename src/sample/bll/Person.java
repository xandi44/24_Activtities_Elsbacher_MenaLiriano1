package sample.bll;

import sample.dal.dao.Dao;

import java.util.HashSet;
import java.util.Set;

public class Person {
    private int id;
    private Activity activity;
    private String firstname;
    private String lastname;

    public Person(int id, Activity activity, String firstname, String lastname) {
        this.id = id;
        this.activity = activity;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Person(Activity activity, String firstname, String lastname) {
        this.activity = activity;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public static Set<Person> getPersons(Dao<Person> dao){
        return new HashSet<>(dao.getAll());
    }
    public boolean insert(Dao<Person> dao){
        return dao.insert(this);
    }
    public boolean update(Dao<Person> dao){
        return dao.update(this);
    }
    public boolean delete(Dao<Person> dao,int id){
        return dao.delete(id);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", activity=" + activity +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
