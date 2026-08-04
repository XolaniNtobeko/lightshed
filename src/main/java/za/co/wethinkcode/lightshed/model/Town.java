package za.co.wethinkcode.lightshed.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Town {
    @JsonProperty("name")
    private String name;

    @JsonProperty("province")
    private String province;

    public Town(String name, String province){
        this.name = name;
        this.province = province;
    }

    public Town(){

    }
    public String getName(){
        return this.name;
    }
    public String getProvince(){
        return this.province;
    }

}
