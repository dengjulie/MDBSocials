package com.juliedeng.mdbsocials;

import java.util.Comparator;

/**
 * Comparator class to override the compare method so that socials are compared by
 * and subsequently ordered by the posting time.
 */

public class SocialComparator implements Comparator<Social> {
    @Override
    public int compare(Social o1, Social o2) {
        return o1.compareTo(o2);
    }
}
