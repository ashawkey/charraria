import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class archive {
	static ObjectOutputStream saveStream = null;
    static ObjectInputStream loadStream = null;
    static Archive archive = null;
    
    public static boolean readArchive() {
        boolean success = false;
        try {
            loadStream = new ObjectInputStream(
                    new BufferedInputStream(
                    new FileInputStream("./save/.game_save.sav")));
            archive=null;
            archive = (Archive)loadStream.readObject();
            loadStream.close();
            System.out.println("Archive has been read.");
            success = true;
        } catch (FileNotFoundException e) {
            System.out.println("(File not found)");
        } catch (InvalidClassException e) {
        	System.out.println("(Version error)");
        } catch (Exception e) {
            //ExceptionManager.handleException(e);
//          ConsoleWindow.println("Failed.");
        } finally {
            try {
                if (loadStream != null)
                    loadStream.close();
            } catch (IOException e) {
                //ExceptionManager.handleException(e);
            }
        }
        return success;
    }
    
    public static boolean writeArchive() {
    	System.out.println("Writing archive to disk...");
        if (archive == null) {
           // ConsoleWindow.println("Null archive.");
            return false;
        }
        try {
            saveStream = new ObjectOutputStream(
                    new BufferedOutputStream(
                    new FileOutputStream("./save/.game_save.sav")));
            saveStream.writeObject(archive);
            saveStream.close();
            System.out.println("Archive has been written.");
            return true;
        } catch (Exception e) {
            //ExceptionManager.handleException(e);
            return false;
        } finally {
            try {
                saveStream.close();
            } catch (IOException e) {
                //ExceptionManager.handleException(e);
            }
        }

    }
    public static void saveArchive() {
      archive.save();
  }
    public static void loadArchive() {
      archive.load();
  }
    public static void saveGame() {
    	if (MainPanel.stat == MainPanel.STAT_GAME ||
                MainPanel.stat == MainPanel.STAT_PAUSE) {
            archive = new Archive();
            saveArchive();
            if (!writeArchive()) {
                archive = null;
            }
            archive = null;
        }
    }
    public static boolean loadGame() {
    	archive = new Archive();
        if (!readArchive()) {
            return false;
        }
        loadArchive();
        archive = null;
        MainPanel.stat = MainPanel.STAT_GAME;
        return true;
    }
    private static class Archive implements Serializable {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1234L;
		private int x;
		private int y;
        private int focus_x;
        private int focus_y;
        private int [][] world;
        private int [][] backWorld;
        private int [][] frontWorld;
        private int invVolume;
        private int invCurrent;
        private int [] invItemNo;
        private int [] invItemNum;
        private int [][] light;
        private int [][] visited;
        private int timer;
        
        public Archive() {
        	world=new int[200][1000];
        	backWorld = new int[200][1000];
        	frontWorld = new int[200][1000];
        	light= new int [200][1000];
        	visited= new int [200][1000];
        }
        void save() {
        	timer=MainPanel.gettimer();
        	System.out.println("SAVE START");
            focus_x=landGenerator.Focus_x;
            focus_y=landGenerator.Focus_y;
            x=MainPanel.heroX;
            y=MainPanel.heroY;
            invVolume=inventory.getItemTotal();
            invItemNo=new int[invVolume];
            invItemNum=new int[invVolume];
            invCurrent=inventory.getcurrent();
            for(int i=0;i<invVolume;i++) {
            	invItemNo[i]=inventory.getItemNo(i);
            	invItemNum[i]=inventory.getItemNum(i);
            }
            for(int i=0;i<200;i++) {
            	System.out.println("i="+i);
            	for(int j=0;j<1000;j++) {
            		light[i][j]=landGenerator.getlight(i, j);
            		visited[i][j]=landGenerator.getvis(i,j);
            		world[i][j]=landGenerator.getWorldBlock(i,j).getBlockNo();
            		//frontWorld[i][j]=landGenerator.getWorldEntity(i, j).getEntityNo();
            		if(landGenerator.getbackWorldBlock(i, j)!=null)
            		backWorld[i][j]=landGenerator.getbackWorldBlock(i, j).getBlockNo();
            		//System.out.print(world[i][j]+" ");
            	}
            }
        }
        void load() {
        	MainPanel.settimer(timer);
            landGenerator.Focus_x=focus_x;
            System.out.println("the focus_x is "+focus_x);
            landGenerator.Focus_y=focus_y;
            MainPanel.heroX=x;
            MainPanel.heroY=y;
            //MainPanel.setInventory(Inventory);
            for(int i=0;i<landGenerator.getWorHeight();i++) {
            	for(int j=0;j<landGenerator.getWorWidth();j++) {
            		landGenerator.setlight(i, j, light[i][j]);
            		landGenerator.setvis(i, j, visited[i][j]);
            		Block Worldtmp = null;
            		Block backWorldtmp = null;
            		Entity frontWorldtmp = null;
            		Worldtmp=switchBlock(world[i][j]);
            		backWorldtmp=switchBlock(backWorld[i][j]);
            		
            		landGenerator.changeWorldBlock(i,j,Worldtmp);
            		//landGenerator.changeWorldEntity(i,j,frontWorldtmp);
            		landGenerator.changebackWorldBlock(i, j, backWorldtmp);
            		
            	}
            }
            Entity Entitytmp=null;
    		for(int i=0;i<invVolume;i++) {
    			Entitytmp=switchEntity(invItemNo[i]);
    			if(Entitytmp!=null) {
    			Entitytmp.setTotal(invItemNum[i]);
    			inventory.addEntity(Entitytmp);
    			}
    		}
        }
        Block switchBlock(int i) {
        	Block Worldtmp = null;
        	switch(i) {
    		case 0:
    			Worldtmp= new Air(0);
    			break;
    		case 1:
    			Worldtmp= new Dirt();
    			break;
    		case 2:
    			Worldtmp= new grassDirt();
    			break;
    		case 3:
    			Worldtmp= new Water(10);
    			break;
    		case 4:
    			Worldtmp= new Wood(1);
    			break;
    		case 5:
    			Worldtmp= new Wood(2);
    			break;
    		case 6:
    			Worldtmp= new Wood(3);
    			break;
    		case 7:
    			Worldtmp= new Leaf();
    			break;
    		case 8:
    			Worldtmp= new BaseStone();
    			break;
    		case 9:
    			Worldtmp= new TopAir();
    			break;
    		case 10:
    			Worldtmp= new SideEndStone();
    			break;
    		case 11:
    			Worldtmp= new cloud();
    			break;
    		case 12:
    			Worldtmp= new rain();
    			break;
    		case 141:
    			Worldtmp= new bgMountainBlock(1,100);
    			break;
    		case 142:
    			Worldtmp= new bgMountainBlock(2,100);
    			break;
    		case 143:
    			Worldtmp= new bgMountainBlock(3,100);
    			break;
    		case 15:
    			Worldtmp= new redWood1();
    			break;
    		case 99:
    			Worldtmp= new redWood2();
    			break;
    		case 98:
    			Worldtmp= new redWood3();
    			break;
    		case 97:
    			Worldtmp= new redWood4();
    			break;
    		case 96:
    			Worldtmp= new BrownWood1();
    			break;
    		case 16:
    			Worldtmp= new Stone();
    			break;
    		case 17:
    			Worldtmp= new Sand();
    			break;
    		case 18:
    			Worldtmp= new Coal();
    			break;
    		case 19:
    			Worldtmp=new  IronOre();
    			break;
    		case 20:
    			Worldtmp= new Torch();
    			break;
    		case 21:
    			Worldtmp= new  BamboWood();
    			break;
    		case 23:
    			Worldtmp= new  BamboLeaf1();
    			break;
    		case 22:
    			Worldtmp= new  BamboLeaf2();
    			break;
    		}
        	return Worldtmp;
        }
        Entity switchEntity(int i) {
        	Block entitytmp=null;
        	Entity entity=null;
        	switch(i) {
        	case 1000:
    			entitytmp= new Air(0);
    			break;
    		case 1001:
    			entitytmp= new Dirt();
    			break;
    		case 1002:
    			entitytmp= new grassDirt();
    			break;
    		case 1003:
    			entitytmp= new Water(10);
    			break;
    		case 1004:
    			entitytmp= new Wood(1);
    			break;
    		case 1005:
    			entitytmp= new Wood(2);
    			break;
    		case 1006:
    			entitytmp= new Wood(3);
    			break;
    		case 1007:
    			entitytmp= new Leaf();
    			break;
    		case 1008:
    			entitytmp= new BaseStone();
    			break;
    		case 1009:
    			entitytmp= new TopAir();
    			break;
    		case 1010:
    			entitytmp= new SideEndStone();
    			break;
    		case 1011:
    			entitytmp= new cloud();
    			break;
    		case 1012:
    			entitytmp= new rain();
    			break;
    		case 1141:
    			entitytmp= new bgMountainBlock(1,100);
    			break;
    		case 1142:
    			entitytmp= new bgMountainBlock(2,100);
    			break;
    		case 1143:
    			entitytmp= new bgMountainBlock(3,100);
    			break;
    		case 1015:
    			entitytmp= new redWood1();
    			break;
    		case 1099:
    			entitytmp= new redWood2();
    			break;
    		case 1098:
    			entitytmp= new redWood3();
    			break;
    		case 1097:
    			entitytmp= new redWood4();
    			break;
    		case 1096:
    			entitytmp= new BrownWood1();
    			break;
    		case 1016:
    			entitytmp= new Stone();
    			break;
    		case 1017:
    			entitytmp= new Sand();
    			break;
    		case 1018:
    			entitytmp= new Coal();
    			break;
    		case 1019:
    			entitytmp=new  IronOre();
    			break;
    		case 1020:
    			entitytmp= new Torch();
    			break;
    		case 1021:
    			entitytmp= new  BamboWood();
    			break;
    		case 1023:
    			entitytmp= new  BamboLeaf1();
    			break;
    		case 1022:
    			entitytmp= new  BamboLeaf2();
    			break;
    		}
        	if(entitytmp!=null)
        	entity=new EntityBlock(entitytmp);
        	return entity;
        	}
        }
    }

