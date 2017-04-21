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

	Private btnAddOne As Button
	Private btnAddTen As Button
	Private btnDelOne As Button
	Private btnDelTen As Button
	Private btnExit As Button
	Private btnSave As Button
	Private lblView As Label
	
	'要读取的配置文件名
	Dim myFile As String = "/home/SpermSettings.ini"
	Private ltvTable As ListView
	'全局map类型,存储配置文件中的键值对
	Dim itemMap As Map
	Dim itemMapKey As String
	'全局map类型,存储配置文件初始值
	Dim itemMap_Begin As Map
	'全局变量,避免加减法时出现溢出现象
	Dim lbl_text_int As Int
	Dim lbl_text_double As Double
	
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
	Activity.LoadLayout("IniWindow")
	Dim jo As JavaObject = Activity
   	Dim e As Object = jo.CreateEvent("android.view.View.OnSystemUiVisibilityChangeListener", "VisibilityChanged", Null)
   	jo.RunMethod("setOnSystemUiVisibilityChangeListener", Array As Object(e))
	Activity.Title = "属性设置"
	Activity.Width = -1
	Activity.Height = -1
	'readSpermSettings
	read_Table_Dict
	fullTable
	lbl_Text_ltvTable
End Sub

Sub Activity_Resume
	read_Table_Dict
	fullTable
	lbl_Text_ltvTable
End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

'填充listView
Sub fullTable
	Dim strs As String
	If ltvTable.Size > 0 Then
		For i = 0 To ltvTable.Size
			ltvTable.Clear()
		Next
	End If
	
	For i = 0 To itemMap.Size - 1
		If i <> 0 Or itemMapKey <> Null Then
		Else
			lblView.Text = itemMap.GetValueAt(i)
			itemMapKey = itemMap.GetKeyAt(i)
		End If
		strs = itemMap.GetKeyAt(i) &":" &itemMap.GetValueAt(i)
		ltvTable.AddSingleLine2(strs,strs)
	Next
End	Sub

'全局map的数据更新
Sub mapUpdate
	For i = 0 To itemMap.Size -1
		If itemMap.GetKeyAt(i) == itemMapKey Then
			'itemMap.Remove(itemMapKey)
			itemMap.Put(itemMapKey,lblView.Text)
		End If
	Next
End Sub

'从数据库中读取配置写入到map
Sub read_Table_Dict
	Dim cursors As Cursor
	Dim cloumns() As Object 
	Dim params As Map
	
	itemMap.Initialize()
	itemMap_Begin.Initialize()
	params.Initialize()
	cloumns = Array As Object("name","value")
	
	sql_capture.FlagIni()
	cursors = sql_capture.QueryColumnData("dict_properties",cloumns,params,False)
	'ToastMessageShow(cursors.RowCount,True)
	For i = 0 To cursors.RowCount - 1
		cursors.Position = i
		itemMap.put(cursors.GetString(cloumns(0)),cursors.GetString(cloumns(1)))
		itemMap_Begin.put(cursors.GetString(cloumns(0)),cursors.GetString(cloumns(1)))
	Next
	
End Sub

'读取配置文件并将内容写入到map
Sub readSpermSettings
	Dim tr As TextReader
	'Dim itemMap As Map
	Dim flagInt As Int
	Dim line As String
	
	itemMap.Initialize()
	flagInt = -1
	
	'读取配置文件
	If File.Exists(File.DirRootExternal,myFile) Then
		
	Else
		File.WriteString(File.DirRootExternal,myFile,"")
	End If
	tr.Initialize(File.OpenInput(File.DirRootExternal,myFile))
	
	Do While line <> Null
		line = tr.ReadLine()
		If line <> Null Then
			flagInt = charInString("=",line)
			line = line.Trim()
			If flagInt <> -1 Then
				If itemMap.IsInitialized() Then
					itemMap.Put(line.SubString2(0,flagInt),line.SubString(flagInt+1))
				End If
			End If
		End If
	Loop

	tr.Close()	
End Sub

'指定字符串所在字符串中的位置
Sub charInString(ch As String,str As String) As Int
	Dim strs As String
	Dim returnInt As Int
	returnInt = -1
	If str <> Null Then
		If ch <> Null Then
			strs = str.Trim()
			If strs.Length() > 0 Then
				For i = 0 To strs.Length() - 1
					If strs.CharAt(i) == ch.CharAt(0) Then
						If i <> 0 Or i <> strs.Length() -1 Then
							returnInt = i
						End If
					End If
				Next
			End If
		End If
	End If
	Return returnInt
End Sub

'保存listView上的相关改动到配置文件中
Sub btnSave_Click
	'Dim strs As String
	'Dim tw As TextWriter
	
	'写入文件
	'tw.Initialize(File.OpenOutput(File.DirRootExternal,myFile,False))
	'tw.WriteLine("[isperm]")
	'For i = 0 To itemMap.Size - 1
	'	strs = itemMap.GetKeyAt(i) & "=" & itemMap.GetValueAt(i)
	'	tw.WriteLine(strs)
	'Next
	
	'tw.Close()
	
	'StartActivity("TestExp")
	Dim columns As Map
	Dim params As Map
	
	columns.Initialize()
	params.Initialize()
	
	If itemMap.Size == itemMap_Begin.Size Then
		For i = 0 To itemMap.Size - 1
			If itemMap.GetValueAt(i) <> itemMap_Begin.GetValueAt(i) Then
				columns.Put("value",itemMap.GetValueAt(i))
				params.Put("name",itemMap.GetKeyAt(i))
				params.Put("params1","[isperm]")
				sql_capture.UpdateToTable("dict_properties",columns,params)
			End If
		Next
	End If
	ToastMessageShow("保存成功...",False)
End Sub

'退出
Sub btnExit_Click
	
	Activity.Finish()
	'StartActivity("TestExp")
End Sub

Sub btnDelTen_Click
	If lblView.Text >= 1 Then
		lbl_text_int = lblView.Text - 10
		If (lbl_text_int <= 0) Then
			lblView.Text = 0
		Else
			lblView.Text = lbl_text_int
		End If
	Else
		lbl_text_double = lblView.Text - 10 * 0.01
		If lbl_text_double <= 0 Then
			lblView.Text = 0
		Else
			lblView.Text = doubleToString(lbl_text_double)
		End If
	End If
	mapUpdate
	fullTable
End Sub

Sub btnDelOne_Click
	If lblView.Text >= 1 Then
		lbl_text_int = lblView.Text - 1
		If lbl_text_int <= 0 Then
			lblView.Text = 0
		Else
			lblView.Text = lbl_text_int
		End If
	Else
		lbl_text_double = lblView.Text - 1 * 0.01
		If lbl_text_double <= 0 Then
			lblView.Text = 0
		Else 
			lblView.Text = doubleToString(lbl_text_double)
		End If
	End If
	mapUpdate
	fullTable
End Sub

Sub btnAddTen_Click
	If lblView.Text >= 1 Then
		lbl_text_int = lblView.Text + 10
		lblView.Text = lbl_text_int
	Else
		lbl_text_double = lblView.Text + 10 * 0.01
		lblView.Text = doubleToString(lbl_text_double)
	End If
	mapUpdate
	fullTable
End Sub

Sub btnAddOne_Click
	If lblView.Text >= 1 Then
		lbl_text_int = lblView.Text + 1
		lblView.Text = lbl_text_int
	Else
		lbl_text_double = lblView.Text + 1 * 0.01
		lblView.Text = doubleToString(lbl_text_double)
		
	End If
	mapUpdate
	fullTable
End Sub

'判断double类型的长度是否大于5,大于则只保留5位
'如0.1235648  返回0.124
'四舍五入
Sub doubleToString(double1 As Double) As String
	Dim strs As String
	strs = double1&""
	If double1 > 1 Then
		strs = 1
	Else
		If strs.Length() > 5 Then
			strs = strs.SubString2(0,5)
		End If
	End If
	Return strs
End Sub

'listView上的item单击事件
Sub ltvTable_ItemClick (Position As Int, value As String)
	values_Text(value)	
End Sub

'listView 不为空时,lblView.Text的值为listView的第一个item的值
Sub lbl_Text_ltvTable
	If ltvTable.Size > 0 Then
		values_Text(ltvTable.GetItem(0))
	End If
End Sub

'
Sub values_Text(value As String)
	Dim flagInt As Int
	If value <> Null And value <> "" Then
		flagInt = charInString(":",value)
		value = value.Trim()
		If flagInt <> -1 Then
			lblView.Text = value.SubString(flagInt+1)
			itemMapKey = value.SubString2(0,flagInt)
		End If
	End If	
End Sub
