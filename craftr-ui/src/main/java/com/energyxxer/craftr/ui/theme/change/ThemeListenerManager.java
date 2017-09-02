package com.energyxxer.craftr.ui.theme.change;

import com.energyxxer.craftr.ui.common.Disposable;

import java.util.ArrayList;
import java.util.List;

public class ThemeListenerManager implements Disposable {
    private List<ThemeChangeListener> listeners = new ArrayList<>();

    public void addThemeChangeListener(ThemeChangeListener l) {
        addThemeChangeListener(l, false);
    }
    public void addThemeChangeListener(ThemeChangeListener l, boolean priority) {
        ThemeChangeListener.addThemeChangeListener(l, priority);
        listeners.add(l);
    }

    @Override
    public void dispose() {
        System.out.println("Disposing of " + listeners.size() + " listeners");
        listeners.forEach(ThemeChangeListener::dispose);
        listeners.clear();
        listeners = null;
    }
}
