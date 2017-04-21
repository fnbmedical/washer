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
	'数据库操作对象
	Dim SQL1 As SQL
	'数据库返回数据
	Dim cursor1 As Cursor
End Sub

'初始化SQL
'fileName数据库文件名
Sub InitSQL(fileName As String)
	If SQL1.IsInitialized() == False Then
		SQL1.Initialize(File.DirInternal,fileName,False) 
	End If
End Sub

'SQL连接关闭
Sub CloseSQL(fileName As String)
	If SQL1.IsInitialized() == True Then
		SQL1.Close()
	End If
End Sub

'查询某表的全部数据
'Sub QueryAllData(tableName As String,params() As Object) As Cursor
'	Dim strSQL As StringBuilder
	
'	strSQL.Initialize()
	
'	strSQL.Append("select * from ").Append(tableName)
'	SQL1.ExecQueryAsync("SQL",strSQL.ToString(),Null)
	
'	Return cursor1
'End Sub

'新增记录
'tableName要插入数据的表的名称
'params() 一维阵列
Sub InsertToCapture(tableName As String,params() As Object)
	Dim strSQL As StringBuilder
	InitSQL("pigpet.db")
	strSQL.Initialize()
	
	If params.Length <> 0 Then
		strSQL.Append("insert into "&tableName).Append(" values(")
		For i = 0 To params.Length - 1
			If i <> params.Length - 1 Then
				strSQL.Append("?,")
			Else
				strSQL.Append("?)")
			End If
		Next
	End If
	
	SQL1.ExecNonQuery2(strSQL.ToString(),params)
	
End Sub

'修改记录
'tableName要修改的表名
'columns 要修改的列及值的键值对 name = 'zyg'
'params 要修改的是表的哪一行,键值对 id = 1
Sub UpdateToTable(tableName As String,columns As Map,params As Map)
	Dim strSQL As StringBuilder
	InitSQL("pigpet.db")
	strSQL.Initialize()
	
	If columns.Size > 0 Then
		strSQL.Append("update "&tableName).Append(" set ")
		For i = 0 To columns.Size - 1 
			If i <> columns.Size - 1  Then
				strSQL.Append(columns.GetKeyAt(i)).Append(" = '"&columns.GetValueAt(i)).Append("',")
			Else
				strSQL.Append(columns.GetKeyAt(i)).Append(" = '"&columns.GetValueAt(i)).Append("' ")
			End If
		Next
		strSQL.Append(" where 1 = 1 ")
		If params.Size > 0 Then
			For i = 0 To params.Size - 1 
				strSQL.Append(" and "&params.GetKeyAt(i)).Append(" = '"&params.GetValueAt(i)).Append("'")
			Next
		End If
	End If
	'ToastMessageShow(strSQL,True)
	SQL1.ExecNonQuery(strSQL.ToString())
	'SQL1.Close()
End Sub

'删除表的所有数据
'tableName
Sub del_Table(tableName As String,params As Map)
	Dim strSQL As StringBuilder
	InitSQL("pigpet.db")
	strSQL.Initialize()
	
	If tableName <> Null And tableName <> "" Then
		strSQL.Append("delete from "&tableName).Append(" where 1 = 1 ")
		If params.Size > 0 Then
			For i = 0 To params.Size - 1 
				strSQL.Append(" and "&params.GetKeyAt(i)).Append(" = '"&params.GetValueAt(i)).Append("' ")
			Next
		End If
	End If
	
	SQL1.ExecNonQuery2(strSQL.ToString(),Array As Object())
'	SQL1.Close()
End Sub

'查询某表的指定列数据
'tableName指定表名
'column() 一维阵列  要查询的列
'params() 键值对  条件
'flag_distinct 是否去重 distinct 关键字 或  group by
'返回数据类型 Cursor
Sub QueryColumnData(tableName As String,columns() As Object,params As Map,flag_distinct As Boolean) As Cursor
	Dim strSQL As StringBuilder
	InitSQL("pigpet.db")
	strSQL.Initialize()
	
	If columns.Length <> 0 Then
		strSQL.Append("select ")
		For i = 0 To columns.Length - 1
			If i <> columns.Length - 1 Then
				strSQL.Append(columns(i)).Append(",")
			Else
				strSQL.Append(columns(i)).Append(" from ")
			End If
		Next
		strSQL.Append(tableName)
		
		If params.Size <> 0 Then
			strSQL.Append(" where 1 = 1 ")
			For i = 0 To params.Size - 1
				strSQL.Append(" and "&params.GetKeyAt(i)).Append(" = '"&params.GetValueAt(i)&"'")
			Next
		End If
		
		If flag_distinct Then
			strSQL.Append(" group by ")
			For i = 0 To columns.Length - 1
				If i <> columns.Length - 1 Then
					strSQL.Append(columns(i)).Append(",")
				Else
					strSQL.Append(columns(i)).Append(" ")
				End If
			Next
		End If
		'If("capture" == tableName) Then
		'	ToastMessageShow(strSQL.ToString(),True)
		'End If
		cursor1 = SQL1.ExecQuery(strSQL.ToString())
'		SQL1.Close()
	End If
	
	Return cursor1
End Sub

'查询表中id最大值or   pigid的最大值
'返回值 string类型
Sub QueryIDMax(tableName As String,params As String) As String
	Dim strSQL As StringBuilder
	Dim strReturn As String 
	InitSQL("pigpet.db")
	strSQL.Initialize()
	
	strSQL.Append("select max(").Append(params).Append(") from ").Append(tableName)
	
	strReturn = SQL1.ExecQuerySingleResult(strSQL.ToString())
	SQL1.Close()
	If strReturn <> Null And strReturn <> "" Then
		Return strReturn
	Else
		strReturn = "0"
		If params <> Null Then
			If "pigid" == params Then
				strReturn = 10000
			End If
		End If
	End If
	Return strReturn
End Sub

'判断表是否存在
'tableName表名
'返回类型 boolean
Sub TableExists(tableName As String) As Boolean
	Dim strSQL As String
	Dim resultSQL As Int
	InitSQL("pigpet.db")
	strSQL = "select count(*) from sqlite_master where type = 'table' and name = '"&tableName &"'"
	resultSQL = SQL1.ExecQuerySingleResult(strSQL)
'	SQL1.Close()
	If resultSQL == 0 Then
		Return False
	Else
		Return True
	End If
End Sub

'判断表是否有数据
'tableName表名
'返回类型boolean
Sub table_Data_Exists(tableName As String) As Boolean
	Dim strSQL As String
	Dim resultSQL As Int
	InitSQL("pigpet.db")
	strSQL = "select count(*) from "&tableName
	resultSQL = SQL1.ExecQuerySingleResult(strSQL)
'	SQL1.Close()
	If resultSQL == 0 Then
		Return False
	Else
		Return True
	End If
End Sub

'创建表Capture
Sub CreateTeble_Capture
	Dim strb As StringBuilder
	InitSQL("pigpet.db")
	strb.Initialize()
	
	strb.Append("create table capture (id integer primary key,pigid varchar(15),")
	strb.Append("ana_time timestamp not null default(datetime('now','localtime')),ana_capture_point integer,")
	strb.Append("ana_path varchar(100),ana_robot_num varchar(40),ana_sperm_count integer,")
	strb.Append("ana_sperm_rate real,ana_flag integer)")
	
	SQL1.ExecNonQuery(strb.ToString())
'	SQL1.Close()
End Sub

'创建表resulthistory
Sub CreateTeble_History
	Dim strb As StringBuilder
	InitSQL("pigpet.db")
	strb.Initialize()
	
	strb.Append("create table resulthistory (id integer primary key,pigid varchar(15),")
	strb.Append("anay_time timestamp not null default(datetime('now','localtime')),")
	strb.Append("ana_capture_point integer,ana_path varchar(100),ana_robot_num varchar(40),")
	strb.Append("ana_sperm_count integer,ana_sperm_active integer,videocalw integer,")
	strb.Append("videocalh integer,videoshoww integer,videoshowh integer,initialthresh integer,")
	strb.Append("threshsub integer,minarea integer,maxarea integer,ratio real)")
	
	SQL1.ExecNonQuery(strb.ToString())
'	SQL1.Close()
End Sub

'创建表dict_properties
Sub CreateTeble_Dict
	Dim strb As StringBuilder
	InitSQL("pigpet.db")
	strb.Initialize()
	
	strb.Append("create table dict_properties (id integer primary key,name varchar(50),")
	strb.Append("value varchar(50),params1 varchar(20),params2 varchar(50) )")
	
	SQL1.ExecNonQuery(strb.ToString())
'	SQL1.Close()
End Sub

'判断数据库文件是否存在
'不存在则创建
'然后判断相关表是否存在
'不存在的表则创建
Sub FlagIni
	Dim flagShow As Boolean
	
	create_File.FileExists("pigpet.db")
	InitSQL("pigpet.db")
	flagShow = TableExists("capture")
	CloseSQL("pigpet.db")
	If flagShow == False Then
		CreateTeble_Capture
		CloseSQL("pigpet.db")
	End If
	
	flagShow = TableExists("resulthistory")
	CloseSQL("pigpet.db")
	If flagShow == False Then
		CreateTeble_History
		CloseSQL("pigpet.db")
	End If
	
	flagShow = TableExists("dict_properties")
	CloseSQL("pigpet.db")
	If flagShow == False Then
		CreateTeble_Dict
		CloseSQL("pigpet.db")
		table_Dict_Begin
		CloseSQL("pigpet.db")
	Else
		If table_Data_Exists("dict_properties") Then
			CloseSQL("pigpet.db")
		Else
			table_Dict_Begin
			CloseSQL("pigpet.db")
		End If	
	End If
	
End Sub

'dict_properties表初始化
Sub table_Dict_Begin
	Dim params() As Object 
	Dim id As String
	Dim paramsMap As Map
	Dim columns As Map
	
	columns.Initialize()
	paramsMap.Initialize()
	
	del_Table("dict_properties",columns)	
	
	paramsMap.Put("videocalw","640")
	paramsMap.Put("videocalh","480")
	paramsMap.Put("videoshoww","640")
	paramsMap.Put("videoshowh","480")
	paramsMap.Put("initialthresh","80")
	paramsMap.Put("threshsub","20")
	paramsMap.Put("minarea","6")
	paramsMap.Put("maxarea","24")
	paramsMap.Put("ratio","0.159")
	paramsMap.Put("nshowmidrst","1")
	For i = 0 To paramsMap.Size - 1
		id = QueryIDMax("dict_properties","id")
		params = Array As Object(id+1,paramsMap.GetKeyAt(i),paramsMap.GetValueAt(i),"[isperm]","")
		InsertToCapture("dict_properties",params)
	Next
End Sub
