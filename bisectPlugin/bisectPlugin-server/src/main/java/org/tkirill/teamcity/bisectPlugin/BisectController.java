package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BisectController extends BaseFormXmlController {
    public BisectController(SBuildServer server, WebControllerManager manager) {
        super(server);
        manager.registerController("/bisect/run.html", this);
    }

    @Override
    protected ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, Element xmlResponse) {
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
