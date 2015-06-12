(function (global) {
    var Bisect = function (buildId) {
        var self = this;

        self.run = function() {
            BS.ajaxRequest('/bisect/run.html', {
                method: 'post',
                parameters: {buildId: buildId},
                onSuccess: function () {alert("Success!");},
                onFailure: function () {alert("Error!");}
            });
        };

        self.bind = function () {
            $j('#bisect-run').click(function () {
                self.run();
            })
        };

        document.observe("dom:loaded", function() {
            self.bind();
        });
    }

    BS.Bisect = Bisect;
})(this);