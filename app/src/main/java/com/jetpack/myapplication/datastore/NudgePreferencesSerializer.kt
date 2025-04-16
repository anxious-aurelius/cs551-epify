package com.jetpack.myapplication.datastore

import androidx.datastore.core.Serializer
import com.jetpack.myapplication.NudgePreferencesProto
import java.io.InputStream
import java.io.OutputStream

object NudgePreferencesSerializer : Serializer<NudgePreferencesProto> {
    override val defaultValue: NudgePreferencesProto = NudgePreferencesProto.newBuilder()
        .setFrequency(NudgePreferencesProto.Frequency.DAILY)
        .setHour(9)
        .setMinute(0)
        .build()

    override suspend fun readFrom(input: InputStream): NudgePreferencesProto {
        return NudgePreferencesProto.parseFrom(input)
    }

    override suspend fun writeTo(t: NudgePreferencesProto, output: OutputStream) {
        t.writeTo(output)
    }
}
