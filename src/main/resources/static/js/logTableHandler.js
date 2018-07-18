function TdClicker() {
    $("td[title]").click(function (e) {
        e.preventDefault();
        var dialog = $("#dialog");
        dialog.text($(this).attr("title"));
        dialog.dialog("open");
    });
    $("button.tag").click(function (e) {
        e.preventDefault();
        console.log($(this).attr("id"));
        var index = $(this).attr("id");
        entries[index-entries[0].id].tag = "red";
    });
}

function createRow(element) {
    var newRow = "<tr>";
    // star clm
    newRow += "<td class='' ><button class='btn btn-primary tag' id="+element.id+">click</button></td>"
    // timestamp clm
    /*
     * Time needs to be parsed better, to give a better look 
     */
    newRow += "<td>" +element.timestamp+ "</td>"
    // newRow += "<td>" + ParseTime(element.timestamp, element.mSec) + "</td>"
    // level
    newRow += "<td>" + element.level + "</td>"
    // thread 
    newRow += "<td>" + element.process + "</td>"
    // class
    newRow += "<td>" + element.className + "</td>"
    // message 
    var str = element.message
    str = str.split('"').join('\'');
    if (element.message.length > 50) {
        newRow += "<td title=\"" + str + "\">" + element.message.substring(0, 50) + "..." + "</td>";
    } else {
        newRow += "<td title=\"" + str + "\">" + element.message + "</td>";
    }
    // newRow += "<td title='"+element.message+"'>" + element.message.substring(0,50) + "</td>"
    // exception
    if (element.exception != null) {
        str = element.exception;
        str = str.split('"').join('\'');
        if (element.exception.length > 50) {
            newRow += "<td title=\"" + str + "\">" + element.exception.substring(0, 50) + "..." + "</td>";
        } else {
            newRow += "<td title=\"" + str + "\">" + element.exception + "</td>";
        }
    }
    else
        newRow += "<td> -- </td>"
    return newRow + "</tr>";
}
function StructureLogs(response) {
    var newHTML = "";
    response.forEach(element => {
        if (FilterByWord(element))
            newHTML += createRow(element);
    });
    return newHTML;
}
function getPage(page) {
    var data;
    $.ajax({
        type: "get",
        url: "http://localhost:8080/log/page/" + page,
        data: {
            page: page,
            level: glevel,
            file: gfile,
            maxTime: gmaxTime,
            minTime: gminTime,
            thread: gThread,
            className: gClassName
        },
        dataType: "json",
        success: function (response) {
            entries = entries.concat(response);
            var newHTML = StructureLogs(response);
            // console.log("NewHTML : " + newHTML);
            $("#logTableBody").append(newHTML);

            // Reset the listeners
            TdClicker();
        }
    });
}
// 2018-07-09T07:08:37.000+0000
function ParseTime(TimeString, mSec) {
    // var str = "2018-07-09T07:08:37.000+0000";
    // mSec = 255;
    var newTime = "";
    var splited = TimeString.split("T");
    // console.log(splited);
    // console.log(splited[1].split("\."));
    newTime = splited[0] + " " + splited[1].split("\.")[0] + "," + mSec;
    // console.log(newTime);

    return newTime;
}

function FilterByWord(element) {
    //Search in message/exception/class/thread
    var hasWord = false;
    var hasTag = true;
    var message = element.message.toLowerCase();
    var exception = element.exception;
    if (gFilterWord != null && gFilterWord != "") {
        if (message.indexOf(gFilterWord) != -1) {     //match to search word
            hasWord = true;
        }
        if (exception != null) {
            if (exception.toLowerCase().indexOf(gFilterWord) != -1) {     //match to search word
                hasWord = true;
            }
        }

    } else {      //no Search word
        hasWord = true;
    }
    return hasWord;
}

/*
 * Variables
 */
var page = 1;
var glevel = '';
var gfile = -1;
var gminTime = "";
var gmaxTime = "";
var gClassName = "";
var gThread = "";
var gFilterWord = "";
var entries = [];
getPage(1);

$("#getPageBtn").click(function (e) {
    e.preventDefault();
    var page = $("#pageSelector").val();
    console.log("Page : " + page);
    // Check if there is a number
    if (page == 0) {
        alert("Need to enter a number!");
    } else {
        getPage(page);
    }
});
$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() == $(document).height()) {
        page++;
        getPage(page);
    }
});


/*
        This Section needs some major changing, the code has lots of similar code that 
        keeps repeating. 
*/
// Maybe i could do the filtering from the front end, with the entries variable.
$("#selectLevel").change(function (e) {
    e.preventDefault();
    console.log($(this).val());
    // Set the new level value
    glevel = $(this).val();
    //Reset the table 
    $("#logTableBody").empty();
    //load the first page again
    page = 1;
    entries = [];
    getPage(page);
});

$("#selectFile").change(function (e) {
    e.preventDefault();
    console.log($(this).val());
    // Set the new level value
    if ($(this).val() != "")
        gfile = $(this).val();
    else
        gfile = -1;
    //Reset the table 
    $("#logTableBody").empty();
    //load the first page again
    page = 1;
    entries = [];
    getPage(page);
});


$("#minTimestamp").change(function (e) {
    e.preventDefault();
    console.log($(this).val());
    // Set the new level value
    gminTime = $(this).val();
    gminTime = gminTime.replace("T", " ").replace(".", ",");
    //Reset the table 
    $("#logTableBody").empty();
    //load the first page again
    page = 1;
    entries = [];
    getPage(page);
});

$("#maxTimestamp").change(function (e) {
    e.preventDefault();
    console.log($(this).val());
    // Set the new level value
    gmaxTime = $(this).val();
    gminTime = $(this).val();
    gminTime = gminTime.replace("T", " ").replace(".", ",");
    //Reset the table 
    $("#logTableBody").empty();
    //load the first page again
    page = 1;
    entries = [];
    getPage(page);
});

$("#selectThread").change(function (e) {
    e.preventDefault();
    console.log($(this).val());
    // Set the new level value
    gThread = $(this).val();
    //Reset the table 
    $("#logTableBody").empty();
    //load the first page again
    page = 1;
    entries = [];
    getPage(page);
});

$("#selectClass").change(function (e) {
    e.preventDefault();
    console.log($(this).val());
    // Set the new level value
    gClassName = $(this).val();
    //Reset the table 
    $("#logTableBody").empty();
    //load the first page again
    page = 1;
    entries = [];
    getPage(page);
});
$("#wordSearch").change(function (e) {
    e.preventDefault();
    console.log($(this).val());
    gFilterWord = $(this).val().toLowerCase();
    $("#logTableBody").empty();
    var newHTML = StructureLogs(entries);
    $("#logTableBody").append(newHTML);
    TdClicker();
});

$("#dialog").dialog({ autoOpen: false, title: "Message" });
$("#containerForm").submit(function (e) {
    e.preventDefault();
});
$("#formToggler").click(function (e) { 
    e.preventDefault();
    $("#dropdownBtn").toggle();
    $("#dropdown").toggleClass("dropdown-menu");
});





/*
 *      This part should be done by the back end, when it renders the template
 *      
 */

$.ajax({
    type: "get",
    url: "http:/service",
    dataType: "json",
    success: function (response) {
        var select = $("#selectFile");
        response.forEach(element => {
            $(select).append("<option value='" + element.id + "'>" + element.name + "</option>");
        });
    }
});
$.ajax({
    type: "get",
    url: "http:/log/thread",
    dataType: "json",
    success: function (response) {
        var select = $("#selectThread");
        response.forEach(element => {
            $(select).append("<option value='" + element + "'>" + element + "</option>");
        });
    }
});
$.ajax({
    type: "get",
    url: "http:/log/class",
    dataType: "json",
    success: function (response) {
        var select = $("#selectClass");
        response.forEach(element => {
            $(select).append("<option value='" + element + "'>" + element + "</option>");
        });
    }
});