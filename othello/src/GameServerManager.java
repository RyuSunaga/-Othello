import java.io.IOException;
import java.net.ServerSocket;

public class GameServerManager extends Thread {

	static int SERVER_PORT = 15000;
	
	ServerSocket serverSocket = null;
	
	public GameServerManager(){

	}
	
	
	public void run(){
		
		
		try {
		
			serverSocket = new ServerSocket(SERVER_PORT);
			System.out.println("[ServerManager]サーバソケットを生成しましたポート番号："+String.valueOf(SERVER_PORT)+"で待機します");
			
		}  catch (IOException already_used){
			
			System.out.println("[ERROR]サーバの起動に失敗しました。ポート"+SERVER_PORT+"がすでに使われているようです。"+
								"振込処理は行えません\n振り込みを行う場合システムの再起動を行なって下さい");
			String[] err_messageToView = {"[エラー]","サーバソケットの初期化に失敗しました。システムの再起動を行なって下さい"};
			return;
			
		}
		int SavePoint = 0;
		while(true){
			System.out.println("------------------" + SavePoint);
			System.out.println();
			try {
				
				GameServer GS = new GameServer(serverSocket.accept());
				new Thread(GS).start();
				System.out.println("[Server]接続を受けました");
				SavePoint = GS.SavePoint;
				//System.out.println("---------------------------------" + GS.SavePoint);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
}
