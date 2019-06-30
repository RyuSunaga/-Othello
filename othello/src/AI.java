//AIプレイヤー

public class AI extends Player {
	static int SURRENDER_ = -1;
	static int SPACE_ = 0;
	//両プレイヤー合わせて40回打つまではEvaluation1を使う
	//40ターン以降は石の数を最大にすることを目指す
	int TURN_ = 0;
	
	//αβ法を使うのに現在の盤の状況を使う常に保管したいので
	//盤を変数として持っておく
	//プレイヤー同士石を置くたびに更新する必要がある
	int[][] AIBoard_ = 	{ {0,0,0,0,0,0,0,0},
			 			  {0,0,0,0,0,0,0,0},
			 			  {0,0,0,0,0,0,0,0},
			 			  {0,0,0,1,-1,0,0,0},
			 			  {0,0,0,-1,1,0,0,0},
			 			  {0,0,0,0,0,0,0,0},
			 			  {0,0,0,0,0,0,0,0},
			 			  {0,0,0,0,0,0,0,0}
	   					}; 
	BoardManager AIBM = new BoardManager(AIBoard_);

	public AI(boolean IsBlack,int[][] Board,String ip) {
		super(IsBlack,ip);
		//更新
		
	}
	
	
	public void UpDateBoard(int[][] Board){
		int x,y;
		for(y = 0; y < 8; y++)for(x = 0; x < 8; x++)AIBoard_[y][x] = Board[y][x];
	}
	
	//評価関数
	//盤を引数にしてその盤での評価値を返す
	public int EvaluateFunction1(int[][] Board){
		/*	A = 自分の着手可能数 - 相手の着手可能数
			B = 自石の四隅周辺の形の良さ - 相手石の四隅周辺の形の良さ
			k = 適当な係数
			スコア = A + kB
			評価関数の設定は
			https://qiita.com/na-o-ys/items/10d894635c2a6c07ac70
			から持ってきました(アクセス日2018.12.29)
		*/
		//a,b計算用の下準備
		int BlackIsPutNum = AIBM.CalcIsPutNum(Board,BLACK_);
		int WhiteIsPutNum = AIBM.CalcIsPutNum(Board,WHITE_);
		int BlackGoodness = AIBM.CalcGoodness(Board,BLACK_);
		int WhiteGoodness = AIBM.CalcGoodness(Board,WHITE_);
		
		
		int a,b;
		//AIが黒の場合
		if(PlayerColor_ == BLACK_){
			a = BlackIsPutNum - WhiteIsPutNum;
			b = BlackGoodness - WhiteGoodness;
		}
		else{
			a = WhiteIsPutNum - BlackIsPutNum;
			b = WhiteGoodness - BlackGoodness;
		}
		
		return a + 2*b;
	}
	
	public int GetScore(int[][] Board){
		int i,j;
		int ReverseColor = PlayerColor_*(-1);
		int score = 0;
		for(i = 0; i < 8; i++)for(j = 0; j < 8; j++){
			if(Board[i][j] == PlayerColor_)score++;
			else if(Board[i][j] == ReverseColor)score--;
		}
		return score;
	}
	
	//αβ法メイン部分
	//Turnが0になるまではEvaluation()を評価関数とする
	//Turnが0になってからは石の数を最大化することを目指す
	public int alpha_beta(int[][] Board, int Turn){
		
		int MaxValue = Integer.MIN_VALUE;
		int ret_x = -1,ret_y = -1;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		
		int[][] CopyBoard = new int[8][8];
		int i,j;
		for(i = 0; i < 8; i++)for(j = 0; j < 8; j++)CopyBoard[i][j] = Board[i][j];
		
		//一番価値の高い手を探索する
		int x,y;
		int value = MaxValue;
		for(y = 0; y < 8; y++)for(x = 0; x < 8; x++){
			if(AIBM.IsPutStoneAI(Board, /*PlayerColor_*/1, x, y)){
				Board[y][x] = PlayerColor_;
				//ここに石の変化を書く
				AIBM.ChangeStoneAI(Board, x, y);
				//AIBM.ShowBoardAI(Board);
				value = alpha_beta_calc2(Board,PlayerColor_*(-1),60, Turn ,alpha,beta);
				//System.out.println("x = " + x + " y = " + y + " value = " + value);
				
				if(value > MaxValue){
					MaxValue = value;
					ret_x = x;
					ret_y = y;
				}
				//もとに戻す
				for(i = 0; i < 8; i++)for(j = 0; j < 8; j++)Board[i][j] = CopyBoard[i][j];
			}
		}
		
		return ret_x + 10*ret_y;
	}
	
	public int alpha_beta_calc2(int[][] NowBoard,int NowColor,int depth,int Turn, int alpha, int beta){
		if(depth == 0 || AIBM.IsFinishAI(NowBoard, NowColor)){
			return this.EvaluateFunction1(NowBoard);
		}
		else if(Turn < 0 || AIBM.IsFinishAI(NowBoard, NowColor)){
			return this.GetScore(NowBoard);
		}
		int i,j;
		int x,y;
		
		int MaxValue = Integer.MIN_VALUE;
		int MinValue = Integer.MAX_VALUE;
		
		//盤のコピー用の変数
		int[][] CopyBoard = new int[8][8];
		for(y = 0; y < 8; y++)for(x = 0; x < 8;x++)CopyBoard[y][x] = NowBoard[y][x];
		
		//Max木(AIのターンの場合)
		if(NowColor == PlayerColor_){
			for(y = 0; y < 8; y++)for(x = 0; x < 8; x++){
				//打てるかどうか
				if(AIBM.IsPutStoneAI(NowBoard,NowColor, x, y)){
					CopyBoard[y][x] = NowColor;
					
					//自分の次のノードの評価値を得る
					int nextvalue = alpha_beta_calc2(CopyBoard,NowColor*(-1),depth-1,Turn-1,alpha,beta);
					//戻す
					for(i = 0; i < 8; i++)for(j = 0; j < 8; j++)CopyBoard[i][j] = NowBoard[i][j];
					//カット
					if(nextvalue >= beta){
						return nextvalue;
					}
					//更新
					if(nextvalue > MaxValue){
						MaxValue = nextvalue;
						alpha = Math.max(alpha, MaxValue);
					}
					
				}
			}
		}
		//Min木(人間のターンの場合)
		else{
			for(y = 0; y < 8; y++)for(x = 0; x < 8; x++){
				//打てるかどうか
				if(AIBM.IsPutStoneAI(NowBoard,NowColor, x, y)){
					CopyBoard[y][x] = NowColor;
					
					//自分の次のノードの評価値を得る
					int nextvalue = alpha_beta_calc2(CopyBoard,NowColor*(-1),depth-1,Turn-1,alpha,beta);
					//戻す
					for(i = 0; i < 8; i++)for(j = 0; j < 8; j++)CopyBoard[i][j] = NowBoard[i][j];
					//カット
					if(nextvalue <= alpha){
						return nextvalue;
					}
					//更新
					if(nextvalue < MinValue){
						MinValue = nextvalue;
						beta = Math.min(beta, MinValue);
					}
				}
			}
		}
		return 0;
	}
	

	@Override
	//打つ座標を探索する
	public int SelectPoint(int p) {
			TURN_ -= 2;
			return alpha_beta(AIBoard_,TURN_);
		
	}
	
	
	
}
