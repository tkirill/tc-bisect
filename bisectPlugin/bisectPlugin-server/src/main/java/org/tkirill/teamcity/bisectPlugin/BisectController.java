package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.serverSide.CustomDataStorage;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.vcs.SVcsModification;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BisectController extends BaseFormXmlController {
    public BisectController(SBuildServer server, WebControllerManager manager) {
        super(server);
        manager.registerController("/bisect/run.html", this);
    }

    @Override
    protected ModelAndView doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        return null;
    }

    @Override
    protected void doPost(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Element xmlResponse) {
        long buildId = Long.parseLong(request.getParameter("buildId"));
        SBuild build = myServer.findBuildInstanceById(buildId);
        if (build == null) {
            response.setStatus(404);
            return;
        }

        CustomDataStorage storage = build.getBuildType().getCustomDataStorage("bisectPlugin");
        BisectRepository repository = new BisectRepository(storage);
        if (!repository.exists(buildId)) {
            try {
                Bisect bisect = new Bisect(buildId);
                bisect.setChanges(getChangeIds(build));
                repository.save(bisect);
            } catch (IOException e) {
                response.setStatus(500);
                return;
            }
        }
        response.setStatus(200);
    }

    private List<Long> getChangeIds(SBuild build) {
        List<Long> result = new ArrayList<Long>();

        for (SVcsModification modification : build.getContainingChanges()) {
            result.add(modification.getId());
        }

        return result;
    }
}
