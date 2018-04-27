package uk.co.wedgetech.dagger2experiment

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

class SASCallGappingExeption(message: String): IOException(message) {

}

internal class ErrorResponseBodyConverter<T>() : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        if (value.string().contains("gist-content-wrapper"))
            throw SASCallGappingExeption("Call gapping")
        throw IOException("Error in stream")
    }
}

/*
class ConvertorFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *> {
        return ErrorResponseBodyConverter<*>()
    }

/*    override fun responseBodyConverterZ(type: Type?, annotations: Array<Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type!!))
        return GsonResponseBodyConverter<*>(gson, adapter)
    }*/

}*/