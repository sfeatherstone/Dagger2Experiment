package uk.co.wedgetech.dagger2experiment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Toast
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.gson.*
import java.lang.reflect.Type


/*private class HostDeserializer : JsonDeserializer<Host> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Host {

    }
}
*/

class HostAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType as Class<T>
        if (rawType != Host::class.java) {
            //return null
        }

        val delegate = gson.getDelegateAdapter(this, type)

        return object : TypeAdapter<T>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: T?) {
                delegate.write(out, value)
            }

            @Throws(IOException::class)
            override fun read(reader: JsonReader): T? {
                try {
                    return delegate.read(reader)
                } catch (e : Exception) {


                    if (reader.toString().contains("gist-content-wrapper"))
                        throw SASCallGappingExeption("Call gapping")
                    throw e
                }
/*
                if (reader.peek() === JsonToken.NULL) {
                    reader.nextNull()
                    return null
                } else {
                    return lowercaseToConstant[reader.nextString()]
*/
            }
        }
    }
}

class MainActivity : AppCompatActivity() {

    val okHttpClient = OkHttpClient()

    val gson = GsonBuilder()
            .registerTypeAdapterFactory(HostAdapterFactory())
            .create()

    val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/sfeatherstone/8978299568cd6b688a4dafdc630f8bfd/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    val api = retrofit.create(RFService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        buttonGood.setOnClickListener {
            api.loadHosts().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({response -> showToast("Success") },
                            {error -> showToast("Fail")})
        }

        buttonBad.setOnClickListener {
            api.loadError().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({response -> showToast("Success") },
                        {error ->
                            error.message
                            showToast("Fail ${error.message?:""}")})
        }
    }

    fun showToast(message:String) {
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show()
    }
}
