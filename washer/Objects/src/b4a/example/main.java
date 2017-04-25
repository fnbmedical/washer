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
public static anywheresoftware.b4a.objects.Timer _tasktime = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_fixed = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_personalized = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_select = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_fixed_back = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_fixed_cancel = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_fixed_dry = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_fixed_gentlew = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_fixed_next = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_fixed_quickw = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_fixed_rinse_dry = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_fixed_strongw = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_fixed_washw = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_select_back = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_select_cancel = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_select_next = null;
public anywheresoftware.b4a.objects.TabHostWrapper _tht_select = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_select = null;
public anywheresoftware.b4a.objects.TabHostWrapper _tht_index = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_index_tht = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_personalized_back = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_personalized_cancel = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_personalized_next = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _igv_personalized = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_success_ok = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_success = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_pay_back = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_pay_cancel = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_pay_next = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _igv_pay = null;
public anywheresoftware.b4a.objects.WebViewWrapper _wv_pay_pay = null;
public anywheresoftware.b4a.objects.WebViewWrapper _wv_pay_ser = null;
public anywheresoftware.b4a.objects.WebViewWrapper _wv_pay_wei = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _igv_pay_type_ali = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _igv_pay_type_my = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _igv_pay_type_weixin = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_pay_type = null;
public uk.co.martinpearman.b4a.webviewextras.WebViewExtras _wviewex = null;
public uk.co.martinpearman.b4a.webviewextras.WebViewExtras _wviewexs = null;
public uk.co.martinpearman.b4a.webviewextras.WebViewExtras _wviewexwei = null;
public static int _current_tab = 0;
public static int _current_tab_count = 0;
public static int _qr_show_flag = 0;
public static int _order_price = 0;
public static String _order_desc = "";
public static int _qrcodeshow_ali = 0;
public static int _qrcodeshow_weixin = 0;
public static int _pay_flag_ali = 0;
public static int _pay_flag_weixin = 0;
public anywheresoftware.b4a.objects.collections.Map _tht_index_lbl_map = null;
public anywheresoftware.b4a.objects.collections.Map _select_price_map = null;
public anywheresoftware.b4a.objects.collections.Map _select_lbl_text = null;
public static int _temporary_price = 0;
public static int _load_photo_flag = 0;
public static String _pay_load_url_flag = "";
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _page_button1 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _page_button2 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _page_button3 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _page_button4 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _page_button5 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _page_button6 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _page_button7 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _page_button8 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _page_button9 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _page_button10 = null;
public anywheresoftware.b4a.randomaccessfile.AsyncStreams _async = null;
public android_serialport_api.SerialPort _usbserial = null;
public static String _serial_port = "";
public static String _serial_rate = "";
public static int _serial_num = 0;
public static int _serial_return_data = 0;
public b4a.example.alipay _alipay = null;
public b4a.example.myutils _myutils = null;
public b4a.example.weixinpay _weixinpay = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 186;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 190;BA.debugLine="tht_index_lbl_map.Initialize()";
mostCurrent._tht_index_lbl_map.Initialize();
 //BA.debugLineNum = 191;BA.debugLine="tht_index_lbl_map.Put(\"page1\",\"欢迎使用自动洗衣系统\")";
mostCurrent._tht_index_lbl_map.Put((Object)("page1"),(Object)("欢迎使用自动洗衣系统"));
 //BA.debugLineNum = 192;BA.debugLine="tht_index_lbl_map.Put(\"page2\",\"固定洗衣模式有以下几种，请选择：\")";
mostCurrent._tht_index_lbl_map.Put((Object)("page2"),(Object)("固定洗衣模式有以下几种，请选择："));
 //BA.debugLineNum = 193;BA.debugLine="tht_index_lbl_map.Put(\"page3\",\"自定义组合模式请按步骤进行选择：\")";
mostCurrent._tht_index_lbl_map.Put((Object)("page3"),(Object)("自定义组合模式请按步骤进行选择："));
 //BA.debugLineNum = 194;BA.debugLine="tht_index_lbl_map.Put(\"page4\",\"个性化请打开app，扫描下方二维码\"";
mostCurrent._tht_index_lbl_map.Put((Object)("page4"),(Object)("个性化请打开app，扫描下方二维码"));
 //BA.debugLineNum = 195;BA.debugLine="tht_index_lbl_map.Put(\"page5\",\"请选择支付方式\")";
mostCurrent._tht_index_lbl_map.Put((Object)("page5"),(Object)("请选择支付方式"));
 //BA.debugLineNum = 196;BA.debugLine="tht_index_lbl_map.Put(\"page6\",\"请扫描下方二维码进行支付\")";
mostCurrent._tht_index_lbl_map.Put((Object)("page6"),(Object)("请扫描下方二维码进行支付"));
 //BA.debugLineNum = 197;BA.debugLine="tht_index_lbl_map.Put(\"page7\",\"支付成功，请按照提示将衣物放入制定位";
mostCurrent._tht_index_lbl_map.Put((Object)("page7"),(Object)("支付成功，请按照提示将衣物放入制定位置"));
 //BA.debugLineNum = 200;BA.debugLine="Activity.Height = -1";
mostCurrent._activity.setHeight((int) (-1));
 //BA.debugLineNum = 201;BA.debugLine="Activity.Width = -1";
mostCurrent._activity.setWidth((int) (-1));
 //BA.debugLineNum = 202;BA.debugLine="Activity.LoadLayout(\"tht_index\")";
mostCurrent._activity.LoadLayout("tht_index",mostCurrent.activityBA);
 //BA.debugLineNum = 204;BA.debugLine="alipay.saveImage(alipay.ALI_LOGO_NAME)";
mostCurrent._alipay._saveimage(mostCurrent.activityBA,mostCurrent._alipay._ali_logo_name);
 //BA.debugLineNum = 205;BA.debugLine="alipay.saveImage(weixinpay.WEIXIN_LOGO_NAME)";
mostCurrent._alipay._saveimage(mostCurrent.activityBA,mostCurrent._weixinpay._weixin_logo_name);
 //BA.debugLineNum = 206;BA.debugLine="alipay.saveImage(alipay.MY_LOGO_NAME)";
mostCurrent._alipay._saveimage(mostCurrent.activityBA,mostCurrent._alipay._my_logo_name);
 //BA.debugLineNum = 208;BA.debugLine="tht_index.AddTab(\"\",\"index\")";
mostCurrent._tht_index.AddTab(mostCurrent.activityBA,"","index");
 //BA.debugLineNum = 209;BA.debugLine="tht_index.AddTab(\"\",\"fixed\")";
mostCurrent._tht_index.AddTab(mostCurrent.activityBA,"","fixed");
 //BA.debugLineNum = 210;BA.debugLine="tht_index.AddTab(\"\",\"selectmod\")";
mostCurrent._tht_index.AddTab(mostCurrent.activityBA,"","selectmod");
 //BA.debugLineNum = 211;BA.debugLine="tht_index.AddTab(\"\",\"personalized\")";
mostCurrent._tht_index.AddTab(mostCurrent.activityBA,"","personalized");
 //BA.debugLineNum = 212;BA.debugLine="tht_index.AddTab(\"\",\"pay_type\")";
mostCurrent._tht_index.AddTab(mostCurrent.activityBA,"","pay_type");
 //BA.debugLineNum = 213;BA.debugLine="tht_index.AddTab(\"\",\"paymod\")";
mostCurrent._tht_index.AddTab(mostCurrent.activityBA,"","paymod");
 //BA.debugLineNum = 214;BA.debugLine="tht_index.AddTab(\"\",\"successmod\")";
mostCurrent._tht_index.AddTab(mostCurrent.activityBA,"","successmod");
 //BA.debugLineNum = 225;BA.debugLine="taskTime.Initialize(\"taskTime\",10)";
_tasktime.Initialize(processBA,"taskTime",(long) (10));
 //BA.debugLineNum = 228;BA.debugLine="lbl_index_tht.Text = tht_index_lbl_map.Get(\"page1";
mostCurrent._lbl_index_tht.setText(mostCurrent._tht_index_lbl_map.Get((Object)("page1")));
 //BA.debugLineNum = 230;BA.debugLine="igv_pay_type_ali.Bitmap = LoadBitmap(alipay.PHOTO";
mostCurrent._igv_pay_type_ali.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._alipay._photo_path,mostCurrent._alipay._ali_logo_name).getObject()));
 //BA.debugLineNum = 231;BA.debugLine="igv_pay_type_weixin.Bitmap = LoadBitmap(alipay.PH";
mostCurrent._igv_pay_type_weixin.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._alipay._photo_path,mostCurrent._weixinpay._weixin_logo_name).getObject()));
 //BA.debugLineNum = 232;BA.debugLine="igv_pay_type_my.Bitmap = LoadBitmap(alipay.PHOTO_";
mostCurrent._igv_pay_type_my.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._alipay._photo_path,mostCurrent._alipay._my_logo_name).getObject()));
 //BA.debugLineNum = 234;BA.debugLine="btn_pay_next.Enabled = False";
mostCurrent._btn_pay_next.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 235;BA.debugLine="btn_personalized_next.Enabled = False";
mostCurrent._btn_personalized_next.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 236;BA.debugLine="page_content";
_page_content();
 //BA.debugLineNum = 237;BA.debugLine="current_tab_count = tht_index.TabCount";
_current_tab_count = mostCurrent._tht_index.getTabCount();
 //BA.debugLineNum = 238;BA.debugLine="Try";
try { //BA.debugLineNum = 239;BA.debugLine="usbSerial.SetPort(SERIAL_PORT,SERIAL_RATE,SERIAL";
mostCurrent._usbserial.SetPort(mostCurrent._serial_port,(int)(Double.parseDouble(mostCurrent._serial_rate)),_serial_num);
 } 
       catch (Exception e109) {
			processBA.setLastException(e109); //BA.debugLineNum = 241;BA.debugLine="ToastMessageShow(LastException.Message,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 243;BA.debugLine="aSync.Initialize(usbSerial.InputStream,usbSerial.";
mostCurrent._async.Initialize(processBA,mostCurrent._usbserial.getInputStream(),mostCurrent._usbserial.getOutputStream(),"Astreams");
 //BA.debugLineNum = 245;BA.debugLine="send_Cmd(\"ddddddddddddddddddddd\")";
_send_cmd("ddddddddddddddddddddd");
 //BA.debugLineNum = 246;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 257;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 258;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 259;BA.debugLine="aSync.Close()";
mostCurrent._async.Close();
 //BA.debugLineNum = 260;BA.debugLine="usbSerial.close()";
mostCurrent._usbserial.close();
 };
 //BA.debugLineNum = 262;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 248;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 249;BA.debugLine="Try";
try { //BA.debugLineNum = 250;BA.debugLine="usbSerial.SetPort(SERIAL_PORT,SERIAL_RATE,SERIAL";
mostCurrent._usbserial.SetPort(mostCurrent._serial_port,(int)(Double.parseDouble(mostCurrent._serial_rate)),_serial_num);
 } 
       catch (Exception e118) {
			processBA.setLastException(e118); //BA.debugLineNum = 252;BA.debugLine="ToastMessageShow(LastException.Message,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 254;BA.debugLine="aSync.Initialize(usbSerial.InputStream,usbSerial.";
mostCurrent._async.Initialize(processBA,mostCurrent._usbserial.getInputStream(),mostCurrent._usbserial.getOutputStream(),"Astreams");
 //BA.debugLineNum = 255;BA.debugLine="End Sub";
return "";
}
public static String  _activity_windowfocuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 274;BA.debugLine="Sub Activity_WindowFocusChanged(HasFocus As Boolea";
 //BA.debugLineNum = 275;BA.debugLine="If HasFocus Then";
if (_hasfocus) { 
 //BA.debugLineNum = 276;BA.debugLine="ForceImmersiceMode";
_forceimmersicemode();
 };
 //BA.debugLineNum = 278;BA.debugLine="End Sub";
return "";
}
public static String  _astreams_error() throws Exception{
 //BA.debugLineNum = 1003;BA.debugLine="Sub Astreams_Error()";
 //BA.debugLineNum = 1004;BA.debugLine="ToastMessageShow(LastException.Message,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1005;BA.debugLine="End Sub";
return "";
}
public static String  _astreams_newdata(byte[] _buffer) throws Exception{
String _cmd_recevie = "";
anywheresoftware.b4a.agraham.byteconverter.ByteConverter _conv = null;
 //BA.debugLineNum = 980;BA.debugLine="Sub Astreams_NewData(Buffer() As Byte)";
 //BA.debugLineNum = 981;BA.debugLine="Dim cmd_Recevie As String";
_cmd_recevie = "";
 //BA.debugLineNum = 982;BA.debugLine="Dim conv As ByteConverter";
_conv = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 985;BA.debugLine="cmd_Recevie = conv.HexFromBytes(Buffer).ToLowerCa";
_cmd_recevie = _conv.HexFromBytes(_buffer).toLowerCase();
 //BA.debugLineNum = 989;BA.debugLine="If cmd_Recevie.Length() < 22 Then";
if (_cmd_recevie.length()<22) { 
 //BA.debugLineNum = 990;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 992;BA.debugLine="recevie_Cmd(cmd_Recevie)";
_recevie_cmd(_cmd_recevie);
 //BA.debugLineNum = 994;BA.debugLine="If serial_Return_Data <> 0 Then";
if (_serial_return_data!=0) { 
 //BA.debugLineNum = 995;BA.debugLine="excu_Cmd_Data";
_excu_cmd_data();
 };
 //BA.debugLineNum = 1000;BA.debugLine="End Sub";
return "";
}
public static String  _btn_fixed_back_click() throws Exception{
 //BA.debugLineNum = 374;BA.debugLine="Sub btn_fixed_back_Click";
 //BA.debugLineNum = 375;BA.debugLine="tht_index.CurrentTab = 0";
mostCurrent._tht_index.setCurrentTab((int) (0));
 //BA.debugLineNum = 376;BA.debugLine="processInit";
_processinit();
 //BA.debugLineNum = 377;BA.debugLine="End Sub";
return "";
}
public static String  _btn_fixed_cancel_click() throws Exception{
 //BA.debugLineNum = 370;BA.debugLine="Sub btn_fixed_cancel_Click";
 //BA.debugLineNum = 371;BA.debugLine="tht_index.CurrentTab = 0";
mostCurrent._tht_index.setCurrentTab((int) (0));
 //BA.debugLineNum = 372;BA.debugLine="processInit";
_processinit();
 //BA.debugLineNum = 373;BA.debugLine="End Sub";
return "";
}
public static String  _btn_fixed_click() throws Exception{
 //BA.debugLineNum = 302;BA.debugLine="Sub btn_fixed_Click";
 //BA.debugLineNum = 303;BA.debugLine="tht_index.CurrentTab = 1";
mostCurrent._tht_index.setCurrentTab((int) (1));
 //BA.debugLineNum = 304;BA.debugLine="End Sub";
return "";
}
public static String  _btn_fixed_dry_click() throws Exception{
 //BA.debugLineNum = 333;BA.debugLine="Sub btn_fixed_dry_Click";
 //BA.debugLineNum = 334;BA.debugLine="order_price = 2";
_order_price = (int) (2);
 //BA.debugLineNum = 335;BA.debugLine="order_desc = \"单脱水\"";
mostCurrent._order_desc = "单脱水";
 //BA.debugLineNum = 336;BA.debugLine="End Sub";
return "";
}
public static String  _btn_fixed_gentlew_click() throws Exception{
 //BA.debugLineNum = 328;BA.debugLine="Sub btn_fixed_gentleW_Click";
 //BA.debugLineNum = 329;BA.debugLine="order_price = 3";
_order_price = (int) (3);
 //BA.debugLineNum = 330;BA.debugLine="order_desc = \"轻柔洗\"";
mostCurrent._order_desc = "轻柔洗";
 //BA.debugLineNum = 331;BA.debugLine="End Sub";
return "";
}
public static String  _btn_fixed_next_click() throws Exception{
String _return_str = "";
String _order_num = "";
String _url_str = "";
 //BA.debugLineNum = 339;BA.debugLine="Sub btn_fixed_next_Click";
 //BA.debugLineNum = 340;BA.debugLine="Dim return_str As String = \"\"";
_return_str = "";
 //BA.debugLineNum = 341;BA.debugLine="Dim order_num As String = \"\"";
_order_num = "";
 //BA.debugLineNum = 343;BA.debugLine="Dim url_str As String = \"\"";
_url_str = "";
 //BA.debugLineNum = 345;BA.debugLine="If order_price <> 0 Then";
if (_order_price!=0) { 
 //BA.debugLineNum = 346;BA.debugLine="order_num = alipay.ORDERNUM&DateTime.Now";
_order_num = mostCurrent._alipay._ordernum+BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 347;BA.debugLine="return_str = alipay.aliQrCode(order_num,order_pr";
_return_str = mostCurrent._alipay._aliqrcode(mostCurrent.activityBA,_order_num,_order_price,mostCurrent._order_desc);
 //BA.debugLineNum = 348;BA.debugLine="wviewEx.addJavascriptInterface(wv_pay_Pay,\"B4A\")";
mostCurrent._wviewex.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._wv_pay_pay.getObject()),"B4A");
 //BA.debugLineNum = 349;BA.debugLine="wv_pay_Pay.LoadHtml(return_str)";
mostCurrent._wv_pay_pay.LoadHtml(_return_str);
 //BA.debugLineNum = 351;BA.debugLine="url_str = \"http://www.k91d.com/weixinpay/example";
_url_str = "http://www.k91d.com/weixinpay/example/mynative.php?check_flag=yangguangzyg&body="+mostCurrent._order_desc+"&attach="+mostCurrent._order_desc+"&out_trade_no="+_order_num+"&total_fee="+BA.NumberToString(_order_price);
 //BA.debugLineNum = 353;BA.debugLine="wviewExWei.addJavascriptInterface(wv_pay_Wei,\"B4";
mostCurrent._wviewexwei.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._wv_pay_wei.getObject()),"B4A");
 //BA.debugLineNum = 355;BA.debugLine="wv_pay_Wei.LoadUrl(url_str)";
mostCurrent._wv_pay_wei.LoadUrl(_url_str);
 //BA.debugLineNum = 357;BA.debugLine="url_str = \"http://www.k91d.com/service/index.php";
_url_str = "http://www.k91d.com/service/index.php?head=0527&type=ter&data="+_order_num+":"+mostCurrent._order_desc+":1:1:-1:-1:zgyhtfg34dde:"+BA.NumberToString(_order_price);
 //BA.debugLineNum = 360;BA.debugLine="wviewExs.addJavascriptInterface(wv_pay_Ser,\"B4A\"";
mostCurrent._wviewexs.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._wv_pay_ser.getObject()),"B4A");
 //BA.debugLineNum = 361;BA.debugLine="wv_pay_Ser.LoadUrl(url_str)";
mostCurrent._wv_pay_ser.LoadUrl(_url_str);
 //BA.debugLineNum = 362;BA.debugLine="pay_load_url_flag = 0";
mostCurrent._pay_load_url_flag = BA.NumberToString(0);
 //BA.debugLineNum = 363;BA.debugLine="taskTime.Enabled = True";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 364;BA.debugLine="current_tab = 1";
_current_tab = (int) (1);
 //BA.debugLineNum = 365;BA.debugLine="tht_index.CurrentTab = 4";
mostCurrent._tht_index.setCurrentTab((int) (4));
 }else {
 //BA.debugLineNum = 367;BA.debugLine="ToastMessageShow(\"请选择清洗模式\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("请选择清洗模式",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 369;BA.debugLine="End Sub";
return "";
}
public static String  _btn_fixed_quickw_click() throws Exception{
 //BA.debugLineNum = 323;BA.debugLine="Sub btn_fixed_quickW_Click";
 //BA.debugLineNum = 324;BA.debugLine="order_price = 3";
_order_price = (int) (3);
 //BA.debugLineNum = 325;BA.debugLine="order_desc = \"快速洗\"";
mostCurrent._order_desc = "快速洗";
 //BA.debugLineNum = 326;BA.debugLine="End Sub";
return "";
}
public static String  _btn_fixed_rinse_dry_click() throws Exception{
 //BA.debugLineNum = 318;BA.debugLine="Sub btn_fixed_rinse_dry_Click";
 //BA.debugLineNum = 319;BA.debugLine="order_price = 4";
_order_price = (int) (4);
 //BA.debugLineNum = 320;BA.debugLine="order_desc = \"漂洗+脱水\"";
mostCurrent._order_desc = "漂洗+脱水";
 //BA.debugLineNum = 321;BA.debugLine="End Sub";
return "";
}
public static String  _btn_fixed_strongw_click() throws Exception{
 //BA.debugLineNum = 313;BA.debugLine="Sub btn_fixed_strongW_Click";
 //BA.debugLineNum = 314;BA.debugLine="order_price = 5";
_order_price = (int) (5);
 //BA.debugLineNum = 315;BA.debugLine="order_desc = \"强力洗\"";
mostCurrent._order_desc = "强力洗";
 //BA.debugLineNum = 316;BA.debugLine="End Sub";
return "";
}
public static String  _btn_fixed_washw_click() throws Exception{
 //BA.debugLineNum = 308;BA.debugLine="Sub btn_fixed_washW_Click";
 //BA.debugLineNum = 309;BA.debugLine="order_price = 1";
_order_price = (int) (1);
 //BA.debugLineNum = 310;BA.debugLine="order_desc = \"洁桶洗\"";
mostCurrent._order_desc = "洁桶洗";
 //BA.debugLineNum = 311;BA.debugLine="End Sub";
return "";
}
public static String  _btn_pay_back_click() throws Exception{
 //BA.debugLineNum = 707;BA.debugLine="Sub btn_pay_back_Click";
 //BA.debugLineNum = 708;BA.debugLine="tht_index.CurrentTab = current_tab";
mostCurrent._tht_index.setCurrentTab(_current_tab);
 //BA.debugLineNum = 709;BA.debugLine="qr_show_flag = -1";
_qr_show_flag = (int) (-1);
 //BA.debugLineNum = 710;BA.debugLine="End Sub";
return "";
}
public static String  _btn_pay_cancel_click() throws Exception{
 //BA.debugLineNum = 703;BA.debugLine="Sub btn_pay_cancel_Click";
 //BA.debugLineNum = 704;BA.debugLine="tht_index.CurrentTab = 0";
mostCurrent._tht_index.setCurrentTab((int) (0));
 //BA.debugLineNum = 705;BA.debugLine="processInit";
_processinit();
 //BA.debugLineNum = 706;BA.debugLine="End Sub";
return "";
}
public static String  _btn_pay_next_click() throws Exception{
 //BA.debugLineNum = 698;BA.debugLine="Sub btn_pay_next_Click";
 //BA.debugLineNum = 699;BA.debugLine="If current_tab_count > 0 Then";
if (_current_tab_count>0) { 
 //BA.debugLineNum = 700;BA.debugLine="tht_index.CurrentTab = current_tab_count - 1";
mostCurrent._tht_index.setCurrentTab((int) (_current_tab_count-1));
 };
 //BA.debugLineNum = 702;BA.debugLine="End Sub";
return "";
}
public static String  _btn_personalized_back_click() throws Exception{
 //BA.debugLineNum = 687;BA.debugLine="Sub btn_personalized_back_Click";
 //BA.debugLineNum = 688;BA.debugLine="tht_index.CurrentTab = 0";
mostCurrent._tht_index.setCurrentTab((int) (0));
 //BA.debugLineNum = 689;BA.debugLine="processInit";
_processinit();
 //BA.debugLineNum = 690;BA.debugLine="End Sub";
return "";
}
public static String  _btn_personalized_cancel_click() throws Exception{
 //BA.debugLineNum = 683;BA.debugLine="Sub btn_personalized_cancel_Click";
 //BA.debugLineNum = 684;BA.debugLine="tht_index.CurrentTab = 0";
mostCurrent._tht_index.setCurrentTab((int) (0));
 //BA.debugLineNum = 685;BA.debugLine="processInit";
_processinit();
 //BA.debugLineNum = 686;BA.debugLine="End Sub";
return "";
}
public static String  _btn_personalized_click() throws Exception{
 //BA.debugLineNum = 294;BA.debugLine="Sub btn_personalized_Click";
 //BA.debugLineNum = 295;BA.debugLine="taskTime.Enabled = False";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 297;BA.debugLine="load_photo_flag = alipay.QRCODE.returnSucOrFail(\"";
_load_photo_flag = mostCurrent._alipay._qrcode.returnSucOrFail("hggghttffght",mostCurrent._alipay._photo_path,mostCurrent._alipay._machine_qr_code_infi,mostCurrent._alipay._qrcode_width_height);
 //BA.debugLineNum = 299;BA.debugLine="taskTime.Enabled = True";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 300;BA.debugLine="tht_index.CurrentTab = 3";
mostCurrent._tht_index.setCurrentTab((int) (3));
 //BA.debugLineNum = 301;BA.debugLine="End Sub";
return "";
}
public static String  _btn_personalized_next_click() throws Exception{
 //BA.debugLineNum = 679;BA.debugLine="Sub btn_personalized_next_Click";
 //BA.debugLineNum = 680;BA.debugLine="current_tab = 3";
_current_tab = (int) (3);
 //BA.debugLineNum = 681;BA.debugLine="tht_index.CurrentTab = 4";
mostCurrent._tht_index.setCurrentTab((int) (4));
 //BA.debugLineNum = 682;BA.debugLine="End Sub";
return "";
}
public static String  _btn_select_back_click() throws Exception{
 //BA.debugLineNum = 464;BA.debugLine="Sub btn_select_back_Click";
 //BA.debugLineNum = 465;BA.debugLine="tht_index.CurrentTab = 0";
mostCurrent._tht_index.setCurrentTab((int) (0));
 //BA.debugLineNum = 466;BA.debugLine="processInit";
_processinit();
 //BA.debugLineNum = 467;BA.debugLine="End Sub";
return "";
}
public static String  _btn_select_cancel_click() throws Exception{
 //BA.debugLineNum = 460;BA.debugLine="Sub btn_select_cancel_Click";
 //BA.debugLineNum = 461;BA.debugLine="tht_index.CurrentTab = 0";
mostCurrent._tht_index.setCurrentTab((int) (0));
 //BA.debugLineNum = 462;BA.debugLine="processInit";
_processinit();
 //BA.debugLineNum = 463;BA.debugLine="End Sub";
return "";
}
public static String  _btn_select_click() throws Exception{
 //BA.debugLineNum = 282;BA.debugLine="Sub btn_select_Click";
 //BA.debugLineNum = 283;BA.debugLine="select_lbl_text.Initialize()";
mostCurrent._select_lbl_text.Initialize();
 //BA.debugLineNum = 284;BA.debugLine="select_lbl_text.Put(\"page1\",\"请选择水量\")";
mostCurrent._select_lbl_text.Put((Object)("page1"),(Object)("请选择水量"));
 //BA.debugLineNum = 285;BA.debugLine="select_lbl_text.Put(\"page2\",\"请选择洗涤剂\")";
mostCurrent._select_lbl_text.Put((Object)("page2"),(Object)("请选择洗涤剂"));
 //BA.debugLineNum = 286;BA.debugLine="select_lbl_text.Put(\"page3\",\"请选择洗涤时间\")";
mostCurrent._select_lbl_text.Put((Object)("page3"),(Object)("请选择洗涤时间"));
 //BA.debugLineNum = 287;BA.debugLine="select_lbl_text.Put(\"page4\",\"请选择洗涤次数\")";
mostCurrent._select_lbl_text.Put((Object)("page4"),(Object)("请选择洗涤次数"));
 //BA.debugLineNum = 288;BA.debugLine="select_lbl_text.Put(\"page5\",\"请选择脱水次数\")";
mostCurrent._select_lbl_text.Put((Object)("page5"),(Object)("请选择脱水次数"));
 //BA.debugLineNum = 289;BA.debugLine="lbl_select.Text = select_lbl_text.Get(\"page1\")";
mostCurrent._lbl_select.setText(mostCurrent._select_lbl_text.Get((Object)("page1")));
 //BA.debugLineNum = 290;BA.debugLine="select_price_map.Initialize()";
mostCurrent._select_price_map.Initialize();
 //BA.debugLineNum = 291;BA.debugLine="tht_select.CurrentTab = 0";
mostCurrent._tht_select.setCurrentTab((int) (0));
 //BA.debugLineNum = 292;BA.debugLine="tht_index.CurrentTab = 2";
mostCurrent._tht_index.setCurrentTab((int) (2));
 //BA.debugLineNum = 293;BA.debugLine="End Sub";
return "";
}
public static String  _btn_select_next_click() throws Exception{
String _return_str = "";
String _order_num = "";
 //BA.debugLineNum = 380;BA.debugLine="Sub btn_select_next_Click";
 //BA.debugLineNum = 381;BA.debugLine="If page_button1.Checked == True Then";
if (mostCurrent._page_button1.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 382;BA.debugLine="temporary_price = temporary_price + 1";
_temporary_price = (int) (_temporary_price+1);
 //BA.debugLineNum = 383;BA.debugLine="order_desc = order_desc&\"水：15L；\"";
mostCurrent._order_desc = mostCurrent._order_desc+"水：15L；";
 };
 //BA.debugLineNum = 385;BA.debugLine="If page_button2.Checked == True Then";
if (mostCurrent._page_button2.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 386;BA.debugLine="temporary_price = temporary_price + 1";
_temporary_price = (int) (_temporary_price+1);
 //BA.debugLineNum = 387;BA.debugLine="order_desc = order_desc&\"水：20L；\"";
mostCurrent._order_desc = mostCurrent._order_desc+"水：20L；";
 };
 //BA.debugLineNum = 389;BA.debugLine="If page_button3.Checked == True Then";
if (mostCurrent._page_button3.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 390;BA.debugLine="temporary_price = temporary_price + 1";
_temporary_price = (int) (_temporary_price+1);
 //BA.debugLineNum = 391;BA.debugLine="order_desc = order_desc&\"洗涤剂：洗衣粉；\"";
mostCurrent._order_desc = mostCurrent._order_desc+"洗涤剂：洗衣粉；";
 };
 //BA.debugLineNum = 393;BA.debugLine="If page_button4.Checked == True Then";
if (mostCurrent._page_button4.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 394;BA.debugLine="temporary_price = temporary_price + 1";
_temporary_price = (int) (_temporary_price+1);
 //BA.debugLineNum = 395;BA.debugLine="order_desc = order_desc&\"洗涤剂：洗衣液；\"";
mostCurrent._order_desc = mostCurrent._order_desc+"洗涤剂：洗衣液；";
 };
 //BA.debugLineNum = 397;BA.debugLine="If page_button5.Checked == True Then";
if (mostCurrent._page_button5.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 398;BA.debugLine="temporary_price = temporary_price + 1";
_temporary_price = (int) (_temporary_price+1);
 //BA.debugLineNum = 399;BA.debugLine="order_desc = order_desc&\"洗涤时间：30min；\"";
mostCurrent._order_desc = mostCurrent._order_desc+"洗涤时间：30min；";
 };
 //BA.debugLineNum = 401;BA.debugLine="If page_button6.Checked == True Then";
if (mostCurrent._page_button6.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 402;BA.debugLine="temporary_price = temporary_price + 1";
_temporary_price = (int) (_temporary_price+1);
 //BA.debugLineNum = 403;BA.debugLine="order_desc = order_desc&\"洗涤时间：60min；\"";
mostCurrent._order_desc = mostCurrent._order_desc+"洗涤时间：60min；";
 };
 //BA.debugLineNum = 405;BA.debugLine="If page_button7.Checked == True Then";
if (mostCurrent._page_button7.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 406;BA.debugLine="temporary_price = temporary_price + 1";
_temporary_price = (int) (_temporary_price+1);
 //BA.debugLineNum = 407;BA.debugLine="order_desc = order_desc&\"洗涤次数：1；\"";
mostCurrent._order_desc = mostCurrent._order_desc+"洗涤次数：1；";
 };
 //BA.debugLineNum = 409;BA.debugLine="If page_button8.Checked == True Then";
if (mostCurrent._page_button8.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 410;BA.debugLine="temporary_price = temporary_price + 1";
_temporary_price = (int) (_temporary_price+1);
 //BA.debugLineNum = 411;BA.debugLine="order_desc = order_desc&\"洗涤次数：3；\"";
mostCurrent._order_desc = mostCurrent._order_desc+"洗涤次数：3；";
 };
 //BA.debugLineNum = 413;BA.debugLine="If page_button9.Checked == True Then";
if (mostCurrent._page_button9.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 414;BA.debugLine="temporary_price = temporary_price + 1";
_temporary_price = (int) (_temporary_price+1);
 //BA.debugLineNum = 415;BA.debugLine="order_desc = order_desc&\"脱水次数：1；\"";
mostCurrent._order_desc = mostCurrent._order_desc+"脱水次数：1；";
 };
 //BA.debugLineNum = 417;BA.debugLine="If page_button10.Checked == True Then";
if (mostCurrent._page_button10.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 418;BA.debugLine="temporary_price = temporary_price + 1";
_temporary_price = (int) (_temporary_price+1);
 //BA.debugLineNum = 419;BA.debugLine="order_desc = order_desc&\"脱水次数：3；\"";
mostCurrent._order_desc = mostCurrent._order_desc+"脱水次数：3；";
 };
 //BA.debugLineNum = 422;BA.debugLine="Dim return_str As String = \"\"";
_return_str = "";
 //BA.debugLineNum = 423;BA.debugLine="Dim order_num As String = \"\"";
_order_num = "";
 //BA.debugLineNum = 425;BA.debugLine="If temporary_price <> 0 Then";
if (_temporary_price!=0) { 
 //BA.debugLineNum = 426;BA.debugLine="order_num = alipay.ORDERNUM&DateTime.Now";
_order_num = mostCurrent._alipay._ordernum+BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 427;BA.debugLine="return_str = alipay.aliQrCode(order_num,temporar";
_return_str = mostCurrent._alipay._aliqrcode(mostCurrent.activityBA,_order_num,_temporary_price,mostCurrent._order_desc);
 //BA.debugLineNum = 428;BA.debugLine="wviewEx.addJavascriptInterface(wv_pay_Pay,\"B4A\")";
mostCurrent._wviewex.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._wv_pay_pay.getObject()),"B4A");
 //BA.debugLineNum = 429;BA.debugLine="wv_pay_Pay.LoadHtml(return_str)";
mostCurrent._wv_pay_pay.LoadHtml(_return_str);
 //BA.debugLineNum = 431;BA.debugLine="wviewExWei.addJavascriptInterface(wv_pay_Wei,\"B4";
mostCurrent._wviewexwei.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._wv_pay_wei.getObject()),"B4A");
 //BA.debugLineNum = 432;BA.debugLine="wv_pay_Wei.LoadUrl(\"http://www.k91d.com/weixinpa";
mostCurrent._wv_pay_wei.LoadUrl("http://www.k91d.com/weixinpay/example/mynative.php?check_flag=yangguangzyg&body="+mostCurrent._order_desc+"&attach="+mostCurrent._order_desc+"&out_trade_no="+_order_num+"&total_fee="+BA.NumberToString(_temporary_price));
 //BA.debugLineNum = 437;BA.debugLine="wviewExs.addJavascriptInterface(wv_pay_Ser,\"B4A\"";
mostCurrent._wviewexs.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._wv_pay_ser.getObject()),"B4A");
 //BA.debugLineNum = 438;BA.debugLine="wv_pay_Ser.LoadUrl(\"http://www.k91d.com/service/";
mostCurrent._wv_pay_ser.LoadUrl("http://www.k91d.com/service/index.php?head=0527&type=ter&data="+_order_num+":"+mostCurrent._order_desc+":1:1:-1:-1:zgyhtfg34dde:"+BA.NumberToString(_temporary_price));
 //BA.debugLineNum = 440;BA.debugLine="pay_load_url_flag = 0";
mostCurrent._pay_load_url_flag = BA.NumberToString(0);
 //BA.debugLineNum = 441;BA.debugLine="taskTime.Enabled = True";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 442;BA.debugLine="current_tab = 2";
_current_tab = (int) (2);
 //BA.debugLineNum = 443;BA.debugLine="tht_index.CurrentTab = 4";
mostCurrent._tht_index.setCurrentTab((int) (4));
 };
 //BA.debugLineNum = 459;BA.debugLine="End Sub";
return "";
}
public static String  _btn_success_ok_click() throws Exception{
 //BA.debugLineNum = 693;BA.debugLine="Sub btn_success_ok_Click";
 //BA.debugLineNum = 694;BA.debugLine="ToastMessageShow(\"支付成功，请放入衣物……\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("支付成功，请放入衣物……",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 695;BA.debugLine="End Sub";
return "";
}
public static String  _excu_cmd_data() throws Exception{
 //BA.debugLineNum = 1008;BA.debugLine="Sub excu_Cmd_Data";
 //BA.debugLineNum = 1012;BA.debugLine="End Sub";
return "";
}
public static String  _forceimmersicemode() throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 265;BA.debugLine="Sub ForceImmersiceMode";
 //BA.debugLineNum = 266;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 267;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 268;BA.debugLine="r.Target = r.RunMethod(\"getWindow\")";
_r.Target = _r.RunMethod("getWindow");
 //BA.debugLineNum = 269;BA.debugLine="r.Target = r.RunMethod(\"getDecorView\")";
_r.Target = _r.RunMethod("getDecorView");
 //BA.debugLineNum = 270;BA.debugLine="r.RunMethod2(\"setSystemUiVisibility\", 5894, \"java";
_r.RunMethod2("setSystemUiVisibility",BA.NumberToString(5894),"java.lang.int");
 //BA.debugLineNum = 271;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 31;BA.debugLine="Private btn_fixed As Button";
mostCurrent._btn_fixed = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private btn_personalized As Button";
mostCurrent._btn_personalized = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private btn_select As Button";
mostCurrent._btn_select = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private btn_fixed_back As Button";
mostCurrent._btn_fixed_back = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private btn_fixed_cancel As Button";
mostCurrent._btn_fixed_cancel = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private btn_fixed_dry As Button";
mostCurrent._btn_fixed_dry = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private btn_fixed_gentleW As Button";
mostCurrent._btn_fixed_gentlew = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private btn_fixed_next As Button";
mostCurrent._btn_fixed_next = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private btn_fixed_quickW As Button";
mostCurrent._btn_fixed_quickw = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private btn_fixed_rinse_dry As Button";
mostCurrent._btn_fixed_rinse_dry = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private btn_fixed_strongW As Button";
mostCurrent._btn_fixed_strongw = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private btn_fixed_washW As Button";
mostCurrent._btn_fixed_washw = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private btn_select_back As Button";
mostCurrent._btn_select_back = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private btn_select_cancel As Button";
mostCurrent._btn_select_cancel = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private btn_select_next As Button";
mostCurrent._btn_select_next = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private tht_select As TabHost";
mostCurrent._tht_select = new anywheresoftware.b4a.objects.TabHostWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private lbl_select As Label";
mostCurrent._lbl_select = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private tht_index As TabHost";
mostCurrent._tht_index = new anywheresoftware.b4a.objects.TabHostWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private lbl_index_tht As Label";
mostCurrent._lbl_index_tht = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Private btn_personalized_back As Button";
mostCurrent._btn_personalized_back = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Private btn_personalized_cancel As Button";
mostCurrent._btn_personalized_cancel = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Private btn_personalized_next As Button";
mostCurrent._btn_personalized_next = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Private igv_personalized As ImageView";
mostCurrent._igv_personalized = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Private btn_success_ok As Button";
mostCurrent._btn_success_ok = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Private lbl_success As Label";
mostCurrent._lbl_success = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private btn_pay_back As Button";
mostCurrent._btn_pay_back = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Private btn_pay_cancel As Button";
mostCurrent._btn_pay_cancel = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Private btn_pay_next As Button";
mostCurrent._btn_pay_next = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Private igv_pay As ImageView";
mostCurrent._igv_pay = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 85;BA.debugLine="Private wv_pay_Pay As WebView";
mostCurrent._wv_pay_pay = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Private wv_pay_Ser As WebView";
mostCurrent._wv_pay_ser = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 87;BA.debugLine="Private wv_pay_Wei As WebView";
mostCurrent._wv_pay_wei = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Private igv_pay_type_ali As ImageView";
mostCurrent._igv_pay_type_ali = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Private igv_pay_type_my As ImageView";
mostCurrent._igv_pay_type_my = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Private igv_pay_type_weixin As ImageView";
mostCurrent._igv_pay_type_weixin = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 95;BA.debugLine="Private lbl_pay_type As Label";
mostCurrent._lbl_pay_type = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 100;BA.debugLine="Dim wviewEx As WebViewExtras";
mostCurrent._wviewex = new uk.co.martinpearman.b4a.webviewextras.WebViewExtras();
 //BA.debugLineNum = 101;BA.debugLine="Dim wviewExs As WebViewExtras";
mostCurrent._wviewexs = new uk.co.martinpearman.b4a.webviewextras.WebViewExtras();
 //BA.debugLineNum = 102;BA.debugLine="Dim wviewExWei As WebViewExtras";
mostCurrent._wviewexwei = new uk.co.martinpearman.b4a.webviewextras.WebViewExtras();
 //BA.debugLineNum = 108;BA.debugLine="Dim current_tab As Int = 0";
_current_tab = (int) (0);
 //BA.debugLineNum = 110;BA.debugLine="Dim current_tab_count As Int = 0";
_current_tab_count = (int) (0);
 //BA.debugLineNum = 114;BA.debugLine="Dim qr_show_flag As Int = -1";
_qr_show_flag = (int) (-1);
 //BA.debugLineNum = 117;BA.debugLine="Dim order_price As Int = 0";
_order_price = (int) (0);
 //BA.debugLineNum = 119;BA.debugLine="Dim order_desc As String = \"\"";
mostCurrent._order_desc = "";
 //BA.debugLineNum = 123;BA.debugLine="Dim qrCodeShow_ali As Int = -1";
_qrcodeshow_ali = (int) (-1);
 //BA.debugLineNum = 125;BA.debugLine="Dim qrCodeShow_weixin As Int = -1";
_qrcodeshow_weixin = (int) (-1);
 //BA.debugLineNum = 128;BA.debugLine="Dim pay_Flag_ali As Int = -1";
_pay_flag_ali = (int) (-1);
 //BA.debugLineNum = 130;BA.debugLine="Dim pay_Flag_weixin As Int = -1";
_pay_flag_weixin = (int) (-1);
 //BA.debugLineNum = 133;BA.debugLine="Dim tht_index_lbl_map As Map";
mostCurrent._tht_index_lbl_map = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 136;BA.debugLine="Dim select_price_map As Map";
mostCurrent._select_price_map = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 138;BA.debugLine="Dim select_lbl_text As Map";
mostCurrent._select_lbl_text = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 140;BA.debugLine="Dim temporary_price As Int = 0";
_temporary_price = (int) (0);
 //BA.debugLineNum = 144;BA.debugLine="Dim load_photo_flag As Int = -1";
_load_photo_flag = (int) (-1);
 //BA.debugLineNum = 148;BA.debugLine="Dim pay_load_url_flag = -1";
mostCurrent._pay_load_url_flag = BA.NumberToString(-1);
 //BA.debugLineNum = 155;BA.debugLine="Dim page_button1 As RadioButton";
mostCurrent._page_button1 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 156;BA.debugLine="Dim page_button2 As RadioButton";
mostCurrent._page_button2 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 157;BA.debugLine="Dim page_button3 As RadioButton";
mostCurrent._page_button3 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 158;BA.debugLine="Dim page_button4 As RadioButton";
mostCurrent._page_button4 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 159;BA.debugLine="Dim page_button5 As RadioButton";
mostCurrent._page_button5 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 160;BA.debugLine="Dim page_button6 As RadioButton";
mostCurrent._page_button6 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 161;BA.debugLine="Dim page_button7 As RadioButton";
mostCurrent._page_button7 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 162;BA.debugLine="Dim page_button8 As RadioButton";
mostCurrent._page_button8 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 163;BA.debugLine="Dim page_button9 As RadioButton";
mostCurrent._page_button9 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 164;BA.debugLine="Dim page_button10 As RadioButton";
mostCurrent._page_button10 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 171;BA.debugLine="Dim aSync As AsyncStreams";
mostCurrent._async = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 172;BA.debugLine="Dim usbSerial As SerialPort";
mostCurrent._usbserial = new android_serialport_api.SerialPort();
 //BA.debugLineNum = 175;BA.debugLine="Dim SERIAL_PORT As String = \"/dev/ttyAMA2\"";
mostCurrent._serial_port = "/dev/ttyAMA2";
 //BA.debugLineNum = 176;BA.debugLine="Dim SERIAL_RATE As String = \"9600\"";
mostCurrent._serial_rate = "9600";
 //BA.debugLineNum = 177;BA.debugLine="Dim SERIAL_NUM As Int = 2";
_serial_num = (int) (2);
 //BA.debugLineNum = 180;BA.debugLine="Dim serial_Return_Data As Int = 0";
_serial_return_data = (int) (0);
 //BA.debugLineNum = 184;BA.debugLine="End Sub";
return "";
}
public static String  _igv_pay_type_ali_click() throws Exception{
 //BA.debugLineNum = 905;BA.debugLine="Sub igv_pay_type_ali_Click";
 //BA.debugLineNum = 906;BA.debugLine="qr_show_flag = 0";
_qr_show_flag = (int) (0);
 //BA.debugLineNum = 907;BA.debugLine="current_tab = tht_index.CurrentTab";
_current_tab = mostCurrent._tht_index.getCurrentTab();
 //BA.debugLineNum = 908;BA.debugLine="If current_tab_count - 1 > 0 Then";
if (_current_tab_count-1>0) { 
 //BA.debugLineNum = 909;BA.debugLine="tht_index.CurrentTab = current_tab_count - 2";
mostCurrent._tht_index.setCurrentTab((int) (_current_tab_count-2));
 };
 //BA.debugLineNum = 911;BA.debugLine="End Sub";
return "";
}
public static String  _igv_pay_type_my_click() throws Exception{
 //BA.debugLineNum = 898;BA.debugLine="Sub igv_pay_type_my_Click";
 //BA.debugLineNum = 899;BA.debugLine="qr_show_flag = 2";
_qr_show_flag = (int) (2);
 //BA.debugLineNum = 900;BA.debugLine="current_tab = tht_index.CurrentTab";
_current_tab = mostCurrent._tht_index.getCurrentTab();
 //BA.debugLineNum = 901;BA.debugLine="If current_tab_count - 1 > 0 Then";
if (_current_tab_count-1>0) { 
 //BA.debugLineNum = 902;BA.debugLine="tht_index.CurrentTab = current_tab_count - 2";
mostCurrent._tht_index.setCurrentTab((int) (_current_tab_count-2));
 };
 //BA.debugLineNum = 904;BA.debugLine="End Sub";
return "";
}
public static String  _igv_pay_type_weixin_click() throws Exception{
 //BA.debugLineNum = 889;BA.debugLine="Sub igv_pay_type_weixin_Click";
 //BA.debugLineNum = 890;BA.debugLine="qr_show_flag = 1";
_qr_show_flag = (int) (1);
 //BA.debugLineNum = 891;BA.debugLine="current_tab = tht_index.CurrentTab";
_current_tab = mostCurrent._tht_index.getCurrentTab();
 //BA.debugLineNum = 893;BA.debugLine="If current_tab_count - 1 > 0 Then";
if (_current_tab_count-1>0) { 
 //BA.debugLineNum = 894;BA.debugLine="tht_index.CurrentTab = current_tab_count - 2";
mostCurrent._tht_index.setCurrentTab((int) (_current_tab_count-2));
 };
 //BA.debugLineNum = 897;BA.debugLine="End Sub";
return "";
}
public static String  _lbl_select_click() throws Exception{
 //BA.debugLineNum = 618;BA.debugLine="Sub lbl_select_Click";
 //BA.debugLineNum = 620;BA.debugLine="End Sub";
return "";
}
public static String  _page_btn_back1_click() throws Exception{
 //BA.debugLineNum = 622;BA.debugLine="Sub page_btn_back1_Click";
 //BA.debugLineNum = 623;BA.debugLine="tht_select.CurrentTab = 0";
mostCurrent._tht_select.setCurrentTab((int) (0));
 //BA.debugLineNum = 624;BA.debugLine="End Sub";
return "";
}
public static String  _page_btn_back2_click() throws Exception{
 //BA.debugLineNum = 628;BA.debugLine="Sub page_btn_back2_Click";
 //BA.debugLineNum = 629;BA.debugLine="tht_select.CurrentTab = 0";
mostCurrent._tht_select.setCurrentTab((int) (0));
 //BA.debugLineNum = 630;BA.debugLine="End Sub";
return "";
}
public static String  _page_btn_back3_click() throws Exception{
 //BA.debugLineNum = 634;BA.debugLine="Sub page_btn_back3_Click";
 //BA.debugLineNum = 635;BA.debugLine="tht_select.CurrentTab = 1";
mostCurrent._tht_select.setCurrentTab((int) (1));
 //BA.debugLineNum = 636;BA.debugLine="End Sub";
return "";
}
public static String  _page_btn_back4_click() throws Exception{
 //BA.debugLineNum = 640;BA.debugLine="Sub page_btn_back4_Click";
 //BA.debugLineNum = 641;BA.debugLine="tht_select.CurrentTab = 2";
mostCurrent._tht_select.setCurrentTab((int) (2));
 //BA.debugLineNum = 642;BA.debugLine="End Sub";
return "";
}
public static String  _page_btn_back5_click() throws Exception{
 //BA.debugLineNum = 646;BA.debugLine="Sub page_btn_back5_Click";
 //BA.debugLineNum = 647;BA.debugLine="tht_select.CurrentTab = 3";
mostCurrent._tht_select.setCurrentTab((int) (3));
 //BA.debugLineNum = 648;BA.debugLine="End Sub";
return "";
}
public static String  _page_btn_next1_click() throws Exception{
 //BA.debugLineNum = 625;BA.debugLine="Sub page_btn_next1_Click";
 //BA.debugLineNum = 626;BA.debugLine="tht_select.CurrentTab = 1";
mostCurrent._tht_select.setCurrentTab((int) (1));
 //BA.debugLineNum = 627;BA.debugLine="End Sub";
return "";
}
public static String  _page_btn_next2_click() throws Exception{
 //BA.debugLineNum = 631;BA.debugLine="Sub page_btn_next2_Click";
 //BA.debugLineNum = 632;BA.debugLine="tht_select.CurrentTab = 2";
mostCurrent._tht_select.setCurrentTab((int) (2));
 //BA.debugLineNum = 633;BA.debugLine="End Sub";
return "";
}
public static String  _page_btn_next3_click() throws Exception{
 //BA.debugLineNum = 637;BA.debugLine="Sub page_btn_next3_Click";
 //BA.debugLineNum = 638;BA.debugLine="tht_select.CurrentTab = 3";
mostCurrent._tht_select.setCurrentTab((int) (3));
 //BA.debugLineNum = 639;BA.debugLine="End Sub";
return "";
}
public static String  _page_btn_next4_click() throws Exception{
 //BA.debugLineNum = 643;BA.debugLine="Sub page_btn_next4_Click";
 //BA.debugLineNum = 644;BA.debugLine="tht_select.CurrentTab = 4";
mostCurrent._tht_select.setCurrentTab((int) (4));
 //BA.debugLineNum = 645;BA.debugLine="End Sub";
return "";
}
public static String  _page_btn_next5_click() throws Exception{
 //BA.debugLineNum = 649;BA.debugLine="Sub page_btn_next5_Click";
 //BA.debugLineNum = 650;BA.debugLine="tht_select.CurrentTab = 4";
mostCurrent._tht_select.setCurrentTab((int) (4));
 //BA.debugLineNum = 651;BA.debugLine="End Sub";
return "";
}
public static String  _page_content() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pl = null;
anywheresoftware.b4a.objects.PanelWrapper _pl2 = null;
anywheresoftware.b4a.objects.PanelWrapper _pl3 = null;
anywheresoftware.b4a.objects.PanelWrapper _pl4 = null;
anywheresoftware.b4a.objects.PanelWrapper _pl5 = null;
anywheresoftware.b4a.objects.LabelWrapper _page_label1 = null;
anywheresoftware.b4a.objects.LabelWrapper _page_label2 = null;
anywheresoftware.b4a.objects.LabelWrapper _page_label3 = null;
anywheresoftware.b4a.objects.LabelWrapper _page_label4 = null;
anywheresoftware.b4a.objects.LabelWrapper _page_label5 = null;
anywheresoftware.b4a.objects.LabelWrapper _page_label6 = null;
anywheresoftware.b4a.objects.LabelWrapper _page_label7 = null;
anywheresoftware.b4a.objects.LabelWrapper _page_label8 = null;
anywheresoftware.b4a.objects.LabelWrapper _page_label9 = null;
anywheresoftware.b4a.objects.LabelWrapper _page_label10 = null;
anywheresoftware.b4a.objects.ButtonWrapper _page_btn_back1 = null;
anywheresoftware.b4a.objects.ButtonWrapper _page_btn_next1 = null;
anywheresoftware.b4a.objects.ButtonWrapper _page_btn_back2 = null;
anywheresoftware.b4a.objects.ButtonWrapper _page_btn_next2 = null;
anywheresoftware.b4a.objects.ButtonWrapper _page_btn_back3 = null;
anywheresoftware.b4a.objects.ButtonWrapper _page_btn_next3 = null;
anywheresoftware.b4a.objects.ButtonWrapper _page_btn_back4 = null;
anywheresoftware.b4a.objects.ButtonWrapper _page_btn_next4 = null;
anywheresoftware.b4a.objects.ButtonWrapper _page_btn_back5 = null;
anywheresoftware.b4a.objects.ButtonWrapper _page_btn_next5 = null;
 //BA.debugLineNum = 469;BA.debugLine="Sub page_content";
 //BA.debugLineNum = 470;BA.debugLine="Dim pl As Panel";
_pl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 471;BA.debugLine="Dim pl2 As Panel";
_pl2 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 472;BA.debugLine="Dim pl3 As Panel";
_pl3 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 473;BA.debugLine="Dim pl4 As Panel";
_pl4 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 474;BA.debugLine="Dim pl5 As Panel";
_pl5 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 476;BA.debugLine="Dim page_label1 As Label";
_page_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 477;BA.debugLine="Dim page_label2 As Label";
_page_label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 478;BA.debugLine="Dim page_label3 As Label";
_page_label3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 479;BA.debugLine="Dim page_label4 As Label";
_page_label4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 480;BA.debugLine="Dim page_label5 As Label";
_page_label5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 481;BA.debugLine="Dim page_label6 As Label";
_page_label6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 482;BA.debugLine="Dim page_label7 As Label";
_page_label7 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 483;BA.debugLine="Dim page_label8 As Label";
_page_label8 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 484;BA.debugLine="Dim page_label9 As Label";
_page_label9 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 485;BA.debugLine="Dim page_label10 As Label";
_page_label10 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 487;BA.debugLine="Dim page_btn_back1 As Button";
_page_btn_back1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 488;BA.debugLine="Dim page_btn_next1 As Button";
_page_btn_next1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 489;BA.debugLine="Dim page_btn_back2 As Button";
_page_btn_back2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 490;BA.debugLine="Dim page_btn_next2 As Button";
_page_btn_next2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 491;BA.debugLine="Dim page_btn_back3 As Button";
_page_btn_back3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 492;BA.debugLine="Dim page_btn_next3 As Button";
_page_btn_next3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 493;BA.debugLine="Dim page_btn_back4 As Button";
_page_btn_back4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 494;BA.debugLine="Dim page_btn_next4 As Button";
_page_btn_next4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 495;BA.debugLine="Dim page_btn_back5 As Button";
_page_btn_back5 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 496;BA.debugLine="Dim page_btn_next5 As Button";
_page_btn_next5 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 499;BA.debugLine="pl.Initialize(\"pl\")";
_pl.Initialize(mostCurrent.activityBA,"pl");
 //BA.debugLineNum = 500;BA.debugLine="pl2.Initialize(\"pl2\")";
_pl2.Initialize(mostCurrent.activityBA,"pl2");
 //BA.debugLineNum = 501;BA.debugLine="pl3.Initialize(\"pl3\")";
_pl3.Initialize(mostCurrent.activityBA,"pl3");
 //BA.debugLineNum = 502;BA.debugLine="pl4.Initialize(\"pl4\")";
_pl4.Initialize(mostCurrent.activityBA,"pl4");
 //BA.debugLineNum = 503;BA.debugLine="pl5.Initialize(\"pl5\")";
_pl5.Initialize(mostCurrent.activityBA,"pl5");
 //BA.debugLineNum = 505;BA.debugLine="page_button1.Initialize(\"page_button1\")";
mostCurrent._page_button1.Initialize(mostCurrent.activityBA,"page_button1");
 //BA.debugLineNum = 506;BA.debugLine="page_button2.Initialize(\"page_button2\")";
mostCurrent._page_button2.Initialize(mostCurrent.activityBA,"page_button2");
 //BA.debugLineNum = 507;BA.debugLine="page_button3.Initialize(\"page_button3\")";
mostCurrent._page_button3.Initialize(mostCurrent.activityBA,"page_button3");
 //BA.debugLineNum = 508;BA.debugLine="page_button4.Initialize(\"page_button4\")";
mostCurrent._page_button4.Initialize(mostCurrent.activityBA,"page_button4");
 //BA.debugLineNum = 509;BA.debugLine="page_button5.Initialize(\"page_button5\")";
mostCurrent._page_button5.Initialize(mostCurrent.activityBA,"page_button5");
 //BA.debugLineNum = 510;BA.debugLine="page_button6.Initialize(\"page_button6\")";
mostCurrent._page_button6.Initialize(mostCurrent.activityBA,"page_button6");
 //BA.debugLineNum = 511;BA.debugLine="page_button7.Initialize(\"page_button7\")";
mostCurrent._page_button7.Initialize(mostCurrent.activityBA,"page_button7");
 //BA.debugLineNum = 512;BA.debugLine="page_button8.Initialize(\"page_button8\")";
mostCurrent._page_button8.Initialize(mostCurrent.activityBA,"page_button8");
 //BA.debugLineNum = 513;BA.debugLine="page_button9.Initialize(\"page_button9\")";
mostCurrent._page_button9.Initialize(mostCurrent.activityBA,"page_button9");
 //BA.debugLineNum = 514;BA.debugLine="page_button10.Initialize(\"page_button10\")";
mostCurrent._page_button10.Initialize(mostCurrent.activityBA,"page_button10");
 //BA.debugLineNum = 516;BA.debugLine="page_label1.Initialize(\"page_label1\")";
_page_label1.Initialize(mostCurrent.activityBA,"page_label1");
 //BA.debugLineNum = 517;BA.debugLine="page_label2.Initialize(\"page_label2\")";
_page_label2.Initialize(mostCurrent.activityBA,"page_label2");
 //BA.debugLineNum = 518;BA.debugLine="page_label3.Initialize(\"page_label3\")";
_page_label3.Initialize(mostCurrent.activityBA,"page_label3");
 //BA.debugLineNum = 519;BA.debugLine="page_label4.Initialize(\"page_label4\")";
_page_label4.Initialize(mostCurrent.activityBA,"page_label4");
 //BA.debugLineNum = 520;BA.debugLine="page_label5.Initialize(\"page_label5\")";
_page_label5.Initialize(mostCurrent.activityBA,"page_label5");
 //BA.debugLineNum = 521;BA.debugLine="page_label6.Initialize(\"page_label6\")";
_page_label6.Initialize(mostCurrent.activityBA,"page_label6");
 //BA.debugLineNum = 522;BA.debugLine="page_label7.Initialize(\"page_label7\")";
_page_label7.Initialize(mostCurrent.activityBA,"page_label7");
 //BA.debugLineNum = 523;BA.debugLine="page_label8.Initialize(\"page_label8\")";
_page_label8.Initialize(mostCurrent.activityBA,"page_label8");
 //BA.debugLineNum = 524;BA.debugLine="page_label9.Initialize(\"page_label9\")";
_page_label9.Initialize(mostCurrent.activityBA,"page_label9");
 //BA.debugLineNum = 525;BA.debugLine="page_label10.Initialize(\"page_label0\")";
_page_label10.Initialize(mostCurrent.activityBA,"page_label0");
 //BA.debugLineNum = 527;BA.debugLine="page_btn_back1.Initialize(\"page_btn_back1\")";
_page_btn_back1.Initialize(mostCurrent.activityBA,"page_btn_back1");
 //BA.debugLineNum = 528;BA.debugLine="page_btn_next1.Initialize(\"page_btn_next1\")";
_page_btn_next1.Initialize(mostCurrent.activityBA,"page_btn_next1");
 //BA.debugLineNum = 529;BA.debugLine="page_btn_back2.Initialize(\"page_btn_back2\")";
_page_btn_back2.Initialize(mostCurrent.activityBA,"page_btn_back2");
 //BA.debugLineNum = 530;BA.debugLine="page_btn_next2.Initialize(\"page_btn_next2\")";
_page_btn_next2.Initialize(mostCurrent.activityBA,"page_btn_next2");
 //BA.debugLineNum = 531;BA.debugLine="page_btn_back3.Initialize(\"page_btn_back3\")";
_page_btn_back3.Initialize(mostCurrent.activityBA,"page_btn_back3");
 //BA.debugLineNum = 532;BA.debugLine="page_btn_next3.Initialize(\"page_btn_next3\")";
_page_btn_next3.Initialize(mostCurrent.activityBA,"page_btn_next3");
 //BA.debugLineNum = 533;BA.debugLine="page_btn_back4.Initialize(\"page_btn_back4\")";
_page_btn_back4.Initialize(mostCurrent.activityBA,"page_btn_back4");
 //BA.debugLineNum = 534;BA.debugLine="page_btn_next4.Initialize(\"page_btn_next4\")";
_page_btn_next4.Initialize(mostCurrent.activityBA,"page_btn_next4");
 //BA.debugLineNum = 535;BA.debugLine="page_btn_back5.Initialize(\"page_btn_back5\")";
_page_btn_back5.Initialize(mostCurrent.activityBA,"page_btn_back5");
 //BA.debugLineNum = 536;BA.debugLine="page_btn_next5.Initialize(\"page_btn_next5\")";
_page_btn_next5.Initialize(mostCurrent.activityBA,"page_btn_next5");
 //BA.debugLineNum = 538;BA.debugLine="page_button1.Checked = True";
mostCurrent._page_button1.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 539;BA.debugLine="page_button3.Checked = True";
mostCurrent._page_button3.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 540;BA.debugLine="page_button5.Checked = True";
mostCurrent._page_button5.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 541;BA.debugLine="page_button7.Checked = True";
mostCurrent._page_button7.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 542;BA.debugLine="page_button9.Checked = True";
mostCurrent._page_button9.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 544;BA.debugLine="page_btn_back1.Text = \"返回\"";
_page_btn_back1.setText((Object)("返回"));
 //BA.debugLineNum = 545;BA.debugLine="page_btn_back1.TextSize = \"20\"";
_page_btn_back1.setTextSize((float)(Double.parseDouble("20")));
 //BA.debugLineNum = 546;BA.debugLine="page_btn_next1.Text = \"其他\"";
_page_btn_next1.setText((Object)("其他"));
 //BA.debugLineNum = 547;BA.debugLine="page_btn_next1.TextSize = \"20\"";
_page_btn_next1.setTextSize((float)(Double.parseDouble("20")));
 //BA.debugLineNum = 548;BA.debugLine="page_btn_back2.Text = \"返回\"";
_page_btn_back2.setText((Object)("返回"));
 //BA.debugLineNum = 549;BA.debugLine="page_btn_back2.TextSize = \"20\"";
_page_btn_back2.setTextSize((float)(Double.parseDouble("20")));
 //BA.debugLineNum = 550;BA.debugLine="page_btn_next2.Text = \"其他\"";
_page_btn_next2.setText((Object)("其他"));
 //BA.debugLineNum = 551;BA.debugLine="page_btn_next2.TextSize = \"20\"";
_page_btn_next2.setTextSize((float)(Double.parseDouble("20")));
 //BA.debugLineNum = 552;BA.debugLine="page_btn_back3.Text = \"返回\"";
_page_btn_back3.setText((Object)("返回"));
 //BA.debugLineNum = 553;BA.debugLine="page_btn_back3.TextSize = \"20\"";
_page_btn_back3.setTextSize((float)(Double.parseDouble("20")));
 //BA.debugLineNum = 554;BA.debugLine="page_btn_next3.Text = \"其他\"";
_page_btn_next3.setText((Object)("其他"));
 //BA.debugLineNum = 555;BA.debugLine="page_btn_next3.TextSize = \"20\"";
_page_btn_next3.setTextSize((float)(Double.parseDouble("20")));
 //BA.debugLineNum = 556;BA.debugLine="page_btn_back4.Text = \"返回\"";
_page_btn_back4.setText((Object)("返回"));
 //BA.debugLineNum = 557;BA.debugLine="page_btn_back4.TextSize = \"20\"";
_page_btn_back4.setTextSize((float)(Double.parseDouble("20")));
 //BA.debugLineNum = 558;BA.debugLine="page_btn_next4.Text = \"其他\"";
_page_btn_next4.setText((Object)("其他"));
 //BA.debugLineNum = 559;BA.debugLine="page_btn_next4.TextSize = \"20\"";
_page_btn_next4.setTextSize((float)(Double.parseDouble("20")));
 //BA.debugLineNum = 560;BA.debugLine="page_btn_back5.Text = \"返回\"";
_page_btn_back5.setText((Object)("返回"));
 //BA.debugLineNum = 561;BA.debugLine="page_btn_back5.TextSize = \"20\"";
_page_btn_back5.setTextSize((float)(Double.parseDouble("20")));
 //BA.debugLineNum = 562;BA.debugLine="page_btn_next5.Text = \"其他\"";
_page_btn_next5.setText((Object)("其他"));
 //BA.debugLineNum = 563;BA.debugLine="page_btn_next5.TextSize = \"20\"";
_page_btn_next5.setTextSize((float)(Double.parseDouble("20")));
 //BA.debugLineNum = 565;BA.debugLine="page_label1.Text = \"15L\"";
_page_label1.setText((Object)("15L"));
 //BA.debugLineNum = 566;BA.debugLine="page_label2.Text = \"20L\"";
_page_label2.setText((Object)("20L"));
 //BA.debugLineNum = 567;BA.debugLine="page_label3.Text = \"洗衣粉\"";
_page_label3.setText((Object)("洗衣粉"));
 //BA.debugLineNum = 568;BA.debugLine="page_label4.Text = \"洗衣液\"";
_page_label4.setText((Object)("洗衣液"));
 //BA.debugLineNum = 569;BA.debugLine="page_label5.Text = \"30min\"";
_page_label5.setText((Object)("30min"));
 //BA.debugLineNum = 570;BA.debugLine="page_label6.Text = \"60min\"";
_page_label6.setText((Object)("60min"));
 //BA.debugLineNum = 571;BA.debugLine="page_label7.Text = \"1次\"";
_page_label7.setText((Object)("1次"));
 //BA.debugLineNum = 572;BA.debugLine="page_label8.Text = \"3次\"";
_page_label8.setText((Object)("3次"));
 //BA.debugLineNum = 573;BA.debugLine="page_label9.Text = \"1次\"";
_page_label9.setText((Object)("1次"));
 //BA.debugLineNum = 574;BA.debugLine="page_label10.Text = \"2次\"";
_page_label10.setText((Object)("2次"));
 //BA.debugLineNum = 576;BA.debugLine="pl.AddView(page_button1,0,0,30,20)";
_pl.AddView((android.view.View)(mostCurrent._page_button1.getObject()),(int) (0),(int) (0),(int) (30),(int) (20));
 //BA.debugLineNum = 577;BA.debugLine="pl.AddView(page_button2,0,100,30,20)";
_pl.AddView((android.view.View)(mostCurrent._page_button2.getObject()),(int) (0),(int) (100),(int) (30),(int) (20));
 //BA.debugLineNum = 578;BA.debugLine="pl.AddView(page_label1,40,0,500,40)";
_pl.AddView((android.view.View)(_page_label1.getObject()),(int) (40),(int) (0),(int) (500),(int) (40));
 //BA.debugLineNum = 579;BA.debugLine="pl.AddView(page_label2,40,100,500,40)";
_pl.AddView((android.view.View)(_page_label2.getObject()),(int) (40),(int) (100),(int) (500),(int) (40));
 //BA.debugLineNum = 580;BA.debugLine="pl2.AddView(page_button3,0,0,30,20)";
_pl2.AddView((android.view.View)(mostCurrent._page_button3.getObject()),(int) (0),(int) (0),(int) (30),(int) (20));
 //BA.debugLineNum = 581;BA.debugLine="pl2.AddView(page_button4,0,100,30,20)";
_pl2.AddView((android.view.View)(mostCurrent._page_button4.getObject()),(int) (0),(int) (100),(int) (30),(int) (20));
 //BA.debugLineNum = 582;BA.debugLine="pl2.AddView(page_label3,40,0,500,40)";
_pl2.AddView((android.view.View)(_page_label3.getObject()),(int) (40),(int) (0),(int) (500),(int) (40));
 //BA.debugLineNum = 583;BA.debugLine="pl2.AddView(page_label4,40,100,500,40)";
_pl2.AddView((android.view.View)(_page_label4.getObject()),(int) (40),(int) (100),(int) (500),(int) (40));
 //BA.debugLineNum = 584;BA.debugLine="pl3.AddView(page_button5,0,0,30,20)";
_pl3.AddView((android.view.View)(mostCurrent._page_button5.getObject()),(int) (0),(int) (0),(int) (30),(int) (20));
 //BA.debugLineNum = 585;BA.debugLine="pl3.AddView(page_button6,0,100,30,20)";
_pl3.AddView((android.view.View)(mostCurrent._page_button6.getObject()),(int) (0),(int) (100),(int) (30),(int) (20));
 //BA.debugLineNum = 586;BA.debugLine="pl3.AddView(page_label5,40,0,500,40)";
_pl3.AddView((android.view.View)(_page_label5.getObject()),(int) (40),(int) (0),(int) (500),(int) (40));
 //BA.debugLineNum = 587;BA.debugLine="pl3.AddView(page_label6,40,100,500,40)";
_pl3.AddView((android.view.View)(_page_label6.getObject()),(int) (40),(int) (100),(int) (500),(int) (40));
 //BA.debugLineNum = 588;BA.debugLine="pl4.AddView(page_button7,0,0,30,20)";
_pl4.AddView((android.view.View)(mostCurrent._page_button7.getObject()),(int) (0),(int) (0),(int) (30),(int) (20));
 //BA.debugLineNum = 589;BA.debugLine="pl4.AddView(page_button8,0,100,30,20)";
_pl4.AddView((android.view.View)(mostCurrent._page_button8.getObject()),(int) (0),(int) (100),(int) (30),(int) (20));
 //BA.debugLineNum = 590;BA.debugLine="pl4.AddView(page_label7,40,0,500,40)";
_pl4.AddView((android.view.View)(_page_label7.getObject()),(int) (40),(int) (0),(int) (500),(int) (40));
 //BA.debugLineNum = 591;BA.debugLine="pl4.AddView(page_label8,40,100,500,40)";
_pl4.AddView((android.view.View)(_page_label8.getObject()),(int) (40),(int) (100),(int) (500),(int) (40));
 //BA.debugLineNum = 592;BA.debugLine="pl5.AddView(page_button9,0,0,30,20)";
_pl5.AddView((android.view.View)(mostCurrent._page_button9.getObject()),(int) (0),(int) (0),(int) (30),(int) (20));
 //BA.debugLineNum = 593;BA.debugLine="pl5.AddView(page_button10,0,100,30,20)";
_pl5.AddView((android.view.View)(mostCurrent._page_button10.getObject()),(int) (0),(int) (100),(int) (30),(int) (20));
 //BA.debugLineNum = 594;BA.debugLine="pl5.AddView(page_label9,40,0,500,40)";
_pl5.AddView((android.view.View)(_page_label9.getObject()),(int) (40),(int) (0),(int) (500),(int) (40));
 //BA.debugLineNum = 595;BA.debugLine="pl5.AddView(page_label10,40,100,500,40)";
_pl5.AddView((android.view.View)(_page_label10.getObject()),(int) (40),(int) (100),(int) (500),(int) (40));
 //BA.debugLineNum = 597;BA.debugLine="pl.AddView(page_btn_back1,0,500,100,50)";
_pl.AddView((android.view.View)(_page_btn_back1.getObject()),(int) (0),(int) (500),(int) (100),(int) (50));
 //BA.debugLineNum = 598;BA.debugLine="pl.AddView(page_btn_next1,800,500,100,50)";
_pl.AddView((android.view.View)(_page_btn_next1.getObject()),(int) (800),(int) (500),(int) (100),(int) (50));
 //BA.debugLineNum = 599;BA.debugLine="pl2.AddView(page_btn_back2,0,500,100,50)";
_pl2.AddView((android.view.View)(_page_btn_back2.getObject()),(int) (0),(int) (500),(int) (100),(int) (50));
 //BA.debugLineNum = 600;BA.debugLine="pl2.AddView(page_btn_next2,800,500,100,50)";
_pl2.AddView((android.view.View)(_page_btn_next2.getObject()),(int) (800),(int) (500),(int) (100),(int) (50));
 //BA.debugLineNum = 601;BA.debugLine="pl3.AddView(page_btn_back3,0,500,100,50)";
_pl3.AddView((android.view.View)(_page_btn_back3.getObject()),(int) (0),(int) (500),(int) (100),(int) (50));
 //BA.debugLineNum = 602;BA.debugLine="pl3.AddView(page_btn_next3,800,500,100,50)";
_pl3.AddView((android.view.View)(_page_btn_next3.getObject()),(int) (800),(int) (500),(int) (100),(int) (50));
 //BA.debugLineNum = 603;BA.debugLine="pl4.AddView(page_btn_back4,0,500,100,50)";
_pl4.AddView((android.view.View)(_page_btn_back4.getObject()),(int) (0),(int) (500),(int) (100),(int) (50));
 //BA.debugLineNum = 604;BA.debugLine="pl4.AddView(page_btn_next4,800,500,100,50)";
_pl4.AddView((android.view.View)(_page_btn_next4.getObject()),(int) (800),(int) (500),(int) (100),(int) (50));
 //BA.debugLineNum = 605;BA.debugLine="pl5.AddView(page_btn_back5,0,500,100,50)";
_pl5.AddView((android.view.View)(_page_btn_back5.getObject()),(int) (0),(int) (500),(int) (100),(int) (50));
 //BA.debugLineNum = 606;BA.debugLine="pl5.AddView(page_btn_next5,800,500,100,50)";
_pl5.AddView((android.view.View)(_page_btn_next5.getObject()),(int) (800),(int) (500),(int) (100),(int) (50));
 //BA.debugLineNum = 608;BA.debugLine="lbl_select.TextColor = Colors.Red";
mostCurrent._lbl_select.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 609;BA.debugLine="lbl_select.TextSize = 20";
mostCurrent._lbl_select.setTextSize((float) (20));
 //BA.debugLineNum = 610;BA.debugLine="tht_select.AddTab2(\"\",pl)";
mostCurrent._tht_select.AddTab2("",(android.view.View)(_pl.getObject()));
 //BA.debugLineNum = 611;BA.debugLine="tht_select.AddTab2(\"\",pl2)";
mostCurrent._tht_select.AddTab2("",(android.view.View)(_pl2.getObject()));
 //BA.debugLineNum = 612;BA.debugLine="tht_select.AddTab2(\"\",pl3)";
mostCurrent._tht_select.AddTab2("",(android.view.View)(_pl3.getObject()));
 //BA.debugLineNum = 613;BA.debugLine="tht_select.AddTab2(\"\",pl4)";
mostCurrent._tht_select.AddTab2("",(android.view.View)(_pl4.getObject()));
 //BA.debugLineNum = 614;BA.debugLine="tht_select.AddTab2(\"\",pl5)";
mostCurrent._tht_select.AddTab2("",(android.view.View)(_pl5.getObject()));
 //BA.debugLineNum = 616;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
alipay._process_globals();
myutils._process_globals();
weixinpay._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 23;BA.debugLine="Dim taskTime As Timer";
_tasktime = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _processhtml(String _html) throws Exception{
anywheresoftware.b4a.objects.collections.Map _parms_map = null;
anywheresoftware.b4a.keywords.StringBuilderWrapper _sb_url = null;
 //BA.debugLineNum = 781;BA.debugLine="Sub processHTML(html As String)";
 //BA.debugLineNum = 782;BA.debugLine="Dim parms_map As Map";
_parms_map = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 783;BA.debugLine="Dim sb_url As StringBuilder";
_sb_url = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 785;BA.debugLine="sb_url.Initialize()";
_sb_url.Initialize();
 //BA.debugLineNum = 786;BA.debugLine="parms_map.Initialize()";
_parms_map.Initialize();
 //BA.debugLineNum = 788;BA.debugLine="If pay_load_url_flag == 0 Then";
if ((mostCurrent._pay_load_url_flag).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 790;BA.debugLine="qrCodeShow_ali = alipay.getQrCodeFromHtml(html)";
_qrcodeshow_ali = mostCurrent._alipay._getqrcodefromhtml(mostCurrent.activityBA,_html);
 //BA.debugLineNum = 792;BA.debugLine="pay_Flag_ali = alipay.getSuccessFromHtml(html)";
_pay_flag_ali = mostCurrent._alipay._getsuccessfromhtml(mostCurrent.activityBA,_html);
 //BA.debugLineNum = 795;BA.debugLine="If pay_Flag_ali <> -1 Then";
if (_pay_flag_ali!=-1) { 
 //BA.debugLineNum = 798;BA.debugLine="html = html.Replace(\"&amp;\",\"&\")";
_html = _html.replace("&amp;","&");
 //BA.debugLineNum = 800;BA.debugLine="parms_map = myutils.getOrderData(html)";
_parms_map = mostCurrent._myutils._getorderdata(mostCurrent.activityBA,_html);
 //BA.debugLineNum = 801;BA.debugLine="pay_load_url_flag = 1";
mostCurrent._pay_load_url_flag = BA.NumberToString(1);
 //BA.debugLineNum = 802;BA.debugLine="If parms_map <> Null And parms_map.Size > 0 The";
if (_parms_map!= null && _parms_map.getSize()>0) { 
 //BA.debugLineNum = 803;BA.debugLine="sb_url.Append(\"http://www.k91d.com/service/ind";
_sb_url.Append("http://www.k91d.com/service/index.php?head=0527&type=ter&data=");
 //BA.debugLineNum = 804;BA.debugLine="sb_url.Append(parms_map.Get(\"out_trade_no\")).A";
_sb_url.Append(BA.ObjectToString(_parms_map.Get((Object)("out_trade_no")))).Append(":");
 //BA.debugLineNum = 805;BA.debugLine="sb_url.Append(parms_map.Get(\"body\")).Append(\":";
_sb_url.Append(BA.ObjectToString(_parms_map.Get((Object)("body")))).Append(":");
 //BA.debugLineNum = 807;BA.debugLine="sb_url.Append(\"1\").Append(\":\")";
_sb_url.Append("1").Append(":");
 //BA.debugLineNum = 809;BA.debugLine="sb_url.Append(\"1\").Append(\":\")";
_sb_url.Append("1").Append(":");
 //BA.debugLineNum = 811;BA.debugLine="sb_url.Append(parms_map.Get(\"buyer_email\")).Ap";
_sb_url.Append(BA.ObjectToString(_parms_map.Get((Object)("buyer_email")))).Append(":");
 //BA.debugLineNum = 813;BA.debugLine="sb_url.Append(\"2\").Append(\":\")";
_sb_url.Append("2").Append(":");
 //BA.debugLineNum = 815;BA.debugLine="sb_url.Append(\"00000000001\").Append(\":\")";
_sb_url.Append("00000000001").Append(":");
 //BA.debugLineNum = 816;BA.debugLine="sb_url.Append(parms_map.Get(\"total_fee\"))";
_sb_url.Append(BA.ObjectToString(_parms_map.Get((Object)("total_fee"))));
 }else {
 //BA.debugLineNum = 818;BA.debugLine="sb_url.Append(\"http://www.k91d.com/service/ind";
_sb_url.Append("http://www.k91d.com/service/index.php?head=0527&type=ter&data=-1:-1:-1:-1:-1:-1:-1:-1");
 };
 //BA.debugLineNum = 821;BA.debugLine="wviewEx.addJavascriptInterface(wv_pay_Ser,\"B4A\"";
mostCurrent._wviewex.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._wv_pay_ser.getObject()),"B4A");
 //BA.debugLineNum = 822;BA.debugLine="wv_pay_Ser.LoadUrl(sb_url.ToString())";
mostCurrent._wv_pay_ser.LoadUrl(_sb_url.ToString());
 };
 };
 //BA.debugLineNum = 825;BA.debugLine="End Sub";
return "";
}
public static String  _processinit() throws Exception{
 //BA.debugLineNum = 874;BA.debugLine="Sub processInit";
 //BA.debugLineNum = 875;BA.debugLine="current_tab = 0";
_current_tab = (int) (0);
 //BA.debugLineNum = 876;BA.debugLine="order_price = 0";
_order_price = (int) (0);
 //BA.debugLineNum = 877;BA.debugLine="order_desc = \"\"";
mostCurrent._order_desc = "";
 //BA.debugLineNum = 878;BA.debugLine="qrCodeShow_ali = -1";
_qrcodeshow_ali = (int) (-1);
 //BA.debugLineNum = 879;BA.debugLine="qrCodeShow_weixin = -1";
_qrcodeshow_weixin = (int) (-1);
 //BA.debugLineNum = 880;BA.debugLine="pay_Flag_ali = -1";
_pay_flag_ali = (int) (-1);
 //BA.debugLineNum = 881;BA.debugLine="pay_Flag_weixin = -1";
_pay_flag_weixin = (int) (-1);
 //BA.debugLineNum = 882;BA.debugLine="temporary_price = 0";
_temporary_price = (int) (0);
 //BA.debugLineNum = 883;BA.debugLine="pay_load_url_flag = -1";
mostCurrent._pay_load_url_flag = BA.NumberToString(-1);
 //BA.debugLineNum = 884;BA.debugLine="load_photo_flag = -1";
_load_photo_flag = (int) (-1);
 //BA.debugLineNum = 885;BA.debugLine="qr_show_flag = -1";
_qr_show_flag = (int) (-1);
 //BA.debugLineNum = 886;BA.debugLine="End Sub";
return "";
}
public static String  _recevie_cmd(String _cmd_recevie) throws Exception{
 //BA.debugLineNum = 932;BA.debugLine="Sub recevie_Cmd(cmd_Recevie As String)";
 //BA.debugLineNum = 933;BA.debugLine="ToastMessageShow(cmd_Recevie,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(_cmd_recevie,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 934;BA.debugLine="Select cmd_Recevie.SubString2(10,16)";
switch (BA.switchObjectToInt(_cmd_recevie.substring((int) (10),(int) (16)),"03434f","030102","030201","030202","030203","030204","030205","030301","030302","040302","040303","040401","040402","030502","040501")) {
case 0:
 //BA.debugLineNum = 937;BA.debugLine="serial_Return_Data = 1";
_serial_return_data = (int) (1);
 break;
case 1:
 //BA.debugLineNum = 940;BA.debugLine="serial_Return_Data = 2";
_serial_return_data = (int) (2);
 break;
case 2:
case 3:
case 4:
case 5:
case 6:
 //BA.debugLineNum = 943;BA.debugLine="serial_Return_Data = 3";
_serial_return_data = (int) (3);
 break;
case 7:
 //BA.debugLineNum = 947;BA.debugLine="If cmd_Recevie.SubString2(18,20) == \"00\" Then";
if ((_cmd_recevie.substring((int) (18),(int) (20))).equals("00")) { 
 //BA.debugLineNum = 948;BA.debugLine="serial_Return_Data = 4";
_serial_return_data = (int) (4);
 };
 //BA.debugLineNum = 951;BA.debugLine="serial_Return_Data = 5";
_serial_return_data = (int) (5);
 break;
case 8:
 //BA.debugLineNum = 954;BA.debugLine="serial_Return_Data = 6";
_serial_return_data = (int) (6);
 break;
case 9:
 //BA.debugLineNum = 957;BA.debugLine="serial_Return_Data = 7";
_serial_return_data = (int) (7);
 break;
case 10:
 //BA.debugLineNum = 960;BA.debugLine="serial_Return_Data = 8";
_serial_return_data = (int) (8);
 break;
case 11:
 //BA.debugLineNum = 963;BA.debugLine="serial_Return_Data = 9";
_serial_return_data = (int) (9);
 break;
case 12:
 //BA.debugLineNum = 966;BA.debugLine="serial_Return_Data = 10";
_serial_return_data = (int) (10);
 break;
case 13:
 //BA.debugLineNum = 969;BA.debugLine="serial_Return_Data = 11";
_serial_return_data = (int) (11);
 break;
case 14:
 //BA.debugLineNum = 972;BA.debugLine="serial_Return_Data = 12";
_serial_return_data = (int) (12);
 break;
default:
 //BA.debugLineNum = 975;BA.debugLine="serial_Return_Data = 13";
_serial_return_data = (int) (13);
 break;
}
;
 //BA.debugLineNum = 977;BA.debugLine="End Sub";
return "";
}
public static String  _send_cmd(String _cmd) throws Exception{
byte[] _bytearray = null;
anywheresoftware.b4a.agraham.byteconverter.ByteConverter _astr = null;
 //BA.debugLineNum = 915;BA.debugLine="Sub send_Cmd(cmd As String)";
 //BA.debugLineNum = 916;BA.debugLine="Dim byteArray(0) As Byte";
_bytearray = new byte[(int) (0)];
;
 //BA.debugLineNum = 917;BA.debugLine="Dim aStr As ByteConverter";
_astr = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 920;BA.debugLine="If cmd.Length < 22 Then";
if (_cmd.length()<22) { 
 //BA.debugLineNum = 921;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 924;BA.debugLine="byteArray = aStr.HexToBytes(cmd)";
_bytearray = _astr.HexToBytes(_cmd);
 //BA.debugLineNum = 925;BA.debugLine="aSync.Write(byteArray)";
mostCurrent._async.Write(_bytearray);
 //BA.debugLineNum = 929;BA.debugLine="End Sub";
return "";
}
public static String  _serverhtml(String _html) throws Exception{
 //BA.debugLineNum = 727;BA.debugLine="Sub serverHTML(html As String)";
 //BA.debugLineNum = 729;BA.debugLine="End Sub";
return "";
}
public static String  _tasktime_tick() throws Exception{
 //BA.debugLineNum = 828;BA.debugLine="Sub taskTime_Tick";
 //BA.debugLineNum = 829;BA.debugLine="If pay_Flag_ali <> -1 Then";
if (_pay_flag_ali!=-1) { 
 //BA.debugLineNum = 830;BA.debugLine="If current_tab_count > 0 Then";
if (_current_tab_count>0) { 
 //BA.debugLineNum = 831;BA.debugLine="tht_index.CurrentTab = current_tab_count - 1";
mostCurrent._tht_index.setCurrentTab((int) (_current_tab_count-1));
 };
 //BA.debugLineNum = 833;BA.debugLine="processInit";
_processinit();
 //BA.debugLineNum = 834;BA.debugLine="taskTime.Enabled = False";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 838;BA.debugLine="If pay_Flag_weixin <> -1 Then";
if (_pay_flag_weixin!=-1) { 
 //BA.debugLineNum = 839;BA.debugLine="If current_tab_count > 0 Then";
if (_current_tab_count>0) { 
 //BA.debugLineNum = 840;BA.debugLine="tht_index.CurrentTab = current_tab_count - 1";
mostCurrent._tht_index.setCurrentTab((int) (_current_tab_count-1));
 };
 //BA.debugLineNum = 842;BA.debugLine="processInit";
_processinit();
 //BA.debugLineNum = 843;BA.debugLine="taskTime.Enabled = False";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 846;BA.debugLine="If qrCodeShow_ali <> 1 And qrCodeShow_weixin <> 1";
if (_qrcodeshow_ali!=1 && _qrcodeshow_weixin!=1) { 
 //BA.debugLineNum = 847;BA.debugLine="igv_pay.Bitmap = LoadBitmap(alipay.PHOTO_PATH,al";
mostCurrent._igv_pay.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._alipay._photo_path,mostCurrent._alipay._ali_logo_name).getObject()));
 }else {
 //BA.debugLineNum = 849;BA.debugLine="If qr_show_flag == 0 Then";
if (_qr_show_flag==0) { 
 //BA.debugLineNum = 850;BA.debugLine="If qrCodeShow_ali == 1 Then";
if (_qrcodeshow_ali==1) { 
 //BA.debugLineNum = 851;BA.debugLine="igv_pay.Bitmap = LoadBitmap(alipay.PHOTO_PATH,";
mostCurrent._igv_pay.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._alipay._photo_path,mostCurrent._alipay._qr_code_logo).getObject()));
 };
 }else if(_qr_show_flag==1) { 
 //BA.debugLineNum = 854;BA.debugLine="If qrCodeShow_weixin == 1 And pay_Flag_weixin =";
if (_qrcodeshow_weixin==1 && _pay_flag_weixin==-1) { 
 //BA.debugLineNum = 856;BA.debugLine="igv_pay.Bitmap = LoadBitmap(alipay.PHOTO_PATH,";
mostCurrent._igv_pay.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._alipay._photo_path,mostCurrent._weixinpay._weixin_logo_qrcode_name).getObject()));
 };
 }else if(_qr_show_flag==2) { 
 //BA.debugLineNum = 860;BA.debugLine="ToastMessageShow(\"公司\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("公司",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 862;BA.debugLine="qr_show_flag = -1";
_qr_show_flag = (int) (-1);
 };
 //BA.debugLineNum = 865;BA.debugLine="If load_photo_flag == 1 Then";
if (_load_photo_flag==1) { 
 //BA.debugLineNum = 866;BA.debugLine="igv_personalized.Bitmap = LoadBitmap(alipay.PHOT";
mostCurrent._igv_personalized.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._alipay._photo_path,mostCurrent._alipay._machine_qr_code_infi).getObject()));
 //BA.debugLineNum = 867;BA.debugLine="taskTime.Enabled = False";
_tasktime.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 869;BA.debugLine="igv_personalized.Bitmap = LoadBitmap(alipay.PHOT";
mostCurrent._igv_personalized.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(mostCurrent._alipay._photo_path,mostCurrent._alipay._ali_logo_name).getObject()));
 };
 //BA.debugLineNum = 871;BA.debugLine="End Sub";
return "";
}
public static String  _tht_index_tabchanged() throws Exception{
int _i = 0;
 //BA.debugLineNum = 667;BA.debugLine="Sub tht_index_TabChanged";
 //BA.debugLineNum = 669;BA.debugLine="If current_tab_count > 0 Then";
if (_current_tab_count>0) { 
 //BA.debugLineNum = 670;BA.debugLine="For i = 0 To current_tab_count Step 1";
{
final int step461 = (int) (1);
final int limit461 = _current_tab_count;
for (_i = (int) (0); (step461 > 0 && _i <= limit461) || (step461 < 0 && _i >= limit461); _i = ((int)(0 + _i + step461))) {
 //BA.debugLineNum = 671;BA.debugLine="If tht_index.CurrentTab == i Then";
if (mostCurrent._tht_index.getCurrentTab()==_i) { 
 //BA.debugLineNum = 672;BA.debugLine="lbl_index_tht.Text = tht_index_lbl_map.Get(\"pa";
mostCurrent._lbl_index_tht.setText(mostCurrent._tht_index_lbl_map.Get((Object)("page"+BA.NumberToString((_i+1)))));
 };
 }
};
 };
 //BA.debugLineNum = 676;BA.debugLine="End Sub";
return "";
}
public static String  _tht_select_tabchanged() throws Exception{
int _ss = 0;
int _i = 0;
 //BA.debugLineNum = 654;BA.debugLine="Sub tht_select_TabChanged";
 //BA.debugLineNum = 655;BA.debugLine="Dim ss As Int = 0";
_ss = (int) (0);
 //BA.debugLineNum = 656;BA.debugLine="ss = tht_select.TabCount";
_ss = mostCurrent._tht_select.getTabCount();
 //BA.debugLineNum = 657;BA.debugLine="If ss > 0 Then";
if (_ss>0) { 
 //BA.debugLineNum = 658;BA.debugLine="For i = 0 To ss Step 1";
{
final int step452 = (int) (1);
final int limit452 = _ss;
for (_i = (int) (0); (step452 > 0 && _i <= limit452) || (step452 < 0 && _i >= limit452); _i = ((int)(0 + _i + step452))) {
 //BA.debugLineNum = 659;BA.debugLine="If tht_select.CurrentTab == i Then";
if (mostCurrent._tht_select.getCurrentTab()==_i) { 
 //BA.debugLineNum = 660;BA.debugLine="lbl_select.Text = select_lbl_text.Get(\"page\"&(";
mostCurrent._lbl_select.setText(mostCurrent._select_lbl_text.Get((Object)("page"+BA.NumberToString((_i+1)))));
 };
 }
};
 };
 //BA.debugLineNum = 665;BA.debugLine="End Sub";
return "";
}
public static String  _weixinhtml(String _html) throws Exception{
anywheresoftware.b4a.objects.collections.Map _parms_map = null;
anywheresoftware.b4a.keywords.StringBuilderWrapper _sb_url = null;
 //BA.debugLineNum = 731;BA.debugLine="Sub weiXinHtml(html As String)";
 //BA.debugLineNum = 732;BA.debugLine="Dim parms_map As Map";
_parms_map = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 733;BA.debugLine="Dim sb_url As StringBuilder";
_sb_url = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 735;BA.debugLine="sb_url.Initialize()";
_sb_url.Initialize();
 //BA.debugLineNum = 736;BA.debugLine="parms_map.Initialize()";
_parms_map.Initialize();
 //BA.debugLineNum = 741;BA.debugLine="If pay_load_url_flag == 0 Then";
if ((mostCurrent._pay_load_url_flag).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 742;BA.debugLine="qrCodeShow_weixin = weixinpay.qrCodeFromHtml(htm";
_qrcodeshow_weixin = mostCurrent._weixinpay._qrcodefromhtml(mostCurrent.activityBA,_html);
 //BA.debugLineNum = 744;BA.debugLine="pay_Flag_weixin = alipay.getSuccessFromHtml(html";
_pay_flag_weixin = mostCurrent._alipay._getsuccessfromhtml(mostCurrent.activityBA,_html);
 //BA.debugLineNum = 747;BA.debugLine="If pay_Flag_weixin <> -1 Then";
if (_pay_flag_weixin!=-1) { 
 //BA.debugLineNum = 750;BA.debugLine="html = html.Replace(\"&amp;\",\"&\")";
_html = _html.replace("&amp;","&");
 //BA.debugLineNum = 752;BA.debugLine="parms_map = myutils.getOrderData(html)";
_parms_map = mostCurrent._myutils._getorderdata(mostCurrent.activityBA,_html);
 //BA.debugLineNum = 753;BA.debugLine="pay_load_url_flag = 1";
mostCurrent._pay_load_url_flag = BA.NumberToString(1);
 //BA.debugLineNum = 754;BA.debugLine="If parms_map <> Null And parms_map.Size > 0 The";
if (_parms_map!= null && _parms_map.getSize()>0) { 
 //BA.debugLineNum = 755;BA.debugLine="sb_url.Append(\"http://www.k91d.com/service/ind";
_sb_url.Append("http://www.k91d.com/service/index.php?head=0527&type=ter&data=");
 //BA.debugLineNum = 756;BA.debugLine="sb_url.Append(parms_map.Get(\"out_trade_no\")).A";
_sb_url.Append(BA.ObjectToString(_parms_map.Get((Object)("out_trade_no")))).Append(":");
 //BA.debugLineNum = 757;BA.debugLine="sb_url.Append(parms_map.Get(\"body\")).Append(\":";
_sb_url.Append(BA.ObjectToString(_parms_map.Get((Object)("body")))).Append(":");
 //BA.debugLineNum = 759;BA.debugLine="sb_url.Append(\"1\").Append(\":\")";
_sb_url.Append("1").Append(":");
 //BA.debugLineNum = 761;BA.debugLine="sb_url.Append(\"1\").Append(\":\")";
_sb_url.Append("1").Append(":");
 //BA.debugLineNum = 763;BA.debugLine="sb_url.Append(parms_map.Get(\"buyer_email\")).Ap";
_sb_url.Append(BA.ObjectToString(_parms_map.Get((Object)("buyer_email")))).Append(":");
 //BA.debugLineNum = 765;BA.debugLine="sb_url.Append(\"2\").Append(\":\")";
_sb_url.Append("2").Append(":");
 //BA.debugLineNum = 767;BA.debugLine="sb_url.Append(\"00000000001\").Append(\":\")";
_sb_url.Append("00000000001").Append(":");
 //BA.debugLineNum = 768;BA.debugLine="sb_url.Append(parms_map.Get(\"total_fee\"))";
_sb_url.Append(BA.ObjectToString(_parms_map.Get((Object)("total_fee"))));
 }else {
 //BA.debugLineNum = 770;BA.debugLine="sb_url.Append(\"http://www.k91d.com/service/ind";
_sb_url.Append("http://www.k91d.com/service/index.php?head=0527&type=ter&data=-1:-1:-1:-1:-1:-1:-1:-1");
 };
 //BA.debugLineNum = 773;BA.debugLine="wviewEx.addJavascriptInterface(wv_pay_Ser,\"B4A\"";
mostCurrent._wviewex.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._wv_pay_ser.getObject()),"B4A");
 //BA.debugLineNum = 774;BA.debugLine="wv_pay_Ser.LoadUrl(sb_url.ToString())";
mostCurrent._wv_pay_ser.LoadUrl(_sb_url.ToString());
 };
 };
 //BA.debugLineNum = 779;BA.debugLine="End Sub";
return "";
}
public static String  _wv_pay_pay_pagefinished(String _url) throws Exception{
String _jsstatement = "";
 //BA.debugLineNum = 716;BA.debugLine="Sub wv_pay_Pay_PageFinished (Url As String)";
 //BA.debugLineNum = 717;BA.debugLine="Dim jsStatement As String";
_jsstatement = "";
 //BA.debugLineNum = 718;BA.debugLine="jsStatement = \"B4A.CallSub('processHTML',false,do";
_jsstatement = "B4A.CallSub('processHTML',false,document.documentElement.outerHTML)";
 //BA.debugLineNum = 719;BA.debugLine="wviewEx.executeJavascript(wv_pay_Pay,jsStatement)";
mostCurrent._wviewex.executeJavascript((android.webkit.WebView)(mostCurrent._wv_pay_pay.getObject()),_jsstatement);
 //BA.debugLineNum = 720;BA.debugLine="End Sub";
return "";
}
public static String  _wv_pay_ser_pagefinished(String _url) throws Exception{
String _jsstatement = "";
 //BA.debugLineNum = 711;BA.debugLine="Sub wv_pay_Ser_PageFinished (Url As String)";
 //BA.debugLineNum = 712;BA.debugLine="Dim jsStatement As String";
_jsstatement = "";
 //BA.debugLineNum = 713;BA.debugLine="jsStatement = \"B4A.CallSub('serverHTML',false,doc";
_jsstatement = "B4A.CallSub('serverHTML',false,document.documentElement.outerHTML)";
 //BA.debugLineNum = 714;BA.debugLine="wviewEx.executeJavascript(wv_pay_Ser,jsStatement)";
mostCurrent._wviewex.executeJavascript((android.webkit.WebView)(mostCurrent._wv_pay_ser.getObject()),_jsstatement);
 //BA.debugLineNum = 715;BA.debugLine="End Sub";
return "";
}
public static String  _wv_pay_wei_pagefinished(String _url) throws Exception{
String _jsstatement = "";
 //BA.debugLineNum = 721;BA.debugLine="Sub wv_pay_Wei_PageFinished (Url As String)";
 //BA.debugLineNum = 722;BA.debugLine="Dim jsStatement As String";
_jsstatement = "";
 //BA.debugLineNum = 723;BA.debugLine="jsStatement = \"B4A.CallSub('weiXinHtml',false,doc";
_jsstatement = "B4A.CallSub('weiXinHtml',false,document.documentElement.outerHTML)";
 //BA.debugLineNum = 724;BA.debugLine="wviewEx.executeJavascript(wv_pay_Wei,jsStatement)";
mostCurrent._wviewex.executeJavascript((android.webkit.WebView)(mostCurrent._wv_pay_wei.getObject()),_jsstatement);
 //BA.debugLineNum = 725;BA.debugLine="End Sub";
return "";
}
}
