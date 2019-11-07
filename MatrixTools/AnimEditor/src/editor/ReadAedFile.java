package editor;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class ReadAedFile {
	
	public static void readaed() throws Exception{
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(
		        new File(XUtil.getDefPropStr("SavePath") + "\\testani.aed"))));
		
		int actlenght =in.readInt();//得到有几个动作
		System.out.println("动作长度 ="+actlenght);
		if(actlenght>0){
			for(int i=0;i<actlenght;i++){
				//int actid =in.readInt(); //动作ID
				//处理当前动作下的桢
				int frameLenght =in.readInt();//桢长度
				System.out.println("桢长度 ="+frameLenght);
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
						//矩形(测试ANI为0)
						int rectLt =in.readInt();
						if(rectLt >0){
							//循环
						}
					}
				}
			}
		}
		in.close();
	}
	public static void loadClipInfo(DataInputStream in) throws Exception{
		//得到每一桢上的切片信息
		int clipNum =in.readInt();//取得当前桢上的切片个数
		if(clipNum >0){
			for(int n=0;n<clipNum;n++){
				int posx =in.readInt();//x 坐标
				int posy =in.readInt();//y坐标
				System.out.println("posx  ="+posx+",posy"+posy); 
				double scale =in.readDouble();//缩放比
				System.out.println("scale  ="+scale); 
				double jiao =in.readDouble();//旋转角度
				//切片信息
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
