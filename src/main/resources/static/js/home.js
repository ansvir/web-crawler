$(document).ready(function () {

    let termsCounter = 1;
    let queries = [];
    let stompClient = null;
    let crawlingFinished = true;

    $('#loadingImage').hide();

    $.ajax({
        url: contextPath + "/stat/query/all",
        success: function (response) {
            queries = response;
            for (let i = 0; i < queries.length; i++) {
                $('#querySelect').append(`
                    <option name="queryOption" id="query-${queries[i].id}">${queries[i].name}</option>
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
        for (let i = 1; i <= termsCounter; i++) {
            $(document).on('click', `#deleteTerm-${i}`, function () {
                $(`#termRecord-${i}`).remove();
            });
        }
        termsCounter++;
    });

    $('#crawlButton').on('click', function () {
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
                seed: seed,
                terms: terms
            }),
            dataType: 'json',
            beforeSend: function () {
                crawlingFinished = false;
                loadingImage.show();
                let socket = new SockJS('/websocket');
                stompClient = Stomp.over(socket);
                stompClient.connect({}, function () {
                    stompClient.subscribe('/crawling/log', function (log) {
                        $output.append(">>" + JSON.parse(log.body).content + "\n");
                        if($output.length) {
                            $output.scrollTop($output[0].scrollHeight - $output.height());
                        }
                    });
                });
            },
            complete: function () {
                crawlingFinished = true;
                $output = $('#output');
                if (stompClient !== null) {
                    stompClient.disconnect();
                }
                loadingImage.hide();
                $output.append("Crawling finished\n");
                if($output.length) {
                    $output.scrollTop($output[0].scrollHeight - $output.height());
                }
                $.ajax({
                    url: contextPath + "/stat/query/all",
                    success: function (response) {
                        queries = response;
                        let $querySelect = $('#querySelect');
                        $querySelect.html(`
                            <option name="queryOption">-</option>
                        `)
                        for (let i = 0; i < queries.length; i++) {
                            $querySelect.append(`
                                <option name="queryOption" id="query-${queries[i].id}">${queries[i].name}</option>
                            `);
                        }
                    }
                }).fail(function (xhr, status, error) {
                    $('#output').append("Some error occurred\n");
                    crawlingFinished = true;
                    console.log(error);
                    console.log(status);
                });
            }
        });
    });

    $('#stopCrawlButton').on('click', function () {
        if (crawlingFinished) {
            return;
        }
        $('#loadingImage').hide();
        stompClient.send("/app/stop");
    });

    $(document).on('change', '#querySelect', function () {
        $('#downloadAllStatReport').on('click', function () {
            let $querySelect = $('#querySelect');
            if ($querySelect.val() === "-") {
                return;
            }
            let queryId = null;
            $.each($("#querySelect option:selected"), function () {
                queryId = $(this).attr('id');
                queryId = queryId.substring(6);
                console.log(queryId);
            });

            $.ajax({
                url: contextPath + `/stat/all/${queryId}`,
                success: function() {
                    window.location = contextPath + `/stat/all/${queryId}`;
                }
            });

        });
        $('#downloadTopTenStatReport').on('click', function () {
            let $querySelect = $('#querySelect');
            if ($querySelect.val() === "-") {
                return;
            }
            let queryId = null;
            $.each($("#querySelect option:selected"), function () {
                queryId = $(this).attr('id');
                queryId = queryId.substring(6);
                console.log(queryId);
            });

            $.ajax({
                url: contextPath + `/stat/top/ten/${queryId}`,
                success: function() {
                    window.location = contextPath + `/stat/top/ten/${queryId}`;
                }
            });
        });
    });

    $('#output').on('input', function() {
        if ($(this).val().length >= 5000) {
            $(this).val("");
        }
    })
});