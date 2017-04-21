package b4a.example;


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
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.main");
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
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public static int _type_isperm_capture = 0;
public static int _type_isperm_test = 0;
public static int _type_isperm_ini = 0;
public static int _fill_parent = 0;
public static int _wrap_content = 0;
public static int _currentpage = 0;
public static anywheresoftware.b4a.objects.Timer _tasktime = null;
public static anywheresoftware.b4a.objects.Timer _tasktime2 = null;
public de.amberhome.viewpager.AHPageContainer _container = null;
public de.amberhome.viewpager.AHViewPager _pager = null;
public de.amberhome.viewpager.AHViewPagerTabs _tabs = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncapture = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnexit_main = null;
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
public static int _pigid_main = 0;
public static int _serial_return_data = 0;
public static String _serial_port = "";
public static String _serial_rate = "";
public static int _serial_num = 0;
public anywheresoftware.b4a.objects.ButtonWrapper _btnanay = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndrawcontours = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnexit_test = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btngaussianblur = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnstretch = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnsubdraw = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _igshow = null;
public anywheresoftware.b4a.objects.ListViewWrapper _ltv_anay_point = null;
public anywheresoftware.b4a.objects.ListViewWrapper _ltv_anay_time = null;
public anywheresoftware.b4a.objects.ListViewWrapper _ltv_pigid = null;
public anywheresoftware.b4a.objects.ListViewWrapper _ltvtable_test = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_count = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_rate = null;
public com.example.ispermcpp.IspermCpp _isperm = null;
public static String _pigid = "";
public static String _ana_capture_point = "";
public static String _anay_time = "";
public static String _ana_capture_path = "";
public static String _ana_history_path = "";
public anywheresoftware.b4a.objects.ButtonWrapper _btnaddone = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnaddten = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndelone = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndelten = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnexit_ini = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnsave = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblview = null;
public anywheresoftware.b4a.objects.ListViewWrapper _ltvtable_ini = null;
public anywheresoftware.b4a.objects.collections.Map _itemmap = null;
public static String _itemmapkey = "";
public anywheresoftware.b4a.objects.collections.Map _itemmap_begin = null;
public static int _lbl_text_int = 0;
public static double _lbl_text_double = 0;
public anywheresoftware.b4a.objects.ButtonWrapper _button_rgba = null;
public b4a.example.create_file _create_file = null;
public b4a.example.sql_capture _sql_capture = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static class _panelinfo{
public boolean IsInitialized;
public int PanelType;
public boolean LayoutLoaded;
public void Initialize() {
IsInitialized = true;
PanelType = 0;
LayoutLoaded = false;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _activity_create(boolean _firsttime) throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.PanelWrapper _pan = null;
 //BA.debugLineNum = 157;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 160;BA.debugLine="Activity.Height = -1";
mostCurrent._activity.setHeight((int) (-1));
 //BA.debugLineNum = 161;BA.debugLine="Activity.Width = -1";
mostCurrent._activity.setWidth((int) (-1));
 //BA.debugLineNum = 162;BA.debugLine="ltvCapture.Initialize(\"ltvCapture\")";
mostCurrent._ltvcapture.Initialize(mostCurrent.activityBA,"ltvCapture");
 //BA.debugLineNum = 163;BA.debugLine="palShowRadio.Initialize(\"\")";
mostCurrent._palshowradio.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 165;BA.debugLine="igShow.Initialize(\"igShow\")";
mostCurrent._igshow.Initialize(mostCurrent.activityBA,"igShow");
 //BA.debugLineNum = 166;BA.debugLine="ltv_pigid.Initialize(\"ltv_pigid\")";
mostCurrent._ltv_pigid.Initialize(mostCurrent.activityBA,"ltv_pigid");
 //BA.debugLineNum = 167;BA.debugLine="ltv_anay_point.Initialize(\"ltv_anay_point\")";
mostCurrent._ltv_anay_point.Initialize(mostCurrent.activityBA,"ltv_anay_point");
 //BA.debugLineNum = 168;BA.debugLine="ltv_anay_time.Initialize(\"ltv_anay_time\")";
mostCurrent._ltv_anay_time.Initialize(mostCurrent.activityBA,"ltv_anay_time");
 //BA.debugLineNum = 169;BA.debugLine="ltvTable_Test.Initialize(\"ltvTable_Test\")";
mostCurrent._ltvtable_test.Initialize(mostCurrent.activityBA,"ltvTable_Test");
 //BA.debugLineNum = 170;BA.debugLine="lbl_Count.Initialize(\"lbl_Count\")";
mostCurrent._lbl_count.Initialize(mostCurrent.activityBA,"lbl_Count");
 //BA.debugLineNum = 171;BA.debugLine="lbl_Rate.Initialize(\"lbl_Rate\")";
mostCurrent._lbl_rate.Initialize(mostCurrent.activityBA,"lbl_Rate");
 //BA.debugLineNum = 173;BA.debugLine="ltvTable_Ini.Initialize(\"ltvTable_Ini\")";
mostCurrent._ltvtable_ini.Initialize(mostCurrent.activityBA,"ltvTable_Ini");
 //BA.debugLineNum = 174;BA.debugLine="lblView.Initialize(\"\")";
mostCurrent._lblview.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 176;BA.debugLine="container.Initialize()";
mostCurrent._container.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 177;BA.debugLine="For i = 0 To 2";
{
final int step102 = 1;
final int limit102 = (int) (2);
for (_i = (int) (0); (step102 > 0 && _i <= limit102) || (step102 < 0 && _i >= limit102); _i = ((int)(0 + _i + step102))) {
 //BA.debugLineNum = 178;BA.debugLine="Dim pan As Panel";
_pan = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 179;BA.debugLine="pan.Initialize(\"\")";
_pan.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 180;BA.debugLine="Select i";
switch (_i) {
case 0:
 //BA.debugLineNum = 182;BA.debugLine="pan = CreatePanel(TYPE_ISPERM_CAPTURE,\"mainwin";
_pan = _createpanel(_type_isperm_capture,"mainwindow");
 //BA.debugLineNum = 183;BA.debugLine="pan.AddView(palShowRadio,0,34dip + 2dip,640dip";
_pan.AddView((android.view.View)(mostCurrent._palshowradio.getObject()),(int) (0),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (34))+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (640)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (480)));
 //BA.debugLineNum = 184;BA.debugLine="pan.AddView(ltvCapture,640dip,34dip + 2dip,300";
_pan.AddView((android.view.View)(mostCurrent._ltvcapture.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (640)),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (34))+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (300)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (420)));
 //BA.debugLineNum = 185;BA.debugLine="container.AddPage(pan,\"采集\")";
mostCurrent._container.AddPage((android.view.View)(_pan.getObject()),"采集");
 break;
case 1:
 //BA.debugLineNum = 187;BA.debugLine="pan = CreatePanel(TYPE_ISPERM_TEST,\"testwindow";
_pan = _createpanel(_type_isperm_test,"testwindow");
 //BA.debugLineNum = 188;BA.debugLine="pan.AddView(igShow,0,80,480,360)";
_pan.AddView((android.view.View)(mostCurrent._igshow.getObject()),(int) (0),(int) (80),(int) (480),(int) (360));
 //BA.debugLineNum = 189;BA.debugLine="pan.AddView(ltv_pigid,20,0,130,70)";
_pan.AddView((android.view.View)(mostCurrent._ltv_pigid.getObject()),(int) (20),(int) (0),(int) (130),(int) (70));
 //BA.debugLineNum = 190;BA.debugLine="pan.AddView(ltv_anay_point,170,0,100,70)";
_pan.AddView((android.view.View)(mostCurrent._ltv_anay_point.getObject()),(int) (170),(int) (0),(int) (100),(int) (70));
 //BA.debugLineNum = 191;BA.debugLine="pan.AddView(ltv_anay_time,290,0,190,70)";
_pan.AddView((android.view.View)(mostCurrent._ltv_anay_time.getObject()),(int) (290),(int) (0),(int) (190),(int) (70));
 //BA.debugLineNum = 192;BA.debugLine="pan.AddView(ltvTable_Test,500,130,280,200)";
_pan.AddView((android.view.View)(mostCurrent._ltvtable_test.getObject()),(int) (500),(int) (130),(int) (280),(int) (200));
 //BA.debugLineNum = 193;BA.debugLine="lbl_Count.SetTextSizeAnimated(0,30)";
mostCurrent._lbl_count.SetTextSizeAnimated((int) (0),(float) (30));
 //BA.debugLineNum = 194;BA.debugLine="pan.AddView(lbl_Count,490,340,120,40)";
_pan.AddView((android.view.View)(mostCurrent._lbl_count.getObject()),(int) (490),(int) (340),(int) (120),(int) (40));
 //BA.debugLineNum = 195;BA.debugLine="lbl_Rate.SetTextSizeAnimated(0,30)";
mostCurrent._lbl_rate.SetTextSizeAnimated((int) (0),(float) (30));
 //BA.debugLineNum = 196;BA.debugLine="pan.AddView(lbl_Rate,610,340,120,40)";
_pan.AddView((android.view.View)(mostCurrent._lbl_rate.getObject()),(int) (610),(int) (340),(int) (120),(int) (40));
 //BA.debugLineNum = 197;BA.debugLine="container.AddPage(pan,\"调试\")";
mostCurrent._container.AddPage((android.view.View)(_pan.getObject()),"调试");
 break;
case 2:
 //BA.debugLineNum = 199;BA.debugLine="pan = CreatePanel(TYPE_ISPERM_INI,\"iniwindow\")";
_pan = _createpanel(_type_isperm_ini,"iniwindow");
 //BA.debugLineNum = 200;BA.debugLine="lblView.SetTextSizeAnimated(0,50)";
mostCurrent._lblview.SetTextSizeAnimated((int) (0),(float) (50));
 //BA.debugLineNum = 201;BA.debugLine="pan.AddView(lblView,400dip,50dip,120dip,60dip)";
_pan.AddView((android.view.View)(mostCurrent._lblview.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (400)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 202;BA.debugLine="pan.AddView(ltvTable_Ini,10dip,20dip,370dip,40";
_pan.AddView((android.view.View)(mostCurrent._ltvtable_ini.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (370)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (400)));
 //BA.debugLineNum = 203;BA.debugLine="container.AddPage(pan,\"设置\")";
mostCurrent._container.AddPage((android.view.View)(_pan.getObject()),"设置");
 break;
}
;
 }
};
 //BA.debugLineNum = 207;BA.debugLine="pager.Initialize(container,\"Pager\")";
mostCurrent._pager.Initialize(mostCurrent.activityBA,mostCurrent._container,"Pager");
 //BA.debugLineNum = 209;BA.debugLine="tabs.Initialize(pager)";
mostCurrent._tabs.Initialize(mostCurrent.activityBA,mostCurrent._pager);
 //BA.debugLineNum = 211;BA.debugLine="tabs.LineHeight = 5dip";
mostCurrent._tabs.setLineHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 212;BA.debugLine="tabs.UpperCaseTitle = True";
mostCurrent._tabs.setUpperCaseTitle(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 213;BA.debugLine="Activity.AddView(tabs,0,0,FILL_PARENT,WRAP_CONTEN";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._tabs.getObject()),(int) (0),(int) (0),_fill_parent,_wrap_content);
 //BA.debugLineNum = 215;BA.debugLine="Activity.AddView(pager,0,34dip + 2dip,Activity.Wi";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pager.getObject()),(int) (0),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (34))+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))),mostCurrent._activity.getWidth(),mostCurrent._activity.getHeight());
 //BA.debugLineNum = 217;BA.debugLine="Try";
try { //BA.debugLineNum = 218;BA.debugLine="usbSerial1.SetPort(SERIAL_PORT,SERIAL_RATE,SERIA";
mostCurrent._usbserial1.SetPort(mostCurrent._serial_port,(int)(Double.parseDouble(mostCurrent._serial_rate)),_serial_num);
 } 
       catch (Exception e140) {
			processBA.setLastException(e140); //BA.debugLineNum = 220;BA.debugLine="ToastMessageShow(LastException.Message,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 222;BA.debugLine="aSync.Initialize(usbSerial1.InputStream,usbSerial";
mostCurrent._async.Initialize(processBA,mostCurrent._usbserial1.getInputStream(),mostCurrent._usbserial1.getOutputStream(),"Astreams");
 //BA.debugLineNum = 224;BA.debugLine="IniListView_Pigid";
_inilistview_pigid();
 //BA.debugLineNum = 225;BA.debugLine="IniListView_Point";
_inilistview_point();
 //BA.debugLineNum = 226;BA.debugLine="IniListView_Time";
_inilistview_time();
 //BA.debugLineNum = 227;BA.debugLine="Show_Photo";
_show_photo();
 //BA.debugLineNum = 228;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 230;BA.debugLine="read_Table_Dict";
_read_table_dict();
 //BA.debugLineNum = 231;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 232;BA.debugLine="lbl_Text_ltvTable";
_lbl_text_ltvtable();
 //BA.debugLineNum = 233;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 258;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 259;BA.debugLine="camera1.StopPreview()";
mostCurrent._camera1.StopPreview();
 //BA.debugLineNum = 260;BA.debugLine="camera1.Release()";
mostCurrent._camera1.Release();
 //BA.debugLineNum = 261;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 262;BA.debugLine="aSync.Close()";
mostCurrent._async.Close();
 //BA.debugLineNum = 263;BA.debugLine="usbSerial1.close()";
mostCurrent._usbserial1.close();
 };
 //BA.debugLineNum = 265;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 235;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 236;BA.debugLine="pager.GotoPage(CurrentPage,False)";
mostCurrent._pager.GotoPage(_currentpage,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 238;BA.debugLine="CreateTable";
_createtable();
 //BA.debugLineNum = 239;BA.debugLine="camera1.Initialize(palShowRadio,\"Camera2\")";
mostCurrent._camera1.Initialize(mostCurrent.activityBA,(android.view.ViewGroup)(mostCurrent._palshowradio.getObject()),"Camera2");
 //BA.debugLineNum = 240;BA.debugLine="Try";
try { //BA.debugLineNum = 241;BA.debugLine="usbSerial1.SetPort(SERIAL_PORT,SERIAL_RATE,SERIA";
mostCurrent._usbserial1.SetPort(mostCurrent._serial_port,(int)(Double.parseDouble(mostCurrent._serial_rate)),_serial_num);
 } 
       catch (Exception e159) {
			processBA.setLastException(e159); //BA.debugLineNum = 243;BA.debugLine="ToastMessageShow(LastException.Message,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 245;BA.debugLine="aSync.Initialize(usbSerial1.InputStream,usbSerial";
mostCurrent._async.Initialize(processBA,mostCurrent._usbserial1.getInputStream(),mostCurrent._usbserial1.getOutputStream(),"Astreams");
 //BA.debugLineNum = 247;BA.debugLine="IniListView_Pigid";
_inilistview_pigid();
 //BA.debugLineNum = 248;BA.debugLine="IniListView_Point";
_inilistview_point();
 //BA.debugLineNum = 249;BA.debugLine="IniListView_Time";
_inilistview_time();
 //BA.debugLineNum = 250;BA.debugLine="Show_Photo";
_show_photo();
 //BA.debugLineNum = 251;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 253;BA.debugLine="read_Table_Dict";
_read_table_dict();
 //BA.debugLineNum = 254;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 255;BA.debugLine="lbl_Text_ltvTable";
_lbl_text_ltvtable();
 //BA.debugLineNum = 256;BA.debugLine="End Sub";
return "";
}
public static String  _activity_windowfocuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 151;BA.debugLine="Sub Activity_WindowFocusChanged(HasFocus As Boolea";
 //BA.debugLineNum = 152;BA.debugLine="If HasFocus Then";
if (_hasfocus) { 
 //BA.debugLineNum = 153;BA.debugLine="ForceImmersiceMode";
_forceimmersicemode();
 };
 //BA.debugLineNum = 155;BA.debugLine="End Sub";
return "";
}
public static String  _astreams_error() throws Exception{
 //BA.debugLineNum = 653;BA.debugLine="Sub Astreams_Error()";
 //BA.debugLineNum = 654;BA.debugLine="ToastMessageShow(LastException.Message,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 655;BA.debugLine="End Sub";
return "";
}
public static String  _astreams_newdata(byte[] _buffer) throws Exception{
String _cmd_recevie = "";
anywheresoftware.b4a.agraham.byteconverter.ByteConverter _conv = null;
 //BA.debugLineNum = 630;BA.debugLine="Sub Astreams_NewData(Buffer() As Byte)";
 //BA.debugLineNum = 631;BA.debugLine="Dim cmd_Recevie As String";
_cmd_recevie = "";
 //BA.debugLineNum = 632;BA.debugLine="Dim conv As ByteConverter";
_conv = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 635;BA.debugLine="cmd_Recevie = conv.HexFromBytes(Buffer).ToLowerCa";
_cmd_recevie = _conv.HexFromBytes(_buffer).toLowerCase();
 //BA.debugLineNum = 639;BA.debugLine="If cmd_Recevie.Length() < 22 Then";
if (_cmd_recevie.length()<22) { 
 //BA.debugLineNum = 640;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 642;BA.debugLine="recevie_Cmd(cmd_Recevie)";
_recevie_cmd(_cmd_recevie);
 //BA.debugLineNum = 644;BA.debugLine="If serial_Return_Data <> 0 Then";
if (_serial_return_data!=0) { 
 //BA.debugLineNum = 645;BA.debugLine="excu_Cmd_Data";
_excu_cmd_data();
 };
 //BA.debugLineNum = 650;BA.debugLine="End Sub";
return "";
}
public static String  _btnaddone_click() throws Exception{
 //BA.debugLineNum = 1179;BA.debugLine="Sub btnAddOne_Click";
 //BA.debugLineNum = 1180;BA.debugLine="If lblView.Text >= 1 Then";
if ((double)(Double.parseDouble(mostCurrent._lblview.getText()))>=1) { 
 //BA.debugLineNum = 1181;BA.debugLine="lbl_text_int = lblView.Text + 1";
_lbl_text_int = (int) ((double)(Double.parseDouble(mostCurrent._lblview.getText()))+1);
 //BA.debugLineNum = 1182;BA.debugLine="lblView.Text = lbl_text_int";
mostCurrent._lblview.setText((Object)(_lbl_text_int));
 }else {
 //BA.debugLineNum = 1184;BA.debugLine="lbl_text_double = lblView.Text + 1 * 0.01";
_lbl_text_double = (double)(Double.parseDouble(mostCurrent._lblview.getText()))+1*0.01;
 //BA.debugLineNum = 1185;BA.debugLine="lblView.Text = doubleToString(lbl_text_double)";
mostCurrent._lblview.setText((Object)(_doubletostring(_lbl_text_double)));
 };
 //BA.debugLineNum = 1188;BA.debugLine="mapUpdate";
_mapupdate();
 //BA.debugLineNum = 1189;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 1190;BA.debugLine="End Sub";
return "";
}
public static String  _btnaddten_click() throws Exception{
 //BA.debugLineNum = 1167;BA.debugLine="Sub btnAddTen_Click";
 //BA.debugLineNum = 1168;BA.debugLine="If lblView.Text >= 1 Then";
if ((double)(Double.parseDouble(mostCurrent._lblview.getText()))>=1) { 
 //BA.debugLineNum = 1169;BA.debugLine="lbl_text_int = lblView.Text + 10";
_lbl_text_int = (int) ((double)(Double.parseDouble(mostCurrent._lblview.getText()))+10);
 //BA.debugLineNum = 1170;BA.debugLine="lblView.Text = lbl_text_int";
mostCurrent._lblview.setText((Object)(_lbl_text_int));
 }else {
 //BA.debugLineNum = 1172;BA.debugLine="lbl_text_double = lblView.Text + 10 * 0.01";
_lbl_text_double = (double)(Double.parseDouble(mostCurrent._lblview.getText()))+10*0.01;
 //BA.debugLineNum = 1173;BA.debugLine="lblView.Text = doubleToString(lbl_text_double)";
mostCurrent._lblview.setText((Object)(_doubletostring(_lbl_text_double)));
 };
 //BA.debugLineNum = 1175;BA.debugLine="mapUpdate";
_mapupdate();
 //BA.debugLineNum = 1176;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 1177;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 880;BA.debugLine="Sub btnAnay_Click";
 //BA.debugLineNum = 881;BA.debugLine="Dim isperm_dict_cursors As Cursor";
_isperm_dict_cursors = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 882;BA.debugLine="Dim isperm_dict_array() As Float";
_isperm_dict_array = new float[(int) (0)];
;
 //BA.debugLineNum = 883;BA.debugLine="Dim isperm_Count() As Int";
_isperm_count = new int[(int) (0)];
;
 //BA.debugLineNum = 884;BA.debugLine="Dim isperm_dict_cloumns() As Object";
_isperm_dict_cloumns = new Object[(int) (0)];
{
int d0 = _isperm_dict_cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_isperm_dict_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 885;BA.debugLine="Dim isperm_dict_params As Map";
_isperm_dict_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 886;BA.debugLine="Dim isperm_dict_map As Map";
_isperm_dict_map = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 888;BA.debugLine="Dim timeNow As Long";
_timenow = 0L;
 //BA.debugLineNum = 889;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 890;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 891;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 893;BA.debugLine="Dim id As Int";
_id = 0;
 //BA.debugLineNum = 894;BA.debugLine="Dim history_params() As Object";
_history_params = new Object[(int) (0)];
{
int d0 = _history_params.length;
for (int i0 = 0;i0 < d0;i0++) {
_history_params[i0] = new Object();
}
}
;
 //BA.debugLineNum = 896;BA.debugLine="isperm_dict_map.Initialize()";
_isperm_dict_map.Initialize();
 //BA.debugLineNum = 897;BA.debugLine="isperm_dict_params.Initialize()";
_isperm_dict_params.Initialize();
 //BA.debugLineNum = 898;BA.debugLine="isperm_dict_cloumns = Array As Object(\"name\",\"val";
_isperm_dict_cloumns = new Object[]{(Object)("name"),(Object)("value")};
 //BA.debugLineNum = 900;BA.debugLine="btnEnabled_Test(False)";
_btnenabled_test(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 901;BA.debugLine="cloumns = Array As Object(\"ana_path\")";
_cloumns = new Object[]{(Object)("ana_path")};
 //BA.debugLineNum = 902;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 903;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 904;BA.debugLine="ana_history_path = \"\"";
mostCurrent._ana_history_path = "";
 //BA.debugLineNum = 905;BA.debugLine="ana_capture_path = \"\"";
mostCurrent._ana_capture_path = "";
 //BA.debugLineNum = 907;BA.debugLine="If pigid <> Null And pigid <> \"\" Then";
if (mostCurrent._pigid!= null && (mostCurrent._pigid).equals("") == false) { 
 //BA.debugLineNum = 908;BA.debugLine="params.Put(\"pigid\",pigid)";
_params.Put((Object)("pigid"),(Object)(mostCurrent._pigid));
 //BA.debugLineNum = 909;BA.debugLine="params.Put(\"ana_capture_point\",ana_capture_point";
_params.Put((Object)("ana_capture_point"),(Object)(mostCurrent._ana_capture_point));
 };
 //BA.debugLineNum = 913;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"capture\",c";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"capture",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 914;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step636 = 1;
final int limit636 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step636 > 0 && _i <= limit636) || (step636 < 0 && _i >= limit636); _i = ((int)(0 + _i + step636))) {
 //BA.debugLineNum = 915;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 916;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 917;BA.debugLine="ana_capture_path = cursor1.GetString(cloumns(0)";
mostCurrent._ana_capture_path = _cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]));
 };
 }
};
 //BA.debugLineNum = 920;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 921;BA.debugLine="If ana_capture_path <> \"\" And ana_capture_path <>";
if ((mostCurrent._ana_capture_path).equals("") == false && mostCurrent._ana_capture_path!= null) { 
 //BA.debugLineNum = 923;BA.debugLine="timeNow = DateTime.Now";
_timenow = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 924;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 925;BA.debugLine="ana_history_path = create_File.Create_Path_Dir(a";
mostCurrent._ana_history_path = mostCurrent._create_file._create_path_dir(mostCurrent.activityBA,mostCurrent._ana_capture_path,_timenow);
 //BA.debugLineNum = 926;BA.debugLine="isperm_dict_cursors = sql_capture.QueryColumnDat";
_isperm_dict_cursors = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"dict_properties",_isperm_dict_cloumns,_isperm_dict_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 928;BA.debugLine="For i = 0 To isperm_dict_cursors.RowCount - 1";
{
final int step648 = 1;
final int limit648 = (int) (_isperm_dict_cursors.getRowCount()-1);
for (_i = (int) (0); (step648 > 0 && _i <= limit648) || (step648 < 0 && _i >= limit648); _i = ((int)(0 + _i + step648))) {
 //BA.debugLineNum = 929;BA.debugLine="isperm_dict_cursors.Position = i";
_isperm_dict_cursors.setPosition(_i);
 //BA.debugLineNum = 930;BA.debugLine="isperm_dict_map.put(isperm_dict_cursors.GetStri";
_isperm_dict_map.Put((Object)(_isperm_dict_cursors.GetString(BA.ObjectToString(_isperm_dict_cloumns[(int) (0)]))),(Object)(_isperm_dict_cursors.GetString(BA.ObjectToString(_isperm_dict_cloumns[(int) (1)]))));
 }
};
 //BA.debugLineNum = 932;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 934;BA.debugLine="If isperm_dict_map.Size > 9 Then";
if (_isperm_dict_map.getSize()>9) { 
 //BA.debugLineNum = 935;BA.debugLine="isperm_dict_array = Array As Float(isperm_dict_";
_isperm_dict_array = new float[]{(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (0)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (1)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (2)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (3)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (4)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (5)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (6)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (7)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (8)))),(float)(BA.ObjectToNumber(_isperm_dict_map.GetValueAt((int) (9))))};
 };
 //BA.debugLineNum = 940;BA.debugLine="isperm_Count = isperm.returnCount(File.DirRootEx";
_isperm_count = mostCurrent._isperm.returnCount(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._ana_capture_path+"/",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._ana_history_path,_isperm_dict_array);
 //BA.debugLineNum = 946;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 947;BA.debugLine="id = sql_capture.QueryIDMax(\"resulthistory\",\"id\"";
_id = (int)(Double.parseDouble(mostCurrent._sql_capture._queryidmax(mostCurrent.activityBA,"resulthistory","id")));
 //BA.debugLineNum = 948;BA.debugLine="history_params = Array As Object(id+1,pigid,crea";
_history_params = new Object[]{(Object)(_id+1),(Object)(mostCurrent._pigid),(Object)(mostCurrent._create_file._timeformat(mostCurrent.activityBA,_timenow)),(Object)(mostCurrent._ana_capture_point),(Object)(mostCurrent._ana_history_path),(Object)(""),(Object)(_isperm_count[(int) (0)]),(Object)(_isperm_count[(int) (1)]),(Object)(_isperm_dict_array[(int) (0)]),(Object)(_isperm_dict_array[(int) (1)]),(Object)(_isperm_dict_array[(int) (2)]),(Object)(_isperm_dict_array[(int) (3)]),(Object)(_isperm_dict_array[(int) (4)]),(Object)(_isperm_dict_array[(int) (5)]),(Object)(_isperm_dict_array[(int) (6)]),(Object)(_isperm_dict_array[(int) (7)]),(Object)(_isperm_dict_array[(int) (8)])};
 //BA.debugLineNum = 951;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 952;BA.debugLine="sql_capture.InsertToCapture(\"resulthistory\",hist";
mostCurrent._sql_capture._inserttocapture(mostCurrent.activityBA,"resulthistory",_history_params);
 //BA.debugLineNum = 953;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 954;BA.debugLine="IniListView_Time";
_inilistview_time();
 }else {
 //BA.debugLineNum = 956;BA.debugLine="ToastMessageShow(\"此采集点并未采集到图片....\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("此采集点并未采集到图片....",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 958;BA.debugLine="btnEnabled_Test(True)";
_btnenabled_test(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 959;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 961;BA.debugLine="End Sub";
return "";
}
public static String  _btncapture_click() throws Exception{
 //BA.debugLineNum = 484;BA.debugLine="Sub btnCapture_Click";
 //BA.debugLineNum = 486;BA.debugLine="send_Cmd(\"AA5508001001434f4e544d\")";
_send_cmd("AA5508001001434f4e544d");
 //BA.debugLineNum = 492;BA.debugLine="If ana_Point_Num_Count <> 0 Then";
if (_ana_point_num_count!=0) { 
 //BA.debugLineNum = 493;BA.debugLine="ana_Point_Num_Count = 0";
_ana_point_num_count = (int) (0);
 };
 //BA.debugLineNum = 496;BA.debugLine="taskTime.Initialize(\"taskTime\",250)";
_tasktime.Initialize(processBA,"taskTime",(long) (250));
 //BA.debugLineNum = 497;BA.debugLine="taskTime2.Initialize(\"taskTime2\",250)";
_tasktime2.Initialize(processBA,"taskTime2",(long) (250));
 //BA.debugLineNum = 498;BA.debugLine="taskTime2.Enabled = True";
_tasktime2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 500;BA.debugLine="btnEnabled(False)";
_btnenabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 508;BA.debugLine="excu_Cmd_Data";
_excu_cmd_data();
 //BA.debugLineNum = 509;BA.debugLine="End Sub";
return "";
}
public static String  _btndelone_click() throws Exception{
 //BA.debugLineNum = 1147;BA.debugLine="Sub btnDelOne_Click";
 //BA.debugLineNum = 1148;BA.debugLine="If lblView.Text >= 1 Then";
if ((double)(Double.parseDouble(mostCurrent._lblview.getText()))>=1) { 
 //BA.debugLineNum = 1149;BA.debugLine="lbl_text_int = lblView.Text - 1";
_lbl_text_int = (int) ((double)(Double.parseDouble(mostCurrent._lblview.getText()))-1);
 //BA.debugLineNum = 1150;BA.debugLine="If lbl_text_int <= 0 Then";
if (_lbl_text_int<=0) { 
 //BA.debugLineNum = 1151;BA.debugLine="lblView.Text = 0";
mostCurrent._lblview.setText((Object)(0));
 }else {
 //BA.debugLineNum = 1153;BA.debugLine="lblView.Text = lbl_text_int";
mostCurrent._lblview.setText((Object)(_lbl_text_int));
 };
 }else {
 //BA.debugLineNum = 1156;BA.debugLine="lbl_text_double = lblView.Text - 1 * 0.01";
_lbl_text_double = (double)(Double.parseDouble(mostCurrent._lblview.getText()))-1*0.01;
 //BA.debugLineNum = 1157;BA.debugLine="If lbl_text_double <= 0 Then";
if (_lbl_text_double<=0) { 
 //BA.debugLineNum = 1158;BA.debugLine="lblView.Text = 0";
mostCurrent._lblview.setText((Object)(0));
 }else {
 //BA.debugLineNum = 1160;BA.debugLine="lblView.Text = doubleToString(lbl_text_double)";
mostCurrent._lblview.setText((Object)(_doubletostring(_lbl_text_double)));
 };
 };
 //BA.debugLineNum = 1163;BA.debugLine="mapUpdate";
_mapupdate();
 //BA.debugLineNum = 1164;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 1165;BA.debugLine="End Sub";
return "";
}
public static String  _btndelten_click() throws Exception{
 //BA.debugLineNum = 1127;BA.debugLine="Sub btnDelTen_Click";
 //BA.debugLineNum = 1128;BA.debugLine="If lblView.Text >= 1 Then";
if ((double)(Double.parseDouble(mostCurrent._lblview.getText()))>=1) { 
 //BA.debugLineNum = 1129;BA.debugLine="lbl_text_int = lblView.Text - 10";
_lbl_text_int = (int) ((double)(Double.parseDouble(mostCurrent._lblview.getText()))-10);
 //BA.debugLineNum = 1130;BA.debugLine="If (lbl_text_int <= 0) Then";
if ((_lbl_text_int<=0)) { 
 //BA.debugLineNum = 1131;BA.debugLine="lblView.Text = 0";
mostCurrent._lblview.setText((Object)(0));
 }else {
 //BA.debugLineNum = 1133;BA.debugLine="lblView.Text = lbl_text_int";
mostCurrent._lblview.setText((Object)(_lbl_text_int));
 };
 }else {
 //BA.debugLineNum = 1136;BA.debugLine="lbl_text_double = lblView.Text - 10 * 0.01";
_lbl_text_double = (double)(Double.parseDouble(mostCurrent._lblview.getText()))-10*0.01;
 //BA.debugLineNum = 1137;BA.debugLine="If lbl_text_double <= 0 Then";
if (_lbl_text_double<=0) { 
 //BA.debugLineNum = 1138;BA.debugLine="lblView.Text = 0";
mostCurrent._lblview.setText((Object)(0));
 }else {
 //BA.debugLineNum = 1140;BA.debugLine="lblView.Text = doubleToString(lbl_text_double)";
mostCurrent._lblview.setText((Object)(_doubletostring(_lbl_text_double)));
 };
 };
 //BA.debugLineNum = 1143;BA.debugLine="mapUpdate";
_mapupdate();
 //BA.debugLineNum = 1144;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 1145;BA.debugLine="End Sub";
return "";
}
public static String  _btndrawcontours_click() throws Exception{
 //BA.debugLineNum = 822;BA.debugLine="Sub btnDrawContours_Click";
 //BA.debugLineNum = 823;BA.debugLine="Show_anay_Photo(\"DrawContours\")";
_show_anay_photo("DrawContours");
 //BA.debugLineNum = 824;BA.debugLine="End Sub";
return "";
}
public static String  _btnenabled(boolean _flag_enabled) throws Exception{
 //BA.debugLineNum = 413;BA.debugLine="Sub btnEnabled(flag_Enabled As Boolean)";
 //BA.debugLineNum = 416;BA.debugLine="If flag_Enabled == False Then";
if (_flag_enabled==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 417;BA.debugLine="button_RGBA.Initialize(\"button_RGBA\")";
mostCurrent._button_rgba.Initialize(mostCurrent.activityBA,"button_RGBA");
 //BA.debugLineNum = 418;BA.debugLine="button_RGBA.Color = Colors.ARGB(0,0,0,0)";
mostCurrent._button_rgba.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 419;BA.debugLine="Activity.AddView(button_RGBA, 0, 0, Activity.Wid";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._button_rgba.getObject()),(int) (0),(int) (0),mostCurrent._activity.getWidth(),mostCurrent._activity.getHeight());
 }else {
 //BA.debugLineNum = 421;BA.debugLine="Try";
try { //BA.debugLineNum = 422;BA.debugLine="button_RGBA.RemoveView()";
mostCurrent._button_rgba.RemoveView();
 } 
       catch (Exception e299) {
			processBA.setLastException(e299); //BA.debugLineNum = 424;BA.debugLine="ToastMessageShow(LastException.Message,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 427;BA.debugLine="btnCapture.Enabled = flag_Enabled";
mostCurrent._btncapture.setEnabled(_flag_enabled);
 //BA.debugLineNum = 428;BA.debugLine="btnExit_Main.Enabled = flag_Enabled";
mostCurrent._btnexit_main.setEnabled(_flag_enabled);
 //BA.debugLineNum = 429;BA.debugLine="End Sub";
return "";
}
public static String  _btnenabled_test(boolean _flag_enabled) throws Exception{
 //BA.debugLineNum = 826;BA.debugLine="Sub btnEnabled_Test(flag_Enabled As Boolean)";
 //BA.debugLineNum = 827;BA.debugLine="btnAnay.Enabled = flag_Enabled";
mostCurrent._btnanay.setEnabled(_flag_enabled);
 //BA.debugLineNum = 828;BA.debugLine="btnExit_Test.Enabled = flag_Enabled";
mostCurrent._btnexit_test.setEnabled(_flag_enabled);
 //BA.debugLineNum = 829;BA.debugLine="End Sub";
return "";
}
public static String  _btnexit_ini_click() throws Exception{
 //BA.debugLineNum = 1120;BA.debugLine="Sub btnExit_Ini_Click";
 //BA.debugLineNum = 1121;BA.debugLine="camera1.StopPreview()";
mostCurrent._camera1.StopPreview();
 //BA.debugLineNum = 1122;BA.debugLine="camera1.Release()";
mostCurrent._camera1.Release();
 //BA.debugLineNum = 1123;BA.debugLine="Activity.Finish()";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 1125;BA.debugLine="End Sub";
return "";
}
public static String  _btnexit_main_click() throws Exception{
 //BA.debugLineNum = 407;BA.debugLine="Sub btnExit_Main_Click";
 //BA.debugLineNum = 408;BA.debugLine="camera1.StopPreview()";
mostCurrent._camera1.StopPreview();
 //BA.debugLineNum = 409;BA.debugLine="camera1.Release()";
mostCurrent._camera1.Release();
 //BA.debugLineNum = 410;BA.debugLine="Activity.Finish()";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 411;BA.debugLine="End Sub";
return "";
}
public static String  _btnexit_test_click() throws Exception{
 //BA.debugLineNum = 816;BA.debugLine="Sub btnExit_Test_Click";
 //BA.debugLineNum = 817;BA.debugLine="Activity.Finish()";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 819;BA.debugLine="End Sub";
return "";
}
public static String  _btngaussianblur_click() throws Exception{
 //BA.debugLineNum = 811;BA.debugLine="Sub btnGaussianBlur_Click";
 //BA.debugLineNum = 812;BA.debugLine="Show_anay_Photo(\"GaussianBlur\")";
_show_anay_photo("GaussianBlur");
 //BA.debugLineNum = 813;BA.debugLine="End Sub";
return "";
}
public static String  _btnsave_click() throws Exception{
anywheresoftware.b4a.objects.collections.Map _columns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 1096;BA.debugLine="Sub btnSave_Click";
 //BA.debugLineNum = 1097;BA.debugLine="Dim columns As Map";
_columns = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1098;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1100;BA.debugLine="columns.Initialize()";
_columns.Initialize();
 //BA.debugLineNum = 1101;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 1102;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 1104;BA.debugLine="If itemMap.Size == itemMap_Begin.Size Then";
if (mostCurrent._itemmap.getSize()==mostCurrent._itemmap_begin.getSize()) { 
 //BA.debugLineNum = 1105;BA.debugLine="For i = 0 To itemMap.Size - 1";
{
final int step781 = 1;
final int limit781 = (int) (mostCurrent._itemmap.getSize()-1);
for (_i = (int) (0); (step781 > 0 && _i <= limit781) || (step781 < 0 && _i >= limit781); _i = ((int)(0 + _i + step781))) {
 //BA.debugLineNum = 1106;BA.debugLine="If itemMap.GetValueAt(i) <> itemMap_Begin.GetVa";
if ((mostCurrent._itemmap.GetValueAt(_i)).equals(mostCurrent._itemmap_begin.GetValueAt(_i)) == false) { 
 //BA.debugLineNum = 1107;BA.debugLine="columns.Put(\"value\",itemMap.GetValueAt(i))";
_columns.Put((Object)("value"),mostCurrent._itemmap.GetValueAt(_i));
 //BA.debugLineNum = 1108;BA.debugLine="params.Put(\"name\",itemMap.GetKeyAt(i))";
_params.Put((Object)("name"),mostCurrent._itemmap.GetKeyAt(_i));
 //BA.debugLineNum = 1109;BA.debugLine="params.Put(\"params1\",\"[isperm]\")";
_params.Put((Object)("params1"),(Object)("[isperm]"));
 //BA.debugLineNum = 1110;BA.debugLine="sql_capture.UpdateToTable(\"dict_properties\",co";
mostCurrent._sql_capture._updatetotable(mostCurrent.activityBA,"dict_properties",_columns,_params);
 };
 }
};
 };
 //BA.debugLineNum = 1114;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 1115;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 1116;BA.debugLine="ToastMessageShow(\"保存成功...\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("保存成功...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1117;BA.debugLine="End Sub";
return "";
}
public static String  _btnstretch_click() throws Exception{
 //BA.debugLineNum = 806;BA.debugLine="Sub btnStretch_Click";
 //BA.debugLineNum = 807;BA.debugLine="Show_anay_Photo(\"Stretch\")";
_show_anay_photo("Stretch");
 //BA.debugLineNum = 808;BA.debugLine="End Sub";
return "";
}
public static String  _btnsubdraw_click() throws Exception{
 //BA.debugLineNum = 801;BA.debugLine="Sub btnSubDraw_Click";
 //BA.debugLineNum = 802;BA.debugLine="Show_anay_Photo(\"SubDrawContours\")";
_show_anay_photo("SubDrawContours");
 //BA.debugLineNum = 803;BA.debugLine="End Sub";
return "";
}
public static String  _camera2_picturetaken(byte[] _data) throws Exception{
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
String _str_num = "";
 //BA.debugLineNum = 375;BA.debugLine="Sub Camera2_PictureTaken(Data() As Byte)";
 //BA.debugLineNum = 376;BA.debugLine="Dim out As OutputStream";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 377;BA.debugLine="Dim str_num As String";
_str_num = "";
 //BA.debugLineNum = 379;BA.debugLine="taskTime.Enabled = False";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 380;BA.debugLine="camera1.PictureSize(640,480)";
mostCurrent._camera1.PictureSize((int) (640),(int) (480));
 //BA.debugLineNum = 381;BA.debugLine="camera1.StartPreview()";
mostCurrent._camera1.StartPreview();
 //BA.debugLineNum = 383;BA.debugLine="If photo_num < 10 Then";
if (_photo_num<10) { 
 //BA.debugLineNum = 384;BA.debugLine="str_num = \"00\"&photo_num";
_str_num = "00"+BA.NumberToString(_photo_num);
 }else {
 //BA.debugLineNum = 386;BA.debugLine="str_num = \"0\"&photo_num";
_str_num = "0"+BA.NumberToString(_photo_num);
 };
 //BA.debugLineNum = 389;BA.debugLine="out = File.OpenOutput(File.DirRootExternal&strPat";
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+mostCurrent._strpath,_str_num+".jpg",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 390;BA.debugLine="out.WriteBytes(Data, 0, Data.Length)";
_out.WriteBytes(_data,(int) (0),_data.length);
 //BA.debugLineNum = 391;BA.debugLine="out.Close";
_out.Close();
 //BA.debugLineNum = 393;BA.debugLine="If photo_num >= photo_Count Then";
if (_photo_num>=_photo_count) { 
 //BA.debugLineNum = 394;BA.debugLine="flag_Picture = False";
_flag_picture = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 395;BA.debugLine="taskTime.Enabled = False";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 396;BA.debugLine="flag_Task = True";
_flag_task = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 397;BA.debugLine="ana_Point_Num_Count = ana_Point_Num_Count + 1";
_ana_point_num_count = (int) (_ana_point_num_count+1);
 }else {
 //BA.debugLineNum = 399;BA.debugLine="flag_Picture = True";
_flag_picture = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 400;BA.debugLine="taskTime.Enabled = True";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 403;BA.debugLine="photo_num = photo_num + 1";
_photo_num = (int) (_photo_num+1);
 //BA.debugLineNum = 404;BA.debugLine="End Sub";
return "";
}
public static String  _camera2_ready(boolean _success) throws Exception{
 //BA.debugLineNum = 349;BA.debugLine="Sub Camera2_Ready(Success As Boolean)";
 //BA.debugLineNum = 351;BA.debugLine="If Activity.Width > Activity.Height Then";
if (mostCurrent._activity.getWidth()>mostCurrent._activity.getHeight()) { 
 //BA.debugLineNum = 352;BA.debugLine="Try";
try { //BA.debugLineNum = 353;BA.debugLine="camera1.OriLandscape()";
mostCurrent._camera1.OriLandscape();
 } 
       catch (Exception e244) {
			processBA.setLastException(e244); //BA.debugLineNum = 355;BA.debugLine="Msgbox(LastException.Message,\"设置错误...\")";
anywheresoftware.b4a.keywords.Common.Msgbox(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),"设置错误...",mostCurrent.activityBA);
 };
 }else {
 //BA.debugLineNum = 359;BA.debugLine="Try";
try { //BA.debugLineNum = 360;BA.debugLine="camera1.OriPortrait()";
mostCurrent._camera1.OriPortrait();
 } 
       catch (Exception e250) {
			processBA.setLastException(e250); //BA.debugLineNum = 362;BA.debugLine="Msgbox(LastException,\"设置错误...\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),"设置错误...",mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 366;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 367;BA.debugLine="camera1.PictureSize(640,480)";
mostCurrent._camera1.PictureSize((int) (640),(int) (480));
 //BA.debugLineNum = 368;BA.debugLine="camera1.StartPreview()";
mostCurrent._camera1.StartPreview();
 }else {
 //BA.debugLineNum = 370;BA.debugLine="ToastMessageShow(\"无法开启相机....\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("无法开启相机....",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 372;BA.debugLine="End Sub";
return "";
}
public static int  _charinstring(String _ch,String _str) throws Exception{
String _strs = "";
int _returnint = 0;
int _i = 0;
 //BA.debugLineNum = 1074;BA.debugLine="Sub charInString(ch As String,str As String) As In";
 //BA.debugLineNum = 1075;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 1076;BA.debugLine="Dim returnInt As Int";
_returnint = 0;
 //BA.debugLineNum = 1077;BA.debugLine="returnInt = -1";
_returnint = (int) (-1);
 //BA.debugLineNum = 1078;BA.debugLine="If str <> Null Then";
if (_str!= null) { 
 //BA.debugLineNum = 1079;BA.debugLine="If ch <> Null Then";
if (_ch!= null) { 
 //BA.debugLineNum = 1080;BA.debugLine="strs = str.Trim()";
_strs = _str.trim();
 //BA.debugLineNum = 1081;BA.debugLine="If strs.Length() > 0 Then";
if (_strs.length()>0) { 
 //BA.debugLineNum = 1082;BA.debugLine="For i = 0 To strs.Length() - 1";
{
final int step762 = 1;
final int limit762 = (int) (_strs.length()-1);
for (_i = (int) (0); (step762 > 0 && _i <= limit762) || (step762 < 0 && _i >= limit762); _i = ((int)(0 + _i + step762))) {
 //BA.debugLineNum = 1083;BA.debugLine="If strs.CharAt(i) == ch.CharAt(0) Then";
if (_strs.charAt(_i)==_ch.charAt((int) (0))) { 
 //BA.debugLineNum = 1084;BA.debugLine="If i <> 0 Or i <> strs.Length() -1 Then";
if (_i!=0 || _i!=_strs.length()-1) { 
 //BA.debugLineNum = 1085;BA.debugLine="returnInt = i";
_returnint = _i;
 };
 };
 }
};
 };
 };
 };
 //BA.debugLineNum = 1092;BA.debugLine="Return returnInt";
if (true) return _returnint;
 //BA.debugLineNum = 1093;BA.debugLine="End Sub";
return 0;
}
public static anywheresoftware.b4a.objects.PanelWrapper  _createpanel(int _paneltype,String _title) throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pan = null;
b4a.example.main._panelinfo _pio = null;
 //BA.debugLineNum = 269;BA.debugLine="Sub CreatePanel(PanelType As Int, Title As String)";
 //BA.debugLineNum = 270;BA.debugLine="Dim pan As Panel";
_pan = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 271;BA.debugLine="Dim pio As PanelInfo";
_pio = new b4a.example.main._panelinfo();
 //BA.debugLineNum = 273;BA.debugLine="pio.Initialize()";
_pio.Initialize();
 //BA.debugLineNum = 274;BA.debugLine="pio.LayoutLoaded = False";
_pio.LayoutLoaded = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 275;BA.debugLine="pio.PanelType = PanelType";
_pio.PanelType = _paneltype;
 //BA.debugLineNum = 277;BA.debugLine="pan.Initialize(\"\")";
_pan.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 279;BA.debugLine="pan.Tag = pio";
_pan.setTag((Object)(_pio));
 //BA.debugLineNum = 280;BA.debugLine="Return pan";
if (true) return _pan;
 //BA.debugLineNum = 281;BA.debugLine="End Sub";
return null;
}
public static String  _createtable() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursors = null;
String _strs = "";
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 544;BA.debugLine="Sub CreateTable";
 //BA.debugLineNum = 545;BA.debugLine="Dim cursors As Cursor";
_cursors = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 546;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 547;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 548;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 550;BA.debugLine="ltvCapture.Clear()";
mostCurrent._ltvcapture.Clear();
 //BA.debugLineNum = 551;BA.debugLine="cloumns = Array As Object(\"pigid\",\"ana_time\")";
_cloumns = new Object[]{(Object)("pigid"),(Object)("ana_time")};
 //BA.debugLineNum = 552;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 553;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 555;BA.debugLine="cursors = sql_capture.QueryColumnData(\"capture\",c";
_cursors = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"capture",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 557;BA.debugLine="For i = 0 To cursors.RowCount - 1";
{
final int step379 = 1;
final int limit379 = (int) (_cursors.getRowCount()-1);
for (_i = (int) (0); (step379 > 0 && _i <= limit379) || (step379 < 0 && _i >= limit379); _i = ((int)(0 + _i + step379))) {
 //BA.debugLineNum = 558;BA.debugLine="cursors.Position = i";
_cursors.setPosition(_i);
 //BA.debugLineNum = 559;BA.debugLine="strs = cursors.GetInt(cloumns(0))&\"   \"&cursors.";
_strs = BA.NumberToString(_cursors.GetInt(BA.ObjectToString(_cloumns[(int) (0)])))+"   "+_cursors.GetString(BA.ObjectToString(_cloumns[(int) (1)]));
 //BA.debugLineNum = 560;BA.debugLine="ltvCapture.AddSingleLine2(strs,strs)";
mostCurrent._ltvcapture.AddSingleLine2(_strs,(Object)(_strs));
 }
};
 //BA.debugLineNum = 562;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 563;BA.debugLine="End Sub";
return "";
}
public static String  _doubletostring(double _double1) throws Exception{
String _strs = "";
 //BA.debugLineNum = 1195;BA.debugLine="Sub doubleToString(double1 As Double) As String";
 //BA.debugLineNum = 1196;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 1197;BA.debugLine="strs = double1&\"\"";
_strs = BA.NumberToString(_double1)+"";
 //BA.debugLineNum = 1198;BA.debugLine="If double1 > 1 Then";
if (_double1>1) { 
 //BA.debugLineNum = 1199;BA.debugLine="strs = 1";
_strs = BA.NumberToString(1);
 }else {
 //BA.debugLineNum = 1201;BA.debugLine="If strs.Length() > 5 Then";
if (_strs.length()>5) { 
 //BA.debugLineNum = 1202;BA.debugLine="strs = strs.SubString2(0,5)";
_strs = _strs.substring((int) (0),(int) (5));
 };
 };
 //BA.debugLineNum = 1205;BA.debugLine="Return strs";
if (true) return _strs;
 //BA.debugLineNum = 1206;BA.debugLine="End Sub";
return "";
}
public static String  _excu_cmd_data() throws Exception{
 //BA.debugLineNum = 658;BA.debugLine="Sub excu_Cmd_Data";
 //BA.debugLineNum = 660;BA.debugLine="serial_Return_Data = 0";
_serial_return_data = (int) (0);
 //BA.debugLineNum = 661;BA.debugLine="End Sub";
return "";
}
public static String  _forceimmersicemode() throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 142;BA.debugLine="Sub ForceImmersiceMode";
 //BA.debugLineNum = 143;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 144;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 145;BA.debugLine="r.Target = r.RunMethod(\"getWindow\")";
_r.Target = _r.RunMethod("getWindow");
 //BA.debugLineNum = 146;BA.debugLine="r.Target = r.RunMethod(\"getDecorView\")";
_r.Target = _r.RunMethod("getDecorView");
 //BA.debugLineNum = 147;BA.debugLine="r.RunMethod2(\"setSystemUiVisibility\", 5894, \"java";
_r.RunMethod2("setSystemUiVisibility",BA.NumberToString(5894),"java.lang.int");
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
return "";
}
public static String  _fulltable() throws Exception{
String _strs = "";
int _i = 0;
 //BA.debugLineNum = 1022;BA.debugLine="Sub fullTable";
 //BA.debugLineNum = 1023;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 1024;BA.debugLine="If ltvTable_Ini.Size > 0 Then";
if (mostCurrent._ltvtable_ini.getSize()>0) { 
 //BA.debugLineNum = 1025;BA.debugLine="For i = 0 To ltvTable_Ini.Size";
{
final int step716 = 1;
final int limit716 = mostCurrent._ltvtable_ini.getSize();
for (_i = (int) (0); (step716 > 0 && _i <= limit716) || (step716 < 0 && _i >= limit716); _i = ((int)(0 + _i + step716))) {
 //BA.debugLineNum = 1026;BA.debugLine="ltvTable_Ini.Clear()";
mostCurrent._ltvtable_ini.Clear();
 }
};
 };
 //BA.debugLineNum = 1030;BA.debugLine="For i = 0 To itemMap.Size - 1";
{
final int step720 = 1;
final int limit720 = (int) (mostCurrent._itemmap.getSize()-1);
for (_i = (int) (0); (step720 > 0 && _i <= limit720) || (step720 < 0 && _i >= limit720); _i = ((int)(0 + _i + step720))) {
 //BA.debugLineNum = 1031;BA.debugLine="If i <> 0 Or itemMapKey <> Null Then";
if (_i!=0 || mostCurrent._itemmapkey!= null) { 
 }else {
 //BA.debugLineNum = 1033;BA.debugLine="lblView.Text = itemMap.GetValueAt(i)";
mostCurrent._lblview.setText(mostCurrent._itemmap.GetValueAt(_i));
 //BA.debugLineNum = 1034;BA.debugLine="itemMapKey = itemMap.GetKeyAt(i)";
mostCurrent._itemmapkey = BA.ObjectToString(mostCurrent._itemmap.GetKeyAt(_i));
 };
 //BA.debugLineNum = 1036;BA.debugLine="strs = itemMap.GetKeyAt(i) &\":\" &itemMap.GetValu";
_strs = BA.ObjectToString(mostCurrent._itemmap.GetKeyAt(_i))+":"+BA.ObjectToString(mostCurrent._itemmap.GetValueAt(_i));
 //BA.debugLineNum = 1037;BA.debugLine="ltvTable_Ini.AddSingleLine2(strs,strs)";
mostCurrent._ltvtable_ini.AddSingleLine2(_strs,(Object)(_strs));
 }
};
 //BA.debugLineNum = 1039;BA.debugLine="End	Sub";
return "";
}
public static String  _get_ana_capture_path() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 707;BA.debugLine="Sub get_ana_capture_path";
 //BA.debugLineNum = 708;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 709;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 710;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 712;BA.debugLine="cloumns = Array As Object(\"ana_path\")";
_cloumns = new Object[]{(Object)("ana_path")};
 //BA.debugLineNum = 713;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 714;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 715;BA.debugLine="ana_capture_path = \"\"";
mostCurrent._ana_capture_path = "";
 //BA.debugLineNum = 716;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"capture\",c";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"capture",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 717;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step481 = 1;
final int limit481 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step481 > 0 && _i <= limit481) || (step481 < 0 && _i >= limit481); _i = ((int)(0 + _i + step481))) {
 //BA.debugLineNum = 718;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 719;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 720;BA.debugLine="ana_capture_path = cursor1.GetString(cloumns(0)";
mostCurrent._ana_capture_path = _cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]));
 };
 }
};
 //BA.debugLineNum = 723;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 724;BA.debugLine="Show_Photo";
_show_photo();
 //BA.debugLineNum = 725;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 726;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 41;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 46;BA.debugLine="Dim container As AHPageContainer";
mostCurrent._container = new de.amberhome.viewpager.AHPageContainer();
 //BA.debugLineNum = 47;BA.debugLine="Dim pager As AHViewPager";
mostCurrent._pager = new de.amberhome.viewpager.AHViewPager();
 //BA.debugLineNum = 48;BA.debugLine="Dim tabs As AHViewPagerTabs";
mostCurrent._tabs = new de.amberhome.viewpager.AHViewPagerTabs();
 //BA.debugLineNum = 53;BA.debugLine="Private btnCapture As Button";
mostCurrent._btncapture = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private btnExit_Main As Button";
mostCurrent._btnexit_main = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private ltvCapture As ListView";
mostCurrent._ltvcapture = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private palShowRadio As Panel";
mostCurrent._palshowradio = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Dim usbSerial1 As SerialPort";
mostCurrent._usbserial1 = new android_serialport_api.SerialPort();
 //BA.debugLineNum = 59;BA.debugLine="Dim aSync As AsyncStreams";
mostCurrent._async = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 62;BA.debugLine="Dim camera1 As AdvancedCamera";
mostCurrent._camera1 = new xvs.ACL.ACL();
 //BA.debugLineNum = 64;BA.debugLine="Dim flag_Picture As Boolean";
_flag_picture = false;
 //BA.debugLineNum = 65;BA.debugLine="Dim flag_Task As Boolean = True";
_flag_task = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 68;BA.debugLine="Dim strPath As String";
mostCurrent._strpath = "";
 //BA.debugLineNum = 70;BA.debugLine="Dim photo_num As Int";
_photo_num = 0;
 //BA.debugLineNum = 72;BA.debugLine="Dim photo_Count As Int = 20";
_photo_count = (int) (20);
 //BA.debugLineNum = 74;BA.debugLine="Dim ana_time As String";
mostCurrent._ana_time = "";
 //BA.debugLineNum = 76;BA.debugLine="Dim ana_Point_Num As Int = 2";
_ana_point_num = (int) (2);
 //BA.debugLineNum = 78;BA.debugLine="Dim ana_Point_Num_Count As Int = 0";
_ana_point_num_count = (int) (0);
 //BA.debugLineNum = 80;BA.debugLine="Dim pigid_Main As Int = 10000";
_pigid_main = (int) (10000);
 //BA.debugLineNum = 83;BA.debugLine="Dim serial_Return_Data As Int = 0";
_serial_return_data = (int) (0);
 //BA.debugLineNum = 85;BA.debugLine="Dim SERIAL_PORT As String = \"/dev/ttyAMA2\"";
mostCurrent._serial_port = "/dev/ttyAMA2";
 //BA.debugLineNum = 86;BA.debugLine="Dim SERIAL_RATE As String = \"9600\"";
mostCurrent._serial_rate = "9600";
 //BA.debugLineNum = 87;BA.debugLine="Dim SERIAL_NUM As Int = 2";
_serial_num = (int) (2);
 //BA.debugLineNum = 92;BA.debugLine="Private btnAnay As Button";
mostCurrent._btnanay = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Private btnDrawContours As Button";
mostCurrent._btndrawcontours = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Private btnExit_Test As Button";
mostCurrent._btnexit_test = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 95;BA.debugLine="Private btnGaussianBlur As Button";
mostCurrent._btngaussianblur = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 96;BA.debugLine="Private btnStretch As Button";
mostCurrent._btnstretch = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 97;BA.debugLine="Private btnSubDraw As Button";
mostCurrent._btnsubdraw = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 98;BA.debugLine="Private igShow As ImageView";
mostCurrent._igshow = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 99;BA.debugLine="Private ltv_anay_point As ListView";
mostCurrent._ltv_anay_point = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 100;BA.debugLine="Private ltv_anay_time As ListView";
mostCurrent._ltv_anay_time = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 101;BA.debugLine="Private ltv_pigid As ListView";
mostCurrent._ltv_pigid = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 102;BA.debugLine="Private ltvTable_Test As ListView";
mostCurrent._ltvtable_test = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 103;BA.debugLine="Private lbl_Count As Label";
mostCurrent._lbl_count = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 104;BA.debugLine="Private lbl_Rate As Label";
mostCurrent._lbl_rate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 107;BA.debugLine="Dim isperm As IspermCpp";
mostCurrent._isperm = new com.example.ispermcpp.IspermCpp();
 //BA.debugLineNum = 109;BA.debugLine="Dim pigid As String";
mostCurrent._pigid = "";
 //BA.debugLineNum = 110;BA.debugLine="Dim ana_capture_point As String";
mostCurrent._ana_capture_point = "";
 //BA.debugLineNum = 111;BA.debugLine="Dim anay_time As String";
mostCurrent._anay_time = "";
 //BA.debugLineNum = 113;BA.debugLine="Dim ana_capture_path As String";
mostCurrent._ana_capture_path = "";
 //BA.debugLineNum = 114;BA.debugLine="Dim ana_history_path As String";
mostCurrent._ana_history_path = "";
 //BA.debugLineNum = 119;BA.debugLine="Private btnAddOne As Button";
mostCurrent._btnaddone = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 120;BA.debugLine="Private btnAddTen As Button";
mostCurrent._btnaddten = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 121;BA.debugLine="Private btnDelOne As Button";
mostCurrent._btndelone = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 122;BA.debugLine="Private btnDelTen As Button";
mostCurrent._btndelten = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 123;BA.debugLine="Private btnExit_Ini As Button";
mostCurrent._btnexit_ini = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 124;BA.debugLine="Private btnSave As Button";
mostCurrent._btnsave = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 125;BA.debugLine="Private lblView As Label";
mostCurrent._lblview = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 126;BA.debugLine="Private ltvTable_Ini As ListView";
mostCurrent._ltvtable_ini = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 129;BA.debugLine="Dim itemMap As Map";
mostCurrent._itemmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 130;BA.debugLine="Dim itemMapKey As String";
mostCurrent._itemmapkey = "";
 //BA.debugLineNum = 132;BA.debugLine="Dim itemMap_Begin As Map";
mostCurrent._itemmap_begin = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 134;BA.debugLine="Dim lbl_text_int As Int";
_lbl_text_int = 0;
 //BA.debugLineNum = 135;BA.debugLine="Dim lbl_text_double As Double";
_lbl_text_double = 0;
 //BA.debugLineNum = 138;BA.debugLine="Dim button_RGBA As Button";
mostCurrent._button_rgba = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
return "";
}
public static String  _inilistview_pigid() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 667;BA.debugLine="Sub IniListView_Pigid";
 //BA.debugLineNum = 668;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 669;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 670;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 672;BA.debugLine="cloumns = Array As Object(\"pigid\")";
_cloumns = new Object[]{(Object)("pigid")};
 //BA.debugLineNum = 673;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 674;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 675;BA.debugLine="pigid = \"\"";
mostCurrent._pigid = "";
 //BA.debugLineNum = 676;BA.debugLine="ltv_pigid.Clear()";
mostCurrent._ltv_pigid.Clear();
 //BA.debugLineNum = 680;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"capture\",c";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"capture",_cloumns,_params,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 681;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step454 = 1;
final int limit454 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step454 > 0 && _i <= limit454) || (step454 < 0 && _i >= limit454); _i = ((int)(0 + _i + step454))) {
 //BA.debugLineNum = 682;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 683;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 684;BA.debugLine="pigid = cursor1.GetInt(cloumns(0))";
mostCurrent._pigid = BA.NumberToString(_cursor1.GetInt(BA.ObjectToString(_cloumns[(int) (0)])));
 };
 //BA.debugLineNum = 686;BA.debugLine="ltv_pigid.AddSingleLine2(cursor1.GetInt(cloumns(";
mostCurrent._ltv_pigid.AddSingleLine2(BA.NumberToString(_cursor1.GetInt(BA.ObjectToString(_cloumns[(int) (0)]))),(Object)(_cursor1.GetInt(BA.ObjectToString(_cloumns[(int) (0)]))));
 }
};
 //BA.debugLineNum = 688;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 689;BA.debugLine="get_ana_capture_path";
_get_ana_capture_path();
 //BA.debugLineNum = 690;BA.debugLine="End Sub";
return "";
}
public static String  _inilistview_point() throws Exception{
int _i = 0;
 //BA.debugLineNum = 693;BA.debugLine="Sub IniListView_Point";
 //BA.debugLineNum = 694;BA.debugLine="ltv_anay_point.Clear()";
mostCurrent._ltv_anay_point.Clear();
 //BA.debugLineNum = 697;BA.debugLine="For i = 0 To 3";
{
final int step466 = 1;
final int limit466 = (int) (3);
for (_i = (int) (0); (step466 > 0 && _i <= limit466) || (step466 < 0 && _i >= limit466); _i = ((int)(0 + _i + step466))) {
 //BA.debugLineNum = 698;BA.debugLine="ltv_anay_point.AddSingleLine2(\"采集点\"&(i+1),i+1)";
mostCurrent._ltv_anay_point.AddSingleLine2("采集点"+BA.NumberToString((_i+1)),(Object)(_i+1));
 }
};
 //BA.debugLineNum = 701;BA.debugLine="ana_capture_point = 1";
mostCurrent._ana_capture_point = BA.NumberToString(1);
 //BA.debugLineNum = 703;BA.debugLine="get_ana_capture_path";
_get_ana_capture_path();
 //BA.debugLineNum = 704;BA.debugLine="End Sub";
return "";
}
public static String  _inilistview_time() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 729;BA.debugLine="Sub IniListView_Time";
 //BA.debugLineNum = 730;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 731;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 732;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 734;BA.debugLine="cloumns = Array As Object(\"anay_time\",\"ana_path\")";
_cloumns = new Object[]{(Object)("anay_time"),(Object)("ana_path")};
 //BA.debugLineNum = 735;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 736;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 737;BA.debugLine="anay_time = \"\"";
mostCurrent._anay_time = "";
 //BA.debugLineNum = 738;BA.debugLine="ana_history_path = \"\"";
mostCurrent._ana_history_path = "";
 //BA.debugLineNum = 739;BA.debugLine="ltv_anay_time.Clear()";
mostCurrent._ltv_anay_time.Clear();
 //BA.debugLineNum = 741;BA.debugLine="If pigid <> Null And pigid <> \"\" Then";
if (mostCurrent._pigid!= null && (mostCurrent._pigid).equals("") == false) { 
 //BA.debugLineNum = 742;BA.debugLine="params.Put(\"pigid\",pigid)";
_params.Put((Object)("pigid"),(Object)(mostCurrent._pigid));
 //BA.debugLineNum = 743;BA.debugLine="params.Put(\"ana_capture_point\",ana_capture_point";
_params.Put((Object)("ana_capture_point"),(Object)(mostCurrent._ana_capture_point));
 };
 //BA.debugLineNum = 747;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"resulthist";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"resulthistory",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 748;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step506 = 1;
final int limit506 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step506 > 0 && _i <= limit506) || (step506 < 0 && _i >= limit506); _i = ((int)(0 + _i + step506))) {
 //BA.debugLineNum = 749;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 750;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 751;BA.debugLine="anay_time = cursor1.GetString(cloumns(0))";
mostCurrent._anay_time = _cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]));
 //BA.debugLineNum = 752;BA.debugLine="ana_history_path = cursor1.GetString(cloumns(1)";
mostCurrent._ana_history_path = _cursor1.GetString(BA.ObjectToString(_cloumns[(int) (1)]));
 };
 //BA.debugLineNum = 754;BA.debugLine="ltv_anay_time.AddSingleLine2(cursor1.GetString(c";
mostCurrent._ltv_anay_time.AddSingleLine2(_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)])),(Object)(_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]))));
 }
};
 //BA.debugLineNum = 756;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 757;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_text_ltvtable() throws Exception{
 //BA.debugLineNum = 1214;BA.debugLine="Sub lbl_Text_ltvTable";
 //BA.debugLineNum = 1215;BA.debugLine="If ltvTable_Ini.Size > 0 Then";
if (mostCurrent._ltvtable_ini.getSize()>0) { 
 //BA.debugLineNum = 1216;BA.debugLine="values_Text(ltvTable_Ini.GetItem(0))";
_values_text(BA.ObjectToString(mostCurrent._ltvtable_ini.GetItem((int) (0))));
 };
 //BA.debugLineNum = 1218;BA.debugLine="End Sub";
return "";
}
public static String  _ltv_anay_point_itemclick(int _position,Object _value) throws Exception{
String _strs = "";
 //BA.debugLineNum = 976;BA.debugLine="Sub ltv_anay_point_ItemClick(Position As Int, Valu";
 //BA.debugLineNum = 977;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 979;BA.debugLine="strs = Value";
_strs = BA.ObjectToString(_value);
 //BA.debugLineNum = 980;BA.debugLine="If strs <> Null And strs <> \"\" Then";
if (_strs!= null && (_strs).equals("") == false) { 
 //BA.debugLineNum = 981;BA.debugLine="ana_capture_point = strs";
mostCurrent._ana_capture_point = _strs;
 //BA.debugLineNum = 982;BA.debugLine="IniListView_Time";
_inilistview_time();
 //BA.debugLineNum = 983;BA.debugLine="Show_Photo";
_show_photo();
 //BA.debugLineNum = 984;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 };
 //BA.debugLineNum = 986;BA.debugLine="End Sub";
return "";
}
public static String  _ltv_anay_time_itemclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor_path = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 988;BA.debugLine="Sub ltv_anay_time_ItemClick (Position As Int, Valu";
 //BA.debugLineNum = 989;BA.debugLine="Dim cursor_path As Cursor";
_cursor_path = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 990;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 991;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 993;BA.debugLine="cloumns = Array As Object(\"ana_path\")";
_cloumns = new Object[]{(Object)("ana_path")};
 //BA.debugLineNum = 994;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 995;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 996;BA.debugLine="ana_history_path = \"\"";
mostCurrent._ana_history_path = "";
 //BA.debugLineNum = 998;BA.debugLine="If pigid <> Null And pigid <> \"\" Then";
if (mostCurrent._pigid!= null && (mostCurrent._pigid).equals("") == false) { 
 //BA.debugLineNum = 999;BA.debugLine="params.Put(\"pigid\",pigid)";
_params.Put((Object)("pigid"),(Object)(mostCurrent._pigid));
 //BA.debugLineNum = 1000;BA.debugLine="params.Put(\"ana_capture_point\",ana_capture_point";
_params.Put((Object)("ana_capture_point"),(Object)(mostCurrent._ana_capture_point));
 //BA.debugLineNum = 1001;BA.debugLine="params.Put(\"anay_time\",Value)";
_params.Put((Object)("anay_time"),_value);
 };
 //BA.debugLineNum = 1003;BA.debugLine="anay_time = Value";
mostCurrent._anay_time = BA.ObjectToString(_value);
 //BA.debugLineNum = 1005;BA.debugLine="cursor_path = sql_capture.QueryColumnData(\"result";
_cursor_path = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"resulthistory",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1006;BA.debugLine="For i = 0 To cursor_path.RowCount - 1";
{
final int step704 = 1;
final int limit704 = (int) (_cursor_path.getRowCount()-1);
for (_i = (int) (0); (step704 > 0 && _i <= limit704) || (step704 < 0 && _i >= limit704); _i = ((int)(0 + _i + step704))) {
 //BA.debugLineNum = 1007;BA.debugLine="cursor_path.Position = i";
_cursor_path.setPosition(_i);
 //BA.debugLineNum = 1008;BA.debugLine="ana_history_path = cursor_path.GetString(cloumns";
mostCurrent._ana_history_path = _cursor_path.GetString(BA.ObjectToString(_cloumns[(int) (0)]));
 }
};
 //BA.debugLineNum = 1010;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 1011;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 1012;BA.debugLine="End Sub";
return "";
}
public static String  _ltv_pigid_itemclick(int _position,Object _value) throws Exception{
String _strs = "";
 //BA.debugLineNum = 964;BA.debugLine="Sub ltv_pigid_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 965;BA.debugLine="Dim strs As String";
_strs = "";
 //BA.debugLineNum = 967;BA.debugLine="strs = Value";
_strs = BA.ObjectToString(_value);
 //BA.debugLineNum = 968;BA.debugLine="pigid = strs.Trim()";
mostCurrent._pigid = _strs.trim();
 //BA.debugLineNum = 970;BA.debugLine="IniListView_Point";
_inilistview_point();
 //BA.debugLineNum = 971;BA.debugLine="IniListView_Time";
_inilistview_time();
 //BA.debugLineNum = 972;BA.debugLine="Show_Photo";
_show_photo();
 //BA.debugLineNum = 973;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 //BA.debugLineNum = 974;BA.debugLine="End Sub";
return "";
}
public static String  _ltvtable_ini_itemclick(int _position,String _value) throws Exception{
 //BA.debugLineNum = 1209;BA.debugLine="Sub ltvTable_Ini_ItemClick (Position As Int, value";
 //BA.debugLineNum = 1210;BA.debugLine="values_Text(value)";
_values_text(_value);
 //BA.debugLineNum = 1211;BA.debugLine="End Sub";
return "";
}
public static String  _ltvtable_test_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1014;BA.debugLine="Sub ltvTable_Test_ItemClick (Position As Int, Valu";
 //BA.debugLineNum = 1016;BA.debugLine="End Sub";
return "";
}
public static String  _mapupdate() throws Exception{
int _i = 0;
 //BA.debugLineNum = 1042;BA.debugLine="Sub mapUpdate";
 //BA.debugLineNum = 1043;BA.debugLine="For i = 0 To itemMap.Size -1";
{
final int step731 = 1;
final int limit731 = (int) (mostCurrent._itemmap.getSize()-1);
for (_i = (int) (0); (step731 > 0 && _i <= limit731) || (step731 < 0 && _i >= limit731); _i = ((int)(0 + _i + step731))) {
 //BA.debugLineNum = 1044;BA.debugLine="If itemMap.GetKeyAt(i) == itemMapKey Then";
if ((mostCurrent._itemmap.GetKeyAt(_i)).equals((Object)(mostCurrent._itemmapkey))) { 
 //BA.debugLineNum = 1046;BA.debugLine="itemMap.Put(itemMapKey,lblView.Text)";
mostCurrent._itemmap.Put((Object)(mostCurrent._itemmapkey),(Object)(mostCurrent._lblview.getText()));
 };
 }
};
 //BA.debugLineNum = 1049;BA.debugLine="End Sub";
return "";
}
public static String  _pager_pagechanged(int _position) throws Exception{
 //BA.debugLineNum = 284;BA.debugLine="Sub Pager_PageChanged (Position As Int)";
 //BA.debugLineNum = 285;BA.debugLine="Log (\"Page Changed to \" & Position)";
anywheresoftware.b4a.keywords.Common.Log("Page Changed to "+BA.NumberToString(_position));
 //BA.debugLineNum = 287;BA.debugLine="CurrentPage = Position";
_currentpage = _position;
 //BA.debugLineNum = 289;BA.debugLine="If CurrentPage == 0 Then";
if (_currentpage==0) { 
 //BA.debugLineNum = 292;BA.debugLine="camera1.StartPreview()";
mostCurrent._camera1.StartPreview();
 //BA.debugLineNum = 293;BA.debugLine="CreateTable";
_createtable();
 }else if(_currentpage==1) { 
 //BA.debugLineNum = 295;BA.debugLine="camera1.StopPreview()";
mostCurrent._camera1.StopPreview();
 //BA.debugLineNum = 296;BA.debugLine="If taskTime.IsInitialized() Then";
if (_tasktime.IsInitialized()) { 
 //BA.debugLineNum = 297;BA.debugLine="taskTime.Enabled = False";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 299;BA.debugLine="If taskTime2.IsInitialized() Then";
if (_tasktime2.IsInitialized()) { 
 //BA.debugLineNum = 300;BA.debugLine="taskTime2.Enabled = False";
_tasktime2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 302;BA.debugLine="IniListView_Pigid";
_inilistview_pigid();
 //BA.debugLineNum = 303;BA.debugLine="IniListView_Point";
_inilistview_point();
 //BA.debugLineNum = 304;BA.debugLine="IniListView_Time";
_inilistview_time();
 //BA.debugLineNum = 305;BA.debugLine="Show_Photo";
_show_photo();
 //BA.debugLineNum = 306;BA.debugLine="Show_LtvTable";
_show_ltvtable();
 }else if(_currentpage==2) { 
 //BA.debugLineNum = 309;BA.debugLine="camera1.StopPreview()";
mostCurrent._camera1.StopPreview();
 //BA.debugLineNum = 310;BA.debugLine="read_Table_Dict";
_read_table_dict();
 //BA.debugLineNum = 311;BA.debugLine="fullTable";
_fulltable();
 //BA.debugLineNum = 312;BA.debugLine="lbl_Text_ltvTable";
_lbl_text_ltvtable();
 };
 //BA.debugLineNum = 314;BA.debugLine="End Sub";
return "";
}
public static String  _pager_pagecreated(int _position,Object _page) throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pan = null;
b4a.example.main._panelinfo _pi = null;
 //BA.debugLineNum = 317;BA.debugLine="Sub Pager_PageCreated (Position As Int, Page As Ob";
 //BA.debugLineNum = 318;BA.debugLine="Log (\"Page created \" & Position)";
anywheresoftware.b4a.keywords.Common.Log("Page created "+BA.NumberToString(_position));
 //BA.debugLineNum = 320;BA.debugLine="Dim pan As Panel";
_pan = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 321;BA.debugLine="Dim pi As PanelInfo";
_pi = new b4a.example.main._panelinfo();
 //BA.debugLineNum = 323;BA.debugLine="pan = Page";
_pan.setObject((android.view.ViewGroup)(_page));
 //BA.debugLineNum = 324;BA.debugLine="pi = pan.Tag";
_pi = (b4a.example.main._panelinfo)(_pan.getTag());
 //BA.debugLineNum = 326;BA.debugLine="Select pi.PanelType";
switch (BA.switchObjectToInt(_pi.PanelType,_type_isperm_capture,_type_isperm_test,_type_isperm_ini)) {
case 0:
 //BA.debugLineNum = 328;BA.debugLine="If Not(pi.LayoutLoaded) Then";
if (anywheresoftware.b4a.keywords.Common.Not(_pi.LayoutLoaded)) { 
 //BA.debugLineNum = 329;BA.debugLine="pan.LoadLayout(\"mainwindow\")";
_pan.LoadLayout("mainwindow",mostCurrent.activityBA);
 //BA.debugLineNum = 330;BA.debugLine="pi.LayoutLoaded = True";
_pi.LayoutLoaded = anywheresoftware.b4a.keywords.Common.True;
 };
 break;
case 1:
 //BA.debugLineNum = 333;BA.debugLine="If Not(pi.LayoutLoaded) Then";
if (anywheresoftware.b4a.keywords.Common.Not(_pi.LayoutLoaded)) { 
 //BA.debugLineNum = 334;BA.debugLine="pan.LoadLayout(\"testwindow\")";
_pan.LoadLayout("testwindow",mostCurrent.activityBA);
 //BA.debugLineNum = 335;BA.debugLine="pi.LayoutLoaded = True";
_pi.LayoutLoaded = anywheresoftware.b4a.keywords.Common.True;
 };
 break;
case 2:
 //BA.debugLineNum = 338;BA.debugLine="If Not(pi.LayoutLoaded) Then";
if (anywheresoftware.b4a.keywords.Common.Not(_pi.LayoutLoaded)) { 
 //BA.debugLineNum = 339;BA.debugLine="pan.LoadLayout(\"iniwindow\")";
_pan.LoadLayout("iniwindow",mostCurrent.activityBA);
 //BA.debugLineNum = 340;BA.debugLine="pi.LayoutLoaded = True";
_pi.LayoutLoaded = anywheresoftware.b4a.keywords.Common.True;
 };
 break;
}
;
 //BA.debugLineNum = 343;BA.debugLine="End Sub";
return "";
}
public static String  _photo_point() throws Exception{
 //BA.debugLineNum = 453;BA.debugLine="Sub photo_Point";
 //BA.debugLineNum = 455;BA.debugLine="If ana_Point_Num_Count == 0 Then";
if (_ana_point_num_count==0) { 
 //BA.debugLineNum = 456;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 457;BA.debugLine="pigid_Main = sql_capture.QueryIDMax(\"capture\",\"p";
_pigid_main = (int)(Double.parseDouble(mostCurrent._sql_capture._queryidmax(mostCurrent.activityBA,"capture","pigid")));
 //BA.debugLineNum = 458;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 460;BA.debugLine="photo_Save(pigid_Main,ana_Point_Num_Count + 1)";
_photo_save(_pigid_main,(int) (_ana_point_num_count+1));
 };
 //BA.debugLineNum = 463;BA.debugLine="If ana_Point_Num_Count == 1 Then";
if (_ana_point_num_count==1) { 
 //BA.debugLineNum = 466;BA.debugLine="photo_Save(pigid_Main,ana_Point_Num_Count + 1)";
_photo_save(_pigid_main,(int) (_ana_point_num_count+1));
 };
 //BA.debugLineNum = 469;BA.debugLine="If ana_Point_Num_Count == 2 Then";
if (_ana_point_num_count==2) { 
 //BA.debugLineNum = 472;BA.debugLine="photo_Save(pigid_Main,ana_Point_Num_Count + 1)";
_photo_save(_pigid_main,(int) (_ana_point_num_count+1));
 };
 //BA.debugLineNum = 475;BA.debugLine="If ana_Point_Num_Count == 3 Then";
if (_ana_point_num_count==3) { 
 //BA.debugLineNum = 478;BA.debugLine="photo_Save(pigid_Main,ana_Point_Num_Count + 1)";
_photo_save(_pigid_main,(int) (_ana_point_num_count+1));
 };
 //BA.debugLineNum = 480;BA.debugLine="End Sub";
return "";
}
public static String  _photo_save(int _pigid_save,int _i_params) throws Exception{
int _id = 0;
long _timenow = 0L;
Object[] _params = null;
 //BA.debugLineNum = 512;BA.debugLine="Sub photo_Save(pigid_Save As Int,i_params As Int)";
 //BA.debugLineNum = 513;BA.debugLine="Dim id As Int";
_id = 0;
 //BA.debugLineNum = 515;BA.debugLine="Dim timeNow As Long";
_timenow = 0L;
 //BA.debugLineNum = 516;BA.debugLine="Dim params() As Object";
_params = new Object[(int) (0)];
{
int d0 = _params.length;
for (int i0 = 0;i0 < d0;i0++) {
_params[i0] = new Object();
}
}
;
 //BA.debugLineNum = 518;BA.debugLine="photo_num = 0";
_photo_num = (int) (0);
 //BA.debugLineNum = 519;BA.debugLine="flag_Picture = True";
_flag_picture = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 521;BA.debugLine="timeNow = DateTime.Now";
_timenow = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 522;BA.debugLine="strPath = create_File.Create_Dir(timeNow)";
mostCurrent._strpath = mostCurrent._create_file._create_dir(mostCurrent.activityBA,_timenow);
 //BA.debugLineNum = 523;BA.debugLine="ana_time = create_File.TimeFormat(timeNow)";
mostCurrent._ana_time = mostCurrent._create_file._timeformat(mostCurrent.activityBA,_timenow);
 //BA.debugLineNum = 524;BA.debugLine="taskTime.Enabled = True";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 526;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 527;BA.debugLine="id = sql_capture.QueryIDMax(\"capture\",\"id\")";
_id = (int)(Double.parseDouble(mostCurrent._sql_capture._queryidmax(mostCurrent.activityBA,"capture","id")));
 //BA.debugLineNum = 528;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 529;BA.debugLine="params = Array As Object(id+1,pigid_Save+1,ana_ti";
_params = new Object[]{(Object)(_id+1),(Object)(_pigid_save+1),(Object)(mostCurrent._ana_time),(Object)(_i_params),(Object)(mostCurrent._strpath),(Object)(0),(Object)(0),(Object)(0),(Object)(1)};
 //BA.debugLineNum = 530;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 531;BA.debugLine="sql_capture.InsertToCapture(\"capture\",params)";
mostCurrent._sql_capture._inserttocapture(mostCurrent.activityBA,"capture",_params);
 //BA.debugLineNum = 532;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 533;BA.debugLine="CreateTable";
_createtable();
 //BA.debugLineNum = 534;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
create_file._process_globals();
sql_capture._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 22;BA.debugLine="Dim TYPE_ISPERM_CAPTURE As Int : TYPE_ISPERM_CAPT";
_type_isperm_capture = 0;
 //BA.debugLineNum = 22;BA.debugLine="Dim TYPE_ISPERM_CAPTURE As Int : TYPE_ISPERM_CAPT";
_type_isperm_capture = (int) (1);
 //BA.debugLineNum = 23;BA.debugLine="Dim TYPE_ISPERM_TEST As Int : TYPE_ISPERM_TEST =";
_type_isperm_test = 0;
 //BA.debugLineNum = 23;BA.debugLine="Dim TYPE_ISPERM_TEST As Int : TYPE_ISPERM_TEST =";
_type_isperm_test = (int) (2);
 //BA.debugLineNum = 24;BA.debugLine="Dim TYPE_ISPERM_INI As Int : TYPE_ISPERM_INI = 3";
_type_isperm_ini = 0;
 //BA.debugLineNum = 24;BA.debugLine="Dim TYPE_ISPERM_INI As Int : TYPE_ISPERM_INI = 3";
_type_isperm_ini = (int) (3);
 //BA.debugLineNum = 26;BA.debugLine="Dim FILL_PARENT As Int : FILL_PARENT = -1";
_fill_parent = 0;
 //BA.debugLineNum = 26;BA.debugLine="Dim FILL_PARENT As Int : FILL_PARENT = -1";
_fill_parent = (int) (-1);
 //BA.debugLineNum = 27;BA.debugLine="Dim WRAP_CONTENT As Int : WRAP_CONTENT = -2";
_wrap_content = 0;
 //BA.debugLineNum = 27;BA.debugLine="Dim WRAP_CONTENT As Int : WRAP_CONTENT = -2";
_wrap_content = (int) (-2);
 //BA.debugLineNum = 29;BA.debugLine="Type PanelInfo (PanelType As Int, LayoutLoaded As";
;
 //BA.debugLineNum = 31;BA.debugLine="Dim CurrentPage As Int : CurrentPage = 0";
_currentpage = 0;
 //BA.debugLineNum = 31;BA.debugLine="Dim CurrentPage As Int : CurrentPage = 0";
_currentpage = (int) (0);
 //BA.debugLineNum = 36;BA.debugLine="Dim taskTime As Timer";
_tasktime = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 37;BA.debugLine="Dim taskTime2 As Timer";
_tasktime2 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _read_table_dict() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursors = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 1052;BA.debugLine="Sub read_Table_Dict";
 //BA.debugLineNum = 1053;BA.debugLine="Dim cursors As Cursor";
_cursors = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 1054;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 1055;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1057;BA.debugLine="itemMap.Initialize()";
mostCurrent._itemmap.Initialize();
 //BA.debugLineNum = 1058;BA.debugLine="itemMap_Begin.Initialize()";
mostCurrent._itemmap_begin.Initialize();
 //BA.debugLineNum = 1059;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 1060;BA.debugLine="cloumns = Array As Object(\"name\",\"value\")";
_cloumns = new Object[]{(Object)("name"),(Object)("value")};
 //BA.debugLineNum = 1062;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 1063;BA.debugLine="cursors = sql_capture.QueryColumnData(\"dict_prope";
_cursors = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"dict_properties",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1065;BA.debugLine="For i = 0 To cursors.RowCount - 1";
{
final int step747 = 1;
final int limit747 = (int) (_cursors.getRowCount()-1);
for (_i = (int) (0); (step747 > 0 && _i <= limit747) || (step747 < 0 && _i >= limit747); _i = ((int)(0 + _i + step747))) {
 //BA.debugLineNum = 1066;BA.debugLine="cursors.Position = i";
_cursors.setPosition(_i);
 //BA.debugLineNum = 1067;BA.debugLine="itemMap.put(cursors.GetString(cloumns(0)),cursor";
mostCurrent._itemmap.Put((Object)(_cursors.GetString(BA.ObjectToString(_cloumns[(int) (0)]))),(Object)(_cursors.GetString(BA.ObjectToString(_cloumns[(int) (1)]))));
 //BA.debugLineNum = 1068;BA.debugLine="itemMap_Begin.put(cursors.GetString(cloumns(0)),";
mostCurrent._itemmap_begin.Put((Object)(_cursors.GetString(BA.ObjectToString(_cloumns[(int) (0)]))),(Object)(_cursors.GetString(BA.ObjectToString(_cloumns[(int) (1)]))));
 }
};
 //BA.debugLineNum = 1070;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 1071;BA.debugLine="End Sub";
return "";
}
public static String  _recevie_cmd(String _cmd_recevie) throws Exception{
 //BA.debugLineNum = 583;BA.debugLine="Sub recevie_Cmd(cmd_Recevie As String)";
 //BA.debugLineNum = 584;BA.debugLine="Select cmd_Recevie.SubString2(10,16)";
switch (BA.switchObjectToInt(_cmd_recevie.substring((int) (10),(int) (16)),"03434f","030102","030201","030202","030203","030204","030205","030301","030302","040302","040303","040401","040402","030502","040501")) {
case 0:
 //BA.debugLineNum = 587;BA.debugLine="serial_Return_Data = 1";
_serial_return_data = (int) (1);
 break;
case 1:
 //BA.debugLineNum = 590;BA.debugLine="serial_Return_Data = 2";
_serial_return_data = (int) (2);
 break;
case 2:
case 3:
case 4:
case 5:
case 6:
 //BA.debugLineNum = 593;BA.debugLine="serial_Return_Data = 3";
_serial_return_data = (int) (3);
 break;
case 7:
 //BA.debugLineNum = 597;BA.debugLine="If cmd_Recevie.SubString2(18,20) == \"00\" Then";
if ((_cmd_recevie.substring((int) (18),(int) (20))).equals("00")) { 
 //BA.debugLineNum = 598;BA.debugLine="serial_Return_Data = 4";
_serial_return_data = (int) (4);
 };
 //BA.debugLineNum = 601;BA.debugLine="serial_Return_Data = 5";
_serial_return_data = (int) (5);
 break;
case 8:
 //BA.debugLineNum = 604;BA.debugLine="serial_Return_Data = 6";
_serial_return_data = (int) (6);
 break;
case 9:
 //BA.debugLineNum = 607;BA.debugLine="serial_Return_Data = 7";
_serial_return_data = (int) (7);
 break;
case 10:
 //BA.debugLineNum = 610;BA.debugLine="serial_Return_Data = 8";
_serial_return_data = (int) (8);
 break;
case 11:
 //BA.debugLineNum = 613;BA.debugLine="serial_Return_Data = 9";
_serial_return_data = (int) (9);
 break;
case 12:
 //BA.debugLineNum = 616;BA.debugLine="serial_Return_Data = 10";
_serial_return_data = (int) (10);
 break;
case 13:
 //BA.debugLineNum = 619;BA.debugLine="serial_Return_Data = 11";
_serial_return_data = (int) (11);
 break;
case 14:
 //BA.debugLineNum = 622;BA.debugLine="serial_Return_Data = 12";
_serial_return_data = (int) (12);
 break;
default:
 //BA.debugLineNum = 625;BA.debugLine="serial_Return_Data = 13";
_serial_return_data = (int) (13);
 break;
}
;
 //BA.debugLineNum = 627;BA.debugLine="End Sub";
return "";
}
public static String  _send_cmd(String _cmd) throws Exception{
byte[] _bytearray = null;
anywheresoftware.b4a.agraham.byteconverter.ByteConverter _astr = null;
 //BA.debugLineNum = 566;BA.debugLine="Sub send_Cmd(cmd As String)";
 //BA.debugLineNum = 567;BA.debugLine="Dim byteArray(0) As Byte";
_bytearray = new byte[(int) (0)];
;
 //BA.debugLineNum = 568;BA.debugLine="Dim aStr As ByteConverter";
_astr = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 571;BA.debugLine="If cmd.Length < 22 Then";
if (_cmd.length()<22) { 
 //BA.debugLineNum = 572;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 575;BA.debugLine="byteArray = aStr.HexToBytes(cmd)";
_bytearray = _astr.HexToBytes(_cmd);
 //BA.debugLineNum = 576;BA.debugLine="aSync.Write(byteArray)";
mostCurrent._async.Write(_bytearray);
 //BA.debugLineNum = 580;BA.debugLine="End Sub";
return "";
}
public static String  _show_anay_photo(String _picture_type) throws Exception{
 //BA.debugLineNum = 792;BA.debugLine="Sub Show_anay_Photo(picture_Type As String)";
 //BA.debugLineNum = 793;BA.debugLine="If ana_history_path <> Null And ana_history_path";
if (mostCurrent._ana_history_path!= null && (mostCurrent._ana_history_path).equals("") == false) { 
 //BA.debugLineNum = 794;BA.debugLine="If File.Exists(File.DirRootExternal, ana_history";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._ana_history_path+"/"+_picture_type+".jpg")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 795;BA.debugLine="igShow.Bitmap = LoadBitmapSample(File.DirRootEx";
mostCurrent._igshow.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._ana_history_path+"/"+_picture_type+".jpg",(int) (360),(int) (480)).getObject()));
 };
 };
 //BA.debugLineNum = 798;BA.debugLine="End Sub";
return "";
}
public static String  _show_count_active() throws Exception{
 //BA.debugLineNum = 876;BA.debugLine="Sub Show_Count_Active";
 //BA.debugLineNum = 877;BA.debugLine="End Sub";
return "";
}
public static String  _show_ltvtable() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
int _j = 0;
 //BA.debugLineNum = 832;BA.debugLine="Sub Show_LtvTable";
 //BA.debugLineNum = 833;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 834;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 835;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 837;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 838;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 839;BA.debugLine="ltvTable_Test.Clear()";
mostCurrent._ltvtable_test.Clear();
 //BA.debugLineNum = 841;BA.debugLine="If anay_time <> Null And anay_time <> \"\" Then";
if (mostCurrent._anay_time!= null && (mostCurrent._anay_time).equals("") == false) { 
 //BA.debugLineNum = 842;BA.debugLine="params.Put(\"pigid\",pigid)";
_params.Put((Object)("pigid"),(Object)(mostCurrent._pigid));
 //BA.debugLineNum = 843;BA.debugLine="params.Put(\"ana_capture_point\",ana_capture_point";
_params.Put((Object)("ana_capture_point"),(Object)(mostCurrent._ana_capture_point));
 //BA.debugLineNum = 844;BA.debugLine="params.Put(\"anay_time\",anay_time)";
_params.Put((Object)("anay_time"),(Object)(mostCurrent._anay_time));
 //BA.debugLineNum = 845;BA.debugLine="cloumns = Array As Object(\"ana_sperm_count\",\"ana";
_cloumns = new Object[]{(Object)("ana_sperm_count"),(Object)("ana_sperm_active"),(Object)("videocalw"),(Object)("videocalh"),(Object)("videoshoww"),(Object)("videoshowh"),(Object)("initialthresh"),(Object)("threshsub"),(Object)("minarea"),(Object)("maxarea"),(Object)("ratio")};
 }else {
 //BA.debugLineNum = 848;BA.debugLine="cloumns = Array As Object(\"name\",\"value\")";
_cloumns = new Object[]{(Object)("name"),(Object)("value")};
 //BA.debugLineNum = 849;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"dict_prop";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"dict_properties",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 850;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step583 = 1;
final int limit583 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step583 > 0 && _i <= limit583) || (step583 < 0 && _i >= limit583); _i = ((int)(0 + _i + step583))) {
 //BA.debugLineNum = 851;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 852;BA.debugLine="If i < 9 Then";
if (_i<9) { 
 //BA.debugLineNum = 853;BA.debugLine="ltvTable_Test.AddSingleLine2(cursor1.GetString";
mostCurrent._ltvtable_test.AddSingleLine2(_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]))+":"+_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (1)])),(Object)(_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (1)]))));
 };
 }
};
 //BA.debugLineNum = 856;BA.debugLine="lbl_Count.Text = \"总数:\"";
mostCurrent._lbl_count.setText((Object)("总数:"));
 //BA.debugLineNum = 857;BA.debugLine="lbl_Rate.Text = \"活跃数:\"";
mostCurrent._lbl_rate.setText((Object)("活跃数:"));
 //BA.debugLineNum = 858;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 860;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 861;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"resulthist";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"resulthistory",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 862;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step595 = 1;
final int limit595 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step595 > 0 && _i <= limit595) || (step595 < 0 && _i >= limit595); _i = ((int)(0 + _i + step595))) {
 //BA.debugLineNum = 863;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 864;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 865;BA.debugLine="For j = 2 To cloumns.Length - 1";
{
final int step598 = 1;
final int limit598 = (int) (_cloumns.length-1);
for (_j = (int) (2); (step598 > 0 && _j <= limit598) || (step598 < 0 && _j >= limit598); _j = ((int)(0 + _j + step598))) {
 //BA.debugLineNum = 866;BA.debugLine="ltvTable_Test.AddSingleLine2(cloumns(j)&\":\"&cu";
mostCurrent._ltvtable_test.AddSingleLine2(BA.ObjectToString(_cloumns[_j])+":"+_cursor1.GetString(BA.ObjectToString(_cloumns[_j])),(Object)(_cursor1.GetString(BA.ObjectToString(_cloumns[_j]))));
 }
};
 //BA.debugLineNum = 868;BA.debugLine="lbl_Count.Text = \"总数:\"&cursor1.GetString(cloumn";
mostCurrent._lbl_count.setText((Object)("总数:"+_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]))));
 //BA.debugLineNum = 869;BA.debugLine="lbl_Rate.Text = \"活跃数:\"&cursor1.GetString(cloumn";
mostCurrent._lbl_rate.setText((Object)("活跃数:"+_cursor1.GetString(BA.ObjectToString(_cloumns[(int) (1)]))));
 };
 }
};
 //BA.debugLineNum = 872;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 873;BA.debugLine="End Sub";
return "";
}
public static String  _show_photo() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
Object[] _cloumns = null;
anywheresoftware.b4a.objects.collections.Map _params = null;
int _i = 0;
 //BA.debugLineNum = 760;BA.debugLine="Sub Show_Photo";
 //BA.debugLineNum = 761;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 762;BA.debugLine="Dim cloumns() As Object";
_cloumns = new Object[(int) (0)];
{
int d0 = _cloumns.length;
for (int i0 = 0;i0 < d0;i0++) {
_cloumns[i0] = new Object();
}
}
;
 //BA.debugLineNum = 763;BA.debugLine="Dim params As Map";
_params = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 765;BA.debugLine="cloumns = Array As Object(\"ana_path\")";
_cloumns = new Object[]{(Object)("ana_path")};
 //BA.debugLineNum = 766;BA.debugLine="params.Initialize()";
_params.Initialize();
 //BA.debugLineNum = 767;BA.debugLine="sql_capture.FlagIni()";
mostCurrent._sql_capture._flagini(mostCurrent.activityBA);
 //BA.debugLineNum = 768;BA.debugLine="ana_capture_path = \"\"";
mostCurrent._ana_capture_path = "";
 //BA.debugLineNum = 770;BA.debugLine="If pigid <> Null And pigid <> \"\" Then";
if (mostCurrent._pigid!= null && (mostCurrent._pigid).equals("") == false) { 
 //BA.debugLineNum = 771;BA.debugLine="params.Put(\"pigid\",pigid)";
_params.Put((Object)("pigid"),(Object)(mostCurrent._pigid));
 //BA.debugLineNum = 772;BA.debugLine="params.Put(\"ana_capture_point\",ana_capture_point";
_params.Put((Object)("ana_capture_point"),(Object)(mostCurrent._ana_capture_point));
 };
 //BA.debugLineNum = 776;BA.debugLine="cursor1 = sql_capture.QueryColumnData(\"capture\",c";
_cursor1 = mostCurrent._sql_capture._querycolumndata(mostCurrent.activityBA,"capture",_cloumns,_params,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 777;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step529 = 1;
final int limit529 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0); (step529 > 0 && _i <= limit529) || (step529 < 0 && _i >= limit529); _i = ((int)(0 + _i + step529))) {
 //BA.debugLineNum = 778;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 779;BA.debugLine="If i == 0 Then";
if (_i==0) { 
 //BA.debugLineNum = 780;BA.debugLine="ana_capture_path = cursor1.GetString(cloumns(0)";
mostCurrent._ana_capture_path = _cursor1.GetString(BA.ObjectToString(_cloumns[(int) (0)]));
 };
 }
};
 //BA.debugLineNum = 783;BA.debugLine="sql_capture.CloseSQL(\"pigpet.db\")";
mostCurrent._sql_capture._closesql(mostCurrent.activityBA,"pigpet.db");
 //BA.debugLineNum = 784;BA.debugLine="If ana_capture_path <> Null And ana_capture_path";
if (mostCurrent._ana_capture_path!= null && (mostCurrent._ana_capture_path).equals("") == false) { 
 //BA.debugLineNum = 785;BA.debugLine="If File.Exists(File.DirRootExternal, ana_capture";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._ana_capture_path+"/"+"000.jpg")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 786;BA.debugLine="igShow.Bitmap = LoadBitmapSample(File.DirRootEx";
mostCurrent._igshow.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),mostCurrent._ana_capture_path+"/000.jpg",(int) (360),(int) (480)).getObject()));
 };
 };
 //BA.debugLineNum = 789;BA.debugLine="End Sub";
return "";
}
public static String  _tasktime_tick() throws Exception{
 //BA.debugLineNum = 431;BA.debugLine="Sub taskTime_Tick";
 //BA.debugLineNum = 432;BA.debugLine="If flag_Picture Then";
if (_flag_picture) { 
 //BA.debugLineNum = 433;BA.debugLine="flag_Picture = False";
_flag_picture = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 434;BA.debugLine="camera1.TakePicture()";
mostCurrent._camera1.TakePicture();
 };
 //BA.debugLineNum = 436;BA.debugLine="End Sub";
return "";
}
public static String  _tasktime2_tick() throws Exception{
 //BA.debugLineNum = 438;BA.debugLine="Sub taskTime2_Tick";
 //BA.debugLineNum = 440;BA.debugLine="If ana_Point_Num_Count == ana_Point_Num Then";
if (_ana_point_num_count==_ana_point_num) { 
 //BA.debugLineNum = 441;BA.debugLine="taskTime2.Enabled = False";
_tasktime2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 442;BA.debugLine="btnEnabled(True)";
_btnenabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 443;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 446;BA.debugLine="If flag_Task Then";
if (_flag_task) { 
 //BA.debugLineNum = 447;BA.debugLine="flag_Task = False";
_flag_task = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 448;BA.debugLine="photo_Point";
_photo_point();
 };
 //BA.debugLineNum = 450;BA.debugLine="End Sub";
return "";
}
public static String  _values_text(String _value) throws Exception{
int _flagint = 0;
 //BA.debugLineNum = 1221;BA.debugLine="Sub values_Text(value As String)";
 //BA.debugLineNum = 1222;BA.debugLine="Dim flagInt As Int";
_flagint = 0;
 //BA.debugLineNum = 1223;BA.debugLine="If value <> Null And value <> \"\" Then";
if (_value!= null && (_value).equals("") == false) { 
 //BA.debugLineNum = 1224;BA.debugLine="flagInt = charInString(\":\",value)";
_flagint = _charinstring(":",_value);
 //BA.debugLineNum = 1225;BA.debugLine="value = value.Trim()";
_value = _value.trim();
 //BA.debugLineNum = 1226;BA.debugLine="If flagInt <> -1 Then";
if (_flagint!=-1) { 
 //BA.debugLineNum = 1228;BA.debugLine="lblView.Text = value.SubString(flagInt+1)";
mostCurrent._lblview.setText((Object)(_value.substring((int) (_flagint+1))));
 //BA.debugLineNum = 1229;BA.debugLine="itemMapKey = value.SubString2(0,flagInt)";
mostCurrent._itemmapkey = _value.substring((int) (0),_flagint);
 };
 };
 //BA.debugLineNum = 1232;BA.debugLine="End Sub";
return "";
}
}
