package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.MetalLookAndFeel;

class GroupImagePoint{
	public int x;
	public int y;
	public boolean isUse;
	public int w;
	public int h;
	public int id;
	
	public GroupImagePoint(int x, int y){
		this.x = x;
		this.y = y;
		this.isUse = false;
		this.w = 0;
		this.h = 0;
		this.id = -1;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setIsUse(boolean isUse){
		this.isUse = isUse;
	}
	
	public void setWH(int w, int h){
		this.w = w;
		this.h = h;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean getIsUse(){
		return isUse;
	}
	
	public GroupImagePoint copySelf(){
		GroupImagePoint p = new GroupImagePoint(x, y);
		p.setId(id);
		p.setIsUse(isUse);
		p.setWH(w, h);
		return p;
	}
}

class SIGroup {

	private int id;
	private String name;

	public SIGroup(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}
}

class CheckDuplicateImage {

	public static void main(String[] args) throws Exception {
		String siFolder = XUtil.getDefPropStr("ImageFolder");
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(
		        XUtil.getDefPropStr("ImageIniFile"))));
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

class SIManager {
	private SIGroup[] groups;
	private SingleImage[] sis;
	private ArrayList<GroupImage> gis;
	private int maxId;
	private boolean isChange;

	public SIManager() {
		gis = new ArrayList<GroupImage>();
		maxId = 0;
		isChange = false;
		//test
//		add("test");
//		add("test2");
	}
	
	public void save(DataOutputStream out) throws Exception {
		out.writeInt(maxId);
		out.writeInt(gis.size());
		for(int i=0; i<gis.size(); ++i) {
			gis.get(i).save(out);
		}
		
	//	if(isChange) {
			// 生成文件list.txt
			DataOutputStream tout;

			String output = "list.txt";
			output = XUtil.getDefPropStr("PakFolder")  + "\\" + output;
			
			String s = "";
			if(groups != null) {
				for(int i=0; i<groups.length; ++i) {
					s += groups[i].getID() + ".png\r\n";
				}
			}

			tout = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(output)));
			byte[] bytes = s.getBytes();
			tout.write(bytes);
			tout.flush();
			tout.close();
	//	}
	}
	
	public void load(DataInputStream in) throws Exception {
		maxId = in.readInt();
		
		int len = in.readInt();
		groups = new SIGroup[len];
		sis = new SingleImage[len];
		for(int i=0; i<len; ++i) {
			GroupImage gi = new GroupImage();
			gi.load(in);
			SIGroup group = new SIGroup(gi.getId(), gi.getName());
			gi.init(group, gi.getTypeSort(), gi.getTimeLast());
			groups[i] = group;
			sis[i] = gi.getSI();
			gis.add(gi);
		}
		MainFrame.self.UpdatePic();
	}

	public SIGroup[] getGroups() {
		return groups;
	}
	
	public GroupImage getGI(int groupId) {
		int index = -1;
		for(int i=0; i<groups.length; ++i) {
			if(groups[i].getID() == groupId){
				index = i;
				break;
			}
		}
		
		if(index < 0)
			return null;
		else
			return gis.get(index);
	}
	
	public int getIndex(int groupId) {
		for(int i=0; i<groups.length; ++i) {
			if(groups[i].getID() == groupId){
				return i;
			}
		}
		
		return -1;
	}
	
	public void setGISortType(int groupId, int[] sortType) {
		GroupImage gi = getGI(groupId);
		gi.setTypeSort(sortType);
		
		MainFrame.self.trees[0].updateOneChild(getGI(groupId).getSI().image, groupId, groupId);
		MainFrame.self.trees[0].repaint();
	}
	
	public void reGenerate(int groupId) {
		GroupImage gi = getGI(groupId);
		gi.reGenerate();
		
		MainFrame.self.trees[0].updateOneChild(getGI(groupId).getSI().image, groupId, groupId);
		MainFrame.self.trees[0].repaint();
	}
	
	public void refresh(int groupId) {
		GroupImage gi = getGI(groupId);
		try {
			gi.refresh();
			
			int index = getIndex(groupId);
			if(index >= 0) {
				if(gi.isNoPic())
					remove(index);
				else
					sis[index] = gi.getSI();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		MainFrame.self.trees[0].updateOneChild(getGI(groupId).getSI().image, groupId, groupId);
//		MainFrame.self.trees[0].repaint();
		MainFrame.self.UpdatePic();
		MainFrame.self.animPanel.aniedit.showjpanel.repaint();
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
	
	// load完后调用
	public void delLossPic() {
		if(gis != null) {
			int nowi = 0;
			boolean f = false;
			for(int i=0; i<gis.size(); ++i) {
				gis.get(nowi).delLossPic();
				if(gis.get(nowi).isNoPic()) {
					remove(nowi);
					f = true;
				}
				else {
					nowi += 1;
				}
			}
			if(f) {
				MainFrame.self.UpdatePic();
				MainFrame.self.animPanel.aniedit.showjpanel.repaint();
			}
		}
	}
	
	public boolean isExist(String dirName) {
		boolean is = false;
		if(groups != null) {
			for(int i=0; i<groups.length; ++i){
				if(dirName.equals(groups[i].getName())) {
					System.out.println("is exist: "+i);
					is = true;
					break;
				}
			}
		}
		return is;
	}
	
	public boolean add(String dirName) throws Exception {
		if(isExist(dirName))
			return false;
		
		isChange = true;
		maxId++;
		SIGroup sg = new SIGroup(maxId, dirName);
		GroupImage gi = new GroupImage();
		if(gi.init(sg, gi.makeTypeSort(GroupImage.VERTICAL, GroupImage.NOSORT), "")) {		
			int len = 0;
			if(sis != null)
				len = sis.length;
			SIGroup[] tmpGroups = new SIGroup[len];
			SingleImage[] tmpSis = new SingleImage[len];
			for(int i=0; i<len; ++i){
				tmpGroups[i] = groups[i];
				tmpSis[i] = sis[i];
			}
			groups = new SIGroup[len+1];
			sis = new SingleImage[len+1];
			groups[len] = sg;
			sis[len] = gi.getSI();
			gis.add(len, gi);
			for(int i=0; i<len; ++i){
				groups[i] = tmpGroups[i];
				sis[i] = tmpSis[i];
			}
		}
		else
			return false;
		
		return true;
	}
	
	public void remove(int  index) {
		gis.remove(index);
		int len = 0;
		if(sis != null)
			len = sis.length;
		SIGroup[] tmpGroups = new SIGroup[len];
		SingleImage[] tmpSis = new SingleImage[len];
		for(int i=0; i<len; ++i){
			tmpGroups[i] = groups[i];
			tmpSis[i] = sis[i];
		}
		groups = new SIGroup[len-1];
		sis = new SingleImage[len-1];
		for(int i=0; i<index; ++i){
			groups[i] = tmpGroups[i];
			sis[i] = tmpSis[i];
		}
		for(int i=index+1; i<len; ++i){
			groups[i-1] = tmpGroups[i];
			sis[i-1] = tmpSis[i];
		}
	}
	
	public void getFocus() throws Exception {
		int nowi = 0;
		int len = gis.size();
		boolean  isChange = false;
		for(int i=0; i<len; ++i){
			if(gis.get(nowi).getFocus()) {
				isChange = true;
				if(gis.get(nowi).isNoPic()) {
					remove(nowi);
				}
				else {
					nowi += 1;
					sis[i] = gis.get(i).getSI();
				}
			}
			else {
				nowi += 1;
			}
		}
		
		if(isChange) {
			MainFrame.self.UpdatePic();
			MainFrame.self.animPanel.aniedit.showjpanel.repaint();
			MainFrame.self.showJp.recoverALlThing();
		}
	}
}

class SIFinder {
	public String name;
	public int id;	// 图片对应的唯一的切块号
	public int x;
	public int y;
	public SingleImage si;
	
	SIFinder(String name, int id, int x, int y) {
		this.name = name;
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setSI(SingleImage si) {
		this.si = si;
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class GroupImage {
	public final static int MaxSize = 1024;
	public final static int TypeCount = 2;
	
	public final static String[] menuList = {
		"铺满方向",
		"排序类型",
	};
	
	public final static String[][] labelList = {
		{
			"水平铺满",
			"垂直铺满",
			"√",
		},
		
		{
			"不排序",
			"宽度优先",
			"高度优先",
			"√",
		}
	};
	
	//TypeSort
	public final static int HORIZON = 0;	//优先水平铺满
	public final static int VERTICAL = 1;	//优先垂直铺满
	//Sort优先
	public final static int NOSORT = 0;		//不排序
	public final static int WIDTH = 1;		//以宽高从大到小排序
	public final static int HEIGHT = 2;		//以高宽从大到小排序
	
	private int id;
	private String dirPath;
	private String name;
	private int[] typeSort;
	private ArrayList<SIFinder> nameList;
	private ArrayList<SIFinder> nameLossList;
	private int area;
	private int areaW;
	private int areaH;
	private int width;
	private int height;
	private String timeLast;
	private SIGroup group;
	private ArrayList<GroupImagePoint> borders;
	private boolean isChange;
	private clipInfoSave clipInfo;
	private int clipInfoMaxId;
	private int sizeIncrease;
	
	private SingleImage[] sis;
	private SingleImage groupSI;
	
	public GroupImage(){
		width = 0;
		height = 0;
		area = 0;
		areaW = 0;
		areaH = 0;
		clipInfoMaxId = 0;
		sizeIncrease = 0;
		isChange = false;
		borders = new ArrayList<GroupImagePoint>();
		nameList = new ArrayList<SIFinder>();
		nameLossList = new ArrayList<SIFinder>();
		typeSort = new int[2];
	}
	
	public void save(DataOutputStream out) throws Exception {
		clipInfoMaxId = clipInfo.maxId;
		
		out.writeInt(id);
		SL.writeString(name, out);
		for(int i=0; i<TypeCount; ++i)
			out.writeInt(typeSort[i]);
		out.writeInt(width);
		out.writeInt(height);
		SL.writeString(timeLast, out);
		out.writeInt(clipInfoMaxId);
		out.writeInt(nameList.size());
		for(int i=0; i<nameList.size(); ++i) {
			SIFinder sif = nameList.get(i);
			SL.writeString(sif.name, out);
			out.writeInt(sif.id);
			out.writeInt(sif.x);
			out.writeInt(sif.y);
		}
		
		//save group image
		String fullPath = "res\\" + MainFrame.self.getMapName() + "\\" + id + ".png";
		File f = new File(fullPath);
		if(!f.exists()) {
			isChange = false;
			savePNG();
		}
		else {
			if(isChange) {
				isChange = false;
				savePNG();
			}
		}
	}
	
	public void load(DataInputStream in) throws Exception {
		id = in.readInt();
		name = SL.readString(in);
		for(int i=0; i<TypeCount; ++i)
			typeSort[i] = in.readInt();
		width = in.readInt();
		height = in.readInt();
		timeLast = SL.readString(in);
		clipInfoMaxId = in.readInt();
		int len = in.readInt();
		for(int i=0; i<len; ++i) {
			String name = SL.readString(in);
			int id = in.readInt();
			int x = in.readInt();
			int y = in.readInt();
//			int x = -1;
//			int y = -1;
			nameList.add(new SIFinder(name, id, x, y));
		}
	}
	
	public void paintLeftTop(Graphics g, int left, int top) {
		for(int i=0; i<clipInfo.clipArea.length; ++i) {
			int[] info = clipInfo.clipArea[i];
			if(info[6] == 0 && i != clipInfo.chooseArr) {
				SingleImage si = getSonSI(info[5]);
				if(si != null)
					si.paintLeftTop(g, info[0], info[1], false);
			}
		}
		if(clipInfo.chooseArr >= 0) {
			int[] info = clipInfo.clipArea[clipInfo.chooseArr];
			SingleImage si = getSonSI(info[5]);
			if(si != null)
				si.paintLeftTop(g, info[0], info[1], false);
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int[] getTypeSort() {
		return typeSort;
	}
	
	public String getTimeLast() {
		return timeLast;
	}
	
	public SingleImage getSI() {
		return groupSI;
	}
	
	public SingleImage[] getSIs() {
		return sis;
	}
	
	public SingleImage getSonSI(int clipId) {
		for(int i=0; i<sis.length; ++i) {
			if(sis[i].getSIFinder().id == clipId)
				return sis[i];
		}
		return null;
	}
	
	public int[] makeTypeSort(int... args) {
		int[] tmp = new int[TypeCount];
		for(int i=0; i<TypeCount; ++i)
			tmp[i] = args[i];
		return tmp;
	}
	
	private void clearClipPos() {
		for(int i=0; i<sis.length; ++i) {
			sis[i].getSIFinder().setPos(-1, -1);
		}
	}
	
	//set完后需调用generate()
	public void setTypeSort(int[] type) {
		boolean isUpdate = false;//this.typeSort == type ? false : true;
		for(int i=0; i<TypeCount; ++i) {
			if(type[i] != this.typeSort[i]) {
				isUpdate = true;
				break;
			}
		}
		for(int i=0; i<TypeCount; ++i)
			this.typeSort[i] = type[i];
		
		if(isUpdate) {
			isChange = true;
			width = 0;
			height = 0;
			sizeIncrease = 0;
			clearClipPos();
			generate();
		}
	}
	
	private void savePNG() throws IOException {
		BufferedImage basicImage = groupSI.image;
		BufferedImage image = null;
		Raster raster = basicImage.getData();
		ColorModel model = basicImage.getColorModel();
		Color[][] colors = new Color[basicImage.getWidth()][basicImage.getHeight()];
		Object data;
		Color color;

		for (int y = 0; y < basicImage.getHeight(); ++y) {
			for (int x = 0; x < basicImage.getWidth(); ++x) {
				data = raster.getDataElements(x, y, null);
				color = new Color(model.getRGB(data), true);
				colors[x][y] = color;
			}
		}

		boolean found;
		int minX = 0;
		found = false;
		while (minX < basicImage.getWidth() && !found) {
			for (int y = 0; y < basicImage.getHeight(); ++y) {
				if (colors[minX][y].getAlpha() != 0) {
					found = true;
					break;
				}
			}
			if (!found) ++minX;
		}
		if (minX >= basicImage.getWidth()) minX = basicImage.getWidth() - 1;

		int maxX = basicImage.getWidth() - 1;
		found = false;
		while (maxX >= minX && !found) {
			for (int y = 0; y < basicImage.getHeight(); ++y) {
				if (colors[maxX][y].getAlpha() != 0) {
					found = true;
					break;
				}
			}
			if (!found) --maxX;
		}
		if (maxX < minX) maxX = minX;

		int minY = 0;
		found = false;
		while (minY < basicImage.getHeight() && !found) {
			for (int x = 0; x < basicImage.getWidth(); ++x) {
				if (colors[x][minY].getAlpha() != 0) {
					found = true;
					break;
				}
			}
			if (!found) ++minY;
		}
		if (minY >= basicImage.getHeight()) minY = basicImage.getHeight() - 1;

		int maxY = basicImage.getHeight() - 1;
		found = false;
		while (maxY >= minY && !found) {
			for (int x = 0; x < basicImage.getWidth(); ++x) {
				if (colors[x][maxY].getAlpha() != 0) {
					found = true;
					break;
				}
			}
			if (!found) --maxY;
		}
		if (maxY < minY) maxY = minY;

		// create real data
		int left = minX;
		int top = minY;
		int width = maxX - minX + 1;
		int height = maxY - minY + 1;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		g.drawImage(basicImage, -left, -top, null);//绘画
		image.flush();
		
		String dirPath = "res\\" + MainFrame.self.getMapName();
		File dir = new File(dirPath);
		if(!dir.exists())
			dir.mkdir();
		String fullPath = dirPath + "\\" + id + ".png";
		File f = new File(fullPath);
		ImageIO.write(image, "png", f);
		XUtil.copyFile(fullPath, XUtil.getDefPropStr("PakFolder") + "\\" + id + ".png");
	}
	
	public void checkNameList() {
		int len = nameList.size();
		int nowi = 0;
		for(int i=0; i<len; ++i) {
			SIFinder sif = nameList.get(nowi);
			String fullPath = dirPath + "\\" + sif.name;
			File f = new File(fullPath);
			if(!f.exists()) {
				nameLossList.add(nameList.remove(nowi));
			}
			else
				nowi += 1;
		}
	}
	
	public void delLossPic() {
		if(nameLossList != null) {
			for(int i=0; i<nameLossList.size(); ++i) {
				SIFinder sif = nameLossList.get(i);
				if(sif.id >= 0) {
					//更新编辑区
					MainFrame.self.animPanel.delClipEdit(-id, sif.id);
					//更新其他act的切片信息
					MainFrame.self.animList.delClipInfo(-id, sif.id);
				}
			}
		}
	}
	
	public boolean isNoPic() {
		if(nameList.isEmpty()) {
			MainFrame.self.trees[MainFrame.LAYER_CLIP].delAllChild(-id, true);
			return true;
		}
		
		return false;
	}
	
	public boolean getFocus() throws Exception {
		File dir = new File(dirPath);
		String nowTimeLast = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dir.lastModified());
		if(!nowTimeLast.equals(timeLast)) {
			init(group, typeSort, timeLast);
			delLossPic();
			return true;
		}
		return false;
	}
	
	public void refresh() throws Exception {
		init(group, typeSort, "refresh");
		delLossPic();
	}
	
	private boolean isInNameList(String name) {
		boolean is = false;
		for(int i=0; i<nameList.size(); ++i) {
			if(nameList.get(i).name.equals(name)) {
				is = true;
				break;
			}
		}
		
		return is;
	}
	
	public boolean init(SIGroup group, int[] typeSort, String t_timeLast) throws Exception {
		String siFolder = XUtil.getDefPropStr("ImageFolder");
		this.id = group.getID();
		this.name = group.getName();//dirPath.substring(dirPath.lastIndexOf("\\")+1);
		this.dirPath =siFolder + "\\" + this.name;
		this.typeSort = typeSort;
		this.group = group;
		this.area = 0;
		this.areaW = 0;
		this.areaH = 0;
		
		checkNameList();
		
		File dir = new File(dirPath);
		timeLast = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dir.lastModified());
	//	System.out.println(timeLast);
		if(!timeLast.equals(t_timeLast)) {
			isChange = true;
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					   if (!files[i].isDirectory()) {
						   String name = files[i].getName();
						   String exp = name.substring(name.indexOf("."));
						   if(exp.equalsIgnoreCase(".png") || exp.equalsIgnoreCase(".bmp")) {
							   if(!isInNameList(name)) {
								   nameList.add(new SIFinder(name, -1, -1, -1));
							   }
						   }
					  }
				}
			}
		}
		
		if(t_timeLast.equals("") && nameList.isEmpty())
			return false;

		int groupLayer = SingleImage.DEF_LAYER;
		Color maskColor = SingleImage.DEF_MASK_COLOR;
		ArrayList<SingleImage> tmpSIs = new ArrayList<SingleImage>();
		int len = nameList.size();
		for (int i = 0; i < len; i++) {
			SIFinder sif = nameList.get(i);
			String name = sif.name;
		    String fullPath = dirPath + "\\" + name;
	//	    System.out.println(fullPath);
		    SingleImage si = new SingleImage(group, sif, i,  fullPath, maskColor, groupLayer);
		    tmpSIs.add(si);
		    sif.setSI(si);
		}
			
		sis = new SingleImage[len];
		groupSI = new SingleImage(group, null, 0,  null, maskColor, groupLayer);
		for (int i=0; i<tmpSIs.size(); ++i) {
			SingleImage si = tmpSIs.get(i);
			try {
				si.load();
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "加载图片 " + si.toString() + " 失败/n" + e,
				        "加载错误", JOptionPane.ERROR_MESSAGE);
			}
			area += si.getWidth()*si.getHeight();
			if(si.getWidth() > areaW)
				areaW = si.getWidth();
			if(si.getHeight() > areaH)
				areaH = si.getHeight();
		//	System.out.println(i+", area: "+area+", wh: "+(si.getWidth()*si.getHeight()+", w: "+si.getWidth()+", h: "+si.getHeight()));
			sis[i] = si;
		}
		
		generate();
		return true;
	}
	
	public void reGenerate() {
		isChange = true;
		BufferedImage tempImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = tempImg.createGraphics();
		
		for(int i=0; i<borders.size(); ++i) {
			GroupImagePoint p = borders.get(i);
			if(p.getIsUse()) {
				int index = clipInfo.getCutIndex(p.id);
				if(index >= 0) {
					int[] info = clipInfo.clipArea[index];
					SingleImage si = getSonSI(info[5]);
					if(p.x != info[0] || p.y != info[1]) {
						MainFrame.self.animPanel.updateClipEdit(si.image, new int[]{info[0],  info[1], p.w, p.h,  -id, p.id});
						MainFrame.self.animList.updateClipInfo(si.image, new int[]{info[0],  info[1], p.w, p.h,  -id, p.id});
					}
					p.x = info[0];
					p.y = info[1];
					si.getSIFinder().x = p.x;
					si.getSIFinder().y = p.y;
					
					g.drawImage(si.image, p.x, p.y, p.x+p.w, p.y+p.h, 0, 0, p.w, p.h, null);
				}
			}
		}
		tempImg.flush();
		
		groupSI.image = null;
		groupSI.image = tempImg;
	}
	
	private void adjustSize() {
		if(width > 0 && height > 0)
			return;
		
		int n=1;
		int s = area;
		while(s>1) {
			s /= 2;
			n += 1;
		}
		n += sizeIncrease;		
		int n2 = (int) Math.floor(n/2.0);	//low
		n -= n2;
		
		if(typeSort[0] == HORIZON) {
			width = new BigInteger("2").pow(n).intValue();//(int) Math.pow(2.0, (double)n);
			height = new BigInteger("2").pow(n2).intValue();
		}
		else if(typeSort[0] == VERTICAL) {
			width = new BigInteger("2").pow(n2).intValue();
			height = new BigInteger("2").pow(n).intValue();
		}
		if(width < areaW) {
			width = areaW;
		//	height = area/width;
		}
		else if(height < areaH) {
			height = areaH;
		//	width = area/height;
		}
		
		System.out.println("w: "+width+", h: "+height+", size: "+area+", "+n+", "+n2);
	}
	
	private void generate() {
		adjustSize();
		//test
		clipInfoSave cifs =new clipInfoSave(id, id);
		MainFrame.self.delClipArr(id, id);
		MainFrame.clipArr.add(cifs);
		clipInfo = cifs;
		clipInfo.setMaxId(clipInfoMaxId);
		
		if(typeSort[1] == NOSORT) {
			sortSIs_NoSort();
		}
		else if(typeSort[1] == WIDTH) {
			sortSIs_Width();
		}
		else if(typeSort[1] == HEIGHT) {
			sortSIs_Height();
		}
		
		int nowx = 0;
		int nowy = 0;
		borders.clear();
		borders.add(new GroupImagePoint(nowx, nowy));
		BufferedImage tempImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = tempImg.createGraphics();
		MainFrame.self.trees[MainFrame.LAYER_CLIP].delAllChild(-id, false);
		SIGroup sicc =new SIGroup(-id, name);
		
		for (int i = 0; i < sis.length; ++i) {
			if(sis[i] != null) {
				SIFinder sif = sis[i].getSIFinder();
				int w = sis[i].getWidth();
				int h = sis[i].getHeight();
				int clipId = sif.id;
				clipInfo.setMaxId(clipId+1);

				if(sif.x >= 0) {
					nowx = sif.x;
					nowy = sif.y;
					GroupImagePoint gip = new GroupImagePoint(nowx, nowy);
					gip.setId(clipId);
					gip.setIsUse(true);
					gip.setWH(w, h);
					borders.add(gip);
					
					g.drawImage(sis[i].image, nowx, nowy, nowx+w, nowy+h, 0, 0, w, h, null);
					//test
					cifs.addOneCut(nowx, nowy, w, h, -id, clipId, 0);
					ClipPicPanel cp = new ClipPicPanel(sis[i].image, clipId, -id, nowx, nowy, w, h);
					cp.setName(sis[i].getName());
					MainFrame.self.trees[MainFrame.LAYER_CLIP].addNewNodeToTree(new SIPainterGroup(sicc), cp);
					MainFrame.self.animPanel.updateClipEdit(sis[i].image, new int[]{nowx, nowy, w, h,  -id, clipId});
					MainFrame.self.animList.updateClipInfo(sis[i].image, new int[]{nowx, nowy, w, h,  -id, clipId});
				//	MainFrame.self.trees[MainFrame.LAYER_CLIP].expandRow(0);
				//	System.out.println(nowx+", "+nowy+", "+w+", "+h);
				}
			}
		}
		
		for (int i = 0; i < sis.length; ++i) {
			if(sis[i] != null) {
				SIFinder sif = sis[i].getSIFinder();
				if(sif.x >= 0) continue;
				
				int w = sis[i].getWidth();
				int h = sis[i].getHeight();
				int clipId = sif.id;
				if(clipId < 0) {
					clipId = clipInfo.getMaxId();
					sif.setId(clipId);
				}
				else {
					clipInfo.setMaxId(clipId+1);
				}
				
				boolean isAdd = true;
				boolean isFind = false;
				for(int j=0; j<borders.size(); ++j) {
					GroupImagePoint p = borders.get(j);
					if(!p.getIsUse() && p.x+w <= width && p.y+h <= height) {
						boolean isThis = true;
						for(int k=0; k<borders.size(); ++k){
							if(k != j){
								GroupImagePoint p2 = borders.get(k);
								if(p2.getIsUse() && p2.x < p.x+w && p2.x+p2.w > p.x && p2.y < p.y+h && p2.y+p2.h > p.y){
									isThis = false;
									break;
								}
							}
						}
						if(isThis){
							isFind = true;
							nowx = p.x;
							nowy = p.y;	
							p.setId(clipId);
							p.setIsUse(true);
							p.setWH(w, h);
							break;
						}
					}
				}
				
				if(!isFind) {
					// 自增长一次
					if(sizeIncrease == 0) {
						sizeIncrease += 1;
						width = 0;
						height = 0;
						generate();
						return;
					}
					else {
						isAdd = false;
						nowx = width-w;
						nowy = height-h;
						GroupImagePoint gip = new GroupImagePoint(nowx, nowy);
						gip.setId(clipId);
						gip.setIsUse(true);
						gip.setWH(w, h);
						borders.add(gip);
					}
				}
				
				g.drawImage(sis[i].image, nowx, nowy, nowx+w, nowy+h, 0, 0, w, h, null);
				//test
				cifs.addOneCut(nowx, nowy, w, h, -id, clipId, 0);
				ClipPicPanel cp = new ClipPicPanel(sis[i].image, clipId, -id, nowx, nowy, w, h);
				cp.setName(sis[i].getName());
				MainFrame.self.trees[MainFrame.LAYER_CLIP].addNewNodeToTree(new SIPainterGroup(sicc), cp);
				MainFrame.self.animPanel.updateClipEdit(sis[i].image, new int[]{nowx, nowy, w, h,  -id, clipId});
				MainFrame.self.animList.updateClipInfo(sis[i].image, new int[]{nowx, nowy, w, h,  -id, clipId});
			//	MainFrame.self.trees[MainFrame.LAYER_CLIP].expandRow(0);
			//	System.out.println(nowx+", "+nowy+", "+w+", "+h);
				
				if(isAdd) {
					borders.add(new GroupImagePoint(nowx+w, nowy));
					borders.add(new GroupImagePoint(nowx, nowy+h));
				}
				
				if(typeSort[0] == HORIZON) {
					sortBorders_fillWidth();
				}
				else if(typeSort[0] == VERTICAL) {
					sortBorders_fillHeight();
				}
			}
		}
		tempImg.flush();
		
		groupSI.image = tempImg;
		groupSI.setId(id);
		groupSI.setGroup(new SIGroup(id, name));
		groupSI.setName(name);
	}
	
	private void sortBorders_fillWidth() {
		ArrayList<GroupImagePoint> tmp = new ArrayList<GroupImagePoint>();
		for(int j=0; j<borders.size(); ++j){
			int index = tmp.size();
			GroupImagePoint p = borders.get(j);
			for(int k=0; k<tmp.size(); ++k){
				GroupImagePoint p2 = tmp.get(k);
				if(p.y < p2.y){
					index = k;
					break;
				}
				else if(p.y == p2.y){
					boolean isBreak = false;
					for(int k2=k; k2<tmp.size(); ++k2){
						p2 = tmp.get(k2);
						if(p.y == p2.y){
							if(p.x < p2.x ){
								index = k2;
								isBreak = true;
								break;
							}
						}
						else{
							index = k2;
							isBreak = true;
							break;
						}
					}
					if(isBreak)
						break;
				}
			}
			
			tmp.add(index, p.copySelf());
	//		System.out.println(index+", "+p.x+", "+p.y);
		}
		borders.clear();
		for(int j=0; j<tmp.size(); ++j){
			GroupImagePoint p = tmp.get(j);
			borders.add(p.copySelf());
	//		System.out.println(p.x+", "+p.y+", "+p.isUse);
		}
	//	System.out.println("");
		tmp.clear();
	}
	
	private void sortBorders_fillHeight() {
		ArrayList<GroupImagePoint> tmp = new ArrayList<GroupImagePoint>();
		for(int j=0; j<borders.size(); ++j){
			int index = tmp.size();
			GroupImagePoint p = borders.get(j);
			for(int k=0; k<tmp.size(); ++k){
				GroupImagePoint p2 = tmp.get(k);
				if(p.x < p2.x){
					index = k;
					break;
				}
				else if(p.x == p2.x){
					boolean isBreak = false;
					for(int k2=k; k2<tmp.size(); ++k2){
						p2 = tmp.get(k2);
						if(p.x == p2.x){
							if(p.y < p2.y ){
								index = k2;
								isBreak = true;
								break;
							}
						}
						else{
							index = k2;
							isBreak = true;
							break;
						}
					}
					if(isBreak)
						break;
				}
			}
			
			tmp.add(index, p.copySelf());
	//		System.out.println(index+", "+p.x+", "+p.y);
		}
		borders.clear();
		for(int j=0; j<tmp.size(); ++j){
			GroupImagePoint p = tmp.get(j);
			borders.add(p.copySelf());
	//		System.out.println(p.x+", "+p.y+", "+p.isUse);
		}
	//	System.out.println("");
		tmp.clear();
	}
	
	private void sortSIs_Width() {
		ArrayList<SingleImage> tmpSIs = new ArrayList<SingleImage>();
		for(int i=0; i<sis.length; ++i) {
			int index = tmpSIs.size();
			int w = sis[i].image.getWidth();
			int h = sis[i].image.getHeight();
			for(int j=0; j<tmpSIs.size(); ++j) {
				int w2 = tmpSIs.get(j).getWidth();
				if(w > w2) {
					index = j;
					break;
				}
				else if(w == w2) {
					boolean isFind = false;
					for(int k=j; k<tmpSIs.size(); ++k) {
						int w3 = tmpSIs.get(k).getWidth();
						int h2 = tmpSIs.get(k).getHeight();
						if(w == w3) {
							if(h > h2) {
								index = k;
								isFind = true;
								break;
							}
						}
						else {
							index = k;
							isFind = true;
							break;
						}
					}
					if(isFind)
						break;
				}
			}
			
			tmpSIs.add(index, sis[i]);
		}
		
		int len = tmpSIs.size();
		for(int i=0; i<len; ++i){
			sis[i] = tmpSIs.remove(0);
		}
	}
	
	private void sortSIs_Height() {
		ArrayList<SingleImage> tmpSIs = new ArrayList<SingleImage>();
		for(int i=0; i<sis.length; ++i) {
			int index = tmpSIs.size();
			int w = sis[i].image.getWidth();
			int h = sis[i].image.getHeight();
			for(int j=0; j<tmpSIs.size(); ++j) {
				int h2 = tmpSIs.get(j).getHeight();
				if(h > h2) {
					index = j;
					break;
				}
				else if(h == h2) {
					boolean isFind = false;
					for(int k=j; k<tmpSIs.size(); ++k) {
						int h3 = tmpSIs.get(k).getHeight();
						int w2 = tmpSIs.get(k).getWidth();
						if(h == h3) {
							if(w > w2) {
								index = k;
								isFind = true;
								break;
							}
						}
						else {
							index = k;
							isFind = true;
							break;
						}
					}
					if(isFind)
						break;
				}
			}
			
			tmpSIs.add(index, sis[i]);
		}
		
		int len = tmpSIs.size();
		for(int i=0; i<len; ++i){
			sis[i] = tmpSIs.remove(0);
		}
	}
	
	private void sortSIs_NoSort() {
		ArrayList<SingleImage> tmpSIs = new ArrayList<SingleImage>();
		for(int i=0; i<nameList.size(); ++i) {
			tmpSIs.add(nameList.get(i).si);
		}
		
		int len = tmpSIs.size();
		for(int i=0; i<len; ++i){
			sis[i] = tmpSIs.remove(0);
		}
	}
}

public class SingleImage {

	public final static int DEF_LAYER = 50;
	public final static Color DEF_MASK_COLOR = new Color(0xFF00FF);

	public BufferedImage image;
	private BufferedImage flippedImage;
	private SIGroup group;
	private SIFinder sif;
	private int id;
	private File f;
	private Color maskColor;
	private String name;
	private int defLayer;

	public SingleImage(SIGroup group, SIFinder sif, int id, String fileFullName, Color maskColor, int defLayer) {
		this.group = group;
		this.sif = sif;
		this.id = id;
		this.maskColor = maskColor;
		this.defLayer = defLayer;
		
		if(fileFullName != null) {
			this.f = new File(fileFullName);
			this.name = FileExtFilter.getName(f);
		}
	}

	public int getDefLayer() {
		return defLayer;
	}

	public SIGroup getGroup() {
		return group;
	}
	
	public SIFinder getSIFinder() {
		return sif;
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
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setGroup(SIGroup group) {
		this.group = group;
	}
	
	public void setName(String name) {
		this.name = name;
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

		//进行翻转操作
		AffineTransform at = new AffineTransform(-1, 0, 0, 1, image.getWidth(), 0);
		AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		flippedImage = ato.filter(image, null);
	}

	public void paintLeftTop(Graphics g, int left, int top, boolean flip) {
		BufferedImage img = image;
		if (flip) {
			img = flippedImage;
		}
		g.drawImage(img, left, top, null);
	}

	public void paintOrigin(Graphics g, int originX, int originY, boolean flip) {
		paintLeftTop(g, getLeft(originX), getTop(originY), flip);
	}

	public String toString() {
		return name;
	}
}

class PointSI extends SingleImage {

	public final static int GROUP_ID = -1000;
	public final static SIGroup GROUP = new SIGroup(GROUP_ID, "逻辑点");

	private String name;
	private Color color;

	public PointSI(int id, String name, Color color) {
		super(GROUP, null, -Math.abs(id + 1), "nothing.bmp", Color.BLACK, 1000);
		this.name = name;
		this.color = color;
	}
	
	public String getLuaName() {
		return Frame.POINT_LUA_NAMES[Math.abs(getID()) - 1];
	}

	public int getHeight() {
		return 3;
	}

	public int getLeft(int originX) {
		return originX - 1;
	}

	public int getOriginX(int left) {
		return left + 1;
	}

	public Color getMaskColor() {
		return Color.BLACK;
	}

	public String getName() {
		return name;
	}

	public int getTop(int originY) {
		return originY - 1;
	}

	public int getOriginY(int top) {
		return top + 1;
	}

	public int getWidth() {
		return 3;
	}

	public void load() throws Exception {}

	public void paintLeftTop(Graphics g, int left, int top, boolean flip) {
		Color oldColor = g.getColor();

		g.setColor(color);

		g.fillRect(left, top, 3, 3);
		g.fillOval(left, top, getWidth(), getHeight());

		g.setColor(oldColor);
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
				result[i].computeSize();//设置每一个节点的显示区域
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
		si.paintLeftTop(g, left, top, isFlip());
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

class SISelecter extends OKCancelDialog {

	private DefaultListModel siModel;
	private JList siList;
	private ScrollablePanel siPanel;
	private SIManager siManager;
	private SingleImage si;

	public SISelecter(JDialog owner, int siID, SIManager siManager) {
		super(owner);
		this.siManager = siManager;
		setTitle("选择图片");
		Container cp = this.getContentPane();
		si = null;

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

		siPanel = new ScrollablePanel(XUtil.getDefPropInt("BRPanelWidth"),
		        XUtil.getDefPropInt("BRPanelHeight")) {

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (!isVisible()) { return; }
				if (si != null) {
					si.paintOrigin(g, getBasicWidth() / 2, getBasicHeight() / 2, false);
				}
			}
		};
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, siScroll,
		        siPanel.getBackPanel());
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
		si = null;
		Object obj = siList.getSelectedValue();
		if (obj != null) {
			if (obj instanceof SingleImage) {
				si = (SingleImage) obj;
			}
		}
		repaint();
	}

	private void siListMouseClicked(MouseEvent e) {
		if (e.getButton() == XUtil.LEFT_BUTTON && e.getClickCount() == 2) {
			okPerformed();
		}
	}
}

class SISprite extends BasicSprite implements Layerable, Copyable, Flipable {

	public final static SISprite[] copyArray(SISprite[] array) {
		SISprite[] result = null;
		if (array != null) {
			result = new SISprite[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copySISprite();
			}
		}
		return result;
	}

	public final static SISprite[] createArrayViaFile(DataInputStream in, SIManager siManager)
	        throws Exception {
		SISprite[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new SISprite[length];
			for (int i = 0; i < length; ++i) {
				result[i] = SISprite.createViaFile(in, siManager);
			}
		}
		return result;
	}

	public final static SISprite createViaFile(DataInputStream in, SIManager siManager)
	        throws Exception {
		int siID = in.readInt();
		SingleImage si = siManager.getSI(siID);
		SISprite result = new SISprite(si, 0, 0, 0, false, "");
		result.load(in, siManager);
		return result;
	}

	private SingleImage si;
	private int layer;
	private boolean flip;

	public SISprite(SingleImage si, int id, int left, int top, boolean flip) {
		super(id, left, top, si.getName());
		init(si, flip);
	}

	public SISprite(SingleImage si, int id, int left, int top, boolean flip, String name) {
		super(id, left, top, name);
		init(si, flip);
	}

	// public Color getSelectedBorderColor() {
	// return new Color(0xFF00FF);
	// }

	// public Color getMovingBorderColor() {
	// return new Color(0xFF00FF);
	// }

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
		this.flip = source.flip;
	}

	public final SISprite copySISprite() {
		SISprite result = new SISprite(this.si, this.getID(), this.getX(), this.getY(), this.flip,
		        this.getName());
		result.copyFrom(this);
		return result;
	}

	public void flip() {
		flip = !flip;
	}
	
	public boolean ifFlip()
	{
		return flip;
	}

	public int getHeight() {
		return si.getHeight();
	}

	public String getInfo() {
		if (si.getID() >= 0) {
			String result = getName() + "  ID：" + getID() + "  宽：" + getWidth() + "  高："
			        + getHeight() + "  层：" + getLayer();
			return result;
		}
		else {
			PointSI psi = (PointSI) si;
			return si.getName() + "  x：" + (psi.getOriginX(getX()) - FrameManager.ORIGIN_X)
			        + "  y：" + (psi.getOriginY(getY()) - FrameManager.ORIGIN_Y);
		}
	}

	public int getLayer() {
		return layer;
	}

	public int getLeft() {
		return getX();
	}

	public SingleImage getSI() {
		return si;
	}

	public String getSelectMenuName() {
		SingleImage si = getSI();
		return si.getGroup().getName() + "\\" + si.getName();
	}

	public int getTop() {
		return getY();
	}

	public int getWidth() {
		return si.getWidth();
	}

	private void init(SingleImage si, boolean flip) {
		this.si = si;
		this.flip = flip;
		this.layer = si.getDefLayer();
	}

	public boolean isFlip() {
		return flip;
	}

	protected void load(DataInputStream in, SIManager siManager) throws Exception {
		super.load(in);
		this.layer = in.readInt();
		this.flip = in.readBoolean();
	}

	public void paintIdle(Graphics g, int x, int y) {
		si.paintLeftTop(g, x, y, isFlip());
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(si.getID());
		super.save(out);
		out.writeInt(layer);
		out.writeBoolean(flip);
	}

	public void saveMobile(DataOutputStream out) throws Exception {
		int id = si.getID();
		int x = getX() - FrameManager.ORIGIN_X;
		int y = getY() - FrameManager.ORIGIN_Y;

		boolean shortId = (id > 0xFF);
		boolean shortX = (x > 0xFF);
		boolean shortY = (y > 0xFF);

		int flag = 0;
		if (flip) flag |= 0x1;
		if (shortId) flag |= 0x2;
		if (shortX) flag |= 0x4;
		if (shortY) flag |= 0x8;

		SL.writeByte(flag, out);

		if (shortId) {
			SL.writeShort(id, out);
		}
		else {
			SL.writeByte(id, out);
		}
		if (shortX) {
			SL.writeShort(x, out);
		}
		else {
			SL.writeByte(x, out);
		}
		if (shortY) {
			SL.writeShort(y, out);
		}
		else {
			SL.writeByte(y, out);
		}
	}

	public void saveLua(StringBuffer s) {
		int id = si.getID();
		if (id >= 0) {
			int x = getX() - FrameManager.ORIGIN_X;
			int y = getY() - FrameManager.ORIGIN_Y;
			s.append("{i=" + id + ",x=" + x + ",y=" + y + (flip ? ",f=1" : "") + "}");
		}
		else {
			PointSI psi = (PointSI)si;
			int x = psi.getOriginX(getX()) - FrameManager.ORIGIN_X;
			int y = psi.getOriginY(getY()) - FrameManager.ORIGIN_Y;
			s.append("{x=" + x + ",y=" + y + ",id=" + Math.abs(getSI().getID()) + "}");
		}
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public void setSI(SingleImage si) {
		this.si = si;
	}
}

class CreateImageList extends JFrame {
	
	public static void main(String[] args) {
		try {
			DefaultTheme theme = new DefaultTheme();
			MetalLookAndFeel.setCurrentTheme(theme);
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e, "初始化错误", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		new CreateImageList();
	}
	
	public CreateImageList() {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 600));
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {

			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		try {
			init();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private JFileChooser fc;
	private NumberSpinner idSpinner;
	private JTextField fileText;
	private JTextArea listArea;
	
	private void init() throws Exception  {
		setTitle("图片列表生成工具");
		fc = new JFileChooser(".\\res");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(true);
        
        idSpinner = new NumberSpinner();
        idSpinner.setIntValue(1);
        
        fileText = new JTextField();
        JButton fileButton = new JButton("...");
        fileButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		chooseFiles();
        	}
        });
                
        listArea = new JTextArea();
        listArea.setLineWrap(false);
         
        JButton createButton = new JButton("生成列表");
        createButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		createList();
        	}
        });
        
		Container cp = this.getContentPane();
		cp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 2, 3, 3);
		
		c.gridy = 0;
		c.gridx = 0;
		c.weighty = 0;
		c.weightx = 0;
		cp.add(new JLabel("起始ID："), c);
		c.gridx = 1;
		c.weightx = 1;
		c.gridwidth = 2;
		cp.add(idSpinner, c);		
		c.gridwidth = 1;
		
		c.gridy = 1;
		c.gridx = 0;
		c.weightx = 0;
		cp.add(new JLabel("文件列表："), c);
		c.gridx = 1;
		c.weightx = 1;
		cp.add(fileText, c);
		c.gridx = 2;
		c.weightx = 0;
		cp.add(fileButton, c);
		
		c.gridy = 2;
		c.gridx = 0;
		c.weightx = 1;
		c.gridwidth = 3;
		cp.add(createButton, c);
		
		c.gridy = 3;
		c.gridx = 0;
		c.weightx = 1;
		c.gridwidth = 3;
		c.weighty = 1;
		cp.add(new JScrollPane(listArea), c);
		
		show();
	}
	
	private File[] getFiles() {
		ArrayList tmp = new ArrayList();
		String s = fileText.getText();
		String[] names = s.split(";");
		if(names != null) {
			for(int i = 0; i < names.length; ++i) {
				String name = names[i];
				if(name != null) {
					if(!name.trim().equals("")) {
						tmp.add(new File(name));
					}
				}
			}
		}
		if(tmp.isEmpty()) return null;
		File[] files = new File[tmp.size()];
		for(int i = 0; i < tmp.size(); ++i) {
			files[i] = (File)(tmp.get(i));
		}
		return files;
	}
	
	private void chooseFiles() {
		File[] files = getFiles();
		if(files != null) {
			fc.setSelectedFiles(files);
		}
		int ct = fc.showOpenDialog(this);
		if(ct == JFileChooser.APPROVE_OPTION ) {
			files = fc.getSelectedFiles();
			if(files != null) {
				String s = "";
				for(int i = 0; i < files.length; ++i) {
					File f = files[i];
					if(f != null) {
						s += f.getAbsolutePath();
						if(i != files.length - 1) {
							s += ";";
						}
					}
				}
				fileText.setText(s);
			}
		}
		
	}
	
	private void createList() {
		File[] files = getFiles();
		if(files == null) return;
		
		String s = "";
		int id = idSpinner.getIntValue();
		for(int i = 0; i < files.length; ++i) {
			File f = files[i];
			if(f != null) {
				s += "@" + (id + i) + ", " + f.getName() + ", 0xFF00FF, 20;\r\n";
			}
		}
		s += "//后面从" + (id + files.length) + "开始";
		
		listArea.setText(s);
	}
}

class DirChooser {
	public DirChooser() {
		parent =	MainFrame.self;
		dirPath = "";
		
		try {
			init();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	private JFileChooser fc;
	private String dirPath;
	private Component parent;
	
	private void init() throws Exception  {
		fc = new JFileChooser(".\\res");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(false);
   //     chooseDir();
	}
	
	public void setParent(Component parent){
		this.parent = parent;
	}
	
	public String getDir(){
		String path = dirPath;
		dirPath = "";
		return path;
	}
	
	public void chooseDir() {
		int ct = fc.showOpenDialog(parent);
		if(ct == JFileChooser.APPROVE_OPTION ) {
			dirPath = fc.getSelectedFile().getName();
		//	System.out.println("You chose to open this file: " + dirPath);
		}
	}
}
