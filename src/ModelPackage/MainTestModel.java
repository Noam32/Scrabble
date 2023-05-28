package ModelPackage;

public class MainTestModel {
	
	//This test assumes that the dictionary server is already running
	public static void main(String[]args) throws Exception {
		System.out.println("this is the test for Model package");
		GameStateTest.main(args);
		ConnectedBoardTest.main(args);
		GameStateTest.main(args);
		GuestClientHandlerTest.main(args);
		ModelHostTest.main(args);
		ModelGuestTest.main(args);;
		ModelTest.main(args);
		ObjectStreamTest.main(args);
		PlayerTest.main(args);
		
	}
}
