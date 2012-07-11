package com.chocodev.killall;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class KillAllActivity extends Activity {
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ActivityManager actManager = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        MemoryInfo orgMemory = new MemoryInfo();
        MemoryInfo finMemory = new MemoryInfo();
        
        List<RunningAppProcessInfo> procList = null;
        StringBuilder ProcNames = new StringBuilder();
        
        
        //ArrayList<Integer> Pid2KillList = new ArrayList<Integer>();
        procList = actManager.getRunningAppProcesses();        
        if( procList != null ){
        	//Enumerate process
        	int iMax = procList.size();
        	RunningAppProcessInfo currProcess;
        	//Get Running Memory Information
        	actManager.getMemoryInfo(orgMemory);
        	String EOL = "";
        	PackageManager pm = this.getPackageManager();
        	String AppName;
        	for( int i=0; i< iMax;i++){
        		currProcess = procList.get(i);
        				
        		//Pid2KillList.add(currProcess.pid);
        		if( currProcess.pid != this.getApplicationInfo().uid
        			&& currProcess.importance != RunningAppProcessInfo.IMPORTANCE_SERVICE
        			&& currProcess.importanceReasonCode != RunningAppProcessInfo.REASON_PROVIDER_IN_USE ){
        			actManager.killBackgroundProcesses(currProcess.processName);
        			actManager.restartPackage(currProcess.processName);
        			ProcNames.append(EOL);
        			ProcNames.append(" *");
        			
        			try {
        				AppName = (String) pm.getApplicationLabel(pm.getApplicationInfo(currProcess.processName, PackageManager.GET_META_DATA)).toString();
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						AppName = currProcess.processName;
					}
        			
        			ProcNames.append(AppName);
        			EOL = "\n";
        		}
        		       		
        		//Toast.makeText(this,currProcess.processName,Toast.LENGTH_SHORT);	
        	}
        	ProcNames.append(EOL);
        	new Thread(new Runnable() {
	            @Override
	            public void run() {
	                try {
	                    Thread.sleep(100);
	                }
	                catch (Exception e) { }
	                //System.exit(0);
	            }
	        }).start();
        	actManager.getMemoryInfo( finMemory);
        	//Prepare Message to Show        	
        	ShowFinalToast(orgMemory, finMemory, ProcNames.toString() );
        	//adBlockHandler.sendEmptyMessage(0);
        	new Thread(new Runnable() {
	            @Override
	            public void run() {
	                try {
	                    Thread.sleep(3500);
	                }
	                catch (Exception e) { }
	                System.exit(0);
	            }
	        }).start();
        }
        
        
        finish();        
       //System.exit(0);
    }
	
	
    private void ShowFinalToast(MemoryInfo startMem, MemoryInfo finMem, String ProcNames ){
		String Message;
		long FreedMem;
		FreedMem = (startMem.availMem - finMem.availMem)/1024;
    	Message = "Freed Memory Total = " + String.valueOf(FreedMem/1024) + " MB";
    	Message += "\n" + ProcNames ;
    	//Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG).show();
    	
    	LayoutInflater inflater = getLayoutInflater();
    	View layout = inflater.inflate(R.layout.reporttoast,
    	                               (ViewGroup) findViewById(R.id.toast_layout_root));

    	ImageView image = (ImageView) layout.findViewById(R.id.image);
    	image.setImageResource(R.drawable.blobwars);
    	TextView text = (TextView) layout.findViewById(R.id.text);
    	text.setText(Message);
    	Toast toast = new Toast(getApplicationContext());
    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);    	
    	toast.setDuration(Toast.LENGTH_LONG);
    	toast.setView(layout);
    	toast.show();
	}
}