package editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

class InfoPanel extends JPanel {

	private JLabel mapLabel;
	private JLabel mouseLabel;
	private JLabel shapeLabel;
	private ScrollablePanel scrollablePanel;
	private Manager manager;
	private ScrollablePanelListener scrollablePanelListener;
	private ManagerListener managerListener;

	public InfoPanel() {
		init(true, true, true);
	}

	public InfoPanel(boolean showMapInfo, boolean showMouseInfo, boolean showSelectionInfo) {
		init(showMapInfo, showMouseInfo, showSelectionInfo);
	}

	private void init(boolean showMapInfo, boolean showMouseInfo, boolean showSelectionInfo) {
		setPreferredSize(new Dimension(100, XUtil.getDefPropInt("InfoHeight"))); // 100是随意值
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		mapLabel = new JLabel();
		mapLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mapLabel.setBorder(raisedetched);

		mouseLabel = new JLabel();
		mouseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mouseLabel.setBorder(raisedetched);

		shapeLabel = new JLabel();
		shapeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		shapeLabel.setBorder(raisedetched);

		scrollablePanel = null;
		manager = null;

		scrollablePanelListener = new ScrollablePanelListener() {

			public void scaleChanged() {
				refreshMapInfo();
			}

			public void sizeChanged() {
				refreshMapInfo();
			}
		};

		managerListener = new ManagerAdapter() {

			public void mouseChanged(MouseEvent e) {
				refreshMouseInfo(e);
			}

			public void selectionChanged() {
				refreshSelectionInfo();
			}
		};

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;

		mapLabel.setPreferredSize(new Dimension(XUtil.getDefPropInt("MapInfoWidth"),
		        XUtil.getDefPropInt("InfoHeight")));
		mouseLabel.setPreferredSize(new Dimension(XUtil.getDefPropInt("MouseInfoWidth"),
		        XUtil.getDefPropInt("InfoHeight")));

		if (showMapInfo) {
			this.add(mapLabel, c);
			c.gridx++;
		}
		if (showMouseInfo) {
			this.add(mouseLabel, c);
			c.gridx++;
		}
		c.weightx = 1;
		if (showSelectionInfo) {
			this.add(shapeLabel, c);
		}
		else {
			this.add(new JLabel(), c);
		}
	}

	public void refresh() {
		refreshMapInfo();
		refreshSelectionInfo();
	}

	private void refreshMapInfo() {
		if (scrollablePanel == null) {
			setMapInfo(null);
		}
		else {
			setMapInfo("宽：" + scrollablePanel.getBasicWidth() + "  高："
			        + scrollablePanel.getBasicHeight() + "  比例："
			        + (int) (scrollablePanel.getScale() * 100) + "%");
		}
	}

	private void refreshMouseInfo(MouseEvent e) {
		if (manager == null) {
			setMouseInfo(null);
		}
		else {
			boolean setted = false;
			if (scrollablePanel != null) {
				if (!scrollablePanel.isMouseIn()) {
					setMouseInfo(null);
					setted = true;
				}
			}
			if (!setted) {
			//	setMouseInfo("X：" + manager.getMouseX() + "  Y：" + manager.getMouseY());
				MapInfo mapinfo = MainFrame.self.getMapInfo();
				int left = mapinfo.getRealLeft();
				int top = mapinfo.getRealTop();
				
				setMouseInfo("X：" + (e.getX()-left) + "  Y：" + (e.getY()-top));
			}
		}
	}

	private void refreshSelectionInfo() {
		if (manager == null) {
			setSelectionInfo(null);
		}
		else {
			setSelectionInfo(manager.getSelectionInfo());
		}
	}

	public void setManager(Manager m) {
		if (this.manager != null) {
			this.manager.removeListener(this.managerListener);
		}
		this.manager = null;
		if (m != null) {
			this.manager = m;
			this.manager.addListener(this.managerListener);
		}
		refresh();
	}

	public void setMapInfo(String info) {
		mapLabel.setText(info);
		mapLabel.setToolTipText(info);
	}

	public void setMouseInfo(String info) {
		mouseLabel.setText(info);
		mouseLabel.setToolTipText(info);
	}

	public void setScrollablePanel(ScrollablePanel p) {
		if (this.scrollablePanel != null) {
			this.scrollablePanel.removeListener(this.scrollablePanelListener);
		}
		this.scrollablePanel = null;
		if (p != null) {
			this.scrollablePanel = p;
			this.scrollablePanel.addListener(this.scrollablePanelListener);
		}
		refresh();
	}

	public void setSelectionInfo(String info) {
		shapeLabel.setText(info);
		shapeLabel.setToolTipText(info);
	}
}