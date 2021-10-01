function cadenaInsertar() {

    var cadena = document.getElementById("cadena").value;
    fetch('http://localhost:4567/testInsert?string=' + cadena)
}
