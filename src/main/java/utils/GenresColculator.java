package utils;

import java.util.Iterator;

import org.json.simple.JSONArray;

public class GenresColculator {
    
    public static boolean isSumOfGenresMatch(JSONArray s) {
        
        // Return false if array is empty
        if (s.size() == 0)
            return false;
        
        int genresSum= 0;
        Iterator<?> i = s.iterator();
        while(i.hasNext()){
            genresSum += i.next().hashCode();
        }

        if (genresSum > 400)
            return true;
        else
            return false;
        
    }

}
