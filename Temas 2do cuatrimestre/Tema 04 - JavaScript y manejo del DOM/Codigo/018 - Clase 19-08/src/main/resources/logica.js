alert('Hola Mundo con JS!');
console.log('Esto es un log');

var identificarVar = 0;
let identificadorLet = "";
const IDENTIFICADORCONST = 'const';

/*
let arreglo = ['Texto', 1, true, null, undefined, 'Ensalada de frutas'];

for(let i = 0; arreglo.length; i++){
    console.log(arreglo[i])
}*/

function hacerClic(){
    alert('Hiciste clic sobre el boton');
}

const inputNombre = document.getElementById('campoNombre');
const buttonSaludar = document.getElementById('btnSaludar');
const parrafoSaludo = document.getElementById('msjSaludo');

buttonSaludar.addEventListener("click", function (){
    let nombreIngresado = inputNombre.value;

    if(nombreIngresado.trim() === ''){
        alert('Ingrese un nombre');
    } else {

        parrafoSaludo.textContent = 'Hola ¡' + nombreIngresado + '!, Bienvenido a la clase de JS';
        parrafoSaludo.style.color = 'red';
        inputNombre.value = '';
    }
});