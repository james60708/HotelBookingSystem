(function($) {

    'use strict';

    $.getJSON('/GetAllHotel', function (data) {
        
        $(".post .media").remove();
        var template = `
        <div class="media media-custom d-block mb-4 h-100">
            <a href="rooms?id=%(id)s" class="mb-4 d-block">
                <div class="row py-2">
                    <div class="col-lg-4 col-12 align-self-center">
                        <img src="images/room_picture/room_%(pic)s.jpg" width="100%%" alt="Image placeholder">
                    </div>
                    <div class="col-lg-5 col-12 align-self-center">
                        <div class="media-body">
                            <span class="meta-post"> %(star)s <span class="fa fa-star text-primary"></span></span>
                            <h2 class="mt-0 mb-3">%(locality)s %(street)s</h2>
                            <span>Single Room Price: %(singleRoomPrice)s (%(singleRoomNum)s remained)</span></br>
                            <span>Double Room Price: %(doubleRoomPrice)s (%(doubleRoomNum)s remained)</span></br>
                            <span>Quad Room Price: %(quadRoomPrice)s (%(quadRoomNum)s remained)</span></br>
                            <span>TWD: %(avgprice)s (total price per day)</span>
                        </div>
                    </div>
                </div>
            </a>
        </div>
        `;
        
        var empty = `
            <div class="media media-custom d-block mb-4 h-100">
                <div class="row py-2">
                    <div class="col-lg-4 col-12 align-self-center">
                        <img src="images/noresult.png" width="100%%" alt="Image placeholder">
                    </div>
                    <div class="col-lg-5 col-12 align-self-center">
                        <div class="media-body">                           
                            <h1 class="mt-0 mb-3">Search No Result!</h1>
                        </div>
                    </div>
                </div>
            </div>
            `;
        
        var i;
        for(i = 0;i < data.length; ++i){
        	data[i]["pic"] = Math.floor(Math.random()*23 +1);
        	console.log(data[i]["pic"]);
            var tmp = sprintf(template, data[i]);
            $("div.post").append(tmp);
        }
        if(data.length == 0)
        	$("div.post").append(empty);

        //$.each(data, function (key, value) {

        //});
    });
})(jQuery);





