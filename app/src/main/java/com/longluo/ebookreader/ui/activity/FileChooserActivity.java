package com.longluo.ebookreader.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.longluo.ebookreader.ui.fragment.DirectoryFragment;
import com.longluo.ebookreader.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FileChooserActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;
    private DirectoryFragment mDirectoryFragment;

    public static final int EXTERNAL_STORAGE_REQ_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filechooser);
        ButterKnife.bind(this);

        toolbar.setTitle("目录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        mDirectoryFragment = new DirectoryFragment();
        mDirectoryFragment.setDelegate(new DirectoryFragment.DocumentSelectActivityDelegate() {

            @Override
            public void startDocumentSelectActivity() {

            }

            @Override
            public void didSelectFiles(DirectoryFragment activity,
                                       ArrayList<String> files) {
                mDirectoryFragment.showReadBox(files.get(0).toString());
            }

            @Override
            public void updateToolBarName(String name) {
                toolbar.setTitle(name);

            }
        });

        fragmentTransaction.add(R.id.fragment_container, mDirectoryFragment, "" + mDirectoryFragment.toString());
        fragmentTransaction.commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission(FileChooserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, EXTERNAL_STORAGE_REQ_CODE, "添加图书需要此权限，请允许");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mDirectoryFragment.onFragmentDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDirectoryFragment.onBackPressed_()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_select_file) {
            Intent intent = new Intent(FileChooserActivity.this, FileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 检查是否拥有权限
     *
     * @param thisActivity
     * @param permission
     * @param requestCode
     * @param errorText
     */
    protected void checkPermission(Activity thisActivity, String permission, int requestCode, String errorText) {
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(thisActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    permission)) {
                Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission},
                        requestCode);
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission},
                        requestCode);
            }
        } else {

        }
    }
}
