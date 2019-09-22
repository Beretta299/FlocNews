package com.karasm.flocnews.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.karasm.flocnews.R
import com.karasm.flocnews.Utils.UtilsClass
import com.karasm.flocnews.adapters.CitySpinnerAdapter
import com.karasm.flocnews.adapters.CountrySpjnnerAdapter
import com.karasm.flocnews.models.CityModel
import com.karasm.flocnews.models.CountryModel
import com.karasm.flocnews.models.UserModel
import com.karasm.flocnews.viewmodels.UserDataViewModel
import kotlinx.android.synthetic.main.sources_screen.view.*
import kotlinx.android.synthetic.main.user_data_fragment.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserDataFragment:Fragment(R.layout.user_data_fragment),AdapterView.OnItemSelectedListener,DatePickerDialog.OnDateSetListener,
    View.OnClickListener {



    companion object{
        fun newInstance():UserDataFragment{
            return UserDataFragment()
        }
    }


    lateinit var countrySpinner:Spinner
    lateinit var citySpinner:Spinner
    lateinit var firstName:TextInputEditText
    lateinit var lastName:TextInputEditText
    lateinit var phone:TextInputEditText
    lateinit var registrationConfirm:Button
    lateinit var countryList:List<CountryModel>
    lateinit var cityList:List<CityModel>

    lateinit var adapter:CountrySpjnnerAdapter
    lateinit var cityAdapter:CitySpinnerAdapter
    lateinit var datePicker:DatePickerDialog
    lateinit var dateOfBirthField:TextView

    private lateinit var mViewModel: UserDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel=ViewModelProviders.of(this).get(UserDataViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        getCountries()
    }
    private fun initViews(view:View){
        countrySpinner=view.findViewById(R.id.countrySpinner)
        citySpinner=view.findViewById(R.id.citySpinner)
        dateOfBirthField=view.findViewById(R.id.dateOfBirth)
        firstName=view.findViewById(R.id.textInputName)
        lastName=view.findViewById(R.id.textInputLastName)
        phone=view.findViewById(R.id.textInputPhone)
        registrationConfirm=view.findViewById(R.id.confirmRegistration)
    }

    fun initListeners(){
        countrySpinner.onItemSelectedListener = this
        citySpinner.onItemSelectedListener=this
        dateOfBirthField.setOnClickListener(this)
        confirmRegistration.setOnClickListener(this)
    }

    fun setUpDatePickerDialog(){
        val calendar:Calendar=Calendar.getInstance()
        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)
        val day=calendar.get(Calendar.DAY_OF_MONTH)
        datePicker=DatePickerDialog(context!!,this,year,month,day)
        datePicker.show()
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
            cityAdapter= CitySpinnerAdapter(context!!,it)
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
        val simpleDateFormat=SimpleDateFormat("dd-MMM-yyyy")

        dateOfBirthField.text=simpleDateFormat.format(calendar.time)
    }

    private fun registerUser(){
        val user=UserModel(firstName.text.toString(),lastName.text.toString(),phone.text.toString(),dateOfBirthField.text.toString(),countryList[countrySpinner.selectedItemPosition].keyValue,cityList[citySpinner.selectedItemPosition].keyValue)
        mViewModel.saveUserData(user).observe(this, Observer {
            fragmentManager!!.beginTransaction()
                .replace(R.id.fragment_container,NewsFragment.newInstance())
                .commit()
        })
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            dateOfBirthField.id->{
                setUpDatePickerDialog()
            }
            registrationConfirm.id->{
                registerUser()
            }
        }
    }
}