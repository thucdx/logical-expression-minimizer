package core;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Quine–McCluskey algorithm
 * author    : ThucDX 
 * date      : 16-17/01/2013
 * reference : http://goo.gl/wWKJc
 */
public class QuineMcCluskeyAlgorithm {        
    ArrayList<String> terms = new ArrayList<String>();
    ArrayList<String> origin = new ArrayList<String>();
        
    public Expression optimize(Expression exp) {        
        terms = exp.termsInFormalForm();
        origin = (ArrayList<String>) exp.termsInFormalForm().clone();
        
        int step = 0;
        while (canOptimizeMore()) {            
            System.out.println("Step " + step);
            for (int i = 0; i < terms.size(); ++i) {
                System.out.print(terms.get(i) + "   ");
            }
            System.out.println("");
            if (step > 20) break;
            step++;
        }
                
        primeImplicantStep();
        exp.setTerm(terms);
        
        return exp;
    }
    
    private boolean canOptimizeMore() {
        elimiateTrivialTerm();
        boolean canOptimize = false;                       
        ArrayList<String> newTerms = new ArrayList<String>();
                
        boolean match[][] = new boolean[terms.size()][terms.size()];
        for (int i = 0; i < terms.size(); ++i) {
            boolean canMatchFirstKind = false;
            boolean canMatchSecondKind = false;
            
            for (int j = 0; j < terms.size(); ++j) {
                if (match[i][j] == true) {
                    canMatchFirstKind = true;                    
                    continue;
                }
                int pos = matchFirstKind(terms.get(i), terms.get(j));                
                if (pos != -1) {
                    canOptimize = true;
                    canMatchFirstKind = true;
                    match[i][j] = match[j][i] = true;
                    StringBuilder newTerm = new StringBuilder(terms.get(i));                                   
                    newTerm.setCharAt(pos, 'x');
                    if (!newTerms.contains(newTerm.toString()))
                        newTerms.add(newTerm.toString());                    
                }            
            }
            
            if (!canMatchFirstKind) {
                for (int j = 0; j < terms.size(); ++j) {
                    int pos = matchSecondKind(terms.get(i), terms.get(j));
                    if (pos != -1) {                        
                        canOptimize = true;
                        canMatchSecondKind = true;
                        StringBuilder newTerm = new StringBuilder(terms.get(i));                        
                        newTerm.setCharAt(pos, 'x');
                        if (!newTerms.contains(newTerm.toString()))
                            newTerms.add(newTerm.toString());
                    }
                }
            }
            if (!canMatchSecondKind) {
                if (!newTerms.contains(terms.get(i)))
                    newTerms.add(terms.get(i));
            }
        }                       
        terms = (ArrayList<String>) newTerms.clone();
        return canOptimize;
    }
    
    private void elimiateTrivialTerm() {
        ArrayList<String> shortTerm = new ArrayList<String>();
        for (int i = 0; i < terms.size(); ++i) {
            boolean isCoverByOther = false;
            for (int j = 0; j < terms.size(); ++j) {
                if (i != j && isCoveredBy(i, j)) {
                    System.out.println(terms.get(i) + " covered by " + terms.get(j));
                    isCoverByOther = true;
                    break;
                }
            }
            if (!isCoverByOther) {
                shortTerm.add(terms.get(i));
            }
        }
        terms = (ArrayList<String>) shortTerm.clone();
    }
    
    /**
     * Test wherether sample could be covered by another
     * for ex: 10xxx is covered by 1xxxx
     */
    private boolean isCoveredBy(int i, int j) {        
        int xFirst = countX(terms.get(i)), xSecond = countX(terms.get(j));
        if (xFirst >= xSecond) {
            return false;
        }        
        
        String first = terms.get(i), second = terms.get(j);
        for (int t = 0; t < first.length(); ++t) {
            char charFirst = first.charAt(t);
            char charSecond = second.charAt(t);
            if (charSecond != 'x') {
                if (charFirst != charSecond) return false;
            }            
        }
        return true;
    }
    
    /*
     * first and second differ at only one place, 0 - 1
     * return position or -1
     */
    private static int matchFirstKind(String first, String second) {
        int pos = -1;
        for (int i = 0; i < first.length(); ++i) {
            if (first.charAt(i) != second.charAt(i)) {
                if(pos != -1) {
                    pos = i;
                } else {
                    return -1;
                }                
            }
        }
        return pos;
    }
    
    /*
     * first and second differ at only one place : (0 - 1), other : (number - x)
     * return position or -1
     */
    private int matchSecondKind(String first, String second) {
        int pos = -1;
        for (int i = 0; i < first.length(); ++i) {
            char charFirst = first.charAt(i);
            char charSecond = second.charAt(i);
            if (charFirst != charSecond) {
                if (charSecond != 'x') {                    
                    if (pos != -1) {
                        return -1;
                    } else if (charFirst != 'x') {                        
                        pos = i;
                    } else return -1;                   
                } 
            }
        }        
        return pos;        
    }
    
    private void primeImplicantStep() {        
        HashSet<String> all = new HashSet<String>();
        for(String a : origin) {
            ArrayList<String> tmp = Util.span(a);
            for(String str : tmp) {
                all.add(str);
            }
        }
                        
        ArrayList<ArrayList<String>> primes = new ArrayList<ArrayList<String>>();        
        for (int i = 0; i < terms.size(); ++i) {
            ArrayList<String> cur = Util.span(terms.get(i));                        
            primes.add(Util.span(terms.get(i)));
        }
        
        int bestSolution = -1, minMember = terms.size() + 1;
        int maxMember = terms.size();
        for (int n = 1; n <= (1 << maxMember) - 1; ++n) {
            HashSet<String> thisState = new HashSet<String>();
            int countMember = 0;
            for (int dig = 0; dig < maxMember; dig++) {
                if ((n & (1 << dig)) != 0) {                    
                    countMember++;                    
                    for (int i = 0; i < primes.get(dig).size(); ++i) {
                        String tmp = primes.get(dig).get(i);                        
                        thisState.add(tmp);
                    }
                }
            }                        
            if (countMember < minMember) {
                //Check cover all 
                boolean isOK = true;
                for (String term : all) {
                    if (!thisState.contains(term)) {
                        isOK = false;
                        break;
                    }
                }
                if (isOK) {
                    bestSolution = n;
                    minMember = countMember;
                }
            }
        }                
        ArrayList<String> finalSolution = new ArrayList<String>();
        for (int dig = 0; dig < maxMember; ++dig) {
            if ((bestSolution & (1 << dig)) != 0) {
                finalSolution.add(terms.get(dig));
            }
        }
        terms = (ArrayList<String>) finalSolution.clone();
    }
    
    private static int countX(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); ++i) { 
            if (str.charAt(i) == 'x') {
                count++;
            }
        }
        return count;
    }        
}