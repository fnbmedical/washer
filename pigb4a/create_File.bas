Type=StaticCode
Version=5.02
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'author:张阳光
'Created on 2017年2月7日
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim INI_SPERMSETTINGS_NAME As String = "/home/SpermSettings.ini"
	Dim INI_PIGPET_NAME As String = "/home/pigpet.db"
End Sub

'初始化创建程序所用到的文件以及相关配置文件
'文件夹/home
'调试配置文件 SpermSettings.ini
'数据库文件 pigpet.db
Sub Create_File_Home
	Dim tw As TextWriter
	File.MakeDir(File.DirRootExternal,"/home")
	If File.Exists(File.DirRootExternal,INI_SPERMSETTINGS_NAME) == False Then
		
		File.WriteString(File.DirRootExternal,INI_SPERMSETTINGS_NAME,"")
		'File.MakeDir(File.DirRootExternal,"")
		'写入文件
		tw.Initialize(File.OpenOutput(File.DirRootExternal,INI_SPERMSETTINGS_NAME,False))
		tw.WriteLine("[isperm]")
		tw.WriteLine("videocalw=640")
		tw.WriteLine("videocalh=480")
		tw.WriteLine("videoshoww=640")
		tw.WriteLine("videoshowh=480")
		tw.WriteLine("initialthresh=80")
		tw.WriteLine("threshsub=20")
		tw.WriteLine("minarea=6")
		tw.WriteLine("maxarea=24")
		tw.WriteLine("ratio=0.159")
		tw.WriteLine("nshowmidrst=1")
		'ToastMessageShow("保存成功...",False)
		tw.Close()
	End If
	
	If File.Exists(File.DirRootExternal,INI_PIGPET_NAME) == False  Then
		'File.MakeDir(File.DirRootExternal,INI_PIGPET_NAME)
		File.WriteString(File.DirRootExternal,INI_PIGPET_NAME,"")
	End If
End Sub

'数据库文件是否存在,不存在则创建
'fileName数据库文件名
Sub FileExists(fileName As String)
	If File.Exists(File.DirInternal, fileName) == False Then
		File.WriteString(File.DirInternal,fileName,"")
	End If
End Sub

'根据当前事件创建相关文件夹，用于存储照片
Sub Create_Dir(timeNow As Long) As String
	Dim strPath As StringBuilder
	
	strPath.Initialize()
	
	strPath.Append("/home/").Append(DateTime.GetYear(timeNow)&"/").Append(DateTime.GetMonth(timeNow)&"/").Append(timeNow&"")
	
	File.MakeDir(File.DirRootExternal,strPath.ToString())
	
	Return strPath.ToString()
End Sub

'根据当前事件创建相关文件夹，用于存储照片
Sub Create_Path_Dir(capture_path As String,timeNow As Long) As String
	Dim strPath As StringBuilder
	
	strPath.Initialize()
	
	strPath.Append(capture_path).Append("/").Append(timeNow&"")
	
	File.MakeDir(File.DirRootExternal,strPath.ToString())
	
	Return strPath.ToString()
End Sub

'时间转换函数
Sub TimeFormat(timeNow As Long) As String
	Dim timeReturn As String
	'Dim timeTemp As String
	
	timeReturn = ""
	If IsNotNull(timeNow) Then
		DateTime.DateFormat = "yyyy/M/d"
		timeReturn = DateTime.Date(timeNow)
		DateTime.TimeFormat = "H:mm:ss"
		timeReturn = timeReturn &":"& DateTime.Time(timeNow)
		'ToastMessageShow(timeReturn,True)
	End If
	Return timeReturn
End Sub

'判空函数
Sub IsNotNull(params As Object) As Boolean
	If params <> Null And params <> "" Then
		Return True
	Else
		Return False
	End If
End Sub