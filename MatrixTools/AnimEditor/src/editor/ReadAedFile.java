package editor;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class ReadAedFile {
	
	public static void readaed() throws Exception{
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(
		        new File(XUtil.getDefPropStr("SavePath") + "\\testani.aed"))));
		
		int actlenght =in.readInt();//�õ��м�������
		System.out.println("�������� ="+actlenght);
		if(actlenght>0){
			for(int i=0;i<actlenght;i++){
				//int actid =in.readInt(); //����ID
				//����ǰ�����µ���
				int frameLenght =in.readInt();//�峤��
				System.out.println("�峤�� ="+frameLenght);
				if(frameLenght >0){
					for(int j=0;j<frameLenght;j++){
						int interval = in.readInt();
						boolean special = in.readBoolean();
						int offsetX = in.readInt();
						int offsetY = in.readInt();
						int moveType = in.readInt();
						int damageType = in.readInt();
						int hitDist = in.readInt();
						int damage = in.readInt();
						boolean shadow = in.readBoolean();
						int soundID = in.readInt();
						loadClipInfo(in);
						//����(����ANIΪ0)
						int rectLt =in.readInt();
						if(rectLt >0){
							//ѭ��
						}
					}
				}
			}
		}
		in.close();
	}
	public static void loadClipInfo(DataInputStream in) throws Exception{
		//�õ�ÿһ���ϵ���Ƭ��Ϣ
		int clipNum =in.readInt();//ȡ�õ�ǰ���ϵ���Ƭ����
		if(clipNum >0){
			for(int n=0;n<clipNum;n++){
				int posx =in.readInt();//x ����
				int posy =in.readInt();//y����
				System.out.println("posx  ="+posx+",posy"+posy); 
				double scale =in.readDouble();//���ű�
				System.out.println("scale  ="+scale); 
				double jiao =in.readDouble();//��ת�Ƕ�
				//��Ƭ��Ϣ
				int picId =in.readInt();
				int cutx =in.readInt();
				int cuty =in.readInt();
				int cutw =in.readInt();
				int cuth =in.readInt();
				System.out.println("cc  ="+picId+","+cutx+","+cuty+","+cutw+","+cuth); 
			}
		}
	}
	public static void main(String[] args) {
		try{
			readaed();
		}catch (Exception e) {
			
		}
	}
}
