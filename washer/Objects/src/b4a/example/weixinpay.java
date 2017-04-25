package b4a.example;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class weixinpay {
private static weixinpay mostCurrent = new weixinpay();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static String _weixin_logo_name = "";
public static String _qr_name_weixin = "";
public static String _weixin_logo_qrcode_name = "";
public static String _str_qrcode_wei_index = "";
public static String _str_qrcode_wei_last = "";
public b4a.example.main _main = null;
public b4a.example.alipay _alipay = null;
public b4a.example.myutils _myutils = null;
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 3;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Dim WEIXIN_LOGO_NAME As String = \"weixin_LOGO.png";
_weixin_logo_name = "weixin_LOGO.png";
 //BA.debugLineNum = 10;BA.debugLine="Dim QR_NAME_WEIXIN As String = \"qr_weixin.png\"";
_qr_name_weixin = "qr_weixin.png";
 //BA.debugLineNum = 12;BA.debugLine="Dim WEIXIN_LOGO_QRCODE_NAME As String  = \"WEIXIN_";
_weixin_logo_qrcode_name = "WEIXIN_qrCodeWithLogo.png";
 //BA.debugLineNum = 15;BA.debugLine="Dim str_qrcode_wei_index As String = \"weixin\"";
_str_qrcode_wei_index = "weixin";
 //BA.debugLineNum = 16;BA.debugLine="Dim str_qrcode_wei_last As String = \"<\"";
_str_qrcode_wei_last = "<";
 //BA.debugLineNum = 17;BA.debugLine="End Sub";
return "";
}
public static int  _qrcodefromhtml(anywheresoftware.b4a.BA _ba,String _html) throws Exception{
int _return_int = 0;
int _str_start_int = 0;
int _str_end_int = 0;
int _return_qrcode = 0;
String _html_str = "";
 //BA.debugLineNum = 20;BA.debugLine="Sub qrCodeFromHtml(html As String) As Int";
 //BA.debugLineNum = 21;BA.debugLine="Dim return_int As Int = -1";
_return_int = (int) (-1);
 //BA.debugLineNum = 22;BA.debugLine="Dim str_start_int As Int = 0";
_str_start_int = (int) (0);
 //BA.debugLineNum = 23;BA.debugLine="Dim str_end_int As Int = 0";
_str_end_int = (int) (0);
 //BA.debugLineNum = 24;BA.debugLine="Dim return_qrcode As Int = 0";
_return_qrcode = (int) (0);
 //BA.debugLineNum = 25;BA.debugLine="Dim html_str As String = \"\"";
_html_str = "";
 //BA.debugLineNum = 27;BA.debugLine="If html <> \"\" And html.Length() > 0 Then";
if ((_html).equals("") == false && _html.length()>0) { 
 //BA.debugLineNum = 28;BA.debugLine="str_start_int = html.IndexOf(str_qrcode_wei_inde";
_str_start_int = _html.indexOf(_str_qrcode_wei_index);
 //BA.debugLineNum = 29;BA.debugLine="If str_start_int > 0 And str_start_int < html.Le";
if (_str_start_int>0 && _str_start_int<_html.length()) { 
 //BA.debugLineNum = 30;BA.debugLine="html_str = html.SubString2(str_start_int,html.L";
_html_str = _html.substring(_str_start_int,_html.length());
 };
 //BA.debugLineNum = 32;BA.debugLine="str_end_int = html_str.IndexOf(str_qrcode_wei_la";
_str_end_int = _html_str.indexOf(_str_qrcode_wei_last);
 //BA.debugLineNum = 33;BA.debugLine="If str_end_int > 0 Then";
if (_str_end_int>0) { 
 //BA.debugLineNum = 34;BA.debugLine="html_str = html_str.SubString2(0,str_end_int)";
_html_str = _html_str.substring((int) (0),_str_end_int);
 };
 };
 //BA.debugLineNum = 39;BA.debugLine="return_qrcode = alipay.QRCODE.returnSucOrFail(htm";
_return_qrcode = mostCurrent._alipay._qrcode.returnSucOrFail(_html_str,mostCurrent._alipay._photo_path,_qr_name_weixin,mostCurrent._alipay._qrcode_width_height);
 //BA.debugLineNum = 42;BA.debugLine="If return_qrcode == 1 Then";
if (_return_qrcode==1) { 
 //BA.debugLineNum = 43;BA.debugLine="If alipay.logoIntoQrCode(WEIXIN_LOGO_NAME, QR_NA";
if (mostCurrent._alipay._logointoqrcode(_ba,_weixin_logo_name,_qr_name_weixin,_weixin_logo_qrcode_name)==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 44;BA.debugLine="return_int = 1";
_return_int = (int) (1);
 };
 };
 //BA.debugLineNum = 47;BA.debugLine="Return return_int";
if (true) return _return_int;
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return 0;
}
}
