document.addEventListener('DOMContentLoaded', function() {
    // data confirm
    document.body.addEventListener("click", function(e) {
        var el = e.target;
        var confirmMessage = el.getAttribute("data-confirm");

        if(!!confirmMessage) {
            e.preventDefault();

            if(confirmMessage && confirm(confirmMessage)) {
                el.closest('form').submit();
            }
        }
    });

    // auto save
    var timer = null;
    var saveCount = 0;
    document.body.addEventListener('keyup', function(e) {
        if (e.target.matches('[name="post/title"],[name="post/body"]')) {
            if (!!timer) {
                // reset timer
                clearTimeout(timer);
            }

            document.getElementById("status").textContent = "Unsaved";
            var form = e.target.form;

            // auto save if nothing happens in the next 5 sec
            timer = setTimeout(function () {
                var url = form.attributes.action.value;
                var method = form.attributes.method.value;
                var data = new FormData(form);
                var request = new XMLHttpRequest();
                request.open(method, url);
                request.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
                request.onload = function(event) {
                    timer = null;
                    console.log(request);
                    if (request.status == 200) {
                        document.getElementById("status").textContent = "Saved";
                    }
                    else {
                        document.getElementById("status").textContent = "Something went wrong";
                    }
                };
                request.send(data);
            }, 5000);
        }
    });
});
