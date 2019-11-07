
#ifndef __MATRIX_MONO_SCRIPTDEBUG__
#define __MATRIX_MONO_SCRIPTDEBUG__

void MM_LOG(const char * pszFormat, ...);

#ifdef WIN32
#include <assert.h>
#define MM_ASSERT(cond)    assert(cond)

#elif defined(ANDROID)
#include "android/log.h"

#define CC_NO_MESSAGE_PSEUDOASSERT(cond)                        \
	if (!(cond)) {                                              \
	__android_log_print(ANDROID_LOG_ERROR,                  \
	"MatrixMono assert",                 \
	"%s function:%s line:%d",           \
	__FILE__, __FUNCTION__, __LINE__);  \
	}

#define CC_MESSAGE_PSEUDOASSERT(cond, msg)                          \
	if (!(cond)) {                                                  \
	__android_log_print(ANDROID_LOG_ERROR,                      \
	"MatrixMono assert",                     \
	"file:%s function:%s line:%d, %s",      \
	__FILE__, __FUNCTION__, __LINE__, msg); \
	}

#define MM_ASSERT(cond) CC_NO_MESSAGE_PSEUDOASSERT(cond)
#endif //end of WIN32

#define mm_assert(cond) { if (!(cond))MM_ASSERT(cond); }
#define mm_printf(exp, ...) { MM_LOG(exp, ##__VA_ARGS__); }
#define mm_error(exp, ...) { MM_LOG(exp, ##__VA_ARGS__); }
#define mm_warning(exp, ...) { MM_LOG(exp, ##__VA_ARGS__); }

#endif // end of __MATRIX_MONO_SCRIPTDEBUG__