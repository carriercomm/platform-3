var addNewDoc = function () {
    var apiName = $("#item-info h2")[0].innerHTML.split("-v")[0];
    var version = $("#item-info h2")[0].innerHTML.split("-v")[1];
    var docName = $("#docName").val();
    var summary = $("#summary").val();
    var docType = getRadioValue($('input[name=optionsRadios]:radio:checked'));
    var sourceType = getRadioValue($('input[name=optionsRadios1]:radio:checked'));
    var docUrl = $("#docUrl").val();

    jagg.post("/site/blocks/documentation/ajax/docs.jag", { action:"addDocumentation",
        apiName:apiName, version:version,docName:docName,docType:docType,summary:summary,sourceType:sourceType,
        docUrl:docUrl},
              function (result) {
                  if (!result.error) {
                      clearDocs();
                      window.location.reload();
                  } else {
                      jagg.message(result.message);
                  }
              }, "json");


};


var removeDocumentation = function (apiName, version, docName, docType) {

    jagg.post("/site/blocks/documentation/ajax/docs.jag", { action:"removeDocumentation",
        apiName:apiName, version:version,docName:docName,docType:docType},
              function (result) {
                  if (!result.error) {
                      $('#' + apiName + '-' + docName).hide('slow');
                  } else {
                      jagg.message(result.message);
                  }
              }, "json");


};

var copyDocumentation = function (apiName, version, docName, docType, summary) {
    $('#newDoc .btn-primary').text('update');
    $('#newDoc').show('slow');
    $('#newDoc #docName').val(docName + '-copy');
    $('#newDoc #summary').val(summary);

    for (var i = 1; i <= 6; i++) {
        if ($('#optionsRadios' + i).val().toUpperCase().indexOf(docType.toUpperCase()) >= 0) {
            $('#optionsRadios' + i).attr('checked', true)
        }
    }
};

var updateDocumentation = function (apiName, version, docName, docType, summary, docUrl) {
    $('#newDoc .btn-primary').text('update');
    $('#newDoc').show('slow');
    $('#newDoc #docName').val(docName);
    $('#newDoc #summary').val(summary);
    if (docUrl != "undefined") {
        $('#newDoc #docUrl').val(docUrl);
        $('#optionsRadios8').attr('checked', true);
        $('#newDoc #docUrl').show();
    }

    for (var i = 1; i <= 6; i++) {
        if ($('#optionsRadios' + i).val().toUpperCase().indexOf(docType.toUpperCase()) >= 0) {
            $('#optionsRadios' + i).attr('checked', true);
        }
    }
};

var editInlineContent = function (apiName, version, docName) {
    var current = window.location.pathname;
    if (current.indexOf("item-info.jag") >= 0) {
        window.open("inline-editor.jag?docName=" + docName + "&apiName=" + apiName + "&version=" + version);
    } else {
        window.open("site/pages/inline-editor.jag?docName=" + docName + "&apiName=" + apiName + "&version=" + version);
    }

};

var clearDocs = function () {
    var doc = document;
    doc.getElementById('docName').value = '';
    doc.getElementById('summary').value = '';
    doc.getElementById('docUrl').value = '';
    $('#newDoc').hide('slow');
};

var getRadioValue = function (radioButton) {
    if (radioButton.length > 0) {
        return radioButton.val();
    }
    else {
        return 0;
    }
};





