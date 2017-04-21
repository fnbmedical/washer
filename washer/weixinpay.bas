Type=StaticCode
Version=5.02
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	
	'微信logo名称
	Dim WEIXIN_LOGO_NAME As String = "weixin_LOGO.png"
	'二维码图片名称
	Dim QR_NAME_WEIXIN As String = "qr_weixin.png"
	'微信带logo的二维码名称
	Dim WEIXIN_LOGO_QRCODE_NAME As String  = "WEIXIN_qrCodeWithLogo.png"
	
	'微信code_url截取
	Dim str_qrcode_wei_index As String = "weixin"
	Dim str_qrcode_wei_last As String = "<"
End Sub

'从html提取微信二维码
Sub qrCodeFromHtml(html As String) As Int
	Dim return_int As Int = -1
	Dim str_start_int As Int = 0
	Dim str_end_int As Int = 0
	Dim return_qrcode As Int = 0
	Dim html_str As String = ""
	'ToastMessageShow(html.Trim(),True)
	If html <> "" And html.Length() > 0 Then
		str_start_int = html.IndexOf(str_qrcode_wei_index)
		If str_start_int > 0 And str_start_int < html.Length() Then
			html_str = html.SubString2(str_start_int,html.Length())
		End If
		str_end_int = html_str.IndexOf(str_qrcode_wei_last)
		If str_end_int > 0 Then
			html_str = html_str.SubString2(0,str_end_int)
		End If
	End If
	'ToastMessageShow(html_str,True)
	'"http://paysdk.weixin.qq.com/example/qrcode.php?data="&
	return_qrcode = alipay.QRCODE.returnSucOrFail(html_str, alipay.PHOTO_PATH, QR_NAME_WEIXIN, alipay.QRCODE_WIDTH_HEIGHT)
	'Log(return_qrcode)
	'ToastMessageShow(return_qrcode,True)
	If return_qrcode == 1 Then
		If alipay.logoIntoQrCode(WEIXIN_LOGO_NAME, QR_NAME_WEIXIN, WEIXIN_LOGO_QRCODE_NAME) == True Then
			return_int = 1		
		End If
	End If	
	Return return_int
End Sub