package com.gaoshin.top;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.quartz.CronExpression;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.gaoshin.sorma.AndroidContentResolver;
import com.gaoshin.sorma.AnnotatedORM;

public class TimerService extends IntentService {
    private static final String TAG = TimerService.class.getSimpleName();

    private AnnotatedORM orm = TopContentProvider.orm;

    public TimerService() {
        super(TimerService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        orm.setContentResolver(new AndroidContentResolver(getContentResolver()));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "instanceCounter " + TopApplication.instanceCounter + ", appCounter " + TopApplication.appCounter);
        if (ShortcutActivity.running()) {
            Log.i(TAG, "EDIT mode. Skip TimerService.");
            return;
        }
        
//        checkOrientation();

        checkJobs();
    }

    private void checkOrientation() {
        Log.i(TAG, "check orientation.");
        com.gaoshin.top.Configuration conf = getApp().getConfService().get("Orientation", "Portrait");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (conf.getValue().equals("Portrait")) {
                getApp().hideShortcutBars();
            } else {
                getApp().displayShortcutBars();
            }
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (conf.getValue().equals("Landscape")) {
                getApp().hideShortcutBars();
            } else {
                getApp().displayShortcutBars();
            }
        }
	}

	private void checkJobs() {
        List<Job> jobs = orm.getObjectList(Job.class, null, null);
        for (Job job : jobs) {
            try {
                checkJob(job);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkJob(Job job) throws ParseException {
        String cron = job.getCron();
        String expr = "0 " + cron;
        CronExpression ce = new CronExpression(expr);
        Date now = new Date();
        Date next = ce.getNextValidTimeAfter(now);
        if (next == null) {
            orm.delete(JobExecution.class, "job=" + job.getId(), null);
            return;
        }

        Shortcut shortcut = orm.getObject(Shortcut.class, "_id=" + job.getShortcut(), null);
        ShortcutGroup group = orm.getObject(ShortcutGroup.class, "_id=" + shortcut.getGroupId(), null);
        if (!group.isEnabled()) {
            return;
        }

        for (int i = 0; i < 5; i++) {
            JobExecution execution = orm.getObject(JobExecution.class, "job=" + job.getId() + " and startTime=" + next.getTime(), null);
            if (execution == null) {
                execution = new JobExecution();
                execution.setJob(job.getId());
                execution.setStartTime(next.getTime());
                execution.setStatus(JobStatus.Idle);
                Log.i(TAG, "Schedul job " + shortcut.getPkg() + " at time " + next);
                orm.insert(execution);
            }
            next = ce.getNextValidTimeAfter(next);
            if (next == null) {
                break;
            }
        }

        List<JobExecution> execs = orm.getObjectList(JobExecution.class, "job=" + job.getId(), null);
        long currentTime = System.currentTimeMillis();
        now = new Date();
        for (JobExecution je : execs) {
            if (JobStatus.Idle.equals(je.getStatus()) && je.getStartTime() < currentTime) {
                orm.delete(je);
                try {
                    Log.i(TAG, "Execute job " + shortcut.getPkg() + " at " + now);
                    shortcut.exec(getBaseContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private TopApplication getApp() {
        return (TopApplication) getApplication();
    }

}
