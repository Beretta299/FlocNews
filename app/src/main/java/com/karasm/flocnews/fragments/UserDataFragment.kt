package com.karasm.flocnews.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.adapters.CitySpinnerAdapter
import com.karasm.flocnews.adapters.CountrySpjnnerAdapter
import com.karasm.flocnews.models.CityModel
import com.karasm.flocnews.models.CountryModel
import com.karasm.flocnews.models.UserModel
import com.karasm.flocnews.viewmodels.UserDataViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.sources_screen.view.*
import kotlinx.android.synthetic.main.user_data_fragment.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class UserDataFragment:Fragment(R.layout.user_data_fragment),AdapterView.OnItemSelectedListener,DatePickerDialog.OnDateSetListener,
    View.OnClickListener,TextWatcher {



    companion object{
        fun newInstance(isRegistration:Boolean):UserDataFragment{
            val bundle=Bundle()
            bundle.putBoolean(UserDataFragment::class.java.simpleName,isRegistration)
            val fragment=UserDataFragment()
            fragment.arguments=bundle
            return fragment
        }
    }


    lateinit var countrySpinner:Spinner
    lateinit var citySpinner:Spinner
    lateinit var firstName:TextInputEditText
    lateinit var lastName:TextInputEditText
    lateinit var phone:TextInputEditText
    lateinit var oldPassField:TextInputEditText
    lateinit var newPassField:TextInputEditText
    lateinit var newPassRepField:TextInputEditText
    lateinit var oldPassLayout:TextInputLayout
    lateinit var newPassLayout:TextInputLayout
    lateinit var newPassRepLayout:TextInputLayout
    lateinit var confirmPassChange:Button

    lateinit var passwordRestoration:Group
    lateinit var registrationConfirm:Button
    var countryList:List<CountryModel>?=null
    var cityList:List<CityModel>?=null

    lateinit var adapter:CountrySpjnnerAdapter
    lateinit var cityAdapter:CitySpinnerAdapter
    lateinit var datePicker:DatePickerDialog
    lateinit var dateOfBirthField:TextView
    var isRegistration=false
    var passText=""
    var passRepeatText=""
    lateinit var passLayouts:ArrayList<TextInputLayout>
    lateinit var passFields:ArrayList<TextInputEditText>

    private lateinit var mViewModel: UserDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel=ViewModelProviders.of(this).get(UserDataViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isRegistration=arguments!!.getBoolean(UserDataFragment::class.java.simpleName)
        initViews(view)
        initListeners()
        getCountries()
        isUserExists()
        isUserRegistration(isRegistration)
        setCurrentDate()
        observePassValue()
    }

    private fun observePassValue() {
        mViewModel.isPassCorrectObserve().observe(this, Observer {
            if(!it){
                oldPassLayout.error=getString(R.string.wrong_password)
            }else{
                Toast.makeText(context,"Congratulations! Password changed",Toast.LENGTH_LONG).show()
            }
        })
    }

    fun setCurrentDate(){
        val calendar=Calendar.getInstance()
        dateOfBirthField.text=SimpleDateFormat(getString(R.string.date_pattern)).format(calendar.time)
    }

    private fun initViews(view:View){
        countrySpinner=view.findViewById(R.id.countrySpinner)
        citySpinner=view.findViewById(R.id.citySpinner)
        dateOfBirthField=view.findViewById(R.id.dateOfBirth)
        firstName=view.findViewById(R.id.textInputName)
        lastName=view.findViewById(R.id.textInputLastName)
        phone=view.findViewById(R.id.textInputPhone)
        registrationConfirm=view.findViewById(R.id.confirmRegistration)
        passwordRestoration=view.findViewById(R.id.groupData)
        oldPassLayout=view.findViewById(R.id.textInputOldPassLayout)
        newPassLayout=view.findViewById(R.id.textInputNewPassLayout)
        newPassRepLayout=view.findViewById(R.id.textInputRepeatNewPassLayout)
        oldPassField=view.findViewById(R.id.textInputOldPass)
        newPassField=view.findViewById(R.id.textInputNewPass)
        newPassRepField=view.findViewById(R.id.textInputRepeatNewPass)
        confirmPassChange=view.findViewById(R.id.confirmPassChange)
        passLayouts= ArrayList()
        passFields= ArrayList()
        passLayouts.add(oldPassLayout)
        passLayouts.add(newPassLayout)
        passLayouts.add(newPassRepLayout)
        passFields.add(oldPassField)
        passFields.add(newPassField)
        passFields.add(newPassRepField)
    }

    fun initListeners(){
        countrySpinner.onItemSelectedListener = this
        citySpinner.onItemSelectedListener=this
        dateOfBirthField.setOnClickListener(this)
        confirmRegistration.setOnClickListener(this)
        confirmPassChange.setOnClickListener(this)
        oldPassField.addTextChangedListener(this)
        newPassField.addTextChangedListener(this)
        newPassRepField.addTextChangedListener(this)
    }


    private fun setUpDatePickerDialog(){
        val currentDate=SimpleDateFormat(getString(R.string.date_pattern))
        val calendar=Calendar.getInstance()
        calendar.time=currentDate.parse(dateOfBirthField.text.toString())!!
        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)
        val day=calendar.get(Calendar.DAY_OF_MONTH)
        datePicker=DatePickerDialog(context!!,this,year,month,day)
        datePicker.show()
    }

    fun isUserExists(){
        mViewModel.isUserExists().observe(this, Observer {
            exists->
            if(exists){
                getUserData()
            }
        })
    }

    fun getUserData(){
        mViewModel.getUserData().observe(this, Observer {
            model->
            setUserData(model!!)
        })
    }

    fun isUserRegistration(state:Boolean){
        if(state){
            passwordRestoration.visibility=View.GONE
        }else{
            passwordRestoration.visibility=View.VISIBLE
        }
    }

    private fun setUserData(model: UserModel) {
        firstName.text=Editable.Factory.getInstance().newEditable(model.firstName)
        lastName.text=Editable.Factory.getInstance().newEditable(model.lastName)
        phone.text=Editable.Factory.getInstance().newEditable(model.phoneNumber)
        dateOfBirthField.text=model.birthDate
        setNeededCountry(model.countryId)
        Single.timer(500,TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setNeededCity(model.cityId)
            },{})
    }

    private fun setNeededCountry(countryId: String) {
        for((i,country) in countryList!!.withIndex()){
           if(country.keyValue==countryId){
               countrySpinner.setSelection(i)
               break
           }
        }
    }

    private fun setNeededCity(cityId:String){
        for((i,city) in cityList!!.withIndex()){
            if(city.keyValue==cityId){
                //Log.d(UtilsClass.RESULT_TAG,city.keyValue +" "+cityId)
                citySpinner.setSelection(i)
                break
            }
        }
    }

    fun isNoError():Boolean{
        var boolean=true
        for(layout in passLayouts){
            if(layout.error!=null){
                boolean=false
                break
            }
        }
        for((i,field) in passFields.withIndex()){
            if(field.text.toString()==""){
                passLayouts[i].error=getString(R.string.empty_field)
                boolean=false
            }
        }

        return boolean
    }

    private fun getCountries(){
        mViewModel.getCountries().observe(this, Observer{
            countryList=it
            setUpCountrySpinner(it)
        })
    }

    private fun setUpCountrySpinner(list:List<CountryModel>){
        adapter=CountrySpjnnerAdapter(context!!,list)
        countrySpinner.adapter=adapter
    }

    fun getCities(position: Int){
        val cModel:CountryModel=adapter.getItem(position) as CountryModel
        mViewModel.getCities(cModel.keyValue).observe(this, Observer {
            cityList=it
            cityAdapter= CitySpinnerAdapter(context!!,cityList!!.toList())
            citySpinner.adapter=cityAdapter
        })
    }



    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        when(p0!!.id){
            countrySpinner.id->{
                getCities(position)
            }
        }

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val calendar=Calendar.getInstance()
        calendar.set(Calendar.YEAR,p1)
        calendar.set(Calendar.MONTH,p2)
        calendar.set(Calendar.DAY_OF_MONTH,p3)
        val simpleDateFormat=SimpleDateFormat(getString(R.string.date_pattern))

        dateOfBirthField.text=simpleDateFormat.format(calendar.time)
    }

    private fun registerUser(){
        val user=UserModel(firstName.text.toString(),
            lastName.text.toString(),
            phone.text.toString(),
            dateOfBirthField.text.toString(),
            cityList!![citySpinner.selectedItemPosition].keyValue,
            countryList!![countrySpinner.selectedItemPosition].keyValue)

        mViewModel.saveUserData(user).observe(this, Observer {
            if(isRegistration){
            fragmentManager!!.beginTransaction()
                .replace(R.id.fragment_container,NewsFragment.newInstance())
                .commit()
            }
        })
    }

    fun changePassword(){
        mViewModel.changePassword(oldPassField.text.toString(),newPassField.text.toString())
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            dateOfBirthField.id->{
                setUpDatePickerDialog()
            }
            registrationConfirm.id->{
                registerUser()
            }
            confirmPassChange.id->{
                if(isNoError()){
                    changePassword()
                }
            }
        }
    }


    override fun afterTextChanged(p0: Editable?) {
        if(passText!=passRepeatText){
            newPassRepLayout.error=getString(R.string.pass_not_match)
        }else{
            newPassRepLayout.error=""
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        passText=newPassField.text!!.trim().toString()
        passRepeatText=newPassRepField.text!!.trim().toString()
        when(p0.hashCode())
        {
            oldPassField.text.hashCode()->{
                if(!oldPassField.text!!.matches(Regex(UtilsClass.PASSWORD_REGEX))){
                    oldPassLayout.error=getString(R.string.pass_error)
                }else{
                    oldPassLayout.error=""
                }
            }
        }
    }
}