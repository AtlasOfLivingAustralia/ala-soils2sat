if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}

function showModal(options) {

    var opts = {
        url: options.url ? options.url : false,
        id: options.id ? options.id : 'modal_element_id',
        height: options.height ? options.height : 500,
        width: options.width ? options.width : 600,
        title: options.title ? options.title : 'Modal Title',
        hideHeader: options.hideHeader ? options.hideHeader : false,
        onClose: options.onClose ? options.onClose : null
    }

    var html = "<div id='" + opts.id + "' class='modal hide fade' role='dialog' aria-labelledby='modal_label_" + opts.id + "' aria-hidden='true' style='height: " + opts.height + "px;width: " + opts.width + "px; overflow: hidden'>";
    if (!opts.hideHeader) {
        html += "<div class='modal-header'><button type='button' class='close' data-dismiss='modal' aria-hidden='true'>x</button><h3 id='modal_label_" + opts.id + "'>" + opts.title + "</h3></div>";
    }
    html += "<div class='modal-body' style='max-height: " + opts.height + "px'>loading</div></div>";

    $("body").append(html);

    var selector = "#" + opts.id;

    $(selector).on("hidden", function() {
        $(selector).remove();
        if (opts.onClose) {
            opts.onClose();
        }
    });

    $(selector).modal({
        remote: opts.url
    });
}

function hideModal() {
    $("#modal_element_id").modal('hide');
}
