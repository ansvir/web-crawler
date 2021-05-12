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
                    <option id="query-${queries[i].id}">${queries[i].name}</option>
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
                                <option id="query-${queries[i].id}">${queries[i].name}</option>
                            `);
                        }
                    }
                }).fail(function (xhr, status, error) {
                    $('#output').append("Some error occurred\n");
                    console.log(error);
                    console.log(status);
                });
            }
        });
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
});