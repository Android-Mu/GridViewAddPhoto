package com.example.mjj.gridviewaddphoto;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mjj.gridviewaddphoto.adapter.GridViewAdapter;
import com.example.mjj.gridviewaddphoto.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：GridView动态添加图片
 * <p>
 * Created by Mjj on 2016/12/3.
 */
public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private List<Bitmap> data = new ArrayList<Bitmap>();
    private String photoPath;
    private GridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gv_main);
        // 设置默认图片为加号
        Bitmap bp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_addpic);
        data.add(bp);

        adapter = new GridViewAdapter(getApplicationContext(), data);
        gridView.setAdapter(adapter);
        // 设置点击监听事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (data.size() == 10) {
                    Toast.makeText(MainActivity.this, "图片数9张已满", Toast.LENGTH_SHORT).show();
                } else {
                    if (position == data.size() - 1) {
                        Toast.makeText(MainActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                        // 调用系统图库选择图片
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 0x1);
                    } else {
                        Toast.makeText(MainActivity.this, "点击了第" + (position + 1) + "张图片", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    Uri uri = data.getData();
                    // 获取图片路径的数组对象
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.managedQuery(uri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    // 根据索引获取图片路径
                    photoPath = cursor.getString(column_index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(photoPath)) {
            // 将路径转成图片对象
            Bitmap newBp = BitmapUtils.decodeSampledBitmapFromFd(photoPath, 300, 300);
            data.remove(data.size() - 1);
            Bitmap bp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_addpic);
            data.add(newBp);
            data.add(bp);
            //将路径设置为空，防止在手机休眠后返回Activity调用此方法时添加照片
            photoPath = null;
            adapter.notifyDataSetChanged();
        }
    }

}
