package com.forbitbd.tasker.ui.gantt;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

public class MyChromeClient extends WebChromeClient {

    @Override
    public boolean onConsoleMessage(ConsoleMessage cm)
    {
        Log.d("CONTENT", String.format("%s @ %d: %s",
                cm.message(), cm.lineNumber(), cm.sourceId()));
        return true;
    }
}
