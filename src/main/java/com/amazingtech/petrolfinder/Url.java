package com.amazingtech.petrolfinder;

public class Url {
	
	public String PersonalImage(String ID, String imgName) {
		String Path = "http://webapp.v4ride.com//UserImages/User" + ID
				+ "/Personal/" + imgName;

		return Path.replaceAll(" ", "%20");

	}

	public String VehicleImage(String ID, String imgName) {
		
		String Path = "http://webapp.v4ride.com//UserImages/User" + ID
				+ "/Vehicle/" + imgName;
System.out.println("PathVehicle"+Path);
		return Path.replaceAll(" ", "%20");

	}

	public String IdProofImage(String ID, String imgName, int type) {
		String Path = null;
		switch (type) {
			case 1 :
				Path = "http://webapp.v4ride.com//UserImages/User" + ID
						+ "/IdProof/AadharCard/" + imgName;
				break;
			case 2 :
				Path = "http://webapp.v4ride.com//UserImages/User" + ID
						+ "/IdProof/VoterCard/" + imgName;
				break;
			case 3 :
				Path = "http://webapp.v4ride.com//UserImages/User" + ID
						+ "/IdProof/PanCard/" + imgName;
				break;
			case 4 :
				Path = "http://webapp.v4ride.com//UserImages/User" + ID
						+ "/IdProof/Passport/" + imgName;
				break;
			case 5 :
				Path = "http://webapp.v4ride.com//UserImages/User" + ID
						+ "/IdProof/License/" + imgName;
				break;
			default :
				break;
		}

		return Path.replaceAll(" ", "%20");

	}


	public static String GetUrl(String url) {
		url = url.replaceAll(" ", "%20");
		url = url.replaceAll("\n", "%0A");
		url = url.replaceAll("#", "%23");

		return url;
	}

}