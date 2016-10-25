package be.ciesites.myminiproject;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.config.Configuration;
import com.omertron.themoviedbapi.model.discover.Discover;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.results.ResultList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static be.ciesites.myminiproject.R.id.recyclerView;


public class AppController extends Application {
    private static AppController instance ;
    private List<MovieBasic> movieList = new ArrayList<>();
    private List<OnMovieListChangedListener> allListeners = new ArrayList<>();
    private TheMovieDbApi api ;
    private Configuration configuration;
  //  private MovieListAdapter movieListAdapter;


    public static synchronized AppController getInstance() {

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        try {
            api = new TheMovieDbApi(getString(R.string.stringKey)); // add your own key here
            FetchConfiguration fetchConfiguration = new FetchConfiguration();
            fetchConfiguration.execute();
            FetchMovieInfo fetchMovieInfo = new FetchMovieInfo();
            fetchMovieInfo.execute();   // type object: asynchtask


        } catch (MovieDbException e){
            e.printStackTrace();
            Log.e("TheMovieDbApi", "Error: " +e.getMessage() );
        }
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.ic_hourglass_empty_black_24dp)
                .showImageOnLoading(R.drawable.ic_hourglass_empty_black_24dp)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();

        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(getApplicationContext())
                        .defaultDisplayImageOptions(defaultOptions)
                        .denyCacheImageMultipleSizesInMemory()
                        .build();

        ImageLoader.getInstance().init(config);

    }


    private class FetchConfiguration extends AsyncTask<Void, Void, Configuration> {
        @Override
        protected  Configuration doInBackground(Void... params){
            try {
                return api.getConfiguration();
            }catch (MovieDbException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Configuration configuration){
            AppController.this.configuration = configuration;


        }


    }
    private class FetchMovieInfo extends AsyncTask<Void, Void, ResultList<MovieBasic>>{

        @Override
        protected ResultList<MovieBasic> doInBackground(Void... params){
            try{
                return api.getDiscoverMovies(new Discover());
            }catch (MovieDbException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResultList<MovieBasic> movieBasicResultList){
            super.onPostExecute(movieBasicResultList);
            movieList.clear();
            movieList.addAll(movieBasicResultList.getResults());
            notifyAllListeners();
        }

    }


    public List<MovieBasic> getMovieList(){
        return movieList;

    }

    private void notifyAllListeners(){
        for(OnMovieListChangedListener listener : allListeners){
            listener.onMovieListChanged();
        }
    }

    public void addOnMovieListChangedListener(OnMovieListChangedListener listener){
        allListeners.add(listener);
    }

    public void removeOnMovieListChangedListener(OnMovieListChangedListener listener){
        allListeners.add(listener);
    }



    public interface OnMovieListChangedListener {
            void onMovieListChanged();
        }

    public URL createImageUrl(String imagePath, String size){
        try {
            return configuration.createImageUrl(imagePath, size);
        } catch (MovieDbException e) {
            e.printStackTrace();
        }
        return null;
    }

}
