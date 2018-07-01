import java.awt.*;
import java.util.ArrayList;

public class Entity {
    protected static final int Block2EntityOffset=1000;
    protected int EntityNo = 0;  //null entity
    protected String name;
    protected char symbol;
    protected Color color;
    protected int Total;
    protected boolean stackable;
    protected String description;
    public boolean getEntityStackable(){
        return stackable;
    }
    public int getEntityNo(){
        return EntityNo;
    }
    public char getEntitySymbol(){
        return symbol;
    }
    public Color getEntityColor(){
        return color;
    }
    protected double WorkDist;
    public double getWorkDist(){
        return WorkDist;
    }
    void setTotal(int i) {
    	Total=i;
    }
}

class EntityBlock extends Entity{
    public Block block;
    EntityBlock(Block b){
        super();
        block=b;
        name=b.getClass().getName();
        stackable=true;
        symbol=b.symbol;
        color=b.color;
        EntityNo=b.BlockNo+Block2EntityOffset;
        Total=1;
        description=b.description;
        if(MainWindow.DEBUG==1) WorkDist=999;
        else WorkDist=10;
    }
    EntityBlock(Block b, int i){
        super();
        block=b;
        name=b.getClass().getName();
        stackable=true;
        symbol=b.symbol;
        color=b.color;
        EntityNo=b.BlockNo+Block2EntityOffset;
        Total=i;
        description=b.description;
        if(MainWindow.DEBUG==1) WorkDist=999;
        else WorkDist=10;
    }
    void setTolal(int i) {
    	Total=i;
    }
}


abstract class Tools extends Entity{
    int Slowness;
    int DestroyAbility;
    Tools(){
        super();
        stackable=false;
        Total=1;
    }
}

abstract class Weapon extends Tools{
    int attack;
    Weapon(){
        super();
    }
}

abstract class LongRangedWeapon extends Weapon{
    public int SPEED;
    LongRangedWeapon(){
        super();
    }
}

class Hand extends Weapon{
    Hand(){
        super();
        name="Hand";
        DestroyAbility=5;
        attack=1;
        Slowness=100;  //ms
        EntityNo=1;
        symbol='@';
        color=Color.WHITE;
        description="Bare Hand is the best!";
        WorkDist=2;
    }
}

class HandOfGod extends Weapon{
    HandOfGod(){
        super();
        name="HandOfGod";
        DestroyAbility=999;
        attack=999;
        Slowness=10;
        EntityNo=1;
        symbol='@';
        color=Color.orange;
        description="ONLY for DEBUG!";
        WorkDist=999;
    }
}

class Arrow extends LongRangedWeapon{
    Arrow(){
        super();
        DestroyAbility=0;
        attack=999;
        Slowness=1000;
        EntityNo=2;
        symbol='<';
        WorkDist=999;
        SPEED=1;
    }
}

abstract class SolidMobsEntity extends Entity{
    public Mobs m;
    SolidMobsEntity(){
        super();
    }
}

class SolidMonsterEntity extends SolidMobsEntity{
    public int damage;
    SolidMonsterEntity(int _dmg, Monster _m){
        super();
        EntityNo=12;
        symbol='.';
        color=new Color(0,0,0,0);
        description="--Monster--";
        damage=_dmg;
        m=_m;
    }
}

class SolidHeroEntity extends SolidMobsEntity{
    SolidHeroEntity(hero _h){
        super();
        EntityNo=13;
        symbol='.';
        color=new Color(0,0,0,0);
        description="--Myself--";
        m=_h;
    }
}
