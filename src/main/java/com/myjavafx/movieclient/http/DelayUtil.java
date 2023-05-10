package com.myjavafx.movieclient.http;


import javafx.application.Platform;

public class DelayUtil {

    /**
     * 推迟几秒，并在UI线程执行
     *
     * @param delay
     * @param runnable
     */
    public static void delayRunToUIThread(long delay, Runnable runnable) {
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(delay);
                            Platform.runLater(runnable);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        thread.start();
    }
}
