package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.buildTriggers.BuildTriggerDescriptor;
import jetbrains.buildServer.buildTriggers.BuildTriggerService;
import jetbrains.buildServer.buildTriggers.BuildTriggeringPolicy;
import jetbrains.buildServer.serverSide.BuildCustomizerFactory;
import jetbrains.buildServer.serverSide.SBuildServer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class BisectBuildTrigger extends BuildTriggerService {
    private SBuildServer server;
    private BuildCustomizerFactory customizerFactory;

    public BisectBuildTrigger(SBuildServer server, BuildCustomizerFactory customizerFactory) {
        this.server = server;
        this.customizerFactory = customizerFactory;
    }

    @NotNull
    @Override
    public String getName() {
        return "bisectBuildTrigger";
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Bisect build trigger";
    }

    @NotNull
    @Override
    public String describeTrigger(@NotNull BuildTriggerDescriptor trigger) {
        return "Waits for bisect requests";
    }

    @NotNull
    @Override
    public BuildTriggeringPolicy getBuildTriggeringPolicy() {
        return new BisectBuildTriggerPolicy(server, customizerFactory);
    }
}
