package com.juliedeng.mdbsocials;

import java.util.Comparator;

/**
 * Created by juliedeng on 2/22/18.
 */

public class SocialComparator implements Comparator<Social> {
    @Override
    public int compare(Social o1, Social o2) {
        return o1.compareTo(o2);
    }
}
