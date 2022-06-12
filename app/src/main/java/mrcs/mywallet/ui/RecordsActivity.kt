package mrcs.mywallet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mrcs.mywallet.databinding.ActivityRecordsBinding

class RecordsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecordsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecordsBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}