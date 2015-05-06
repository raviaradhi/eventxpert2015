var firstli = undefined;
/*Temporary statements for testing*/
function temp()
{
    //$(document).find('input[type="checkbox"]').css('display','block');
}

      function removeServiceItem(link,chkbox)
      {
          //alert();
          var divHolder = $(link).parent().parent().parent();
          var divTitle =  $(divHolder).attr('class');
          $('#'+divTitle).hide(500, function () { });
          divHolder.html('');
          divHolder.hide();
          $('#'+chkbox).attr('checked',false);
            updateAmount();
      }

      function updateAmount()
      {
            var amount =0;
           $('#divAllHotels').find('input[type="checkbox"]').each(function () {
                if(this.checked ==  true)
                {
                 amount += parseFloat($('#' + this.name).find('span[class="spnAmount"]').html());
                }
            });

        $('#spnUsed').html(amount.toFixed(2));
        var remAmount = parseFloat($('#txtBudget1').val()) - parseFloat(amount);
        $('#spnRemaining').html(remAmount.toFixed(2));
      }
        function removediv(ctrl) {
            $('#div' + ctrl.id).remove();// css("display", "none"); 
        }



        function heighlight(divId, divNo) {

            var divs = ['divHome', 'divFunction', 'divHotel', 'divResort'];

            $(divs).each(function () {
                $('#' + this).css('background-color', '');
            });

            $('#divWhere').css('background-image', 'url(images/where-bg' + divNo + '.jpg)');

            $('#' + divId).css('background-color', '#e6eeee');

            $('#divServices').attr("disabled", '');
            $('#divServices img').css("opacity", "1.0");
            $('#divServices img').css("filter", "alpha(opacity = 100)");

        }

        function getParameterByName(name) {
            name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
            var regexS = "[\\?&]" + name + "=([^&#]*)";
            var regex = new RegExp(regexS);
            var results = regex.exec(window.location.search);
            if (results == null)
                return "";
            else
                return decodeURIComponent(results[1].replace(/\+/g, " "));
        }


        function disableVenueServices() {


            $('#divVenue input').attr("checked", '');
            //$('#divVenue input[type="checkbox"]').parent().parent().css('background-color', '');


            $('#divVenue').attr("enabled", 'f');
            //$('#divVenue img').css("opacity", "0.3");
            //$('#divVenue img').css("filter", "alpha(opacity = 30)");


            $('#divServices input').attr("checked", '');
            $('#divServices').attr("disabled", 'disabled');
            $('#divServices img').css("opacity", "0.3");
            $('#divServices img').css("filter", "alpha(opacity = 30)");
        }

        function filterAreas()
        {
       
            var selval=$('#ddlArea1').val();
          
         //Filtering
                $('#tableHotel').find('td[title="tdArea"]').each(function(){
                
                if($.trim($(this).text()) == selval || selval =="All")
                    {
                        $(this).parent().css("display","");
                    } 
                    else{
                        $(this).parent().css("display","none");
                        
                    }
                });
       }


        $( document ).ready(function () {
//        jQuery(document).ready(function($){
        temp();
            $('#btnModify').click(function(){
                var amount = 0;
                $('#txtBudget1').val($('#txtBudget2').val())
                var remAmount = parseFloat($('#txtBudget2').val()) - parseFloat($('#spnUsed').text());
                $('#spnRemaining').html(remAmount.toFixed(2));
                $('#divAmtModified').show(500);
                $('#divAmtModified').hide(500);
            });
            $('span[name="spnTitle"]').html(getParameterByName('type'));

            //alert();


            disableVenueServices();

            $('#ddlArea1,#ddlArea2').change(function () {
            var selval = this.value ;
                if (this.value == 'msDropDown') {
                    disableVenueServices();
                    return;
                }
                $('#divVenue').attr("disabled", '');
                $('#divVenue img').css("opacity", "1.0");
                $('#divVenue img').css("filter", "alpha(opacity = 100)");


                filterAreas();
            });

            //: ; /* For IE8 and earlier */

            $('#txtdatepick1,#txtdatepick2').datepicker();
            try {
                $("#ddlTime1").msDropDown({ mainCSS: 'dd2' });
                $("#ddlTime2").msDropDown({ mainCSS: 'dd2' });
                
                //alert($.msDropDown.version);
                //$.msDropDown.create("body select");
                $("#ver").html($.msDropDown.version);
            } catch (e) {
                alert("Error: " + e.message);
            }
            
            try {
                $("#ddlCity1").msDropDown({ mainCSS: 'dd2' });
                $("#ddlCity2").msDropDown({ mainCSS: 'dd2' });
                
                //alert($.msDropDown.version);
                //$.msDropDown.create("body select");
                $("#ver").html($.msDropDown.version);
            } catch (e) {
                alert("Error: " + e.message);
            }


            try {
                $("#ddlArea1").msDropDown({ mainCSS: 'dd2' });
                $("#ddlArea2").msDropDown({ mainCSS: 'dd2' });
                
                //alert($.msDropDown.version);
                //$.msDropDown.create("body select");
                $("#ver").html($.msDropDown.version);
            } catch (e) {
                alert("Error: " + e.message);
            }

             try {
                $("#ddlSort").msDropDown({ mainCSS: 'dd2' });
                //alert($.msDropDown.version);
                //$.msDropDown.create("body select");
                $("#ver").html($.msDropDown.version);
            } catch (e) {
                alert("Error: " + e.message);
            }

            

            $('#maindiv2').hide();
            $('#maindiv3').hide();
             $('#summary_container').hide();
            $('input[name=Input3]').click(function () {

                var items = $('input[type=checkbox]');
                items.each(function (index, item) {

                    //debugger;
                    item.checked = false;
                    $(item).parent().parent().css('background-color', '');

                });

            });

            $('#imgbtnnext1').click(function () {

//            alert($('#ddlArea2').val());
//            $('#ddlArea2').val($('#ddlArea1').val());
//            alert($('#ddlArea2').val());
//            return;

            hideItems();
             
                var ctrlValidation = [


                { ctrlid: 'txtdatepick1', message: 'Please Select Date.' },
                { ctrlid: 'ddlCity1', message: 'Please Select City.' },
                { ctrlid: 'ddlArea1', message: 'Please Select Area.' },
                { ctrlid: 'txtBudget1', message: 'Please Enter Amount.' },
                { ctrlid: 'txtChild1', message: 'Please Enter Number of Children.' },
                { ctrlid: 'txtAdult1', message: 'Please Enter Number of Adult(s).' },
                { ctrlid: 'txtGuest1', message: 'Please Enter Number of Guest(s).' }

        ];

                $('#txtdatepick2').val($('#txtdatepick1').val());
                $('#ddlCity2').val($('#ddlCity1').val());
                $('#ddlArea2').val($('#ddlArea1').val());
                $('#txtBudget2').val($('#txtBudget1').val());
                $('#txtChild2').val($('#txtChild1').val());
                $('#txtAdult2').val($('#txtAdult1').val());
                $('#txtGuest2').val($('#txtGuest1').val());


                $('#spnUsed').html("0");
                $('#spnRemaining').text($('#txtBudget1').val());



                //debugger;
                // alert(ctrlValidation.length );
                
                var failedRules = 0;
                var focusTobeSetId;
                $(ctrlValidation).each(function () {
                return true;
                    if ($.trim($('#' + this.ctrlid).val()) == '') {
                        //message += this.message + "<br/>";

                        if(failedRules == 0){
                            focusTobeSetId = this.ctrlid;
                        }

                        var divleft = $('#' + this.ctrlid).offset().left;
                        var divtop = $('#' + this.ctrlid).offset().top;
                        jQuery('<div/>', {
                            id: 'div' + this.ctrlid +","
                        }).appendTo($('#divMessageContainer'));

                        $('#div' + this.ctrlid).text(this.message);
                        $('#div' + this.ctrlid).css("background-color", "#FBF9EA");
                        $('#div' + this.ctrlid).css("position", "absolute");
                        $('#div' + this.ctrlid).css("width", "auto");
                        $('#div' + this.ctrlid).css("height", "22px");
                        $('#div' + this.ctrlid).css("left", divleft + 100);
                        $('#div' + this.ctrlid).css("vertical-align", "middle");
                        $('#div' + this.ctrlid).css("padding-top", "5px");
                        $('#div' + this.ctrlid).css("padding-right", "15px");
                        $('#div' + this.ctrlid).css("padding-left", "10px");
                        $('#div' + this.ctrlid).css("top", divtop );
                        $('#div' + this.ctrlid).css("font-family", "Arial");
                        $('#div' + this.ctrlid).css("font-size", "12px");
                        $('#div' + this.ctrlid).css("font-weight", "bold");
                        $('#div' + this.ctrlid).css("border", "1px solid black");
                        failedRules++;
                    }
                });


               if(failedRules>0){
                    setTimeout(function () { $('#'+focusTobeSetId).focus() }, 2000);
                    return;
               }

               var selectedSvcs = 0;
               /*Checking whether the service checkbox checked or not (at least one)*/
                $('#divServices' ).find('input[type="checkbox"]').each(function () {

                    if (this.checked) {
                        selectedSvcs+=1;
                    }
                });

                if(selectedSvcs == 0)
                {
                    $('#divAlertPopup').html("Please select at lease one service.");
                    $('#divAlertPopup').dialog({
                        autoOpen: true,
                        show: "drop",
                        draggable: false,
                        hide: { effect: 'drop', direction: "right" },
                        modal: true,
                        resizable: false,
                        buttons: [
                            {
                                text: "OK",
                                click: function () { $(this).dialog("close"); }
                            }
                        ]
                    });
                    return;
                }

               
               /*Added on 11202012*/
               filterAreas();
               heighlightserviceli(firstli);

                $('#maindiv2').show('slow');
                $('#maindiv1').hide(1000, function () { });
                


                
            });
            $('#imgBtnNext2').click(function () {


            
                $('#summary_container').show('slow');
                //$('#maindiv3').show('slow');
                $('#divsummary')[0].className = "box-nav2 active";
                //$('#divpayment')[0].className = "box-nav3 active";

                $('#maindiv2').hide(1000, function () { });
                $('#h1title').text('2. Summary.');

            });
             $('#btnContinueBooking').click(function () {

               
                $('#maindiv3').show('slow');
                
                $('#divpayment')[0].className = "box-nav3 active";

                $('#summary_container').hide(1000, function () { });
                $('#h1title').text('3. Money Payment.');

            });

           // btnContinueBooking

            $('#divdetails').click(function () {
            return;
                $('#maindiv1').show('slow');
                $('#divsummary')[0].className = "box-nav2";
                $('#divdetails')[0].className = "box-nav1 active";
                $('#divpayment')[0].className = "box-nav3";

                $('#maindiv4').hide(1000, function () { });
                $('#maindiv3').hide(1000, function () { });
                $('#maindiv2').hide(1000, function () { });

                $('#h1title').text('1. ' + getParameterByName('type') + ' Details.');

            });

            $('#divsummary').click(function () {
                return;
                $('#maindiv4').show('slow');
                $('#divsummary')[0].className = "box-nav2 active";
                $('#divdetails')[0].className = "box-nav1";
                $('#divpayment')[0].className = "box-nav3";

                $('#maindiv1').hide(1000, function () { });
                $('#maindiv2').hide(1000, function () { });
                $('#maindiv3').hide(1000, function () { });

                $('#h1title').text('2. ' + getParameterByName('type') + ' Summary.');
                hideItems();

            });

            $('#divpayment').click(function () {
                return;
                $('#maindiv3').show('slow');
                $('#divsummary')[0].className = "box-nav2";
                $('#divdetails')[0].className = "box-nav1";
                $('#divpayment')[0].className = "box-nav3 active";

                $('#maindiv1').hide(1000, function () { });
                $('#maindiv2').hide(1000, function () { });
                $('#maindiv4').hide(1000, function () { });

                $('#h1title').text('3. Money Payment.');

            });

                        
        });

        /*Displays top level services based on selected services in first page.*/
        function showServices(divId) {
            //alert(divId);
            var secondDivID = '';
            /*When this function called from first div then the first div id is "divServices" and second div id is "divServices2" 
            when the call is from 2nd div those ids are swapped. */
            if(divId == 'divServices')
            {
                divId = 'divServices';
                secondDivID= 'divServices2';
            }
            else 
            {
                divId = 'divServices2';
                secondDivID= 'divServices';
            }
            var count = 0;
            firstli =undefined;
            $('#'+divId ).find('input[type="checkbox"]').each(function () {
                
                 //$('#'+secondDivID).find('input[type="checkbox"]').parent().parent().css('background-color', '');
                 $('#'+secondDivID).find('input[type="checkbox"]').is(':checked',false);
                    

                $(this).parent().parent().css('background-color', '');
                if (this.checked) {
                    if(firstli ==undefined)
                        firstli = $('#'+this.name)[0];
                    var servicecheckbox = this;
                    $(this).parent().parent().css('background-color', '#e6eeee');
                    $('#' + this.name).css("display", "");

                    /*Updating second services div as same as previous page services div*/
                    $('#'+secondDivID).find('input[type="checkbox"]').each(function () {
                      
                        if(this.name == servicecheckbox.name)
                        {
                            this.checked = true;
                            /*Highlighting selected services.*/
                            $(this).parent().parent().css('background-color', '#e6eeee');
                        }
                    });

                    count+=1;
                }
                else {
                    $('#' + this.name).css("display", "none");
                }
            });

            heighlightserviceli(firstli);
            if(count >=4){
            $('#imgNext').css("display","");
            }
            else{
            $('#imgNext').css("display","none");
            }

        
        }
    
        function isNumberKey(evt) {
            var charCode = (evt.which) ? evt.which : event.keyCode
            if (charCode > 31 && (charCode < 48 || charCode > 57))
                return false;

            return true;
        }
        
        function isAlphaNumericKey(event) {
              var regex = new RegExp("^[a-zA-Z0-9.,:;]+$");
            var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
            if (!regex.test(key)) {
            event.preventDefault();
            return false;
            }
            return true;
        }
    
     function isNumberKeyWithoutZero(evt,comp) {
            var charCode = (evt.which) ? evt.which : event.keyCode
            if (charCode > 31 && (charCode < 48 || charCode > 57)){
                return false;
            }
            if (comp.value.length == 0 && evt.which == 48 ){
                return false;
            }
            return true;
        }

    
    $(document).ready(function () {
        if ( jQuery('#tableHotel').tablesorter() )
        $("#tableHotel").tablesorter();

    }

    );

    function sortTable(ddlSortEle) {
        var val = $(ddlSortEle).val();
        //return;

        if (val == "Amount") {
            $("#thAmount").click();
        }
        else if (val == "Venue") {
            $("#thHotel").click();
        }
        else if (val == "Area") {
            $("#thArea").click();
        }
    }

    var selectedService = 'liVenue';
    function bookNow(chkBox) {


        $('#tableHotel').find('input[type="checkbox"]').each(function () {
            this.checked = false;
        });
        $('#' + chkBox).attr('checked', true);
        fillVenue(chkBox);
    }
    function fillVenue(chkbox,changeVal) {

        var html = "";
        var amount = 0;
        $('#divAllHotels').find('input[type="checkbox"]').each(function () {
        //debugger;
            if (this.id == chkbox) {
                html += $('#' + this.name).html() + ' <div class="hr-dot-line" style="margin-bottom: 15px;"></div>';
               //tempcheckbox = this;   
            }
            if(this.checked ==  true)
            {
             amount += parseFloat($('#' + this.name).find('span[class="spnAmount"]').html());
            }
        });



        //debugger;
        if (selectedService == 'liVenue') {

            $('div[name="divVenueTitle"]').css("display", "");
           
            if(changeVal == undefined)
            {
               checkServiceSelection('div[name="divSelectedVenue"]',chkbox)
            }
            else if(changeVal == true)
            {
                $('div[name="divSelectedVenue"]').css("display", "");
                $('div[name="divSelectedVenue"]').html(html);
            }
        }
        else if (selectedService == 'liCatering') {
            $('div[name="divCateringTitle"]').css("display", "");
            
            if(changeVal == undefined)
            {
               checkServiceSelection('div[name="divSelectedCatering"]',chkbox)
            }
            else if(changeVal == true)
            {
                $('div[name="divSelectedCatering"]').css("display", "");
                $('div[name="divSelectedCatering"]').html(html);
                
            }

        }
        else if (selectedService == 'liPhotographer') {
            $('div[name="divPhotographerTitle"]').css("display", "");
            
            if(changeVal == undefined)
            {
               checkServiceSelection('div[name="divSelectedPhotographer"]',chkbox)
            }
            else if(changeVal == true)
            {
                $('div[name="divSelectedPhotographer"]').css("display", "");
                $('div[name="divSelectedPhotographer"]').html(html);
                
            }
            
        }
        else if (selectedService == 'liInvitations') {
            $('div[name="divInvitationTitle"]').css("display", "");
            

            if(changeVal == undefined)
            {
               checkServiceSelection('div[name="divSelectedInvitation"]',chkbox)
            }
            else if(changeVal == true)
            {
                $('div[name="divSelectedInvitation"]').css("display", "");
                $('div[name="divSelectedInvitation"]').html(html);
                //tempcheckbox.checked = false;
            }
            
        }
         else if (selectedService == 'liEntertainment') {
            $('div[name="divEntertrainTitle"]').css("display", "");
            
            if(changeVal == undefined)
            {
               checkServiceSelection('div[name="divSelectedEntertrain"]',chkbox)
            }
            else if(changeVal == true)
            {
                $('div[name="divSelectedEntertrain"]').css("display", "");
                $('div[name="divSelectedEntertrain"]').html(html);
                //tempcheckbox.checked = false;
            }
        }

        $('#spnUsed').html(amount.toFixed(2));

        var remAmount = parseFloat($('#txtBudget1').val()) - parseFloat(amount);

        $('#spnRemaining').html(remAmount.toFixed(2));
    }

    

    function checkServiceSelection(service,chkbox)
    {//alert($(service).css('display'));
    //alert($(service)[0].style.display);
        if($(service)[0].style.display != 'none')
        {
                 $('#divAlertPopup').html("Are you sure you want to change the selection?");
                    $('#divAlertPopup').dialog({
                        autoOpen: true,
                        show: "drop",
                        draggable: false,
                        hide: { effect: 'drop', direction: "right" },
                        modal: true,
                        resizable: false,
                        buttons: [
                            {
                                text: "Yes",
                                click: function () { $(this).dialog("close"); fillVenue(chkbox, true);}
                            },
                            {
                                text: "No",
                                click: function () { $(this).dialog("close"); fillVenue(chkbox,false);}
                            }
                        ]
                    });
        }
        else
        {
            fillVenue(chkbox,true)
        }
    }
    var currIndex = 2;
    var totalRows = 0;

    $(document).ready(function () {
        //$('#txtBudget1').val("30000");
        hideItems();
        //showMoreItems();
        $(window).scroll(function () {

        return;
            var pageHeight = ($(document).height() - $(window).height());
            var scrollPos = $(window).scrollTop();

            if (pageHeight == scrollPos) {
                if (totalRows <= currIndex)
                    return;
                $('#imgLoading').css('display', 'block');
                setTimeout(showMoreItems, 1500);
            }



        });
    });


    /**/
    function hideItems() {

        var items = $('#tableHotel').find('input[type=checkbox]');
        items.each(function () {
            this.checked = false;
        });

        currIndex = 2;
        totalRows = 0;
        $('#tableHotel tr').each(function (index, obj) {
            if (index > 2)
                $(obj).css('display', 'none');
            totalRows = index - 1;
        });
    }


    function showMoreItems() {
        $('#imgLoading').css('display', 'none');
        currIndex = currIndex + 2;

        $('#tableHotel tr').each(function (index, obj) {
            if (index == 0) {
                return true;
            }
            else if (index <= currIndex) {
                $(this).css('display', '');
            }

            //alert(index);
        });
    }

    


    function setNextService(liobj)
    {
    //debugger;
        var serviceFound = false;
        var nextServiceName = undefined;
        $('#litopservices li').each(function (index, obj) {
         
         if(serviceFound == true && nextServiceName==undefined)
           {
                if($(obj).css("display") != 'none')
                {
                    nextServiceName = $(obj).text();
                }
           }
           if($(liobj).text() == $(obj).text())
           {
                serviceFound = true;
           }
           
           //this.style = "";
        });
        if(nextServiceName==undefined)
         {
           nextServiceName = "Next"; 
         }
         
        $('#imgBtnNext2').text(nextServiceName);
    }


    function heighlightserviceli(liobj) {
    //debugger;
    
        $('#liCateringHotel').hide(500, function () { });
        $('#liPhotographerHotel').hide(500, function () { });
        $('#liEntertainmentHotel').hide(500, function () { });
        $('#liInvitationsHotel').hide(500, function () { });
        $('#liCateringHotel, #liPhotographerHotel, #liEntertainmentHotel, #liInvitationsHotel').css('display','none');
            if(liobj == undefined)
            {
                var firstLiHeighlighted = false;
                 $('#litopservices li').each(function () {
                   if(firstLiHeighlighted == false)
                   {
                       liobj= this;
                       firstLiHeighlighted = true;
                   }
                });
            }
            
         $('#'+liobj.id+'Hotel').show(500, function () { });

        $('#litopservices li').each(function (index, obj) {
           var display = $(obj).css("display");
            //$(obj).removeAttr("style");
            $(obj).removeClass("active");
            $(obj).css("display", display);
            
            //this.style = "";
        });

        
        $(liobj).addClass("active");
        selectedService = liobj.id;
        setNextService(liobj);
        
    }

    function maximizeHotel(hotelname, address, amount, imgSrc) {

        $('#spnMaxiHotelName').html(hotelname);
        $('#spnHotelAddress').html(address);
        $('#spnHotelAmount').html(amount);
        $('#imgHotelImage')[0].src = imgSrc;


        $('#maxiHotel').dialog({
            autoOpen: true,
            show: "drop",
            width: 900,
            height: 500,
			dialogClass: 'uiReviewPop',
            draggable: false,
            hide: { effect: 'drop' },
            modal: true,
            resizable: false,
            buttons: [
                            {
                                text: "Close",
                                click: function () { $(this).dialog("close"); }
                            }
                        ]
        });
    }
   

   function colapseExpand(handle,divobj)
   {
//alert( $(handle).css("background-image"));
//alert('url("'+document.URL.substring(0,document.URL.indexOf("event-details.htm"))+"images/summary-box.jpg"+'")');

        if($(handle).css("background-image").indexOf("summary-box-plus.jpg") > -1)
        {
    
            $('#'+divobj).show('slow');
            $(handle).css("background-image",'url("'+document.URL.substring(0,document.URL.indexOf("event-details.htm"))+"images/summary-box.jpg"+'")');
        }
        else
        {
            $('#'+divobj).hide('slow');
            $(handle).css("background-image",'url("'+document.URL.substring(0,document.URL.indexOf("event-details.htm"))+"images/summary-box-plus.jpg"+'")');
        }
    
   }

   $(document).ready(function(){
   //alert(document.URL.replace("event-details.htm","")); 
   });
   
  // $('#paymentTabs li').live('click', function(i,e){
   $('#paymentTabs li').on('click', function(i,e){
  		$(this).addClass('active')
			   .siblings()
			   .removeClass('active');
		var id = $(this).attr('id');
		$('#creditCardDetails,#debitCardDetails,#netBankingDetails').hide(0);
		$('#'+ id + 'Details').show(100);
 	})
   
   //$('#popTab li').live('click', function(i,e){
   $('#popTab li').on('click', function(i,e){
		var curId = $(this).attr('id');
		var curClass = $(this).attr('class');
		if(curClass == 'inactiveTab'){
			$(this).toggleClass('activeTab inactiveTab');
			$(this).siblings('li').toggleClass('activeTab inactiveTab');
			$('.rewPopContainer').find('.popUpTabContents').hide();
			$('.rewPopContainer').find('#'+ curId +'-content.popUpTabContents').show();
			
		}
	})
   
   	
//$('#adminTab li').live('click', function(){
$('#adminTab li').on('click', function(){
	$(this).addClass('active').siblings('li').removeClass('active');
	$('#adminTabContent').find('.adminTabContent').hide();
	var id = $(this).attr('id');
	$('#adminTabContent #'+ id +'-content').fadeIn(500);
	selectCategory();
})
function selectCategory(){
	var initVal = $('#selectCategory').attr('value');
	$('#'+ initVal +'Content').fadeIn(500);
	$('#selectCategory').change(function(){
		var value = $(this).attr('value');
		$('.selectCatContent').hide();
		$('#'+ value +'Content').fadeIn(500);
	})
}