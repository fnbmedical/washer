Type=StaticCode
Version=5.02
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'@date 2017年4月12日10:02:12
'@author 张阳光
'方法包
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	
	'取得支付宝qrcode 的前段与后段
	Dim str_index_html As String = "body="
	Dim str_last_html As String = "sign_type=MD5"
	
	'字符串中的指定符号
	Dim str_split As String = "&"
	Dim str_split_map As String = "="
	
End Sub

'获取unique ID
Sub getAndroidID As String
	Dim return_Android As String
	Dim R As Reflector
	R.Target = R.GetContext
	R.Target = R.RunMethod2("getSystemService","wifi","java.lang.String")
	R.Target = R.RunMethod("getConnectionInfo")
	return_Android = R.RunMethod("getMacAddress")&""
	Return return_Android
End Sub

'从html中提取相关信息
Sub getOrderData(html As String) As Map
	Dim html_str As String
	Dim html_index_int As Int = -1
	Dim html_last_int As Int = -1
	
	Dim str_int As Int = -1
	
	Dim params_map As Map
	Dim str_list As List
	
	params_map.Initialize()
	str_list.Initialize()
	
	If html <> "" And html.Trim().Length() > 0 Then
		html_str = html.Trim()
		html_index_int = html_str.IndexOf(str_index_html)
		html_last_int = html_str.LastIndexOf(str_last_html)
	End If
	
	If html_index_int > -1 And html_last_int > -1 And html_index_int < html_last_int Then
		html_str = html_str.SubString2(html_index_int,html_last_int)
		str_list = strSplit(html_str,str_split)
	End If
	
	If str_list <> Null And str_list.Size > 0 Then
		Dim str_temp As String = ""
		For i = 0 To str_list.Size - 1
			str_temp = str_list.Get(i)
			If str_temp <> "" And str_temp.Length > 0 Then
				str_int = str_temp.IndexOf(str_split_map)
				If str_int > 0 And str_int + 1 < str_temp.Length() Then
					params_map.Put(str_temp.SubString2(0,str_int),str_temp.SubString(str_int + 1))
				End If
			End If
		Next
	End If
	Return params_map
End Sub

Sub strSplit(str As String, str_flag As String) As List
	Dim str_list As List
	Dim str_int As Int = -1
	
	str_list.Initialize()
	
	If str <> "" And str.Length() > 0 And str_flag <> "" And str_flag.Length() > 0 Then
		For i = 0 To 1000
			str_int = str.IndexOf(str_flag)
			If str_int > -1 Then
				str_list.Add(str.SubString2(0,str_int))
			Else
				str_list.Add(str)
				Exit
			End If
			If str_int + 1 < str.Length() Then
				str = str.SubString(str_int+1)
			End If
		Next
	End If
	
	Return str_list
End Sub