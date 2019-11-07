package editor;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;

class Floor extends SISprite {

	public final static Floor[] copyArray(Floor[] array) {
		Floor[] result = null;
		if (array != null) {
			result = new Floor[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyFloor();
			}
		}
		return result;
	}

	public final static Floor[] createArrayViaFile(DataInputStream in, SIManager siManager)
	        throws Exception {
		Floor[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new Floor[length];
			for (int i = 0; i < length; ++i) {
				result[i] = Floor.createViaFile(in, siManager);
			}
		}
		return result;
	}

	public final static Floor createViaFile(DataInputStream in, SIManager siManager)
	        throws Exception {
		Floor result = new Floor();
		result.load(in, siManager);
		return result;
	}

	private Floor() {
		super();
	}

	public Floor(SingleImage si, int id, int left, int top, boolean flip, int layer) {
		super(si, id, left, top, flip, layer);
		init();
	}

	public Floor(SingleImage si, int id, int left, int top, boolean flip, String name, int layer) {
		super(si, id, left, top, flip, name, layer);
		init();
	}

	// public Color getSelectedBorderColor() {
	// return new Color(0xFF00FF);
	// }

	// public Color getMovingBorderColor() {
	// return new Color(0xFF00FF);
	// }

	public int compareTo(Object o) {
		return super.compareTo(o);
	}

	public Copyable copy() {
		return copyFloor();
	}

	public void copyFrom(Floor source) {
		super.copyFrom(source);
	}

	public final Floor copyFloor() {
		Floor result = new Floor(this.getSI(), this.getID(), this.getX(), this.getY(),
		        this.isFlip(), this.getName(), this.getLayer2());
		result.copyFrom(this);
		return result;
	}

	public String getSelectMenuName() {
		SingleImage si = getSI();
		return si.getGroup().getName() + "\\" + si.getName();
	}

	private void init() {}

	protected void load(DataInputStream in, SIManager siManager) throws Exception {
		super.load(in, siManager);
	}

	public void save(DataOutputStream out) throws Exception {
		super.save(out);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		SL.writeShort(getSI().getID(), out);
		SL.writeShort(getX(), out);
		SL.writeShort(getY(), out);
		SL.writeShort(isFlip() ? 1 : 0, out);
		SL.writeDouble(this.getRoate(),out);
		SL.writeDouble(this.getScale(),out);
	}
}

class FloorManager extends SpriteManager {

	public FloorManager(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) {
		super(scrollablePanel, mouseInfo);
	}

	protected Sprite createSprite(int x, int y) {
		if (!(mouseInfo.getPainter() instanceof SIPainter)) { return null; }
		if(mouseInfo.getLayer() < 0) {return null;}
		if(!MainFrame.self.getLayerManager().getLayerIsShow(mouseInfo.getLayer())) {return null;}
		SIPainter painter = (SIPainter) mouseInfo.getPainter();
		return new Floor(painter.getSI(), useMaxID(), painter.getSI().getLeft(x),
		        painter.getSI().getTop(y), mouseInfo.isFlip(), mouseInfo.getLayer());
	}

//	public void paintStatic(Graphics g) {
//		MainFrame.self.getMapInfo().paintBackground(g);
//		
//		LayerManager lm = MainFrame.self.getLayerManager();
//		for(int i=lm.layers.size()-1; i>=0; --i) {
//		//	super.paintStatic(g, lm.getLayer(i).id);
//		}
//	}
//
//	public void paintSprites(Graphics g) {
//		MainFrame.self.getMapInfo().paintBackground(g);
//		super.paintSprites(g);
//	}
	
	@Override
	public void saveMobile(DataOutputStream out, int layer) throws Exception {
		// TODO Auto-generated method stub
		ArrayList ss = new ArrayList();
		MapInfo mapInfo = MainFrame.self.getMapInfo();
		Sprite[] _ss = getSprites(layer);
		int min = getMinLayer(_ss);
		int max = getMaxLayer(_ss)+1;
		for(int l=min; l<max; ++l) {
			for (int i = 0; i < _ss.length; ++i) {
				Floor floor = (Floor) (_ss[i]);
				if(floor.getLayer() == l) {
					if (floor.getLeft() + floor.getWidth() < mapInfo.getRealLeft()
					        || floor.getLeft() > mapInfo.getRealLeft() + mapInfo.getRealWidth()
					        || floor.getTop() + floor.getHeight() < mapInfo.getRealTop()
					        || floor.getTop() > mapInfo.getRealTop() + mapInfo.getRealHeight()) {
						
						//donothing
					}
					else {
						ss.add(floor);
					}
				}
			}
		}

		ArrayList ids = new ArrayList();
		int maxid = 0;
		if (ss.size() > 0) {
			for (int i = 0; i < ss.size(); ++i) {
				SingleImage si = ((SISprite) (ss.get(i))).getSI();
				boolean found = false;
				for (int j = 0; j < ids.size(); ++j) {
					if (si.getID() == ((Integer) (ids.get(j))).intValue()) {
						found = true;
						break;
					}
				}
				if (!found) {
					if(maxid < si.getID()) maxid = si.getID();
					ids.add(new Integer(si.getID()));
				}
			}
		}

		SL.writeShort(ids.size(), out);
		SL.writeShort(maxid, out);
		for (int i = 0; i < ids.size(); ++i) {
			SL.writeShort(((Integer) (ids.get(i))).intValue(), out);
		}

		SL.writeShort(ss.size(), out);
		for (int i = 0; i < ss.size(); ++i) {
			Floor floor = (Floor) (ss.get(i));
			floor.saveMobile(out);
		}
	}

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception {
		ArrayList ss = new ArrayList();
		MapInfo mapInfo = MainFrame.self.getMapInfo();
		for (int i = 0; i < sprites.size(); ++i) {
			Floor floor = (Floor) (sprites.get(i));
			if (floor.getLeft() + floor.getWidth() < mapInfo.getRealLeft()
			        || floor.getLeft() > mapInfo.getRealLeft() + mapInfo.getRealWidth()
			        || floor.getTop() + floor.getHeight() < mapInfo.getRealTop()
			        || floor.getTop() > mapInfo.getRealTop() + mapInfo.getRealHeight()) {
				
				//donothing
			}
			else {
				ss.add(floor);
			}
		}

		ArrayList ids = new ArrayList();
		int maxid = 0;
		if (ss.size() > 0) {
			for (int i = 0; i < ss.size(); ++i) {
				SingleImage si = ((SISprite) (ss.get(i))).getSI();
				boolean found = false;
				for (int j = 0; j < ids.size(); ++j) {
					if (si.getID() == ((Integer) (ids.get(j))).intValue()) {
						found = true;
						break;
					}
				}
				if (!found) {
					if(maxid < si.getID()) maxid = si.getID();
					ids.add(new Integer(si.getID()));
				}
			}
		}

		SL.writeShort(ids.size(), out);
		SL.writeShort(maxid, out);
		for (int i = 0; i < ids.size(); ++i) {
			SL.writeShort(((Integer) (ids.get(i))).intValue(), out);
		}

		SL.writeShort(ss.size(), out);
		for (int i = 0; i < ss.size(); ++i) {
			Floor floor = (Floor) (ss.get(i));
			floor.saveMobile(out);
		}

		// MapInfo mapInfo = MainFrame.self.getMapInfo();
		// int mapBlockCount = (mapInfo.getRealWidth() +
		// MainFrame.MAP_BLOCK_WIDTH - 1)
		// / MainFrame.MAP_BLOCK_WIDTH;
		//
		// SL.writeInt(MainFrame.MAP_BLOCK_WIDTH, out);
		// SL.writeInt(mapBlockCount, out);
		// for (int i = 0; i < mapBlockCount; ++i) {
		// int bLeft = mapInfo.getRealLeft() + i * MainFrame.MAP_BLOCK_WIDTH;
		// int bRight = bLeft + MainFrame.MAP_BLOCK_WIDTH;
		// ids.clear();
		// for (int j = 0; j < ss.size(); ++j) {
		// Floor floor = (Floor) (ss.get(j));
		// if (floor.getLeft() + floor.getWidth() >= bLeft && floor.getLeft() <
		// bRight) {
		// ids.add(new Integer(j));
		// }
		// }
		// System.out.println(ids.size());
		// SL.saveArrayMobile(ids, out);
		// }
	}

	public void save(DataOutputStream out, Object[] resManagers) throws Exception {
		out.writeInt(sprites.size());
		for (int i = 0; i < sprites.size(); ++i) {
			Floor floor = (Floor) (sprites.get(i));
			floor.save(out);
		}
	}

	public void load(DataInputStream in, Object[] resManagers) throws Exception {
		reset();
		SIManager siManager = (SIManager) (resManagers[MainFrame.RES_FLOOR]);
		int length = in.readInt();
		for (int i = 0; i < length; ++i) {
			Floor floor = Floor.createViaFile(in, siManager);
			addSprite(floor);
		}
	}
}

class FloorPanel extends SpriteManagerPanel {

	public FloorPanel(JFrame owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	public FloorPanel(JDialog owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	protected SpriteManager createManager() {
		return new FloorManager(this, mouseInfo);
	}
}