package editor;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JDialog;
import javax.swing.JPanel;

abstract public class MapPanel extends JPanel {

	public class Manager extends SpriteManager {

		private Floor[] floors;
		private Enemy[] enemys;
		private LogicLine[] lines;

		public Manager(ManagerPanel panel, MouseInfo mouseInfo) {
			super(panel, mouseInfo);
			Sprite[] sprites = ((FloorManager) (MainFrame.self.getPanels()[MainFrame.LAYER_FLOOR].getManager())).getSprites();
			if(sprites == null) {
				floors = null;
			}
			else {
				floors = new Floor[sprites.length];
				for(int i = 0; i < sprites.length; ++i) {
					floors[i] = (Floor)(sprites[i]);
				}
			}
			
			sprites = ((EnemyManager) (MainFrame.self.getPanels()[MainFrame.LAYER_ENEMY].getManager())).getSprites();
			if(sprites == null) {
				enemys = null;
			}
			else {
				enemys = new Enemy[sprites.length];
				for(int i = 0; i < sprites.length; ++i) {
					enemys[i] = ((Enemy)(sprites[i])).copyEnemy();
				}
			}
			
			Shape[] shapes = ((ShapeManager)(MainFrame.self.getPanels()[MainFrame.LAYER_LINE].getManager())).getShapes();
			if(shapes == null) {
				lines = null;
			}
			else {
				lines = new LogicLine[shapes.length];
				for(int i = 0; i < shapes.length; ++i) {
					lines[i] = ((LogicLine)(shapes[i])).copyLogicLine();
				}
			}
			
			setSelectionMode(NONE_SELECTION);
			setNewspriteMode(NONE_NEWSPRITE);
			setMoveable(false);
			setDeleteable(false);
		}
		
		public void addAllEnemys() {
			if(enemys != null) {
				for(int i = 0; i < enemys.length; ++i) {
					addSprite(enemys[i]);
				}
			}
		}
		
		public Enemy[] getEnemys() {
			return enemys;
		}
		
		public void paintEnemys(Graphics g) {
			if(enemys != null) {
				for(int i = 0; i < enemys.length; ++i) {
					enemys[i].paintIdle(g);
				}
			}
		}
		
		public LogicLine[] getLines() {
			return lines;
		}
		
		public void paintLines(Graphics g) {
			if(lines != null) {
				for(int i = 0; i < lines.length; ++i) {
					lines[i].paintIdle(g);
				}
			}
		}
		
		public void paintFloors(Graphics g) {
			MainFrame.self.getMapInfo().paintBackground(g);
			if(floors != null) {
				for(int i = 0; i < floors.length; ++i) {
					floors[i].paintIdle(g);
				}
			}
		}

		public void load(DataInputStream in, Object[] resManagers) throws Exception {}

		public void save(DataOutputStream out, Object[] resManagers) throws Exception {}

		public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception {}

		@Override
		public void paintStatic(Graphics g) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void paintStatic(Graphics g, int id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void saveMobile(DataOutputStream out, int layer)
				throws Exception {
			// TODO Auto-generated method stub
			
		}
	};

	public class ManagerPanel extends SpriteManagerPanel {

		public ManagerPanel() {
			super(owner, new MouseInfo());
			MapInfo info = MainFrame.self.getMapInfo();
			setMapSize(info.getWidth(), info.getHeight());
		}

		protected SpriteManager createManager() {
			return new Manager(this, mouseInfo);
		}

		protected void paintSelf(Graphics g) {
			paintManager(g);
		}
	}

	private JDialog owner;
	private Manager manager;
	private ManagerPanel panel;
	private InfoPanel infoPanel;

	public MapPanel(JDialog owner) {
		super();
		this.owner = owner;
		panel = new ManagerPanel();
		manager = (Manager)panel.getManager();
		infoPanel = new InfoPanel();
		infoPanel.setScrollablePanel(panel);
		infoPanel.setManager(manager);
		setLayout(new BorderLayout());
		add(panel.getBackPanel(), BorderLayout.CENTER);
		add(infoPanel, BorderLayout.SOUTH);
	}
	
	public Manager getManager() {
		return manager;
	}
	
	public ManagerPanel getPanel() {
		return panel;
	}
	
	public InfoPanel getInfoPanel() {
		return infoPanel;
	}
	
	abstract public void paintManager(Graphics g);
}