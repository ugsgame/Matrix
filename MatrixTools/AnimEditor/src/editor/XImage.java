package editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.Raster;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class XImage {

	private final static void basicWriteByte(int data, DataOutputStream out) throws Exception {
		out.writeByte(data & 0xFF);
	}

	private final static boolean equalsColor(Color color, Color maskColor) {
		if (get4BitValue(color.getRed()) == get4BitValue(maskColor.getRed())
		        && get4BitValue(color.getBlue()) == get4BitValue(maskColor.getBlue())
		        && get4BitValue(color.getGreen()) == get4BitValue(maskColor.getGreen())) { return true; }
		return false;
	}

	public final static int get4BitRGB(Color color) {
		return (get4BitValue(color.getRed()) << 8) | (get4BitValue(color.getGreen()) << 4)
		        | (get4BitValue(color.getBlue()));
	}

	public final static int get4BitValue(int value) {
		return ((value & 0xF0) >> 4);
	}

	// readBMP
	public final static BufferedImage readBMP(File f, Color maskColor) {
		return readBMP(f, maskColor, null);
	}

	public final static BufferedImage readBMP(File f, Color maskColor, XImage image) {
		BufferedImage result = null;
		try {
			FileInputStream fs = new FileInputStream(f);

			// ��ȡ14�ֽ�BMP�ļ�ͷ
			int fileHeadLen = 14;
			byte fileHeadData[] = new byte[fileHeadLen];
			fs.read(fileHeadData, 0, fileHeadLen);
			if (fileHeadData[0] != 'B' || fileHeadData[1] != 'M') throw new Exception("����BMP�ļ�");

			// ��ȡ40�ֽ�BMP��Ϣͷ
			int fileInfoLen = 40;
			byte fileInfoData[] = new byte[fileInfoLen];
			fs.read(fileInfoData, 0, fileInfoLen);

			// ��ȡһЩ��Ҫ����
			int width = ((fileInfoData[7] & 0xFF) << 24)// Դͼ���
			        | ((fileInfoData[6] & 0xFF) << 16)
			        | ((fileInfoData[5] & 0xFF) << 8)
			        | (fileInfoData[4] & 0xFF);
			// System.out.println("��:" + width);

			int height = ((fileInfoData[11] & 0xFF) << 24)// Դͼ�߶�
			        | ((fileInfoData[10] & 0xFF) << 16)
			        | ((fileInfoData[9] & 0xFF) << 8)
			        | (fileInfoData[8] & 0xFF);
			// System.out.println("��:"+height);

			// λ��
			int bitCount = ((fileInfoData[15] & 0xFF) << 8) | (fileInfoData[14] & 0xFF);

			// System.out.println("λ��:" + bitCount);

			int[] argbData = new int[width * height];

			if (bitCount == 24) {// ��24λBMP���н���
				if (image != null) image.palettes = null;
				int pad = (4 - ((width * 3) % 4)) % 4;// ÿ��������3��byte�����ݣ�����һ�е������ܺͱ�����4�ı��������������ʹ��pad�������ݲ���
				// System.out.println("pad = " + pad);
				byte[] fileData = new byte[(width * 3 + pad) * height];
				fs.read(fileData, 0, fileData.length);
				int index = 0;
				int argb;
				for (int y = 0; y < height; ++y) {
					for (int x = 0; x < width; ++x) {
						argb = (0xFF << 24)// Alpha
						        | ((fileData[index + 2] & 0xFF) << 16)// Red
						        | ((fileData[index + 1] & 0xFF) << 8)// Green
						        | (fileData[index] & 0xFF);// Blue
						if (equalsColor(new Color(argb, true), maskColor)) argb = 0;
						argbData[width * (height - 1 - y) + x] = argb;
						index += 3;
					}
					index += pad;
				}
			}
			else if (bitCount == 1) {// ��1λBMP���н���
				int bytesPerRow = ((width + 31) >> 5) << 2; // Bytes Per
				// Row,һ��byte����8�����ص�����
				byte[] fileData = new byte[bytesPerRow * height];
				fs.skip(8);// skip FF FF FF 00 00 00 00 00 ��ɫ�Ͱ�ɫ
				fs.read(fileData, 0, fileData.length);
				for (int y = 0; y < height; ++y) {
					for (int x = 0; x < width; ++x) {
						int bitColor = (~(fileData[y * bytesPerRow + (x >> 3)]) >> (7 - (x % 8))) & 1;
						int argb = (bitColor == 1 ? 0 : 0xFF000000);
						argbData[width * (height - 1 - y) + x] = argb;
					}
				}
			}
			else if (bitCount == 8) {// ��8λBMP���н���
				// ��ȡ��ɫ��
				int paletteCount = ((fileInfoData[35] & 0xFF) << 24)// ��ɫ����
				        | ((fileInfoData[34] & 0xFF) << 16)
				        | ((fileInfoData[33] & 0xFF) << 8)
				        | (fileInfoData[32] & 0xFF);
				if(paletteCount == 0) paletteCount = 256;
				byte[] paletteData = new byte[paletteCount * 4];
				fs.read(paletteData, 0, paletteData.length);
				int[] palettes = new int[paletteCount];
				for (int i = 0; i < paletteCount; ++i) {
					int di = i * 4;
					palettes[i] = ((paletteData[di + 2] & 0xFF) << 16)// ��ɫ
					        | ((paletteData[di + 1] & 0xFF) << 8) | (paletteData[di] & 0xFF);
					palettes[i] &= 0xFFFFFF;
				}
				if (image != null) image.palettes = palettes;
				// ��ȡ����
				int pad = (4 - (width % 4)) % 4;// ÿ��������1��byte�����ݣ�����һ�е������ܺͱ�����4�ı��������������ʹ��pad�������ݲ���
				byte[] fileData = new byte[(width + pad) * height];
				fs.read(fileData, 0, fileData.length);
				int index = 0;
				int argb;
				for (int y = 0; y < height; ++y) {
					for (int x = 0; x < width; ++x) {
						argb = (0xFF << 24) | palettes[(fileData[index] & 0xFF)];
						if (equalsColor(new Color(argb, true), maskColor)) argb = 0;
						argbData[width * (height - 1 - y) + x] = argb;
						++index;
					}
					index += pad;
				}
			}
			else {
				throw new Exception("���� 24 λ��8λ���� 1 λ��BMPͼ��");
			}

			// create Image
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image tmp = kit.createImage(new MemoryImageSource(width, height, argbData, 0, width));
			result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics g = result.createGraphics();
			g.drawImage(tmp, 0, 0, null);
			result.flush();
			fs.close(); // �ر�������
		}
		catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		return result;
	}

	// readPNG
	public final static BufferedImage readPNG(File f, Color maskColor) {
		BufferedImage img;
		try {
			if (!FileExtFilter.getExtension(f).equalsIgnoreCase("png")) { throw new Exception(
			        "����PNGͼƬ"); }
			img = ImageIO.read(f);
		}
		catch (Exception e) {
			img = null;
			e.printStackTrace();
		}
		
		Raster raster = img.getData();
		ColorModel model = img.getColorModel();
		int[] colors = new int[img.getWidth() * img.getHeight()];
		Object data;
		Color color;

		int index = 0;
		for (int y = 0; y < img.getHeight(); ++y) {
			for (int x = 0; x < img.getWidth(); ++x) {
				data = raster.getDataElements(x, y, null);
				color = new Color(model.getRGB(data), true);
				if(maskColor != null) {
					if(equalsColor(color, maskColor)) {
						color = new Color(0, true);
					}
				}
				colors[index++] = color.getRGB();
				
			}
		}

		Toolkit kit = Toolkit.getDefaultToolkit();
		Image tmp = kit.createImage(new MemoryImageSource(img.getWidth(), img.getHeight(), colors, 0, img.getWidth()));
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.createGraphics();
		g.drawImage(tmp, 0, 0, null);
		result.flush();
		
		return result;
	}

	private final static void writeByte(int data, DataOutputStream out) throws Exception {
		basicWriteByte(data, out);
	}

	private final static void writeInt(int data, DataOutputStream out) throws Exception {
		basicWriteByte((data & 0xFF), out);
		basicWriteByte(((data & 0xFF00) >> 8), out);
		basicWriteByte(((data & 0xFF0000) >> 16), out);
		basicWriteByte(((data & 0xFF000000) >> 24), out);
	}

	private final static void writeShort(int data, DataOutputStream out) throws Exception {
		basicWriteByte((data & 0xFF), out);
		basicWriteByte(((data & 0xFF00) >> 8), out);
	}

	private int left, top, width, height, basicWidth, basicHeight;
	private BufferedImage image;
	private Color[][] argbColor;
	private File fparent;
	private String name;

	private String ext;

	private Color maskColor;

	private int id;

	private boolean changed;

	private int[] palettes = null;
	
	private boolean solid = false;

	public XImage(File f, Color maskColor) throws Exception {
		id = 0;
		read(f, maskColor);
	}

	public boolean equals(XImage other) {
		if (other == null) return false;
		if (other.width != this.width || other.height != this.height) return false;

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				Color selfColor = this.argbColor[x][y], otherColor = other.argbColor[x][y];

				if (selfColor.getAlpha() != otherColor.getAlpha()
				        || selfColor.getRed() != otherColor.getRed()
				        || selfColor.getGreen() != otherColor.getGreen()
				        || selfColor.getBlue() != otherColor.getBlue()) return false;

				if (!equalsColor(selfColor, otherColor)) return false;
			}
		}

		return true;
	}

	public int getBasicHeight() {
		return basicHeight;
	}

	public int getBasicWidth() {
		return basicWidth;
	}

	public Color[][] getData() {
		return argbColor;
	}

	public String getExtension() {
		return ext;
	}

	public int getHeight() {
		return height;
	}

	public int getID() {
		return id;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getLeft() {
		return left;
	}

	public String getName() {
		return name;
	}

	public int getTop() {
		return top;
	}

	public int getWidth() {
		return width;
	}

	public boolean isChanged() {
		return changed;
	}

	public void read(File f, Color maskColor) throws Exception {
		fparent = new File(f.getParent());
		name = FileExtFilter.getName(f);
		ext = FileExtFilter.getExtension(f);
		this.maskColor = maskColor;

		BufferedImage basicImage = null;
		if (ext.equalsIgnoreCase("png")) {
			basicImage = readPNG(f, maskColor);
		}
		else if (ext.equalsIgnoreCase("bmp")) {
			basicImage = readBMP(f, maskColor, this);
		}
		if (basicImage == null) throw new Exception("�޷���ȡԴ�ļ�" + f.getAbsolutePath());
		basicWidth = basicImage.getWidth();
		basicHeight = basicImage.getHeight();

		Raster raster = basicImage.getData();
		ColorModel model = basicImage.getColorModel();
		Color[][] colors = new Color[basicImage.getWidth()][basicImage.getHeight()];
		Object data;
		Color color;

		for (int y = 0; y < basicImage.getHeight(); ++y) {
			for (int x = 0; x < basicImage.getWidth(); ++x) {
				data = raster.getDataElements(x, y, null);
				color = new Color(model.getRGB(data), true);
				colors[x][y] = color;
			}
		}

		boolean found;
		int minX = 0;
		found = false;
		while (minX < basicImage.getWidth() && !found) {
			for (int y = 0; y < basicImage.getHeight(); ++y) {
				if (colors[minX][y].getAlpha() != 0) {
					found = true;
					break;
				}
			}
			if (!found) ++minX;
		}
		if (minX >= basicImage.getWidth()) minX = basicImage.getWidth() - 1;

		int maxX = basicImage.getWidth() - 1;
		found = false;
		while (maxX >= minX && !found) {
			for (int y = 0; y < basicImage.getHeight(); ++y) {
				if (colors[maxX][y].getAlpha() != 0) {
					found = true;
					break;
				}
			}
			if (!found) --maxX;
		}
		if (maxX < minX) maxX = minX;

		int minY = 0;
		found = false;
		while (minY < basicImage.getHeight() && !found) {
			for (int x = 0; x < basicImage.getWidth(); ++x) {
				if (colors[x][minY].getAlpha() != 0) {
					found = true;
					break;
				}
			}
			if (!found) ++minY;
		}
		if (minY >= basicImage.getHeight()) minY = basicImage.getHeight() - 1;

		int maxY = basicImage.getHeight() - 1;
		found = false;
		while (maxY >= minY && !found) {
			for (int x = 0; x < basicImage.getWidth(); ++x) {
				if (colors[x][maxY].getAlpha() != 0) {
					found = true;
					break;
				}
			}
			if (!found) --maxY;
		}
		if (maxY < minY) maxY = minY;

		// create real data
		left = minX;
		top = minY;
		width = maxX - minX + 1;
		height = maxY - minY + 1;
		changed = !(left == 0 && top == 0 && width == basicWidth && height == basicHeight);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		g.drawImage(basicImage, -left, -top, null);//�滭
		image.flush();

		argbColor = new Color[width][height];
		solid = true;
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				argbColor[x][y] = colors[left + x][top + y];
				int alpha = argbColor[x][y].getAlpha();
				if(alpha != 0 && alpha != 0xFF) {
					solid = false;
				}
			}
		}
		// if(name.equals("0100000")) {
		// Color c = argbColor[33][26];
		// System.out.println(name + " " + c.getRed() + " " + c.getGreen() + " "
		// + c.getBlue());
		// }
	}

	public void save() throws Exception {
		save(fparent);
	}

	public void save(File parent) throws Exception {
		if (ext.equalsIgnoreCase("png")) {
			savePNG(parent);
		}
		else if (ext.equalsIgnoreCase("bmp")) {
			saveBMP(parent);
		}
	}

	private void saveBMP(File parent) throws Exception {
		String fileFullName = parent.getPath() + "\\" + name + ".bmp";
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
		        fileFullName)));
		if(palettes == null) {
			saveBMP24(out);
		}
		else {
			saveBMP8(out);
		}
		out.flush();
		out.close();
	}
	
	private void saveBMP24(DataOutputStream out) throws Exception {
		int pad = (4 - ((width * 3) % 4)) % 4;// ÿ��������3��byte�����ݣ�����һ�е������ܺͱ�����4�ı��������������ʹ��pad�������ݲ���

		// 14�ֽڵ��ļ�ͷ
		writeByte(0x42, out);// 'B'
		writeByte(0x4D, out);// 'M'
		int fileSize = 14 + 40 + ((width * 3) + pad) * height + 2;// ����2��Ϊ��ʹ�����ļ���С�ܹ���4����
		writeInt(fileSize, out);
		writeInt(0, out);
		writeInt(0x36, out);// 14 + 40 = 54 = 0x36

		// 40�ֽ���Ϣͷ
		writeInt(40, out);// ��Ϣͷ����
		writeInt(width, out);
		writeInt(height, out);
		writeShort(1, out);
		writeShort(24, out);// 24λBMP
		writeInt(0, out);// ��ѹ��
		writeInt(0, out);// 24λͼ��ѹ���Ļ��˴���Ϊ0
		writeInt(0xB12, out);// horizontal resolution: Pixels/meter
		writeInt(0xB12, out);// vertical resolution: Pixels/meter
		writeInt(0, out);// ��ͼ��ʵ���õ�����ɫ���������ֵΪ0�����õ�����ɫ��Ϊ2��(��ɫλ��)����
		writeInt(0, out);// ָ����ͼ������Ҫ����ɫ���������ֵΪ0������Ϊ���е���ɫ������Ҫ��

		// ����
		for (int y = height - 1; y >= 0; --y) {
			for (int x = 0; x < width; ++x) {
				if (argbColor[x][y].getAlpha() == 0) {// �����͸�������ΪmaskColor
					writeByte(maskColor.getBlue(), out);
					writeByte(maskColor.getGreen(), out);
					writeByte(maskColor.getRed(), out);
				}
				else {// �����Ϊԭʼ��ɫ
					writeByte(argbColor[x][y].getBlue(), out);
					writeByte(argbColor[x][y].getGreen(), out);
					writeByte(argbColor[x][y].getRed(), out);
				}
			}
			for (int p = 0; p < pad; ++p) {// ��0
				writeByte(0, out);
			}
		}

		writeByte(0, out);
		writeByte(0, out);
	}
	
	private void saveBMP8(DataOutputStream out) throws Exception {
		int pad = (4 - (width % 4)) % 4;// ÿ��������1��byte�����ݣ�����һ�е������ܺͱ�����4�ı��������������ʹ��pad�������ݲ���
		
		// 14�ֽڵ��ļ�ͷ
		writeByte(0x42, out);// 'B'
		writeByte(0x4D, out);// 'M'
		int fileSize = 14 + 40 + palettes.length * 4 + (width + pad) * height + 2;// ����2��Ϊ��ʹ�����ļ���С�ܹ���4����
		writeInt(fileSize, out);
		writeInt(0, out);
		writeInt(54 + palettes.length * 4, out);// 14 + 40 = 54

		// 40�ֽ���Ϣͷ
		writeInt(40, out);// ��Ϣͷ����
		writeInt(width, out);
		writeInt(height, out);
		writeShort(1, out);
		writeShort(8, out);// 8λBMP
		writeInt(0, out);// ��ѹ��
		writeInt((width + pad) * height + 2, out);// ��ѹ���Ļ��˴���Ϊ0��һ���ʾ����ͷ��Ϣ�͵�ɫ��֮��ʣ�µ������ֽ���
		writeInt(0xB12, out);// horizontal resolution: Pixels/meter
		writeInt(0xB12, out);// vertical resolution: Pixels/meter
		writeInt(((palettes.length == 256) ? 0 : palettes.length), out);// ��ͼ��ʵ���õ�����ɫ���������ֵΪ0�����õ�����ɫ��Ϊ2��(��ɫλ��)����
		writeInt(((palettes.length == 256) ? 0 : palettes.length), out);// ָ����ͼ������Ҫ����ɫ���������ֵΪ0������Ϊ���е���ɫ������Ҫ��
		
		for(int i = 0; i < palettes.length; ++i) {
			writeInt(palettes[i], out);
		}
		
		// ����
		for (int y = height - 1; y >= 0; --y) {
			for (int x = 0; x < width; ++x) {
				Color color;
				if (argbColor[x][y].getAlpha() == 0) {// �����͸�������ΪmaskColor
					color = maskColor;
				}
				else {// �����Ϊԭʼ��ɫ
					color = argbColor[x][y];
				}
				for(int i = 0; i < palettes.length; ++i) {
					if(color.equals(new Color(palettes[i]))) {
						writeByte(i, out);
						break;
					}
				}
			}
			for (int p = 0; p < pad; ++p) {// ��0
				writeByte(0, out);
			}
		}

		writeByte(0, out);
		writeByte(0, out);
	}
	
	public void saveXMG(File file) throws Exception {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		if(solid) {
			g.setColor(new Color(0xFFFF00FF));
			g.fillRect(0, 0, width, height);
		}
		g.drawImage(image, 0, 0, null);
		img.flush();
		
		ImageIO.write(image, "png", file);
		Runtime.getRuntime().exec("png2xmg.exe " + file.getAbsolutePath());
		//System.out.println(file);
	}

	public void savePNG(File parent) throws Exception {
		if (XUtil.getDefPropInt("Type") == 0) {// Symbian
			//saveRWA(parent);
		}
		File f = new File(parent.getPath() + "\\" + name + ".png");
		ImageIO.write(image, "png", f);
	}

	private void saveRWA(File parent) throws Exception {
		String fileFullName = parent.getPath() + "\\" + name + ".rwa";
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
		        fileFullName)));

		writeInt(width, out);
		writeInt(height, out);
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				writeByte(argbColor[x][y].getAlpha(), out);
				writeByte(argbColor[x][y].getRed(), out);
				writeByte(argbColor[x][y].getGreen(), out);
				writeByte(argbColor[x][y].getBlue(), out);
			}
		}
		out.flush();
		out.close();
	}

	public void setID(int id) {
		this.id = id;
	}
}