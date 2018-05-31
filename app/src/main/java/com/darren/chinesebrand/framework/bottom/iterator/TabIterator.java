package com.darren.chinesebrand.framework.bottom.iterator;

import com.darren.chinesebrand.framework.bottom.BottomTabItem;

/**
 * Created by hcDarren on 2017/10/22.
 */

public interface TabIterator {
    BottomTabItem next();

    boolean hashNext();
}
