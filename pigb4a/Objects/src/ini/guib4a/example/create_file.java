package ini.guib4a.example;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class create_file {
private static create_file mostCurrent = new create_file();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static String _ini_spermsettings_name = "";
public static String _ini_pigpet_name = "";
public ini.guib4a.example.main _main = null;
public ini.guib4a.example.sql_capture _sql_capture = null;
public ini.guib4a.example.testexp _testexp = null;
public ini.guib4a.example.iniexp _iniexp = null;
public static String  _create_dir(anywheresoftware.b4a.BA _ba,long _timenow) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strpath = null;
 //BA.debugLineNum = 55;BA.debugLine="Sub Create_Dir(timeNow As Long) As String";
 //BA.debugLineNum = 56;BA.debugLine="Dim strPath As StringBuilder";
_strpath = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 58;BA.debugLine="strPath.Initialize()";
_strpath.Initialize();
 //BA.debugLineNum = 60;BA.debugLine="strPath.Append(\"/home/\").Append(DateTime.GetYear(";
_strpath.Append("/home/").Append(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(_timenow))+"/").Append(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(_timenow))+"/").Append(BA.NumberToString(_timenow)+"");
 //BA.debugLineNum = 62;BA.debugLine="File.MakeDir(File.DirRootExternal,strPath.ToStrin";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_strpath.ToString());
 //BA.debugLineNum = 64;BA.debugLine="Return strPath.ToString()";
if (true) return _strpath.ToString();
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static String  _create_file_home(anywheresoftware.b4a.BA _ba) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _tw = null;
 //BA.debugLineNum = 16;BA.debugLine="Sub Create_File_Home";
 //BA.debugLineNum = 17;BA.debugLine="Dim tw As TextWriter";
_tw = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 18;BA.debugLine="File.MakeDir(File.DirRootExternal,\"/home\")";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"/home");
 //BA.debugLineNum = 19;BA.debugLine="If File.Exists(File.DirRootExternal,INI_SPERMSETT";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_ini_spermsettings_name)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 21;BA.debugLine="File.WriteString(File.DirRootExternal,INI_SPERMS";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_ini_spermsettings_name,"");
 //BA.debugLineNum = 24;BA.debugLine="tw.Initialize(File.OpenOutput(File.DirRootExtern";
_tw.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_ini_spermsettings_name,anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 25;BA.debugLine="tw.WriteLine(\"[isperm]\")";
_tw.WriteLine("[isperm]");
 //BA.debugLineNum = 26;BA.debugLine="tw.WriteLine(\"videocalw=640\")";
_tw.WriteLine("videocalw=640");
 //BA.debugLineNum = 27;BA.debugLine="tw.WriteLine(\"videocalh=480\")";
_tw.WriteLine("videocalh=480");
 //BA.debugLineNum = 28;BA.debugLine="tw.WriteLine(\"videoshoww=640\")";
_tw.WriteLine("videoshoww=640");
 //BA.debugLineNum = 29;BA.debugLine="tw.WriteLine(\"videoshowh=480\")";
_tw.WriteLine("videoshowh=480");
 //BA.debugLineNum = 30;BA.debugLine="tw.WriteLine(\"initialthresh=80\")";
_tw.WriteLine("initialthresh=80");
 //BA.debugLineNum = 31;BA.debugLine="tw.WriteLine(\"threshsub=20\")";
_tw.WriteLine("threshsub=20");
 //BA.debugLineNum = 32;BA.debugLine="tw.WriteLine(\"minarea=6\")";
_tw.WriteLine("minarea=6");
 //BA.debugLineNum = 33;BA.debugLine="tw.WriteLine(\"maxarea=24\")";
_tw.WriteLine("maxarea=24");
 //BA.debugLineNum = 34;BA.debugLine="tw.WriteLine(\"ratio=0.159\")";
_tw.WriteLine("ratio=0.159");
 //BA.debugLineNum = 35;BA.debugLine="tw.WriteLine(\"nshowmidrst=1\")";
_tw.WriteLine("nshowmidrst=1");
 //BA.debugLineNum = 37;BA.debugLine="tw.Close()";
_tw.Close();
 };
 //BA.debugLineNum = 40;BA.debugLine="If File.Exists(File.DirRootExternal,INI_PIGPET_NA";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_ini_pigpet_name)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 42;BA.debugLine="File.WriteString(File.DirRootExternal,INI_PIGPET";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_ini_pigpet_name,"");
 };
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public static String  _create_path_dir(anywheresoftware.b4a.BA _ba,String _capture_path,long _timenow) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strpath = null;
 //BA.debugLineNum = 68;BA.debugLine="Sub Create_Path_Dir(capture_path As String,timeNow";
 //BA.debugLineNum = 69;BA.debugLine="Dim strPath As StringBuilder";
_strpath = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 71;BA.debugLine="strPath.Initialize()";
_strpath.Initialize();
 //BA.debugLineNum = 73;BA.debugLine="strPath.Append(capture_path).Append(\"/\").Append(t";
_strpath.Append(_capture_path).Append("/").Append(BA.NumberToString(_timenow)+"");
 //BA.debugLineNum = 75;BA.debugLine="File.MakeDir(File.DirRootExternal,strPath.ToStrin";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),_strpath.ToString());
 //BA.debugLineNum = 77;BA.debugLine="Return strPath.ToString()";
if (true) return _strpath.ToString();
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static String  _fileexists(anywheresoftware.b4a.BA _ba,String _filename) throws Exception{
 //BA.debugLineNum = 48;BA.debugLine="Sub FileExists(fileName As String)";
 //BA.debugLineNum = 49;BA.debugLine="If File.Exists(File.DirInternal, fileName) == Fal";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 50;BA.debugLine="File.WriteString(File.DirInternal,fileName,\"\")";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename,"");
 };
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public static boolean  _isnotnull(anywheresoftware.b4a.BA _ba,Object _params) throws Exception{
 //BA.debugLineNum = 97;BA.debugLine="Sub IsNotNull(params As Object) As Boolean";
 //BA.debugLineNum = 98;BA.debugLine="If params <> Null And params <> \"\" Then";
if (_params!= null && (_params).equals((Object)("")) == false) { 
 //BA.debugLineNum = 99;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 101;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 103;BA.debugLine="End Sub";
return false;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 5;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Dim INI_SPERMSETTINGS_NAME As String = \"/home/Spe";
_ini_spermsettings_name = "/home/SpermSettings.ini";
 //BA.debugLineNum = 9;BA.debugLine="Dim INI_PIGPET_NAME As String = \"/home/pigpet.db\"";
_ini_pigpet_name = "/home/pigpet.db";
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _timeformat(anywheresoftware.b4a.BA _ba,long _timenow) throws Exception{
String _timereturn = "";
 //BA.debugLineNum = 81;BA.debugLine="Sub TimeFormat(timeNow As Long) As String";
 //BA.debugLineNum = 82;BA.debugLine="Dim timeReturn As String";
_timereturn = "";
 //BA.debugLineNum = 85;BA.debugLine="timeReturn = \"\"";
_timereturn = "";
 //BA.debugLineNum = 86;BA.debugLine="If IsNotNull(timeNow) Then";
if (_isnotnull(_ba,(Object)(_timenow))) { 
 //BA.debugLineNum = 87;BA.debugLine="DateTime.DateFormat = \"yyyy/M/d\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy/M/d");
 //BA.debugLineNum = 88;BA.debugLine="timeReturn = DateTime.Date(timeNow)";
_timereturn = anywheresoftware.b4a.keywords.Common.DateTime.Date(_timenow);
 //BA.debugLineNum = 89;BA.debugLine="DateTime.TimeFormat = \"H:mm:ss\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("H:mm:ss");
 //BA.debugLineNum = 90;BA.debugLine="timeReturn = timeReturn &\":\"& DateTime.Time(time";
_timereturn = _timereturn+":"+anywheresoftware.b4a.keywords.Common.DateTime.Time(_timenow);
 };
 //BA.debugLineNum = 93;BA.debugLine="Return timeReturn";
if (true) return _timereturn;
 //BA.debugLineNum = 94;BA.debugLine="End Sub";
return "";
}
}
