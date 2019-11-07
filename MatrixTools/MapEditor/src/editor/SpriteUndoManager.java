package editor;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * ��������Sprite����ӡ�ɾ�����ƶ��ĳ�����������
 */
public class SpriteUndoManager extends UndoManager {

	protected SpriteManager spriteManager;
	private int used;

	public SpriteUndoManager(SpriteManager spriteManager) {
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

	public void addUndoSpriteAdd(Sprite[] sprites) {
		if (sprites != null) {
			if (sprites.length > 0) {
				addEdit(new UndoSpriteAdd(spriteManager, sprites));
			}
		}
	}

	public void addUndoSpriteFlip(Sprite[] sprites) {
		if (sprites != null) {
			if (sprites.length > 0) {
				addEdit(new UndoSpriteFlip(spriteManager, sprites));
			}
		}
	}

	public void addUndoSpriteMove(Sprite[] sprites, int offsetX, int offsetY) {
		if ((offsetX != 0 || offsetY != 0) && sprites != null) {
			if (sprites.length > 0) {
				addEdit(new UndoSpriteMove(spriteManager, sprites, offsetX, offsetY));
			}
		}
	}

	public void addUndoSpriteRemove(Sprite[] sprites) {
		if (sprites != null) {
			if (sprites.length > 0) {
				addEdit(new UndoSpriteRemove(spriteManager, sprites));
			}
		}
	}

	public void redo() {
		try {
			if (canRedo()) {
				super.redo();
				++used;
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
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class UndoSpriteAdd extends AbstractUndoableEdit {

	private Sprite[] sprites;
	private SpriteManager spriteManager;

	public UndoSpriteAdd(SpriteManager spriteManager, Sprite[] sprites) {
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

class UndoSpriteFlip extends AbstractUndoableEdit {

	private SpriteManager spriteManager;
	private Sprite[] sprites;

	public UndoSpriteFlip(SpriteManager spriteManager, Sprite[] sprites) {
		this.spriteManager = spriteManager;
		this.sprites = sprites;
	}

	public void redo() {
		super.redo();
		spriteManager.flipSprites(sprites);
	}

	public void undo() {
		super.undo();
		spriteManager.flipSprites(sprites);
	}
}

class UndoSpriteMove extends AbstractUndoableEdit {

	private SpriteManager spriteManager;
	private Sprite[] sprites;
	int offsetX, offsetY;

	public UndoSpriteMove(SpriteManager spriteManager, Sprite[] sprites, int offsetX, int offsetY) {
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

class UndoSpriteRemove extends AbstractUndoableEdit {

	private Sprite[] sprites;
	private SpriteManager spriteManager;

	public UndoSpriteRemove(SpriteManager spriteManager, Sprite[] sprites) {
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