
public class math__operation {
	
    double add(double num1, double num2) {
        double answer = num1+num2;
        return Math.round(answer*1000d)/1000d;  }
 
    double subtract(double num1, double num2) {
        double answer = num1-num2;
        return Math.round(answer*1000d)/1000d;          
    }

    double multiply(double num1, double num2) {
        double answer = num1*num2;
        return Math.round(answer*1000d)/1000d;          
    }

    double divide(double num1, double num2) {
        double answer = num1/num2;
        return Math.round(answer*1000d)/1000d;    
    }
}