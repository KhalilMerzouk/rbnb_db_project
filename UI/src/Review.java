import javafx.beans.property.SimpleStringProperty;

/**
 * Internal representation of a row in the REVIEWS table
 */
public class Review implements Row{


    private final SimpleStringProperty review_id;
    private final SimpleStringProperty listing_id;
    private final SimpleStringProperty reviewer_id;
    private final SimpleStringProperty comments;
    private final SimpleStringProperty review_date;

    public Review(String review_id, String listing_id, String reviewer_id, String comments, String review_date){

        this.review_id = new SimpleStringProperty(review_id);
        this.listing_id = new SimpleStringProperty(listing_id);
        this.reviewer_id = new SimpleStringProperty(reviewer_id);
        this.comments = new SimpleStringProperty(comments);
        this.review_date = new SimpleStringProperty(review_date);
    }


    //getters

    public String getReview_id(){
        return this.review_id.get();
    }

    public String getListing_id(){
        return this.listing_id.get();
    }

    public String getReviewer_id(){
        return this.reviewer_id.get();
    }

    public String getComments(){
        return this.comments.get();
    }

    public String getReview_date(){
        return this.review_date.get();
    }


    //setters

    public void setReview_id(String review_id){
        this.review_id.set(review_id);
    }

    public void setListing_id(String listing_id){
        this.listing_id.set(listing_id);
    }

    public void setReviewer_id(String reviewer_id){
        this.reviewer_id.set(reviewer_id);
    }

    public void  setComments(String comments){
        this.comments.set(comments);
    }

    public void setReview_date(String review_date){
        this.review_date.set(review_date);
    }


}
