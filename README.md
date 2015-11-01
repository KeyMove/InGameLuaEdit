# InGameLuaEdit
在游戏中动态编辑Lua代码
#		使用luajava.bindClass获取类
```lua
		material = luajava.bindClass("org.bukkit.Material")
```
#		使用luajava.newInstance新建类实例
```lua
		paper=luajava.newInstance("org.bukkit.inventory.ItemStack",material.PAPER)
```
# 常量:
		储存NMS和OBC的路径
		例如1.7.10:
```lua
		NMSPATH = "net.minecraft.server.v1_7_R4."
		OBCPATH = "org.bukkit.craftbukkit.server.v1_7_R4."
```

# 命令
		/lua restart - 重载所有lua文件
		/lua rebuild - 重新生成事件类
# 配置
		config.yml文件内为激活事件的列表

#API 
##Tools:
###1.注册命令
	  Tools.Command(命令名称,lua回调函数)
	  
	  参数:命令名称字符串
	       lua回调函数(玩家实例,参数数组)
	  返回: 无
	  
	  用例:
```lua
	  Tools.Command("test",function(e,args)
	  print("test")
	  end)
```

###2.创建异步线程
	  Tools.AsyncThread(lua回调函数)
	  
	  参数:lua回调函数(线程实例)
	  返回: 无
	  
	  用例:
```lua	  
	  Tools.AsyncThread(function(th)
	  th:sleep(1000)
	  print("Thread sleep")
	  end)
```

###3.创建同步线程
	  Tools.SyncThread(lua回调函数,[延迟启动时间])
	  
	  参数:lua回调函数
	      可选:延迟启动时间
	  返回: 线程ID
	  
	  用例:
```lua
	  Tools.SyncThread(function()
	  print("SyncThread")
	  end)
```

###4.停止一个同步线程
	  Tools.CancelSync(线程ID)
	  
	  参数:无
	  返回: 线程ID
	  
	  用例:
```lua
	  id=Tools.SyncThread(function()
	  print("SyncThread")
	  end)
	  Tools.CancelSync(id)
```

###5.注册一个事件
	  Tools.Event(事件名称,lua回调函数)
	  
	  参数:lua回调函数(事件)
	  返回: 无
	  
	  用例:
```lua
	  Tools.Event("PlayerJoinEvent",function(e)
	  e:getPlayer():sendMessage("hello world");
	  end)
```
	  说明:
	  config.yml关系到事件是否启动

###6.注册一个玩家数据包事件
	  Tools.PacketEvent(玩家名称,lua回调函数)
	  
	  参数:lua回调函数(包名称,玩家,包实例) 如果返回不会nil 则服务器不会处理这个包
	  返回: 无
	  
	  用例:
```lua
	  Tools.PacketEvent("test",function(name,p,event)
	  print(name)
	  print(p)
	  print(event);
	  end)
```

###7.注销一个玩家数据包事件
	  Tools.unPacketEvent(玩家名称)
	  
	  参数: 玩家名称字符串
	  返回: 无
	  
	  用例:
```lua
	  Tools.unPacketEvent("test")
```

###8.加载其他插件的API
	  Tools.LoadAPI(插件名称)
	  
	  参数: 
	  插件名称字符串
	  
	  返回: 
	  插件实例 或 nil
	  
	  用例:
```lua
	  Tools.LoadAPI("RedisPipe")
```
##Ref:
###1.加载类
	  Ref:Class(类名)
	  
	  参数: 
	  类名称字符串
	  
	  返回: 
	  类
	  
	  说明:
	  跟luajava.bindClass用法相同 对于KCauldron的兼容性更好
	  
	  用例:
```lua
	  Ref:Class("org.bukkit.Bukkit")
```
###2.构建类实例
	  Ref:getNewClass(类名)
	  
	  参数: 
	  类名称字符串
	  
	  返回: 
	  类实例
	  
	  用例:
```lua
	  Ref:getNewClass("org.bukkit.inventory.ItemStack")
```
###3.新建类实例
	  Ref:New(类名,{参数列表})
	  
	  参数: 
	  类名称字符串 或者 类对象
	  参数列表 可选
	  
	  返回: 
	  类
	  
	  
	  用例:
```lua
	  mate = Ref:Class("org.bukkit.Material")
	  item=Ref:New("org.bukkit.inventory.ItemStack",{mate.STONE})
	  
	  ItemStack=Ref:Class("org.bukkit.inventory.ItemStack")
	  item=Ref:New(ItemStack,{mate.STONE})
```
###4.新建类数组
	  Ref:ClassArray(类名,数量)
	  
	  参数: 
	  类名称字符串 或者 类对象
	  参数列表 可选
	  
	  返回: 
	  类数组
	  
	  
	  用例:
```lua
	  itemarray=Ref:ClassArray("org.bukkit.inventory.ItemStack",2)
	  
	  ItemStack=Ref:ClassArray("org.bukkit.inventory.ItemStack")
	  itemarray=Ref:New(ItemStack,2)
```
###5.获取类字段
	  Ref:getMembers(字段名称,对象)
	  
	  参数: 
	  字段名称字符串
	  类对象
	  
	  返回: 
	  字段对象
	  
	  
	  用例:
```lua
	  itemhandle=Ref:getMembers("handle",itemstack)
```
###6.设置类字段
	  Ref:setMembers(字段名称,目标实例,值)
	  
	  参数: 
	  字段名称字符串
	  目标实例 要修改字段的实例
	  值对象
	  
	  返回: 
	  字段对象
	  
	  
	  用例:
```lua
	  Ref:setMembers("handle",itemstack,newhandle)
```
###7.获取类型枚举
	  Ref:getType(类型名称)
	  
	  参数: 
	  类型名称字符串
	  
	  返回: 
	  类型枚举
	  
	  说明:
		类型有:
			Object,
			Double,
			Float,
			Short,
			Byte,
			Char,
			Long,
			Int,
			Bool,
		lua属于弱类型在调用部分方法时必须要使用对应的类型
	  
	  用例:
```lua
	  Ref:getType("Short")
```
###8.生成对象类型数组
	  Ref:Objects(类型名称)
	  
	  参数: 
	  参数与类型 数组
	  
	  返回: 
	  对象数组
	  
	  说明:
		配合Ref:getType使用生成参数数组
		lua属于弱类型在调用部分方法时必须要使用对应的类型
	  
	  用例:
```lua
	  Ref:Objects({Ref:getType("Short"),1,2,Ref:getType("float"),1.23})
```


