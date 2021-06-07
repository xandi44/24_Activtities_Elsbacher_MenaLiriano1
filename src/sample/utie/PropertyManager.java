package sample.utie;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
    private Properties properties = new Properties();
    private static PropertyManager instance;
    private String fileName = "";
    private PropertyManager(){

    }
    public static PropertyManager getInstance(){
        if(instance == null){
            instance = new PropertyManager();
        }
        return instance;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        this.fillProperties();
    }

    private void fillProperties() {
        try(FileReader fileReader = new FileReader(this.fileName)){
            this.properties.load(fileReader);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readProperty(String key, String defaultValue){
        return this.properties.getProperty(key, defaultValue);
    }
}
