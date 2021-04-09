/*

Name: Stephen Cartner
Student Id: S1706321

 */

package org.me.gcu.equakestartercode;

public class EarthquakeClass {

    private String title;
    private String description;
    private String date;
    private String time;
    private String location;
    private String latLong;
    private String depth;
    private String magnitude;
    private String link;
    private String category;
    private String geoLat;
    private String geoLong;

    public EarthquakeClass()
    {
        title = "";
        description = "";
        date = "";
        time = "";
        location = "";
        latLong = "";
        depth = "";
        magnitude = "";
        link = "";
        category = "";
        geoLat = "";
        geoLong = "";
    }

    public EarthquakeClass(String theTitle, String theDescription, String theDate, String theTime, String theLocation, String theLatLong, String theDepth, String theMagnitude, String theLink, String theCategory, String theGeoLat, String theGeoLong)
    {
        title = theTitle;
        description = theDescription;
        date = theDate;
        time = theTime;
        location = theLocation;
        latLong = theLatLong;
        depth = theDepth;
        magnitude = theMagnitude;
        link = theLink;
        category = theCategory;
        geoLat = theGeoLat;
        geoLong = theGeoLong;
    }

    public String getTitle() { return  title;}

    public void setTitle(String theTitle) {title = theTitle;}

    public String getDescription() { return  description;}

    public void setDescription(String theDescription) {description = theDescription;}

    public String getDate() { return date;}

    public void setDate(String theDate) {date = theDate;}

    public String getTime() { return time;}

    public void setTime(String theTime) {time = theTime;}

    public String getLocation() { return location;}

    public void setLocation(String theLocation) {location = theLocation;}

    public String getLatLong() { return latLong;}

    public void setLatLong(String theLatLong) {latLong = theLatLong;}

    public String getDepth() { return depth;}

    public void setDepth(String theDepth) {depth = theDepth;}

    public String getMagnitude() { return  magnitude;}

    public void setMagnitude(String theMagnitude) {magnitude = theMagnitude;}

    public String getLink() { return  link;}

    public void setLink(String theLink) {link = theLink;}

    public String getCategory() { return  category;}

    public void setCategory(String theCategory) {category = theCategory;}

    public String getGeoLat() { return  geoLat;}

    public void setGeoLat(String theGeoLat) {geoLat = theGeoLat;}

    public String getGeoLong() { return  geoLong;}

    public void setGeoLong(String theGeoLong) {geoLong = theGeoLong;}

    public String earthquakeToString()
    {
        String temp;

        temp = title + " " + description + " " + date + " " + location + " " + latLong + " " +  depth + " " + magnitude + link + " " + category + " " + geoLat + " " + geoLong;

        return temp;
    }

}// End of Class