package core;

import java.util.ArrayList;

/**
 * @author thucdx
 */
public class LogicalExpressionMinimizer {    
    public static void main(String[] args) {               
        String exp1 = "a'bcd' + ab'c'd' + ab'c'd + ab'cd' + ab'cd + abc'd' + abc'd + abcd'"; // expect : ab' + ac' + bcd'
        String exp2 = "a + ab + cd + cd'" ;
        String exp3 = "xy + xy' +z+xz+d+g+s+e+fr+fd";
        Expression a = new Expression(exp3);
        ArrayList<String> terms = a.termsInReadableForm();        
        System.out.println("Get readable form : ");
        for (int i = 0; i < terms.size(); ++i) {
            System.out.println(terms.get(i));
        }
        
        QuineMcCluskeyAlgorithm minizer = new QuineMcCluskeyAlgorithm();
        minizer.optimize(a);        
        ArrayList<String> tmp = a.termsInReadableForm();
        System.out.println("Simplify : ");
        for (int i = 0; i < tmp.size(); ++i) {
            System.out.print(tmp.get(i));
            if(i < tmp.size() - 1) System.out.print(" + ");
        }
        System.out.println("");
    }
}
