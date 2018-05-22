import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Monster extends Mobs{
    private char Symbol;
    public String name;
    private int atk;
    public int getAtk(){
        return atk;
    }
    public void setAtk(int _a){
        atk=_a;
    }
    private int slowness;
    private int SPEED;
    private Direction dir=Direction.STOP;
    private int JUMP_flag=0;
    private int JUMPloop=0;
    private static int MAX_JUMP_HEIGHT;
    private static int JUMPING_ABILITY;
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
                    if ((!(e instanceof SolidHeroEntity))&&b.permeable) {
                        x -= 1;
                    }
                    else if(e instanceof SolidHeroEntity){
                        ((SolidHeroEntity) e).m.modifyHP(-atk);
                    }
                }
                break;
            case R:
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x+1);
                    Entity e=landGenerator.getWorldEntity(y,x+1);
                    if ((!(e instanceof SolidHeroEntity))&&b.permeable) {
                        x += 1;
                    }
                    else if(e instanceof SolidHeroEntity){
                        ((SolidHeroEntity) e).m.modifyHP(-atk);
                    }
                }
                break;
            case JUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    Entity e=landGenerator.getWorldEntity(y+1,x);
                    if ((!(e instanceof SolidHeroEntity))&&b.permeable) {
                        y += 1;
                    }
                    else if(e instanceof SolidHeroEntity){
                        ((SolidHeroEntity) e).m.modifyHP(-atk);
                    }
                }
                break;
            case LJUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    Entity e=landGenerator.getWorldEntity(y+1,x);
                    if ((!(e instanceof SolidHeroEntity))&&b.permeable) {
                        y += 1;
                    }
                    else if(e instanceof SolidHeroEntity){
                        ((SolidHeroEntity) e).m.modifyHP(-atk);
                    }
                }
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x-1);
                    Entity e=landGenerator.getWorldEntity(y,x-1);
                    if ((!(e instanceof SolidHeroEntity))&&b.permeable) {
                        x -= 1;
                    }
                    else if(e instanceof SolidHeroEntity){
                        ((SolidHeroEntity) e).m.modifyHP(-atk);
                    }
                }
                break;
            case RJUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    Entity e=landGenerator.getWorldEntity(y+1,x);
                    if ((!(e instanceof SolidHeroEntity))&&b.permeable) {
                        y += 1;
                    }
                    else if(e instanceof SolidHeroEntity){
                        ((SolidHeroEntity) e).m.modifyHP(-atk);
                    }
                }
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x+1);
                    Entity e=landGenerator.getWorldEntity(y,x+1);
                    if ((!(e instanceof SolidHeroEntity))&&b.permeable) {
                        x += 1;
                    }
                    else if(e instanceof SolidHeroEntity){
                        ((SolidHeroEntity) e).m.modifyHP(-atk);
                    }
                }
                break;
            case STOP:
                break;
        }
        //gravity
        Block b=landGenerator.getWorldBlock(y-1,x);
        Entity e=landGenerator.getWorldEntity(y - 1,x);
        if((!(e instanceof SolidHeroEntity))&&b.permeable){
            y--;
        }
        else if(e instanceof SolidHeroEntity){
            ((SolidHeroEntity) e).m.modifyHP(-atk);
        }
        else {
            //when landed, reset jump lock.
            JUMPloop=0;
            JUMP_flag=0;
        }
    }
    protected Color MonsterColor;
    public boolean isAlive() {
        return HP>0;
    }
    public AI ai;
    private int slowness_timer=0;
    public void draw(Graphics g){
        if(x>=landGenerator.Focus_x-landGenerator.getWinOffsetX() && x<= landGenerator.Focus_x+landGenerator.getWinOffsetX() && y>=landGenerator.Focus_y-landGenerator.getWinOffsetY() && y<=landGenerator.Focus_y+landGenerator.getWinOffsetY()){
            g.setColor(MonsterColor);
            g.drawString(String.valueOf(Symbol),(x-(landGenerator.Focus_x-landGenerator.getWinOffsetX()))*landGenerator.getFontSize(),MainWindow.getWinHeight()-(y-(landGenerator.Focus_y-landGenerator.getWinOffsetY()))*landGenerator.getFontSize());
        }
        slowness_timer++;
        if(slowness_timer==slowness) {
            slowness_timer=0;
            dir = ai.Walker();
        }
        else if(!Direction.containDir(dir,Direction.JUMP)){
            dir=Direction.STOP;
        }
        landGenerator.changeWorldEntity(y,x,new Entity());
        move();
        landGenerator.changeWorldEntity(y,x,new SolidMonsterEntity(atk,this));
    }
    public ArrayList<Entity> Drops(){return null;};
    Monster(int _x,int _y, int _atk, int _speed, char _s, int _ja, int _jh,int _slow,int _hp,Color _color,String _name){
        x=_x;
        y=_y;
        atk=_atk;
        SPEED=_speed;
        Symbol=_s;
        JUMPING_ABILITY=_ja;
        MAX_JUMP_HEIGHT=_jh;
        slowness=_slow;
        HP=_hp;
        MonsterColor=_color;
        name=_name;
    }
}

class Slime extends Monster{
    Random rand=new Random();
    private Color SlimeColorGenerator(){
        int r=rand.nextInt(100);
        if(r>80) return Color.BLUE;
        else if(r>60) return Color.magenta;
        else return Color.GREEN;
    }
    Slime(int x,int y){
        super(x,y,1,1,'S',2,5,10,5,Color.GREEN,"Slime");
        MonsterColor=SlimeColorGenerator();
        ai=new SlimeAI();
    }
    @Override
    public ArrayList<Entity> Drops(){
        ArrayList<Entity> drops=new ArrayList<Entity>();
        int r=rand.nextInt(100);
        if(r>50){
            drops.add(new Arrow());
        }
        return drops;
    }
}



