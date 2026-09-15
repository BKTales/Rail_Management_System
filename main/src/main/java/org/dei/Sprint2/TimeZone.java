package org.dei.Sprint2;

public class TimeZone {
    private final String continent;
    private final String city;

    public TimeZone(String raw) {
        String cleaned = raw.replaceAll("[\\(\\)'\",]", "").trim();
        String[] parts = cleaned.split("/");
        if(parts.length == 2){
            this.continent = parts[0];
            this.city = parts[1];
        }else{
            this.continent = "";
            this.city = "";
        }
    }

    public String getZoneId(){
        return (continent + '/' + city);
    }

    public String getContinent() { return continent; }
    public String getCity() { return city; }

    @Override
    public String toString() {
        return continent + "/" + city;
    }
}

