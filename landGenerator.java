import java.awt.*;
import java.util.*;

public class landGenerator{
    public static final int WORLD_WID=1000;
    private Random r=new Random();
    public static final int WORLD_HEIGHT=200;
    private static final int CloudHeight=WORLD_HEIGHT/2+40;
    private static final int HoleHeight=20;
    private static final int paintBuffer=10;
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
    public static Block getbackWorldBlock(int y,int x) {
    	return backWorld[y][x];
    }
    public static Entity getWorldEntity(int y,int x){
        return frontWorld[y][x];
    }
    public static void changeWorldBlock(int y,int x,Block b){
        world[y][x]=b;
    }
    public static void changebackWorldBlock(int y,int x,Block b) {
    	backWorld[y][x]=b;
    }
    public static void changeWorldEntity(int y,int x,Entity e){
        frontWorld[y][x]=e;
    }

    public static boolean inWorld(int x, int y){
        return (x>=1 && x<WORLD_WID-1 && y>=1 && y<WORLD_HEIGHT-1);
    }

    private static Block[][] backWorld = new Block[WORLD_HEIGHT][WORLD_WID];
    private static Entity[][] frontWorld = new Entity[WORLD_HEIGHT][WORLD_WID];
    private static int[][] visited=new int[WORLD_HEIGHT][WORLD_WID];
    /*
    -1: sky, always visited
    0: unvisited underground blocks
    1: recently not visited
    2~7202: recently visited
     */
    public static int getvis(int y,int x) {
    	return visited[y][x];
    }
    public static void setvis(int y,int x,int vis) {
    	visited[y][x]=vis;
    }
    private static int[][] light=new int[WORLD_HEIGHT][WORLD_WID];
    public static int getlight(int y,int x) {
    	return light[y][x];
    }
    public static void setlight(int y,int x,int light_) {
    	light[y][x]=light_;
    }

    private static int focus_radius=5;  //meter
    private static int focus_resist=1000; //timer unit


    public void draw(Graphics g) {
        int time=MainPanel.gettimer();
        double timeFactor=Math.abs(time-3600.0)/3600;
        //renew Light by time
        for(int i=Focus_x-window_offset_x-paintBuffer;i<Focus_x+window_offset_x+paintBuffer;i++){
            boolean flag=true;
            for(int j=Focus_y+window_offset_y+paintBuffer;j>Focus_y-window_offset_y-paintBuffer;j--){
                if(i>=0 && i<WORLD_WID && j>=0 && j+1<WORLD_HEIGHT) {
                    if (world[j][i].permeable && flag) light[j][i] = (int) (15 * timeFactor);
                    else {
                        if(world[j][i].permeable) light[j][i]=light[j+1][i];
                        else light[j][i] = light[j + 1][i] - 1 > 0 ? light[j + 1][i] - 1 : 0;
                        flag=false;
                    }
                }
            }
        }
        
        //hole light remedy
        for(int i=Focus_x-window_offset_x;i<Focus_x+window_offset_x;i++){
            for(int j=Focus_y+window_offset_y;j>Focus_y-window_offset_y;j--){
                if(i>=0 && i<WORLD_WID && j>=0 && j<WORLD_HEIGHT) {
                    if (light[j][i] <= 10) {
                        int dx[] = {0, 0, 1, -1};
                        int dy[] = {1, -1, 0, 0};
                        int mx = light[j][i];
                        for (int d = 0; d < 4; d++) {
                            if (inWorld(i+dx[d],j+dy[d]) &&light[j + dy[d]][i + dx[d]] > mx) {
                                mx = light[j + dy[d]][i + dx[d]];
                            }
                        }
                        if(world[j][i].permeable)light[j][i] = mx-1>0 ? mx-1 : 0;
                        else light[j][i]=mx-3>0?mx-3:0;
                    }
                }
            }
        }
        for(int i=Focus_x+window_offset_x;i>Focus_x-window_offset_x;i--){
            for(int j=Focus_y+window_offset_y;j>Focus_y-window_offset_y;j--){
                if(i>=0 && i<WORLD_WID && j>=0 && j<WORLD_HEIGHT) {
                    if (light[j][i] <= 10) {
                        int dx[] = {0, 0, 1, -1};
                        int dy[] = {1, -1, 0, 0};
                        int mx = light[j][i];
                        for (int d = 0; d < 4; d++) {
                            if (inWorld(i+dx[d],j+dy[d])&&light[j + dy[d]][i + dx[d]] > mx) {
                                mx = light[j + dy[d]][i + dx[d]];
                            }
                        }
                        if(world[j][i].permeable)light[j][i] = mx-1>0 ? mx-1 : 0;
                        else light[j][i]=mx-3>0?mx-3:0;
                    }
                }
            }
        }
        for(int i=Focus_x+window_offset_x;i>Focus_x-window_offset_x;i--){
            for(int j=Focus_y+window_offset_y;j>Focus_y-window_offset_y;j--){
            	if(i>=0 && i<WORLD_WID && j>=0 && j<WORLD_HEIGHT) {
            		if(world[j][i].BlockNo==20) {
            			pointLight(j,i,world[j][i].light);
            		}
            	}
            }	
        }
        //paint the world
        for(int i=Focus_x-window_offset_x-paintBuffer;i<=Focus_x+window_offset_x+paintBuffer;i++){
            for(int j=Focus_y+window_offset_y+paintBuffer;j>=Focus_y-window_offset_y-paintBuffer;j--){
                if(i>=0 && i<WORLD_WID && j>=0 && j<WORLD_HEIGHT){
                    double lightFactor=light[j][i]/15.0;
                    double heightFactor=1-j/(double)WORLD_HEIGHT;
                    Color col=new Color((int)(255*heightFactor*lightFactor),(int)(255*heightFactor*lightFactor),(int)(255*lightFactor));
                    g.setColor(col);
                    g.fillRect((i-(Focus_x-window_offset_x))*fontSize,MainWindow.getWinHeight()-(j-(Focus_y-window_offset_y))*fontSize, fontSize, fontSize);
                    if(visited[j][i]>=0 &&Math.sqrt(Math.pow(Math.abs(i-Focus_x),2)+Math.pow(Math.abs(j-Focus_y),2))<focus_radius){
                        visited[j][i]=time+2;
                    }
                    else if(visited[j][i]>=2){
                        int gap=time+2-visited[j][i];
                        if(gap<0) gap+=7200;
                        if(gap>=focus_resist) visited[j][i]=1;
                    }
                    if(visited[j][i]==0){
                        g.setColor(col.darker().darker().darker());
                        g.fillRect((i-(Focus_x-window_offset_x))*fontSize,MainWindow.getWinHeight()-(j-(Focus_y-window_offset_y)+1)*fontSize, fontSize, fontSize);
                    }
                    else if(visited[j][i]==1){
                        g.setColor(col.darker());
                        g.fillRect((i-(Focus_x-window_offset_x))*fontSize,MainWindow.getWinHeight()-(j-(Focus_y-window_offset_y)+1)*fontSize, fontSize, fontSize);
                    }
                    //backgroud layer
                    if(backWorld[j][i]!=null) {
                        Block b=backWorld[j][i];
                        g.setFont(mainFont);
                        g.setColor(b.color);
                        g.drawString(String.valueOf(b.symbol),(i-(Focus_x-window_offset_x))*fontSize,MainWindow.getWinHeight()-(j-(Focus_y-window_offset_y))*fontSize);
                        if(b instanceof cloud){
                            if(MainPanel.gettimer()%50==0) {
                                backWorld[j][i] = new Air(0);
                                if(inWorld(i-1,j)) backWorld[j][i - 1] = new cloud();
                            }
                        }
                    }
                    //solid&&fluid layer
                    if(world[j][i]!=null) {
                        Block b = world[j][i];
                        g.setFont(mainFont);
                        g.setColor(b.color);
                        //water flow animation
                        if(b instanceof Water) {
                            if(((Water) b).volume<=1) {
                                //changeWorldBlock(j, i, new Air(0));
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
                            for(int d=2;d>=0;d--){
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
                    /*
                    if(frontWorld[j][i]!=null)  {
                        Entity e=frontWorld[j][i];
                        if(e instanceof SolidMobsEntity){
                            if(((SolidMobsEntity) e).m==null || ((SolidMobsEntity) e).m.getHP()<=0 ) {
                                System.out.println("clear mobs");
                                changeWorldEntity(j,i,new Entity());
                                continue;
                            }
                        }
                    }
                    */
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
        int offset=r.nextInt(10000);
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
    public void StoneGenesis(){
        int offset=r.nextInt(10000);
        int stoneHeight=WORLD_HEIGHT/4;
        for(int j=0;j<WORLD_WID;j++) {
            int jj = j + offset;
            double n = (ImprovedNoise.noise(jj * (1.0 / 300.0), 0, 0) +
                    ImprovedNoise.noise(jj * (1.0 / 165.0), 0, 0) * 0.5 +
                    ImprovedNoise.noise(jj * (1.0 / 78.0), 0, 0) * 0.25 +
                    ImprovedNoise.noise(jj * (1.0 / 20.0), 0, 0) * 0.125) * 100;
            for (int i = 1; i <= stoneHeight + (int) n; i++) {
                world[i][j] = new Stone();
            }
        }
    }
    public void WaterGenesis(){
        for(int j=1;j<WORLD_HEIGHT/2 - 10;j++){
            for(int i=1;i<WORLD_HEIGHT-1;i++){
                if(world[j][i].BlockNo==0) {
                    world[j][i] = new Water(10);
                }
            }
        }
    }
    public void bgMountainGenesis(){
        int offset=r.nextInt(10000);
        double oldn=0;
        for(int j=0;j<WORLD_WID;j++){
            int jj=WORLD_WID-(j+offset);
            double n=(ImprovedNoise.noise(jj*(1.0/300.0),0,0)+
                    ImprovedNoise.noise(jj*(1.0/150.0),0,0)*0.5+
                    ImprovedNoise.noise(jj*(1.0/75.0),0,0)*0.25+
                    ImprovedNoise.noise(jj*(1.0/37.5),0,0)*0.125)*100;
            backWorld[WORLD_HEIGHT/2+(int)Math.floor(n)][j]=new bgMountainBlock((oldn==n)?1:(oldn>n)?2:3,100);
            oldn=n;
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
    public void TreeGenesis(){

    }
    public void SlimTreeGenesis(){
        int TreeTotal=150;
        int TreeWidthToHeight=8;
        for(int z=0;z<TreeTotal;z++){
            int TreeHeight=(int)(r.nextGaussian()*10);
            int TreeWidth=(int)Math.ceil(TreeHeight/TreeWidthToHeight);
            int TreeX=z*WORLD_WID/TreeTotal+(int)r.nextGaussian()*5;
            if(TreeX>WORLD_WID || TreeX <0) continue;;
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
        int offset=r.nextInt(10000);
        for(int j=CloudHeight;j<WORLD_HEIGHT;j++) {
            for (int i = 1; i < WORLD_WID - 1; i++) {
                double n = ImprovedNoise.noise((j + offset) * 0.5, (i + offset) * 0.05, 0);
                if (n < (-0.45)) backWorld[j][i] = new cloud();
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
            double radius=r.nextGaussian()*2;
            while(life--!=0){
                double nx=ImprovedNoise.noise(x*0.06,y*0.02,0);
                double ny=ImprovedNoise.noise(x*0.03,y*0.04,0);
                if(nx>0) x++; else x--;
                if(ny>0) y++; else y--;
                for(int xi=x-(int)radius;xi<x+(int)radius;xi++){
                    for(int yi=y-(int)radius;yi<y+(int)radius;yi++){
                        if(Math.sqrt(Math.pow((xi-x),2)+Math.pow((yi-y),2))<radius && inWorld(xi,yi) && !world[yi][xi].permeable)
                            world[yi][xi]=new Water(10);
                    }
                }
            }
        }
        //pockets
        int offset=r.nextInt(10000);
        for(int j=1;j<WORLD_HEIGHT/2;j++){
            for(int i=1;i<WORLD_WID-1;i++){
                if(i>WORLD_WID/2+10 || i<WORLD_WID/2-10) {
                    double n = ImprovedNoise.noise((j+offset) * 0.05, (i+offset) * 0.05, 0);
                    if (n < (-0.25)) world[j][i] = new Air(0);
                }
            }
        }
    }
    public void SandGenesis(){
        int offset=r.nextInt(10000);
        for(int j=WORLD_HEIGHT/5;j<WORLD_HEIGHT/2;j++){
            for(int i=1;i<WORLD_WID-1;i++){
                if(i>WORLD_WID/2+10 || i<WORLD_WID/2-10) {
                    double n = ImprovedNoise.noise((j+offset) * 0.05, (i+offset) * 0.05, 0);
                    if (n < -0.45 && !world[j][i].permeable) world[j][i] = new Sand();
                }
            }
        }
    }
    public void MineralGenesis(){
        int offset=r.nextInt(10000);
        for(int j=1;j<WORLD_HEIGHT/2;j++){
            for(int i=1;i<WORLD_WID-1;i++){
                double n = ImprovedNoise.noise((j+offset) * 0.05, (i+offset) * 0.05, 0);
                if (n < (-0.55) && !world[j][i].permeable) world[j][i] = new Coal();
            }
        }
        offset=r.nextInt(10000);
        for(int j=1;j<WORLD_HEIGHT/2;j++){
            for(int i=1;i<WORLD_WID-1;i++){
                double n = ImprovedNoise.noise((j+offset) * 0.05, (i+offset) * 0.05, 0);
                if (n < (-0.60) && !world[j][i].permeable) world[j][i] = new IronOre();
            }
        }

    }
    public void shadeGenesis(){
        for(int i=0;i<WORLD_WID;i++){
            int flag=5;
            for(int j=WORLD_HEIGHT-1;j>0;j--){
                if(world[j][i].permeable && world[j][i].BlockNo!=3){
                    visited[j][i]=-1;
                }
                else if(flag>0){
                    flag--;
                    visited[j][i]=-1;
                }
                else visited[j][i]=0;
            }
        }
    }
    public void pointLight(int y,int x,int init) {
    	light[y][x]=light[y][x]>init?light[y][x]:init;
    	int minx=x-init-1>0?x-init-1:0;
    	int miny=y-init-1>0?y-init-1:0;
    	int maxx=x+init+1<WORLD_WID?x+init+1:WORLD_WID;
    	int maxy=y+init+1<WORLD_HEIGHT?y+init+1:WORLD_HEIGHT;
    	for(int i=minx;i<maxx;i++) {
    		for(int j=miny;j<maxy;j++) {
    			if(light[y][x]-Math.abs(y-j)-Math.abs(x-i)>0)
    			light[j][i]=(light[y][x]-Math.abs(y-j)-Math.abs(x-i))>light[j][i]?light[y][x]-Math.abs(y-j)-Math.abs(x-i):light[j][i];
    		}
    	}
    }
    public void lightGenesis(){
        for(int i=0;i<WORLD_WID;i++){
            boolean flag=true;
            for(int j=WORLD_HEIGHT-1;j>0;j--){
                if(world[j][i].permeable && flag) light[j][i]=15;
                else {
                    light[j][i]=(light[j+1][i]-1)>0?light[j+1][i]-1:0;
                    flag=false;
                }
                if(world[j][i].BlockNo==20) {
                	System.out.println("We find a torch!");
                	pointLight(j,i,((Torch)world[i][j]).light);
                }
            }
        }
        for (int i = 1; i < WORLD_WID-1; i++) {
            for(int j=WORLD_HEIGHT-1;j>1;j--) {
                if(world[j][i].permeable && light[j][i]!=15){
                    int dx[]={0,0,1,-1};
                    int dy[]={1,-1,0,0};
                    int mx=light[j][i];
                    for(int d=0;d<4;d++){
                        if(light[j+dy[d]][i+dx[d]]>mx){
                            mx=light[j+dy[d]][i+dx[d]];
                        }
                    }
                    light[j][i]=mx-1>0?mx-1:0;
                }
            }
        }
    }
    landGenerator(){
        basicGenesis();
        GrassLandGenesis();
        shadeGenesis();
        StoneGenesis();
        MineralGenesis();
        SlimTreeGenesis();
        CloudGenesis();
        bgMountainGenesis();
        //WaterGenesis();
        HoleGenesis();
        SandGenesis();
        lightGenesis();
        EndOfWorldGenesis();
    }



}
