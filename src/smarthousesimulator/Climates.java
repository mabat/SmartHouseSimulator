package smarthousesimulator;

import java.util.Map;

public class Climates {

    private Long huminidity;    //vrijednost vlage
    private Long temperature; //vrijednost temperature
    private Long availability; //potvrda o upravljanju  klimom
    private Long cmdClimate;
    
    public Climates() {}
    
    public Climates( Long a, Long cmdC, Long h, Long t){
        
        this.availability = a;
        this.cmdClimate = cmdC;
        this.huminidity = h;
        this.temperature = t;
    }
    public void setAvailability(Long a){
        this.availability=a;
    }
    public void setCmdClimate(Long c){
        this.cmdClimate=c;
    }
    public void setHuminidity(Long n){
        this.huminidity=n;
    }
    public void setTemperature(Long n){
        this.temperature = n;
    }
    public Long getAvailability(){
        return this.availability;
    }
    public Long getCmdClimate(){
        return this.cmdClimate;
    }
    public Long getHuminidity(){
        return this.huminidity;
    }
    public Long getTemperature(){
        return this.temperature;
    }
    
}
