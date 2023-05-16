package ModelPackage;

public class ModelHostTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testInitGame();
		
	}
	
	 static void testInitGame() {
		//testing init game:
		ModelHost model_host=new ModelHost();
		System.out.println(model_host.getGameState().listOfPlayers);
		model_host.addAplayer("player1");
		model_host.addAplayer("player2");
		model_host.addAplayer("player3");
		model_host.addAplayer("player4");
		model_host.addAplayer("player5");
		//initialize game:
		System.out.println(model_host.getGameState().listOfPlayers); 
		model_host.initGame();
		System.out.println(model_host.getGameState().listOfPlayers); 
		//printing to verify:
		for(int i=0;i<model_host.getGameState().getCurrentNumOfPlayers();i++) {
			Player currPlayer=model_host.getGameState().listOfPlayers.get(i);
			for(int j=0;j<7;j++) {
				System.out.print(currPlayer.myTiles.get(j).letter);
			}
		}
	}
	

}
