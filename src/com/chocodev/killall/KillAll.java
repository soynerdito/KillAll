package com.chocodev.killall;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.util.Log;
import android.widget.Toast;

public class KillAll extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ActivityManager actManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        MemoryInfo outInfo = new MemoryInfo();
        
        List<RunningAppProcessInfo> procList = null;
        ArrayList<Integer> Pid2KillList = new ArrayList<Integer>();
        
        procList = actManager.getRunningAppProcesses();
        
        if( procList != null ){
        	//Enumerate process
        	int iMax = procList.size();
        	RunningAppProcessInfo currProcess;
        	for( int i=0; i< iMax;i++){
        		currProcess = procList.get(i);
        		        		
        		Pid2KillList.add(currProcess.pid);
        		Log.d("TAG", currProcess.processName);
        		if( i>0 ){
        			actManager.killBackgroundProcesses(currProcess.processName);
        		}
        		
        		//Toast.makeText(this,currProcess.processName,Toast.LENGTH_SHORT);
        		
        		
        	}
        	
        	Toast.makeText(getApplicationContext(),"kill phone",Toast.LENGTH_LONG).show();
        	//Lopp for killing
        	
        }
        actManager.getMemoryInfo( outInfo);
        
        System.exit(0);
	}


}
