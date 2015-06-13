package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.web.openapi.BuildTab;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BisectBuildTab extends BuildTab {
    public BisectBuildTab(WebControllerManager manager, BuildsManager buildManager, PluginDescriptor myDescriptor) {
        super("bisectBuildTab", "Bisect", manager, buildManager, myDescriptor.getPluginResourcesPath("BisectBuildTab.jsp"));
        addJsFile(myDescriptor.getPluginResourcesPath("js/bisect.js"));
    }

    @Override
    protected boolean isAvailableFor(@NotNull SBuild build) {
        //TODO check for BisectBuildTrigger in configuration
        return build.getBuildStatus().isFailed() && build.getContainingChanges().size() > 1;
    }

    @Override
    protected void fillModel(@NotNull Map<String, Object> model, @NotNull SBuild build) {
        model.put("buildId", build.getBuildId());
        CustomDataStorage storage = build.getBuildType().getCustomDataStorage("bisectPlugin");
        BisectRepository repository = new BisectRepository(storage);
        boolean isRunning = repository.exists(build.getBuildId());
        model.put("isRunning", isRunning);
    }
}
