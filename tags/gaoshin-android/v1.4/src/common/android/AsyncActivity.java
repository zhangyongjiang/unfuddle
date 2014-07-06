package common.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class AsyncActivity extends Activity {
	
	private TasksThread tasksThread;
	private Handler activityHandler;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        activityHandler = new Handler();
        tasksThread = new TasksThread();
        tasksThread.start();
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		tasksThread.requestStop();	
    }

    protected synchronized void addTask(final Runnable longRunTask, final Runnable uiCallback) {
        tasksThread.addTask(longRunTask, uiCallback);
    }

    private class TasksThread extends Thread {
        private Handler threadHandler;
        
        public TasksThread() {
        }
        
        @Override
        public void run() {
            try {
                Looper.prepare();
                threadHandler = new Handler();
                Looper.loop();
            } catch (Throwable t) {
            } 
        }
        
        // This method is allowed to be called from any thread
        public synchronized void requestStop() {
            threadHandler.post(new Runnable() {
                @Override
                public void run() {
                    // This is guaranteed to run on the DownloadThread
                    // so we can use myLooper() to get its looper
                    Looper.myLooper().quit();
                }
            });
        }
        
        protected synchronized void addTask(final Runnable task, final Runnable uiCallback) {
            threadHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        task.run();
                    } finally {                 
                        activityHandler.post(uiCallback);
                    }               
                }
            });
        }
    }
}
