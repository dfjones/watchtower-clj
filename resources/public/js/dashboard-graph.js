var graphOpts = {
    width: "100%",
    height: "400px"
};

var done = function (data) {
    console.log(data);
    var renderTimeP95 = [];
    var renderTimeAvg = [];
    var jsErrors = [];
    var serverErrors = [];
    data.stats.forEach(function (stat) {
        renderTimeP95.push(stat["renderTime"]["p95"]);
        renderTimeAvg.push(stat["renderTime"]["avg"]);
        jsErrors.push(stat["browserErrors"]["max"]);
        serverErrors.push(stat["serverErrors"]["max"]);
    });
    $("#p95RenderTime").sparkline(renderTimeP95, graphOpts);
    $("#avgRenderTime").sparkline(renderTimeAvg, graphOpts);
    $("#totalJSErrors").sparkline(jsErrors, graphOpts);
    $("#totalServerErrors").sparkline(serverErrors, graphOpts);
};

var renderGraph = function () {
    $.ajax({
        url: "/dashboard/stats"
    }).done(done);
};

renderGraph();
