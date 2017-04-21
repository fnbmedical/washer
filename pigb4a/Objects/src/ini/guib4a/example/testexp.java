package ini.guib4a.example;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class testexp extends Activity implements B4AActivity{
	public static testexp mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "ini.guib4a.example", "ini.guib4a.example.testexp");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (testexp).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
		BA.handler.postDelayed(new WaitForLayout(), 5);

	}
	private static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "ini.guib4a.example", "ini.guib4a.example.testexp");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "ini.guib4a.example.testexp", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (testexp) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (testexp) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return testexp.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (testexp) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (testexp) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnanay = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndrawcontours = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnexit = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btngaussianblur = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnstretch = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnsubdraw = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btntomain = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _igshow = null;
public anywheresoftware.b4a.objects.ListViewWrapper _ltv_anay_point = null;
public anywheresoftware.b4a.objects.ListViewWrapper _ltv_anay_time = null;
public anywheresoftware.b4a.objects.ListViewWrapper _ltv_pigid = null;
public anywheresoftware.b4a.objects.ListViewWrapper _ltvtable = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_count = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_rate = null;
public com.example.ispermcpp.IspermCpp _isperm = null;
public static String _pigid = "";
public static String _ana_capture_point = "";
public static String _anay_time = "";
public static String _ana_capture_path = "";
public static String _ana_history_path = "";
public ini.guib4a.example.main _main = null;
public ini.guib4a.example.sql_capture _sql_capture = null;
public ini.guib4a.example.create_file _create_file = null;
public ini.guib4a.example.iniexp _iniexp = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
Object _e = null;
 //BA.debugLineNum = 61;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 64;BA.debugLine="Activity.LoadLayout(\"testWindow\")";
mostCurrent._activity.LoadLayout("testWindow",mostCurrent.activityBA);
 //BA.debugLineNum = 65;BA.debugLine="Dim jo As JavaObject = Activity";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._activity.getObject()));
 //BA.debugLineNum = 66;BA.debugLine="Dim e As Object = jo.CreateEvent(\"android.view";
_e = _jo.CreateEvent(processBA,"android.view.View.OnSystemUiVisibilityChangeListener","VisibilityChanged",anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 67;BA.debugLine="jo.RunMethod(\"setOnSystemUiVisibilityChangeLis";
_jo.RunMethod("setOnSystemUiVisibilityChangeListener",new Object[]{_e});
 //BA.debugLineNum = 68;BA.debugLine="Activity.Title = \"分析数据\"";
mostCurrent._activity.setTitle((Object)("分析数据"));
 //BA.debugLineNum = 69;BA.debugLine="Activity.Width = -1";
mostCurrent._activity.setWidth((int) (-1));
 //BA.debugLineNum = 70;BA.debugLine="Activity.Height = -1";
mostCurrent._activity.setHeight((int) (-1));
 //BA.debugLineNum = 71;BA.debugLine="IniListView_Pigid";
_inilistview_pigid();
 //BA.debugLineNum = 72;BA.debugLine="IniListView_Point";
_inilistview_point();
 //BA.debugLineNum = 73;BA.debugLine="IniListView_Time";
_inilistview_time();
 //BA.debugLineNum = 74;BA.debugLine="Show_Photo";
_show_photo();
 //BA.debugLineNum = 75;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 76;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 86;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 88;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 78;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 79;BA.debugLine="IniListView_Pigid";
_inilistview_pigid();
 //BA.debugLineNum = 80;BA.debugLine="IniListView_Point";
_inilistview_point();
 //BA.debugLineNum = 81;BA.debugLine="IniListView_Time";
_inilistview_time();
 //BA.debugLineNum = 82;BA.debugLine="Show_Photo";
_show_photo();
 //BA.debugLineNum = 83;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return "";
}
public static String  _activity_windowfocuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 55;BA.debugLine="Sub Activity_WindowFocusChanged(HasFocus As Boolea";
 //BA.debugLineNum = 56;BA.debugLine="If HasFocus Then";
if (_hasfocus) { 
 //BA.debugLineNum = 57;BA.debugLine="ForceImmersiceMode";
_forceimmersicemode();
 };
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _btnanay_click() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _isperm_dict_cursors = null;
float[] _isperm_dict_array = null;
int[] _isperm_count = null;
Object[] _isperm_dict_cloumns = null;
anywheresoftware.b4a.objects.collections.Map _isperm_dict_params = null;
anywheresoftware.b4a.objects.collections.Map _isperm_dict_map = null;
long _timenow = 0L;
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _id = 0;
Object[] _history_params = null;
int _i = 0;
 //BA.debugLineNum = 308;BA.debugLine="Sub btnAnay_Click";
 //BA.debugLineNum = 309;BA.debugLine="Dim isperm_dict_cursors As Cursor";
_isperm_dict_cursors = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 310;BA.debugLine="Dim isperm_dict_array() As Float";
_isperm_dict_array = new float[(int) (0)];
;
 //BA.debugLineNum = 311;BA.debugLine="Dim isperm_Count() As Int";
_isperm_count = new int[(int) (0)];
;
 //BA.debugLineNum = 312;BA.debugLine="Dim isperm_dict_cloumns() As Object";
_isperm_dict_cloumns = new Object[(int) (0)];
{
int d0 = _isperm_dict_cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_isperm_dict_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 313;BA.debugLine="Dim isperm_dict_params As Map";
_isperm_dict_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 314;BA.debugLine="Dim isperm_dict_map As Map";
_isperm_dict_map = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 316;BA.debugLine="Dim timeNow As Long";
_timenow = 0L;
 //BA.debugLineNum = 317;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 318;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 319;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 321;BA.debugLine="Dim id As Int";
_id = 0;
 //BA.debugLineNum = 322;BA.debugLine="Dim history_params() As Object";
_history_params = new Object[(int) (0)];
{
int d0 = _history_params.length;
for (int i0 = 0;i0 < d0;i0++) {
_history_params[i0] = new Object();
}
}
;
 //BA.debugLineNum = 324;BA.debugLine="isperm_dict_map.Initialize()";
_isperm_dict_map.Initialize();
 //BA.debugLineNum = 325;BA.debugLine="isperm_dict_params.Initialize()";
_isperm_dict_params.Initialize();
 //BA.debugLineNum = 326;BA.debugLine="isperm_dict_cloumns = Array As Object(\"name\",\"val";
_isperm_dict_cloumns = new Object[]{(Object)("name"),(Object)("value")};
 //BA.debugLineNum = 328;BA.debugLine="btnEnabled(False)";
_btnenabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 329;BA.debugLine="cloumns = Array As Object(\"ana_path\")";
_cloumns = new Object[]{(Object)("ana_path")};
 //BA.debugLineNum = 330;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 331;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 332;BA.debugLine="ana_history_path = \"\"";
mostCurrent._ana_history_path = "";
 //BA.debugLineNum = 333;BA.debugLine="ana_capture_path = \"\"";
mostCurrent._ana_capture_path = "";
 //BA.debugLineNum = 335;BA.debugLine="If pigid <> Null And pigid <> \"\" Then";
if (mostCurrent._pigid!= null && (mostCurrent._pigid).equals("") == false) { 
 //BA.debugLineNum = 336;BA.debugLine="params.Put(\"pigid\",pigid)";
_params.Put((Object)("pigid"),(Object)(mostCurrent._pigid));
 //BA.debugLineNum = 337;BA.debugLine="params.Put(\"ana_capture_point\",ana_capture_point";
_params.Put((Object)("ana_capture_point"),(Object)(mostCurrent._ana_capture_point));
 };
 //BA.debugLineNum = 341;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"capture\",c";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"capture",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 342;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step249 = 1;
final int limit249 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step249 > 0 && _i <= limit249) || (step249 < 0 && _i >= limit249); _i = ((int)(0 + _i + step249))) {
 //BA.debugLineNum = 343;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 344;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 345;BA.debugLine="ana_capture_path = cursor1.GetString(cloumns(0)";
mostCurrent._ana_capture_path = _cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]));
 };
 }
};
 //BA.debugLineNum = 349;BA.debugLine="If ana_capture_path <> \"\" And ana_capture_path <>";
if ((mostCurrent._ana_capture_path).equals("") == false && mostCurrent._ana_capture_path!= null) { 
 //BA.debugLineNum = 351;BA.debugLine="timeNow = DateTime.Now";
_timenow = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 352;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 353;BA.debugLine="ana_history_path = create_File.Create_Path_Dir(a";
mostCurrent._ana_history_path = mostCurrent._create_file._create_path_dir(mostCurrent.activityBA,mostCurrent._ana_capture_path,_timenow);
 //BA.debugLineNum = 354;BA.debugLine="isperm_dict_cursors = sql_capture.QueryColumnDat";
_isperm_dict_cursors = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"dict_properties",_isperm_dict_cloumns,_isperm_dict_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 356;BA.debugLine="For i = 0 To isperm_dict_cursors.RowCount - 1";
{
final int step260 = 1;
final int limit260 = (int) (_isperm_dict_cursors.getRowCount()-1);
for (_i = (int) (0); (step260 > 0 && _i <= limit260) || (step260 < 0 && _i >= limit260); _i = ((int)(0 + _i + step260))) {
 //BA.debugLineNum = 357;BA.debugLine="isperm_dict_cursors.Position = i";
_isperm_dict_cursors.setPosition(_i);
 //BA.debugLineNum = 358;BA.debugLine="isperm_dict_map.put(isperm_dict_cursors.GetStri";
_isperm_dict_map.Put((Object)(_isperm_dict_cursors.GetString(BA.ObjectToString(_isperm_dict_cloumns[(int) (0)]))),(Object)(_isperm_dict_cursors.GetString(BA.ObjectToString(_isperm_dict_cloumns[(int) (1)]))));
 }
};
 //BA.debugLineNum = 362;BA.debugLine="If isperm_dict_map.Size > 9 Then";
if (_isperm_dict_map.getSize()>9) { 
 //BA.debugLineNum = 363;BA.debugLine="isperm_dict_array = Array As Float(isperm_dict_";
_isperm_dict_array = new float[]{(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (0)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (1)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (2)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (3)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (4)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (5)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (6)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (7)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (8)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (9))))};
 };
 //BA.debugLineNum = 368;BA.debugLine="isperm_Count = isperm.returnCount(File.DirRootEx";
_isperm_count = mostCurrent._isperm.returnCount(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._ana_capture_path+"/",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._ana_history_path,_isperm_dict_array);
 //BA.debugLineNum = 375;BA.debugLine="id = sql_capture.QueryIDMax(\"resulthistory\",\"id\"";
_id = (int)(Double.parseDouble(mostCurrent._sql_capture._queryidmax(mostCurrent.activityBA,"resulthistory","id")));
 //BA.debugLineNum = 376;BA.debugLine="history_params = Array As Object(id+1,pigid,crea";
_history_params = new Object[]{(Object)(_id+1),(Object)(mostCurrent._pigid),(Object)(mostCurrent._create_file._timeformat(mostCurrent.activityBA,_timenow)),(Object)(mostCurrent._ana_capture_point),(Object)(mostCurrent._ana_history_path),(Object)(""),(Object)(_isperm_count[(int) (0)]),(Object)(_isperm_count[(int) (1)]),(Object)(_isperm_dict_array[(int) (0)]),(Object)(_isperm_dict_array[(int) (1)]),(Object)(_isperm_dict_array[(int) (2)]),(Object)(_isperm_dict_array[(int) (3)]),(Object)(_isperm_dict_array[(int) (4)]),(Object)(_isperm_dict_array[(int) (5)]),(Object)(_isperm_dict_array[(int) (6)]),(Object)(_isperm_dict_array[(int) (7)]),(Object)(_isperm_dict_array[(int) (8)])};
 //BA.debugLineNum = 380;BA.debugLine="sql_capture.InsertToCapture(\"resulthistory\",hist";
mostCurrent._sql_capture._inserttocapture(mostCurrent.activityBA,"resulthistory",_history_params);
 //BA.debugLineNum = 382;BA.debugLine="IniListView_Time";
_inilistview_time();
 }else {
 //BA.debugLineNum = 384;BA.debugLine="ToastMessageShow(\"此采集点并未采集到图片....\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("此采集点并未采集到图片....",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 386;BA.debugLine="btnEnabled(True)";
_btnenabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 387;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 389;BA.debugLine="End Sub";
return "";
}
public static String  _btndrawcontours_click() throws Exception{
 //BA.debugLineNum = 250;BA.debugLine="Sub btnDrawContours_Click";
 //BA.debugLineNum = 251;BA.debugLine="Show_anay_Photo(\"DrawContours\")";
_show_anay_photo("DrawContours");
 //BA.debugLineNum = 252;BA.debugLine="End Sub";
return "";
}
public static String  _btnenabled(boolean _flag_enabled) throws Exception{
 //BA.debugLineNum = 254;BA.debugLine="Sub btnEnabled(flag_Enabled As Boolean)";
 //BA.debugLineNum = 255;BA.debugLine="btnAnay.Enabled = flag_Enabled";
mostCurrent._btnanay.setEnabled(_flag_enabled);
 //BA.debugLineNum = 256;BA.debugLine="btnToMain.Enabled = flag_Enabled";
mostCurrent._btntomain.setEnabled(_flag_enabled);
 //BA.debugLineNum = 257;BA.debugLine="btnExit.Enabled = flag_Enabled";
mostCurrent._btnexit.setEnabled(_flag_enabled);
 //BA.debugLineNum = 258;BA.debugLine="End Sub";
return "";
}
public static String  _btnexit_click() throws Exception{
 //BA.debugLineNum = 244;BA.debugLine="Sub btnExit_Click";
 //BA.debugLineNum = 245;BA.debugLine="Activity.Finish()";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 247;BA.debugLine="End Sub";
return "";
}
public static String  _btngaussianblur_click() throws Exception{
 //BA.debugLineNum = 239;BA.debugLine="Sub btnGaussianBlur_Click";
 //BA.debugLineNum = 240;BA.debugLine="Show_anay_Photo(\"GaussianBlur\")";
_show_anay_photo("GaussianBlur");
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
return "";
}
public static String  _btnstretch_click() throws Exception{
 //BA.debugLineNum = 234;BA.debugLine="Sub btnStretch_Click";
 //BA.debugLineNum = 235;BA.debugLine="Show_anay_Photo(\"Stretch\")";
_show_anay_photo("Stretch");
 //BA.debugLineNum = 236;BA.debugLine="End Sub";
return "";
}
public static String  _btnsubdraw_click() throws Exception{
 //BA.debugLineNum = 229;BA.debugLine="Sub btnSubDraw_Click";
 //BA.debugLineNum = 230;BA.debugLine="Show_anay_Photo(\"SubDrawContours\")";
_show_anay_photo("SubDrawContours");
 //BA.debugLineNum = 231;BA.debugLine="End Sub";
return "";
}
public static String  _btntomain_click() throws Exception{
 //BA.debugLineNum = 221;BA.debugLine="Sub btnToMain_Click";
 //BA.debugLineNum = 224;BA.debugLine="StartActivity(IniExp)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._iniexp.getObject()));
 //BA.debugLineNum = 226;BA.debugLine="End Sub";
return "";
}
public static String  _forceimmersicemode() throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 46;BA.debugLine="Sub ForceImmersiceMode";
 //BA.debugLineNum = 47;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 48;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 49;BA.debugLine="r.Target = r.RunMethod(\"getWindow\")";
_r.Target = _r.RunMethod("getWindow");
 //BA.debugLineNum = 50;BA.debugLine="r.Target = r.RunMethod(\"getDecorView\")";
_r.Target = _r.RunMethod("getDecorView");
 //BA.debugLineNum = 51;BA.debugLine="r.RunMethod2(\"setSystemUiVisibility\", 5894, \"java";
_r.RunMethod2("setSystemUiVisibility",BA.NumberToString(5894),"java.lang.int");
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public static String  _get_ana_capture_path() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 130;BA.debugLine="Sub get_ana_capture_path";
 //BA.debugLineNum = 131;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 132;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 133;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 135;BA.debugLine="cloumns = Array As Object(\"ana_path\")";
_cloumns = new Object[]{(Object)("ana_path")};
 //BA.debugLineNum = 136;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 137;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 138;BA.debugLine="ana_capture_path = \"\"";
mostCurrent._ana_capture_path = "";
 //BA.debugLineNum = 139;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"capture\",c";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"capture",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 140;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step95 = 1;
final int limit95 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step95 > 0 && _i <= limit95) || (step95 < 0 && _i >= limit95); _i = ((int)(0 + _i + step95))) {
 //BA.debugLineNum = 141;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 142;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 143;BA.debugLine="ana_capture_path = cursor1.GetString(cloumns(0)";
mostCurrent._ana_capture_path = _cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]));
 };
 }
};
 //BA.debugLineNum = 146;BA.debugLine="Show_Photo";
_show_photo();
 //BA.debugLineNum = 147;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 18;BA.debugLine="Private btnAnay As Button";
mostCurrent._btnanay = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private btnDrawContours As Button";
mostCurrent._btndrawcontours = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private btnExit As Button";
mostCurrent._btnexit = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private btnGaussianBlur As Button";
mostCurrent._btngaussianblur = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private btnStretch As Button";
mostCurrent._btnstretch = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private btnSubDraw As Button";
mostCurrent._btnsubdraw = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private btnToMain As Button";
mostCurrent._btntomain = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private igShow As ImageView";
mostCurrent._igshow = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private ltv_anay_point As ListView";
mostCurrent._ltv_anay_point = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private ltv_anay_time As ListView";
mostCurrent._ltv_anay_time = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private ltv_pigid As ListView";
mostCurrent._ltv_pigid = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private ltvTable As ListView";
mostCurrent._ltvtable = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private lbl_Count As Label";
mostCurrent._lbl_count = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private lbl_Rate As Label";
mostCurrent._lbl_rate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim isperm As IspermCpp";
mostCurrent._isperm = new com.example.ispermcpp.IspermCpp();
 //BA.debugLineNum = 36;BA.debugLine="Dim pigid As String";
mostCurrent._pigid = "";
 //BA.debugLineNum = 37;BA.debugLine="Dim ana_capture_point As String";
mostCurrent._ana_capture_point = "";
 //BA.debugLineNum = 38;BA.debugLine="Dim anay_time As String";
mostCurrent._anay_time = "";
 //BA.debugLineNum = 40;BA.debugLine="Dim ana_capture_path As String";
mostCurrent._ana_capture_path = "";
 //BA.debugLineNum = 41;BA.debugLine="Dim ana_history_path As String";
mostCurrent._ana_history_path = "";
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return "";
}
public static String  _inilistview_pigid() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 91;BA.debugLine="Sub IniListView_Pigid";
 //BA.debugLineNum = 92;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 94;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 96;BA.debugLine="cloumns = Array As Object(\"pigid\")";
_cloumns = new Object[]{(Object)("pigid")};
 //BA.debugLineNum = 97;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 98;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 99;BA.debugLine="pigid = \"\"";
mostCurrent._pigid = "";
 //BA.debugLineNum = 100;BA.debugLine="ltv_pigid.Clear()";
mostCurrent._ltv_pigid.Clear();
 //BA.debugLineNum = 104;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"capture\",c";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"capture",_cloumns,_params,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 105;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step69 = 1;
final int limit69 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step69 > 0 && _i <= limit69) || (step69 < 0 && _i >= limit69); _i = ((int)(0 + _i + step69))) {
 //BA.debugLineNum = 106;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 107;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 108;BA.debugLine="pigid = cursor1.GetInt(cloumns(0))";
mostCurrent._pigid = BA.NumberToString(_cursor1.GetInt(BA.ObjectToString(_cloumns[(int) (0)])));
 };
 //BA.debugLineNum = 110;BA.debugLine="ltv_pigid.AddSingleLine2(cursor1.GetInt(cloumns(";
mostCurrent._ltv_pigid.AddSingleLine2(BA.NumberToString(_cursor1.GetInt(BA.ObjectToString(_cloumns[(int) (0)]))),(Object)(_cursor1.GetInt(BA.ObjectToString(_cloumns[(int) (0)]))));
 }
};
 //BA.debugLineNum = 112;BA.debugLine="get_ana_capture_path";
_get_ana_capture_path();
 //BA.debugLineNum = 113;BA.debugLine="End Sub";
return "";
}
public static String  _inilistview_point() throws Exception{
int _i = 0;
 //BA.debugLineNum = 116;BA.debugLine="Sub IniListView_Point";
 //BA.debugLineNum = 117;BA.debugLine="ltv_anay_point.Clear()";
mostCurrent._ltv_anay_point.Clear();
 //BA.debugLineNum = 120;BA.debugLine="For i = 0 To 3";
{
final int step80 = 1;
final int limit80 = (int) (3);
for (_i = (int) (0); (step80 > 0 && _i <= limit80) || (step80 < 0 && _i >= limit80); _i = ((int)(0 + _i + step80))) {
 //BA.debugLineNum = 121;BA.debugLine="ltv_anay_point.AddSingleLine2(\"采集点\"&(i+1),i+1)";
mostCurrent._ltv_anay_point.AddSingleLine2("采集点"+BA.NumberToString((_i+1)),(Object)(_i+1));
 }
};
 //BA.debugLineNum = 124;BA.debugLine="ana_capture_point = 1";
mostCurrent._ana_capture_point = BA.NumberToString(1);
 //BA.debugLineNum = 126;BA.debugLine="get_ana_capture_path";
_get_ana_capture_path();
 //BA.debugLineNum = 127;BA.debugLine="End Sub";
return "";
}
public static String  _inilistview_time() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 151;BA.debugLine="Sub IniListView_Time";
 //BA.debugLineNum = 152;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 153;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 154;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 156;BA.debugLine="cloumns = Array As Object(\"anay_time\",\"ana_path\")";
_cloumns = new Object[]{(Object)("anay_time"),(Object)("ana_path")};
 //BA.debugLineNum = 157;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 158;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 159;BA.debugLine="anay_time = \"\"";
mostCurrent._anay_time = "";
 //BA.debugLineNum = 160;BA.debugLine="ana_history_path = \"\"";
mostCurrent._ana_history_path = "";
 //BA.debugLineNum = 161;BA.debugLine="ltv_anay_time.Clear()";
mostCurrent._ltv_anay_time.Clear();
 //BA.debugLineNum = 163;BA.debugLine="If pigid <> Null And pigid <> \"\" Then";
if (mostCurrent._pigid!= null && (mostCurrent._pigid).equals("") == false) { 
 //BA.debugLineNum = 164;BA.debugLine="params.Put(\"pigid\",pigid)";
_params.Put((Object)("pigid"),(Object)(mostCurrent._pigid));
 //BA.debugLineNum = 165;BA.debugLine="params.Put(\"ana_capture_point\",ana_capture_point";
_params.Put((Object)("ana_capture_point"),(Object)(mostCurrent._ana_capture_point));
 };
 //BA.debugLineNum = 169;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"resulthist";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"resulthistory",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 170;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step119 = 1;
final int limit119 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step119 > 0 && _i <= limit119) || (step119 < 0 && _i >= limit119); _i = ((int)(0 + _i + step119))) {
 //BA.debugLineNum = 171;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 172;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 173;BA.debugLine="anay_time = cursor1.GetString(cloumns(0))";
mostCurrent._anay_time = _cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]));
 //BA.debugLineNum = 174;BA.debugLine="ana_history_path = cursor1.GetString(cloumns(1)";
mostCurrent._ana_history_path = _cursor1.GetString(BA.ObjectToString(_cloumns[(int) (1)]));
 };
 //BA.debugLineNum = 176;BA.debugLine="ltv_anay_time.AddSingleLine2(cursor1.GetString(c";
mostCurrent._ltv_anay_time.AddSingleLine2(_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)])),(Object)(_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]))));
 }
};
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
return "";
}
public static String  _ltv_anay_point_itemclick(int _position,Object _value) throws Exception{
String _strs = "";
 //BA.debugLineNum = 404;BA.debugLine="Sub ltv_anay_point_ItemClick(Position As Int, Valu";
 //BA.debugLineNum = 405;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 407;BA.debugLine="strs = Value";
_strs = BA.ObjectToString(_value);
 //BA.debugLineNum = 408;BA.debugLine="If strs <> Null And strs <> \"\" Then";
if (_strs!= null && (_strs).equals("") == false) { 
 //BA.debugLineNum = 409;BA.debugLine="ana_capture_point = strs";
mostCurrent._ana_capture_point = _strs;
 //BA.debugLineNum = 410;BA.debugLine="IniListView_Time";
_inilistview_time();
 //BA.debugLineNum = 411;BA.debugLine="Show_Photo";
_show_photo();
 //BA.debugLineNum = 412;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 };
 //BA.debugLineNum = 414;BA.debugLine="End Sub";
return "";
}
public static String  _ltv_anay_time_itemclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor_path = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 416;BA.debugLine="Sub ltv_anay_time_ItemClick (Position As Int, Valu";
 //BA.debugLineNum = 417;BA.debugLine="Dim cursor_path As Cursor";
_cursor_path = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 418;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 419;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 421;BA.debugLine="cloumns = Array As Object(\"ana_path\")";
_cloumns = new Object[]{(Object)("ana_path")};
 //BA.debugLineNum = 422;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 423;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 424;BA.debugLine="ana_history_path = \"\"";
mostCurrent._ana_history_path = "";
 //BA.debugLineNum = 426;BA.debugLine="If pigid <> Null And pigid <> \"\" Then";
if (mostCurrent._pigid!= null && (mostCurrent._pigid).equals("") == false) { 
 //BA.debugLineNum = 427;BA.debugLine="params.Put(\"pigid\",pigid)";
_params.Put((Object)("pigid"),(Object)(mostCurrent._pigid));
 //BA.debugLineNum = 428;BA.debugLine="params.Put(\"ana_capture_point\",ana_capture_point";
_params.Put((Object)("ana_capture_point"),(Object)(mostCurrent._ana_capture_point));
 //BA.debugLineNum = 429;BA.debugLine="params.Put(\"anay_time\",Value)";
_params.Put((Object)("anay_time"),_value);
 };
 //BA.debugLineNum = 431;BA.debugLine="anay_time = Value";
mostCurrent._anay_time = BA.ObjectToString(_value);
 //BA.debugLineNum = 433;BA.debugLine="cursor_path = sql_capture.QueryColumnData(\"result";
_cursor_path = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"resulthistory",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 434;BA.debugLine="For i = 0 To cursor_path.RowCount - 1";
{
final int step312 = 1;
final int limit312 = (int) (_cursor_path.getRowCount()-1);
for (_i = (int) (0); (step312 > 0 && _i <= limit312) || (step312 < 0 && _i >= limit312); _i = ((int)(0 + _i + step312))) {
 //BA.debugLineNum = 435;BA.debugLine="cursor_path.Position = i";
_cursor_path.setPosition(_i);
 //BA.debugLineNum = 436;BA.debugLine="ana_history_path = cursor_path.GetString(cloumns";
mostCurrent._ana_history_path = _cursor_path.GetString(BA.ObjectToString(_cloumns[(int) (0)]));
 }
};
 //BA.debugLineNum = 438;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 439;BA.debugLine="End Sub";
return "";
}
public static String  _ltv_pigid_itemclick(int _position,Object _value) throws Exception{
String _strs = "";
 //BA.debugLineNum = 392;BA.debugLine="Sub ltv_pigid_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 393;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 395;BA.debugLine="strs = Value";
_strs = BA.ObjectToString(_value);
 //BA.debugLineNum = 396;BA.debugLine="pigid = strs.Trim()";
mostCurrent._pigid = _strs.trim();
 //BA.debugLineNum = 398;BA.debugLine="IniListView_Point";
_inilistview_point();
 //BA.debugLineNum = 399;BA.debugLine="IniListView_Time";
_inilistview_time();
 //BA.debugLineNum = 400;BA.debugLine="Show_Photo";
_show_photo();
 //BA.debugLineNum = 401;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 402;BA.debugLine="End Sub";
return "";
}
public static String  _ltvtable_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 441;BA.debugLine="Sub ltvTable_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 443;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _show_anay_photo(String _picture_type) throws Exception{
 //BA.debugLineNum = 212;BA.debugLine="Sub Show_anay_Photo(picture_Type As String)";
 //BA.debugLineNum = 213;BA.debugLine="If ana_history_path <> Null And ana_history_path";
if (mostCurrent._ana_history_path!= null && (mostCurrent._ana_history_path).equals("") == false) { 
 //BA.debugLineNum = 214;BA.debugLine="If File.Exists(File.DirRootExternal, ana_history";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._ana_history_path+"/"+_picture_type+".jpg")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 215;BA.debugLine="igShow.Bitmap = LoadBitmapSample(File.DirRootEx";
mostCurrent._igshow.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._ana_history_path+"/"+_picture_type+".jpg",(int) (240),(int) (320)).getObject()));
 };
 };
 //BA.debugLineNum = 218;BA.debugLine="End Sub";
return "";
}
public static String  _show_count_active() throws Exception{
 //BA.debugLineNum = 304;BA.debugLine="Sub Show_Count_Active";
 //BA.debugLineNum = 305;BA.debugLine="End Sub";
return "";
}
public static String  _show_ltvtable() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
int _j = 0;
 //BA.debugLineNum = 261;BA.debugLine="Sub Show_LtvTable";
 //BA.debugLineNum = 262;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 263;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 264;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 266;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 267;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 268;BA.debugLine="ltvTable.Clear()";
mostCurrent._ltvtable.Clear();
 //BA.debugLineNum = 270;BA.debugLine="If anay_time <> Null And anay_time <> \"\" Then";
if (mostCurrent._anay_time!= null && (mostCurrent._anay_time).equals("") == false) { 
 //BA.debugLineNum = 271;BA.debugLine="params.Put(\"pigid\",pigid)";
_params.Put((Object)("pigid"),(Object)(mostCurrent._pigid));
 //BA.debugLineNum = 272;BA.debugLine="params.Put(\"ana_capture_point\",ana_capture_point";
_params.Put((Object)("ana_capture_point"),(Object)(mostCurrent._ana_capture_point));
 //BA.debugLineNum = 273;BA.debugLine="params.Put(\"anay_time\",anay_time)";
_params.Put((Object)("anay_time"),(Object)(mostCurrent._anay_time));
 //BA.debugLineNum = 274;BA.debugLine="cloumns = Array As Object(\"ana_sperm_count\",\"ana";
_cloumns = new Object[]{(Object)("ana_sperm_count"),(Object)("ana_sperm_active"),(Object)("videocalw"),(Object)("videocalh"),(Object)("videoshoww"),(Object)("videoshowh"),(Object)("initialthresh"),(Object)("threshsub"),(Object)("minarea"),(Object)("maxarea"),(Object)("ratio")};
 }else {
 //BA.debugLineNum = 277;BA.debugLine="cloumns = Array As Object(\"name\",\"value\")";
_cloumns = new Object[]{(Object)("name"),(Object)("value")};
 //BA.debugLineNum = 278;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"dict_prop";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"dict_properties",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 279;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step198 = 1;
final int limit198 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step198 > 0 && _i <= limit198) || (step198 < 0 && _i >= limit198); _i = ((int)(0 + _i + step198))) {
 //BA.debugLineNum = 280;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 281;BA.debugLine="If i < 9 Then";
if (_i<9) { 
 //BA.debugLineNum = 282;BA.debugLine="ltvTable.AddSingleLine2(cursor1.GetString(clou";
mostCurrent._ltvtable.AddSingleLine2(_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]))+":"+_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (1)])),(Object)(_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (1)]))));
 };
 }
};
 //BA.debugLineNum = 285;BA.debugLine="lbl_Count.Text = \"总数:\"";
mostCurrent._lbl_count.setText((Object)("总数:"));
 //BA.debugLineNum = 286;BA.debugLine="lbl_Rate.Text = \"活跃数:\"";
mostCurrent._lbl_rate.setText((Object)("活跃数:"));
 //BA.debugLineNum = 287;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 290;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"resulthist";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"resulthistory",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 291;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step209 = 1;
final int limit209 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step209 > 0 && _i <= limit209) || (step209 < 0 && _i >= limit209); _i = ((int)(0 + _i + step209))) {
 //BA.debugLineNum = 292;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 293;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 294;BA.debugLine="For j = 2 To cloumns.Length - 1";
{
final int step212 = 1;
final int limit212 = (int) (_cloumns.length-1);
for (_j = (int) (2); (step212 > 0 && _j <= limit212) || (step212 < 0 && _j >= limit212); _j = ((int)(0 + _j + step212))) {
 //BA.debugLineNum = 295;BA.debugLine="ltvTable.AddSingleLine2(cloumns(j)&\":\"&cursor1";
mostCurrent._ltvtable.AddSingleLine2(BA.ObjectToString(_cloumns[_j])+":"+_cursor1.GetString(BA.ObjectToString(_cloumns[_j])),(Object)(_cursor1.GetString(BA.ObjectToString(_cloumns[_j]))));
 }
};
 //BA.debugLineNum = 297;BA.debugLine="lbl_Count.Text = \"总数:\"&cursor1.GetString(cloumn";
mostCurrent._lbl_count.setText((Object)("总数:"+_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]))));
 //BA.debugLineNum = 298;BA.debugLine="lbl_Rate.Text = \"活跃数:\"&cursor1.GetString(cloumn";
mostCurrent._lbl_rate.setText((Object)("活跃数:"+_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (1)]))));
 };
 }
};
 //BA.debugLineNum = 301;BA.debugLine="End Sub";
return "";
}
public static String  _show_photo() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 181;BA.debugLine="Sub Show_Photo";
 //BA.debugLineNum = 182;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 183;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 184;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 186;BA.debugLine="cloumns = Array As Object(\"ana_path\")";
_cloumns = new Object[]{(Object)("ana_path")};
 //BA.debugLineNum = 187;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 188;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 189;BA.debugLine="ana_capture_path = \"\"";
mostCurrent._ana_capture_path = "";
 //BA.debugLineNum = 191;BA.debugLine="If pigid <> Null And pigid <> \"\" Then";
if (mostCurrent._pigid!= null && (mostCurrent._pigid).equals("") == false) { 
 //BA.debugLineNum = 192;BA.debugLine="params.Put(\"pigid\",pigid)";
_params.Put((Object)("pigid"),(Object)(mostCurrent._pigid));
 //BA.debugLineNum = 193;BA.debugLine="params.Put(\"ana_capture_point\",ana_capture_point";
_params.Put((Object)("ana_capture_point"),(Object)(mostCurrent._ana_capture_point));
 };
 //BA.debugLineNum = 197;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"capture\",c";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"capture",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 198;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step141 = 1;
final int limit141 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step141 > 0 && _i <= limit141) || (step141 < 0 && _i >= limit141); _i = ((int)(0 + _i + step141))) {
 //BA.debugLineNum = 199;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 200;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 201;BA.debugLine="ana_capture_path = cursor1.GetString(cloumns(0)";
mostCurrent._ana_capture_path = _cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]));
 };
 }
};
 //BA.debugLineNum = 204;BA.debugLine="If ana_capture_path <> Null And ana_capture_path";
if (mostCurrent._ana_capture_path!= null && (mostCurrent._ana_capture_path).equals("") == false) { 
 //BA.debugLineNum = 205;BA.debugLine="If File.Exists(File.DirRootExternal, ana_capture";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._ana_capture_path+"/"+"000.jpg")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 206;BA.debugLine="igShow.Bitmap = LoadBitmapSample(File.DirRootEx";
mostCurrent._igshow.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._ana_capture_path+"/000.jpg",(int) (240),(int) (320)).getObject()));
 };
 };
 //BA.debugLineNum = 209;BA.debugLine="End Sub";
return "";
}
}
