(function () {
    BS.Bisect = function (buildId) {
        var self = this;

        console.log('Bisect creating: ', buildId, typeof buildId);

        self.run = function() {
            //noinspection JSUnusedGlobalSymbols
            BS.ajaxRequest('/bisect/run.html', {
                method: 'post',
                parameters: {buildId: buildId},
                onSuccess: function () {window.location.reload(true);},  // TODO replace with AJAX
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
    };
})();