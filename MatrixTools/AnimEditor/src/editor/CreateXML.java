package editor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class CreateXML {
	public static Document getDocument(Animation[] anims) {
		Document document = DocumentHelper.createDocument();
		// 生成一个接点
		Element root = document.addElement("ANIMATE");
		for(int i=0;i<anims.length;i++){
			//动作节点
			Element act =root.addElement("ACT");
			Frame[] frames =anims[i].getFrames();
			for(int j=0;frames !=null &&j<frames.length;j++){
				Element frame =act.addElement("FRAME");
				Element frameInfo =frame.addElement("FRAMEINFO");
				frameInfo.addAttribute("INTERVAL", ""+frames[j].getInterval());
				frameInfo.addAttribute("SPECIAL", ""+frames[j].isSpecial());
				frameInfo.addAttribute("OFFX", ""+frames[j].getOffsetX());
				frameInfo.addAttribute("OFFY", ""+frames[j].getOffsetY());
				frameInfo.addAttribute("MOVETYPE", ""+frames[j].getMoveType());
				frameInfo.addAttribute("DAMAGETYPE", ""+frames[j].getDamageType());
				frameInfo.addAttribute("HITDIST", ""+frames[j].getHitDist());
				frameInfo.addAttribute("DAMAGE", ""+frames[j].getDamage());
				frameInfo.addAttribute("SHADOW", ""+frames[j].isShadow());
				frameInfo.addAttribute("SOUNDID", ""+frames[j].getSoundID());
				
				Element clipInfo =frame.addElement("CLIPINFO");
				if(!frames[j].getFrameDraw().getClipArr().isEmpty()){
					ArrayList tempArr =frames[j].getFrameDraw().getClipArr();
					for(int k=0;k<tempArr.size();k++){
						ClipPic cp =(ClipPic)tempArr.get(k);
						Element clipWrite =clipInfo.addElement("CLIPWRITE");
						clipWrite.addAttribute("X", ""+cp.posX);
						clipWrite.addAttribute("Y", ""+cp.posY);
						clipWrite.addAttribute("SCALE", ""+cp.scale);
						clipWrite.addAttribute("JIAO", ""+cp.jiao);
						clipWrite.addAttribute("LVFILP", ""+cp.levelFilp);
						clipWrite.addAttribute("VTFILP", ""+cp.verticalFilp);
						clipWrite.addAttribute("PICID", ""+(-cp.getGroupID()));
						clipWrite.addAttribute("CLIPX", ""+cp.cutx);
						clipWrite.addAttribute("CLIPY", ""+cp.cuty);
						clipWrite.addAttribute("CLIPW", ""+cp.cutw);
						clipWrite.addAttribute("CLIPH", ""+cp.cuth);
						//creatClipElement(cp, clipWrite);
					}
				}
				Element rectInfo =frame.addElement("RECTINFO");
				if(!frames[j].getFrameRect().getArr().isEmpty()){
					ArrayList tempArr =frames[j].getFrameRect().getArr();
					for(int k=0;k<tempArr.size();k++){
						LogicRect fra =(LogicRect)tempArr.get(k);
						Element rectWrite =rectInfo.addElement("RECTWRITE");
						creatRectElement(fra, rectWrite);
					}
				}
			}
		}
		return document;
	}
	public static void creatRectElement(LogicRect lr, Element elt){
		elt.addAttribute("RECTTYPE", ""+lr.getType());
		elt.addAttribute("RECTX", ""+lr.left);
		elt.addAttribute("RECTY", ""+lr.top);
		elt.addAttribute("RECTW", ""+lr.width);
		elt.addAttribute("RECTH", ""+lr.height);
	}
	public static void creatClipElement(ClipPic cp, Element elt){
		for(int i=0;i<MainFrame.clipArr.size();i++){
			clipInfoSave cis =(clipInfoSave)MainFrame.clipArr.get(i);
			if(cis.sonType ==-cp.getGroupID()){
				for(int j=0;cis.clipArea !=null&&j<cis.clipArea.length;j++){
					if(cis.clipArea[j][5] ==cp.getID()){
						//elt.addAttribute("PICID", ""+(-cis.clipArea[j][4]));
						elt.addAttribute("CLIPX", ""+cis.clipArea[j][0]);
						elt.addAttribute("CLIPY", ""+cis.clipArea[j][1]);
						elt.addAttribute("CLIPW", ""+cis.clipArea[j][2]);
						elt.addAttribute("CLIPH", ""+cis.clipArea[j][3]);
						break;
					}
				}
				break;
			}
		}
	}
	

	/**
	 * 写入xml文件地址
	 * 
	 * @param document
	 *            所属要写入的内容
	 * @param outFile
	 *            文件存放的地址
	 */
	public static void writeDocument(Animation[] anims, String outFile) {
		try {
			// 读取文件
			FileWriter fileWriter = new FileWriter(outFile);
			// 设置文件编码
			OutputFormat xmlFormat = new OutputFormat();
			xmlFormat.setEncoding("UTF-8");
			// 创建写文件方法
			XMLWriter xmlWriter = new XMLWriter(fileWriter, xmlFormat);
			// 写入文件
			Document document =CreateXML.getDocument(anims);
			xmlWriter.write(document);
			// 关闭
			xmlWriter.close();
		} catch (IOException e) {
			System.out.println("文件没有找到");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length == 1) {
			System.out.println("请输入文件存放地址");
			return;
		}
		CreateXML.writeDocument(null, XUtil.getDefPropStr("SavePath") + "\\xml_tmp.xml");
	}
}
