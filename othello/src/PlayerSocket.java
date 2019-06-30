import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;



/**BankSocketクラスは振込などの外部にデータを送信する際に呼び出されるクラスです。
 * 
 * 
 * 
 * @author fvi@
 * @version 0.51
 * 
 * 
 * @param SERVER_PORT TCP/IPソケット通信上で用いる統一ポート番号 15000<BR>
 * 
 * 
 */
public class PlayerSocket{


	Socket socket;

	static int SERVER_PORT = 15000;
	
	public PlayerSocket(String dst_ip,String X_Point, String Y_Point,String NewBoard) {
		
		
		System.out.println("接続開始");
		try {
			
			
			
			socket = new Socket(dst_ip, SERVER_PORT);
			
			
		    /* 準備：データ入力ストリームの定義--ソケットからデータを
		       取ってくる．sok → in  */  
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			
			/* 準備：データ出力ストリームの定義--ソケットにデータを
		       書き込む．  sok ← out */

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					socket.
					getOutputStream()));
			
			//---------------------------------------------------------------1回目
			//最初の接続の振込み処理開始を告げるメッセージ　ここから
			//最後に置いたX座標を送る
			System.out.println("送るのはx" + X_Point);
			writer.write(X_Point);
			writer.newLine();
			writer.flush();
			
			//ここから先　実際のサーバとの通信
			
			
			// 送信先からの戻り値を受信
			//--------------------------------------------Boadrを表示できたかどうかメッセージを受け取る
			System.out.println("メッセージ受信中");
			String response = reader.readLine();
			System.out.println("受信したメッセージ： " + response);
			//-----------------------------------------------------------------ここまで1回目
		
			//----------------------------------------------------------------------2回目
			//最初の接続の振込み処理開始を告げるメッセージ　ここから
			//最後に置いたX座標を送る
			writer.write(Y_Point);
			writer.newLine();
			writer.flush();
	
			// 送信先からの戻り値を受信
			//--------------------------------------------Boadrを表示できたかどうかメッセージを受け取る
			System.out.println("メッセージ受信中");
			response = reader.readLine();
			System.out.println("受信したメッセージ： " + response);
			
			//----------------------------------------------------------ここまで2回目
			
			
			//----------------------------------------------------------------------3回目
			//最初の接続の振込み処理開始を告げるメッセージ　ここから
			//最新の盤を送る
			writer.write(NewBoard);
			writer.newLine();
			writer.flush();
	
			// 送信先からの戻り値を受信
			//--------------------------------------------Boadrを表示できたかどうかメッセージを受け取る
			System.out.println("メッセージ受信中");
			response = reader.readLine();
			System.out.println("受信したメッセージ： " + response);
			
			//----------------------------------------------------------ここまで3回目
			
			
			//以降、通信処理の後片付け
			System.out.println("[Socket]転送システムを終了します");

			reader.close();
			writer.close();

			socket.close();
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("通信ソケットの確立に失敗しました。FWの設定or すでにポートが専有されているかも");
			e.printStackTrace();
		}

	}

}
