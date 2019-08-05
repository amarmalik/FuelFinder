package com.amazingtech.petrolfinder;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by mayankthakur on 7/1/2017.
 */
public class Multipart_Request extends Request<String> {

    private MultipartEntity entity = new MultipartEntity();

    private static final String ImageTag = "add_image";

    private final Response.Listener<String> mListener;
    private final File file;
    private final HashMap<String, String> params;

    public Multipart_Request(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, File file, HashMap<String, String> params) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        this.file = file;
        this.params = params;
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {

if(file!=null)
{
    entity.addPart("add_image", new FileBody(file));
}

        try {
            for (String key : params.keySet()) {
                entity.addPart(key, new StringBody(params.get(key)));
            }
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    /**
     * copied from Android StringRequest class
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}

