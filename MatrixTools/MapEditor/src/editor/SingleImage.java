package editor;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class SIGroup {

	private int id;
	private String name;
	private int layer;

	public SIGroup(int id, String name, int layer) {
		this.id = id;
		this.name = name;
		this.layer = layer;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getLayer() {
		return layer;
	}
}

class SIManager {
	
	public final static int MAX_IMAGE_ID = 60000;
	
	public static void main(String[] args) throws Exception {
		StringBuffer s = new StringBuffer();

		String siFolder = XUtil.getDefPropStr("MapImageFolder");
		String pakFolder = XUtil.getDefPropStr("PakFolder");

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(
		        XUtil.getDefPropStr("MapImageIniFile"))));
		ArrayList data = new ArrayList();
		String sLine;

		sLine = in.readLine();
		while (sLine != null) {
			data.add(sLine);
			sLine = in.readLine();
		}
		in.close();
		BufferedReader in2 = new BufferedReader(new InputStreamReader(new FileInputStream(
		        XUtil.getDefPropStr("TileImageIniFile"))));
		
		sLine = in2.readLine();
		while (sLine != null) {
			data.add(sLine);
			sLine = in2.readLine();
		}
		in2.close();

		int groupID = -1;
		String groupName = "";

		int maxId = 0;

		// 生成图片列表
		for (int i = 0; i < data.size(); ++i) {
			sLine = ((String) (data.get(i))).trim();
			if (sLine == null) continue;
			if (sLine.length() < 2) continue;
			if (sLine.startsWith("$") && sLine.endsWith(";") && sLine.length() > 2) { // group
				String infos[] = sLine.substring(1, sLine.length() - 1).split(",", 0);
				if (infos != null) {
					if (infos.length >= 1) {
						groupName = infos[0].trim();
						++groupID;
						if (groupID > 0) {
							s.append("\r\n");
						}
					}
				}
			}
			if (sLine.startsWith("@") && sLine.endsWith(";") && sLine.length() > 2) { // image
				String infos[] = sLine.substring(1, sLine.length() - 1).split(",", 0);
				if(infos.length >= 5){
					int isPak = Integer.parseInt(infos[4].trim());
					if(isPak == 1)
						continue;
				}
				if (infos.length >= 2) {
					int siID = Integer.parseInt(infos[0].trim());
					String siName = infos[1].trim();
					Color maskColor = SingleImage.DEF_MASK_COLOR;
					if (infos.length >= 3) {
						String sColor = infos[2].trim();
						if (sColor.length() > 2) {
							if (sColor.substring(0, 2).equalsIgnoreCase("0x")) {
								sColor = sColor.substring(2);
							}
						}
						maskColor = new Color(Integer.parseInt(sColor, 16));
					}
					String fileFullName;
					if (groupName.equals("")) {
						fileFullName = siFolder + "\\" + siName;
					}
					else {
						fileFullName = siFolder + "\\" + groupName + "\\" + siName;
					}
					File imageFile = new File(fileFullName);
					XImage image = new XImage(imageFile, maskColor);
					image.saveXMG(new File(pakFolder + "\\" + siID + ".png"));
				//	String listName = siID + ".xmg";
					String listName = siID + ".png";

					if (siID > maxId) maxId = siID;
					s.append(listName + "\r\n");
					System.out.println(groupName + "\\" + siName);
				}
			}
		}
		s.append("\r\n\r\n");

		// 在最前面加上存档文件的文件名
		// s.insert(0, "anim.dat\r\n" + "\r\n\r\n");

		if (maxId > MAX_IMAGE_ID) {
			JOptionPane.showMessageDialog(null, "图片ID大于最大值" + MAX_IMAGE_ID + "。", "ID过大",
			        JOptionPane.ERROR_MESSAGE);
			return;
		}

		// 生成文件list.txt
		DataOutputStream out;

		String output = "list.txt";
		if (args != null) {
			if (args.length >= 1) {
				if (args[0] != null) {
					if (!args[0].trim().equals("")) {
						output = args[0] + ".txt";
					}
				}
			}
		}
		output = pakFolder + "\\" + output;

		out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(output)));
		byte[] bytes = s.toString().getBytes();
		out.write(bytes);
		out.flush();
		out.close();
	}

	private String name;
	private SIGroup[] groups;
	private SingleImage[] sis;

	public SIManager(String name) throws Exception {
		this.name = name;
		readIniFile();
		for (int i = 0; i < sis.length; ++i) {
			try {
				sis[i].load();
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "加载图片 " + sis[i].toString() + " 失败/n" + e,
				        "加载错误", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public SIGroup[] getGroups() {
		return groups;
	}

	public SingleImage getSI(int siID) {
		for (int i = 0; i < sis.length; ++i) {
			if (sis[i].getID() == siID) { return sis[i]; }
		}
		return null;
	}

	public SingleImage[] getSIs() {
		return sis;
	}

	public SingleImage[] getSIs(int groupID) {
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < sis.length; ++i) {
			if (sis[i] != null) {
				if (sis[i].getGroup().getID() == groupID) {
					tmp.add(sis[i]);
				}
			}
		}

		SingleImage[] result = new SingleImage[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			result[i] = (SingleImage) (tmp.get(i));
		}
		return result;
	}

	private void readIniFile() throws Exception {
		String siFolder = XUtil.getDefPropStr(name + "ImageFolder");
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(
		        XUtil.getDefPropStr(name + "ImageIniFile"))));
		ArrayList data = new ArrayList();
		String sLine;

		sLine = in.readLine();
		while (sLine != null) {
			data.add(sLine);
			sLine = in.readLine();
		}
		in.close();

		int groupID = -1;
		int groupLayer = SingleImage.DEF_LAYER;
		String groupName = "";
		SIGroup group = new SIGroup(groupID, groupName, groupLayer);
		ArrayList tmpGroups = new ArrayList();
		ArrayList tmpSIs = new ArrayList();

		for (int i = 0; i < data.size(); ++i) {
			sLine = ((String) (data.get(i))).trim();
			if (sLine == null) continue;
			if (sLine.length() < 2) continue;
			if (sLine.startsWith("$") && sLine.endsWith(";") && sLine.length() > 2) { // group
				String infos[] = sLine.substring(1, sLine.length() - 1).split(",", 0);
				if (infos != null) {
					if (infos.length >= 1) {
						groupName = infos[0].trim();
						++groupID;
						if (infos.length >= 2) {
							groupLayer = Integer.parseInt(infos[1].trim());
						}
						else {
							groupLayer = SingleImage.DEF_LAYER;
						}
						group = new SIGroup(groupID, groupName, groupLayer);
						tmpGroups.add(group);
					}
				}
			}
			if (sLine.startsWith("@") && sLine.endsWith(";") && sLine.length() > 2) { // image
				String infos[] = sLine.substring(1, sLine.length() - 1).split(",", 0);
				if (infos.length >= 2) {
					int siID = Integer.parseInt(infos[0].trim());
					String siName = infos[1].trim();
					String fileFullName;
					if (groupName.equals("")) {
						fileFullName = siFolder + "\\" + siName;
					}
					else {
						fileFullName = siFolder + "\\" + groupName + "\\" + siName;
					}
					Color maskColor = SingleImage.DEF_MASK_COLOR;
					if (infos.length >= 3) {
						String sColor = infos[2].trim();
						if (sColor.length() > 2) {
							if (sColor.substring(0, 2).equalsIgnoreCase("0x")) {
								sColor = sColor.substring(2);
							}
						}
						maskColor = new Color(Integer.parseInt(sColor, 16));
					}
					int defLayer = groupLayer;
					if (infos.length >= 4) {
						defLayer = Integer.parseInt(infos[3].trim());
					}
					boolean isSave = true;
					if(infos.length >= 5) {
						int v = Integer.parseInt(infos[4].trim());
						if(v == 1)
							isSave = false;
					}
					tmpSIs.add(new SingleImage(group, siID, fileFullName, maskColor, defLayer, isSave));
				}
			}
		}

		groups = new SIGroup[tmpGroups.size()];
		for (int i = 0; i < tmpGroups.size(); ++i) {
			groups[i] = (SIGroup) (tmpGroups.get(i));
		}

		sis = new SingleImage[tmpSIs.size()];
		for (int i = 0; i < tmpSIs.size(); ++i) {
			sis[i] = (SingleImage) (tmpSIs.get(i));
		}
	}
}

public class SingleImage {

	public final static int DEF_LAYER = 0;
	public final static Color DEF_MASK_COLOR = new Color(0xFF00FF);

	BufferedImage image;
	private BufferedImage flippedImage;
	private SIGroup group;
	private int id;
	private File f;
	private Color maskColor;
	private String name;
	private int defLayer;
	private float scale;
	boolean isSave;

	public SingleImage(SIGroup group, int id, String fileFullName, Color maskColor, int defLayer, boolean isSave) {
		this.group = group;
		this.id = id;
		this.f = new File(fileFullName);
		this.maskColor = maskColor;
		this.defLayer = defLayer;
		this.name = FileExtFilter.getName(f);
		this.scale = 1;
		this.isSave = isSave;
	}
	
	public SingleImage(BufferedImage image, SIGroup group, int id, String name) {
		this.image = image;
		this.group = group;
		this.id = id;
		this.maskColor = DEF_MASK_COLOR;
		this.defLayer = DEF_LAYER;
		this.name = name;
		this.scale = 1;
		this.isSave = true;
		this.f = null;
		
		AffineTransform at = new AffineTransform(-1, 0, 0, 1, image.getWidth(), 0);
		AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		flippedImage = ato.filter(image, null);
	}
	
	public void SetScale(float scale){
		this.scale = scale;
	}
	
	public float GetScale(){
		return scale;
	}

	public int getDefLayer() {
		return defLayer;
	}

	public SIGroup getGroup() {
		return group;
	}

	public int getHeight() {
		return image.getHeight();
	}

	public int getID() {
		return id;
	}

	public int getLeft(int originX) {
		return originX - image.getWidth() / 2;
	}

	public Color getMaskColor() {
		return this.maskColor;
	}

	public String getName() {
		return name;
	}

	public int getTop(int originY) {
		return originY - image.getHeight() / 2;
	}

	public int getWidth() {
		return image.getWidth();
	}

	public void load() throws Exception {
		image = null;

		XImage ximage = new XImage(f, maskColor);
		if (ximage.isChanged()) {
			System.out.println("切割" + f);
			ximage.save();
		}
		image = ximage.getImage();

		if (image == null) { throw new Exception("无法读取图片：" + f.getAbsolutePath()); }

		AffineTransform at = new AffineTransform(-1, 0, 0, 1, image.getWidth(), 0);
		AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		flippedImage = ato.filter(image, null);
	}
	
	public void paintRoate(Graphics g, int left, int top, boolean flip ,double roate ,double scale)
	{
		BufferedImage img = image;
		if (flip) {
			img = flippedImage;
		}
		
		int w = img.getWidth();
		int h = img.getHeight();
		if(scale > 0)
		{
			Image tepImg =img.getScaledInstance((int)(image.getWidth()*scale), (int)(image.getHeight()*scale), Image.SCALE_DEFAULT);
			img =new BufferedImage(tepImg.getWidth(null), tepImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g2 = img.createGraphics(); 
			g2.drawImage(tepImg, 0, 0, null);
			g2.dispose(); 
		}
		
		Graphics2D g2 =(Graphics2D)g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1));
		AffineTransform oldTransform = g2.getTransform();
		
		int w1 = (img.getWidth()-w)/2;
		int h1 = (img.getHeight()-h)/2;
		
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); // 这里使用双线性过滤插件算法 VALUE_INTERPOLATION_BICUBIC   VALUE_INTERPOLATION_BILINEAR
		g2.rotate(Math.toRadians(roate),left-w1+img.getWidth()/2, top-h1+img.getHeight()/2);
		
		
		
		g2.drawImage(img, left-w1, top-h1, null);
		g2.setTransform(oldTransform);
		g2.setComposite(oldComposite);
	}
	//最终绘制地图
	public void paintLeftTop(Graphics g, int left, int top, boolean flip) {
		BufferedImage img = image;
		if (flip) {
			img = flippedImage;
		}
		g.drawImage(img, left, top, (int)(getWidth()*scale), (int)(getHeight()*scale), null);
	}

	public void paintOrigin(Graphics g, int originX, int originY, boolean flip) {
		paintLeftTop(g, getLeft(originX), getTop(originY), flip);
	}

	public String toString() {
		return name;
	}
}

class SIPainter extends PainterPanel {

	public final static SIPainter[] getPainters(SingleImage[] sis) {
		SIPainter[] result = null;
		if (sis != null) {
			result = new SIPainter[sis.length];
			for (int i = 0; i < sis.length; ++i) {
				result[i] = new SIPainter(sis[i]);
				result[i].computeSize();
			}
		}
		return result;
	}

	SingleImage si;

	public SIPainter(SingleImage si) {
		super();
		this.si = si;
	}

	public int getGroupID() {
		return si.getGroup().getID();
	}

	public int getID() {
		return si.getID();
	}

	public int getImageHeight() {
		return si.getHeight();
	}

	public int getImageWidth() {
		return si.getWidth();
	}

	public String getName() {
		return si.getName();
	}

	public SingleImage getSI() {
		return si;
	}

	public boolean isFlip() {
		return false;
	}

	public void paintLeftTop(Graphics g, int left, int top) {
		float scale = si.GetScale();
		si.SetScale(getSacle());
		si.paintLeftTop(g, left, top, isFlip());
		si.SetScale(scale);
	}

	public void paintOrigin(Graphics g, int originX, int originY) {
		si.paintOrigin(g, originX, originY, isFlip());
	}

	public void paintOrigin(Graphics g, int originX, int originY, boolean flip) {
		si.paintOrigin(g, originX, originY, flip);
	}
}

class SIPainterGroup implements PainterGroup {

	public final static SIPainterGroup[] getGroups(SIGroup[] groups) {
		SIPainterGroup[] result = null;
		if (groups != null) {
			result = new SIPainterGroup[groups.length];
			for (int i = 0; i < groups.length; ++i) {
				result[i] = new SIPainterGroup(groups[i]);
			}
		}
		return result;
	}

	private SIGroup siGroup;
	private boolean selected;

	public SIPainterGroup(SIGroup siGroup) {
		this.siGroup = siGroup;
		this.selected = false;
	}

	public int getID() {
		return siGroup.getID();
	}

	public String getName() {
		return siGroup.getName();
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}

class SIPanel extends JPanel {

	private SingleImage si;

	public SIPanel() {
		super();
		setSI(null);
	}

	public void setSI(SingleImage si) {
		this.si = si;
		if (si != null) {
			setPreferredSize(new Dimension(si.getWidth(), si.getHeight()));
		}
		else {
			setPreferredSize(new Dimension(25, 25));
		}
		repaint();
		Container parent = this.getParent();
		if(parent != null) {
			parent.validate();
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (si != null) {
			paintSI(g, getWidth(), getHeight());
		}
	}

	protected void paintSI(Graphics g, int width, int height) {
		si.paintOrigin(g, width / 2, height / 2, false);
	}
}

class SISelecter extends OKCancelDialog {

	private DefaultListModel siModel;
	private JList siList;
	private SIPanel siPanel;
	private SIManager siManager;

	public SISelecter(JDialog owner, int siID, SIManager siManager) {
		super(owner);
		this.siManager = siManager;
		setTitle("选择图片");
		Container cp = this.getContentPane();

		siModel = new DefaultListModel();
		SingleImage[] sis = siManager.getSIs();
		for (int i = 0; i < sis.length; ++i) {
			siModel.addElement(sis[i]);
		}
		siList = new JList(siModel);
		siList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		siList.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				siChanged();
			}
		});
		siList.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				siListMouseClicked(e);
			}
		});
		JScrollPane siScroll = new JScrollPane(siList);
		SwingUtil.setDefScrollIncrement(siScroll);

		siPanel = new SIPanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, siScroll, new JScrollPane(siPanel));
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(200);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		cp.add(splitPane, BorderLayout.CENTER);
		cp.add(buttonPanel, BorderLayout.SOUTH);

		setSelectedSI(siID);
	}

	public void cancelPerformed() {
		dispose();
	}

	public SingleImage getSelectedSI() {
		Object obj = siList.getSelectedValue();
		if (obj != null) {
			if (obj instanceof SingleImage) { return (SingleImage) obj; }
		}
		return null;
	}

	public int getSelectedSIID() {
		Object obj = siList.getSelectedValue();
		if (obj != null) {
			if (obj instanceof SingleImage) { return ((SingleImage) obj).getID(); }
		}
		return -1;
	}

	public void okPerformed() {
		closeType = OK_PERFORMED;
		dispose();
	}

	private void setSelectedSI(int siID) {
		siList.clearSelection();
		SingleImage si = siManager.getSI(siID);
		if (si != null) {
			siList.setSelectedValue(si, true);
		}
	}

	private void siChanged() {
		SingleImage si = null;
		Object obj = siList.getSelectedValue();
		if (obj != null) {
			if (obj instanceof SingleImage) {
				si = (SingleImage) obj;
			}
		}
		siPanel.setSI(si);
	}

	private void siListMouseClicked(MouseEvent e) {
		if (e.getButton() == XUtil.LEFT_BUTTON && e.getClickCount() == 2) {
			System.out.println("mouse left button clicked");
			okPerformed();
		}
	}
}

class SISprite extends BasicSprite implements Layerable, Copyable, Flipable {

	protected SingleImage si;
	protected int layer;	// 在本层的图层
	protected int layer2;	// 图层
	protected boolean flip;
	private boolean isVisible;
	
	protected SISprite() {
		super(0, 0, 0, "");
		isVisible = true;
	}

	public SISprite(SingleImage si, int id, int x, int y, boolean flip, int layer) {
		super(id, x, y, si.getName());
		init(si, flip, layer);
	}

	public SISprite(SingleImage si, int id, int x, int y, boolean flip, String name, int layer) {
		super(id, x, y, name);
		init(si, flip, layer);
	}

	public int compareTo(Object o) {
		if (o != null) {
			if (o instanceof Layerable) {
				Layerable dest = (Layerable) o;
				if (this.getLayer() != dest.getLayer()) { return this.getLayer() - dest.getLayer(); }
			}
		}
		return super.compareTo(o);
	}

	public Copyable copy() {
		return copySISprite();
	}

	public void copyFrom(SISprite source) {
		super.copyFrom(source);
		this.si = source.si;
		this.layer = source.layer;
		this.layer2 = source.layer2;
		this.flip = source.flip;
	}

	public final SISprite copySISprite() {
		SISprite result = new SISprite();
		result.copyFrom(this);
		return result;
	}

	public void flip() {
		flip = !flip;
	}

	public int getHeight() {
		return si.getHeight();
	}

	public String getInfo() {
		String result = getName() + "  ID：" + getID() + "  宽：" + getWidth() + "  高：" + getHeight()
		        + "  层：" + getLayer();
		return result;
	}

	public int getLayer() {
		return layer;
	}
	
	public int getLayer2() {
		return layer2;
	}

	public int getLeft() {
		return getX();
	}

	public SingleImage getSI() {
		return si;
	}

	public int getTop() {
		return getY();
	}

	public int getWidth() {
		return si.getWidth();
	}

	private void init(SingleImage si, boolean flip, int layer) {
		this.si = si;
		this.flip = flip;
		this.layer = 0;
		this.layer2 = layer;
		this.isVisible = true;
	}

	public boolean isFlip() {
		return flip;
	}
	
	public void setLayer(int layer) {
		this.layer = layer;
	}

	public void setSI(SingleImage si) {
		this.si = si;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		if(!isVisible)
			setSelected(false);
	}

	protected void load(DataInputStream in, SIManager siManager) throws Exception {
		super.load(in);
		int siID = in.readInt();
		this.si = siManager.getSI(siID);
		this.layer = in.readInt();
		this.layer2 = in.readInt();
		this.flip = in.readBoolean();
		this.setRoate(in.readDouble());
		this.setScale(in.readDouble());
	}

	public void paintIdle(Graphics g, int x, int y ,double roate ,double scale) {
		if(isVisible) {
			si.paintRoate(g, x + getLeft() - getX(), y + getTop() - getY(), isFlip() ,roate ,scale);
			//si.paintLeftTop(g, x + getLeft() - getX(), y + getTop() - getY(), isFlip());
		}
	}

	public void save(DataOutputStream out) throws Exception {
		super.save(out);
		out.writeInt(si.getID());
		out.writeInt(layer);
		out.writeInt(layer2);
		out.writeBoolean(flip);
		out.writeDouble(this.getRoate());
		out.writeDouble(this.getScale());
		
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		SL.writeInt(si.getID(), out);
		SL.writeInt(getX(), out);
		SL.writeInt(getY(), out);
		SL.writeBoolean(flip, out);
		SL.writeDouble(this.getRoate(),out);
		SL.writeDouble(this.getScale(),out);		
	}
}

class CheckDuplicateImage {

	public static void main(String[] args) throws Exception {
		String siFolder = XUtil.getDefPropStr("MapImageFolder");
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(
		        XUtil.getDefPropStr("MapImageIniFile"))));
		ArrayList data = new ArrayList();
		String sLine;
		String sOut;

		sLine = in.readLine();
		while (sLine != null) {
			data.add(sLine);
			sLine = in.readLine();
		}
		in.close();

		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
		        ".\\相同的图片.txt")));

		String groupName = "";
//		boolean groupDuplicate = false;
		ArrayList images = new ArrayList();

		for (int i = 0; i < data.size(); ++i) {
			sLine = ((String) (data.get(i))).trim();
			if (sLine == null) continue;
			if (sLine.length() < 2) continue;
			if (sLine.startsWith("$") && sLine.endsWith(";") && sLine.length() > 2) { // group
				String infos[] = sLine.substring(1, sLine.length() - 1).split(",", 0);
				if (infos != null) {
					if (infos.length >= 1) {
						groupName = infos[0].trim();
//						images.clear();
//						groupDuplicate = false;
					}
				}
			}
			if (sLine.startsWith("@") && sLine.endsWith(";") && sLine.length() > 2) { // image
				String infos[] = sLine.substring(1, sLine.length() - 1).split(",", 0);
				if (infos.length >= 2) {
					String imageName = infos[1].trim();
					String fileFullName;
					if (groupName.equals("")) {
						fileFullName = siFolder + "\\" + imageName;
					}
					else {
						fileFullName = siFolder + "\\" + groupName + "\\" + imageName;
					}
					Color maskColor = SingleImage.DEF_MASK_COLOR;
					if (infos.length >= 3) {
						String sColor = infos[2].trim();
						if (sColor.length() > 2) {
							if (sColor.substring(0, 2).equalsIgnoreCase("0x")) {
								sColor = sColor.substring(2);
							}
						}
						maskColor = new Color(Integer.parseInt(sColor, 16));
					}
					XImage image = new XImage(new File(fileFullName), maskColor);
					boolean duplicate = false;
					Pair p = null;
					XImage prev = null;
					for (int j = 0; j < images.size(); ++j) {
						p = (Pair)(images.get(j));
						prev = (XImage)(p.second);
						if (image.equals(prev)) {
							duplicate = true;
							break;
						}
					}
					if (duplicate) {
						XImage img2 = (XImage)(p.second);
						sOut = (String)(p.first) + "\\" + (img2.getName() + "." + img2.getExtension()) + "\t" + 
								groupName + "\\" + image.getName() + "." + image.getExtension() + "\r\n"; 
								
						System.out.print(sOut);
						out.write(sOut.getBytes());
					}
					else {
						images.add(new Pair(groupName, image));
					}
				}
			}
		}

		sOut = "Done.";
		System.out.print(sOut);
		out.write(sOut.getBytes());
		out.flush();
		out.close();
	}
}