package sample.dal.dao;

import sample.bll.Person;
import sample.dal.DatabaseManager;

import java.util.List;

public class PersonDBDao implements  Dao<Person>{
    @Override
    public List<Person> getAll() {
        return DatabaseManager.getInstance().getAllPersons();
    }

    @Override
    public Person getById(int id) {
        return null;
    }

    @Override
    public boolean insert(Person item) {
        return DatabaseManager.getInstance().insertPerson(item);
    }

    @Override
    public boolean update(Person item) {
        return DatabaseManager.getInstance().updatePerson(item);
    }

    @Override
    public boolean delete(int id) {
        return DatabaseManager.getInstance().deletePerson(id);
    }
}
