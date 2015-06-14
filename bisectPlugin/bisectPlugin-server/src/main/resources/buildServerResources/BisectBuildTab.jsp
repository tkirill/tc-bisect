<%@include file="/include.jsp" %>

<c:choose>
    <c:when test="${bisect != null}">
        <c:choose>
            <c:when test="${answer != null}">
                <p>
                    Answer: ${answer.version}
                </p>
            </c:when>
        </c:choose>
        <c:if test="${empty historyRecords}">
            <p>No builds available</p>
        </c:if>
        <c:if test="${not empty historyRecords}">
            <div class="clr"></div>

            <div class="successMessage" id="successMessage" style="display: none; background-color: #fff;">&nbsp;</div>
            <table cellspacing="0" class="testList historyList dark borderBottom">
            <thead>
            <tr>
            <c:choose>
                <c:when test="${showTrivialColumnNames and hasBranches}">
                    <th class="branch hasBranch">Branch</th>
                </c:when>
                <c:otherwise>
                    <th class="branch"></th>
                </c:otherwise>
            </c:choose>
            <th class="buildNumber">${showTrivialColumnNames ? '#' : ''}</th>
            <th>Results</th>
            <th>Changes</th>
            <th class="sorted">Started</th>
            <th>Duration</th>
            <th>Progress</th>
            <th>Agent</th>
            <th title="Pinned builds won't be removed from the history upon automatic history cleanup">${showTrivialColumnNames ? 'Pin' : ''}</th>
            </tr>
            </thead>
            <c:forEach var="entry" items="${historyRecords}" varStatus="recordStatus">
                <jsp:useBean id="entry" type="jetbrains.buildServer.serverSide.SBuild"/>

                <tr>
                    <bs:buildRow build="${entry}" rowClass="${rowClass}"
                                 showBranchName="true"
                                 maxBranchNameLength="15"
                                 showBuildNumber="true"
                                 showStatus="true"
                                 showArtifacts="flase"
                                 showChanges="true"
                                 showStartDate="true"
                                 showDuration="true"
                                 showProgress="true"
                                 showStop="false"
                                 showAgent="true"
                                 showPin="false"
                                 showTags="false"
                                 showUsedByOtherBuildsIcon="true"/>
                </tr>
            </c:forEach>
            </table>
            <bs:pinBuildDialog onBuildPage="${false}" buildType="${buildType}"/>
        </c:if>
    </c:when>

    <c:otherwise>
        <script type="text/javascript">
            new BS.Bisect(${buildId});
        </script>

        <p>
            <button id="bisect-run">Run</button>
        </p>
    </c:otherwise>
</c:choose>