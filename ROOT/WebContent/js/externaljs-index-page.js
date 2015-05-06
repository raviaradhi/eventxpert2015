var options = {};
$(document).ready(function () {
   
    $("#dialoglogin").dialog({
        autoOpen: false,
        show: "drop",
        draggable: false,
        hide: { effect: 'drop', direction: "right" },
        modal: true,
        resizable: false,
        buttons: [
                            {
                                text: "Login",
                                click: function () {

                                    if ($.trim($('#txtUserName').val()) == '' || $.trim($('#txtPassword').val()) == '') {
                                        $('#divMessage').text('Please enter username and password.');
                                        $(this).effect("shake", options, 200, function () { $('#txtUserName').focus() });
                                    }
                                    else if ($.trim($('#txtUserName').val()) != '' && $.trim($('#txtPassword').val()) != '') {
                                        $('#divMessage').text('');
                                        $(this).effect("bounce", options, 250, function () {
                                            $(this).dialog("close");
                                            setTimeout(function () { moveToPage(); }, 600);
                                        });
                                    }



                                }
                            },
                            {
                                text: "Cancel",
                                click: function () { $(this).dialog("close"); }
                            }
                        ]
    });

    $("#anchorlogin").click(function () {
        $("#dialoglogin").dialog("open");
        return false;
    });

});

        function clearText() {
            $('#divMessage').text('');
        }

        function moveToPage() {
            window.location = "event-details.htm";
        }
	