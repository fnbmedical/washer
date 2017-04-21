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

public class iniexp extends Activity implements B4AActivity{
	public static iniexp mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "ini.guib4a.example", "ini.guib4a.example.iniexp");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (iniexp).");
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
		activityBA = new BA(this, layout, processBA, "ini.guib4a.example", "ini.guib4a.example.iniexp");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "ini.guib4a.example.iniexp", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (iniexp) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (iniexp) Resume **");
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
		return iniexp.class;
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
        BA.LogInfo("** Activity (iniexp) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (iniexp) Resume **");
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
public anywheresoftware.b4a.objects.ButtonWrapper _btnaddone = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnaddten = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndelone = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndelten = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnexit = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnsave = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblview = null;
public static String _myfile = "";
public anywheresoftware.b4a.objects.ListViewWrapper _ltvtable = null;
public anywheresoftware.b4a.objects.collections.Map _itemmap = null;
public static String _itemmapkey = "";
public anywheresoftware.b4a.objects.collections.Map _itemmap_begin = null;
public static int _lbl_text_int = 0;
public static double _lbl_text_double = 0;
public ini.guib4a.example.main _main = null;
public ini.guib4a.example.sql_capture _sql_capture = null;
public ini.guib4a.example.create_file _create_file = null;
public ini.guib4a.example.testexp _testexp = null;

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
 //BA.debugLineNum = 56;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 59;BA.debugLine="Activity.LoadLayout(\"IniWindow\")";
mostCurrent._activity.LoadLayout("IniWindow",mostCurrent.activityBA);
 //BA.debugLineNum = 60;BA.debugLine="Dim jo As JavaObject = Activity";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._activity.getObject()));
 //BA.debugLineNum = 61;BA.debugLine="Dim e As Object = jo.CreateEvent(\"android.view";
_e = _jo.CreateEvent(processBA,"android.view.View.OnSystemUiVisibilityChangeListener","VisibilityChanged",anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 62;BA.debugLine="jo.RunMethod(\"setOnSystemUiVisibilityChangeLis";
_jo.RunMethod("setOnSystemUiVisibilityChangeListener",new Object[]{_e});
 //BA.debugLineNum = 63;BA.debugLine="Activity.Title = \"属性设置\"";
mostCurrent._activity.setTitle((Object)("属性设置"));
 //BA.debugLineNum = 64;BA.debugLine="Activity.Width = -1";
mostCurrent._activity.setWidth((int) (-1));
 //BA.debugLineNum = 65;BA.debugLine="Activity.Height = -1";
mostCurrent._activity.setHeight((int) (-1));
 //BA.debugLineNum = 67;BA.debugLine="read_Table_Dict";
_read_table_dict();
 //BA.debugLineNum = 68;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 69;BA.debugLine="lbl_Text_ltvTable";
_lbl_text_ltvtable();
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 78;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 72;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 73;BA.debugLine="read_Table_Dict";
_read_table_dict();
 //BA.debugLineNum = 74;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 75;BA.debugLine="lbl_Text_ltvTable";
_lbl_text_ltvtable();
 //BA.debugLineNum = 76;BA.debugLine="End Sub";
return "";
}
public static String  _activity_windowfocuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 50;BA.debugLine="Sub Activity_WindowFocusChanged(HasFocus As Boolea";
 //BA.debugLineNum = 51;BA.debugLine="If HasFocus Then";
if (_hasfocus) { 
 //BA.debugLineNum = 52;BA.debugLine="ForceImmersiceMode";
_forceimmersicemode();
 };
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _btnaddone_click() throws Exception{
 //BA.debugLineNum = 284;BA.debugLine="Sub btnAddOne_Click";
 //BA.debugLineNum = 285;BA.debugLine="If lblView.Text >= 1 Then";
if ((double)(Double.parseDouble(mostCurrent._lblview.getText()))>=1) { 
 //BA.debugLineNum = 286;BA.debugLine="lbl_text_int = lblView.Text + 1";
_lbl_text_int = (int) ((double)(Double.parseDouble(mostCurrent._lblview.getText()))+1);
 //BA.debugLineNum = 287;BA.debugLine="lblView.Text = lbl_text_int";
mostCurrent._lblview.setText((Object)(_lbl_text_int));
 }else {
 //BA.debugLineNum = 289;BA.debugLine="lbl_text_double = lblView.Text + 1 * 0.01";
_lbl_text_double = (double)(Double.parseDouble(mostCurrent._lblview.getText()))+1*0.01;
 //BA.debugLineNum = 290;BA.debugLine="lblView.Text = doubleToString(lbl_text_double)";
mostCurrent._lblview.setText((Object)(_doubletostring(_lbl_text_double)));
 };
 //BA.debugLineNum = 293;BA.debugLine="mapUpdate";
_mapupdate();
 //BA.debugLineNum = 294;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 295;BA.debugLine="End Sub";
return "";
}
public static String  _btnaddten_click() throws Exception{
 //BA.debugLineNum = 272;BA.debugLine="Sub btnAddTen_Click";
 //BA.debugLineNum = 273;BA.debugLine="If lblView.Text >= 1 Then";
if ((double)(Double.parseDouble(mostCurrent._lblview.getText()))>=1) { 
 //BA.debugLineNum = 274;BA.debugLine="lbl_text_int = lblView.Text + 10";
_lbl_text_int = (int) ((double)(Double.parseDouble(mostCurrent._lblview.getText()))+10);
 //BA.debugLineNum = 275;BA.debugLine="lblView.Text = lbl_text_int";
mostCurrent._lblview.setText((Object)(_lbl_text_int));
 }else {
 //BA.debugLineNum = 277;BA.debugLine="lbl_text_double = lblView.Text + 10 * 0.01";
_lbl_text_double = (double)(Double.parseDouble(mostCurrent._lblview.getText()))+10*0.01;
 //BA.debugLineNum = 278;BA.debugLine="lblView.Text = doubleToString(lbl_text_double)";
mostCurrent._lblview.setText((Object)(_doubletostring(_lbl_text_double)));
 };
 //BA.debugLineNum = 280;BA.debugLine="mapUpdate";
_mapupdate();
 //BA.debugLineNum = 281;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 282;BA.debugLine="End Sub";
return "";
}
public static String  _btndelone_click() throws Exception{
 //BA.debugLineNum = 252;BA.debugLine="Sub btnDelOne_Click";
 //BA.debugLineNum = 253;BA.debugLine="If lblView.Text >= 1 Then";
if ((double)(Double.parseDouble(mostCurrent._lblview.getText()))>=1) { 
 //BA.debugLineNum = 254;BA.debugLine="lbl_text_int = lblView.Text - 1";
_lbl_text_int = (int) ((double)(Double.parseDouble(mostCurrent._lblview.getText()))-1);
 //BA.debugLineNum = 255;BA.debugLine="If lbl_text_int <= 0 Then";
if (_lbl_text_int<=0) { 
 //BA.debugLineNum = 256;BA.debugLine="lblView.Text = 0";
mostCurrent._lblview.setText((Object)(0));
 }else {
 //BA.debugLineNum = 258;BA.debugLine="lblView.Text = lbl_text_int";
mostCurrent._lblview.setText((Object)(_lbl_text_int));
 };
 }else {
 //BA.debugLineNum = 261;BA.debugLine="lbl_text_double = lblView.Text - 1 * 0.01";
_lbl_text_double = (double)(Double.parseDouble(mostCurrent._lblview.getText()))-1*0.01;
 //BA.debugLineNum = 262;BA.debugLine="If lbl_text_double <= 0 Then";
if (_lbl_text_double<=0) { 
 //BA.debugLineNum = 263;BA.debugLine="lblView.Text = 0";
mostCurrent._lblview.setText((Object)(0));
 }else {
 //BA.debugLineNum = 265;BA.debugLine="lblView.Text = doubleToString(lbl_text_double)";
mostCurrent._lblview.setText((Object)(_doubletostring(_lbl_text_double)));
 };
 };
 //BA.debugLineNum = 268;BA.debugLine="mapUpdate";
_mapupdate();
 //BA.debugLineNum = 269;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 270;BA.debugLine="End Sub";
return "";
}
public static String  _btndelten_click() throws Exception{
 //BA.debugLineNum = 232;BA.debugLine="Sub btnDelTen_Click";
 //BA.debugLineNum = 233;BA.debugLine="If lblView.Text >= 1 Then";
if ((double)(Double.parseDouble(mostCurrent._lblview.getText()))>=1) { 
 //BA.debugLineNum = 234;BA.debugLine="lbl_text_int = lblView.Text - 10";
_lbl_text_int = (int) ((double)(Double.parseDouble(mostCurrent._lblview.getText()))-10);
 //BA.debugLineNum = 235;BA.debugLine="If (lbl_text_int <= 0) Then";
if ((_lbl_text_int<=0)) { 
 //BA.debugLineNum = 236;BA.debugLine="lblView.Text = 0";
mostCurrent._lblview.setText((Object)(0));
 }else {
 //BA.debugLineNum = 238;BA.debugLine="lblView.Text = lbl_text_int";
mostCurrent._lblview.setText((Object)(_lbl_text_int));
 };
 }else {
 //BA.debugLineNum = 241;BA.debugLine="lbl_text_double = lblView.Text - 10 * 0.01";
_lbl_text_double = (double)(Double.parseDouble(mostCurrent._lblview.getText()))-10*0.01;
 //BA.debugLineNum = 242;BA.debugLine="If lbl_text_double <= 0 Then";
if (_lbl_text_double<=0) { 
 //BA.debugLineNum = 243;BA.debugLine="lblView.Text = 0";
mostCurrent._lblview.setText((Object)(0));
 }else {
 //BA.debugLineNum = 245;BA.debugLine="lblView.Text = doubleToString(lbl_text_double)";
mostCurrent._lblview.setText((Object)(_doubletostring(_lbl_text_double)));
 };
 };
 //BA.debugLineNum = 248;BA.debugLine="mapUpdate";
_mapupdate();
 //BA.debugLineNum = 249;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 250;BA.debugLine="End Sub";
return "";
}
public static String  _btnexit_click() throws Exception{
 //BA.debugLineNum = 226;BA.debugLine="Sub btnExit_Click";
 //BA.debugLineNum = 228;BA.debugLine="Activity.Finish()";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 230;BA.debugLine="End Sub";
return "";
}
public static String  _btnsave_click() throws Exception{
anywheresoftware.b4a.objects.collections.Map _columns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 191;BA.debugLine="Sub btnSave_Click";
 //BA.debugLineNum = 206;BA.debugLine="Dim columns As Map";
_columns = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 207;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 209;BA.debugLine="columns.Initialize()";
_columns.Initialize();
 //BA.debugLineNum = 210;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 212;BA.debugLine="If itemMap.Size == itemMap_Begin.Size Then";
if (mostCurrent._itemmap.getSize()==mostCurrent._itemmap_begin.getSize()) { 
 //BA.debugLineNum = 213;BA.debugLine="For i = 0 To itemMap.Size - 1";
{
final int step140 = 1;
final int limit140 = (int) (mostCurrent._itemmap.getSize()-1);
for (_i = (int) (0); (step140 > 0 && _i <= limit140) || (step140 < 0 && _i >= limit140); _i = ((int)(0 + _i + step140))) {
 //BA.debugLineNum = 214;BA.debugLine="If itemMap.GetValueAt(i) <> itemMap_Begin.GetVa";
if ((mostCurrent._itemmap.GetValueAt(_i)).equals(mostCurrent._itemmap_begin.GetValueAt(_i)) == false) { 
 //BA.debugLineNum = 215;BA.debugLine="columns.Put(\"value\",itemMap.GetValueAt(i))";
_columns.Put((Object)("value"),mostCurrent._itemmap.GetValueAt(_i));
 //BA.debugLineNum = 216;BA.debugLine="params.Put(\"name\",itemMap.GetKeyAt(i))";
_params.Put((Object)("name"),mostCurrent._itemmap.GetKeyAt(_i));
 //BA.debugLineNum = 217;BA.debugLine="params.Put(\"params1\",\"[isperm]\")";
_params.Put((Object)("params1"),(Object)("[isperm]"));
 //BA.debugLineNum = 218;BA.debugLine="sql_capture.UpdateToTable(\"dict_properties\",co";
mostCurrent._sql_capture._updatetotable(mostCurrent.activityBA,"dict_properties",_columns,_params);
 };
 }
};
 };
 //BA.debugLineNum = 222;BA.debugLine="ToastMessageShow(\"保存成功...\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("保存成功...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 223;BA.debugLine="End Sub";
return "";
}
public static int  _charinstring(String _ch,String _str) throws Exception{
String _strs = "";
int _returnint = 0;
int _i = 0;
 //BA.debugLineNum = 169;BA.debugLine="Sub charInString(ch As String,str As String) As In";
 //BA.debugLineNum = 170;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 171;BA.debugLine="Dim returnInt As Int";
_returnint = 0;
 //BA.debugLineNum = 172;BA.debugLine="returnInt = -1";
_returnint = (int) (-1);
 //BA.debugLineNum = 173;BA.debugLine="If str <> Null Then";
if (_str!= null) { 
 //BA.debugLineNum = 174;BA.debugLine="If ch <> Null Then";
if (_ch!= null) { 
 //BA.debugLineNum = 175;BA.debugLine="strs = str.Trim()";
_strs = _str.trim();
 //BA.debugLineNum = 176;BA.debugLine="If strs.Length() > 0 Then";
if (_strs.length()>0) { 
 //BA.debugLineNum = 177;BA.debugLine="For i = 0 To strs.Length() - 1";
{
final int step122 = 1;
final int limit122 = (int) (_strs.length()-1);
for (_i = (int) (0); (step122 > 0 && _i <= limit122) || (step122 < 0 && _i >= limit122); _i = ((int)(0 + _i + step122))) {
 //BA.debugLineNum = 178;BA.debugLine="If strs.CharAt(i) == ch.CharAt(0) Then";
if (_strs.charAt(_i)==_ch.charAt((int) (0))) { 
 //BA.debugLineNum = 179;BA.debugLine="If i <> 0 Or i <> strs.Length() -1 Then";
if (_i!=0 || _i!=_strs.length()-1) { 
 //BA.debugLineNum = 180;BA.debugLine="returnInt = i";
_returnint = _i;
 };
 };
 }
};
 };
 };
 };
 //BA.debugLineNum = 187;BA.debugLine="Return returnInt";
if (true) return _returnint;
 //BA.debugLineNum = 188;BA.debugLine="End Sub";
return 0;
}
public static String  _doubletostring(double _double1) throws Exception{
String _strs = "";
 //BA.debugLineNum = 300;BA.debugLine="Sub doubleToString(double1 As Double) As String";
 //BA.debugLineNum = 301;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 302;BA.debugLine="strs = double1&\"\"";
_strs = BA.NumberToString(_double1)+"";
 //BA.debugLineNum = 303;BA.debugLine="If double1 > 1 Then";
if (_double1>1) { 
 //BA.debugLineNum = 304;BA.debugLine="strs = 1";
_strs = BA.NumberToString(1);
 }else {
 //BA.debugLineNum = 306;BA.debugLine="If strs.Length() > 5 Then";
if (_strs.length()>5) { 
 //BA.debugLineNum = 307;BA.debugLine="strs = strs.SubString2(0,5)";
_strs = _strs.substring((int) (0),(int) (5));
 };
 };
 //BA.debugLineNum = 310;BA.debugLine="Return strs";
if (true) return _strs;
 //BA.debugLineNum = 311;BA.debugLine="End Sub";
return "";
}
public static String  _forceimmersicemode() throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 41;BA.debugLine="Sub ForceImmersiceMode";
 //BA.debugLineNum = 42;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 43;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 44;BA.debugLine="r.Target = r.RunMethod(\"getWindow\")";
_r.Target = _r.RunMethod("getWindow");
 //BA.debugLineNum = 45;BA.debugLine="r.Target = r.RunMethod(\"getDecorView\")";
_r.Target = _r.RunMethod("getDecorView");
 //BA.debugLineNum = 46;BA.debugLine="r.RunMethod2(\"setSystemUiVisibility\", 5894, \"java";
_r.RunMethod2("setSystemUiVisibility",BA.NumberToString(5894),"java.lang.int");
 //BA.debugLineNum = 47;BA.debugLine="End Sub";
return "";
}
public static String  _fulltable() throws Exception{
String _strs = "";
int _i = 0;
 //BA.debugLineNum = 83;BA.debugLine="Sub fullTable";
 //BA.debugLineNum = 84;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 85;BA.debugLine="If ltvTable.Size > 0 Then";
if (mostCurrent._ltvtable.getSize()>0) { 
 //BA.debugLineNum = 86;BA.debugLine="For i = 0 To ltvTable.Size";
{
final int step52 = 1;
final int limit52 = mostCurrent._ltvtable.getSize();
for (_i = (int) (0); (step52 > 0 && _i <= limit52) || (step52 < 0 && _i >= limit52); _i = ((int)(0 + _i + step52))) {
 //BA.debugLineNum = 87;BA.debugLine="ltvTable.Clear()";
mostCurrent._ltvtable.Clear();
 }
};
 };
 //BA.debugLineNum = 91;BA.debugLine="For i = 0 To itemMap.Size - 1";
{
final int step56 = 1;
final int limit56 = (int) (mostCurrent._itemmap.getSize()-1);
for (_i = (int) (0); (step56 > 0 && _i <= limit56) || (step56 < 0 && _i >= limit56); _i = ((int)(0 + _i + step56))) {
 //BA.debugLineNum = 92;BA.debugLine="If i <> 0 Or itemMapKey <> Null Then";
if (_i!=0 || mostCurrent._itemmapkey!= null) { 
 }else {
 //BA.debugLineNum = 94;BA.debugLine="lblView.Text = itemMap.GetValueAt(i)";
mostCurrent._lblview.setText(mostCurrent._itemmap.GetValueAt(_i));
 //BA.debugLineNum = 95;BA.debugLine="itemMapKey = itemMap.GetKeyAt(i)";
mostCurrent._itemmapkey = BA.ObjectToString(mostCurrent._itemmap.GetKeyAt(_i));
 };
 //BA.debugLineNum = 97;BA.debugLine="strs = itemMap.GetKeyAt(i) &\":\" &itemMap.GetValu";
_strs = BA.ObjectToString(mostCurrent._itemmap.GetKeyAt(_i))+":"+BA.ObjectToString(mostCurrent._itemmap.GetValueAt(_i));
 //BA.debugLineNum = 98;BA.debugLine="ltvTable.AddSingleLine2(strs,strs)";
mostCurrent._ltvtable.AddSingleLine2(_strs,(Object)(_strs));
 }
};
 //BA.debugLineNum = 100;BA.debugLine="End	Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 18;BA.debugLine="Private btnAddOne As Button";
mostCurrent._btnaddone = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private btnAddTen As Button";
mostCurrent._btnaddten = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private btnDelOne As Button";
mostCurrent._btndelone = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private btnDelTen As Button";
mostCurrent._btndelten = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private btnExit As Button";
mostCurrent._btnexit = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private btnSave As Button";
mostCurrent._btnsave = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private lblView As Label";
mostCurrent._lblview = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim myFile As String = \"/home/SpermSettings.ini\"";
mostCurrent._myfile = "/home/SpermSettings.ini";
 //BA.debugLineNum = 28;BA.debugLine="Private ltvTable As ListView";
mostCurrent._ltvtable = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim itemMap As Map";
mostCurrent._itemmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 31;BA.debugLine="Dim itemMapKey As String";
mostCurrent._itemmapkey = "";
 //BA.debugLineNum = 33;BA.debugLine="Dim itemMap_Begin As Map";
mostCurrent._itemmap_begin = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 35;BA.debugLine="Dim lbl_text_int As Int";
_lbl_text_int = 0;
 //BA.debugLineNum = 36;BA.debugLine="Dim lbl_text_double As Double";
_lbl_text_double = 0;
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_text_ltvtable() throws Exception{
 //BA.debugLineNum = 319;BA.debugLine="Sub lbl_Text_ltvTable";
 //BA.debugLineNum = 320;BA.debugLine="If ltvTable.Size > 0 Then";
if (mostCurrent._ltvtable.getSize()>0) { 
 //BA.debugLineNum = 321;BA.debugLine="values_Text(ltvTable.GetItem(0))";
_values_text(BA.ObjectToString(mostCurrent._ltvtable.GetItem((int) (0))));
 };
 //BA.debugLineNum = 323;BA.debugLine="End Sub";
return "";
}
public static String  _ltvtable_itemclick(int _position,String _value) throws Exception{
 //BA.debugLineNum = 314;BA.debugLine="Sub ltvTable_ItemClick (Position As Int, value As";
 //BA.debugLineNum = 315;BA.debugLine="values_Text(value)";
_values_text(_value);
 //BA.debugLineNum = 316;BA.debugLine="End Sub";
return "";
}
public static String  _mapupdate() throws Exception{
int _i = 0;
 //BA.debugLineNum = 103;BA.debugLine="Sub mapUpdate";
 //BA.debugLineNum = 104;BA.debugLine="For i = 0 To itemMap.Size -1";
{
final int step67 = 1;
final int limit67 = (int) (mostCurrent._itemmap.getSize()-1);
for (_i = (int) (0); (step67 > 0 && _i <= limit67) || (step67 < 0 && _i >= limit67); _i = ((int)(0 + _i + step67))) {
 //BA.debugLineNum = 105;BA.debugLine="If itemMap.GetKeyAt(i) == itemMapKey Then";
if ((mostCurrent._itemmap.GetKeyAt(_i)).equals((Object)(mostCurrent._itemmapkey))) { 
 //BA.debugLineNum = 107;BA.debugLine="itemMap.Put(itemMapKey,lblView.Text)";
mostCurrent._itemmap.Put((Object)(mostCurrent._itemmapkey),(Object)(mostCurrent._lblview.getText()));
 };
 }
};
 //BA.debugLineNum = 110;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _read_table_dict() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursors = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 113;BA.debugLine="Sub read_Table_Dict";
 //BA.debugLineNum = 114;BA.debugLine="Dim cursors As Cursor";
_cursors = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 115;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 116;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 118;BA.debugLine="itemMap.Initialize()";
mostCurrent._itemmap.Initialize();
 //BA.debugLineNum = 119;BA.debugLine="itemMap_Begin.Initialize()";
mostCurrent._itemmap_begin.Initialize();
 //BA.debugLineNum = 120;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 121;BA.debugLine="cloumns = Array As Object(\"name\",\"value\")";
_cloumns = new Object[]{(Object)("name"),(Object)("value")};
 //BA.debugLineNum = 123;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 124;BA.debugLine="cursors = sql_capture.QueryColumnData(\"dict_prope";
_cursors = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"dict_properties",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 126;BA.debugLine="For i = 0 To cursors.RowCount - 1";
{
final int step83 = 1;
final int limit83 = (int) (_cursors.getRowCount()-1);
for (_i = (int) (0); (step83 > 0 && _i <= limit83) || (step83 < 0 && _i >= limit83); _i = ((int)(0 + _i + step83))) {
 //BA.debugLineNum = 127;BA.debugLine="cursors.Position = i";
_cursors.setPosition(_i);
 //BA.debugLineNum = 128;BA.debugLine="itemMap.put(cursors.GetString(cloumns(0)),cursor";
mostCurrent._itemmap.Put((Object)(_cursors.GetString(BA.ObjectToString(_cloumns[(int) (0)]))),(Object)(_cursors.GetString(BA.ObjectToString(_cloumns[(int) (1)]))));
 //BA.debugLineNum = 129;BA.debugLine="itemMap_Begin.put(cursors.GetString(cloumns(0)),";
mostCurrent._itemmap_begin.Put((Object)(_cursors.GetString(BA.ObjectToString(_cloumns[(int) (0)]))),(Object)(_cursors.GetString(BA.ObjectToString(_cloumns[(int) (1)]))));
 }
};
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
public static String  _readspermsettings() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _tr = null;
int _flagint = 0;
String _line = "";
 //BA.debugLineNum = 135;BA.debugLine="Sub readSpermSettings";
 //BA.debugLineNum = 136;BA.debugLine="Dim tr As TextReader";
_tr = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 138;BA.debugLine="Dim flagInt As Int";
_flagint = 0;
 //BA.debugLineNum = 139;BA.debugLine="Dim line As String";
_line = "";
 //BA.debugLineNum = 141;BA.debugLine="itemMap.Initialize()";
mostCurrent._itemmap.Initialize();
 //BA.debugLineNum = 142;BA.debugLine="flagInt = -1";
_flagint = (int) (-1);
 //BA.debugLineNum = 145;BA.debugLine="If File.Exists(File.DirRootExternal,myFile) Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._myfile)) { 
 }else {
 //BA.debugLineNum = 148;BA.debugLine="File.WriteString(File.DirRootExternal,myFile,\"\")";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._myfile,"");
 };
 //BA.debugLineNum = 150;BA.debugLine="tr.Initialize(File.OpenInput(File.DirRootExternal";
_tr.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._myfile).getObject()));
 //BA.debugLineNum = 152;BA.debugLine="Do While line <> Null";
while (_line!= null) {
 //BA.debugLineNum = 153;BA.debugLine="line = tr.ReadLine()";
_line = _tr.ReadLine();
 //BA.debugLineNum = 154;BA.debugLine="If line <> Null Then";
if (_line!= null) { 
 //BA.debugLineNum = 155;BA.debugLine="flagInt = charInString(\"=\",line)";
_flagint = _charinstring("=",_line);
 //BA.debugLineNum = 156;BA.debugLine="line = line.Trim()";
_line = _line.trim();
 //BA.debugLineNum = 157;BA.debugLine="If flagInt <> -1 Then";
if (_flagint!=-1) { 
 //BA.debugLineNum = 158;BA.debugLine="If itemMap.IsInitialized() Then";
if (mostCurrent._itemmap.IsInitialized()) { 
 //BA.debugLineNum = 159;BA.debugLine="itemMap.Put(line.SubString2(0,flagInt),line.S";
mostCurrent._itemmap.Put((Object)(_line.substring((int) (0),_flagint)),(Object)(_line.substring((int) (_flagint+1))));
 };
 };
 };
 }
;
 //BA.debugLineNum = 165;BA.debugLine="tr.Close()";
_tr.Close();
 //BA.debugLineNum = 166;BA.debugLine="End Sub";
return "";
}
public static String  _values_text(String _value) throws Exception{
int _flagint = 0;
 //BA.debugLineNum = 326;BA.debugLine="Sub values_Text(value As String)";
 //BA.debugLineNum = 327;BA.debugLine="Dim flagInt As Int";
_flagint = 0;
 //BA.debugLineNum = 328;BA.debugLine="If value <> Null And value <> \"\" Then";
if (_value!= null && (_value).equals("") == false) { 
 //BA.debugLineNum = 329;BA.debugLine="flagInt = charInString(\":\",value)";
_flagint = _charinstring(":",_value);
 //BA.debugLineNum = 330;BA.debugLine="value = value.Trim()";
_value = _value.trim();
 //BA.debugLineNum = 331;BA.debugLine="If flagInt <> -1 Then";
if (_flagint!=-1) { 
 //BA.debugLineNum = 332;BA.debugLine="lblView.Text = value.SubString(flagInt+1)";
mostCurrent._lblview.setText((Object)(_value.substring((int) (_flagint+1))));
 //BA.debugLineNum = 333;BA.debugLine="itemMapKey = value.SubString2(0,flagInt)";
mostCurrent._itemmapkey = _value.substring((int) (0),_flagint);
 };
 };
 //BA.debugLineNum = 336;BA.debugLine="End Sub";
return "";
}
}
