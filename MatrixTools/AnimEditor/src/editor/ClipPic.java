package editor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;

class ClipPicPanel extends PainterPanel{
	
	ClipPic clip;
	
	public ClipPicPanel(BufferedImage img, int id, int grid ,int x, int y, int w, int h){
		clip = new ClipPic(img, id, grid, x, y, w, h);
		computeSize();
	}
	
	public ClipPicPanel(BufferedImage img, int x, int y, int grid, int id, int cx, int cy, int cw, int ch){
		clip = new ClipPic(img, x, y, grid, id, cx, cy, cw, ch);
	}
	
	public String getInfo(){
		return clip.getInfo();
	}
	
	public boolean inRect(int left, int top, int width, int height) {
		return clip.inRect(left, top, width, height);
	}
	
	public int getLogicId() {
		return clip.getLogicId();
	}
	
	public void setLogicId(int logicId) {
		clip.setLogicId(logicId);
	}
	
	public void setName(String name) {
		clip.setName(name);
	}
	
	public void setLevelFilp(){
		clip.levelFilp = !clip.levelFilp;
	}
	
	public void setVerticalFilp(){
		clip.verticalFilp = !clip.verticalFilp;
	}
	public void updateInfo(BufferedImage img, int[] clipArr){
		clip.updateInfo(img, clipArr);
	}
	public void export(DataOutputStream out) throws Exception {
		clip.export(out);
	}
	
	public void save(DataOutputStream out) throws Exception {
		clip.save(out);
	}
	public void load(DataInputStream in) throws Exception {
		clip.load(in);
	}
	public void getLoadImg(){
		clip.getLoadImg();
	}
	
	public void updateImage() {
		clip.updateImage();
	}
	
	public double getScale(){
		return clip.scale;
	}
	
	public void paint(Graphics g, boolean isFlip, float alpha){
		clip.paint(g, isFlip, alpha);
		
	}
	public int returnMouState(int mousX, int mousY){
		return clip.returnMouState(mousX, mousY);
	}
	private int[] changeCorPos(int corx, int cory){
		return clip.changeCorPos(corx, cory);
	}
	public boolean isluo(int x, int y){
		return clip.isluo(x, y);
	}
	public void paint(Graphics g, int left, int top) {
		clip.paint(g, left, top);
	}

	public int getGroupID() {
		// TODO Auto-generated method stub
		return clip.getGroupID();
	}

	public int getID() {
		// TODO Auto-generated method stub
		return clip.getID();
	}
	
	public String getName() { 
		return clip.getName();
	}

	public int getImageHeight() {
		// TODO Auto-generated method stub
		return clip.getImageHeight();
	}

	public int getImageWidth() {
		// TODO Auto-generated method stub
		return clip.getImageWidth();
	}

	public void paintLeftTop(Graphics g, int left, int top) {
		// TODO Auto-generated method stub
		clip.paintLeftTop(g, left, top);
	}

	public void paintOrigin(Graphics g, int originX, int originY) {
		// TODO Auto-generated method stub
		clip.paintOrigin(g, originX, originY);
	}
	
	
}

class ClipPic {
	
	public BufferedImage image;
	public BufferedImage scaleImg;
	public BufferedImage drawImg;
	private int id;
	private int groupId;
	int logicId;
	public int posX, posY;
	boolean isSelect;//是否被选择
	double scale;//缩放比
	int oldW, oldH;//原始宽高
	double jiao;
	boolean levelFilp;//水平翻转
	boolean verticalFilp;//垂直翻转
	public int cutx, cuty, cutw, cuth;
	public String name;
	public int alpha;
	public int layer;
	
	public ClipPic(BufferedImage img, int id, int grid ,int x, int y, int w, int h){
		this.image =img;
		this.groupId =grid;
		this.id =id;
		this.logicId = 0;
		this.cutx =x;
		this.cuty =y;
		this.cutw =w;
		this.cuth =h;
		this.name = "切块"+id;
		this.alpha = 255;
		this.layer = 20;
	}
	public ClipPic(BufferedImage img, int x, int y, int grid, int id, int cx, int cy, int cw, int ch){
		this.image =img;
		this.groupId =grid;
		this.id =id;
		posX =x;
		posY =y;
		this.cutx =cx;
		this.cuty =cy;
		this.cutw =cw;
		this.cuth =ch;
		scaleImg =img;
		drawImg = img;
		oldW = img==null?1:img.getWidth();
		oldH = img==null?1:img.getHeight();
		// the under must reload
		logicId = 0;
		alpha = 255;
		scale =1.0;
		levelFilp =false;
		verticalFilp =false;
		layer = 20;
	}
	
	public String getInfo(){
		String s = "";
		s = s + "id:"+logicId+", layer:"+layer+", l:" + (posX-drawImg.getWidth()/2) + ", r:" + (posY-drawImg.getHeight()/2) + ", w:" + drawImg.getWidth() + ", h:" + drawImg.getHeight() + 
				", scale:" + String.valueOf(((int)(scale*100)/100.0f)) +
				", alpha:" + alpha +
				", angle:" + String.valueOf((int)jiao);
		return s;
	}
	
	public int[] getRect() {
		return new int[]{(posX-drawImg.getWidth()/2), posY-drawImg.getHeight()/2, drawImg.getWidth(), drawImg.getHeight()};
	}
	
	public boolean inRect(int left, int top, int width, int height) {
		return (posX-drawImg.getWidth()/2 >= left && posX+drawImg.getWidth()/2 <= left + width
		        && posY-drawImg.getHeight()/2 >= top && posY+drawImg.getHeight()/2 <= top + height);
	}
	
	public int getLogicId() {
		return logicId;
	}
	
	public void setLogicId(int logicId) {
		this.logicId = logicId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLevelFilp(){
		levelFilp = !levelFilp;
		
		AffineTransform at = new AffineTransform(-1, 0, 0, 1, drawImg.getWidth(), 0);
		AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		scaleImg =ato.filter(drawImg, null);
		drawImg =scaleImg;
	}
	
	public void setVerticalFilp(){
		verticalFilp = !verticalFilp;
		
		AffineTransform at = new AffineTransform(1, 0, 0, -1, 0, drawImg.getHeight());
		AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		scaleImg =ato.filter(drawImg, null);
		drawImg =scaleImg;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
		
		Image tepImg =image.getScaledInstance((int)(image.getWidth()*scale), (int)(image.getHeight()*scale), Image.SCALE_DEFAULT);
		drawImg =new BufferedImage(tepImg.getWidth(null), tepImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = drawImg.createGraphics(); 
		g.drawImage(tepImg, 0, 0, null);
		g.dispose(); 
	}
	
	public void updateInfo(BufferedImage img, int[] clipArr){
		this.image =img;
	//	scale =1.0;
	//	jiao =0.0;
		this.cutx =clipArr[0];
		this.cuty =clipArr[1];
		this.cutw =clipArr[2];
		this.cuth =clipArr[3];
	//	levelFilp =false;
	//	verticalFilp =false;
		getLoadImg();
	}
	public void export(DataOutputStream out) throws Exception {
		// start v100, 2013.7.27
		out.writeInt(logicId);
		// end
		// start v101, 2013.8.5
		out.writeInt(layer);
		// end
		out.writeInt(posX);
		out.writeInt(posY);
		out.writeDouble(scale);
		out.writeDouble(jiao);
		out.writeBoolean(levelFilp);
		out.writeBoolean(verticalFilp);
		// start v100, 2013.7.27
		out.writeInt(alpha);
		// end
		for(int i=0;i<MainFrame.self.picNameList.length;i++){
			if(MainFrame.self.picNameList[i] ==-groupId){
				out.writeInt(i+1);//图片ID
				break;
			}
		}
	//	out.writeInt(-groupId);//图片ID
		out.writeInt(cutx);
		out.writeInt(cuty);
		out.writeInt(cutw);
		out.writeInt(cuth);
	}
	
	public void save(DataOutputStream out) throws Exception {
		out.writeInt(groupId);
		out.writeInt(id);
		out.writeInt(layer);
		out.writeInt(posX);
		out.writeInt(posY);
		out.writeDouble(scale);
		out.writeDouble(jiao);
		out.writeBoolean(levelFilp);
		out.writeBoolean(verticalFilp);
		// start 2013.7.27
		out.writeInt(alpha);
		out.writeInt(logicId);
		// end
		out.writeInt(cutx);
		out.writeInt(cuty);
		out.writeInt(cutw);
		out.writeInt(cuth);
	}
	public void load(DataInputStream in) throws Exception {
		groupId =in.readInt();
		id =in.readInt();
		// start v101, 2013.8.5
		if(MainFrame.self.verLoad == 101) {
			layer = in.readInt();
		}
		// end
		posX =in.readInt();
		posY =in.readInt();
		scale =in.readDouble();
		jiao =in.readDouble();
		levelFilp =in.readBoolean();
		verticalFilp =in.readBoolean();
		// start v100, 2013.7.27
		alpha = in.readInt();
		logicId = in.readInt();
//		logicId = MainFrame.self.getAnimPanel().getAnim().getCutId();
		// end
		cutx =in.readInt();
		cuty =in.readInt();
		cutw =in.readInt();
		cuth =in.readInt();
		
		//System.out.println("groud id ="+groupId+"god id="+id);
		/*for(int i=0;i<MainFrame.clipArr.size();i++){
			clipInfoSave cis =(clipInfoSave)MainFrame.clipArr.get(i);
			if(cis.sonType ==-groupId){
				for(int j=0;cis.clipArea !=null&&j<cis.clipArea.length;j++){
					if(cis.clipArea[j][5] ==id){
						cutx =cis.clipArea[j][0];
						cuty =cis.clipArea[j][1];
						cutw =cis.clipArea[j][2];
						cuth =cis.clipArea[j][3];
						break;
					}
				}
				break;
			}
		}*/
		
		getLoadImg();
//		saveX =posX;
//		saveY =posY;
	}
	public void getLoadImg(){
		//重载图片
		ClipPicPanel pt;
		BufferedImage tempImg;
		if(image == null) {
			pt =(ClipPicPanel)(MainFrame.self.trees[MainFrame.LAYER_CLIP].getClipPainterById(groupId, id));
			if(pt == null){
				scaleImg = null;
				drawImg = null;
				return;
			}
			image =pt.clip.image;
		}
		
		tempImg =image;
		oldW =tempImg.getWidth();
		oldH =tempImg.getHeight();
		
		if(scale != 1){
			Image tepImg =tempImg.getScaledInstance((int)(tempImg.getWidth()*scale), (int)(tempImg.getHeight()*scale), Image.SCALE_DEFAULT);
			scaleImg =new BufferedImage(tepImg.getWidth(null), tepImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g = scaleImg.createGraphics(); 
			g.drawImage(tepImg, 0, 0, null);
			g.dispose(); 
		}else{
			scaleImg =tempImg;
		}
		if(levelFilp){
			tempImg =scaleImg;
			AffineTransform at = new AffineTransform(-1, 0, 0, 1, tempImg.getWidth(), 0);
			AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			scaleImg =ato.filter(tempImg, null);
		}
		if(verticalFilp){
			tempImg =scaleImg;
			AffineTransform at = new AffineTransform(1, 0, 0, -1, 0, tempImg.getHeight());
			AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			scaleImg =ato.filter(tempImg, null);
		}
		
		drawImg =scaleImg;
	}
	
	public void updateImage() {
		getLoadImg();
//		double s = (oldW*scale)/image.getWidth();
//		Image tepImg =image.getScaledInstance((int)(image.getWidth()*s), (int)(image.getHeight()*s), Image.SCALE_DEFAULT);
//		scaleImg =new BufferedImage(tepImg.getWidth(null), tepImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
//		Graphics g = scaleImg.createGraphics(); 
//		g.drawImage(tepImg, 0, 0, null);
//		g.dispose(); 
	}
	
	public double getScale(){
		return scale;
	}
	
	public void paint(Graphics g, boolean isFlip, float alpha){
		float tempAlpha = alpha;
		if(tempAlpha == 1) {
			tempAlpha = (float)this.alpha/255;
			if(tempAlpha == 0)
				tempAlpha = 0.1f;
		}
		Graphics2D g2 =(Graphics2D)g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, tempAlpha));
		AffineTransform oldTransform = g2.getTransform();
//		AffineTransform oldTrans = new AffineTransform(g2.getTransform());
		
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); // 这里使用双线性过滤插件算法 VALUE_INTERPOLATION_BICUBIC   VALUE_INTERPOLATION_BILINEAR
		g2.rotate(Math.toRadians(jiao),posX, posY);
		
		if(scale > 0)
			g2.drawImage(drawImg, posX -drawImg.getWidth()/2, posY -drawImg.getHeight()/2, null);
		g2.setComposite(oldComposite);
		if(isSelect){
			if(MainFrame.editType ==0){
				g2.setColor(Color.black);
				int cornerX = (int) ((posX -drawImg.getWidth()/2 + RectShape.CORNER_OFFSETS[RectShape.CORNERS[4]][0] * drawImg.getWidth()))- (RectShape.CORNER_SIZE / 2);
				int cornerY = (int) ((posY -drawImg.getHeight()/2 + RectShape.CORNER_OFFSETS[RectShape.CORNERS[4]][1] * drawImg.getHeight())) - (RectShape.CORNER_SIZE / 2);
				g2.fillRect(cornerX, cornerY, RectShape.CORNER_SIZE , RectShape.CORNER_SIZE );
				
				int compositeRule = AlphaComposite.SRC_OVER; //源排斥目标法合成规则  
				AlphaComposite alphaComposite=AlphaComposite.getInstance(compositeRule,0.3f); //创建AlphaComposite对象 半透率
				g2.setComposite(alphaComposite);
				g2.setColor(Color.RED);
				g2.fillRect(posX -drawImg.getWidth()/2, posY -drawImg.getHeight()/2, drawImg.getWidth(), drawImg.getHeight());
				g2.setColor(Color.WHITE);
				g2.drawRect(posX -drawImg.getWidth()/2, posY -drawImg.getHeight()/2, drawImg.getWidth(), drawImg.getHeight());
			}else {
				/*int compositeRule = AlphaComposite.SRC_OVER; //源排斥目标法合成规则  
				AlphaComposite alphaComposite=AlphaComposite.getInstance(compositeRule,0.4f); //创建AlphaComposite对象 半透率
				g2.setComposite(alphaComposite);
				g2.setColor(Color.WHITE);
				g2.fillRect(posX -drawImg.getWidth()/2, posY -drawImg.getHeight()/2, drawImg.getWidth(), drawImg.getHeight());*/
				
				g2.setColor(Color.RED);
				g2.drawRect(posX -drawImg.getWidth()/2, posY -drawImg.getHeight()/2, drawImg.getWidth(), drawImg.getHeight());
				g2.setColor(Color.black);
				for (int i = 0; i < RectShape.CORNERS.length; ++i) {
					int cornerX = (int) ((posX -drawImg.getWidth()/2 + RectShape.CORNER_OFFSETS[RectShape.CORNERS[i]][0] * drawImg.getWidth())) + (int)((RectShape.CORNER_OFFSETS[RectShape.CORNERS[i]][0]-1) * RectShape.CORNER_SIZE);
					int cornerY = (int) ((posY -drawImg.getHeight()/2 + RectShape.CORNER_OFFSETS[RectShape.CORNERS[i]][1] * drawImg.getHeight())) + (int)((RectShape.CORNER_OFFSETS[RectShape.CORNERS[i]][1]-1) * RectShape.CORNER_SIZE);
					g2.fillRect(cornerX, cornerY, RectShape.CORNER_SIZE , RectShape.CORNER_SIZE );
				}
			}
		}
			
		g2.setTransform(oldTransform);
		g2.setComposite(oldComposite);
		
	}
	public int returnMouState(int mousX, int mousY){
		if(isSelect){
			if(MainFrame.editType == 1) {
				int mul = 1;
				for (int i = 0; i < RectShape.CORNERS.length; ++i) {
					int cornerX = (int) ((posX -drawImg.getWidth()/2 + RectShape.CORNER_OFFSETS[RectShape.CORNERS[i]][0] * drawImg.getWidth()));
					int cornerY = (int) ((posY -drawImg.getHeight()/2 + RectShape.CORNER_OFFSETS[RectShape.CORNERS[i]][1] * drawImg.getHeight()));
					int[] tmpInt = new int[]{cornerX, cornerY};
					if(jiao != 0 && i != 4)
						tmpInt = changeCorPos(cornerX, cornerY);
					tmpInt = changePosOfPos(mousX, mousY, tmpInt[0], tmpInt[1]);
					cornerX =tmpInt[0];
					cornerY =tmpInt[1];
					
					if(Tools.checkBoxInter(-RectShape.CORNER_SIZE*mul, -RectShape.CORNER_SIZE*mul, RectShape.CORNER_SIZE*mul*2, RectShape.CORNER_SIZE*mul*2, cornerX, cornerY, 0, 0)){
						return i;
					}
				}
			}
		}
		
		int temp[] = changePosOfPos(mousX, mousY, posX, posY);
		if(Tools.checkBoxInter(-drawImg.getWidth()/2, -drawImg.getHeight()/2, drawImg.getWidth(), drawImg.getHeight(), temp[0], temp[1], 0, 0)){
			return RectShape.CORNER_CENTER_MIDDLE-1;
		}
		
		return -1;
	}
	
	public int[] changePosOfPos(int x, int y, int cx, int cy) {
		int dx = x-cx;
		int dy = y-cy;
		double len = Math.sqrt(Math.pow(dy, 2)+Math.pow(dx, 2));
		double angle = dx==0?(Math.PI/2):Math.atan((double)dy/dx);
		if(dx < 0 || (dx == 0 && dy < 0)) {
			angle = Math.PI+angle;
		}
		
		int[] result =new int[2];
		result[0] = (int)(len*Math.cos(angle-Math.toRadians(jiao)));
		result[1] = (int)(len*Math.sin(angle-Math.toRadians(jiao)));
		
		return result;
	}

	public int[] changeCorPos(int corx, int cory){
		int[] result =new int[2];
		double zb1 =Math.abs(corx -this.posX);
		double zb2 =Math.abs(cory -this.posY);
		double xbl =Math.sqrt(zb1*zb1+zb2*zb2);
		double C =0;
		
		int a =corx -this.posX;
		int b =cory -this.posY;
		
		C = a==0?(Math.PI/2):Math.atan((double)b/a);
		if(a < 0 || (a == 0 && b < 0)) {
			C = Math.PI+C;
		}
		C = Math.toDegrees(C) + jiao;
		
		result[0] = posX+(int)(xbl*Math.cos(Math.toRadians(C)));
		result[1] = posY+(int)(xbl*Math.sin(Math.toRadians(C)));
		
		return result;
	}
	
	public boolean isluo(int x, int y){
		int mul = 1;
		for (int i = 0; i < RectShape.CORNERS.length; ++i) {
			int cornerX = (int) ((posX -drawImg.getWidth()/2 + RectShape.CORNER_OFFSETS[RectShape.CORNERS[i]][0] * drawImg.getWidth()));
			int cornerY = (int) ((posY -drawImg.getHeight()/2 + RectShape.CORNER_OFFSETS[RectShape.CORNERS[i]][1] * drawImg.getHeight()));
			int[] tmpInt = new int[]{cornerX, cornerY};
			if(jiao != 0 && i != 4)
				tmpInt = changeCorPos(cornerX, cornerY);
			tmpInt = changePosOfPos(x, y, tmpInt[0], tmpInt[1]);
			cornerX =tmpInt[0];
			cornerY =tmpInt[1];
			
			if(Tools.checkBoxInter(-RectShape.CORNER_SIZE*mul, -RectShape.CORNER_SIZE*mul, RectShape.CORNER_SIZE*mul*2, RectShape.CORNER_SIZE*mul*2, cornerX, cornerY, 0, 0)){
				return true;
			}
		}
		
		int temp[] = changePosOfPos(x, y, posX, posY);
		if(Tools.checkBoxInter(-drawImg.getWidth()/2, -drawImg.getHeight()/2, drawImg.getWidth(), drawImg.getHeight(), temp[0], temp[1], 0, 0)){
			return true;
		}
		
		return false;
	}
	
	public void paint(Graphics g, int left, int top) {
		g.drawImage(image, left, top, null);
	}
	//@Override
	public int getGroupID() {
		// TODO Auto-generated method stub
		return groupId;
	}

	//@Override
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}

	//@Override
	public int getImageHeight() {
		return image.getHeight();
	}

	//@Override
	public int getImageWidth() {
		return image.getWidth();
	}
	public String getName() { 
		return id+"_"+name;
	}
	//@Override
	public void paintLeftTop(Graphics g, int left, int top) {
		paint(g, left, top);
	}

	//@Override
	public void paintOrigin(Graphics g, int originX, int originY) {
		// TODO Auto-generated method stub
		
	}
	
}