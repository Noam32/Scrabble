package modelPackage;

public class MainTestModel {
	
	//This test assumes that the dictionary server is already running
	public static void main(String[]args) throws Exception {
		System.out.println("this is the test for Model package");
		//firstTestBatch(args);
		secondTest(args);
		
	}
	public static void firstTestBatch(String[]args) throws Exception {
		System.out.println("this is the test for Model package");
		GameStateTest.main(args);
		System.out.println("----------------------------------\n");
		PlayerTest.main(args);
		System.out.println("----------------------------------\n");
		ConnectedBoardTest.main(args);
		System.out.println("----------------------------------\n");
		GuestClientHandlerTest.main(args);
		System.out.println("----------------------------------\n");
		ModelHostTest.main(args);
		System.out.println("----------------------------------\n");
		ModelTest.main(args);
		System.out.println("----------------------------------\n");
		ObjectStreamTest.main(args);
		//System.out.println("\n\n ***TEST 1 :PASSED***");
		
	}
	public static void secondTest(String[]args) {
		ModelGuestTest.main(args);
		System.out.println("\n\n ***TEST 2 :PASSED***");
	}
}
