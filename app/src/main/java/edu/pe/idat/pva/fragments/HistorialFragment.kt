package edu.pe.idat.pva.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.FragmentHistorialBinding
import edu.pe.idat.pva.providers.OrdenProvider
import edu.pe.idat.pva.utils.SharedPref

class HistorialFragment : Fragment() {

    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!

    private lateinit var ordenProvider: OrdenProvider
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistorialBinding.inflate(inflater, container, false)

        ordenProvider = ViewModelProvider(requireActivity())[OrdenProvider::class.java]

        return inflater.inflate(R.layout.fragment_historial, container, false)
    }

}