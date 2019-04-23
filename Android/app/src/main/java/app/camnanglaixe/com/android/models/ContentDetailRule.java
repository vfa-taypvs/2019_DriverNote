package app.camnanglaixe.com.android.models;

/**
 * Created by taypham on 29/11/2016.
 */
public class ContentDetailRule {

    public String image;
    public String title;
    public String detail;
    public boolean isGroup;
    public int indexGroup;

    public ContentDetailRule(String title, String detail, String image){
        this.title = title;
        this.detail = detail;
        this.image = image;
    }

}
