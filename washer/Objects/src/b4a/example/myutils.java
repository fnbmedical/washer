package b4a.example;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class myutils {
private static myutils mostCurrent = new myutils();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static String _str_index_html = "";
public static String _str_last_html = "";
public static String _str_split = "";
public static String _str_split_map = "";
public b4a.example.main _main = null;
public b4a.example.alipay _alipay = null;
public b4a.example.weixinpay _weixinpay = null;
public static String  _getandroidid(anywheresoftware.b4a.BA _ba) throws Exception{
String _return_android = "";
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 21;BA.debugLine="Sub getAndroidID As String";
 //BA.debugLineNum = 22;BA.debugLine="Dim return_Android As String";
_return_android = "";
 //BA.debugLineNum = 23;BA.debugLine="Dim R As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 24;BA.debugLine="R.Target = R.GetContext";
_r.Target = (Object)(_r.GetContext((_ba.processBA == null ? _ba : _ba.processBA)));
 //BA.debugLineNum = 25;BA.debugLine="R.Target = R.RunMethod2(\"getSystemService\",\"wifi\"";
_r.Target = _r.RunMethod2("getSystemService","wifi","java.lang.String");
 //BA.debugLineNum = 26;BA.debugLine="R.Target = R.RunMethod(\"getConnectionInfo\")";
_r.Target = _r.RunMethod("getConnectionInfo");
 //BA.debugLineNum = 27;BA.debugLine="return_Android = R.RunMethod(\"getMacAddress\")&\"\"";
_return_android = BA.ObjectToString(_r.RunMethod("getMacAddress"))+"";
 //BA.debugLineNum = 28;BA.debugLine="Return return_Android";
if (true) return _return_android;
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.Map  _getorderdata(anywheresoftware.b4a.BA _ba,String _html) throws Exception{
String _html_str = "";
int _html_index_int = 0;
int _html_last_int = 0;
int _str_int = 0;
anywheresoftware.b4a.objects.collections.Map _params_map = null;
anywheresoftware.b4a.objects.collections.List _str_list = null;
String _str_temp = "";
int _i = 0;
 //BA.debugLineNum = 32;BA.debugLine="Sub getOrderData(html As String) As Map";
 //BA.debugLineNum = 33;BA.debugLine="Dim html_str As String";
_html_str = "";
 //BA.debugLineNum = 34;BA.debugLine="Dim html_index_int As Int = -1";
_html_index_int = (int) (-1);
 //BA.debugLineNum = 35;BA.debugLine="Dim html_last_int As Int = -1";
_html_last_int = (int) (-1);
 //BA.debugLineNum = 37;BA.debugLine="Dim str_int As Int = -1";
_str_int = (int) (-1);
 //BA.debugLineNum = 39;BA.debugLine="Dim params_map As Map";
_params_map = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 40;BA.debugLine="Dim str_list As List";
_str_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 42;BA.debugLine="params_map.Initialize()";
_params_map.Initialize();
 //BA.debugLineNum = 43;BA.debugLine="str_list.Initialize()";
_str_list.Initialize();
 //BA.debugLineNum = 45;BA.debugLine="If html <> \"\" And html.Trim().Length() > 0 Then";
if ((_html).equals("") == false && _html.trim().length()>0) { 
 //BA.debugLineNum = 46;BA.debugLine="html_str = html.Trim()";
_html_str = _html.trim();
 //BA.debugLineNum = 47;BA.debugLine="html_index_int = html_str.IndexOf(str_index_html";
_html_index_int = _html_str.indexOf(_str_index_html);
 //BA.debugLineNum = 48;BA.debugLine="html_last_int = html_str.LastIndexOf(str_last_ht";
_html_last_int = _html_str.lastIndexOf(_str_last_html);
 };
 //BA.debugLineNum = 51;BA.debugLine="If html_index_int > -1 And html_last_int > -1 And";
if (_html_index_int>-1 && _html_last_int>-1 && _html_index_int<_html_last_int) { 
 //BA.debugLineNum = 52;BA.debugLine="html_str = html_str.SubString2(html_index_int,ht";
_html_str = _html_str.substring(_html_index_int,_html_last_int);
 //BA.debugLineNum = 53;BA.debugLine="str_list = strSplit(html_str,str_split)";
_str_list = _strsplit(_ba,_html_str,_str_split);
 };
 //BA.debugLineNum = 56;BA.debugLine="If str_list <> Null And str_list.Size > 0 Then";
if (_str_list!= null && _str_list.getSize()>0) { 
 //BA.debugLineNum = 57;BA.debugLine="Dim str_temp As String = \"\"";
_str_temp = "";
 //BA.debugLineNum = 58;BA.debugLine="For i = 0 To str_list.Size - 1";
{
final int step35 = 1;
final int limit35 = (int) (_str_list.getSize()-1);
for (_i = (int) (0); (step35 > 0 && _i <= limit35) || (step35 < 0 && _i >= limit35); _i = ((int)(0 + _i + step35))) {
 //BA.debugLineNum = 59;BA.debugLine="str_temp = str_list.Get(i)";
_str_temp = BA.ObjectToString(_str_list.Get(_i));
 //BA.debugLineNum = 60;BA.debugLine="If str_temp <> \"\" And str_temp.Length > 0 Then";
if ((_str_temp).equals("") == false && _str_temp.length()>0) { 
 //BA.debugLineNum = 61;BA.debugLine="str_int = str_temp.IndexOf(str_split_map)";
_str_int = _str_temp.indexOf(_str_split_map);
 //BA.debugLineNum = 62;BA.debugLine="If str_int > 0 And str_int + 1 < str_temp.Leng";
if (_str_int>0 && _str_int+1<_str_temp.length()) { 
 //BA.debugLineNum = 63;BA.debugLine="params_map.Put(str_temp.SubString2(0,str_int)";
_params_map.Put((Object)(_str_temp.substring((int) (0),_str_int)),(Object)(_str_temp.substring((int) (_str_int+1))));
 };
 };
 }
};
 };
 //BA.debugLineNum = 68;BA.debugLine="Return params_map";
if (true) return _params_map;
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return null;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 11;BA.debugLine="Dim str_index_html As String = \"body=\"";
_str_index_html = "body=";
 //BA.debugLineNum = 12;BA.debugLine="Dim str_last_html As String = \"sign_type=MD5\"";
_str_last_html = "sign_type=MD5";
 //BA.debugLineNum = 15;BA.debugLine="Dim str_split As String = \"&\"";
_str_split = "&";
 //BA.debugLineNum = 16;BA.debugLine="Dim str_split_map As String = \"=\"";
_str_split_map = "=";
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.collections.List  _strsplit(anywheresoftware.b4a.BA _ba,String _str,String _str_flag) throws Exception{
anywheresoftware.b4a.objects.collections.List _str_list = null;
int _str_int = 0;
int _i = 0;
 //BA.debugLineNum = 71;BA.debugLine="Sub strSplit(str As String, str_flag As String) As";
 //BA.debugLineNum = 72;BA.debugLine="Dim str_list As List";
_str_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 73;BA.debugLine="Dim str_int As Int = -1";
_str_int = (int) (-1);
 //BA.debugLineNum = 75;BA.debugLine="str_list.Initialize()";
_str_list.Initialize();
 //BA.debugLineNum = 77;BA.debugLine="If str <> \"\" And str.Length() > 0 And str_flag <>";
if ((_str).equals("") == false && _str.length()>0 && (_str_flag).equals("") == false && _str_flag.length()>0) { 
 //BA.debugLineNum = 78;BA.debugLine="For i = 0 To 1000";
{
final int step52 = 1;
final int limit52 = (int) (1000);
for (_i = (int) (0); (step52 > 0 && _i <= limit52) || (step52 < 0 && _i >= limit52); _i = ((int)(0 + _i + step52))) {
 //BA.debugLineNum = 79;BA.debugLine="str_int = str.IndexOf(str_flag)";
_str_int = _str.indexOf(_str_flag);
 //BA.debugLineNum = 80;BA.debugLine="If str_int > -1 Then";
if (_str_int>-1) { 
 //BA.debugLineNum = 81;BA.debugLine="str_list.Add(str.SubString2(0,str_int))";
_str_list.Add((Object)(_str.substring((int) (0),_str_int)));
 }else {
 //BA.debugLineNum = 83;BA.debugLine="str_list.Add(str)";
_str_list.Add((Object)(_str));
 //BA.debugLineNum = 84;BA.debugLine="Exit";
if (true) break;
 };
 //BA.debugLineNum = 86;BA.debugLine="If str_int + 1 < str.Length() Then";
if (_str_int+1<_str.length()) { 
 //BA.debugLineNum = 87;BA.debugLine="str = str.SubString(str_int+1)";
_str = _str.substring((int) (_str_int+1));
 };
 }
};
 };
 //BA.debugLineNum = 92;BA.debugLine="Return str_list";
if (true) return _str_list;
 //BA.debugLineNum = 93;BA.debugLine="End Sub";
return null;
}
}
