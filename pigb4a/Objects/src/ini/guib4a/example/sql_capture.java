package ini.guib4a.example;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class sql_capture {
private static sql_capture mostCurrent = new sql_capture();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.sql.SQL _sql1 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
public ini.guib4a.example.main _main = null;
public ini.guib4a.example.create_file _create_file = null;
public ini.guib4a.example.testexp _testexp = null;
public ini.guib4a.example.iniexp _iniexp = null;
public static String  _createteble_capture(anywheresoftware.b4a.BA _ba) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strb = null;
 //BA.debugLineNum = 210;BA.debugLine="Sub CreateTeble_Capture";
 //BA.debugLineNum = 211;BA.debugLine="Dim strb As StringBuilder";
_strb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 213;BA.debugLine="strb.Initialize()";
_strb.Initialize();
 //BA.debugLineNum = 215;BA.debugLine="strb.Append(\"create table capture (id integer pri";
_strb.Append("create table capture (id integer primary key,pigid varchar(15),");
 //BA.debugLineNum = 216;BA.debugLine="strb.Append(\"ana_time timestamp not null default(";
_strb.Append("ana_time timestamp not null default(datetime('now','localtime')),ana_capture_point integer,");
 //BA.debugLineNum = 217;BA.debugLine="strb.Append(\"ana_path varchar(100),ana_robot_num";
_strb.Append("ana_path varchar(100),ana_robot_num varchar(40),ana_sperm_count integer,");
 //BA.debugLineNum = 218;BA.debugLine="strb.Append(\"ana_sperm_rate real,ana_flag integer";
_strb.Append("ana_sperm_rate real,ana_flag integer)");
 //BA.debugLineNum = 220;BA.debugLine="SQL1.ExecNonQuery(strb.ToString())";
_sql1.ExecNonQuery(_strb.ToString());
 //BA.debugLineNum = 221;BA.debugLine="End Sub";
return "";
}
public static String  _createteble_dict(anywheresoftware.b4a.BA _ba) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strb = null;
 //BA.debugLineNum = 240;BA.debugLine="Sub CreateTeble_Dict";
 //BA.debugLineNum = 241;BA.debugLine="Dim strb As StringBuilder";
_strb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 243;BA.debugLine="strb.Initialize()";
_strb.Initialize();
 //BA.debugLineNum = 245;BA.debugLine="strb.Append(\"create table dict_properties (id int";
_strb.Append("create table dict_properties (id integer primary key,name varchar(50),");
 //BA.debugLineNum = 246;BA.debugLine="strb.Append(\"value varchar(50),params1 varchar(20";
_strb.Append("value varchar(50),params1 varchar(20),params2 varchar(50) )");
 //BA.debugLineNum = 248;BA.debugLine="SQL1.ExecNonQuery(strb.ToString())";
_sql1.ExecNonQuery(_strb.ToString());
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return "";
}
public static String  _createteble_history(anywheresoftware.b4a.BA _ba) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strb = null;
 //BA.debugLineNum = 224;BA.debugLine="Sub CreateTeble_History";
 //BA.debugLineNum = 225;BA.debugLine="Dim strb As StringBuilder";
_strb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 227;BA.debugLine="strb.Initialize()";
_strb.Initialize();
 //BA.debugLineNum = 229;BA.debugLine="strb.Append(\"create table resulthistory (id integ";
_strb.Append("create table resulthistory (id integer primary key,pigid varchar(15),");
 //BA.debugLineNum = 230;BA.debugLine="strb.Append(\"anay_time timestamp not null default";
_strb.Append("anay_time timestamp not null default(datetime('now','localtime')),");
 //BA.debugLineNum = 231;BA.debugLine="strb.Append(\"ana_capture_point integer,ana_path v";
_strb.Append("ana_capture_point integer,ana_path varchar(100),ana_robot_num varchar(40),");
 //BA.debugLineNum = 232;BA.debugLine="strb.Append(\"ana_sperm_count integer,ana_sperm_ac";
_strb.Append("ana_sperm_count integer,ana_sperm_active integer,videocalw integer,");
 //BA.debugLineNum = 233;BA.debugLine="strb.Append(\"videocalh integer,videoshoww integer";
_strb.Append("videocalh integer,videoshoww integer,videoshowh integer,initialthresh integer,");
 //BA.debugLineNum = 234;BA.debugLine="strb.Append(\"threshsub integer,minarea integer,ma";
_strb.Append("threshsub integer,minarea integer,maxarea integer,ratio real)");
 //BA.debugLineNum = 236;BA.debugLine="SQL1.ExecNonQuery(strb.ToString())";
_sql1.ExecNonQuery(_strb.ToString());
 //BA.debugLineNum = 237;BA.debugLine="End Sub";
return "";
}
public static String  _del_table(anywheresoftware.b4a.BA _ba,String _tablename,anywheresoftware.b4a.objects.collections.Map _params) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strsql = null;
int _i = 0;
 //BA.debugLineNum = 87;BA.debugLine="Sub del_Table(tableName As String,params As Map)";
 //BA.debugLineNum = 88;BA.debugLine="Dim strSQL As StringBuilder";
_strsql = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 90;BA.debugLine="strSQL.Initialize()";
_strsql.Initialize();
 //BA.debugLineNum = 92;BA.debugLine="If tableName <> Null And tableName <> \"\" Then";
if (_tablename!= null && (_tablename).equals("") == false) { 
 //BA.debugLineNum = 93;BA.debugLine="strSQL.Append(\"delete from \"&tableName).Append(\"";
_strsql.Append("delete from "+_tablename).Append(" where 1 = 1 ");
 //BA.debugLineNum = 94;BA.debugLine="If params.Size > 0 Then";
if (_params.getSize()>0) { 
 //BA.debugLineNum = 95;BA.debugLine="For i = 0 To params.Size - 1";
{
final int step51 = 1;
final int limit51 = (int) (_params.getSize()-1);
for (_i = (int) (0); (step51 > 0 && _i <= limit51) || (step51 < 0 && _i >= limit51); _i = ((int)(0 + _i + step51))) {
 //BA.debugLineNum = 96;BA.debugLine="strSQL.Append(\" and \"&params.GetKeyAt(i)).Appe";
_strsql.Append(" and "+BA.ObjectToString(_params.GetKeyAt(_i))).Append(" = '"+BA.ObjectToString(_params.GetValueAt(_i))).Append("' ");
 }
};
 };
 };
 //BA.debugLineNum = 101;BA.debugLine="SQL1.ExecNonQuery2(strSQL.ToString(),Array As Obj";
_sql1.ExecNonQuery2(_strsql.ToString(),anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{}));
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
return "";
}
public static String  _flagini(anywheresoftware.b4a.BA _ba) throws Exception{
boolean _flagshow = false;
 //BA.debugLineNum = 255;BA.debugLine="Sub FlagIni";
 //BA.debugLineNum = 256;BA.debugLine="Dim flagShow As Boolean";
_flagshow = false;
 //BA.debugLineNum = 258;BA.debugLine="create_File.FileExists(\"pigpet.db\")";
mostCurrent._create_file._fileexists(_ba,"pigpet.db");
 //BA.debugLineNum = 259;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 260;BA.debugLine="flagShow = TableExists(\"capture\")";
_flagshow = _tableexists(_ba,"capture");
 //BA.debugLineNum = 261;BA.debugLine="If flagShow == False Then";
if (_flagshow==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 262;BA.debugLine="CreateTeble_Capture";
_createteble_capture(_ba);
 };
 //BA.debugLineNum = 265;BA.debugLine="flagShow = TableExists(\"resulthistory\")";
_flagshow = _tableexists(_ba,"resulthistory");
 //BA.debugLineNum = 266;BA.debugLine="If flagShow == False Then";
if (_flagshow==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 267;BA.debugLine="CreateTeble_History";
_createteble_history(_ba);
 };
 //BA.debugLineNum = 270;BA.debugLine="flagShow = TableExists(\"dict_properties\")";
_flagshow = _tableexists(_ba,"dict_properties");
 //BA.debugLineNum = 271;BA.debugLine="If flagShow == False Then";
if (_flagshow==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 272;BA.debugLine="CreateTeble_Dict";
_createteble_dict(_ba);
 //BA.debugLineNum = 273;BA.debugLine="table_Dict_Begin";
_table_dict_begin(_ba);
 }else {
 //BA.debugLineNum = 275;BA.debugLine="If table_Data_Exists(\"dict_properties\") Then";
if (_table_data_exists(_ba,"dict_properties")) { 
 }else {
 //BA.debugLineNum = 277;BA.debugLine="table_Dict_Begin";
_table_dict_begin(_ba);
 };
 };
 //BA.debugLineNum = 281;BA.debugLine="End Sub";
return "";
}
public static String  _initsql(anywheresoftware.b4a.BA _ba,String _filename) throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub InitSQL(fileName As String)";
 //BA.debugLineNum = 17;BA.debugLine="If SQL1.IsInitialized() == False Then";
if (_sql1.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 18;BA.debugLine="SQL1.Initialize(File.DirInternal,fileName,False)";
_sql1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename,anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _inserttocapture(anywheresoftware.b4a.BA _ba,String _tablename,Object[] _params) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strsql = null;
int _i = 0;
 //BA.debugLineNum = 37;BA.debugLine="Sub InsertToCapture(tableName As String,params() A";
 //BA.debugLineNum = 38;BA.debugLine="Dim strSQL As StringBuilder";
_strsql = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 40;BA.debugLine="strSQL.Initialize()";
_strsql.Initialize();
 //BA.debugLineNum = 42;BA.debugLine="If params.Length <> 0 Then";
if (_params.length!=0) { 
 //BA.debugLineNum = 43;BA.debugLine="strSQL.Append(\"insert into \"&tableName).Append(\"";
_strsql.Append("insert into "+_tablename).Append(" values(");
 //BA.debugLineNum = 44;BA.debugLine="For i = 0 To params.Length - 1";
{
final int step14 = 1;
final int limit14 = (int) (_params.length-1);
for (_i = (int) (0); (step14 > 0 && _i <= limit14) || (step14 < 0 && _i >= limit14); _i = ((int)(0 + _i + step14))) {
 //BA.debugLineNum = 45;BA.debugLine="If i <> params.Length - 1 Then";
if (_i!=_params.length-1) { 
 //BA.debugLineNum = 46;BA.debugLine="strSQL.Append(\"?,\")";
_strsql.Append("?,");
 }else {
 //BA.debugLineNum = 48;BA.debugLine="strSQL.Append(\"?)\")";
_strsql.Append("?)");
 };
 }
};
 };
 //BA.debugLineNum = 53;BA.debugLine="SQL1.ExecNonQuery2(strSQL.ToString(),params)";
_sql1.ExecNonQuery2(_strsql.ToString(),anywheresoftware.b4a.keywords.Common.ArrayToList(_params));
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 5;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim SQL1 As SQL";
_sql1 = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 11;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.sql.SQL.CursorWrapper  _querycolumndata(anywheresoftware.b4a.BA _ba,String _tablename,Object[] _columns,anywheresoftware.b4a.objects.collections.Map _params,boolean _flag_distinct) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strsql = null;
int _i = 0;
 //BA.debugLineNum = 110;BA.debugLine="Sub QueryColumnData(tableName As String,columns()";
 //BA.debugLineNum = 111;BA.debugLine="Dim strSQL As StringBuilder";
_strsql = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 113;BA.debugLine="strSQL.Initialize()";
_strsql.Initialize();
 //BA.debugLineNum = 115;BA.debugLine="If columns.Length <> 0 Then";
if (_columns.length!=0) { 
 //BA.debugLineNum = 116;BA.debugLine="strSQL.Append(\"select \")";
_strsql.Append("select ");
 //BA.debugLineNum = 117;BA.debugLine="For i = 0 To columns.Length - 1";
{
final int step63 = 1;
final int limit63 = (int) (_columns.length-1);
for (_i = (int) (0); (step63 > 0 && _i <= limit63) || (step63 < 0 && _i >= limit63); _i = ((int)(0 + _i + step63))) {
 //BA.debugLineNum = 118;BA.debugLine="If i <> columns.Length - 1 Then";
if (_i!=_columns.length-1) { 
 //BA.debugLineNum = 119;BA.debugLine="strSQL.Append(columns(i)).Append(\",\")";
_strsql.Append(BA.ObjectToString(_columns[_i])).Append(",");
 }else {
 //BA.debugLineNum = 121;BA.debugLine="strSQL.Append(columns(i)).Append(\" from \")";
_strsql.Append(BA.ObjectToString(_columns[_i])).Append(" from ");
 };
 }
};
 //BA.debugLineNum = 124;BA.debugLine="strSQL.Append(tableName)";
_strsql.Append(_tablename);
 //BA.debugLineNum = 126;BA.debugLine="If params.Size <> 0 Then";
if (_params.getSize()!=0) { 
 //BA.debugLineNum = 127;BA.debugLine="strSQL.Append(\" where 1 = 1 \")";
_strsql.Append(" where 1 = 1 ");
 //BA.debugLineNum = 128;BA.debugLine="For i = 0 To params.Size - 1";
{
final int step73 = 1;
final int limit73 = (int) (_params.getSize()-1);
for (_i = (int) (0); (step73 > 0 && _i <= limit73) || (step73 < 0 && _i >= limit73); _i = ((int)(0 + _i + step73))) {
 //BA.debugLineNum = 129;BA.debugLine="strSQL.Append(\" and \"&params.GetKeyAt(i)).Appe";
_strsql.Append(" and "+BA.ObjectToString(_params.GetKeyAt(_i))).Append(" = '"+BA.ObjectToString(_params.GetValueAt(_i))+"'");
 }
};
 };
 //BA.debugLineNum = 133;BA.debugLine="If flag_distinct Then";
if (_flag_distinct) { 
 //BA.debugLineNum = 134;BA.debugLine="strSQL.Append(\" group by \")";
_strsql.Append(" group by ");
 //BA.debugLineNum = 135;BA.debugLine="For i = 0 To columns.Length - 1";
{
final int step79 = 1;
final int limit79 = (int) (_columns.length-1);
for (_i = (int) (0); (step79 > 0 && _i <= limit79) || (step79 < 0 && _i >= limit79); _i = ((int)(0 + _i + step79))) {
 //BA.debugLineNum = 136;BA.debugLine="If i <> columns.Length - 1 Then";
if (_i!=_columns.length-1) { 
 //BA.debugLineNum = 137;BA.debugLine="strSQL.Append(columns(i)).Append(\",\")";
_strsql.Append(BA.ObjectToString(_columns[_i])).Append(",");
 }else {
 //BA.debugLineNum = 139;BA.debugLine="strSQL.Append(columns(i)).Append(\" \")";
_strsql.Append(BA.ObjectToString(_columns[_i])).Append(" ");
 };
 }
};
 };
 //BA.debugLineNum = 146;BA.debugLine="cursor1 = SQL1.ExecQuery(strSQL.ToString())";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery(_strsql.ToString())));
 };
 //BA.debugLineNum = 149;BA.debugLine="Return cursor1";
if (true) return _cursor1;
 //BA.debugLineNum = 150;BA.debugLine="End Sub";
return null;
}
public static String  _queryidmax(anywheresoftware.b4a.BA _ba,String _tablename,String _params) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strsql = null;
String _strreturn = "";
 //BA.debugLineNum = 154;BA.debugLine="Sub QueryIDMax(tableName As String,params As Strin";
 //BA.debugLineNum = 155;BA.debugLine="Dim strSQL As StringBuilder";
_strsql = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 156;BA.debugLine="Dim strReturn As String";
_strreturn = "";
 //BA.debugLineNum = 158;BA.debugLine="strSQL.Initialize()";
_strsql.Initialize();
 //BA.debugLineNum = 160;BA.debugLine="strSQL.Append(\"select max(\").Append(params).Appen";
_strsql.Append("select max(").Append(_params).Append(") from ").Append(_tablename);
 //BA.debugLineNum = 162;BA.debugLine="strReturn = SQL1.ExecQuerySingleResult(strSQL.ToS";
_strreturn = _sql1.ExecQuerySingleResult(_strsql.ToString());
 //BA.debugLineNum = 164;BA.debugLine="If strReturn <> Null And strReturn <> \"\" Then";
if (_strreturn!= null && (_strreturn).equals("") == false) { 
 //BA.debugLineNum = 165;BA.debugLine="Return strReturn";
if (true) return _strreturn;
 }else {
 //BA.debugLineNum = 167;BA.debugLine="strReturn = \"0\"";
_strreturn = "0";
 //BA.debugLineNum = 168;BA.debugLine="If params <> Null Then";
if (_params!= null) { 
 //BA.debugLineNum = 169;BA.debugLine="If \"pigid\" == params Then";
if (("pigid").equals(_params)) { 
 //BA.debugLineNum = 170;BA.debugLine="strReturn = 10000";
_strreturn = BA.NumberToString(10000);
 };
 };
 };
 //BA.debugLineNum = 174;BA.debugLine="Return strReturn";
if (true) return _strreturn;
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return "";
}
public static boolean  _table_data_exists(anywheresoftware.b4a.BA _ba,String _tablename) throws Exception{
String _strsql = "";
int _resultsql = 0;
 //BA.debugLineNum = 196;BA.debugLine="Sub table_Data_Exists(tableName As String) As Bool";
 //BA.debugLineNum = 197;BA.debugLine="Dim strSQL As String";
_strsql = "";
 //BA.debugLineNum = 198;BA.debugLine="Dim resultSQL As Int";
_resultsql = 0;
 //BA.debugLineNum = 200;BA.debugLine="strSQL = \"select count(*) from \"&tableName";
_strsql = "select count(*) from "+_tablename;
 //BA.debugLineNum = 201;BA.debugLine="resultSQL = SQL1.ExecQuerySingleResult(strSQL)";
_resultsql = (int)(Double.parseDouble(_sql1.ExecQuerySingleResult(_strsql)));
 //BA.debugLineNum = 202;BA.debugLine="If resultSQL == 0 Then";
if (_resultsql==0) { 
 //BA.debugLineNum = 203;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 205;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 207;BA.debugLine="End Sub";
return false;
}
public static String  _table_dict_begin(anywheresoftware.b4a.BA _ba) throws Exception{
Object[] _params = null;
String _id = "";
anywheresoftware.b4a.objects.collections.Map _paramsmap = null;
anywheresoftware.b4a.objects.collections.Map _columns = null;
int _i = 0;
 //BA.debugLineNum = 284;BA.debugLine="Sub table_Dict_Begin";
 //BA.debugLineNum = 285;BA.debugLine="Dim params() As Object";
_params = new Object[(int) (0)];
{
int d0 = _params.length;
for (int i0 = 0;i0 < d0;i0++) {
_params[i0] = new Object();
}
}
;
 //BA.debugLineNum = 286;BA.debugLine="Dim id As String";
_id = "";
 //BA.debugLineNum = 287;BA.debugLine="Dim paramsMap As Map";
_paramsmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 288;BA.debugLine="Dim columns As Map";
_columns = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 290;BA.debugLine="columns.Initialize()";
_columns.Initialize();
 //BA.debugLineNum = 291;BA.debugLine="paramsMap.Initialize()";
_paramsmap.Initialize();
 //BA.debugLineNum = 293;BA.debugLine="del_Table(\"dict_properties\",columns)";
_del_table(_ba,"dict_properties",_columns);
 //BA.debugLineNum = 295;BA.debugLine="paramsMap.Put(\"videocalw\",\"640\")";
_paramsmap.Put((Object)("videocalw"),(Object)("640"));
 //BA.debugLineNum = 296;BA.debugLine="paramsMap.Put(\"videocalh\",\"480\")";
_paramsmap.Put((Object)("videocalh"),(Object)("480"));
 //BA.debugLineNum = 297;BA.debugLine="paramsMap.Put(\"videoshoww\",\"640\")";
_paramsmap.Put((Object)("videoshoww"),(Object)("640"));
 //BA.debugLineNum = 298;BA.debugLine="paramsMap.Put(\"videoshowh\",\"480\")";
_paramsmap.Put((Object)("videoshowh"),(Object)("480"));
 //BA.debugLineNum = 299;BA.debugLine="paramsMap.Put(\"initialthresh\",\"80\")";
_paramsmap.Put((Object)("initialthresh"),(Object)("80"));
 //BA.debugLineNum = 300;BA.debugLine="paramsMap.Put(\"threshsub\",\"20\")";
_paramsmap.Put((Object)("threshsub"),(Object)("20"));
 //BA.debugLineNum = 301;BA.debugLine="paramsMap.Put(\"minarea\",\"6\")";
_paramsmap.Put((Object)("minarea"),(Object)("6"));
 //BA.debugLineNum = 302;BA.debugLine="paramsMap.Put(\"maxarea\",\"24\")";
_paramsmap.Put((Object)("maxarea"),(Object)("24"));
 //BA.debugLineNum = 303;BA.debugLine="paramsMap.Put(\"ratio\",\"0.159\")";
_paramsmap.Put((Object)("ratio"),(Object)("0.159"));
 //BA.debugLineNum = 304;BA.debugLine="paramsMap.Put(\"nshowmidrst\",\"1\")";
_paramsmap.Put((Object)("nshowmidrst"),(Object)("1"));
 //BA.debugLineNum = 305;BA.debugLine="For i = 0 To paramsMap.Size - 1";
{
final int step199 = 1;
final int limit199 = (int) (_paramsmap.getSize()-1);
for (_i = (int) (0); (step199 > 0 && _i <= limit199) || (step199 < 0 && _i >= limit199); _i = ((int)(0 + _i + step199))) {
 //BA.debugLineNum = 306;BA.debugLine="id = QueryIDMax(\"dict_properties\",\"id\")";
_id = _queryidmax(_ba,"dict_properties","id");
 //BA.debugLineNum = 307;BA.debugLine="params = Array As Object(id+1,paramsMap.GetKeyAt";
_params = new Object[]{(Object)((double)(Double.parseDouble(_id))+1),_paramsmap.GetKeyAt(_i),_paramsmap.GetValueAt(_i),(Object)("[isperm]"),(Object)("")};
 //BA.debugLineNum = 308;BA.debugLine="InsertToCapture(\"dict_properties\",params)";
_inserttocapture(_ba,"dict_properties",_params);
 }
};
 //BA.debugLineNum = 310;BA.debugLine="End Sub";
return "";
}
public static boolean  _tableexists(anywheresoftware.b4a.BA _ba,String _tablename) throws Exception{
String _strsql = "";
int _resultsql = 0;
 //BA.debugLineNum = 180;BA.debugLine="Sub TableExists(tableName As String) As Boolean";
 //BA.debugLineNum = 181;BA.debugLine="Dim strSQL As String";
_strsql = "";
 //BA.debugLineNum = 182;BA.debugLine="Dim resultSQL As Int";
_resultsql = 0;
 //BA.debugLineNum = 184;BA.debugLine="strSQL = \"select count(*) from sqlite_master wher";
_strsql = "select count(*) from sqlite_master where type = 'table' and name = '"+_tablename+"'";
 //BA.debugLineNum = 185;BA.debugLine="resultSQL = SQL1.ExecQuerySingleResult(strSQL)";
_resultsql = (int)(Double.parseDouble(_sql1.ExecQuerySingleResult(_strsql)));
 //BA.debugLineNum = 186;BA.debugLine="If resultSQL == 0 Then";
if (_resultsql==0) { 
 //BA.debugLineNum = 187;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 189;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 191;BA.debugLine="End Sub";
return false;
}
public static String  _updatetotable(anywheresoftware.b4a.BA _ba,String _tablename,anywheresoftware.b4a.objects.collections.Map _columns,anywheresoftware.b4a.objects.collections.Map _params) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strsql = null;
int _i = 0;
 //BA.debugLineNum = 60;BA.debugLine="Sub UpdateToTable(tableName As String,columns As M";
 //BA.debugLineNum = 61;BA.debugLine="Dim strSQL As StringBuilder";
_strsql = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 63;BA.debugLine="strSQL.Initialize()";
_strsql.Initialize();
 //BA.debugLineNum = 65;BA.debugLine="If columns.Size > 0 Then";
if (_columns.getSize()>0) { 
 //BA.debugLineNum = 66;BA.debugLine="strSQL.Append(\"update \"&tableName).Append(\" set";
_strsql.Append("update "+_tablename).Append(" set ");
 //BA.debugLineNum = 67;BA.debugLine="For i = 0 To columns.Size - 1";
{
final int step29 = 1;
final int limit29 = (int) (_columns.getSize()-1);
for (_i = (int) (0); (step29 > 0 && _i <= limit29) || (step29 < 0 && _i >= limit29); _i = ((int)(0 + _i + step29))) {
 //BA.debugLineNum = 68;BA.debugLine="If i <> columns.Size - 1  Then";
if (_i!=_columns.getSize()-1) { 
 //BA.debugLineNum = 69;BA.debugLine="strSQL.Append(columns.GetKeyAt(i)).Append(\" =";
_strsql.Append(BA.ObjectToString(_columns.GetKeyAt(_i))).Append(" = '"+BA.ObjectToString(_columns.GetValueAt(_i))).Append("',");
 }else {
 //BA.debugLineNum = 71;BA.debugLine="strSQL.Append(columns.GetKeyAt(i)).Append(\" =";
_strsql.Append(BA.ObjectToString(_columns.GetKeyAt(_i))).Append(" = '"+BA.ObjectToString(_columns.GetValueAt(_i))).Append("' ");
 };
 }
};
 //BA.debugLineNum = 74;BA.debugLine="strSQL.Append(\" where 1 = 1 \")";
_strsql.Append(" where 1 = 1 ");
 //BA.debugLineNum = 75;BA.debugLine="If params.Size > 0 Then";
if (_params.getSize()>0) { 
 //BA.debugLineNum = 76;BA.debugLine="For i = 0 To params.Size - 1";
{
final int step38 = 1;
final int limit38 = (int) (_params.getSize()-1);
for (_i = (int) (0); (step38 > 0 && _i <= limit38) || (step38 < 0 && _i >= limit38); _i = ((int)(0 + _i + step38))) {
 //BA.debugLineNum = 77;BA.debugLine="strSQL.Append(\" and \"&params.GetKeyAt(i)).Appe";
_strsql.Append(" and "+BA.ObjectToString(_params.GetKeyAt(_i))).Append(" = '"+BA.ObjectToString(_params.GetValueAt(_i))).Append("'");
 }
};
 };
 };
 //BA.debugLineNum = 82;BA.debugLine="SQL1.ExecNonQuery(strSQL.ToString())";
_sql1.ExecNonQuery(_strsql.ToString());
 //BA.debugLineNum = 83;BA.debugLine="End Sub";
return "";
}
}
