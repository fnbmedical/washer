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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "ini.guib4a.example", "ini.guib4a.example.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "ini.guib4a.example", "ini.guib4a.example.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "ini.guib4a.example.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (main) Resume **");
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
public static anywheresoftware.b4a.objects.Timer _tasktime = null;
public static anywheresoftware.b4a.objects.Timer _tasktime2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnanay = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncapture = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnexit = null;
public anywheresoftware.b4a.objects.ListViewWrapper _ltvcapture = null;
public anywheresoftware.b4a.objects.PanelWrapper _palshowradio = null;
public android_serialport_api.SerialPort _usbserial1 = null;
public anywheresoftware.b4a.randomaccessfile.AsyncStreams _async = null;
public xvs.ACL.ACL _camera1 = null;
public static boolean _flag_picture = false;
public static boolean _flag_task = false;
public static String _strpath = "";
public static int _photo_num = 0;
public static int _photo_count = 0;
public static String _ana_time = "";
public static int _ana_point_num = 0;
public static int _ana_point_num_count = 0;
public static int _pigid = 0;
public static int _serial_return_data = 0;
public static String _serial_port = "";
public static String _serial_rate = "";
public static int _serial_num = 0;
public ini.guib4a.example.sql_capture _sql_capture = null;
public ini.guib4a.example.create_file _create_file = null;
public ini.guib4a.example.testexp _testexp = null;
public ini.guib4a.example.iniexp _iniexp = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (testexp.mostCurrent != null);
vis = vis | (iniexp.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
Object _e = null;
 //BA.debugLineNum = 82;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 85;BA.debugLine="Activity.LoadLayout(\"mainWindow\")";
mostCurrent._activity.LoadLayout("mainWindow",mostCurrent.activityBA);
 //BA.debugLineNum = 86;BA.debugLine="Dim jo As JavaObject = Activity";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo.setObject((java.lang.Object)(mostCurrent._activity.getObject()));
 //BA.debugLineNum = 87;BA.debugLine="Dim e As Object = jo.CreateEvent(\"android.view";
_e = _jo.CreateEvent(processBA,"android.view.View.OnSystemUiVisibilityChangeListener","VisibilityChanged",anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 88;BA.debugLine="jo.RunMethod(\"setOnSystemUiVisibilityChangeLis";
_jo.RunMethod("setOnSystemUiVisibilityChangeListener",new Object[]{_e});
 //BA.debugLineNum = 90;BA.debugLine="Activity.Width = -1";
mostCurrent._activity.setWidth((int) (-1));
 //BA.debugLineNum = 91;BA.debugLine="Activity.Height = -1";
mostCurrent._activity.setHeight((int) (-1));
 //BA.debugLineNum = 92;BA.debugLine="Activity.Title = \"采集\"";
mostCurrent._activity.setTitle((Object)("采集"));
 //BA.debugLineNum = 94;BA.debugLine="CreateTable";
_createtable();
 //BA.debugLineNum = 96;BA.debugLine="Try";
try { //BA.debugLineNum = 97;BA.debugLine="usbSerial1.SetPort(SERIAL_PORT,SERIAL_RATE,SERIA";
mostCurrent._usbserial1.SetPort(mostCurrent._serial_port,(int)(Double.parseDouble(mostCurrent._serial_rate)),_serial_num);
 } 
       catch (Exception e51) {
			processBA.setLastException(e51); //BA.debugLineNum = 99;BA.debugLine="ToastMessageShow(LastException.Message,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 101;BA.debugLine="aSync.Initialize(usbSerial1.InputStream,usbSerial";
mostCurrent._async.Initialize(processBA,mostCurrent._usbserial1.getInputStream(),mostCurrent._usbserial1.getOutputStream(),"Astreams");
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 175;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 176;BA.debugLine="camera1.StopPreview()";
mostCurrent._camera1.StopPreview();
 //BA.debugLineNum = 177;BA.debugLine="camera1.Release()";
mostCurrent._camera1.Release();
 //BA.debugLineNum = 178;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 179;BA.debugLine="aSync.Close()";
mostCurrent._async.Close();
 //BA.debugLineNum = 180;BA.debugLine="usbSerial1.close()";
mostCurrent._usbserial1.close();
 };
 //BA.debugLineNum = 182;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 104;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 105;BA.debugLine="CreateTable";
_createtable();
 //BA.debugLineNum = 106;BA.debugLine="camera1.Initialize(palShowRadio,\"Camera1\")";
mostCurrent._camera1.Initialize(mostCurrent.activityBA,(android.view.ViewGroup)(mostCurrent._palshowradio.getObject()),"Camera1");
 //BA.debugLineNum = 108;BA.debugLine="Try";
try { //BA.debugLineNum = 109;BA.debugLine="usbSerial1.SetPort(SERIAL_PORT,SERIAL_RATE,SERIA";
mostCurrent._usbserial1.SetPort(mostCurrent._serial_port,(int)(Double.parseDouble(mostCurrent._serial_rate)),_serial_num);
 } 
       catch (Exception e61) {
			processBA.setLastException(e61); //BA.debugLineNum = 111;BA.debugLine="ToastMessageShow(LastException.Message,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 113;BA.debugLine="aSync.Initialize(usbSerial1.InputStream,usbSerial";
mostCurrent._async.Initialize(processBA,mostCurrent._usbserial1.getInputStream(),mostCurrent._usbserial1.getOutputStream(),"Astreams");
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return "";
}
public static String  _activity_windowfocuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 76;BA.debugLine="Sub Activity_WindowFocusChanged(HasFocus As Boolea";
 //BA.debugLineNum = 77;BA.debugLine="If HasFocus Then";
if (_hasfocus) { 
 //BA.debugLineNum = 78;BA.debugLine="ForceImmersiceMode";
_forceimmersicemode();
 };
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return "";
}
public static String  _astreams_error() throws Exception{
 //BA.debugLineNum = 411;BA.debugLine="Sub Astreams_Error()";
 //BA.debugLineNum = 412;BA.debugLine="ToastMessageShow(LastException.Message,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 413;BA.debugLine="End Sub";
return "";
}
public static String  _astreams_newdata(byte[] _buffer) throws Exception{
String _cmd_recevie = "";
anywheresoftware.b4a.agraham.byteconverter.ByteConverter _conv = null;
 //BA.debugLineNum = 388;BA.debugLine="Sub Astreams_NewData(Buffer() As Byte)";
 //BA.debugLineNum = 389;BA.debugLine="Dim cmd_Recevie As String";
_cmd_recevie = "";
 //BA.debugLineNum = 390;BA.debugLine="Dim conv As ByteConverter";
_conv = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 393;BA.debugLine="cmd_Recevie = conv.HexFromBytes(Buffer).ToLowerCa";
_cmd_recevie = _conv.HexFromBytes(_buffer).toLowerCase();
 //BA.debugLineNum = 397;BA.debugLine="If cmd_Recevie.Length() < 22 Then";
if (_cmd_recevie.length()<22) { 
 //BA.debugLineNum = 398;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 400;BA.debugLine="recevie_Cmd(cmd_Recevie)";
_recevie_cmd(_cmd_recevie);
 //BA.debugLineNum = 402;BA.debugLine="If serial_Return_Data <> 0 Then";
if (_serial_return_data!=0) { 
 //BA.debugLineNum = 403;BA.debugLine="excu_Cmd_Data";
_excu_cmd_data();
 };
 //BA.debugLineNum = 408;BA.debugLine="End Sub";
return "";
}
public static String  _btnanay_click() throws Exception{
 //BA.debugLineNum = 294;BA.debugLine="Sub btnAnay_Click";
 //BA.debugLineNum = 295;BA.debugLine="camera1.StopPreview()";
mostCurrent._camera1.StopPreview();
 //BA.debugLineNum = 296;BA.debugLine="camera1.Release()";
mostCurrent._camera1.Release();
 //BA.debugLineNum = 298;BA.debugLine="StartActivity(TestExp)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._testexp.getObject()));
 //BA.debugLineNum = 300;BA.debugLine="End Sub";
return "";
}
public static String  _btncapture_click() throws Exception{
 //BA.debugLineNum = 249;BA.debugLine="Sub btnCapture_Click";
 //BA.debugLineNum = 252;BA.debugLine="send_Cmd(\"AA5508001001434f4e544d\")";
_send_cmd("AA5508001001434f4e544d");
 //BA.debugLineNum = 258;BA.debugLine="ana_Point_Num_Count = 0";
_ana_point_num_count = (int) (0);
 //BA.debugLineNum = 260;BA.debugLine="taskTime.Initialize(\"taskTime\",250)";
_tasktime.Initialize(processBA,"taskTime",(long) (250));
 //BA.debugLineNum = 261;BA.debugLine="taskTime2.Initialize(\"taskTime2\",250)";
_tasktime2.Initialize(processBA,"taskTime2",(long) (250));
 //BA.debugLineNum = 262;BA.debugLine="taskTime2.Enabled = True";
_tasktime2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 264;BA.debugLine="btnEnabled(False)";
_btnenabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 266;BA.debugLine="excu_Cmd_Data";
_excu_cmd_data();
 //BA.debugLineNum = 267;BA.debugLine="End Sub";
return "";
}
public static String  _btnenabled(boolean _flag_enabled) throws Exception{
 //BA.debugLineNum = 191;BA.debugLine="Sub btnEnabled(flag_Enabled As Boolean)";
 //BA.debugLineNum = 192;BA.debugLine="btnAnay.Enabled = flag_Enabled";
mostCurrent._btnanay.setEnabled(_flag_enabled);
 //BA.debugLineNum = 193;BA.debugLine="btnCapture.Enabled = flag_Enabled";
mostCurrent._btncapture.setEnabled(_flag_enabled);
 //BA.debugLineNum = 194;BA.debugLine="btnExit.Enabled = flag_Enabled";
mostCurrent._btnexit.setEnabled(_flag_enabled);
 //BA.debugLineNum = 195;BA.debugLine="End Sub";
return "";
}
public static String  _btnexit_click() throws Exception{
 //BA.debugLineNum = 185;BA.debugLine="Sub btnExit_Click";
 //BA.debugLineNum = 186;BA.debugLine="camera1.StopPreview()";
mostCurrent._camera1.StopPreview();
 //BA.debugLineNum = 187;BA.debugLine="camera1.Release()";
mostCurrent._camera1.Release();
 //BA.debugLineNum = 188;BA.debugLine="Activity.Finish()";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 189;BA.debugLine="End Sub";
return "";
}
public static String  _camera1_picturetaken(byte[] _data) throws Exception{
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
String _str_num = "";
 //BA.debugLineNum = 144;BA.debugLine="Sub Camera1_PictureTaken(Data() As Byte)";
 //BA.debugLineNum = 145;BA.debugLine="Dim out As OutputStream";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 146;BA.debugLine="Dim str_num As String";
_str_num = "";
 //BA.debugLineNum = 148;BA.debugLine="taskTime.Enabled = False";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 149;BA.debugLine="camera1.PictureSize(640,480)";
mostCurrent._camera1.PictureSize((int) (640),(int) (480));
 //BA.debugLineNum = 150;BA.debugLine="camera1.StartPreview()";
mostCurrent._camera1.StartPreview();
 //BA.debugLineNum = 152;BA.debugLine="If photo_num < 10 Then";
if (_photo_num<10) { 
 //BA.debugLineNum = 153;BA.debugLine="str_num = \"00\"&photo_num";
_str_num = "00"+BA.NumberToString(_photo_num);
 }else {
 //BA.debugLineNum = 155;BA.debugLine="str_num = \"0\"&photo_num";
_str_num = "0"+BA.NumberToString(_photo_num);
 };
 //BA.debugLineNum = 158;BA.debugLine="out = File.OpenOutput(File.DirRootExternal&strPat";
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._strpath,_str_num+".jpg",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 159;BA.debugLine="out.WriteBytes(Data, 0, Data.Length)";
_out.WriteBytes(_data,(int) (0),_data.length);
 //BA.debugLineNum = 160;BA.debugLine="out.Close";
_out.Close();
 //BA.debugLineNum = 162;BA.debugLine="If photo_num >= photo_Count Then";
if (_photo_num>=_photo_count) { 
 //BA.debugLineNum = 163;BA.debugLine="flag_Picture = False";
_flag_picture = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 164;BA.debugLine="taskTime.Enabled = False";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 165;BA.debugLine="flag_Task = True";
_flag_task = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 166;BA.debugLine="ana_Point_Num_Count = ana_Point_Num_Count + 1";
_ana_point_num_count = (int) (_ana_point_num_count+1);
 }else {
 //BA.debugLineNum = 168;BA.debugLine="flag_Picture = True";
_flag_picture = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 169;BA.debugLine="taskTime.Enabled = True";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 172;BA.debugLine="photo_num = photo_num + 1";
_photo_num = (int) (_photo_num+1);
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _camera1_ready(boolean _success) throws Exception{
 //BA.debugLineNum = 118;BA.debugLine="Sub Camera1_Ready(Success As Boolean)";
 //BA.debugLineNum = 120;BA.debugLine="If Activity.Width > Activity.Height Then";
if (mostCurrent._activity.getWidth()>mostCurrent._activity.getHeight()) { 
 //BA.debugLineNum = 121;BA.debugLine="Try";
try { //BA.debugLineNum = 122;BA.debugLine="camera1.OriLandscape()";
mostCurrent._camera1.OriLandscape();
 } 
       catch (Exception e70) {
			processBA.setLastException(e70); //BA.debugLineNum = 124;BA.debugLine="Msgbox(LastException.Message,\"设置错误...\")";
anywheresoftware.b4a.keywords.Common.Msgbox(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),"设置错误...",mostCurrent.activityBA);
 };
 }else {
 //BA.debugLineNum = 128;BA.debugLine="Try";
try { //BA.debugLineNum = 129;BA.debugLine="camera1.OriPortrait()";
mostCurrent._camera1.OriPortrait();
 } 
       catch (Exception e76) {
			processBA.setLastException(e76); //BA.debugLineNum = 131;BA.debugLine="Msgbox(LastException,\"设置错误...\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),"设置错误...",mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 135;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 136;BA.debugLine="camera1.PictureSize(640,480)";
mostCurrent._camera1.PictureSize((int) (640),(int) (480));
 //BA.debugLineNum = 137;BA.debugLine="camera1.StartPreview()";
mostCurrent._camera1.StartPreview();
 }else {
 //BA.debugLineNum = 139;BA.debugLine="ToastMessageShow(\"无法开启相机....\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("无法开启相机....",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return "";
}
public static String  _createtable() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursors = null;
String _strs = "";
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 303;BA.debugLine="Sub CreateTable";
 //BA.debugLineNum = 304;BA.debugLine="Dim cursors As Cursor";
_cursors = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 305;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 306;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 307;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 309;BA.debugLine="ltvCapture.Clear()";
mostCurrent._ltvcapture.Clear();
 //BA.debugLineNum = 310;BA.debugLine="cloumns = Array As Object(\"pigid\",\"ana_time\")";
_cloumns = new Object[]{(Object)("pigid"),(Object)("ana_time")};
 //BA.debugLineNum = 311;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 312;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 314;BA.debugLine="cursors = sql_capture.QueryColumnData(\"capture\",c";
_cursors = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"capture",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 316;BA.debugLine="For i = 0 To cursors.RowCount - 1";
{
final int step201 = 1;
final int limit201 = (int) (_cursors.getRowCount()-1);
for (_i = (int) (0); (step201 > 0 && _i <= limit201) || (step201 < 0 && _i >= limit201); _i = ((int)(0 + _i + step201))) {
 //BA.debugLineNum = 317;BA.debugLine="cursors.Position = i";
_cursors.setPosition(_i);
 //BA.debugLineNum = 318;BA.debugLine="strs = cursors.GetInt(cloumns(0))&\"   \"&cursors.";
_strs = BA.NumberToString(_cursors.GetInt(BA.ObjectToString(_cloumns[(int) (0)])))+"   "+_cursors.GetString(BA.ObjectToString(_cloumns[(int) (1)]));
 //BA.debugLineNum = 319;BA.debugLine="ltvCapture.AddSingleLine2(strs,strs)";
mostCurrent._ltvcapture.AddSingleLine2(_strs,(Object)(_strs));
 }
};
 //BA.debugLineNum = 321;BA.debugLine="End Sub";
return "";
}
public static String  _excu_cmd_data() throws Exception{
 //BA.debugLineNum = 416;BA.debugLine="Sub excu_Cmd_Data";
 //BA.debugLineNum = 418;BA.debugLine="serial_Return_Data = 0";
_serial_return_data = (int) (0);
 //BA.debugLineNum = 419;BA.debugLine="End Sub";
return "";
}
public static String  _forceimmersicemode() throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 67;BA.debugLine="Sub ForceImmersiceMode";
 //BA.debugLineNum = 68;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 69;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 70;BA.debugLine="r.Target = r.RunMethod(\"getWindow\")";
_r.Target = _r.RunMethod("getWindow");
 //BA.debugLineNum = 71;BA.debugLine="r.Target = r.RunMethod(\"getDecorView\")";
_r.Target = _r.RunMethod("getDecorView");
 //BA.debugLineNum = 72;BA.debugLine="r.RunMethod2(\"setSystemUiVisibility\", 5894, \"java";
_r.RunMethod2("setSystemUiVisibility",BA.NumberToString(5894),"java.lang.int");
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 28;BA.debugLine="Private btnAnay As Button";
mostCurrent._btnanay = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private btnCapture As Button";
mostCurrent._btncapture = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private btnExit As Button";
mostCurrent._btnexit = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private ltvCapture As ListView";
mostCurrent._ltvcapture = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private palShowRadio As Panel";
mostCurrent._palshowradio = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim usbSerial1 As SerialPort";
mostCurrent._usbserial1 = new android_serialport_api.SerialPort();
 //BA.debugLineNum = 35;BA.debugLine="Dim aSync As AsyncStreams";
mostCurrent._async = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 38;BA.debugLine="Dim camera1 As AdvancedCamera";
mostCurrent._camera1 = new xvs.ACL.ACL();
 //BA.debugLineNum = 40;BA.debugLine="Dim flag_Picture As Boolean";
_flag_picture = false;
 //BA.debugLineNum = 41;BA.debugLine="Dim flag_Task As Boolean = True";
_flag_task = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 44;BA.debugLine="Dim strPath As String";
mostCurrent._strpath = "";
 //BA.debugLineNum = 46;BA.debugLine="Dim photo_num As Int";
_photo_num = 0;
 //BA.debugLineNum = 48;BA.debugLine="Dim photo_Count As Int = 20";
_photo_count = (int) (20);
 //BA.debugLineNum = 50;BA.debugLine="Dim ana_time As String";
mostCurrent._ana_time = "";
 //BA.debugLineNum = 52;BA.debugLine="Dim ana_Point_Num As Int = 4";
_ana_point_num = (int) (4);
 //BA.debugLineNum = 54;BA.debugLine="Dim ana_Point_Num_Count As Int";
_ana_point_num_count = 0;
 //BA.debugLineNum = 56;BA.debugLine="Dim pigid As Int = 10000";
_pigid = (int) (10000);
 //BA.debugLineNum = 59;BA.debugLine="Dim serial_Return_Data As Int = 0";
_serial_return_data = (int) (0);
 //BA.debugLineNum = 61;BA.debugLine="Dim SERIAL_PORT As String = \"/dev/ttyAMA2\"";
mostCurrent._serial_port = "/dev/ttyAMA2";
 //BA.debugLineNum = 62;BA.debugLine="Dim SERIAL_RATE As String = \"9600\"";
mostCurrent._serial_rate = "9600";
 //BA.debugLineNum = 63;BA.debugLine="Dim SERIAL_NUM As Int = 2";
_serial_num = (int) (2);
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public static String  _photo_point() throws Exception{
 //BA.debugLineNum = 219;BA.debugLine="Sub photo_Point";
 //BA.debugLineNum = 221;BA.debugLine="If ana_Point_Num_Count == 0 Then";
if (_ana_point_num_count==0) { 
 //BA.debugLineNum = 222;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 223;BA.debugLine="pigid = sql_capture.QueryIDMax(\"capture\",\"pigid\"";
_pigid = (int)(Double.parseDouble(mostCurrent._sql_capture._queryidmax(mostCurrent.activityBA,"capture","pigid")));
 //BA.debugLineNum = 225;BA.debugLine="photo_Save(pigid,ana_Point_Num_Count + 1)";
_photo_save(_pigid,(int) (_ana_point_num_count+1));
 };
 //BA.debugLineNum = 228;BA.debugLine="If ana_Point_Num_Count == 1 Then";
if (_ana_point_num_count==1) { 
 //BA.debugLineNum = 231;BA.debugLine="photo_Save(pigid,ana_Point_Num_Count + 1)";
_photo_save(_pigid,(int) (_ana_point_num_count+1));
 };
 //BA.debugLineNum = 234;BA.debugLine="If ana_Point_Num_Count == 2 Then";
if (_ana_point_num_count==2) { 
 //BA.debugLineNum = 237;BA.debugLine="photo_Save(pigid,ana_Point_Num_Count + 1)";
_photo_save(_pigid,(int) (_ana_point_num_count+1));
 };
 //BA.debugLineNum = 240;BA.debugLine="If ana_Point_Num_Count == 3 Then";
if (_ana_point_num_count==3) { 
 //BA.debugLineNum = 243;BA.debugLine="photo_Save(pigid,ana_Point_Num_Count + 1)";
_photo_save(_pigid,(int) (_ana_point_num_count+1));
 };
 //BA.debugLineNum = 245;BA.debugLine="End Sub";
return "";
}
public static String  _photo_save(int _pigid_save,int _i_params) throws Exception{
int _id = 0;
long _timenow = 0L;
Object[] _params = null;
 //BA.debugLineNum = 270;BA.debugLine="Sub photo_Save(pigid_Save As Int,i_params As Int)";
 //BA.debugLineNum = 271;BA.debugLine="Dim id As Int";
_id = 0;
 //BA.debugLineNum = 273;BA.debugLine="Dim timeNow As Long";
_timenow = 0L;
 //BA.debugLineNum = 274;BA.debugLine="Dim params() As Object";
_params = new Object[(int) (0)];
{
int d0 = _params.length;
for (int i0 = 0;i0 < d0;i0++) {
_params[i0] = new Object();
}
}
;
 //BA.debugLineNum = 276;BA.debugLine="photo_num = 0";
_photo_num = (int) (0);
 //BA.debugLineNum = 277;BA.debugLine="flag_Picture = True";
_flag_picture = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 279;BA.debugLine="timeNow = DateTime.Now";
_timenow = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 280;BA.debugLine="strPath = create_File.Create_Dir(timeNow)";
mostCurrent._strpath = mostCurrent._create_file._create_dir(mostCurrent.activityBA,_timenow);
 //BA.debugLineNum = 281;BA.debugLine="ana_time = create_File.TimeFormat(timeNow)";
mostCurrent._ana_time = mostCurrent._create_file._timeformat(mostCurrent.activityBA,_timenow);
 //BA.debugLineNum = 282;BA.debugLine="taskTime.Enabled = True";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 285;BA.debugLine="id = sql_capture.QueryIDMax(\"capture\",\"id\")";
_id = (int)(Double.parseDouble(mostCurrent._sql_capture._queryidmax(mostCurrent.activityBA,"capture","id")));
 //BA.debugLineNum = 287;BA.debugLine="params = Array As Object(id+1,pigid_Save+1,ana_ti";
_params = new Object[]{(Object)(_id+1),(Object)(_pigid_save+1),(Object)(mostCurrent._ana_time),(Object)(_i_params),(Object)(mostCurrent._strpath),(Object)(0),(Object)(0),(Object)(0),(Object)(1)};
 //BA.debugLineNum = 288;BA.debugLine="sql_capture.InsertToCapture(\"capture\",params)";
mostCurrent._sql_capture._inserttocapture(mostCurrent.activityBA,"capture",_params);
 //BA.debugLineNum = 290;BA.debugLine="CreateTable";
_createtable();
 //BA.debugLineNum = 291;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
sql_capture._process_globals();
create_file._process_globals();
testexp._process_globals();
iniexp._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 20;BA.debugLine="Dim taskTime As Timer";
_tasktime = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 21;BA.debugLine="Dim taskTime2 As Timer";
_tasktime2 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _recevie_cmd(String _cmd_recevie) throws Exception{
 //BA.debugLineNum = 341;BA.debugLine="Sub recevie_Cmd(cmd_Recevie As String)";
 //BA.debugLineNum = 342;BA.debugLine="Select cmd_Recevie.SubString2(10,16)";
switch (BA.switchObjectToInt(_cmd_recevie.substring((int) (10),(int) (16)),"03434f","030102","030201","030202","030203","030204","030205","030301","030302","040302","040303","040401","040402","030502","040501")) {
case 0:
 //BA.debugLineNum = 345;BA.debugLine="serial_Return_Data = 1";
_serial_return_data = (int) (1);
 break;
case 1:
 //BA.debugLineNum = 348;BA.debugLine="serial_Return_Data = 2";
_serial_return_data = (int) (2);
 break;
case 2:
case 3:
case 4:
case 5:
case 6:
 //BA.debugLineNum = 351;BA.debugLine="serial_Return_Data = 3";
_serial_return_data = (int) (3);
 break;
case 7:
 //BA.debugLineNum = 355;BA.debugLine="If cmd_Recevie.SubString2(18,20) == \"00\" Then";
if ((_cmd_recevie.substring((int) (18),(int) (20))).equals("00")) { 
 //BA.debugLineNum = 356;BA.debugLine="serial_Return_Data = 4";
_serial_return_data = (int) (4);
 };
 //BA.debugLineNum = 359;BA.debugLine="serial_Return_Data = 5";
_serial_return_data = (int) (5);
 break;
case 8:
 //BA.debugLineNum = 362;BA.debugLine="serial_Return_Data = 6";
_serial_return_data = (int) (6);
 break;
case 9:
 //BA.debugLineNum = 365;BA.debugLine="serial_Return_Data = 7";
_serial_return_data = (int) (7);
 break;
case 10:
 //BA.debugLineNum = 368;BA.debugLine="serial_Return_Data = 8";
_serial_return_data = (int) (8);
 break;
case 11:
 //BA.debugLineNum = 371;BA.debugLine="serial_Return_Data = 9";
_serial_return_data = (int) (9);
 break;
case 12:
 //BA.debugLineNum = 374;BA.debugLine="serial_Return_Data = 10";
_serial_return_data = (int) (10);
 break;
case 13:
 //BA.debugLineNum = 377;BA.debugLine="serial_Return_Data = 11";
_serial_return_data = (int) (11);
 break;
case 14:
 //BA.debugLineNum = 380;BA.debugLine="serial_Return_Data = 12";
_serial_return_data = (int) (12);
 break;
default:
 //BA.debugLineNum = 383;BA.debugLine="serial_Return_Data = 13";
_serial_return_data = (int) (13);
 break;
}
;
 //BA.debugLineNum = 385;BA.debugLine="End Sub";
return "";
}
public static String  _send_cmd(String _cmd) throws Exception{
byte[] _bytearray = null;
anywheresoftware.b4a.agraham.byteconverter.ByteConverter _astr = null;
 //BA.debugLineNum = 324;BA.debugLine="Sub send_Cmd(cmd As String)";
 //BA.debugLineNum = 325;BA.debugLine="Dim byteArray(0) As Byte";
_bytearray = new byte[(int) (0)];
;
 //BA.debugLineNum = 326;BA.debugLine="Dim aStr As ByteConverter";
_astr = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 329;BA.debugLine="If cmd.Length < 22 Then";
if (_cmd.length()<22) { 
 //BA.debugLineNum = 330;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 333;BA.debugLine="byteArray = aStr.HexToBytes(cmd)";
_bytearray = _astr.HexToBytes(_cmd);
 //BA.debugLineNum = 334;BA.debugLine="aSync.Write(byteArray)";
mostCurrent._async.Write(_bytearray);
 //BA.debugLineNum = 338;BA.debugLine="End Sub";
return "";
}
public static String  _tasktime_tick() throws Exception{
 //BA.debugLineNum = 197;BA.debugLine="Sub taskTime_Tick";
 //BA.debugLineNum = 198;BA.debugLine="If flag_Picture Then";
if (_flag_picture) { 
 //BA.debugLineNum = 199;BA.debugLine="flag_Picture = False";
_flag_picture = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 200;BA.debugLine="camera1.TakePicture()";
mostCurrent._camera1.TakePicture();
 };
 //BA.debugLineNum = 202;BA.debugLine="End Sub";
return "";
}
public static String  _tasktime2_tick() throws Exception{
 //BA.debugLineNum = 204;BA.debugLine="Sub taskTime2_Tick";
 //BA.debugLineNum = 206;BA.debugLine="If ana_Point_Num_Count == ana_Point_Num Then";
if (_ana_point_num_count==_ana_point_num) { 
 //BA.debugLineNum = 207;BA.debugLine="taskTime2.Enabled = False";
_tasktime2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 208;BA.debugLine="btnEnabled(True)";
_btnenabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 209;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 212;BA.debugLine="If flag_Task Then";
if (_flag_task) { 
 //BA.debugLineNum = 213;BA.debugLine="flag_Task = False";
_flag_task = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 214;BA.debugLine="photo_Point";
_photo_point();
 };
 //BA.debugLineNum = 216;BA.debugLine="End Sub";
return "";
}
}
