package com.example.admin.fastestbrowser;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
ProgressBar progressBar;
ImageView imageView;
Button button;
WebView webView;
LinearLayout linearLayout;
SwipeRefreshLayout swipeRefreshLayout;
String mucurrenturl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.myProgressbar);
        imageView = (ImageView)findViewById(R.id.myImageview);
        webView = (WebView)findViewById(R.id.webmine);
        linearLayout = (LinearLayout)findViewById(R.id.linearlayout);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.myswipe);
        progressBar.setMax(100);
        webView.loadUrl("https://www.google.com");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                linearLayout.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);
                linearLayout.setVisibility(View.GONE);
                super.onPageFinished(view, url);
                mucurrenturl=url;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                imageView.setImageBitmap(icon);
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request myrequest = new DownloadManager.Request(Uri.parse(url));
                myrequest.allowScanningByMediaScanner();
                myrequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
                DownloadManager mymanager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                mymanager.enqueue(myrequest);
                Toast.makeText(MainActivity.this,"Your file is downloading",Toast.LENGTH_SHORT);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.super_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuback:
                onBackPressed();
                break;
            case R.id.menuforward:
                onForward();
            case R.id.menurefresh:
                webView.reload();
                break;
            case R.id.menushare:
                Intent myint = new Intent(Intent.ACTION_SEND);
                myint.setType("text/plain");
                myint.putExtra(Intent.EXTRA_TEXT,mucurrenturl);
                myint.putExtra(Intent.EXTRA_SUBJECT,"Copied url");
                startActivity(Intent.createChooser(myint,"Share url with your friends "));

        }
        return super.onOptionsItemSelected(item);
    }

    private void onForward() {
        if (webView.canGoForward()){
            webView.canGoForward();
        }else {
            Toast.makeText(this,"Can't go further!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }
    }
}
