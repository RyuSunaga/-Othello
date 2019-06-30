import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class GameServer extends Thread {
	
	
	static int SERVER_PORT = 15000;

	//static ServerSocket ss ;
	private Socket act_socket = null;
	
	int SavePoint;
	
	//Container contentpane;


	public GameServer(Socket accept) {
		//コンストラクタ
		// TODO Auto-generated constructor stub
		this.act_socket = accept;
	}
	
	public void StartCommunication() {
		

		
		try {
			//Socket socket = ss.accept();
			//System.out.println("[Server]"+act_socket.getInetAddress() + "から接続を受けました");

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					act_socket.getInputStream()));

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					act_socket.getOutputStream()));

			

			String X_Point,Y_Point; 
			
			
			//---------------------------------置いたX座標を受け取る
			// 受信するまで待機
			X_Point= reader.readLine();
				
			//int Point = new Integer(inMes).intValue();
			System.out.println(X_Point + "に置くよ");
			
			writer.write("X座標を取得しました");
			writer.newLine();
			writer.flush();
			//---------------------------------ここまで1回目
			
			//-------------------------------------置いたY座標を受け取る
			// 受信するまで待機
			Y_Point= reader.readLine();
							
			//int Point = new Integer(inMes).intValue();
			System.out.println(Y_Point + "に置くよ");
						
			writer.write("Y座標を取得しました");
			writer.newLine();
			writer.flush();
			//------------------------------------ここまで2回目
			
			String NewBoard;
			//-------------------------------------最新の盤を受け取る
			// 受信するまで待機
			NewBoard = reader.readLine();
							
			//int Point = new Integer(inMes).intValue();
			System.out.println("最新の盤を受け取りました/n" + NewBoard);
						
			writer.write("盤サンキュー");
			writer.newLine();
			writer.flush();
			//------------------------------------ここまで3回目
			
			//盤をBoardGUIが呼べるように型変換する
			int[][] NewBoardINT = new int[8][8];
			int i,j;
			for(i = 0; i < 8; i++)for(j = 0; j < 8; j++){
				switch(Integer.valueOf(NewBoard.charAt(i*8+j))){
					case 49:{
						NewBoardINT[i][j] = 0;
						break;
					}
					case 50/*BLACK*/:{
						NewBoardINT[i][j] = 1;
						break;
					}
					case 48/*WHITE*/:{
						NewBoardINT[i][j] = -1;
						break;
					}
				}
				//System.out.println(Integer.valueOf(NewBoard.charAt(i*8+j)));
				//System.out.println(NewBoardINT[i][j]);
			}
			//一番最近に打った手に関しては2に変える
			NewBoardINT[Integer.valueOf(Y_Point).intValue()][Integer.valueOf(X_Point).intValue()] = 2;
			new BoardGUI(NewBoardINT,false);
			
			
			System.out.println("サーバソケットを終了させます");
			//----------------------------------------------------------------ここまで
			//JOptionPane.showMessageDialog(contentpane,"縦" + Y_Point + "横" + X_Point + "に置きました");
			
			reader.close();
			writer.close();
			act_socket.close();
			
			
						

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("[Server]メッセージ通信に失敗しました\n再度やり直してください");
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		StartCommunication();

	}

	public void FinishServer() throws IOException {
		System.out.println("通信を終了させます。To:"+act_socket.getInetAddress().toString());
		
	}

}