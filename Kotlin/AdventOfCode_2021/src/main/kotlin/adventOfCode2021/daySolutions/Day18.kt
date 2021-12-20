package adventOfCode2021.daySolutions

import kotlin.math.max


interface SnailFishNumber{
    fun magnitude(): ULong
    fun reduce(): SnailFishNumber
    operator fun plus(other: SnailFishNumber): SnailFishNumber
}

fun List<SnailFishNumber>.sum(): SnailFishNumber = this.reduce { acc , next -> acc + next}


class Day18 : Day<List<SnailFishNumber>, ULong> {
    override fun parseInput(input: String): List<SnailFishNumber> {
        return input.lines().mapNotNull(::parseSnailFishNumber)
    }

    private fun parseSnailFishNumber(text:String): SnailFishNumber?{
        val (number, endIndex) = parseNextSnailFishNumberAtIndex(text, 0) ?: return null
        return if (endIndex == text.length - 1) number else null
    }

    private fun parseNextSnailFishNumberAtIndex(text:String, index: Int): Pair<SnailFishNumberBase, Int>?{
        if (index >= text.length) {
            return null
        }
        return when {
            text[index].isDigit() -> parseSimpleSnailFishNumberAtIndex(text, index)
            text[index] == '[' -> parsePairSnailFishNumberAtIndex(text, index)
            else -> null
        }
    }

    private fun parseSimpleSnailFishNumberAtIndex(text:String, index: Int): Pair<SnailFishNumberBase, Int>?{
        if(index >= text.length || !text[index].isDigit()) {
            return null
        }
        val valueTextBuilder = StringBuilder()
        var parseIndex = index
        while (parseIndex < text.length && text[parseIndex].isDigit()){
            valueTextBuilder.append(text[parseIndex])
            parseIndex++
        }
        val value = valueTextBuilder.toString().toULongOrNull() ?: return null
        return SimpleSnailFishNumber(value) to (parseIndex - 1)
    }

    private fun parsePairSnailFishNumberAtIndex(text:String, index: Int): Pair<SnailFishNumberBase, Int>?{
        if(index > text.length - 5 || text[index] != '['){
            return null
        }
        val (left, endOfLeft) = parseNextSnailFishNumberAtIndex(text, index + 1) ?: return null
        if(endOfLeft > text.length - 4 || text[endOfLeft + 1] != ','){
            return null
        }
        val (right, endOfRight) = parseNextSnailFishNumberAtIndex(text, endOfLeft + 2) ?: return null
        if (endOfRight > text.length - 2 || text[endOfRight + 1] != ']'){
            return null
        }
        return PairSnailFishNumber(left, right) to (endOfRight + 1)
    }

    override fun solvePart1(input: List<SnailFishNumber>): ULong {
        val snailFishSum = input.sum()
        return snailFishSum.magnitude()
    }

    override fun solvePart2(input: List<SnailFishNumber>): ULong {
        val combinations = input.flatMap { first -> input.map { second -> first to second } }
            .filter { (first, second) -> first != second}
        return combinations.maxOf { (first, second) -> (first + second).magnitude() }
    }
}

private abstract class SnailFishNumberBase(var parent: PairSnailFishNumber?): SnailFishNumber{
    var depth: UInt = 0U
    var needsSplit: Boolean = false

    private val needsExplosion: Boolean
    get() = depth >= 5U

    abstract fun explodeLeftMostLowest()
    abstract fun splitLeftMostRequiringIt(): SnailFishNumberBase

    abstract fun rightMostValue(): SimpleSnailFishNumber
    abstract fun leftMostValue(): SimpleSnailFishNumber

    val isReduced: Boolean
        get() = !needsExplosion && !needsSplit

    override fun reduce():SnailFishNumber = reduceBase()

    fun reduceBase(): SnailFishNumberBase{
        var reductionResult = this
        while (!reductionResult.isReduced){
            if (reductionResult.needsExplosion) {
                reductionResult.explodeLeftMostLowest()
            } else if(reductionResult.needsSplit) {
                reductionResult = reductionResult.splitLeftMostRequiringIt()
            }
        }
        return reductionResult
    }

    override fun plus(other: SnailFishNumber): SnailFishNumber {
        if (!(other is SnailFishNumberBase)){
            throw NotImplementedError("The SnailFishNumber interface is only supposed to be implemented via the abstract base, which is not supposed to be publicly exposed.")
        }
        return this + other
    }

    abstract fun copy(): SnailFishNumberBase

    operator fun plus(other: SnailFishNumberBase): SnailFishNumberBase {
        val result = PairSnailFishNumber(this.copy(), other.copy())
        return result.reduceBase()
    }
}

private class SimpleSnailFishNumber(val value: ULong):SnailFishNumberBase(null){
    init {
        depth = 0U
        needsSplit = value >= 10UL
    }

    override fun explodeLeftMostLowest() {
        throw IllegalStateException("This method should never be called on a leave node based on the specification.")
    }

    override fun splitLeftMostRequiringIt(): SnailFishNumberBase {
        val leftValue = value / 2UL
        val rightValue = value - leftValue
        val leftNumber = SimpleSnailFishNumber(leftValue)
        val rightNumber = SimpleSnailFishNumber(rightValue)
        //Note that we cannot add the two numbers because that could theoretically trigger a further split when reducing.
        //However, the ew pair might need to be exploded first, depending on where the split pair is in the whole number.
        return PairSnailFishNumber(leftNumber, rightNumber)
    }

    override fun rightMostValue(): SimpleSnailFishNumber = this
    override fun leftMostValue(): SimpleSnailFishNumber = this

    override fun copy(): SnailFishNumberBase = SimpleSnailFishNumber(value)

    override fun magnitude(): ULong {
        return value
    }

    override fun toString(): String {
        return value.toString()
    }
}

private class PairSnailFishNumber(var left: SnailFishNumberBase, var right: SnailFishNumberBase):SnailFishNumberBase(null){
    init{
        left.parent = this
        right.parent = this
        updateSplitNecessity()
        updateDepth()
    }

    fun updateDepth() {
        val oldDepth = depth
        depth = max(left.depth, right.depth) + 1U
        if (oldDepth != depth){
            parent?.updateDepth()
        }
    }

    fun updateSplitNecessity() {
        val necessaryBefore = needsSplit
        needsSplit = left.needsSplit || right.needsSplit
        if (necessaryBefore != needsSplit){
            parent?.updateSplitNecessity()
        }
    }

    override fun explodeLeftMostLowest() {
        if (depth > 2U){
            if (right.depth > left.depth){
                right.explodeLeftMostLowest()
            } else {
                left.explodeLeftMostLowest()
            }
        } else {
            if (left is PairSnailFishNumber){
                explode(left as PairSnailFishNumber)
                left = SimpleSnailFishNumber(0UL)
                left.parent = this
            } else {
                explode(right as PairSnailFishNumber)
                right = SimpleSnailFishNumber(0UL)
                right.parent = this
            }
            updateDepth()
        }
    }

    private fun explode(number: PairSnailFishNumber){
        explodeLeft(number)
        explodeRight(number)
    }

    private fun explodeLeft(number: PairSnailFishNumber){
        val leftNeighbour = number.leftNeighbour()
        val leftNeighbourParent = leftNeighbour?.parent ?: return

        val value = number.left.magnitude()
        val leftNeighbourValue = leftNeighbour.value

        val newNumber = SimpleSnailFishNumber(value + leftNeighbourValue)
        newNumber.parent = leftNeighbourParent

        if (leftNeighbourParent.right == leftNeighbour){
            leftNeighbourParent.right = newNumber
        } else {
            leftNeighbourParent.left = newNumber
        }
        leftNeighbourParent.updateSplitNecessity()
    }

    private fun leftNeighbour(): SimpleSnailFishNumber?{
        return if (this == parent?.right) parent?.left?.rightMostValue() else parent?.leftNeighbour()
    }

    override fun leftMostValue(): SimpleSnailFishNumber {
        return left.leftMostValue()
    }

    override fun copy(): SnailFishNumberBase = PairSnailFishNumber(left.copy(), right.copy())

    private fun explodeRight(number: PairSnailFishNumber){
        val rightNeighbour = number.rightNeighbour()
        val rightNeighbourParent = rightNeighbour?.parent ?: return

        val value = number.right.magnitude()
        val rightNeighbourValue = rightNeighbour.value

        val newNumber = SimpleSnailFishNumber(value + rightNeighbourValue)
        newNumber.parent = rightNeighbourParent

        if (rightNeighbourParent.left == rightNeighbour){
            rightNeighbourParent.left = newNumber
        } else {
            rightNeighbourParent.right = newNumber
        }
        rightNeighbourParent.updateSplitNecessity()
    }

    private fun rightNeighbour(): SimpleSnailFishNumber?{
        return if (this == parent?.left) parent?.right?.leftMostValue() else parent?.rightNeighbour()
    }

    override fun rightMostValue(): SimpleSnailFishNumber {
        return right.rightMostValue()
    }

    override fun splitLeftMostRequiringIt(): SnailFishNumberBase {
        if(!needsSplit){
            return this
        }
        if (left.needsSplit){
            val splitResult = left.splitLeftMostRequiringIt()
            if (left is SimpleSnailFishNumber){
                splitResult.parent = this
                left = splitResult
                updateSplitNecessity()
                updateDepth()
            }
        } else {
            val splitResult = right.splitLeftMostRequiringIt()
            if (right is SimpleSnailFishNumber){
                splitResult.parent = this
                right = splitResult
                updateSplitNecessity()
                updateDepth()
            }
        }
        return this
    }

    override fun magnitude(): ULong {
        return 3UL * left.magnitude() + 2UL * right.magnitude()
    }

    override fun toString(): String {
        return "[$left,$right]"
    }
}