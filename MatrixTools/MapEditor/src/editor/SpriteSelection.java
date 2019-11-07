package editor;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * 一个Sprite的集合，用来包含一个或多个被选中的Sprite。
 * 
 * @see SpriteManager
 */
public class SpriteSelection {

	private SpriteManager spriteManager;
	private ArrayList sprites;
	private int movingOffsetX;
	private int movingOffsetY;
	private int nowLayer;

	public int getNowLayer() {
		return nowLayer;
	}

	public void setNowLayer(int nowLayer) {
		this.nowLayer = nowLayer;
	}

	public SpriteSelection(SpriteManager spriteManager) {
		this.spriteManager = spriteManager;
		sprites = new ArrayList();
		movingOffsetX = 0;
		movingOffsetY = 0;
	}

	public boolean addSprite(Sprite sprite) {
		if (!hasSprite(sprite)) {
			SISprite ssprite = (SISprite)sprite;
			if(ssprite.getLayer2() == nowLayer) {
				if (sprites.add(sprite)) {
			//		System.out.println("add sprite");
					sprite.setSelected(true);
					spriteManager.selectionChanged();
					return true;
				}
			}
		}
		return false;
	}

	public void cancelMoving() {
		for (int i = 0; i < sprites.size(); ++i) {
			((Sprite) (sprites.get(i))).cancelMoving();
		}
		movingOffsetX = 0;
		movingOffsetY = 0;
	}

	public void clear() {
		Sprite tmp;
		for (int i = 0; i < sprites.size(); ++i) {
			tmp = (Sprite) (sprites.get(i));
			tmp.setSelected(false);
		}
		sprites.clear();
		spriteManager.selectionChanged();
	}

	public void click(int x, int y) {
	// nothing
	}
	
	public void shiftrelease()
	{
		Sprite tmp;
		for (int i = 0; i < sprites.size(); ++i) {
			tmp = (Sprite) (sprites.get(i));
			tmp.setShiftDownTrue(false);
		}
	}
	
	public void shiftPressed() {
		// TODO Auto-generated method stub
		Sprite tmp;
		for (int i = 0; i < sprites.size(); ++i) {
			tmp = (Sprite) (sprites.get(i));
			tmp.setShiftDownTrue(true);
		}
	}
	
	public void Spressed()
	{
		Sprite tmp;
		for (int i = 0; i < sprites.size(); ++i) {
			tmp = (Sprite) (sprites.get(i));
			tmp.setSdownTrue(true);
		}
	}
	
	public void Srelease()
	{
		Sprite tmp;
		for (int i = 0; i < sprites.size(); ++i) {
			tmp = (Sprite) (sprites.get(i));
			tmp.setSdownTrue(false);
		}
	}
	
	public void click(int x, int y, int keyCode) {
		Sprite sprite;
		switch (keyCode) {
		case KeyEvent.VK_CONTROL:
			int itemIndex = -1;
			for (int i = 0; i < sprites.size(); ++i) {
				sprite = (Sprite) (sprites.get(i));
				if (sprite.containPoint(x, y)) {
					if (itemIndex >= 0) {
						if (sprite.compareTo(sprites.get(itemIndex)) > 0) {
							itemIndex = i;
						}
					}
					else {
						itemIndex = i;
					}
				}
			}
			if (itemIndex >= 0) {
				removeSprite(itemIndex);
			}
			spriteManager.selectionChanged();
			break;
		case KeyEvent.VK_R://按下r
			//System.out.println("shift down");
			Sprite tmp;
			for (int i = 0; i < sprites.size(); ++i) {
				tmp = (Sprite) (sprites.get(i));
				tmp.setShiftDownTrue(true);
			}
			break;
		case KeyEvent.VK_S:
			Sprite tmp1;
			for (int i = 0; i < sprites.size(); ++i) {
				tmp1 = (Sprite) (sprites.get(i));
				tmp1.setSdownTrue(true);
			}
			break;
		default:
			break;
		}
	}

	public void confirmMoving() {
		for (int i = 0; i < sprites.size(); ++i) {
			((Sprite) (sprites.get(i))).confirmMoving();
		}
		spriteManager.getUndoManager().addUndoSpriteMove(getSprites(), movingOffsetX, movingOffsetY);
		movingOffsetX = 0;
		movingOffsetY = 0;
		spriteManager.selectionChanged();
	}

	public Sprite[] getSprites() {
		TreeSet treeSet = new TreeSet();
		Sprite[] result = new Sprite[sprites.size()];
		for (int i = 0; i < sprites.size(); ++i) {
			treeSet.add(sprites.get(i));
		}
		Iterator it = treeSet.iterator();
		int i = 0;
		while (it.hasNext()) {
			result[i++] = (Sprite) (it.next());
		}
		return result;
	}

	public Sprite getTopSpriteAtPoint(int x, int y) {
		
		Sprite sprite = null, result = null;
		for (int i = 0; i < sprites.size(); ++i) {
			sprite = (Sprite) (sprites.get(i));
			if (sprite.containPoint(x, y)) {
				if (result != null) {
					if (sprite.compareTo(result) > 0) {
						result = sprite;
					}
				}
				else {
					result = sprite;
				}
			}
		}
		return result;
	}

	public boolean hasSprite(Sprite sprite) {
		for (int i = 0; i < sprites.size(); ++i) {
			if (((Sprite) (sprites.get(i))).getID() == sprite.getID()) { return true; }
		}
		return false;
	}

	public boolean isEmpty() {
		return sprites.size() <= 0;
	}
	
	public void moveoffsety(int offset ,int x)
	{
		for (int i = 0; i < sprites.size(); ++i) {
			((Sprite) (sprites.get(i))).moveoffsety(offset ,x);
		}
	}
	
	public void scaleoffsety(int offset ,int x)
	{
		for (int i = 0; i < sprites.size(); ++i) {
			((Sprite) (sprites.get(i))).scaleoffsety(offset ,x);
		}
	}
	
	public void resetRange()
	{
		for (int i = 0; i < sprites.size(); ++i) {
			((Sprite) (sprites.get(i))).resetRange();
		}
	}
	
	public void resetScale()
	{
		for (int i = 0; i < sprites.size(); ++i) {
			((Sprite) (sprites.get(i))).resetScale();
		}
	}	
	
	public void moving(int offsetX, int offsetY) {
		for (int i = 0; i < sprites.size(); ++i) {
			((Sprite) (sprites.get(i))).moving(offsetX, offsetY);
		}
		movingOffsetX = offsetX;
		movingOffsetY = offsetY;
	}

	public boolean pointInSprites(int x, int y) {
		for (int i = 0; i < sprites.size(); ++i) {
			if (((Sprite) (sprites.get(i))).containPoint(x, y)) { return true; }
		}
		return false;
	}

	private void removeSprite(int index) {
		if (index < 0 || index >= sprites.size()) { return; }
		((Sprite) (sprites.get(index))).setSelected(false);
		sprites.remove(index);
	}

	public boolean removeSprite(Sprite sprite) {
		for (int i = 0; i < sprites.size(); ++i) {
			if (((Sprite) (sprites.get(i))).getID() == sprite.getID()) {
				removeSprite(i);
				spriteManager.selectionChanged();
				return true;
			}
		}
		return false;
	}

	public void selectSprite(Sprite sprite) {
		Sprite[] tmp = new Sprite[1];
		tmp[0] = sprite;
		selectSprites(tmp);
	}

	public void selectSprites(Sprite[] sprites) {
		clear();
		for (int i = 0; i < sprites.length; ++i) {
			SISprite sprite = (SISprite)sprites[i];
		//	System.out.println(i+", "+sprite.getLayer()+", "+nowLayer);
			if(sprite.getLayer2() == nowLayer) {
				if (this.sprites.add(sprites[i])) {
			//		System.out.println("selectSprites");
					sprites[i].setSelected(true);
				}
			}
		}
		spriteManager.selectionChanged();
	}


}