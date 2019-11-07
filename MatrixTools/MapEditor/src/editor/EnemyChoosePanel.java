package editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class EnemyChoosePanel extends JPanel {
	private JDialog owner;
	private ValueChooser enemyCombo;
	
	public EnemyChoosePanel(JDialog owner) {
		super();
		this.owner = owner;
		Sprite[] sprites = ((EnemyManager)(MainFrame.self.getPanels()[MainFrame.LAYER_ENEMY].getManager())).getSprites();
		int[] ids;
		String[] names;
		int length = 1;
		if(sprites != null) {
			length = sprites.length + 1;
		}
		ids = new int[length];
		names = new String[length];
		ids[0] = -1;
		names[0] = "无";
		if(sprites != null) {
			for(int i = 0; i < sprites.length; ++i) {
				ids[i + 1] = sprites[i].getID();
				names[i + 1] = sprites[i].getName();
			}
		}
		
		enemyCombo = new ValueChooser(-1, ids, names, ValueChooser.NATURE_INDEX);
		
		JButton bt = new JButton("设置");
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseEnemy();
			}
		});
		
		setLayout(new BorderLayout());
		add(new JLabel("敌人："), BorderLayout.WEST);
		add(enemyCombo, BorderLayout.CENTER);
		add(bt, BorderLayout.EAST);
	}
	
	private void chooseEnemy() {
		EnemyChooser chooser = new EnemyChooser(owner);
		chooser.setSelectedEnemy(enemyCombo.getValue());
		chooser.show();
		if(chooser.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			enemyCombo.setValue(chooser.getSelectedEnemyID());
		}
	}
	
	public void setSelectedEnemyID(int aID) {
		enemyCombo.setValue(aID);
	}
	
	public int getSelectedEnemyID() {
		return enemyCombo.getValue();
	}
}


class EnemyChooser extends OKCancelDialog {

	private MapPanel.ManagerPanel panel;
	private MapPanel.Manager manager;
	
	public EnemyChooser(JFrame owner) {
		super(owner);
		init();
	}
	
	public EnemyChooser(JDialog owner) {
		super(owner);
		init();
	}
	
	private void init() {
		setTitle("选择敌人");
		MapPanel mapPanel = new MapPanel(this) {
			public void paintManager(Graphics g) {
				selfPaintManager(g);
			}
		};
		panel = mapPanel.getPanel();
		manager = mapPanel.getManager();
		manager.setSelectionMode(SpriteManager.SINGLE_SELECTION);
		manager.addAllEnemys();
		
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(mapPanel, BorderLayout.CENTER);
		
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);
		
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				scrollToEnemy();
			}
		});
	}
	
	private void scrollToEnemy() {
		Enemy enemy = getSelectedEnemy();
		if(enemy != null) {
			panel.scrollRectToVisible(new Rectangle(enemy.getLeft() - 50, enemy.getTop() - 50, enemy.getWidth() + 100, enemy.getHeight() + 100));
		}
	}
	
	private void selfPaintManager(Graphics g) {
		manager.paintFloors(g);
		manager.paintSprites(g);
		manager.paintLines(g);
	}
	
	public void setSelectedEnemy(int enemyID) {
		Enemy[] enemys = manager.getEnemys();
		if(enemys != null) {
			for(int i = 0; i < enemys.length; ++i) {
				if(enemys[i].getID() == enemyID) {
					manager.getSelection().selectSprite(enemys[i]);
					break;
				}
			}
		}
	}
	
	private Enemy getSelectedEnemy() {
		Enemy result = null;
		Sprite[] sprites = manager.getSelection().getSprites();
		if(sprites != null) {
			if(sprites.length >= 1) {
				result = (Enemy)sprites[0];
			}
		}
		return result;
	}
	
	public int getSelectedEnemyID() {
		Enemy enemy = getSelectedEnemy();
		if(enemy == null) return -1;
		return enemy.getID();
	}
	
	protected void cancelPerformed() {
		closeType = CANCEL_PERFORMED;
		dispose();	    
    }

	protected void okPerformed() {
	    closeType = OK_PERFORMED;
	    dispose();
    }
}