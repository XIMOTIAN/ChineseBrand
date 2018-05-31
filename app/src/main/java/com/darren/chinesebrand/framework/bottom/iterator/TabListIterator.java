package com.darren.chinesebrand.framework.bottom.iterator;

import com.darren.chinesebrand.framework.bottom.BottomTabItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hcDarren on 2017/10/22.
 */

public class TabListIterator<T extends BottomTabItem> implements TabIterator{
    private List<T> mTabItems;
    private int index;

    public TabListIterator(){
        mTabItems = new ArrayList<>();
    }

    public void addItem(T item){
        mTabItems.add(item);
    }

    @Override
    public BottomTabItem next() {
        return mTabItems.get(index++);
    }

    @Override
    public boolean hashNext() {
        return index < mTabItems.size();
    }
}
