package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.buildTriggers.BuildTriggerException;
import jetbrains.buildServer.buildTriggers.PolledBuildTrigger;
import jetbrains.buildServer.buildTriggers.PolledTriggerContext;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.vcs.SVcsModification;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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

        Bisect[] notFinished = repository.getAllNotFinished();
        logger.info("notFinished: " + notFinished.length);
        for (Bisect bisect : notFinished) {
            logger.info("bisect start " + bisect.getBuildId());
            if (bisect.getBuilds().size() != 0) {
                logger.info("bisect has builds");
                BisectBuild lastBuild = bisect.getBuilds().get(bisect.getBuilds().size() - 1);
                SBuild build = server.findBuildInstanceById(lastBuild.getBuildId());
                if (build != null) {
                    if (build.isFinished()) {
                        logger.info("previous build finished");
                        if (build.getBuildStatus().isSuccessful()) {
                            logger.info("previous build success");
                            int mid = getMid(lastBuild);
                            int nextLeft = mid + 1;
                            int nextRight = lastBuild.getRight();
                            nextBuild(repository, bisect, build, nextLeft, nextRight);
                        } else {
                            logger.info("previous build fail");
                            int mid = getMid(lastBuild);
                            nextBuild(repository, bisect, build, lastBuild.getLeft(), mid);
                        }
                    }
                } else {
                    logger.warn("Unknown build " + lastBuild.getBuildId());
                }
            } else {
                logger.info("bisect hasn't builds");
                SBuild build= server.findBuildInstanceById(bisect.getBuildId());
                nextBuild(repository, bisect, build, 0, build.getContainingChanges().size());
            }
            logger.info("bisect finish " + bisect.getBuildId());
        }
        logger.info("triggerBuild finish");
    }

    private void nextBuild(BisectRepository repository, Bisect bisect, SBuild build, int nextLeft, int nextRight) {
        logger.info("nextBuild " + nextLeft + " - " + nextRight);
        if (nextRight <= nextLeft) {
            logger.info("bisect DONE!");
            bisect.setIsFinished(true);
        } else {
            int nextMid = getMid(nextLeft, nextRight);
            SQueuedBuild sQueuedBuild = queueBuild(build, nextMid);
            BisectBuild nextBuild = new BisectBuild();
            nextBuild.setLeft(nextLeft);
            nextBuild.setRight(nextRight);
            nextBuild.setBuildId(Long.parseLong(sQueuedBuild.getItemId()));
            logger.info("next build" + nextBuild.getBuildId() + ": " + nextBuild.getLeft() + " - " + nextBuild.getRight());
            bisect.addBuild(nextBuild);
        }
        try {
            logger.info("save build");
            repository.save(bisect);
        } catch (IOException e) {
            logger.info("save build error");
        }
    }

    private SQueuedBuild queueBuild(SBuild build, int nextMid) {
        SVcsModification vcsModification = build.getContainingChanges().get(nextMid);
        BuildCustomizer buildCustomizer = customizerFactory.createBuildCustomizer(build.getBuildType(), null);
        buildCustomizer.setParameters(build.getBuildType().getParameters());
        buildCustomizer.setChangesUpTo(vcsModification);
        return buildCustomizer.createPromotion().addToQueue("bisect");
    }

    private int getMid(BisectBuild lastBuild) {
        return getMid(lastBuild.getLeft(), lastBuild.getRight());
    }

    private int getMid(int left, int right) {
        return left + (right - left) / 2;
    }
}
