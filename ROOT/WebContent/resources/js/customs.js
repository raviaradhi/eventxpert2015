/* Fix for older browsers */
if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function(obj, start) {
         for (var i = (start || 0), j = this.length; i < j; i++) {
             if (this[i] === obj) { return i; }
         }
         return -1;
    }
}


(function($){
    
    "use strict";

	$(document).ready(function(){

        /* Loader - Remove it to disable loader
        ================================================== */
        jQuery("body").queryLoader2({
            onComplete: function() {
                $(".ut-loader-overlay").fadeOut(500, "easeInOutExpo", function() {
                    $(this).remove();
                });
				
				/* Wow - Scroll Animation
                ================================================== */
                wow.init();
				
				/* TYPIST - ANIMATED TYPING TEXT
                ================================================== */
				$('.typist1')
				.typist({ speed: 12 })
				.typistPause(0) // 0 sec
				.typistAdd('WELCOME TO LUXURY HOTEL')
				.typistStop();
				
				$('.typist2')
				.typist({ speed: 20 })
				.typistPause(3000) // 4 sec
				.typistAdd('Book Direct for Exclusive Offers and Lowest Rates Every Day From Our Official Website')
				.typistStop();

            },
            showbar: "on",
            barColor: "#EC4979",
            textColor: "#EC4979",
            backgroundColor: "#FFF",
            overlayId: 'qLoverlay',
            barHeight: 12,
            percentage: true,
            deepSearch: true,
            completeAnimation: "fade",
            minimumTime: 500  
        });

        /* WOW plugin triggers animation.css on scroll
        ================================================== */
        var wow = new WOW({
            boxClass:     'wow', // animated element css class (default is wow)
            offset:       200,   // distance to the element when triggering the animation (default is 0)
            mobile:       false  // trigger animations on mobile devices (true is default)
		});

        

    });

	
	
	$(document).ready(function(){
	
		/***************************************************************************/
		/* Flex slider */
		/***************************************************************************/

		//testimonal slider
		$('.testimonial-slider').flexslider({
			animation: "slide",
			slideshow: false,
			useCSS : false,
			prevText: "",
			nextText: "",    
			animationLoop: true 	
		});
		
		//package slider
		$('.package-slider').flexslider({
			animation: "slide",
			slideshow: false,
			useCSS : false,
			prevText: "",
			nextText: "",    
			animationLoop: true 	
		});
		
		$('#mainFlexSlider').flexslider(
			{
				animation: 'fade',
				slideshow: true,
				pauseOnHover: true,  
				controlNav: false,
				prevText: "<i class='fa fa-chevron-left'></i>", //String: Set the text for the "previous" directionNav item
				nextText: "<i class='fa fa-chevron-right'></i>", 
			}
		);

	});
	
	
	
	
	
	$(document).ready(function() {
	
		

		/* ------------------------------------------------------------------------ */
		/* BACK TO TOP 
		/* ------------------------------------------------------------------------ */

		$(window).scroll(function(){
			if($(window).scrollTop() > 500){
				$("#back-to-top").fadeIn(200);
			} else{
				$("#back-to-top").fadeOut(200);
			}
		});
		
		$('#back-to-top, .back-to-top').click(function() {
			  $('html, body').animate({ scrollTop:0 }, '500');
			  return false;
		});

		/* ------------------------------------------------------------------------ */
		/* CUSTOM SELECT
		/* ------------------------------------------------------------------------ */
		
		$('select.styled-select').customSelect();
		
		/* ------------------------------------------------------------------------ */
		/* DATE PICKER
		/* ------------------------------------------------------------------------ */
		
		if ( $('.form-control[data-provide="datepicker"]').length > 0 ) {
			$('.form-control[data-provide="datepicker"]').datepicker().on('show', function(e){
				$('.datepicker').css('min-width', $(this).outerWidth() );
			});
		}
		
		/* ------------------------------------------------------------------------ */
		/* CUSTOMIZABLE SCROLLBAR
		/* ------------------------------------------------------------------------ */
		$("html").niceScroll({
			mousescrollstep: 40,
			cursorcolor: "#ED4A7A",
			zindex: 9999,
			cursorborder: "none",
			cursorwidth: "6px",
			cursorborderradius: "none"
		});
		
		/* Counter
        ================================================== */
        $('.counter').each(function(){
            var counter = $(this).data('counter');
            var $this = $(this);
            $this.waypoint(function(direction) {
                if( !$(this).hasClass('animated') ) {    
                    $(this).countTo({
                        from: 0,
                        to: counter,
                        speed: 2000,
                        refreshInterval: 100,
                        onComplete: function() {
                            $(this).addClass('animated');
                        }
                    });
                }
            } , { offset: '100%' } );
        });

        /* PARALAX
        ================================================== */
        if( !device.tablet() && !device.mobile() ) {

            $('section[data-type="parallax"]').each(function(){
                $(this).parallax("50%", 0.4);
            });

            /* fixed background on mobile devices */
            $('section[data-type="parallax"]').each(function(index, element) {
                $(this).addClass('fixed');
            });
                
        }
		
		/* LAZY LOAD
        ================================================== */
		$(function() {
			$("img.lazy").lazyload({
				effect : "fadeIn"
			});
		});

	});
	
	/* Count To Function
    ================================================== */
    $.fn.countTo = function (options) {
        options = options || {};
        
        return $(this).each(function () {
            // set options for current element
            var settings = $.extend({}, $.fn.countTo.defaults, {
                from:            $(this).data('from'),
                to:              $(this).data('to'),
                speed:           $(this).data('speed'),
                refreshInterval: $(this).data('refresh-interval'),
                decimals:        $(this).data('decimals')
            }, options);
            
            // how many times to update the value, and how much to increment the value on each update
            var loops = Math.ceil(settings.speed / settings.refreshInterval),
                increment = (settings.to - settings.from) / loops;
            
            // references & variables that will change with each update
            var self = this,
                $self = $(this),
                loopCount = 0,
                value = settings.from,
                data = $self.data('countTo') || {};
            
            $self.data('countTo', data);
            
            // if an existing interval can be found, clear it first
            if (data.interval) {
                clearInterval(data.interval);
            }
            data.interval = setInterval(updateTimer, settings.refreshInterval);
            
            // initialize the element with the starting value
            render(value);
            
            function updateTimer() {
                value += increment;
                loopCount++;
                
                render(value);
                
                if (typeof(settings.onUpdate) == 'function') {
                    settings.onUpdate.call(self, value);
                }
                
                if (loopCount >= loops) {
                    // remove the interval
                    $self.removeData('countTo');
                    clearInterval(data.interval);
                    value = settings.to;
                    
                    if (typeof(settings.onComplete) == 'function') {
                        settings.onComplete.call(self, value);
                    }
                }
            }
            
            function render(value) {
                var formattedValue = settings.formatter.call(self, value, settings);
                $self.html(formattedValue);
            }
        });
    };
    
    $.fn.countTo.defaults = {
        from: 0,               // the number the element should start at
        to: 0,                 // the number the element should end at
        speed: 1000,           // how long it should take to count between the target numbers
        refreshInterval: 100,  // how often the element should be updated
        decimals: 0,           // the number of decimal places to show
        formatter: formatter,  // handler for formatting the value before rendering
        onUpdate: null,        // callback method for every time the element is updated
        onComplete: null       // callback method for when the element finishes updating
    };
    
    function formatter(value, settings) {
        return value.toFixed(settings.decimals);
    }
	
	/* ------------------------------------------------------------------------ */
	/* CONTACT FORM VALIDATOR
	/* ------------------------------------------------------------------------ */
	$(document).ready(function() {
		$('#contantForm').bootstrapValidator({
			message: 'This value is not valid',
			//live: 'submitted',
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon glyphicon-remove',
				validating: 'glyphicon glyphicon-refresh'
			},
			submitHandler: function(validator, form) {
				// validator is the BootstrapValidator instance
				// form is the jQuery object present the current form
				form.find('.alert').html('Thanks for signing up. Now you can sign in as ' + validator.getFieldElements('conactName').val()).show();
			},
			fields: {
				conactName: {
					message: 'The name is not valid',
					validators: {
						notEmpty: {
							message: 'The name is required and can\'t be empty'
						},
						stringLength: {
							min: 6,
							max: 30,
							message: 'The name must be more than 6 and less than 30 characters long'
						},
						/*remote: {
							url: 'remote.php',
							message: 'The username is not available'
						},*/
						regexp: {
							regexp: /^[a-zA-Z0-9_\.]+$/,
							message: 'The name can only consist of alphabetical, number, dot and underscore'
						}
					}
				},
				email: {
					validators: {
						notEmpty: {
							message: 'The email address is required and can\'t be empty'
						},
						emailAddress: {
							message: 'The input is not a valid email address'
						}
					}
				},
				subject: {
					validators: {
						notEmpty: {
							message: 'The subject is required and can\'t be empty'
						}
					}
				},
				messagetext: {
					validators: {
						notEmpty: {
							message: 'The messgae is required and can\'t be empty'
						}
					}
				}
			}
		});
	});

	/* ------------------------------------------------------------------------ */
	/* NEWSLETTER VALIDATOR
	/* ------------------------------------------------------------------------ */
	$(document).ready(function() {
		$('#newsLetterForm').bootstrapValidator({
			message: 'This value is not valid',
			//live: 'submitted',
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon glyphicon-remove',
				validating: 'glyphicon glyphicon-refresh'
			},
			submitHandler: function(validator, form) {
				// validator is the BootstrapValidator instance
				// form is the jQuery object present the current form
				form.find('.alert').html('Thanks for signing up. Now you can sign in as ' + validator.getFieldElements('conactName').val()).show();
			},
			fields: {
				email: {
					validators: {
						notEmpty: {
							message: 'The email address is required and can\'t be empty'
						},
						emailAddress: {
							message: 'The input is not a valid email address'
						}
					}
				}
			}
		});
	});
	
	
	/* ------------------------------------------------------------------------ */
	/* MAP
	/* ------------------------------------------------------------------------ */
	$(function(){
		$('#address-map-1').gmap3({
			marker:{
				latLng: [13.7500000, 100.5166700],
				options: { icon: 'images/mapicon.png' }
			},
			map:{
				options:{
					zoom: 14,
					scrollwheel: false,
				}
			}
		});
	});


  
})(jQuery);










