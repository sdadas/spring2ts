
function expect(result, expected) {
    if(typeof result["then"] !== "undefined") {
        result.then(function(msg){ checkEquals(msg, expected); })
    } else {
        throw new Error("Unknown response type");
    }
}

function checkEquals(first, second) {
    var result = _.isEqual(first, second);
    var firstJson = JSON.stringify(first);
    var secondJson = JSON.stringify(second);
    var el = document.createElement("h2");
    if(!result) {
        el.style.color = "red";
        el.className += " check-failed";
    }
    el.innerText = firstJson + (result ? " == " : " != ") + secondJson;
    document.body.appendChild(el);
}