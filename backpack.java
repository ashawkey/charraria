import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class backpack {
	public final int BP_WIDTH=MainWindow.getWinWidth()/2;
	public final int BP_HEIGHT=MainWindow.getWinHeight()/4*3;
	private int maxVolume = 100;
	private int maxVolume2 =0;
	public final int volume = 20;
	private int firstItem=0;
	private int focusItem=0;
	private int firstItem2=0;
	private int focusItem2=0;
	private int focusVol=0;
	private ArrayList<Entity> ItemsList;
	private ArrayList<Entity> AviItems;
	public final int fontSize = 15;
	private Font mainFont=new Font("TimesNewRoman",Font.BOLD,fontSize);
	
	
	private Entity[][] items;
	
	public void bagPressW() {
		System.out.println("focusItem= "+focusItem);
		System.out.println("firstItem= "+firstItem);
		if(focusVol==0) {
		if(focusItem>0)
			focusItem--;
		else if(focusItem==0) {
			focusItem=maxVolume-1;
			if(focusItem>=maxVolume)
			firstItem=focusItem-volume+1;
		}
		if(firstItem>focusItem)
			firstItem--;
		}
		else {
			if(focusItem2==0) {
				if(focusItem2>0)
					focusItem2--;
				if(firstItem2>focusItem2)
					firstItem2--;
			}
		}
	}
	public void bagPressS() {
		System.out.println("focusItem= "+focusItem);
		System.out.println("firstItem= "+firstItem);
		if(focusVol==0) {
		if(focusItem<maxVolume-1)
			focusItem++;
		else if(focusItem==maxVolume-1)
			{
			focusItem=0;
			firstItem=0;
			}		
		if(firstItem+volume<=focusItem)
			firstItem=focusItem-volume+1;
		}
		else {
			if(focusItem2<maxVolume2-1)
				focusItem2++;
			else if(focusItem2==maxVolume2-1)
				{
				focusItem2=0;
				firstItem2=0;
				}
			
			if(firstItem2+volume<=focusItem2)
				firstItem2=focusItem2-volume+1;
		}
	}
	public void bagPressA() {
		if(focusVol==1) {
			focusVol=0;
			firstItem=0;
			focusItem=0;
		}
	}
	public void bagPressD() {
		if(focusVol==0&&maxVolume2!=0) {
			focusVol=1;
			firstItem2=0;
			focusItem2=0;
		}
	}
	public void bagPressENTER(int current) {
		if(focusVol==1) {
			meet(((EntityBlock)items[1][focusItem2]).block, true);
			if(items[1][focusItem].name=="Torch") {
				Entity e= new EntityBlock(new Torch());
				inventory.addEntity(e);			
			}	
		}
	}
	
	backpack(ArrayList<Entity> a){
		AviItems= new ArrayList<Entity>();
		ItemsList= new ArrayList<Entity>();
		ItemsList.add(new EntityBlock(new Torch()));
		items = new Entity[2][a.size()];
		int toolPos=0;
		maxVolume=a.size();
		//System.out.println("volume= "+maxVolume);
		int toolTmpPos=a.size();
		for(int i=0;i<a.size();i++) {
			if(a.get(i).getClass().equals(EntityBlock.class)) {
				//System.out.println("Add a Block at "+i);
				items[0][toolPos]=a.get(i);
				toolPos++;
			}
			else if(Weapon.class.isAssignableFrom(a.get(i).getClass())) {
				//System.out.println("Add a Tool at "+i);
				toolTmpPos--;
				items[0][toolTmpPos]=a.get(i);
			}
		}
		for(int i=0;i<a.size()-toolTmpPos;i++) {
			//System.out.println("toolPos is "+toolPos);
			//System.out.println("toolTmpPos is "+toolTmpPos);
			items[0][toolPos+i]=items[0][toolTmpPos+i];
			//items[0][toolTmpPos+i]=null;
		}
		for(Entity en:ItemsList) {
			if(meet(((EntityBlock)en).block,false))
				AviItems.add(en);
		}
		maxVolume2=AviItems.size();
	}
	public  boolean meet(Block b,boolean make) {
		int flag=0;
		for(int[]req :b.requirements) {
			for(Entity en:items[0]) {
				if(en.EntityNo==req[0]&&en.Total>=req[1])
				{
					if(make) en.Total-=req[1];
					flag++;
				}
			}
		}
		if(make) {
			for(Entity en:items[0]) {
				if (en.Total<=0)
				inventory.removeEntity(en);
			}
		}
		if(flag==b.requirements.length)
			return true;
		return false;
	}
	public void draw(Graphics g) {
		//System.out.println("AviItems size is "+AviItems.size());
		for(int i=0;i<AviItems.size();i++) {
			if(AviItems.size()!=0)
			items[1][i]=AviItems.get(i);
		}
		
		g.setColor(Color.gray);
        g.fillRect((MainWindow.getWinWidth()-BP_WIDTH)/2-2,(MainWindow.getWinHeight()-BP_HEIGHT)/2-2,BP_WIDTH+4,BP_HEIGHT+4);
		g.setColor(Color.black);
		g.fillRect((MainWindow.getWinWidth()-BP_WIDTH)/2,(MainWindow.getWinHeight()-BP_HEIGHT)/2,BP_WIDTH,BP_HEIGHT);
		g.setColor(Color.lightGray);

		g.drawLine(MainWindow.getWinWidth()/2, (MainWindow.getWinHeight()-BP_HEIGHT)/2, MainWindow.getWinWidth()/2, (MainWindow.getWinHeight()+BP_HEIGHT/5*2)/2);
		g.drawLine((MainWindow.getWinWidth()-BP_WIDTH)/2, (MainWindow.getWinHeight()+BP_HEIGHT/5*2)/2, (MainWindow.getWinWidth()+BP_WIDTH)/2, (MainWindow.getWinHeight()+BP_HEIGHT/5*2)/2);
		for(int i=0;i<volume;i++) {
			if(firstItem+i<maxVolume&&firstItem+i>=0)
			if(items[0][firstItem+i]!=null) {
				System.out.println("Blockname is "+items[0][firstItem+i].name);
				if(items[0][firstItem+i].Total!=0) {
                    Entity b;
                    b= items[0][firstItem+i];
                    g.setFont(mainFont);
                    g.setColor(Color.gray);
                    if(firstItem+i==focusItem&&focusVol==0) {
                        g.setColor(b.color);
                    }
                    g.drawString(String.valueOf(b.symbol),(MainWindow.getWinWidth()-BP_WIDTH)/2+30,(MainWindow.getWinHeight()-BP_HEIGHT)/2+30+fontSize*i);
                    g.setColor(Color.gray);
                    if(firstItem+i==focusItem&&focusVol==0) {
                        g.setColor(Color.white);
                    }
                    g.drawString(String.valueOf(b.name),(MainWindow.getWinWidth()-BP_WIDTH)/2+30+fontSize*2,(MainWindow.getWinHeight()-BP_HEIGHT)/2+30+fontSize*i);
                    g.drawString(String.valueOf(b.Total),MainWindow.getWinWidth()/2-fontSize*2,(MainWindow.getWinHeight()-BP_HEIGHT)/2+30+fontSize*i);
                }
			}
		}
		for(int i=0;i<AviItems.size();i++) {
			if(firstItem2+i<maxVolume&&firstItem2+i>=0)
			if(items[1][firstItem2+i]!=null) {
				if(items[1][firstItem2+i].Total!=0) {
				Entity t;
				t = items[1][firstItem2+i];
				g.setFont(mainFont);
				g.setColor(Color.gray);
				if(firstItem2+i==focusItem2&&focusVol==1) {
					g.setColor(t.color);
				}
				g.drawString(String.valueOf(t.symbol),(MainWindow.getWinWidth())/2+30,(MainWindow.getWinHeight()-BP_HEIGHT)/2+30+fontSize*i);
				g.setColor(Color.gray);
				if(firstItem2+i==focusItem2&&focusVol==1) {
					g.setColor(Color.white);
				}
				g.drawString(String.valueOf(t.name),(MainWindow.getWinWidth())/2+30+fontSize*2,(MainWindow.getWinHeight()-BP_HEIGHT)/2+30+fontSize*i);
				}
			}
		}
        g.setFont(mainFont);
        g.setColor(Color.white);
        if(focusVol==0) {
            if(items[focusVol][focusItem]!=null)
            g.drawString(String.valueOf(items[focusVol][focusItem].description),(MainWindow.getWinWidth()-BP_WIDTH)/2+30 , (MainWindow.getWinHeight()+BP_HEIGHT/5*2)/2+30);
        }
        else {
            if(items[focusVol][focusItem2]!=null)
                g.drawString(String.valueOf(items[focusVol][focusItem2].description),(MainWindow.getWinWidth()-BP_WIDTH)/2+30 , (MainWindow.getWinHeight()+BP_HEIGHT/5*2)/2+30);
        }
	}
}
