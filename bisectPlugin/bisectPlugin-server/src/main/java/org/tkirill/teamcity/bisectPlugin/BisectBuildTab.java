package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.serverSide.BuildCustomizer;
import jetbrains.buildServer.serverSide.BuildCustomizerFactory;
import jetbrains.buildServer.serverSide.BuildsManager;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.web.openapi.BuildTab;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BisectBuildTab extends BuildTab {
    private BuildCustomizerFactory customizerFactory;

    protected BisectBuildTab(WebControllerManager manager, BuildsManager buildManager, PluginDescriptor myDescriptor, BuildCustomizerFactory customizerFactory) {
        super("bisectBuildTab", "Bisect", manager, buildManager, myDescriptor.getPluginResourcesPath("BisectBuildTab.jsp"));
        this.customizerFactory = customizerFactory;
    }

    @Override
    protected boolean isAvailableFor(@NotNull SBuild build) {
        return build.getBuildStatus().isFailed() && build.getContainingChanges().size() > 1;
    }

    @Override
    protected void fillModel(@NotNull Map<String, Object> model, @NotNull SBuild build) {
        BuildCustomizer buildCustomizer = customizerFactory.createBuildCustomizer(build.getBuildType(), null);
        buildCustomizer.setParameters(build.getBuildType().getParameters());
        buildCustomizer.setChangesUpTo(build.getContainingChanges().get(build.getContainingChanges().size()-1));
        buildCustomizer.createPromotion().addToQueue("bisect");
    }
}
