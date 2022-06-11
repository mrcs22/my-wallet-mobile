package mrcs.mywallet.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api {
    fun config(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://mrcs22-my-wallet.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}

