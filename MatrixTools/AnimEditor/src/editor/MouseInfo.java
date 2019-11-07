package editor;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * 鼠标上附带的一些信息。例如图片或数据等。
 */
public class MouseInfo {

	// 鼠标状态
	public final static int SELECT_POINT = 0;
	public final static int NEW_SPRITE = 1;
	public final static int DELETE_SPRITE = 2;

	private int info;
	private Painter painter;
	private int range;
	private boolean flip;
	private ArrayList listeners;

	public MouseInfo() {
		listeners = new ArrayList();
		info = SELECT_POINT;
		painter = null;
		range = 1;
		flip = false;
	}

	public void addListener(MouseInfoListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public int getInfo() {
		return info;
	}

	public Painter getPainter() {
		return painter;
	}

	public int getRange() {
		return this.range;
	}

	public boolean isFlip() {
		return flip;
	}

	public void paint(Graphics g, int x, int y) {
		if (info == NEW_SPRITE && painter != null) {
			/*if (painter instanceof SIPainter) {
				((SIPainter) (painter)).paintOrigin(g, x, y, flip);
			}else if(painter instanceof ClipPic){
				((ClipPic)painter).paint(g, x-((ClipPic)painter).image.getWidth()/2, y-((ClipPic)painter).image.getHeight()/2);
			}
			else {
				painter.paintOrigin(g, x, y);
			}*/
			if(painter.getGroupID()<0){
				if (painter instanceof SIPainter) {
					((SIPainter) (painter)).paintOrigin(g, x, y, flip);
				}else if(painter instanceof ClipPicPanel){
					((ClipPicPanel)painter).paint(g, x-((ClipPicPanel)painter).clip.image.getWidth()/2, y-((ClipPicPanel)painter).clip.image.getHeight()/2);
				}
			}
		}
	}

	public void removeListener(MouseInfoListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	public void resetAll() {
		resetSelf();
		for (int i = 0; i < listeners.size(); ++i) {
			((MouseInfoListener) (listeners.get(i))).resetAll();
		}
	}

	public void resetSelf() {
		setInfo(SELECT_POINT);
		setPainter(painter = null);
	}

	public void setFlip(boolean flip) {
		this.flip = flip;
	}

	public void setInfo(int info) {
		this.info = info;
		for (int i = 0; i < listeners.size(); ++i) {
			((MouseInfoListener) (listeners.get(i))).infoChanged();
		}
	}

	public void setPainter(Painter painter) {
		this.painter = painter;
		for (int i = 0; i < listeners.size(); ++i) {
			((MouseInfoListener) (listeners.get(i))).painterChanged();
		}
	}

	public void setRange(int range) {
		this.range = range;
	}
}

class MouseInfoAdapter implements MouseInfoListener {

	public void infoChanged() {}

	public void painterChanged() {}

	public void resetAll() {}
}

interface MouseInfoListener {

	public void infoChanged();

	public void painterChanged();

	public void resetAll();
}