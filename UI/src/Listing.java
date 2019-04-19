import javafx.beans.property.SimpleStringProperty;

/**
 * Internal representation of a row in the LISTING table
 */
public class Listing implements Row{


    private final SimpleStringProperty listing_id;
    private final SimpleStringProperty listing_url;
    private final SimpleStringProperty listing_name;
    private final SimpleStringProperty listing_summary;
    private final SimpleStringProperty picture_url;
    private final SimpleStringProperty country;
    private final SimpleStringProperty city;
    private final SimpleStringProperty host_id;


    public Listing(String listing_id, String listing_url, String listing_name, String listing_summary, String picture_url, String country, String city, String host_id){

        this.listing_id = new SimpleStringProperty(listing_id);
        this.listing_url = new SimpleStringProperty(listing_url);
        this.listing_name = new SimpleStringProperty(listing_name);
        this.listing_summary = new SimpleStringProperty(listing_summary);
        this.picture_url = new SimpleStringProperty(picture_url);
        this.country = new SimpleStringProperty(country);
        this.city = new SimpleStringProperty(city);
        this.host_id = new SimpleStringProperty(host_id);

    }


    //getters

    public String getListing_id(){
        return this.listing_id.get();
    }

    public String getListing_url(){
        return this.listing_url.get();
    }

    public String getListing_name(){
        return this.listing_name.get();
    }

    public String getListing_summary(){
        return this.listing_summary.get();
    }

    public String getPicture_url(){
        return this.picture_url.get();
    }

    public String getCountry(){
        return this.country.get();
    }

    public String getCity(){
        return this.city.get();
    }

    public String getHost_id(){
        return this.host_id.get();
    }


    //setters

    public void setListing_id(String listing_id){
        this.listing_id.set(listing_id);
    }

    public void setListing_url(String listing_url){
        this.listing_url.set(listing_url);
    }

    public void setListing_name(String listing_name){
        this.listing_name.set(listing_name);
    }

    public void  setListing_summary(String listing_summary){
        this.listing_summary.set(listing_summary);
    }

    public void setPicture_url(String picture_url){
        this.picture_url.set(picture_url);
    }

    public void setCountry(String country){
        this.country.set(country);
    }

    public void  setCity(String city){
        this.city.set(city);
    }

    public void setHost_id(String host_id){
        this.host_id.set(host_id);
    }

}
