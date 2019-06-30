//オセロの盤を管理するクラス
//
import java.util.ArrayDeque;
import java.util.Queue;

public class BoardManager {
	
	//盤のサイズ(縦)
	static int H_ = 8;
	static int W_ = 8;
	static int SIZE_ = 64;
	
	//石の色
	static int SPACE_ = 0;
	static int BLACK_ = 1;
	static int WHITE_ = -1;
	
	//評価関数を使うときに必要になる値
	int BlackIsPutNum_ = 0;
	int WhiteIsPutNum_ = 0;
	int BlackGoodness_ = 0;
	int WhiteGoodness_ = 0;
	
	//盤を表す変数(0->空、1->黒、-1->白)
	int[][] Board_ = new int[H_][W_];
	
	//チェックする際に使うx,yの組
	static int[] DX_ = {1,1,1,-1,-1,-1,0,0};
	static int[] DY_ = {1,0,-1,1,0,-1,1,-1};
 
	
	public BoardManager(int[][] BoardTmp) {
		//盤の初期化
		for(int i = 0; i < H_; i++)for(int j = 0; j < W_;j++)Board_[i][j] = BoardTmp[i][j];
	}
	
	
	
	//現在の盤を表示する関数
	public void ShowBoard(){
		int i,j;
		System.out.println(" - - - - - - - -");
		for(i = 0; i < H_; i++){
			for(j = 0; j < W_; j++){			
				switch(Board_[i][j]){
					case 0/*SPACE*/:{
						System.out.print("|" + " ");
						break;
					}
					case 1/*BLACK*/:{
						System.out.print("|" + "B");
						break;
					}
					case -1/*WHITE*/:{
						System.out.print("|" + "W");
						break;
					}
				}
			}
			System.out.println("|");
			System.out.println(" - - - - - - - -");
		}
		System.out.println();
	}
	
	//AI用のShowBoard
		public void ShowBoardAI(int[][] Board){
			int i,j;
			System.out.println(" - - - - - - - -");
			for(i = 0; i < H_; i++){
				for(j = 0; j < W_; j++){			
					switch(Board[i][j]){
						case 0/*SPACE*/:{
							System.out.print("|" + " ");
							break;
						}
						case 1/*BLACK*/:{
							System.out.print("|" + "B");
							break;
						}
						case -1/*WHITE*/:{
							System.out.print("|" + "W");
							break;
						}
					}
				}
				System.out.println("|");
				System.out.println(" - - - - - - - -");
			}
			System.out.println();
		}
	
	//そのマスに石が置けるか判断する関数
	//石が置けるならtrue,置けないならfalseを返す
	public boolean IsPutStone(int color, int x, int y){
		//置くのが石じゃなかったら論外
		if(color != WHITE_ && color != BLACK_){
			return false;
		}
		//置く場所が空きますじゃなかったら論外
		if(Board_[y][x] != SPACE_){
			return false;
		}
		
		//置きたい地点
		int dx;
		int dy;
		
		//石は相手の石を取れるマスにしか置くことが出来ない
		//置きたい石が黒の場合
		if(color == WHITE_){
			
			boolean IsReverseColor = false;
			boolean IsSameColor = false;
			
			//8方向分調べる
			for(int i = 0; i < 8; i++){
				//ループのたびに置きたい地点に設定する
				dx = x + DX_[i];
				dy = y + DY_[i];
				
				IsReverseColor = false;
				IsSameColor = false;
				
				if((0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)){
				while(Board_[dy][dx] != SPACE_ /*&& (0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)*/){
					if(Board_[dy][dx] == BLACK_){
						IsReverseColor = true;
					}
					if(Board_[dy][dx] == WHITE_){
						//すでに反対の色が見つかっていればこの方向で相手の色がとれる
						if(IsReverseColor){
							return true;
						}
						//相手の色が見つかっていないならばこの方向で相手の石はとれないことになる
						else{
							IsSameColor = true;
						}
					}
					
					//相手の色が見つかっていない状況で自分と同じ色の石が見つかった場合次の方向を調べる
					if(IsSameColor){
						break;
					}
					
					//現在調べている方向に1進める
					dx += DX_[i];
					dy += DY_[i];
					if(!((0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)))break;
				}
				}
			}
		}
		//置きたい石が白の場合
		else if(color == BLACK_){
		
			boolean IsReverseColor = false;
			boolean IsSameColor = false;
			
			//8方向分調べる
			for(int i = 0; i < 8; i++){
				//ループのたびに置きたい地点に設定する
				dx = x + DX_[i];
				dy = y + DY_[i];
				
				IsReverseColor = false;
				IsSameColor = false;
				if((0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)){
				while(Board_[dy][dx] != SPACE_ /*&& (0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)*/){
					if(Board_[dy][dx] == WHITE_){
						IsReverseColor = true;
					}
					if(Board_[dy][dx] == BLACK_){
						//すでに反対の色が見つかっていればこの方向で相手の色がとれる
						if(IsReverseColor){
							return true;
						}
						//相手の色が見つかっていないならばこの方向で相手の石はとれないことになる
						else{
							IsSameColor = true;
						}
					}
					
					//相手の色が見つかっていない状況で自分と同じ色の石が見つかった場合次の方向を調べる
					if(IsSameColor){
						break;
					}
					
					//現在調べている方向に1進める
					dx += DX_[i];
					dy += DY_[i];
					if(!((0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)))break;
				}
				
			}
			}
		}
		return false;
	}
	
	//そのマスに石が置けるか判断する関数
		//石が置けるならtrue,置けないならfalseを返す
	//AIが評価関数の計算をするように使う
	public boolean IsPutStoneAI(int[][] Board, int color, int x, int y){
			//System.out.println("IsPutStoneAI");
		
			//置くのが石じゃなかったら論外
			if(color != WHITE_ && color != BLACK_){
				//System.out.println("論外");
				return false;
			}
			//置く場所が空きますじゃなかったら論外
			if(Board[y][x] != SPACE_){
				//System.out.println("空いていない");
				return false;
			}
			
			//置きたい地点
			int dx;
			int dy;
			
			//石は相手の石を取れるマスにしか置くことが出来ない
			//置きたい石が黒の場合
			if(color == WHITE_){
				
				boolean IsReverseColor = false;
				boolean IsSameColor = false;
				
				//8方向分調べる
				for(int i = 0; i < 8; i++){
					//ループのたびに置きたい地点に設定する
					dx = x + DX_[i];
					dy = y + DY_[i];
					
					IsReverseColor = false;
					IsSameColor = false;
					
					if((0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)){
					while(Board[dy][dx] != SPACE_ /*&& (0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)*/){
						if(Board[dy][dx] == BLACK_){
							IsReverseColor = true;
						}
						if(Board[dy][dx] == WHITE_){
							//すでに反対の色が見つかっていればこの方向で相手の色がとれる
							if(IsReverseColor){
								return true;
							}
							//相手の色が見つかっていないならばこの方向で相手の石はとれないことになる
							else{
								IsSameColor = true;
							}
						}
						
						//相手の色が見つかっていない状況で自分と同じ色の石が見つかった場合次の方向を調べる
						if(IsSameColor){
							break;
						}
						
						//現在調べている方向に1進める
						dx += DX_[i];
						dy += DY_[i];
						if(0 > dx || dx > 7 || 0 > dy || dy > 7)break;
					   }
					}
				}
			}
			//置きたい石が白の場合
			else if(color == BLACK_){
			
				boolean IsReverseColor = false;
				boolean IsSameColor = false;
				
				//8方向分調べる
				for(int i = 0; i < 8; i++){
					//ループのたびに置きたい地点に設定する
					dx = x + DX_[i];
					dy = y + DY_[i];
					
					IsReverseColor = false;
					IsSameColor = false;
					if((0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)){
					if(Board[dy][dx] == WHITE_){
					while(Board[dy][dx] != SPACE_ /*&& (0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)*/){
						if(Board[dy][dx] == WHITE_){
							IsReverseColor = true;
						}
						if(Board[dy][dx] == BLACK_){
							//すでに反対の色が見つかっていればこの方向で相手の色がとれる
							if(IsReverseColor){
								return true;
							}
							//相手の色が見つかっていないならばこの方向で相手の石はとれないことになる
							else{
								IsSameColor = true;
							}
						}
						
						//相手の色が見つかっていない状況で自分と同じ色の石が見つかった場合次の方向を調べる
						if(IsSameColor){
							break;
						}
						
						//現在調べている方向に1進める
						dx += DX_[i];
						dy += DY_[i];
						if(0 > dx || dx > 7 || 0 > dy || dy > 7)break;
					}
					}
				}
				}
			}
			return false;
		}
	
	//引数の石が置けるかどうか判断する
	public boolean IsFinish(int color){
		int x;
		for(int y = 0; y < H_; y++)for(x = 0; x < W_; x++)if(IsPutStone(color, x, y))return false;
		return true;
	}
	
	//IsFinishのAI盤
	public boolean IsFinishAI(int[][] Board, int color){
		int x;
		for(int y = 0; y < H_; y++)for(x = 0; x < W_; x++)if(IsPutStoneAI(Board,color, x, y))return false;
		return true;
	}
	
	//石を取り除く関数
	//引数の座標の石を調べそこから八方向の間に取ることのできる石を調べ
	//取れる石ならばひっくり返す
	public void ChangeStone(int x, int y){
		//スタート地点
				int dx = x;
				int dy = y;
				
				//取る石の座標はqueueで保管する
				Queue<Integer> QX = new ArrayDeque<Integer>();
				Queue<Integer> QY = new ArrayDeque<Integer>();
				
				//置きたい石が白の場合
				if(Board_[dy][dx] == WHITE_){
					
					//8方向分調べる
					for(int i = 0; i < 8; i++){
						//queueを初期化
						QX.clear();
						QY.clear();
						
						//ループのたびに置きたい地点に設定する
						dx = x + DX_[i];
						dy = y + DY_[i];
						
						if((0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)){
							if(Board_[dy][dx] == BLACK_){
								QX.add(dx);
								QY.add(dy);
								while(Board_[dy][dx] != SPACE_){
									if(Board_[dy][dx] == WHITE_){
										//保存した座標の石を全てWHITE_に変える
										while(!QX.isEmpty()){
											Board_[QY.peek()][QX.peek()] = WHITE_;
											QX.remove();
											QY.remove();
										}
										break;
									}
									//現在調べている方向に1進める
									dx += DX_[i];
									dy += DY_[i];
							
									QX.add(dx);
									QY.add(dy);
									if(dx < 0 || dx > 7 || dy < 0 || dy > 7)break;
								}
							}
						}
					}
				}
				//置きたい石が黒の場合
				else if(Board_[dy][dx] == BLACK_){
					
					//8方向分調べる
					for(int i = 0; i < 8; i++){
						//queueを初期化
						QX.clear();
						QY.clear();
						
						//ループのたびに置きたい地点に設定する
						dx = x + DX_[i];
						dy = y + DY_[i];
						
						if((0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)){
							if(Board_[dy][dx] == WHITE_){
								QX.add(dx);
								QY.add(dy);
								while(Board_[dy][dx] != SPACE_){
									if(Board_[dy][dx] == BLACK_){
										//保存した座標の石を全てWHITE_に変える
										while(!QX.isEmpty()){
											Board_[QY.peek()][QX.peek()] = BLACK_;
											QX.remove();
											QY.remove();
										}
										break;
									}
									//現在調べている方向に1進める
									dx += DX_[i];
									dy += DY_[i];
							
									QX.add(dx);
									QY.add(dy);
									if(dx < 0 || dx > 7 || dy < 0 || dy > 7)break;
								}
							}
						}
					}
				}
	}
	
	//AI用のChangeStone
	//引数に配列を指定可能(盤を引数にする)
	public void ChangeStoneAI(int[][] Board,int x, int y){
		//スタート地点
		int dx = x;
		int dy = y;
		
		//取る石の座標はqueueで保管する
		Queue<Integer> QX = new ArrayDeque<Integer>();
		Queue<Integer> QY = new ArrayDeque<Integer>();
		
		//置きたい石が白の場合
		if(Board[dy][dx] == WHITE_){
			
			//8方向分調べる
			for(int i = 0; i < 8; i++){
				//queueを初期化
				QX.clear();
				QY.clear();
				
				//ループのたびに置きたい地点に設定する
				dx = x + DX_[i];
				dy = y + DY_[i];
				
				if((0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)){
					if(Board[dy][dx] == BLACK_){
						QX.add(dx);
						QY.add(dy);
						while(Board[dy][dx] != SPACE_){
							if(Board[dy][dx] == WHITE_){
								//保存した座標の石を全てWHITE_に変える
								while(!QX.isEmpty()){
									Board[QY.peek()][QX.peek()] = WHITE_;
									QX.remove();
									QY.remove();
								}
								break;
							}
							//現在調べている方向に1進める
							dx += DX_[i];
							dy += DY_[i];
					
							QX.add(dx);
							QY.add(dy);
							if(dx < 0 || dx > 7 || dy < 0 || dy > 7)break;
						}
					}
				}
			}
		}
		//置きたい石が黒の場合
		else if(Board[dy][dx] == BLACK_){
			
			//8方向分調べる
			for(int i = 0; i < 8; i++){
				//queueを初期化
				QX.clear();
				QY.clear();
				
				//ループのたびに置きたい地点に設定する
				dx = x + DX_[i];
				dy = y + DY_[i];
				
				if((0 <= dx && dx <= 7) && (0 <= dy && dy <= 7)){
					if(Board[dy][dx] == WHITE_){
						QX.add(dx);
						QY.add(dy);
						while(Board[dy][dx] != SPACE_){
							if(Board[dy][dx] == BLACK_){
								//保存した座標の石を全てWHITE_に変える
								while(!QX.isEmpty()){
									Board[QY.peek()][QX.peek()] = BLACK_;
									QX.remove();
									QY.remove();
								}
								break;
							}
							//現在調べている方向に1進める
							dx += DX_[i];
							dy += DY_[i];
					
							QX.add(dx);
							QY.add(dy);
							if(dx < 0 || dx > 7 || dy < 0 || dy > 7)break;
						}
					}
				}
			}
		}
	}
	
	//評価関数用に各石の置ける数を数える
	//詳しくはAI.javaを参照
	public int CalcIsPutNum(int[][] Board,int Color){
		int ret = 0;
		int x,y;
		for(y = 0; y < 8; y++)for(x = 0; x < 8; x++){
			if(IsPutStoneAI(Board,Color,x,y))ret++;
		}
		return ret;
	}
	
	//CalcGoodness用の変数
	//左上(右,下,斜め),左下(右,上,斜め),右下(左,上,斜め),右上(左,下,斜め)
	int[][][] GX = {{{0,1,2},{0,0,0},{0,1,2}},{{0,1,2},{0,0,0},{0,1,2}},{{7,6,5},{7,7,7},{7,6,5}},{{7,6,5},{7,7,7},{7,6,5}}};
	int[][][] GY = {{{0,0,0},{0,1,2},{0,1,2}},{{7,7,7},{7,6,5},{7,6,5}},{{7,7,7},{7,6,5},{7,6,5}},{{0,0,0},{0,1,2},{0,1,2}}};
	
	//評価関数用にbを計算する
	//詳しくはAI.javaを参照
	public int CalcGoodness(int[][] Board, int Color){
		//初期化
		int ret = 0;
		int i,j;
		
		//まずは黒
		if(Color == BLACK_){
			for(i = 0; i < 4; i++)for(j = 0; j < 3; j++){
				System.out.println(GY[i][j][0] + "," + GX[i][j][0] );
				if(Board[GY[i][j][0]][GX[i][j][0]] == BLACK_){
					ret += 10;
				}
				else if(Board[GY[i][j][0]][GX[i][j][0]] == SPACE_ && Board[GY[i][j][1]][GX[i][j][1]] == SPACE_ && Board[GY[i][j][2]][GX[i][j][2]] == BLACK_){
					ret += 1;
				}
				else if(Board[GY[i][j][0]][GX[i][j][0]] == SPACE_ && Board[GY[i][j][1]][GX[i][j][1]] == BLACK_ && Board[GY[i][j][2]][GX[i][j][2]] == BLACK_){
					ret -= 5;
				}
			}
		}
		//次は白
		else if(Color == WHITE_){
			for(i = 0; i < 4; i++)for(j = 0; j < 3; j++){
				if(Board[GY[i][j][0]][GX[i][j][0]] == WHITE_){
					ret += 10;
				}
				else if(Board[GY[i][j][0]][GX[i][j][0]] == SPACE_ && Board[GY[i][j][1]][GX[i][j][1]] == SPACE_ && Board[GY[i][j][2]][GX[i][j][2]] == WHITE_){
					ret += 1;
				}
				else if(Board[GY[i][j][0]][GX[i][j][0]] == SPACE_ && Board[GY[i][j][1]][GX[i][j][1]] == WHITE_ && Board[GY[i][j][2]][GX[i][j][2]] == WHITE_){
					ret -= 5;
				}
			}
		}
		
		return ret;

	}
	
	
	//石を置く関数
	//置けたならtrue, 置けなかったならばfalseを返す
	public boolean PutStone(int color, int x, int y){
		//まず石が置けるかどうかを判断する
		if(!IsPutStone(color, x, y)){
			System.out.println("置けない");
			return false;
		}
		System.out.println("置ける");
		//石を置く
		Board_[y][x] = color;
		
		//置いた石と反対の石をひっくり返す
		ChangeStone(x,y);
		
		return true;
	}
	
	
	//勝敗判定をする関数
	//黒が勝ちならば1,引き分けなら0,白が勝ちならば-1を返す
	public int WhichColorWin(){
		int score = 0;
		int i,j;
		for(i = 0; i < H_; i++)for(j = 0; j < W_; j++)score += Board_[i][j];
		
		//黒を1白を-1空きを0で表しているので
		//scoreが正なら黒の勝ち0なら引き分け負なら白の勝ち
		if(score > 0){
			return 1;
		}
		else if(score < 0){
			return -1;
		}
		return 0;
	}
	
	//ボードの更新に使う(αβ法の時必要)
	public void UpDateBoard(int[][] Board){
		int i,j;
		for(i = 0; i < H_; i++)for(j = 0; j < W_; j++){
			Board_[i][j] = Board[i][j];
		}
	}


public static void main(String[] args){
	int[][] BoardTmp_ = { {0,0,0,0,0,0,0,0},
		 {0,0,0,0,0,0,0,0},
		 {0,0,0,0,0,0,0,0},
		 {0,0,0,1,-1,0,0,0},
		 {0,0,0,-1,1,0,0,0},
		 {0,0,0,0,0,0,0,0},
		 {0,0,0,0,0,0,0,0},
		 {0,0,0,0,0,0,0,0}
	};

	
	BoardManager BM = new BoardManager(BoardTmp_);
	BM.ShowBoard();
	//BM.Board_[4][2] = 1;
	BM.PutStone(BLACK_, 2, 4);
	BM.ShowBoard();
	
	//BM.PutStone(WHITE_,2,5);
	//BM.ShowBoard();
}
}
