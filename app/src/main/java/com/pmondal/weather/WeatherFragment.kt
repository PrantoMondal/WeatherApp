package com.pmondal.weather

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
        inflater.inflate(R.menu.weather_menu, menu)
        val searchView = menu.findItem(R.id.item_search).actionView as SearchView
        searchView.queryHint = "Search any City"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    convertQueryToLatLng(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item_location){
            getLocation(requireContext()){
                weatherViewModel.setNewLocation(it)
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun convertQueryToLatLng(query: String) {
        val geocoder = Geocoder(requireActivity())
        val addressList: List<Address> = geocoder.getFromLocationName(query, 1)
        if (addressList.isNotEmpty()) {
            val lat = addressList[0].latitude
            val lng = addressList[0].longitude
            val location = Location("").apply {
                latitude = lat
                longitude = lng
            }
            weatherViewModel.setNewLocation(location)
        }else {
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