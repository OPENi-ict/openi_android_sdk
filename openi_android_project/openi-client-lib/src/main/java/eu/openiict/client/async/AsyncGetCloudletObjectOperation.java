package eu.openiict.client.async;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import eu.openiict.client.api.ObjectsApi;
import eu.openiict.client.async.models.ICloudletObjectResponse;
import eu.openiict.client.common.ApiException;
import eu.openiict.client.model.OPENiObject;

/**
 * Created by dmccarthy on 15/11/14.
 */
public class AsyncGetCloudletObjectOperation extends AsyncTask<String, Void, Object> {

    String objectId;
    Boolean resolveObject;
    private String auth;
    private String cloudletId;
    private ICloudletObjectResponse iCloudletObjectResponse;

    private ObjectsApi objectsApi;


    public AsyncGetCloudletObjectOperation(ObjectsApi objectsApi, String cloudletId, String objectId, Boolean resolveObject, String auth, ICloudletObjectResponse iCloudletObjectResponse) {
        this.auth = auth;
        this.cloudletId = cloudletId;
        this.objectId = objectId;
        this.resolveObject = resolveObject;

        this.objectsApi = objectsApi;
        this.iCloudletObjectResponse = iCloudletObjectResponse;
    }


    public AsyncGetCloudletObjectOperation(ObjectsApi objectsApi, String objectId, Boolean resolveObject, String auth, ICloudletObjectResponse iCloudletObjectResponse) {
        this.auth = auth;
        this.objectId = objectId;
        this.resolveObject = resolveObject;

        this.objectsApi = objectsApi;
        this.iCloudletObjectResponse = iCloudletObjectResponse;
    }


    @Override
    protected Object doInBackground(String... params) {

        try {
            if (null == cloudletId){
                return objectsApi.getObjectByAuthToken(objectId, resolveObject, auth);
            }
            return objectsApi.getObject(cloudletId, objectId, resolveObject, auth);
        } catch (ApiException e) {
            Log.d("AsyncGetCloudletOper1", e.toString());
            return e;
        }

    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        //Log.d("AsyncGetCloudletObjectOperation", "token " + auth.toString());
        Log.d("AsyncGetCloudletObje2", "" + o);

        if (null == o) {
            iCloudletObjectResponse.onFailure("null response");
        }
        else if( o instanceof ApiException){

            try {
                final JSONObject jo = new JSONObject(((ApiException) o).getMessage());
                if (null != jo.get("error") && jo.get("error").equals("permission denied")){
                    iCloudletObjectResponse.onPermissionDenied();
                }
                else {
                    iCloudletObjectResponse.onFailure(((ApiException) o).getMessage());
                }
            }
            catch (JSONException e){
                iCloudletObjectResponse.onFailure(((ApiException) o).getMessage());
            }
        }
        else {
            iCloudletObjectResponse.onSuccess((OPENiObject) o);
        }
    }
}
