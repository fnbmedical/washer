package b4a.example;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class alipay {
private static alipay mostCurrent = new alipay();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static com.alipay.util.AlipaySubmit _alipaysubmit = null;
public static com.alipay.config.AlipayConfig _alipayconfig = null;
public static String _ordernum = "";
public static String _ordername = "";
public static String _orderdesc = "";
public static String _machine_qr_code_infi = "";
public static String _photo_path = "";
public static String _ali_logo_name = "";
public static String _qr_name = "";
public static String _qr_code_logo = "";
public static int _logo_width = 0;
public static int _logo_height = 0;
public static int _qrcode_width_height = 0;
public static com.zyg.jni.QrCodeJNI _qrcode = null;
public static String _my_logo_name = "";
public b4a.example.main _main = null;
public b4a.example.myutils _myutils = null;
public b4a.example.weixinpay _weixinpay = null;
public static String  _aliqrcode(anywheresoftware.b4a.BA _ba,String _order_num,int _order_price,String _order_desc) throws Exception{
String _return_str = "";
String _out_trade_no = "";
String _subject = "";
String _body = "";
String _show_url = "";
String _payment_type = "";
String _notify_url = "";
String _return_url = "";
String _anti_phishing_key = "";
String _exter_invoke_ip = "";
anywheresoftware.b4a.objects.collections.Map _mapali = null;
String _str_money = "";
 //BA.debugLineNum = 48;BA.debugLine="Sub aliQrCode(order_num As String,order_price As I";
 //BA.debugLineNum = 49;BA.debugLine="Dim return_str As String = \"\"";
_return_str = "";
 //BA.debugLineNum = 52;BA.debugLine="Dim out_trade_no As String = order_num";
_out_trade_no = _order_num;
 //BA.debugLineNum = 54;BA.debugLine="Dim subject As String = ORDERNAME";
_subject = _ordername;
 //BA.debugLineNum = 56;BA.debugLine="Dim body As String = ORDERDESC&order_desc";
_body = _orderdesc+_order_desc;
 //BA.debugLineNum = 58;BA.debugLine="Dim show_url As String = \"http://www.k91d.com/pro";
_show_url = "http://www.k91d.com/pro.htm";
 //BA.debugLineNum = 61;BA.debugLine="Dim payment_type As String = \"1\"";
_payment_type = "1";
 //BA.debugLineNum = 63;BA.debugLine="Dim notify_url As String = \"http://www.k91d.com/p";
_notify_url = "http://www.k91d.com/pay/notify_url.php";
 //BA.debugLineNum = 65;BA.debugLine="Dim return_url As String = \"http://www.k91d.com/p";
_return_url = "http://www.k91d.com/pay/return_url.php";
 //BA.debugLineNum = 67;BA.debugLine="Dim anti_phishing_key As String = \"\"";
_anti_phishing_key = "";
 //BA.debugLineNum = 69;BA.debugLine="Dim exter_invoke_ip As String = \"\"";
_exter_invoke_ip = "";
 //BA.debugLineNum = 71;BA.debugLine="Dim mapali As Map";
_mapali = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 72;BA.debugLine="Dim str_Money As String = (order_price * 0.01)&\"\"";
_str_money = BA.NumberToString((_order_price*0.01))+"";
 //BA.debugLineNum = 73;BA.debugLine="mapali.Initialize()";
_mapali.Initialize();
 //BA.debugLineNum = 75;BA.debugLine="mapali.Put(\"service\",\"create_direct_pay_by_user\")";
_mapali.Put((Object)("service"),(Object)("create_direct_pay_by_user"));
 //BA.debugLineNum = 76;BA.debugLine="mapali.Put(\"partner\", alipayConfig.partner)";
_mapali.Put((Object)("partner"),(Object)(_alipayconfig.partner));
 //BA.debugLineNum = 77;BA.debugLine="mapali.put(\"seller_email\", alipayConfig.seller_em";
_mapali.Put((Object)("seller_email"),(Object)(_alipayconfig.seller_email));
 //BA.debugLineNum = 78;BA.debugLine="mapali.put(\"_input_charset\", alipayConfig.input_c";
_mapali.Put((Object)("_input_charset"),(Object)(_alipayconfig.input_charset));
 //BA.debugLineNum = 79;BA.debugLine="mapali.put(\"payment_type\", payment_type)";
_mapali.Put((Object)("payment_type"),(Object)(_payment_type));
 //BA.debugLineNum = 80;BA.debugLine="mapali.put(\"notify_url\", notify_url)";
_mapali.Put((Object)("notify_url"),(Object)(_notify_url));
 //BA.debugLineNum = 81;BA.debugLine="mapali.put(\"return_url\", return_url)";
_mapali.Put((Object)("return_url"),(Object)(_return_url));
 //BA.debugLineNum = 82;BA.debugLine="mapali.put(\"out_trade_no\", out_trade_no)";
_mapali.Put((Object)("out_trade_no"),(Object)(_out_trade_no));
 //BA.debugLineNum = 83;BA.debugLine="mapali.put(\"subject\", subject)";
_mapali.Put((Object)("subject"),(Object)(_subject));
 //BA.debugLineNum = 84;BA.debugLine="mapali.put(\"total_fee\", str_Money)";
_mapali.Put((Object)("total_fee"),(Object)(_str_money));
 //BA.debugLineNum = 85;BA.debugLine="mapali.put(\"body\", body)";
_mapali.Put((Object)("body"),(Object)(_body));
 //BA.debugLineNum = 86;BA.debugLine="mapali.put(\"show_url\", show_url)";
_mapali.Put((Object)("show_url"),(Object)(_show_url));
 //BA.debugLineNum = 87;BA.debugLine="mapali.put(\"anti_phishing_key\", anti_phishing_key";
_mapali.Put((Object)("anti_phishing_key"),(Object)(_anti_phishing_key));
 //BA.debugLineNum = 88;BA.debugLine="mapali.put(\"exter_invoke_ip\", exter_invoke_ip)";
_mapali.Put((Object)("exter_invoke_ip"),(Object)(_exter_invoke_ip));
 //BA.debugLineNum = 89;BA.debugLine="return_str = createHtml(alipaySubmit.buildRequest";
_return_str = _createhtml(_ba,_alipaysubmit.buildRequest((java.util.Map)(_mapali.getObject()),"get","确认"));
 //BA.debugLineNum = 91;BA.debugLine="Return return_str";
if (true) return _return_str;
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _createhtml(anywheresoftware.b4a.BA _ba,String _str) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strbuilder = null;
 //BA.debugLineNum = 107;BA.debugLine="Sub createHtml(str As String) As String";
 //BA.debugLineNum = 108;BA.debugLine="Dim strBuilder As StringBuilder";
_strbuilder = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 110;BA.debugLine="strBuilder.Initialize()";
_strbuilder.Initialize();
 //BA.debugLineNum = 112;BA.debugLine="strBuilder.Append(\"<html>\").Append(\"<head>\")";
_strbuilder.Append("<html>").Append("<head>");
 //BA.debugLineNum = 113;BA.debugLine="strBuilder.Append($\"<meta http-equiv=\"Content-Typ";
_strbuilder.Append(("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"));
 //BA.debugLineNum = 114;BA.debugLine="strBuilder.Append(\"</head>\").Append(\"<body>\").App";
_strbuilder.Append("</head>").Append("<body>").Append(_str).Append("</body>").Append("</html>");
 //BA.debugLineNum = 116;BA.debugLine="Return strBuilder.ToString()";
if (true) return _strbuilder.ToString();
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return "";
}
public static int  _getqrcodefromhtml(anywheresoftware.b4a.BA _ba,String _html) throws Exception{
int _returnint = 0;
String _html_str = "";
int _searchcode_index_int = 0;
int _searchcode_last_int = 0;
int _return_qrcode = 0;
 //BA.debugLineNum = 120;BA.debugLine="Sub getQrCodeFromHtml(html As String) As Int";
 //BA.debugLineNum = 121;BA.debugLine="Dim returnInt As Int = -1";
_returnint = (int) (-1);
 //BA.debugLineNum = 122;BA.debugLine="Dim html_str As String = \"\"";
_html_str = "";
 //BA.debugLineNum = 123;BA.debugLine="Dim searchCode_index_int As Int = -1";
_searchcode_index_int = (int) (-1);
 //BA.debugLineNum = 124;BA.debugLine="Dim searchCode_last_int As Int = -1";
_searchcode_last_int = (int) (-1);
 //BA.debugLineNum = 126;BA.debugLine="html = html.Trim()";
_html = _html.trim();
 //BA.debugLineNum = 127;BA.debugLine="If (html <> \"\" And html.Length() > 0) Then";
if (((_html).equals("") == false && _html.length()>0)) { 
 //BA.debugLineNum = 128;BA.debugLine="searchCode_index_int = html.IndexOf($\"name=\"qrCo";
_searchcode_index_int = _html.indexOf(("name=\"qrCode\""));
 //BA.debugLineNum = 129;BA.debugLine="searchCode_last_int = html.LastIndexOf($\"id=\"J_q";
_searchcode_last_int = _html.lastIndexOf(("id=\"J_qrCode\""));
 };
 //BA.debugLineNum = 132;BA.debugLine="If searchCode_index_int >= 0 And searchCode_last_";
if (_searchcode_index_int>=0 && _searchcode_last_int>=0 && _searchcode_index_int<_searchcode_last_int) { 
 //BA.debugLineNum = 133;BA.debugLine="html_str = html.SubString2(searchCode_index_int,";
_html_str = _html.substring(_searchcode_index_int,_searchcode_last_int);
 };
 //BA.debugLineNum = 136;BA.debugLine="If html_str <> \"\" And html_str.Length() > 0 Then";
if ((_html_str).equals("") == false && _html_str.length()>0) { 
 //BA.debugLineNum = 137;BA.debugLine="html_str = getStringFrom(html_str)";
_html_str = _getstringfrom(_ba,_html_str);
 //BA.debugLineNum = 138;BA.debugLine="If html_str <> \"\" And html_str.Length() > 0 Then";
if ((_html_str).equals("") == false && _html_str.length()>0) { 
 //BA.debugLineNum = 139;BA.debugLine="Dim return_qrcode As Int = 0";
_return_qrcode = (int) (0);
 //BA.debugLineNum = 141;BA.debugLine="return_qrcode = QRCODE.returnSucOrFail(html_str";
_return_qrcode = _qrcode.returnSucOrFail(_html_str,_photo_path,_qr_name,_qrcode_width_height);
 //BA.debugLineNum = 142;BA.debugLine="Log(return_qrcode)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_return_qrcode));
 //BA.debugLineNum = 143;BA.debugLine="If return_qrcode == 1 Then";
if (_return_qrcode==1) { 
 //BA.debugLineNum = 144;BA.debugLine="If logoIntoQrCode(ALI_LOGO_NAME, QR_NAME, QR_C";
if (_logointoqrcode(_ba,_ali_logo_name,_qr_name,_qr_code_logo)==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 145;BA.debugLine="returnInt = 1";
_returnint = (int) (1);
 };
 };
 };
 };
 //BA.debugLineNum = 150;BA.debugLine="Return returnInt";
if (true) return _returnint;
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return 0;
}
public static String  _getstringfrom(anywheresoftware.b4a.BA _ba,String _html_str) throws Exception{
String _return_str = "";
int _search_int = 0;
 //BA.debugLineNum = 171;BA.debugLine="Sub getStringFrom(html_str As String) As String";
 //BA.debugLineNum = 172;BA.debugLine="Dim return_str As String = \"\"";
_return_str = "";
 //BA.debugLineNum = 173;BA.debugLine="Dim search_int As Int = -1";
_search_int = (int) (-1);
 //BA.debugLineNum = 174;BA.debugLine="If html_str <> \"\" And html_str.Length() > 0 Then";
if ((_html_str).equals("") == false && _html_str.length()>0) { 
 //BA.debugLineNum = 175;BA.debugLine="search_int = html_str.LastIndexOf(\"=\")";
_search_int = _html_str.lastIndexOf("=");
 };
 //BA.debugLineNum = 177;BA.debugLine="If search_int >= 0 And search_int + 1 < html_str.";
if (_search_int>=0 && _search_int+1<_html_str.length()) { 
 //BA.debugLineNum = 178;BA.debugLine="return_str = html_str.SubString(search_int+1)";
_return_str = _html_str.substring((int) (_search_int+1));
 };
 //BA.debugLineNum = 180;BA.debugLine="If return_str <> \"\" And return_str.Length() > 0 T";
if ((_return_str).equals("") == false && _return_str.length()>0) { 
 //BA.debugLineNum = 181;BA.debugLine="return_str = return_str.Replace($\"\"\"$,\"\")";
_return_str = _return_str.replace(("\""),"");
 };
 //BA.debugLineNum = 183;BA.debugLine="Return return_str";
if (true) return _return_str;
 //BA.debugLineNum = 184;BA.debugLine="End Sub";
return "";
}
public static int  _getsuccessfromhtml(anywheresoftware.b4a.BA _ba,String _html) throws Exception{
int _assert_int = 0;
 //BA.debugLineNum = 154;BA.debugLine="Sub getSuccessFromHtml(html As String) As Int";
 //BA.debugLineNum = 155;BA.debugLine="Dim assert_int As Int = -1";
_assert_int = (int) (-1);
 //BA.debugLineNum = 157;BA.debugLine="If (html<>\"\" And html.Length() > 0) Then";
if (((_html).equals("") == false && _html.length()>0)) { 
 //BA.debugLineNum = 158;BA.debugLine="assert_int = html.Trim().IndexOf(\"验证成功\")";
_assert_int = _html.trim().indexOf("验证成功");
 };
 //BA.debugLineNum = 163;BA.debugLine="If assert_int <> -1 Then";
if (_assert_int!=-1) { 
 };
 //BA.debugLineNum = 167;BA.debugLine="Return assert_int";
if (true) return _assert_int;
 //BA.debugLineNum = 168;BA.debugLine="End Sub";
return 0;
}
public static boolean  _logointoqrcode(anywheresoftware.b4a.BA _ba,String _logoname,String _qrcodename,String _qrcodewithlogo) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp_logo = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp_qrcode = null;
boolean _return_flag = false;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp_mut = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvs = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _r = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _rt = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _rct = null;
int _bmp_logo_width = 0;
int _bmp_logo_height = 0;
int _rct_top = 0;
int _rct_bottom = 0;
int _rct_width = 0;
int _rct_hight = 0;
 //BA.debugLineNum = 190;BA.debugLine="Sub logoIntoQrCode(logoName As String,qrCodeName A";
 //BA.debugLineNum = 191;BA.debugLine="Dim bmp_logo As Bitmap";
_bmp_logo = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 192;BA.debugLine="Dim bmp_qrCode As Bitmap";
_bmp_qrcode = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 193;BA.debugLine="Dim return_Flag As Boolean = False";
_return_flag = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 195;BA.debugLine="Dim bmp_Mut As Bitmap";
_bmp_mut = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 196;BA.debugLine="Dim cvs As Canvas";
_cvs = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 197;BA.debugLine="Dim out As OutputStream";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 198;BA.debugLine="If File.Exists(PHOTO_PATH,qrCodeName) == False Th";
if (anywheresoftware.b4a.keywords.Common.File.Exists(_photo_path,_qrcodename)==anywheresoftware.b4a.keywords.Common.False) { 
 };
 //BA.debugLineNum = 201;BA.debugLine="If File.Exists(PHOTO_PATH,logoName) And File.Exis";
if (anywheresoftware.b4a.keywords.Common.File.Exists(_photo_path,_logoname) && anywheresoftware.b4a.keywords.Common.File.Exists(_photo_path,_qrcodename)) { 
 //BA.debugLineNum = 202;BA.debugLine="bmp_logo.Initialize(PHOTO_PATH,logoName)";
_bmp_logo.Initialize(_photo_path,_logoname);
 //BA.debugLineNum = 203;BA.debugLine="bmp_qrCode.Initialize(PHOTO_PATH,qrCodeName)";
_bmp_qrcode.Initialize(_photo_path,_qrcodename);
 //BA.debugLineNum = 205;BA.debugLine="bmp_Mut.InitializeMutable(bmp_qrCode.Width,bmp_q";
_bmp_mut.InitializeMutable(_bmp_qrcode.getWidth(),_bmp_qrcode.getHeight());
 //BA.debugLineNum = 207;BA.debugLine="cvs.Initialize2(bmp_Mut)";
_cvs.Initialize2((android.graphics.Bitmap)(_bmp_mut.getObject()));
 //BA.debugLineNum = 208;BA.debugLine="Dim r As Rect";
_r = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 209;BA.debugLine="r.Initialize(0,0,bmp_qrCode.Width,bmp_qrCode.Hei";
_r.Initialize((int) (0),(int) (0),_bmp_qrcode.getWidth(),_bmp_qrcode.getHeight());
 //BA.debugLineNum = 210;BA.debugLine="cvs.DrawRect(r,Colors.White,True,0)";
_cvs.DrawRect((android.graphics.Rect)(_r.getObject()),anywheresoftware.b4a.keywords.Common.Colors.White,anywheresoftware.b4a.keywords.Common.True,(float) (0));
 //BA.debugLineNum = 212;BA.debugLine="Dim rt As Rect";
_rt = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 213;BA.debugLine="rt.Initialize(0,0,cvs.Bitmap.Width,cvs.Bitmap.He";
_rt.Initialize((int) (0),(int) (0),_cvs.getBitmap().getWidth(),_cvs.getBitmap().getHeight());
 //BA.debugLineNum = 214;BA.debugLine="cvs.DrawBitmap(bmp_qrCode,Null,rt)";
_cvs.DrawBitmap((android.graphics.Bitmap)(_bmp_qrcode.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_rt.getObject()));
 //BA.debugLineNum = 215;BA.debugLine="Dim rct As Rect";
_rct = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 216;BA.debugLine="Dim bmp_logo_width As Int = bmp_logo.Width";
_bmp_logo_width = _bmp_logo.getWidth();
 //BA.debugLineNum = 217;BA.debugLine="Dim bmp_logo_height As Int = bmp_logo.Height";
_bmp_logo_height = _bmp_logo.getHeight();
 //BA.debugLineNum = 218;BA.debugLine="If bmp_logo_width > LOGO_WIDTH Then";
if (_bmp_logo_width>_logo_width) { 
 //BA.debugLineNum = 219;BA.debugLine="bmp_logo_width = LOGO_WIDTH";
_bmp_logo_width = _logo_width;
 };
 //BA.debugLineNum = 221;BA.debugLine="If bmp_logo_height > LOGO_HEIGHT Then";
if (_bmp_logo_height>_logo_height) { 
 //BA.debugLineNum = 222;BA.debugLine="bmp_logo_height = LOGO_HEIGHT";
_bmp_logo_height = _logo_height;
 };
 //BA.debugLineNum = 224;BA.debugLine="Dim rct_top As Int = (cvs.Bitmap.Width - bmp_log";
_rct_top = (int) ((_cvs.getBitmap().getWidth()-_bmp_logo_width)/(double)2);
 //BA.debugLineNum = 225;BA.debugLine="Dim rct_bottom As Int = (cvs.Bitmap.Height - bmp";
_rct_bottom = (int) ((_cvs.getBitmap().getHeight()-_bmp_logo_height)/(double)2);
 //BA.debugLineNum = 226;BA.debugLine="Dim rct_width As Int = (cvs.Bitmap.Width + bmp_l";
_rct_width = (int) ((_cvs.getBitmap().getWidth()+_bmp_logo_width)/(double)2);
 //BA.debugLineNum = 227;BA.debugLine="Dim rct_hight As Int = (cvs.Bitmap.Height + bmp_";
_rct_hight = (int) ((_cvs.getBitmap().getHeight()+_bmp_logo_height)/(double)2);
 //BA.debugLineNum = 228;BA.debugLine="rct.Initialize(rct_top,rct_bottom,rct_width,rct_";
_rct.Initialize(_rct_top,_rct_bottom,_rct_width,_rct_hight);
 //BA.debugLineNum = 231;BA.debugLine="cvs.DrawBitmap(bmp_logo,Null,rct)";
_cvs.DrawBitmap((android.graphics.Bitmap)(_bmp_logo.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_rct.getObject()));
 //BA.debugLineNum = 232;BA.debugLine="out = File.OpenOutput(PHOTO_PATH,qrCodeWithLogo,";
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(_photo_path,_qrcodewithlogo,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 233;BA.debugLine="cvs.Bitmap.WriteToStream(out,100,\"PNG\")";
_cvs.getBitmap().WriteToStream((java.io.OutputStream)(_out.getObject()),(int) (100),BA.getEnumFromString(android.graphics.Bitmap.CompressFormat.class,"PNG"));
 //BA.debugLineNum = 234;BA.debugLine="out.Close";
_out.Close();
 //BA.debugLineNum = 235;BA.debugLine="return_Flag = True";
_return_flag = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 237;BA.debugLine="Return return_Flag";
if (true) return _return_flag;
 //BA.debugLineNum = 238;BA.debugLine="End Sub";
return false;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 11;BA.debugLine="Dim alipaySubmit As AlipaySubmit";
_alipaysubmit = new com.alipay.util.AlipaySubmit();
 //BA.debugLineNum = 12;BA.debugLine="Dim alipayConfig As AlipayConfig";
_alipayconfig = new com.alipay.config.AlipayConfig();
 //BA.debugLineNum = 15;BA.debugLine="Dim ORDERNUM As String = \"20881633252\"";
_ordernum = "20881633252";
 //BA.debugLineNum = 17;BA.debugLine="Dim ORDERNAME As String = \"自动洗衣订单\"";
_ordername = "自动洗衣订单";
 //BA.debugLineNum = 19;BA.debugLine="Dim ORDERDESC As String = \"自动洗衣订单\"";
_orderdesc = "自动洗衣订单";
 //BA.debugLineNum = 22;BA.debugLine="Dim MACHINE_QR_CODE_INFI As String = \"machine_inf";
_machine_qr_code_infi = "machine_info.png";
 //BA.debugLineNum = 25;BA.debugLine="Dim PHOTO_PATH As String = File.DirRootExternal&\"";
_photo_path = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/DCIM/";
 //BA.debugLineNum = 27;BA.debugLine="Dim ALI_LOGO_NAME As String = \"T1Z5XfXdxmXXXXXXXX";
_ali_logo_name = "T1Z5XfXdxmXXXXXXXX.png";
 //BA.debugLineNum = 29;BA.debugLine="Dim QR_NAME As String = \"qr_ali.png\"";
_qr_name = "qr_ali.png";
 //BA.debugLineNum = 31;BA.debugLine="Dim QR_CODE_LOGO As String = \"qrCodeWithLogo.png\"";
_qr_code_logo = "qrCodeWithLogo.png";
 //BA.debugLineNum = 33;BA.debugLine="Dim LOGO_WIDTH As Int = 60";
_logo_width = (int) (60);
 //BA.debugLineNum = 34;BA.debugLine="Dim LOGO_HEIGHT As Int = 60";
_logo_height = (int) (60);
 //BA.debugLineNum = 39;BA.debugLine="Dim QRCODE_WIDTH_HEIGHT As Int = 6";
_qrcode_width_height = (int) (6);
 //BA.debugLineNum = 41;BA.debugLine="Dim QRCODE As QrCodeJNI";
_qrcode = new com.zyg.jni.QrCodeJNI();
 //BA.debugLineNum = 45;BA.debugLine="Dim MY_LOGO_NAME As String = \"my_logo.png\"";
_my_logo_name = "my_logo.png";
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public static String  _saveimage(anywheresoftware.b4a.BA _ba,String _logo_name) throws Exception{
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
 //BA.debugLineNum = 95;BA.debugLine="Sub saveImage(logo_name As String)";
 //BA.debugLineNum = 96;BA.debugLine="Dim out As OutputStream";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 97;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 98;BA.debugLine="If File.Exists(File.DirAssets,logo_name) Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),_logo_name)) { 
 //BA.debugLineNum = 99;BA.debugLine="bmp.Initialize(File.DirAssets,logo_name)";
_bmp.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),_logo_name);
 //BA.debugLineNum = 100;BA.debugLine="out = File.OpenOutput(PHOTO_PATH,logo_name,False";
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(_photo_path,_logo_name,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 101;BA.debugLine="bmp.WriteToStream(out,100,\"PNG\")";
_bmp.WriteToStream((java.io.OutputStream)(_out.getObject()),(int) (100),BA.getEnumFromString(android.graphics.Bitmap.CompressFormat.class,"PNG"));
 //BA.debugLineNum = 102;BA.debugLine="out.Close";
_out.Close();
 };
 //BA.debugLineNum = 104;BA.debugLine="End Sub";
return "";
}
}
