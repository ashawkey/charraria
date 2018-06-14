import java.awt.*;
public abstract class Block {
	protected int light=0;
	protected boolean Syntheticable=false;
	protected int [][] requirements;
	protected String name;
    protected int BlockNo;
    protected boolean permeable;
    protected char symbol;
    protected Color color;
    protected String description;
    public int getBlockNo(){
        return BlockNo;
    }
    public char getBlockSymbol(){
        return symbol;
    }
    public Color getBlockColor(){
        return color;
    }
}
abstract class solidBlock extends Block {
    protected int life;
    protected int Flag_damaged;
    public void modifyHP(int x) {life+=x;Flag_damaged=2;}
    solidBlock(){
        super();
        permeable=false;
    }
}
abstract class Fluid extends Block{
   Fluid(){
       super();
       permeable=true;
   }
}
class Dirt extends solidBlock {
    Dirt(){
        super();
        name="Dirt";
        BlockNo=1;
        life=30;
        //symbol='⛶';
        symbol='+';
        color= new Color(80,26,0); //brown
        description="--dirt--";

    }
}
class Air extends Fluid{
    Air(int n){
        super();
        name="Air";
        BlockNo=0;
        symbol='.' ;
        color =new Color(0,0,255,n);
        description="--air--";
    }
}
class grassDirt extends solidBlock{
    grassDirt(){
        super();
        name="grass";
        BlockNo=2;
        life=30;
        symbol='+';
        color=Color.GREEN;
        description="--grass--";
    }
}
class Water extends Fluid{
    public int volume;
    public void modifyVol(int x){
        if(volume+x>10 || volume+x<0) return;
        volume+=x;
        color=new Color(0,0,255,volume*25);
    }
    Water(int n){
        super();
        name="water";
        BlockNo=3;
        symbol='~';
        color=new Color(0,0,255, n*25);
        description="--water--";
        volume=n;
    }
}
class Wood extends solidBlock{
    Wood(){
        super();
        name="Wood";
        permeable=true;
        BlockNo=4;
        life=40;
        symbol='|';
        color=new Color(115,74,35);
        description="--Wood--";
    }
    Wood(int i){
        super();
        permeable=true;
        life=40;
        switch(i){
            case 1:
                name="Wood1";
                symbol='|';
                BlockNo=4;
                break;
            case 2:
                name="Wood2";
                symbol='\\';
                BlockNo=5;
                break;
            case 3:
                name="Wood3";
                symbol='/';
                BlockNo=6;
                break;
        }
        color=new Color(110,85,47);
        description="--Wood--";
    }
}
class Leaf extends solidBlock{
    Leaf(){
        super();
        name="Leaf";
        BlockNo=7;
        permeable=true;
        life=10;
        symbol='*';
        color=new Color(0,255,0);
        description="--leaf--";
    }
}
//end of world set
class BaseStone extends solidBlock{
    BaseStone(){
        super();
        life=1000000000;
        BlockNo=8;
        symbol='\u2588';
        color=Color.black;
        description="UNBREAKABLE";
    }
}

class TopAir extends solidBlock{
    TopAir(){
        super();
        life=1000000000;
        BlockNo=9;
        symbol='.';
        color=Color.WHITE;
        description="UNTOUCHABLE";
    }
}

class SideEndStone extends solidBlock{
    SideEndStone(){
        super();
        life=1000000000;
        BlockNo=10;
        symbol='\u23f8';
        color=Color.BLACK;
        description="UNREACHABLE";
    }
}

//decoration
class cloud extends Fluid{
    cloud(){
        super();
        BlockNo=11;
        symbol='c';
        color=Color.WHITE;
        description="--cloud--";
    }
}

class rain extends Fluid{
    rain(){
        super();
        BlockNo=12;
        symbol='\u26C6';
        color=new Color(0,114,255,130);
    }
}

class flower extends solidBlock{
    flower(){
        super();
        BlockNo=13;

    }
}

class bgMountainBlock extends Fluid{
    bgMountainBlock(int i, int alpha){
        super();
        BlockNo=14;
        switch(i){
            case 1:
                symbol='_';
                BlockNo=141;
                break;
            case 2:
                symbol='\\';
                BlockNo=142;
                break;
            case 3:
                symbol='/';
                BlockNo=143;
                break;
        }
        color=new Color(110,85,47,alpha);
        description="--Mountain--";
    }
}

class redWood1 extends Dirt{
    redWood1(){
        super();
        life=20;
        color=Color.red;
        symbol='_';
        BlockNo=15;
    }
}
class redWood2 extends Dirt{
    redWood2(){
        super();
        life=20;
        color=Color.red;
        symbol='\\';
        BlockNo=99;
    }
}
class redWood3 extends Dirt{
    redWood3(){
        super();
        life=20;
        color=Color.red;
        symbol='/';
        BlockNo=98;
    }
}
class redWood4 extends Dirt{
    redWood4(){
        super();
        life=20;
        color=Color.red;
        symbol='|';
        BlockNo=97;
    }
}
class BrownWood1 extends Dirt{
    BrownWood1(){
        super();
        life=20;
        color=new Color(255,100,100);
        symbol='_';
        BlockNo=96;
    }
}


class Stone extends solidBlock{
    Stone() {
        super();
        BlockNo = 16;
        life = 50;
        symbol = '⛶';
        color =Color.LIGHT_GRAY;
        description = "--stone--";
    }
}

class Sand extends solidBlock{
    Sand() {
        super();
        BlockNo = 17;
        life = 20;
        symbol = '*';
        color =Color.yellow; //brown
        description = "--sand--";
    }
}

class Coal extends solidBlock{
    Coal(){
        super();
        BlockNo=18;
        life=30;
        symbol='m';
        color=new Color(200,200,200);
        description="--coal--";
    }
}

class IronOre extends solidBlock{
    IronOre(){
        super();
        BlockNo=19;
        life=60;
        symbol='m';
        color=Color.gray;
        description="--iron ore--";
    }
}

class Torch extends solidBlock{	
	Torch(){
		super();
		light=15;
		Syntheticable=true;
		requirements=new int [][] {{1004,2},{1018,1}};
		name="Torch";
		BlockNo=20;
		symbol='Ȉ';
		color=Color.RED;
		description="You may need it at night.";
	}
}

class BamboWood extends solidBlock{
    BamboWood() {
        super();
        life = 20;
        color = Color.orange;
        symbol = 'I';
        BlockNo = 21;
    }
}

class BamboLeaf1 extends solidBlock{
    BamboLeaf1() {
        super();
        life = 20;
        color = Color.GREEN;
        symbol = '\\';
        BlockNo = 23;
    }
}
class BamboLeaf2 extends solidBlock{
    BamboLeaf2() {
        super();
        life = 20;
        color = Color.GREEN;
        symbol = '/';
        BlockNo = 22;
    }
}
