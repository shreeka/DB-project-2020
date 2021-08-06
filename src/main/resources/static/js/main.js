
$("button[name='showOutput']").click(function (e) {
    e.preventDefault();
    var worksheetSel = document.getElementById("sheetSelect");
    var worksheet = worksheetSel.options[worksheetSel.selectedIndex].value;

    var taskSel = document.getElementById("taskSelect");
    var task = taskSel.options[taskSel.selectedIndex].value;


    console.log(worksheet);
    console.log(task);
    let url = '/'+worksheet+'/ex'+task
    $.ajax({
        url: url,
        type: "GET",
        contentType: "application/json",
        success: function (data) {
            document.write(data)
        }
    });
});

$("button[name='showAddress']").click(function (e) {
    e.preventDefault();
    var userid = $("#euserid").val();
    //Prevent default submission of form
    $('#addrTable').empty();

    axios.post('/worksheet1/showaddressbook', {
        euid: userid
    }).then(function (response) {
        $("body").html(response.data);
        })
        .catch(function (error) {
            console.log(error);
        });

});

$("button[name='getResult']").click(function (e) {
    e.preventDefault();

    var nickname = $("#nickname").val();
    var query = $("#query").val();
    var from_date = $("#from_date").val();
    var to_date = $("#to_date").val();

    axios.post('/worksheet1/getAttachments', {
        nickname,
        query,
        from_date,
        to_date
    }).then(function (response) {
        $("body").html(response.data);
    })
        .catch(function (error) {
            console.log(error);
        });

});


$("button[name='showViewsResult']").click(function (e) {
    e.preventDefault();
    var stock = $("#stockname").val();
console.log(stock);
    axios.post('/worksheet2/showViews', {
        stockname: stock
    }).then(function (response) {
        $("body").html(response.data);
    })
        .catch(function (error) {
            console.log(error);
        });

});

$("button[name='showMetaData']").click(function (e) {
    e.preventDefault();
    var table_name = $("#imdbtable").val();
    axios.post('/worksheet3/showImdbdata', {
        imdbtable: table_name
    }).then(function (response) {
        $("body").html(response.data);
    })
    .catch(function (error) {
        console.log(error);
    });

});

$( "#search-movie" ).submit(function( e ) {
    e.preventDefault();
    var input = $('input[name="inlineRadioOptions"]:checked').val();
    var query = $("#search").val();

    query_mapping= {"movie_title": "titleName", "credits": "name", "genre":"genre", "plotSummary":"plotSummary"}

    body ={}
    body[query_mapping[input]] = query

    axios.post('/worksheet3/'+input, body).then(function (response) {
        $("body").html(response.data);
    })
    .catch(function (error) {
        console.log(error);
    });

});

$("div.relatedSearch > a").click(function (e) {
    e.preventDefault();

    var searchType = $("input.searchType").val();
    var query = $(this).prev().val();

    query_mapping= {"movie_title": "titleName", "credits": "name", "genre":"genre", "plotSummary":"plotSummary"}

    body ={}
    body[query_mapping[searchType]] = query

    axios.post('/worksheet3/'+searchType, body).then(function (response) {
        $("body").html(response.data);
    })
        .catch(function (error) {
            console.log(error);
        });

});

$("button[name='showAnalysis']").click(function (e) {
    e.preventDefault();
    var select_query = $("select#query").val();
    axios.post('/worksheet4/'+select_query).then(function (response) {
        $("body").html(response.data);
    })
        .catch(function (error) {
            console.log(error);
        });

});