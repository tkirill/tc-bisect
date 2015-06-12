<%@include file="/include.jsp" %>

<c:choose>
    <c:when test="${isRunning}">
        <p>Already running</p>
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