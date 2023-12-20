package com.bangkit.synco.ui.assesment

import com.bangkit.synco.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.synco.MainActivity
import com.bangkit.synco.UserPreferences
import com.bangkit.synco.data.model.Assesment
import com.bangkit.synco.data.model.AssesmentRequest
import com.bangkit.synco.data.repository.ApiConfig
import com.bangkit.synco.databinding.ActivityAssesmentBinding
import com.bangkit.synco.ui.assessment.AssessmentAdapter
import com.shashank.sony.fancytoastlib.FancyToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AssessmentActivity : AppCompatActivity() {
    private lateinit var assessmentAdapter: AssessmentAdapter
    private lateinit var questions: Array<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var usrPref: UserPreferences

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAssesmentBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_assesment)
        usrPref = UserPreferences(this)

        questions = arrayOf(
            "itching",
            "skin_rash",
            "nodal_skin_eruptions",
            "continuous_sneezing",
            "shivering",
            "chills",
            "joint_pain",
            "stomach_pain",
            "acidity",
            "ulcers_on_tongue",
            "muscle_wasting",
            "vomiting",
            "burning_micturition",
            "spotting_urination",
            "fatigue",
            "weight_gain",
            "anxiety",
            "cold_hands_and_feets",
            "mood_swings",
            "weight_loss",
            "restlessnes",
            "lethargy",
            "patches_in_throat",
            "irregular_sugar_level",
            "cough"
        )

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        assessmentAdapter = AssessmentAdapter(questions)
        recyclerView.adapter = assessmentAdapter

        recyclerView.setFocusable(false);
        binding.temp.requestFocus()


        val imageButton = findViewById<View>(R.id.back) as ImageButton
        imageButton.setOnClickListener {
            finish()
            val mainActivity = Intent(this, MainActivity::class.java)
            mainActivity.putExtra("fragmentToLoad", "homeFragment") // You can use this extra to specify which fragment to load in MainActivity
            startActivity(mainActivity)
        }

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {

            val selectedQuestions = assessmentAdapter.getSelectedQuestions()
            doAssessment(selectedQuestions) { assessmentResult ->
                if (assessmentResult != null) {
                    val intent = Intent(this, AssesmentResultActivity::class.java)
                    intent.putStringArrayListExtra("selectedQuestions", ArrayList(selectedQuestions))
                    intent.putExtra("assessmentResult", assessmentResult) // Use the correct key
                    startActivity(intent)
                }
            }
        }
    }

    fun getSelectedQuestions() {
        val selectedQuestions = assessmentAdapter.getSelectedQuestions()
        Log.d("assesmentActivity", "$selectedQuestions")
    }

    private fun doAssessment(selectedQuestions: List<String>, callback: (Assesment?) -> Unit) {
        _isLoading.value = true
        val token = "${usrPref.getLoginData().token}"
        val assessmentRequest = AssesmentRequest(symptoms = selectedQuestions)
        ApiConfig.getApiService().doAssessment(token, assessmentRequest)
            .enqueue(object : Callback<Assesment> {
                override fun onResponse(call: Call<Assesment>, response: Response<Assesment>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val assessmentResult = response.body()
                        callback(assessmentResult)
                    } else {
                        val errorMessage = "Assessment failed: ${response.errorBody()?.string()}"
                        _message.value = errorMessage
                        showMessage(errorMessage)
                    }
                }

                override fun onFailure(call: Call<Assesment>, t: Throwable) {
                    _message.value = "Assessment error: ${t.localizedMessage}"
                    _isLoading.value = false
                    callback(null)
                }
            })
    }

    private fun showMessage(message: String) {
        FancyToast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



}



