package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;


public class Frame implements Copyable, Saveable {

	public final static String[] POINT_LUA_NAMES = { 
			"p1", "p2", 
			"p3", "p4", 
			"p5", "p6", 
			"p7", "p8", 
			"p9", "p10",
			"p11", "p12", 
			"p13", "p14", 
			"p15", "p16", 
			
			"p17", "p18", 
			"p19", "p20", 
			"p21", "p22", 
			"p23", "p24", 
			"p25", "p26",
			"p27", "p28", 
			"p29", "p30", 
			"p31", "p32", 
			
			"p33", "p34", 
			"p35", "p36", 
			"p37", "p38", 
			"p39", "p40", 
			"p41", "p42",
			"p43", "p44", 
			"p45", "p46", 
			"p47", "p48",  
	};


	public final static String[] POINT_NAMES = { 
			"发射点1", "发射点2", 
			"发射点3", "发射点4", 
			"发射点5", "发射点6", 
			"发射点7", "发射点8", 
			"发射点9", "发射点10",
			"发射点11", "发射点12", 
			"发射点13", "发射点14", 
			"发射点15", "发射点16", 
			
			"发射点17", "发射点18", 
			"发射点19", "发射点20", 
			"发射点21", "发射点22", 
			"发射点23", "发射点24", 
			"发射点25", "发射点26",
			"发射点27", "发射点28", 
			"发射点29", "发射点30", 
			"发射点31", "发射点32", 
			
			"发射点33", "发射点34", 
			"发射点35", "发射点36", 
			"发射点37", "发射点38", 
			"发射点39", "发射点40", 
			"发射点41", "发射点42",
			"发射点43", "发射点44", 
			"发射点45", "发射点46", 
			"发射点47", "发射点48",  
	};

	public final static Color[] POINT_COLORS = { 
			Color.RED, Color.RED,
			new Color(0xFF880000), new Color(0xFF880000),
			Color.BLUE, Color.BLUE,
			new Color(0xFF000088), new Color(0xFF000088),  
			Color.GREEN, Color.GREEN,
			new Color(0xFF008800), new Color(0xFF008800),
			new Color(0xFFFFFF00), new Color(0xFFFFFF00), 
			new Color(0xFF00FFFF), new Color(0xFF00FFFF), 
			
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
			new Color(0xFF880088), new Color(0xFF880088), 
	};
	/*
	public final static String[] RECT_LUA_NAMES = { 
			"bb", "ab", 
			"bw", 
			"bc", "ac", "acp",
	        "bdv", "bgn", 
	        "bv", 
	        "aes", 
	        "aeb", "aebp", 
	        "bla", "bra",
	        "bcc"
	};
	
	
	public final static String[] RECT_NAMES = {
			"被子弹攻击范围", "子弹攻击范围", 
			"行走碰撞范围", 
			"被刀攻击范围", "刀攻击范围", "出刀攻击判定", 
			"驾驶员", "机枪手", 
			"判断是否出屏幕", 
			"小爆炸攻击范围", 
			"大爆炸攻击范围", "大爆炸攻击判定", 
			"左机械臂", "右机械臂", 
			"中间大炮"
	};
	*/
	public final static String[] RECT_LUA_NAMES = { 
		"bar",  // be attacked rect
		"ar",	// attack rect
		"fzr", 	// forbidden zone
		"scr",	// in or out screen check
		"mr",	// foot range
	};
	public final static String[] RECT_NAMES = {
		"被攻击范围",
		"攻击范围",
		"障碍范围",
		"范围检测矩形",
		"行走碰撞范围"
	};
	
	public final static Color[] RECT_COLORS = {
			Color.BLUE, new Color(0xFF800000), 
			new Color(0xFF888888), 
			Color.GREEN, Color.RED, new Color(0xFFFF6666), 
			new Color(0xFF888800), new Color(0xFF008888), 
			new Color(0xFFAA00AA), 
			new Color(0xFFAA0000), 
			new Color(0xFFFF0000), new Color(0xFFFF6666),
			new Color(0xFF2222BB), new Color(0xFF2222BB),
			new Color(0xFF2222BB),
	};

	public final static Frame[] copyArray(Frame[] array) {
		Frame[] result = null;
		if (array != null) {
			result = new Frame[array.length];
			for (int i = 0; i < array.length; ++i) {
				result[i] = array[i].copyFrame(true);
			}
		}
		return result;
	}

	public final static Frame[] createArrayViaFile(DataInputStream in, SIManager siManager) throws Exception {
		Frame[] result = null;
		int length = in.readInt();
		if (length > 0) {
			result = new Frame[length];
			for (int i = 0; i < length; ++i) {
				result[i] = Frame.createViaFile(in, siManager);
			}
		}
		return result;
	}

	public final static Frame createViaFile(DataInputStream in, SIManager siManager)
	        throws Exception {
		Frame result = new Frame();
		result.load(in, siManager);
		return result;
	}

	public final static int MOVE_TYPES[] = { 0, 1, 2, 3 };
	public final static String MOVE_TYPE_NAMES[] = { "地面水平移动", "地面跃到空中", "空中任意移动", "从空中落地" };

	public final static int DAMAGE_TYPES[] = { 0, 1, 2 };
	public final static String DAMAGE_TYPE_NAMES[] = { "轻伤", "重伤", "击倒" };

	private SISprite[] sprites;
//	private LogicRect[] rects;
	private OneFrameDraw framedraw;
	private FrameRectArr framerect;
	private FramePointArr framepoint;

	private int interval;//延迟
	private boolean special;//特殊桢
	private int offsetX;//X偏差
	private int offsetY;//Y偏差
	private int moveType;//移动类型
	private int damageType;//攻击类型
	private int hitDist;//击退距离
	private int damage;//伤害值
	private boolean shadow;//是否有拖影
	private IntPair specialPoint;
	private int soundID;

	public Frame() {
		framerect =new FrameRectArr();
		framedraw =new OneFrameDraw();
		framepoint = new FramePointArr();
		sprites = null;
		interval = 100;
		special = false;
		offsetX = 0;
		offsetY = 0;
		moveType = 0;
		damageType = 0;
		hitDist = 0;
		damage = 0;
		shadow = false;
		specialPoint = new IntPair();
		soundID = 0;
	}

	public Copyable copy() {
		return copyFrame(true);
	}

	public final Frame copyFrame(boolean isCopy) {
		Frame result = new Frame();

		result.copyFrom(this, isCopy);

		return result;
	}

	public void copyFrom(Frame source, boolean isCopy) {
//		this.setSprites(source.sprites);
		if(isCopy) {
			this.framerect =(source.framerect.copyFrameRect());
			this.framedraw =(source.framedraw.copyFrameDraw());
			this.framepoint =(source.framepoint.copyFramePoint());
		}
		else 
		{
			this.framerect =(source.framerect.copyFrameRect());
			this.framedraw =(source.framedraw);
			this.framepoint =(source.framepoint.copyFramePoint());
		}
		this.interval = source.interval;
		this.special = source.special;
		this.offsetX = source.offsetX;
		this.offsetY = source.offsetY;
		this.moveType = source.moveType;
		this.damageType = source.damageType;
		this.hitDist = source.hitDist;
		this.damage = source.damage;
		this.shadow = source.shadow;
		this.specialPoint.copyFrom(source.specialPoint);
		this.soundID = source.soundID;
	}

	public int getInterval() {
		return this.interval;
	}

	public SISprite[] getSprites() {
		return sprites;
	}

	public OneFrameDraw getFrameDraw(){
		return framedraw;
	}
	
	public FrameRectArr getFrameRect(){
		return framerect;
	}
	
	public FramePointArr getFramePoint(){
		return framepoint;
	}
	
	public boolean isSpecial() {
		return special;
	}

	public void load(DataInputStream in, SIManager siManager) throws Exception {
		interval = in.readInt();
		special = in.readBoolean();
		offsetX = in.readInt();
		offsetY = in.readInt();
		moveType = in.readInt();
		damageType = in.readInt();
		hitDist = in.readInt();
		damage = in.readInt();
		shadow = in.readBoolean();
		specialPoint = IntPair.createViaFile(in);
		soundID = in.readInt();
		framedraw.load(in);
		framerect.load(in);
		framepoint.load(in);
	}
	public void export(DataOutputStream out) throws Exception {
		out.writeInt(interval);
		out.writeBoolean(special);
		out.writeInt(offsetX);
		out.writeInt(offsetY);
		out.writeInt(moveType);
		out.writeInt(damageType);
		out.writeInt(hitDist);
		out.writeInt(damage);
		out.writeBoolean(shadow);
		out.writeInt(soundID);
		framedraw.export(out);
	//	framerect.save(out);
	//	framepoint.save(out);
	}

	public void save(DataOutputStream out) throws Exception {
		out.writeInt(interval);
		out.writeBoolean(special);
		out.writeInt(offsetX);
		out.writeInt(offsetY);
		out.writeInt(moveType);
		out.writeInt(damageType);
		out.writeInt(hitDist);
		out.writeInt(damage);
		out.writeBoolean(shadow);
		specialPoint.save(out);
		out.writeInt(soundID);
		framedraw.save(out);
		framerect.save(out);
		framepoint.save(out);
	}
	
	public void saveMobile(DataOutputStream out) throws Exception {
		SL.writeShort(interval, out);
		if (sprites == null) {
			SL.writeByte(0, out);
		}
		else {
			SL.writeByte(sprites.length, out);
			for (int i = 0; i < sprites.length; ++i) {
				sprites[i].saveMobile(out);
			}
		}
	}

	public int hasMultiRectsWithSameType() {
		int[] counts = new int[RECT_LUA_NAMES.length];
		for (int i = 0; i < counts.length; ++i) {
			counts[i] = 0;
		}
		return -1;
	}
	
	final static int weaponImgId = 8000;
	public void saveWeaponLua(StringBuffer s)
	{
		ArrayList sps = new ArrayList();
		ArrayList pts = new ArrayList();

		if (sprites != null) {
			for (int i = 0; i < sprites.length; ++i) {
				SISprite sp = sprites[i];
				if (sp.getSI().getID() >= 0) {
					// 图片
					sps.add(sp);
				}
				else {
					// 点
					pts.add(sp);
				}
			}
		}
		
		// 记录基本图的左上角坐标(相对于基准边)
		int baseImgX = 0;
		int baseImgY = 0;
		
		// 武器图片的id从8000开始
		for (int i = 0; i < sps.size(); ++i) 
		{
			// 找到基本图(id小于5000的图被认为是基本图)
			SISprite sprite = (SISprite)sps.get(i);
			if (null == sprite) continue;
			
			// 默认只能有一张基本图，所以找到后记录然后退出循环
			if (sprite.getSI().getID() < weaponImgId) 
			{
				baseImgX = sprite.getX() - FrameManager.ORIGIN_X;
				baseImgY = sprite.getY() - FrameManager.ORIGIN_Y;
				
				s.append("[");
				s.append(sprite.getSI().getID());
				s.append("]=");
				break;
			}
		}
		
		// 图片信息
		s.append("{");		
		s.append("s={");
		int weaponImgCount = 0;
		for (int i = 0; i < sps.size(); ++i) 
		{
			// 找到基本图(id小于3000的图被认为是基本图)
			SISprite sprite = (SISprite)sps.get(i);
			
			if (null == sprite) continue;
			
			// 默认只能有一张基本图，所以找到后记录然后退出循环
			if (sprite.getSI().getID() >= weaponImgId) 
			{
				if (weaponImgCount > 0) s.append(",");
				
				int id = sprite.getSI().getID();
	
				int x = sprite.getX() - FrameManager.ORIGIN_X;
				x -= baseImgX;
				int y = sprite.getY() - FrameManager.ORIGIN_Y;
				y -= baseImgY;
				s.append("{i=" + id + ",x=" + x + ",y=" + y + (sprite.isFlip() ? ",f=1" : "") + "}");
				weaponImgCount++;
			}
		}
		if (weaponImgCount > 0) s.append(",c="+weaponImgCount);
		s.append("}");
		
		s.append("}");
	}
	
	public boolean isSaveLua() {
		boolean isSave = true;
		if(framerect.getArr().size() <= 0 && framepoint.getArr().size() <= 0)
			isSave = false;
		
		return isSave;
	}

	public void saveLua(StringBuffer s) {

//		ArrayList sps = new ArrayList();
//		ArrayList pts = new ArrayList();
//
//		if (sprites != null) {
//			for (int i = 0; i < sprites.length; ++i) {
//				SISprite sp = sprites[i];
//				if (sp.getSI().getID() >= 0) {
//					sps.add(sp);
//				}
//				else {
//					pts.add(sp);
//				}
//			}
//		}
		
		// 间隔
	//	s.append("{i=" + interval + ",");
		
		// 左右位移
		if (0 != offsetX)
		{
			s.append("ox=" + offsetX + ",");
		}
		
		// 击退距离
		if (0 != hitDist)
		{
			s.append("hd=" + hitDist + ",");
		}
		
		// 伤害类型
		if (0 != damageType)
		{
			s.append("dt=" + damageType + ",");
		}
		
		// 伤害量
		if (0 != damage)
		{
			s.append("dm=" + damage + ",");
		}
		
		// 移动类型
		if (0 != moveType)
		{
			s.append("mt=" + moveType +",");
		}
		
		// 拖影
		if (shadow) 
		{
			s.append("sd=1,");
		}
		
		if (special)
		{
			s.append("spec=1,");
		}
		
//		if (sps.isEmpty()) {
//			s.append("s={c=0}");
//		}
//		else {
//			s.append("s={c=" + sps.size() + ",");
//			for (int i = 0; i < sps.size(); ++i) {
//				((SISprite) (sps.get(i))).saveLua(s);
//				if (i != sps.size() - 1) s.append(",");
//			}
//			s.append("}");
//		}

		// 保存挂接点
		/*
		if (!pts.isEmpty()) {
			s.append(",");
			for (int i = 0; i < pts.size(); ++i) {
				SISprite sp = (SISprite) (pts.get(i));
				PointSI psi = (PointSI) (sp.getSI());
				s.append(psi.getLuaName() + "=");
				sp.saveLua(s);
				if (i != pts.size() - 1) s.append(",");
			}
		}
		*/
//		if (!pts.isEmpty())
//		{
//			s.append(",pts={c=");
//			s.append(pts.size());
//			s.append(",");
//			for (int i = 0; i < pts.size(); ++i) 
//			{
//				SISprite sp = (SISprite) (pts.get(i));
//				PointSI psi = (PointSI) (sp.getSI());
//				int idx = i + 1;
//				s.append("[" + idx  + "]=");
//				sp.saveLua(s);
//				if (i != pts.size() - 1) s.append(",");
//			}
//			s.append("}");
//		}
//		s.append("}");
		
		ArrayList list = framerect.getArr();
		if(list.size() > 0){
			s.append("rts={");
			for(int i=0; i<list.size(); ++i){
				LogicRect rect = (LogicRect)list.get(i);
				int index = i+1;
				int l = rect.left;
				int t = rect.top;
				int r = l + rect.width;
				int b = t + rect.height;
				s.append("["+index+"]={");
				s.append("l="+l);
				s.append(",r="+r);
				s.append(",t="+t);
				s.append(",b="+b);
				s.append("}");
				if (i != list.size() - 1) 
					s.append(",");
			}
			s.append("}");
		}
		
		ArrayList list2 = framepoint.getArr();
		if(list.size() > 0 && list2.size() > 0){ // 有碰撞矩形
			s.append(",");
		}
		
		if(list2.size() > 0){
			s.append("pts={");
			for(int i=0; i<list2.size(); ++i){
				LogicPoint point = (LogicPoint)list2.get(i);
				int x = point.x;
				int y = point.y;
				int index = point.getType() + 1;
				s.append("["+index+"]={");
				s.append("x="+x);
				s.append(",y="+y);
				s.append("}");
				if (i != list2.size() - 1) 
					s.append(",");
			}
			s.append("}");
		}
	}

	public void setInterval(int aInterval) {
		this.interval = aInterval;
	}

	public void setSpecial(boolean aSpecial) {
		this.special = aSpecial;
	}

	public void setSprites(SISprite[] aSprites) {
		this.sprites = SISprite.copyArray(aSprites);
	}

	public int getOffsetX() {
		return this.offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return this.offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public int getMoveType() {
		return this.moveType;
	}

	public void setMoveType(int moveType) {
		this.moveType = moveType;
	}

	public int getDamageType() {
		return this.damageType;
	}

	public void setDamageType(int damageType) {
		this.damageType = damageType;
	}

	public int getHitDist() {
		return this.hitDist;
	}

	public void setHitDist(int hitDist) {
		this.hitDist = hitDist;
	}

	public int getDamage() {
		return this.damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public IntPair getSpecialPoint() {
		return this.specialPoint;
	}

	public void setSpecialPoint(IntPair specialPoint) {
		if (specialPoint == null) {
			this.specialPoint = new IntPair();
		}
		else {
			this.specialPoint.copyFrom(specialPoint);
		}
	}

	public boolean isShadow() {
		return this.shadow;
	}

	public void setShadow(boolean shadow) {
		this.shadow = shadow;
	}

	public int getSoundID() {
		return this.soundID;
	}

	public void setSoundID(int soundID) {
		this.soundID = soundID;
	}
}

class FrameButtonPanel extends JPanel {

	private class FBPanel extends JPanel {

		public FBPanel() {
			super();
			addMouseListener(new MouseAdapter() {

				public void mouseReleased(MouseEvent e) {
					selfMouseReleased(e);
				}
			});
		}

		private void mouseLeftReleased(MouseEvent e) {
			if (frameButtons.isEmpty() || playing) return;

			int index = e.getX() / BUTTON_BG_WIDTH;
			if (index < 0) index = 0;
			if (index >= frameButtons.size()) index = frameButtons.size() - 1;

			if (e.isShiftDown()) {
				int min = Math.min(index, currentIndex);
				int max = Math.max(index, currentIndex);
				for (int i = 0; i < frameButtons.size(); ++i) {
					FrameButton fb = (FrameButton) (frameButtons.get(i));
					if (i >= min && i <= max) {
						fb.setSelected(true);
					}
					else {
						fb.setSelected(false);
					}
				}
				fbPanel.repaint();
			}
			else if (e.isControlDown()) {
				if (index != currentIndex) {
					FrameButton fb = (FrameButton) (frameButtons.get(index));
					fb.setSelected(!fb.isSelected());
					fbPanel.repaint();
				}
			}
			else {
				if (index != currentIndex) {
					updatedFromPanel();
					select(index);
				}
			}
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (!frameButtons.isEmpty()) {
				for (int i = 0; i < frameButtons.size(); ++i) {
					FrameButton fb = (FrameButton) (frameButtons.get(i));
					fb.paint(g, i);
				}
			}
		}

		private void selfMouseReleased(MouseEvent e) {
			switch (e.getButton()) {
			case XUtil.LEFT_BUTTON:
				mouseLeftReleased(e);
				break;
			}
		}
	}
	
	private class FrameButton {

		private boolean selected;
		private Frame frame;
		private Frame framePre;
		

		public FrameButton(Frame aFrame, Frame framePre) {
			this.frame = aFrame;
			this.framePre = framePre;
			this.selected = false;
		}
		
		public Frame getFrame() {
			return frame;
		}
		
		public Frame getFramePre() {
			return framePre;
		}

		public boolean isEmpty() {
			boolean result = true;
			SISprite[] sprites = frame.getSprites();
			if (sprites != null) {
				if (sprites.length > 0) {
					result = false;
				}
			}
			return result;
		}

		public boolean isSelected() {
			return selected;
		}

		public void paint(Graphics g, int index) {
			int x = BUTTON_BG_WIDTH * index;
			int y = (BUTTON_PANEL_HEIGHT - BUTTON_BG_HEIGHT) / 2;
			Color oldColor = g.getColor();
			if (selected) {
				g.setColor(BGCOLOR);
				g.fillRect(x, y, BUTTON_BG_WIDTH, BUTTON_BG_HEIGHT);
			}
			x += (BUTTON_BG_WIDTH - BUTTON_WIDTH) / 2;
			y += (BUTTON_BG_HEIGHT - BUTTON_HEIGHT) / 2;
			if (selected) {
				//g.setColor(Color.WHITE);
				g.setColor(new Color(0xFF888888));
				g.fillRect(x - 2, y - 2, BUTTON_WIDTH + 4, BUTTON_HEIGHT + 4);
				g.setColor(Color.BLACK);
				g.fillRect(x - 1, y - 1, BUTTON_WIDTH + 2, BUTTON_HEIGHT + 2);
			}
			g.setColor(frameManager.getColor());
			g.fillRect(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);

			Graphics2D g2 = (Graphics2D) g;
			AffineTransform oldTrans = new AffineTransform(g2.getTransform());
			g2.translate(x, y);
			g2.scale(SCALEX, SCALEY);
			
//			g2.setClip(0, 0, 240, 320);
			g2.translate(-FrameManager.REAL_LEFT, -FrameManager.REAL_TOP);
			if (index == currentIndex) {
				frameManager.paintStatic(g);
			}
			else {
				SISprite[] sprites = frame.getSprites();
				if (sprites != null) {
					for (int i = 0; i < sprites.length; ++i) {
						SISprite sprite = sprites[i];
						sprite.paintIdle(g);
					}
				}
			}
			g2.translate(AniEdit.drawOffSetX, AniEdit.drawOffSetY);
			if(frame.getFrameDraw() !=null){
				frame.getFrameDraw().paint(g, 1);
			}
			
			g2.setTransform(oldTrans);

			
			g.setColor(Color.BLACK);
			g.drawString((index + 1) + "", x, y + 12);
			g.setColor(oldColor);
		}

		public void setSelected(boolean aSelected) {
			this.selected = aSelected;
		}

		public void updataPanel() {
			animPanel.setFrame(frame, framePre);
			frameManager.reset();
			SISprite[] sprites = SISprite.copyArray(frame.getSprites());
			if (sprites != null) {
				for (int i = 0; i < sprites.length; ++i) {
					frameManager.addSprite(sprites[i]);
				}
			}
			frameManager.getScrollablePanel().repaint();
			frameManager.setSpecialPoint(frame.getSpecialPoint());

			frameRectManager.reset();
			/*LogicRect[] rects = LogicRect.copyArray(frame.getRects());
			if (rects != null) {
				for (int i = 0; i < rects.length; ++i) {
					frameRectManager.addShape(rects[i]);
				}
			}*/
			frameRectManager.getScrollablePanel().repaint();
		}

		public void updatedFromPanel() {
			animPanel.updateFrame(frame);
			frame.setSpecialPoint(frameManager.getSpecialPoint());

			frameManager.resortSprites();
			Sprite[] ss = frameManager.getSprites();
			SISprite[] sprites = null;
			if (ss != null) {
				if (ss.length > 0) {
					sprites = new SISprite[ss.length];
					for (int i = 0; i < ss.length; ++i) {
						Sprite s = ss[i];
						sprites[i] = (SISprite) s;
					}
				}
			}
			frame.setSprites(sprites);

			/*Shape[] ss2 = frameRectManager.getShapes();
			LogicRect[] rects = null;
			if (ss2 != null) {
				if (ss2.length > 0) {
					rects = new LogicRect[ss2.length];
					for (int i = 0; i < ss2.length; ++i) {
						Shape s2 = ss2[i];
						rects[i] = (LogicRect) s2;
					}
				}
			}
			frame.setRects(rects);*/
		}
	}

	public final static int BUTTON_WIDTH = 100;
	public final static int BUTTON_HEIGHT = 100;
	public final static int BUTTON_BG_WIDTH = 110;
	public final static int BUTTON_BG_HEIGHT = 110;
	public final static int BUTTON_PANEL_HEIGHT = 114;
	public static double SCALEX = (BUTTON_WIDTH * 1.0) / FrameManager.REAL_WIDTH;
	public static double SCALEY = (BUTTON_HEIGHT * 1.0) / FrameManager.REAL_HEIGHT;

	public final static int TRANSLATEX = FrameManager.BASIC_WIDTH - FrameManager.REAL_WIDTH;

	public final static int TRANSLATEY = FrameManager.BASIC_HEIGHT - FrameManager.REAL_HEIGHT;

	public final static Color BGCOLOR = new Color(0x316AC5);

	private FrameManager frameManager;
	private FrameRectManager frameRectManager;
	private ArrayList frameButtons;
	private ArrayList copiedFrames;
	private ArrayList<Frame> playFrames;
	
	private int currentIndex;
	private FBPanel fbPanel;
	private JScrollPane scrollPane;
	private boolean playing;
	private JButton playButton;
	private AnimPanel animPanel;
	private ManagerListener managerListener;

	public FrameButtonPanel(AnimPanel aAnimPanel) {
		this.animPanel = aAnimPanel;
		this.frameManager = animPanel.getFrameManager();
		this.frameRectManager = animPanel.getFrameRectManager();
		frameButtons = new ArrayList();
		copiedFrames = new ArrayList();
		playFrames = new ArrayList();
		fbPanel = new FBPanel();
		resizeFBPanel();
		scrollPane = new JScrollPane(fbPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// JPanel scrollPanel = new JPanel();
		// scrollPanel.setPreferredSize(new Dimension(1, BUTTON_PANEL_HEIGHT +
		// 20));
		// scrollPanel.setLayout(new BorderLayout());
		// scrollPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout());

		playButton = new JButton("播放");
		playButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (playing) {
					stopPlay();
				}
				else {
					startPlay();
				}
			}
		});
		buttonPanel.add(playButton);

		JButton b;
		b = new JButton("添加（左）");
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (playing) return;
				IntPair p = analyseIndex();
				add(p.x);
			}
		});
		buttonPanel.add(b);

		b = new JButton("添加（右）");
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (playing) return;
				IntPair p = analyseIndex();
				add(p.y);
			}
		});
		buttonPanel.add(b);

		b = new JButton("删除");
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (playing) return;
				remove();
			}
		});
		buttonPanel.add(b);

		b = new JButton("复制");
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (playing) return;
				copy();
			}
		});
		buttonPanel.add(b);

		b = new JButton("粘贴（左）");
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (playing) return;
				IntPair p = analyseIndex();
				paste(p.x);
			}
		});
		buttonPanel.add(b);

		b = new JButton("粘贴（右）");
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (playing) return;
				IntPair p = analyseIndex();
				paste(p.y);
			}
		});
		buttonPanel.add(b);

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		managerListener = new ManagerAdapter() {

			public void spriteChanged() {
				fbPanel.repaint();//重绘
			}
		};
		frameManager.addListener(managerListener);
	}

	public void updatefbPanel(){
		fbPanel.repaint();//重绘
	}
	public void delEditClipInfo(int groupId, int sonId){
		if(!frameButtons.isEmpty()){
			for(int i=0;i<frameButtons.size();i++){
				FrameButton fb = (FrameButton) (frameButtons.get(i));
				OneFrameDraw ofd =fb.frame.getFrameDraw();
				ofd.delOneTypeClip(groupId, sonId);
			}
		}
	}
	public void updateClipInfo(BufferedImage img, int[] clipArr){
		if(!frameButtons.isEmpty()){
			for(int i=0;i<frameButtons.size();i++){
				FrameButton fb = (FrameButton) (frameButtons.get(i));
				OneFrameDraw ofd =fb.frame.getFrameDraw();
				ofd.updateClipPic(img, clipArr);
			}
		}
	}
	public int getIndex(){
		return currentIndex;
	}
	private void add(int aIndex) {
		updatedFromPanel();

		int index = aIndex;
		if (frameButtons.isEmpty())
			index = 0;
		else if (index < 0)
			index = 0;
		else if (index >= frameButtons.size()) index = frameButtons.size();
		
		Frame framePre = null;
		if(index > 0) {
			FrameButton fb = (FrameButton) (frameButtons.get(index-1));
			framePre = fb.frame;
		}
		FrameButton newFB = new FrameButton(new Frame(), framePre);
		frameButtons.add(index, newFB);
		resizeFBPanel();
		select(index);
	}

	private IntPair analyseIndex() {
		int min = 0;
		int max = frameButtons.isEmpty() ? 0 : frameButtons.size();
		boolean hasSelected = false;
		if (!frameButtons.isEmpty()) {
			for (int i = 0; i < frameButtons.size(); ++i) {
				FrameButton fb = (FrameButton) (frameButtons.get(i));
				if (fb.isSelected()) {
					if (hasSelected) {// 之前已经找到有选中的帧了
						max = i + 1;
					}
					else {// 之前还没有找到有选中的帧
						hasSelected = true;
						min = i;
						max = i + 1;
					}
				}
			}
		}
		return new IntPair(min, max);
	}

	public void applyInterval(int aInterval) {
		for (int i = 0; i < frameButtons.size(); ++i) {
			FrameButton fb = (FrameButton) (frameButtons.get(i));
			if (fb.isSelected()) {
				fb.getFrame().setInterval(aInterval);
			}
		}
	}

	private void copy() {
		updatedFromPanel();

		if (!frameButtons.isEmpty()) {
			copiedFrames.clear();
			for (int i = 0; i < frameButtons.size(); ++i) {
				FrameButton fb = (FrameButton) (frameButtons.get(i));
				if (fb.isSelected()) {
					copiedFrames.add(fb.getFrame().copyFrame(true));
				}
			}
		}
	}

	public Frame[] getFrames() {
		updatedFromPanel();
		Frame[] result = null;
		if (!frameButtons.isEmpty()) {
			result = new Frame[frameButtons.size()];
			for (int i = 0; i < frameButtons.size(); ++i) {
				FrameButton fb = (FrameButton) (frameButtons.get(i));
				result[i] = fb.getFrame();
			}
		}
		return result;
	}

	private void paste(int aIndex) {
		if (copiedFrames.isEmpty()) return;

		updatedFromPanel();

		int index = aIndex;
		if (frameButtons.isEmpty())
			index = 0;
		else if (index < 0)
			index = 0;
		else if (index >= frameButtons.size()) index = frameButtons.size();

		if (!frameButtons.isEmpty()) {
			for (int i = 0; i < frameButtons.size(); ++i) {
				FrameButton fb = (FrameButton) (frameButtons.get(i));
				fb.setSelected(false);
			}
		}
		for (int i = 0; i < copiedFrames.size(); ++i) {
			Frame framePre = null;
			if(index+i > 0) {
				FrameButton fb = (FrameButton) (frameButtons.get(index+i-1));
				framePre = fb.frame;
			}
			Frame frame = (Frame) (copiedFrames.get(i));
			FrameButton newFB = new FrameButton(frame.copyFrame(true), framePre);
			frameButtons.add(index + i, newFB);
			newFB.setSelected(true);
			if (i == 0) {
				currentIndex = index;
				updatePanel();
			}
		}
		resizeFBPanel();
		scrollTo(currentIndex);
	}

	private void remove() {
		int i = 0;
		while (!frameButtons.isEmpty() && i < frameButtons.size()) {
			FrameButton fb = (FrameButton) (frameButtons.get(i));
			if (fb.isSelected()) {
				Frame framePre = null;
				if(i > 0) {
					FrameButton fb2 = (FrameButton) (frameButtons.get(i-1));
					framePre = fb2.frame;
				}
				if(i+1 < frameButtons.size()) {
					FrameButton fb3 = (FrameButton) (frameButtons.get(i+1));
					fb3.framePre = framePre;
				}
				frameButtons.remove(i);
			}
			else {
				++i;
			}
		}
		resizeFBPanel();
		int index = currentIndex;
		if (index >= frameButtons.size()) index = frameButtons.size() - 1;
		select(index);
	}

	private void resizeFBPanel() {
		int width = frameButtons.size() * BUTTON_BG_WIDTH;
		if (width <= 0) width = 1;
		fbPanel.setPreferredSize(new Dimension(width, BUTTON_PANEL_HEIGHT));
		fbPanel.repaint();
		fbPanel.revalidate();
	}

	private void scrollTo(int aIndex) {
		if (!frameButtons.isEmpty()) {
			if (aIndex >= 0 && aIndex < frameButtons.size()) {
				fbPanel.scrollRectToVisible(new Rectangle(BUTTON_BG_WIDTH * aIndex, 0,
				        BUTTON_BG_WIDTH, BUTTON_BG_HEIGHT));
			}
		}
	}

	private void select(int aIndex) {
		select(aIndex, true);
	}

	private void select(int aIndex, boolean aScroll) {
		currentIndex = -1;
		if (!frameButtons.isEmpty()) {
			for (int i = 0; i < frameButtons.size(); ++i) {
				FrameButton fb = (FrameButton) (frameButtons.get(i));
				if (i == aIndex) {
					currentIndex = i;
					fb.setSelected(true);
				}
				else {
					fb.setSelected(false);
				}
			}
			fbPanel.repaint();
		}
		updatePanel();
		if (aScroll) {
			scrollTo(currentIndex);
		}
	}

	public void setFrames(Frame[] aFrames) {
		frameButtons.clear();
		if (aFrames != null) {
			for (int i = 0; i < aFrames.length; ++i) {
				Frame framePre = null;
				if(i > 0) {
					FrameButton fb = (FrameButton) (frameButtons.get(i-1));
					framePre = fb.frame;
				}
				frameButtons.add(new FrameButton(aFrames[i], framePre));
			}
		}
		playing = false;
		resizeFBPanel();
		select(0);
	}
	
	private void selectPlay(int aIndex, int playIndex, float rate) {
		currentIndex = -1;
		if (!frameButtons.isEmpty()) {
			for (int i = 0; i < frameButtons.size(); ++i) {
				FrameButton fb = (FrameButton) (frameButtons.get(i));
				if (i == aIndex) {
					currentIndex = i;
					fb.setSelected(true);
				}
				else {
					fb.setSelected(false);
				}
			}
			fbPanel.repaint();
		}
		
		Frame frame = null;
		Frame framePre = null;
		if(playIndex < playFrames.size()) {
			frame = playFrames.get(playIndex);
			if(playIndex > 0)
				framePre = playFrames.get(playIndex-1);
		}
		
		Frame frameNext = null;
		if(playIndex < playFrames.size()-1)
			frameNext = playFrames.get(playIndex+1);
	//	else if(playIndex == playFrames.size()-1)
	//		frameNext = ((FrameButton) (frameButtons.get(aIndex-playIndex))).getFrame();
		if(frame != null) {
			ArrayList objarr = frame.getFrameDraw().getClipArr();
			ArrayList objarr2 = ((FrameButton) (frameButtons.get(aIndex))).getFrame().getFrameDraw().getClipArr();
			for(int i=0; i<objarr.size(); ++i) {
				ClipPic c = (ClipPic)(objarr.get(i));
				ClipPic src = (ClipPic)(objarr2.get(i));
				if(frameNext != null) {
					ClipPic[] cps = frameNext.getFrameDraw().getClipPic(c.getGroupID(), c.getID());
					for(int j=0; j<cps.length; ++j) {
						updateData(c, src, cps[j], rate);
					}
				}
			}
		}
		
		animPanel.setFrame(frame, framePre);
	}
	
	// 帧之间插值
	private void updateData(ClipPic now, ClipPic src, ClipPic des, double rate) {
		if(src == null || des == null)
			return;
		if(src.getLogicId() != des.getLogicId())
			return;
		
		// 线性插值
		now.posX = src.posX + (int)((des.posX-src.posX)*rate);
		now.posY = src.posY + (int)((des.posY-src.posY)*rate);
		now.scale = src.scale + (des.scale-src.scale)*rate;
		now.jiao = src.jiao + (des.jiao-src.jiao)*rate;
		now.alpha = src.alpha + (int)((des.alpha-src.alpha)*rate);
		
		now.updateImage();
	}
	
	// 帧播放完了后reset
	private void updateData(ClipPic c, ClipPic c2) {
		if(c == null || c2 == null)
			return;
		
		c.posX = c2.posX;
		c.posY = c2.posY;
		c.scale = c2.scale;
		c.jiao = c2.jiao;
		c.alpha = c2.alpha;
	}
	
	private void resetData(int dIndex) {
		for (int i=0; i<playFrames.size(); ++i) {
			ArrayList objarr = playFrames.get(i).getFrameDraw().getClipArr();
			ArrayList objarr2 = ((FrameButton) (frameButtons.get(i+dIndex))).getFrame().getFrameDraw().getClipArr();
			for(int j=0; j<objarr.size(); ++j) {
				ClipPic c = (ClipPic)(objarr.get(j));
				ClipPic c2 = (ClipPic)(objarr2.get(j));
				updateData(c, c2);
			}
		}
	}

	private void startPlay() {
		if (playing || frameButtons.isEmpty()) return;

		updatedFromPanel();

		Thread t = new Thread() {

			public void run() {
				playButton.setText("停止");
				frameManager.setPlaying(true);
				int index = currentIndex;
				int start = currentIndex;
				int end = start;
				int dIndex = 0;
				for (int i = 0; i < frameButtons.size(); ++i) {
					FrameButton fb = (FrameButton) (frameButtons.get(i));
					if (fb.isSelected()) {
						if (start > i) start = i;
						if (end < i) end = i;
					}
				}
				if (end <= start) {
					start = 0;
					end = frameButtons.size() - 1;
				}
				
				playFrames.clear();
				for (int i=start; i<=end; ++i) {
					playFrames.add(((FrameButton) (frameButtons.get(i))).getFrame().copyFrame(true));
				}
				dIndex = start;
				start = 0;
				end = playFrames.size()-1;
				
				int offsetX;
				int offsetY;
				while (!playFrames.isEmpty()) {
					if (index < start) index = start;
					if (index > end) {
						index = start;
						resetData(dIndex);
					}
					offsetX = 0;
					offsetY = 0;
					for (int i = 0; i < index; ++i) {
						Frame frame = playFrames.get(i);
						offsetX += frame.getOffsetX();
						offsetY -= frame.getOffsetY();
					}
					//System.out.println("offx ="+offsetX+",offy ="+offsetY);
					frameManager.setOffsetX(offsetX);
					frameManager.setOffsetY(offsetX);
					
					animPanel.aniedit.setOffsetX(offsetX);
					animPanel.aniedit.setOffsetY(offsetY);
//					MainFrame.self.animPanel.aniedit.setOffsetX(offsetX);
//					MainFrame.self.animPanel.aniedit.setOffsetY(offsetY);
					
					FrameButton fb = (FrameButton) (frameButtons.get(index+dIndex));
					MainFrame.self.getSoundManager().play(fb.getFrame().getSoundID());
					
					if(animPanel.getIsFrameAnim()) {
						select(index+dIndex, false);
						try {
							synchronized (this) {
								this.wait(fb.getFrame().getInterval());
							}
						}
						catch (Exception e) {
							break;
						}
					}
					else {
						long time = getMillis();
						long now = time;
						long total = fb.getFrame().getInterval();
						while(now-time < total) {
							long dt = now-time;
							if(dt > total)
								break;
							selectPlay(index+dIndex, index, (float)dt/total);
							try {
								synchronized (this) {
									this.wait(10);
								}
							}
							catch (Exception e) {
								break;
							}
							
							now = getMillis();
							if(!playing)
								break;
						}
					}
					
					if (playing) {
						++index;
					}
					else {
						break;
					}
				}
				frameManager.setPlaying(false);
				frameManager.getScrollablePanel().repaint();
				frameRectManager.getScrollablePanel().repaint();
				playButton.setText("播放");
			}
		};

		playing = true;
		t.start();
	}
	
	public static long getMillis() {
		 java.util.Calendar c = java.util.Calendar.getInstance();
		 c.setTime(new Date());
		 return c.getTimeInMillis();
	}

	public void stopPlay() {
		select(currentIndex);
		animPanel.aniedit.setOffsetX(0);
		animPanel.aniedit.setOffsetY(0);
		animPanel.aniedit.showjpanel.repaint();
		playing = false;
	}

	private void updatedFromPanel() {
		if (!frameButtons.isEmpty()) {
			if (currentIndex >= 0 && currentIndex < frameButtons.size()) {
				FrameButton fb = (FrameButton) (frameButtons.get(currentIndex));
				fb.updatedFromPanel();
			}
		}
	}

	private void updatePanel() {
		animPanel.setFrame(null, null);
		frameManager.reset();
		if (!frameButtons.isEmpty()) {
			if (currentIndex >= 0 && currentIndex < frameButtons.size()) {
				FrameButton fb = (FrameButton) (frameButtons.get(currentIndex));
				fb.updataPanel();
				animPanel.setFrameIndex(currentIndex);
			}
		}
	}
	
}

class FrameManager extends SpriteManager {

	public final static int REAL_LEFT = 80;
	public final static int REAL_TOP = 40;
	public static int REAL_WIDTH = 480;
	public static int REAL_HEIGHT = 800;

	public static int BASIC_WIDTH = REAL_WIDTH + REAL_LEFT * 2 + 500;
	public static int BASIC_HEIGHT = REAL_HEIGHT + REAL_TOP * 2 + 200;

	public final static int ORIGIN_X = REAL_LEFT + 40;
	public final static int ORIGIN_Y = REAL_TOP + REAL_HEIGHT - 60;

	private Color color;
	private IntPair specialPoint;
	private boolean selfPoint;
	private boolean playing;
	private int offsetX;
	private int offsetY;

	public FrameManager(ScrollablePanel scrollablePanel, MouseInfo mouseInfo) {
		super(scrollablePanel, mouseInfo);
		//color = Color.WHITE;
		color = new Color(0xFF888888);
		specialPoint = new IntPair(0, 0);
		selfPoint = false;
		playing = false;
	}

	protected Sprite createSprite(int x, int y) {
		if (!(mouseInfo.getPainter() instanceof SIPainter)) { return null; }
		SIPainter painter = (SIPainter) mouseInfo.getPainter();
		return new SISprite(painter.getSI(), useMaxID(), painter.getSI().getLeft(x),
		        painter.getSI().getTop(y), mouseInfo.isFlip());
	}

	public Color getColor() {
		return color;
	}

	public IntPair getSpecialPoint() {
		return specialPoint;
	}

	public boolean isSelfPoint() {
		return selfPoint;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public void load(DataInputStream in, Object[] resManagers) throws Exception {}

	public void paintPoints(Graphics g) {
		g.setColor(new Color(0xFF00FF));
//		g.fillRect(ORIGIN_X - 1, ORIGIN_Y - 1, 3, 3);
		g.fillRect(0, ORIGIN_Y - 1, 800, 1);
		g.fillRect(ORIGIN_X - 1, 0, 1, 800);

		if (specialPoint.x != 0 || specialPoint.y != 0) {
			g.setColor(new Color(0xFF00FF));
			g.fillRect(specialPoint.x + ORIGIN_X - 2, specialPoint.y + ORIGIN_Y - 2, 4, 4);
		}
	}

	public void paintSprites(Graphics g) {
		Color oldColor = g.getColor();
		g.setColor(color);
		g.fillRect(REAL_LEFT, REAL_TOP, REAL_WIDTH, REAL_HEIGHT);
		g.setColor(oldColor);
		Graphics2D g2 = (Graphics2D) g;

		if (playing) {
			g2.translate(offsetX, offsetY);
		}

		super.paintSprites(g);

		if (playing) {
			// g2.translate(-offsetX, -offsetY);
		}
	}

	public void paintStatic(Graphics g) {
		Color oldColor = g.getColor();
		g.setColor(color);
		g.fillRect(REAL_LEFT, REAL_TOP, REAL_WIDTH, REAL_HEIGHT);
		g.setColor(oldColor);
		Graphics2D g2 = (Graphics2D) g;

		if (playing) {
			g2.translate(offsetX, offsetY);
		}

		super.paintStatic(g);

		if (playing) {
			// g2.translate(-offsetX, -offsetY);
		}
	}

	protected void pasteSprites(int x, int y) {
		if (copiedSprites.size() > 0) {
			ArrayList tmp = new ArrayList();
			for (int i = 0; i < copiedSprites.size(); ++i) {
				Sprite sprite = (Sprite) (copiedSprites.get(i));
				if (sprite instanceof Copyable) {
					tmp.add(((Copyable) sprite).copy());
				}
			}

			if (tmp.size() > 0) {
				Sprite[] sprites = new Sprite[tmp.size()];
				for (int i = 0; i < tmp.size(); ++i) {
					Sprite sprite = (Sprite) (tmp.get(i));
					sprite.setID(useMaxID());
					sprites[i] = sprite;
				}
				addUndoableSprites(sprites);
				selection.selectSprites(sprites);
				scrollablePanel.repaint();
			}
		}
	}

	public void save(DataOutputStream out, Object[] resManagers) throws Exception {}

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception {}

	protected void selectPointMouseLeftPressed(MouseEvent e) {
		if (selfPoint) {
			setSpecialPoint(mouseX - ORIGIN_X, mouseY - ORIGIN_Y);
		}
		else {
			super.selectPointMouseLeftPressed(e);
		}
	}

	protected void selectPointMouseRightPressed(MouseEvent e) {
		if (selfPoint) {
			setSelfPoint(false);
		}
		else {
			super.selectPointMouseRightPressed(e);
		}
	}

	public void setColor(Color color) {
		this.color = color;
		scrollablePanel.repaint();
	}

	public void setSelfPoint(boolean value) {
		selfPoint = value;
		if (selfPoint) {
			scrollablePanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
		else {
			scrollablePanel.setCursor(Cursor.getDefaultCursor());
		}
	}

	public void setSpecialPoint(int x, int y) {
		specialPoint.x = x;
		specialPoint.y = y;
		scrollablePanel.repaint();
	}

	public void setSpecialPoint(IntPair aPoint) {
		setSpecialPoint(aPoint.x, aPoint.y);
	}

	protected void showProperties(Sprite sprite) {}
}

class FramePanel extends SpriteManagerPanel {

	public FramePanel(JDialog owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	public FramePanel(JFrame owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	protected SpriteManager createManager() {
		return new FrameManager(this, mouseInfo);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		((FrameManager) manager).paintPoints(g);
	}
}

class FrameRectManager extends RectManager {

	public FrameRectManager(RectPanel rectPanel, MouseInfo mouseInfo) {
		super(rectPanel, mouseInfo);
	}

	protected void pasteShapes(int x, int y) {
		if (copiedShapes.size() > 0) {
			ArrayList tmp = new ArrayList();
			for (int i = 0; i < copiedShapes.size(); ++i) {
				Shape shape = (Shape) (copiedShapes.get(i));
				if (shape instanceof Copyable) {
					tmp.add(((Copyable) shape).copy());
				}
			}

			if (tmp.size() > 0) {
				Shape[] shapes = new Shape[tmp.size()];
				for (int i = 0; i < tmp.size(); ++i) {
					Shape shape = (Shape) (tmp.get(i));
					shape.setID(useMaxID());
					addShape(shape);
					shapes[i] = shape;
				}
				selection.selectShapes(shapes);
				scrollablePanel.repaint();
			}
		}
	}
}

class FrameRectPanel extends RectPanel {

	public FrameRectPanel(JDialog owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	public FrameRectPanel(JFrame owner, MouseInfo mouseInfo) {
		super(owner, mouseInfo);
	}

	protected ShapeManager createManager() {
		return new FrameRectManager(this, mouseInfo);
	}
}