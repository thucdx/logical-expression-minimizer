package core;

import java.util.ArrayList;

/*
 * Not good name! Change later!
 */
public class Util {
    static StringBuilder current;    
    static int len;
    static ArrayList<String> result = new ArrayList<String>();
    
    private static void attempt(int i) {
        if (i >= len) {
            result.add(current.toString());
            return;
        }       
        if (current.charAt(i) == 'x') {
            current.setCharAt(i, '0');
            attempt(i + 1);
            current.setCharAt(i, '1');
            attempt(i + 1);
            current.setCharAt(i, 'x');
        } else {
            attempt(i + 1);
        }
    }
    
    public static ArrayList<String> span(String a) {
        result = new ArrayList<String>();
        len = a.length();
        current = new StringBuilder(a);
        
        attempt(0);
        return result;
    }
}
