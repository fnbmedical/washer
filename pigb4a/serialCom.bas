Type=StaticCode
Version=5.02
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'author:张阳光
'Created on 2017年2月9日
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	
	'Dim usbManager As UsbManager
	
End Sub

'连接串口
Sub connect_Serial
	Try
		'usbSerial1.SetPort("/dev/ttySAC4","115200",1)
	Catch(LastException)
		ToastMessageShow(LastException.Message,True)
	End Try
	'aSync.Initialize(usbSerial1.InputStream,usbSerial1.OutputStream,"Astreams")
End Sub

'关闭串口连接
Sub close_Serial(UserClosed As Boolean)
	If UserClosed Then
		'aSync.Close()
		'usbSerial1.close()
	End If
End Sub

'数据操作(接收下位机传输的数据)
Sub Astreams_NewData(Buffer() As Byte)
	Dim cmd_Recevie As String
	Dim conv As ByteConverter
	
	cmd_Recevie = conv.HexFromBytes(Buffer)
	
	ToastMessageShow(cmd_Recevie,True)
	ToastMessageShow("Success...",True)
End Sub

'数据错误
Sub Astreams_Error()
	ToastMessageShow(LastException.Message,True)
End Sub

'发送指令
Sub send_Cmd(cmd As String)
	Dim byteArray(0) As Byte
	Dim aStr As ByteConverter
	Dim cmd_type As String
	
	If cmd.Length < 22 Then
		Return
	End If
	'发送指令给下位机
	byteArray = aStr.HexToBytes(cmd)
	'aSync.Write(byteArray)
	
	cmd_type = cmd.SubString2(2,8)
	ToastMessageShow(cmd_type,True)
End Sub

'解析下位机的数据
Sub recevie_Cmd(cmd As String)
	
End Sub













