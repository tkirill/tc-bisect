package org.tkirill.teamcity.bisectPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bisect {
    public Bisect(long buildId) {
        this.buildId = buildId;
        builds = new ArrayList<>();
    }

    public long buildId;
    public List<BisectBuild> builds;
    public boolean isFinished;
}
