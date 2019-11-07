

#ifndef ICONV_STRING_H
#define ICONV_STRING_H

#include <string>
#include "iconv.h"



#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32) 
// �������ӵ�ʱ��ָ����̬��  
#pragma comment(lib,"libiconv.lib") 
#endif  

int code_convert(char *from_charset,char *to_charset,char *inbuf,int inlen,char *outbuf,int outlen) 
{ 
	iconv_t cd; 
	char **pin = (char**)&inbuf; 
	char **pout = &outbuf; 

	cd = iconv_open(to_charset,from_charset);
	if (cd==0) return -1; 
	memset(outbuf,0,outlen);
#if(CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
	if (iconv(cd,pin,(size_t *)&inlen,pout,(size_t *)&outlen)==-1) return -1;
#else
	if (iconv(cd,(const char **)pin,(size_t *)&inlen,pout,(size_t *)&outlen)==-1) return -1;
#endif
	iconv_close(cd);
	return 0; 
} 

int  convert(char  *from_charset, char  *to_charset, char  *inbuf, size_t  inlen, char *outbuf,size_t outlen)
{ 
	iconv_t iconvH;
	iconvH = iconv_open(to_charset, from_charset);
	if( !iconvH )return NULL;
	memset(outbuf, 0, outlen);

#if(CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
	const char *temp = inbuf; 
	const char **pin = &temp; 
	char** pout = &outbuf; 

	if( !iconv(iconvH, pin, &inlen, pout, &outlen) )
	{
		iconv_close(iconvH);
		return NULL; 
	}
#else  
	if( !iconv(iconvH, &inbuf, &inlen, &outbuf, &outlen) )
	{
		iconv_close(iconvH);
		return NULL; 
	}

#endif  
	iconv_close(iconvH);

	return NULL; 

} 

int gbk2utf8(char  *inbuf, size_t  inlen, char *outbuf,size_t outlen)

{ 
	return convert("gb2312","utf-8", inbuf, inlen, outbuf, outlen);
} 

#endif//