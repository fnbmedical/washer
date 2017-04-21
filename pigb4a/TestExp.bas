Type=Activity
Version=5.02
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'author:张阳光
'Created on 2017年2月6日
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private btnAnay As Button
	Private btnDrawContours As Button
	Private btnExit As Button
	Private btnGaussianBlur As Button
	Private btnStretch As Button
	Private btnSubDraw As Button
	Private btnToMain As Button
	Private igShow As ImageView
	Private ltv_anay_point As ListView
	Private ltv_anay_time As ListView
	Private ltv_pigid As ListView
	Private ltvTable As ListView
	Private lbl_Count As Label
	Private lbl_Rate As Label
	
	'引入的分析算法的jar
	Dim isperm As IspermCpp
	
	Dim pigid As String
	Dim ana_capture_point As String
	Dim anay_time As String
	
	Dim ana_capture_path As String
	Dim ana_history_path As String
	
End Sub

'隐藏智能手机的功能键(虚拟按键) 返回/主页/多任务切换
Sub ForceImmersiceMode
	Dim r As Reflector
	r.Target = r.GetActivity
	r.Target = r.RunMethod("getWindow")
	r.Target = r.RunMethod("getDecorView")
	r.RunMethod2("setSystemUiVisibility", 5894, "java.lang.int")
End Sub

'判断当前活动是否获得焦点
Sub Activity_WindowFocusChanged(HasFocus As Boolean)
	If HasFocus Then
		ForceImmersiceMode
	End If
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	Activity.LoadLayout("testWindow")
	Dim jo As JavaObject = Activity
   	Dim e As Object = jo.CreateEvent("android.view.View.OnSystemUiVisibilityChangeListener", "VisibilityChanged", Null)
   	jo.RunMethod("setOnSystemUiVisibilityChangeListener", Array As Object(e))
	Activity.Title = "分析数据"
	Activity.Width = -1
	Activity.Height = -1
	IniListView_Pigid
	IniListView_Point
	IniListView_Time
	Show_Photo
	Show_LtvTable
End Sub

Sub Activity_Resume
	IniListView_Pigid
	IniListView_Point
	IniListView_Time
	Show_Photo
	Show_LtvTable
End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

'ltv_pigid初始化
Sub IniListView_Pigid
	Dim cursor1 As Cursor
	Dim cloumns() As Object 
	Dim params As Map
	
	cloumns = Array As Object("pigid")
	params.Initialize()
	sql_capture.FlagIni()
	pigid = ""
	ltv_pigid.Clear()
	
	
	'ltv_pigid初始化
	cursor1 = sql_capture.QueryColumnData("capture",cloumns,params,True)
	For i = 0 To cursor1.RowCount - 1
		cursor1.Position = i
		If i == 0 Then
			pigid = cursor1.GetInt(cloumns(0))
		End If
		ltv_pigid.AddSingleLine2(cursor1.GetInt(cloumns(0)),cursor1.GetInt(cloumns(0)))
	Next
	get_ana_capture_path
End Sub

'ltv_anay_point初始化
Sub IniListView_Point	
	ltv_anay_point.Clear()
	
	'ltv_anay_point初始化
	For i = 0 To 3
		ltv_anay_point.AddSingleLine2("采集点"&(i+1),i+1)
	Next
	
	ana_capture_point = 1
	
	get_ana_capture_path
End Sub

'
Sub get_ana_capture_path
	Dim cursor1 As Cursor
	Dim cloumns() As Object 
	Dim params As Map
	
	cloumns = Array As Object("ana_path")
	params.Initialize()
	sql_capture.FlagIni()
	ana_capture_path = ""
	cursor1 = sql_capture.QueryColumnData("capture",cloumns,params,False)
	For i = 0 To cursor1.RowCount - 1
		cursor1.Position = i
		If i == 0 Then
			ana_capture_path = cursor1.GetString(cloumns(0))
		End If
	Next
	Show_Photo
	Show_LtvTable
End Sub

'ltv_anay_time初始化
Sub IniListView_Time
	Dim cursor1 As Cursor
	Dim cloumns() As Object 
	Dim params As Map
	
	cloumns = Array As Object("anay_time","ana_path")
	params.Initialize()
	sql_capture.FlagIni()
	anay_time = ""
	ana_history_path = ""
	ltv_anay_time.Clear()
	
	If pigid <> Null And pigid <> "" Then
		params.Put("pigid",pigid)
		params.Put("ana_capture_point",ana_capture_point)
	End If
	
	'ltv_anay_time初始化
	cursor1 = sql_capture.QueryColumnData("resulthistory",cloumns,params,False)
	For i = 0 To cursor1.RowCount - 1
		cursor1.Position = i
		If i == 0 Then
			anay_time = cursor1.GetString(cloumns(0))
			ana_history_path = cursor1.GetString(cloumns(1))
		End If
		ltv_anay_time.AddSingleLine2(cursor1.GetString(cloumns(0)),cursor1.GetString(cloumns(0)))
	Next
End Sub

'显示初始图片
Sub Show_Photo
	Dim cursor1 As Cursor
	Dim cloumns() As Object 
	Dim params As Map
	
	cloumns = Array As Object("ana_path")
	params.Initialize()
	sql_capture.FlagIni()
	ana_capture_path = ""
	
	If pigid <> Null And pigid <> "" Then
		params.Put("pigid",pigid)
		params.Put("ana_capture_point",ana_capture_point)
	End If
	
	'ltv_anay_time初始化
	cursor1 = sql_capture.QueryColumnData("capture",cloumns,params,False)
	For i = 0 To cursor1.RowCount - 1
		cursor1.Position = i
		If i == 0 Then
			ana_capture_path = cursor1.GetString(cloumns(0))
		End If
	Next
	If ana_capture_path <> Null And ana_capture_path <> "" Then
		If File.Exists(File.DirRootExternal, ana_capture_path&"/"&"000.jpg") == True Then
			igShow.Bitmap = LoadBitmapSample(File.DirRootExternal,ana_capture_path&"/000.jpg",240,320)
		End If
	End If
End Sub

'显示分析后的图片
Sub Show_anay_Photo(picture_Type As String)
	If ana_history_path <> Null And ana_history_path <> "" Then
		If File.Exists(File.DirRootExternal, ana_history_path&"/"&picture_Type&".jpg") == True Then
			igShow.Bitmap = LoadBitmapSample(File.DirRootExternal,ana_history_path&"/"&picture_Type&".jpg",240,320)
		End If
	End If
End Sub

'点击跳到设置
Sub btnToMain_Click	
	
	'Activity.Finish()
	StartActivity(IniExp)
	
End Sub

'点击打开活跃图
Sub btnSubDraw_Click
	Show_anay_Photo("SubDrawContours")
End Sub

'点击打开均衡图
Sub btnStretch_Click
	Show_anay_Photo("Stretch")
End Sub

'点击打开滤波图
Sub btnGaussianBlur_Click
	Show_anay_Photo("GaussianBlur")
End Sub

'点击退出
Sub btnExit_Click
	Activity.Finish()
	'StartActivity("Main")
End Sub

'点击打开结果图
Sub btnDrawContours_Click
	Show_anay_Photo("DrawContours")
End Sub

Sub btnEnabled(flag_Enabled As Boolean)
	btnAnay.Enabled = flag_Enabled
	btnToMain.Enabled = flag_Enabled
	btnExit.Enabled = flag_Enabled
End Sub

'ltvTable内容填充
Sub Show_LtvTable
	Dim cursor1 As Cursor
	Dim cloumns() As Object 
	Dim params As Map
	
	params.Initialize()
	sql_capture.FlagIni()
	ltvTable.Clear()
	
	If anay_time <> Null And anay_time <> "" Then
		params.Put("pigid",pigid)
		params.Put("ana_capture_point",ana_capture_point)
		params.Put("anay_time",anay_time)
		cloumns = Array As Object("ana_sperm_count","ana_sperm_active","videocalw","videocalh","videoshoww","videoshowh" _
							,"initialthresh","threshsub","minarea","maxarea","ratio")
	Else
		cloumns = Array As Object("name","value")
		cursor1 = sql_capture.QueryColumnData("dict_properties",cloumns,params,False)
		For i = 0 To cursor1.RowCount - 1
			cursor1.Position = i
			If i < 9 Then
				ltvTable.AddSingleLine2(cursor1.GetString(cloumns(0))&":"&cursor1.GetString(cloumns(1)),cursor1.GetString(cloumns(1)))
			End If
		Next
		lbl_Count.Text = "总数:"
		lbl_Rate.Text = "活跃数:"
		Return
	End If
	
	cursor1 = sql_capture.QueryColumnData("resulthistory",cloumns,params,False)
	For i = 0 To cursor1.RowCount - 1
		cursor1.Position = i
		If i == 0 Then
			For j = 2 To cloumns.Length - 1
				ltvTable.AddSingleLine2(cloumns(j)&":"&cursor1.GetString(cloumns(j)),cursor1.GetString(cloumns(j)))
			Next
			lbl_Count.Text = "总数:"&cursor1.GetString(cloumns(0))
			lbl_Rate.Text = "活跃数:"&cursor1.GetString(cloumns(1))
		End If
	Next
End Sub

'总数与活跃数的显示
Sub Show_Count_Active
End Sub

'点击调用分析算法分析采集到的图片
Sub btnAnay_Click
	Dim isperm_dict_cursors As Cursor
	Dim isperm_dict_array() As Float
	Dim isperm_Count() As Int
	Dim isperm_dict_cloumns() As Object 
	Dim isperm_dict_params As Map
	Dim isperm_dict_map As Map
	
	Dim timeNow As Long
	Dim cursor1 As Cursor
	Dim cloumns() As Object 
	Dim params As Map
	
	Dim id As Int
	Dim history_params() As Object 
	
	isperm_dict_map.Initialize()
	isperm_dict_params.Initialize()
	isperm_dict_cloumns = Array As Object("name","value")
	
	btnEnabled(False)
	cloumns = Array As Object("ana_path")
	params.Initialize()
	sql_capture.FlagIni()
	ana_history_path = ""
	ana_capture_path = ""
	
	If pigid <> Null And pigid <> "" Then
		params.Put("pigid",pigid)
		params.Put("ana_capture_point",ana_capture_point)
	End If
	
	'ltv_anay_time初始化
	cursor1 = sql_capture.QueryColumnData("capture",cloumns,params,False)
	For i = 0 To cursor1.RowCount - 1
		cursor1.Position = i
		If i == 0 Then
			ana_capture_path = cursor1.GetString(cloumns(0))
		End If
	Next
	
	If ana_capture_path <> "" And ana_capture_path <> Null Then
		
		timeNow = DateTime.Now
		sql_capture.FlagIni()
		ana_history_path = create_File.Create_Path_Dir(ana_capture_path,timeNow)
		isperm_dict_cursors = sql_capture.QueryColumnData("dict_properties",isperm_dict_cloumns,isperm_dict_params,False)
		
		For i = 0 To isperm_dict_cursors.RowCount - 1
			isperm_dict_cursors.Position = i
			isperm_dict_map.put(isperm_dict_cursors.GetString(isperm_dict_cloumns(0)),isperm_dict_cursors.GetString(isperm_dict_cloumns(1)))
		Next
		
		
		If isperm_dict_map.Size > 9 Then
			isperm_dict_array = Array As Float(isperm_dict_map.GetValueAt(0),isperm_dict_map.GetValueAt(1),isperm_dict_map.GetValueAt(2) _
				,isperm_dict_map.GetValueAt(3),isperm_dict_map.GetValueAt(4),isperm_dict_map.GetValueAt(5),isperm_dict_map.GetValueAt(6) _
				,isperm_dict_map.GetValueAt(7),isperm_dict_map.GetValueAt(8),isperm_dict_map.GetValueAt(9))
		End If
		
		isperm_Count = isperm.returnCount(File.DirRootExternal&ana_capture_path&"/" _
									,File.DirRootExternal&ana_history_path,isperm_dict_array)
		'isperm_Count = Array As Int(isperm.returnCount(File.DirRootExternal&ana_capture_path&"/",File.DirRootExternal&ana_history_path _
		'							,File.DirRootExternal&"/home/"&"SpermSettings.ini"))
		
		'ToastMessageShow(isperm_Count(0)&" "&isperm_Count(1),True)
		'sql_capture.FlagIni()
		id = sql_capture.QueryIDMax("resulthistory","id")
		history_params = Array As Object(id+1,pigid,create_File.TimeFormat(timeNow),ana_capture_point,ana_history_path,"" _
			,isperm_Count(0),isperm_Count(1),isperm_dict_array(0),isperm_dict_array(1),isperm_dict_array(2),isperm_dict_array(3) _
			,isperm_dict_array(4),isperm_dict_array(5),isperm_dict_array(6),isperm_dict_array(7),isperm_dict_array(8))
	
		sql_capture.InsertToCapture("resulthistory",history_params)
	
		IniListView_Time
	Else
		ToastMessageShow("此采集点并未采集到图片....",True)
	End If
	btnEnabled(True)
	Show_LtvTable
	'ToastMessageShow(ispermInt,False)
End Sub


Sub ltv_pigid_ItemClick (Position As Int, Value As Object)
	Dim strs As String
	
	strs = Value
	pigid = strs.Trim()
	
	IniListView_Point
	IniListView_Time
	Show_Photo
	Show_LtvTable
End Sub
'ltv_anay_point_ItemClick
Sub ltv_anay_point_ItemClick(Position As Int, Value As Object)
	Dim strs As String
	
	strs = Value
	If strs <> Null And strs <> "" Then
		ana_capture_point = strs
		IniListView_Time
		Show_Photo
		Show_LtvTable
	End If
End Sub

Sub ltv_anay_time_ItemClick (Position As Int, Value As Object)
	Dim cursor_path As Cursor
	Dim cloumns() As Object 
	Dim params As Map
	
	cloumns = Array As Object("ana_path")
	params.Initialize()
	sql_capture.FlagIni()
	ana_history_path = ""
	
	If pigid <> Null And pigid <> "" Then
		params.Put("pigid",pigid)
		params.Put("ana_capture_point",ana_capture_point)
		params.Put("anay_time",Value)
	End If
	anay_time = Value
	'ltv_anay_time初始化
	cursor_path = sql_capture.QueryColumnData("resulthistory",cloumns,params,False)
	For i = 0 To cursor_path.RowCount - 1
		cursor_path.Position = i
		ana_history_path = cursor_path.GetString(cloumns(0))
	Next
	Show_LtvTable
End Sub

Sub ltvTable_ItemClick (Position As Int, Value As Object)
	
End Sub