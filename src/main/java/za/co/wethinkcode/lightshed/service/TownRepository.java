package za.co.wethinkcode.lightshed.service;

import za.co.wethinkcode.lightshed.model.Town;

import java.util.Collections;
import java.util.List;

public class TownRepository {
    private final TownCleaner cleaner;

    public TownRepository(TownCleaner cleaner){
        this.cleaner = cleaner;

    }
    public List<Town> getAllTowns(){
        return Collections.emptyList();
    }
    public void loadFromCsv(String resourceName){

    }
}
