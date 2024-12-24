package app.example.imc.data

data class User (
    var id:String,
    var nombre:String,
    var apellido:String,
    var email:String,
    var password:String,
    var imagen:String
){
    constructor():this("","","","","","")
}