package adventOfCode2021.daySolutions



data class BinaryMessage(val binaryContents: String, val startIndex: Int, val endIndex: Int){
    val length: Int
        get() = endIndex - startIndex + 1
}

private fun BinaryMessage.drop(n: Int): BinaryMessage{
    return BinaryMessage(this.binaryContents, this.startIndex + n, this.endIndex)
}

private fun BinaryMessage.restrictLength(maxLength: Int): BinaryMessage{
    val lengthBasedEndIndex = this.startIndex + maxLength - 1
    val newEndIndex = if (lengthBasedEndIndex <= this.endIndex) lengthBasedEndIndex else this.endIndex
    return BinaryMessage(this.binaryContents, this.startIndex, newEndIndex)
}



class Day16 : Day<BinaryMessage, ULong?> {
    override fun parseInput(input: String): BinaryMessage {
        val binaryContents = input
            .map { it.toString().toByte(16).toString(2).padStart(4,'0') }
            .joinToString("")
        val startIndex = 0
        val endIndex = binaryContents.length - 1
        return BinaryMessage(binaryContents, startIndex, endIndex)
    }

    override fun solvePart1(input: BinaryMessage): ULong? {
        val bitsPacket = bitsPacket(input) ?: return null
        return versionSum(bitsPacket)
    }

    private fun versionSum(bitsPacket: BITSPacket): ULong{
        return bitsPacket.version.toULong() + bitsPacket.subPackets.sumOf { versionSum(it) }
    }

    override fun solvePart2(input: BinaryMessage): ULong? {
        val bitsPacket = bitsPacket(input) ?: return null
        return bitsPacket.evaluate()
    }
}



interface BITSPacket{
    val version: Int
    val length: Int
    val typeId: Int
    val subPackets: List<BITSPacket>
}

private const val HEADER_LENGTH = 6
private const val MIN_ENCODING_LENGTH = 4

private fun bitsPacket(binaryMessage: BinaryMessage): BITSPacket?{
    val (version, typeId) = versionAndType(binaryMessage) ?: return null
    val messageBody = binaryMessage.drop(HEADER_LENGTH)
    return when(typeId){
        4 -> literalPacket(messageBody, version)
        else -> operatorPacket(messageBody, version, typeId)
    }
}

private data class LiteralPacket(
    override val version: Int,
    override val length: Int,
    val value: ULong
): BITSPacket{
    override val subPackets: List<BITSPacket> = listOf()
    override val typeId: Int = 4
}

private fun literalPacket(binaryMessage: BinaryMessage, version: Int): LiteralPacket?{
    val (value, valueLength) = literalValue(binaryMessage) ?: return null
    val remainingMessageLength = binaryMessage.length - valueLength
    val length = if (remainingMessageLength < MIN_ENCODING_LENGTH) binaryMessage.length else valueLength
    return LiteralPacket(version, length + HEADER_LENGTH, value)
}

private fun versionAndType(binaryMessage: BinaryMessage): Pair<Int, Int>?{
    if (binaryMessage.length < HEADER_LENGTH) {
        return null
    }
    val version = binaryMessage.binaryContents
        .substring(binaryMessage.startIndex..(binaryMessage.startIndex + 2))
        .toIntOrNull(2) ?: return null
    val typeId = binaryMessage.binaryContents
        .substring((binaryMessage.startIndex + 3)..(binaryMessage.startIndex + 5))
        .toIntOrNull(2) ?: return null
    return  version to typeId
}

private const val VALUE_PART_LENGTH = 5

private fun literalValue(binaryMessage: BinaryMessage): Pair<ULong,Int>?{
    val binaryRepresentationBuilder = StringBuilder()
    var lastPacket: Boolean
    var partsCount = 0
    do{
        lastPacket = when(binaryMessage.binaryContents[binaryMessage.startIndex + VALUE_PART_LENGTH * partsCount]){
            '1' -> false
            '0' -> true
            else -> return null
        }
        val partStartIndex = binaryMessage.startIndex + VALUE_PART_LENGTH * partsCount + 1
        val partEndIndex =  binaryMessage.startIndex + VALUE_PART_LENGTH * (partsCount + 1) - 1
        if (partEndIndex > binaryMessage.endIndex) {
            return null
        }
        val binaryRepresentationPart = binaryMessage.binaryContents
            .substring(partStartIndex..partEndIndex)
        binaryRepresentationBuilder.append(binaryRepresentationPart)
        partsCount++
    } while (!lastPacket)
    val value = binaryRepresentationBuilder.toString().toULongOrNull(2) ?: return null
    val valueLength = VALUE_PART_LENGTH * partsCount
    return value to valueLength
}

private data class OperatorPacket(
    override val version: Int,
    override val length: Int,
    override val typeId: Int,
    override val subPackets: List<BITSPacket>
): BITSPacket


private fun operatorPacket(binaryMessage: BinaryMessage, version: Int, typeId: Int): OperatorPacket? {
    val (subPacketsHeaderLength, subPackets) = subPackets(binaryMessage) ?: return null
    val length = HEADER_LENGTH + subPacketsHeaderLength + subPackets.sumOf { it.length }
    return OperatorPacket(version, length, typeId, subPackets)
}

private fun subPackets(binaryMessage: BinaryMessage): Pair<Int, List<BITSPacket>>? {
    return when(binaryMessage.binaryContents[binaryMessage.startIndex]){
        '0' -> {
            if (binaryMessage.length < 16) {
                return null
            }
            val subPacketsLength = binaryMessage.binaryContents
                .substring((binaryMessage.startIndex + 1)..(binaryMessage.startIndex + 15))
                .toIntOrNull(2) ?: return null
            val subPackets = lengthBasedSubPackets(binaryMessage.drop(16), subPacketsLength) ?: return null
            16 to subPackets
        }
        '1' -> {
            if (binaryMessage.length < 12) {
                return null
            }
            val subPacketCount = binaryMessage.binaryContents
                .substring((binaryMessage.startIndex + 1)..(binaryMessage.startIndex + 11))
                .toIntOrNull(2) ?: return null
            val subPackets = subPacketCountBasedSubPackets(binaryMessage.drop(12), subPacketCount) ?: return null
            12 to subPackets
        }
        else -> null
    }
}

private fun lengthBasedSubPackets(binaryMessage: BinaryMessage, subPacketsLength: Int): List<BITSPacket>?{
    val subPackets = mutableListOf<BITSPacket>()
    var relevantSubMessage = binaryMessage.restrictLength(subPacketsLength)
    while (relevantSubMessage.length > 0){
        val nextSubPacket = bitsPacket(relevantSubMessage) ?: return null
        subPackets.add(nextSubPacket)
        relevantSubMessage = relevantSubMessage.drop(nextSubPacket.length)
    }
    return subPackets
}

private fun subPacketCountBasedSubPackets(binaryMessage: BinaryMessage, subPacketCount: Int): List<BITSPacket>?{
    val subPackets = mutableListOf<BITSPacket>()
    var relevantSubMessage = binaryMessage
    for(PacketIndex in (1..subPacketCount)){
        val nextSubPacket = bitsPacket(relevantSubMessage) ?: return null
        subPackets.add(nextSubPacket)
        relevantSubMessage = relevantSubMessage.drop(nextSubPacket.length)
    }
    return subPackets
}

private fun BITSPacket.evaluate(): ULong?{
    return when(typeId){
        4 -> if (this is LiteralPacket) value else null
        0 -> subPackets.sumOf { it.evaluate() ?: return null }
        1 -> subPackets.fold(1UL) { prod, packet -> prod * (packet.evaluate() ?: return null ) }
        2 -> subPackets.minOf { it.evaluate() ?: return null }
        3 -> subPackets.maxOf { it.evaluate() ?: return null }
        5 -> {
            val firstPacketValue = subPackets.getOrNull(0)?.evaluate() ?: return null
            val secondPacketValue = subPackets.getOrNull(1)?.evaluate() ?: return null
            if (firstPacketValue > secondPacketValue) 1UL else 0UL
        }
        6 -> {
            val firstPacketValue = subPackets.getOrNull(0)?.evaluate() ?: return null
            val secondPacketValue = subPackets.getOrNull(1)?.evaluate() ?: return null
            if (firstPacketValue < secondPacketValue) 1UL else 0UL
        }
        7 -> {
            val firstPacketValue = subPackets.getOrNull(0)?.evaluate() ?: return null
            val secondPacketValue = subPackets.getOrNull(1)?.evaluate() ?: return null
            if (firstPacketValue == secondPacketValue) 1UL else 0UL
        }
        else -> null
    }
}