package b4a.example;


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
public b4a.example.main _main = null;
public b4a.example.create_file _create_file = null;
public static String  _closesql(anywheresoftware.b4a.BA _ba,String _filename) throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub CloseSQL(fileName As String)";
 //BA.debugLineNum = 24;BA.debugLine="If SQL1.IsInitialized() == True Then";
if (_sql1.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 25;BA.debugLine="SQL1.Close()";
_sql1.Close();
 };
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static String  _createteble_capture(anywheresoftware.b4a.BA _ba) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strb = null;
 //BA.debugLineNum = 223;BA.debugLine="Sub CreateTeble_Capture";
 //BA.debugLineNum = 224;BA.debugLine="Dim strb As StringBuilder";
_strb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 225;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 226;BA.debugLine="strb.Initialize()";
_strb.Initialize();
 //BA.debugLineNum = 228;BA.debugLine="strb.Append(\"create table capture (id integer pri";
_strb.Append("create table capture (id integer primary key,pigid varchar(15),");
 //BA.debugLineNum = 229;BA.debugLine="strb.Append(\"ana_time timestamp not null default(";
_strb.Append("ana_time timestamp not null default(datetime('now','localtime')),ana_capture_point integer,");
 //BA.debugLineNum = 230;BA.debugLine="strb.Append(\"ana_path varchar(100),ana_robot_num";
_strb.Append("ana_path varchar(100),ana_robot_num varchar(40),ana_sperm_count integer,");
 //BA.debugLineNum = 231;BA.debugLine="strb.Append(\"ana_sperm_rate real,ana_flag integer";
_strb.Append("ana_sperm_rate real,ana_flag integer)");
 //BA.debugLineNum = 233;BA.debugLine="SQL1.ExecNonQuery(strb.ToString())";
_sql1.ExecNonQuery(_strb.ToString());
 //BA.debugLineNum = 235;BA.debugLine="End Sub";
return "";
}
public static String  _createteble_dict(anywheresoftware.b4a.BA _ba) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strb = null;
 //BA.debugLineNum = 255;BA.debugLine="Sub CreateTeble_Dict";
 //BA.debugLineNum = 256;BA.debugLine="Dim strb As StringBuilder";
_strb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 257;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 258;BA.debugLine="strb.Initialize()";
_strb.Initialize();
 //BA.debugLineNum = 260;BA.debugLine="strb.Append(\"create table dict_properties (id int";
_strb.Append("create table dict_properties (id integer primary key,name varchar(50),");
 //BA.debugLineNum = 261;BA.debugLine="strb.Append(\"value varchar(50),params1 varchar(20";
_strb.Append("value varchar(50),params1 varchar(20),params2 varchar(50) )");
 //BA.debugLineNum = 263;BA.debugLine="SQL1.ExecNonQuery(strb.ToString())";
_sql1.ExecNonQuery(_strb.ToString());
 //BA.debugLineNum = 265;BA.debugLine="End Sub";
return "";
}
public static String  _createteble_history(anywheresoftware.b4a.BA _ba) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strb = null;
 //BA.debugLineNum = 238;BA.debugLine="Sub CreateTeble_History";
 //BA.debugLineNum = 239;BA.debugLine="Dim strb As StringBuilder";
_strb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 240;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 241;BA.debugLine="strb.Initialize()";
_strb.Initialize();
 //BA.debugLineNum = 243;BA.debugLine="strb.Append(\"create table resulthistory (id integ";
_strb.Append("create table resulthistory (id integer primary key,pigid varchar(15),");
 //BA.debugLineNum = 244;BA.debugLine="strb.Append(\"anay_time timestamp not null default";
_strb.Append("anay_time timestamp not null default(datetime('now','localtime')),");
 //BA.debugLineNum = 245;BA.debugLine="strb.Append(\"ana_capture_point integer,ana_path v";
_strb.Append("ana_capture_point integer,ana_path varchar(100),ana_robot_num varchar(40),");
 //BA.debugLineNum = 246;BA.debugLine="strb.Append(\"ana_sperm_count integer,ana_sperm_ac";
_strb.Append("ana_sperm_count integer,ana_sperm_active integer,videocalw integer,");
 //BA.debugLineNum = 247;BA.debugLine="strb.Append(\"videocalh integer,videoshoww integer";
_strb.Append("videocalh integer,videoshoww integer,videoshowh integer,initialthresh integer,");
 //BA.debugLineNum = 248;BA.debugLine="strb.Append(\"threshsub integer,minarea integer,ma";
_strb.Append("threshsub integer,minarea integer,maxarea integer,ratio real)");
 //BA.debugLineNum = 250;BA.debugLine="SQL1.ExecNonQuery(strb.ToString())";
_sql1.ExecNonQuery(_strb.ToString());
 //BA.debugLineNum = 252;BA.debugLine="End Sub";
return "";
}
public static String  _del_table(anywheresoftware.b4a.BA _ba,String _tablename,anywheresoftware.b4a.objects.collections.Map _params) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strsql = null;
int _i = 0;
 //BA.debugLineNum = 96;BA.debugLine="Sub del_Table(tableName As String,params As Map)";
 //BA.debugLineNum = 97;BA.debugLine="Dim strSQL As StringBuilder";
_strsql = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 98;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 99;BA.debugLine="strSQL.Initialize()";
_strsql.Initialize();
 //BA.debugLineNum = 101;BA.debugLine="If tableName <> Null And tableName <> \"\" Then";
if (_tablename!= null && (_tablename).equals("") == false) { 
 //BA.debugLineNum = 102;BA.debugLine="strSQL.Append(\"delete from \"&tableName).Append(\"";
_strsql.Append("delete from "+_tablename).Append(" where 1 = 1 ");
 //BA.debugLineNum = 103;BA.debugLine="If params.Size > 0 Then";
if (_params.getSize()>0) { 
 //BA.debugLineNum = 104;BA.debugLine="For i = 0 To params.Size - 1";
{
final int step59 = 1;
final int limit59 = (int) (_params.getSize()-1);
for (_i = (int) (0); (step59 > 0 && _i <= limit59) || (step59 < 0 && _i >= limit59); _i = ((int)(0 + _i + step59))) {
 //BA.debugLineNum = 105;BA.debugLine="strSQL.Append(\" and \"&params.GetKeyAt(i)).Appe";
_strsql.Append(" and "+BA.ObjectToString(_params.GetKeyAt(_i))).Append(" = '"+BA.ObjectToString(_params.GetValueAt(_i))).Append("' ");
 }
};
 };
 };
 //BA.debugLineNum = 110;BA.debugLine="SQL1.ExecNonQuery2(strSQL.ToString(),Array As Obj";
_sql1.ExecNonQuery2(_strsql.ToString(),anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{}));
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
return "";
}
public static String  _flagini(anywheresoftware.b4a.BA _ba) throws Exception{
boolean _flagshow = false;
 //BA.debugLineNum = 271;BA.debugLine="Sub FlagIni";
 //BA.debugLineNum = 272;BA.debugLine="Dim flagShow As Boolean";
_flagshow = false;
 //BA.debugLineNum = 274;BA.debugLine="create_File.FileExists(\"pigpet.db\")";
mostCurrent._create_file._fileexists(_ba,"pigpet.db");
 //BA.debugLineNum = 275;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 276;BA.debugLine="flagShow = TableExists(\"capture\")";
_flagshow = _tableexists(_ba,"capture");
 //BA.debugLineNum = 277;BA.debugLine="CloseSQL(\"pigpet.db\")";
_closesql(_ba,"pigpet.db");
 //BA.debugLineNum = 278;BA.debugLine="If flagShow == False Then";
if (_flagshow==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 279;BA.debugLine="CreateTeble_Capture";
_createteble_capture(_ba);
 //BA.debugLineNum = 280;BA.debugLine="CloseSQL(\"pigpet.db\")";
_closesql(_ba,"pigpet.db");
 };
 //BA.debugLineNum = 283;BA.debugLine="flagShow = TableExists(\"resulthistory\")";
_flagshow = _tableexists(_ba,"resulthistory");
 //BA.debugLineNum = 284;BA.debugLine="CloseSQL(\"pigpet.db\")";
_closesql(_ba,"pigpet.db");
 //BA.debugLineNum = 285;BA.debugLine="If flagShow == False Then";
if (_flagshow==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 286;BA.debugLine="CreateTeble_History";
_createteble_history(_ba);
 //BA.debugLineNum = 287;BA.debugLine="CloseSQL(\"pigpet.db\")";
_closesql(_ba,"pigpet.db");
 };
 //BA.debugLineNum = 290;BA.debugLine="flagShow = TableExists(\"dict_properties\")";
_flagshow = _tableexists(_ba,"dict_properties");
 //BA.debugLineNum = 291;BA.debugLine="CloseSQL(\"pigpet.db\")";
_closesql(_ba,"pigpet.db");
 //BA.debugLineNum = 292;BA.debugLine="If flagShow == False Then";
if (_flagshow==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 293;BA.debugLine="CreateTeble_Dict";
_createteble_dict(_ba);
 //BA.debugLineNum = 294;BA.debugLine="CloseSQL(\"pigpet.db\")";
_closesql(_ba,"pigpet.db");
 //BA.debugLineNum = 295;BA.debugLine="table_Dict_Begin";
_table_dict_begin(_ba);
 //BA.debugLineNum = 296;BA.debugLine="CloseSQL(\"pigpet.db\")";
_closesql(_ba,"pigpet.db");
 }else {
 //BA.debugLineNum = 298;BA.debugLine="If table_Data_Exists(\"dict_properties\") Then";
if (_table_data_exists(_ba,"dict_properties")) { 
 //BA.debugLineNum = 299;BA.debugLine="CloseSQL(\"pigpet.db\")";
_closesql(_ba,"pigpet.db");
 }else {
 //BA.debugLineNum = 301;BA.debugLine="table_Dict_Begin";
_table_dict_begin(_ba);
 //BA.debugLineNum = 302;BA.debugLine="CloseSQL(\"pigpet.db\")";
_closesql(_ba,"pigpet.db");
 };
 };
 //BA.debugLineNum = 306;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 44;BA.debugLine="Sub InsertToCapture(tableName As String,params() A";
 //BA.debugLineNum = 45;BA.debugLine="Dim strSQL As StringBuilder";
_strsql = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 46;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 47;BA.debugLine="strSQL.Initialize()";
_strsql.Initialize();
 //BA.debugLineNum = 49;BA.debugLine="If params.Length <> 0 Then";
if (_params.length!=0) { 
 //BA.debugLineNum = 50;BA.debugLine="strSQL.Append(\"insert into \"&tableName).Append(\"";
_strsql.Append("insert into "+_tablename).Append(" values(");
 //BA.debugLineNum = 51;BA.debugLine="For i = 0 To params.Length - 1";
{
final int step20 = 1;
final int limit20 = (int) (_params.length-1);
for (_i = (int) (0); (step20 > 0 && _i <= limit20) || (step20 < 0 && _i >= limit20); _i = ((int)(0 + _i + step20))) {
 //BA.debugLineNum = 52;BA.debugLine="If i <> params.Length - 1 Then";
if (_i!=_params.length-1) { 
 //BA.debugLineNum = 53;BA.debugLine="strSQL.Append(\"?,\")";
_strsql.Append("?,");
 }else {
 //BA.debugLineNum = 55;BA.debugLine="strSQL.Append(\"?)\")";
_strsql.Append("?)");
 };
 }
};
 };
 //BA.debugLineNum = 60;BA.debugLine="SQL1.ExecNonQuery2(strSQL.ToString(),params)";
_sql1.ExecNonQuery2(_strsql.ToString(),anywheresoftware.b4a.keywords.Common.ArrayToList(_params));
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 120;BA.debugLine="Sub QueryColumnData(tableName As String,columns()";
 //BA.debugLineNum = 121;BA.debugLine="Dim strSQL As StringBuilder";
_strsql = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 122;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 123;BA.debugLine="strSQL.Initialize()";
_strsql.Initialize();
 //BA.debugLineNum = 125;BA.debugLine="If columns.Length <> 0 Then";
if (_columns.length!=0) { 
 //BA.debugLineNum = 126;BA.debugLine="strSQL.Append(\"select \")";
_strsql.Append("select ");
 //BA.debugLineNum = 127;BA.debugLine="For i = 0 To columns.Length - 1";
{
final int step72 = 1;
final int limit72 = (int) (_columns.length-1);
for (_i = (int) (0); (step72 > 0 && _i <= limit72) || (step72 < 0 && _i >= limit72); _i = ((int)(0 + _i + step72))) {
 //BA.debugLineNum = 128;BA.debugLine="If i <> columns.Length - 1 Then";
if (_i!=_columns.length-1) { 
 //BA.debugLineNum = 129;BA.debugLine="strSQL.Append(columns(i)).Append(\",\")";
_strsql.Append(BA.ObjectToString(_columns[_i])).Append(",");
 }else {
 //BA.debugLineNum = 131;BA.debugLine="strSQL.Append(columns(i)).Append(\" from \")";
_strsql.Append(BA.ObjectToString(_columns[_i])).Append(" from ");
 };
 }
};
 //BA.debugLineNum = 134;BA.debugLine="strSQL.Append(tableName)";
_strsql.Append(_tablename);
 //BA.debugLineNum = 136;BA.debugLine="If params.Size <> 0 Then";
if (_params.getSize()!=0) { 
 //BA.debugLineNum = 137;BA.debugLine="strSQL.Append(\" where 1 = 1 \")";
_strsql.Append(" where 1 = 1 ");
 //BA.debugLineNum = 138;BA.debugLine="For i = 0 To params.Size - 1";
{
final int step82 = 1;
final int limit82 = (int) (_params.getSize()-1);
for (_i = (int) (0); (step82 > 0 && _i <= limit82) || (step82 < 0 && _i >= limit82); _i = ((int)(0 + _i + step82))) {
 //BA.debugLineNum = 139;BA.debugLine="strSQL.Append(\" and \"&params.GetKeyAt(i)).Appe";
_strsql.Append(" and "+BA.ObjectToString(_params.GetKeyAt(_i))).Append(" = '"+BA.ObjectToString(_params.GetValueAt(_i))+"'");
 }
};
 };
 //BA.debugLineNum = 143;BA.debugLine="If flag_distinct Then";
if (_flag_distinct) { 
 //BA.debugLineNum = 144;BA.debugLine="strSQL.Append(\" group by \")";
_strsql.Append(" group by ");
 //BA.debugLineNum = 145;BA.debugLine="For i = 0 To columns.Length - 1";
{
final int step88 = 1;
final int limit88 = (int) (_columns.length-1);
for (_i = (int) (0); (step88 > 0 && _i <= limit88) || (step88 < 0 && _i >= limit88); _i = ((int)(0 + _i + step88))) {
 //BA.debugLineNum = 146;BA.debugLine="If i <> columns.Length - 1 Then";
if (_i!=_columns.length-1) { 
 //BA.debugLineNum = 147;BA.debugLine="strSQL.Append(columns(i)).Append(\",\")";
_strsql.Append(BA.ObjectToString(_columns[_i])).Append(",");
 }else {
 //BA.debugLineNum = 149;BA.debugLine="strSQL.Append(columns(i)).Append(\" \")";
_strsql.Append(BA.ObjectToString(_columns[_i])).Append(" ");
 };
 }
};
 };
 //BA.debugLineNum = 156;BA.debugLine="cursor1 = SQL1.ExecQuery(strSQL.ToString())";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery(_strsql.ToString())));
 };
 //BA.debugLineNum = 160;BA.debugLine="Return cursor1";
if (true) return _cursor1;
 //BA.debugLineNum = 161;BA.debugLine="End Sub";
return null;
}
public static String  _queryidmax(anywheresoftware.b4a.BA _ba,String _tablename,String _params) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strsql = null;
String _strreturn = "";
 //BA.debugLineNum = 165;BA.debugLine="Sub QueryIDMax(tableName As String,params As Strin";
 //BA.debugLineNum = 166;BA.debugLine="Dim strSQL As StringBuilder";
_strsql = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 167;BA.debugLine="Dim strReturn As String";
_strreturn = "";
 //BA.debugLineNum = 168;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 169;BA.debugLine="strSQL.Initialize()";
_strsql.Initialize();
 //BA.debugLineNum = 171;BA.debugLine="strSQL.Append(\"select max(\").Append(params).Appen";
_strsql.Append("select max(").Append(_params).Append(") from ").Append(_tablename);
 //BA.debugLineNum = 173;BA.debugLine="strReturn = SQL1.ExecQuerySingleResult(strSQL.ToS";
_strreturn = _sql1.ExecQuerySingleResult(_strsql.ToString());
 //BA.debugLineNum = 174;BA.debugLine="SQL1.Close()";
_sql1.Close();
 //BA.debugLineNum = 175;BA.debugLine="If strReturn <> Null And strReturn <> \"\" Then";
if (_strreturn!= null && (_strreturn).equals("") == false) { 
 //BA.debugLineNum = 176;BA.debugLine="Return strReturn";
if (true) return _strreturn;
 }else {
 //BA.debugLineNum = 178;BA.debugLine="strReturn = \"0\"";
_strreturn = "0";
 //BA.debugLineNum = 179;BA.debugLine="If params <> Null Then";
if (_params!= null) { 
 //BA.debugLineNum = 180;BA.debugLine="If \"pigid\" == params Then";
if (("pigid").equals(_params)) { 
 //BA.debugLineNum = 181;BA.debugLine="strReturn = 10000";
_strreturn = BA.NumberToString(10000);
 };
 };
 };
 //BA.debugLineNum = 185;BA.debugLine="Return strReturn";
if (true) return _strreturn;
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static boolean  _table_data_exists(anywheresoftware.b4a.BA _ba,String _tablename) throws Exception{
String _strsql = "";
int _resultsql = 0;
 //BA.debugLineNum = 208;BA.debugLine="Sub table_Data_Exists(tableName As String) As Bool";
 //BA.debugLineNum = 209;BA.debugLine="Dim strSQL As String";
_strsql = "";
 //BA.debugLineNum = 210;BA.debugLine="Dim resultSQL As Int";
_resultsql = 0;
 //BA.debugLineNum = 211;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 212;BA.debugLine="strSQL = \"select count(*) from \"&tableName";
_strsql = "select count(*) from "+_tablename;
 //BA.debugLineNum = 213;BA.debugLine="resultSQL = SQL1.ExecQuerySingleResult(strSQL)";
_resultsql = (int)(Double.parseDouble(_sql1.ExecQuerySingleResult(_strsql)));
 //BA.debugLineNum = 215;BA.debugLine="If resultSQL == 0 Then";
if (_resultsql==0) { 
 //BA.debugLineNum = 216;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 218;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 220;BA.debugLine="End Sub";
return false;
}
public static String  _table_dict_begin(anywheresoftware.b4a.BA _ba) throws Exception{
Object[] _params = null;
String _id = "";
anywheresoftware.b4a.objects.collections.Map _paramsmap = null;
anywheresoftware.b4a.objects.collections.Map _columns = null;
int _i = 0;
 //BA.debugLineNum = 309;BA.debugLine="Sub table_Dict_Begin";
 //BA.debugLineNum = 310;BA.debugLine="Dim params() As Object";
_params = new Object[(int) (0)];
{
int d0 = _params.length;
for (int i0 = 0;i0 < d0;i0++) {
_params[i0] = new Object();
}
}
;
 //BA.debugLineNum = 311;BA.debugLine="Dim id As String";
_id = "";
 //BA.debugLineNum = 312;BA.debugLine="Dim paramsMap As Map";
_paramsmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 313;BA.debugLine="Dim columns As Map";
_columns = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 315;BA.debugLine="columns.Initialize()";
_columns.Initialize();
 //BA.debugLineNum = 316;BA.debugLine="paramsMap.Initialize()";
_paramsmap.Initialize();
 //BA.debugLineNum = 318;BA.debugLine="del_Table(\"dict_properties\",columns)";
_del_table(_ba,"dict_properties",_columns);
 //BA.debugLineNum = 320;BA.debugLine="paramsMap.Put(\"videocalw\",\"640\")";
_paramsmap.Put((Object)("videocalw"),(Object)("640"));
 //BA.debugLineNum = 321;BA.debugLine="paramsMap.Put(\"videocalh\",\"480\")";
_paramsmap.Put((Object)("videocalh"),(Object)("480"));
 //BA.debugLineNum = 322;BA.debugLine="paramsMap.Put(\"videoshoww\",\"640\")";
_paramsmap.Put((Object)("videoshoww"),(Object)("640"));
 //BA.debugLineNum = 323;BA.debugLine="paramsMap.Put(\"videoshowh\",\"480\")";
_paramsmap.Put((Object)("videoshowh"),(Object)("480"));
 //BA.debugLineNum = 324;BA.debugLine="paramsMap.Put(\"initialthresh\",\"80\")";
_paramsmap.Put((Object)("initialthresh"),(Object)("80"));
 //BA.debugLineNum = 325;BA.debugLine="paramsMap.Put(\"threshsub\",\"20\")";
_paramsmap.Put((Object)("threshsub"),(Object)("20"));
 //BA.debugLineNum = 326;BA.debugLine="paramsMap.Put(\"minarea\",\"6\")";
_paramsmap.Put((Object)("minarea"),(Object)("6"));
 //BA.debugLineNum = 327;BA.debugLine="paramsMap.Put(\"maxarea\",\"24\")";
_paramsmap.Put((Object)("maxarea"),(Object)("24"));
 //BA.debugLineNum = 328;BA.debugLine="paramsMap.Put(\"ratio\",\"0.159\")";
_paramsmap.Put((Object)("ratio"),(Object)("0.159"));
 //BA.debugLineNum = 329;BA.debugLine="paramsMap.Put(\"nshowmidrst\",\"1\")";
_paramsmap.Put((Object)("nshowmidrst"),(Object)("1"));
 //BA.debugLineNum = 330;BA.debugLine="For i = 0 To paramsMap.Size - 1";
{
final int step224 = 1;
final int limit224 = (int) (_paramsmap.getSize()-1);
for (_i = (int) (0); (step224 > 0 && _i <= limit224) || (step224 < 0 && _i >= limit224); _i = ((int)(0 + _i + step224))) {
 //BA.debugLineNum = 331;BA.debugLine="id = QueryIDMax(\"dict_properties\",\"id\")";
_id = _queryidmax(_ba,"dict_properties","id");
 //BA.debugLineNum = 332;BA.debugLine="params = Array As Object(id+1,paramsMap.GetKeyAt";
_params = new Object[]{(Object)((double)(Double.parseDouble(_id))+1),_paramsmap.GetKeyAt(_i),_paramsmap.GetValueAt(_i),(Object)("[isperm]"),(Object)("")};
 //BA.debugLineNum = 333;BA.debugLine="InsertToCapture(\"dict_properties\",params)";
_inserttocapture(_ba,"dict_properties",_params);
 }
};
 //BA.debugLineNum = 335;BA.debugLine="End Sub";
return "";
}
public static boolean  _tableexists(anywheresoftware.b4a.BA _ba,String _tablename) throws Exception{
String _strsql = "";
int _resultsql = 0;
 //BA.debugLineNum = 191;BA.debugLine="Sub TableExists(tableName As String) As Boolean";
 //BA.debugLineNum = 192;BA.debugLine="Dim strSQL As String";
_strsql = "";
 //BA.debugLineNum = 193;BA.debugLine="Dim resultSQL As Int";
_resultsql = 0;
 //BA.debugLineNum = 194;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 195;BA.debugLine="strSQL = \"select count(*) from sqlite_master wher";
_strsql = "select count(*) from sqlite_master where type = 'table' and name = '"+_tablename+"'";
 //BA.debugLineNum = 196;BA.debugLine="resultSQL = SQL1.ExecQuerySingleResult(strSQL)";
_resultsql = (int)(Double.parseDouble(_sql1.ExecQuerySingleResult(_strsql)));
 //BA.debugLineNum = 198;BA.debugLine="If resultSQL == 0 Then";
if (_resultsql==0) { 
 //BA.debugLineNum = 199;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 201;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 203;BA.debugLine="End Sub";
return false;
}
public static String  _updatetotable(anywheresoftware.b4a.BA _ba,String _tablename,anywheresoftware.b4a.objects.collections.Map _columns,anywheresoftware.b4a.objects.collections.Map _params) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _strsql = null;
int _i = 0;
 //BA.debugLineNum = 68;BA.debugLine="Sub UpdateToTable(tableName As String,columns As M";
 //BA.debugLineNum = 69;BA.debugLine="Dim strSQL As StringBuilder";
_strsql = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 70;BA.debugLine="InitSQL(\"pigpet.db\")";
_initsql(_ba,"pigpet.db");
 //BA.debugLineNum = 71;BA.debugLine="strSQL.Initialize()";
_strsql.Initialize();
 //BA.debugLineNum = 73;BA.debugLine="If columns.Size > 0 Then";
if (_columns.getSize()>0) { 
 //BA.debugLineNum = 74;BA.debugLine="strSQL.Append(\"update \"&tableName).Append(\" set";
_strsql.Append("update "+_tablename).Append(" set ");
 //BA.debugLineNum = 75;BA.debugLine="For i = 0 To columns.Size - 1";
{
final int step36 = 1;
final int limit36 = (int) (_columns.getSize()-1);
for (_i = (int) (0); (step36 > 0 && _i <= limit36) || (step36 < 0 && _i >= limit36); _i = ((int)(0 + _i + step36))) {
 //BA.debugLineNum = 76;BA.debugLine="If i <> columns.Size - 1  Then";
if (_i!=_columns.getSize()-1) { 
 //BA.debugLineNum = 77;BA.debugLine="strSQL.Append(columns.GetKeyAt(i)).Append(\" =";
_strsql.Append(BA.ObjectToString(_columns.GetKeyAt(_i))).Append(" = '"+BA.ObjectToString(_columns.GetValueAt(_i))).Append("',");
 }else {
 //BA.debugLineNum = 79;BA.debugLine="strSQL.Append(columns.GetKeyAt(i)).Append(\" =";
_strsql.Append(BA.ObjectToString(_columns.GetKeyAt(_i))).Append(" = '"+BA.ObjectToString(_columns.GetValueAt(_i))).Append("' ");
 };
 }
};
 //BA.debugLineNum = 82;BA.debugLine="strSQL.Append(\" where 1 = 1 \")";
_strsql.Append(" where 1 = 1 ");
 //BA.debugLineNum = 83;BA.debugLine="If params.Size > 0 Then";
if (_params.getSize()>0) { 
 //BA.debugLineNum = 84;BA.debugLine="For i = 0 To params.Size - 1";
{
final int step45 = 1;
final int limit45 = (int) (_params.getSize()-1);
for (_i = (int) (0); (step45 > 0 && _i <= limit45) || (step45 < 0 && _i >= limit45); _i = ((int)(0 + _i + step45))) {
 //BA.debugLineNum = 85;BA.debugLine="strSQL.Append(\" and \"&params.GetKeyAt(i)).Appe";
_strsql.Append(" and "+BA.ObjectToString(_params.GetKeyAt(_i))).Append(" = '"+BA.ObjectToString(_params.GetValueAt(_i))).Append("'");
 }
};
 };
 };
 //BA.debugLineNum = 90;BA.debugLine="SQL1.ExecNonQuery(strSQL.ToString())";
_sql1.ExecNonQuery(_strsql.ToString());
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
}
