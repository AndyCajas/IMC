package app.example.imc.data

class IMC(
    val indice: Float
) {
    companion object {
        const val NORMAL = "NORMAL"
        const val SOBREPESO = "SOBREPESO"
        const val OBESIDAD1 = "OBESIDAD1"
        const val OBESIDAD2 = "OBESIDAD2"
        const val OBESIDAD_EXTREMA = "OBESIDAD_EXTREMA"
        const val BAJO_PESO="BAJO_PESO"
    }

    fun estado(): String {
        return when {
            indice < 18.5 -> BAJO_PESO
            indice in 18.5..24.9 -> NORMAL
            indice in 25.0..29.9 -> SOBREPESO
            indice in 30.0..34.9 -> OBESIDAD1
            indice in 35.0..39.9 -> OBESIDAD2
            else -> OBESIDAD_EXTREMA
        }


    }

    fun sugerencias(): String {
        var mensaje = ""
        if (estado().equals(NORMAL)) {
            mensaje = " 1. Peso Normal (IMC entre 18.5 y 24.9)\n" +
                    "¡Felicidades! Estás en un rango saludable. Aún así, mantener un estilo de vida equilibrado es esencial.\n" +
                    "\n" +
                    "Alimentación saludable:\n" +
                    "Actividad física regular:\n" +
                    "Cuidado preventivo:\n"

        } else if (estado().equals(SOBREPESO)) {
            mensaje = "2. Sobrepeso (IMC entre 25 y 29.9)\n" +
                    "Es momento de ajustar tu rutina para prevenir complicaciones de salud.\n" +
                    "\n" +
                    "Dieta equilibrada:\n" +
                    "Ejercicio regular:\n"


        } else if (estado().equals(OBESIDAD1)) {
            mensaje = "3. Obesidad Grado 1 (IMC entre 30 y 34.9)\n" +
                    "Tomar acción es importante para prevenir problemas de salud más graves.\n" +
                    "\n" +
                    "Plan de alimentación:\n" +
                    "Actividad física:\n"
        } else if (estado().equals(OBESIDAD2)) {
            mensaje = "4. Obesidad Grado 2 (IMC entre 35 y 39.9)\n" +
                    "Un enfoque estructurado y apoyo profesional son clave.\n" +
                    "\n" +
                    "Intervención nutricional:\n" +
                    "Rutina de ejercicio segura:\n"
        } else if (estado().equals(OBESIDAD_EXTREMA)) {
            mensaje = "5. Obesidad Grado 3 (Obesidad Extrema, IMC ≥ 40)\n" +
                    "Este nivel requiere atención médica urgente para evitar complicaciones graves.\n" +
                    "\n" +
                    "Equipo multidisciplinario:\n" +
                    "Dieta controlada:\n"
        }else if(estado().equals(BAJO_PESO)){
            mensaje="bajo peso"
        }
        return mensaje
    }
}
