package org.Logic;

import java.util.Comparator;

/**
 * The type Journey distance comparator.
 */
public class JourneyDistComparator implements Comparator<OnBoardComputer> {

    @Override
    public int compare(OnBoardComputer obc1, OnBoardComputer obc2) {
        return Float.compare(obc1.getJourneyDistance(), obc2.getJourneyDistance());
    }
}
