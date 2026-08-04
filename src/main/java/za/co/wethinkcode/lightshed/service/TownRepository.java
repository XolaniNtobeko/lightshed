package za.co.wethinkcode.lightshed.service;

import za.co.wethinkcode.lightshed.model.Town;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TownRepository {
    private final TownCleaner cleaner;

    private final List<Town> towns;

    public TownRepository(TownCleaner cleaner){
        this.cleaner = cleaner;
        this.towns = new ArrayList<>();

    }
    public List<Town> getAllTowns(){
        return Collections.unmodifiableList(towns);
    }
    public void loadFromCsv(String resourceName){
        towns.clear();
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);

        if(is == null){
            is = getClass().getResourceAsStream("/"+resourceName);
        }
        if(is == null ){
            throw new IllegalArgumentException("Resource not found"+ resourceName);
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            boolean isHeader = true;
            while((line = reader.readLine()) != null){
                    if(line.isBlank())
                        continue;
                    String[] parts = line.split(",", -1);
                    if(isHeader){
                        isHeader = false;
                        String firstColumn = parts[0].trim();
                        if(firstColumn.equalsIgnoreCase("town") || firstColumn.equalsIgnoreCase("name")) continue;

                    }
                    String rawName = parts[0];
                    String rawProvince = parts.length > 1 ? parts[1].trim(): "";

                    String cleanName = cleaner.cleanText(rawName);
                    towns.add(new Town(cleanName, rawProvince));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
