package com.pmondal.weather

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.pmondal.weather.databinding.FragmentWeatherBinding
import com.pmondal.weather.viewmodels.WeatherViewModel


class WeatherFragment : Fragment() {
    private lateinit var binding : FragmentWeatherBinding
    private val weatherViewModel:WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater,container,false)
        weatherViewModel.locationLiveData.observe(viewLifecycleOwner){
            weatherViewModel.fetchData()
            //Toast.makeText(requireActivity(), "${it.latitude},${it.longitude}", Toast.LENGTH_SHORT).show()
        }
        weatherViewModel.currentLiveData.observe(viewLifecycleOwner){
            Log.d("WeatherFragment", "${it.main.temp}")
            binding.current = it
        }
        weatherViewModel.forecastLiveData.observe(viewLifecycleOwner){
            Log.d("WeatherFragment", "${it.list.size}")
        }
        // Inflate the layout for this fragment
        return binding.root
    }


}