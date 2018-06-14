import java.util.Random;

public enum Direction {
    L(2),
    R(4),
    JUMP(1),
    LJUMP(3),
    RJUMP(5),
    DOWN(-1),
    STOP(0);

    private static final Direction DIRs[]={L,R,JUMP,LJUMP,RJUMP,STOP};

    int value;
    Direction(int v){
        value=v;
    }
    public static Direction getDir(int v){
        for(Direction dir : DIRs)
            if(v==dir.value)
                return dir;
        return null;
    }

    public static Direction compose(Direction base, Direction add){
        return getDir(base.value+add.value);
    }
    public static Direction dispose(Direction base, Direction sub){
        return getDir(base.value-sub.value);
    }

    public static boolean containDir(Direction base,Direction con){
        if(base.value==-1) return false;
        if(base.value%2!=0 && con.value==1) return true;
        else if(con.value!=1&&base.value-1==con.value) return true;
        return false;
    }
    public static Direction randomDir(){
        Random r=new Random();
        int d=r.nextInt(6);
        if(d!=1) return getDir(d);
        else return Direction.STOP;
    }
    public static Direction projectileDir(int x1,int y1,int x2,int y2){
        Direction d=Direction.STOP;
        if(x2-x1>0) d=compose(d,R);
        else d=compose(d,L);
        if(y2>y1) d=compose(d,JUMP);
        return d;
    }

}
