var url = contextRoot;
//alert("test");



function kommentoi(id) {
    
var http = new XMLHttpRequest()

http.onreadystatechange = function() {
    if (this.readyState != 4) {
        return
    }
    
    console.log("tallennettu");
            location.reload();
}
    
    var data = {
        parentMessageId: id,
        sisalto: document.getElementById("text-" + id).value
    }

    http.open("POST", url  + "wallcomments")
    http.setRequestHeader('Accept', 'application/json')
    http.setRequestHeader('Content-Type', 'application/json')
    http.send(JSON.stringify(data))


}

function like(id) {
    
var http = new XMLHttpRequest()

http.onreadystatechange = function() {
    if (this.readyState != 4) {
        return
    }
    
    console.log("tykätty");
    location.reload();
}
    
    var data = {}
    console.log("liking " + id);
    http.open("POST", url  + "wallLike/" + id)
    http.setRequestHeader('Accept', 'application/json')
    http.setRequestHeader('Content-Type', 'application/json')
    http.send(JSON.stringify(data))

}

  
function getcomments(id) {
    
    var http = new XMLHttpRequest()

http.onreadystatechange = function() {
    if (this.readyState != 4 || this.status != 200) {
        return
    }
    
    response = JSON.parse(this.responseText)
    console.log(response);
    var inner = "";
    for (var i = 0; i < response.length; i++){
        inner += "<p>" + response[i].name + "</p>";
    }

}
    
    http.open("GET", url + "wallcomments/" + id)
    http.send()
}