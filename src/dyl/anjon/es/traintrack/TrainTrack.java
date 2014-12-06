package dyl.anjon.es.traintrack;

import java.util.List;

import android.app.Application;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import dyl.anjon.es.traintrack.models.Image;
import dyl.anjon.es.traintrack.models.Journey;
import dyl.anjon.es.traintrack.models.JourneyLeg;
import dyl.anjon.es.traintrack.models.Operator;
import dyl.anjon.es.traintrack.models.Station;
import dyl.anjon.es.traintrack.utils.Utils;

public class TrainTrack extends Application {

	public void onCreate() {
		super.onCreate();
		ParseObject.registerSubclass(Station.class);
		ParseObject.registerSubclass(Operator.class);
		ParseObject.registerSubclass(Journey.class);
		ParseObject.registerSubclass(JourneyLeg.class);
		ParseObject.registerSubclass(Image.class);
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "rvI3JMtQbb3saBrKk5blfeTGdc1uCVs3ueVqwGq2",
				"3uYi6iO1mE1KMfVm6mqXjHq43Ov9iPmi1u9Htk4q");
		ParsePush.subscribeInBackground("broadcast", new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Log.d("com.parse.push",
							"successfully subscribed to the broadcast channel.");
				} else {
					Log.e("com.parse.push", "failed to subscribe for push", e);
				}
			}
		});
		ParseUser.enableAutomaticUser();

		ParseQuery<Operator> query = ParseQuery.getQuery(Operator.class);
		query.fromLocalDatastore();
		try {
			if (query.count() == 0) {
				query = ParseQuery.getQuery(Operator.class);
				query.findInBackground(new FindCallback<Operator>() {
					@Override
					public void done(List<Operator> results, ParseException e) {
						Operator.pinAllInBackground(results);
					}
				});
			}
		} catch (ParseException e) {
			Utils.log(e.getMessage());
		}

	}

}