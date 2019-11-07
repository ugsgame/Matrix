
#ifndef __QC_ARCHIVE__
#define __QC_ARCHIVE__

#include <string>
#include <map>
#include <vector>

struct ArmatureContainer;

class Archive
{
public:
	Archive();
	~Archive();

	static Archive* shareArchive();

	bool load();
	bool save();

	//main frame size
	void setMainSize(int w,int h);
	int getMainSizeW();
	int getMainSizeH();
	//open file state
	std::string getFileOpenPath();
	void setFileOpenPath(std::string filePath);
	//
	std::vector<std::string> getArmaturePaths();
	void setArmaturePaths(std::vector<std::string> container);
	//

protected:

	const unsigned char* createBuffer(int pSize);
	void deleteBuffer(const unsigned char* buffer);

	void writeBuffer(FILE* fp,int size, const unsigned char* buffer);
	const unsigned char* readBuffer(FILE* fp,int &pSize);

	void writeBufferByte(const unsigned char* buffer,int size,int& seek,unsigned char _byte);
	unsigned char readBufferByte(const unsigned char* buffer,int size,int& seek);

	void writeBufferShort(const unsigned char* buffer,int size,int& seek,short _short);
	short readBufferShort(const unsigned char* buffer,int size,int& seek);

	void writeBufferInt(const unsigned char* buffer,int size,int& seek,int _Int);
	int readBufferInt(const unsigned char* buffer,int size,int& seek);

	void  writeBufferFloat(const unsigned char* buffer,int size,int& seek,float _float);
	float readBufferFloat(const unsigned char* buffer,int size,int& seek);

	void writeBufferString(const unsigned char* buffer,int size,int& seek,std::string str );
	std::string readBufferString(const unsigned char* buffer,int size,int& seek);
private:

	static Archive* _shareArchive;

	int width,height;

	std::string openFileDir;

	std::vector<std::string> armatureContainer;
};

#endif