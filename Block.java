import java.awt.*;
public abstract class Block {
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
        BlockNo=1;
        life=30;
        symbol='+';
        color= new Color(80,26,0); //brown
        description="--dirt--";

    }
}

class Air extends Fluid{
    Air(int n){
        super();
        BlockNo=0;
        symbol='.' ;
        color =new Color(0,0,255,n);
        description="--air--";
    }
}

class grassDirt extends solidBlock{
    grassDirt(){
        super();
        BlockNo=2;
        life=30;
        symbol='+';
        color=Color.GREEN;
        description="--grass--";
    }
}

class Water extends Fluid{
    Water(){
        super();
        BlockNo=3;
        symbol='~';
        color=Color.BLUE;
        description="--water--";
    }
}

class Wood extends solidBlock{
    Wood(){
        super();
        permeable=true;
        BlockNo=4;
        life=40;
        symbol='|';
        color=new Color(115,74,35);
        description="--Wood--";
    }
    Wood(int i){
        super();
        BlockNo=4;
        permeable=true;
        life=40;
        switch(i){
            case 1:
                symbol='|';
                break;
            case 2:
                symbol='\\';
                break;
            case 3:
                symbol='/';
                break;
        }
        color=new Color(110,85,47);
        description="--Wood--";
    }
}

class Leaf extends solidBlock{
    Leaf(){
        super();
        BlockNo=5;
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
        BlockNo=6;
        symbol='\u2588';
        color=Color.black;
        description="UNBREAKABLE";
    }
}

class TopAir extends solidBlock{
    TopAir(){
        super();
        life=1000000000;
        BlockNo=7;
        symbol='.';
        color=Color.WHITE;
        description="UNTOUCHABLE";
    }
}

class SideEndStone extends solidBlock{
    SideEndStone(){
        super();
        life=1000000000;
        BlockNo=8;
        symbol='\u23f8';
        color=Color.BLACK;
        description="UNREACHABLE";
    }
}

//decoration
class cloud extends Fluid{
    cloud(){
        super();
        BlockNo=9;
        symbol='c';
        color=Color.WHITE;
        description="--cloud--";
    }
}

class rain extends Fluid{
    rain(){
        super();
        BlockNo=10;
        symbol='\u26C6';
        color=new Color(0,114,255,130);
    }
}

class flower extends solidBlock{
    flower(){
        super();
        BlockNo=11;

    }
}

abstract class SolidMobsBlock extends solidBlock{
    public Mobs m;
    SolidMobsBlock(){
        super();
    }
}

class SolidMonsterBlock extends SolidMobsBlock{
    public int damage;
    SolidMonsterBlock(int _dmg, Monster _m){
        super();
        BlockNo=12;
        life=999999;
        symbol='.';
        color=new Color(0,0,0,0);
        description="--Monster--";
        damage=_dmg;
        m=_m;
    }
}

class SolidHeroBlock extends SolidMobsBlock{
    SolidHeroBlock(hero _h){
        super();
        BlockNo=13;
        life=999999;
        symbol='.';
        color=new Color(0,0,0,0);
        description="--Myself--";
        m=_h;
    }
}

class SolidProjectileBlock extends solidBlock{
    SolidProjectileBlock(){
        super();
        BlockNo=14;
        symbol='.';
        color=new Color(0,0,0,0);
        life=9999999;
        description="--projectile--";
    }
}