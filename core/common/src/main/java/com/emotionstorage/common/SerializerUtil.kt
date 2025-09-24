package com.emotionstorage.common
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalDateSerializer : KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), formatter) // Deserialize from "yyyy-MM-dd"
    }

    override fun serialize(
        encoder: Encoder,
        value: LocalDate
    ) {
        encoder.encodeString(value.format(formatter)) // Serialize to "yyyy-MM-dd"
    }
}

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString())
    }

    override fun serialize(
        encoder: Encoder,
        value: LocalDateTime
    ) {
        encoder.encodeString(value.toString())
    }
}
