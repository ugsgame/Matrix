package editor;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JPanel;

interface Manager {

	public void addListener(ManagerListener listener);

	public int getMouseX();

	public int getMouseY();

	public String getSelectionInfo();

	public void load(DataInputStream in, Object[] resManagers) throws Exception;

	public void paintStatic(Graphics g);

	public void removeListener(ManagerListener listener);

	public void reset();

	public void resetOrigin(int offsetX, int offsetY);

	public void save(DataOutputStream out, Object[] resManagers) throws Exception;

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception;

	public void paintStatic(Graphics g, int id);
}

class ManagerAdapter implements ManagerListener {

	public void keyChanged() {}

	public void mouseChanged(MouseEvent e) {}

	public void selectionChanged() {}

	public void spriteChanged() {}
}

interface ManagerListener {

	public void keyChanged();

	public void mouseChanged(MouseEvent e);

	public void selectionChanged();

	public void spriteChanged();
}

interface ManagerPanel {

	public void addBackManager(Manager manager);

	public void addForeManager(Manager manager);

	public JPanel getBackPanel();

	public Manager getManager();

	public void load(DataInputStream in, Object[] resManagers) throws Exception;

	public void removeBackManager(Manager manager);

	public void removeForeManager(Manager manager);

	public void reset(int basicWidth, int basicHeight);

	public void save(DataOutputStream out, Object[] resManagers) throws Exception;

	public void saveMobile(DataOutputStream out, Object[] resManagers) throws Exception;
	
	public void saveMobile(DataOutputStream out, int layer) throws Exception;

	public void setMapSize(int basicWidth, int basicHeight);
}