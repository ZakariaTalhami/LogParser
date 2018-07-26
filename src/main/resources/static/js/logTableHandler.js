var isinit = false;
/* 
    Reset click event listeners for the newly generated
    table rows.
*/
function TdClicker() {
    $("td[title]").click(function (e) {
        e.preventDefault();
        var dialog = $("#dialog");
        dialog.text($(this).attr("title"));         //Set the text of the dialog to either message or exception
        dialog.dialog("open");
    });
    $("button.tag").click(function (e) {
        e.preventDefault();
        selectedLog = $(this).attr("id");           //Saves the Id of the sele

    });
    if (!isinit) {
        $(document).ready(function () {
            $('#myTable').DataTable({
                paging:false
            });
        });
        isinit = true;
    }

}

/* 
    Convert an LogEntry object into an html table row
*/
function createRow(element) {
    var tag = "None"
    if (element.tag != null)
        tag = element.tag.tag;
    var newRow = "<tr>";
    // Tag clm
    // Clicking The button opens MODAL of id = taggingModal
    newRow += "<td class='' ><button class='btn btn-primary tag btn-block' id='" + element.id + "' data-toggle='modal' data-target='#taggingModal'>" + tag + "</button></td>"
    // timestamp clm
    newRow += "<td>" + ParseTime(element.timestamp, element.mSec) + "</td>"
    // level
    newRow += "<td>" + element.level + "</td>"
    // thread 
    newRow += "<td>" + element.process + "</td>"
    // class
    newRow += "<td>" + element.className + "</td>"
    // message 
    var str = element.message
    str = str.split('"').join('\'');                    //Remove the ' " ' from messages
    if (element.message.length > 50) {                  //cut down the message to better size
        newRow += "<td title=\"" + str + "\">" + element.message.substring(0, 50) + "..." + "</td>";
    } else {
        newRow += "<td title=\"" + str + "\">" + element.message + "</td>";
    }
    // newRow += "<td title='"+element.message+"'>" + element.message.substring(0,50) + "</td>"
    // exception
    if (element.exception != null) {                    // filter non-error logs
        str = element.exception;
        str = str.split('"').join('\'');                //Remove the ' " ' from messages
        if (element.exception.length > 50) {            //Cut down the message to better size
            newRow += "<td title=\"" + str + "\">" + element.exception.substring(0, 50) + "..." + "</td>";
        } else {
            newRow += "<td title=\"" + str + "\">" + element.exception + "</td>";
        }
    }
    else
        newRow += "<td> -- </td>"
    return newRow + "</tr>";
}
/* 
    Structures a list of log entries into HTML and returns it
*/
function StructureLogs(response) {
    var newHTML = "";
    response.forEach(element => {
        if (FilterByWord(element))
            newHTML += createRow(element);
    });
    return newHTML;
}

/* 
    Loads the next Page of logs, by requesting the server
*/
function getPage(page) {
    var data;
    $.ajax({
        type: "get",
        url: "http::/log/page/" + page,
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
            entries = entries.concat(response);         //  Add to the current log list
            var newHTML = StructureLogs(response);      //  Convert list of objects to html rows
            // console.log("NewHTML : " + newHTML);
            $("#logTableBody").append(newHTML);         //  Add New page to the Table

            // Reset the listeners
            TdClicker();
        }
    });
}

/* 
    Converts the time format received from the server
*/
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

/* 
    Applies a filter to the Log Entries that are displayed in the table
        Filters by : 1) search word in the message or exception
                     2) Tag
*/
function FilterByWord(element) {
    //Search in message/exception/class/thread
    var hasWord = false;
    var hasTag = false;
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

    if (gTag > 0) {                     //No tag filter set
        if (element.tag != null) {          //filter unTagged logs
            if (element.tag.id == gTag) {    //Filter disimilar Tagged logs
                hasTag = true;
            }
        }
    } else {
        hasTag = true;
    }

    return (hasWord && hasTag);
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
var gTag = -1;
var entries = [];
var selectedLog = -1;

/* 
    Get the First page
*/
getPage(1);


/* 
    Listens to the event of reach the bottom of the page, used request the next page
    and providing a continuous scrolling table.
*/
$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() > $(document).height() - 1) {
        page++;
        getPage(page);
    }
});


/*
        This Section needs some major changing, the code has lots of similar code that 
        keeps repeating. 
*/
/* 
        Adding listenters to the Various filters
*/
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
    gmaxTime = gmaxTime.replace("T", " ").replace(".", ",");
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
$("#selectTag").change(function (e) {
    e.preventDefault();
    console.log($(this).val());
    // Set the new level value
    gTag = $(this).val();
    //Reset the table 
    $("#logTableBody").empty();
    var newHTML = StructureLogs(entries);
    $("#logTableBody").append(newHTML);
    TdClicker();
});

/* 
    Set up the jQuery dialog
*/
$("#dialog").dialog({ autoOpen: false, title: "Message" });

/* 
    Stop submissions from the form containing all the filter selectors
*/
$("#containerForm").submit(function (e) {
    e.preventDefault();
});

/* 
    Setting listener for toggling the Search bar
*/
$("#formToggler").click(function (e) {
    e.preventDefault();
    $("#dropdownBtn").toggle();                     // show/hide the dropdown button
    $("#dropdown").toggleClass("dropdown-menu");    // show/hide the Search bar
});


/* 
    Handles the creation of new tags, deals with the submission of the
    form in the Modal for creating tags
*/
$("#createTagForm").submit(function (e) {
    e.preventDefault();
    var tagName = $("#createTagInput").val();       //Extract tag name from input
    $.ajax({                                        //Send POST request server
        type: "POST",
        url: "http:tag",
        contentType: "application/json",
        data: JSON.stringify({ tag: tagName }),
        dataType: "json",
        complete: function (response) {
            $("#ModalClose").trigger("click");      //Close the Window
            $("#selectTag").empty();                //Refresh the selects for Tags
            $("#selectTag").append("<option disabled selected value> -- select an option -- </option><option value=\"-1\">None</option>");
            FillTagsSelect();
        }
    });
});


/* 
    Handles Tagging of log entries
*/
$(".selectLogTag").change(function (e) {
    e.preventDefault();
    var newtag = $(this).val();                     //get tag id
    var text = $(this).find(":selected").text();    //get tag text
    $.ajax({
        type: "get",                                //send update request to the server
        url: "http:log/" + selectedLog + "/tag/" + newtag,
        data: "data",
        dataType: "JSON",
        complete: function (response) {
            // console.log(text);
            // console.log($("#" + selectedLog));
            $("#" + selectedLog).html(text);        //Set the tag button to the text of the tag
            var obj = entries.find(o => o.id == selectedLog); //add the tag to the cached entries 
            obj.tag = {                             //Create the tag object
                id: newtag,
                tag: text
            };
        }
    });
});




/*
 *      This part should be done by the back end, when it renders the template
 *      
 */

$.ajax({
    type: "get",
    url: "http:service",
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
    url: "http:log/thread",
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
    url: "http:log/class",
    dataType: "json",
    success: function (response) {
        var select = $("#selectClass");
        response.forEach(element => {
            $(select).append("<option value='" + element + "'>" + element + "</option>");
        });
    }
});
FillTagsSelect();
function FillTagsSelect() {
    $.ajax({
        type: "get",
        url: "http:tag",
        dataType: "json",
        success: function (response) {
            var select = $(".selectTag");
            response.forEach(element => {
                $.each(select, function (indexInArray, valueOfElement) {
                    // console.log(valueOfElement); 
                    $(valueOfElement).append("<option value='" + element.id + "'>" + element.tag + "</option>");
                });
            });
        }
    });
}




/*

    Added
*/


