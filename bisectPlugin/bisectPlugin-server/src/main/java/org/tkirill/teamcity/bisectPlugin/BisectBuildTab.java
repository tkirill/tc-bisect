package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.serverSide.BuildsManager;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.web.openapi.BuildTab;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BisectBuildTab extends BuildTab {
    protected BisectBuildTab(WebControllerManager manager, BuildsManager buildManager, PluginDescriptor myDescriptor) {
        super("bisectBuildTab", "Bisect", manager, buildManager, myDescriptor.getPluginResourcesPath("BisectBuildTab.jsp"));
    }

    @Override
    protected boolean isAvailableFor(@NotNull SBuild build) {
        return build.getBuildStatus().isFailed();
    }

    @Override
    protected void fillModel(@NotNull Map<String, Object> model, @NotNull SBuild build) {

    }
}
