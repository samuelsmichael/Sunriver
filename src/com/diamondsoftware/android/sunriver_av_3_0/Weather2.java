package com.diamondsoftware.android.sunriver_av_3_0;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Creates a page of weather information, fetching the data from wunderground.
 * @author Diamond
 *
 */
public class Weather2 extends Activity {

	public static char degree = '\u00B0';
	public enum QueryType {
		Conditions,
		Hourly,
		Forecast,
		Forceast10Days
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather2);
		Object[] parms=new Object[3];
		parms[0]=getResources().getString(R.string.wunderground_api_conditions);
		parms[1]=this;
		parms[2]=QueryType.Conditions;
		new RetrieveWeatherData().execute(parms);
		Object[] parmsHourly=new Object[3];
		parmsHourly[0]=getResources().getString(R.string.wunderground_api_hourly);
		parmsHourly[1]=this;
		parmsHourly[2]=QueryType.Hourly;
		new RetrieveWeatherData().execute(parmsHourly);
		Object[] parmsForecast=new Object[3];
		parmsForecast[0]=getResources().getString(R.string.wunderground_api_forcast);
		parmsForecast[1]=this;
		parmsForecast[2]=QueryType.Forecast;
		new RetrieveWeatherData().execute(parmsForecast);	}


	public class RetrieveWeatherData extends AsyncTask<Object, Void, String> {
		QueryType mQueryType;
		String mUrl;
		Weather2 mWeather;
		protected String doInBackground(Object... parms) {
			String url="";
			try {
				mUrl =(String)parms[0];
				mWeather=(Weather2)parms[1];
				mQueryType=(QueryType)parms[2];
				return new JsonReader().getRemoteData(mUrl); 
			} catch (Exception ee) {
				Toast.makeText(getApplicationContext(), "Failed trying to fetch data from "+url+" with reason: "+ee.getMessage(), Toast.LENGTH_LONG).show();
				return null;
			}
		}

		protected void onPostExecute(String json) {
			try {
				if(mQueryType==QueryType.Conditions) {
					JSONObject jObj = new JSONObject(json);
					JSONObject jObjCurrentObservation=jObj.getJSONObject("current_observation");
					String weatherIconUrl=jObjCurrentObservation.getString("icon_url");
					ImageView imageRightNow= (ImageView)mWeather.findViewById(R.id.image_rightnow);
					new ImageLoaderRemote(mWeather,false,1f).displayImage(weatherIconUrl, imageRightNow);
					TextView tempRightNow=(TextView)mWeather.findViewById(R.id.temp_rightnow);
					tempRightNow.setText(jObjCurrentObservation.getString("temp_f")+Weather2.degree);
					TextView weatherDesc=(TextView)mWeather.findViewById(R.id.descriptions_rightnow);
					weatherDesc.setText(jObjCurrentObservation.getString("weather"));
					TextView feelsLike=(TextView)mWeather.findViewById(R.id.verbiage_rightnow);
					feelsLike.setText("Feels like "+jObjCurrentObservation.getString("feelslike_f")+Weather2.degree);
					TextView precip=(TextView)mWeather.findViewById(R.id.precipitation_rightnow);
					precip.setText("Precipitation: "+jObjCurrentObservation.getString("precip_today_in")+"\"");
				} else {
					if(mQueryType==QueryType.Hourly) {
						Map<String,Integer> dayIcon=new HashMap<String,Integer>();
						Map<String,Integer> dayCondition=new HashMap<String,Integer>();
						Map<String,Integer> nightIcon=new HashMap<String,Integer>();
						Map<String,Integer> nightCondition=new HashMap<String,Integer>();
						int highTemp=-100;
						int lowTemp=100;
						double gpfDay=0;
						double gpfNight=0;
						double snowDay=0;
						double snowNight=0;
						JSONObject jObj = new JSONObject(json);
						JSONArray hours = jObj.getJSONArray("hourly_forecast");
						for (int i = 0; i < hours.length(); i++) {
							JSONObject theHour=((JSONObject) hours.get(i));
							JSONObject FCTTIME = theHour.getJSONObject("FCTTIME");
							int hour=Integer.valueOf(FCTTIME.getString("hour"));
							JSONObject temps=theHour.getJSONObject("temp");
							int temp=Integer.valueOf(temps.getString("english"));
							JSONObject gpfjson=theHour.getJSONObject("qpf");
							float gpf=0;
							String gpfstr=gpfjson.getString("english");
							if(gpfstr.trim().length()>0) {
								gpf=Float.valueOf(gpfstr);
							}
							float snow=0;
							String snowstr=gpfjson.getString("english");
							if(snowstr.trim().length()>0) {
								snow=Float.valueOf(snowstr);
							}
							if(temp>highTemp) {
								highTemp=temp;
							}
							if(temp<lowTemp) {
								lowTemp=temp;
							}
							String iconUrl=theHour.getString("icon_url");
							String condition=theHour.getString("condition");
							if(hour>6&&hour<18) { // daytime
								snowDay+=snow;
								gpfDay+=gpf;
								if(dayIcon.containsKey(iconUrl)) {
									int iconCnt=((Integer)dayIcon.get(iconUrl)).intValue();
									iconCnt++;
									dayIcon.put(iconUrl, iconCnt);
								} else {
									dayIcon.put(iconUrl, 1);
								}
								if(dayCondition.containsKey(iconUrl)) {
									int cnt=((Integer)dayCondition.get(condition)).intValue();
									cnt++;
									dayCondition.put(condition, cnt);
								} else {
									dayCondition.put(condition, 1);
								}
							} else {
								snowNight+=snow;
								gpfNight+=gpf;
								if(nightIcon.containsKey(iconUrl)) {
									int iconCnt=((Integer)nightIcon.get(iconUrl)).intValue();
									iconCnt++;
									nightIcon.put(iconUrl, iconCnt);
								} else {
									nightIcon.put(iconUrl, 1);
								}
								if(nightCondition.containsKey(iconUrl)) {
									int cnt=((Integer)nightCondition.get(condition)).intValue();
									cnt++;
									nightCondition.put(condition, cnt);
								} else {
									nightCondition.put(condition, 1);
								}

							}
						}
						gpfDay=Utils.round(gpfDay, 3, BigDecimal.ROUND_HALF_UP);
						gpfNight=Utils.round(gpfNight, 3, BigDecimal.ROUND_HALF_UP);
						snowDay=Utils.round(snowDay, 3, BigDecimal.ROUND_HALF_UP);
						snowNight=Utils.round(snowNight, 3, BigDecimal.ROUND_HALF_UP);
						TextView highToday=(TextView)mWeather.findViewById(R.id.temp_today);
						highToday.setText(String.valueOf(highTemp)+Weather2.degree);
						TextView lowTonight=(TextView)mWeather.findViewById(R.id.temp_tonight);
						lowTonight.setText(String.valueOf(lowTemp)+Weather2.degree);
						TextView precipToday=(TextView)mWeather.findViewById(R.id.precipitation_today);
						precipToday.setText("Precipitation: "+String.valueOf(gpfDay)+"\""+"\nSnow: "+String.valueOf(snowDay)+"\"");
						TextView precipTonight=(TextView)mWeather.findViewById(R.id.precipitation_tonight);
						precipTonight.setText("Precipitation: "+String.valueOf(gpfNight)+"\""+"\nSnow: "+String.valueOf(snowNight)+"\"");
						
						/* find most common condition and iconurl */
						String zCondition = null;
						String zIcon = null;
						int cntHighestIcon=-100;
						int cntHighestCondition=-100;
						String zConditionNight = null;
						String zIconNight = null;
						int cntHighestIconNight=-100;
						int cntHighestConditionNight=-100;
						for (Map.Entry<String, Integer> entry : dayCondition.entrySet()) {
							if(entry.getValue().intValue()>cntHighestCondition) {
								zCondition=entry.getKey();
							}
						}
						for (Map.Entry<String, Integer> entry : dayIcon.entrySet()) {
							if(entry.getValue().intValue()>cntHighestIcon) {
								zIcon=entry.getKey();
							}
						}
						for (Map.Entry<String, Integer> entry : nightCondition.entrySet()) {
							if(entry.getValue().intValue()>cntHighestConditionNight) {
								zConditionNight=entry.getKey();
							}
						}
						for (Map.Entry<String, Integer> entry : nightIcon.entrySet()) {
							if(entry.getValue().intValue()>cntHighestIconNight) {
								zIconNight=entry.getKey();
							}
						}					
						TextView todayCondition=(TextView)mWeather.findViewById(R.id.descriptions_today);
						todayCondition.setText(zCondition);
						ImageView todayImage=(ImageView)mWeather.findViewById(R.id.image_today);
						new ImageLoaderRemote(mWeather,false,1f).displayImage(zIcon, todayImage);
						TextView todayConditionNight=(TextView)mWeather.findViewById(R.id.descriptions_tonight);
						todayConditionNight.setText(zConditionNight);
						ImageView todayImageNight=(ImageView)mWeather.findViewById(R.id.image_tonight);
						new ImageLoaderRemote(mWeather,false,1f).displayImage(zIconNight, todayImageNight);

					} else {
						if(mQueryType==QueryType.Forecast) {
							Calendar tomarrow =Calendar.getInstance(Locale.getDefault());
							Calendar dayAfterTomarrow =Calendar.getInstance(Locale.getDefault());
							tomarrow.add(Calendar.DAY_OF_MONTH,1);
							dayAfterTomarrow.add(Calendar.DAY_OF_MONTH, 2);
							JSONObject jObj = new JSONObject(json);
							JSONObject jObjForecast=jObj.getJSONObject("forecast");
							JSONObject JObjSimpleForecast=jObjForecast.getJSONObject("simpleforecast");
							JSONArray dates = JObjSimpleForecast.getJSONArray("forecastday");
							for (int i = 0; i < dates.length(); i++) {
								JSONObject theDate=((JSONObject) dates.get(i));
								JSONObject date=theDate.getJSONObject("date");
								if(i==0) {		// this morning
									TextView txtDayNameToday=(TextView)mWeather.findViewById(R.id.weather_day_one_monthday);
									txtDayNameToday.setText(date.getString("monthname_short")+" "+date.getString("day"));
									TextView day1dayname=(TextView)mWeather.findViewById(R.id.weather_day_one_dayname);
									day1dayname.setText(date.getString("weekday_short"));
									ImageView imViewToday=(ImageView)mWeather.findViewById(R.id.image_day_one);
									String iconUrl=theDate.getString("icon_url");
									new ImageLoaderRemote(mWeather,false,1f).displayImage(iconUrl, imViewToday);
									TextView highLow=(TextView) mWeather.findViewById(R.id.weather_day_one_hilo);
									highLow.setText("Hi "+theDate.getJSONObject("high").getString("fahrenheit")+Weather2.degree+" Lo "+theDate.getJSONObject("low").getString("fahrenheit"));
									TextView day1Verbiage=(TextView)mWeather.findViewById(R.id.weather_day_one_verbiage);
									day1Verbiage.setText(theDate.getString("conditions"));
									
								} else {
									if(i==1) { // 
										TextView txtDayNameToday=(TextView)mWeather.findViewById(R.id.weather_day_two_monthday);
										txtDayNameToday.setText(date.getString("monthname_short")+" "+date.getString("day"));
										TextView day1dayname=(TextView)mWeather.findViewById(R.id.weather_day_two_dayname);
										day1dayname.setText(date.getString("weekday_short"));
										ImageView imViewToday=(ImageView)mWeather.findViewById(R.id.image_day_two);
										String iconUrl=theDate.getString("icon_url");
										new ImageLoaderRemote(mWeather,false,1f).displayImage(iconUrl, imViewToday);
										TextView highLow=(TextView) mWeather.findViewById(R.id.weather_day_two_hilo);
										highLow.setText("Hi "+theDate.getJSONObject("high").getString("fahrenheit")+Weather2.degree+" Lo "+theDate.getJSONObject("low").getString("fahrenheit"));
										TextView day1Verbiage=(TextView)mWeather.findViewById(R.id.weather_day_two_verbiage);
										day1Verbiage.setText(theDate.getString("conditions"));

									} else {
										if(i==2) { // 
											TextView txtDayNameToday=(TextView)mWeather.findViewById(R.id.weather_day_three_monthday);
											txtDayNameToday.setText(date.getString("monthname_short")+" "+date.getString("day"));
											TextView day1dayname=(TextView)mWeather.findViewById(R.id.weather_day_three_dayname);
											day1dayname.setText(date.getString("weekday_short"));
											ImageView imViewToday=(ImageView)mWeather.findViewById(R.id.image_day_three);
											String iconUrl=theDate.getString("icon_url");
											new ImageLoaderRemote(mWeather,false,1f).displayImage(iconUrl, imViewToday);
											TextView highLow=(TextView) mWeather.findViewById(R.id.weather_day_three_hilo);
											highLow.setText("Hi "+theDate.getJSONObject("high").getString("fahrenheit")+Weather2.degree+" Lo "+theDate.getJSONObject("low").getString("fahrenheit"));
											TextView day1Verbiage=(TextView)mWeather.findViewById(R.id.weather_day_three_verbiage);
											day1Verbiage.setText(theDate.getString("conditions"));
										}
									}
								}
							}

						}
					}
				}
			} catch (Exception e) {
				Toast.makeText(Weather2.this, "Failed reading JSON from "+getString(R.string.wunderground_api_conditions)+ ". Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
			}
/*			
				String status = jObj.getString("status");
				if (status == "OVER_QUERY_LIMIT") {
					Thread.currentThread().sleep(1000);
					nbrOfAccessesLeft--;
					nthAccessStartingAt1++;
					return nextPageToken;
				}
			} catch (Exception e) {
			}
			try {
				nextPageToken = jObj.getString("next_page_token");
			} catch (Exception e) {
			}
			JSONArray results = jObj.getJSONArray("results");
			for (int i = 0; i < results.length(); i++) {
				JSONObject geometry = ((JSONObject) results.get(i))
						.getJSONObject("geometry");
				JSONObject location2 = geometry.getJSONObject("location");
				String lat = location2.getString("lat");
				String lng = location2.getString("lng");
				String name = ((JSONObject) results.get(i)).getString("name");
				Address address = new Address(Locale.getDefault());
				address.setLatitude(Double.valueOf(lat));
				address.setLongitude(Double.valueOf(lng));
				address.setAddressLine(0, name);
				runningList.add(address);
			}
			*/
		}
	}
}	

