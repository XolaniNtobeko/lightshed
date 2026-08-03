package za.co.wethinkcode.lightshed.service;

import java.util.Locale;

public class TownCleaner {

    public String cleanText(String inputText){
        if(inputText == null || inputText.isBlank()){
            return "";
        }
        //Trimming leading or trainling whitespaces and split on any spacing
        String[] words = inputText.split("\\s+");
        StringBuilder cleaned = new StringBuilder();

        // Format each word to Title Case eg. "gEorGe" to "George"
        for (String word : words){
            if(!word.isEmpty()){
                String format = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                cleaned.append(format).append(" ");
            }
        }
        return cleaned.toString().trim();
    }
}
