package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** 
 * Expression class 
 */
public class Expression {
    Map<Character, Integer> symbolToInt;
    Map<Integer, Character> intToSymbol;    
    ArrayList<String> terms;
    final private static char X = 'x';
    final private static char INVERT = '\'';        
    
    public Expression() {
        terms = new ArrayList<String>();
        symbolToInt = new HashMap<Character, Integer>();
        intToSymbol = new HashMap<Integer, Character>();    
    }
    
    /**
     * FORMAL form {"10x", "110", "1x1"}
     */
    public Expression(ArrayList<String> expression){
        this();
        regex(expression);
    }    
    
    /**    
     * Form : ab'c + a'bc + abc + b
     */
    public Expression(String expression) {
        this();
        expression = expression.trim();        
        String[] part = expression.split("\\+");        
        ArrayList<String> ts = new ArrayList<String>();
        for (int i = 0; i < part.length; ++i) {                        
            ts.add(part[i]);
        }
        regex(ts);
    }
    
    public boolean setTerm(ArrayList<String> newTerm) {
        terms = (ArrayList<String>) newTerm.clone();
        return true;
    }
    
    private boolean regex(ArrayList<String> part) {        
        ArrayList<Character> symbols = new ArrayList<Character>();
        for (int i = 0; i < part.size(); ++i) {            
            part.set(i, removeWhiteSpace(part.get(i)));
            String curStr = part.get(i);
            for (int j = 0; j< curStr.length(); ++j) {
                char cur = curStr.charAt(j);
                if (cur == INVERT || symbols.contains(cur)) {
                    continue;
                }
                symbols.add(cur);
            }
         } 
        Collections.sort(symbols);
          //Show the number of symbol
         System.out.println("Number of Symbol: " + symbols.size());

         int count = 0;      
         Iterator it = symbols.iterator();
         while (it.hasNext()){
             Character c = (Character) it.next();
             symbolToInt.put(c, count);
             intToSymbol.put(count, c);
             System.out.println(c + " - " + count);
             count++;
         }

         StringBuilder general = new StringBuilder();
         for (int i = 0; i < symbols.size(); ++i) {
             general.append(X);                    
         }
         Collections.sort(part);
         //to FORMAL form
         for (int i = 0; i < part.size(); ++i) {
             StringBuilder formalForm = new StringBuilder(general);
             String curStr = part.get(i);
             int curLen = curStr.length();
             for (int j = 0; j < curLen; ){                 
                 int position = symbolToInt.get(curStr.charAt(j));                
                 char charToSet = '1';
                 if (j < curLen - 1 && curStr.charAt(j + 1) == INVERT) {
                     charToSet = '0';
                     j++;
                 }
                 formalForm.setCharAt(position, charToSet);
                 j++;
             }
             System.out.println(part.get(i) + " => " + formalForm);            
             String formalStr = formalForm.toString();
             if (!terms.contains(formalStr)) {
                terms.add(formalStr);            
             }
         }

         System.out.println("After : ");
         for (String term : terms) {
             System.out.println(term);
         }
        return true;
    }

    private static String removeWhiteSpace(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if (c == ' ' || c == '\t' || c == '\n') {
                continue;
            }
            result.append(c);
        }
        return result.toString();
    }
       
    public ArrayList<String> termsInFormalForm() {
        ArrayList<String> result = new ArrayList<String>();
        for (String term : terms) {
            result.add(term);
        }
        return result;
    }

    public ArrayList<String> termsInReadableForm() {
        ArrayList<String> result = new ArrayList<String>();
        for (String term : terms) {
            StringBuilder readableForm = new StringBuilder();
            for (int i = 0; i < term.length(); ++i) {
                char curChar = term.charAt(i);
                if (curChar == X) {
                    continue;
                }                
                readableForm.append((Character) intToSymbol.get(i));
                if (term.charAt(i) == '0') {
                    readableForm.append(INVERT);
                }
            }
            result.add(readableForm.toString());
        }
        return result;
    }
    public String getExpression() {
        StringBuilder result = new StringBuilder();
        ArrayList<String> readable = termsInReadableForm();
        for (int i = 0; i < readable.size(); ++i) {
            result.append(readable.get(i));
            if (i < readable.size() - 1) result.append(" + ");
        }
        
        if ("".equals(result.toString())) {
            return "1";
        }
        return result.toString();
    }
}
