package me.cookly.ginrai;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ShareFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView previewImage;
    private EditText textfieldPlace;
    private DatabaseReference mDatabase;
    static final int REQUEST_IMAGE_CAPTURE = 111;
    public Uri imgUri;

    private Bitmap imgBitmap;
    private Place place = null;

    static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 11234;
    static final int CROP_PIC = 2133;

    public ShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        View view = inflater.inflate(R.layout.fragment_share, container, false);

        textfieldPlace = (EditText) view.findViewById(R.id.textfieldPlace);
        textfieldPlace.setInputType(InputType.TYPE_NULL);
        textfieldPlace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(view.getId() == R.id.textfieldPlace && b) {
                    try {
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setBoundsBias(new LatLngBounds(new LatLng(12, 100), new LatLng(14, 102)))
                                        .build(getActivity());
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                    }
                }
            }
        });
        textfieldPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("place auto complete");
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setBoundsBias(new LatLngBounds(new LatLng(12,100),new LatLng(14,102)))
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        previewImage = (ImageView) view.findViewById(R.id.previewImage);
        final EditText textfieldDish = (EditText) view.findViewById(R.id.textfieldDish);
        Button postButton = (Button) view.findViewById(R.id.postButton);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://ginrai-9a3fb.appspot.com");
                final String filename = "feed-image/" + UUID.randomUUID().toString() + ".jpg";
                StorageReference mountainImagesRef = storageRef.child(filename);


                Bitmap bitmap = imgBitmap;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();


                UploadTask uploadTask = mountainImagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        System.out.println(exception);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        System.out.println(downloadUrl);

                        String uuid = UUID.randomUUID().toString();
                        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
//                            ActivityCompat.requestPermissions(new String[]{Manifest.permission
//                                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
//                            }, 10);
                            return;
                        }
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        System.out.println(latitude);
                        System.out.println(longitude);
                        double time = (double)System.currentTimeMillis();
                        mDatabase.child("feed").child(uuid).setValue(new FeedItem(textfieldDish.getText().toString(),
                                "gs://ginrai-9a3fb.appspot.com/"+filename,
                                place.getLatLng().latitude,
                                place.getLatLng().longitude,
                                time,
                                place.getName().toString()));
                        ((NavigationBarActivity)getActivity()).startLiveFeedFragment(true);

                    }
                });
            }
        });

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
                imgUri = data.getData();
                //System.out.println(imgUri.toString());
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imgUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int x = (int)Math.min(bitmap.getHeight(),bitmap.getWidth());
                int offsetY = (bitmap.getHeight() - x) / 2;
                Matrix matrix = new Matrix();
                matrix.preRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, offsetY, x, x, matrix, true);
                imgBitmap = bitmap;

                previewImage.setImageBitmap(imgBitmap);
            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format("Place: %s", place.getName());
                System.out.println(toastMsg);
                this.place = place;
                textfieldPlace.setText(place.getName());
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
