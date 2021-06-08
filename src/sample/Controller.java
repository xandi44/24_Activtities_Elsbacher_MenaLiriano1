package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import sample.bll.Activity;
import sample.bll.Person;
import sample.bll.Season;
import sample.dal.dao.Dao;
import sample.dal.dao.PersonDBDao;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    private ComboBox cbSeason;
    @FXML
    private ListView lvActivities;
    @FXML
    private TableView tvPerson;
    TableColumn<Person, String> tcfirstname = null;
    TableColumn<Person, String> tclastname = null;

    Season currentSeason = null;
    Activity currentActivity = null;

    HashSet<Season> seasons = new HashSet<>();
    HashSet<Activity> activities = new HashSet<>();
    HashSet<Person> persons = new HashSet<>();
    Dao<Person> dao = new PersonDBDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.cbSeason.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldObject, Object newObject) {
                currentSeason = (Season) newObject;
                currentActivity = null;
                showActivities();
                tvPerson.getItems().clear();
            }
        });

        Season.getSeasons().values().forEach(s -> seasons.add(s));

        ObservableList<Season> dataSubjects = FXCollections.observableArrayList(seasons);
        cbSeason.setItems(dataSubjects);

        Activity.getActivities().values().forEach(a -> activities.add(a));


        persons = (HashSet<Person>) Person.getPersons(new PersonDBDao());


        this.lvActivities.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.lvActivities.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldObject, Object newObject) {
                currentActivity = (Activity) newObject;
                showPersons();
            }
        });

        this.createColumns();
        this.configureTableView();
    }

    private void showPersons() {
        if(currentActivity != null){
            HashSet<Person> loadedpersons = new HashSet<>();

            for(Person p : persons){
                if(p.getActivity().getIdentifier().equals(currentActivity.getIdentifier())){
                    loadedpersons.add(p);
                }
            }

            ObservableList<Person> booksList = FXCollections.observableList(new ArrayList<>(loadedpersons));
            this.tvPerson.setItems(booksList);
        }
    }

    private void createColumns() {
        tcfirstname = new TableColumn<Person, String>("Firstname");
        tclastname = new TableColumn<Person, String>("Lastname");

        tcfirstname.setCellValueFactory(new PropertyValueFactory<Person, String>("firstname"));
        tclastname.setCellValueFactory(new PropertyValueFactory<Person, String>("lastname"));

        this.tvPerson.getColumns().addAll(tcfirstname,tclastname);
    }

    private void configureTableView() {
        this.tvPerson.setEditable(true);

        tcfirstname.setCellFactory(TextFieldTableCell.forTableColumn());
        tcfirstname.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> t) {
                Person p = (Person) t.getTableView().getItems().get(t.getTablePosition().getRow());
                p.setFirstname(t.getNewValue());
                Dao<Person> dao = new PersonDBDao();
                p.update(dao);


            }

        });

        tclastname.setCellFactory(TextFieldTableCell.forTableColumn());
        tclastname.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> t) {
                Person p = (Person) t.getTableView().getItems().get(t.getTablePosition().getRow());
                p.setLastname(t.getNewValue());
                p.update(dao);
            }

        });
    }

    private void showActivities() {

        HashSet<Activity> SelectedActivities = new HashSet<>();

        for(Activity a : activities){
            if(a.getSeason().getIdentifier().equals(currentSeason.getIdentifier())){
                SelectedActivities.add(a);
            }
        }

        ObservableList<Activity> listActivities= FXCollections.observableList(new ArrayList<>(SelectedActivities));
        this.lvActivities.setItems(listActivities);

    }

    @FXML
    private void onDeleteClicked(ActionEvent actionEvent) {
        if (this.tvPerson.getSelectionModel().getSelectedItem() != null) {
            Person p = (Person) tvPerson.getSelectionModel().getSelectedItem();
            persons.remove(p);
            this.tvPerson.getItems().remove(this.tvPerson.getSelectionModel().getSelectedIndex());
            p.delete(dao,p.getId());
        }
    }

    @FXML
    private void onNewClicked(ActionEvent actionEvent) {
        if(currentActivity != null){
            Person p = new Person(currentActivity,"new","new");
            this.tvPerson.getItems().add(p);
            persons.add(p);
            p.insert(dao);
        }

    }
}
