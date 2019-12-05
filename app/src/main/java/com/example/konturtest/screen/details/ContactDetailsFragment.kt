package com.example.konturtest.screen.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.konturtest.databinding.FragmentContactDetailsBinding

/**
 * Created by Vladimir Kraev
 */
class ContactDetailsFragment : Fragment() {

    private val args: ContactDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentContactDetailsBinding
    private lateinit var viewModel: ContactDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        viewModel = ViewModelProviders.of(this).get(ContactDetailsViewModel::class.java)
        binding = FragmentContactDetailsBinding.inflate(layoutInflater, container, false).also {
            it.viewModel = viewModel
        }
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        viewModel.start(args.contactId)
    }

}