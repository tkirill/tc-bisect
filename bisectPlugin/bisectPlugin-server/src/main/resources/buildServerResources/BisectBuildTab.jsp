<%@include file="/include.jsp" %>

<c:choose>
    <c:when test="${bisect != null}">
        <c:choose>
            <c:when test="${bisect.isFinished()}">
                <p>Done</p>

                <p>
                    Builds:
                    <ul>
                    <c:forEach var="build" items="${bisect.builds}">
                        <li>${build.buildId}: ${build.left} - ${build.right}</li>
                    </c:forEach>
                    </ul>
                </p>
            </c:when>

            <c:otherwise>
                <p>Running</p>
                <p>
                    Builds:
                    <ul>
                    <c:forEach var="build" items="${bisect.builds}">
                        <li>${build.buildId}: ${build.left} - ${build.right}</li>
                    </c:forEach>
                    </ul>
                </p>
            </c:otherwise>
        </c:choose>
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