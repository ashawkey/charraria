import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

abstract class Mobs{
    protected  int x;
    protected  int y;
    protected int HP;
    public int getHP(){
        return HP;
    }
    public void modifyHP(int x){
        HP+=x;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}


public class hero extends Mobs{
    private static final char Symbol='@';
    private static final Color heroColor=Color.RED;
    private int MAX_JUMP_HEIGHT;
    private int SPEED;
    private int JUMPING_ABILITY;
    private int Defense=0;
    private int Attack=0;
    private static final int LifeProtectionTimeAfterDamaged=10;
    private int LifeRegenerationSpeed=100; //every 5s
    private static final char HeartSymbol='\u2665';
    hero(){
        MAX_JUMP_HEIGHT=10;
        SPEED=1;
        JUMPING_ABILITY=2;
        HP=10;
        x=landGenerator.getWorWidth()/2;
        for(int yy=0;yy<landGenerator.getWorHeight();yy++){
            if(landGenerator.getWorldBlock(yy,x).BlockNo==0) {
                y=yy;
                break;
            }
        }
    }
    public void sethX(int x_) {
    	x=x_;
    }
    public void sethY(int y_) {
    	y=y_;
    }
    public int gethX() {
    	return x;
    }
    public int gethY() {
    	return y;
    }
    private int Flag_HPModified=0;
    public void modifyHP(int x){
        if(x<=0 && Flag_HPModified!=0) return;
        HP+=x;
        if(x<0) {
            Flag_HPModified = LifeProtectionTimeAfterDamaged;  //0.5s protected
        }
    }
    public boolean isAlive(){
        return HP>0;
    }
    private int Timer_LifeRegeneration=0;
    public void draw(Graphics g){
        Timer_LifeRegeneration++;
        if(Timer_LifeRegeneration==LifeRegenerationSpeed){
            if(HP<10) HP++;
            Timer_LifeRegeneration=0;
        }
        g.setColor(heroColor);
        //count down Protect and blink
        if(Flag_HPModified!=0){
            Flag_HPModified--;
            if(Flag_HPModified%2==0)
                g.drawString(String.valueOf(Symbol),MainWindow.getWinWidth()/2-landGenerator.getFontSize()/2+5,MainWindow.getWinHeight()/2+7); //????????
        }
        else g.drawString(String.valueOf(Symbol),MainWindow.getWinWidth()/2-landGenerator.getFontSize()/2+5,MainWindow.getWinHeight()/2+7); //????????

        g.setColor(Color.RED);
        StringBuffer hearts=new StringBuffer();
        for(int i=0;i<HP;i++) hearts.append(HeartSymbol);
        g.drawString(hearts.toString(),MainWindow.getWinWidth() - 150, 20);
        //move and change world
        landGenerator.changeWorldEntity(y,x,new Entity());
        move();
        landGenerator.changeWorldEntity(y,x,new SolidHeroEntity(this));
    }
    private Direction dir=Direction.STOP;
    private int JUMPloop=0;
    private int JUMP_flag=0;
    private int fallingDistance=0;
    public void setDir(Direction d){
        dir=d;
    }
    public void PrintDeath(){

    }

    private void move(){
        if(JUMP_flag==0) {
            if (Direction.containDir(dir, Direction.JUMP)) {
                JUMPloop++;
            }
        }
        if(JUMPloop>=MAX_JUMP_HEIGHT) {
            JUMP_flag=1;
        }
        if(JUMP_flag==1){
            if(JUMPloop==0) {
                JUMP_flag=0;
            }
            if(Direction.containDir(dir,Direction.JUMP)) {
                dir = Direction.dispose(dir,Direction.JUMP);
            }
        }
        switch(dir){
            case L:
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x-1);
                    Entity e=landGenerator.getWorldEntity(y,x-1);
                    if ((!(e instanceof SolidMonsterEntity))&&b.permeable) {
                        x -= 1;
                    }
                    else if(e instanceof SolidMonsterEntity){
                        modifyHP(-((SolidMonsterEntity) e).damage);
                    }
                }
                break;
            case R:
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x+1);
                    Entity e=landGenerator.getWorldEntity(y,x+1);
                    if ((!(e instanceof SolidMonsterEntity))&&b.permeable) {
                        x += 1;
                    }
                    else if(e instanceof SolidMonsterEntity){
                        modifyHP(-((SolidMonsterEntity) e).damage);
                    }
                }
                break;
            case JUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    Entity e=landGenerator.getWorldEntity(y+1,x);
                    if ((!(e instanceof SolidMonsterEntity))&&b.permeable) {
                        y += 1;
                    }
                    else if(e instanceof SolidMonsterEntity){
                        modifyHP(-((SolidMonsterEntity) e).damage);
                    }
                }
                break;
            case LJUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    Entity e=landGenerator.getWorldEntity(y+1,x);
                    if ((!(e instanceof SolidMonsterEntity))&&b.permeable) {
                        y += 1;
                    }
                    else if(e instanceof SolidMonsterEntity){
                        modifyHP(-((SolidMonsterEntity) e).damage);
                    }
                }
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x-1);
                    Entity e=landGenerator.getWorldEntity(y,x-1);
                    if ((!(e instanceof SolidMonsterEntity))&&b.permeable) {
                        x -= 1;
                    }
                    else if(e instanceof SolidMonsterEntity){
                        modifyHP(-((SolidMonsterEntity) e).damage);
                    }
                }
                break;
            case RJUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    Entity e=landGenerator.getWorldEntity(y+1,x);
                    if ((!(e instanceof SolidMonsterEntity))&&b.permeable) {
                        y += 1;
                    }
                    else if(e instanceof SolidMonsterEntity){
                        modifyHP(-((SolidMonsterEntity) e).damage);
                    }
                }
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x+1);
                    Entity e=landGenerator.getWorldEntity(y,x+1);
                    if ((!(e instanceof SolidMonsterEntity))&&b.permeable) {
                        x += 1;
                    }
                    else if(e instanceof SolidMonsterEntity){
                        modifyHP(-((SolidMonsterEntity) e).damage);
                    }
                }
                break;
            case STOP:
                break;
        }
        //gravity
        Block b=landGenerator.getWorldBlock(y-1,x);
        Entity e=landGenerator.getWorldEntity(y-1,x);
        if((!(e instanceof SolidMonsterEntity))&&b.permeable){
            y--;
            fallingDistance++;
        }
        else if(e instanceof SolidMonsterEntity){
            modifyHP(-((SolidMonsterEntity) e).damage);
        }
        else {
            //when landed, reset jump lock.
            JUMPloop=0;
            JUMP_flag=0;
            //drop damage
            int fallDamage=fallingDistance>30?-fallingDistance/(2*MAX_JUMP_HEIGHT):0;
            if(fallDamage<0) modifyHP(fallDamage);
            fallingDistance=0;
        }
    }
}
