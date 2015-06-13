package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.buildTriggers.BuildTriggerDescriptor;
import jetbrains.buildServer.serverSide.BuildsManager;
import jetbrains.buildServer.serverSide.CustomDataStorage;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.BuildTab;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BisectBuildTab extends BuildTab {
    private SBuildServer server;

    public BisectBuildTab(WebControllerManager manager, BuildsManager buildManager, PluginDescriptor myDescriptor, SBuildServer server) {
        super("bisectBuildTab", "Bisect", manager, buildManager, myDescriptor.getPluginResourcesPath("BisectBuildTab.jsp"));
        this.server = server;
        addJsFile(myDescriptor.getPluginResourcesPath("js/bisect.js"));
    }

    @Override
    protected boolean isAvailableFor(@NotNull SBuild build) {
        //TODO check that previous change is successful
        BuildTriggerDescriptor bisectBuildTrigger = build.getBuildType().findTriggerById("bisectBuildTrigger");
        CustomDataStorage storage = build.getBuildType().getCustomDataStorage("bisectPlugin");
        BisectRepository repository = new BisectRepository(storage);

        boolean hasBisectBuildTrigger = bisectBuildTrigger != null;
        boolean failedBuild = build.getBuildStatus().isFailed();
        boolean multipleChanges = build.getContainingChanges().size() > 1;
        boolean hasBisect = repository.exists(build.getBuildId());
        return (hasBisectBuildTrigger && failedBuild && multipleChanges) || hasBisect;
    }

    @Override
    protected void fillModel(@NotNull Map<String, Object> model, @NotNull SBuild build) {
        model.put("buildId", build.getBuildId());
        CustomDataStorage storage = build.getBuildType().getCustomDataStorage("bisectPlugin");
        BisectRepository repository = new BisectRepository(storage);
        Bisect bisect = repository.get(build.getBuildId());
        model.put("bisect", bisect);

        List<SBuild> historyRecords = new ArrayList<>();
        if (bisect != null) {
            for (BisectBuild bisectBuild : bisect.getBuilds()) {
                historyRecords.add(server.findBuildInstanceById(bisectBuild.getBuildId()));
            }
        }
        model.put("historyRecords", historyRecords);
    }
}
