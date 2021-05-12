$(document).ready(function () {

    let termsCounter = 1;
    let queries = [];
    let stompClient = null;

    $.ajax({
        url: contextPath + "/stat/query/all",
        success: function (response) {
            queries = response;
            for (let i = 0; i < queries.length; i++) {
                $('#querySelect').append(`
                    <option id="query-${i}">${queries[i].name}</option>
                `);
            }
        }
    });

    $('#addTermButton').on('click', function () {

        $('table tbody').append(`
            <tr id="termRecord-${termsCounter}">
                <td>
                    <div class="input-group">
                        <input id="termInput-${termsCounter}" type="text" class="form-control" placeholder="Term...">
                    </div>
                </td>
                <td style="width: 5%">
                    <button type="button" class="btn btn-danger" id="deleteTerm-${termsCounter}">
                        &times;
                    </button>
                </td>
            </tr>
        `);
        for (let i = 1 ; i <= termsCounter; i++) {
            $(document).on('click', `#deleteTerm-${i}`, function () {
                $(`#termRecord-${i}`).remove();
            });
        }
        termsCounter++;
    });

    $('#crawlButton').on('click', function() {
        let $output = $('#output');
        $output.text("");
        let loadingImage = $('#loadingImage');
        let seed = $('#inputSeed').val();
        let terms = [];
        for (let i = 0; i < termsCounter; i++) {
            let $termInput = $(`#termInput-${i}`);
            if ($termInput.length) {
                terms.push($termInput.val());
            }
        }
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            url: contextPath + '/stat/add',
            data: JSON.stringify({
                seed : seed,
                terms : terms
            }),
            dataType: 'json',
            beforeSend: function() {
                console.log("beforeSend")
                loadingImage.show();
                let socket = new SockJS('/websocket');
                stompClient = Stomp.over(socket);
                stompClient.connect({}, function () {
                    stompClient.subscribe('/crawling/log', function (log) {
                        $output.append(">>" + JSON.parse(log.body).content + "\n");
                        $output.scrollTop = $output.scrollHeight;
                    });
                });
            },
            complete: function () {
                $output = $('#output');
                console.log("complete");
                if (stompClient !== null) {
                    stompClient.disconnect();
                }
                loadingImage.hide();
                $output.append("Crawling finished\n");
                $output.scrollTop = $output.scrollHeight;
                $.ajax({
                    url: contextPath + "/stat/query/all",
                    success: function (response) {
                        queries = response;
                        let $querySelect = $('#querySelect');
                        $querySelect.html(`
                            <option>-</option>
                        `)
                        for (let i = 0; i < queries.length; i++) {
                            $querySelect.append(`
                                <option id="query-${i}">${queries[i].name}</option>
                            `);
                        }
                    }
                }).fail(function(xhr, status, error) {
                    $('#output').append("Some error occurred\n");
                    console.log(error);
                    console.log(status);
                });
            }
        });
    });
});