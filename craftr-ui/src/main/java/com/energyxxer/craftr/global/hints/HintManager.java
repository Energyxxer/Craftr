package com.energyxxer.craftr.global.hints;

import com.energyxxer.craftr.main.window.CraftrWindow;

import javax.swing.JFrame;
import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HintManager {
    private final JFrame owner;
    private ArrayList<Hint> hints = new ArrayList<>();
    private Timer timer = new Timer();

    private static final int FADE_DISTANCE = 30;
    private static final int FORCE_HIDE_DISTANCE = 200;

    public HintManager(JFrame owner) {
        this.owner = owner;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for(int i = 0; i < hints.size(); i++) {
                    Hint hint = hints.get(i);
                    if(hint.disposed) {
                        hints.remove(i);
                        i--;
                        continue;
                    }
                    if(hint.timer < 0) {
                        hint.timer++;
                        if(!hint.shouldContinueShowing()) {
                            CraftrWindow.setStatus("Canceled fade-in");
                            hint.timer = 0;
                            continue;
                        }
                        if(hint.timer == 0) {
                            CraftrWindow.setStatus("Force showing");
                            hint.forceShow();
                        }
                    } else if(hint.timer == 0) {
                        if(hint.isShowing()) {
                            double distance = hint.getDistanceFromPoint(MouseInfo.getPointerInfo().getLocation());
                            if(distance >= FADE_DISTANCE) {
                                if(!hint.shouldContinueShowing()) {
                                    CraftrWindow.setStatus("Started fade-out");
                                    hint.timer = hint.OUT_DELAY;
                                }
                            }
                        }
                    } else {
                        double distance = hint.getDistanceFromPoint(MouseInfo.getPointerInfo().getLocation());
                        if(distance >= FORCE_HIDE_DISTANCE) {
                            CraftrWindow.setStatus("Dismissed due to distance");
                            hint.timer = 0;
                            hint.dismiss();
                            continue;
                        }
                        if(hint.shouldContinueShowing()) {
                            CraftrWindow.setStatus("Canceled fade-out");
                            hint.timer = 0;
                            continue;
                        }
                        hint.timer--;
                        if(hint.timer == 0) {
                            CraftrWindow.setStatus("Dismissed due to timeout");
                            hint.dismiss();
                        }
                    }
                }
            }
        }, 0, 10);
    }

    public TextHint createTextHint(String text) {
        TextHint newHint = new TextHint(owner, text);
        this.hints.add(newHint);
        return newHint;
    }
}
