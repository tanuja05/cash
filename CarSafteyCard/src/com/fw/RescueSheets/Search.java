package com.fw.RescueSheets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kfz.Rettungskarten.database.CarPdfAdapter;

public class Search extends FragmentActivity {
	ArrayList<String> company_name;
	AutoCompleteTextView autoComplete;
	TextView autoComplete1;
	String pdf_url;
	ArrayList<HashMap<String, String>> datalist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> datalist1 = new ArrayList<HashMap<String, String>>();
	ArrayAdapter<String> company_adapter, abarth_adapter, alfa_romeo_adapter,
			audi_adapter, bmw_adapter, cadillac_adapter, chevrolet_adapter,
			chryler_adapter, citroen_adapter, dacia_adapter, diahstu_adapter,
			dodge_adapter, e_wolf_adapter, ford_adapter, honda_adapter,
			hyundayi_adapter, infiniti_adapter, isuzu_adapter, jaguar_adapter,
			jeep_adapter, karabag_adapter, lada_adapter, lancia_adapter,
			landrover_adpter, lexus_adapter, luis_adapter, maybach_adapter,
			mazda_adapter, Mercedes_Benz_transporter_adapter,
			mitsubishi_adapter, porshe_adapter, saab_adapter, skoda_adapter,
			smart_adapter, ssangYong_adapter, subar_adapter, suzuki_adapter,
			tesla_adapter, think_adapter, volovo_adapter, opel_adapter,
			kia_adapter, nissan_adapter, vw_adapter, toyta_adapter,
			seat_adapter, dethleffs_adapter, mercedes_benz_adapter,
			renault_adapter, fiat_adapter, fiat_professional_adapter,
			peugeot_adapter;

	static String data, adapt, s, msg;

	String[] abarth_english = { "500", "695 Tributo Ferrari", "Grande Punto",
			"Punto Evo" };

	// done

	String[] alfa_romeo_english = { "147", "159 Sportwagon", "159", "Brera",
			"Giulietta", "GT", "MITO LPG", "MITO", "Spider" };

	String[] audi_english = { "80 Cabriolet model year 1991_2000",
			"80 S2 model year 1990_1995", "80 S2 Coup model year 1988_1996",
			"80 S2 RS2 Avant model year 1992_1995",
			"100 A6 S6 Avant model year 1991_1997",
			"100 A6 S6 model year 1991_1997", "100 Avant model year 1988_1991",
			"100 model year 1988_1991", "200 Avant model year 1985_1991",
			"200 V8 model year 1984_1991",
			"A1 e-tron complete model year from 2011",
			"A1 S1 model year from 2010",
			"A1 S1 Sportback model year from 2012", "A2 model year 2000_2005",
			"A3 (5-Doors) model year 1999_2003",
			"A3 Cabriolet model year 2008_2013",
			"A3 S3 (3-Doors) model year 1996_2003",
			"A3 S3 (3-Doors) model year 2003_2012",
			"A3 S3 (3-Doors) model year from 2012",
			"A3 S3 Cabriolet model year from 2013",
			"A3 S3 Limousine model year from 2013",
			"A3 S3 RS3 Sportback model year 2004_2013",
			"A3 S3 Sportback model year from 2012",
			"A3 Sportback e-tron complete model year from 2014",
			"A3 Sportback g-tron complete model year from 2013",
			"A4 allroad model year 2009_2012",
			"A4 allroad model year from 2012",
			"A4 S4 Avant model year 2001_2005",
			"A4 S4 Avant model year 2008_2012", "A4 S4 model year 1995_2001",
			"A4 S4 model year 2001_2005", "A4 S4 model year 2007_2012",
			"A4 S4 model year from 2012",
			"A4 S4 Cabriolet model year 2002_2005",
			"A4 S4 RS4 Avant model year from 2012",
			"A4 S4 RS4 Avant model year 1996_2001",
			"A4 S4 RS4 Avant model year 2005_2008",
			"A4 S4 RS4 model year 2005_2007",
			"A4 S4 RS4 Cabriolet model year 2005_2009",
			"A5 S5 Cabriolet model year 2009-2011",
			"A5 S5 RS5 model year 2007_2011", "A5 S5 RS5 model year from 2011",
			"A5 S5 RS5 Cabriolet model year from 2011",
			"A5 S5 Sportback model year 2009_2011",
			"A5 S5 Sportback model year from 2011",
			"A6 allroad model year 2001_2006",
			"A6 allroad model year 2006_2012",
			"A6 allroad model year from 2012",
			"A6 hybrid complete model year from 2012",
			"A6 S6 model year from 2011",
			"A6 S6 RS6 Avant model year 1998_2004",
			"A6 S6 RS6 Avant model year 2004_2012",
			"A6 S6 RS6 model year 1997_2004", "A6 S6 RS6 model year 2004_2008",
			"A6 S6 RS6 model year 2008_2011", "A6 S6 RS6 model year from 2011",
			"A7 S7 RS7 Sportback model year from 2010",
			"A8 hybrid complete model year from 2012",
			"A8 S8 model year 1994_2002", "A8 S8 model year 2002_2010",
			"A8 S8 model year from 2010", "Q3 RS Q3 model year from 2011",
			"Q5 hybrid quattro complete model year from 2011",
			"Q5 SQ5 model year from 2008", "Q7 model year from 2006",
			"R8 model year from 2007", "R8 Spyder model year from 2010",
			"TT Coup model year 1998-2006", "TT Roadster model year 1999_2006",
			"TT TTS Coup model year from 2014",
			"TT TTS TTRS Coup model year 2006_2014",
			"TT TTS TTRS Roadster model year from 2007" };

	// Done audi

	String[] bmw_english = {
			"1-Series-Active-E-Coup-Compact model year from 2011",
			"1-Series-E81-Coup-Compact model year 2007_2011",
			"1-Series-E82-Coup-Compact model year 2007_2014",
			"1-Series-E87-Coup-Compact model year 2004-2011",
			"1-Series-E88-Cabrio model year from 2007",
			"1-Series-F20-Coup-Compact model year from 2011",
			"1-Series-F21-Coup-Compact model year from 2012",
			"2-Series-F22-Coup-Compact model year from 2014",
			"2-Series-F45-Limousine model year from 2014",
			"3-Series-E46-Compact Baujahr 2001_2004",
			"3-Series-Active-Hybrid-3-F30 model year from 2012",
			"3-Series-E36-Cabrio model year 1992_1999",
			"3-Series-E36-Compact model year 1994_2000",
			"3-Series-E36-Coup model year 1990_1999",
			"3-Series-E36-Limousine model year 1990_1999",
			"3-Series-E36-Touring model year 1994_1999",
			"3-Series-E46-Cabrio model year 1998_2006",
			"3-Series-E46-Limousine model year 1997_2005",
			"3-Series-E46-Touring model year 1999_2005",
			"3-Series-E90-Limousine model year 2005_2012",
			"3-Series-E91-Touring model year 2005_2012",
			"3-Series-E92-Coup-Compact model year 2006_2013",
			"3-Series-E93-Cabrio model year 2006_2014",
			"3-Series-F30-Limousine model year from 2012",
			"3-Series-F31-Touring model year from 2012",
			"3-Series-F34 model year from 2013",
			"3-Series-F80 model year from 2014",

			"4-Series-F32-Coup-Compact model year from 2013",
			"4-Series-F33-Cabrio model year from 2014",
			"4-Series-F36-Coup-Compact model year from 2014",
			"4-Series-F82-Coup-Compact model year from 2014",
			"4-Series-F83-Cabrio model year from 2014",
			"5-Series-Active-Hybrid-5-F10 model year from 2011",
			"5-Series-E34-Limousine model year 1987_1996",
			"5-Series-E34-Touring model year 1990_1996",
			"5-Series-E39-Limousine model year 1995_2003",
			"5-Series-E39-Touring model year 1996_2003",
			"5-Series-E60 model year 2003_2010",
			"5-Series-E61-Touring model year 2004_2010",
			"5-Series-F07 model year from 2009",
			"5-Series-F10 model year from 2010",
			"5-Series-F11-Touring model year from 2010",
			"6-Series-E63-Coup-Compact model year 2004_2010",
			"6-Series-E64-Cabrio model year 2004_2010",
			"6-Series-F06-Coup-Compact model year from 2012",
			"6-Series-F12-Cabrio model year from 2011",
			"6-Series-F13-Coup-Compact model year from 2011",
			"7-Series-Active-Hybrid-7-F01-F02 model year from 2012",
			"7-Series-Active-Hybrid-7-F04 model year from 2010",
			"7-Series-E32 model year 1986_1994",
			"7-Series-E38 model year 1993_2001",
			"7-Series-E65-E66 model year 2001_2008",
			"7-Series-F01-F02 model year from 2008",
			"8-Series-E31-Coup-Compact model year 1989_1999",
			"8-Series-Z3-E36-7-Coup model year 1998_2002",
			"8-Series-Z4-E86-Coup model year 2006_2008",
			"i3 model year from 2013",

			"MINI-E Coup model year 2008_2009",
			"MINI-F55 model year from 2014", "MINI-F56 model year from 2014",
			"MINI-R50 model year 2001_2006",
			"MINI-R52-Cabrio model year 2004_2008",
			"MINI-R53-Cooper S model year 2001_2006",
			"MINI-R55-Clubman model year from 2007",
			"MINI-R56 model year 2006_2014",
			"MINI-R57-Cabrio model year from 2008",
			"MINI-R58-Coup model year from 2011",
			"MINI-R59-Roadster model year from 2011",
			"MINI-R60-Countryman model year from 2010",
			"MINI-R61-Paceman model year 2008_2009",
			"Motorrad E-Scooter C evolution",

			"X1-E84-SUV model year from 2009",
			"X3-E83-SUV model year 2003_2010",
			"X3-F25-SUV model year from 2010",
			"X4-F26-SUV model year from 2014",
			"X5-E53-SUV model year 1999_2006",
			"X5-E70-SUV model year 2006_2013",
			"X5-F15-SUV model year from 2013",
			"X6-Active-Hybrid-E72-SUV model year from 2009",
			"X6-E71-SUV model year from 2008",

			"Z3-E36-7-Roadster model Year 1995_2002",
			"Z4-E85-Roadster model year 2002_2008",
			"Z4-E89-Roadster model year from 2009",
			"Z8-E52-Roadster model year 2000_2003" };
	// Done bmw
	String[] chevrolet_english = { "Aveo 3-Doors model year from 2006",
			"Aveo 4-Doors model year from 2011",
			"Aveo 5-Doors model year from 2006",
			"Aveo 5-Doors model year from 2011",

			"Camaro Cabrio model year from 2011",
			"Camaro Coupe model year from 2007",
			"Captiva model year from 2006", "Captiva model year from 2011",
			"Captiva MY 13 SUV model year from 2013",

			"Corvette Cabrio model year from 2008",
			"Corvette Coupe model year from 2008",
			"Corvette Coupe model year from 2014",
			"Corvette Stingray Cabrio model year from 2014",
			"Cruze Kombi model year from 2012",
			"Cruze Limousine 4-Doors model year from 2009",
			"Cruze Limousine 5-Doors model year from 2011",
			"Epica Limousine 4-Doors model year from 2006",
			"HHR Kombi 4-Doors model year from 2006",
			"Lacetti Limousine 5-Doors model year from 2004",
			"Malibu Limousine 4-Doors model year from 2011",
			"Matiz model year from 2005", "Nubira Kombi model year from 2004",
			"Orlando Kombi model year from 2011", "Rezzo model year from 2000",
			"Spark model year from 2010", "Trax SUV model year from 2013",
			"Volt Electro Car model year from 2011" };
	// Done chevrolet
	String[] cadillac_english = { "CTS 4-Doors model year from 2014",
			"Escalade SUV model year from 2014" };
	// Done cadialaic
	String[] chrysler_english = { "300 C Typ LX 4-Doors  model year from 2005",
			"300 C Typ LX 5-Doors model year from 2005",
			"Grand Voyager Typ RT model year from 2007",
			"Sebring Typ JS 2-Doors model year from 2007",
			"Sebring Typ JS 4-Doors model year from 2007" };
	// doneeee
	String[] citroen_english = { "Berlingo B9 electro cars",
			"Berlingo model year from 2008", "Berlingo First",
			"C1 3- and 5-Doors model year from 2005",
			"C2 model year 2003_2009", "C3 model year 2002_2009",
			"C3 model year from 2009", "C3 Picasso model year from 2009",
			"C3 Pluriel model year 2003_2006",
			"C4  (3- and 5-Doors) model year from 2010",
			"C4 (3- and 5-Doors) model year 2004_2010",
			"C4 Picasso (5- and 7 Seats) model year 2006_2013",
			"C4 Picasso (5- and 7-Seats) model year from 2013",
			"C5 Limousine model year 2001_2008",
			"C5 Limousine and Tourer model year from 2008",
			"C5 Tourer model year 2001_2008", "C6", "C8 model year 2002_2006",
			"C-Crosser model year from 2007", "C-Zero model year from 2010",
			"C-Zero", "DS 3 model year from 2009", "DS 4 model year from 2011",
			"DS 5 model year from 2012", "DS 5 Hybrid model year from 2012",
			"DS 5 Hybrid", "Jumper model year from 2007",
			"Jumpy model year from 2007", "Nemo model year from 2008",
			"Xsara Picasso model year from 2000" };
	// Done citroennn
	String[] dacia_english = { "Dokker Typ SD model year from 2012",
			"Duster Typ SD model year from 2010",
			"Lodgy Typ SD model year from 2012",
			"Logan MCV Typ SD model year from 2007",
			"Logan Pick-up Typ SD model year from 2008",
			"Logan Typ SD model year from 2005",
			"Sandero Typ SD model year from 2008",
			"Stepway Typ SD model year from 2008" };
	// donneeee dacia
	String[] daihatsu_english = { "Applause 4-Doors model year 1998_2001",
			"Charade 3-Doors model year 1996_2001",
			"Charade 4-Doors model year 1996_2001",
			"Charade 5-Doors model year 1996_2001",
			"Charade 5-Doors model year from 2011",
			"Copen 2-Doors model year 2004_2006",
			"Copen 2-Doors model year from 2006",
			"Cuore 3-Doors model year 1998_2003",
			"Cuore 5-Doors model year 1998_2003",
			"Cuore 3-Doors model year 2003_2006",
			"Cuore 5-Doors model year 2003_2006",
			"Cuore 5-Doors model year from 2007",
			"Cuore 3-Doors model year 1995_1998",
			"Cuore 5-Doors model year 1995_1998",
			"Grand Move 5-Doors model year 1998_2003",
			"Materia 5-Doors model year from 2007",
			"Move 5-Doors model year 1997_2003",
			"Sirion 5-Doors model year 1998_2005",
			"Sirion 5-Doors model year from 2005",
			"Terios 5-Doors model year 1997_2005",
			"Terios 5-Doors model year from 2006",
			"Trevis 5-Doors model year 2006_2009",
			"YRV 5-Doors model year 2000_2005" };
	// donecdiahtsoo
	String[] dodge_english = { "Caliber Typ PK model year from 2006",
			"Journey Eco Typ JC model year from 2009",
			"Journey Typ JC model year from 2009",
			"Nitro Typ KJ model year from 2007" };

	String[] e_Wolf_english = { "e-Wolf" };

	String[] honda_english = { "Accord (4-Doors) model year 2003_2008",
			"Accord (4-Doors) model year from 2008",
			"Accord Tourer (5-Doors) model year 2003_2008",
			"Accord Tourer (5-Doors) model year from 2008",
			"Civic (3-Doors) model year 2001_2005",
			"Civic (3-Doors) model year from 2007",
			"Civic (4-Doors) model year from 2011",
			"Civic (5-Doors) model year 2001_2005",
			"Civic (5-Doors) model year 2006_2011",
			"Civic (5-Doors) model year from 2012",

			"Civic Hybrid (4-Doors) model year from 2006",
			"Civic Tourer model year from 2014",
			"CR-V (5-Doors) model year 2002_2005",
			"CR-V (5-Doors) model year 2005_2006",
			"CR-V (5-Doors) model year 2007_2012",
			"CR-V (5-Doors) model year from 2013",
			"CR-Z (3-Doors) model year from 2010",
			"FR-V (5-Doors) model year from 2005",
			"Insight (5-Doors) model year from 2009",
			"Jazz (5-Doors) model year 2002_2008",
			"Jazz (5-Doors) model year from 2009",
			"Jazz Hybrid (5-Doors) model year from 2011" };
	// done honda
	String[] hyundai_english = { "Accent (3-Doors) model year 2000_2006",
			"Accent (3-Doors) model year 2006_2009",
			"Accent (4-Doors) model year 2006_2009",
			"Accent (5-Doors) model year 2000_2006",
			"Atos (5-Doors) model year 1998_2008",

			"Coupe (2-Doors) model year 2002_2006",
			"Coupe (2-Doors) model year 2006_2010",

			"Elantra (4-Doors) model year 2000_2005",
			"Elantra (5-Doors) model year 2000_2005",

			"Genesis Coupe (2-Doors) model year from 2011",
			"Getz (3-Doors) model year 2002_2008",
			"Getz (5-Doors) model year 2002_2008",
			"Grandeur (4-Doors) model year from 2005",

			"H-1 model year 1998_2007", "H-1 model year from 2008",

			"i10 (5-Doors) model year 2008_2010",
			"i10 (5-Doors) model year 2011_2014",
			"i10 (5-Doors) model year from 2014",
			"i20 (3-Doors) model year from 2008",
			"i20 (5-Doors) model year from 2008",
			"i30 (5-Doors) model year 2008_2012",
			"i30 (5-Doors) model year from 2012",
			"i30 Coupe model year from 2013", "i30cw model year 2008_2012",
			"i30cw model year from 2012",

			"i40 (4-Doors) model year from 2012", "i40cw model year from 2011",
			"ix20 (5-Doors) model year from 2010",
			"ix35 (5-Doors) model year 2010_2013",
			"ix35 (5-Doors) model year from 2013",
			"ix35 FCEV (5-Doors) model year from 2013",
			"ix55 (5-Doors) model year from 2009",

			"Matrix (5-Doors) model year 2002_2010",

			"Santa Fe (5-Doors) model year 2000_2006",
			"Santa Fe (5-Doors) model year 2006_2012",
			"Santa Fe (5-Doors) model year from 2012",
			"Sonata (4-Doors) model year 1998_2001",
			"Sonata (4-Doors) model year 2001_2005",
			"Sonata (4-Doors) model year from 2005",

			"Terracan (5-Doors) model year 2001_2006",
			"Trajet (5-Doors) model year 2000_2008",
			"Tucson (5-Doors) model year 2004_2010",

			"Veloster (4-Doors) model year from 2011",
			"XG (4-Doors) model year 1999_2005" };
	// done hyundayi
	String[] infiniti_english = { "EX Typ J50 model year from 2009",
			"FX Typ S51 model yeara from 2009",
			"G Cabrio Typ V36 model year from 2009",
			"G Coup Typ V36 model year from 2009",
			"G Limousine Typ V36 model year from 2009",
			"M Typ Y51 model year from 2010" };
	// done
	String[] isuzu_english = { "D-MAX Double Cab model year 2007_2010",
			"D-MAX Single Cab model year 2007-2010",
			"D-MAX Single Cab model year from 2012",
			"D-MAX Space Cab model year 2007_2010",
			"N-Serie Double Cab Modell model year 2009_2010",
			"N-Serie Single Cab Modell model year 2009_2010" };
	// done
	String[] jaguar_english = { "S-Type Typ CCX model year 1999_2007",
			"XF Typ CC9 model year from 2007",
			"XJ Typ N 3 model year 2003_2010",
			"XJ Typ NNA model year from 2010",
			"XK Cabriolet Typ QEV model year 1996_2006",
			"XK Cabriolet Typ QQ6 model year from 2006",
			"XK Coup Typ QEV model year 1996_2006",
			"XK Coup Typ QQ6 model year from 2006",
			"X-Type Estate Typ CF1 model year 2004_2009",
			"X-Type Saloon Typ CF1 model year 2001_2009" };
	// done
	String[] jeep_english = { "Cherokee Typ KK model year from 2008",
			"Grand Cherokee Typ WH model year from 2005",
			"Patriot ECO Typ PK LPG model year from 2008",
			"Patriot Typ PK model year from 2008",
			"Wrangler Typ JK (3-Doors) model year from 2006",
			"Wrangler Typ JK (5-Doors) model year from 2006" };
	// done
	String[] karabag_english = { "FiorinoE", "new500E" };
	// done
	String[] lada_english = { "4x4 BA3-21214", "Kalina sedan BA3-1118",
			"Kalina universal BA3-1117", "Kalina xetubek BA3-1119",
			"Priora sedan BA3-2170", "Priora universal BA3-2171",
			"Priora xetubek BA3-2172" };
	// done

	String[] lancia_english = { "Delta Tirbo LPG", "Delta", "Flavia",
			"Musa LPG", "Musa", "Phedra", "Thema", "Voyager",
			"Ypsilon model year from 2011", "Ypsilon model year till 2011",
			"Ypsilon LPG" };
	// done

	String[] landrover_english = { "Discovery model year from 2004",
			"Discovery model year 1998_2004",
			"Freelander model year from 2007",
			"Freelander model year 1998_2007",
			"Range Rover Evoque model year from 2011",
			"Range Rover Sport model year 2005_2013",
			"Range Rover Sport model year from 2013",
			"Range Rover model year from 2013",
			"Range Rover model year 2002_2006",
			"Range Rover model year 2006_2009",
			"Range Rover model year 2009_2012",
			"Range Rover model year 1994_2002"

	};
	// done
	String[] lexus_english = { "CT200h Hybrid (5-Doors) model year from 2010",
			"GS300 460 S19a model year from 2005",
			"GS450h HS19a model year from 2006",
			"IS C XE2a model year from 2009", "IS XE2a model year from 2005",
			"IS300h XE2a model year from 2013",
			"LS460 F4a model year from 2006",
			"LS600h HF4a model year from 2007",
			"NX300h Hybrid AZ1 (5-Doors) model year from 2014",
			"RC F UXC1 model year from 2014",
			"RX450h AL1 Hybrid (5-Doors) model year from 2009" };
	// done
	String[] luis_english = { "4U" };
	// done
	String[] maybach_english = { "Limousine Typ 240 model year from 2002" };
	// done
	String[] mazda_english = { "2 DE 3-Doors model year from 2007",
			"2 DE 5-Doors model year from 2007", "2 DY model year 2003_2007",
			"3 BK 4-Doors model year 2003_2008",
			"3 BK 5-Doors model year 2003_2008",
			"3 BM Limousine model year from 2013",
			"3 BM hatchback model year from 2013", "5 CR model year 2005_2010",
			"5 CW model year from 2010",
			"6 GG 4-Doors model year from 2002_2007",
			"6 GG 5-Doors model year 2002_2007",
			"6 GH 4-Doors model year from 2007",
			"6 GH 5-Doors model year from 2007",
			"6 GH Kombi model year from 2007",
			"6 GJ Kombi model year from 2012",
			"6 GJ Limousine model year from 2013",
			"6 GY Kombi model year 2002_2007", "BT-50 model year from 2006",
			"CX-5 model year from 2012", "CX-7 model year from 2007",
			"CX-9 model year from 2009",
			"MX-5 NC Roadster Coupe model year from 2007",
			"MX-5 NC Roadster Coupe model year from 2012",
			"MX-5 NC with folding top model year from 2005",
			"MX-5 NC with folding top model year from 2012",
			"RX-8 model year 2003_2010" };
	// done
	String[] mercedes_benz_transporter_english = {
			"Citan Kastenwagen model year from 2012",
			"Citan Kombi model year from 2012",
			"Sprinter LGT model year from 2010",
			"Sprinter NGT model year 1996_2006",
			"Sprinter NGT model year from 2008",
			"Sprinter model year 1995_2006", "Sprinter model year from 2006",
			"Vario model year from 1996", "Vito E-Cell model year from 2010",
			"Vito Viano model year from 2003",
			"V-Klasse Vito model year 1996_2003" };
	// done

	String[] mitsubishi_english = { "ASX model year from 2010",
			"Electric Vehicle model year from 2011",
			"L200 Club Cab model year from 2007",
			"L200 Double Cab model year from 2007",
			"L200 Single Cab model year from 2007",
			"Lancer 4-Doors model year from 2008",
			"Lancer 5-Doors model year from 2009",
			"Outlander model year from 2013",
			"Pajero 3-Doors model year from 2007",
			"Pajero 5-Doors model year from 2007",
			"Plug-in Hybrid Outlander model year from 2014",
			"Space Star model year from 2013" };
	// done
	String[] porsche_english = { "911 Cabriolet model year from 2005",
			"911 Cabriolet model year from 2008",
			"911 Cabriolet model year from 2012",
			"911 Coup model year from 2005", "911 Coup model year from 2007",
			"911 Coup model year from 2012", "911 GT2 model year from 2008",
			"911 GT2 RS model year from 2011", "911 GT3 model year from 2007",
			"911 GT3 model year from 2014", "911 GT3 RS model year from 2007",
			"911 Targa model year from 2007", "911 Targa model year from 2014",
			"918 Spyder model year from 2014", "Boxster model year from 2005",
			"Boxster model year from 2012", "Cayenne model year 2003_2005",
			"Cayenne model year 2006_2010", "Cayenne model year from 2011",
			"Cayenne model year from 2015",
			"Cayenne S E-Hybrid model year from 2015",
			"Cayenne S-Hybrid model year from 2011",
			"Cayman model year from 2006", "Cayman model year from 2014",
			"Macan model year from 2014", "Panamera model year from 2010",
			"Panamera S E-Hybrid model year from 2014",
			"Panamera S Hybrid model year from 2011" };
	// done pe

	String[] saab_english = { "9-3 (4-Doors) model year from 2002",
			"9-3 Cabrio model year from 2003",
			"9-3 SportCombi model year from 2005",
			"9-5 (4-Doors) model year from 1997",
			"9-5 (4-Doors) model year from 2005",
			"9-5 (4-Doors) model year from 2010",
			"9-5 Kombi (SportCombi) model year from 1997",
			"9-5 Kombi (SportCombi) model year from 2005" };

	String[] skoda_english = { "Citigo (3-Doors) model year from 2011",
			"Citigo (3-Doors) CNG model year from 2012",
			"Citigo (5-Doors) model year from 2012",
			"Citigo (5-Doors) CNG model year from 2012",
			"Fabia 1 model year 1999_2007", "Fabia 2 model year from 2006",
			"Fabia Combi 1 model year 2000_2007",
			"Fabia Combi 2 model year from 2006",
			"Fabia Sedan 1 model year 2001_2008",
			"Felicia model year 1994_2001",
			"Felicia Combi model year 1995_2001",
			"Octavia 2 model year from 2004",
			"Octavia 2 LPG model year from 2009",
			"Octavia 3 model year from 2012", "Octavia model year from 1996",
			"Octavia Combi 1 model year from 2000",
			"Octavia Combi 2 model year from 2004",
			"Octavia Combi 2 LPG model year from 2009",
			"Octavia Combi 3 model year from 2013",
			"Pick up model year 1995_2001", "Praktik model year from 2006",
			"Rapid model year from 2012",
			"Rapid Spaceback model year from 2013",
			"Roomster model year from 2006", "Superb 1 model year 2001_2008",
			"Superb 2 model year from 2008",
			"Superb Combi 2 model year from 2009", "Yeti model year from 2009" };
	// done
	String[] smart_english = { "forfour model year 2004_2006",
			"fortwo cabrio electric drive model year from 2009",
			"fortwo cabrio electric drive model year from 2012",
			"fortwo cabrio model year 1998_2007",
			"fortwo cabrio model year from 2007",
			"fortwo coup electric drive model year from 2009",
			"fortwo coup electric drive model year from 2012",
			"fortwo coup model year 1998_2007",
			"fortwo coup model year from 2007",
			"roadster coup model year 2003_2005",
			"roadster model year 2003_2005" };

	String[] ssangYong_english = { "Korando model year from 2010",
			"Korando Sports model year from 2012",
			"Rexton W model year from 2013", "Rodius model year from 2013" };
	// done ssanyang

	String[] subaru_english = { "Airbags", "BRZ model year from 2013",
			"Forester model year from 2009", "Forester model year from 2013",
			"Forester SH SHS model year from 2009",
			"Imbreza model year from 2008", "Impreza STI model year from 2008",
			"Impreza STI model year from 2010", "Justy model year from 2008",
			"Legacy (1) model year 2004_2009",
			"Legacy (2) model year 2004_2009",
			"Legacy (4-Doors) model year from 2010",
			"Legacy (4-Doors) model year 2004_2009",
			"Legacy (5-Doors) model year 2004_2009",
			"Legacy (5-Doors) model year from 2010",
			"Legacy Kombi model year from 2010",
			"Legacy Limousine model year from 2010",
			"Outback (1) model year 2004_2009",
			"Outback (2) model year 2004_2009",
			"Outback 2.5l model year from 2010",
			"Outback model year from 2010", "Tribeca model year from 2008",
			"WRX STI model year from 2015", "XV model year from 2012" };
	// done subaru
	String[] suzuki_english = { "Alto (5-Doors) model year from 2009",
			"Grand Vitara (3-Doors) model year from 2005",
			"Grand Vitara (5-Doors) model year from 2005",
			"Jimny (3-Doors) model year from 1998",
			"Kizashi (4-Doors) model year from 2010",
			"Splash (5-Doors) model year from 2007",
			"Swift (3-Doors) model year 2005_2010",
			"Swift (3-Doors) model year from 2010",
			"Swift (5-Doors) model year 2005_2010",
			"Swift (5-Doors) model year from 2010",
			"Swift Sport (3-Doors) model year from 2011",
			"Swift Sport (5-Doors) model year from 2013",
			"SX4 Classic (5-Doors) model year from 2005",
			"SX4 Classic Limousine (4-Doors) model year from 2005",
			"SX4 S-Cross (5-Doors) model year from 2013" };
	// done

	String[] tesla_english = { "Model S model year 2012_2013",
			"Model S model year from 2014", "Roadster model year 2008_2009",
			"Roadster model year from 2010" };
	// done
	String[] think_english = { "City Typ model year from 2008" };
	// done
	String[] volvo_english = { "C30 model year from 2007",
			"C30 Electric model year from 2011", "C70 model year from 2006",
			"C70 Cabriolet model year 1998_2005",
			"C70 Coup model year 1998_2003", "S40 model year 1996_2004",
			"S40 model year from 2004", "S60 model year 2001_2008",
			"S60 model year from 2010", "S60 Bi-Fuel model year 2001_2007",
			"S80 model year 1999_2006", "S80 model year from 2007",
			"S80 Bi-Fuel model year 1999_2006", "V40 model year 1996_2004",
			"V40 model year from 2012", "V50 model year from 2004",
			"V60 model year from 2010",
			"V60 Plug-in Hybrid model year from 2012",
			"V70 model year 2000_2007", "V70 model year from 2007",
			"V70 Bi-Fuel model year 2000_2007", "XC60 model year from 2009",
			"XC70 model year 2000_2007", "XC70 model year from 2008",
			"XC90 model year from 2003" };
	// done

	String opel_english[] = { "Adam 3-Doors (LPG) model year from 2013",
			"Adam 3-Trer model year from 2013",
			"Agila - A model year from 2000",
			"Agila - B (LPG) model year from 2010",
			"Agila - B model year from 2008", "Ampera model year from 2011",
			"Antara model year from 2006", "Antara model year from 2011",
			"Astra - F 3-Doors model year from 1991",
			"Astra - F Cabrio model year from 1993",
			"Astra - F Classic 4-Doors model year from 1992",
			"Astra - F Classic 5-Doors model year from 1991",
			"Astra - F Classic Kombi (Caravan) model year from 1991",
			"Astra - G 3-Doors model year from 1998",
			"Astra - G Cabrio model year from 2001",
			"Astra - G Classic II 4-Doors model year from 1998",
			"Astra - G Classic II 5-Doors model year from 1998",
			"Astra - G Classic II Kombi (Caravan) model year from 1998",
			"Astra - G Coupe model year from 2000",
			"Astra - G LPG model year from 2003",
			"Astra - H 3-Doors model year from 2005",
			"Astra - H 4-Doors model year from 2007",
			"Astra - H 5-Doors model year from 2004",
			"Astra - H (LPG) model year from 2010",
			"Astra - H Cabrio (Twin Top) model year from 2005",
			"Astra - H Kombi (Caravan) model year from 2004",
			"Astra - J 3-Doors model year from 2011",
			"Astra - J 4-Doors model year from 2012",
			"Astra - J 5-Doors model year from 2009",
			"Astra - J (LPG) model year from 2012",
			"Astra - J Kombi Sports Tourer (LPG) model year from 2012",
			"Astra - J Kombi Sports Tourer model year from 2010",
			"Calibra Coup model year from 1989",
			"Cascada Cabrio model year from 2013",
			"Combo - C (CNG) model year from 2005",
			"Combo - C Tour model year from 2001",
			"Combo - D long wheel base model year from 2012",
			"Combo - D short wheel base model year from 2012",
			"Combo - D Tour model year from 2011",
			"Corsa - B 3-Doors model year from 1993",
			"Corsa - B 5-Doors model year from 1993",
			"Corsa - C 3-Doors model year from 2000",
			"Corsa - C 5-Doors model year from 2000",
			"Corsa - D 3-Doors model year from 2006",
			"Corsa - D 5-Doors model year from 2006",
			"Corsa - D (LPG) model year from 2010",
			"Frontera - A 3-Doors model year from 1991",
			"Frontera - A 5-Doors model year from 1991",
			"Frontera - B 3-Doors model year from 1998",
			"Frontera - B 5-Doors model year from 1998",
			"GT model year from 2007", "HydroGen 4 model year from 2008",
			"Insignia 4-Doors (LPG) model year 2012_2013",
			"Insignia 4-Doors model year 2008_2013",
			"Insignia 5-Doors (LPG) model year 2012_2013",
			"Insignia 5-Doors model year 2008_2013",
			"Insignia Kombi (Sports Tourer) model year 2008_2013",
			"Insignia Kombi (Sports Tourer) LPG model year 2012_2013",
			"Meriva - A (LPG) model year from 2010",
			"Meriva - A model year from 2003",
			"Meriva - B (LPG) model year 2012_2014",
			"Meriva - B model year 2010_2014",
			"Mokka SUV (LPG) model year from 2014",
			"Mokka SUV model year from 2012", "Movano from 2003",
			"Movano from 2010", "Movano from 1998",
			"Omega - B 4-Doors model year from 1994",
			"Omega - B Kombi (Caravan) model year from 1994",
			"Signum model year from 2003", "Sintra model year from 1996",
			"Speedster VX220 model year from 2000",
			"Tigra - A Coup model year from 1994",
			"Tigra - B Cabrio (Twin Top) model year from 2004",
			"Vectra - B 4-Doors model year from 1995",
			"Vectra - B 5-Doors model year from 1995",
			"Vectra - B Kombi (Caravan) model year from 1996",
			"Vectra - C 4-Doors model year from 2002",
			"Vectra - C 5-Doors model year from 2002",
			"Vectra - C Kombi (Caravan) model year from 2002",
			"Vivaro - A model year from 2001",
			"Zafira - A model year from 1999",
			"Zafira - A Erdgas model year from 2001",
			"Zafira - B (LPG) model year from 2010",
			"Zafira - B model year from 2005",
			"Zafira - B (LPG) model year from 2005",
			"Zafira - C (LPG) model year from 2012",
			"Zafira - C model year from 2011",
			"Zafira - C (CNG) model year from 2012"

	};
	// done opel
	String[] ford_english = { "B-MAX model year from 2012",
			"B-MAX LPG model year from 2013", "C-MAX model year 2003_2007",
			"C-MAX model year 2007_2010", "C-MAX model year from 2010",
			"C-MAX CNG model year 2003_2007", "C-MAX CNG model year 2007_2010",
			"C-MAX Grand MAV (5+2-Seats) model year from 2010",
			"C-MAX LPG model year 2003_2007", "C-MAX LPG model year 2007_2010",
			"C-MAX LPG Kompakt-MAV (5-Seats) model year from 2012",
			"Eco Sport model year from 2013",
			"Fiesta (3-Doors) model year 2001_2008",
			"Fiesta (3-Doors) model year from 2008",
			"Fiesta (5-Doors) model year 2001_2008",
			"Fiesta (5-Doors) model year from 2008",
			"Fiesta model year 1996_2000", "Fiesta model year 2000_2002",
			"Fiesta LPG (3-Doors) model year from 2008",
			"Fiesta LPG (5-Doors) model year from 2008",
			"Focus (3-Doors) model year 1998_2004",
			"Focus (3-Doors) model year 2004_2007",
			"Focus (3-Doors) model year 2007_2011",
			"Focus (4-Doors) model year 1998_2004",
			"Focus (4-Doors) model year 2004_2007",
			"Focus (4-Doors) model year 2007_2011",
			"Focus (4-Doors) model year from 2011",
			"Focus (5-Doors) model year 1998_2004",
			"Focus (5-Doors) model year 2004_2007",
			"Focus (5-Doors) model year 2007_2011",
			"Focus (5-Doors) model year from 2011",
			"Focus Cabrio model year 2007_2010",
			"Focus CNG (3-Doors) model year 2004_2007",
			"Focus CNG (3-Doors) model year 2007_2011",
			"Focus CNG (5-Doors) model year 2004_2007",
			"Focus CNG (5-Doors) model year 2007_2011", "Focus E lektrik",
			"Focus LPG (3-Doors) model year 2004_2007",
			"Focus LPG (3-Doors) model year 2007_2011",
			"Focus LPG (5-Doors) model year 2004_2007",
			"Focus LPG (5-Doors) model year 2007_2011",
			"Focus LPG (5-Doors) model year from 2012",
			"Focus LPG Turnier model year 2004_2007",
			"Focus LPG Turnier model year 2007_2011",
			"Focus LPG Turnier model year from 2012",
			"Focus Turnier model year 1998_2004",
			"Focus Turnier model year 2004_2007",
			"Focus Turnier model year 2007_2011",
			"Focus Turnier model year from 2011",
			"Fusion model year from 2002", "Galaxy model year 2000_2006",
			"Galaxy model year from 2006", "KA model year 1996_2008",
			"KA model year from 2008", "Kuga model year 2008_2012",
			"Kuga model year from 2012",
			"Mondeo (4-Doors) model year 2000_2007",
			"Mondeo (4-Doors) model year from 2007",
			"Mondeo (5-Doors) model year 2000_2007",
			"Mondeo (5-Doors) model year from 2007",
			"Mondeo LPG (4-Doors) model year from 2007",
			"Mondeo LPG (5-Doors) model year from 2007",
			"Mondeo LPG Turnier model year from 2007",
			"Mondeo Turnier model year 2000_2007",
			"Mondeo Turnier model year from 2007",

			"Ranger model year 2006_2011", "Ranger model year from 2011",

			"S-MAX model year from 2006",
			"Streetka Cabrio model year 2003_2005",
			"Tourneo Connect model year 2002_2013",
			"Tourneo Connect model year from 2013",
			"Tourneo Courier model year from 2014",
			"Tourneo Custom model year from 2012",
			"Transit Tourneo model year from 2007" };
	// done

	String[] Hyundai_english = { "Accent (3-Doors) model year 2000_2006",
			"Accent (3-Doors) model year 2006_2009",
			"Accent (4-Doors) model year 2006_2009",
			"Accent (5-Doors) model year 2000_2006",
			"Atos (5-Doors) model year 1998_2008",
			"Coupe (2-Doors) model year 2002_2006",
			"Coupe (2-Doors) model year 2006_2010",
			"Elantra (4-Doors) model year 2000_2005",
			"Elantra (5-Doors) model year 2000_2005",
			"Genesis Coupe (2-Doors) model year from 2011",
			"Getz (3-Doors) model year 2002_2008",
			"Getz (5-Doors) model year 2002_2008",
			"Grandeur (4-Doors) model year from 2005",
			"H-1 model year 1998_2007", "H-1 model year from 2008",
			"i10 (5-Doors) model year 2008_2010",
			"i10 (5-Doors) model year 2011_2014",
			"i10 (5-Doors) model year from 2014",
			"i20 (3-Doors) model year from 2008",
			"i20 (5-Doors) model year from 2008",
			"i30 (5-Doors) model year 2008_2012",
			"i30 (5-Doors) model year from 2012",
			"i30 Coupe model year from 2013", "i30cw model year 2008_2012",
			"i30cw model year from 2012", "i40 (4-Doors) model year from 2012",
			"i40cw model year from 2011",
			"ix20 (5-Doors) model year from 2010",
			"ix35 (5-Doors) model year 2010_2013",
			"ix35 (5-Doors) model year from 2013",
			"ix35 FCEV (5-Doors) model year from 2013",
			"ix55 (5-Doors) model year from 2009",
			"Matrix (5-Doors) model year 2002_2010",
			"Santa Fe (5-Doors) model year 2000_2006",
			"Santa Fe (5-Doors) model year 2006_2012",
			"Santa Fe (5-Doors) model year from 2012",
			"Sonata (4-Doors) model year 1998_2001",
			"Sonata (4-Doors) model year 2001_2005",
			"Sonata (4-Doors) model year from 2005",
			"Terracan (5-Doors) model year 2001_2006",
			"Trajet (5-Doors) model year 2000_2008",
			"Tucson (5-Doors) model year 2004_2010",
			"Veloster (4-Doors) model year from 2011",
			"XG (4-Doors) model year 1999_2005" };

	String[] nissan_english = { "350Z Cabriolet model year 2003_2009",
			"350Z Coup model year 2002_2009",
			"370Z Cabriolet model year from 2010",
			"370Z Coup model year from 2009",
			"Almera 3-Doors model year 1995_2007",
			"Almera 4-Doors model year 1995_2007",
			"Almera 5-Doors model year 1995_2007",
			"Almera Tino model year 2000_2006", "Cube model year 2010_2011",
			"e-NV200 model year from 2014", "Evalia model year from 2011",
			"GT-R model year from 2008", "Interstar model year 2004_2010",
			"Juke model year from 2010", "Leaf model year from 2013",
			"Leaf model year 2011_2013", "Micra 3-Doors model year 1996_2003",
			"Micra 3-Doors model year 2003_2010",
			"Micra 5-Doors model year 1996_2003",
			"Micra 5-Doors model year 2003_2010",
			"Micra 5-Doors model year from 2010",
			"Micra CC model year 2005_2009", "Murano model year 2005_2008",
			"Murano model year from 2008",
			"Navara Crew Cab model year from 2005",
			"Navara King Cab model year from 2005",
			"Note model year 2006_2013", "Note model year from 2013",
			"NP300 Double Cab model year 2008_2011",
			"NP300 Single Cab model year 2008_2011",
			"NT400 Cabstar model year from 2006", "NV200 model year from 2009",
			"NV400 model year from 2011", "Pathfinder model year from 2005",
			"Patrol 3-Doors model Year 2000_2009",
			"Patrol 5-Doors model year 2000_2009", "Pixo model year 2009_2013",
			"Primastar model year from 2003",
			"Primera 4-Doors model year 1994_2002",
			"Primera 4-Doors model year 2002_2008",
			"Primera 5-Doors model year 1994_2002",
			"Primera 5-Doors model year 2002_2008",
			"Primera Kombi model year 1994_2002",
			"Primera Kombi model year 2002_2008",
			"Pulsar model year from 2014", "Qashqai model year 2007_2014",
			"Qashqai model year from 2014", "Qashqai 2 model year 2008_2014",
			"Tiida model year 2006_2011", "X-Trail model year 2001_2007",
			"X-Trail model year 2007_2014", "X-Trail model year from 2014",
			"NP300 King Cab model year 2008_2011" };
	// done
	String[] vw_english = { "A03 Limousine model year from 1994",
			"Amarok (2-Doors) model year from 2011",
			"Amarok (4-Doors) model year from 2010", "Apollo T5 Transporter",
			"Beetle model year from 1998", "Beetle model year from 2011",
			"Beetle Cabrio model year from 2013",
			"Beetle Cabriu model year from 2003",
			"Bora Limousine model year from 1998",
			"Bora Variant model year from 1998", "Caddy model year from 2010",
			"Caddy Bifuel model year from 2010",
			"Caddy EcoFuel model year from 2010",
			"Caddy Kombi model year from 1996",
			"Caddy Kombi model year from 2004",
			"Caddy Kombi EcoFuel model year from 2006",
			"Caddy Maxi model year from 2007",
			"Caddy Maxi model year from 2010",
			"Caddy Maxi Bifuel model year from 2010",
			"Caddy Maxi EcoFuel model year from 2007",
			"Caddy Maxi EcoFuel model year from 2010",
			"Caddy Pickup model year from 1997",
			"Carrado modell year from 1988", "Crafter model year from 2006",
			"Crafter chassis (2-Doors) model year from 2006",
			"Crafter chassis (4-Doors) model year from 2006",
			"e-Golf 7 model year from 2014", "Eos model year from 2006",
			"e-up model year from 2013", "Fox model year from 2005",
			"Golf 3 (3-Doors) model year from 1991",
			"Golf 3 (5-Doors) model year from 1991",
			"Golf 3 Cabrio model year from 1993",
			"Golf 3 Variant model year from 1993",
			"Golf 4 (3-Doors) model year from 1997",
			"Golf 4 (5-Doors) model year from 1997",
			"Golf 4 Cabrio model year from 1998",
			"Golf 4 Variant model year from 1999",
			"Golf 4 Variant BiFuel model year from 2003",
			"Golf 5 (3-Doors) model year from 2003",
			"Golf 5 (5-Doors) model year from 2003",
			"Golf 5 Plus model year from 2005",
			"Golf 5 Variant model year from 2006",
			"Golf 6 (3-Doors) model year from 2008",
			"Golf 6 (3-Doors) BiFuel model year from 2009",
			"Golf 6 (5-Doors) model year from 2008",
			"Golf 6 (5-Doors) BiFuel model year from 2009",
			"Golf 6 Cabriolet model year from 2011",
			"Golf 6 Plus model year from 2009",
			"Golf 6 Plus BiFuel model year from 2009",
			"Golf 6 Variant model year from 2009",
			"Golf 7 (3-Doors) model year from 2012",
			"Golf 7 (5-Doors) model year from 2012",
			"Golf 7 Sportsvan model year from 2014",
			"Golf 7 TGI (5-Doors) model year from 2013",
			"Golf 7 TGI Variant model year from 2014",
			"Golf 7 Variant model year from 2013",
			"Jetta model year from 2005", "Jetta model year from 2010",
			"Jetta Hybrid model year from 2012", "Lupo model year from 1998",
			"Passat B4 Limousine model year from 1993",
			"Passat B4 Variant model year from 1993",
			"Passat B5 Limousine model year from 1996",
			"Passat B5 Variant model year from 1997",
			"Passat B6 EcoFuel model year from 2009",
			"Passat B6 Limousine model year from 2005",
			"Passat B6 Variant EcoFuel model year from 2009",
			"Passat B6 Variant model year from 2005",
			"Passat B7 Limousine model year from 2010",
			"Passat B7 Limousine EcoFuel model year from 2010",
			"Passat B7 Variant model year from 2010",
			"Passat B7 Variant EcoFuel model year from 2010",
			"Passat CC model year from 2008", "Phaeton model year from 2002",
			"Polo A03 (3-Doors) model year from 1994",
			"Polo A03 (5-Doors) model year from 1994",
			"Polo A03 Variant model year from 1994",
			"Polo A04 (3-Doors) model year from 2001",
			"Polo A04 (5-Doors) model year from 2001",
			"Polo A04 Limousine model year from 2001",
			"Polo A05 (3-Doors) model year from 2009",
			"Polo A05 (5-Doors) model year from 2009",
			"Polo A05 BiFuel (3-Doors) model year from 2009",
			"Polo A05 BiFuel (5-Doors) modl year from 2009",
			"Scirocco model year from 2008", "Sharan model year from 1995",
			"Sharan model year from 2010",
			"Sharan BiFuel model year from 2006",
			"Tiguan model year from 2007", "Touareg model year from 2002",
			"Touareg model year from 2010",
			"Touareg Hybrid model year from 2010",
			"Touran (5-Seats) model year from 2010",
			"Touran (7-Seats) model year from 2010",
			"Touran model year from 2003", "Touran model year from 2006",
			"Touran EcoFuel (5-Seats) model year from 2010",
			"Touran EcoFuel (7-Seats) model year from 2010",
			"Touran EcoFuel model year from 2005",
			"Touran EcoFuel model year from 2006",
			"Transporter California model year from 2003",
			"Transporter LT2 model year from 1996",
			"Transporter LT2 chassis (2-Doors) model year from 1996",
			"Transporter LT2 chassis (4-Doors) model year from 1996",
			"Transporter T4 model year from 1991",
			"Transporter T4 chassis (2-Doors) model year from 1991",
			"Transporter T4 chassis (4-Doors) model year from 1991",
			"Transporter T5 model year from 2003",
			"Transporter T5 model year from 2010",
			"Transporter T5 California model year from 2010",
			"Transporter T5 chassis (2-Doors) model year from 2003",
			"Transporter T5 chassis (2-Doors) model year from 2010",
			"Transporter T5 chassis (4-Doors) model year from 2003",
			"Transporter T5 chassis (4-Doors) model year from 2010",
			"up! (3-Doors) model year from 2011",
			"up! (5-Doors) model year from 2011",
			"up! EcoFuel (3-Doors) model year from 2013",
			"up! EcoFuel (5-Doors) model year from 2013",
			"Vento model year from 1992", "XL1 Europa model year from 2014" };
	// done
	String[] toyota_english = { "Auris (3-Doors) model year from 2009",
			"Auris (5-Doors) model year from 2009",
			"Auris (5-Doors) model year from 2013",
			"Auris HV (5-Doors) model year from 2010",
			"Auris Hybrid (5-Doors) model year from 2013",
			"Auris TS Hybrid model year from 2013",
			"Auris TS model year from 2013",
			"Avensis (4-Doors) model year from 2008",
			"Avensis T25 (5-Doors) model year from 2003",
			"Avensis T25 Wagon (5-Doors) model year from 2008",
			"Avensis T27 Wagon (5-Doors) model year from 2008",
			"Aygo (3-Doors) model year from 2005",
			"Aygo (3-Doors) model year from 2014",
			"Aygo (5-Doors) model year from 2005",
			"Aygo (5-Doors) model year from 2014",
			"Corolla (4-Doors) model year from 2013", "GT86",
			"Hilux 4x2 4x4 model year from 2007",
			"Hilux N2 model year from 2007",
			"IQ (3-Doors) model year from 2009", "Land Cruiser (5-Doors) J12",
			"Land Cruiser (5-Doors) J15TM J15TM TMG",
			"Prius 2 (5-Doors) model year 2004_2009",
			"Prius Hybrid (5-Doors) model year from 2009",
			"Prius Plug-in Hybrid (5-Doors) model year from 2012",
			"Prius  (5-Doors) model year from 2012",
			"RAV4 (5-Doors) model year from 2012", "RAV4 model year from 2005",
			"Urban Cruiser model year from 2009", "Verso model year from 2009",
			"Verso-S model year from 2010",
			"Yaris (3-Doors) model year from 2005",
			"Yaris (3-Doors) model year from 2010",
			"Yaris (3-Doors) model year from 2014",
			"Yaris (5-Doors) model year from 2005",
			"Yaris (5-Doors) model year from 2010",
			"Yaris (5-Doors) model year from 2014",
			"Yaris Hybrid (5-Doors) model year from 2012",
			"Yaris Hybrid (5-Doors) model year from 2014" };
	// done
	String[] seat_english = {

	"Alhambra model year 2001_2010", "Alhambra model year from 2011",
			"Altea model year from 2004", "Altea BiFuel model year from 2009",
			"Altea XL model year from 2007",
			"Altea XL BiFuel model year from 2009",
			"Arosa model year 1997_2000", "Arosa model year 2000_2004",
			"Cordoba model year 2003_2009", "Exeo model year from 2009",
			"Exeo ST model year from 2010",
			"Ibiza (3-Doors) model year 1993_1999",
			"Ibiza (3-Doors) model year 1999_2002",
			"Ibiza (3-Doors) model year 2002_2009",
			"Ibiza (3-Doors) model year from 2009",
			"Ibiza (3-Doors) (1) model year from 2009",
			"Ibiza (5-Doors) model year 1993_1999",
			"Ibiza (5-Doors) model year 1999_2002",
			"Ibiza (5-Doors) model year 2002_2009",
			"Ibiza (5-Doors) model year from 2009",
			"Ibiza (5-Doors)(1) model year from 2009",
			"Ibiza BiFuel (3-Doors) model year from 2012",
			"Ibiza BiFuel (5-Doors) model year from 2012",
			"Ibiza ST model year from 2011",
			"Leon (3-Doors) model year from 2013",
			"Leon (5-Doors) model year from 2013",
			"Leon (5-Doors) TGI model year from 2013",
			"Leon model year 1999_2006", "Leon model year from 2006",
			"Leon BiFuel model year from 2009", "Leon ST model year from 2014",
			"Leon ST TGI model year from 2014",
			"Leon ST Xperience model year from 2014",
			"Mii (3-Doors) model year from 2012",
			"Mii (3-Doors) EcoFuel model year from 2012",
			"Mii (5-Doors) model year from 2012",
			"Mii (5-Doors) EcoFuel model year from 2012",
			"Toledo model year 1999_2004", "Toledo model year 2005_2009",
			"Toledo model year from 2013"

	};
	// done
	String[] dethleffs_english = { "Advantage A5881 model year from 2012",
			"Advantage A6971 model year from 2012",
			"Advantage A7871-2 model year from 2012",
			"Advantage I5701 model year from 2012",
			"Advantage I5701 model year from 2013",
			"Advantage I5871 model year from 2012",
			"Advantage I5871 model year from 2013",
			"Advantage I5871 model year from 2014",
			"Advantage I6501 model year from 2012",
			"Advantage I6511 model year fom 2013",
			"Advantage I6511 model year from 2012",
			"Advantage I6511 model year from 2014",
			"Advantage I6701 model year from 2014",
			"Advantage I6771 model year from 2012",
			"Advantage I6811 model year from 2013",
			"Advantage I6851 model year from 2012",
			"Advantage I6871 model year from 2013",
			"Advantage I7011 model year from 2013",
			"Advantage I7091 model year from 2013",
			"Advantage I7151 model year from 2013",
			"Advantage I7151 EB model year from 2013",
			"Advantage T5701 model year from 2012",
			"Advantage T5701 model year from 2013",
			"Advantage T5871 model year from 2012",
			"Advantage T5871 model year from 2013",
			"Advantage T5871 model year from 2014",
			"Advantage T6501 model year from 2012",
			"Advantage T6511 model year from 2012",
			"Advantage T6511 model year from 2013",
			"Advantage T6511 model year from 2014",
			"Advantage T6701 model year from 2014",
			"Advantage T6771 model year from 2012",
			"Advantage T6811 model year from 2013",
			"Advantage T6851 model year from 2012",
			"Advantage T6871 model year from 2013",
			"Advantage T7011 model year from 2013",
			"Advantage T7091 model year from 2013",
			"Advantage T7151 model year from 2013",
			"Advantage T7151 EB model year from 2013",
			"AlPa model year from 2012", "AlPa model year from 2013",
			"Esprit Comfort A6820-2 model year from 2013",
			"Esprit Comfort A6820-2 model year from 2014",
			"Esprit Comfort A7870-2 model year from 2014",
			"Esprit Comfort I7010-2 model year from 2013",
			"Esprit Comfort I7010-2 model year from ab 2014",
			"Esprit Comfort I7090-2 model year from 2013",
			"Esprit Comfort I7090-2 model year from 2014",
			"Esprit Comfort I7150-2 model year from 2013",
			"Esprit Comfort I7150-2 model year from 2014",
			"Esprit Comfort I7150-2 EB model year from 2013",
			"Esprit Comfort I7150-2 EB model year from 2014",
			"Esprit Comfort T7010-2 model year from 2013",
			"Esprit Comfort T7010-2 model year from 2014",
			"Esprit Comfort T7090-2 model year from 2013",
			"Esprit Comfort T7090-2 model year from 2014",
			"Esprit Comfort T7150-2 model year from 2013",
			"Esprit Comfort T7150-2 model year from 2014",
			"Esprit Comfort T7150-2 EB model year from 2013",
			"Esprit Comfort T7150-2 EB model year from 2014",
			"Esprit ComfortA_7870-2 model year from 2013",
			"Esprit I6700 model year from 2012",
			"Esprit I6810 model year from 2012",
			"Esprit I6810 model year from 2014",
			"Esprit I6870 model year from 2012",
			"Esprit I6870 model year from 2014",
			"Esprit I7010 model year from 2012",
			"Esprit I7010 model year from 2014",
			"Esprit I7090 model year from 2012",
			"Esprit I7090 model year from 2014",
			"Esprit I7150 model year from 2012",
			"Esprit I7150 model year from 2014",
			"Esprit I7150 EB model year from 2014",
			"Esprit T6700 model year from 2012",
			"Esprit T6810 model year from 2012",
			"Esprit T6810 model year from 2014",
			"Esprit T6870 model year from 2012",
			"Esprit T6870 model year from 2014",
			"Esprit T7010 model year from 2012",
			"Esprit T7010 model year from 2014",
			"Esprit T7090 model year from 2012",
			"Esprit T7090 model year from 2014",
			"Esprit T7150 model year from 2012",
			"Esprit T7150 model year from 2014",
			"Esprit T7150 EB model year from 2014",
			"Evan 520 model year from 2014", "Evan 560 model year from 2014",
			"Globe S 570 model year from 2012",
			"Globe S 584 model year from 2012",
			"Globe S 587 model year from 2012",
			"Globe S 661 model year from 2012",
			"Globe S 677 model year from 2012",
			"Globe S 685 model year from 2012",
			"Globe S 690 model year from 2012",
			"Globe S A547 model year from 2012",
			"Globe S A547 model year from 2013",
			"Globe S A598 model year from 2012",
			"Globe S A598 model year from 2013",
			"Globe S A653 model year from 2013",
			"Globe S A677 model year from 2013",
			"Globe S A677 model year from 2012",
			"Globe S A678 model year from 2013",
			"Globe S A678 model year from 2012",
			"Globe S A701 model year from 2013",
			"Globe S T570 model year from 2013",
			"Globe S T584 model year from 2013",
			"Globe S T661 model year from 2013",
			"Globe S T677 model year from 2013",
			"Globe S T685 model year from 2013",
			"Globe S T690 model year from 2013",
			"Globe4 5881 model year from 2012",
			"Globe4 5881 model year from 2013",
			"Globe4 6401 model year from 2012",
			"Globe4 6401 model year from 2013",
			"Globe4 6471 model year from 2012",
			"Globe4 6671 model year from 2012",
			"Globe4 6671 model year from 2013",
			"Globe4 6801 model year from 2013",
			"Globe4 6911 model year from 2013",
			"Globe4 6911 model year from 2012",
			"Globe4 7151 model year from 2012",
			"Globe4 7151 model year from 2013",
			"Globe4 T6801-4 model year from 2014",
			"Globe4 T6911-4 model year from 2014",
			"Globe4 T6911-4 DB model year from 2014",
			"Globe4 T7151-4 model year from 2014",
			"Globe4 T7151-4 EB model year from 2014",
			"Globebus I1 model year from 2012",
			"Globebus I1 model year from 2013",
			"Globebus I1 model year from 2014",
			"Globebus I2 model year from 2012",
			"Globebus I2 model year from 2013",
			"Globebus I2 model year from 2014",
			"Globebus I4 model year from 2012",
			"Globebus I4 model year from 2013",
			"Globebus I4 model year from 2014",
			"Globebus I8 model year from 2013",
			"Globebus I8 model year from 2014",
			"Globebus I11 model year from 2013",
			"Globebus I11 model year from 2012",
			"Globebus I11 model year from 2014",
			"Globebus I15 model year from 2012",
			"Globebus I15 model year from 2013",
			"Globebus I15 model year from 2014",
			"Globebus T1 model year from 2012",
			"Globebus T1 model year from 2013",
			"Globebus T1 model year from 2014",
			"Globebus T2 model year from 2012",
			"Globebus T2 model year from 2013",
			"Globebus T2 model year from 2014",
			"Globebus T4 model year from 2012",
			"Globebus T4 model year from 2013",
			"Globebus T4 model year from 2014",
			"Globebus T8 model year from 2013",
			"Globebus T8 model year from 2014",
			"Globebus T11 model year from 2012",
			"Globebus T11 model year from 2013",
			"Globebus T11 model year from 2014",
			"Globebus T14 model year from 2012",
			"Globebus T15 model year from 2012",
			"Globebus T15 model year from 2013",
			"Globebus T15 model year from 2014",
			"Globetrotter XL I7070 model year from 2012",
			"Globetrotter XL I7110 model year from 2012",
			"Globetrotter XL I7110 model year from 2013",
			"Globetrotter XL I7110 model year from 2014",
			"Globetrotter XL I7850 model year from 2012",
			"Globetrotter XL I7850 model year from 2013",
			"Globetrotter XL I7850 EB model year from 2013",
			"Globetrotter XL I7850 model year from 2014",
			"Globetrotter XL I7850-EB model year from 2012",
			"Globetrotter XL I7870 model year from 2012",
			"Globetrotter XL I7870 model year from 2013",
			"Globetrotter XL I7870 model year from 2014",
			"Globetrotter XXL 9000 model year from 2013",
			"Globetrotter XXL 9800 model year from 2013",
			"Globetrotter XXL A9000 model year from 2012",
			"Globetrotter XXL A9800 model year from 2012",
			"Globetrotter XXL I9000 model year from 2012",
			"Globetrotter XXL I9000 model year from 2013",
			"Globetrotter XXL I9800 model year from 2012",
			"Globetrotter XXL I9800 model year from 2013",
			"Magic Edition Globe4 7151 DB model year from 2013",
			"Magic Edition Globe4 7151 DB model year from 2012",
			"Magic Edition Globe4 7151 DBM model year from 2013",
			"Magic Edition Globe4 7151 DBM model year from 2012",
			"Magic Edition Globe4 7151 EB model year from 2013",
			"Magic Edition Globe4 7151 EB model year from 2012",
			"Magic Edition I001 DB model year from 2012",
			"Magic Edition I001 DB model year from 2013",
			"Magic Edition I001 DBM model year from 2012",
			"Magic Edition I001 DBM model year from 2013",
			"Magic Edition I001 EB model year from 2012",
			"Magic Edition I001 EB model year from 2013",
			"Magic Edition I001 SG model year from 2012",
			"Magic Edition I001 SG model year from 2013",
			"Magic Edition I1 DB model year from 2014",
			"Magic Edition I1 DBM model year from 2014",
			"Magic Edition I1 EB model year from 2014",
			"Magic Edition T 001 EB model year from 2012",
			"Magic Edition T 001 EB model year from 2013",
			"Magic Edition T001 DB model year from 2012",
			"Magic Edition T001 DB model year from 2013",
			"Magic Edition T001 DBM model year from 2012",
			"Magic Edition T001 DBM model year from 2013",
			"Magic Edition T001 SG model year from 2012",
			"Magic Edition T001 SG model year from 2013",
			"Magic Edition T1 DB model year from 2014",
			"Magic Edition T1 DBM model year from 2014",
			"Magic Edition T1 EB model year from 2014",
			"Premium Liner 9910 model year from 2012",
			"Premium Liner 9910 model year from 2013",
			"Premium Liner 9940 model year from 2012",
			"Premium Liner 9940 model year from 2013",
			"Premium Liner 9950 model year from 2012",
			"Premium Liner 9950 model year from 2013",
			"Summer Edition I6701 model year from 2013",
			"Summer Edition T6701 model year from 2013",
			"Trend A5887 model year from 2014",
			"Trend A6977 model year from 2014",
			"Trend T6617 model year from 2014",
			"Trend T6777 model year from 2014",
			"Trend T6857 model year from 2014" };
	// done

	String[] mercedes_benz_english = { "190 Limousine model year 1982_1993",

	"A-Klasse Coup model year from 2004",
			"A-Klasse E-Cell Limousine model year from 2010",
			"A-Klasse Limousine model year 1997_2004",
			"A-Klasse Limousine model year from 2004",
			"A-Klasse Limousine model year from 2012",
			"Amigo Sprinter model year till 2006",
			"Amigo Sprinter model year from 2006",
			"B-Klasse F-Cell Limousine model year from 2010",
			"B-Klasse Limousine model year 2005_2011",
			"B-Klasse NGT Limousine model year from 2005",
			"B-Klasse-NaturalCasDrive model year from 2013",
			"Citan Kombi model year from 2012",
			"C-Klasse Coup model year from 2011",
			"C-Klasse Limousine model year 1993_2001",
			"C-Klasse Limousine model year 2000_2007",
			"C-Klasse Limousine model year from 2007",
			"C-Klasse Sportcoup model year 2000_2008",
			"C-KLasse T-Model model year 1996_2001",
			"C-KLasse T-Model model year 2001_2007",
			"C-KLasse T-Model model year from 2007",
			"CLA-Klasse-Coup model year from 2013",
			"CLC-Klasse Coup model year from 2008",
			"CLK-Klasse Cabriolet model year 1997_2002",
			"CLK-Klasse Cabriolet model year 2003_2009",
			"CLK-Klasse Coup model year 1997_2002",
			"CLK-Klasse Coup model year 2002_2009",
			"CL-Klasse Coup model year 1999_2006",
			"CL-Klasse Coup model year from 2006",
			"CLS-Klasse Coup model year from 2010",
			"CLS-Klasse Coup model year 2004_2009",
			"CLS-Klasse Shooting Break model year from 2012",
			"Corona Sprinter model year till 2006",
			"Corona Sprinter model year from 2006",
			"Cosmo Sprinter model year till 2006",
			"Cosmo Sprinter model year from 2006", "Duo model year till 2006",
			"Duo Independent Sprinter model year from 2006",
			"Duo Sprinter model year from 2006",
			"E-Klasse Cabriolet model year 1987_1997",
			"E-Klasse Cabriolet model year from 2010",
			"E-Klasse Coup model year 1977_1985",
			"E-Klasse Coup model year 1987_1997",
			"E-Klasse Coup model year from 2009",
			"E-Klasse Hybrid Limousine model year from 2012",
			"E-Klasse Limousine model year 1975_1986",
			"E-Klasse Limousine Typ model year 1984_1995",
			"E-Klasse Limousine model year 1995_2002",
			"E-Klasse Limousine model year 2002_2009",
			"E-Klasse Limousine model year from 2009",
			"E-Klasse Limousine model year from 2013",
			"E-Klasse NGT Limousine model year 2004_2009",
			"E-Klasse NGT Limousine model year from 2010",
			"E-KLasse T-Model model year 1977_1986",
			"E-KLasse T-Model model year 1984_1996",
			"E-KLasse T-Model model year 1996_2003",
			"E-KLasse T-Model model year 2003_2009",
			"E-KLasse T-Model model year from 2009",
			"E-Klasse T-Model model year from 2013",
			"E-Klasse model year from 2013",
			"E-Klasse-Cabrio model year from 2013",
			"E-Klasse-Coup model year from 2013",
			"G-Klasse SUV model year from 1990",
			"GLK-Klasse SUV model year from 2008",
			"GL-Klasse SUV model year from 2006",
			"GL-Klasse SUV model year from 2012",
			"Independent Sprinter LPG model year from 2006",
			"Independent Sprinter model year from 2006",
			"Korsika Sprinter model year till 2006",
			"Luxor Sprinter model year from 2006",
			"Maybach Limousine Typ 240 model year from 2002",
			"M-Klasse SUV model year 1997_2005",
			"M-Klasse SUV model year 2005_2011",
			"M-Klasse SUV model year from 2011",
			"M-Klasse Hybrid SUV model year from 2009",
			"R-Klasse Van model year from 2005",
			"Rondo L Sprinter model year from 2006",
			"Rondo Sprinter model year till 2006",
			"Rondo Sprinter model year from 2006",
			"Rondo XL Sprinter model year till 2006",
			"Rondo XL Sprinter model year from 2006",
			"S-Klasse Coup model year 1981_1991",
			"S-Klasse Coup Typ model year 1992_1998",
			"S-Klasse Hybrid Limousine model year from 2009",
			"S-Klasse Hybrid model year from 2013",
			"S-Klasse Limousine model year 1972_1980",
			"S-Klasse Limousine model year 1979_1991",
			"S-Klasse Limousine model year 1991_1998",
			"S-Klasse Limousine model year 1998_2005",
			"S-Klasse Limousine model year from 2005",
			"S-Klasse Pullman Limousine model year 1996_2000",
			"S-Klasse Pullman Limousine model year 2000_2005",
			"S-Klasse Typ 222 model year from 2013",
			"SLC-Klasse Coup model year 1971_1989",
			"SLK-Klasse Roadster model year 1996_2004",
			"SLK-Klasse Roadster model year 2004_2011",
			"SLK-Klasse Roadster model year from 2011",
			"SL-Klasse Roadster model year 1971_1989",
			"SL-Klasse Roadster model year 1989_2001",
			"SL-Klasse Roadster model year from 2001",
			"SLR McLaren model year 2004_2009",
			"SL-Roadster model year from 2012",
			"SLS AMG Coup electric drive model year from 2013",
			"SLS AMG Roadster model year from 2011",
			"SLS AMG model year from 2010",
			"Sprinter JAMES COOK Westfalia model year 1995_2006",
			"Sprinter JAMES COOK Westfalia model year from 2006",
			"Vaneo model year 2002_2005", "Viano FUN model year from 2003",
			"Viano MARCO POLO Westfalia model year from 2003",
			"Viano Vito Van model year from 2003",
			"V-Klasse Vito model year 1996_2003" };
	// done
	String[] kia_english = { "Carens model year from 2013",
			"Carens model year 2006_2013", "Carnival model year 1999_2006",
			"Carnival model year from 2006",
			"Ceed_sw Sporty Wagon model year 2007_2012",
			"Ceed_sw 5-Doors model year from 2012", "Ceed 5-Doors model",
			"Ceed Sportswagon from 2012",
			"Cerato 5-Doors model year 2004_2007",
			"Cerato model year 2004_2008", "Magentis model year from 2006",
			"Opirus model year from 2003", "Optima model year from 2012",
			"Optima Hybrid model year from 2012",
			"Picanto model year 2004_2011",
			"Picanto 3-Doors model year from 2011",
			"Picanto 5-Doors model year from 2011",
			"Picanto LPG 3-Doors model year from 2011",
			"Picanto LPG 5-Doors model year from 2011",
			"Pro Ceed 3-Doors model 2008-2012",
			"Pro Ceed 3-Doors model from 2013", "Rio model year 2005_2011",
			"Rio 3-Doors model year from 2011",
			"Rio 5-Doors model year from 2011", "Sorento model year 2002_2006",
			"Sorento model year 2006_2009", "Sorento model year from 2009",
			"Soul model year from 2009", "Sportage model year 2004_2010",
			"Sportage model year from 2010", "Venga model year from 2010" };
	// done
	String[] renault_english = { "Avantime model year 2001_2003",
			"Captur model year from 2013",
			"Clio 1 (3-Doors) model year 1994_1998",
			"Clio 2 (3-Doors) model year from 1998",
			"Clio 3 (3-Doors) model year from 2005",
			"Clio 3 (5-Doors) model year 2005_2012",
			"Clio 3 Grandtour model year 2008_2013",
			"Clio 4 model year from 2012",
			"Clio 4 Grandtour model year from 2013",
			"Espace 3 model year 1997_2002", "Espace 4 model year from2002",
			"Fluence model year from 2010",
			"Fluence Z.E. model year from 2011",
			"Grand Scnic 3 model year from 2009",
			"Kangoo 1 Kankoo Rapid 1 model year 2003_2007",
			"Kangoo 2 Kangoo Rapid 2 model year from 2007",
			"Kangoo be bop model year from 2009",
			"Kangoo Z.E. model year from 2011", "Koleos model year from 2008",
			"Laguna 1 model year 1994_1997", "Laguna 1 model year 1997_2001",
			"Laguna 1 Grandtour model year 1995_1997",
			"Laguna 1 Grandtour model year 1997_2001",
			"Laguna 2 model year 2000_2007",
			"Laguna 2 Grandtour model year 2000_2007",
			"Laguna 3 model year from 2009",
			"Laguna 3 Coup model year from 2008",
			"Laguna 3 Grandtour model year from 2009",
			"Latitude model year from 2011", "Master 2 model year 2003_2010",
			"Master 3 model year from 2010",
			"Mgane 1 (4-Doors)  Chamade model year 1996_1999",
			"Mgane 1 (4-Doors)  Chamade model year 1999_2003",
			"Mgane 1 (5-Doors) model year 1996_1999",
			"Mgane 1 (5-Doors) model year 1999_2003",
			"Mgan 1 Cabriolet model year 1997_1999",
			"Mgane 1 Cabriolet model year 1999_2003",
			"Mgane 1 Coup Coach model year 1996_1999",
			"Mgane 1 Coup  Coach model year 1999_2002",
			"Mgane 1 Grandtour model year 1999_2003",
			"Mgane 2 (3-Doors) model year 2002_2008",
			"Mgane 2 (4-Doors) model year 2003_2008",
			"Mgane 2 (5-Doors) model year 2002_2008",
			"Mgane 2 Coup Cabrio model year 2003_2009",
			"Mgane 2 Grandtour model year 2003_2009",
			"Mgane 3 model year from 2009",
			"Mgane 3 Coup model year from 2009",
			"Mgane 3 Coup Cabrio model year from 2010",
			"Mgane 3 Grandtour model year from 2009",
			"Mgane Scnic 1 model year 1996_1999", "Modus model year from 2004",
			"R19 (3-Doors) model year 1994_1995",
			"R19 (4-Doors) model year 1994_1995",
			"R19 (5-Doors) model year 1994_1996",
			"R19 Cabriolet model year 1995_1996",
			"Safrane model year 1992_2000", "Scnic 1  model year 1999_2003",
			"Scnic 2 model year 2003_2009", "Scnic 3 model year from 2009",
			"Thalia Symbol model year 1998_2005",
			"Trafic 2 model year from 2001", "Twingo 1 model year 1993_2007",
			"Twingo 2 model year from 2007", "Twizy model year from 2012",
			"Vel Satis model year 2002_2009", "Wind model year from 2010",
			"Zoe Z.E. model year from 2013" };
	// done
	String[] fiat_english = { "500", "500L", "Bravo LPG", "Bravo", "Croma",
			"Doblo Natural Power_from 01-2010",
			"Doblo Natural Power till 12-2009", "Doblo from 01-2010",
			"Freemont", "Grande Punto 3-Doors", "Grande Punto 5-Doors",
			"Grande Punto LPG 3-Doors", "Grande Punto LPG 5-Doors",
			"Grande Punto Natural Power 3-Doors",
			"Grande Punto Natural Power 5-Doors", "Idea LPG", "Idea", "Linea",
			"Multipla Natural Power", "Multipla", "Nuova Panda", "Panda LPG",
			"Panda Natural Power", "Panda", "Punto Classic 3-Doors",
			"Punto Classic 5-Doors", "Punto Classic LPG 3-Doors",
			"Punto Classic LPG 5-Doors", "Punto Classic Natural Power 3-Doors",
			"Punto Classic Natural Power 5-Doors", "Punto Evo 3-Doors",
			"Punto Evo 5-Doors", "Punto Evo LPG 3-Doors",
			"Punto Evo LPG 5-Doors", "Punto Evo Natural Power 3-Doors",
			"Punto Evo Natural Power 5-Doors", "Qubo Natural Power", "Qubo",
			"Sedici", "Stilo 3-Doors", "Stilo 5-Doors", "Stilo Multiwagon",
			"Ulysse" };
	// done
	String[] fiat_professional_english = { "Doblo from 01-2010",
			"Doblo till 12-2009", "Doblo Natural Power from 01-2010",
			"Ducato (1)", "Ducato (2)", "Ducato (3)", "Ducato Natural Power",
			"Fiorino model year from 2008",
			"Fiorino Natural Power model year from 2008", "Scudo", "Strada" };

	String[] peugeot_english = { "107 3- and 5-Doors model year from 2005",
			"206 3- and 5-Doors model year from 1998", "206 3- and 5-Doors",
			"206cc", "206SW model year from 2002",
			"207 3- and 5-Doors model year from 2006",
			"207cc model year from 2007", "207SW model year from 2006",
			"208 3- and 5-Doors model year from 2012",
			"307 3- and 5-Doors model year 2001_2007",
			"307cc model year 2003_2008", "307SW model year 2002_2008",
			"308 3- and 5-Doors model year from 2013",
			"308cc model year from 2008", "308SW model year from 2007",
			"308SW model year from 2013", "407 coup model year from 2005",
			"407 model year from 2004", "407SW model year from 2004",
			"508 4-Doors model year from 2011",
			"508 Hybrid model year from 2012", "508RXH model year from 2012",
			"508SW model year from 2011", "807 model year from 2002",
			"1007 model year 2002_2009", "2008 model year from 2013",
			"3008 model year from 2009", "3008 Hybrid 4 model year from 2012",
			"4007 model year from 2007", "5008 model year from 2009",
			"Bipper model year from 2008", "Boxer model year from 2007",
			"Electro Car", "Expert model year from 2007", "Hybrid4-Cars",
			"iOn model year from 2010", "iOn",
			"Partner  Partner Origin model year from 1996",
			"Partner model year from 2008",
			"Partner Partner Origin model year from 2010",
			"RCZ model year from 2010", "Torro Boxer" };
	// done

	ConnectionDetector cd;
	String[] company, abarth, alfa_romeo, audi, bmw, cadillac, chysler,
			chevrolet, citroen, dacia, daihastu, dodge, e_wolf, honda, hyundai,
			infiniti, isuzu, jaguar, jeep, karabag, lada, lancia, landrover,
			lexus, luis, maybach, mazda, mercedes_benz, mercedes_benz_tranport,
			mitsubishi, porshe, saab, skoda, smart, ssangYong, subaru, suzuki,
			tesla, think, volvo, opel, ford, nissan, vw, toyta, seat, kia,
			dethleffs, renault, fiat, fiat_professional, peugeot;
	ProgressDialog progressDialog;
	Button btn_submit, button2;
	String url = "http://congosolution.org/carsafety_card/Cars/app_search";
	// ProgressBar pb;
	Dialog dialog;
	Button all_data;
	int downloadedSize = 0;
	int totalSize = 0;

	String carname, modelnum;
	CarPdfAdapter carAdap;

	@Override
	protected void onCreate(Bundle tanu) {
		// TODO Auto-generated method stub
		super.onCreate(tanu);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_fragement);
		cd = new ConnectionDetector(getApplicationContext());
		carAdap = new CarPdfAdapter(Search.this);
		carAdap.open();
		all_data = (Button) findViewById(R.id.btn_download);
		
		Button btn_symbols = (Button)findViewById(R.id.btn_symbols);
		btn_symbols.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Search.this,WebviewActivity.class);
				intent.putExtra("url", "http://congosolution.org/carsafety_card/files/symbols_meanings/StandardTranslEnglish.pdf");
				startActivity(intent);
			}
		});
		
		
		progressDialog = new ProgressDialog(Search.this);
		company = getResources().getStringArray(R.array.company_name);
		abarth = abarth_english;
		alfa_romeo = alfa_romeo_english;
		audi = alfa_romeo_english;
		bmw = bmw_english;
		cadillac = cadillac_english;
		citroen =citroen_english;
		chysler = chrysler_english;
		chevrolet = chevrolet_english;
		dacia = dacia_english;
		daihastu = daihatsu_english;
		dodge =dodge_english;
		e_wolf = e_Wolf_english;
		honda = honda_english;
		fiat = fiat_english;
		fiat_professional = fiat_professional_english;
		hyundai = hyundai_english;
		infiniti = infiniti_english;
		isuzu = isuzu_english;
		jaguar = jaguar_english;
		jeep = jeep_english;
		karabag = karabag_english;
		kia = kia_english;
		lada = lada_english;
		lancia = lancia_english;
		landrover =landrover_english;
		lexus = lexus_english;
		maybach = maybach_english;
		luis = luis_english;
		mazda = mazda_english;
		opel = opel_english;
		peugeot = peugeot_english;
		mercedes_benz = mercedes_benz_english;
		mercedes_benz_tranport = mercedes_benz_transporter_english;
		mitsubishi = mitsubishi_english;
		nissan = nissan_english;
		dethleffs = dethleffs_english;
		porshe = porsche_english;
		renault =renault_english;
		saab = saab_english;
		skoda = skoda_english;
		suzuki = suzuki_english;
		smart = smart_english;
		ssangYong = ssangYong_english;
		subaru = subaru_english;
		seat = seat_english;
		tesla = tesla_english;
		think = think_english;
		toyta = toyota_english;
		volvo = volvo_english;
		opel = opel_english;
		ford = ford_english;
		vw = vw_english;
		btn_submit = (Button) findViewById(R.id.btn_search);
		chevrolet_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, chevrolet);
		company_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, company);
		abarth_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, abarth);
		alfa_romeo_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, alfa_romeo);
		audi_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, audi);
		bmw_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, bmw);
		cadillac_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, cadillac);
		dacia_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dacia);
		diahstu_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, daihastu);
		dodge_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dodge);
		dethleffs_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dethleffs);
		e_wolf_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, e_wolf);
		ford_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ford);
		fiat_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, fiat);
		fiat_professional_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, fiat_professional);
		honda_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, honda);
		hyundayi_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, hyundai);
		isuzu_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, isuzu);
		infiniti_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, infiniti);
		jaguar_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, jaguar);
		jeep_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, jeep);
		karabag_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, karabag);
		kia_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, kia);
		lada_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lada);
		lancia_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lancia);
		landrover_adpter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, landrover);
		luis_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, luis);
		opel_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, opel);
		maybach_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, maybach);
		mazda_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mazda);
		mercedes_benz_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mercedes_benz);
		peugeot_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, peugeot);
		Mercedes_Benz_transporter_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mercedes_benz_tranport);
		nissan_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, nissan);
		mitsubishi_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mitsubishi);
		porshe_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, porshe);
		renault_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, renault);
		saab_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, saab);
		skoda_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, skoda);
		smart_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, smart);
		ssangYong_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ssangYong);
		subar_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, subaru);
		suzuki_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, suzuki);
		seat_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, seat);
		think_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, think);
		toyta_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, toyta);
		volovo_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, volvo);
		vw_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, vw);

		autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		autoComplete1 = (TextView) findViewById(R.id.select_model_btn);
		autoComplete.setAdapter(company_adapter);
		autoComplete.setThreshold(1);

		all_data.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (cd.isConnectingToInternet() == false) {
					Toast.makeText(getApplicationContext(),
							"Something went wrong ! No internet connection", 1)
							.show();

				} else {
					// AllCard();
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							Search.this);

					// set title
					alertDialogBuilder.setTitle("Rettungskarten");

					// set dialog message
					alertDialogBuilder
							.setMessage(
									"Data has been schedule to download. Make sure you internet speed should be high to download complete data")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, close
											// current activity
											Intent i = new Intent(Search.this,
													DownloadService.class);
											startService(i);
											dialog.dismiss();
										}
									})
							.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, close
											// current activity
											dialog.dismiss();
										}
									});
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();

				}
			}
		});
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (cd.isConnectingToInternet() == false) {

					if (data.contains("-")) {
						data = data.replace("-", "");
					}
					datalist = carAdap.searchCards(adapt, data);
					datalist1 = carAdap.fetchStudents();
					Log.e("HELLOO", datalist.size() + "" + adapt + "@@" + data);

					if (datalist.size() > 0) {
						String sdPath = datalist.get(0).get("sdcard_path");

						File dbFile = new File(sdPath);

						Intent testIntent = new Intent(Intent.ACTION_VIEW);
						testIntent.setDataAndType(Uri.fromFile(dbFile),
								"application/pdf");
						PackageManager pm = getPackageManager();
						List<ResolveInfo> activities = pm
								.queryIntentActivities(testIntent,
										PackageManager.MATCH_DEFAULT_ONLY);

						if (activities.size() > 0) {
							Intent target = Intent.createChooser(testIntent,
									"Open File");

							try {
								startActivity(target);
							} catch (ActivityNotFoundException e) {

							}
						} else {
							Toast.makeText(getApplicationContext(),
									"package not found", 1).show();
						}

					}

					else {
						Toast.makeText(getApplicationContext(),
								"Please turn on your intenet connection", 1)
								.show();
					}

				} else {
					Log.e("Button clicked", "search card");
					if (data == null || adapt == null) {
						Toast.makeText(getApplicationContext(),
								"Please select manufacturer  and car name", 1)
								.show();
					} else {
						searchCard();
					}

				}
			}
		});
		autoComplete.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				adapt = arg0.getItemAtPosition(arg2).toString();

			}
		});

		autoComplete1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (adapt != null) {
					if (adapt.equals("Abarth")) {
						selectModel(abarth, abarth_english);

					} else if (adapt.equals("Alfa Romeo")) {
						selectModel(alfa_romeo, alfa_romeo_english);

					} else if (adapt.equals("Audi")) {
						selectModel(audi, audi_english);
					} else if (adapt.equals("BMW")) {
						selectModel(bmw, bmw_english);
					} else if (adapt.equals("Chrysler")) {
						selectModel(chysler, chrysler_english);
					} else if (adapt.equals("Chevrolet")) {
						selectModel(chevrolet, chevrolet_english);
					} else if (adapt.equals("Citroen")) {
						selectModel(citroen, citroen_english);
					} else if (adapt.equals("Cadillac")) {
						selectModel(cadillac, cadillac_english);
					} else if (adapt.equals("Dacia")) {
						selectModel(dacia, dacia_english);
					} else if (adapt.equals("Dodge")) {
						selectModel(dodge, dodge_english);
					} else if (adapt.equals("Daihatsu")) {
						selectModel(daihastu, daihatsu_english);
					} else if (adapt.equals("e-Wolf")) {
						selectModel(e_wolf, e_Wolf_english);
					} else if (adapt.equals("Honda")) {
						selectModel(honda, honda_english);
					} else if (adapt.equals("Hyundai")) {
						selectModel(hyundai, hyundai_english);
					} else if (adapt.equals("Jaguar")) {
						selectModel(jaguar, jaguar_english);
					} else if (adapt.equals("Jeep")) {
						selectModel(jeep, jeep_english);
					} else if (adapt.equals("Karabag")) {
						selectModel(karabag, karabag_english);
					} else if (adapt.equals("Lada")) {
						selectModel(lada, lada_english);
					} else if (adapt.equals("Lancia")) {
						selectModel(lancia, lancia_english);
					} else if (adapt.equals("Landrover")) {
						selectModel(landrover, landrover_english);
					} else if (adapt.equals("Lexus")) {
						selectModel(lexus, lexus_english);
					} else if (adapt.equals("Luis")) {
						selectModel(luis, luis_english);
					} else if (adapt.equals("Maybach")) {
						selectModel(maybach, maybach_english);
					} else if (adapt.equals("Mazda")) {
						selectModel(mazda, mazda_english);
					} else if (adapt.equals("Mercedes-Benz-Transporter")) {
						selectModel(mercedes_benz_tranport,
								mercedes_benz_transporter_english);
					} else if (adapt.equals("Infiniti")) {
						selectModel(infiniti, infiniti_english);
					} else if (adapt.equals("Isuzu")) {
						selectModel(isuzu, isuzu_english);
					} else if (adapt.equals("Mitsubishi")) {
						selectModel(mitsubishi, mitsubishi_english);
					}
					// TO Do//
					else if (adapt.equals("Kia")) {
						selectModel(kia, kia_english);
					} else if (adapt.equals("Opel")) {
						selectModel(opel, opel_english);
					} else if (adapt.equals("Renault")) {
						selectModel(renault, renault_english);
					} else if (adapt.equals("Peugeot")) {
						selectModel(peugeot, peugeot_english);
					} else if (adapt.equals("Fiat")) {
						selectModel(fiat, fiat_english);
					} else if (adapt.equals("Fiat Professional")) {
						selectModel(fiat_professional,
								fiat_professional_english);
					} else if (adapt.equals("Mercedes Benz")) {
						selectModel(mercedes_benz, mercedes_benz_english);
					} else if (adapt.equals("Porsche")) {
						selectModel(porshe, porsche_english);
					} else if (adapt.equals("Toyota")) {
						selectModel(toyta, toyota_english);
					} else if (adapt.equals("Seat")) {
						selectModel(seat, seat_english);
					} else if (adapt.equals("Nissan")) {
						selectModel(nissan, nissan_english);
					} else if (adapt.equals("Saab")) {
						selectModel(saab, saab_english);
					} else if (adapt.equals("Skoda")) {
						selectModel(skoda, skoda_english);
					} else if (adapt.equals("Smart")) {
						selectModel(smart, smart_english);
					} else if (adapt.equals("SsangYong")) {
						selectModel(ssangYong, ssangYong_english);
					} else if (adapt.equals("Suzuki")) {
						selectModel(suzuki, suzuki_english);
					} else if (adapt.equals("Tesla")) {
						selectModel(tesla, tesla_english);
					} else if (adapt.equals("Think")) {
						selectModel(think, think_english);
					} else if (adapt.equals("Volvo")) {
						selectModel(volvo, volvo_english);
					} else if (adapt.equals("Subaru")) {
						selectModel(subaru, subaru_english);
					} else if (adapt.equals("Ford")) {
						selectModel(ford, ford_english);
					} else if (adapt.equals("VW")) {
						selectModel(vw, vw_english);
					} else if (adapt.equals("Dethleffs")) {
						selectModel(dethleffs, dethleffs_english);
					}
				}

				else {
					Toast.makeText(Search.this,
							"Please select company name first", 1).show();

				}

			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		carAdap.close();
		super.onDestroy();
	}

	public void selectModel(final String[] modeladapter,
			final String[] eng_adapter) {

		Builder builder = new AlertDialog.Builder(Search.this);
		builder.setTitle("Modell auswählen");
		builder.setSingleChoiceItems(modeladapter, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						data = eng_adapter[which].toString();
						String data1 = modeladapter[which].toString();

						autoComplete1.setText(data1);
						autoComplete1
								.setBackgroundResource(R.drawable.all_data_bg);
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("aufheben",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	public void searchCard() {
		Log.e("Button clicked", "search card");
		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressDialog = new ProgressDialog(Search.this);
				progressDialog.setMessage("Wait...");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();

			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				try {

					if (data.contains("-") || data.contains("+")) {
						data = data.replace("-", "");
					}

					multipartEntity.addPart("data[Car][carname]",
							new StringBody(adapt));
					multipartEntity.addPart("data[Car][model]", new StringBody(
							data));
					// httpPost.setEntity(new
					// UrlEncodedFormEntity(multipartEntity));
					httpPost.setEntity(multipartEntity);
					// HttpResponse response = httpClient.execute(httpPost);
					HttpResponse httpResponse = null;

					try {

						httpResponse = httpClient.execute(httpPost);
					} catch (ClientProtocolException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					try {
						s = EntityUtils.toString(httpResponse.getEntity());

					} catch (ParseException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				try {
					JSONObject jsonObject = new JSONObject(s);
					msg = jsonObject.getString("error");
					if (msg.equals("1")) {
						errorDialog("Card not found");
						progressDialog.dismiss();
					}
					JSONArray jsonList = jsonObject.getJSONArray("list");
					for (int i = 0; i < jsonList.length(); i++) {

						JSONObject jListobj = jsonList.getJSONObject(i);
						JSONObject jListobjdata = jListobj.getJSONObject("Car");
						pdf_url = jListobjdata.getString("url");
						carname = jListobjdata.getString("carname");
						modelnum = jListobjdata.getString("model");

					}
					Intent i = new Intent(Search.this, WebviewActivity.class);
					i.putExtra("url", "" + pdf_url);
					startActivity(i);

					new Thread(new Runnable() {
						public void run() {

							downloadFile(pdf_url, carname, modelnum);
						}
					}).start();

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		};

		asyncTask.execute((Void[]) null);
	}

	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				Search.this);

		// set title
		alertDialogBuilder.setTitle("RETTUNGSKARTEN");
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		// set dialog message
		alertDialogBuilder.setMessage("" + str).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						dialog.dismiss();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	void downloadAllFile(String url1, final String carname,
			final String model_name, int iii) {

		try {
			totalSize = 0;

			final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
			String urlEncoded = Uri.encode(url1, ALLOWED_URI_CHARS);
			URL url = new URL(urlEncoded);

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			// connect
			urlConnection.connect();

			File SDCardRoot = new File(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/Car safety Card");

			if (!SDCardRoot.exists()) {
				if (!SDCardRoot.mkdirs()) {

				}
			}

			File file = new File(SDCardRoot, carname + "_" + model_name
					+ ".pdf");

			FileOutputStream fileOutput = new FileOutputStream(file);

			InputStream inputStream = urlConnection.getInputStream();

			// this is the total size of the file which we are downloading
			totalSize = urlConnection.getContentLength();

			// create a buffer...
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			downloadedSize = 0;

			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;

				// update the progressbar //
				runOnUiThread(new Runnable() {
					public void run() {

						float per = ((float) downloadedSize / totalSize) * 100;
						// Log.e("PDFFF", "Downloaded :::: " + downloadedSize
						// + "KB / " + totalSize + "KB (" + (int) per
						// + "%)");
						// if (downloadedSize > 5000) {
						// progressDialog.dismiss();
						// }
					}
				});
			}

			fileOutput.close();

		} catch (final MalformedURLException e) {
			showError("Error : MalformedURLException " + e);
			e.printStackTrace();
		} catch (final IOException e) {
			showError("Error : IOException " + e);
			e.printStackTrace();
		} catch (final Exception e) {
			showError("Error : Please check your internet connection " + e);
		}
	}

	void downloadFile(String url1, String carname, String model_name) {

		try {
			progressDialog.dismiss();

			final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
			String urlEncoded = Uri.encode(url1, ALLOWED_URI_CHARS);
			URL url = new URL(urlEncoded);

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			// connect
			urlConnection.connect();
			File SDCardRoot = new File(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/Car safety Card");

			if (!SDCardRoot.exists()) {
				if (!SDCardRoot.mkdirs()) {

				}
			}

			File file = new File(SDCardRoot, adapt + "_" + data + ".pdf");
			FileOutputStream fileOutput = new FileOutputStream(file);
			InputStream inputStream = urlConnection.getInputStream();
			// this is the total size of the file which we are downloading
			totalSize = urlConnection.getContentLength();

			// create a buffer...
			byte[] buffer = new byte[1024];
			int bufferLength = 0;

			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;

			}
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/Car safety Card/"
					+ carname
					+ "_"
					+ model_name + ".pdf";
			Log.e("downloadFile clicked", path + "..." + carname + "..."
					+ model_name);
			carAdap.icarname = carname;
			carAdap.imodelname = model_name;
			carAdap.isdcardpath = path;
			carAdap.CreateRecent();

			fileOutput.close();

		} catch (final MalformedURLException e) {
			// showError("Error : MalformedURLException " + e);
			e.printStackTrace();
		} catch (final IOException e) {
			// showError("Error : IOException " + e);
			e.printStackTrace();
		} catch (final Exception e) {
			// showError("Error : Please check your internet connection " + e);
		}
	}

	void showError(final String err) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(Search.this, err, Toast.LENGTH_LONG).show();
			}
		});
	}

	public void AllCard() {

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				progressDialog
						.setMessage("Wait downloading all file to Sd card...");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				url = "http://congosolution.org/carsafety_card/Cars/app_alllist";
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				HttpResponse httpResponse = null;

				try {

					httpResponse = httpClient.execute(httpPost);
				} catch (ClientProtocolException e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				try {
					s = EntityUtils.toString(httpResponse.getEntity());

				} catch (ParseException e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (s.equals(null)) {
					try {
						JSONObject jsonObject = new JSONObject(s);
						msg = jsonObject.getString("error");

						JSONArray jsonList = jsonObject.getJSONArray("list");
						final int size = jsonList.length();
						for (int i = 0; i < jsonList.length(); i++) {
							JSONObject jListobj = jsonList.getJSONObject(i);
							JSONObject jListobjdata = jListobj
									.getJSONObject("Car");

							final int ii = i;

							pdf_url = jListobjdata.getString("url");

							carname = jListobjdata.getString("carname");

							modelnum = jListobjdata.getString("model");
							String path = Environment
									.getExternalStorageDirectory()
									.getAbsolutePath()
									+ "/Car safety Card/"
									+ carname
									+ "_"
									+ modelnum + ".pdf";

							carAdap.icarname = carname;
							carAdap.imodelname = modelnum;
							carAdap.isdcardpath = path;
							carAdap.CreateRecent();
							new Thread(new Runnable() {
								public void run() {

									downloadAllFile(pdf_url, carname, modelnum,
											ii);

								}
							}).start();

						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		};

		asyncTask.execute((Void[]) null);
	}

}
