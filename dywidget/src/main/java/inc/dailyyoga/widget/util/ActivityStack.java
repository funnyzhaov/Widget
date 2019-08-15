package inc.dailyyoga.widget.util;


import android.app.Activity;
import android.util.Log;


import java.util.Stack;



public class ActivityStack {

    private static final String TAG = "ActivityStack";
    private static Stack<Activity> mActivityStack=new Stack<>();

    private ActivityStack() {
    }

    public static void push(Activity activity) {
        mActivityStack.add(activity);
    }

    public static void pop(Activity activity) {
        mActivityStack.remove(activity);
    }

    public static void finishAllActivity() {
        for (Activity activity : mActivityStack) {
            Log.d(TAG, activity.getClass().getName());
            activity.finish();
        }
    }

    public static Activity finishOtherActivity(String className) {
        Activity result = null;
        for (Activity activity : mActivityStack) {
            if (activity.getClass().getName().equals(className)) {
                result = activity;
            } else {
                activity.finish();
            }
        }
        return result;
    }

    public static boolean finishActivity(String className) {
        if (mActivityStack.isEmpty()) return false;
        boolean result = false;
        for (Activity activity : mActivityStack) {
            if (activity.getClass().getName().equals(className)) {
                activity.finish();
                result = true;
            }
        }
        return result;
    }

    public static boolean hasActivity(String className) {
        if (mActivityStack.isEmpty()) return false;
        for (Activity activity : mActivityStack) {
            if (activity.getClass().getName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    public static int getActivitySize(){
        int ActivitySize = 0;
        if (mActivityStack != null && mActivityStack.size() > 0) {
            ActivitySize =   mActivityStack.size();
        }
        return ActivitySize;
    }

}
