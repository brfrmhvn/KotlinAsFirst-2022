@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import kotlin.math.pow

/**
 * Фабричный метод для создания комплексного числа из строки вида x+yi
 */
fun Complex(s: String): Complex {
    val complex = s.replace("i", "").replace("+", " +").replace("-", " -").split(" ").map { it.toDouble() }
    return Complex(complex.first(), complex.last())
}

/**
 * Класс "комплексное число".
 *
 * Общая сложность задания -- лёгкая, общая ценность в баллах -- 8.
 * Объект класса -- комплексное число вида x+yi.
 * Про принципы работы с комплексными числами см. статью Википедии "Комплексное число".
 *
 * Аргументы конструктора -- вещественная и мнимая часть числа.
 */
class Complex(val re: Double, val im: Double) {

    /**
     * Конструктор из вещественного числа
     */
    constructor(x: Double) : this(x, 0.0)

    /**
     * Сложение.
     */
    operator fun plus(other: Complex): Complex = Complex(this.re + other.re, this.im + other.im)

    /**
     * Смена знака (у обеих частей числа)
     */
    operator fun unaryMinus(): Complex = Complex(-this.re, -this.im)

    /**
     * Вычитание
     */
    operator fun minus(other: Complex): Complex = Complex(this.re - other.re, this.im - other.im)

    /**
     * Умножение
     */
    operator fun times(other: Complex): Complex =
        Complex(this.re * other.re - this.im * other.im, this.im * other.re + this.re * other.im)

    /**
     * Деление
     */
    operator fun div(other: Complex): Complex =
        Complex(
            (this.re * other.re + this.im * other.im) / (other.im.pow(2) + other.re.pow(2)),
            (this.im * other.re - this.re * other.im) / (other.im.pow(2) + other.re.pow(2))
        )

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = other is Complex && im == other.im && re == other.re

    /**
     * Преобразование в строку
     */
    override fun toString() =
        this.re.toString() + (if (this.im > 0) "+" + this.im.toString() else "-" + (-this.im).toString()) + "i"

    override fun hashCode(): Int {
        var result = re.hashCode()
        result = 31 * result + im.hashCode()
        return result
    }
}