# InGameLuaEdit
在游戏中动态编辑Lua代码
#		使用luajava.bindClass获取类
		material = luajava.bindClass("org.bukkit.Material")
#		使用luajava.newInstance新建类实例/<br>
		paper=luajava.newInstance("org.bukkit.inventory.ItemStack",material.PAPER)
# 常量:
		NMSPATH : net.minecraft.server.v1_7_R4.
		OBCPATH : org.bukkit.craftbukkit.server.v1_7_R4.

#API 
##Tools:
###1.注册命令
	  Tools.Command(命令名称,lua回调函数)
	  
	  参数:命令名称字符串
	       lua回调函数(玩家实例,参数数组)
	  返回: 无
	  
	  用例:
	  Tools.Command(“test”,function(e,args)
	  print("test")
	  end)

###2.创建异步线程
	  Tools.AsyncThread(lua回调函数)
	  
	  参数:lua回调函数(线程实例)
	  返回: 无
	  
	  用例:
	  Tools.AsyncThread(function(th)
	  th:sleep(1000)
	  print("Thread sleep")
	  end)


###3.创建同步线程
	  Tools.SyncThread(lua回调函数,[延迟启动时间])
	  
	  参数:lua回调函数
	      可选:延迟启动时间
	  返回: 线程ID
	  
	  用例:
	  Tools.SyncThread(function()
	  print("SyncThread")
	  end)

###4.停止一个同步线程
	  Tools.CancelSync(线程ID)
	  
	  参数:无
	  返回: 线程ID
	  
	  用例:
	  id=Tools.SyncThread(function()
	  print("SyncThread")
	  end)
	  Tools.CancelSync(id)

###5.注册一个事件
	  Tools.Event(事件名称,lua回调函数)
	  
	  参数:lua回调函数(事件)
	  返回: 无
	  
	  用例:
	  Tools.Event("PlayerJoinEvent",function(e)
	  e:getPlayer():sendMessage("hello world");
	  end)

###6.注册一个玩家数据包事件
	  Tools.PacketEvent(玩家名称,lua回调函数)
	  
	  参数:lua回调函数(包名称,玩家,包实例) 如果返回不会nil 则服务器不会处理这个包
	  返回: 无
	  
	  用例:
	  Tools.PacketEvent(“test”,function(name,p,event)
	  print(name)
	  print(p)
	  print(event);
	  end)

###7.注销一个玩家数据包事件
	  Tools.PacketEvent(玩家名称)
	  
	  参数: 玩家名称字符串
	  返回: 无
	  
	  用例:
	  Tools.unPacketEvent(“test”)

###8.加载其他插件的API
	  Tools.LoadAPI(插件名称)
	  
	  参数: 
	  插件名称字符串
	  
	  返回: 
	  插件实例 或 nil
	  
	  用例:
	  Tools.LoadAPI("RedisPipe")






