package com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.individual

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.FragmentIndividualTasksBinding
import com.google.rpc.context.AttributeContext.Resource

class IndividualTasksFragment : Fragment() {

    private lateinit var binding:FragmentIndividualTasksBinding
    private var userCode=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentIndividualTasksBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    binding.btnSendCode.setOnClickListener {
        userCode=binding.etAddCode.text.toString()
        updateWebView(userCode)
    }
        binding.wvShowCode.settings.javaScriptEnabled=true
        binding.wvShowCode.webViewClient= WebViewClient()
        updateWebView("")
    }
    private fun updateWebView(userCode: String) {
        val htmlData = getHtmlWithUserCode(userCode)
       binding.wvShowCode.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null)
    }

    private fun getHtmlWithUserCode(userCode: String): String {
        val escapedUserCode = escapeHtml(userCode)
        return "<html><body><pre><code>" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "\t"+escapedUserCode +"\n"+
                "    }\n" +
                "}\n" +
                "</code></pre></body></html>"
    }

    private fun escapeHtml(input: String): String {
        return input.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }

}