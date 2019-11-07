
#include "ScriptDebug.h"

#include <string.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>


static const int kMaxLogLen = 16*1024;
#define MAX_LEN         (kMaxLogLen + 1)

#ifdef WIN32
#include <Windows.h>

void MM_LOG(const char * pszFormat, ...)
{
	char szBuf[MAX_LEN];

	va_list ap;
	va_start(ap, pszFormat);
	vsnprintf_s(szBuf, MAX_LEN, MAX_LEN, pszFormat, ap);
	va_end(ap);

	WCHAR wszBuf[MAX_LEN] = {0};
	MultiByteToWideChar(CP_UTF8, 0, szBuf, -1, wszBuf, sizeof(wszBuf));
	OutputDebugStringW(wszBuf);
	OutputDebugStringA("\n");

	WideCharToMultiByte(CP_ACP, 0, wszBuf, sizeof(wszBuf), szBuf, sizeof(szBuf), NULL, FALSE);
	printf("MatrixMono debug info: %s\n", szBuf);
}

#elif defined(ANDROID)
void MM_LOG(const char * pszFormat, ...)
{
	char buf[MAX_LEN];

	va_list args;
	va_start(args, pszFormat);
	vsnprintf(buf, MAX_LEN, pszFormat, args);
	va_end(args);

	__android_log_print(ANDROID_LOG_DEBUG, "MatrixMono debug info:", "%s", buf);
}

#endif // WIN32


