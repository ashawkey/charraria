import java.awt.*;
import java.util.ArrayList;

public class inventory {
    private static final int InventoryFontSize=20;
    private Font InventoryFont=new Font("TimesNewRoman",Font.BOLD,InventoryFontSize);
    private Font InventoryDescriptionFont=new Font("arial",Font.ITALIC,InventoryFontSize/2+5);
    private static int volume=10;
    private static int showvolume=7;
    private static int firstItem=0;
    private static int current=0;
    private static ArrayList<Entity> a=new ArrayList<Entity>(){
        @Override
        public boolean contains(Object o) {
            Entity en=(Entity) o;
            for(Entity e:this){
                if(e.EntityNo==en.EntityNo)
                    return true;
            }
            return false;
        }

        @Override
        public int indexOf(Object o) {
            Entity en=(Entity) o;
            for(int i=0;i<this.size();i++){
                if(this.get(i).EntityNo==en.EntityNo){
                    return i;
                }
            }
            return -1;
        }
    };

    inventory(){
        a.add(new Hand());
        if(MainWindow.DEBUG==1){
            a.add(new HandOfGod());
            a.add(new EntityBlock(new Torch(),99));
            a.add(new EntityBlock(new Stone(),999));
            a.add(new EntityBlock(new redWood1(),99));
            a.add(new EntityBlock(new redWood2(),99));
            a.add(new EntityBlock(new redWood3(),99));
            a.add(new EntityBlock(new redWood4(),99));
            a.add(new EntityBlock(new BrownWood1(),99));
            a.add(new EntityBlock(new BamboWood(),99));
            a.add(new EntityBlock(new BamboLeaf1(),99));
            a.add(new EntityBlock(new BamboLeaf2(),99));

        }
    }
    public static int getVolume() {
    	return volume;
    }
    public static int getItemTotal() {
    	return a.size();
    }
    public static int getItemNo(int i) {
    	return a.get(i).EntityNo;
    }
    public static int getItemNum(int i) {
    	return a.get(i).Total;
    }
    public static int getcurrent() {
    	return current;
    }
    public static ArrayList<Entity> geta(){
    	return a;
    }
    public boolean isFull(){
        return a.size()==volume;
    }

    public Entity getCurrent(){
        if(current>=a.size()){
            current=a.size()-1;
        }
        return a.get(current);
    }

    public void moveCurrent(int mv){
        if(mv<0){
            for(int i=mv;i<0;i++){
                if(current>firstItem) current--;
                else if (firstItem>0){
                	firstItem--;
                	current--;
                }
            }
        }
        else{
            for(int i=0;i<mv;i++){
            	System.out.println("Current is "+current);
            	System.out.println("First is "+firstItem);
            	System.out.println("Size is "+a.size());
                if(current+1<firstItem+showvolume&&current+1<a.size()) current++;
                else {
                	if(current+1<a.size()) {
                		firstItem++;
                		current++;
                	}
                }
            }
        }
    }

    public static void setCurrent(int s){
        System.out.println("serCurrent "+s);
        if(s<a.size() && s>=0){
            current=s;
            if(current<a.size()-showvolume)
            	firstItem=current;
            else firstItem=a.size()-showvolume;
            System.out.println("successfully set");
        }
    }

    public static void addEntity(Entity en){
        /*
        if(a.size()==volume) {
            return false;
        }
        */
        System.out.println("ADD:"+en.symbol);
        System.out.println("ADD:"+en.name);
        if(a.contains(en) && a.get(a.indexOf(en)).stackable ){
            a.get(a.indexOf(en)).Total++;
            System.out.println("Contains:"+en.Total);
            return;
        }
        a.add(en);
        System.out.println("Newly added "+en.symbol);
    }

    public static void removeEntity(Entity en){
        if(a.contains(en)) {
            a.remove(a.indexOf(en));
        }
    }

    public void draw(Graphics g){
        //  (*)4 (.)1
        StringBuffer d=new StringBuffer();
        for(int i=0;i<a.size();i++){
            Entity en=a.get(i);
            if(i==current){
                d.append(en.description);
            }

        }
        g.setColor(Color.gray);
        g.fillRect(0,0,360,30);
        g.setColor(Color.black);
        g.fillRect(5,3,350,24);
        g.setFont(InventoryFont);
        g.setColor(Color.white);
        for(int i=firstItem;i<a.size();i++) {
        	if((i-firstItem)==showvolume)
        		break;
        	Entity en = a.get(i);
        	g.setFont(InventoryFont);
        	g.setColor(Color.white);
        	if(i==current)
            g.setColor(en.color);
            g.drawString(String.valueOf(en.symbol),25*2*(i-firstItem)+10,18);
            if(en.Total<=99)
            g.drawString(String.valueOf(en.Total), 25*(2*(i-firstItem))+25,18);
            else
            g.drawString(String.valueOf(99), 25*(2*(i-firstItem))+25,18);
        }
        /*
        g.setColor(Color.white);
        g.setFont(InventoryDescriptionFont);
        g.drawString(d.toString(),10,40);
        */
    }

}
