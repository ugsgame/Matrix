package editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

class Sound {
	private int id;
	private String name;
	private File file;
	
	public static Pair getSoundsPair(Sound[] sounds) {

		ArrayList tmp = new ArrayList();
		if(sounds != null) {
			for(int i = 0; i < sounds.length; ++i) {
				Sound sound = sounds[i];
				if(sound != null) {
					if(sound.getID() > 0) {
						tmp.add(sound);
					}
				}
			}
		}

		int[] ids = new int[tmp.size() + 1];
		String[] names = new String[tmp.size() + 1];
		ids[0] = 0;
		names[0] = "Œﬁ…˘“Ù";
		for(int i = 0; i < tmp.size(); ++i) {
			Sound sound = (Sound)(tmp.get(i));
			ids[i + 1] = sound.getID();
			names[i + 1] = sound.getName();
		}
		
		return new Pair(ids, names);
	}
	
	
	public Sound(int id, String name, String fileFullName) {
		this.id = id;
		this.name = name;
		this.file = new File(fileFullName);
	}
	
    public int getID() {
    	return this.id;
    }
	
    public String getName() {
    	return this.name;
    }
    
    public void play() {
    	if(!file.exists()) return;
    	
		try {
//			AudioPlayer.player.start(new AudioStream(new FileInputStream(file)));
		}
		catch(Exception e) {
		}
    }
}

class SoundManager {
	private Sound[] sounds;
	
	public SoundManager() {
		this.sounds = null;
		try {
			init();
		}
		catch(Exception e) {			
		}
	}
	
	private void init() throws Exception {	
		String sourceFolder = XUtil.getDefPropStr("SoundPath");
		if(!(new File(sourceFolder)).exists()) return;
		
		File sourceSoundFile = new File(sourceFolder + "\\wav.txt");
		if(!sourceSoundFile.exists()) return;
		
		File selfSoundFile = new File(".\\sound.txt");
		if(!selfSoundFile.exists()) return;

		String sLine;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(sourceSoundFile)));
		ArrayList sourceData = new ArrayList();
		sLine = in.readLine();
		while (sLine != null) {
			sLine = sLine.trim();
			if(!sLine.equals("")) {
				sourceData.add(sLine);
			}
			sLine = in.readLine();
		}
		in.close();
		
		in = new BufferedReader(new InputStreamReader(new FileInputStream(selfSoundFile)));
		ArrayList selfData = new ArrayList();
		sLine = in.readLine();
		while (sLine != null) {
			sLine = sLine.trim();
			if(!sLine.equals("")) {
				selfData.add(sLine);
			}
			sLine = in.readLine();
		}
		in.close();
		
		if(selfData.isEmpty()) return;
		
		ArrayList tmp = new ArrayList();
		
		for(int i = 0; i < selfData.size(); ++i) {
			
			sLine = (String)(selfData.get(i));
			String[] infos = sLine.split(",");
			if(infos == null) continue;
			if(infos.length < 1) continue;
			int id = 0;
			String name = "Œ¥√¸√˚";
			try {
				id = Integer.parseInt(infos[0]);
				if(infos.length >= 2) name = infos[1];
			}
			catch(Exception e) {
			}
			if(id <= 0 || id >= sourceData.size()) continue;
						
			Sound sound = null;
			try {
				sound = new Sound(id, name, sourceFolder + "\\" + (String)(sourceData.get(id)));
			}
			catch(Exception e) {
				sound = new Sound(id, name, "");
			}
			tmp.add(sound);
		}
		
		if(!tmp.isEmpty()) {
			sounds = new Sound[tmp.size()];
			for(int i = 0; i < tmp.size(); ++i) {
				sounds[i] = (Sound)(tmp.get(i));
			}
		}
	}
	
	public Sound[] getSounds() {
		return sounds;
	}
	
	public void play(int soundID) {
		if(sounds == null || soundID <= 0) return;
		
		for(int i = 0; i < sounds.length; ++i) {
			Sound sound = sounds[i];
			if(sound == null) continue;
			if(sound.getID() == soundID) {
				sound.play();
				break;
			}
		}
	}
}










