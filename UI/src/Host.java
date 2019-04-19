import javafx.beans.property.SimpleStringProperty;

/**
 * Internal representation of a row in the HOST table
 */
public class Host implements Row {

    private final SimpleStringProperty host_id;
    private final SimpleStringProperty host_url;
    private final SimpleStringProperty host_name;
    private final SimpleStringProperty host_since;
    private final SimpleStringProperty host_thumbnail_url;

    public Host(String host_id, String host_url, String host_name, String host_since, String host_thumbnail_url){

        this.host_id = new SimpleStringProperty(host_id);
        this.host_url = new SimpleStringProperty(host_url);
        this.host_name = new SimpleStringProperty(host_name);
        this.host_since = new SimpleStringProperty(host_since);
        this.host_thumbnail_url = new SimpleStringProperty(host_thumbnail_url);
    }


    //getters

    public String getHost_id(){
        return this.host_id.get();
    }

    public String getHost_url(){
        return this.host_url.get();
    }

    public String getHost_name(){
        return this.host_name.get();
    }

    public String getHost_since(){
        return this.host_since.get();
    }

    public String getHost_thumbnail_url(){
        return this.host_thumbnail_url.get();
    }


    //setters

    public void setHost_id(String host_id){
        this.host_id.set(host_id);
    }

    public void setHost_url(String host_url){
        this.host_url.set(host_url);
    }

    public void setHost_name(String host_name){
        this.host_name.set(host_name);
    }

    public void  setHost_since(String host_since){
        this.host_since.set(host_since);
    }

    public void setHost_thumbnail_url(String host_thumbnail_url){
        this.host_thumbnail_url.set(host_thumbnail_url);
    }

}
