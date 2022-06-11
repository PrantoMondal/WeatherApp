package com.pmondal.weather

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pmondal.weather.adapter.ForecastAdapter
import com.pmondal.weather.databinding.FragmentWeatherBinding
import com.pmondal.weather.viewmodels.WeatherViewModel


class WeatherFragment : Fragment() {
    private lateinit var binding : FragmentWeatherBinding
    private val weatherViewModel:WeatherViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weather_menu,menu)
        val searchView = menu.findItem(R.id.item_search).actionView as SearchView
        searchView.queryHint = "Search any city"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let {
                    convertQueryToLatLong(p0)

                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
    }

    private fun convertQueryToLatLong(p0: String) {
        val geocoder = Geocoder(requireActivity())
        val addressList : List<Address> = geocoder.getFromLocationName(p0,1)
        if (addressList.isNotEmpty()){
            val lat = addressList[0].latitude
            val lng = addressList[0].longitude
            val location = Location("").apply {
                latitude = lat
                longitude = lng
            }
            weatherViewModel.setNewLocation(location)

        }
        else{
            Toast.makeText(requireActivity(), "Invalid city name", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater,container,false)
        val adapter = ForecastAdapter()
        binding.forecastRV.layoutManager =
            LinearLayoutManager(requireActivity()).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.forecastRV.adapter = adapter

        weatherViewModel.locationLiveData.observe(viewLifecycleOwner){
            weatherViewModel.fetchData()
            //Toast.makeText(requireActivity(), "${it.latitude},${it.longitude}", Toast.LENGTH_SHORT).show()
        }
        weatherViewModel.currentLiveData.observe(viewLifecycleOwner){
            Log.d("WeatherFragment", "${it.main.temp}")
            binding.current = it
        }
        weatherViewModel.forecastLiveData.observe(viewLifecycleOwner){
            Log.d("WeatherFragmentFC", "${it.list.size}")
            adapter.submitList(it.list)
        }
        // Inflate the layout for this fragment
        return binding.root
    }


}