import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class MainPanel extends JPanel {
    private static int dispearDistance=100;
    private static int MaxMonsterNum=50;
    public static final int STAT_GAME=0;
    public static final int STAT_PAUSE=1;
    public static final int STAT_START=2;
    public static final int STAT_BAG=3;
    public static int stat=0;
    public Thread GameThread;
    private Random rand=new Random();

    private StringBuffer BlockDescription=new StringBuffer();
    private Font DescriptionFont=new Font("TimesNewRoman",Font.BOLD,15);

    private landGenerator Land=new landGenerator();
    private hero Hero = new hero();
    private inventory Inventory = new inventory();

    MainPanel(){
        setFocusable(true);
        requestFocus();
        setDoubleBuffered(true);
        addKeyListener(new MainKeyMonitor());
        MainMouseMonitor MMM=new MainMouseMonitor();
        addMouseListener(MMM);
        addMouseWheelListener(MMM);  //must add alone! not just mouseListener!
        addMouseMotionListener(MMM);  //too, there are three different mouse listeners...
        Thread t=new MainThread();
        t.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Land.draw(g);
        Hero.draw(g);
        Inventory.draw(g);
        //draw description
        g.setFont(DescriptionFont);
        g.setColor(Color.CYAN);
        if(BlockDescription!=null) {
            g.drawString(BlockDescription.toString(), 20, 50);
        }
        //draw monsters
        Iterator<Monster> iM=Monsters.iterator();
        while(iM.hasNext()){
            Monster M=iM.next();
            if(distance(M.getX(),0,Hero.getX(),0)>dispearDistance){
                iM.remove();
                landGenerator.changeWorldBlock(M.getY(),M.getX(),new Air(0));
            }
            else if(!M.isAlive()){
                //drops
                ArrayList<Entity> drops=M.Drops();
                for(Entity e:drops){
                    Inventory.addEntity(e);
                }
                //remove
                iM.remove();
                landGenerator.changeWorldBlock(M.getY(),M.getX(),new Air(0));
            }
        }
        for(Monster M:Monsters){
            M.draw(g);
        }
        //draw projectiles
        for(Projectile p:projectiles){
            p.draw(g);
        }
    }
    private class MainThread extends Thread {
        public void run(){
            while(true){
                if(!Hero.isAlive()){
                    System.out.println("YOU DIED!!!");
                    Hero.PrintDeath();
                    try{
                        Thread.sleep(5000);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Hero=new hero();
                }
                //remain focused
                landGenerator.Focus_x=Hero.getX();
                landGenerator.Focus_y=Hero.getY();
                RareEventGenerator();
                MonsterGenerator();
                repaint();
                try{
                    Thread.sleep(50);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public double distance(int x1,int y1,int x2,int y2){
        return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }
    private int Timer_RareEventGenerator=0;
    private void RareEventGenerator(){
        Timer_RareEventGenerator++;
        if(Timer_RareEventGenerator==1000) {   //50s
            Timer_RareEventGenerator=0;
            if (rand.nextInt(100) > 90) {
                System.out.println("It begins to rain!");

            }
        }
    }
    private class MainKeyMonitor extends KeyAdapter {
        private long APressed=0;
        private long DPressed=0;
        private long SPACEPressed=0;
        private long SPressed=0;
        public void keyReleased(KeyEvent e){
            switch(stat){
                case STAT_GAME:
                    gameKeyReleased(e);
            }

        }
        public void keyPressed(KeyEvent e){
            switch(stat){
                case STAT_GAME:
                    gameKeyPressed(e);
                    break;
                case STAT_START:
                    startKeyPressed(e);
                    break;
                case STAT_BAG:
                    break;
            }
        }
        private void startKeyPressed(KeyEvent e){

        }
        private void gameKeyReleased(KeyEvent e){
            switch(e.getKeyCode()){
                case VK_SPACE:
                    SPACEPressed=0;
                    break;
                case VK_A:
                    APressed=0;
                    break;
                case VK_D:
                    DPressed=0;
                    break;
                case VK_S:
                    SPressed=0;
                    break;
            }
            locateDirection();
        }
        private void locateDirection(){
            Direction d=Direction.STOP;
            if(APressed>DPressed)
                d=Direction.compose(d,Direction.L);
            else if(DPressed>APressed)
                d=Direction.compose(d,Direction.R);
            if(SPACEPressed!=0)
                d=Direction.compose(d,Direction.JUMP);
            else if(Direction.containDir(d,Direction.JUMP))
                d=Direction.dispose(d,Direction.JUMP);
            if(SPressed!=0)
                d=Direction.STOP;
            Hero.setDir(d);
        }
        private void gameKeyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case VK_SPACE:
                    SPACEPressed=System.currentTimeMillis();
                    break;
                case VK_A:
                    APressed=System.currentTimeMillis();
                    break;
                case VK_D:
                    DPressed=System.currentTimeMillis();
                    break;
                case VK_S:
                    SPressed=System.currentTimeMillis();
                    break;
                case VK_0:
                    Inventory.setCurrent(0);
                    break;
                case VK_1:
                    Inventory.setCurrent(1);
                    break;
                case VK_2:
                    Inventory.setCurrent(2);
                    break;
                case VK_3:
                    Inventory.setCurrent(3);
                    break;
                case VK_4:
                    Inventory.setCurrent(4);
                    break;
                case VK_5:
                    Inventory.setCurrent(5);
                    break;
                case VK_E:

                    break;
            }
            locateDirection();
        }
    }
    private class MainMouseMonitor extends MouseAdapter {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            super.mouseWheelMoved(e);
            switch(stat){
                case STAT_GAME:
                    gameMouseWheelMoved(e);
            }
        }
        private void gameMouseWheelMoved(MouseWheelEvent e){
            Inventory.moveCurrent(e.getWheelRotation());
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            switch(stat){
                case STAT_GAME:
                    gameMouseMoved(e);
            }
        }
        private void gameMouseMoved(MouseEvent e){
            BlockUnderMouse(e.getPoint());
        }
        /*
        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            switch(stat){
                case STAT_GAME:
                    gameMouseDragged(e);
            }
        }
        private void gameMouseDragged(MouseEvent e){
            BlockClicked(e.getPoint());
        }
        */
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            switch(stat){
                case STAT_GAME:
                    gameMousePressed(e);
            }
        }
        public void gameMousePressed(MouseEvent e){
            switch(e.getButton()){
                case MouseEvent.BUTTON1:
                    mouseDown=true;
                    initThread_mouseDown();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            switch(stat){
                case STAT_GAME:
                gameMouseReleased(e);
            }
        }
        public void gameMouseReleased(MouseEvent e){
            switch(e.getButton()){
                case MouseEvent.BUTTON1:
                    mouseDown=false;
            }
        }

    }
    volatile private boolean mouseDown=false;
    volatile private boolean Flag_mouseDown_running=false;
    private synchronized void initThread_mouseDown(){
        if(Flag_mouseDown_running) return;
        Flag_mouseDown_running=true;
        new Thread(){
            @Override
            public void run() {
                super.run();
                do{
                   BlockClicked(pointSubtract(MouseInfo.getPointerInfo().getLocation(),MainPanel.super.getLocationOnScreen()));
                }while(mouseDown);
                Flag_mouseDown_running=false;
            }
        }.start();
    }
    public Point pointSubtract(Point a,Point b) {
        return new Point(a.x-b.x,a.y-b.y);
    }
    private double TimeClicked=System.currentTimeMillis();
    private void BlockClicked(Point p){
        int x=landGenerator.Focus_x+((int)Math.floor(p.x/landGenerator.getFontSize())-landGenerator.getWinOffsetX());
        int y=landGenerator.Focus_y-((int)Math.floor(p.y/landGenerator.getFontSize())-landGenerator.getWinOffsetY());
        if(!(x>=0&&x<landGenerator.getWorWidth() && y>=0 && y<landGenerator.getWorHeight())){
            return;   //out of world range
        }
        Block b=landGenerator.getWorldBlock(y,x);
        Entity e=landGenerator.getWorldEntity(y,x);
        Entity inHand=Inventory.getCurrent();
        if(distance(x,y,Hero.getX(),Hero.getY())>inHand.getWorkDist()){
            return;  //out of work distance
        }
        if(e instanceof SolidMonsterEntity){
            if(inHand instanceof Weapon){
                if(System.currentTimeMillis()<TimeClicked+((Tools) inHand).Slowness){
                    return;
                }
                TimeClicked=System.currentTimeMillis();
                System.out.println("Monster is "+((Monster)((SolidMonsterEntity) e).m).name);
                System.out.println("Life is "+((SolidMonsterEntity) e).m.getHP());
                ((SolidMonsterEntity) e).m.modifyHP(-((Weapon) inHand).attack);
                System.out.println("Life remains "+((SolidMonsterEntity) e).m.getHP());
                return;
            }
        }
        if(inHand instanceof LongRangedWeapon){
            if(System.currentTimeMillis()<TimeClicked+((Tools) inHand).Slowness){
                return;
            }
            TimeClicked=System.currentTimeMillis();
            Direction d=Direction.projectileDir(Hero.getX(),Hero.getY(),x,y);
            System.out.println("Projectile direction is "+d.name());
            projectiles.add(new Projectile(Hero.getX(),Hero.getY(),((LongRangedWeapon) inHand).attack,inHand.symbol,((LongRangedWeapon) inHand).SPEED,d,inHand.color));
        }
        else if(b instanceof solidBlock){
            System.out.println("Block is "+b.symbol);
            System.out.println("life is:"+((solidBlock) b).life);
            if(inHand instanceof Tools){
                //tool slowness
                if(System.currentTimeMillis()<TimeClicked+((Tools) inHand).Slowness){
                    return;
                }
                TimeClicked=System.currentTimeMillis();
                //try destroy
                ((solidBlock) b).modifyHP(-((Tools) inHand).DestroyAbility);
                System.out.println("life remain:"+((solidBlock) b).life);
                if(((solidBlock) b).life<=0){
                    if(!Inventory.isFull()){
                        Inventory.addEntity(new EntityBlock(b));
                        landGenerator.changeWorldBlock(y,x,new Air(0));
                    }
                    else{
                        //??? how to fall
                    }
                }
            }
        }
        else if(b instanceof Fluid){
            if(inHand instanceof EntityBlock){
                //try to place
                EntityBlock eb = (EntityBlock) inHand;
                eb.Total--;
                if(eb.Total<=0){
                    Inventory.removeEntity(eb);
                }
                //create new object
                Block newBlock;
                try{
                    newBlock=eb.block.getClass().newInstance();
                    landGenerator.changeWorldBlock(y,x,newBlock);
                    //dynamic system to new an unknown class!!!
                    System.out.println("block added:"+newBlock.symbol);
                }catch (Exception ex){ex.printStackTrace();}
            }
        }
    }
    private void BlockUnderMouse(Point p){
        int x=landGenerator.Focus_x+((int)Math.floor(p.x/landGenerator.getFontSize())-landGenerator.getWinOffsetX());
        int y=landGenerator.Focus_y-((int)Math.floor(p.y/landGenerator.getFontSize())-landGenerator.getWinOffsetY());
        if(!(x>=0&&x<landGenerator.getWorWidth() && y>=0 && y<landGenerator.getWorHeight())){
            return;
        }
        Block b=landGenerator.getWorldBlock(y,x);
        BlockDescription.setLength(0);  //so...WTF clear method?!
        BlockDescription.append(b.description);
    }
    private ArrayList<Monster> Monsters = new ArrayList<Monster>();
    private int Timer_MonsterGenerator=0;
    private void MonsterGenerator(){
        Timer_MonsterGenerator++;
        if(Timer_MonsterGenerator==50) {   //2.5s
            Timer_MonsterGenerator=0;
            if (Monsters.size() < MaxMonsterNum) {
                int x = Hero.getX()+rand.nextInt(landGenerator.getWinOffsetX()*4)-2*landGenerator.getWinOffsetX();
                int y = Hero.getY()+rand.nextInt(landGenerator.getWinOffsetY()*4)-2*landGenerator.getWinOffsetY();
                if(!(x>=0&&x<landGenerator.getWorWidth() && y>=0 && y<landGenerator.getWorHeight())){
                    return;   //out of world range
                }
                if (landGenerator.getWorldBlock(y, x) instanceof Fluid) {
                    Slime s=new Slime(x,y);
                    Monsters.add(s);
                    System.out.println("Slime generated at " + x + " " + y+" Life is "+s.getHP());
                }
            }
        }
    }
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

}
