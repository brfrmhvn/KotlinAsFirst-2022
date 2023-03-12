@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import java.util.*
import kotlin.math.pow

/**
 * Класс "полином с вещественными коэффициентами".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса -- полином от одной переменной (x) вида 7x^4+3x^3-6x^2+x-8.
 * Количество слагаемых неограничено.
 *
 * Полиномы можно складывать -- (x^2+3x+2) + (x^3-2x^2-x+4) = x^3-x^2+2x+6,
 * вычитать -- (x^3-2x^2-x+4) - (x^2+3x+2) = x^3-3x^2-4x+2,
 * умножать -- (x^2+3x+2) * (x^3-2x^2-x+4) = x^5+x^4-5x^3-3x^2+10x+8,
 * делить с остатком -- (x^3-2x^2-x+4) / (x^2+3x+2) = x-5, остаток 12x+16
 * вычислять значение при заданном x: при x=5 (x^2+3x+2) = 42.
 *
 * В конструктор полинома передаются его коэффициенты, начиная со старшего.
 * Нули в середине и в конце пропускаться не должны, например: x^3+2x+1 --> Polynom(1.0, 2.0, 0.0, 1.0)
 * Старшие коэффициенты, равные нулю, игнорировать, например Polynom(0.0, 0.0, 5.0, 3.0) соответствует 5x+3
 */
class Polynom(vararg coeffs: Double) {
    private val polynom: List<Double> = when {
        coeffs.isEmpty() || coeffs.size == 1 && coeffs.first() == 0.0 -> listOf(0.0)
        else -> {
            val result = coeffs.reversed().toMutableList()
            while (result.last() == 0.0) {
                result.remove(result.last())
            }
            result.toList()
        }
    }

    /**
     * Геттер: вернуть значение коэффициента при x^i
     */
    fun coeff(i: Int): Double = polynom.getOrNull(i) ?: throw NoSuchElementException()

    /**
     * Расчёт значения при заданном x
     */
    fun getValue(x: Double): Double = polynom.indices.sumOf { x.pow(it) * coeff(it) }

    /**
     * Степень (максимальная степень x при ненулевом слагаемом, например 2 для x^2+x+1).
     *
     * Степень полинома с нулевыми коэффициентами считать равной 0.
     * Слагаемые с нулевыми коэффициентами игнорировать, т.е.
     * степень 0x^2+0x+2 также равна 0.
     */
    fun degree(): Int = polynom.size - 1

    /**
     * Сложение
     */
    operator fun plus(other: Polynom): Polynom {
        val result = mutableListOf<Double>()
        if (polynom.size < other.polynom.size) {
            for (i in other.polynom.indices) {
                result.add(polynom.getOrElse(i) { 0.0 } + other.polynom.getOrElse(i) { 0.0 })
            }
        } else {
            for (i in polynom.indices) {
                result.add(polynom.getOrElse(i) { 0.0 } + other.polynom.getOrElse(i) { 0.0 })
            }
        }
        return Polynom(*result.reversed().toDoubleArray())
    }

    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom = Polynom(*polynom.map { -it }.reversed().toDoubleArray())

    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom = plus(other.unaryMinus())

    /**
     * Умножение
     */
    operator fun times(other: Polynom): Polynom {
        val result = mutableMapOf<Int, Double>()
        for (i in polynom.indices) {
            for (k in other.polynom.indices) {
                if (result.containsKey(i + k)) result[i + k] =
                    polynom[i] * other.polynom[k] + result.getValue(i + k) else result[i + k] =
                    polynom[i] * other.polynom[k]
            }
        }
        return Polynom(*result.values.reversed().toDoubleArray())
    }

    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */
    operator fun div(other: Polynom): Polynom {
        val dividend = polynom.toMutableList()
        val divisor = other.polynom.toMutableList()
        val result = MutableList(dividend.size - divisor.size + 1) { 0.0 }
        while (dividend.size >= divisor.size) {
            val ratio = dividend.last() / divisor.last()
            val degree = dividend.size - divisor.size
            result[degree] = ratio
            for (i in 0 until divisor.size) {
                dividend[degree + i] -= ratio * divisor[i]
            }
            dividend.removeLast()
        }
        return Polynom(*result.reversed().toDoubleArray())
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom =
        if (polynom.size < other.polynom.size) Polynom(*polynom.toDoubleArray()) else Polynom(
            *(this - other * (this / other)).polynom.reversed().toDoubleArray()
        )

    /**
     * Преобразование в строку
     */
    //override fun toString(): String = "Polynom(${polynom.toList()}"

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = other is Polynom && polynom == other.polynom

    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int = polynom.hashCode()
}