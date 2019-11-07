package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MapAreaPanel extends JPanel {

	private JDialog owner;
	private NumberSpinner leftSpinner;
	private NumberSpinner topSpinner;
	private NumberSpinner widthSpinner;
	private NumberSpinner heightSpinner;
	private IntPair minSize;

	public MapAreaPanel(JDialog owner, IntPair minSize) {
		super();

		this.owner = owner;
		leftSpinner = new NumberSpinner();
		topSpinner = new NumberSpinner();
		widthSpinner = new NumberSpinner();
		heightSpinner = new NumberSpinner();
		this.minSize = minSize;

		JButton bt = new JButton("设置");
		bt.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setMapArea();
			}
		});

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 5, 3, 5);
		c.weighty = 1;
		
		c.gridx = 0;
		c.weightx = 0;
		add(new JLabel("左："), c);
		c.gridx++;
		c.weightx = 1;
		add(leftSpinner, c);
		
		c.gridx++;
		c.weightx = 0;
		add(new JLabel("上："), c);
		c.gridx++;
		c.weightx = 1;
		add(topSpinner, c);

		c.gridx++;
		c.weightx = 0;
		add(new JLabel("宽："), c);
		c.gridx++;
		c.weightx = 1;
		add(widthSpinner, c);

		c.gridx++;
		c.weightx = 0;
		add(new JLabel("高："), c);
		c.gridx++;
		c.weightx = 1;
		add(heightSpinner, c);
		
		c.gridx++;
		c.weightx = 0;
		add(bt, c);		
	}

	private void setMapArea() {
		MapAreaSetter setter = new MapAreaSetter(owner, minSize);
		setter.setArea(leftSpinner.getIntValue(), topSpinner.getIntValue(),
		        widthSpinner.getIntValue(), heightSpinner.getIntValue());
		setter.show();
		if(setter.getCloseType() == OKCancelDialog.OK_PERFORMED) {
			leftSpinner.setIntValue(setter.getAreaLeft());
			topSpinner.setIntValue(setter.getAreaTop());
			widthSpinner.setIntValue(setter.getAreaWidth());
			heightSpinner.setIntValue(setter.getAreaHeight());
		}
	}
	
	public void setArea(int left, int top, int width, int height) {
		leftSpinner.setIntValue(left);
		topSpinner.setIntValue(top);
		widthSpinner.setIntValue(width);
		heightSpinner.setIntValue(height);
	}
	
	public int getAreaLeft() {
		return leftSpinner.getIntValue();
	}
	
	public int getAreaTop() {
		return topSpinner.getIntValue();
	}
	
	public int getAreaWidth() {
		return widthSpinner.getIntValue();
	}
	
	public int getAreaHeight() {
		return heightSpinner.getIntValue();
	}
}

class MapAreaSetter extends OKCancelDialog {

	private MapPanel.ManagerPanel panel;
	private MapPanel.Manager manager;
	private InfoPanel infoPanel;
	private IntPair minSize;
	private RectShape rect;
	private int dragCorner;
	private int dragStartX;
	private int dragStartY;

	public MapAreaSetter(JDialog owner, IntPair minSize) {
		super(owner);
		init(minSize);
	}

	public MapAreaSetter(JFrame owner, IntPair minSize) {
		super(owner);
		init(minSize);
	}

	private void init(IntPair minSize) {
		setTitle("设置地图区域");
		MapPanel mapPanel = new MapPanel(this) {

			public void paintManager(Graphics g) {
				selfPaintManager(g);
			}
		};
		panel = mapPanel.getPanel();
		manager = mapPanel.getManager();
		infoPanel = mapPanel.getInfoPanel();

		this.minSize = minSize;
		if (this.minSize == null) {
			this.minSize = new IntPair(5, 5);
		}

		MapInfo info = MainFrame.self.getMapInfo();

		rect = new RectShape(info.getRealLeft(), info.getRealTop(), 50, 50) {

			public int getID() {
				return 0;
			}

			public String getName() {
				return "地图范围";
			}

			public void setID(int id) {}

			public int compareTo(Object arg0) {
				return 0;
			}

			public void paintIdle(Graphics g) {
				System.out.println("fuck rect");
				Graphics2D g2 = (Graphics2D) g;
				Composite oldComposite = g2.getComposite();
				g2.setComposite(ScrollablePanel.ALPHA_COMPOSITE);
				Color oldColor = g.getColor();
				Color color = Color.BLUE;
				g.setColor(color);
				g.fillRect(left, top, width, height);
				g.setColor(oldColor);
				g2.setComposite(oldComposite);
			}

			@Override
			public void Rpressed(boolean t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void roate(int x, int y) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public int getLayer() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void setLayer(int layer) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean isVisible() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setVisible(boolean isVisible) {
				// TODO Auto-generated method stub
				
			}
		};

		rect.setScale(panel.getScale(), panel.getScale());
		rect.setSelected(true);
		
		infoPanel.setSelectionInfo(rect.getInfo());

		panel.addListener(new ScrollablePanelAdapter() {

			public void scaleChanged() {
				rect.setScale(panel.getScale(), panel.getScale());
			}
		});

		panel.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				if (panel.isMouseMovingViewport()) return;
				if (e.getButton() != XUtil.LEFT_BUTTON) return;
				selfMousePressed(e);
			}

			public void mouseReleased(MouseEvent e) {
				if (panel.isMouseMovingViewport()) return;
				if (e.getButton() != XUtil.LEFT_BUTTON) return;
				selfMouseReleased(e);
			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseMoved(MouseEvent e) {
				if (panel.isMouseMovingViewport()) return;
				selfMouseMoved(e);
			}

			public void mouseDragged(MouseEvent e) {
				if (panel.isMouseMovingViewport()) return;
				System.out.println(e.getButton());
				//if (e.getButton() != XUtil.LEFT_BUTTON) return;
				selfMouseDragged(e);
			}
		});

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(mapPanel, BorderLayout.CENTER);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		cp.add(buttonPanel, BorderLayout.SOUTH);

		addComponentListener(new ComponentAdapter() {

			public void componentShown(ComponentEvent e) {
				scrollToRect();
			}
		});
	}

	private void scrollToRect() {
		panel.scrollRectToVisible(new Rectangle(rect.left - 25, rect.top - 25, rect.width + 50,
		        rect.height + 50));
	}

	private void selfPaintManager(Graphics g) {
		manager.paintFloors(g);
		manager.paintEnemys(g);
		manager.paintLines(g);
		//System.out.println("22222");
		rect.paint(g);
	}

	private void selfMousePressed(MouseEvent e) {
		dragCorner = rect.getCornerAtPoint(e.getX(), e.getY());
		if (dragCorner != Shape.CORNER_NONE) {
			dragStartX = manager.getMouseX();
			dragStartY = manager.getMouseY();
		}
	}

	private void selfMouseReleased(MouseEvent e) {
		if (dragCorner != RectShape.CORNER_NONE) {
			rect.confirmDrag();
			dragCorner = RectShape.CORNER_NONE;
			infoPanel.setSelectionInfo(rect.getInfo());
			panel.repaint();
		}
	}

	private void selfMouseMoved(MouseEvent e) {
		panel.setCursor(rect.getCornerCursor(rect.getCornerAtPoint(e.getX(), e.getY())));
		panel.repaint();
	}

	private void selfMouseDragged(MouseEvent e) {
		int x = manager.getMouseX();
		int y = manager.getMouseY();
		int offsetX = x - dragStartX;
		int offsetY = y - dragStartY;
		dragCorner = rect.drag(dragCorner, offsetX, offsetY);

		dragStartX = x;
		dragStartY = y;

		infoPanel.setSelectionInfo(rect.getInfo());
		panel.repaint();
	}

	public void setArea(int left, int top, int width, int height) {
		MapInfo info = MainFrame.self.getMapInfo();
		rect.left = left + info.getRealLeft();
		rect.top = top + info.getRealTop();
		rect.width = width;
		rect.height = height;
		infoPanel.setSelectionInfo(rect.getInfo());
		panel.repaint();
	}

	public int getAreaLeft() {
		MapInfo info = MainFrame.self.getMapInfo();
		return rect.left - info.getRealLeft();
	}

	public int getAreaTop() {
		MapInfo info = MainFrame.self.getMapInfo();
		return rect.top - info.getRealTop();
	}

	public int getAreaWidth() {
		return rect.width;
	}

	public int getAreaHeight() {
		return rect.height;
	}

	protected void cancelPerformed() {
		closeType = CANCEL_PERFORMED;
		dispose();
	}

	protected void okPerformed() {
		if (getAreaWidth() < minSize.x) {
			JOptionPane.showMessageDialog(this, "设置的区域宽度应该不小于" + minSize.x, "宽度过小",
			        JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (getAreaHeight() < minSize.y) {
			JOptionPane.showMessageDialog(this, "设置的区域高度应该不小于" + minSize.y, "高度过小",
			        JOptionPane.ERROR_MESSAGE);
			return;
		}
		closeType = OK_PERFORMED;
		dispose();
	}
}