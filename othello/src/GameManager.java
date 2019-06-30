import java.awt.Container;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;



//ゲームを全体を管理するクラス
//メインクラス

public class GameManager {
	
	static int[][] Board_ = { {0,0,0,0,0,0,0,0},
			 			      {0,0,0,0,0,0,0,0},
			 			      {0,0,0,0,0,0,0,0},
			 			      {0,0,0,1,-1,0,0,0},
			 			      {0,0,0,-1,1,0,0,0},
			 			      {0,0,0,0,0,0,0,0},
			 			      {0,0,0,0,0,0,0,0},
			 			      {0,0,0,0,0,0,0,0}
						};
	
	BoardManager BM;
	AI AI;
	Human Human;
	
	Container contentpane;
	
	static int test_num = 0;
	
	//GameServer GS1;
	//GameServer GS2;
	
	int BLACK_ = 1;
	int WHITE_ = -1;
	int TurnColor_;
	
	//通信関係
	private static GameServerManager GSM;
	
	//IPアドレス
	String dst_ip;
	
	//AIはターン数で戦法が変わる
	//終盤までは評価関数
	//終盤は石の最大化を目指す
	int TotalTurn_ = 0;
	
	int SelectAction_;
	int SURRENDER_ = -100;
	
	//引数は通信先のipアドレス
	GameManager(String ip){
		
		BM = new BoardManager(Board_);
		//AIが黒
		AI = new AI(true,Board_,ip);
		//	人間が白
		Human = new Human(false,ip);
		
		//サーバーを起動
		if(GSM == null){
			GSM = new GameServerManager();
			GSM.start();
			JOptionPane.showMessageDialog(contentpane,
					"ゲーム開始！ \n あなたは白で先行です\n 左に表示されたボードの中で自分が石を置きたい場所をタッチしてください!\n" 
					+ " 右には最後に石が置かれた場所をグレーにした状態のボードが表示されます。\n");
		}
		dst_ip = ip;
		
	}
	
	
	//メイン関数
	public void MAIN(){
		BoardGUI BGUI;
		
		//石を置く座標
		int Point;
		int x,y;
		int i,j;
		int NowColor = WHITE_;
		String StringBoard = "";
		
		while(!BM.IsFinish(NowColor) || !BM.IsFinish(NowColor*(-1))){
			//盤を更新
			AI.UpDateBoard(BM.Board_);
			AI.AIBM.UpDateBoard(BM.Board_);
			
			if(NowColor == AI.PlayerColor_){
				System.out.println("AIのターンです。");
				
				if(!BM.IsFinish(NowColor)){
					
					Point = AI.SelectPoint(0);
					x = Point % 10;
					y = Point / 10;
				
					//サーバーに送る用のデータ
					StringBoard = "";
					for(i = 0; i < 8; i++)for(j = 0; j < 8; j++)StringBoard += (BM.Board_[i][j]+1);
						
					System.out.println("サーバーにデータを送ります");
					//サーバーに情報を送る
					new PlayerSocket("localhost",Integer.toString(x),Integer.toString(y),StringBoard);
					
					BM.PutStone(NowColor, x, y);
					
				}
				else{
					System.out.println("置ける場所がないのでAIのターンはパスです");
					JOptionPane.showMessageDialog(contentpane,"置ける場所がないのでAIのターンはパスです");
				}
				
				NowColor *= -1;
			}else{
				System.out.println("あなたのターンです。");
				
				if(!BM.IsFinish(NowColor)){
					//手を選ぶ
					while(true){
						BGUI = new BoardGUI(BM.Board_,true);
						Point = BGUI.SelectCell();
						x = Point % 10;
						y = Point / 10;
						//指定した座標が置ける場所ならばループを抜ける
						if(BM.IsPutStone(NowColor, x, y))break;
					}
			
					//サーバーに送る用のデータ
					StringBoard = "";
					for(i = 0; i < 8; i++)for(j = 0; j < 8; j++)StringBoard += (BM.Board_[i][j]+1);
				
					System.out.println("サーバーにデータを送ります");
					//サーバーに情報を送る
					new PlayerSocket("localhost",Integer.toString(x),Integer.toString(y),StringBoard);
					
					BM.PutStone(NowColor, x, y);
				}
				else{
					//System.out.println("置ける場所がないのでパスです");
					JOptionPane.showMessageDialog(contentpane,"あなたの置ける場所はないのでパスです");
				}
				
				NowColor *= -1;
			}
			
		}
		
		//ここらへんは後で画面に表示させるように変更する
		System.out.println("ゲーム終了");
		if(BM.WhichColorWin() > 0){
			JOptionPane.showMessageDialog(contentpane,"AIの勝ちです...");
		}
		else if(BM.WhichColorWin() < 0){
			JOptionPane.showMessageDialog(contentpane,"あなたの勝ちです!");
		}
		else{
			JOptionPane.showMessageDialog(contentpane,"引き分けです");
		}
		return;
	}
	
	public static void main(String[] args){
		GameManager GM;
		try {
			GM = new GameManager(InetAddress.getLocalHost().getHostAddress());
			//メイン
			GM.MAIN();
		} catch (UnknownHostException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		
	}
		
		
}
	


