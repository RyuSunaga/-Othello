import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardGUI extends JFrame implements MouseListener{

	/*public static void main(String[] args){
		int[][] BoardTmp  = { {0,0,0,0,0,0,0,0},
				 			  {0,0,0,0,0,0,0,0},
				 			  {0,0,0,0,0,0,0,0},
				 			  {0,0,0,1,-1,0,0,0},
				 			  {0,0,0,-1,1,0,0,0},
				 			  {0,0,0,0,0,0,0,0},
				 			  {0,0,0,0,0,0,0,0},
				 			  {0,0,0,0,0,0,0,0}
		   					};

		BoardGUI BG = new BoardGUI(BoardTmp);
		BG.SelectCell();
		System.out.println("SelectCell : " + BG.SelectPoint);
	}
	*/
	
	
	int[][] Board_ = new int[8][8]; 
	int PX;
	int PY;
	int SelectPoint;
	boolean IsContinue;

	BoardGUI(int Board[][],boolean IsHuman){
		int appWidth = 1000;
		int appHeight = 1000;
		int x,y;
		
		IsContinue = IsHuman;

		for(y = 0; y < 8;y++)for(x = 0; x < 8; x ++)Board_[y][x] = Board[y][x];

		super.setTitle("どこに打ちますか??");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.getContentPane().setPreferredSize(new Dimension(appWidth,appHeight));
		
		if(!IsHuman){
			super.setTitle("AIの打った手");
			super.setBounds(1050, 0, appWidth, appHeight);
		}
		super.pack();
		
		JPanel p = new JPanel();
		p.setBackground(Color.GREEN);
		super.add(p);
		setVisible(true);
	}
	
	//マウスクリックのイベントでタッチした場所の座標を取得して返す関数
	//これを使うことでマウスを使って石を置く場所を選択できる
	public int SelectCell(){
		try{
			while(this.IsContinue){
				addMouseListener(this);
				Thread.sleep(1000);
			}
		}
		catch(Exception e){}
		int x = (this.PX-15) / 125;
		int y = (this.PY-65) / 125;
		
		this.SelectPoint = x+10*y;
		System.out.println(SelectPoint);
		return this.SelectPoint;
	}


	@Override
	public void paint(Graphics g) {
		super.paint(g);
		MakeBoard(g);
	}

	//引数の配列に合わせてオセロの盤を作る
	void MakeBoard(Graphics g) {
		int X = 15;
		int Y = 65;
		for(int i = 0; i <= 8; i++){
			//縦線を引く
			g.drawLine(X, 0, X, 1065);
			//横線を引く
			g.drawLine(0, Y, 1015, Y);
			X += 125;
			Y += 125;
		}
		
		int x,y;
		for(y = 0; y < 8; y++)for(x = 0; x < 8; x++){
			if(Board_[y][x] == 1/*BLACK*/){
				g.setColor(Color.BLACK);
				//System.out.println("BLACK");
				g.fillOval(30+125*x, 83+125*y, 90, 90);
			}
			else if(Board_[y][x] == -1/*WHITE*/){
				g.setColor(Color.WHITE);
				//System.out.println("WHITE");
				g.fillOval(30+125*x, 83+125*y, 90, 90);
			}
			else if(Board_[y][x] == 2){
				g.setColor(Color.GRAY);
				//System.out.println("WHITE");
				g.fillOval(30+125*x, 83+125*y, 90, 90);
			}
		}
	}



	@Override
	//マウスでタッチした座標を後で配列用の整数に変換するために保存しておく
	//一回タッチしたら終わるようにするためにIsContinueをfalseに変換する
	public void mouseClicked(MouseEvent e) {
		if(this.IsContinue){
		  Point point = e.getPoint();
		  this.PX = point.x;
		  this.PY = point.y;
		  this.IsContinue = false;
		}
	}



	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}