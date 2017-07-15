package com.benoitquenaudon.tvfoot.red.testutil

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import timber.log.Timber
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Type
import javax.inject.Inject

class Fixture @Inject constructor(private val gson: Gson) {

  private val listMatchType =
      TypeToken.getParameterized(List::class.java, Match::class.java).type

  fun anyMatches(): List<Match> {
    return anyObject("matches_sample.json", listMatchType)
  }

  fun anyMatchesShort(): List<Match> {
    return anyObject("matches_sample_short.json", listMatchType)
  }

  fun anyMatch(): Match {
    return anyObject("match_sample.json", Match::class.java)
  }

  fun anyNextMatches(): List<Match> {
    return anyObject("matches_next_sample.json", listMatchType)
  }

  private fun <T> anyObject(filename: String, typeOfT: Type): T {
    val inputStream = this.javaClass.classLoader.getResourceAsStream(filename)

    try {
      val reader = JsonReader(InputStreamReader(inputStream, "UTF-8"))
      val result = gson.fromJson<T>(reader, typeOfT)

      reader.close()

      return result
    } catch (e: IOException) {
      Timber.e(e)
      throw RuntimeException("fixture $filename failed")
    }
  }
}
