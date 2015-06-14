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
import java.util.*;

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

            List<Long> ids = new ArrayList<Long>();
            for (BisectBuild bisectBuild : bisect.getBuilds()) {
                ids.add(bisectBuild.getBuildId());
            }

            Collection<SFinishedBuild> entries = server.getHistory().findEntries(ids);
            Map<Long, Boolean> history = new HashMap<Long, Boolean>();
            for (SFinishedBuild entry : entries) {
                history.put(entry.getBuildId(), entry.getBuildStatus().isSuccessful());
            }

            List<BisectFinishedBuild> finishedBuilds = new ArrayList<BisectFinishedBuild>();
            for (BisectBuild bisectBuild : bisect.getBuilds()) {
                if (history.containsKey(bisectBuild.getBuildId())) {
                    BisectFinishedBuild finishedBuild = new BisectFinishedBuild(bisectBuild.getStep(), history.get(bisectBuild.getBuildId()));
                    finishedBuilds.add(finishedBuild);
                }
            }

            SBuild build= server.findBuildInstanceById(bisect.getBuildId());
            BisectStep currentStep;
            if (finishedBuilds.isEmpty()) {
                currentStep = new BisectStep(0, build.getContainingChanges().size());
            } else {
                BisectFinishedBuild lastBuild = finishedBuilds.get(finishedBuilds.size() - 1);
                currentStep = new BisectStep(lastBuild.getStep());
            }
            BisectDecision step = BisectHelper.getNextStep(finishedBuilds, currentStep);
            if (step == null) {
                bisect.setFinished(true);
                bisect.setSolved(false);
                try {
                    repository.save(bisect);
                } catch (IOException e) {
                }
            } else {
                if (step.isSolved()) {
                    bisect.setFinished(true);
                    bisect.setSolved(true);
                    bisect.setAnswer(step.getAnswer());
                } else {
                    nextBuild(repository, bisect, build, step.getNextStep());
                }
            }
            logger.info("bisect finish " + bisect.getBuildId());
        }
        logger.info("triggerBuild finish");
    }

    private void nextBuild(BisectRepository repository, Bisect bisect, SBuild build, BisectStep nextStep) {
        logger.info("nextBuild " + nextStep.getLeft() + " - " + nextStep.getRight());

        SQueuedBuild sQueuedBuild = queueBuild(build, nextStep.getMid());
        BisectBuild nextBuild = new BisectBuild(Long.parseLong(sQueuedBuild.getItemId()), nextStep);
        logger.info("next build" + nextBuild.getBuildId() + ": " + nextBuild.getStep().getLeft() + " - " + nextBuild.getStep().getRight());
        bisect.addBuild(nextBuild);

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
}
