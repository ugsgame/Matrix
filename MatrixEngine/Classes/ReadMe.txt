
从KISS原则方向设计

0.0.2版本
TODO
1 明确插件模块
  把cocos2dx，cocostudio，以插件的形式布置到引擎
  
2 去掉其它第三方代码
 去掉非本引擎的代码，如Actorsytem中采用了cocos2dx的代码

3 确定基本结构
  所有基本数据工具代码应该要用MatrixCore的，且引擎要从自己的appication进入
  

在这里的CCNode 相当于以后的entity,以后替换即可

0.0.3版本