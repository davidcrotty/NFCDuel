package nfc;

import interfaces.NFCCompleted;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.json.JSONException;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class NdefReader extends AsyncTask<Tag, Void, String>{

	private final static String TAG = "NDEFREADER";
	private Context context;
	private NFCCompleted backgroundListener;
	
	public NdefReader(Context context,NFCCompleted backgroundListener)
	{
		this.context = context;
		this.backgroundListener = backgroundListener;
		
	}
	
	@Override
	protected String doInBackground(Tag... params) {

		Tag tag = params[0];
        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            // NDEF is not supported by this Tag.
            return null;
        }
        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                    Log.d(TAG, "Unsupported encoding");
                }
            }
        }
        return null;
        
	}
	
	private String readText(NdefRecord record) throws UnsupportedEncodingException {

        byte[] payload = record.getPayload();
        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"
        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }
	
	 @Override
	    protected void onPostExecute(String result) {
	        if (result != null) {
	        	try{
	        	backgroundListener.taskComplete(result);
	        	} catch (NullPointerException n)
	        	{
	        		
	        	} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
	        }
	    }

}
