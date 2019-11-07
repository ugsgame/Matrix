package editor;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

class FramePointArr {
	private ArrayList pointArr;
	private ArrayList copyPointArr;
	public FramePointArr(){
		pointArr =new ArrayList();
		copyPointArr =new ArrayList();
	}
	public void addOneToArr(LogicPoint lp){
		pointArr.add(lp);
	}
	public ArrayList getArr(){
		return pointArr;
	}
	public void paint(Graphics g){
		if(!pointArr.isEmpty()){
			for(int i=0;i<pointArr.size();i++){
				LogicPoint lr =(LogicPoint)pointArr.get(i);
				lr.paint(g);
			}
		}
	}
	
	public String GetInfo(){
		String s="";
		int index = -1;
		int sNum = 0;
		
		if(!pointArr.isEmpty()){
			for(int i=0;i<pointArr.size();i++){
				LogicPoint lr =(LogicPoint)pointArr.get(i);
				if(lr.isSelected()){
					sNum = sNum + 1;
					index = i;
				}
			}
		}
		
		if(sNum == 1){
			LogicPoint lr =(LogicPoint)pointArr.get(index);
			s = Frame.POINT_NAMES[lr.getType()] + ": x = " + lr.x + ", y = "+ lr.y;
		}else if(sNum > 1){
			s = "一共选择了 " + sNum + " 个物体";
		}
		
		return s;
	}
	
	public int GetIndex(int type){
		int index=-1;
		if(!pointArr.isEmpty()){
			for(int i=0;i<pointArr.size();i++){
				LogicPoint lr =(LogicPoint)pointArr.get(i);
				if(lr.getType() == type){
					index=i;
					break;
				}
			}
		}
		return index;
	}
	
	public LogicPoint GetPoint(int index){
		LogicPoint lr =(LogicPoint)pointArr.get(index);
		return lr;
	}
	
	public void removeObj(){
		if(!pointArr.isEmpty()){
			for(int i=0;i<pointArr.size();i++){
				LogicPoint lr =(LogicPoint)pointArr.get(i);
				if(lr.isSelected()){
					pointArr.remove(i);
					i=-1;
				}
			}
		}
	}
	public FramePointArr copyFramePoint(){
		FramePointArr result =new FramePointArr();
		result.copyOne(this);
		return result;
	}
	public void copyOne(FramePointArr ofd){
		if(!ofd.pointArr.isEmpty()){
			for(int i=0;i<ofd.pointArr.size();i++){
				LogicPoint lr =(LogicPoint)ofd.pointArr.get(i);
				LogicPoint newLr =lr.copyLogicPoint();
				newLr.setSelected(false);
				this.pointArr.add(newLr);
			}
		}
		
		if(!ofd.copyPointArr.isEmpty()){
			copyPointArr.clear();
			for(int i=0;i<ofd.copyPointArr.size();i++){
				LogicPoint lp =(LogicPoint)ofd.copyPointArr.get(i);
				this.copyPointArr.add(lp);
			}
		}
	}
	
	public void todoCopy(){
		if(!pointArr.isEmpty()){
//			copyPointArr.clear();
//			LogicPoint lp;
//			for(int i=0;i<pointArr.size();i++){
//				lp =(LogicPoint)pointArr.get(i);
//				if(lp.isSelected()){
//					copyPointArr.add(lp);
//				}
//			}
		}
	}
	public void todoPaste(FramePointArr framepoint, int dx, int dy){
		if(!framepoint.copyPointArr.isEmpty()){
//			setState(false);
//			LogicPoint lp;
//			for(int i=0;i<framepoint.copyPointArr.size();i++){
//				lp =(LogicPoint)framepoint.copyPointArr.get(i);
//				LogicPoint newlp = lp.copyLogicPoint();
//				newlp.setSelected(true);
//				newlp.x = newlp.x + dx;
//				newlp.y = newlp.y + dy;
//				pointArr.add(newlp);
//			}
		//	JOptionPane.showMessageDialog(null, "已经成功复制粘贴"+copyArrList.size()+"个对象", "提示", JOptionPane.INFORMATION_MESSAGE);
		//	copyPointArr.clear();
		}
	}
	
	public void setState(boolean bl){
		if(!pointArr.isEmpty()){
			for(int i=0;i<pointArr.size();i++){
				LogicPoint lr =(LogicPoint)pointArr.get(i);
				lr.setSelected(bl);
			}
		}
	}
	public int mouseState(int x, int y){
		int stateNum =-1;
		int sel =selectLenght();
		if(sel>0){
			if(!pointArr.isEmpty()){
				for(int i=0;i<pointArr.size();i++){
					LogicPoint lr =(LogicPoint)pointArr.get(i);
					if(lr.isSelected() && lr.containPoint(x, y)){
						stateNum = 0;
						break;
					}
				}
			}
		}
		return stateNum;
	}
	
	public void movePoint(int offx, int offy){
		if(!pointArr.isEmpty()){
			for(int i=0;i<pointArr.size();i++){
				LogicPoint lr =(LogicPoint)pointArr.get(i);
				if(lr.isSelected()){
					lr.move(offx, offy);
				}
			}
		}
	}
	
	public void inRect(int left, int top, int width, int height) {
		if(!pointArr.isEmpty()){
			for(int i=0;i<pointArr.size();i++){
				LogicPoint lp = (LogicPoint)(pointArr.get(i));
				if(lp.inRect(left, top, width, height))
					lp.setSelected(true);
				else
					lp.setSelected(false);
			}
		}
	}
	
	public boolean returnSelect(int x,int y,boolean isCtr, boolean isClear){
		boolean f=false;
		if(isCtr){
			if(!pointArr.isEmpty()){
				for(int i=0;i<pointArr.size();i++){
					LogicPoint lr =(LogicPoint)pointArr.get(i);
					if(lr.containPoint(x, y)){
						f=true;
						lr.setSelected(!lr.isSelected());
					}
				}
			}
		}else{
			int sl =selectLenght();
			if(sl<2){
				if(isClear)
					setState(false);
				if(!pointArr.isEmpty()){
					for(int i=0;i<pointArr.size();i++){
						LogicPoint lr =(LogicPoint)pointArr.get(i);
						if(lr.containPoint(x, y)){
							f=true;
							lr.setSelected(true);
							break;
						}
					}
				}
			}else{
				boolean inFw =false;
				if(!pointArr.isEmpty()){
					for(int i=0;i<pointArr.size();i++){
						LogicPoint lr =(LogicPoint)pointArr.get(i);
						if(lr.containPoint(x, y)){
							f=true;
							inFw =true;
							if(!lr.isSelected()){
								setState(false);
								lr.setSelected(true);
							}
							break;
						}
					}
					if(!inFw && isClear){
						setState(false);
					}
				}
			}
		}
		return f;
	}
	private int selectLenght(){
		int reslut =0;
		if(!pointArr.isEmpty()){
			for(int i=0;i<pointArr.size();i++){
				LogicPoint lr =(LogicPoint)pointArr.get(i);
				if(lr.isSelected()){
					reslut ++;
				}
			}
		}
		return reslut;
	}

	public void save(DataOutputStream out)throws Exception{
		if(!pointArr.isEmpty()){
			out.writeInt(pointArr.size());
			for(int i=0;i<pointArr.size();i++){
				LogicPoint lr =(LogicPoint)pointArr.get(i);
				lr.save(out);
			}
		}else{
			out.writeInt(0);
		}
	}
	public void load(DataInputStream in) throws Exception{
		pointArr.clear();
		int drawNum =in.readInt();
		if(drawNum >0){
			for(int i=0;i<drawNum;i++){
				LogicPoint lr =LogicPoint.createViaFile(in);
				pointArr.add(lr);
			}
		}
	}
}

public class FrameRectArr {
	private ArrayList rectArr;
	private ArrayList copyRectArr;
	public FrameRectArr(){
		rectArr =new ArrayList();
		copyRectArr =new ArrayList();
	}
	public void addOneToArr(LogicRect lr){
		rectArr.add(lr);
	}
	public ArrayList getArr(){
		return rectArr;
	}
	public void paint(Graphics g){
		if(!rectArr.isEmpty()){
			for(int i=0;i<rectArr.size();i++){
				LogicRect lr =(LogicRect)rectArr.get(i);
				lr.paint(g);
			}
		}
	}
	
	public LogicRect getCopyFirst(){
		if(!copyRectArr.isEmpty()){
			return (LogicRect)(copyRectArr.get(0));
		}
		
		return null;
	}
	
	public void doCopy(){
		copyRectArr.clear();
		if(!rectArr.isEmpty()){
			for(int i=0;i<rectArr.size();i++){
				LogicRect lr =(LogicRect)rectArr.get(i);
				if(lr.isSelected()){
					copyRectArr.add(lr);
				}
			}
		}
	}
	public void doPaste(FrameRectArr framerect, int dx, int dy){
		if(!framerect.copyRectArr.isEmpty()){
			setState(false);
			for(int i=0;i<framerect.copyRectArr.size();i++){
				LogicRect lr =(LogicRect)framerect.copyRectArr.get(i);
				LogicRect newLr =lr.copyLogicRect();
				newLr.top = newLr.top + dy;
				newLr.left = newLr.left + dx;
				newLr.setSelected(true);
				rectArr.add(newLr);
			}
		//	JOptionPane.showMessageDialog(null, "已经成功复制粘贴"+copyRectArr.size()+"个对象", "提示", JOptionPane.INFORMATION_MESSAGE);
		//	copyRectArr.clear();
		}
	}
	public void removeObj(){
		if(!rectArr.isEmpty()){
			for(int i=0;i<rectArr.size();i++){
				LogicRect lr =(LogicRect)rectArr.get(i);
				if(lr.isSelected()){
					rectArr.remove(i);
					i=-1;
				}
			}
		}
	}
	
	public String GetInfo(){
		String s="";
		int index = -1;
		int sNum = 0;
		
		if(!rectArr.isEmpty()){
			for(int i=0;i<rectArr.size();i++){
				LogicRect lr =(LogicRect)rectArr.get(i);
				if(lr.isSelected()){
					sNum = sNum + 1;
					index = i;
				}
			}
		}
		
		if(sNum == 1){
			LogicRect lr =(LogicRect)rectArr.get(index);
			s = Frame.RECT_NAMES[lr.getType()]+ (index+1) + ": l = " + lr.left + ", t = "+ lr.top + ", w = "+ lr.width + ", h = "+ lr.height;
		}else if(sNum > 1){
			s = "一共选择了 " + sNum + " 个物体";
		}
		
		return s;
	}
	
	public void setState(boolean bl){
		if(!rectArr.isEmpty()){
			for(int i=0;i<rectArr.size();i++){
				LogicRect lr =(LogicRect)rectArr.get(i);
				lr.setSelected(bl);
			}
		}
	}
	public int mouseState(int x, int y){
		int stateNum =-1;
		int sel =selectLenght();
		if(sel>0){
			if(!rectArr.isEmpty()){
				for(int i=0;i<rectArr.size();i++){
					LogicRect lr =(LogicRect)rectArr.get(i);
					stateNum =lr.getCornerCorner(x, y, sel);
					if(stateNum >=0){
						break;
					}
				}
			}
		}
		return stateNum;
	}
	public void changeRect(int mousState, int offx, int offy){
		if(!rectArr.isEmpty()){
			for(int i=0;i<rectArr.size();i++){
				LogicRect lr =(LogicRect)rectArr.get(i);
				if(lr.isSelected()){
					lr.drag(mousState, offx, offy);
				}
			}
		}
	}
	
	public void moveRect(int offx, int offy){
		if(!rectArr.isEmpty()){
			for(int i=0;i<rectArr.size();i++){
				LogicRect lr =(LogicRect)rectArr.get(i);
				if(lr.isSelected()){
					lr.move(offx, offy);
				}
			}
		}
	}
	
	public void inRect(int left, int top, int width, int height) {
		if(!rectArr.isEmpty()){
			for(int i=0;i<rectArr.size();i++){
				LogicRect lr = (LogicRect)(rectArr.get(i));
				if(lr.inRect(left, top, width, height))
					lr.setSelected(true);
				else
					lr.setSelected(false);
			}
		}
	}
	
	public void chooseRects(int x,int y,boolean isCtr){
		if(isCtr){
			if(!rectArr.isEmpty()){
				for(int i=0;i<rectArr.size();i++){
					LogicRect lr =(LogicRect)rectArr.get(i);
					if(Tools.checkBoxInter(lr.left, lr.top, lr.width, lr.height, x, y, 0, 0)){
						lr.setSelected(!lr.isSelected());
					}
				}
			}
		}else{
			int sl =selectLenght();
			if(sl<2){
				setState(false);
				if(!rectArr.isEmpty()){
					for(int i=0;i<rectArr.size();i++){
						LogicRect lr =(LogicRect)rectArr.get(i);
						if(lr.getCornerCorner(x, y, 1) >=0 ||Tools.checkBoxInter(lr.left, lr.top, lr.width, lr.height, x, y, 0, 0)){
							lr.setSelected(true);
							break;
						}
					}
				}
			}else{
				boolean inFw =false;
				if(!rectArr.isEmpty()){
					for(int i=0;i<rectArr.size();i++){
						LogicRect lr =(LogicRect)rectArr.get(i);
						if(Tools.checkBoxInter(lr.left, lr.top, lr.width, lr.height, x, y, 0, 0)){
							inFw =true;
							break;
						}
					}
					if(!inFw){
						setState(false);
					}
				}
			}
		}
	}
	private int selectLenght(){
		int reslut =0;
		if(!rectArr.isEmpty()){
			for(int i=0;i<rectArr.size();i++){
				LogicRect lr =(LogicRect)rectArr.get(i);
				if(lr.isSelected()){
					reslut ++;
				}
			}
		}
		return reslut;
	}
	public FrameRectArr copyFrameRect(){
		FrameRectArr result =new FrameRectArr();
		result.copyOne(this);
		return result;
	}
	public void copyOne(FrameRectArr ofd){
		if(!ofd.rectArr.isEmpty()){
			for(int i=0;i<ofd.rectArr.size();i++){
				LogicRect lr =(LogicRect)ofd.rectArr.get(i);
				LogicRect newLr =lr.copyLogicRect();
				newLr.setSelected(false);
				this.rectArr.add(newLr);
			}
		}
		
		if(!ofd.copyRectArr.isEmpty()){
			copyRectArr.clear();
			for(int i=0;i<ofd.copyRectArr.size();i++){
				LogicRect lr =(LogicRect)ofd.copyRectArr.get(i);
				this.copyRectArr.add(lr);
			}
		}
	}
	public void save(DataOutputStream out)throws Exception{
		if(!rectArr.isEmpty()){
			out.writeInt(rectArr.size());
			for(int i=0;i<rectArr.size();i++){
				LogicRect lr =(LogicRect)rectArr.get(i);
				lr.save(out);
			}
		}else{
			out.writeInt(0);
		}
	}
	public void load(DataInputStream in) throws Exception{
		rectArr.clear();
		int drawNum =in.readInt();
		if(drawNum >0){
			for(int i=0;i<drawNum;i++){
				LogicRect lr =LogicRect.createViaFile(in);
				rectArr.add(lr);
			}
		}
	}
}

class OneFrameDraw{
	private int offsetX;
	private int offsetY;
	private double offsetScale;
	private double offsetAngle;
	
	private ArrayList objarr;//图片绘画集
	private ArrayList copyArrList;
	
	public ClipPicUndoManager undoManager;
	
	public OneFrameDraw(){
		offsetX = 0;
		offsetY = 0;
		offsetScale = 0;
		offsetAngle = 0;
		
		objarr =new ArrayList();
		copyArrList =new ArrayList();
		undoManager = new ClipPicUndoManager(this);
	}
	public ArrayList getClipArr(){
		return objarr;
	}
	public void export(DataOutputStream out)throws Exception{
		if(!objarr.isEmpty()){
			ClipPic cp;
			int index;
			ArrayList temp = new ArrayList();
			for(int i=0;i<objarr.size();i++){
				cp = (ClipPic)objarr.get(i);
				index = temp.size();
				for(int j=0;j<temp.size();j++){
					if(cp.layer < ((ClipPic)temp.get(j)).layer) {
						index = j;
						break;
					}
				}
				temp.add(index, cp);
			}
			
			out.writeInt(temp.size());
			for(int i=0;i<temp.size();i++){
				((ClipPic)(temp.get(i))).export(out);
			}
		}else{
			out.writeInt(0);
		}
	}
	public void save(DataOutputStream out)throws Exception{
		if(!objarr.isEmpty()){
			out.writeInt(objarr.size());
			for(int i=0;i<objarr.size();i++){
				((ClipPic)(objarr.get(i))).save(out);
			}
		}else{
			out.writeInt(0);
		}
	}
	public void load(DataInputStream in) throws Exception{
		objarr.clear();
		int drawNum =in.readInt();
		if(drawNum >0){
			for(int i=0;i<drawNum;i++){
				ClipPic cp =new ClipPic(null, 1, 1, 1, 1, 1, 1, 1, 1);
				cp.load(in);
				if(cp.image != null)
					objarr.add(cp);
			}
		}
	}
	
	public OneFrameDraw copyFrameDraw(){
		OneFrameDraw result =new OneFrameDraw();
		result.copyFrameDraw(this);
		return result;
	}
	
	public void copyFrameDraw(OneFrameDraw ofd){
		if(!ofd.objarr.isEmpty()){
			for(int i=0;i<ofd.objarr.size();i++){
				ClipPic cp =(ClipPic)ofd.objarr.get(i);
				ClipPic newclip =new ClipPic(cp.image, cp.posX, cp.posY, cp.getGroupID(), cp.getID(), cp.cutx, cp.cuty, cp.cutw, cp.cuth);
				newclip.oldW = cp.oldW;
				newclip.oldH = cp.oldH;
				newclip.scale =cp.scale;
				newclip.jiao =cp.jiao;
				newclip.levelFilp =cp.levelFilp;
				newclip.verticalFilp =cp.verticalFilp;
				newclip.alpha = cp.alpha;
				newclip.logicId = cp.logicId;
				newclip.setLayer(cp.layer);
				newclip.getLoadImg();
				this.objarr.add(newclip);
			}
		}
		
//		if(!ofd.copyArrList.isEmpty()){
//			copyArrList.clear();
//			for(int i=0;i<ofd.copyArrList.size();i++){
//				ClipPic cp =(ClipPic)ofd.copyArrList.get(i);
//				this.copyArrList.add(cp);
//			}
//		}
//		
//		this.undoManager = ofd.undoManager;
	}
	
	private int matchId(int groupId, int id) {
		int logicId = -1;
		Frame frame = MainFrame.self.getAnimPanel().getFrame();
		if(frame != null) {
			ClipPic[] cps = frame.getFrameDraw().getClipPic(groupId, id);
			ClipPic cp = null;
			if(cps != null && cps.length > 0)
				cp = cps[0];
			if(cp == null) {
				boolean is = false;
				frame = MainFrame.self.getAnimPanel().getPreFrame();
				if(frame != null) {
					cps = frame.getFrameDraw().getClipPic(groupId, id);
					if(cps != null && cps.length > 0)
						cp = cps[0];
					if(cp != null){
						is = true;
						logicId = cp.logicId;
						
						Frame[] frames = MainFrame.self.getAnimPanel().fbPanel.getFrames();
						int start = MainFrame.self.getAnimPanel().frameIndex+1;
						for(int i=start; i<frames.length; ++i) {
							cps = frames[i].getFrameDraw().getClipPic(groupId, id);
							if(cps != null && cps.length > 0)
								cp = cps[0];
							if(cp != null) {
								cp.logicId = logicId;
							}
							else {
								break;
							}
						}
					}
				}
				
				if(!is) {
					frame = MainFrame.self.getAnimPanel().getNextFrame();
					if(frame != null) {
						cps = frame.getFrameDraw().getClipPic(groupId, id);
						if(cps != null && cps.length > 0)
							cp = cps[0];
						if(cp != null){
							logicId = cp.logicId;
						}
					}
				}
			}
		}
		return logicId;
	}
	
	public void addClipPic(ClipPic cp) {
		objarr.add(cp);
	}
	
	public void addOneToFrame(Painter p, int[] pos){
		ClipPic cp = ((ClipPicPanel)p).clip;
	//	System.out.println("打印:画"+(-cp.getGroupID())+".png图片中的第"+cp.getID()+"号id的切块到绘画区域");
		ClipPic newclip =new ClipPic(cp.image, pos[0], pos[1], cp.getGroupID(), cp.getID(), cp.cutx, cp.cuty, cp.cutw, cp.cuth);
		newclip.logicId = MainFrame.self.getAnimPanel().getAnim().getCutId();
		
		int logicId = matchId(cp.getGroupID(), cp.getID());
		if(logicId >= 0) {
			newclip.logicId = logicId;
		}
		
		ClipPic painter;
		for(int i=0;objarr !=null&&i<objarr.size();i++){
			painter =(ClipPic)objarr.get(i);
			if(painter instanceof ClipPic){
				((ClipPic)painter).isSelect =false;
			}
		}
		newclip.setLayer(getMaxLayer()+1);
		objarr.add(newclip);
		undoManager.addUndoSpriteAdd(new ClipPic[]{newclip});
	}
	
	/*
	 * 当预切区域做删除操作的时候，相应的编辑区域也要做删除
	 */
	public void delOneTypeClip(int groupId, int sonId){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter.getGroupID() ==groupId &&painter.getID() ==sonId){
					objarr.remove(i);
					break;
				}
			}
		}
	}
	/*
	 * 预区区域图片修改，相应编辑区域改变
	 */
	public void updateClipPic(BufferedImage img, int[] clipArr){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter instanceof ClipPic){
					if(((ClipPic)painter).getGroupID() ==clipArr[4] &&((ClipPic)painter).getID() ==clipArr[5]){
						((ClipPic)painter).updateInfo(img, clipArr);
					}
				}
			}
		}
	}
	
	public ClipPic getClipPic(int index){
		if(!objarr.isEmpty()){
			return (ClipPic)(objarr.get(index));
		}
		
		return null;
	}
	
	public ClipPic[] getClipPic(int groupId, int id) {
		if(!objarr.isEmpty()){
			ArrayList temp = new ArrayList();
			for(int i=0;i<objarr.size();i++){
				ClipPic c =(ClipPic)objarr.get(i);
				if(c.getGroupID() == groupId && c.getID() == id){
					temp.add(c);
				}
			}
			
			ClipPic[] cps = new ClipPic[temp.size()];
			for(int i=0;i<temp.size();i++){
				cps[i] = (ClipPic)temp.get(i);
			}
			return cps;
		}
		
		return null;
	}
	
	public ClipPic getCopyFirst(){
		if(!copyArrList.isEmpty()){
			return (ClipPic)(copyArrList.get(0));
		}
		
		return null;
	}
	
	public void todoCopy(){
		if(!objarr.isEmpty()){
			copyArrList.clear();
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(((ClipPic)painter).isSelect){
					if(painter instanceof ClipPic){
						copyArrList.add((ClipPic)painter);
					}
				}
			}
		}
	}
	public void todoPaste(OneFrameDraw framedraw, int dx, int dy){
		if(!framedraw.copyArrList.isEmpty()){
			allSelectObj(false);
			ClipPic cp;
			for(int i=0;i<framedraw.copyArrList.size();i++){
				cp =(ClipPic)framedraw.copyArrList.get(i);
				ClipPic newclip =new ClipPic(cp.image, cp.posX+dx, cp.posY+dy, cp.getGroupID(), cp.getID(), 
						cp.cutx, cp.cuty, cp.cutw, cp.cuth);
				newclip.logicId = MainFrame.self.getAnimPanel().getAnim().getCutId();
				int logicId = matchId(cp.getGroupID(), cp.getID());
				if(logicId >= 0) {
					newclip.logicId = logicId;
				}
				
				newclip.isSelect =true;
				newclip.oldW = cp.oldW;
				newclip.oldH = cp.oldH;
				newclip.scale =cp.scale;
				newclip.jiao =cp.jiao;
				newclip.levelFilp =cp.levelFilp;
				newclip.verticalFilp =cp.verticalFilp;
				newclip.alpha = cp.alpha;
				newclip.getLoadImg();
				objarr.add(newclip);
				adjustLayer(objarr.size()-1, true);
			}
		//	JOptionPane.showMessageDialog(null, "已经成功复制粘贴"+copyArrList.size()+"个对象", "提示", JOptionPane.INFORMATION_MESSAGE);
		//	copyArrList.clear();
		}
	}
	
	public void allSelectObj(boolean selectState){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter instanceof ClipPic){
					((ClipPic)painter).isSelect =selectState;
				}
			}
		}
	}
	
	// 删除操作
	public void removeObj(){
		if(!objarr.isEmpty()){
			ClipPic painter;
			int nowi = 0;
			int len = objarr.size();
			for(int i=0;i<len;i++){
				painter =(ClipPic)objarr.get(nowi);
				if(painter.isSelect){
					painter.isSelect = false;
					objarr.remove(nowi);
					undoManager.addUndoSpriteRemove(new ClipPic[]{painter});
				}
				else {
					nowi++;
				}
			}
		}
	}
	
	private void removeObj(ClipPic cp){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter == cp){
					cp.isSelect = false;
					objarr.remove(i);
					break;
				}
			}
		}
	}
	
	public void moveClipPic(int w, int h){
		if(!objarr.isEmpty()){
			ClipPic painter;
			ArrayList temp = new ArrayList();
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter.isSelect){
					painter.posX +=w;
					painter.posY +=h;
					temp.add(painter);
				}
			}
			offsetX += w;
			offsetY += h;
			if(w == 0 && h== 0 && (offsetX != 0 || offsetY != 0)) {
				ClipPic[] cps = new ClipPic[temp.size()];
				for(int i=0; i<temp.size(); ++i)
					cps[i] = (ClipPic)temp.get(i);
				undoManager.addUndoSpriteMove(cps, offsetX, offsetY);
				offsetX = 0;
				offsetY = 0;
			}
		}
	}
	
	/**
	 * 
	 * @param type 0.上 1下 2左 3右
	 */
	public void picMove(int type){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter instanceof ClipPic){
					if(((ClipPic)painter).isSelect){
						switch (type) {
						case 0:
							offsetY += -1;
							((ClipPic)painter).posY --;
							break;
						case 1:
							offsetY += 1;
							((ClipPic)painter).posY ++;
							break;
						case 2:
							offsetX += -1;
							((ClipPic)painter).posX --;
							break;
						case 3:
							offsetX += 1;
							((ClipPic)painter).posX ++;
							break;
						default:
							break;
						}
						if(type == 4 && (offsetX != 0 || offsetY != 0)) {
							undoManager.addUndoSpriteMove(new ClipPic[]{painter}, offsetX, offsetY);
							offsetX = 0;
							offsetY = 0;
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param upOrDown  true是UP
	 */
	public void todoMoveUpOrDown(boolean upOrDown){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter.isSelect){
					if(upOrDown){
						painter.setLayer(painter.layer+1);
						adjustLayer(i, upOrDown);
//						if(i+1<objarr.size() && !((ClipPic)objarr.get(i+1)).isSelect){
//							ClipPic tempClip =painter;
//							objarr.set(i, objarr.get(i+1));
//							objarr.set(i+1, tempClip);
//						}
					}else {
						painter.setLayer(painter.layer-1);
						adjustLayer(i, upOrDown);
//						if(i-1>=0 && !((ClipPic)objarr.get(i-1)).isSelect){
//							ClipPic tempClip =painter;
//							objarr.set(i, objarr.get(i-1));
//							objarr.set(i-1, tempClip);
//						}
					}
					break;
				}
			}
		}
	}
	
	private void adjustLayer(int index, boolean isUp) {
		if(!objarr.isEmpty()){
			int now = index;
			int oldNow = now;
			while(now >= 0) {
				ClipPic cp = (ClipPic)objarr.get(now);
				ClipPic cp2;
				now = -1;
				for(int i=0;i<objarr.size();i++){
					if(i != oldNow) {
						cp2 =(ClipPic)objarr.get(i);
						if(cp.layer == cp2.layer && Tools.checkBoxInter(cp.getRect(), cp2.getRect())) {
							if(isUp)
								cp.setLayer(cp.layer+1);
							else
								cp.setLayer(cp.layer-1);
							
							for(int j=0;j<objarr.size();++j){
								if(j != oldNow && cp.layer == ((ClipPic)objarr.get(j)).layer) {
									now = j;
									oldNow = now;
									break;
								}
							}
							break;
						}
					}
				}
			}
		}
	}
	
	public int getMaxLayer() {
		if(!objarr.isEmpty()){
			int max = 0;
			ClipPic cp;
			for(int i=0;i<objarr.size();i++){
				cp = (ClipPic)objarr.get(i);
				if(cp.layer > max) {
					max = cp.layer;
				}
			}
			return max;
		}
		else {
			return 20-1;
		}
	}
	
	/*
	 * 得到旋转的角度
	 */
	public void computeJiao(int ex, int ey, int sx, int sy){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter.isSelect){
					if(ex != 0 || ey != 0 || sx != 0 || sy != 0) {
						int ax =Math.abs(ex -painter.posX);
						int ay =Math.abs(ey -painter.posY);
						double a =Math.sqrt(ax*ax +ay*ay);
						ax =Math.abs(sx -painter.posX);
						ay =Math.abs(sy -painter.posY);
						double b =Math.sqrt(ax*ax +ay*ay);
						ax =Math.abs(sx -ex);
						ay =Math.abs(sy -ey);
						double c =Math.sqrt(ax*ax +ay*ay);
						
						double C = Math.acos((b*b + a*a - c*c)/(2.0*a*b)); 
						C = Math.toDegrees(C);
						C = getRotationJiao(painter, sx, sy, ex, ey, C);
						double oldAngle = painter.jiao;
						painter.jiao += C;
						painter.jiao = painter.jiao%360;
						offsetAngle += painter.jiao - oldAngle;
					}
					else if(offsetAngle != 0) {
						undoManager.addUndoSpriteAngle(new ClipPic[]{painter}, offsetAngle);
						offsetAngle = 0;
					}
//					System.out.println("当前的角度 ="+((ClipPic)painter).jiao);
				}
			}
		}
	}
	//进行角度对比 得到是正旋还是反旋
	private double getRotationJiao(ClipPic cp, int sx, int sy, int ex, int ey, double jiao){
		int spx =sx -cp.posX;
		int spy =sy -cp.posY;
		int epx =ex -cp.posX;
		int epy =ey -cp.posY;
		if(spy <0 &&spx >=0){
			if(epx <0&&epy <0){
				return -jiao;
			}else if(epx >0&&epy >0){
				return jiao;
			}else if(epx >0&&epy <0){
				double tde =(double)epx/(double)epy;
				double tds =(double)spx/(double)spy;
				if(Math.abs(tde) <Math.abs(tds)){
					return -jiao;
				}
			}
		}else if(spy <=0 &&spx <0){
			if(epx >0&&epy <0){
				return jiao;
			}else if(epx <0&&epy >0){
				return -jiao;
			}else if(spy <0 &&spx <0){
				double tde =(double)epy/(double)epx;
				double tds =(double)spy/(double)spx;
				if(Math.abs(tde) <Math.abs(tds)){
					return -jiao;
				}
			}
		}else if(spy >0 &&spx <=0){
			if(epx <0&&epy <0){
				return jiao;
			}else if(epx >0&&epy >0){
				return -jiao;
			}else if(epx <0&&epy >0){
				double tde =(double)epx/(double)epy;
				double tds =(double)spx/(double)spy;
				if(Math.abs(tde) <Math.abs(tds)){
					return -jiao;
				}
			}
		}else if(spy >=0 &&spx >0){
			if(epx <0&&epy >0){
				return jiao;
			}else if(epx >0&&epy <0){
				return -jiao;
			}else if(epx >0&&epy >0){
				double tde =(double)epy/(double)epx;
				double tds =(double)spy/(double)spx;
				if(Math.abs(tde) <Math.abs(tds)){
					return -jiao;
				}
			}
		}
		return jiao;
	}
	
	public void todoFlip(int type){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter instanceof ClipPic){
					if(((ClipPic)painter).isSelect){
						//水平翻转
						if(type ==1){
							painter.setLevelFilp();
						}else if(type ==2){
							painter.setVerticalFilp();
						}
						undoManager.addUndoSpriteFlip(new ClipPic[]{painter}, type);
					}
				}
			}
		}
	}
	/*
	 * type 0计算X方向  1计算Y方向
	 */
	public void changeClipPic(int differNum, int type){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter.isSelect){
					int cc =0;
					double bf =1.0;
					double angle = Math.abs(painter.jiao);
					if(angle>90 && angle<270)
						differNum = -differNum;
					if(differNum <0){
						if(differNum +((ClipPic)painter).scaleImg.getWidth()/2 <=0){
							differNum =-((ClipPic)painter).scaleImg.getWidth()/2 +1;
						}
					}
					if(type ==0){
						cc=2*differNum+((ClipPic)painter).scaleImg.getWidth();
						bf =(double)cc/(double)((ClipPic)painter).scaleImg.getWidth();
					}else{
						cc=2*differNum+((ClipPic)painter).scaleImg.getHeight();
						bf =(double)cc/(double)((ClipPic)painter).scaleImg.getHeight();
					}
					
					double scale =(double)((ClipPic)painter).scaleImg.getWidth()*bf/(double)((ClipPic)painter).oldW;
					if(scale < 0.1)
						scale = 0.1;
					if(scale-painter.scale != 0) {
						offsetScale += scale-painter.scale;
						painter.setScale(scale);
					}
				}
			}
		}
	}
	
	public void changePainterImg(){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter.isSelect){
					if(painter.drawImg.getWidth() != painter.scaleImg.getWidth()){
						painter.scaleImg = painter.drawImg;
						undoManager.addUndoSpriteScale(new ClipPic[]{painter}, offsetScale);
						offsetScale = 0;
						break;
					}
				}
			}
		}
		
	}
	
	public int mousChange(int x, int y){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter.isSelect){
					int resInt =((ClipPic)painter).returnMouState(x, y);
					if(resInt >=0){
						return resInt;
					}
				}
			}
		}
		return -1;
	}
	
	public void todoAlpha(int alpha) {
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter instanceof ClipPic){
					if(((ClipPic)painter).isSelect){
						((ClipPic)painter).alpha = alpha;
					}
				}
			}
		}
	}
	
	public int getAlpha() {
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter instanceof ClipPic){
					if(((ClipPic)painter).isSelect){
						return ((ClipPic)painter).alpha;
					}
				}
			}
		}
		
		return 255;
	}
	
	public boolean isShowPP(int x, int y){
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter.isSelect && painter.isluo(x, y)){
					return true;
				}
			}
		}
		return false;
	}
	
	public String GetInfo(){
		String s="";
		if(!objarr.isEmpty()){
			for(int i=0;i<objarr.size();i++){
				ClipPic cp =(ClipPic)objarr.get(i);
				if(cp.isSelect){
					s = s+cp.getInfo();
					break;
				}
			}
		}
		return s;
	}
	
	public void inRect(int left, int top, int width, int height) {
		if(!objarr.isEmpty()){
			for(int i=0;i<objarr.size();i++){
				ClipPic cp = (ClipPic)(objarr.get(i));
				if(cp.inRect(left, top, width, height))
					cp.isSelect =true;
				else
					cp.isSelect =false;
			}
		}
	}
	
	public ClipPic getSelect() {
		if(!objarr.isEmpty()){
			for(int i=0;i<objarr.size();i++){
				ClipPic cp =(ClipPic)objarr.get(i);
				if(cp.isSelect){
					return cp;
				}
			}
		}
		
		return null;
	}
	
	public boolean returnSelect(int x, int y, boolean isCtrl){
		boolean isFind =false;
		if(!objarr.isEmpty()){
			ClipPic painter;
			for(int i=objarr.size()-1;i>=0;i--){
				
//			}
//			for(int i=0;i<objarr.size();i++){
				painter =(ClipPic)objarr.get(i);
				if(painter instanceof ClipPic){
					//按下ctrl键
					if(isCtrl){
						if(((ClipPic)painter).isluo(x, y)){
							if(!((ClipPic)painter).isSelect){
								((ClipPic)painter).isSelect =true;
								isFind =true;
							}else{
								((ClipPic)painter).isSelect =false;
							}
						}else{
							if(((ClipPic)painter).isSelect){
								isFind =true;
							}
						}
					}else{
						((ClipPic)painter).isSelect =false;
						if(!isFind){
							if(((ClipPic)painter).isluo(x, y)){
								((ClipPic)painter).isSelect =true;
								isFind =true;
							}
						}
					}
				}
			}
		}
		if(isFind){
			return true;
		}
		return false;
	}
	
	public int selectLenght(){
		int reslut =0;
		if(!objarr.isEmpty()){
			for(int i=0;i<objarr.size();i++){
				ClipPic cp =(ClipPic)objarr.get(i);
				if(cp.isSelect){
					reslut ++;
				}
			}
		}
		return reslut;
	}
	
	public void paint(Graphics g, float alpha){
		if(!objarr.isEmpty()){		    
			ClipPic cp;
			int index;
			ArrayList temp = new ArrayList();
			for(int i=0;i<objarr.size();i++){
				cp = (ClipPic)objarr.get(i);
				index = temp.size();
				for(int j=0;j<temp.size();j++){
					if(cp.layer < ((ClipPic)temp.get(j)).layer) {
						index = j;
						break;
					}
				}
				temp.add(index, cp);
			}
			
			for(int i=0;i<temp.size();i++){
				cp = (ClipPic)temp.get(i);
				if(cp !=null){
					cp.paint(g, true, alpha);
				}
			}
		}
	}
	
	public void redoAdd(ClipPic[] sprites) {
		// TODO Auto-generated method stub
		for(int i=0; i<sprites.length; ++i) {
			addClipPic(sprites[i]);
		}
	}
	public void undoAdd(ClipPic[] sprites) {
		// TODO Auto-generated method stub
		for(int i=0; i<sprites.length; ++i) {
			removeObj(sprites[i]);
		}
	}
	public void flipSprites(ClipPic[] sprites, int type) {
		// TODO Auto-generated method stub
		for(int i=0; i<sprites.length; ++i) {
			if(type == 1)
				sprites[i].setLevelFilp();
			else if(type == 2)
				sprites[i].setVerticalFilp();
		}
	}
	public void redoMove(ClipPic[] sprites, int offsetX, int offsetY) {
		// TODO Auto-generated method stub
		for(int i=0; i<sprites.length; ++i) {
			sprites[i].posX += offsetX;
			sprites[i].posY += offsetY;
		}
	}
	public void undoMove(ClipPic[] sprites, int offsetX, int offsetY) {
		// TODO Auto-generated method stub
		for(int i=0; i<sprites.length; ++i) {
			sprites[i].posX -= offsetX;
			sprites[i].posY -= offsetY;
		}
	}
	public void redoRemove(ClipPic[] sprites) {
		// TODO Auto-generated method stub
		for(int i=0; i<sprites.length; ++i) {
			removeObj(sprites[i]);
		}
	}
	public void undoRemove(ClipPic[] sprites) {
		// TODO Auto-generated method stub
		for(int i=0; i<sprites.length; ++i) {
			addClipPic(sprites[i]);
		}
	}
	public void scaleSprites(ClipPic[] sprites, double offsetScale) {
		// TODO Auto-generated method stub
		for(int i=0; i<sprites.length; ++i) {
			sprites[i].setScale(sprites[i].scale+offsetScale);
			sprites[i].scaleImg = sprites[i].drawImg;
		}
	}
	public void angleSprites(ClipPic[] sprites, double offsetAngle) {
		// TODO Auto-generated method stub
		for(int i=0; i<sprites.length; ++i) {
			sprites[i].jiao += offsetAngle;
		}
	}
	public void alphaSprites(ClipPic[] sprites, double offsetAlpha) {
		// TODO Auto-generated method stub
		for(int i=0; i<sprites.length; ++i) {
			sprites[i].alpha += offsetAlpha;
		}
	}
	
}
