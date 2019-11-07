package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MapInfo {

	private int width;
	private int height;
	private String name;
	private Color color;
	private boolean colorInit;
	private int realLeft;
	private int realWidth;
	private int realTop;
	private int realHeight;
	private int gridHeight;
	private int gridWidth;
	private int tileWidth;
	private int tileHeight;
	
	private int drawLeft;
	private int drawTop;
	private int drawWidth;
	private int drawHeight;
	boolean isInit;

	public MapInfo(int width, int height, String name) {
		this.width = width;
		this.height = height;
		this.name = name;
		this.color = Color.WHITE;
		isInit = false;
		colorInit = false;
		realLeft = 0;
		realWidth = width;
		realTop = 0;
		realHeight = height;
		gridHeight = 0;
		gridWidth = 0;
		tileWidth =0;
		tileHeight = 0;
		setDrawInfo(0, 0, 0, 0);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if(width > this.width)
			this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if(height > this.height)
			this.height = height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		colorInit = true;
	}

	public int getRealLeft() {
		return realLeft;
	}

	public void setRealLeft(int realLeft) {
		if(realLeft > this.realLeft)
			this.realLeft = realLeft;
	}

	public int getRealWidth() {
		return realWidth;
	}

	public void setRealWidth(int realWidth) {
		if(realWidth > this.realWidth)
			this.realWidth = realWidth;
	}
	
	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}
	
	public int getGridHeight() {
		return this.gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}
	
	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
		if(tileWidth > 0) {
			int w = XUtil.getDefPropInt("TileDataWidth")*tileWidth;
			setRealWidth(w);
			setWidth(w + 2*realLeft);
		}
	}
	
	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
		if(tileHeight > 0) {
			int h = XUtil.getDefPropInt("TileDataHeight")*tileHeight;
			setRealHeight(h);
			setHeight(h + 2*realTop);
		}
	}

	public int getDrawLeft() {
		return drawLeft;
	}

	public void setDrawLeft(int drawLeft) {
		this.drawLeft = drawLeft;
	}

	public int getDrawTop() {
		return drawTop;
	}

	public void setDrawTop(int drawTop) {
		this.drawTop = drawTop;
	}

	public int getDrawWidth() {
		return drawWidth;
	}

	public void setDrawWidth(int drawWidth) {
		this.drawWidth = drawWidth;
	}

	public int getDrawHeight() {
		return drawHeight;
	}

	public void setDrawHeight(int drawHeight) {
		this.drawHeight = drawHeight;
	}
	
	public void setDrawInfo(int drawLeft, int drawTop, int drawWidth, int drawHeight) {
		this.drawLeft = drawLeft;
		this.drawTop = drawTop;
		this.drawWidth = drawWidth;
		this.drawHeight = drawHeight;
		
		setRealWidth(drawWidth);
		setRealHeight(drawHeight);
		setWidth(drawLeft*2 + drawWidth);
		setHeight(drawTop*2 + drawHeight);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		SL.writeInt(realLeft, out);
		SL.writeInt(realWidth, out);
		SL.writeInt(realTop, out);
		SL.writeInt(realHeight, out);
		SL.writeByte(color.getRed(), out);
		SL.writeByte(color.getGreen(), out);
		SL.writeByte(color.getBlue(), out);
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(width);
		out.writeInt(height);
		out.writeInt(color.getRGB());
		out.writeInt(realLeft);
		out.writeInt(realWidth);
		out.writeInt(realTop);
		out.writeInt(realHeight);
		out.writeInt(gridWidth);
		out.writeInt(gridHeight);
		out.writeInt(tileWidth);
		out.writeInt(tileHeight);
	}

	public void load(DataInputStream in) throws Exception {
		width = in.readInt();
		height = in.readInt();
		color = new Color(in.readInt(), true);
		colorInit = true;
		realLeft = in.readInt();
		realWidth = in.readInt();
		realTop = in.readInt();
		realHeight = in.readInt();
		gridWidth = in.readInt();
		gridHeight = in.readInt();
		tileWidth = in.readInt();
		tileHeight = in.readInt();
		isInit = true;
		
		setDrawInfo(realLeft, realTop, realWidth, realHeight);
	}

	public int getRealTop() {
		return this.realTop;
	}

	public void setRealTop(int realTop) {
		this.realTop = realTop;
	}

	public int getRealHeight() {
		return realHeight;
	}

	public void setRealHeight(int realHeight) {
		this.realHeight = realHeight;
	}

	public void paintBorder(Graphics g) {
		if(drawWidth == 0 || drawHeight == 0)
			return;
		
		Color oldColor = g.getColor();
		g.setColor(Color.WHITE);
		g.drawRect(drawLeft - 1, drawTop - 1, drawWidth + 1, drawHeight + 1);
		g.setColor(oldColor);
	}
	
	public void paintBackground(Graphics g) {
		if(drawWidth == 0 || drawHeight == 0)
			return;
		
		if (colorInit && color != null) {
			Color oldColor = g.getColor();
			g.setColor(color);
			g.fillRect(drawLeft, drawTop, drawWidth, drawHeight);
			g.setColor(oldColor);
		}
	}
}

class MapPropDialog extends OKCancelDialog {

	private JTextField nameText;
	private NumberSpinner widthSpinner;
	private NumberSpinner heightSpinner;
	private JButton colorButton;
	private NumberSpinner realLeftSpinner;
	private NumberSpinner realWidthSpinner;
	private NumberSpinner realTopSpinner;
	private NumberSpinner realHeightSpinner;
	private NumberSpinner gridWidthSpinner;
	private NumberSpinner gridHeightSpinner;
	private NumberSpinner tileWidthSpinner;
	private NumberSpinner tileHeightSpinner;
	
	private MapInfo mapInfo;

	public MapPropDialog(JFrame owner) {
		super(owner);
		init();
	}

	private void init() {
		mapInfo = MainFrame.self.getMapInfo();
		nameText = new JTextField(XUtil.getDefPropStr("DefMapName"));
		widthSpinner = new NumberSpinner();
		widthSpinner.setIntValue(XUtil.getDefPropInt("DefMapWidth"));
		heightSpinner = new NumberSpinner();
		heightSpinner.setIntValue(XUtil.getDefPropInt("DefMapHeight"));
		colorButton = new JButton("设置颜色");
		colorButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				chooseColor();
			}
		});
		realLeftSpinner = new NumberSpinner();
		realLeftSpinner.setIntValue(XUtil.getDefPropInt("RealMapLeft"));
		realWidthSpinner = new NumberSpinner();
		realWidthSpinner.setIntValue(XUtil.getDefPropInt("RealMapWidth"));
		realTopSpinner = new NumberSpinner();
		realTopSpinner.setIntValue(XUtil.getDefPropInt("RealMapTop"));
		realHeightSpinner = new NumberSpinner();
		realHeightSpinner.setIntValue(XUtil.getDefPropInt("RealMapHeight"));
		gridWidthSpinner = new NumberSpinner();
		gridWidthSpinner.setIntValue(0);
		gridHeightSpinner = new NumberSpinner();
		gridHeightSpinner.setIntValue(0);
		tileWidthSpinner = new NumberSpinner();
		tileWidthSpinner.setIntValue(0);
		tileHeightSpinner = new NumberSpinner();
		tileHeightSpinner.setIntValue(0);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0;
		c.insets = new Insets(5, 5, 5, 5);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		centerPanel.add(new JLabel("名称："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(nameText, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		centerPanel.add(new JLabel("宽："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(widthSpinner, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		centerPanel.add(new JLabel("高："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(heightSpinner, c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		centerPanel.add(new JLabel("背景色："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(colorButton, c);

		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 0;
		centerPanel.add(new JLabel("左边空隙："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(realLeftSpinner, c);

		c.gridx = 0;
		c.gridy = 5;
		c.weightx = 0;
		centerPanel.add(new JLabel("实际宽度："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(realWidthSpinner, c);

		c.gridx = 0;
		c.gridy = 6;
		c.weightx = 0;
		centerPanel.add(new JLabel("上边空隙："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(realTopSpinner, c);

		c.gridx = 0;
		c.gridy = 7;
		c.weightx = 0;
		centerPanel.add(new JLabel("实际高度："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(realHeightSpinner, c);
		
		c.gridx = 0;
		c.gridy = 8;
		c.weightx = 0;
		centerPanel.add(new JLabel("网格行数："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(gridWidthSpinner, c);
		
		c.gridx = 0;
		c.gridy = 9;
		c.weightx = 0;
		centerPanel.add(new JLabel("网格列数："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(gridHeightSpinner, c);
		
		c.gridx = 0;
		c.gridy = 10;
		c.weightx = 0;
		centerPanel.add(new JLabel("图层宽："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(tileWidthSpinner, c);
		
		c.gridx = 0;
		c.gridy = 11;
		c.weightx = 0;
		centerPanel.add(new JLabel("图层高："), c);

		c.gridx = 1;
		c.weightx = 1;
		centerPanel.add(tileHeightSpinner, c);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(centerPanel, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void chooseColor() {
//		SwingUtilities.invokeLater(new Runnable() {
//			 public void run() {
//				 final JColorChooser colorChooser = new JColorChooser(colorButton.getBackground());
//				 
//					// for okay selection, change button background to selected color
//					ActionListener okActionListener = new ActionListener() {
//						public void actionPerformed(ActionEvent event) {
//							Color newColor = colorChooser.getColor();
//							
//						}
//					};
//
//					// for cancel selection, change button background to red
//					ActionListener cancelActionListener = new ActionListener() {
//						public void actionPerformed(ActionEvent event) {
//							
//						}
//					};
//
//					final JDialog dialog = JColorChooser.createDialog(MainFrame.self, "选择颜色", true, colorChooser, okActionListener, cancelActionListener);
//					dialog.setVisible(true);
//			 }
//		});
		
		Color color = JColorChooser.showDialog(this, "选择颜色", colorButton.getBackground());
		if (color != null) {
			colorButton.setBackground(color);
		}
	}

	public void setMapInfo(MapInfo mapInfo) {
		colorButton.setBackground(mapInfo.getColor());
		nameText.setText(mapInfo.getName());
		widthSpinner.setIntValue(mapInfo.getWidth());
		heightSpinner.setIntValue(mapInfo.getHeight());
		realLeftSpinner.setIntValue(mapInfo.getRealLeft());
		realWidthSpinner.setIntValue(mapInfo.getRealWidth());
		realTopSpinner.setIntValue(mapInfo.getRealTop());
		realHeightSpinner.setIntValue(mapInfo.getRealHeight());
		gridWidthSpinner.setIntValue(mapInfo.getGridWidth());
		gridHeightSpinner.setIntValue(mapInfo.getGridHeight());
		tileWidthSpinner.setIntValue(mapInfo.getTileWidth());
		tileHeightSpinner.setIntValue(mapInfo.getTileHeight());
	}

	public void updateMapInfo(MapInfo mapInfo) {
		if(mapInfo == null) return;
		mapInfo.setName(nameText.getText());
		mapInfo.setColor(colorButton.getBackground());
		mapInfo.setWidth(widthSpinner.getIntValue());
		mapInfo.setHeight(heightSpinner.getIntValue());
		mapInfo.setRealLeft(realLeftSpinner.getIntValue());
		mapInfo.setRealWidth(realWidthSpinner.getIntValue());
		mapInfo.setRealTop(realTopSpinner.getIntValue());
		mapInfo.setRealHeight(realHeightSpinner.getIntValue());
		mapInfo.setGridWidth(gridWidthSpinner.getIntValue());
		mapInfo.setGridHeight(gridHeightSpinner.getIntValue());
		mapInfo.setTileWidth(tileWidthSpinner.getIntValue());
		mapInfo.setTileHeight(tileHeightSpinner.getIntValue());
	}

	public void okPerformed() {
		mapInfo.isInit = true;
		closeType = OK_PERFORMED;
		dispose();
	}

	public void cancelPerformed() {
		dispose();
	}
}