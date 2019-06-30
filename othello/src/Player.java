

//PlayerはHumanとAIの二種類があるのでそれらの抽象クラスがPlayer
public abstract class Player {
	
	//通信先のIPアドレス
	static String dst_ip;
	static int BLACK_ = 1;
	static int WHITE_ = -1;
	int PlayerColor_;

	/*あとで通信機能を追加するためにSocketとバッファを機能フィールドに加える*/
	public Player(boolean IsBlack,String ip) {
		if(IsBlack){
			PlayerColor_ = BLACK_;
		}
		else{
			PlayerColor_ = WHITE_;
		}
		dst_ip = ip;
	}
	
	//自分の色を表示する
	public void GetMyColor(){
		if(PlayerColor_ == BLACK_)System.out.println("私の色は黒である");
		else System.out.println("私の色は白である");
	}
	 
	/*次に盤に置く座標を、x座標を1の位、y座標を10の位に圧縮して返す
	この関数はHuman型とAIで挙動が変わるのでabstractにする*/
	public abstract int SelectPoint(int Point);
	
	
	

}
