function sleep(miliseconds) {
    var currentTime = new Date().getTime();
    while (currentTime + miliseconds >= new Date().getTime()) {
    }
}
function sleep500() {
    var value = 0;
    value = $('#bar_me').progressbar('option', 'value');
    if (value < 100) {
        value = value + 10;
        $('#bar_me').progressbar('option', 'value', value);
        setTimeout(() => {
            sleep500();
        }, 1000);
    }else{
        $('#bar_me').progressbar('destroy');
    }
}
var inprogress = false;
$('#bar_me').progressbar();

$('#barBtn').click(function (e) {
    if(!inprogress){
        sleep500();
        inprogress = true;
    }
  
});

$('#autocomp').autocomplete({
    minLength: 0,
    source: function (request , response) {
        $.ajax({
            type: "get",
            url: "http://localhost:8080/service",
            data: {
                q: request.term
            },
            dataType: "json",
            success: function (response2) {
                var data = [];
                $.each(response2, function (indexInArray, valueOfElement) { 

                    data.push(valueOfElement.name);
                     console.log(data);
                });
                response(data);
            }
        });
      }
});

$('#myAcord').accordion({
    active:1,
    animate:200,
    disabled: false,
    collapsible: false,
    header:'h3',
    activate: function (event, ui) {
        console.log(event );
        console.log(ui);
        console.log(ui.newHeader);
      }
});

$('#btn1').button({
    icon: 'ui-icon-cancel',
    iconPosition:'bottom'
});
$('#btn2').button({
    disabled:true
});
$('#btn3').button({
    icon: "ui-icon-gear",
    iconPosition:'end'
});

$('#menu').menu();
