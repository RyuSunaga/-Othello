//人間プレイヤーのためのクラス

public class Human extends Player {

	public Human(boolean IsBlack,String ip) {
		super(IsBlack,ip);
	}

	
	//Human型の場合はBoardGUIのSelectCellから置きたい場所の
	//座標を取得してBoardManagerのPutStoneに代入する
	@Override
	public int SelectPoint(int Point) {
		return Point;
	}




}
