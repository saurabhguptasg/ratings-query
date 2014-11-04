/**
 * Created by sgupta on 10/31/14.
 */
var waitingImagePath = 'images/waiting.gif';

var data;
var chart;
var chartRawData;
var chartData;
var dates;
var selectedDate;
var selectedMode;
var stateLookup;

google.load("visualization", "1", {packages:["corechart", "geochart"]});
google.setOnLoadCallback(drawRegionsMap);

function initApp() {
    chartRawData =         [
        ['State', 'Issue Amount', 'Number of Bonds']
    ];

    $("#dataModeRadio").buttonset();

    selectedMode = "new";
    getService("/query/dates", {}, function(data, textStatus){
        dates = data;
        console.log("dates are: " + JSON.stringify(dates));
        initSlider();
    });
}

function initSlider() {
    $( "#dataSelectionSlider" ).slider({
        value:dates.length - 1,
        min: 0,
        max: dates.length - 1,
        step: 1,
        slide: function( event, ui ) {
            selectedDate = dates[ui.value];
            requestDataUpdate();
        }
    });
    selectedDate = dates[dates.length - 1];
    requestDataUpdate();
}

function getService(urlName, args, callback) {
    showWaiting("waitingDiv", "");
    var headers = {};
    $.ajax({url: urlName,
        data: args,
        dataType: "json",
        success: function(data, textStatus, request) {
            $("#ipAddressDiv").text("last ip address: " + request.getResponseHeader("x-pivotal-ip-address"));
            clearDiv("waitingDiv");
            callback(data, textStatus);
        },
        headers: headers,
        type: "GET",
        async: true,
        statusCode:{
            401:function(data){renderLoginHeader(JSON.parse(data['responseText']));}
        }
    });
}

function showWaiting(divname, message) {
    $("#"+divname).text("").append("<img src='"+waitingImagePath+"'/>");
}

function clearDiv(divname) {
    $("#"+divname).text("");
}

function clearChart() {
    if(chart) {
        chart.clearChart();
    }
}

function drawRegionsMap() {

    chartData = google.visualization.arrayToDataTable(chartRawData);

    var options = {
        region: "US",
        displayMode: "regions",
        enableRegionInteractivity: true,
        width: 1024,
        keepAspectRatio: true,
        resolution: 'provinces'
    };

    chart = new google.visualization.GeoChart(document.getElementById('dataGraphics'));
    chart.draw(chartData, options);

    google.visualization.events.addListener(chart, 'regionClick', regionClickHandler);
    google.visualization.events.addListener(chart, 'select', selectHandler);
}

function regionClickHandler() {
    console.log("region click handler");
}

function selectHandler() {
    console.log("select handler");
    var selections = chart.getSelection();
    var row = selections[0]['row']+1;
    console.log("selected data row: " + JSON.stringify(chartRawData[row]));
    var summary = stateLookup[chartRawData[row][0]];

    var items = summary["items"];
    var dataHtml = "<table id='dataTable' width='100%'></table>";
    clearDiv("dataTableDiv");
    $("#dataTableDiv").append(dataHtml);
    $.each(items, function(index, item){
        var actionRows = "";
        $.each(item['actions'], function(actionIndex, action){
            var actionDateString = ""+action['actionYearMonth'];
            var actionDate = actionDateString.substring(0,4) + "/" + actionDateString.substring(4,6) + "/" + actionDateString.substring(6);
            actionRows +=
                "<tr>" +
                "<td>Action Id: "+action['actionId']+"</td>" +
                "<td>Action Type: "+action['actionType']+"</td>" +
                "<td>Rating Symbol: "+action['ratingSymbol']+"</td>" +
                "<td>Action Date: "+actionDate+"</td>" +
                "</tr>";
        });
        var itemRow =
            "<tr><td width='100%'><table width='100%' style='background-color: #d3d3d3; -moz-border-radius: 5px;'>" +
            "<tr><td>CUSIP: "+item['cusip']+"</td><td>Group: "+item['group']+"</td><td>Issuer: "+item['issuer']['name']+"</td><td>Dept.: "+item['issuer']['groupDepartment']+"</td></tr>" +
            "<tr><td>State: "+item['regionState']+"</td><td>Debt Type: "+item['debtType']+"</td><td>Issue Amount: $"+item['amount']+"million</td><td>Issuer Id: "+item['issuer']['issuerId']+"</td></tr>" +
            "<tr><td colspan='4'>" +
                "<div id='dataTableRow_"+index+"'>" + "<h3>Actions:</h3><div><table>" + actionRows + "</table></div>" + "</div>" +
                "</td></tr>" +
                "</table>" +
                "</td></tr><tr><td>&nbsp;</td></tr>";
        $("#dataTable").append(itemRow);
        $("#dataTableRow_"+index).accordion({
            collapsible: true
        });
    });
}

function requestDataUpdate() {
    console.log("selected date is: " + JSON.stringify(selectedDate));
    $("#dataSelectionSliderValue").text("Bonds for " + selectedDate['year'] + "/" + selectedDate['month']);
    selectedMode = $("input:radio[name=modeRadio]").filter(":checked").val();
    console.log("selected mode: " + selectedMode);

    getService("/query/bonds",
        {"month":selectedDate["formattedDate"],
         "action":selectedMode},
        function(data, textStatus){
            updateWithBondData(data);
        });
}

function updateWithBondData(data) {
    stateLookup = {};
    $.each(data, function(index, item){
        var state = "US-"+item["issuer"]["stateType"];
        if(!stateLookup[state]) {
            stateLookup[state] = {"amount": 0, "count": 0, "state":state, "items":[]};
        }
        var summary = stateLookup[state];
        summary["count"] += 1;
        summary["amount"] += item["amount"];
        summary["items"].push(item);
    });
    console.log("stateLookup: " + JSON.stringify(stateLookup));
    chartRawData =         [
        ['State', 'Issue Amount', 'Number of Bonds']
    ];

    if(data.length == 0) {
        clearChart();
    }
    else {
        $.each(stateLookup, function(index, item){
            chartRawData.push([item["state"], item["amount"], item["count"]]);
        });
        drawRegionsMap();
    }
}

