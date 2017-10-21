
public class TestCalculator {
	
	public boolean testParser(){
		boolean success = false;
		Calculator calculator = new Calculator();
		//try{
		if (calculator.x("12 + 5").equals(new Double(17))){
			System.out.println("[OK] basic parsing adds correctly");
			success = true;
		}
		else{
			success = false;
			System.out.println("[FAIL] basic parsing fails to add");
		}
		if (calculator.x("12 x 5").equals(new Double(60))){
			System.out.println("[OK] basic parsing multiplies correctly");
			success = true;
		}
		else{
			success = false;
			System.out.println("[FAIL] basic parsing fails to multiply");
		}
		if (calculator.x("12 [ 3") == null){
			System.out.println("[OK] parser returns null for incorrect operator");
			success = true;
		}
		else{
			success = false;
			System.out.println("[FAIL] Parser does not return null for operators which are not supported.");
			
		}
		return success;
	}
	
	public boolean testAdd(){
		boolean success = false;
		Calculator calculator = new Calculator();
		//testing for +ve numbers
		calculator.x = new Double(6);
		if (calculator.x(new Double(5)).equals(new Double(11))){
			System.out.println("[OK] Calculator adds correctly");
			success = true;
		}
		else{
			success = false;
			System.out.println("[FAIL] Calculator adds incorrectly");
		}	
		
		//testing for -ve numbers
		calculator.x = new Double(-10);
		if (calculator.x(new Double(-5)).equals(new Double(-15))){
			System.out.println("[OK] Calculator adds negatives correctly");
			success = true;
		}
		else {
			success = false;
			System.out.println("[FAIL] Calculator adds negatives incorrectly");
		}	
		return success;
	}
	
	 public boolean testMultiplication(){
		 boolean success = false;
		 Calculator calculator = new Calculator();
		//testing for +ve numbers
		calculator.x = new Double(6);
		double a = 5;
		if (calculator.x(a).equals(new Double(30))){
			System.out.println("[OK] Calculator multiplies correctly");
			success = true;
		}
		else{
			success = false;
			System.out.println("[FAIL] Calculator multiplies incorrectly");
		}	
		//testing for -ve numbers
		calculator.x = new Double(7);
		a = -3;
		if (calculator.x(a).equals(new Double(-21))){
			System.out.println("[OK] Calculator multiplies negatives correctly");
			success = true;
		}
		else{
			success = false;
			System.out.println("[FAIL] Calculator multiplies negatives incorrectly");
		}
		return success;
	 }

}
