package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.buildTriggers.BuildTriggerException;
import jetbrains.buildServer.buildTriggers.PolledBuildTrigger;
import jetbrains.buildServer.buildTriggers.PolledTriggerContext;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.*;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class BisectBuildTriggerPolicy extends PolledBuildTrigger {
    private static final Logger logger = Logger.getLogger(Loggers.SERVER_CATEGORY + BisectBuildTriggerPolicy.class);
    private SBuildServer server;
    private BuildCustomizerFactory customizerFactory;

    public BisectBuildTriggerPolicy(SBuildServer server, BuildCustomizerFactory customizerFactory) {
        this.server = server;
        this.customizerFactory = customizerFactory;
    }

    @Override
    public void triggerBuild(@NotNull PolledTriggerContext context) throws BuildTriggerException {
        logger.info("triggerBuild start");
        CustomDataStorage storage = context.getBuildType().getCustomDataStorage("bisectPlugin");
        BisectRepository repository = new BisectRepository(storage);
        BisectBuildTriggerHelper triggerBuildHelper = new BisectBuildTriggerHelper(server, customizerFactory, repository);
        triggerBuildHelper.triggerBuild(context);
        logger.info("triggerBuild finish");
    }
}
