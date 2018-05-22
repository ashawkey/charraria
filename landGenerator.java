import java.awt.*;
import java.util.*;

public class landGenerator{
    private static final int WORLD_WID=1000;
    private Random r=new Random(RandSeed);
    private static final int WORLD_HEIGHT=200;
    private static final int RandSeed=42;
    private static final int CloudHeight=40;
    private static final int HoleHeight=20;
    public static int getWorWidth(){
        return WORLD_WID;
    }
    public static int getWorHeight(){
        return WORLD_HEIGHT;
    }

    public static int Focus_x=WORLD_WID/2;
    public static int Focus_y=WORLD_HEIGHT/2;
    private static final int fontSize=15;
    public static int getFontSize(){
        return fontSize;
    }

    private static final int window_offset_x= MainWindow.getWinWidth()/(2*fontSize);
    private static final int window_offset_y= MainWindow.getWinHeight()/(2*fontSize);
    private Font mainFont=new Font("TimesNewRoman",Font.BOLD,fontSize);
    private Font entityFont=new Font("TimesNewRoman",Font.PLAIN,fontSize/2);
    public static int getWinOffsetX(){
        return window_offset_x;
    }

    public static int getWinOffsetY(){
        return window_offset_y;
    }


    private static Block [][] world = new Block[WORLD_HEIGHT][WORLD_WID];
    public static Block getWorldBlock(int y,int x){
        return world[y][x];
    }
    public static Entity getWorldEntity(int y,int x){
        return frontWorld[y][x];
    }
    public static void changeWorldBlock(int y,int x,Block b){
        world[y][x]=b;
    }
    public static void changeWorldEntity(int y,int x,Entity e){
        frontWorld[y][x]=e;
    }

    public static boolean inWorld(int x, int y){
        return (x>=1 && x<WORLD_WID-1 && y>=1 && y<WORLD_HEIGHT-1);
    }

    private static Block[][] backWorld = new Block[WORLD_HEIGHT][WORLD_WID];
    private static Entity[][] frontWorld = new Entity[WORLD_HEIGHT][WORLD_WID];



    public void draw(Graphics g) {
        for(int i=Focus_x-window_offset_x;i<=Focus_x+window_offset_x;i++){
            for(int j=Focus_y+window_offset_y ;j>=Focus_y-window_offset_y;j--){
                if(i>=0 && i<WORLD_WID && j>=0 && j<WORLD_HEIGHT){
                    //backgroud layer
                    if(backWorld[j][i]!=null) {
                        Block b=backWorld[j][i];
                        g.setFont(mainFont);
                        g.setColor(b.color);
                        g.drawString(String.valueOf(b.symbol),(i-(Focus_x-window_offset_x))*fontSize,MainWindow.getWinHeight()-(j-(Focus_y-window_offset_y))*fontSize);
                    }
                    //solid&&fluid layer
                    if(world[j][i]!=null) {
                        Block b = world[j][i];
                        g.setFont(mainFont);
                        g.setColor(b.color);
                        //water flow animation
                        if(b instanceof Water) {
                            if(((Water) b).volume<=1) {
                                b=new Air(0);
                                continue;
                            }
                            int dx[]={0,1,-1};
                            int dy[]={-1,0,0};
                            for(int d=0;d<3;d++){
                                if(inWorld(i+dx[d],j+dy[d])) {
                                    Block nb = world[j + dy[d]][i + dx[d]];
                                    if (nb instanceof Air) {
                                        if (((Water) b).volume >= 2) {
                                            changeWorldBlock(j + dy[d], i + dx[d], new Water(1));
                                        }
                                        ((Water) b).modifyVol(-1);
                                    }
                                    if (nb instanceof Water) {
                                        if (((Water) nb).volume < ((Water) b).volume) {
                                            if (d == 0) {
                                                int remedy = 10 - ((Water) nb).volume;
                                                if (((Water) b).volume > remedy) {
                                                    ((Water) nb).modifyVol(remedy);
                                                    ((Water) b).modifyVol(-remedy);
                                                } else {
                                                    ((Water) nb).modifyVol(((Water) b).volume);
                                                    ((Water) b).volume = 0;
                                                }
                                            } else {
                                                ((Water) nb).modifyVol(1);
                                                ((Water) b).modifyVol(-1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //being damaged, blink animation
                        if (b instanceof solidBlock && ((solidBlock) b).Flag_damaged != 0) {
                            System.out.println("block blinking");
                            if (--((solidBlock) b).Flag_damaged % 2 == 0)
                                continue;
                        }
                        g.drawString(String.valueOf(b.symbol), (i - (Focus_x - window_offset_x)) * fontSize, MainWindow.getWinHeight() - (j - (Focus_y - window_offset_y)) * fontSize);
                    }
                    //entity layer
                    if(frontWorld[j][i]!=null)  {
                        Entity e=frontWorld[j][i];
                        if(e instanceof SolidMobsEntity){
                            if(((SolidMobsEntity) e).m==null || ((SolidMobsEntity) e).m.getHP()<=0 ) {
                                System.out.println("clear mobs");
                                changeWorldEntity(j,i,new Entity());
                                continue;
                            }
                        }
                        g.setFont(entityFont);
                        g.setColor(e.color);
                        g.drawString(String.valueOf(e.symbol),(i-(Focus_x-window_offset_x))*fontSize,MainWindow.getWinHeight()-(j-(Focus_y-window_offset_y))*fontSize);
                    }
                }
            }
        }
    }

    public void basicGenesis(){
        for(int i=0;i<WORLD_HEIGHT/2;i++) {
            for(int j=0;j<WORLD_WID;j++){
                world[i][j]=new Dirt();
            }
        }
        for(int i=WORLD_HEIGHT/2;i<WORLD_HEIGHT;i++) {
            Arrays.fill(world[i], new Air((i-WORLD_HEIGHT/2-30>0)?i-WORLD_HEIGHT/2-30:0));
        }
    }
    public void GrassLandGenesis(){
        int offset=r.nextInt(100);
        for(int j=0;j<WORLD_WID;j++){
            int jj=j+offset;
            double n=(ImprovedNoise.noise(jj*(1.0/300.0),0,0)+
                    ImprovedNoise.noise(jj*(1.0/150.0),0,0)*0.5+
                    ImprovedNoise.noise(jj*(1.0/75.0),0,0)*0.25+
                    ImprovedNoise.noise(jj*(1.0/37.5),0,0)*0.125)*100;
            if(n>0){
                for(int i=0;i<n;i++){
                    if(i+1>n) world[WORLD_HEIGHT/2+i][j]=new grassDirt();
                    else world[WORLD_HEIGHT/2+i][j]=new Dirt();
                }
            }
            else{
                for(int i=0;i>n;i--){
                    if(i-1<n)world[WORLD_HEIGHT/2+i][j]=new grassDirt();
                    else world[WORLD_HEIGHT/2+i][j]=new Air(0);
                }
            }
        }
    }
    public void WaterGenesis(){
        for(int j=1;j<WORLD_HEIGHT/2;j++){
            for(int i=1;i<WORLD_HEIGHT-1;i++){
                if(world[j][i].BlockNo==0){
                    System.out.println("Water genesis");
                    world[j][i]=new Water(10);
                }
            }
        }
    }
    public void EndOfWorldGenesis(){
        for(int i=0;i<WORLD_HEIGHT;i++){
            world[i][0]=new SideEndStone();
        }
        for(int i=0;i<WORLD_HEIGHT;i++){
            world[i][WORLD_WID-1]=new SideEndStone();
        }
        for(int i=0;i<WORLD_WID;i++){
            world[0][i]=new BaseStone();
        }
        for(int i=0;i<WORLD_WID;i++){
            world[WORLD_HEIGHT-1][i]=new TopAir();
        }
    }
    public void SlimTreeGenesis(){
        int TreeTotal=150;
        int TreeWidthToHeight=8;
        for(int z=0;z<TreeTotal;z++){
            int TreeHeight=(int)(r.nextGaussian()*10);
            int TreeWidth=(int)Math.ceil(TreeHeight/TreeWidthToHeight);
            int TreeX=z*WORLD_WID/TreeTotal+(int)r.nextGaussian()*5;
            int TreeY=0;
            int flag_Vacant=0;
            for(int j=0;j<WORLD_HEIGHT;j++){
                if(world[j][TreeX].getBlockNo()==0){
                    flag_Vacant++;
                    if(flag_Vacant>=1.5*TreeHeight){
                        TreeY=j-flag_Vacant;
                        break;
                    }
                }
            }
            if(TreeY!=0) {
                for (int i = TreeX; i < TreeX+TreeWidth; i++) {
                    for (int j = TreeY; j < TreeY+ TreeHeight; j++) {
                        world[j][i] = new Wood(1);
                    }
                }
                for (int i = TreeX + TreeWidth / 2; i < TreeX + TreeWidth / 2 + TreeWidth; i++) {
                    for (int j = TreeY + TreeHeight / 2; j < TreeHeight + TreeY; j++) {
                        int tmp = r.nextInt(10);
                        if (tmp > 8) {
                            world[j][i] = new Wood(3);
                        } else if (tmp > 3) {
                            world[j][i] = new Leaf();
                        }
                    }
                }
                for (int i = TreeX + TreeWidth / 2; i > TreeX + TreeWidth / 2 - TreeWidth; i--) {
                    for (int j = TreeY + TreeHeight / 2; j < TreeHeight + TreeY; j++) {
                        int tmp = r.nextInt(10);
                        if (tmp > 8) {
                            world[j][i] = new Wood(2);
                        } else if (tmp > 3) {
                            world[j][i] = new Leaf();
                        }
                    }
                }
            }
        }
    }
    public void CloudGenesis(){
        for(int j=WORLD_HEIGHT/2+CloudHeight;j<WORLD_HEIGHT-1;j++){
            for(int i=1;i<WORLD_WID-1;i++){
                if( backWorld[j][i-1]!=null || backWorld[j][i+1]!=null){
                    if(r.nextInt(100)>50){
                        backWorld[j][i]=new cloud();
                    }
                }
                else if(backWorld[j-1][i]!=null || backWorld[j+1][i]!=null){
                    if(r.nextInt(100)>80){
                        backWorld[j][i]=new cloud();
                    }
                }
                else {
                    if (r.nextInt(100) > 95) {
                        backWorld[j][i] = new cloud();
                    }
                }
            }
        }
    }
    public void HoleGenesis(){
        //perlin worms
        int totalWorms=80;
        while(totalWorms--!=0){
            int x=r.nextInt(WORLD_WID);
            int y=r.nextInt(WORLD_HEIGHT/2);
            int life=r.nextInt(100)+50;
            double radius=r.nextGaussian()*5;
            while(life--!=0){
                double nx=ImprovedNoise.noise(x*0.06,y*0.02,0);
                double ny=ImprovedNoise.noise(x*0.03,y*0.04,0);
                System.out.println(totalWorms+" "+nx+" "+ny);
                if(nx>0) x++; else x--;
                if(ny>0) y++; else y--;
                for(int xi=x-(int)radius;xi<x+(int)radius;xi++){
                    for(int yi=y-(int)radius;yi<y+(int)radius;yi++){
                        if(Math.sqrt(Math.pow((xi-x),2)+Math.pow((yi-y),2))<radius && inWorld(xi,yi))
                            world[yi][xi]=new Water(10);
                    }
                }

            }
        }
        //pockets
        for(int j=1;j<WORLD_HEIGHT/2;j++){
            for(int i=1;i<WORLD_WID-1;i++){
                if(i>WORLD_WID/2+10 || i<WORLD_WID/2-10) {
                    double n = ImprovedNoise.noise(j * 0.05, i * 0.05, 0);
                    if (n < (-0.25)) world[j][i] = new Air(0);
                }
            }
        }
    }
    public void PyramidGenesis(){

    }


    landGenerator(){
        basicGenesis();
        GrassLandGenesis();
        EndOfWorldGenesis();
        SlimTreeGenesis();
        CloudGenesis();
        WaterGenesis();
        HoleGenesis();
    }



}
