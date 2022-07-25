package com.contactdemo.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.contactdemo.R;
import com.contactdemo.api.ApiList;
import com.contactdemo.api.RestClient;
import com.contactdemo.helper.ToastHelper;
import com.contactdemo.interfaces.ClickEvent;
import com.contactdemo.model.ContactModel;
import com.contactdemo.permissionUtils.PermissionClass;
import com.contactdemo.utils.Constants;
import com.contactdemo.utils.GenericView;
import com.contactdemo.utils.Utils;
import com.contactdemo.validator.ValidationClass;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import okhttp3.internal.Util;

public class AddRecordActivity extends AppCompatActivity implements ClickEvent {
    // xml component
    private RelativeLayout relParent;
    private TextView txtHeader;
    private EditText edtFirstName, edtLastName, edtEmail, edtPhone;
    private Snackbar snackbar;
    private Button btnRegOrUpdate;
    private ImageView ivDp;

    // variable declaration
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_GALLERY = 2;
    private String selectedImagePath;
    Activity activityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        init();
    }

    public void init() {
        relParent = GenericView.findViewById(this, R.id.activity_add_record);
        txtHeader = GenericView.findViewById(this, R.id.txt_header);
        ivDp = GenericView.findViewById(this, R.id.iv_dp);
        edtFirstName = GenericView.findViewById(this, R.id.edt_firstName);
        edtLastName = GenericView.findViewById(this, R.id.edt_lastName);
        edtEmail = GenericView.findViewById(this, R.id.edt_email);
        edtPhone = GenericView.findViewById(this, R.id.edt_phone);
        btnRegOrUpdate = GenericView.findViewById(this, R.id.btn_registerOrUpdate);
        Utils.setupOutSideTouchHideKeyboard(relParent);
    }


    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_dp:
                selectImage();
                break;
            case R.id.btn_registerOrUpdate:
                validateFunction();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK && data != null) {
                    onCaptureImageResult(data);
                }

                break;

            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    onSelectFromGalleryResult(data);
                }

                break;

            case PermissionClass.REQUEST_CODE_PERMISSION_SETTING:
                if (resultCode == RESULT_OK && data != null) {
                    ToastHelper.getInstance(this).displayCustomToast(getString(R.string.errorMsgToDo));
                }
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_CAMERA:
                if (grantResults.length > 0) {
                    if (PermissionClass.verifyPermission(grantResults)) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    } else if (((!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                            !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)))) {
                        showCustomSnackBar();
                    }
                }
                break;

            case PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE:
                if (grantResults.length > 0) {
                    if (PermissionClass.verifyPermission(grantResults)) {

                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType(Constants.GALLERY_FILE_TYPE);
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.strSelectFile)), REQUEST_GALLERY);

                    } else if (((!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                            !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)))) {
                        showCustomSnackBar();
                    }
                }
                break;

        }
    }

    /**
     * It show the snackBar in footer when user check "Never ask again" option in
     * runtime permission access. On "Ok" option click it open setting bar where user can manually
     * allows the permission
     */
    public void showCustomSnackBar() {
        snackbar = Snackbar.make(relParent, getString(R.string.strPermissionRequired), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.strOk), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PermissionClass.openSettingBar(activityContext);
                    }
                });
        snackbar.show();
    }

    /**
     * This method check validation of required view component
     *
     * @return (boolean) : return either true or false, if all validation succeed than it return true
     */
    private boolean validateFunction() {
        if (ValidationClass.isEmpty(edtFirstName.getText().toString().trim())) {
            displayErrorAndRequestFocus(edtFirstName, getString(R.string.errorMsgFirstName));
            return false;
        } else if (!ValidationClass.matchPattern(edtEmail.getText().toString().trim(), Patterns.EMAIL_ADDRESS.pattern())) {
            displayErrorAndRequestFocus(edtEmail, getString(R.string.errorMsgValidEmailAddress));
            return false;
        } else if (ValidationClass.isEmpty(edtPhone.getText().toString().trim())) {
            displayErrorAndRequestFocus(edtPhone, getString(R.string.errorMsgPhoneNo));
            return false;
        } else if (!ValidationClass.checkPhoneNumber(edtPhone.getText().toString().trim(), Constants.PHONE_NUMBER_LENGTH)) {
            displayErrorAndRequestFocus(edtPhone, getString(R.string.errorMsgValidPhoneNo));
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method display required validation error message and focus cursor on editText.
     *
     * @param editText     (EditText)   : EditText object
     * @param errorMessage (String) : errorMessage to be display
     */
    private void displayErrorAndRequestFocus(EditText editText, String errorMessage) {
        editText.requestFocus();
        ToastHelper.getInstance(this).displayCustomToast(errorMessage);
    }

    /**
     * it show popup with option to select an image
     */
    public void selectImage() {
        final CharSequence[] items = {getString(R.string.strTakePhoto), getString(R.string.strOpenGallery), getString(R.string.strCancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRecordActivity.this);
        builder.setTitle(getString(R.string.strDialogTitle));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.strTakePhoto))) {
                    if (PermissionClass.checkPermission(activityContext, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_CAMERA,
                            Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);

                    }

                    //remaining process
                } else if (items[item].equals(getString(R.string.strOpenGallery))) {
                    if (PermissionClass.checkPermission(activityContext, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE,
                            Arrays.asList(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {

                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType(Constants.GALLERY_FILE_TYPE);
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.strSelectFile)), REQUEST_GALLERY);

                    }

                } else if (items[item].equals(getString(R.string.strCancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * To manage camera image
     *
     * @param data (Intent) : data to be comes in {@link #onActivityResult(int, int, Intent)}
     */
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get(Constants.DATA);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                //currentTimeMillis is create the new image name
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Uri camimagepath = Uri.parse(destination.getAbsolutePath());
        // store camera image path into global variable "selectedImagePath"
        selectedImagePath = camimagepath.toString();

        Log.d("cameraImgPath", selectedImagePath);

        Picasso.get()
                .load(new File(selectedImagePath))
                .placeholder(R.drawable.ic_missing_profile_dp)
                .into(ivDp);
    }

    /**
     * To manage data that selected from External Storage
     *
     * @param data (Intent) : data to be comes in {@link #onActivityResult(int, int, Intent)}
     */
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImage = data.getData();
        String[] path = {MediaStore.Images.Media.DATA};

        Cursor c = getContentResolver().query(selectedImage, path, null, null, null);
        c.moveToFirst();
        int columnindex = c.getColumnIndex(path[0]);
        String imagePath = c.getString(columnindex);
        c.close();
        Bitmap thumbnail = (BitmapFactory.decodeFile(imagePath));
        // store camera image path into global variable "selectedImagePath"
        selectedImagePath = imagePath;

        Picasso.get()
                .load(new File(selectedImagePath))
                .placeholder(R.drawable.ic_missing_profile_dp)
                .into(ivDp);

        Log.d("galleryImagePath", imagePath);

    }


    public void postStringRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiList.SERVER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("strResponse", response);



                // Log.d("listSize", String.valueOf(blogList.get(1).getTitle()));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "";
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    errorMessage = getString(R.string.internet_not_available);
                } else {
                    errorMessage = getString(R.string.error_msg_server);
                }
                Toast.makeText(AddRecordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void getDataFromServer(boolean isDialogRequired){
       // RestClient.getInstance().post(getApplicationContext(),Request.Method.POST,);
    }

}