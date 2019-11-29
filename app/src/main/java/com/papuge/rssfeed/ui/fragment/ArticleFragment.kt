package com.papuge.rssfeed.ui.fragment


import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.navigation.fragment.navArgs

import com.papuge.rssfeed.R


class ArticleFragment : Fragment() {

    private val args: ArticleFragmentArgs by navArgs()
    private lateinit var webView: WebView
    private val TAG = "ArticleFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.article_webview)
        webView.settings.loadWithOverviewMode = true

        webView.settings.javaScriptEnabled = true
        webView.isHorizontalScrollBarEnabled = false
        webView.webChromeClient = WebChromeClient()
        webView.loadDataWithBaseURL(null, "<style>img{display: inline; height: auto; max-width: 100%;} " +
                "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n"
                + args.article.content, null, "utf-8", null)
    }


}
