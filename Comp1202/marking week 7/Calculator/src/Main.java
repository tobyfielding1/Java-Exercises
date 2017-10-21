
public class Main {

	public static void main(String [ ] args){
		TestCalculator myTestCalculator = new TestCalculator();
		boolean parserResult = myTestCalculator.testParser();
		boolean addResult = myTestCalculator.testAdd();
		boolean multiplyResult = myTestCalculator.testMultiplication();
		
		if (parserResult && addResult && multiplyResult)
			System.out.println("congratulations your calculator works");
	}
}
