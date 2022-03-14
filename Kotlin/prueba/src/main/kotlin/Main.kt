import java.io.File
import kotlin.math.max

class Pais(var nombre: String, var cerveza: Int, var espirituosas: Int, var vino: Int)

fun selector(pais: Pais): Int {
    return max(max(pais.cerveza, pais.espirituosas), pais.vino)
}

fun main() {
    val lineas: List<String> = File("data/drinks.txt").readLines()
    val paises: MutableList<Pais> = mutableListOf()
    var trozos: List<String>
    var nombre: String
    var cerveza: Int
    var espirituosas: Int
    var vino: Int

    for (linea in lineas) {
        trozos = linea.split(",")
        nombre = trozos[0]
        cerveza = trozos[1].toInt()
        espirituosas = trozos[2].toInt()
        vino = trozos[3].toInt()
        paises += Pais(nombre, cerveza, espirituosas, vino)
    }

    paises.sortByDescending { selector(it) }
    paises.forEach { println("${it.nombre} : ${selector(it)}") }
}