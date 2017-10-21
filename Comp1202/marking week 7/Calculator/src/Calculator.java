
public class Calculator {
        Double x;
        /*
        * Chops up input on operator then decides whether to add or multiply.
        * If the string does not contain a valid format returns null.
        */
        public Double x(String x){
        		x = x.replaceAll("\\s","");
        		if (x.contains("+")){
        			this.x = new Double(x.split("\\+")[0]);
        			return x(new Double (x.split("\\+")[1]));
        		}
        		else if (x.contains("x")){
        			this.x = new Double(x.split("x")[0]);
        			return x(Double.parseDouble(x.split("x")[1]));
        		}
        		else
        			return null;
        }

        /*
        * Adds the parameter x to the instance variable x and returns the answer as a Double.
        */
        public Double x(Double x){
                System.out.println("== Adding ==");
                return new Double(this.x + x);
        }

        /*
        * Multiplies the parameter x by instance variable x and return the value as a Double.
        */
        public Double x(double x){
                System.out.println("== Multiplying ==");
                return new Double(this.x * x);
        }

}
