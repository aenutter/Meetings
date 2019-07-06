package org.example.aafinder;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.net.Uri;

//import android.util.Log;
/*Location locationA = new Location("point A");

locationA.setLatitude(pointA.getLatitudeE6() / 1E6); locationA.setLongitude(pointA.getLongitudeE6() / 1E6);

Location locationB = new Location("point B");

locationB.setLatitude(pointB.getLatitudeE6() / 1E6); locationB.setLongitude(pointB.getLongitudeE6() / 1E6);

double distance = locationA.distanceTo(locationB);
*/
/**
 * Class to represent meeting objects.  The class has private member variables and public methods 
*/
public class Meeting {
  private String id;
  private String startTime;
  private String type;
  private String groupName;
  private String street;
  private String phone;
  private String city;
  private String state;
  private String day;
  private String longitude;
  private String latitude;
  //private String MAC;
  private String fellowship;
  private String location;
  private String zipcode;
  private Boolean isSelected;
  public double distance;
  //private String baseUrl = "http://192.168.42.180/meetings/";
  private String baseUrl = "http://aenutter.com/";
  /**
   * accessor for SQL primary key
   * @return primary key meeting.id
   */
  public Boolean isSelected() { return isSelected; }
  public Double getDistance() { return distance; } 
  public String getID() { return id; }  
  /**
   * accessor for meeting start time
   * @return meeting.startTime
   */
  public String getTime() { return startTime; }
  /**
   * accessor for meeting type (i.e., closed, smoking, speaker, discussion...)
   * @return meeting.type
   */
  public String getType() { return type; }
  /**
   * accessor for meeting name 
   * @return meeting.groupName
   */
  public String getGroupName() { return groupName; }
  /**
   * accessor for street address for a meeting 
   * @return meeting.street
   */
  public String getStreet() { return street; }
  /**
   * accessor for a meeting's phone number 
   * @return meeting.phone
   */
  public String getPhone() { return phone; }
  /**
   * accessor for a meeting's city 
   * @return meeting.city
   */
  public String getCity() { return city; }
  /**
   * accessor for a meeting's state 
   * @return meeting.state
   */
  public String getState() { return state; }
  /**
   * accessor for a meeting's day of week 
   * @return meeting.day
   */
  public String getDay() { return day; }
  /**
   * accessor for a meeting's latitude on the GEO plane 
   * @return meeting.latitude
   */
  public String getLatitude() {return latitude;}
  /**
   * accessor for a meeting's longitude on the GEO plane 
   * @return meeting.longitude
   */
  public String getLongitude() {return longitude; }
  /**
   * accessor for the MAC address of the meetings creator 
   * @return meeting.MAC
   */
  //public String getMAC() {return MAC; }
  /**
   * accessor for meeting's fellowship 
   * @return meeting.fellowship
   */
  public String getFellowship() {return fellowship; }
  /**
   * accessor for meeting's location 
   * @return meeting.location
   */
  public String getLocation() {return location; }
  /**
   * accessor for meeting's zipcode 
   * @return meeting.zipcode
   */
  public String getZipcode() {return zipcode; }
  /**
   * mutator for a meeting's start time 
   */
  public void setSelected(Boolean _isSelected) { isSelected = _isSelected; }
  public void setTime(String _startTime) { startTime = _startTime; }
  /**
   * mutator for a meeting's type 
   */
  public void setType(String _type) { type = _type; }
  /**
   * mutator for a meeting's group name 
   */
  public void setGroupName(String _groupName) { groupName = _groupName; }
  /**
   * mutator for a meeting's street address 
   */
  public void setStreet(String _street) { street = _street; }
  /**
   * mutator for a meeting's phone number 
   */
  public void setPhone(String _phone) { phone = _phone; }
  /**
   * mutator for a meeting's city 
   */
  public void setCity(String _city) { city = _city; }
  /**
   * mutator for a meeting's state 
   */
  public void setState(String _state) { state = _state; }
  /**
   * mutator for a meeting's week day 
   */
  public void setDay(String _day) { day = _day; }
  /**
   * mutator for a meeting's latitude 
   */
  public void setLatitude(String _latitude) { latitude = _latitude; }
  /**
   * mutator for a meeting's longitude 
   */
  public void setLongitude(String _longitude) { longitude = _longitude; }
  /**
   * mutator for a meeting's MAC address 
   */ 
  //public void setMAC(String _MAC) { MAC = "38:E7:D8:Bf:53:38" + " D8:B3:77:39:97:F0 " + _MAC; }
  /**
   * mutator for a meeting' fellowship 
   */ 
  public void setFellowship(String _fellowship) { fellowship = _fellowship; }
  /**
   * mutator for a meeting's location 
   */ 
  public void setLocation(String _location) { location = _location; }
  /**
   * mutator for a meeting's zipcode 
   */ 
  public void setZipcode(String _zipcode) { zipcode = _zipcode; }
  /**
   * Constructor for a meeting object 
   */
  public Meeting(String _id, String _startTime, String _type, String _groupName, String _street, String _phone, String _city, String _state, String _day, String _latitude, String _longitude, /*,String _MAC*/ String _fellowship, String _location, String _zipcode, Boolean _isSelected, double _distance) {
    id = _id;
	startTime = _startTime;
	type = _type;
    groupName = _groupName;
    street = _street;
    phone = _phone;
    city = _city;
    state = _state;
    day = _day;
    latitude = _latitude;
    longitude = _longitude;
   // MAC = _MAC;
    fellowship = _fellowship;
    location = _location;
    zipcode = _zipcode;
    isSelected = false;
    distance = _distance;
  }
  /**
   * Constructor for a meeting object 
   */
  public Meeting() {
	    id = null;
		startTime = null;
		type = null;
	    groupName = null;
	    street = null;
	    phone = null;
	    city = null;
	    state = null;
	    day = null;
	    latitude = null;
	    longitude = null;
	   // MAC = _MAC;
	    fellowship = null;
	    location = null;
	    zipcode = null;
	    distance = 0;
	  }
  /**
   * method to access an object's string members 
   * @return  an objects private members in a string
   */
  @Override
  public String toString() {
    //SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
   // String dateString = sdf.format(date);
    //return dateString + ": " + magnitude + " " + details;
	  return startTime + " " + day + " " + fellowship + "\nGroup: " + groupName + " " + type + " " + phone + "\nAddress: " + street + " " + city + " " + state + " " + zipcode + "\nNotes: " + location + "\nDistance: " + distance;
  }
  /**
   * method to access an object's string members 
   * @return  an objects private members in a string
   */
  public String toStringUnFormatted() {
    //SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
   // String dateString = sdf.format(date);
    //return dateString + ": " + magnitude + " " + details;
	  return startTime + " " + day + " " + fellowship + "Group: " + groupName + " " + type + " " + phone + "Address: " + street + " " + city + " " + state + " " + zipcode + " Notes: " + location + " Distance: " + distance;
  }
  
  
  /**
   * method to generate a meeting's GEO coordinates based on the street address, city and state 
   * this method will catch Interrupted exceptions, ClientProtocol exceptions and IO exceptions.
   * The Google GEOCode service returns a status code and a comma seperated variable containing the coordinates
   * If the status code is 200 (HTTP OK), all is fine. return true.
   * If the status code is 620, the thread sleeps, to give the GEO service more time
   * All other status code's result in failure. return false.
   * @param  a meeting object without GEO coordinates
   * @return True or False depending on GEO coding success or failure respectively
   */
	public boolean setGeoPoints(Meeting selectedMeeting) {
		String address = null;
	    URL url = null;
	    
		    address = selectedMeeting.getStreet();
			address = address +" " + selectedMeeting.getCity();
			address = address + ", " + selectedMeeting.getState();
			
			
		    //Log.e("Refresh earthmeetings: ", getString(R.string.base_url) + "server.php?day=" +nameValuePairs.toString());
		    String parameters = URLEncoder.encode(address);
		    //Log.e("Query: ", "http://maps.googleapis.com/maps/api/geocode/xml?address="+parameters+"&sensor=false");
		    Uri uri = Uri.parse("http://maps.googleapis.com/maps/api/geocode/xml?address="+parameters+"&sensor=false");
		    try {
				url = new URL(uri.toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    //Log.e("HTTP Response ", url.toString());     
		    URLConnection connection = null;
		    try {
				connection = url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    HttpURLConnection httpConnection = (HttpURLConnection)connection; 
		    int responseCode = 0;
			try {
				responseCode = httpConnection.getResponseCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    

		    if (responseCode == HttpURLConnection.HTTP_OK) { 
		      InputStream in = null;
			try {
				in = httpConnection.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		          
		      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		      DocumentBuilder db = null;
			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		      // Parse the earthmeeting feed.
		      Document dom = null;
			try {
				dom = db.parse(in);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}      
		      Element docEle = dom.getDocumentElement();
		      		          
		      // Get a list of each earthmeeting entry.

		      NodeList n2 = docEle.getElementsByTagName("status");
		      String status = n2.item(0).getFirstChild().getNodeValue();
		      if (status.compareTo("OK")==0) {
		    	  NodeList nl = docEle.getElementsByTagName("result");
		    	  //Log.e("meeting activity", "number meetings: "+String.format("%s", nl.getLength())+" status: "+status);
		    	  if (nl != null && nl.getLength() > 0) {
		        
		    		  Element entry = (Element)nl.item(0);
		          
		          
		    		  entry.getElementsByTagName("geometry").item(0);
		    		  entry.getElementsByTagName("location").item(0);
		    		  Element _latitude = (Element) entry.getElementsByTagName("lat").item(0);
		    		  String latitude = _latitude.getFirstChild().getNodeValue();
		    		  Element _longitude = (Element) entry.getElementsByTagName("lng").item(0);
		    		  String longitude = _longitude.getFirstChild().getNodeValue();
		    		  Element _location_type = (Element) entry.getElementsByTagName("location_type").item(0);
		    		  String location_type = _location_type.getFirstChild().getNodeValue();
		    		  
		    		  NodeList n3 = docEle.getElementsByTagName("address_component");
		    		  int addressComponentNodeLength = n3.getLength();
		    		  Element _address_component = (Element)n3.item(addressComponentNodeLength-1);
		    		  Element _zipcode = (Element) _address_component.getElementsByTagName("short_name").item(0);
		    		  String  zipcode = _zipcode.getFirstChild().getNodeValue();
		    		  
		    		  //Element _address_component = (Element) entry.getElementsByTagName("address_component").item(8);
		    		  //Element _zipcode = (Element) _address_component.getElementsByTagName("short_name").item(0);
		    		  //String  zipcode = _zipcode.getFirstChild().getNodeValue();
		    		  //Log.e("geo code", "latitude: "+latitude+" longitude: "+longitude+" location type: "+location_type+" Street: "+selectedMeeting.getStreet()+" City: "+selectedMeeting.getCity()+" State: "+selectedMeeting.getState()+" Zipcode: "+zipcode);
		    		  if (location_type.compareTo("ROOFTOP")==0 || location_type.compareTo("RANGE_INTERPOLATED")==0) {
		    			  selectedMeeting.setLatitude(latitude);
		    			  selectedMeeting.setLongitude(longitude);
		    			  selectedMeeting.setZipcode(zipcode);
		    			  //Log.e("success", "latitude: "+latitude+" longitude: "+longitude+" location type: "+location_type+" Street: "+selectedMeeting.getStreet()+" City: "+selectedMeeting.getCity()+" State: "+selectedMeeting.getState()+" Zipcode: "+selectedMeeting.getZipcode());
		    			  return true;
		    		  }
		         }
		      	 
		      }
		   }
		return false;
  }
	/**
	   * method to take a meeting that has been updated on the device and update the 
	   * remote database with the new meeting details
	   * catches httpClient protocol exceptions
	   * @param updated meeting object
	   * @return true if the SQL query returns update successful, otherwise return false 
	   */
	public boolean updateMeeting(Meeting _selectedMeeting) {
		HttpPost httppost;
		
		HttpClient httpclient;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(14);
        nameValuePairs.add(new BasicNameValuePair("id",_selectedMeeting.getID()));
        nameValuePairs.add(new BasicNameValuePair("start_time",_selectedMeeting.getTime()));
        nameValuePairs.add(new BasicNameValuePair("type",_selectedMeeting.getType()));
        nameValuePairs.add(new BasicNameValuePair("group_name",_selectedMeeting.getGroupName()));
        nameValuePairs.add(new BasicNameValuePair("street",_selectedMeeting.getStreet()));
        nameValuePairs.add(new BasicNameValuePair("city",_selectedMeeting.getCity()));
        nameValuePairs.add(new BasicNameValuePair("state",_selectedMeeting.getState()));
        nameValuePairs.add(new BasicNameValuePair("phone",_selectedMeeting.getPhone()));
        nameValuePairs.add(new BasicNameValuePair("day",_selectedMeeting.getDay()));
        nameValuePairs.add(new BasicNameValuePair("latitude",_selectedMeeting.getLatitude()));
        nameValuePairs.add(new BasicNameValuePair("longitude",_selectedMeeting.getLongitude()));
        nameValuePairs.add(new BasicNameValuePair("fellowship",_selectedMeeting.getFellowship()));
        nameValuePairs.add(new BasicNameValuePair("location",_selectedMeeting.getLocation()));
        nameValuePairs.add(new BasicNameValuePair("zip",_selectedMeeting.getZipcode()));
		
          
        //Log.e("Quake.java update meeting", "http://aenutter.onlinewebshop.net/update_meeting.php?parameters=" +nameValuePairs.toString());
		Uri uri = Uri.parse(baseUrl+"update_meeting.php?parameters=" +URLEncoder.encode(nameValuePairs.toString()));
		
		//Log.e("update_meetings", baseUrl+"update_meeting.php?parameters=" + nameValuePairs.toString());
		//URL request_url = new URL(uri.toString());
		httppost = new HttpPost(uri.toString());
		httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		String responseBody = null;
		try {
			response = httpclient.execute(httppost);
			//Log.e("Response Phrase: ", response.getStatusLine().getReasonPhrase());
			
		// Response from the server          
		
		responseBody = EntityUtils.toString(response.getEntity());
		
		}
		
		catch (Exception e) {
		// Exception handling
			//Log.e("Exception", e.toString());
			e.printStackTrace(); 
		}
		boolean result;
		if(responseBody.compareTo("true") == 0){
		    result = true;   
		}else{
		    result = false;
		}
		//Log.e("updateResponse Body: ", responseBody);
		return result;
		//Log.e("Response Body: ", responseBody);
		
	}
	/**
	   * method to create a SQL meeting record using web service
	   * catches httpclient protocol exceptions and IO exceptions
	   * @param a new meeting object
	   * @return true if database query is successful, otherwise return false
	   */
	public boolean createMeeting(Meeting _selectedMeeting) {
		HttpPost httppost;

		HttpClient httpclient;
		String responseBody = null;
		// List with arameters and their values
		

		
		
		//WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
		//WifiInfo wifiInfo = manager.getConnectionInfo(); 
		//String MACAddress = wifiInfo.getMacAddress(); 

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(13);
        nameValuePairs.add(new BasicNameValuePair("start_time",_selectedMeeting.getTime()));
        nameValuePairs.add(new BasicNameValuePair("type",_selectedMeeting.getType()));
        nameValuePairs.add(new BasicNameValuePair("group_name",_selectedMeeting.getGroupName()));
        nameValuePairs.add(new BasicNameValuePair("street",_selectedMeeting.getStreet()));
        nameValuePairs.add(new BasicNameValuePair("city",_selectedMeeting.getCity()));
        nameValuePairs.add(new BasicNameValuePair("state",_selectedMeeting.getState()));
        nameValuePairs.add(new BasicNameValuePair("phone",_selectedMeeting.getPhone()));
        nameValuePairs.add(new BasicNameValuePair("day",_selectedMeeting.getDay()));
        nameValuePairs.add(new BasicNameValuePair("latitude",_selectedMeeting.getLatitude()));
        nameValuePairs.add(new BasicNameValuePair("longitude",_selectedMeeting.getLongitude()));
        nameValuePairs.add(new BasicNameValuePair("fellowship",_selectedMeeting.getFellowship()));
        nameValuePairs.add(new BasicNameValuePair("location",_selectedMeeting.getLocation()));
        nameValuePairs.add(new BasicNameValuePair("zip",_selectedMeeting.getZipcode()));
        //nameValuePairs.add(new BasicNameValuePair("MAC",_selectedMeeting.getMAC()));
		//Log.e("Quake.java create meeting: ", "http://aenutter.onlinewebshop.net/create_meeting.php?parameters=" +nameValuePairs.toString());
		Uri uri = Uri.parse(baseUrl+ "create_meeting.php?parameters=" +URLEncoder.encode(nameValuePairs.toString()));
		//Log.e("create_meeting", uri.toString());
		//URL request_url = new URL(uri.toString());
		httppost = new HttpPost(uri.toString());
		httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		
			
		response.getStatusLine().getReasonPhrase();
		response.getStatusLine().getStatusCode();
		}
		catch (Exception e) {
		// Exception handling
			//Log.e("Exception", e.toString());
			e.printStackTrace();
		 
		}
		
		try {
			responseBody = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Log.e("response body", responseBody);
	if (!(responseBody.compareTo("true") == 0)) {
		//Log.e("Quake.java inside first if response body: ", responseBody);
		return true;
	} else {
		//Log.e("Quake.java inside second if response body: ", responseBody);
		return false;
	}
		
		
	}
	
	public boolean distanceMatrix(Meeting selectedMeeting, float myLatitude, float myLongitude) {
			URL url = null;
		    
			//Log.e("Query: ", "http://maps.googleapis.com/maps/api/distancematrix/xml?origins="+selectedMeeting.getLatitude()+"%20"+selectedMeeting.getLongitude()+"&destinations="+myLatitude+"%20"+myLongitude+"&mode=car&language=en-EN&sensor=false");
			//http://maps.googleapis.com/maps/api/distancematrix/xml?origins=37.2767946%20-97.1637035&destinations=30.660837%20-88.106378&mode=car&language=en-EN&sensor=false

			Uri uri = Uri.parse("http://maps.googleapis.com/maps/api/distancematrix/xml?origins="+URLEncoder.encode(selectedMeeting.getLatitude()+" ")+URLEncoder.encode(selectedMeeting.getLongitude())+"&destinations="+URLEncoder.encode(String.format("%s", myLatitude)+" ")+URLEncoder.encode(String.format("%s", myLongitude))+"&mode=car&language=en-EN&sensor=false");
			//Log.e("Query: ", uri.toString());
			try {
				url = new URL(uri.toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    //Log.e("HTTP Response ", url.toString());     
		    URLConnection connection = null;
		    try {
				connection = url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    HttpURLConnection httpConnection = (HttpURLConnection)connection; 
		    int responseCode = 0;
			try {
				responseCode = httpConnection.getResponseCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			//Log.e("response body", String.format("%s", responseCode));

		    if (responseCode == HttpURLConnection.HTTP_OK) { 
		      InputStream in = null;
			try {
				in = httpConnection.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		          
		      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		      DocumentBuilder db = null;
			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		      // Parse the earthmeeting feed.
		      Document dom = null;
			try {
				dom = db.parse(in);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}      
		      Element docEle = dom.getDocumentElement();
		      		          
		      // Get a list of each earthmeeting entry.


		      NodeList n2 = docEle.getElementsByTagName("status");
		      if (n2 != null && n2.getLength() > 0) {
		      //String status = n2.item(0).getFirstChild().getNodeValue();
		      NodeList nl = docEle.getElementsByTagName("row");
		      //Log.e("meeting", "number meetings: "+String.format("%s", nl.getLength()));
		    	  
		        
		    		  Element entry = (Element)nl.item(0);
		    		  //entry.getElementsByTagName("element").item(0);
		    		  entry.getElementsByTagName("element").item(0);
		    		  //entry.getElementsByTagName("duration").item(0);
		    		  entry.getElementsByTagName("distance").item(0);
		    		  Element _distance = (Element) entry.getElementsByTagName("distance").item(0);
		    		  Element _value = (Element) _distance.getElementsByTagName("value").item(0);
		    		  //Element _value = (Element) entry.getElementsByTagName("value").item(0);
		    		  //Element _value2 = (Element) entry.getElementsByTagName("value").item(0);
		    		  String value = _value.getFirstChild().getNodeValue();
		    		  
		    		  		    		  
		    		 // Element _distance = (Element) entry.getElementsByTagName("distance").item(0);
		    		  //String distance = _distance.getFirstChild().getNodeValue();
		    		  
		    		  selectedMeeting.distance = Double.valueOf(value).doubleValue() * 0.00062137119;
		    		  //Log.e("matrix", "distance: "+selectedMeeting.distance+" miles group: "+selectedMeeting.groupName);

		    		  
		         }
		      	 
		      
		   }
		return false;
  }
	
	String convertStreamToString(java.io.InputStream is) {
	    try {
	        return new java.util.Scanner(is).useDelimiter("\\A").next();
	    } catch (java.util.NoSuchElementException e) {
	        return "";
	    }
	}
	
}