var url = contextRoot;
//alert("test");


function kommentoi(id) {
    
var http = new XMLHttpRequest()

http.onreadystatechange = function() {
    if (this.readyState != 4) {
        return
    }
    location.reload();
    console.log("tallennettu");
}
    
    var data = {
        parentPictureId: id,
        sisalto: document.getElementById("text-" + id).value
    }

    http.open("POST", url  + "piccomments")
    http.setRequestHeader('Accept', 'application/json')
    http.setRequestHeader('Content-Type', 'application/json')
    http.send(JSON.stringify(data))


}


function profiilikuvaksi(id) {
    
var http = new XMLHttpRequest()

http.onreadystatechange = function() {
    if (this.readyState != 4) {

        return
    }
    
    console.log("tallennettu");
}
    
    var data = {
    }

    http.open("POST", url  + "kuva/profiilikuvaksi/" + id);
    http.setRequestHeader('Accept', 'application/json')
    http.setRequestHeader('Content-Type', 'application/json')
    http.send(JSON.stringify(data))
    location.reload();

}

function poistakuva(id) {
    
var http = new XMLHttpRequest()

http.onreadystatechange = function() {
    if (this.readyState != 4) {

        return
    }
    
    console.log("tallennettu");
}
    
    var data = {
    }

    http.open("POST", url  + "kuva/poistakuva/" + id);
    http.setRequestHeader('Accept', 'application/json')
    http.setRequestHeader('Content-Type', 'application/json')
    http.send(JSON.stringify(data))
    location.reload();

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

    http.open("POST", url  + "picLike/" + id)
    http.setRequestHeader('Accept', 'application/json')
    http.setRequestHeader('Content-Type', 'application/json')
    http.send(JSON.stringify(data))

}
