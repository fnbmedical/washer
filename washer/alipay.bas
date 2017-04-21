Type=StaticCode
Version=5.02
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'@date 2017年4月12日10:02:12
'@author 张阳光
'支付
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	
	'支付宝jar
	Dim alipaySubmit As AlipaySubmit
	Dim alipayConfig As AlipayConfig
	
	'订单编号
	Dim ORDERNUM As String = "20881633252"
	'订单名
	Dim ORDERNAME As String = "自动洗衣订单"
	'订单描述
	Dim ORDERDESC As String = "自动洗衣订单"
	
	'终端被扫描信息二维码图片名称
	Dim MACHINE_QR_CODE_INFI As String = "machine_info.png"
	
	'所有图片存储位置
	Dim PHOTO_PATH As String = File.DirRootExternal&"/DCIM/"
	'支付宝logo名称
	Dim ALI_LOGO_NAME As String = "T1Z5XfXdxmXXXXXXXX.png"
	'阿里二维码图片名称
	Dim QR_NAME As String = "qr_ali.png"
	'带logo的二维码名称
	Dim QR_CODE_LOGO As String = "qrCodeWithLogo.png"
	'logo的默认宽高
	Dim LOGO_WIDTH As Int = 60
	Dim LOGO_HEIGHT As Int = 60
	'二维码的宽高
	'n	1	2	3	4	5	6	7	8	9	10
	'm	59	61	63	65	67	69	71	73	75	77
	'算法   QRCODE_WIDTH_HEIGHT * (59 + 2 * (QRCODE_WIDTH_HEIGHT - 1))
	Dim QRCODE_WIDTH_HEIGHT As Int = 6
	
	Dim QRCODE As QrCodeJNI
	
	'自身支付logo名称
	Dim MY_LOGO_NAME As String = "my_logo.png"
	
	'验证支付宝支付成功
	Dim str_pay_success As String = "验证成功"
	
	'找支付宝的二维码code_url
	Dim str_qrcode_index As String = $"name="qrCode""$
	Dim str_qrcode_last As String = $"id="J_qrCode""$
	
	'商品展示地址
	Dim show_url As String = "http://www.k91d.com/pro.htm"
	
	'支付类型
	Dim payment_type As String = "1"
	'服务器异步通知地址
	Dim notify_url As String = "http://www.k91d.com/pay/notify_url.php"
	'页面跳转通知路径
	Dim return_url As String = "http://www.k91d.com/pay/return_url.php"
	'防钓鱼时间戳
	Dim anti_phishing_key As String = ""
	'客户端的IP地址
	Dim exter_invoke_ip As String = ""
	
	Dim str_Money As String = ""
End Sub

Sub aliQrCode(order_num As String,order_price As Int,order_desc As String) As String
	Dim return_str As String = ""
	
	'订单编号
	Dim out_trade_no As String = order_num
	'订单名称
	Dim subject As String = ORDERNAME
	'订单描述
	Dim body As String = ORDERDESC&order_desc
	
	str_Money = (order_price * 0.01)&""
	
	Dim mapali As Map
	
	mapali.Initialize()
	
	mapali.Put("service","create_direct_pay_by_user")
	mapali.Put("partner", alipayConfig.partner)
	mapali.put("seller_email", alipayConfig.seller_email)
	mapali.put("_input_charset", alipayConfig.input_charset)
	mapali.put("payment_type", payment_type)
	mapali.put("notify_url", notify_url)
	mapali.put("return_url", return_url)
	mapali.put("out_trade_no", out_trade_no)
	mapali.put("subject", subject)
	mapali.put("total_fee", str_Money)
	mapali.put("body", body)
	mapali.put("show_url", show_url)
	mapali.put("anti_phishing_key", anti_phishing_key)
	mapali.put("exter_invoke_ip", exter_invoke_ip)
	return_str = createHtml(alipaySubmit.buildRequest(mapali,"get","确认"))
	
	Return return_str
End Sub

'将logo载入然后复制到制定位置
Sub saveImage(logo_name As String) 
	Dim out As OutputStream
	Dim bmp As Bitmap
	If File.Exists(File.DirAssets,logo_name) Then
		bmp.Initialize(File.DirAssets,logo_name)
		out = File.OpenOutput(PHOTO_PATH,logo_name,False)
		bmp.WriteToStream(out,100,"PNG")
		out.Close
	End If
End Sub

'生成html文件供WebView调用
Sub createHtml(str As String) As String
	Dim strBuilder As StringBuilder
	
	strBuilder.Initialize()
	
	strBuilder.Append("<html>").Append("<head>")
	strBuilder.Append($"<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />"$)
	strBuilder.Append("</head>").Append("<body>").Append(str).Append("</body>").Append("</html>")
	
	Return strBuilder.ToString()
End Sub

'取得二维码信息
Sub getQrCodeFromHtml(html As String) As Int 
	Dim returnInt As Int = -1
	Dim html_str As String = ""
	Dim searchCode_index_int As Int = -1
	Dim searchCode_last_int As Int = -1
	'去空
	html = html.Trim()
	If (html <> "" And html.Length() > 0) Then
		searchCode_index_int = html.IndexOf(str_qrcode_index)
		searchCode_last_int = html.LastIndexOf(str_qrcode_last)
	End If
	
	If searchCode_index_int >= 0 And searchCode_last_int >= 0 And searchCode_index_int < searchCode_last_int Then
		html_str = html.SubString2(searchCode_index_int,searchCode_last_int)
	End If
	
	If html_str <> "" And html_str.Length() > 0 Then
		html_str = getStringFrom(html_str)
		If html_str <> "" And html_str.Length() > 0 Then
			Dim return_qrcode As Int = 0
			'ToastMessageShow(html_str,True)
			return_qrcode = QRCODE.returnSucOrFail(html_str, PHOTO_PATH, QR_NAME, QRCODE_WIDTH_HEIGHT)
			Log(return_qrcode)
			If return_qrcode == 1 Then
				If logoIntoQrCode(ALI_LOGO_NAME, QR_NAME, QR_CODE_LOGO) == True Then
					returnInt = 1
				End If
			End If	
		End If
	End If
	Return returnInt
End Sub

'取得支付成功信息
Sub getSuccessFromHtml(html As String) As Int 
	Dim assert_int As Int = -1
	
	If (html<>"" And html.Length() > 0) Then
		assert_int = html.Trim().IndexOf(str_pay_success)
	End If
	
	'Log(assert_int)
	'assert_int不为-1时进行下位机的控制
	If assert_int <> -1 Then
		
	End If
	
	Return assert_int
End Sub

'从字符串中提取指定字符
Sub getStringFrom(html_str As String) As String
	Dim return_str As String = ""
	Dim search_int As Int = -1
	If html_str <> "" And html_str.Length() > 0 Then
		search_int = html_str.LastIndexOf("=") 
	End If
	If search_int >= 0 And search_int + 1 < html_str.Length() Then
		return_str = html_str.SubString(search_int+1)
	End If
	If return_str <> "" And return_str.Length() > 0 Then
		return_str = return_str.Replace($"""$,"")
	End If	
	Return return_str
End Sub

'将logo插入到二维码中
'logoName   logo名称
'qrCodeName 二维码名称
'qrCodeWithLogo 带logo的二维码名称
Sub logoIntoQrCode(logoName As String,qrCodeName As String,qrCodeWithLogo As String) As Boolean
	Dim bmp_logo As Bitmap
	Dim bmp_qrCode As Bitmap
	Dim return_Flag As Boolean = False
	'画布
	Dim bmp_Mut As Bitmap
	Dim cvs As Canvas
	Dim out As OutputStream
	If File.Exists(PHOTO_PATH,qrCodeName) == False Then
		'qrCodeName = qrCodeName&"8"
	End If
	If File.Exists(PHOTO_PATH,logoName) And File.Exists(PHOTO_PATH,qrCodeName) Then
		bmp_logo.Initialize(PHOTO_PATH,logoName)
		bmp_qrCode.Initialize(PHOTO_PATH,qrCodeName)
		'Log(bmp_qrCode.Width&"dddddddd"&bmp_qrCode.Height)
		bmp_Mut.InitializeMutable(bmp_qrCode.Width,bmp_qrCode.Height)
		
		cvs.Initialize2(bmp_Mut)
		Dim r As Rect
		r.Initialize(0,0,bmp_qrCode.Width,bmp_qrCode.Height)
		cvs.DrawRect(r,Colors.White,True,0)
		
		Dim rt As Rect
		rt.Initialize(0,0,cvs.Bitmap.Width,cvs.Bitmap.Height)
		cvs.DrawBitmap(bmp_qrCode,Null,rt)
		Dim rct As Rect
		Dim bmp_logo_width As Int = bmp_logo.Width
		Dim bmp_logo_height As Int = bmp_logo.Height
		If bmp_logo_width > LOGO_WIDTH Then
			bmp_logo_width = LOGO_WIDTH
		End If
		If bmp_logo_height > LOGO_HEIGHT Then
			bmp_logo_height = LOGO_HEIGHT
		End If
		Dim rct_top As Int = (cvs.Bitmap.Width - bmp_logo_width)/2
		Dim rct_bottom As Int = (cvs.Bitmap.Height - bmp_logo_height)/2
		Dim rct_width As Int = (cvs.Bitmap.Width + bmp_logo_width)/2 
		Dim rct_hight As Int = (cvs.Bitmap.Height + bmp_logo_height)/2
		rct.Initialize(rct_top,rct_bottom,rct_width,rct_hight)
		'旋转
		'cvs.DrawBitmapRotated(bmp_logo,Null,rct,30)
		cvs.DrawBitmap(bmp_logo,Null,rct)
		out = File.OpenOutput(PHOTO_PATH,qrCodeWithLogo,False)
		cvs.Bitmap.WriteToStream(out,100,"PNG")
		out.Close
		return_Flag = True
	End If
	Return return_Flag
End Sub