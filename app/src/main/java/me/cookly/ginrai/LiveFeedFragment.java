package me.cookly.ginrai;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LiveFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LiveFeedFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public LiveFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_feed, container, false);

        return view;
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

    public static class TripCardRecyclerViewAdapter
            extends RecyclerView.Adapter<TripCardRecyclerViewAdapter.ViewHolder> {

        private List<JSONObject> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTextView = (TextView) view.findViewById(R.id.textViewFeedTitle);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public JSONObject getValueAt(int position) {
            return mValues.get(position);
        }

        public TripCardRecyclerViewAdapter(List<JSONObject> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.feed_card, parent, false);
            return new LiveFeedFragment.TripCardRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final JSONObject jsonObject = mValues.get(position);
            holder.mTextView.setText(jsonObject.optString("bookingID"));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("view clicked");
                    //System.out.println(jsonObject.optString("bookingID"));

                    Context context = v.getContext();
                    //Intent intent = new Intent(context, TripDetailsActivity.class);
                    //intent.putExtra(TripDetailsActivity.jsonStringExtra, jsonObject.toString());
                    //context.startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
