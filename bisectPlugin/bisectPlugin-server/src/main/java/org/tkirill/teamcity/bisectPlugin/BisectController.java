package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.serverSide.CustomDataStorage;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            repository.create(buildId);
        }


//        storage.putValue(String.valueOf(buildId), "running");

        response.setStatus(200);
//        String buildId = request.getParameter("buildId");
//        if (buildId != null) {
//            if (storage.getValue("bisect-plugin-build-" + buildId) != null) {
//                response.setStatus(400);
//                return;
//            }
//            storage.putValue("bisect-plugin-build-" + buildId, "started");
//        }
    }
}
