package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.buildTriggers.PolledTriggerContext;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.vcs.SVcsModification;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class BisectBuildTriggerHelper {
    private static final Logger logger = Logger.getLogger(Loggers.SERVER_CATEGORY + BisectBuildTriggerHelper.class);
    private SBuildServer server;
    private BuildCustomizerFactory customizerFactory;
    private BisectRepository repository;

    public BisectBuildTriggerHelper(SBuildServer server, BuildCustomizerFactory customizerFactory, BisectRepository repository) {
        this.server = server;
        this.customizerFactory = customizerFactory;
        this.repository = repository;
    }

    public void triggerBuild(@NotNull PolledTriggerContext context) {
        Bisect[] notFinished = repository.getAllNotFinished();
        logger.info("notFinished: " + notFinished.length);
        for (Bisect bisect : notFinished) processBisect(bisect);
    }

    private void processBisect(Bisect bisect) {
        logger.info("bisect start " + bisect.getBuildId());
        SBuild build= server.findBuildInstanceById(bisect.getBuildId());

        List<BisectFinishedBuild> finishedBuilds = getFinishedBuilds(bisect);
        BisectStep currentStep = getCurrentStep(build, finishedBuilds);
        BisectDecision step = BisectHelper.getNextStep(finishedBuilds, currentStep);

        if (step == null) {
            bisectFailed(bisect);
            return;
        }

        if (step.isSolved()) bisectSolved(bisect, step);
        else nextBuild(bisect, build, step.getNextStep());

        logger.info("bisect finish " + bisect.getBuildId());
    }

    private void bisectSolved(Bisect bisect, BisectDecision step) {
        bisect.setFinished(true);
        bisect.setSolved(true);
        bisect.setAnswer(step.getAnswer());
    }

    private void bisectFailed(Bisect bisect) {
        bisect.setFinished(true);
        bisect.setSolved(false);
        trySave(bisect);
    }

    @NotNull
    private BisectStep getCurrentStep(SBuild build, List<BisectFinishedBuild> finishedBuilds) {
        if (finishedBuilds.isEmpty()) {
            return new BisectStep(0, build.getContainingChanges().size());
        } else {
            BisectFinishedBuild lastBuild = finishedBuilds.get(finishedBuilds.size() - 1);
            return new BisectStep(lastBuild.getStep());
        }
    }

    @NotNull
    private List<BisectFinishedBuild> getFinishedBuilds(Bisect bisect) {
        List<Long> ids = bisect.getBuildIds();
        Map<Long, Boolean> history = getHistory(ids);

        List<BisectFinishedBuild> finishedBuilds = new ArrayList<BisectFinishedBuild>();
        for (BisectBuild bisectBuild : bisect.getBuilds()) {
            if (history.containsKey(bisectBuild.getBuildId())) {
                BisectFinishedBuild finishedBuild = new BisectFinishedBuild(bisectBuild.getStep(), history.get(bisectBuild.getBuildId()));
                finishedBuilds.add(finishedBuild);
            }
        }
        return finishedBuilds;
    }

    @NotNull
    private Map<Long, Boolean> getHistory(List<Long> ids) {
        Collection<SFinishedBuild> entries = server.getHistory().findEntries(ids);
        Map<Long, Boolean> history = new HashMap<Long, Boolean>();
        for (SFinishedBuild entry : entries) history.put(entry.getBuildId(), entry.getBuildStatus().isSuccessful());
        return history;
    }

    private void nextBuild(Bisect bisect, SBuild build, BisectStep nextStep) {
        logger.info("nextBuild " + nextStep.getLeft() + " - " + nextStep.getRight());

        SQueuedBuild sQueuedBuild = queueBuild(build, nextStep.getMid());
        BisectBuild nextBuild = new BisectBuild(Long.parseLong(sQueuedBuild.getItemId()), nextStep);
        logger.info("next build" + nextBuild.getBuildId() + ": " + nextBuild.getStep().getLeft() + " - " + nextBuild.getStep().getRight());
        bisect.addBuild(nextBuild);

        trySave(bisect);
    }

    private SQueuedBuild queueBuild(SBuild build, int nextMid) {
        SVcsModification vcsModification = build.getContainingChanges().get(nextMid);
        BuildCustomizer buildCustomizer = customizerFactory.createBuildCustomizer(build.getBuildType(), null);
        buildCustomizer.setParameters(build.getBuildType().getParameters());
        buildCustomizer.setChangesUpTo(vcsModification);
        return buildCustomizer.createPromotion().addToQueue("bisect");
    }

    private void trySave(Bisect bisect) {
        logger.info("Saving bisect");
        try {
            repository.save(bisect);
        } catch (IOException e) {
            logger.error("Bisect save error", e);
        }
    }
}
