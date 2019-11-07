/****************************************************************************
 Copyright (c) 2013 Kevin Sun and RenRen Games

 email:happykevins@gmail.com
 http://wan.renren.com
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/
#ifndef __DFONT_CONFIG_H__ 
#define __DFONT_CONFIG_H__

#define _DFONT_DEBUG 0

namespace dfont 
{
	extern const char* dfont_default_fontpath;
	extern int dfont_default_ppi;
}


#define DFONT_DEFAULT_FONTPATH		dfont::dfont_default_fontpath
#define DFONT_DEFAULT_FONTALIAS		"default"
#define DFONT_DEFAULT_FONTPPI		dfont::dfont_default_ppi

#define DFONT_BITMAP_PADDING		1
#define DFONT_TEXTURE_SIZE_WIDTH	256
#define DFONT_TEXTURE_SIZE_HEIGHT	256
#define DFONT_MAX_TEXTURE_NUM_PERFONT 8

//
// TODO:
//	- 1.GlyphSolt��̭�㷨�Ż�
//	- 2.��ͬƽ̨���������·����Ĭ�����崴����ʹ�ù���
//	- 3.������ڴ�ռ�ã�ʣ���λ����Ϣ��ʵʱ��ؽӿ�
//	- (?)4.���̴߳�������
//	- (?)5.Kerning����
//	- 6.����wtexture��
//

#endif//__DFONT_CONFIG_H__
