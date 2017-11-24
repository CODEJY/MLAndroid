package org.tensorflow.demo.android;

import android.util.Log;

/**
 * Created by wujy on 2017/11/22.
 */

public class SingleIns {
    private SingleIns(){
    }
    private static class Holder {
        private static SingleIns singleIns = new SingleIns();
    }
    public static SingleIns getInstance() {
        return Holder.singleIns;
    }
    public volatile  boolean isSend = false;
}
