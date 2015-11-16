package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.FBScoresAppWidgetProvider;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

/**
 * Created by Paul Aranas on 11/9/2015.
 */
public class FBScoresWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FBScoresRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class FBScoresRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor cursor;
    private ContentResolver cr;
    private int mAppWidgetId;

    private static final String [] FB_SCORES_COLUMNS = {
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,

    };
    public FBScoresRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        cr = mContext.getContentResolver();

    }

    @Override
    public void onCreate() {

        cursor = cr.query(DatabaseContract.BASE_CONTENT_URI, FB_SCORES_COLUMNS, null, null, null);
    }


    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        cursor.close();
    }

    @Override
    public int getCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        cursor.moveToPosition(i);
        int leagueIndex = cursor.getColumnIndex(DatabaseContract.scores_table.LEAGUE_COL);
        int dateIndex = cursor.getColumnIndex(DatabaseContract.scores_table.DATE_COL);
        int homeIndex = cursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL);
        int awayIndex = cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL);
        int homeGoalsIndex = cursor.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL);
        int awayGoalsIndex = cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL);

        String league = cursor.getString(leagueIndex);
        String date = cursor.getString(dateIndex);
        String home = cursor.getString(homeIndex);
        String away = cursor.getString(awayIndex);
        String homeGoals = cursor.getString(homeGoalsIndex);
        String awayGoals = cursor.getString(awayGoalsIndex);



        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        switch (league) {

            case "394":
                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.league_bundesliga1));

            case "395":
                rv.setTextViewText(R.id.widget_league,mContext.getString(R.string.league_bundesliga2));

            case "396":

                rv.setTextViewText(R.id.widget_league,mContext.getString(R.string.league_ligue1));

            case "397":
                rv.setTextViewText(R.id.widget_league,mContext.getString(R.string.league_ligue2));

            case "398":

                rv.setTextViewText(R.id.widget_league,mContext.getString(R.string.league_premiere_league));

            case "399":

                rv.setTextViewText(R.id.widget_league,mContext.getString(R.string.league_primera_division) );

            case "400":

                rv.setTextViewText(R.id.widget_league,mContext.getString(R.string.league_segunda_division));

            case "401":
                rv.setTextViewText(R.id.widget_league,mContext.getString(R.string.league_serie_A ));

            case "402":
                rv.setTextViewText(R.id.widget_league,mContext.getString(R.string.league_primera_liga) );

            case "403":
                rv.setTextViewText(R.id.widget_league,mContext.getString(R.string.league_bundesliga3));
            case "404":
                rv.setTextViewText(R.id.widget_league,mContext.getString(R.string.league_eredivisie));


        }

        if (homeGoals.equals("-1") && awayGoals.equals("-1")){
            rv.setTextViewText(R.id.widget_text_home, date + "\n" + "No score yet \n" + "@" + home + " vs " );
            rv.setTextViewText(R.id.widget_text_away, away + " " );

        } else {
            rv.setTextViewText(R.id.widget_text_home, date + "\n" + " " + "@" + home + " " + homeGoals);
            rv.setTextViewText(R.id.widget_text_away, away + " " + awayGoals);
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
