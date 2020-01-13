package practise01;


import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;





public class DayThree {
//������ѧϰSwing�ĵ������γ̣�С��Ϸ�ɻ���ս
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new MyJFrame();
            }
		});
	}
}

class MyJFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int count=1;
	MyJFrame(){
		/*���ñ���*/
		setTitle("Shoot Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*���óߴ��Լ�λ��*/
		setSize(new Dimension(Static.BG.getIconWidth(),Static.BG.getIconHeight()));
		setLocationRelativeTo(null);//�������
		setResizable(false);//�޷��ı䴰��ߴ�
		
		/*���ñ���ͼƬ*/
		JLabel label=new JLabel(Static.BG);
		label.setBounds(0,0,Static.BG.getIconWidth(),Static.BG.getIconHeight());
		getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
		
		/*�����ͣ��ť*/
		JButton gameL= new JButton(Static.stopg);
		 gameL.setBounds(420, 20, Static.stopg.getIconWidth(), Static.stopg.getIconHeight());
		 gameL.setBorder(null);
		 gameL.setFocusPainted(false);//��ȥ����Ŀ�  
		 gameL.setContentAreaFilled(false);//��ȥĬ�ϵı������ 
		 gameL.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e2) {
				 if(Static.state==1||Static.state==2) {
					 if(count%2!=0) {
						 Static.state=2;
						 gameL.setIcon(Static.startg);
					 }
					 if(count%2==0) {
						 Static.state=1;
						 gameL.setIcon(Static.stopg);
					 }
					 if(count==1){
						 Static.state=2;
						 gameL.setIcon(Static.startg);
					 } 
					 count++;
				 }
			}
		});
		 getContentPane().add(gameL);
		 
		((JComponent) getContentPane()).setOpaque(false);//��������͸������Ȼ������BGͼƬ
		
		/*�������*/
		ShootGame panel=new ShootGame();
		add(panel);
		
		/*��������Ϊ�ɼ�*/
		setVisible(true);
	}	
}

class ShootGame extends JPanel{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image offScreenImage = null;
	Hero hero=new Hero();//Ӣ�ۻ�����
	JProgressBar n=new JProgressBar() ;//���Ӣ��Ѫ��
	JLabel hp= new JLabel("HP: ");
	Timer timer=new Timer();//������ʱ������
	Toolkit tk = Toolkit.getDefaultToolkit(); //�������ڼ���ȫ�ּ�����"���ϼ�"
	Random rand=new Random();
	private int mspeed=2;//Ӣ���ƶ��ٶ�
	private int broom=20;//�ӵ��ܼ���
	private int shootIndex=0,eplane=0;//�ӵ�������� �о�����
	private int count=0;
	private Bullet[] bullets={};//�ӵ�����
	private EmPlane[] flyings={};//�л�����
	 ShootGame(){
		 	setLayout(null);
			// n.setBounds(40, 20,120, 20);
			 n.setLocation(40, 20);
			 n.setSize(new Dimension(120,20));
			 n.setValue(hero.getLife());
			 //n.setBackground(Color.WHITE);
			 n.setForeground(Color.red);
			 n.setOpaque(false);
			 n.setStringPainted(true);
			 	
			 hp.setFont(new Font(Font.SANS_SERIF,Font.BOLD+ Font.ITALIC,17));
			 hp.setLocation(10, 10);
			 hp.setSize(new Dimension(40,40));	 
			 
			 add(hp);
			 add(n);
			
			 timer.schedule(new TimerTask(){
				public void run() {
					if(hero.y>550&&Static.state==0) {	//Ӣ���볡����
						      hero.y-=3;
						      hero.step();
					 }
					if(Static.state==Static.RUNNING){	//��ʼ��Ϸ
					Heromove();			//Ӣ���ƶ�
					shootAction();		//���
					EplaneAction();		//�о��볡
					bandAction();		//��ײ���
					}
					repaint();
					}
			},10,10);	
			
		 /*��������Ϊ͸��*/
		 setOpaque(false);
	}
	/*Ӣ�����ż����������Ҽ��ƶ�*/
	 public void Heromove() {
		if(Static.left==true) {
			if(hero.outBoundsx1())hero.x-=mspeed;
		}
		if(Static.right==true) {
			if(hero.outBoundsx2())hero.x+=mspeed;
		}
		if(Static.up==true) {
			if(hero.outBoundsy1())hero.y-=mspeed;
		}
		if(Static.down==true) {
			if(hero.outBoundsy2())hero.y+=mspeed;
		}
	 }
	 /**������ɵл�����*/
	 public EmPlane nextOne(){
			count=rand.nextInt(35); 
			if(count==0){
				return new EmPlane(new File("./image/airplane.png"),null,200,5);
			}
			else if(count==2){
				return new EmPlane(new File("./image/airplane.png"),new File("./image/airplane2.png"),100,8);
			}
			else if(count==3) {
				return new EmPlane(new File("./image/airplane.png"),null,800,2);
			}else{
				return new EmPlane(new File("./image/airplane.png"),null,200,1);
			}	
		}
	 /** �л��볡*/
	 public void EplaneAction(){
		 eplane++;
		 if(eplane%100==0) {
			 EmPlane o=nextOne();
     		flyings=Arrays.copyOf(flyings, flyings.length+1);//����
     		flyings[flyings.length-1]=o;//��������뵽������
		 }
	 }
	 
     /** Ӣ�ۻ������ӵ�--�ӵ��볡*/
     public void shootAction(){
     	shootIndex++;
     	if(shootIndex%broom==0&&Static.fire==true){
     		Bullet[] bs=hero.shoot();
     		bullets=Arrays.copyOf(bullets, bullets.length+bs.length);
     		System.arraycopy(bs,0,bullets, bullets.length-bs.length,bs.length);
     	}
     }
     /**��ײ���*/
     public void bandAction() {
    	 for(Bullet b:bullets){
    		 emPlaneBang(b);
     	}
     }
     /**�ӵ���ײ*/
     public void bulletBang(FlyingObject f) {
    	 int life =f.getLife();
    	 for(Bullet b:bullets) {
    		if(f.hit(b)) {
    			life-=20;
    			f.setLife(life);
    		}
    	 }
     }
     /**�л���ײ*/
     public void emPlaneBang(FlyingObject f) {
    	 int index=-1;
    	 int life =f.getLife();
     	for(int i=0;i<flyings.length;i++){
     		FlyingObject obj=flyings[i];
     		if(obj.hit(f)){
     			index=i;
     			break;
     		}
     	}
     	if(index!=-1){//��ײ����
     		FlyingObject one=flyings[index];
     		if(one instanceof EmPlane){//�л�
     			one.setLife(one.getLife()-life);
     		}
     		if(one.isDie()) {
     		FlyingObject t=flyings[index];
     		flyings[index]=flyings[flyings.length-1];
     		flyings[flyings.length-1]=(EmPlane) t;
     		flyings=Arrays.copyOf(flyings, flyings.length-1);//����
     		}
    	 }
     }
     //��ͼ
     public void paint(Graphics g) {
		 if(Static.state==1)super.paint(g);
			paintHero(g);
			paintBullet(g);
			painEPlane(g);
			paintScore(g);	
			paintState(g);
		}
     //��Ӣ��
	 private void paintHero(Graphics g) {
				g.drawImage(hero.image,hero.x,hero.y,null);
				if(Static.state==1)hero.step();	
		}
	 
	 //���л�
	 private void painEPlane(Graphics g) {
		 for(EmPlane e:flyings) {
			 if(Static.state==Static.RUNNING&&e.getState()==Static.LIFE)e.move();
			 //e.step();
			 if(e.outBoundsy2())g.drawImage(e.image, e.x,e.y, null);  
		}	  
	 }
	 
	 //������
	 private void paintScore(Graphics g) {
		 g.setColor(new Color(0xFF0000));//������ɫ--��ɫ
		 g.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));//����,�Ӵ�,�ֺ�
		 if(Static.state==1)g.drawString("Score:"+Static.Score,10,70);
	 }
	 
	 //��״̬
	 public void paintState(Graphics g){
		 switch (Static.state) {//�ж�״̬
			case Static.START:
				if(hero.y==550) {
				g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));
				g.setColor(Color.BLACK);//������ɫ--��ɫ
				g.drawString("�������Enter����ʼ��Ϸ��", 290, 800);	
				tk.addAWTEventListener(new ImplAWTEventListener(), AWTEvent.KEY_EVENT_MASK);
				}
				g.drawImage(Static.start,10,0,null);
				break;
			case Static.PAUSE:
				g.drawImage(Static.pause,0,0,null);
				break;
			case Static.GAME_OVER: 
				g.drawImage(Static.gameover,0,0,null);
				break;
		}
	}
	 
	 //���ӵ�
	public void paintBullet(Graphics g) {
		for(Bullet b:bullets) {
			if(!b.outBoundsy1())g.drawImage(b.image, b.x,b.y, null);	
			if(Static.state==1)b.step();	 
		}	  
	}
	
	//˫��������˸
	public void update(Graphics g){
		if (offScreenImage == null)
			offScreenImage = this.createImage(Static.BG.getIconWidth(),Static.BG.getIconHeight());
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}
}

/**
 �����ࡪ����̬
*/
class Static{
	public static Icon BG = new ImageIcon("./image/background.png");
	public static ImageIcon image1 = new ImageIcon("./image/bee.png");
	public static Image beer = image1.getImage();
	public static ImageIcon image2= new ImageIcon("./image/bullet.png");
	public static Image bullet = image2.getImage();
	public static ImageIcon image4 = new ImageIcon("./image/hero1.png");
	public static Image hero1 = image4.getImage();
	public static ImageIcon image5 = new ImageIcon("./image/pause.png");
	public static Image pause = image5.getImage();
	public static ImageIcon image6 = new ImageIcon("./image/gameover.png");
	public static Image gameover = image6.getImage();
	public static ImageIcon image7= new ImageIcon("./image/start.png");
	public static Image start = image7.getImage();
	public static ImageIcon image8= new ImageIcon("./image/airPlane.png");
	public static Image EPlane = image8.getImage();
	public static Icon startg = new ImageIcon("./image/startgame.png");
	public static Icon stopg = new ImageIcon("./image/stopgame.png");
	public static final int START=0;//����״̬
	public static final int RUNNING=1;//����
	public static final int PAUSE=2;//��ͣ
	public static final int GAME_OVER=3;//����
	public static int state=0;//״̬
	public static boolean up,down,left,right;
	public static boolean fire=false;
	public static int Score=0;
	public static final int REMOVE=2;
	public static final int LIFE = 0;// ���
	public static final int DEAD = 1;// over
}

/* *
Ӣ����
*/
class Hero extends FlyingObject{
	private int life;//��
	private int doubleFire;//����ֵ
	private BufferedImage[] images;//ͼƬ
	private int index;//ͼƬ�л���Ƶ��
	
	public Hero() {
		try {
		image= ImageIO.read(new File("./image/hero0.png"));//ͼƬ
		}
		catch(IOException e) {	
		}
		width=image.getWidth();//��
		height=image.getHeight();//��
		x=200;
		y=820;
		life=100;//��ֵΪ100
		doubleFire=0;//����ֵΪ0,��������
		try {
		images=new BufferedImage[]{ ImageIO.read(new File("./image/hero0.png")), ImageIO.read(new File("./image/hero1.png"))};
	}
	catch(IOException e) {	
	}
		index=0;
	}
	public void step() {
		index++;
		int a=index/10;//ÿ100m b=0,1
		int b=a%2;
		image=images[b];
	}
	/** �����ӵ�*/
	public Bullet[] shoot(){
		int xStep=this.width/4;//1/4Ӣ�ۻ��Ŀ��
		int yStep=20;//�ӵ���ɻ��ľ���
		if(doubleFire>0){//˫������
			Bullet[] bullets=new Bullet[2];
			bullets[0]=new Bullet(this.x+xStep,this.y-yStep);
			bullets[1]=new Bullet(this.x+3*xStep,this.y-yStep);
			doubleFire-=2;
			return bullets;
		}else{//��������
			Bullet[] bullets=new Bullet[1];
			bullets[0]=new Bullet(this.x+2*xStep,this.y-yStep);
			return bullets;
		}
	}

	/**��ȡ��*/
	public int getLife(){
		return life;
	}
	/** ����*/
	public int subtractLife(){
		return life--;
	}
	
	public void addLife(){
		life++;
	}
	/** ������ֵ*/
	public void addDoubleFire(){
		doubleFire+=40;
	}
	/** ���û���ֵ*/
	public void setDoubleFire(int doubleFire){
		this.doubleFire=doubleFire;
	}
	public boolean outBoundsx1() { 
		int x1=this.x+this.width/2;
		return x1>0;//����Խ��
	}
	public boolean outBoundsx2() {
		int x1=this.x+this.width/2;
		return x1<Static.BG.getIconWidth();
	}
	public boolean outBoundsy1() {
		int y1=this.y+this.height/2;
		return y1>0;
		
	}
	public boolean outBoundsy2() {
		int y1=this.y+this.height/2;
		return y1<Static.BG.getIconHeight();
		
	}
	public boolean hit(FlyingObject other){
		int x1=other.x-this.width/2;
		int x2=other.x+other.width+this.width/2;
		int y1=other.y-this.height/2;
		int y2=other.y+other.height/2;
		int hx=this.x+this.width/2;// 
		int hy=this.y+this.height/2;
		return hx>x1 && hx<x2
		       &&
		       hy>y1 && hy<y2;
	}
	@Override
	protected boolean isDie() {
		return this.life<=0;
	}
}

/** 
 �л���
 */
class EmPlane extends FlyingObject{
	private int life,speed;
	private BufferedImage[] images;//ͼƬ
	int index=0,index2=0,index3=0;
	private int state;//״̬�Ǵ��
	EmPlane(File f1,File f2,int life,int speed){
		try {
		image= ImageIO.read(f1);//ͼƬ
		}catch(IOException e) {
		}
		width=image.getWidth();//��
		height=image.getHeight();//��
		Random rand=new Random();
		x=rand.nextInt(Static.BG.getIconWidth()-this.width);//x����	
		y=-height;//y����
		this.life=life;
		this.speed=speed;
		try {
			if(f2!=null)images=new BufferedImage[]{ ImageIO.read(f1), ImageIO.read(f2)};
		}
		catch(IOException e) {	
		}
		}
	public void bomb(String str) {
		images=new BufferedImage[4];
		if(isDie())
		for (int i = 0; i <=images.length-1; i++) {
			try {
				images[i] = ImageIO.read(new File(str + (i+1) + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	public boolean isDie()  {	
		return this.life<=0;
	}	
	public void setState(int state) {
		this.state=state;
	}
	public int getState() {
		return this.state;
		
	}
	public boolean hit(FlyingObject other){
		int x1=other.x-this.width/2;
		int x2=other.x+other.width+this.width/2;
		int y1=other.y-this.height/2;
		int y2=other.y+other.height/2;
		int hx=this.x+this.width/2;// 
		int hy=this.y+this.height/2;
		return hx>x1 && hx<x2
		       &&
		       hy>y1 && hy<y2;
	}
	@Override
	public void step() {
		index++;
		int a=index/10;//ÿ100m b=0,1
		int b=a%2;
		image=images[b];
	}
	public void move() {
			y+=speed;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life){
		this.life=life;
	}
	@Override
	public boolean outBoundsx1() {
		return false;
	}

	@Override
	public boolean outBoundsx2() {
		return false;
	}

	@Override
	public boolean outBoundsy1() {
		return false;
	}

	@Override
	public boolean outBoundsy2() {	
		return y<Static.BG.getIconHeight();
	}
}

/**
 �ӵ���
*/
class Bullet extends FlyingObject{
	private int speed=4;//�ӵ�����
	private int life=20;
	/** Bullet���췽��*/
	public Bullet(int x,int y){
		try {
		image= ImageIO.read(new File("./image/bullet.png"));
		}catch(IOException e) {
		}
		width= image.getWidth();
		height=image.getHeight();
		this.x=x;//x����
		this.y=y;//y����
	}
	public void step() {
		y-=speed;
	}
	public boolean outBoundsx1() {
		return false;
	}
	public boolean outBoundsx2() {
		return false;
	}
	public boolean outBoundsy1() {
		return this.y<-height;
	}
	public boolean outBoundsy2() {
		return false;	
	}
	@Override
	protected int getLife() {
		return this.life;
	}
	@Override
	protected boolean hit(FlyingObject other) {
		int x1=other.x-this.width/2;
		int x2=other.x+other.width+this.width/2;
		int y1=other.y-this.height/2;
		int y2=other.y+other.height/2;
		int hx=this.x+this.width/2;// 
		int hy=this.y+this.height/2;
		return hx>x1 && hx<x2
		       &&
		       hy>y1 && hy<y2;
	}
	@Override
	protected boolean isDie() {
		return this.life<=0;
	}
}

/**
  �����ﹹ����
 */
abstract class FlyingObject{
	protected int x;
	protected int y;
	protected int life;
	protected int width;
	protected int height;
	protected BufferedImage image;
	
	protected abstract boolean isDie();
	public void setLife(int life) {
		this.life=life;
	}
	protected abstract int getLife();
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public BufferedImage getImage() {
		return  image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	protected boolean hit(FlyingObject other){
		return false;
	}
	public abstract void step();
	//���˱��ӵ���
	public  boolean shootBy(Bullet bullet){
		int x=bullet.x;//�ӵ���x
		int y=bullet.y;//�ӵ���y
		return x>this.x&&x<this.x+this.width
		&&
		y>this.y&&y<this.y+this.height;
	};
	public abstract boolean outBoundsx1();//��߽�
	public abstract boolean outBoundsx2();//�ұ߽�
	public abstract boolean outBoundsy1();//�ϱ߽�
	public abstract boolean outBoundsy2();//�±߽�
}

/**
 ȫ�ּ��̼�����
*/
class ImplAWTEventListener implements AWTEventListener { 
    @Override  
    public void eventDispatched(AWTEvent event){  
    	if (event.getClass() == KeyEvent.class) {  
    		// ��������¼��Ǽ����¼�.  
            KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.getID() == KeyEvent.KEY_TYPED) {
            	keyTyped(keyEvent);
            	if ((char)keyEvent.getKeyChar()==KeyEvent.VK_ENTER) {
            	Static.state=Static.RUNNING;      
            	} 
            }
            if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {  
            	keyPressed(keyEvent); //��סʱ��Ҫ��������
            	if(keyEvent.getKeyCode()==KeyEvent.VK_LEFT){
            	 Static.left=true;
            	}
            	if(keyEvent.getKeyCode()==KeyEvent.VK_RIGHT){
               	 Static.right=true;
               	}
            	if(keyEvent.getKeyCode()==KeyEvent.VK_UP){
               	 Static.up=true;
               	}
            	if(keyEvent.getKeyCode()==KeyEvent.VK_DOWN){
               	 Static.down=true;
               	}
            	if(keyEvent.getKeyCode()==KeyEvent.VK_X){
            		Static.fire=true;
            	}
                  
            } else if (keyEvent.getID() == KeyEvent.KEY_RELEASED) {  
            	keyReleased(keyEvent);//�ſ�ʱ��Ҫ��������   
            	if(keyEvent.getKeyCode()==KeyEvent.VK_LEFT){
               	 Static.left=false;
               	}
               	if(keyEvent.getKeyCode()==KeyEvent.VK_RIGHT){
                  	 Static.right=false;
                  	}
               	if(keyEvent.getKeyCode()==KeyEvent.VK_UP){
                  	 Static.up=false;
                  	}
               	if(keyEvent.getKeyCode()==KeyEvent.VK_DOWN){
                  	 Static.down=false;
                  	}
               	if(keyEvent.getKeyCode()==KeyEvent.VK_X){
            		Static.fire=false;
            	}
            	
            }  
        }    
}  
    private void keyPressed(KeyEvent event) {}  
    private void keyReleased(KeyEvent event) {}   
    private void keyTyped(KeyEvent event) {}
} 