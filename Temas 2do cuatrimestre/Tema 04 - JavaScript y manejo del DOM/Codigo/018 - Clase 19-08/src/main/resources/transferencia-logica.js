const divErrores = document.getElementById('msjErrores');
const formulario = document.getElementById('formTransferencia');

formulario.addEventListener("submit", function (event){
    let errores = [];

    let inputCbu = document.getElementById('cbuDestino').value.trim();
    let inputMonto = parseFloat(document.getElementById('monto').value);
    let comboMotivo = document.getElementById('motivo').value;

    if(inputCbu === "") {
        errores.push("El CBU es obligatorio");
    } else if(isNaN(inputCbu)) {
        errores.push("El CBU debe de contener numeros");
    }

    if(isNaN(inputMonto) || inputMonto <= 0){
        errores.push("El monto debe de ser mayor a 0");
    }

    if(comboMotivo === "-"){
        errores.push("Debe de seleccionar un motivo");
    }

    if(errores.length > 0) {
        event.preventDefault();

        divErrores.innerHTML = "<ul> " + errores.map(err => `<li>${err}</li>`).join('') + "</ul>"
    } else {
        divErrores.innerHTML = "";
    }
});