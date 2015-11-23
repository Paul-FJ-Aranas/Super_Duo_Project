package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;
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

    private static final String[] FB_SCORES_COLUMNS = {
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
                break;

            case "395":
                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.league_bundesliga2));
                break;

            case "396":

                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.league_ligue1));
                break;

            case "397":
                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.league_ligue2));
                break;

            case "398":

                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.league_premiere_league));
                break;

            case "399":

                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.league_primera_division));
                break;

            case "400":

                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.league_segunda_division));
                break;

            case "401":
                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.league_serie_A));
                break;

            case "402":
                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.league_primera_liga));
                break;

            case "403":
                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.league_bundesliga3));
                break;

            case "404":
                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.league_eredivisie));
                break;
            default:
                rv.setTextViewText(R.id.widget_league, mContext.getString(R.string.no_league_info));


        }

        if (homeGoals.equals("-1") && awayGoals.equals("-1")) {
            rv.setImageViewResource(R.id.widget_image_football, R.drawable.football);
            rv.setTextViewText(R.id.widget_text_home, date + "\n" + mContext.getString(R.string.no_score_yet) + " \n" + "@" + home + " " + mContext.getString(R.string.versus));
            rv.setTextViewText(R.id.widget_text_away, away + " ");

        } else {
            rv.setImageViewResource(R.id.widget_image_football, R.drawable.football);
            rv.setTextViewText(R.id.widget_text_home, date + "\n" + " " + "@" + home + " " + homeGoals);
            rv.setTextViewText(R.id.widget_text_away, away + " " + awayGoals);
        }


        Intent fillInIntent = new Intent();
        rv.setOnClickFillInIntent(R.id.widget_image_football, fillInIntent);

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
