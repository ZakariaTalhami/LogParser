$('p').hover(function (e) {
    $(e.target).toggleClass("hovered");
}
);

$('input').focus(function (e) {
    e.preventDefault();
    $(this).toggleClass("focused");
});

$('input').blur(function (e) {
    e.preventDefault();
    $(this).toggleClass("focused");
});


$('#mybtn').click(function (e) { 
    e.preventDefault();
    alert("something");
    $('#myDiv').text("textString");
    $('#myDiv').attr('class', 'form_sec');
    var div = $('#myDiv');
    console.log(div);
    
    alert($.contains(document , div));
});

var arr = ['Pickle','dry skin','Firemint', 'Goldfish scale', 'Frog bone'];

$.each(arr, function (indexInArray, valueOfElement) { 
     $("#potion_ing").append("<li>"+valueOfElement+"</li>");
});

$('#ajaxBtn').click(function (e) { 
    e.preventDefault();
    $('#ajaxContent').load("load.html");
    // $.getJSON("http://localhost:8080/log/122601",
    //     function (data) {
    //         console.log(data);
    //     }
    // );
});