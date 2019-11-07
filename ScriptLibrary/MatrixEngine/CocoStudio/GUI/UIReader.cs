using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MatrixEngine.CocoStudio.Native; 

namespace MatrixEngine.CocoStudio.GUI
{
    public class UIReader
    {
        public static UIWidget GetWidget(string fileName)
        {
            return new UIWidget(NativeGUIReader.WidgetFromJsonFile(fileName));
        }
    }
}
