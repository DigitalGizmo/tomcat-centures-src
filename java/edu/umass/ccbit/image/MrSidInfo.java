/*
 * Title:        MrSidInfo<p>
 * Description:  gets info about mrsid image file<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: MrSidInfo.java,v 1.8 2003/01/16 14:16:15 pbrown Exp $
 *
 * $Log: MrSidInfo.java,v $
 * Revision 1.8  2003/01/16 14:16:15  pbrown
 * merged lizardtech content server changes onto main branch
 *
 * Revision 1.7.2.1  2002/08/01 16:57:33  pbrown
 * made code changes for new lizardtech image server
 *
 * Revision 1.7  2002/05/21 19:46:10  pbrown
 * changed code so that info about sid images (size, number of zoom levels) is
 * stored in hashtable to reduce number of hits on sid server
 *
 * Revision 1.6  2002/04/12 14:07:46  pbrown
 * fixed copyright info
 *
 * Revision 1.5  2001/05/24 14:04:56  pbrown
 * added mrsid root member to httpdbjspbase...no more init servlet
 *
 * Revision 1.4  2001/02/26 16:47:36  pbrown
 * changes for searching, and relocation of www root
 *
 * Revision 1.3  2001/01/11 19:15:32  tarmstro
 * fixed constant error(?)
 *
 * Revision 1.2  2000/11/21 21:21:45  pbrown
 * reimplemented so that image info is read from mrsid server instead of by
 * opening the file..this makes the mrsidarchive_ parameter unnecessary
 *
 * Revision 1.1  2000/05/05 21:21:32  pbrown
 * rebuilt repository
 *
 * Revision 1.2  2000/05/04 14:12:57  pbrown
 * added cvs info and info for running jdb
 *
 *
 */
package edu.umass.ccbit.image;
import java.lang.ArrayIndexOutOfBoundsException;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Hashtable;
import java.lang.Integer;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

public class MrSidInfo
{
	protected class MrSidValues
	{
    public int width_=-1;
    public int height_=-1;
    public int levels_=-1;

		public boolean isValid()
		{
			return width_ != -1 && height_ != -1 && levels_ != -1;
		}
	}

	static protected Hashtable cachedValues_=new Hashtable();
	MrSidValues values;
  boolean isValid_;

  /**
   * get info from MrSid file
   * @param file object created in constructor
   */
  protected MrSidValues getMrSidInfo(String client, String image) 
  {
		if (client==null || image==null)
		{
			// return new, invalid, MrSidValues object
			return new MrSidValues();
		}
		else
		{
  		MrSidValues values=(MrSidValues) cachedValues_.get(hashKey(client, image));
  		if (values != null)
  			return values;
		}
			
    try
    {
      URL url=new URL(MrSidImage.imageRef(client, image, true));
      InputStream istream=url.openStream();
      SAXBuilder builder=new SAXBuilder(false);
      Document doc = builder.build(new BufferedReader(new InputStreamReader(istream)));
			Element root=doc.getRootElement();
			Element imageElement=root.getChild("Response").getChild("Catalog").getChild("Image");
  		MrSidValues values=new MrSidValues();
      values.width_=imageElement.getAttribute("width").getIntValue();
      values.height_=imageElement.getAttribute("height").getIntValue();
      values.levels_=imageElement.getAttribute("numlevels").getIntValue();
			if (values.isValid())
			{
  			cachedValues_.put(hashKey(client, image), values);
			}
			// return values, could be invalid, but is not null
			return values;
    }
    catch (Exception e)
    {
			// return invalid object
			return new MrSidValues();
    }
  }

	/**
	 * return hash key
	 */
	protected static String hashKey(String client, String image)
	{
		return client + "/" + image;		
	}

  /**
   * constructor for MrSidInfo object
   * @param pathname of image
   */
  public MrSidInfo(String client, String image)
  {
		this.values=getMrSidInfo(client, image);
  }
  // returns true iff image info was read successfully
  public boolean isValid(){return values.isValid();}
  // methods to return data read from the MrSid image
  public int width(){return values.width_;}
  public int height(){return values.height_;}
  public int levels(){return values.levels_;}
}
