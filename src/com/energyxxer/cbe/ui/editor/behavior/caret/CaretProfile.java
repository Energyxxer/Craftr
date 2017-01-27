package com.energyxxer.cbe.ui.editor.behavior.caret;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by User on 1/10/2017.
 */
public class CaretProfile implements Iterable<Integer> {

    private ArrayList<Integer> list = new ArrayList<>();

    public CaretProfile() {}

    public CaretProfile(Collection<? extends Integer> dots) {
        list.addAll(dots);
    }

    public CaretProfile(Integer... dots) {
        list.addAll(Arrays.asList(dots));
    }

    public boolean add(Integer dot, Integer mark) {
        list.add(dot);
        list.add(mark);
        return true;
    }

    public boolean add(Dot dot) {
        return add(dot.index, dot.mark);
    }

    public Integer get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public boolean contains(Integer o) {
        return list.contains(o);
    }

    public int indexOf(Integer o) {
        return list.indexOf(o);
    }

    public void clear() {
        list.clear();
    }

    public boolean addAll(Collection<? extends Integer> c) {
        return c.size() % 2 == 0 && list.addAll(c);
    }

    public boolean addAllDots(Collection<? extends Dot> c) {
        for(Dot dot : c) {
            add(dot);
        }
        return true;
    }

    public void sort() {
        this.bSort();
    }

    public Iterator<Integer> iterator() {
        return list.iterator();
    }

    public List<Integer> asList() {
        return list;
    }

    @Override
    public String toString() {
        return list.toString();
    }

    private void bSort() {
        int changes = 1;
        while(changes > 0) {
            changes = 0;
            for (int i = 0; i < list.size() - 2; i += 2) {
                if (list.get(i) > list.get(i + 2)) {
                    int d1 = list.get(i),
                            m1 = list.get(i + 1),
                            d2 = list.get(i + 2),
                            m2 = list.get(i + 3);
                    list.set(i, d2);
                    list.set(i + 1, m2);
                    list.set(i + 2, d1);
                    list.set(i + 3, m1);
                    changes++;
                }
            }
        }
    }
}
