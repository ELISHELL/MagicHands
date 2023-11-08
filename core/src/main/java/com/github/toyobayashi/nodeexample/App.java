package com.github.toyobayashi.nodeexample;

import android.app.Application;

public class App extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    NodeJs.initialize();
  }
}
