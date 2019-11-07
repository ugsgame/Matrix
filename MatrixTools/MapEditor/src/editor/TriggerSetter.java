package editor;

//should modify

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * ´¥·¢Æ÷ÉèÖÃ´°¿Ú¡£
 */
public abstract class TriggerSetter extends OKCancelDialog {

	protected Trigger trigger;

	public TriggerSetter(JDialog owner, Trigger trigger) {
		super(owner);
		init(trigger);
	}

	public TriggerSetter(JFrame owner, Trigger trigger) {
		super(owner);
		init(trigger);
	}

	private void init(Trigger trigger) {
		this.trigger = trigger;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void show() {
		if (getTrigger() == null) { return; }
		super.show();
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public static TriggerSetter createSetter(JDialog owner, Trigger trigger) {
		if (owner == null || trigger == null) { return null; }
		TriggerSetter result = null;
		switch (trigger.getType()) {
		case Trigger.SWITCH:
			result = new SwitchTriggerSetter(owner, trigger);
			break;
		case Trigger.CAMERA_OVER_X:
			result = new CameraOverXSetter(owner, trigger);
			break;
		case Trigger.ENEMY_DEAD:
			result = new EnemyChooserTrigger(owner, trigger);
			break;
		case Trigger.PLAYER_IN_AREA:
			result = new PlayerInAreaSetter(owner, trigger);
			break;
		default:
			result = null;
			break;
		}
		return result;
	}
}
