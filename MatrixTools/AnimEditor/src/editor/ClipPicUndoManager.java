package editor;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * 用来管理ClipPic的添加、删除和移动的撤销管理器。
 */
public class ClipPicUndoManager extends UndoManager {

	protected OneFrameDraw spriteManager;
	private int used;

	public ClipPicUndoManager(OneFrameDraw spriteManager) {
		this.spriteManager = spriteManager;
		used = 0;
	}

	public boolean addEdit(UndoableEdit anEdit) {
		if (used >= getLimit() - 10) {
			setLimit(getLimit() + 100);
		}
		++used;
		return super.addEdit(anEdit);
	}

	public void addUndoSpriteAdd(ClipPic[] sprites) {
		if (sprites != null) {
			if (sprites.length > 0) {
				addEdit(new UndoClipPicAdd(spriteManager, sprites));
			}
		}
	}

	public void addUndoSpriteFlip(ClipPic[] sprites, int type) {
		if (sprites != null) {
			if (sprites.length > 0) {
				addEdit(new UndoClipPicFlip(spriteManager, sprites, type));
			}
		}
	}

	public void addUndoSpriteMove(ClipPic[] sprites, int offsetX, int offsetY) {
		if ((offsetX != 0 || offsetY != 0) && sprites != null) {
			if (sprites.length > 0) {
				addEdit(new UndoClipPicMove(spriteManager, sprites, offsetX, offsetY));
			}
		}
	}

	public void addUndoSpriteRemove(ClipPic[] sprites) {
		if (sprites != null) {
			if (sprites.length > 0) {
				addEdit(new UndoClipPicRemove(spriteManager, sprites));
			}
		}
	}
	
	public void addUndoSpriteScale(ClipPic[] sprites, double offsetScale) {
		if (sprites != null) {
			if (sprites.length > 0) {
				addEdit(new UndoClipPicScale(spriteManager, sprites, offsetScale));
			}
		}
	}
	
	public void addUndoSpriteAngle(ClipPic[] sprites, double offsetAngle) {
		if (sprites != null) {
			if (sprites.length > 0) {
				addEdit(new UndoClipPicAngle(spriteManager, sprites, offsetAngle));
			}
		}
	}
	
	public void addUndoSpriteAlpha(ClipPic[] sprites, double offsetAlpha) {
		if (sprites != null) {
			if (sprites.length > 0) {
				addEdit(new UndoClipPicAlpha(spriteManager, sprites, offsetAlpha));
			}
		}
	}
	
	public int getUsedId() {
		return used;
	}

	public void redo() {
		try {
			if (canRedo()) {
				super.redo();
				++used;
			//	System.out.println("redo: "+used);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void undo() {
		try {
			if (canUndo()) {
				super.undo();
				--used;
			//	System.out.println("undo: "+used);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class UndoClipPicAdd extends AbstractUndoableEdit {

	private ClipPic[] sprites;
	private OneFrameDraw spriteManager;

	public UndoClipPicAdd(OneFrameDraw spriteManager, ClipPic[] sprites) {
		this.spriteManager = spriteManager;
		this.sprites = sprites;
	}

	public void redo() {
		super.redo();
		spriteManager.redoAdd(sprites);
	}

	public void undo() {
		super.undo();
		spriteManager.undoAdd(sprites);
	}
}

class UndoClipPicFlip extends AbstractUndoableEdit {

	private OneFrameDraw spriteManager;
	private ClipPic[] sprites;
	private int type;

	public UndoClipPicFlip(OneFrameDraw spriteManager, ClipPic[] sprites, int type) {
		this.spriteManager = spriteManager;
		this.sprites = sprites;
		this.type = type;
	}

	public void redo() {
		super.redo();
		spriteManager.flipSprites(sprites, type);
	}

	public void undo() {
		super.undo();
		spriteManager.flipSprites(sprites, type);
	}
}

class UndoClipPicMove extends AbstractUndoableEdit {

	private OneFrameDraw spriteManager;
	private ClipPic[] sprites;
	int offsetX, offsetY;

	public UndoClipPicMove(OneFrameDraw spriteManager, ClipPic[] sprites, int offsetX, int offsetY) {
		this.spriteManager = spriteManager;
		this.sprites = sprites;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public void redo() {
		super.redo();
		spriteManager.redoMove(sprites, offsetX, offsetY);
	}

	public void undo() {
		super.undo();
		spriteManager.undoMove(sprites, offsetX, offsetY);
	}
}

class UndoClipPicRemove extends AbstractUndoableEdit {

	private ClipPic[] sprites;
	private OneFrameDraw spriteManager;

	public UndoClipPicRemove(OneFrameDraw spriteManager, ClipPic[] sprites) {
		this.spriteManager = spriteManager;
		this.sprites = sprites;
	}

	public void redo() {
		super.redo();
		spriteManager.redoRemove(sprites);
	}

	public void undo() {
		super.undo();
		spriteManager.undoRemove(sprites);
	}
}

class UndoClipPicScale extends AbstractUndoableEdit {

	private ClipPic[] sprites;
	private OneFrameDraw spriteManager;
	private double offsetScale;

	public UndoClipPicScale(OneFrameDraw spriteManager, ClipPic[] sprites, double offsetScale) {
		this.spriteManager = spriteManager;
		this.sprites = sprites;
		this.offsetScale = offsetScale;
	}

	public void redo() {
		super.redo();
		spriteManager.scaleSprites(sprites, offsetScale);
	}

	public void undo() {
		super.undo();
		spriteManager.scaleSprites(sprites, -offsetScale);
	}
}

class UndoClipPicAngle extends AbstractUndoableEdit {

	private ClipPic[] sprites;
	private OneFrameDraw spriteManager;
	private double offsetAngle;

	public UndoClipPicAngle(OneFrameDraw spriteManager, ClipPic[] sprites, double offsetAngle) {
		this.spriteManager = spriteManager;
		this.sprites = sprites;
		this.offsetAngle = offsetAngle;
	}

	public void redo() {
		super.redo();
		spriteManager.angleSprites(sprites, offsetAngle);
	}

	public void undo() {
		super.undo();
		spriteManager.angleSprites(sprites, -offsetAngle);
	}
}

class UndoClipPicAlpha extends AbstractUndoableEdit {

	private ClipPic[] sprites;
	private OneFrameDraw spriteManager;
	private double offsetAlpha;

	public UndoClipPicAlpha(OneFrameDraw spriteManager, ClipPic[] sprites, double offsetAlpha) {
		this.spriteManager = spriteManager;
		this.sprites = sprites;
		this.offsetAlpha = offsetAlpha;
	}

	public void redo() {
		super.redo();
		spriteManager.alphaSprites(sprites, offsetAlpha);
	}

	public void undo() {
		super.undo();
		spriteManager.alphaSprites(sprites, -offsetAlpha);
	}
}
