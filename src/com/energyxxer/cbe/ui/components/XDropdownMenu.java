package com.energyxxer.cbe.ui.components;

import com.energyxxer.cbe.ui.components.factory.Factory;
import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class XDropdownMenu<T> extends XButton implements ActionListener {

    protected ArrayList<T> options = new ArrayList<>();

    protected int selected = -1;

    protected Factory<JPopupMenu> popupFactory = JPopupMenu::new;
    protected Factory<JMenuItem> itemFactory = JMenuItem::new;

    private ArrayList<ChoiceListener<T>> choiceListeners = new ArrayList<>();

    public XDropdownMenu() {
        super(" ");
    }

    public XDropdownMenu(T[] options) {
        super(" ");
        setOptions(options);
    }

    {
        this.addActionListener(this);
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    public void setOptions(T[] options) {
        this.options.clear();
        addOptions(options);
    }

    public void addOption(T option) {
        this.options.add(option);
        updateOptions();
    }

    public void addOptions(T[] options) {
        for(T o : options) {
            addOption(o);
        }
    }

    protected void updateOptions() {
        if(selected == -1 && options.size() > 0) {
            selected = 0;
            this.setText(options.get(0).toString());
        } else {
            this.setText(options.get(selected).toString());
        }
    }

    public void setPopupFactory(@NotNull Factory<JPopupMenu> f) {
        this.popupFactory = f;
    }

    public void setPopupItemFactory(@NotNull Factory<JMenuItem> f) {
        this.itemFactory = f;
    }

    public void addChoiceListener(@NotNull ChoiceListener<T> l) {choiceListeners.add(l);}

    private void registerChoice(int index) {
        selected = index;
        updateOptions();
        T selected = options.get(index);
        for(ChoiceListener<T> listener : choiceListeners) listener.onChoice(selected);
    }

    public T getValue() {
        if(selected < 0) return null;
        if(selected >= options.size()) return null;
        return options.get(selected);
    }

    public void setValue(T value) {
        int index = options.indexOf(value);
        System.out.println(options);
        System.out.println(value);
        System.out.println(index);
        if(index >= 0) {
            selected = index;
            updateOptions();
        }
    }

    public void clear() {
        selected = -1;
        options.clear();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JPopupMenu pm = popupFactory.createInstance();
        //pm.setMaximumSize(new Dimension(this.getWidth(),Integer.MAX_VALUE));

        int height = 2;

        for(int i = 0; i < options.size(); i++) {
            T option = options.get(i);
            JMenuItem item = itemFactory.createInstance();
            item.setText(option.toString());
            int choice = i;
            item.addActionListener(arg0 -> registerChoice(choice));
            pm.add(item);
            height += item.getPreferredSize().getHeight();
        }

        pm.setPreferredSize(new Dimension(this.getWidth(),height));
        pm.show(this,this.getX(),this.getY()-9);
    }
}
